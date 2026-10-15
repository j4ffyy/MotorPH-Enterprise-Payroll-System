/**
 * src/lib/auditLog.ts — Audit Logging
 *
 * IAS2 MS2 Control R1 — Accountability & Non-Repudiation
 *
 * The MS2 rubric Security Advanced criterion requires:
 *   "Methods or processes for tracing an authorized user's actions
 *    (accountability) and proving that actions or events occurred
 *    (non-repudiation)."
 *
 * VibeWrench Manual Checklist: "Add audit logging for all payroll
 * data changes."
 *
 * This module:
 *   1. Records every state-changing action to the AuditLog table
 *   2. Captures: actor EID, action type, target resource, before/after
 *      state, IP address, and timestamp
 *   3. Logs are append-only (no update/delete methods exposed)
 *   4. Uses a non-blocking fire-and-forget pattern so a log failure
 *      never breaks the main API response
 *
 * NOTE: The AuditLog model must be added to schema.prisma (see comment below).
 * Add this to prisma/schema.prisma:
 *
 *   model AuditLog {
 *     id           Int      @id @default(autoincrement())
 *     actorEid     Int      @map("Actor_EID")
 *     actorRole    String   @map("Actor_Role")
 *     action       String   @map("Action")
 *     resourceType String   @map("Resource_Type")
 *     resourceId   String   @map("Resource_ID")
 *     before       String?  @map("Before_State")
 *     after        String?  @map("After_State")
 *     ipAddress    String?  @map("IP_Address")
 *     timestamp    DateTime @default(now()) @map("Timestamp")
 *
 *     @@map("audit_logs")
 *   }
 */

import { prisma } from './prisma';
import type { UserSessionPayload } from './auth';

// ── Audit Action Types ────────────────────────────────────────────────────────

export type AuditAction =
  // Authentication
  | 'LOGIN_SUCCESS'
  | 'LOGIN_FAILED'
  | 'LOGOUT'
  // Employee management
  | 'EMPLOYEE_CREATED'
  | 'EMPLOYEE_UPDATED'
  | 'EMPLOYEE_DEACTIVATED'
  | 'EMPLOYEE_ROLE_CHANGED'
  // Payroll
  | 'PAYROLL_CALCULATED'
  | 'SALARY_UPDATED'
  // Leaves
  | 'LEAVE_APPLIED'
  | 'LEAVE_APPROVED'
  | 'LEAVE_REJECTED'
  // Timesheet
  | 'CLOCK_IN'
  | 'CLOCK_OUT'
  // Security events
  | 'RATE_LIMIT_TRIGGERED'
  | 'CSRF_VALIDATION_FAILED'
  | 'ACCESS_DENIED';

export type ResourceType =
  | 'EMPLOYEE'
  | 'PAYROLL'
  | 'LEAVE'
  | 'TIMESHEET'
  | 'AUTH'
  | 'SYSTEM';

export interface AuditEventParams {
  session:      UserSessionPayload;
  action:       AuditAction;
  resourceType: ResourceType;
  resourceId:   string | number;
  before?:      Record<string, unknown> | null;
  after?:       Record<string, unknown> | null;
  ipAddress?:   string;
}

// ── Core Logger ───────────────────────────────────────────────────────────────

/**
 * Writes an immutable audit event to the database.
 * Fire-and-forget — failures are logged to console but never thrown.
 *
 * @example
 *   await logAudit({
 *     session,
 *     action: 'EMPLOYEE_UPDATED',
 *     resourceType: 'EMPLOYEE',
 *     resourceId: targetEid,
 *     before: { basicSalary: 25000 },
 *     after:  { basicSalary: 30000 },
 *     ipAddress: getClientIp(request),
 *   });
 */
export async function logAudit(params: AuditEventParams): Promise<void> {
  try {
    // Sanitize before/after: strip password hashes before storing
    const sanitize = (obj: Record<string, unknown> | null | undefined) => {
      if (!obj) return null;
      const { password: _pw, ...safe } = obj as { password?: unknown } & Record<string, unknown>;
      return JSON.stringify(safe);
    };

    await (prisma as unknown as {
      auditLog: {
        create: (args: {
          data: {
            actorEid: number;
            actorRole: string;
            action: string;
            resourceType: string;
            resourceId: string;
            before: string | null;
            after: string | null;
            ipAddress: string;
          }
        }) => Promise<unknown>
      }
    }).auditLog.create({
      data: {
        actorEid:     params.session.eid,
        actorRole:    params.session.role,
        action:       params.action,
        resourceType: params.resourceType,
        resourceId:   String(params.resourceId),
        before:       sanitize(params.before ?? null),
        after:        sanitize(params.after ?? null),
        ipAddress:    params.ipAddress ?? 'unknown',
      },
    });
  } catch (err) {
    // Audit log failure must never crash the main request
    console.error('[AuditLog] Failed to write audit event:', {
      action: params.action,
      actorEid: params.session.eid,
      error: err,
    });
  }
}

/**
 * Convenience wrapper — creates a partial logger pre-bound to a session.
 * Use inside route handlers to avoid repeating session on every call.
 *
 * @example
 *   const audit = createAuditLogger(session, getClientIp(request));
 *   await audit('EMPLOYEE_UPDATED', 'EMPLOYEE', targetEid, oldData, newData);
 */
export function createAuditLogger(session: UserSessionPayload, ipAddress: string) {
  return async (
    action: AuditAction,
    resourceType: ResourceType,
    resourceId: string | number,
    before?: Record<string, unknown> | null,
    after?: Record<string, unknown> | null
  ) => {
    await logAudit({ session, action, resourceType, resourceId, ipAddress, before, after });
  };
}
