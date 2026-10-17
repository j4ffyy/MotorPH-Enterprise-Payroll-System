import { NextRequest, NextResponse } from 'next/server';

export function middleware(request: NextRequest) {
  // Generate a cryptographically secure 128-bit random nonce encoded as base64
  const nonce = Buffer.from(crypto.randomUUID()).toString('base64');
  const isDev = process.env.NODE_ENV === 'development';

  // Build the Content-Security-Policy header with the dynamic per-request nonce
  // In development mode, React requires 'unsafe-eval' for callstack debugging.
  // In production, 'unsafe-eval' is strictly omitted for maximum security.
  const scriptSrcValues = [
    "'self'",
    `'nonce-${nonce}'`,
    "'strict-dynamic'",
    isDev ? "'unsafe-eval'" : '',
    'https://*.vercel.app',
    'https://vercel.live',
  ]
    .filter(Boolean)
    .join(' ');

  const cspHeader = [
    "default-src 'self'",
    `script-src ${scriptSrcValues}`,
    "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com",
    "img-src 'self' data: blob: https:",
    "font-src 'self' https://fonts.gstatic.com data:",
    "connect-src 'self' https://*.neon.tech https://*.vercel-insights.com https://vitals.vercel-insights.com https://vercel.live wss://ws-us3.pusher.com",
    "frame-ancestors 'none'",
    "object-src 'none'",
    "base-uri 'self'",
    "form-action 'self'",
    'upgrade-insecure-requests',
  ].join('; ');

  // Clone request headers and pass both 'x-nonce' and 'content-security-policy'
  const requestHeaders = new Headers(request.headers);
  requestHeaders.set('x-nonce', nonce);
  requestHeaders.set('content-security-policy', cspHeader);

  const response = NextResponse.next({
    request: {
      headers: requestHeaders,
    },
  });

  // Attach CSP and security headers to the outgoing response
  response.headers.set('Content-Security-Policy', cspHeader);

  return response;
}

export const config = {
  matcher: [
    /*
     * Match all request paths except for:
     * - API routes (/api/*)
     * - Next.js static files (/_next/static/*)
     * - Image optimization (/_next/image/*)
     * - Static asset files (favicon.ico, robots.txt, etc.)
     */
    '/((?!api|_next/static|_next/image|favicon.ico|robots.txt|logo.svg).*)',
  ],
};
