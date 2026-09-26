import type { NextConfig } from "next";

const nextConfig: NextConfig = {
  agentRules: false,

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
};

export default nextConfig;
