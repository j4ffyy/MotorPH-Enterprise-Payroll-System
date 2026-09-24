'use client';

import React, { useState, useEffect, useRef } from 'react';
import { useRouter } from 'next/navigation';
import { Lock, UserCheck, ShieldAlert, ArrowRight, CheckCircle2 } from 'lucide-react';
import MotorPHLogo from '@/components/MotorPHLogo';

// ─── Animated Canvas Background ────────────────────────────────────────────────
type ShapeType = 'square' | 'circle' | 'triangle' | 'cross' | 'hexagon' | 'diamond' | 'dash' | 'star';

interface Shape {
  x: number;
  y: number;
  size: number;
  type: ShapeType;
  vx: number;
  vy: number;
  baseVx: number;
  baseVy: number;
  rotation: number;
  rotSpeed: number;
  alpha: number;
  baseAlpha: number;
  highlighted: boolean;
}

function drawShape(ctx: CanvasRenderingContext2D, shape: Shape) {
  const { x, y, size, type, rotation, alpha, highlighted } = shape;
  ctx.save();
  ctx.globalAlpha = Math.min(1, alpha);
  ctx.strokeStyle = highlighted ? '#4166F5' : '#8e96a8';
  ctx.lineWidth = highlighted ? 3.5 : 2.5;
  ctx.lineCap = 'round';
  ctx.lineJoin = 'round';
  ctx.translate(x, y);
  ctx.rotate(rotation);

  switch (type) {
    case 'square':
      ctx.strokeRect(-size / 2, -size / 2, size, size);
      break;
    case 'circle':
      ctx.beginPath();
      ctx.arc(0, 0, size / 2, 0, Math.PI * 2);
      ctx.stroke();
      break;
    case 'triangle':
      ctx.beginPath();
      ctx.moveTo(0, -size / 2);
      ctx.lineTo(size / 2, size / 2);
      ctx.lineTo(-size / 2, size / 2);
      ctx.closePath();
      ctx.stroke();
      break;
    case 'cross':
      ctx.beginPath();
      ctx.moveTo(-size / 2, 0); ctx.lineTo(size / 2, 0);
      ctx.moveTo(0, -size / 2); ctx.lineTo(0, size / 2);
      ctx.stroke();
      break;
    case 'hexagon': {
      ctx.beginPath();
      for (let i = 0; i < 6; i++) {
        const a = (Math.PI / 3) * i - Math.PI / 6;
        const px = Math.cos(a) * (size / 2);
        const py = Math.sin(a) * (size / 2);
        i === 0 ? ctx.moveTo(px, py) : ctx.lineTo(px, py);
      }
      ctx.closePath();
      ctx.stroke();
      break;
    }
    case 'diamond':
      ctx.beginPath();
      ctx.moveTo(0, -size / 2);
      ctx.lineTo(size / 2, 0);
      ctx.lineTo(0, size / 2);
      ctx.lineTo(-size / 2, 0);
      ctx.closePath();
      ctx.stroke();
      break;
    case 'dash':
      ctx.beginPath();
      ctx.moveTo(-size / 2, -3); ctx.lineTo(size / 2, -3);
      ctx.moveTo(-size / 2, 3);  ctx.lineTo(size / 2, 3);
      ctx.stroke();
      break;
    case 'star': {
      ctx.beginPath();
      for (let i = 0; i < 10; i++) {
        const r = i % 2 === 0 ? size / 2 : size / 4;
        const a = (Math.PI / 5) * i - Math.PI / 2;
        const px = Math.cos(a) * r;
        const py = Math.sin(a) * r;
        i === 0 ? ctx.moveTo(px, py) : ctx.lineTo(px, py);
      }
      ctx.closePath();
      ctx.stroke();
      break;
    }
  }
  ctx.restore();
}

const REPEL_RADIUS = 110;   // px — how far the cursor influence reaches
const REPEL_FORCE  = 4.5;   // push strength
const DAMPING      = 0.92;  // how fast shapes slow back down

