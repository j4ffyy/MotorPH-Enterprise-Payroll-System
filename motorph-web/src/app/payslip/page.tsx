'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import AppShell from '@/components/AppShell';
import MotorPHLogo from '@/components/MotorPHLogo';
import {
  FileText,
  Printer,
  TrendingUp,
  ShieldCheck,
  Wallet,
  RefreshCw,
  Building2,
  Calendar,
  CheckCircle2,
} from 'lucide-react';
import { formatPHP } from '@/lib/payrollCalculator';

export default function PayslipPage() {
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [payslip, setPayslip] = useState<any | null>(null);
  const [loading, setLoading] = useState(false);
  const [periodStart, setPeriodStart] = useState('2026-01-01');
  const [periodEnd, setPeriodEnd] = useState('2026-01-31');

  useEffect(() => {
    fetchSession();
    // eslint-disable-next-line react-hooks/exhaustive-deps
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
      loadMyPayslip(data.user);
    } catch {
      router.push('/');
    }
  };

  const loadMyPayslip = async (user?: any) => {
    setLoading(true);
    try {
      const u = user || currentUser;
      const res = await fetch('/api/payroll/calculate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          periodStart,
          periodEnd,
          eid: u?.eid,
        }),
      });

      const data = await res.json();
      if (res.ok && data.payrolls && data.payrolls.length > 0) {
        setPayslip(data.payrolls[0]);
      } else {
        setPayslip(null);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  if (!currentUser) return null;

  return (
    <AppShell user={currentUser}>
      <div className="space-y-6 pb-16">
        {/* Header Bar */}
        <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4 print:hidden">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <span className="text-[10px] font-bold uppercase tracking-widest text-[#4166F5] px-2.5 py-0.5 rounded-full bg-[#EBF0FF] border border-[#C7D3FF]">
                Official Payroll Record
              </span>
              <span className="text-xs text-slate-400">&bull;</span>
              <span className="text-xs text-slate-500 font-medium">Confidential Employee Payslip</span>
            </div>
            <h1 className="text-2xl font-extrabold text-[#0F1E36] tracking-tight flex items-center gap-2.5">
              <FileText className="text-[#4166F5]" size={26} />
              My Payslip
            </h1>
            <p className="text-xs text-slate-500 mt-1">
              Individual earnings breakdown, statutory government contributions, and net take-home pay.
            </p>
          </div>

          {/* Period selector & print actions */}
          <div className="flex items-center gap-2 bg-white p-2 rounded-2xl border border-slate-200 shadow-xs flex-wrap">
            <div className="flex items-center gap-1.5 px-2.5 py-1.5 bg-slate-50 rounded-xl border border-slate-200 text-xs">
              <Calendar size={13} className="text-slate-400" />
              <span className="text-[10px] uppercase font-bold text-slate-400">Cutoff:</span>
              <input
                type="date"
                value={periodStart}
                onChange={(e) => setPeriodStart(e.target.value)}
                className="bg-transparent text-xs text-slate-800 focus:outline-none font-medium"
              />
              <span className="text-slate-400 font-bold">&ndash;</span>
              <input
                type="date"
                value={periodEnd}
                onChange={(e) => setPeriodEnd(e.target.value)}
                className="bg-transparent text-xs text-slate-800 focus:outline-none font-medium"
              />
            </div>

            <button
              onClick={() => loadMyPayslip()}
              disabled={loading}
              className="flex items-center gap-1.5 px-3.5 py-2 rounded-xl bg-[#0F1E36] hover:bg-[#1A2E4E] text-white font-medium text-xs shadow-xs transition disabled:opacity-60 cursor-pointer"
            >
              <RefreshCw size={13} className={loading ? 'animate-spin' : ''} />
              <span>{loading ? 'Refreshing...' : 'Update'}</span>
            </button>

            <button
              onClick={() => window.print()}
              className="flex items-center gap-1.5 px-3.5 py-2 rounded-xl bg-emerald-600 hover:bg-emerald-700 text-white font-medium text-xs shadow-xs transition cursor-pointer"
            >
              <Printer size={13} />
              <span>Print Slip</span>
            </button>
          </div>
        </div>

        {/* Quick KPI Overview Cards */}
        {payslip && (
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4 print:hidden">
            {/* Net Pay Card */}
            <div className="bg-gradient-to-br from-emerald-500/10 via-white to-white p-5 rounded-2xl border border-emerald-200 shadow-xs relative overflow-hidden group">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-emerald-800 uppercase tracking-wider">
                  Net Take-Home Pay
                </span>
                <div className="w-9 h-9 rounded-xl bg-emerald-100 text-emerald-700 flex items-center justify-center">
                  <Wallet size={18} />
                </div>
              </div>
              <div className="mt-3 text-2xl font-black text-emerald-700 font-mono tracking-tight">
                {formatPHP(payslip.netPay)}
              </div>
              <div className="flex items-center gap-1.5 text-[11px] text-emerald-600 mt-1.5">
                <CheckCircle2 size={12} />
                <span>Credited to bank disbursement account</span>
              </div>
            </div>

            {/* Total Gross Earnings */}
            <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs relative overflow-hidden group">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                  Gross Earnings
                </span>
                <div className="w-9 h-9 rounded-xl bg-blue-50 text-[#4166F5] flex items-center justify-center">
                  <TrendingUp size={18} />
                </div>
              </div>
              <div className="mt-3 text-2xl font-black text-[#0F1E36] font-mono tracking-tight">
                {formatPHP((payslip.grossIncome || 0) + (payslip.totalBenefits || 0))}
              </div>
              <div className="flex items-center gap-1.5 text-[11px] text-slate-500 mt-1.5">
                <span>Base: {formatPHP(payslip.basicSalary)} + Allowances</span>
              </div>
            </div>

            {/* Total Deductions */}
            <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs relative overflow-hidden group">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                  Total Deductions
                </span>
                <div className="w-9 h-9 rounded-xl bg-rose-50 text-rose-600 flex items-center justify-center">
                  <ShieldCheck size={18} />
                </div>
              </div>
              <div className="mt-3 text-2xl font-black text-rose-600 font-mono tracking-tight">
                &minus;{formatPHP(payslip.totalDeductions)}
              </div>
              <div className="flex items-center gap-1.5 text-[11px] text-slate-500 mt-1.5">
                <span>Statutory benefits + TRAIN withholding tax</span>
              </div>
            </div>
          </div>
        )}

        {/* Printable Official Payslip Sheet */}
        {payslip ? (
          <div className="bg-white border border-slate-200 rounded-3xl p-6 sm:p-10 shadow-sm print:shadow-none print:border-none print:p-0 max-w-4xl mx-auto">
            {/* Payslip Header */}
            <div className="text-center pb-6 border-b border-slate-200 print:border-black">
              <div className="flex justify-center mb-3">
                <MotorPHLogo size={48} />
              </div>
              <h2 className="text-xl font-black tracking-wider uppercase text-[#0F1E36] print:text-black">
                MotorPH Corporation
              </h2>
              <p className="text-xs text-slate-500 print:text-gray-600">
                7 Jupiter Avenue, Makati Commercial Center, Metro Manila &bull; Tel: (02) 8812-4500
              </p>
              <div className="inline-block mt-2 px-3 py-1 rounded-full bg-[#EBF0FF] print:bg-transparent border border-[#C7D3FF] print:border-black">
                <p className="text-xs font-bold text-[#3456D4] print:text-black uppercase tracking-wider">
                  Official Employee Payroll Statement
                </p>
              </div>
            </div>

            {/* Employee Information Section */}
            <div className="grid grid-cols-1 sm:grid-cols-2 gap-4 py-5 text-xs border-b border-slate-200 print:border-black bg-slate-50/50 print:bg-transparent rounded-2xl px-5 my-4">
              <div className="space-y-1.5">
                <div>
                  <span className="text-slate-500 print:text-gray-600 font-medium">Employee Name:</span>{' '}
                  <span className="font-extrabold text-[#0F1E36] print:text-black text-sm">
                    {payslip.name}
                  </span>
                </div>
                <div>
                  <span className="text-slate-500 print:text-gray-600 font-medium">Position:</span>{' '}
                  <strong className="text-slate-800 print:text-black">{payslip.designation}</strong>
                </div>
                <div>
                  <span className="text-slate-500 print:text-gray-600 font-medium">Department:</span>{' '}
                  <span className="text-slate-800 print:text-black">{payslip.department}</span>
                </div>
                <div>
                  <span className="text-slate-500 print:text-gray-600 font-medium">Role:</span>{' '}
                  <span className="px-2 py-0.5 rounded-md bg-slate-200/80 text-slate-800 font-bold text-[10px] uppercase">
                    {currentUser.role}
                  </span>
                </div>
              </div>

              <div className="sm:text-right space-y-1.5">
                <div>
                  <span className="text-slate-500 print:text-gray-600 font-medium">Employee ID:</span>{' '}
                  <span className="font-mono font-extrabold text-[#0F1E36] print:text-black text-sm">
                    #{payslip.eid}
                  </span>
                </div>
                <div>
                  <span className="text-slate-500 print:text-gray-600 font-medium">Payroll Cutoff:</span>{' '}
                  <strong className="text-slate-800 print:text-black">
                    {payslip.periodStart} to {payslip.periodEnd}
                  </strong>
                </div>
                <div>
                  <span className="text-slate-500 print:text-gray-600 font-medium">Disbursement Date:</span>{' '}
                  <span className="text-slate-800 print:text-black">
                    {new Date().toLocaleDateString('en-US', { month: 'short', day: 'numeric', year: 'numeric' })}
                  </span>
                </div>
                <div>
                  <span className="text-slate-500 print:text-gray-600 font-medium">Hourly Base Rate:</span>{' '}
                  <span className="font-mono text-slate-800 print:text-black">
                    {formatPHP(payslip.hourlyRate)}/hr
                  </span>
                </div>
              </div>
            </div>

            {/* Earnings & Deductions Columns */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6 py-4 text-xs">
              {/* Earnings Column */}
              <div className="p-4 rounded-2xl bg-emerald-50/30 border border-emerald-100 print:border-black print:bg-white">
                <h4 className="font-extrabold uppercase tracking-wider text-emerald-800 print:text-black pb-2 border-b border-emerald-200/60 print:border-black flex items-center justify-between">
                  <span>1. Earnings &amp; Allowances</span>
                  <span className="text-[10px] text-emerald-600 font-mono">Amount (PHP)</span>
                </h4>
                <div className="mt-3 space-y-2 font-mono">
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">Basic Monthly Salary</span>
                    <span className="text-slate-900 font-semibold">{formatPHP(payslip.basicSalary)}</span>
                  </div>
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">
                      Overtime Pay ({payslip.overtimeHours || 0} hrs)
                    </span>
                    <span className="text-slate-900">{formatPHP(payslip.overtimePay || 0)}</span>
                  </div>
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">Rice Subsidy</span>
                    <span className="text-slate-900">{formatPHP(payslip.riceSubsidy || 0)}</span>
                  </div>
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">Phone Allowance</span>
                    <span className="text-slate-900">{formatPHP(payslip.phoneAllowance || 0)}</span>
                  </div>
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">Clothing Allowance</span>
                    <span className="text-slate-900">{formatPHP(payslip.clothingAllowance || 0)}</span>
                  </div>
                  <div className="flex justify-between pt-2 border-t-2 border-emerald-300 font-bold text-[#0F1E36] text-sm">
                    <span className="font-sans">Total Gross Earnings:</span>
                    <span>{formatPHP((payslip.grossIncome || 0) + (payslip.totalBenefits || 0))}</span>
                  </div>
                </div>
              </div>

              {/* Deductions Column */}
              <div className="p-4 rounded-2xl bg-rose-50/30 border border-rose-100 print:border-black print:bg-white">
                <h4 className="font-extrabold uppercase tracking-wider text-rose-800 print:text-black pb-2 border-b border-rose-200/60 print:border-black flex items-center justify-between">
                  <span>2. Statutory Deductions</span>
                  <span className="text-[10px] text-rose-600 font-mono">Amount (PHP)</span>
                </h4>
                <div className="mt-3 space-y-2 font-mono">
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">SSS Contribution (RA 11199)</span>
                    <span className="text-slate-900 font-semibold">{formatPHP(payslip.sssContribution)}</span>
                  </div>
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">PhilHealth (UHC Act 2.5%)</span>
                    <span className="text-slate-900 font-semibold">{formatPHP(payslip.philhealthContribution)}</span>
                  </div>
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">Pag-IBIG / HDMF (Circ. 460)</span>
                    <span className="text-slate-900 font-semibold">{formatPHP(payslip.pagibigContribution)}</span>
                  </div>
                  <div className="flex justify-between py-1 border-b border-slate-100">
                    <span className="text-slate-600 print:text-gray-700 font-sans">Withholding Tax (TRAIN Law)</span>
                    <span className="text-slate-900 font-semibold">{formatPHP(payslip.withholdingTax)}</span>
                  </div>
                  <div className="flex justify-between pt-2 border-t-2 border-rose-300 font-bold text-rose-700 print:text-black text-sm">
                    <span className="font-sans">Total Deductions:</span>
                    <span>&minus;{formatPHP(payslip.totalDeductions)}</span>
                  </div>
                </div>
              </div>
            </div>

            {/* Net Pay Grand Highlight Box */}
            <div className="mt-4 p-5 rounded-2xl bg-gradient-to-r from-emerald-50 via-white to-emerald-50 border-2 border-emerald-300 print:border-black flex flex-col sm:flex-row items-center justify-between gap-3 shadow-xs">
              <div>
                <span className="text-xs uppercase font-extrabold text-emerald-800 print:text-gray-700 block">
                  Net Take-Home Pay
                </span>
                <span className="text-[11px] text-slate-500 print:text-gray-500">
                  Total Gross Earnings &minus; Total Deductions
                </span>
              </div>
              <div className="text-3xl font-black font-mono text-emerald-700 print:text-black tracking-tight">
                {formatPHP(payslip.netPay)}
              </div>
            </div>

            {/* Signatures */}
            <div className="mt-10 pt-8 border-t border-slate-200 print:border-black grid grid-cols-2 gap-8 text-center text-xs">
              <div>
                <div className="w-56 mx-auto border-b border-slate-400 print:border-black mb-1.5"></div>
                <span className="text-slate-500 print:text-black font-medium">Prepared By: Finance / Payroll Officer</span>
              </div>
              <div>
                <div className="w-56 mx-auto border-b border-slate-400 print:border-black mb-1.5"></div>
                <span className="text-slate-500 print:text-black font-medium">Received By: {payslip.name}</span>
              </div>
            </div>
          </div>
        ) : (
          <div className="bg-white border border-slate-200 rounded-3xl p-12 text-center">
            {loading ? (
              <div className="flex flex-col items-center justify-center gap-3">
                <RefreshCw size={24} className="animate-spin text-[#4166F5]" />
                <p className="text-sm font-semibold text-slate-600">Calculating your official payslip...</p>
              </div>
            ) : (
              <div>
                <FileText size={36} className="mx-auto text-slate-300 mb-2" />
                <p className="text-sm font-semibold text-slate-700">No payroll record found for this cutoff.</p>
                <p className="text-xs text-slate-400 mt-1">Select a valid date range and click Update.</p>
              </div>
            )}
          </div>
        )}
      </div>
    </AppShell>
  );
}
