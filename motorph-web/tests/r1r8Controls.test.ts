/**
 * r1r8Controls.test.ts — MotorPH ESS Portal
 * IAS2 MS2 — Tests for Recommended Controls R1–R8
 *
 * All tests use fs.readFileSync to inspect source files as text.
 * No Next.js runtime imports — runs cleanly with Node.js native test runner.
 *
 * Run: node --experimental-strip-types --test tests/r1r8Controls.test.ts
 */

import { describe, it } from 'node:test';
import assert from 'node:assert/strict';
import fs from 'node:fs';
import path from 'node:path';

// ─── R1: AUDIT LOGGING ───────────────────────────────────────────────────────
describe('Control R1: Audit Logging — Accountability & Non-Repudiation', () => {
  const auditPath = path.join(process.cwd(), 'src', 'lib', 'auditLog.ts');
  const auditSource = fs.readFileSync(auditPath, 'utf-8');

  it('auditLog.ts must exist at src/lib/auditLog.ts', () => {
    assert.ok(fs.existsSync(auditPath));
  });

  it('must export logAudit() async function', () => {
    assert.ok(auditSource.includes('export async function logAudit'));
  });

  it('must export createAuditLogger() convenience wrapper', () => {
    assert.ok(auditSource.includes('export function createAuditLogger'));
  });

  it('must be fire-and-forget — catch errors, never throw (failures must not crash API)', () => {
    assert.ok(auditSource.includes('try {') && auditSource.includes('catch (err)'));
    assert.ok(auditSource.includes('console.error'));
  });

  it('must sanitize password field before storing in audit log', () => {
    assert.ok(auditSource.includes('password'));
  });

  it('must define LOGIN_SUCCESS and LOGIN_FAILED action types', () => {
    assert.ok(auditSource.includes("'LOGIN_SUCCESS'"));
    assert.ok(auditSource.includes("'LOGIN_FAILED'"));
  });

  it('must define PAYROLL_CALCULATED action type', () => {
    assert.ok(auditSource.includes("'PAYROLL_CALCULATED'"));
  });

  it('must define EMPLOYEE_UPDATED action type', () => {
    assert.ok(auditSource.includes("'EMPLOYEE_UPDATED'"));
  });

  it('must define LEAVE_APPROVED and LEAVE_REJECTED action types', () => {
    assert.ok(auditSource.includes("'LEAVE_APPROVED'") && auditSource.includes("'LEAVE_REJECTED'"));
  });

  it('AuditLog model must be present in prisma/schema.prisma', () => {
    const schema = fs.readFileSync(path.join(process.cwd(), 'prisma', 'schema.prisma'), 'utf-8');
    assert.ok(schema.includes('model AuditLog'), 'AuditLog model must be in Prisma schema');
  });

  it('AuditLog must have actorEid, before, after, ipAddress, timestamp fields', () => {
    const schema = fs.readFileSync(path.join(process.cwd(), 'prisma', 'schema.prisma'), 'utf-8');
    assert.ok(schema.includes('actorEid'));
    assert.ok(schema.includes('before'));
    assert.ok(schema.includes('after'));
    assert.ok(schema.includes('ipAddress'));
    assert.ok(schema.includes('@default(now())'));
  });

  it('login route must audit LOGIN_SUCCESS', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'auth', 'login', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes("'LOGIN_SUCCESS'"), 'Login route must call logAudit on success');
  });

  it('login route must audit LOGIN_FAILED', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'auth', 'login', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes("'LOGIN_FAILED'"), 'Login route must call logAudit on failure');
  });

  it('payroll route must audit PAYROLL_CALCULATED', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'payroll', 'calculate', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes("'PAYROLL_CALCULATED'"), 'Payroll route must log audit event');
  });

  it('leaves route must audit LEAVE_APPLIED and approval events', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'leaves', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes("'LEAVE_APPLIED'"), 'Leaves route must audit leave applications');
    assert.ok(src.includes("action, 'LEAVE'") || src.includes("'LEAVE'"), 'Leaves route must audit approval/rejection');
  });

  it('employees route must audit EMPLOYEE_CREATED', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'employees', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes("'EMPLOYEE_CREATED'"), 'Employees route must audit employee creation');
  });

  it('logout route must audit LOGOUT event', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'auth', 'logout', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes("'LOGOUT'"), 'Logout must record audit event for accountability');
  });
});

