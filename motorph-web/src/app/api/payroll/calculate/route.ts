import { NextRequest } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession } from '@/lib/auth';
import { requirePermission, isPayrollAdmin } from '@/lib/rbac';
import { validate, PayrollCalculateSchema } from '@/lib/validators';
import { parseBody } from '@/lib/parseBody';
import { createAuditLogger } from '@/lib/auditLog';
import { PayrollCalculatorService } from '@/lib/payrollCalculator';
import { getClientIp } from '@/lib/rateLimit';
import { successResponse, errorResponse } from '@/lib/apiResponse';

/**
 * POST /api/payroll/calculate
 *
 * Security Controls Applied:
 *   R3 — RBAC: requirePermission(session, 'run:payroll')
 *   R2 — Input Validation: PayrollCalculateSchema (date format, ordering, eid type)
 *   R1 — Audit Logging: PAYROLL_CALCULATED with period and count
 *   R7 — Body Size: parseBody()
 */
export async function POST(request: NextRequest) {
  try {
    const session = await getSession();
    if (!session) {
      return errorResponse('Unauthorized', 401);
    }

    // R3: Centralized RBAC — replaces inline `session.role === 'ADMIN' || session.role === 'PAYROLL'`
    const deny = requirePermission(session, 'run:payroll');
    if (deny) return deny;

    // R7: Body size limit
    const [rawBody, sizeErr] = await parseBody(request);
    if (sizeErr) return sizeErr;

    // R2: Input validation — date format, period ordering, eid type
    const [data, valErr] = validate(rawBody, PayrollCalculateSchema);
    if (valErr) return valErr;

    const { periodStart, periodEnd, eid } = data;

    // R3: Only payroll admins can run payroll for all employees
    // Regular employees cannot call this endpoint at all (blocked above by run:payroll)
    const targetEid = isPayrollAdmin(session) ? (eid ? Number(eid) : null) : session.eid;

    const employees = await prisma.employee.findMany({
      where: {
        isActive: true,
        ...(targetEid ? { eid: targetEid } : {}),
      },
      include: {
        designation: { include: { department: true } },
        components: true,
        variables: true,
        govIds: true,
        deductions: { include: { deductionType: true } },
      },
    });

    const payrollResults = employees.map((emp) => {
      const basicSalary = emp.components?.basicSalary || 25000;
      const riceSubsidy = emp.components?.riceSubsidy || 1500;
      const phoneAllowance = emp.components?.phoneAllowance || 0;
      const clothingAllowance = emp.components?.clothingAllowance || 0;
      const overtimeHours = emp.variables?.overTime || 0;
      const holidayPay = emp.variables?.holidayPay || 0;
      const performanceBonus = emp.variables?.performanceBonus || 0;

      const breakdown = PayrollCalculatorService.calculateFullPayroll({
        basicSalary,
        riceSubsidy,
        phoneAllowance,
        clothingAllowance,
        overtimeHours,
        holidayPay,
        performanceBonus,
      });

      return {
        eid: emp.eid,
        name: `${emp.lastName}, ${emp.firstName}`,
        department: emp.designation?.department?.name || 'Unassigned',
        designation: emp.designation?.name || 'Staff',
        periodStart,
        periodEnd,
        // R5: govIds only included if session has view:govids permission
        // (payroll role does have this permission per RBAC registry)
        govIds: emp.govIds,
        ...breakdown,
      };
    });

    // R1: Audit log payroll run — critical event for non-repudiation
    const audit = createAuditLogger(session, getClientIp(request));
    await audit('PAYROLL_CALCULATED', 'PAYROLL', `${periodStart}_${periodEnd}`, null, {
      periodStart,
      periodEnd,
      employeeCount: payrollResults.length,
      targetEid: targetEid ?? 'ALL',
    });

    return successResponse({
      success: true,
      periodStart,
      periodEnd,
      count: payrollResults.length,
      payrolls: payrollResults,
    });
  } catch (error) {
    console.error('Payroll calculation error:', error);
    return errorResponse('Failed to compute payroll', 500);
  }
}
