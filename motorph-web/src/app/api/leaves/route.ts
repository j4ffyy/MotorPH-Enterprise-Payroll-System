import { NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession } from '@/lib/auth';

export async function GET(request: Request) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const { searchParams } = new URL(request.url);
    const all = searchParams.get('all') === 'true';

    const isManager = session.role === 'ADMIN' || session.role === 'HR';

    const leaves = await prisma.leave.findMany({
      where: isManager && all ? {} : { eid: session.eid },
      include: {
        employee: {
          select: {
            firstName: true,
            lastName: true,
            designation: { select: { name: true } },
          },
        },
      },
      orderBy: { dateFiled: 'desc' },
    });

    return NextResponse.json({ leaves });
  } catch (error) {
    console.error('Fetch leaves error:', error);
    return NextResponse.json({ error: 'Failed to retrieve leave records' }, { status: 500 });
  }
}

export async function POST(request: Request) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const body = await request.json();
    const { dateFrom, dateTo, reasonForLeave } = body;

    if (!dateFrom || !dateTo || !reasonForLeave) {
      return NextResponse.json(
        { error: 'Date from, date to, and reason are required' },
        { status: 400 }
      );
    }

    const leaveCount = await prisma.leave.count();
    const leaveId = `L${1000 + leaveCount + 1}`;
    const today = new Date().toISOString().split('T')[0];

    const newLeave = await prisma.leave.create({
      data: {
        leaveId,
        eid: session.eid,
        dateFiled: today,
        dateFrom,
        dateTo,
        reasonForLeave,
        leaveStatus: 'Pending',
      },
    });

    return NextResponse.json({ success: true, leave: newLeave });
  } catch (error) {
    console.error('Apply leave error:', error);
    return NextResponse.json({ error: 'Failed to submit leave application' }, { status: 500 });
  }
}

export async function PUT(request: Request) {
  try {
    const session = await getSession();
    if (!session || (session.role !== 'ADMIN' && session.role !== 'HR')) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 403 });
    }

    const body = await request.json();
    const { leaveId, leaveStatus } = body;

    if (!leaveId || !['Approved', 'Rejected'].includes(leaveStatus)) {
      return NextResponse.json({ error: 'Invalid leave status update' }, { status: 400 });
    }

    const updated = await prisma.leave.update({
      where: { leaveId },
      data: { leaveStatus },
    });

    return NextResponse.json({ success: true, leave: updated });
  } catch (error) {
    console.error('Update leave error:', error);
    return NextResponse.json({ error: 'Failed to update leave request' }, { status: 500 });
  }
}
