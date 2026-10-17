import jwt from 'jsonwebtoken';
import bcrypt from 'bcryptjs';
import { cookies } from 'next/headers';
import { prisma } from './prisma';

function getJwtSecret(): string {
  const secret = process.env.JWT_SECRET;
  if (!secret) {
    throw new Error(
      '[auth] JWT_SECRET environment variable is not set. ' +
        'Set it in .env (local) or Vercel Dashboard > Settings > Environment Variables.'
    );
  }
  return secret;
}

export const AUTH_COOKIE_NAME = 'motorph_session';

export interface UserSessionPayload {
  eid: number;
  username: string;
  name: string;
  role: string;
  designation?: string;
  department?: string;
}

export function signToken(payload: UserSessionPayload): string {
  return jwt.sign(payload, getJwtSecret(), { expiresIn: '8h' });
}

export function verifyToken(token: string): UserSessionPayload | null {
  try {
    return jwt.verify(token, getJwtSecret()) as UserSessionPayload;
  } catch {
    return null;
  }
}

export async function hashPassword(plainText: string): Promise<string> {
  return bcrypt.hash(plainText, 10);
}

export async function comparePassword(plainText: string, hashed: string): Promise<boolean> {
  return bcrypt.compare(plainText, hashed);
}

export async function getSession(): Promise<UserSessionPayload | null> {
  const cookieStore = await cookies();
  const token = cookieStore.get(AUTH_COOKIE_NAME)?.value;
  if (!token) return null;
  return verifyToken(token);
}

export async function getCurrentEmployee() {
  const session = await getSession();
  if (!session) return null;

  return prisma.employee.findUnique({
    where: { eid: session.eid },
    include: {
      designation: {
        include: { department: true },
      },
      supervisor: true,
      govIds: true,
      components: true,
      variables: true,
    },
  });
}
