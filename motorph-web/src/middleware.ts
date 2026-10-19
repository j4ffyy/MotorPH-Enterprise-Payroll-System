import { NextRequest, NextResponse } from 'next/server';

export function middleware(request: NextRequest) {
  // Generate a cryptographically secure 128-bit random nonce encoded as base64
  const nonce = Buffer.from(crypto.randomUUID()).toString('base64');
  const isDev = process.env.NODE_ENV === 'development';

  // Build the Content-Security-Policy header with the dynamic per-request nonce
  // In development mode, React requires 'unsafe-eval' for callstack debugging and 'unsafe-inline' for style injection.
  // In production, 'unsafe-eval' and 'unsafe-inline' are strictly omitted for maximum security and OWASP ZAP compliance.
  const scriptSrcValues = [
    "'self'",
    `'nonce-${nonce}'`,
    "'strict-dynamic'",
    isDev ? "'unsafe-eval'" : '',
    'https://motorph-eps.vercel.app',
    'https://vercel.live',
  ]
    .filter(Boolean)
    .join(' ');

  const styleSrcValues = [
    "'self'",
    `'nonce-${nonce}'`,
    'https://fonts.googleapis.com',
    isDev ? "'unsafe-inline'" : '',
  ]
    .filter(Boolean)
    .join(' ');

  const cspHeader = [
    "default-src 'self'",
    `script-src ${scriptSrcValues}`,
    `style-src ${styleSrcValues}`,
    "img-src 'self' data: blob: https://motorph-eps.vercel.app",
    "font-src 'self' https://fonts.gstatic.com data:",
    "connect-src 'self' https://motorph-eps.vercel.app https://vitals.vercel-insights.com https://vercel.live",
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