function AnimatedBackground() {
  const canvasRef = useRef<HTMLCanvasElement>(null);
  const shapesRef = useRef<Shape[]>([]);
  const rafRef    = useRef<number>(0);
  const mouseRef  = useRef<{ x: number; y: number }>({ x: -9999, y: -9999 });

  useEffect(() => {
    const canvas = canvasRef.current;
    if (!canvas) return;
    const ctx = canvas.getContext('2d');
    if (!ctx) return;

    const types: ShapeType[] = ['square', 'circle', 'triangle', 'cross', 'hexagon', 'diamond', 'dash', 'star'];
    const COUNT = 100;

    const resize = () => {
      canvas.width  = window.innerWidth;
      canvas.height = window.innerHeight;
    };
    resize();
    window.addEventListener('resize', resize);

    // Track mouse across the full window (canvas is pointer-events:none so we listen on window)
    const onMouse = (e: MouseEvent) => {
      mouseRef.current = { x: e.clientX, y: e.clientY };
    };
    const onLeave = () => {
      mouseRef.current = { x: -9999, y: -9999 };
    };
    window.addEventListener('mousemove', onMouse);
    window.addEventListener('mouseleave', onLeave);

    // Spawn shapes
    shapesRef.current = Array.from({ length: COUNT }, () => {
      const bvx = (Math.random() - 0.5) * 0.35;
      const bvy = (Math.random() - 0.5) * 0.35;
      const ba  = 0.18 + Math.random() * 0.27;
      return {
        x:           Math.random() * window.innerWidth,
        y:           Math.random() * window.innerHeight,
        size:        18 + Math.random() * 34,
        type:        types[Math.floor(Math.random() * types.length)],
        vx: bvx, vy: bvy,
        baseVx: bvx, baseVy: bvy,
        rotation:    Math.random() * Math.PI * 2,
        rotSpeed:    (Math.random() - 0.5) * 0.010,
        alpha: ba,   baseAlpha: ba,
        highlighted: false,
      };
    });

    const animate = () => {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      const mx = mouseRef.current.x;
      const my = mouseRef.current.y;

      for (const s of shapesRef.current) {
        // ── Cursor repulsion ──────────────────────────────────────
        const dx   = s.x - mx;
        const dy   = s.y - my;
        const dist = Math.sqrt(dx * dx + dy * dy);

        if (dist < REPEL_RADIUS && dist > 0.5) {
          // Push away from cursor, stronger when closer
          const force = (REPEL_RADIUS - dist) / REPEL_RADIUS * REPEL_FORCE;
          s.vx += (dx / dist) * force;
          s.vy += (dy / dist) * force;
          s.highlighted = true;
          s.alpha = Math.min(s.baseAlpha * 2.2, 0.95);
          s.rotSpeed = s.rotSpeed * 1.08;            // spin faster on touch
        } else {
          s.highlighted = false;
          // Gently drift back toward base velocity & alpha
          s.vx = s.vx * DAMPING + s.baseVx * (1 - DAMPING);
          s.vy = s.vy * DAMPING + s.baseVy * (1 - DAMPING);
          s.alpha += (s.baseAlpha - s.alpha) * 0.05;
        }

        // Speed cap so shapes don't fly off-screen too fast
        const speed = Math.sqrt(s.vx * s.vx + s.vy * s.vy);
        if (speed > 8) { s.vx = (s.vx / speed) * 8; s.vy = (s.vy / speed) * 8; }

        s.x += s.vx;
        s.y += s.vy;
        s.rotation += s.rotSpeed;

        // Wrap edges
        if (s.x < -60) s.x = canvas.width  + 60;
        if (s.x > canvas.width  + 60) s.x = -60;
        if (s.y < -60) s.y = canvas.height + 60;
        if (s.y > canvas.height + 60) s.y = -60;

        drawShape(ctx, s);
      }

      rafRef.current = requestAnimationFrame(animate);
    };

    animate();

    return () => {
      cancelAnimationFrame(rafRef.current);
      window.removeEventListener('resize', resize);
      window.removeEventListener('mousemove', onMouse);
      window.removeEventListener('mouseleave', onLeave);
    };
  }, []);

  return (
    <canvas
      ref={canvasRef}
      className="absolute inset-0 w-full h-full pointer-events-none"
      style={{ zIndex: 0 }}
    />
  );
}

