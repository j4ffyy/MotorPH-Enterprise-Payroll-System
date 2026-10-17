/**
 * Rate Limiting Utility — MotorPH ESS Portal
 *
 * IAS2 MS2 Control: C7 — Rate Limiting on Login / Authentication Endpoints
 *
 * Finding source:
 *   - VibeWrench Manual Verification Checklist:
 *     "Implement rate limiting on login/password reset endpoints"
 *   - Milestone 2 Scanner Results: Authentication bypass risk noted
 *     (source code scan requirement)
 *
 * Implementation: In-memory sliding-window rate limiter
 *   - 5 failed login attempts → 15-minute lockout per IP
 *   - Compatible with Vercel Edge/Node.js runtime
 *   - NOTE: For multi-instance production, replace Map with
 *     Redis/Upstash to share state across Vercel function instances.
 *
 * Security rationale:
 *   A payroll system storing PII and salary data is a high-value target
 *   for credential-stuffing and brute-force attacks. Without rate limiting,
 *   attackers can try thousands of password combinations per second.
 *   This control directly protects employee accounts and the
 *   Confidentiality pillar identified in the Security Implementation Plan.
 */

interface RateLimitEntry {
  count: number;
  firstAttempt: number;
  lockedUntil: number | null;
}

// In-memory store — suitable for single-instance dev/staging.
// Replace with Upstash Redis for multi-instance production.
const rateLimitStore = new Map<string, RateLimitEntry>();

const MAX_ATTEMPTS = 5;
const WINDOW_MS = 15 * 60 * 1000; // 15-minute sliding window
const LOCKOUT_MS = 15 * 60 * 1000; // 15-minute lockout after MAX_ATTEMPTS

/**
 * Checks and updates the rate limit for a given identifier (IP or username).
 *
 * @param identifier  IP address or username string.
 * @returns Object with `allowed` boolean and optional `retryAfterSeconds`.
 */
export function checkRateLimit(identifier: string): {
  allowed: boolean;
  retryAfterSeconds?: number;
  attemptsRemaining?: number;
} {
  const now = Date.now();
  const entry = rateLimitStore.get(identifier);

  // Check if currently locked out
  if (entry?.lockedUntil && now < entry.lockedUntil) {
    const retryAfterSeconds = Math.ceil((entry.lockedUntil - now) / 1000);
    return { allowed: false, retryAfterSeconds };
  }

  if (!entry || now - entry.firstAttempt > WINDOW_MS) {
    // First attempt or window has expired — reset
    rateLimitStore.set(identifier, {
      count: 1,
      firstAttempt: now,
      lockedUntil: null,
    });
    return { allowed: true, attemptsRemaining: MAX_ATTEMPTS - 1 };
  }

  // Within the window
  const newCount = entry.count + 1;

  if (newCount >= MAX_ATTEMPTS) {
    // Lockout triggered
    rateLimitStore.set(identifier, {
      count: newCount,
      firstAttempt: entry.firstAttempt,
      lockedUntil: now + LOCKOUT_MS,
    });
    const retryAfterSeconds = Math.ceil(LOCKOUT_MS / 1000);
    return { allowed: false, retryAfterSeconds };
  }

  rateLimitStore.set(identifier, {
    count: newCount,
    firstAttempt: entry.firstAttempt,
    lockedUntil: null,
  });

  return { allowed: true, attemptsRemaining: MAX_ATTEMPTS - newCount };
}

/**
 * Resets the rate limit counter for a given identifier.
 * Call this on successful authentication to clear failed attempt history.
 *
 * @param identifier  IP address or username string.
 */
export function resetRateLimit(identifier: string): void {
  rateLimitStore.delete(identifier);
}

/**
 * Extracts the client IP from the request headers.
 * Vercel sets the real IP in x-forwarded-for.
 */
export function getClientIp(request: Request): string {
  const forwarded = (request as Request & { headers: Headers }).headers.get('x-forwarded-for');
  if (forwarded) return forwarded.split(',')[0].trim();
  return 'unknown';
}
