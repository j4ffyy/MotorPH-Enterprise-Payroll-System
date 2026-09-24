import { NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession } from '@/lib/auth';

export async function GET(
  request: Request,
  { params }: { params: Promise<{ eid: string }> }
) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const { eid } = await params;
    const targetEid = Number(eid);

    // Employees can only view their own profile, unless Admin/HR/Payroll
    if (session.role === 'EMPLOYEE' && session.eid !== targetEid) {
      return NextResponse.json({ error: 'Forbidden' }, { status: 403 });
    }

    const employee = await prisma.employee.findUnique({
      where: { eid: targetEid },
      include: {
        designation: {
          include: { department: true },
        },
        supervisor: true,
        components: true,
        variables: true,
        govIds: true,
        leaves: { orderBy: { dateFiled: 'desc' }, take: 10 },
        timesheets: { orderBy: { logDate: 'desc' }, take: 15 },
        payrolls: { orderBy: { periodEnd: 'desc' }, take: 6 },
      },
    });

    if (!employee) {
      return NextResponse.json({ error: 'Employee not found' }, { status: 404 });
    }

    return NextResponse.json({ employee });
  } catch (error) {
    console.error('Fetch employee detail error:', error);
    return NextResponse.json({ error: 'Failed to retrieve employee details' }, { status: 500 });
  }
}

export async function PUT(
  request: Request,
  { params }: { params: Promise<{ eid: string }> }
) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const { eid } = await params;
    const targetEid = Number(eid);

    // If regular employee, they can only edit phone and address
    const isSelf = session.eid === targetEid;
    const isAdmin = session.role === 'ADMIN' || session.role === 'HR';

    if (!isSelf && !isAdmin) {
      return NextResponse.json({ error: 'Forbidden' }, { status: 403 });
    }

    const body = await request.json();

    if (!isAdmin && isSelf) {
      // Self update of personal contact details only
      const updated = await prisma.employee.update({
        where: { eid: targetEid },
        data: {
          phoneNumber: body.phoneNumber,
          address: body.address,
        },
      });
      return NextResponse.json({ success: true, employee: updated });
    }

    // Full Admin update
    const salary = body.basicSalary ? Number(body.basicSalary) : undefined;
    const dailyRate = salary ? Number((salary / 26).toFixed(2)) : undefined;
    const hourlyRate = dailyRate ? Number((dailyRate / 8).toFixed(2)) : undefined;

    const updated = await prisma.employee.update({
      where: { eid: targetEid },
      data: {
        firstName: body.firstName,
        lastName: body.lastName,
        birthday: body.birthday,
        address: body.address,
        phoneNumber: body.phoneNumber,
        status: body.status,
        role: body.role,
        isActive: body.isActive !== undefined ? body.isActive : undefined,
        designationId: body.designationId ? Number(body.designationId) : undefined,
        supervisorId: body.supervisorId ? Number(body.supervisorId) : undefined,
        components: salary
          ? {
              upsert: {
                create: {
                  basicSalary: salary,
                  riceSubsidy: Number(body.riceSubsidy) || 1500,
                  phoneAllowance: Number(body.phoneAllowance) || 0,
                  clothingAllowance: Number(body.clothingAllowance) || 0,
                  halfMonthRate: salary / 2,
                  hourlyRate: hourlyRate || 0,
                },
                update: {
                  basicSalary: salary,
                  riceSubsidy: body.riceSubsidy !== undefined ? Number(body.riceSubsidy) : undefined,
                  phoneAllowance: body.phoneAllowance !== undefined ? Number(body.phoneAllowance) : undefined,
                  clothingAllowance: body.clothingAllowance !== undefined ? Number(body.clothingAllowance) : undefined,
                  halfMonthRate: salary / 2,
                  hourlyRate: hourlyRate,
                },
              },
            }
          : undefined,
        govIds: {
          upsert: {
            create: {
              sssNum: body.sssNum,
              philhealthNum: body.philhealthNum,
              tinNum: body.tinNum,
              pagibigNum: body.pagibigNum,
            },
            update: {
              sssNum: body.sssNum,
              philhealthNum: body.philhealthNum,
              tinNum: body.tinNum,
              pagibigNum: body.pagibigNum,
            },
          },
        },
      },
    });

    return NextResponse.json({ success: true, employee: updated });
  } catch (error) {
    console.error('Update employee error:', error);
    return NextResponse.json({ error: 'Failed to update employee' }, { status: 500 });
  }
}