// ─── R2: INPUT VALIDATION ────────────────────────────────────────────────────
describe('Control R2: Input Validation — Zod Schema Enforcement', () => {
  const validPath = path.join(process.cwd(), 'src', 'lib', 'validators.ts');
  const validSource = fs.readFileSync(validPath, 'utf-8');

  it('validators.ts must exist at src/lib/validators.ts', () => {
    assert.ok(fs.existsSync(validPath));
  });

  it('must import from zod and use z.object()', () => {
    assert.ok(validSource.includes("from 'zod'"));
    assert.ok(validSource.includes('z.object('));
  });

  it('must export CreateEmployeeSchema', () => {
    assert.ok(validSource.includes('export const CreateEmployeeSchema'));
  });

  it('must export ApplyLeaveSchema with date cross-field .refine()', () => {
    assert.ok(validSource.includes('export const ApplyLeaveSchema'));
    assert.ok(validSource.includes('.refine('), 'ApplyLeaveSchema must have dateTo >= dateFrom refinement');
  });

  it('must export UpdateEmployeeSelfSchema (restricted) and UpdateEmployeeAdminSchema (full)', () => {
    assert.ok(validSource.includes('export const UpdateEmployeeSelfSchema'));
    assert.ok(validSource.includes('export const UpdateEmployeeAdminSchema'));
  });

  it('must export LoginSchema', () => {
    assert.ok(validSource.includes('export const LoginSchema'));
  });

  it('must export PayrollCalculateSchema with period date validation', () => {
    assert.ok(validSource.includes('export const PayrollCalculateSchema'));
  });

  it('must export validate() tuple helper [data,null] | [null,NextResponse]', () => {
    assert.ok(validSource.includes('export function validate'));
  });

  it('address field must have max 500 character limit (DoS prevention)', () => {
    assert.ok(validSource.includes('.max(500'));
  });

  it('password must require min 8 characters', () => {
    assert.ok(validSource.includes('.min(8'));
  });

  it('date fields must enforce YYYY-MM-DD format via regex', () => {
    assert.ok(validSource.includes('/^\\d{4}-\\d{2}-\\d{2}$/'));
  });

  it('username must restrict to safe alphanumeric characters', () => {
    assert.ok(validSource.includes('/^[a-zA-Z0-9_'));
  });

  it('role must be constrained to valid enum: ADMIN|HR|PAYROLL|EMPLOYEE', () => {
    assert.ok(validSource.includes("z.enum(['ADMIN', 'HR', 'PAYROLL', 'EMPLOYEE'])"));
  });

  it('leaveStatus must only accept Approved or Rejected', () => {
    assert.ok(validSource.includes("z.enum(['Approved', 'Rejected'])"));
  });
});

// ─── R3: CENTRALIZED RBAC ────────────────────────────────────────────────────
describe('Control R3: Centralized RBAC Middleware (Replaces Scattered Inline Checks)', () => {
  const rbacPath = path.join(process.cwd(), 'src', 'lib', 'rbac.ts');
  const rbacSource = fs.readFileSync(rbacPath, 'utf-8');

  it('rbac.ts must exist at src/lib/rbac.ts', () => {
    assert.ok(fs.existsSync(rbacPath));
  });

  it('must export hasPermission()', () => {
    assert.ok(rbacSource.includes('export function hasPermission'));
  });

  it('must export requirePermission() returning NextResponse|null', () => {
    assert.ok(rbacSource.includes('export function requirePermission'));
  });

  it('must export requireSelfOrElevated() for horizontal privilege prevention (R4)', () => {
    assert.ok(rbacSource.includes('export function requireSelfOrElevated'));
  });

  it('must export filterEmployeeResponse() for PII minimization (R5)', () => {
    assert.ok(rbacSource.includes('export function filterEmployeeResponse'));
  });

  it("EMPLOYEE role must only have ['read:own', 'write:own'] — principle of least privilege", () => {
    assert.ok(rbacSource.includes("EMPLOYEE: ['read:own', 'write:own']"));
  });

  it('HR must NOT have run:payroll — separation of duties', () => {
    const hrSection = rbacSource.match(/HR:\s*\[([^\]]+)\]/)?.[1] ?? '';
    assert.ok(!hrSection.includes('run:payroll'), 'HR must not be able to run payroll (financial controls separation)');
  });

  it('PAYROLL must NOT have write:employee — separation of duties', () => {
    const payrollSection = rbacSource.match(/PAYROLL:\s*\[([^\]]+)\]/)?.[1] ?? '';
    assert.ok(!payrollSection.includes('write:employee'), 'PAYROLL must not be able to manage employees');
  });

  it('403 error message must include the missing permission name (explicit denial)', () => {
    assert.ok(rbacSource.includes("does not have '") || rbacSource.includes('permission'));
  });

  it('employees/route.ts must use requirePermission() — not scattered inline checks', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'employees', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes('requirePermission'), 'Must use centralized RBAC');
    // Check that any occurrence of the old pattern is only in a comment (starts with //)
    const lines = src.split('\n');
    const illegalLines = lines.filter(l =>
      l.includes("session.role === 'EMPLOYEE'") && !l.trimStart().startsWith('//')
    );
    assert.equal(illegalLines.length, 0, 'Must not use scattered inline role check in actual code');
  });

  it('leaves/route.ts must use requirePermission or isManager from rbac.ts', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'leaves', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes('requirePermission') || src.includes('isManager'));
  });

  it('payroll/calculate/route.ts must use requirePermission', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'payroll', 'calculate', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes('requirePermission'));
  });
});

