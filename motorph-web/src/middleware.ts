import { NextRequest, NextResponse } from 'next/server';

export function middleware(request: NextRequest) {
  // Generate a cryptographically secure 128-bit random nonce encoded as base64
  const nonce = Buffer.from(crypto.randomUUID()).toString('base64');

  // Build the Content-Security-Policy header with the dynamic per-request nonce
  // Notice: 'unsafe-inline' is completely omitted for scripts.
  // 'strict-dynamic' instructs modern browsers to trust scripts loaded by the nonced root script.
  const cspHeader = [
    "default-src 'self'",
    `script-src 'self' 'nonce-${nonce}' 'strict-dynamic' https://*.vercel.app https://vercel.live`,
    "style-src 'self' 'unsafe-inline' https://fonts.googleapis.com",
    "img-src 'self' data: blob: https:",
    "font-src 'self' https://fonts.gstatic.com data:",
    "connect-src 'self' https://*.neon.tech https://*.vercel-insights.com https://vitals.vercel-insights.com https://vercel.live wss://ws-us3.pusher.com",
    "frame-ancestors 'none'",
    "object-src 'none'",
    "base-uri 'self'",
    "form-action 'self'",
    "upgrade-insecure-requests",
  ].join('; ');

  // Clone request headers and pass both 'x-nonce' and 'content-security-policy'
  // Next.js App Router reads 'x-nonce' and automatically applies the nonce attribute
  // to every internal script tag it emits for streaming hydration and bundling.
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
    {
      source: '/((?!api|_next/static|_next/image|favicon.ico|robots.txt|.*\\.(?:svg|png|jpg|jpeg|gif|webp|ico|txt)$).*)',
      missing: [
        { type: 'header', key: 'next-router-prefetch' },
        { type: 'header', key: 'purpose', value: 'prefetch' },
      ],
    },
  ],
};
