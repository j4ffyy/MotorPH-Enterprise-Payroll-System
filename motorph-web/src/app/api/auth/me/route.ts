import { NextResponse } from 'next/server';
import { getSession, getCurrentEmployee } from '@/lib/auth';
import { successResponse, errorResponse } from '@/lib/apiResponse';

/**
 * GET /api/auth/me
 *
 * Security Controls Applied:
 *   R5 — PII Minimization:
 *     - password hash NEVER returned to client
 *     - govIds NEVER returned (not needed for session/UI context)
 *     - Only fields needed by the frontend are returned
 */
export async function GET() {
  const session = await getSession();
  if (!session) {
    return successResponse({ authenticated: false }, 200);
  }

  const employee = await getCurrentEmployee();

  // R5: Strip all sensitive fields before returning to client
  // The full employee object from DB contains: password (bcrypt hash),
  // govIds (SSS/TIN/PhilHealth/Pag-IBIG), components (salary details).
  // None of these are needed by the session context — only identity fields are.
  const safeEmployee = employee
    ? (() => {
        const {
          password: _pw,
          govIds: _govIds,
          // Return minimal fields needed for UI session context
          eid,
          firstName,
          lastName,
          username,
          role,
          isActive,
          status,
          designation,
          // eslint-disable-next-line @typescript-eslint/no-unused-vars
          ...rest
        } = employee as typeof employee & {
          password?: string;
          govIds?: unknown;
          [key: string]: unknown;
        };
        // Suppress rest — we explicitly select what to expose
        void rest;
        return { eid, firstName, lastName, username, role, isActive, status, designation };
      })()
    : null;

  return successResponse({
    authenticated: true,
    user: session,
    employee: safeEmployee,
  });
}