// ─── Login Page ─────────────────────────────────────────────────────────────────
export default function LoginPage() {
  const router = useRouter();
  const [username, setUsername] = useState('mgarcia@motorph.com');
  const [password, setPassword] = useState('123abc');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const res = await fetch('/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username, password }),
      });

      const data = await res.json();
      if (!res.ok) {
        throw new Error(data.error || 'Authentication failed');
      }

      router.push('/dashboard');
      router.refresh();
    } catch (err: any) {
      setError(err.message || 'Failed to login');
      setLoading(false);
    }
  };

  const setDemoRole = (user: string, pass: string) => {
    setUsername(user);
    setPassword(pass);
    setError(null);
  };

  return (
    <div className="min-h-screen bg-white flex flex-col justify-center items-center px-4 py-12 relative overflow-hidden">
      {/* Animated floating geometric shapes */}
      <AnimatedBackground />

      {/* Soft radial glow behind the card */}
      <div className="absolute inset-0 pointer-events-none" style={{ zIndex: 1 }}>
        <div
          style={{
            position: 'absolute',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
            width: '700px',
            height: '500px',
            background: 'radial-gradient(ellipse at center, rgba(65,102,245,0.06) 0%, transparent 70%)',
            borderRadius: '50%',
          }}
        />
      </div>

      <div className="w-full max-w-md relative" style={{ zIndex: 2 }}>
        {/* Brand Header */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-20 h-20 rounded-2xl shadow-xl shadow-[#4166F5]/20 mb-4 overflow-hidden">
            <MotorPHLogo size={80} />
          </div>
          <h1 className="text-3xl font-extrabold text-[#0F1E36] tracking-tight">
            Motor<span className="text-[#4166F5]">PH</span> Enterprise
          </h1>
          <p className="text-sm text-slate-500 mt-1">
            Payroll Management &amp; Employee Self-Service Portal
          </p>
        </div>

        {/* Login Card */}
        <div className="bg-white/90 backdrop-blur-sm border border-slate-200/90 rounded-2xl p-6 sm:p-8 shadow-2xl shadow-slate-200/60">
          <h2 className="text-lg font-bold text-[#0F1E36] mb-1">Sign In to Your Account</h2>
          <p className="text-xs text-slate-500 mb-6">
            Access your payroll, timesheets, and employee self-service records.
          </p>

          {error && (
            <div className="mb-5 p-3.5 rounded-xl bg-rose-50 border border-rose-200 flex items-start gap-2.5 text-rose-700 text-xs">
              <ShieldAlert size={16} className="shrink-0 mt-0.5 text-rose-500" />
              <span>{error}</span>
            </div>
          )}

          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1.5">
                Work Email or Employee ID
              </label>
              <input
                type="text"
                required
                value={username}
                onChange={(e) => setUsername(e.target.value)}
                placeholder="e.g. mgarcia@motorph.com or 10001"
                className="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 placeholder:text-slate-400 text-sm focus:outline-none focus:border-[#4166F5] focus:bg-white focus:ring-2 focus:ring-[#C7D3FF] transition-all"
              />
            </div>

            <div>
              <label className="block text-xs font-semibold text-slate-700 mb-1.5">
                Password
              </label>
              <input
                type="password"
                required
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                className="w-full px-3.5 py-2.5 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 placeholder:text-slate-400 text-sm focus:outline-none focus:border-[#4166F5] focus:bg-white focus:ring-2 focus:ring-[#C7D3FF] transition-all"
              />
            </div>

            <button
              type="submit"
              disabled={loading}
              className="w-full py-2.5 px-4 rounded-xl bg-[#0F1E36] hover:bg-[#1A2E4E] text-white font-medium text-sm shadow-md shadow-[#0F1E36]/25 flex items-center justify-center gap-2 transition-all disabled:opacity-60 disabled:cursor-not-allowed mt-2"
            >
              {loading ? (
                <span>Authenticating...</span>
              ) : (
                <>
                  <span>Sign In</span>
                  <ArrowRight size={16} />
                </>
              )}
            </button>
          </form>

          {/* Role Switcher Demo Bar */}
          <div className="mt-6 pt-5 border-t border-slate-100">
            <span className="block text-[11px] font-semibold uppercase tracking-wider text-slate-400 mb-2">
              Instant Demo Roles (Click to Fill):
            </span>
            <div className="grid grid-cols-2 gap-2 text-xs">
              <button
                type="button"
                onClick={() => setDemoRole('mgarcia@motorph.com', '123abc')}
                className="p-2 rounded-lg bg-slate-50 border border-slate-200 text-left hover:bg-slate-100 hover:border-slate-300 transition cursor-pointer"
              >
                <div className="font-semibold text-rose-700">Admin (CEO)</div>
                <div className="text-[10px] text-slate-500 truncate">mgarcia@motorph.com</div>
              </button>
              <button
                type="button"
                onClick={() => setDemoRole('amvillanueva@motorph.com', '123abc')}
                className="p-2 rounded-lg bg-slate-50 border border-slate-200 text-left hover:bg-slate-100 hover:border-slate-300 transition cursor-pointer"
              >
                <div className="font-semibold text-amber-700">HR Manager</div>
                <div className="text-[10px] text-slate-500 truncate">amvillanueva@motorph.com</div>
              </button>
              <button
                type="button"
                onClick={() => setDemoRole('ralvaro@motorph.com', '123abc')}
                className="p-2 rounded-lg bg-slate-50 border border-slate-200 text-left hover:bg-slate-100 hover:border-slate-300 transition cursor-pointer"
              >
                <div className="font-semibold text-[#3456D4]">Payroll Head</div>
                <div className="text-[10px] text-slate-500 truncate">ralvaro@motorph.com</div>
              </button>
              <button
                type="button"
                onClick={() => setDemoRole('ratienza@motorph.com', '123abc')}
                className="p-2 rounded-lg bg-slate-50 border border-slate-200 text-left hover:bg-slate-100 hover:border-slate-300 transition cursor-pointer"
              >
                <div className="font-semibold text-emerald-700">Employee (ESS)</div>
                <div className="text-[10px] text-slate-500 truncate">ratienza@motorph.com</div>
              </button>
            </div>
          </div>
        </div>

        {/* Footer info */}
        <div className="mt-8 text-center text-xs text-slate-500">
          <p className="font-medium text-slate-600">MotorPH Enterprise Payroll System — TEAM CTRL+ALT+ELITE</p>
          <p className="text-[11px] text-slate-400 mt-1">
            Mapúa Malayan Digital College &bull; Term 1 AY 2026-2027
          </p>
        </div>
      </div>
    </div>
  );
}
