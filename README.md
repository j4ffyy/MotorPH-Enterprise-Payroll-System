<div align="center">

<img src="https://img.shields.io/badge/MotorPH-Enterprise%20Payroll%20System-4166F5?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIiBzdHJva2U9IiNmZmZmZmYiIHN0cm9rZS13aWR0aD0iMiI+PHBhdGggZD0iTTEyIDJMMiA3bDEwIDUgMTAtNS0xMC01eiIvPjxwYXRoIGQ9Ik0yIDE3bDEwIDUgMTAtNSIvPjxwYXRoIGQ9Ik0yIDEybDEwIDUgMTAtNSIvPjwvc3ZnPg==" alt="MotorPH EPS"/>

# MotorPH Enterprise Payroll System

**A full-stack, enterprise-grade payroll management web application**
built for the MO-IT101 / AOOP course — Team **CTRL+ALT+ELITE** (Group 8)

[![Next.js](https://img.shields.io/badge/Next.js-16.3-black?logo=nextdotjs)](https://nextjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Prisma](https://img.shields.io/badge/Prisma-ORM-2D3748?logo=prisma)](https://www.prisma.io/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-336791?logo=postgresql&logoColor=white)](https://neon.tech/)
[![Tests](https://img.shields.io/badge/Tests-19%20passing-22c55e?logo=node.js&logoColor=white)](#testing)
[![Deployed on Vercel](https://img.shields.io/badge/Deployed-Vercel-000000?logo=vercel)](https://motorph-eps.vercel.app)

</div>

---

## 🌐 Live Demo

> **Production URL:** https://motorph-eps.vercel.app

### Demo Login Credentials

| Role | Username | Password | Access Level |
|------|----------|----------|--------------|
| **CEO / Admin** | `garcia.manuel` | `admin123` | Full system access — Payroll Engine, all employees |
| **HR Manager** | `lim.antonio` | `hr123` | Employee directory, My Payslip, Leave approvals |
| **Payroll Officer** | `san-jose.bianca` | `payroll123` | Payroll Engine, My Payslip |
| **Employee** | `reyes.sofia` | `123abc` | My Payslip, Timesheet, Leave requests |

> Passwords are stored as BCrypt hashes (10 salt rounds) — never in plaintext.

---

## Overview

The **MotorPH Enterprise Payroll System (EPS)** is a web-based rewrite of the legacy MotorPH Java desktop application. It automates payroll computation, timesheet tracking, leave management, and employee directory management — all compliant with **2025/2026 Philippine statutory regulations** (SSS, PhilHealth, Pag-IBIG, BIR TRAIN Law).

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | [Next.js 16](https://nextjs.org/) (App Router, Server Components, Turbopack) |
| Language | TypeScript 5 (strict mode) |
| ORM | [Prisma 6](https://prisma.io/) |
| Database | [Neon PostgreSQL](https://neon.tech/) (cloud-hosted, serverless) |
| Auth | JWT via `jsonwebtoken` + HTTP-only cookies (15-min expiry) |
| Password Hashing | `bcryptjs` (10 salt rounds) |
| Icons | `lucide-react` |
| Date Utilities | `date-fns` |
| Testing | Node.js 22 native test runner (`node:test`) — 19 tests, 0 external deps |
| Deployment | [Vercel](https://vercel.com/) (auto-deploy on push to `main`) |

---

## Features

### Modules & Role-Based Access

| Module | ADMIN | PAYROLL | HR | EMPLOYEE |
|--------|:-----:|:-------:|:--:|:--------:|
| 🏠 **Dashboard** — KPI cards, headcount, payroll cycle | ✅ | ✅ | ✅ | ✅ |
| 👥 **Employee Directory** — full roster, search, add/view | ✅ | ❌ | ✅ | ❌ |
| 🧮 **Payroll Engine** — workforce-wide computation & bank remittance | ✅ | ✅ | ❌ | ❌ |
| 📄 **My Payslip** — individual official payslip (printable) | ✅ | ✅ | ✅ | ✅ |
| ⏱️ **Timesheet / DTR** — clock-in/out, attendance history | ✅ | ✅ | ✅ | ✅ |
| 🏖️ **Leave Requests** — submit and approve leave | ✅ | ✅ | ✅ | ✅ |

### Payroll Compliance (2025/2026)

| Contribution | Rate | Cap |
|---|---|---|
| SSS (RA 11199) | 5% employee share | ₱1,500.00 @ ₱30,000 MSC |
| PhilHealth (UHC Act) | 2.5% employee share | Floor ₱250 · Ceiling ₱2,500 |
| Pag-IBIG / HDMF (Circ. 460) | 2% | ₱200.00 max |
| BIR TRAIN Law (RA 10963) | 0% – 35% brackets | Monthly taxable income |

### Security Controls

| Control | Implementation |
|---------|----------------|
| Authentication | JWT with HTTP-only cookies (15-min expiry) |
| Authorization | Role-based access: `ADMIN`, `HR`, `PAYROLL`, `EMPLOYEE` |
| Separation of Duties | HR/EMPLOYEE cannot access company Payroll Engine — redirected to personal My Payslip |
| Password Storage | BCrypt (10 rounds) — no plaintext storage |
| API Protection | Server-side session validation on every API route |
| Input Validation | Strict TypeScript types + Prisma schema constraints |
| EID Integrity | Auto-incremented Employee ID (server-side `MAX(eid) + 1`) with DB duplicate check |

---

## Project Structure

```
G8-TA-AOOP/
└── motorph-web/                   # Next.js web application
    ├── prisma/
    │   ├── schema.prisma          # PostgreSQL database schema
    │   ├── seed.js                # 34-employee seed data
    │   └── prisma.config.ts       # Neon connection config
    ├── src/
    │   ├── app/
    │   │   ├── api/               # REST API routes
    │   │   │   ├── auth/          # login, logout, me
    │   │   │   ├── employees/     # CRUD for employees
    │   │   │   ├── payroll/       # Payroll calculation engine
    │   │   │   ├── timesheet/     # Clock-in/out
    │   │   │   └── leaves/        # Leave management
    │   │   ├── dashboard/         # Dashboard page
    │   │   ├── employees/         # Employee directory
    │   │   ├── payroll/           # Payroll Engine (ADMIN & PAYROLL only)
    │   │   ├── payslip/           # Individual My Payslip (all roles)
    │   │   ├── timesheet/         # Time tracking
    │   │   └── leaves/            # Leave management
    │   ├── components/
    │   │   ├── AppShell.tsx       # Role-aware sidebar navigation
    │   │   └── MotorPHLogo.tsx    # Brand logo component
    │   └── lib/
    │       ├── auth.ts            # JWT session management
    │       ├── payrollCalculator.ts  # Statutory computation engine
    │       └── prisma.ts          # Prisma client singleton
    ├── public/
    │   └── logo.svg               # MotorPH motorcycle SVG logo
    └── tests/
        └── payrollCalculator.test.ts  # 19-test statutory parity suite
```

---

## Getting Started (Local Development)

### Prerequisites
- Node.js 22+
- npm 10+
- A [Neon](https://neon.tech/) PostgreSQL project (or use the `.env` from your team)

### Installation

```bash
# 1. Navigate to the web app directory
cd motorph-web

# 2. Install dependencies (also auto-runs prisma generate via postinstall)
npm install

# 3. Copy environment variables
# Create a .env file with:
# DATABASE_URL=<your-neon-pooled-connection-string>
# DATABASE_URL_UNPOOLED=<your-neon-direct-connection-string>
# JWT_SECRET=<any-strong-secret>

# 4. Push schema to database and seed all 34 employees
npx prisma db push
node prisma/seed.js

# 5. Start the development server
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

---

## Testing

This project includes a **Control 4 unit test suite** for Philippine statutory compliance, using Node.js 22's native test runner (no external test framework required):

```bash
npm test
```

**Test coverage — 19 / 19 passing ✅**

| Suite | Tests |
|-------|-------|
| SSS Statutory Contribution (RA 11199) | 3 |
| PhilHealth Contribution (UHC Act) | 3 |
| Pag-IBIG / HDMF (Circular No. 460) | 2 |
| BIR TRAIN Law Withholding Tax (RA 10963) | 4 |
| Rate Derivations & Overtime | 3 |
| Full Payroll Integration | 2 |
| BCrypt Password Hashing (Security Control 1) | 2 |
| **Total** | **19 / 19 ✅** |

---

## API Endpoints

| Method | Endpoint | Required Role | Description |
|--------|----------|---------------|-------------|
| `POST` | `/api/auth/login` | Public | Login and receive JWT cookie |
| `POST` | `/api/auth/logout` | Any | Clear session |
| `GET` | `/api/auth/me` | Any | Get current session |
| `GET` | `/api/employees` | ADMIN, HR | List all employees |
| `POST` | `/api/employees` | ADMIN, HR | Add new employee (EID auto-assigned) |
| `GET` | `/api/employees/[eid]` | ADMIN, HR, PAYROLL | Get employee by ID |
| `PUT` | `/api/employees/[eid]` | ADMIN, HR | Update employee |
| `DELETE` | `/api/employees/[eid]` | ADMIN | Delete employee |
| `POST` | `/api/payroll/calculate` | ADMIN, PAYROLL (own EID only for HR/EMPLOYEE) | Compute payroll |
| `GET` | `/api/timesheet` | Any (filtered by role) | Get timesheet records |
| `PUT` | `/api/timesheet` | Any | Clock in / Clock out |
| `GET` | `/api/leaves` | Any (filtered by role) | Get leave requests |
| `POST` | `/api/leaves` | Any | Submit leave request |

---

## Deployment

The app is deployed on **Vercel** with automatic deployments on every push to `main`.

**Required Vercel Environment Variables:**

| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | Neon pooled connection string |
| `DATABASE_URL_UNPOOLED` | Neon direct (unpooled) connection string |
| `JWT_SECRET` | Strong random secret for JWT signing |

> The `postinstall` script automatically runs `prisma generate` during Vercel builds.

---

## Team

**CTRL+ALT+ELITE** — Group 8

> Mapúa Malayan Digital College

---

## License

This project is for academic purposes only.
