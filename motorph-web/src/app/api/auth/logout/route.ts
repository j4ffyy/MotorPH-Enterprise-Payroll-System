import { getSession, AUTH_COOKIE_NAME } from '@/lib/auth';
import { createAuditLogger } from '@/lib/auditLog';
import { successResponse } from '@/lib/apiResponse';

/**
 * POST /api/auth/logout
 *
 * Security Controls Applied:
 *   R5 — PII Minimization: clears CSRF cookie in addition to JWT cookie
 *   R1 — Audit Logging: LOGOUT event recorded for accountability
 *
 * Note on R6 (JWT Blocklist):
 *   Full JWT blocklist requires the AuditLog/TokenBlocklist table in Prisma.
 *   The current implementation clears both cookies (JWT + CSRF).
 *   The SameSite=Lax + HttpOnly cookie means without the cookie the token
 *   cannot be sent — this is the pragmatic implementation for the academic
 *   deployment scope. Production should add jti to a Redis blocklist.
 */
export async function POST(request: Request) {
  const session = await getSession();

  const response = successResponse({ success: true });

  // Clear JWT session cookie
  response.cookies.delete(AUTH_COOKIE_NAME);

  // R6: Also clear CSRF token cookie (complete session teardown)
  response.cookies.delete('motorph_csrf');

  // R1: Audit log the logout event if session was valid
  if (session) {
    try {
      const audit = createAuditLogger(session, '');
      await audit('LOGOUT', 'AUTH', session.eid, null, { username: session.username });
    } catch {
      // Non-blocking — logout must succeed even if audit fails
    }
  }

  return response;
}
