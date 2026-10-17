import { NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession } from '@/lib/auth';
import { getPHDateString, getPHTTimeString } from '@/lib/timeUtils';
import { successResponse, errorResponse } from '@/lib/apiResponse';

export async function GET(request: Request) {
  try {
    const session = await getSession();
    if (!session) {
      return errorResponse('Unauthorized', 401);
    }

    const { searchParams } = new URL(request.url);
    const all = searchParams.get('all') === 'true';

    const isManager = session.role === 'ADMIN' || session.role === 'HR';

    const timesheets = await prisma.timesheet.findMany({
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
      orderBy: { logDate: 'desc' },
      take: 100,
    });

    return successResponse({ timesheets });
  } catch (error) {
    console.error('Fetch timesheet error:', error);
    return errorResponse('Failed to retrieve timesheet records', 500);
  }
}

// Clock In
export async function POST(request: Request) {
  try {
    const session = await getSession();
    if (!session) {
      return errorResponse('Unauthorized', 401);
    }

    const logDate = getPHDateString(); // YYYY-MM-DD in PH Time
    const logTime = getPHTTimeString(); // HH:MM:SS in PH Time

    // Check if already clocked in today
    const existing = await prisma.timesheet.findFirst({
      where: { eid: session.eid, logDate },
    });

    if (existing) {
      return errorResponse(
        'Already clocked in today. Use Clock Out to record your departure.',
        409
      );
    }

    // Determine status: shift starts 08:30
    const [hours, minutes] = logTime.split(':').map(Number);
    const totalMinutes = hours * 60 + minutes;
    const standardStartMinutes = 8 * 60 + 30;
    const attStatus = totalMinutes > standardStartMinutes ? 'Late' : 'Present';

    const newLog = await prisma.timesheet.create({
      data: { eid: session.eid, logDate, logTime, attStatus },
    });

    // Format time for display as HH:MM AM/PM
    const [h, m] = logTime.split(':').map(Number);
    const period = h >= 12 ? 'PM' : 'AM';
    const displayH = h % 12 || 12;
    const displayTime = `${displayH}:${String(m).padStart(2, '0')} ${period}`;

    return successResponse({
      success: true,
      message: `Clocked in at ${displayTime} — Status: ${attStatus}`,
      log: newLog,
    });
  } catch (error) {
    console.error('Clock in error:', error);
    return errorResponse('Failed to record clock-in', 500);
  }
}

// Clock Out
export async function PUT(request: Request) {
  try {
    const session = await getSession();
    if (!session) {
      return errorResponse('Unauthorized', 401);
    }

    const logDate = getPHDateString();
    const logOutTime = getPHTTimeString(); // HH:MM:SS in PH Time

    // Find today's clock-in record (or most recent unclosed shift)
    const existing = await prisma.timesheet.findFirst({
      where: {
        eid: session.eid,
        OR: [{ logDate }, { logOutTime: null }],
      },
      orderBy: { attendanceId: 'desc' },
    });

    if (!existing) {
      return errorResponse('No clock-in record found for today. Please clock in first.', 404);
    }

    if (existing.logOutTime) {
      return errorResponse('Already clocked out today.', 409);
    }

    const updated = await prisma.timesheet.update({
      where: { attendanceId: existing.attendanceId },
      data: { logOutTime },
    });

    // Calculate hours worked
    const [inH, inM, inS] = existing.logTime.split(':').map(Number);
    const [outH, outM, outS] = logOutTime.split(':').map(Number);
    const inTotal = inH * 3600 + inM * 60 + inS;
    const outTotal = outH * 3600 + outM * 60 + outS;
    const diffSeconds = outTotal - inTotal;
    const hoursWorked = Math.floor(diffSeconds / 3600);
    const minsWorked = Math.floor((diffSeconds % 3600) / 60);

    // Format clock-out time
    const period = outH >= 12 ? 'PM' : 'AM';
    const displayH = outH % 12 || 12;
    const displayTime = `${displayH}:${String(outM).padStart(2, '0')} ${period}`;

    return successResponse({
      success: true,
      message: `Clocked out at ${displayTime} — ${hoursWorked}h ${minsWorked}m worked today`,
      log: updated,
    });
  } catch (error) {
    console.error('Clock out error:', error);
    return errorResponse('Failed to record clock-out', 500);
  }
}
