'use client';

import React, { useState } from 'react';
import { Clock, CheckCircle2 } from 'lucide-react';
import { useRouter } from 'next/navigation';

interface DashboardInteractiveProps {
  hasClockedInToday: boolean;
  clockInTime?: string;
}

export default function DashboardInteractive({
  hasClockedInToday: initialClockedIn,
  clockInTime: initialTime,
}: DashboardInteractiveProps) {
  const router = useRouter();
  const [clockedIn, setClockedIn] = useState(initialClockedIn);
  const [time, setTime] = useState(initialTime || '');
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<string | null>(null);

  const handleClockIn = async () => {
    setLoading(true);
    setMessage(null);
    try {
      const res = await fetch('/api/timesheet', { method: 'POST' });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || 'Failed to clock in');

      setClockedIn(true);
      setTime(data.log.logTime);
      setMessage(data.message);
      router.refresh();
    } catch (err: any) {
      alert(err.message || 'Error recording attendance');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="flex flex-col sm:items-end gap-2">
      {clockedIn ? (
        <div className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-emerald-500/20 border border-emerald-400/30 text-emerald-200 text-xs">
          <CheckCircle2 size={16} className="text-emerald-300" />
          <span>Clocked in today at <strong className="font-mono text-white">{time}</strong></span>
        </div>
      ) : (
        <button
          onClick={handleClockIn}
          disabled={loading}
          className="flex items-center gap-2 px-5 py-2.5 rounded-xl bg-white hover:bg-slate-100 text-[#0F1E36] font-semibold text-xs shadow-md transition-all disabled:opacity-60"
        >
          <Clock size={16} className="text-[#4166F5]" />
          <span>{loading ? 'Recording...' : 'Clock In for Today'}</span>
        </button>
      )}

      {message && (
        <span className="text-[11px] text-cyan-300 font-medium">{message}</span>
      )}
    </div>
  );
}
