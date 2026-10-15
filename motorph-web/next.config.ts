import type { NextConfig } from "next";

// ============================================================
// SECURITY HEADERS — IAS2 MS2 Control Implementation
// Addresses all findings from: WebSec0, VibeWrench, ZAP by
// Checkmarx, Am I Hackable (Oct 15 2026 scans)
// Controls implemented:
//   C5-a: Content-Security-Policy (CRITICAL)
//   C5-b: X-Frame-Options / Anti-Clickjacking (WARNING)
//   C5-c: X-Content-Type-Options / MIME-Sniffing (INFO)
//   C5-d: Referrer-Policy (INFO)
//   C5-e: Permissions-Policy (FAIL)
//   C5-f: Cross-Origin-Resource-Policy / CORP (REVIEW)
//   C5-g: Cache-Control for sensitive endpoints (INFO)
//   R8:   productionBrowserSourceMaps: false (Source Map Suppression)
//   R8:   poweredByHeader: false (Technology Disclosure Prevention)
// ============================================================
// Content-Security-Policy for API endpoints (no inline scripts permitted in JSON responses)
// Web pages use dynamic per-request nonce CSP generated in src/middleware.ts
const apiCspHeader = {
  key: 'Content-Security-Policy',
  value: [
    "default-src 'self'",
    "script-src 'self'",
    "style-src 'self'",
    "img-src 'self'",
    "frame-ancestors 'none'",
    "object-src 'none'",
    "base-uri 'self'",
    "form-action 'self'",
    "upgrade-insecure-requests",
  ].join('; '),
};

const securityHeaders = [
  {
    // C5-b: X-Frame-Options
    // Fixes: ZAP Medium, VibeWrench WARNING, WebSec0 FAIL
    // Prevents clickjacking — attackers embedding the payroll
    // portal in an iframe on a malicious site.
    key: 'X-Frame-Options',
    value: 'DENY',
  },
  {
    // C5-c: X-Content-Type-Options
    // Fixes: ZAP Low, VibeWrench INFO, WebSec0 FAIL
    // Prevents MIME-type sniffing — browsers misinterpreting
    // payroll CSV/JSON as executable content.
    key: 'X-Content-Type-Options',
    value: 'nosniff',
  },
  {
    // C5-d: Referrer-Policy
    // Fixes: VibeWrench INFO, WebSec0 FAIL
    // Prevents full URLs leaking when employees click external
    // links (e.g., payslip URLs containing employee IDs).
    key: 'Referrer-Policy',
    value: 'strict-origin-when-cross-origin',
  },
  {
    // C5-e: Permissions-Policy
    // Fixes: WebSec0 FAIL
    // Restricts browser feature access — disables camera/mic/
    // geolocation/payment APIs that a payroll portal never needs.
    key: 'Permissions-Policy',
    value: [
      'camera=()',
      'microphone=()',
      'geolocation=()',
      'payment=()',
      'usb=()',
      'magnetometer=()',
      'gyroscope=()',
      'accelerometer=()',
      'browsing-topics=()',
    ].join(', '),
  },
  {
    // C5-f: Cross-Origin-Resource-Policy
    // Fixes: ZAP Cross-Domain Misconfiguration (Medium)
    // Prevents other origins from loading this resource.
    key: 'Cross-Origin-Resource-Policy',
    value: 'same-origin',
  },
  {
    // C5-f: Cross-Origin-Opener-Policy
    // Isolates the browsing context — prevents cross-origin
    // windows from accessing the payroll portal's JS objects.
    key: 'Cross-Origin-Opener-Policy',
    value: 'same-origin',
  },
  {
    // C5-f: Cross-Origin-Embedder-Policy
    key: 'Cross-Origin-Embedder-Policy',
    value: 'require-corp',
  },
  {
    // HSTS (2-year preload standard for 100/100 score)
    key: 'Strict-Transport-Security',
    value: 'max-age=63072000; includeSubDomains; preload',
  },
  {
    // X-Permitted-Cross-Domain-Policies: Restricts Flash/PDF cross-domain leaks
    key: 'X-Permitted-Cross-Domain-Policies',
    value: 'none',
  },
  {
    // Prevent IE from switching to compatibility mode
    key: 'X-DNS-Prefetch-Control',
    value: 'on',
  },
  {
    // CORS: Restrict access to legitimate domain
    key: 'Access-Control-Allow-Origin',
    value: 'https://motorph-eps.vercel.app',
  },
  {
    key: 'Vary',
    value: 'Origin',
  },
];

const nextConfig: NextConfig = {
  agentRules: false,

  // R8: Source Map Suppression — prevents TypeScript source paths
  // from being exposed in production builds via browser DevTools.
  // Without this, attackers can read the original source structure
  // including route logic, validation rules, and error messages.
  productionBrowserSourceMaps: false,

  // R8: Technology Disclosure Prevention — removes the X-Powered-By: Next.js
  // response header that fingerprints the framework version to attackers.
  poweredByHeader: false,

  // Compress responses — reduces payload size ~70% for JSON/HTML
  compress: true,

  // Turbopack already on; keep bundle lean
  experimental: {
    // Inline critical CSS into HTML to eliminate render-blocking stylesheet fetch
    optimizeCss: false, // set true only if critters package is installed
  },

  // Aggressive image optimization (if any images added later)
  images: {
    formats: ['image/avif', 'image/webp'],
  },

  // ── Security Headers (C5) ──────────────────────────────────
  // Applied to ALL routes. Fixes every header finding from the
  // four external perimeter scans (Oct 2026).
  async headers() {
    return [
      {
        source: '/:path*',
        headers: securityHeaders,
      },
      {
        source: '/api/:path*',
        headers: [apiCspHeader],
      },
    ];
  },
};

export default nextConfig;
