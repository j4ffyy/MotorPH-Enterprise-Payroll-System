'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import AppShell from '@/components/AppShell';
import {
  CalendarCheck,
  Plus,
  CheckCircle2,
  XCircle,
  Clock,
  Calendar,
} from 'lucide-react';

export default function LeavesPage() {
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [leaves, setLeaves] = useState<any[]>([]);
  const [loading, setLoading] = useState(true);
  const [showApplyModal, setShowApplyModal] = useState(false);

  // Form state
  const [form, setForm] = useState({
    dateFrom: '',
    dateTo: '',
    reasonForLeave: 'Vacation Leave',
  });

  useEffect(() => {
    fetchSession();
  }, []);

  const fetchSession = async () => {
    try {
      const res = await fetch('/api/auth/me');
      const data = await res.json();
      if (!data.authenticated) {
        router.push('/');
        return;
      }
      setCurrentUser(data.user);
      loadLeaves(data.user);
    } catch {
      router.push('/');
    }
  };

  const loadLeaves = async (user?: any) => {
    setLoading(true);
    try {
      const u = user || currentUser;
      const isManager = u?.role === 'ADMIN' || u?.role === 'HR';
      const res = await fetch(`/api/leaves?all=${isManager}`);
      const data = await res.json();
      if (res.ok) {
        setLeaves(data.leaves || []);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const handleApply = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await fetch('/api/leaves', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(form),
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || 'Failed to submit application');

      setShowApplyModal(false);
      setForm({ dateFrom: '', dateTo: '', reasonForLeave: 'Vacation Leave' });
      loadLeaves();
      alert('Leave application submitted successfully!');
    } catch (err: any) {
      alert(err.message || 'Error submitting leave');
    }
  };

  const handleStatusUpdate = async (leaveId: string, leaveStatus: 'Approved' | 'Rejected') => {
    try {
      const res = await fetch('/api/leaves', {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ leaveId, leaveStatus }),
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || 'Failed to update status');

      loadLeaves();
    } catch (err: any) {
      alert(err.message || 'Error updating status');
    }
  };

  if (!currentUser) return null;

  const isManager = currentUser.role === 'ADMIN' || currentUser.role === 'HR';

  return (
    <AppShell user={currentUser}>
      <div className="space-y-6">
        {/* Header Bar */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-[#0F1E36] flex items-center gap-2.5">
              <CalendarCheck className="text-[#4166F5]" size={26} />
              Leave Management
            </h1>
            <p className="text-xs text-slate-500 mt-1">
              {isManager
                ? 'Review and manage employee leave requests and company PTO.'
                : 'File leave applications and track approval statuses.'}
            </p>
          </div>

          <button
            onClick={() => setShowApplyModal(true)}
            className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-[#0F1E36] hover:bg-[#1A2E4E] text-white font-medium text-xs shadow-md shadow-[#0F1E36]/20 transition-all self-start sm:self-auto"
          >
            <Plus size={16} />
            <span>Apply for Leave</span>
          </button>
        </div>

        {/* Leave Requests Table - Clean White */}
        <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse text-xs">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50 text-slate-600 font-semibold uppercase tracking-wider text-[10px]">
                  <th className="py-3 px-4">Leave ID</th>
                  {isManager && <th className="py-3 px-4">Employee</th>}
                  <th className="py-3 px-4">Filed Date</th>
                  <th className="py-3 px-4">Duration (From - To)</th>
                  <th className="py-3 px-4">Reason / Type</th>
                  <th className="py-3 px-4">Status</th>
                  {isManager && <th className="py-3 px-4 text-right">Approval Actions</th>}
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-slate-700">
                {loading ? (
                  <tr>
                    <td colSpan={isManager ? 7 : 5} className="py-8 text-center text-slate-400">
                      Loading leave records...
                    </td>
                  </tr>
                ) : leaves.length === 0 ? (
                  <tr>
                    <td colSpan={isManager ? 7 : 5} className="py-8 text-center text-slate-400">
                      No leave requests on record.
                    </td>
                  </tr>
                ) : (
                  leaves.map((l) => (
                    <tr key={l.leaveId} className="hover:bg-slate-50 transition">
                      <td className="py-3 px-4 font-mono font-medium text-slate-500">
                        {l.leaveId}
                      </td>
                      {isManager && (
                        <td className="py-3 px-4 font-medium text-[#0F1E36]">
                          {l.employee ? `${l.employee.lastName}, ${l.employee.firstName}` : `EID: #${l.eid}`}
                          <div className="text-[10px] text-slate-500 font-normal">
                            {l.employee?.designation?.name}
                          </div>
                        </td>
                      )}
                      <td className="py-3 px-4 text-slate-500">{l.dateFiled || 'N/A'}</td>
                      <td className="py-3 px-4 font-semibold text-slate-800">
                        {l.dateFrom} &rarr; {l.dateTo}
                      </td>
                      <td className="py-3 px-4 text-slate-700">{l.reasonForLeave}</td>
                      <td className="py-3 px-4">
                        <span
                          className={`inline-flex items-center gap-1 px-2.5 py-0.5 rounded-full text-[10px] font-semibold border ${
                            l.leaveStatus === 'Approved'
                              ? 'bg-emerald-50 text-emerald-700 border-emerald-200'
                              : l.leaveStatus === 'Rejected'
                              ? 'bg-rose-50 text-rose-700 border-rose-200'
                              : 'bg-amber-50 text-amber-700 border-amber-200'
                          }`}
                        >
                          {l.leaveStatus === 'Approved' && <CheckCircle2 size={12} />}
                          {l.leaveStatus === 'Rejected' && <XCircle size={12} />}
                          {l.leaveStatus === 'Pending' && <Clock size={12} />}
                          <span>{l.leaveStatus}</span>
                        </span>
                      </td>
                      {isManager && (
                        <td className="py-3 px-4 text-right">
                          {l.leaveStatus === 'Pending' ? (
                            <div className="flex items-center justify-end gap-1.5">
                              <button
                                onClick={() => handleStatusUpdate(l.leaveId, 'Approved')}
                                className="px-2.5 py-1 rounded-lg bg-emerald-50 hover:bg-emerald-600 text-emerald-700 hover:text-white border border-emerald-200 text-[11px] font-medium transition"
                              >
                                Approve
                              </button>
                              <button
                                onClick={() => handleStatusUpdate(l.leaveId, 'Rejected')}
                                className="px-2.5 py-1 rounded-lg bg-rose-50 hover:bg-rose-600 text-rose-700 hover:text-white border border-rose-200 text-[11px] font-medium transition"
                              >
                                Reject
                              </button>
                            </div>
                          ) : (
                            <span className="text-[11px] text-slate-400">Processed</span>
                          )}
                        </td>
                      )}
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Leave Application Modal - Clean White */}
        {showApplyModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-xs">
            <div className="bg-white border border-slate-200 rounded-2xl w-full max-w-md p-6 shadow-2xl">
              <div className="flex items-center justify-between pb-4 border-b border-slate-100">
                <h3 className="text-base font-bold text-[#0F1E36] flex items-center gap-2">
                  <Calendar size={18} className="text-[#4166F5]" />
                  File Leave Application
                </h3>
                <button
                  onClick={() => setShowApplyModal(false)}
                  className="p-1 rounded-lg text-slate-400 hover:text-slate-700"
                >
                  <XCircle size={20} />
                </button>
              </div>

              <form onSubmit={handleApply} className="mt-4 space-y-4 text-xs">
                <div>
                  <label className="text-slate-600 font-medium block mb-1">Leave Category / Reason *</label>
                  <select
                    value={form.reasonForLeave}
                    onChange={(e) => setForm({ ...form, reasonForLeave: e.target.value })}
                    className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                  >
                    <option value="Vacation Leave">Vacation Leave (VL)</option>
                    <option value="Medical Leave">Medical / Sick Leave (SL)</option>
                    <option value="Personal Time Off">Personal Time Off (PTO)</option>
                    <option value="Bereavement Leave">Bereavement Leave</option>
                    <option value="Compensatory Time Off">Compensatory Time Off</option>
                    <option value="Leave Without Pay">Leave Without Pay (LWOP)</option>
                  </select>
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">Start Date *</label>
                    <input
                      type="date"
                      required
                      value={form.dateFrom}
                      onChange={(e) => setForm({ ...form, dateFrom: e.target.value })}
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">End Date *</label>
                    <input
                      type="date"
                      required
                      value={form.dateTo}
                      onChange={(e) => setForm({ ...form, dateTo: e.target.value })}
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                </div>

                <div className="pt-4 flex justify-end gap-2 border-t border-slate-100">
                  <button
                    type="button"
                    onClick={() => setShowApplyModal(false)}
                    className="px-4 py-2 rounded-xl bg-slate-100 text-slate-700 hover:bg-slate-200 font-medium"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-2 rounded-xl bg-[#0F1E36] hover:bg-[#1A2E4E] text-white font-medium shadow-md shadow-[#0F1E36]/20"
                  >
                    Submit Application
                  </button>
                </div>
              </form>
            </div>
          </div>
        )}
      </div>
    </AppShell>
  );
}
