import { NextResponse } from 'next/server';
import { getSession, getCurrentEmployee } from '@/lib/auth';

export async function GET() {
  const session = await getSession();
  if (!session) {
    return NextResponse.json({ authenticated: false }, { status: 401 });
  }

  const employee = await getCurrentEmployee();
  return NextResponse.json({
    authenticated: true,
    user: session,
    employee,
  });
}
