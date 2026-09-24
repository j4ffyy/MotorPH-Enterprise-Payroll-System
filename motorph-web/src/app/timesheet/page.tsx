'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import AppShell from '@/components/AppShell';
import { Clock, CheckCircle2, AlertCircle, LogOut, LogIn } from 'lucide-react';

// Format YYYY-MM-DD → MM/DD/YYYY
function formatDate(dateStr: string): string {
  if (!dateStr) return '—';
  const [year, month, day] = dateStr.split('-');
  return `${month}/${day}/${year}`;
}

// Format HH:MM:SS → H:MM AM/PM
function formatTime(timeStr: string): string {
  if (!timeStr) return '—';
  const [h, m] = timeStr.split(':').map(Number);
  const period = h >= 12 ? 'PM' : 'AM';
  const displayH = h % 12 || 12;
  return `${displayH}:${String(m).padStart(2, '0')} ${period}`;
}

// Calculate hours worked between two HH:MM:SS strings
function calcHoursWorked(inTime: string, outTime: string): string {
  if (!inTime || !outTime) return '—';
  const [inH, inM, inS] = inTime.split(':').map(Number);
  const [outH, outM, outS] = outTime.split(':').map(Number);
  const diff = (outH * 3600 + outM * 60 + outS) - (inH * 3600 + inM * 60 + inS);
  if (diff <= 0) return '—';
  const h = Math.floor(diff / 3600);
  const m = Math.floor((diff % 3600) / 60);
  return `${h}h ${m}m`;
}

