/**
 * Centralized API Response Builder Helper (DRY Principle)
 * Standardizes success and error responses across all backend route handlers.
 */

import { NextResponse } from 'next/server';

export function successResponse(data: any, status = 200) {
  return NextResponse.json(data, { status });
}

export function errorResponse(message: string, status = 400, details?: any) {
  return NextResponse.json(
    details ? { error: message, details } : { error: message },
    { status }
  );
}
