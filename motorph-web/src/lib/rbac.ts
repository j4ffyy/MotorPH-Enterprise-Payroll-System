/**
 * src/lib/rbac.ts — Centralized RBAC Middleware
 *
 * IAS2 MS2 Control R3 — Role-Based Access Control
 *
 * Replaces 3 different inline role-check patterns scattered across routes:
 *   - employees: session.role === 'EMPLOYEE'  (different pattern)
 *   - payroll:   session.role === 'ADMIN' || session.role === 'PAYROLL'
 *   - leaves:    session.role === 'ADMIN' || session.role === 'HR'
 *
 * A single missed check = silent privilege escalation.
 * This module is the single source of truth for all authorization decisions.
 *
 * Permission model (principle of least privilege):
 *   ADMIN   — full system access
 *   HR      — read all, manage employees and leaves (not payroll)
 *   PAYROLL — read all, run payroll (not employee management)
 *   EMPLOYEE — read own data only
 */

import { NextResponse } from 'next/server';
import type { UserSessionPayload } from './auth';

// ── Permission Registry ───────────────────────────────────────────────────────

export type Permission =
  | 'read:own'           // own profile, own leaves, own timesheet
  | 'read:all'           // any employee record
  | 'write:employee'     // create / update employee records
  | 'write:salary'       // modify payroll components / salary
  | 'run:payroll'        // execute payroll calculation
  | 'approve:leave'      // approve / reject leave requests
  | 'manage:roles'       // change employee roles (admin only)
  | 'view:govids'        // view SSS, TIN, PhilHealth numbers
  | 'write:own'          // update own phone/address (self-service);

const ROLE_PERMISSIONS: Record<string, Permission[]> = {
  ADMIN:    [
    'read:own', 'read:all',
    'write:employee', 'write:salary',
    'run:payroll', 'approve:leave',
    'manage:roles', 'view:govids', 'write:own',
  ],
  HR:       [
    'read:own', 'read:all',
    'write:employee', 'approve:leave',
    'view:govids', 'write:own',
  ],
  PAYROLL:  [
    'read:own', 'read:all',
    'run:payroll', 'view:govids', 'write:own',
  ],
  EMPLOYEE: ['read:own', 'write:own'],
};

// ── Core Checks ───────────────────────────────────────────────────────────────

/**
 * Returns true if the session's role has the requested permission.
 */
export function hasPermission(
  session: UserSessionPayload,
  permission: Permission
): boolean {
  const perms = ROLE_PERMISSIONS[session.role] ?? [];
  return perms.includes(permission);
}

/**
 * Returns a 403 NextResponse if the session lacks the permission, else null.
 * Use as a guard at the top of every API route handler.
 *
 * @example
 *   const deny = requirePermission(session, 'run:payroll');
 *   if (deny) return deny;
 */
export function requirePermission(
  session: UserSessionPayload,
  permission: Permission
): NextResponse | null {
  if (!hasPermission(session, permission)) {
    return NextResponse.json(
      {
        error: `Forbidden. Your role (${session.role}) does not have '${permission}' permission.`,
      },
      { status: 403 }
    );
  }
  return null;
}

/**
 * Enforces that an EMPLOYEE can only access their own resource.
 * Admins/HR/Payroll can access any resource.
 *
 * @param session   The authenticated session
 * @param targetEid The resource owner's EID
 * @returns 403 NextResponse if access is denied, else null
 */
export function requireSelfOrElevated(
  session: UserSessionPayload,
  targetEid: number
): NextResponse | null {
  if (session.role === 'EMPLOYEE' && session.eid !== targetEid) {
    return NextResponse.json(
      {
        error: 'Forbidden. You may only access your own records.',
      },
      { status: 403 }
    );
  }
  return null;
}

/**
 * Returns true if the session belongs to a manager-level role
 * (ADMIN or HR) — used for manager-only views of all employees/leaves.
 */
export function isManager(session: UserSessionPayload): boolean {
  return session.role === 'ADMIN' || session.role === 'HR';
}

/**
 * Returns true if the session can run payroll.
 */
export function isPayrollAdmin(session: UserSessionPayload): boolean {
  return session.role === 'ADMIN' || session.role === 'PAYROLL';
}

/**
 * Strips fields from an employee object that the requesting role
 * is not permitted to see (PII minimization — Control R5).
 *
 * EMPLOYEE / PAYROLL: govIds fields redacted
 * HR / ADMIN: full access
 */
export function filterEmployeeResponse<T extends Record<string, unknown>>(
  employee: T,
  session: UserSessionPayload
): T {
  if (!hasPermission(session, 'view:govids')) {
    const { govIds: _govIds, password: _pw, ...safe } = employee as {
      govIds?: unknown;
      password?: unknown;
    } & Record<string, unknown>;
    return safe as T;
  }
  // Always strip password hash regardless of role
  const { password: _pw, ...safe } = employee as {
    password?: unknown;
  } & Record<string, unknown>;
  return safe as T;
}
