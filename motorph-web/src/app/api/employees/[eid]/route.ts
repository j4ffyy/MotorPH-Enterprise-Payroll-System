import { NextRequest, NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession } from '@/lib/auth';
import { requireSelfOrElevated, filterEmployeeResponse, hasPermission } from '@/lib/rbac';
import { validate, UpdateEmployeeAdminSchema, UpdateEmployeeSelfSchema } from '@/lib/validators';
import { parseBody } from '@/lib/parseBody';
import { createAuditLogger } from '@/lib/auditLog';
import { getClientIp } from '@/lib/rateLimit';

/**
 * GET /api/employees/[eid]
 *
 * Security Controls Applied:
 *   R4 — Horizontal Privilege Escalation: requireSelfOrElevated()
 *   R3 — RBAC: role-scoped response via filterEmployeeResponse()
 *   R5 — PII Minimization: govIds and password stripped per role
 */
export async function GET(
  request: NextRequest,
  { params }: { params: Promise<{ eid: string }> }
) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const { eid } = await params;
    const targetEid = Number(eid);

    if (isNaN(targetEid)) {
      return NextResponse.json({ error: 'Invalid employee ID' }, { status: 400 });
    }

    // R4: CRITICAL — Horizontal Privilege Escalation Prevention
    // EMPLOYEE role cannot read another employee's profile (salary, SSS, TIN, etc.)
    // Replaces previous inline: if (session.role === 'EMPLOYEE' && session.eid !== targetEid)
    const deny = requireSelfOrElevated(session, targetEid);
    if (deny) return deny;

    const employee = await prisma.employee.findUnique({
      where: { eid: targetEid },
      include: {
        designation: { include: { department: true } },
        supervisor: true,
        components: true,
        variables: true,
        govIds: true,
        leaves:     { orderBy: { dateFiled: 'desc' }, take: 10 },
        timesheets: { orderBy: { logDate: 'desc' },   take: 15 },
        payrolls:   { orderBy: { periodEnd: 'desc' }, take: 6 },
      },
    });

    if (!employee) {
      return NextResponse.json({ error: 'Employee not found' }, { status: 404 });
    }

    // R5: Strip govIds/password based on requesting role
    const safeEmployee = filterEmployeeResponse(
      employee as unknown as Record<string, unknown>,
      session
    );

    return NextResponse.json({ employee: safeEmployee });

  } catch (error) {
    console.error('Fetch employee detail error:', error);
    return NextResponse.json({ error: 'Failed to retrieve employee details' }, { status: 500 });
  }
}

/**
 * PUT /api/employees/[eid]
 *
 * Security Controls Applied:
 *   R4 — Horizontal Privilege Escalation: requireSelfOrElevated()
 *   R2 — Input Validation: schema-split for admin vs self update
 *   R3 — RBAC: hasPermission() guards salary and role changes
 *   R1 — Audit Logging: captures before/after state for every update
 *   R7 — Body Size: parseBody() enforces 64KB limit
 */
