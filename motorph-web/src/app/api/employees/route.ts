import { NextRequest, NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession, hashPassword } from '@/lib/auth';
import { requirePermission, requireSelfOrElevated, filterEmployeeResponse } from '@/lib/rbac';
import { validate, CreateEmployeeSchema } from '@/lib/validators';
import { parseBody } from '@/lib/parseBody';
import { createAuditLogger } from '@/lib/auditLog';
import { getClientIp } from '@/lib/rateLimit';

/**
 * GET /api/employees
 *
 * Security Controls Applied:
 *   R3 — RBAC: requirePermission(session, 'read:all')
 *   R5 — PII Minimization: filterEmployeeResponse() strips govIds for non-admin roles
 */
export async function GET(request: NextRequest) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    // R3: Centralized RBAC — replaces inline `session.role === 'EMPLOYEE'` check
    const deny = requirePermission(session, 'read:all');
    if (deny) return deny;

    const { searchParams } = new URL(request.url);
    const search = searchParams.get('q') || '';
    const department = searchParams.get('department') || '';

    const employees = await prisma.employee.findMany({
      where: {
        AND: [
          search
            ? {
                OR: [
                  { firstName: { contains: search } },
                  { lastName: { contains: search } },
                  { username: { contains: search } },
                  { eid: Number(search) || -1 },
                ],
              }
            : {},
          department
            ? {
                designation: {
                  department: { name: department },
                },
              }
            : {},
        ],
      },
      include: {
        designation: {
          include: { department: true },
        },
        supervisor: true,
        components: true,
        govIds: true,    // R5: filtered per role by filterEmployeeResponse()
      },
      orderBy: { eid: 'asc' },
    });

    // R5 — PII Minimization: strip govIds + password hash for non-view:govids roles
    const filtered = employees.map((emp) =>
      filterEmployeeResponse(emp as unknown as Record<string, unknown>, session)
    );

    const response = NextResponse.json({ employees: filtered });
    // Cache private — not for CDN/shared caches (contains PII)
    response.headers.set('Cache-Control', 'private, max-age=30, stale-while-revalidate=60');
    return response;

  } catch (error) {
    console.error('Fetch employees error:', error);
    return NextResponse.json({ error: 'Failed to retrieve employees' }, { status: 500 });
  }
}

/**
 * POST /api/employees
 *
 * Security Controls Applied:
 *   R2 — Input Validation: CreateEmployeeSchema via validate()
 *   R3 — RBAC: requirePermission(session, 'write:employee')
 *   R1 — Audit Logging: EMPLOYEE_CREATED with actor EID + IP
 *   R7 — Body Size: parseBody() enforces 64KB limit
 */
export async function POST(request: NextRequest) {
  try {
    const session = await getSession();
    if (!session || (session.role !== 'ADMIN' && session.role !== 'HR')) {
      return NextResponse.json({ error: 'Unauthorized. HR/Admin access required.' }, { status: 403 });
    }

    // R3: Centralized permission check
    const deny = requirePermission(session, 'write:employee');
    if (deny) return deny;

    // R7: Body size limit
    const [rawBody, sizeErr] = await parseBody(request);
    if (sizeErr) return sizeErr;

    // R2: Input validation
    const [data, valErr] = validate(rawBody, CreateEmployeeSchema);
    if (valErr) return valErr;

    const {
      eid, firstName, lastName, birthday, address, phoneNumber,
      username, password, designationId, supervisorId, status,
      basicSalary, riceSubsidy, phoneAllowance, clothingAllowance,
      sssNum, philhealthNum, tinNum, pagibigNum, role,
    } = data;

    let finalEid = eid ? Number(eid) : null;
    if (!finalEid) {
      const maxRecord = await prisma.employee.findFirst({
        orderBy: { eid: 'desc' },
        select: { eid: true },
      });
      finalEid = maxRecord ? maxRecord.eid + 1 : 10001;
    }

    const existing = await prisma.employee.findUnique({ where: { eid: finalEid } });
    if (existing) {
      return NextResponse.json(
        { error: `Employee ID ${finalEid} already exists` },
        { status: 400 }
      );
    }

    const hashedPassword = await hashPassword(password);
    const salary = Number(basicSalary) || 25000;
    const dailyRate = Number((salary / 26).toFixed(2));
    const hourlyRate = Number((dailyRate / 8).toFixed(2));

    const newEmployee = await prisma.employee.create({
      data: {
        eid: finalEid,
        firstName: firstName.trim(),
        lastName: lastName.trim(),
        birthday: birthday || null,
        address: address || null,
        phoneNumber: phoneNumber || null,
        username: username.trim(),
        password: hashedPassword,
        designationId: designationId ? Number(designationId) : null,
        supervisorId: supervisorId ? Number(supervisorId) : null,
        status: status || 'Regular',
        role: role || 'EMPLOYEE',
        components: {
          create: {
            basicSalary: salary,
            riceSubsidy: Number(riceSubsidy) || 1500,
            phoneAllowance: Number(phoneAllowance) || 0,
            clothingAllowance: Number(clothingAllowance) || 0,
            halfMonthRate: salary / 2,
            hourlyRate: hourlyRate,
          },
        },
        govIds: {
          create: {
            sssNum: sssNum || null,
            philhealthNum: philhealthNum || null,
            tinNum: tinNum || null,
            pagibigNum: pagibigNum || null,
          },
        },
      },
    });

    // R1: Audit log — record who created the employee and when
    const audit = createAuditLogger(session, getClientIp(request));
    await audit('EMPLOYEE_CREATED', 'EMPLOYEE', finalEid, null, {
      eid: finalEid,
      firstName,
      lastName,
      username,
      role: role || 'EMPLOYEE',
    });

    // R5: Strip password from response
    const { password: _pw, ...safeEmployee } = newEmployee as typeof newEmployee & { password?: string };
    return NextResponse.json({ success: true, employee: safeEmployee });

  } catch (error) {
    console.error('Create employee error:', error);
    return NextResponse.json({ error: 'Failed to create employee' }, { status: 500 });
  }
}
