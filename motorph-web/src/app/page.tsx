'use client';

import React, { useState } from 'react';
import { useRouter } from 'next/navigation';
import { Lock, UserCheck, ShieldAlert, ArrowRight, CheckCircle2 } from 'lucide-react';
import MotorPHLogo from '@/components/MotorPHLogo';

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
    <div className="min-h-screen bg-slate-50 flex flex-col justify-center items-center px-4 py-12 relative overflow-hidden">
      {/* Decorative subtle night-blue radial glow */}
      <div className="absolute top-0 left-1/2 -translate-x-1/2 w-[600px] h-[350px] bg-gradient-to-b from-[#4166F5]/10 to-transparent rounded-full blur-3xl pointer-events-none" />

      <div className="w-full max-w-md relative z-10">
        {/* Brand Header */}
        <div className="text-center mb-8">
          <div className="inline-flex items-center justify-center w-20 h-20 rounded-2xl shadow-xl shadow-[#4166F5]/20 mb-4 overflow-hidden">
            <MotorPHLogo size={80} />
          </div>
          <h1 className="text-3xl font-extrabold text-[#0F1E36] tracking-tight">
            Motor<span className="text-[#4166F5]">PH</span> Enterprise
          </h1>
          <p className="text-sm text-slate-500 mt-1">
            Payroll Management & Employee Self-Service Portal
          </p>
        </div>

        {/* Login Card - Crisp White */}
        <div className="bg-white border border-slate-200/90 rounded-2xl p-6 sm:p-8 shadow-xl shadow-slate-200/50">
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
                className="p-2 rounded-lg bg-slate-50 border border-slate-200 text-left hover:bg-slate-100 hover:border-slate-300 transition"
              >
                <div className="font-semibold text-rose-700">Admin (CEO)</div>
                <div className="text-[10px] text-slate-500 truncate">mgarcia@motorph.com</div>
              </button>
              <button
                type="button"
                onClick={() => setDemoRole('amvillanueva@motorph.com', '123abc')}
                className="p-2 rounded-lg bg-slate-50 border border-slate-200 text-left hover:bg-slate-100 hover:border-slate-300 transition"
              >
                <div className="font-semibold text-amber-700">HR Manager</div>
                <div className="text-[10px] text-slate-500 truncate">amvillanueva@motorph.com</div>
              </button>
              <button
                type="button"
                onClick={() => setDemoRole('ralvaro@motorph.com', '123abc')}
                className="p-2 rounded-lg bg-slate-50 border border-slate-200 text-left hover:bg-slate-100 hover:border-slate-300 transition"
              >
                <div className="font-semibold text-[#3456D4]">Payroll Head</div>
                <div className="text-[10px] text-slate-500 truncate">ralvaro@motorph.com</div>
              </button>
              <button
                type="button"
                onClick={() => setDemoRole('ratienza@motorph.com', '123abc')}
                className="p-2 rounded-lg bg-slate-50 border border-slate-200 text-left hover:bg-slate-100 hover:border-slate-300 transition"
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
