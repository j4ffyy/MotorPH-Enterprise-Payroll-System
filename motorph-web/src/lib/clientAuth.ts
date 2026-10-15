'use client';

export interface ClientUser {
  eid: number;
  name: string;
  username: string;
  role: string;
  designation?: string;
  department?: string;
}

const STORAGE_KEY = 'motorph_cached_user';

export function getCachedUser(): ClientUser | null {
  if (typeof window === 'undefined') return null;
  try {
    const raw = sessionStorage.getItem(STORAGE_KEY);
    return raw ? JSON.parse(raw) : null;
  } catch {
    return null;
  }
}

export function setCachedUser(user: ClientUser | null | undefined) {
  if (typeof window === 'undefined') return;
  try {
    if (user) {
      sessionStorage.setItem(STORAGE_KEY, JSON.stringify(user));
    } else {
      sessionStorage.removeItem(STORAGE_KEY);
    }
  } catch {}
}

export function clearCachedUser() {
  if (typeof window === 'undefined') return;
  try {
    sessionStorage.removeItem(STORAGE_KEY);
  } catch {}
}
