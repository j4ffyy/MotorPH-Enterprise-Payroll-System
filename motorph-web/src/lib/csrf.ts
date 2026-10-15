/**
 * CSRF Protection Utility — MotorPH ESS Portal
 *
 * IAS2 MS2 Control: C6 — Anti-CSRF Token Validation
 *
 * Finding source:
 *   - VibeWrench: "Forms without visible CSRF protection" (INFO)
 *   - Milestone 2 Scanner Results: "Anti-CSRF Token Validation on Form
 *     Submissions" — Vibe App Scanner & VibeWrench flagged payroll forms
 *     lacking explicit Anti-CSRF protections.
 *
 * Implementation: Double-Submit Cookie Pattern
 *   1. Server generates a cryptographically random token.
 *   2. Token is written to a readable (non-HttpOnly) cookie.
 *   3. Client JavaScript reads the cookie and submits it in a request header.
 *   4. Server validates the header value matches the cookie value.
 *
 * Why this pattern:
 *   - Works without server-side session storage.
 *   - Compatible with Next.js App Router (no shared state needed).
 *   - The SameSite=Lax cookie on the JWT already provides structural
 *     CSRF protection; this adds an explicit token layer for payroll
 *     state-changing endpoints (POST /api/payroll, PUT /api/employees, etc.)
 */

import { cookies } from 'next/headers';
import { NextRequest, NextResponse } from 'next/server';
import crypto from 'crypto';

export const CSRF_COOKIE_NAME = 'motorph_csrf';
export const CSRF_HEADER_NAME = 'x-csrf-token';

/**
 * Generates a 32-byte cryptographically random CSRF token (hex).
 */
export function generateCsrfToken(): string {
  return crypto.randomBytes(32).toString('hex');
}

/**
 * Retrieves the current CSRF token from cookies, or generates a new one.
 * Call this in GET handlers / Server Components to hydrate the value
 * into forms as a hidden input.
 */
export async function getCsrfToken(): Promise<string> {
  const cookieStore = await cookies();
  const existing = cookieStore.get(CSRF_COOKIE_NAME)?.value;
  if (existing) return existing;
  return generateCsrfToken();
}

/**
 * Validates a CSRF token on a mutating request (POST / PUT / PATCH / DELETE).
 *
 * Usage inside an API route:
 *   const csrfError = await validateCsrf(request);
 *   if (csrfError) return csrfError;
 *
 * @param request The incoming NextRequest.
 * @returns NextResponse with 403 if CSRF is invalid, or null if valid.
 */
export async function validateCsrf(
  request: NextRequest
): Promise<NextResponse | null> {
  // Skip validation for safe methods
  const safeMethod = ['GET', 'HEAD', 'OPTIONS'].includes(request.method);
  if (safeMethod) return null;

  const cookieStore = await cookies();
  const cookieToken = cookieStore.get(CSRF_COOKIE_NAME)?.value;
  const headerToken = request.headers.get(CSRF_HEADER_NAME);

  if (!cookieToken || !headerToken) {
    return NextResponse.json(
      { error: 'CSRF token missing. Request rejected.' },
      { status: 403 }
    );
  }

  // Constant-time comparison to prevent timing attacks
  const cookieBuf = Buffer.from(cookieToken, 'hex');
  const headerBuf = Buffer.from(headerToken, 'hex');

  if (
    cookieBuf.length !== headerBuf.length ||
    !crypto.timingSafeEqual(cookieBuf, headerBuf)
  ) {
    return NextResponse.json(
      { error: 'CSRF token mismatch. Request rejected.' },
      { status: 403 }
    );
  }

  return null; // Valid — proceed with the request
}

/**
 * Sets the CSRF token cookie on a response.
 * Call in login/session-init handlers so the client receives a fresh token.
 *
 * @param response The NextResponse to attach the cookie to.
 * @param token    The CSRF token string (from generateCsrfToken()).
 */
export function setCsrfCookie(response: NextResponse, token: string): void {
  response.cookies.set({
    name: CSRF_COOKIE_NAME,
    value: token,
    httpOnly: false,   // Must be readable by JS (double-submit pattern)
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'strict',
    path: '/',
    maxAge: 8 * 60 * 60, // Match JWT session length (8 hours)
  });
}
