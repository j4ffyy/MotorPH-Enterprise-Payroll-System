import { NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession } from '@/lib/auth';
import { calculateFullPayroll } from '@/lib/payrollCalculator';

export async function POST(request: Request) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const body = await request.json();
    const { periodStart = '2026-01-01', periodEnd = '2026-01-31', eid } = body;

    // Only ADMIN and PAYROLL roles can process workforce-wide payroll.
    // Regular EMPLOYEE and HR accounts are strictly restricted to their own individual EID.
    const isPayrollAdmin = session.role === 'ADMIN' || session.role === 'PAYROLL';
    const targetEid = isPayrollAdmin ? (eid ? Number(eid) : null) : session.eid;

    const employees = await prisma.employee.findMany({
      where: {
        isActive: true,
        ...(targetEid ? { eid: targetEid } : {}),
      },
      include: {
        designation: {
          include: { department: true },
        },
        components: true,
        variables: true,
        govIds: true,
        deductions: {
          include: { deductionType: true },
        },
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

      const breakdown = calculateFullPayroll({
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
        govIds: emp.govIds,
        ...breakdown,
      };
    });

    return NextResponse.json({
      success: true,
      periodStart,
      periodEnd,
      count: payrollResults.length,
      payrolls: payrollResults,
    });
  } catch (error) {
    console.error('Payroll calculation error:', error);
    return NextResponse.json({ error: 'Failed to compute payroll' }, { status: 500 });
  }
}
