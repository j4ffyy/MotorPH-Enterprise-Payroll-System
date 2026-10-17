/**
 * src/lib/parseBody.ts — Request Body Size Limiting
 *
 * IAS2 MS2 Control R7 — DoS Prevention via Body Size Enforcement
 *
 * Gap: All POST routes called request.json() with no size limit.
 * Next.js has a 4MB default but this was not explicitly enforced at
 * the route level. A 4MB reasonForLeave string would be stored in
 * the database and returned in every leave list response.
 *
 * This module enforces a 64KB default limit (configurable per route)
 * and returns HTTP 413 Payload Too Large on violation.
 */

import { NextResponse } from 'next/server';

const DEFAULT_MAX_BYTES = 64 * 1024; // 64KB — more than enough for any payroll form

/**
 * Parses the request body as JSON and enforces a maximum byte limit.
 * Returns [data, null] on success, or [null, 413 NextResponse] on
 * oversized payloads.
 *
 * @param request   The incoming Request
 * @param maxBytes  Maximum allowed body size in bytes (default: 64KB)
 *
 * @example
 *   const [body, sizeErr] = await parseBody(request);
 *   if (sizeErr) return sizeErr;
 */
export async function parseBody<T = unknown>(
  request: Request,
  maxBytes: number = DEFAULT_MAX_BYTES
): Promise<[T, null] | [null, NextResponse]> {
  const contentLength = Number(request.headers.get('content-length') ?? 0);

  if (contentLength > maxBytes) {
    return [
      null,
      NextResponse.json(
        {
          error: `Request body too large. Maximum allowed: ${maxBytes} bytes.`,
        },
        { status: 413 }
      ),
    ];
  }

  try {
    const text = await request.text();

    if (text.length > maxBytes) {
      return [
        null,
        NextResponse.json(
          { error: `Request body too large. Maximum allowed: ${maxBytes} bytes.` },
          { status: 413 }
        ),
      ];
    }

    const data = JSON.parse(text) as T;
    return [data, null];
  } catch {
    return [null, NextResponse.json({ error: 'Invalid JSON in request body.' }, { status: 400 })];
  }
}