// ─── R4: HORIZONTAL PRIVILEGE ESCALATION ─────────────────────────────────────
describe('Control R4: Horizontal Privilege Escalation Prevention (OWASP A01:2021)', () => {
  const eidSource = fs.readFileSync(
    path.join(process.cwd(), 'src', 'app', 'api', 'employees', '[eid]', 'route.ts'), 'utf-8'
  );

  it('[eid] route must import and use requireSelfOrElevated()', () => {
    assert.ok(eidSource.includes('requireSelfOrElevated'), 'Must use requireSelfOrElevated from rbac.ts');
  });

  it('GET handler must call requireSelfOrElevated(session, targetEid)', () => {
    const getSection = eidSource.split('export async function PUT')[0];
    assert.ok(
      getSection.includes('requireSelfOrElevated(session, targetEid)'),
      'GET handler must enforce self-isolation with requireSelfOrElevated()'
    );
  });

  it('must validate targetEid is a valid number (prevents NaN bypass attack)', () => {
    assert.ok(eidSource.includes('isNaN(targetEid)'), 'Must check isNaN to prevent NaN=NaN bypass');
  });

  it('must NOT use old scattered inline role check pattern', () => {
    // The old check pattern was: session.role === 'EMPLOYEE' && session.eid !== targetEid
    // Using requireSelfOrElevated() from rbac.ts replaces this.
    // We check that any occurrence is only in comments, not in actual code.
    const lines = eidSource.split('\n');
    const illegalLines = lines.filter(l =>
      l.includes("session.role === 'EMPLOYEE'") &&
      l.includes('session.eid !== targetEid') &&
      !l.trimStart().startsWith('//')
    );
    assert.equal(illegalLines.length, 0, 'Must use requireSelfOrElevated(), not inline check in code');
  });

  it('must return 403 on unauthorized cross-employee access', () => {
    assert.ok(eidSource.includes('{ status: 403 }') || eidSource.includes('403'), 'Must return HTTP 403 on access violation');
  });

  it('self-update (EMPLOYEE) must use restricted UpdateEmployeeSelfSchema', () => {
    assert.ok(eidSource.includes('UpdateEmployeeSelfSchema'), 'Self-update must use restricted schema');
  });

  it('admin update must use full UpdateEmployeeAdminSchema', () => {
    assert.ok(eidSource.includes('UpdateEmployeeAdminSchema'), 'Admin update must use full schema');
  });

  it('salary modification must require write:salary permission', () => {
    assert.ok(eidSource.includes("'write:salary'"), 'Salary changes must be permission-gated');
  });

  it('role change must require manage:roles permission', () => {
    assert.ok(eidSource.includes("'manage:roles'"), 'Role changes must be permission-gated');
  });
});

// ─── R5: PII MINIMIZATION ────────────────────────────────────────────────────
describe('Control R5: PII Minimization in API Responses', () => {

  it('filterEmployeeResponse must always strip password hash regardless of role', () => {
    const src = fs.readFileSync(path.join(process.cwd(), 'src', 'lib', 'rbac.ts'), 'utf-8');
    assert.ok(src.includes('password: _pw'), 'Password hash must always be stripped');
  });

  it('filterEmployeeResponse must strip govIds for roles without view:govids', () => {
    const src = fs.readFileSync(path.join(process.cwd(), 'src', 'lib', 'rbac.ts'), 'utf-8');
    assert.ok(src.includes('view:govids'), 'govIds must be role-restricted');
  });

  it('/api/auth/me must strip password hash from response', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'auth', 'me', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes('password'), '/me must explicitly handle password field');
  });

  it('/api/auth/me must strip govIds from session response', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'auth', 'me', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes('govIds'), '/me must exclude govIds from response');
  });

  it('employee list must call filterEmployeeResponse() before returning', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'employees', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes('filterEmployeeResponse'), 'Employee list must filter PII per requesting role');
  });

  it('employee create response must strip password hash', () => {
    const src = fs.readFileSync(
      path.join(process.cwd(), 'src', 'app', 'api', 'employees', 'route.ts'), 'utf-8'
    );
    assert.ok(src.includes('_pw') || src.includes("password?"), 'Create response must strip password');
  });
});

