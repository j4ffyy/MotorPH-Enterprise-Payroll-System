import { NextResponse } from 'next/server';
import { prisma } from '@/lib/prisma';
import { comparePassword, signToken, AUTH_COOKIE_NAME } from '@/lib/auth';

export async function POST(request: Request) {
  try {
    const body = await request.json();
    const { username, password } = body;

    if (!username || !password) {
      return NextResponse.json(
        { error: 'Username/Email and password are required' },
        { status: 400 }
      );
    }

    // Find employee by username or email
    const employee = await prisma.employee.findFirst({
      where: {
        OR: [
          { username: username.trim() },
          { eid: Number(username) || -1 },
        ],
      },
      include: {
        designation: {
          include: { department: true },
        },
      },
    });

    if (!employee) {
      return NextResponse.json(
        { error: 'Invalid username or password' },
        { status: 401 }
      );
    }

    if (!employee.isActive) {
      return NextResponse.json(
        { error: 'Account is deactivated. Please contact HR.' },
        { status: 403 }
      );
    }

    // Verify bcrypt password hash
    const isValid = await comparePassword(password, employee.password);
    if (!isValid) {
      return NextResponse.json(
        { error: 'Invalid username or password' },
        { status: 401 }
      );
    }

    // Sign session JWT
    const payload = {
      eid: employee.eid,
      username: employee.username || '',
      name: `${employee.firstName} ${employee.lastName}`,
      role: employee.role,
      designation: employee.designation?.name,
      department: employee.designation?.department?.name,
    };

    const token = signToken(payload);

    const response = NextResponse.json({
      success: true,
      user: payload,
    });

    // Set secure HTTP-only cookie
    response.cookies.set({
      name: AUTH_COOKIE_NAME,
      value: token,
      httpOnly: true,
      secure: process.env.NODE_ENV === 'production',
      sameSite: 'lax',
      path: '/',
      maxAge: 8 * 60 * 60, // 8 hours
    });

    return response;
  } catch (error) {
    console.error('Login error:', error);
    return NextResponse.json(
      { error: 'An unexpected authentication error occurred.' },
      { status: 500 }
    );
  }
}
