<div align="center">

<img src="https://img.shields.io/badge/MotorPH-Enterprise%20Payroll%20System-4166F5?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIiBzdHJva2U9IiNmZmZmZmYiIHN0cm9rZS13aWR0aD0iMiI+PHBhdGggZD0iTTEyIDJMMiA3bDEwIDUgMTAtNS0xMC01eiIvPjxwYXRoIGQ9Ik0yIDE3bDEwIDUgMTAtNSIvPjxwYXRoIGQ9Ik0yIDEybDEwIDUgMTAtNSIvPjwvc3ZnPg==" alt="MotorPH EPS"/>

# MotorPH Enterprise Payroll System (EPS)

**A Secure, Full-Stack, Enterprise-Grade Payroll & Workforce Management Platform**  
Built for **AOOP** & **IAS2** (Information Assurance & Security 2) — Team **CTRL+ALT+ELITE** (Group 8)

[![Next.js](https://img.shields.io/badge/Next.js-16.4.1-black?logo=nextdotjs)](https://nextjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Prisma](https://img.shields.io/badge/Prisma-6.19-2D3748?logo=prisma)](https://www.prisma.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon%20Serverless-336791?logo=postgresql&logoColor=white)](https://neon.tech/)
[![Tests](https://img.shields.io/badge/Tests-163%20Passing%20(100%25)-22c55e?logo=node.js&logoColor=white)](#-testing--quality-assurance)
[![Security Headers](https://img.shields.io/badge/Security%20Headers-A%2B%20Hardened-brightgreen?logo=shield)](https://securityheaders.com)
[![Deployed on Vercel](https://img.shields.io/badge/Deployed-Vercel-000000?logo=vercel)](https://motorph-eps.vercel.app)
[![AmIHackable](https://amihackable.dev/api/badge/ec901602-22a9-4920-8ce5-ec56be5d1d13)](https://amihackable.dev/scan/ec901602-22a9-4920-8ce5-ec56be5d1d13)

</div>

---

## 📑 Table of Contents

- [🌐 Live Deployment & Credentials](#-live-deployment--credentials)
- [✨ Key System Highlights](#-key-system-highlights)
- [🏗️ Technology Architecture](#️-technology-architecture)
- [🛡️ Security Architecture & IAS2 Controls (R1–R8)](#️-security-architecture--ias2-controls-r1r8)
- [🧮 2025/2026 Philippine Statutory Payroll Engine](#-20252026-philippine-statutory-payroll-engine)
- [📁 Project Structure](#-project-structure)
- [🧪 Testing & Quality Assurance](#-testing--quality-assurance)
- [🔌 REST API Reference](#-rest-api-reference)
- [🚀 Local Development & Setup](#-local-development--setup)
- [👥 Team CTRL+ALT+ELITE](#-team-ctrlaltelite)

---

## 🌐 Live Deployment & Credentials

> **Production Application:** [https://motorph-eps.vercel.app](https://motorph-eps.vercel.app)  
> **Source Repository:** [https://github.com/j4ffyy/MotorPH-Enterprise-Payroll-System](https://github.com/j4ffyy/MotorPH-Enterprise-Payroll-System)

### Demo Role Accounts

| Role | Username | Password | Access Level & Scope |
|------|----------|----------|----------------------|
| **CEO / Admin** | `garcia.manuel` | `admin123` | Full administrative control: Company-wide Payroll Engine, Employee Directory CRUD, Audit Logs, System Settings. |
| **HR Manager** | `lim.antonio` | `hr123` | Workforce Administration: Employee Directory (view/create/update), Leave Request approvals, personal My Payslip. |
| **Payroll Officer** | `san-jose.bianca` | `payroll123` | Financial Operations: Workforce-wide Payroll Computation, Bank Remittance summary, personal My Payslip. |
| **Regular Employee** | `reyes.sofia` | `123abc` | Employee Self-Service (ESS): Personal My Payslip, Timesheet Clock-In/Clock-Out, Leave Submissions. |

> **Security Note:** All credentials are authenticated via BCrypt (10 salt rounds) against salted hashes. Plaintext passwords or backdoor credentials are strictly barred and validated via automated unit testing.

---

## ✨ Key System Highlights

- **Modernized Full-Stack Cloud App**: Replaces legacy desktop software with a responsive, zero-install Next.js 16 web application featuring fluid dark-mode ergonomics.
- **Enterprise Separation of Duties (SoD)**: Strict role boundaries isolate HR personnel from executive payroll calculations and prevent employees from viewing coworkers' compensation.
- **Audit Logging & Non-Repudiation**: Dedicated PostgreSQL audit trail records who did what, when, and from what IP address.
- **Full Statutory Compliance**: Automatic computation of Philippine SSS, PhilHealth, Pag-IBIG (HDMF), and BIR TRAIN Law withholding taxes.
- **Zero-Trust Hardened API**: Endpoints feature centralized role-based access control, Zod schema validation, PII redaction, sliding-window rate limiting, and double-submit CSRF defenses.

---

## 🏗️ Technology Architecture

```mermaid
graph TD
    Client["Client Browser (React 19 / Next.js 16)"]
    VercelEdge["Vercel Edge & HTTP Security Headers (CSP, HSTS)"]
    CSRFMiddleware["CSRF & Rate Limiting Guard"]
    RBACMiddleware["Centralized RBAC Middleware (requireAuth, requireRole)"]
    APIHandlers["Next.js Route Handlers (Zod Input Validation)"]
    PrismaClient["Prisma 6.19 ORM (Safe Select & PII Minimization)"]
    NeonDB[("Neon PostgreSQL Serverless Database")]
    AuditLog["Audit Logging Engine (audit_logs table)"]

    Client -->|HTTPS / Strict Headers| VercelEdge
    VercelEdge --> CSRFMiddleware
    CSRFMiddleware --> RBACMiddleware
    RBACMiddleware --> APIHandlers
    APIHandlers --> PrismaClient
    APIHandlers --> AuditLog
    PrismaClient --> NeonDB
    AuditLog --> NeonDB
```

| Layer | Technology | Purpose |
|-------|------------|---------|
| **Frontend** | [Next.js 16](https://nextjs.org/) (React 19, Server Components, Turbopack) | High-performance user interface, server-side rendering, and responsive layout |
| **Styling & UI** | Tailwind CSS 4 & [Lucide React](https://lucide.dev/) | Modern executive dashboard styling, dark mode, accessible components |
| **Backend & Routing** | Next.js App Router API Routes | REST API with server-side validation and secure session orchestration |
| **Data Layer (ORM)** | [Prisma 6.19](https://prisma.io/) | Type-safe database queries, schema migrations, and connection pooling |
| **Database** | [Neon Serverless PostgreSQL](https://neon.tech/) | Cloud database with connection pooling and instant point-in-time recovery |
| **Session & Auth** | JSON Web Tokens (`jsonwebtoken`) + HTTP-only Cookies | Stateless, tamper-proof session tokens with strict SameSite security |
| **Input Validation** | [Zod 3](https://zod.dev/) | Strict runtime schema validation across all mutate endpoints |
| **Security Scanning** | Node.js native test runner (`node:test`) | 163 end-to-end unit, integration, and security regression tests |

---

## 🛡️ Security Architecture & IAS2 Controls (R1–R8)

The application implements a defense-in-depth posture aligned with OWASP Top 10 (2021) recommendations and the IAS2 curriculum:

```
┌────────────────────────────────────────────────────────────────────────┐
│                   PERIMETER & TRANSPORT SECURITY                       │
│  - HSTS (Strict-Transport-Security: max-age=63072000; preload)         │
│  - Content-Security-Policy (CSP) with script & connect restrictions    │
│  - X-Frame-Options: DENY, X-Content-Type-Options: nosniff              │
│  - RFC 9116 security.txt & robots.txt PII crawl protection             │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                    API GATEWAY & TRAFFIC SHAPING                       │
│  - In-Memory Sliding Window Rate Limiting (5 failed auth/15m) (R6)     │
│  - Double-Submit Cookie CSRF Defense (motorph_csrf) (R7)               │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                  AUTHENTICATION & ACCESS CONTROL                       │
│  - BCrypt Hashing (10 rounds, zero plaintext fallbacks)                │
│  - Centralized Declarative RBAC Middleware (requireRole) (R3)          │
│  - Horizontal Privilege Escalation Prevention (EID ownership) (R4)     │
└───────────────────────────────────┬────────────────────────────────────┘
                                    │
┌───────────────────────────────────▼────────────────────────────────────┐
│                   DATA INTEGRITY & ACCOUNTABILITY                      │
│  - Zod Runtime Schema Validation & Length Limits (R2)                  │
│  - Response PII Minimization (Safe Select Filters) (R5)                │
│  - Immutable Audit Logging (audit_logs table) (R1)                     │
└────────────────────────────────────────────────────────────────────────┘
```

### Detailed Controls Matrix

| Control ID | Security Control | Description & Vulnerability Mitigated | Implementation |
|------------|------------------|---------------------------------------|----------------|
| **R1** | **Audit Logging & Non-Repudiation** | Mitigates lack of accountability (OWASP A09). Records sensitive actions (`LOGIN`, `LOGOUT`, `EMPLOYEE_CREATE`, `EMPLOYEE_UPDATE`, `PAYROLL_CALCULATE`, `LEAVE_APPROVE`) with actor, role, IP, and timestamp. | [`src/lib/auditLog.ts`](file:///src/lib/auditLog.ts) + `audit_logs` Prisma model |
| **R2** | **Strict Schema Validation (Zod)** | Mitigates injection and payload poisoning (OWASP A03). Strips unexpected fields, enforces string boundaries, email formats, and date structures. | [`src/lib/validation.ts`](file:///src/lib/validation.ts) |
| **R3** | **Centralized RBAC Middleware** | Replaces fragmented inline checks with uniform declarative route protectors (`requireAuth`, `requireRole`). Eliminates privilege escalation bugs. | [`src/lib/rbac.ts`](file:///src/lib/rbac.ts) |
| **R4** | **Horizontal Privilege Escalation Fix** | Prevents IDOR (OWASP A01). Ensures `EMPLOYEE` role can only request and view their own personal payroll and employee records. | [`src/app/api/employees/[eid]/route.ts`](file:///src/app/api/employees/[eid]/route.ts) |
| **R5** | **PII Minimization in Responses** | Mitigates sensitive data exposure (OWASP A02). Redacts SSS, TIN, PhilHealth, Pag-IBIG numbers, and salary data when queried by non-admin roles or in bulk listings. | [`src/lib/pii.ts`](file:///src/lib/pii.ts) |
| **R6** | **Sliding Window Rate Limiting** | Defends against brute-force password cracking and DoS (OWASP A07). Enforces 5 failed attempts per 15-minute window with `429 Too Many Requests` & `Retry-After`. | [`src/lib/rateLimit.ts`](file:///src/lib/rateLimit.ts) |
| **R7** | **Double-Submit Cookie CSRF Defense** | Neutralizes Cross-Site Request Forgery (OWASP A01). Requires `x-csrf-token` header matching `motorph_csrf` cookie on all state-altering requests (`POST`, `PUT`, `DELETE`). | [`src/lib/csrf.ts`](file:///src/lib/csrf.ts) |
| **R8** | **HTTP Security Headers & Crawler Policy** | Comprehensive hardening: CSP, HSTS, X-Content-Type-Options, X-Frame-Options, Referrer-Policy, plus `robots.txt` and RFC 9116 `.well-known/security.txt`. | [`next.config.ts`](file:///next.config.ts), `public/robots.txt`, `public/.well-known/security.txt` |

---

## 🧮 2025/2026 Philippine Statutory Payroll Engine

All statutory deduction formulas strictly follow Republic Acts and government circulars:

### 1. Social Security System (SSS) — RA 11199
- **Contribution Rate**: 5% employee share based on Monthly Salary Credit (MSC).
- **Minimum MSC**: ₱4,000.00 (Minimum contribution: ₱200.00).
- **Maximum MSC**: ₱30,000.00 (Maximum contribution: ₱1,500.00).

### 2. Philippine Health Insurance Corp. (PhilHealth) — UHC Act
- **Premium Rate**: 5% total monthly premium split equally (2.5% employee share).
- **Income Floor**: ₱10,000.00 (Minimum deduction: ₱250.00).
- **Income Ceiling**: ₱100,000.00 (Maximum deduction: ₱2,500.00).

### 3. Home Development Mutual Fund (Pag-IBIG / HDMF) — Circular No. 460
- **Rate**: 1% for basic salary ≤ ₱1,500; 2% for basic salary > ₱1,500.
- **Maximum Employee Contribution**: Capped at ₱200.00 / month.

### 4. Bureau of Internal Revenue (BIR) Withholding Tax — RA 10963 (TRAIN Law)
Calculated on **Taxable Income** (`Gross Income - Mandatory SSS, PhilHealth, Pag-IBIG Deductions`):

| Monthly Taxable Income | Tax Computation Formula |
|---|---|
| Up to ₱20,833.00 | ₱0.00 (0% exempt) |
| ₱20,833.01 – ₱33,333.00 | 15% of excess over ₱20,833 |
| ₱33,333.01 – ₱66,667.00 | ₱1,875.00 + 20% of excess over ₱33,333 |
| ₱66,667.01 – ₱166,667.00 | ₱8,541.80 + 25% of excess over ₱66,667 |
| ₱166,667.01 – ₱666,667.00 | ₱33,541.80 + 30% of excess over ₱166,667 |
| Over ₱666,667.00 | ₱183,541.80 + 35% of excess over ₱666,667 |

---

## 📁 Project Structure

```
MotorPH-Enterprise-Payroll-System/
├── motorph-web/                       # Next.js 16 Web Application
│   ├── prisma/
│   │   ├── schema.prisma              # PostgreSQL database models (including audit_logs)
│   │   ├── seed.js                    # 34-employee seed dataset
│   │   └── prisma.config.ts           # Neon connection pooling configuration
│   ├── public/
│   │   ├── .well-known/
│   │   │   └── security.txt           # RFC 9116 Vulnerability Disclosure Policy
│   │   ├── robots.txt                 # Crawler blocking for confidential payroll routes
│   │   └── logo.svg                   # MotorPH corporate vector brand
│   ├── src/
│   │   ├── app/
│   │   │   ├── api/                   # Hardened REST API Endpoints
│   │   │   │   ├── auth/              # Login, Logout, Session verification
│   │   │   │   ├── employees/         # Employee roster & IDOR-protected detail views
│   │   │   │   ├── payroll/           # Company & individual payroll computation
│   │   │   │   ├── timesheet/         # Clock-in/out records
│   │   │   │   └── leaves/            # Leave application & approval workflows
│   │   │   ├── dashboard/             # Role-adaptive analytics & quick actions
│   │   │   ├── employees/             # Employee roster & registration modal
│   │   │   ├── payroll/               # Payroll computation table (Admin & Payroll only)
│   │   │   ├── payslip/               # Official printable employee payslips
│   │   │   ├── timesheet/             # DTR tracking & attendance history
│   │   │   └── leaves/                # Leave balance & submission manager
│   │   ├── components/
│   │   │   ├── AppShell.tsx           # Role-aware sidebar, topbar, & navigation
│   │   │   └── MotorPHLogo.tsx        # Dynamic SVG company logo
│   │   └── lib/
│   │       ├── auditLog.ts            # Audit logging module (R1)
│   │       ├── validation.ts          # Zod input schemas (R2)
│   │       ├── rbac.ts                # Centralized RBAC middleware (R3)
│   │       ├── pii.ts                 # PII redaction and safe select filters (R5)
│   │       ├── rateLimit.ts           # In-memory sliding-window rate limiter (R6)
│   │       ├── csrf.ts                # Double-submit cookie CSRF manager (R7)
│   │       ├── auth.ts                # JWT cookie issuance & session verification
│   │       ├── payrollCalculator.ts   # Philippine statutory computation engine
│   │       └── prisma.ts              # Global singleton Prisma client
│   ├── tests/
│   │   ├── payrollCalculator.test.ts  # 19 tests: Statutory formulas & TRAIN tax
│   │   ├── securityControls.test.ts   # 60 tests: Baseline IAS2 MS1 controls
│   │   └── r1r8Controls.test.ts       # 84 tests: Controls R1 through R8 verification
│   ├── next.config.ts                 # HTTP Security Headers (CSP, HSTS, etc.)
│   └── package.json                   # Dependencies, build scripts & test configurations
└── README.md                          # Repository Documentation
```

---

## 🧪 Testing & Quality Assurance

The codebase includes **163 automated tests** running on Node.js 22's native test runner (`node:test`) with zero external test harness overhead.

```bash
# Run all 163 tests across all suites
npm run test:all

# Run statutory payroll calculation compliance tests (19 tests)
npm test

# Run all security control test suites (144 tests)
npm run test:security

# Run R1-R8 enterprise controls specifically (84 tests)
npm run test:r1r8
```

### Test Suite Summary

```
✔ Statutory Compliance & Tax Computation (19/19)
  ├── SSS Statutory Contribution (RA 11199)
  ├── PhilHealth Contribution (UHC Act)
  ├── Pag-IBIG / HDMF (Circular No. 460)
  ├── BIR TRAIN Law Withholding Tax (RA 10963)
  └── BCrypt Password Hashing & Salt Verification

✔ Baseline Security Controls (60/60)
  ├── Role-Based Access Control Boundaries
  ├── Session Cookie Security (HttpOnly, SameSite, Secure)
  ├── Input Sanitization & Payload Sizing
  └── HTTP Security Headers & Disallow Policies

✔ R1–R8 Enterprise Security Controls (84/84)
  ├── Control R1: Audit Logging & Non-Repudiation
  ├── Control R2: Strict Zod Input Validation
  ├── Control R3: Centralized Declarative RBAC
  ├── Control R4: Horizontal Privilege Escalation Protection (IDOR)
  ├── Control R5: PII Minimization in API Responses
  ├── Control R6: Sliding-Window Rate Limiting
  ├── Control R7: Double-Submit Cookie CSRF Defense
  └── Control R8: Security Headers, robots.txt & security.txt

-----------------------------------------------------------
Total Suites: 43   |   Total Tests: 163   |   Pass Rate: 100%
Execution Time: ~500ms
```

---

## 🔌 REST API Reference

| Method | Endpoint | Allowed Roles | CSRF Required | Description |
|:------:|----------|:-------------:|:-------------:|-------------|
| `POST` | `/api/auth/login` | Public | No (exempt) | Authenticates credentials with rate limiting (5 attempts/15m). Issues JWT & CSRF cookies. |
| `POST` | `/api/auth/logout` | Any authenticated | Yes | Clears session cookie and logs audit logout event. |
| `GET` | `/api/auth/me` | Any authenticated | No | Returns currently authenticated user session. |
| `GET` | `/api/employees` | `ADMIN`, `HR` | No | Lists employee roster with role-based PII filtering. |
| `POST` | `/api/employees` | `ADMIN`, `HR` | Yes | Registers new employee with Zod validation & auto-assigned EID. |
| `GET` | `/api/employees/[eid]` | `ADMIN`, `HR`, `PAYROLL`, `EMPLOYEE` (self only) | No | Retrieves employee record. Non-admins cannot view other employees' records. |
| `PUT` | `/api/employees/[eid]` | `ADMIN`, `HR` | Yes | Updates employee information with audit logging. |
| `DELETE` | `/api/employees/[eid]` | `ADMIN` | Yes | Deletes an employee record and records audit event. |
| `POST` | `/api/payroll/calculate` | `ADMIN`, `PAYROLL`, `EMPLOYEE` (self only) | Yes | Computes Philippine statutory payroll. Employees restricted to self calculation. |
| `GET` | `/api/timesheet` | Any authenticated | No | Fetches timesheet logs (employees restricted to own records). |
| `PUT` | `/api/timesheet` | Any authenticated | Yes | Clocks in or clocks out employee for current workday. |
| `GET` | `/api/leaves` | Any authenticated | No | Fetches leave records (filtered by role scope). |
| `POST` | `/api/leaves` | Any authenticated | Yes | Submits a validated leave request with Zod bounds checking. |

---

## 🚀 Local Development & Setup

### Prerequisites
- **Node.js**: `v20.x` or `v22.x`
- **npm**: `v10.x` or higher
- **PostgreSQL**: Neon Serverless database (or any standard PostgreSQL 15+ instance)

### Setup Instructions

```bash
# 1. Clone the repository
git clone https://github.com/j4ffyy/MotorPH-Enterprise-Payroll-System.git
cd MotorPH-Enterprise-Payroll-System/motorph-web

# 2. Install dependencies (auto-generates Prisma client via postinstall)
npm install

# 3. Configure environment variables
# Create a .env file in the motorph-web directory with:
# DATABASE_URL="postgresql://user:pass@ep-xyz-pooler.neon.tech/motorph?sslmode=require"
# DATABASE_URL_UNPOOLED="postgresql://user:pass@ep-xyz.neon.tech/motorph?sslmode=require"
# JWT_SECRET="your-secure-random-jwt-secret-key"

# 4. Synchronize database schema and seed employee roster
npx prisma db push
node prisma/seed.js

# 5. Run test verification suite
npm run test:all

# 6. Launch development server with Turbopack
npm run dev
```

Navigate to `http://localhost:3000` in your web browser.

---

## 👥 Team CTRL+ALT+ELITE

**Group 8 — Mapúa Malayan Digital College (MMDC)**  
- Advanced Object-Oriented Programming (AOOP)
- Information Assurance and Security 2 (IAS2)

---

## 📄 Academic License

This project was developed strictly for academic coursework, demonstration, and security auditing purposes.