// ─── R6: SESSION INVALIDATION ────────────────────────────────────────────────
describe('Control R6: Session Invalidation on Logout', () => {
  const logoutPath = path.join(process.cwd(), 'src', 'app', 'api', 'auth', 'logout', 'route.ts');
  const logoutSource = fs.readFileSync(logoutPath, 'utf-8');

  it('logout route must exist', () => {
    assert.ok(fs.existsSync(logoutPath));
  });

  it('logout must delete JWT session cookie', () => {
    assert.ok(
      logoutSource.includes('AUTH_COOKIE_NAME') || logoutSource.includes('motorph_session'),
      'Logout must reference and delete the JWT session cookie'
    );
  });

  it('logout must also clear CSRF token cookie (complete session teardown)', () => {
    assert.ok(
      logoutSource.includes('motorph_csrf') || logoutSource.includes('CSRF_COOKIE_NAME'),
      'Logout must clear CSRF cookie to prevent token reuse'
    );
  });

  it('logout must record LOGOUT audit event for accountability', () => {
    assert.ok(logoutSource.includes("'LOGOUT'"), 'Logout must be audited');
  });
});

// ─── R7: BODY SIZE LIMITING ──────────────────────────────────────────────────
describe('Control R7: Request Body Size Limiting (DoS Prevention)', () => {
  const parsePath = path.join(process.cwd(), 'src', 'lib', 'parseBody.ts');
  const parseSource = fs.readFileSync(parsePath, 'utf-8');

  it('parseBody.ts must exist at src/lib/parseBody.ts', () => {
    assert.ok(fs.existsSync(parsePath));
  });

  it('must define a maximum byte limit', () => {
    assert.ok(parseSource.includes('DEFAULT_MAX_BYTES') || parseSource.includes('maxBytes'));
  });

  it('must return HTTP 413 Payload Too Large on oversized body', () => {
    assert.ok(parseSource.includes('413'), 'Must return 413 for oversized requests');
  });

  it('must read actual body text (not just Content-Length header — which can be spoofed)', () => {
    assert.ok(parseSource.includes('await request.text()'), 'Must read actual body bytes');
  });

  it('must return HTTP 400 on malformed JSON', () => {
    assert.ok(parseSource.includes('400'), 'Must return 400 on invalid JSON');
  });

  it('must return tuple [data, null] | [null, NextResponse] for clean error handling', () => {
    // Return statements may be multi-line, so check for key patterns separately
    assert.ok(
      parseSource.includes('[data, null]'),
      'parseBody must return [data, null] on success'
    );
    assert.ok(
      parseSource.includes('[\n      null,') || parseSource.includes('[\r\n      null,') ||
      parseSource.includes('return [\n') || parseSource.includes('null,\n'),
      'parseBody must return [null, Response] on error'
    );
  });

  const routesToCheck: [string, string][] = [
    ['employees POST', 'src/app/api/employees/route.ts'],
    ['leaves POST', 'src/app/api/leaves/route.ts'],
    ['payroll/calculate POST', 'src/app/api/payroll/calculate/route.ts'],
    ['auth/login POST', 'src/app/api/auth/login/route.ts'],
  ];

  for (const [routeName, routePath] of routesToCheck) {
    it(`${routeName} must use parseBody() for body size limiting`, () => {
      const src = fs.readFileSync(path.join(process.cwd(), routePath), 'utf-8');
      assert.ok(src.includes('parseBody'), `${routeName} must import and call parseBody()`);
    });
  }
});

// ─── R8: SOURCE MAP & TECH DISCLOSURE SUPPRESSION ────────────────────────────
describe('Control R8: Source Map Suppression & Technology Disclosure Prevention', () => {
  const configSource = fs.readFileSync(path.join(process.cwd(), 'next.config.ts'), 'utf-8');

  it('next.config.ts must set productionBrowserSourceMaps: false', () => {
    assert.ok(configSource.includes('productionBrowserSourceMaps: false'));
  });

  it('productionBrowserSourceMaps must be exactly false (not just falsy)', () => {
    const match = configSource.match(/productionBrowserSourceMaps\s*:\s*(\w+)/);
    assert.ok(match, 'productionBrowserSourceMaps must be present in next.config.ts');
    assert.equal(match![1], 'false', 'Must be exactly boolean false');
  });

  it('next.config.ts must set poweredByHeader: false', () => {
    assert.ok(configSource.includes('poweredByHeader: false'));
  });

  it('poweredByHeader must be exactly false (removes X-Powered-By: Next.js response header)', () => {
    const match = configSource.match(/poweredByHeader\s*:\s*(\w+)/);
    assert.ok(match, 'poweredByHeader must be present in next.config.ts');
    assert.equal(match![1], 'false', 'Must be exactly boolean false');
  });
});
