import { NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession, hashPassword } from '@/lib/auth';

export async function GET(request: Request) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    // Role check: Only Admin, HR, and Payroll can view all employees
    if (session.role === 'EMPLOYEE') {
      return NextResponse.json({ error: 'Forbidden' }, { status: 403 });
    }

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
        govIds: true,
      },
      orderBy: { eid: 'asc' },
    });

    return NextResponse.json({ employees });
  } catch (error) {
    console.error('Fetch employees error:', error);
    return NextResponse.json({ error: 'Failed to retrieve employees' }, { status: 500 });
  }
}

export async function POST(request: Request) {
  try {
    const session = await getSession();
    if (!session || (session.role !== 'ADMIN' && session.role !== 'HR')) {
      return NextResponse.json({ error: 'Unauthorized. HR/Admin access required.' }, { status: 403 });
    }

    const body = await request.json();
    const {
      eid,
      firstName,
      lastName,
      birthday,
      address,
      phoneNumber,
      username,
      password,
      designationId,
      supervisorId,
      status,
      basicSalary,
      riceSubsidy,
      phoneAllowance,
      clothingAllowance,
      sssNum,
      philhealthNum,
      tinNum,
      pagibigNum,
      role,
    } = body;

    if (!eid || !firstName || !lastName || !username || !password) {
      return NextResponse.json(
        { error: 'Missing required employee details' },
        { status: 400 }
      );
    }

    const existing = await prisma.employee.findUnique({
      where: { eid: Number(eid) },
    });

    if (existing) {
      return NextResponse.json(
        { error: `Employee ID ${eid} already exists` },
        { status: 400 }
      );
    }

    const hashedPassword = await hashPassword(password);
    const salary = Number(basicSalary) || 25000;
    const dailyRate = Number((salary / 26).toFixed(2));
    const hourlyRate = Number((dailyRate / 8).toFixed(2));

    const newEmployee = await prisma.employee.create({
      data: {
        eid: Number(eid),
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

    return NextResponse.json({ success: true, employee: newEmployee });
  } catch (error) {
    console.error('Create employee error:', error);
    return NextResponse.json({ error: 'Failed to create employee' }, { status: 500 });
  }
}
