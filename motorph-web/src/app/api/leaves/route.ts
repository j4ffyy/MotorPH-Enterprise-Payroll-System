import { NextRequest, NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { getSession } from '@/lib/auth';
import { requirePermission, isManager } from '@/lib/rbac';
import { validate, ApplyLeaveSchema, UpdateLeaveStatusSchema } from '@/lib/validators';
import { parseBody } from '@/lib/parseBody';
import { createAuditLogger } from '@/lib/auditLog';
import { getClientIp } from '@/lib/rateLimit';

/**
 * GET /api/leaves
 *
 * Security Controls Applied:
 *   R3 — RBAC: isManager() from centralized rbac.ts
 */
export async function GET(request: NextRequest) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    const { searchParams } = new URL(request.url);
    const all = searchParams.get('all') === 'true';

    // R3: isManager() from centralized RBAC — replaces inline pattern
    const canViewAll = isManager(session) && all;

    const leaves = await prisma.leave.findMany({
      where: canViewAll ? {} : { eid: session.eid },
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

/**
 * POST /api/leaves
 *
 * Security Controls Applied:
 *   R2 — Input Validation: ApplyLeaveSchema (date format, reason length, date ordering)
 *   R1 — Audit Logging: LEAVE_APPLIED with actor
 *   R7 — Body Size: parseBody()
 */
export async function POST(request: NextRequest) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    // R7: Body size limit
    const [rawBody, sizeErr] = await parseBody(request);
    if (sizeErr) return sizeErr;

    // R2: Input validation — enforces date format, min/max reason length,
    //     and date ordering (dateTo >= dateFrom)
    const [data, valErr] = validate(rawBody, ApplyLeaveSchema);
    if (valErr) return valErr;

    const { dateFrom, dateTo, reasonForLeave } = data;

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

    // R1: Audit log leave application
    const audit = createAuditLogger(session, getClientIp(request));
    await audit('LEAVE_APPLIED', 'LEAVE', leaveId, null, { dateFrom, dateTo, reasonForLeave });

    return NextResponse.json({ success: true, leave: newLeave });
  } catch (error) {
    console.error('Apply leave error:', error);
    return NextResponse.json({ error: 'Failed to submit leave application' }, { status: 500 });
  }
}

/**
 * PUT /api/leaves
 *
 * Security Controls Applied:
 *   R3 — RBAC: requirePermission(session, 'approve:leave')
 *   R2 — Input Validation: UpdateLeaveStatusSchema
 *   R1 — Audit Logging: LEAVE_APPROVED / LEAVE_REJECTED
 *   R7 — Body Size: parseBody()
 */
export async function PUT(request: NextRequest) {
  try {
    const session = await getSession();
    if (!session) {
      return NextResponse.json({ error: 'Unauthorized' }, { status: 401 });
    }

    // R3: Centralized RBAC — replaces inline `session.role === 'ADMIN' || session.role === 'HR'`
    const deny = requirePermission(session, 'approve:leave');
    if (deny) return deny;

    // R7: Body size limit
    const [rawBody, sizeErr] = await parseBody(request);
    if (sizeErr) return sizeErr;

    // R2: Input validation — enum constraint on leaveStatus
    const [data, valErr] = validate(rawBody, UpdateLeaveStatusSchema);
    if (valErr) return valErr;

    const { leaveId, leaveStatus } = data;

    // R1: Capture before state
    const before = await prisma.leave.findUnique({ where: { leaveId } });
    if (!before) {
      return NextResponse.json({ error: 'Leave record not found' }, { status: 404 });
    }

    const updated = await prisma.leave.update({
      where: { leaveId },
      data: { leaveStatus },
    });

    // R1: Audit log approval/rejection
    const audit = createAuditLogger(session, getClientIp(request));
    const action = leaveStatus === 'Approved' ? 'LEAVE_APPROVED' : 'LEAVE_REJECTED';
    await audit(action, 'LEAVE', leaveId,
      { leaveStatus: before.leaveStatus },
      { leaveStatus }
    );

    return NextResponse.json({ success: true, leave: updated });
  } catch (error) {
    console.error('Update leave error:', error);
    return NextResponse.json({ error: 'Failed to update leave request' }, { status: 500 });
  }
}
