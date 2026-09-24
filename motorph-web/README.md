# MotorPH Enterprise Payroll System

**Live:** https://motorph-eps.vercel.app

A full-stack web payroll system built with **Next.js 16**, **Prisma**, and **Neon PostgreSQL** for Team CTRL+ALT+ELITE (Group 8) — MO-IT101 / AOOP.

See the [root README](../README.md) for full documentation.

## Quick Start

```bash
npm install
npm run dev
```

Open [http://localhost:3000](http://localhost:3000).

## Scripts

| Command | Description |
|---------|-------------|
| `npm run dev` | Start development server (Turbopack) |
| `npm run build` | Build for production |
| `npm test` | Run 19-test statutory compliance suite |
| `npm run lint` | Run ESLint |

## Environment Variables

Create a `.env` file in `motorph-web/`:

```env
DATABASE_URL=<neon-pooled-connection-string>
DATABASE_URL_UNPOOLED=<neon-direct-connection-string>
JWT_SECRET=<strong-random-secret>
```