export default function TimesheetPage() {
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [timesheets, setTimesheets] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [actionLoading, setActionLoading] = useState(false);
  const [viewAll, setViewAll] = useState(false);
  const [todayRecord, setTodayRecord] = useState<any>(null);
  const [toast, setToast] = useState<{ msg: string; type: 'success' | 'error' } | null>(null);

  useEffect(() => {
    fetchSession();
  }, []);

  const showToast = (msg: string, type: 'success' | 'error') => {
    setToast({ msg, type });
    setTimeout(() => setToast(null), 4000);
  };

  const fetchSession = async () => {
    try {
      const res = await fetch('/api/auth/me');
      const data = await res.json();
      if (!data.authenticated) { router.push('/'); return; }
      setCurrentUser(data.user);
      loadTimesheets(data.user, false);
    } catch {
      router.push('/');
    }
  };

  const loadTimesheets = async (user?: any, all: boolean = false) => {
    setLoading(true);
    try {
      const u = user || currentUser;
      const isManager = u?.role === 'ADMIN' || u?.role === 'HR';
      const res = await fetch(`/api/timesheet?all=${isManager && all}`);
      const data = await res.json();
      if (res.ok) {
        const records: any[] = data.timesheets || [];
        setTimesheets(records);
        // Detect today's record for current user (or active open shift)
        const d = new Date();
        const utcDate = d.toISOString().split('T')[0];
        const localDate = `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
        const tr = records.find(
          (r) => r.eid === u?.eid && (r.logDate === utcDate || r.logDate === localDate || !r.logOutTime)
        );
        setTodayRecord(tr || null);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleClockIn = async () => {
    setActionLoading(true);
    try {
      const res = await fetch('/api/timesheet', { method: 'POST' });
      const data = await res.json();
      if (!res.ok) { showToast(data.error || 'Clock-in failed', 'error'); return; }
      showToast(data.message || 'Clocked in!', 'success');
      loadTimesheets(currentUser, viewAll);
    } catch {
      showToast('Network error — please try again', 'error');
    } finally {
      setActionLoading(false);
    }
  };

  const handleClockOut = async () => {
    setActionLoading(true);
    try {
      const res = await fetch('/api/timesheet', { method: 'PUT' });
      const data = await res.json();
      if (!res.ok) { showToast(data.error || 'Clock-out failed', 'error'); return; }
      showToast(data.message || 'Clocked out!', 'success');
      loadTimesheets(currentUser, viewAll);
    } catch {
      showToast('Network error — please try again', 'error');
    } finally {
      setActionLoading(false);
    }
  };

  if (!currentUser) return null;

  const isManager = currentUser.role === 'ADMIN' || currentUser.role === 'HR';
  const hasClockedIn  = !!todayRecord;
  const hasClockedOut = !!todayRecord?.logOutTime;
  const colSpan = viewAll ? 7 : 6;

  return (
    <AppShell user={currentUser}>
      <div className="space-y-6">

        {/* Toast notification */}
        {toast && (
          <div
            className={`fixed top-20 right-6 z-50 px-5 py-3.5 rounded-xl shadow-lg text-sm font-medium flex items-center gap-2 transition-all ${
              toast.type === 'success'
                ? 'bg-emerald-600 text-white'
                : 'bg-rose-600 text-white'
            }`}
          >
            {toast.type === 'success' ? <CheckCircle2 size={16} /> : <AlertCircle size={16} />}
            {toast.msg}
          </div>
        )}

        {/* Header */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-[#0F1E36] flex items-center gap-2.5">
              <Clock className="text-[#4166F5]" size={26} />
              Timesheet &amp; Daily Time Records (DTR)
            </h1>
            <p className="text-xs text-slate-500 mt-1">
              Shift starts <strong>08:30 AM</strong>. Clock in daily and clock out when you leave.
            </p>
          </div>

          <div className="flex items-center gap-3 flex-wrap">
            {isManager && (
              <button
                onClick={() => { const n = !viewAll; setViewAll(n); loadTimesheets(currentUser, n); }}
                className={`px-3 py-2 rounded-xl text-xs font-medium border transition ${
                  viewAll
                    ? 'bg-[#0F1E36] text-white border-[#0F1E36]'
                    : 'bg-white text-slate-700 border-slate-200 hover:bg-slate-50 shadow-xs'
                }`}
              >
                {viewAll ? 'All Employees' : 'My Records Only'}
              </button>
            )}

            {/* Clock In button — hidden once clocked in */}
            {!hasClockedIn && (
              <button
                onClick={handleClockIn}
                disabled={actionLoading}
                className="flex items-center gap-2 px-4 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-500 text-white font-medium text-xs shadow-md shadow-emerald-600/20 transition disabled:opacity-60"
              >
                <LogIn size={15} />
                <span>{actionLoading ? 'Recording...' : 'Clock In'}</span>
              </button>
            )}

            {/* Clock Out button — shown once clocked in but not yet clocked out */}
            {hasClockedIn && !hasClockedOut && (
              <button
                onClick={handleClockOut}
                disabled={actionLoading}
                className="flex items-center gap-2 px-4 py-2 rounded-xl bg-rose-600 hover:bg-rose-500 text-white font-medium text-xs shadow-md shadow-rose-600/20 transition disabled:opacity-60"
              >
                <LogOut size={15} />
                <span>{actionLoading ? 'Recording...' : 'Clock Out'}</span>
              </button>
            )}

            {/* Done for the day */}
            {hasClockedIn && hasClockedOut && (
              <div className="flex items-center gap-2 px-4 py-2 rounded-xl bg-slate-100 text-slate-600 text-xs font-medium border border-slate-200">
                <CheckCircle2 size={15} className="text-emerald-500" />
                Done for today · {calcHoursWorked(todayRecord.logTime, todayRecord.logOutTime)}
              </div>
            )}
          </div>
        </div>

        {/* Today's status banner */}
        {hasClockedIn && (
          <div className={`p-4 rounded-2xl border flex items-center gap-3 text-sm ${
            hasClockedOut
              ? 'bg-emerald-50 border-emerald-200'
              : 'bg-[#EBF0FF] border-[#C7D3FF]'
          }`}>
            <Clock size={18} className={hasClockedOut ? 'text-emerald-600' : 'text-[#4166F5]'} />
            <div>
              <span className="font-semibold text-[#0F1E36]">
                {hasClockedOut ? 'Shift Complete' : 'Currently Clocked In'}
              </span>
              <span className="text-slate-500 ml-2 text-xs">
                In: {formatTime(todayRecord.logTime)}
                {hasClockedOut && (
                  <> &nbsp;·&nbsp; Out: {formatTime(todayRecord.logOutTime)} &nbsp;·&nbsp; Hours worked: {calcHoursWorked(todayRecord.logTime, todayRecord.logOutTime)}</>
                )}
              </span>
            </div>
          </div>
        )}

        {/* Timesheet Table */}
        <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse text-xs">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50 text-slate-600 font-semibold uppercase tracking-wider text-[10px]">
                  <th className="py-3.5 px-5">Log ID</th>
                  {viewAll && <th className="py-3.5 px-5">Employee</th>}
                  <th className="py-3.5 px-5">Date</th>
                  <th className="py-3.5 px-5">Clock In</th>
                  <th className="py-3.5 px-5">Clock Out</th>
                  <th className="py-3.5 px-5">Hours</th>
                  <th className="py-3.5 px-5">Status</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-slate-700">
                {loading ? (
                  <tr>
                    <td colSpan={colSpan} className="py-10 text-center text-slate-400 text-sm">
                      Loading attendance records...
                    </td>
                  </tr>
                ) : timesheets.length === 0 ? (
                  <tr>
                    <td colSpan={colSpan} className="py-10 text-center text-slate-400 text-sm">
                      No attendance records found. Clock in to start tracking.
                    </td>
                  </tr>
                ) : (
                  timesheets.map((ts) => (
                    <tr key={ts.attendanceId} className="hover:bg-slate-50/70 transition-colors">
                      <td className="py-3.5 px-5 text-slate-400 font-mono text-xs">#{ts.attendanceId}</td>
                      {viewAll && (
                        <td className="py-3.5 px-5 font-semibold text-[#0F1E36]">
                          {ts.employee ? `${ts.employee.lastName}, ${ts.employee.firstName}` : `EID: #${ts.eid}`}
                          <div className="text-[10px] text-slate-400 font-normal">{ts.employee?.designation?.name}</div>
                        </td>
                      )}

                      {/* Date — MM/DD/YYYY */}
                      <td className="py-3.5 px-5 text-slate-700 font-medium">{formatDate(ts.logDate)}</td>

                      {/* Clock In — H:MM AM/PM */}
                      <td className="py-3.5 px-5">
                        <span className="font-mono font-semibold text-[#0F1E36]">{formatTime(ts.logTime)}</span>
                      </td>

                      {/* Clock Out */}
                      <td className="py-3.5 px-5">
                        {ts.logOutTime ? (
                          <span className="font-mono font-semibold text-slate-700">{formatTime(ts.logOutTime)}</span>
                        ) : (
                          <span className="text-slate-400 italic text-[11px]">Not yet</span>
                        )}
                      </td>

                      {/* Hours worked */}
                      <td className="py-3.5 px-5">
                        {ts.logOutTime ? (
                          <span className="font-mono text-emerald-700 font-semibold">
                            {calcHoursWorked(ts.logTime, ts.logOutTime)}
                          </span>
                        ) : (
                          <span className="text-slate-400 text-[11px]">In progress</span>
                        )}
                      </td>

                      {/* Status badge */}
                      <td className="py-3.5 px-5">
                        <span
                          className={`inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[10px] font-semibold border ${
                            ts.attStatus === 'Present'
                              ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
                              : 'bg-amber-50 text-amber-700 border-amber-200'
                          }`}
                        >
                          {ts.attStatus === 'Present' ? <CheckCircle2 size={11} /> : <AlertCircle size={11} />}
                          {ts.attStatus}
                        </span>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </AppShell>
  );
}
