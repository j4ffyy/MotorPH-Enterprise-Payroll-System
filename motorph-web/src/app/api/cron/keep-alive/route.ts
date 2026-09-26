import { NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';

// Lightweight keep-alive ping — called by Vercel Cron every 4 minutes
// Prevents Neon serverless compute from suspending (cold start = 1–3s delay)
export async function GET() {
  try {
    await prisma.$queryRaw`SELECT 1`;
    return NextResponse.json({ ok: true, ts: new Date().toISOString() });
  } catch {
    return NextResponse.json({ ok: false }, { status: 500 });
  }
}
