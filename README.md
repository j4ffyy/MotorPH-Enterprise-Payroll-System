<div align="center">

<img src="https://img.shields.io/badge/MotorPH-Enterprise%20Payroll%20System-4166F5?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIyNCIgaGVpZ2h0PSIyNCIgdmlld0JveD0iMCAwIDI0IDI0IiBmaWxsPSJub25lIiBzdHJva2U9IiNmZmZmZmYiIHN0cm9rZS13aWR0aD0iMiI+PHBhdGggZD0iTTEyIDJMMiA3bDEwIDUgMTAtNS0xMC01eiIvPjxwYXRoIGQ9Ik0yIDE3bDEwIDUgMTAtNSIvPjxwYXRoIGQ9Ik0yIDEybDEwIDUgMTAtNSIvPjwvc3ZnPg==" alt="MotorPH EPS"/>

# MotorPH Enterprise Payroll System

**A full-stack, enterprise-grade payroll management web application**
built for the MO-IT101 / AOOP course — Team **CTRL+ALT+ELITE** (Group 8)

[![Next.js](https://img.shields.io/badge/Next.js-16.3-black?logo=nextdotjs)](https://nextjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Prisma](https://img.shields.io/badge/Prisma-ORM-2D3748?logo=prisma)](https://www.prisma.io/)
[![SQLite](https://img.shields.io/badge/SQLite-Database-003B57?logo=sqlite)](https://sqlite.org/)
[![Tests](https://img.shields.io/badge/Tests-19%20passing-22c55e?logo=node.js&logoColor=white)](#testing)

</div>

---

## Overview

The **MotorPH Enterprise Payroll System (EPS)** is a web-based rewrite of the legacy MotorPH Java desktop application. It automates payroll computation, timesheet tracking, leave management, and employee directory management — all compliant with **2025/2026 Philippine statutory regulations** (SSS, PhilHealth, Pag-IBIG, BIR TRAIN Law).

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Framework | [Next.js 16](https://nextjs.org/) (App Router, Server Components) |
| Language | TypeScript 5 (strict mode) |
| ORM | [Prisma](https://prisma.io/) |
| Database | SQLite (`dev.db`) |
| Auth | JWT via `jsonwebtoken` + HTTP-only cookies |
| Password Hashing | `bcryptjs` (10 salt rounds) |
| Icons | `lucide-react` |
| Date Utilities | `date-fns` |
| Testing | Node.js 22 native test runner (`node:test`) |

---

## Features

### Modules
| Module | Description |
|--------|-------------|
| 🏠 **Dashboard** | Live KPI cards — headcount, payroll cycle, semi-monthly rate, attendance summary |
| 👥 **Employee Directory** | Full roster of 34 employees with search, department filter, and modal detail view |
| 💰 **Payroll** | Auto-computed payroll per employee: gross earnings, statutory deductions, net take-home |
| 🕐 **Timesheet** | Daily clock-in / clock-out with attendance history |
| 🏖️ **Leave Management** | Leave request submission and approval workflow |

### Payroll Compliance (2025/2026)
| Contribution | Rate | Cap |
|---|---|---|
| SSS (RA 11199) | 5% employee share | ₱1,500.00 @ ₱30,000 MSC |
| PhilHealth (UHC Act) | 2.5% employee share | Floor ₱250 · Ceiling ₱2,500 |
| Pag-IBIG / HDMF (Circ. 460) | 2% | ₱200.00 max |
| BIR TRAIN Law (RA 10963) | 0% – 35% brackets | Monthly taxable income |

### Security Controls
| Control | Implementation |
|---------|---------------|
| Authentication | JWT with HTTP-only cookies (15-min expiry) |
| Authorization | Role-based access: `HR_ADMIN`, `PAYROLL_ADMIN`, `EMPLOYEE` |
| Password Storage | BCrypt (10 rounds) — no plaintext storage |
| Input Validation | Strict TypeScript types + Prisma schema constraints |

---

## Project Structure

```
G8-TA-AOOP/
└── motorph-web/               # Next.js web application
    ├── prisma/
    │   ├── schema.prisma      # Database schema
    │   ├── seed.js            # 34-employee seed data
    │   └── dev.db             # SQLite database
    ├── src/
    │   ├── app/
    │   │   ├── api/           # REST API routes
    │   │   ├── dashboard/     # Dashboard page
    │   │   ├── employees/     # Employee directory
    │   │   ├── payroll/       # Payroll computation table
    │   │   ├── timesheet/     # Time tracking
    │   │   └── leaves/        # Leave management
    │   ├── components/
    │   │   └── AppShell.tsx   # Sidebar navigation shell
    │   └── lib/
    │       ├── auth.ts        # JWT session management
    │       ├── payrollCalculator.ts  # Statutory computation engine
    │       └── prisma.ts      # Prisma client singleton
    └── tests/
        └── payrollCalculator.test.ts  # 19-test statutory parity suite
```

---

## Getting Started

### Prerequisites
- Node.js 22+
- npm 10+

### Installation

```bash
# 1. Navigate to the web app directory
cd motorph-web

# 2. Install dependencies
npm install

# 3. Generate Prisma client
npx prisma generate

# 4. Run database migrations and seed all 34 employees
npx prisma migrate dev --name init
node prisma/seed.js

# 5. Start the development server
npm run dev
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

### Default Login Credentials

| Role | Username | Password |
|------|----------|----------|
| HR Admin | `garcia.manuel` | `password` |
| Employee | `reyes.sofia` | `password` |

> Passwords are stored as BCrypt hashes — never in plaintext.

---

## Testing

This project includes a **Control 4 unit test suite** for Philippine statutory compliance, run using Node.js 22's native test runner (no external test framework needed):

```bash
npm test
```

**Test coverage:**

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

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| `POST` | `/api/auth/login` | Public | Login and receive JWT cookie |
| `POST` | `/api/auth/logout` | Any | Clear session |
| `GET` | `/api/auth/me` | Any | Get current session |
| `GET` | `/api/employees` | HR_ADMIN | List all employees |
| `GET` | `/api/employees/[eid]` | HR_ADMIN | Get employee by ID |
| `POST` | `/api/payroll/calculate` | PAYROLL_ADMIN | Compute payroll for all or one employee |
| `GET` | `/api/timesheet` | Any | Get timesheet records |
| `PUT` | `/api/timesheet` | Any | Clock in / Clock out |
| `GET` | `/api/leaves` | Any | Get leave requests |
| `POST` | `/api/leaves` | Any | Submit leave request |

---

## Team

**CTRL+ALT+ELITE** — Group 8

> MO-IT101 / Advanced Object-Oriented Programming (AOOP)
> Mapúa Malayan Digital College

---

## License

This project is for academic purposes only.
