/**
 * src/lib/validators.ts — Input Validation Schemas (Zod)
 *
 * IAS2 MS2 Control R2 — Input Validation & Sanitization
 *
 * Gaps fixed:
 *   - employees/route.ts L72–94: body destructured with no type/length/format checks
 *   - leaves/route.ts L45–46: dateFrom/dateTo accepted as any string
 *   - [eid]/route.ts L71: body.phoneNumber, body.address with no max-length
 *   - payroll/calculate/route.ts L14: periodStart/periodEnd as any string
 *
 * All schemas use .trim() to strip whitespace and explicit max lengths
 * to prevent oversized inputs (DoS / DB bloat — related to R7).
 *
 * Usage:
 *   const result = CreateEmployeeSchema.safeParse(body);
 *   if (!result.success) {
 *     return NextResponse.json({ error: result.error.flatten() }, { status: 400 });
 *   }
 *   const data = result.data; // fully typed and validated
 */

import { z } from 'zod';

// ── Reusable primitives ───────────────────────────────────────────────────────

const dateString = z.string().regex(/^\d{4}-\d{2}-\d{2}$/, 'Must be YYYY-MM-DD format');

const phoneNumber = z
  .string()
  .max(20, 'Phone number must be at most 20 characters')
  .regex(/^[\d\s\-+().]*$/, 'Phone number contains invalid characters')
  .optional();

const govId = z
  .string()
  .max(30, 'Government ID must be at most 30 characters')
  .optional()
  .nullable();

// ── R2-a: Create Employee ─────────────────────────────────────────────────────
export const CreateEmployeeSchema = z.object({
  eid: z.number().int().positive().optional(),
  firstName: z.string().min(1, 'First name is required').max(100).trim(),
  lastName: z.string().min(1, 'Last name is required').max(100).trim(),
  birthday: dateString.optional().nullable(),
  address: z
    .string()
    .max(500, 'Address must be at most 500 characters')
    .trim()
    .optional()
    .nullable(),
  phoneNumber,
  username: z
    .string()
    .min(3, 'Username min 3 chars')
    .max(50)
    .regex(
      /^[a-zA-Z0-9_.\-]+$/,
      'Username may only contain letters, numbers, underscores, dots, hyphens'
    ),
  password: z.string().min(8, 'Password must be at least 8 characters').max(128),
  designationId: z.number().int().positive().optional().nullable(),
  supervisorId: z.number().int().positive().optional().nullable(),
  status: z.enum(['Regular', 'Probationary', 'Part-time']).optional(),
  basicSalary: z.number().min(0).max(10_000_000).optional(),
  riceSubsidy: z.number().min(0).max(100_000).optional(),
  phoneAllowance: z.number().min(0).max(100_000).optional(),
  clothingAllowance: z.number().min(0).max(100_000).optional(),
  sssNum: govId,
  philhealthNum: govId,
  tinNum: govId,
  pagibigNum: govId,
  role: z.enum(['ADMIN', 'HR', 'PAYROLL', 'EMPLOYEE']).optional(),
});

// ── R2-b: Update Employee (Admin full update) ─────────────────────────────────
export const UpdateEmployeeAdminSchema = z.object({
  firstName: z.string().min(1).max(100).trim().optional(),
  lastName: z.string().min(1).max(100).trim().optional(),
  birthday: dateString.optional().nullable(),
  address: z.string().max(500).trim().optional().nullable(),
  phoneNumber,
  status: z.enum(['Regular', 'Probationary', 'Part-time']).optional(),
  role: z.enum(['ADMIN', 'HR', 'PAYROLL', 'EMPLOYEE']).optional(),
  isActive: z.boolean().optional(),
  designationId: z.number().int().positive().optional().nullable(),
  supervisorId: z.number().int().positive().optional().nullable(),
  basicSalary: z.number().min(0).max(10_000_000).optional(),
  riceSubsidy: z.number().min(0).max(100_000).optional(),
  phoneAllowance: z.number().min(0).max(100_000).optional(),
  clothingAllowance: z.number().min(0).max(100_000).optional(),
  sssNum: govId,
  philhealthNum: govId,
  tinNum: govId,
  pagibigNum: govId,
});

// ── R2-c: Update Employee (Self — personal contact only) ──────────────────────
export const UpdateEmployeeSelfSchema = z.object({
  phoneNumber,
  address: z.string().max(500).trim().optional().nullable(),
});

// ── R2-d: Apply Leave ─────────────────────────────────────────────────────────
export const ApplyLeaveSchema = z
  .object({
    dateFrom: dateString,
    dateTo: dateString,
    reasonForLeave: z
      .string()
      .min(10, 'Reason must be at least 10 characters')
      .max(1000, 'Reason must be at most 1000 characters')
      .trim(),
  })
  .refine((data) => new Date(data.dateTo) >= new Date(data.dateFrom), {
    message: 'Date To must be on or after Date From',
    path: ['dateTo'],
  });

// ── R2-e: Update Leave Status (HR/Admin) ──────────────────────────────────────
export const UpdateLeaveStatusSchema = z.object({
  leaveId: z.string().min(1).max(20),
  leaveStatus: z.enum(['Approved', 'Rejected']),
});

// ── R2-f: Payroll Calculation ─────────────────────────────────────────────────
export const PayrollCalculateSchema = z
  .object({
    periodStart: dateString.optional().default('2026-01-01'),
    periodEnd: dateString.optional().default('2026-01-31'),
    eid: z.number().int().positive().optional(),
  })
  .refine((data) => new Date(data.periodEnd!) >= new Date(data.periodStart!), {
    message: 'Period end must be on or after period start',
    path: ['periodEnd'],
  });

// ── R2-g: Login ───────────────────────────────────────────────────────────────
export const LoginSchema = z.object({
  username: z.string().min(1).max(100).trim(),
  password: z.string().min(1).max(128),
});

// ── Helper: parse and return error response ───────────────────────────────────
import { NextResponse } from 'next/server';

/**
 * Validates `body` against `schema`. Returns `[data, null]` on success,
 * or `[null, 400 NextResponse]` on failure.
 *
 * @example
 *   const [data, err] = validate(body, CreateEmployeeSchema);
 *   if (err) return err;
 */
export function validate<T>(body: unknown, schema: z.ZodType<T>): [T, null] | [null, NextResponse] {
  const result = schema.safeParse(body);
  if (!result.success) {
    return [
      null,
      NextResponse.json(
        {
          error: 'Validation failed',
          details: result.error.flatten(),
        },
        { status: 400 }
      ),
    ];
  }
  return [result.data, null];
}
