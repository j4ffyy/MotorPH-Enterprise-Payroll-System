import { NextRequest, NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { comparePassword, signToken, AUTH_COOKIE_NAME } from '@/lib/auth';
import { checkRateLimit, resetRateLimit, getClientIp } from '@/lib/rateLimit';
import { generateCsrfToken, setCsrfCookie } from '@/lib/csrf';
import { validate, LoginSchema } from '@/lib/validators';
import { parseBody } from '@/lib/parseBody';
import { logAudit } from '@/lib/auditLog';
import { successResponse, errorResponse } from '@/lib/apiResponse';

/**
 * POST /api/auth/login
 *
 * IAS2 MS2 Security Controls Integrated:
 *   C7 — Rate Limiting: 5 attempts / 15-min window per IP
 *   C6 — CSRF Token Issuance: Fresh token set on successful login
 *   C1 — Externalized Secrets: JWT_SECRET from env (not hardcoded)
 *   C3 — Parameterized Queries: Prisma ORM (no raw SQL)
 *   R1 — Audit Logging: LOGIN_SUCCESS / LOGIN_FAILED events
 *   R2 — Input Validation: LoginSchema (length bounds, trim)
 *   R7 — Body Size: parseBody() enforces 64KB limit
 */
export async function POST(request: NextRequest) {
  try {
    // R7: Body size limit
    const [rawBody, sizeErr] = await parseBody(request);
    if (sizeErr) return sizeErr;

    // R2: Input validation — sanitizes and length-bounds username/password
    const [data, valErr] = validate(rawBody, LoginSchema);
    if (valErr) return valErr;
    const { username, password } = data;

    const clientIp = getClientIp(request);
    const rateCheck = checkRateLimit(clientIp);
    if (!rateCheck.allowed) {
      // R1: Audit log the rate-limit trigger — non-repudiation for brute-force attempts
      // Uses a system-level session since we don't yet know who is attempting to log in
      await logAudit({
        session: { eid: 0, username: data.username, name: 'SYSTEM', role: 'SYSTEM' },
        action: 'RATE_LIMIT_TRIGGERED',
        resourceType: 'AUTH',
        resourceId: data.username,
        ipAddress: clientIp,
      });
      return NextResponse.json(
        {
          error: `Too many failed attempts. Please try again in ${rateCheck.retryAfterSeconds} seconds.`,
          retryAfterSeconds: rateCheck.retryAfterSeconds,
        },
        {
          status: 429,
          headers: {
            'Retry-After': String(rateCheck.retryAfterSeconds),
          },
        }
      );
    }

    // ── C3: Parameterized DB Lookup (via Prisma ORM) ────────────────
    const employee = await prisma.employee.findFirst({
      where: {
        OR: [{ username: username.trim() }, { eid: Number(username) || -1 }],
      },
      include: {
        designation: {
          include: { department: true },
        },
      },
    });

    if (!employee) {
      // Do NOT reveal whether the username exists (prevents user enumeration)
      return errorResponse('Invalid username or password', 401);
    }

    if (!employee.isActive) {
      return errorResponse('Account is deactivated. Please contact HR.', 403);
    }

    // ── Password Verification ───────────────────────────────────────
    const isValid = await comparePassword(password, employee.password);
    if (!isValid) {
      // R1: Audit log failed login attempt (non-repudiation for security events)
      await logAudit({
        session: {
          eid: employee.eid,
          username: employee.username || '',
          name: '',
          role: employee.role,
        },
        action: 'LOGIN_FAILED',
        resourceType: 'AUTH',
        resourceId: employee.eid,
        ipAddress: clientIp,
      });
      return errorResponse('Invalid username or password', 401);
    }

    // ── Successful Login ────────────────────────────────────────────
    // C7: Reset rate limit on successful authentication
    resetRateLimit(clientIp);

    const payload = {
      eid: employee.eid,
      username: employee.username || '',
      name: `${employee.firstName} ${employee.lastName}`,
      role: employee.role,
      designation: employee.designation?.name,
      department: employee.designation?.department?.name,
    };

    const token = signToken(payload);

    const response = successResponse({
      success: true,
      user: payload,
    });

    // ── C1: Secure JWT Cookie (HttpOnly + SameSite=Lax) ─────────────
    response.cookies.set({
      name: AUTH_COOKIE_NAME,
      value: token,
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'lax',
      path: '/',
      maxAge: 8 * 60 * 60, // 8 hours — matches JWT expiry
    });

    // ── C6: CSRF Token Cookie (readable by JS, SameSite=Strict) ─────
    const csrfToken = generateCsrfToken();
    setCsrfCookie(response, csrfToken);

    // R1: Audit log successful login (accountability)
    await logAudit({
      session: {
        eid: employee.eid,
        username: employee.username || '',
        name: payload.name,
        role: employee.role,
      },
      action: 'LOGIN_SUCCESS',
      resourceType: 'AUTH',
      resourceId: employee.eid,
      ipAddress: clientIp,
    });

    return response;
  } catch (error) {
    console.error('Login error:', error);
    return errorResponse('An unexpected authentication error occurred.', 500);
  }
}