export async function PUT(
  request: NextRequest,
  { params }: { params: Promise<{ eid: string }> }
) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const { eid } = await params;
    const targetEid = Number(eid);

    if (isNaN(targetEid)) {
      return NextResponse.json({ error: 'Invalid employee ID' }, { status: 400 });
    }

    const isSelf  = session.eid === targetEid;
    const isAdmin = hasPermission(session, 'write:employee');

    // R4: Only self OR elevated role can update
    if (!isSelf && !isAdmin) {
      return NextResponse.json({ error: 'Forbidden' }, { status: 403 });
    }

    // R7: Body size limit
    const [rawBody, sizeErr] = await parseBody(request);
    if (sizeErr) return sizeErr;

    const audit = createAuditLogger(session, getClientIp(request));

    if (!isAdmin && isSelf) {
      // R2: Self-update — restricted schema (phone + address only)
      const [data, valErr] = validate(rawBody, UpdateEmployeeSelfSchema);
      if (valErr) return valErr;

      // R1: Capture before state
      const before = await prisma.employee.findUnique({
        where: { eid: targetEid },
        select: { phoneNumber: true, address: true },
      });

      const updated = await prisma.employee.update({
        where: { eid: targetEid },
        data: {
          phoneNumber: data.phoneNumber,
          address: data.address,
        },
      });

      // R1: Audit log self-update
      await audit('EMPLOYEE_UPDATED', 'EMPLOYEE', targetEid,
        before as Record<string, unknown>,
        { phoneNumber: data.phoneNumber, address: data.address }
      );

      const { password: _pw, ...safe } = updated as typeof updated & { password?: string };
      return NextResponse.json({ success: true, employee: safe });
    }

    // R3: Only ADMIN/HR can do full updates
    // R2: Admin full update schema
    const [data, valErr] = validate(rawBody, UpdateEmployeeAdminSchema);
    if (valErr) return valErr;

    // R3: Salary changes require write:salary permission
    if (data.basicSalary !== undefined && !hasPermission(session, 'write:salary')) {
      return NextResponse.json(
        { error: 'Forbidden. Your role cannot modify salary components.' },
        { status: 403 }
      );
    }

    // R3: Role changes require manage:roles permission
    if (data.role !== undefined && !hasPermission(session, 'manage:roles')) {
      return NextResponse.json(
        { error: 'Forbidden. Your role cannot change employee roles.' },
        { status: 403 }
      );
    }

    // R1: Capture before state for audit
    const before = await prisma.employee.findUnique({
      where: { eid: targetEid },
      include: { components: true, govIds: true },
    });

    const salary    = data.basicSalary ? Number(data.basicSalary) : undefined;
    const dailyRate = salary ? Number((salary / 26).toFixed(2)) : undefined;
    const hourlyRate = dailyRate ? Number((dailyRate / 8).toFixed(2)) : undefined;

    const updated = await prisma.employee.update({
      where: { eid: targetEid },
      data: {
        firstName:    data.firstName,
        lastName:     data.lastName,
        birthday:     data.birthday,
        address:      data.address,
        phoneNumber:  data.phoneNumber,
        status:       data.status,
        role:         data.role,
        isActive:     data.isActive !== undefined ? data.isActive : undefined,
        designationId: data.designationId ? Number(data.designationId) : undefined,
        supervisorId:  data.supervisorId  ? Number(data.supervisorId) : undefined,
        components: salary ? {
          upsert: {
            create: {
              basicSalary: salary,
              riceSubsidy: Number(data.riceSubsidy) || 1500,
              phoneAllowance: Number(data.phoneAllowance) || 0,
              clothingAllowance: Number(data.clothingAllowance) || 0,
              halfMonthRate: salary / 2,
              hourlyRate: hourlyRate || 0,
            },
            update: {
              basicSalary: salary,
              riceSubsidy: data.riceSubsidy !== undefined ? Number(data.riceSubsidy) : undefined,
              phoneAllowance: data.phoneAllowance !== undefined ? Number(data.phoneAllowance) : undefined,
              clothingAllowance: data.clothingAllowance !== undefined ? Number(data.clothingAllowance) : undefined,
              halfMonthRate: salary / 2,
              hourlyRate: hourlyRate,
            },
          },
        } : undefined,
        govIds: {
          upsert: {
            create: {
              sssNum: data.sssNum,
              philhealthNum: data.philhealthNum,
              tinNum: data.tinNum,
              pagibigNum: data.pagibigNum,
            },
            update: {
              sssNum: data.sssNum,
              philhealthNum: data.philhealthNum,
              tinNum: data.tinNum,
              pagibigNum: data.pagibigNum,
            },
          },
        },
      },
    });

    // R1: Audit log admin update with before/after
    const auditAction = data.role !== before?.role ? 'EMPLOYEE_ROLE_CHANGED'
      : data.isActive === false              ? 'EMPLOYEE_DEACTIVATED'
      : data.basicSalary !== undefined       ? 'SALARY_UPDATED'
      : 'EMPLOYEE_UPDATED';

    await audit(auditAction, 'EMPLOYEE', targetEid,
      before   as unknown as Record<string, unknown>,
      { ...data }
    );

    const { password: _pw, ...safe } = updated as typeof updated & { password?: string };
    return NextResponse.json({ success: true, employee: safe });

  } catch (error) {
    console.error('Update employee error:', error);
    return NextResponse.json({ error: 'Failed to update employee' }, { status: 500 });
  }
}
