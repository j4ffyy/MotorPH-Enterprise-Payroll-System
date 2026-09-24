'use client';

import React, { useState, useEffect, useMemo } from 'react';
import { useRouter } from 'next/navigation';
import AppShell from '@/components/AppShell';
import {
  Calculator,
  Search,
  Filter,
  FileText,
  Printer,
  XCircle,
  TrendingUp,
  ShieldCheck,
  Wallet,
  ArrowUpRight,
  ArrowDownRight,
  User,
  Building2,
  Receipt,
  Download,
  CheckCircle2,
  RefreshCw,
} from 'lucide-react';
import { formatPHP } from '@/lib/payrollCalculator';

export default function PayrollPage() {
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [payrolls, setPayrolls] = useState<any[]>([]);
  const [loading, setLoading] = useState(false);
  const [periodStart, setPeriodStart] = useState('2026-01-01');
  const [periodEnd, setPeriodEnd] = useState('2026-01-31');
  const [activePayslip, setActivePayslip] = useState<any | null>(null);

  // Search and filter state
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedDept, setSelectedDept] = useState('ALL');
  const [sortBy, setSortBy] = useState<'eid' | 'name' | 'gross' | 'net'>('eid');

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
      loadPayroll(data.user);
    } catch {
      router.push('/');
    }
  };

  const loadPayroll = async (user?: any) => {
    setLoading(true);
    try {
      const u = user || currentUser;
      const res = await fetch('/api/payroll/calculate', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          periodStart,
          periodEnd,
          eid: u?.role === 'EMPLOYEE' ? u.eid : undefined,
        }),
      });

      const data = await res.json();
      if (res.ok) {
        setPayrolls(data.payrolls || []);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  // Distinct departments for filter tabs
  const departments = useMemo(() => {
    const depts = Array.from(new Set(payrolls.map((p) => p.department).filter(Boolean)));
    return ['ALL', ...depts];
  }, [payrolls]);

  // Filtered & sorted payroll records
  const filteredPayrolls = useMemo(() => {
    return payrolls
      .filter((p) => {
        const matchesDept = selectedDept === 'ALL' || p.department === selectedDept;
        const q = searchQuery.toLowerCase().trim();
        const matchesSearch =
          !q ||
          p.name?.toLowerCase().includes(q) ||
          p.designation?.toLowerCase().includes(q) ||
          String(p.eid).includes(q);
        return matchesDept && matchesSearch;
      })
      .sort((a, b) => {
        if (sortBy === 'name') return (a.name || '').localeCompare(b.name || '');
        if (sortBy === 'gross') return (b.grossIncome || 0) - (a.grossIncome || 0);
        if (sortBy === 'net') return (b.netPay || 0) - (a.netPay || 0);
        return a.eid - b.eid;
      });
  }, [payrolls, selectedDept, searchQuery, sortBy]);

  // Aggregated totals for filtered view
  const totals = useMemo(() => {
    return filteredPayrolls.reduce(
      (acc, p) => ({
        basicSalary: acc.basicSalary + (p.basicSalary || 0),
        totalBenefits: acc.totalBenefits + (p.totalBenefits || 0),
        grossIncome: acc.grossIncome + (p.grossIncome || 0),
        sss: acc.sss + (p.sssContribution || 0),
        philhealth: acc.philhealth + (p.philhealthContribution || 0),
        pagibig: acc.pagibig + (p.pagibigContribution || 0),
        tax: acc.tax + (p.withholdingTax || 0),
        totalDeductions: acc.totalDeductions + (p.totalDeductions || 0),
        netPay: acc.netPay + (p.netPay || 0),
      }),
      {
        basicSalary: 0,
        totalBenefits: 0,
        grossIncome: 0,
        sss: 0,
        philhealth: 0,
        pagibig: 0,
        tax: 0,
        totalDeductions: 0,
        netPay: 0,
      }
    );
  }, [filteredPayrolls]);

  // Overall totals for KPI cards
  const totalDisbursement = payrolls.reduce((acc, p) => acc + (p.netPay || 0), 0);
  const totalTax = payrolls.reduce((acc, p) => acc + (p.withholdingTax || 0), 0);
  const totalStatutory = payrolls.reduce(
    (acc, p) =>
      acc + (p.sssContribution || 0) + (p.philhealthContribution || 0) + (p.pagibigContribution || 0),
    0
  );

  // Helper for initials
  const getInitials = (name: string) => {
    if (!name) return 'MP';
    const parts = name.split(',').map((p) => p.trim());
    if (parts.length >= 2) {
      return `${parts[1][0] || ''}${parts[0][0] || ''}`.toUpperCase();
    }
    return name.slice(0, 2).toUpperCase();
  };

  if (!currentUser) return null;

  return (
    <AppShell user={currentUser}>
      <div className="space-y-6 pb-12">
        {/* Header Bar */}
        <div className="flex flex-col lg:flex-row lg:items-center justify-between gap-4">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <span className="text-[10px] font-bold uppercase tracking-widest text-[#4166F5] px-2.5 py-0.5 rounded-full bg-[#EBF0FF] border border-[#C7D3FF]">
                Philippine Labor &amp; Tax Compliant
              </span>
              <span className="text-xs text-slate-400">&bull;</span>
              <span className="text-xs text-slate-500 font-medium">TRAIN Law (RA 10963)</span>
            </div>
            <h1 className="text-2xl font-extrabold text-[#0F1E36] tracking-tight flex items-center gap-2.5">
              <Calculator className="text-[#4166F5]" size={26} />
              Payroll Calculation Engine
            </h1>
            <p className="text-xs text-slate-500 mt-1">
              Real-time statutory tax schedules, allowances, deductions, and automated net disbursement.
            </p>
          </div>

          {/* Period selector & action */}
          <div className="flex items-center gap-2 bg-white p-2 rounded-2xl border border-slate-200 shadow-sm flex-wrap">
            <div className="flex items-center gap-1.5 px-2 py-1 bg-slate-50 rounded-xl border border-slate-200 text-xs">
              <span className="text-[10px] uppercase font-bold text-slate-400">Period</span>
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
              onClick={() => loadPayroll()}
              disabled={loading}
              className="flex items-center gap-1.5 px-4 py-2 rounded-xl bg-[#0F1E36] hover:bg-[#1A2E4E] text-white font-medium text-xs shadow-sm shadow-[#0F1E36]/20 transition disabled:opacity-60 cursor-pointer"
            >
              <RefreshCw size={13} className={loading ? 'animate-spin' : ''} />
              <span>{loading ? 'Recalculating...' : 'Recalculate'}</span>
            </button>
          </div>
        </div>

        {/* Aggregate KPI Summary Cards */}
        {currentUser.role !== 'EMPLOYEE' && (
          <div className="grid grid-cols-1 sm:grid-cols-3 gap-4">
            {/* Net Disbursement */}
            <div className="bg-white p-5 rounded-2xl border border-slate-200/90 shadow-sm hover:shadow-md transition relative overflow-hidden group">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                  Total Net Disbursement
                </span>
                <div className="w-9 h-9 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center shadow-xs">
                  <Wallet size={18} />
                </div>
              </div>
              <div className="mt-3 text-2xl font-black text-emerald-700 font-mono tracking-tight">
                {formatPHP(totalDisbursement)}
              </div>
              <div className="flex items-center gap-1.5 text-[11px] text-slate-500 mt-1.5">
                <span className="w-2 h-2 rounded-full bg-emerald-500"></span>
                <span>Ready for bank remittance &bull; {payrolls.length} Employees</span>
              </div>
            </div>

            {/* Withholding Tax */}
            <div className="bg-white p-5 rounded-2xl border border-slate-200/90 shadow-sm hover:shadow-md transition relative overflow-hidden group">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                  Total Withholding Tax
                </span>
                <div className="w-9 h-9 rounded-xl bg-blue-50 text-[#4166F5] flex items-center justify-center shadow-xs">
                  <TrendingUp size={18} />
                </div>
              </div>
              <div className="mt-3 text-2xl font-black text-[#4166F5] font-mono tracking-tight">
                {formatPHP(totalTax)}
              </div>
              <div className="flex items-center gap-1.5 text-[11px] text-slate-500 mt-1.5">
                <span className="w-2 h-2 rounded-full bg-[#4166F5]"></span>
                <span>BIR Form 1601-C schedule</span>
              </div>
            </div>

            {/* Statutory Deductions */}
            <div className="bg-white p-5 rounded-2xl border border-slate-200/90 shadow-sm hover:shadow-md transition relative overflow-hidden group">
              <div className="flex items-center justify-between">
                <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                  Total Statutory Contributions
                </span>
                <div className="w-9 h-9 rounded-xl bg-amber-50 text-amber-600 flex items-center justify-center shadow-xs">
                  <ShieldCheck size={18} />
                </div>
              </div>
              <div className="mt-3 text-2xl font-black text-amber-600 font-mono tracking-tight">
                {formatPHP(totalStatutory)}
              </div>
              <div className="flex items-center gap-1.5 text-[11px] text-slate-500 mt-1.5">
                <span className="w-2 h-2 rounded-full bg-amber-500"></span>
                <span>SSS, PhilHealth, &amp; Pag-IBIG remittance</span>
              </div>
            </div>
          </div>
        )}

        {/* Enhanced Compensation Breakdown & Deductions Table Container */}
        <div className="bg-white border border-slate-200 rounded-3xl overflow-hidden shadow-sm shadow-slate-200/50">
          {/* Table Header & Controls Bar */}
          <div className="p-5 sm:p-6 border-b border-slate-200 bg-gradient-to-r from-slate-50/80 via-white to-slate-50/40 flex flex-col gap-4">
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-3">
              <div>
                <div className="flex items-center gap-2.5">
                  <div className="w-8 h-8 rounded-xl bg-[#0F1E36] text-white flex items-center justify-center shadow-xs">
                    <FileText size={16} />
                  </div>
                  <h2 className="font-extrabold text-base text-[#0F1E36] tracking-tight">
                    Compensation Breakdown &amp; Deductions
                  </h2>
                  <span className="text-xs font-bold text-emerald-700 bg-emerald-50 px-2 py-0.5 rounded-full border border-emerald-200 flex items-center gap-1">
                    <CheckCircle2 size={11} /> Live Engine
                  </span>
                </div>
                <p className="text-xs text-slate-500 mt-1">
                  Itemized item-level earnings, mandatory government deductions, and take-home disbursement.
                </p>
              </div>

              {/* Action utilities */}
              <div className="flex items-center gap-2">
                <button
                  onClick={() => window.print()}
                  className="flex items-center gap-1.5 px-3 py-1.5 rounded-xl border border-slate-200 bg-white hover:bg-slate-50 text-slate-700 text-xs font-semibold shadow-xs transition"
                >
                  <Printer size={13} />
                  <span>Print Table</span>
                </button>
                <div className="text-xs text-slate-500 font-medium px-2 py-1 rounded-lg bg-slate-100">
                  Showing <strong>{filteredPayrolls.length}</strong> of {payrolls.length}
                </div>
              </div>
            </div>

            {/* Filter & Search Bar */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-3 pt-2">
              {/* Department Filter Pills */}
              <div className="flex items-center gap-1.5 overflow-x-auto pb-1 md:pb-0 scrollbar-none">
                {departments.map((dept) => {
                  const isActive = selectedDept === dept;
                  const count =
                    dept === 'ALL'
                      ? payrolls.length
                      : payrolls.filter((p) => p.department === dept).length;
                  return (
                    <button
                      key={dept}
                      onClick={() => setSelectedDept(dept)}
                      className={`px-3 py-1.5 rounded-xl text-xs font-semibold whitespace-nowrap transition-all flex items-center gap-1.5 ${
                        isActive
                          ? 'bg-[#0F1E36] text-white shadow-xs'
                          : 'bg-white text-slate-600 border border-slate-200 hover:bg-slate-50'
                      }`}
                    >
                      <span>{dept === 'ALL' ? 'All Workforce' : dept}</span>
                      <span
                        className={`text-[10px] px-1.5 py-0.2 rounded-full font-bold ${
                          isActive ? 'bg-white/20 text-white' : 'bg-slate-100 text-slate-600'
                        }`}
                      >
                        {count}
                      </span>
                    </button>
                  );
                })}
              </div>

              {/* Search & Sort Input */}
              <div className="flex items-center gap-2">
                <div className="relative w-full sm:w-64">
                  <Search
                    size={14}
                    className="absolute left-3 top-1/2 -translate-y-1/2 text-slate-400"
                  />
                  <input
                    type="text"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    placeholder="Search name, role, EID..."
                    className="w-full pl-9 pr-3 py-1.5 rounded-xl bg-white border border-slate-200 text-slate-800 placeholder:text-slate-400 text-xs focus:outline-none focus:border-[#4166F5] focus:ring-2 focus:ring-[#C7D3FF] transition"
                  />
                  {searchQuery && (
                    <button
                      onClick={() => setSearchQuery('')}
                      className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-400 hover:text-slate-600 text-xs"
                    >
                      &times;
                    </button>
                  )}
                </div>

                <select
                  value={sortBy}
                  onChange={(e: any) => setSortBy(e.target.value)}
                  className="px-2.5 py-1.5 rounded-xl bg-white border border-slate-200 text-slate-700 text-xs font-medium focus:outline-none focus:border-[#4166F5]"
                >
                  <option value="eid">Sort: EID</option>
                  <option value="name">Sort: Name (A-Z)</option>
                  <option value="gross">Sort: Highest Gross</option>
                  <option value="net">Sort: Highest Net</option>
                </select>
              </div>
            </div>
          </div>

          {/* Premium Multi-Tier Table */}
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse" style={{ minWidth: '1160px' }}>
              <thead>
                {/* Master Group Tier Header */}
                <tr className="border-b border-slate-200 text-[10px] font-bold uppercase tracking-wider">
                  <th
                    colSpan={2}
                    className="py-3 px-5 bg-slate-100/90 text-slate-600 border-r border-slate-200"
                  >
                    <div className="flex items-center gap-1.5">
                      <User size={13} className="text-slate-500" />
                      <span>Employee Details</span>
                    </div>
                  </th>
                  <th
                    colSpan={3}
                    className="py-3 px-5 bg-emerald-50/80 text-emerald-800 border-r border-slate-200 text-center"
                  >
                    <div className="flex items-center justify-center gap-1.5">
                      <TrendingUp size={13} className="text-emerald-600" />
                      <span>Earnings &amp; Compensation (Philippine Pesos)</span>
                    </div>
                  </th>
                  <th
                    colSpan={4}
                    className="py-3 px-5 bg-rose-50/80 text-rose-800 border-r border-slate-200 text-center"
                  >
                    <div className="flex items-center justify-center gap-1.5">
                      <ShieldCheck size={13} className="text-rose-600" />
                      <span>Mandatory Statutory Deductions</span>
                    </div>
                  </th>
                  <th
                    colSpan={2}
                    className="py-3 px-5 bg-[#EBF0FF]/80 text-[#3456D4] text-center"
                  >
                    <div className="flex items-center justify-center gap-1.5">
                      <Wallet size={13} className="text-[#4166F5]" />
                      <span>Disbursement Summary</span>
                    </div>
                  </th>
                </tr>

                {/* Sub-Column Header Tier */}
                <tr className="border-b-2 border-slate-200 bg-white text-slate-600 font-bold uppercase tracking-wider text-[11px]">
                  <th className="py-3.5 px-5 whitespace-nowrap w-20">EID</th>
                  <th className="py-3.5 px-5 whitespace-nowrap border-r border-slate-200 min-w-[200px]">
                    Employee Name
                  </th>

                  {/* Earnings */}
                  <th className="py-3.5 px-5 whitespace-nowrap bg-emerald-50/30 text-slate-800">
                    <div>Basic Rate</div>
                    <div className="text-[9px] text-slate-400 font-normal lowercase">monthly basic</div>
                  </th>
                  <th className="py-3.5 px-5 whitespace-nowrap bg-emerald-50/30 text-slate-800">
                    <div>Benefits</div>
                    <div className="text-[9px] text-emerald-600 font-normal lowercase">rice / phone / clothing</div>
                  </th>
                  <th className="py-3.5 px-5 whitespace-nowrap bg-emerald-50/40 text-[#0F1E36] font-extrabold border-r border-slate-200">
                    <div>Gross Income</div>
                    <div className="text-[9px] text-slate-500 font-normal lowercase">salary + benefits + ot</div>
                  </th>

                  {/* Statutory Deductions */}
                  <th className="py-3.5 px-4 whitespace-nowrap bg-rose-50/20 text-slate-700">
                    <div>SSS</div>
                    <div className="text-[9px] text-slate-400 font-normal lowercase">5% (max ₱1,500)</div>
                  </th>
                  <th className="py-3.5 px-4 whitespace-nowrap bg-rose-50/20 text-slate-700">
                    <div>PhilHealth</div>
                    <div className="text-[9px] text-slate-400 font-normal lowercase">2.5% premium</div>
                  </th>
                  <th className="py-3.5 px-4 whitespace-nowrap bg-rose-50/20 text-slate-700">
                    <div>Pag-IBIG</div>
                    <div className="text-[9px] text-slate-400 font-normal lowercase">2% (max ₱200)</div>
                  </th>
                  <th className="py-3.5 px-5 whitespace-nowrap bg-rose-50/30 text-[#3456D4] font-bold border-r border-slate-200">
                    <div>W/Tax</div>
                    <div className="text-[9px] text-slate-500 font-normal lowercase">TRAIN brackets</div>
                  </th>

                  {/* Net Pay & Action */}
                  <th className="py-3.5 px-5 whitespace-nowrap text-emerald-700 font-black">
                    <div>Net Take-Home</div>
                    <div className="text-[9px] text-emerald-600 font-normal lowercase">final payout</div>
                  </th>
                  <th className="py-3.5 px-5 whitespace-nowrap text-right">Payslip</th>
                </tr>
              </thead>

              <tbody className="divide-y divide-slate-100 text-slate-700 text-xs">
                {loading ? (
                  <tr>
                    <td colSpan={11} className="py-16 text-center text-slate-400 text-sm">
                      <div className="flex flex-col items-center justify-center gap-2">
                        <RefreshCw size={24} className="animate-spin text-[#4166F5]" />
                        <span>Recalculating Philippine statutory payroll...</span>
                      </div>
                    </td>
                  </tr>
                ) : filteredPayrolls.length === 0 ? (
                  <tr>
                    <td colSpan={11} className="py-16 text-center text-slate-400 text-sm">
                      No employees match your search or department filter.
                    </td>
                  </tr>
                ) : (
                  filteredPayrolls.map((p) => {
                    const initials = getInitials(p.name);
                    return (
                      <tr
                        key={p.eid}
                        className="hover:bg-slate-50/90 transition-all group"
                      >
                        {/* EID */}
                        <td className="py-4 px-5 font-mono font-bold text-slate-400 group-hover:text-slate-700">
                          #{p.eid}
                        </td>

                        {/* Employee Name & Profile */}
                        <td className="py-4 px-5 border-r border-slate-200">
                          <div className="flex items-center gap-3">
                            <div className="w-8 h-8 rounded-full bg-[#0F1E36] text-white flex items-center justify-center font-bold text-[11px] shrink-0 shadow-xs">
                              {initials}
                            </div>
                            <div className="min-w-0">
                              <div className="font-bold text-[#0F1E36] text-xs truncate">
                                {p.name}
                              </div>
                              <div className="flex items-center gap-1.5 mt-0.5">
                                <span className="text-[10px] text-slate-500 font-medium truncate max-w-[130px]">
                                  {p.designation}
                                </span>
                                <span className="text-[9px] px-1.5 py-0.2 rounded bg-slate-100 text-slate-600 font-medium truncate">
                                  {p.department}
                                </span>
                              </div>
                            </div>
                          </div>
                        </td>

                        {/* Earnings: Basic Rate */}
                        <td className="py-4 px-5 bg-emerald-50/15 font-mono text-xs text-slate-800 font-medium">
                          {formatPHP(p.basicSalary)}
                        </td>

                        {/* Earnings: Benefits */}
                        <td className="py-4 px-5 bg-emerald-50/15 font-mono text-xs text-emerald-700 font-semibold">
                          +{formatPHP(p.totalBenefits)}
                        </td>

                        {/* Earnings: Gross Income */}
                        <td className="py-4 px-5 bg-emerald-50/25 font-mono text-xs font-bold text-slate-900 border-r border-slate-200">
                          <div className="flex items-center gap-1">
                            <span>{formatPHP(p.grossIncome)}</span>
                            {p.overtimePay > 0 && (
                              <span className="text-[9px] text-amber-600 font-sans font-semibold px-1 rounded bg-amber-50">
                                +OT
                              </span>
                            )}
                          </div>
                        </td>

                        {/* Deductions: SSS */}
                        <td className="py-4 px-4 bg-rose-50/10 font-mono text-xs text-slate-700">
                          <span className="px-1.5 py-0.5 rounded bg-slate-100 text-slate-700 text-[11px]">
                            {formatPHP(p.sssContribution)}
                          </span>
                        </td>

                        {/* Deductions: PhilHealth */}
                        <td className="py-4 px-4 bg-rose-50/10 font-mono text-xs text-slate-700">
                          <span className="px-1.5 py-0.5 rounded bg-slate-100 text-slate-700 text-[11px]">
                            {formatPHP(p.philhealthContribution)}
                          </span>
                        </td>

                        {/* Deductions: Pag-IBIG */}
                        <td className="py-4 px-4 bg-rose-50/10 font-mono text-xs text-slate-700">
                          <span className="px-1.5 py-0.5 rounded bg-slate-100 text-slate-700 text-[11px]">
                            {formatPHP(p.pagibigContribution)}
                          </span>
                        </td>

                        {/* Deductions: Withholding Tax */}
                        <td className="py-4 px-5 bg-rose-50/15 font-mono text-xs text-[#3456D4] font-semibold border-r border-slate-200">
                          <span className="px-2 py-0.5 rounded-lg bg-blue-50 text-[#3456D4] border border-blue-200/60">
                            {formatPHP(p.withholdingTax)}
                          </span>
                        </td>

                        {/* Net Pay Hero Cell */}
                        <td className="py-4 px-5">
                          <div className="font-extrabold font-mono text-sm text-emerald-700 tracking-tight">
                            {formatPHP(p.netPay)}
                          </div>
                          <div className="text-[10px] text-rose-500 font-sans font-medium mt-0.5">
                            &minus;{formatPHP(p.totalDeductions)} deductions
                          </div>
                        </td>

                        {/* Action: View Payslip */}
                        <td className="py-4 px-5 text-right">
                          <button
                            onClick={() => setActivePayslip(p)}
                            className="inline-flex items-center gap-1.5 px-3 py-1.5 rounded-xl bg-white hover:bg-[#0F1E36] text-slate-700 hover:text-white border border-slate-200 hover:border-[#0F1E36] transition-all text-xs font-semibold shadow-xs cursor-pointer group/btn"
                          >
                            <Receipt size={13} className="text-[#4166F5] group-hover/btn:text-white transition-colors" />
                            <span>View Slip</span>
                          </button>
                        </td>
                      </tr>
                    );
                  })
                )}
              </tbody>

              {/* Table Totals / Summary Reconciliation Footer */}
              {!loading && filteredPayrolls.length > 0 && (
                <tfoot>
                  <tr className="border-t-2 border-slate-300 bg-slate-100 font-bold text-xs text-slate-800">
                    <td colSpan={2} className="py-4 px-5 border-r border-slate-200">
                      <div className="font-extrabold text-[#0F1E36] uppercase tracking-wider text-[11px]">
                        Totals ({filteredPayrolls.length} Employees)
                      </div>
                      <div className="text-[10px] text-slate-500 font-normal">
                        Reconciled Accounting Summary
                      </div>
                    </td>

                    {/* Earnings Totals */}
                    <td className="py-4 px-5 font-mono text-xs text-slate-900 bg-emerald-50/40">
                      {formatPHP(totals.basicSalary)}
                    </td>
                    <td className="py-4 px-5 font-mono text-xs text-emerald-800 bg-emerald-50/40">
                      +{formatPHP(totals.totalBenefits)}
                    </td>
                    <td className="py-4 px-5 font-mono text-xs font-black text-[#0F1E36] bg-emerald-100/60 border-r border-slate-200">
                      {formatPHP(totals.grossIncome)}
                    </td>

                    {/* Deductions Totals */}
                    <td className="py-4 px-4 font-mono text-xs text-slate-800 bg-rose-50/30">
                      {formatPHP(totals.sss)}
                    </td>
                    <td className="py-4 px-4 font-mono text-xs text-slate-800 bg-rose-50/30">
                      {formatPHP(totals.philhealth)}
                    </td>
                    <td className="py-4 px-4 font-mono text-xs text-slate-800 bg-rose-50/30">
                      {formatPHP(totals.pagibig)}
                    </td>
                    <td className="py-4 px-5 font-mono text-xs font-bold text-[#3456D4] bg-rose-50/40 border-r border-slate-200">
                      {formatPHP(totals.tax)}
                    </td>

                    {/* Net Pay Grand Total */}
                    <td className="py-4 px-5 font-mono text-sm font-black text-emerald-700 bg-emerald-50">
                      {formatPHP(totals.netPay)}
                    </td>

                    {/* Action empty */}
                    <td className="py-4 px-5 text-right bg-slate-100">
                      <button
                        onClick={() => window.print()}
                        title="Print Full Payroll Register"
                        className="p-1.5 rounded-lg bg-white border border-slate-200 hover:bg-slate-50 text-slate-600 transition"
                      >
                        <Printer size={14} />
                      </button>
                    </td>
                  </tr>
                </tfoot>
              )}
            </table>
          </div>
        </div>

        {/* Printable Official Payslip Modal */}
        {activePayslip && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-xs print:p-0">
            <div className="bg-white border border-slate-200 rounded-3xl w-full max-w-2xl max-h-[92vh] overflow-y-auto p-6 sm:p-8 shadow-2xl print:border-none print:shadow-none print:w-full print:max-w-none">
              {/* Modal Header */}
              <div className="flex items-center justify-between pb-4 border-b border-slate-200 print:hidden">
                <div className="flex items-center gap-2.5">
                  <div className="w-8 h-8 rounded-xl bg-[#4166F5] text-white flex items-center justify-center shadow-xs">
                    <Receipt size={16} />
                  </div>
                  <div>
                    <h3 className="text-base font-extrabold text-[#0F1E36]">Official MotorPH Payslip</h3>
                    <p className="text-[11px] text-slate-400">Authenticated Statutory Payroll Record</p>
                  </div>
                </div>
                <div className="flex items-center gap-2">
                  <button
                    onClick={() => window.print()}
                    className="flex items-center gap-1.5 px-3.5 py-1.5 rounded-xl bg-[#0F1E36] hover:bg-[#1A2E4E] text-white text-xs font-semibold shadow-xs transition"
                  >
                    <Printer size={14} />
                    <span>Print Payslip</span>
                  </button>
                  <button
                    onClick={() => setActivePayslip(null)}
                    className="p-1.5 rounded-xl text-slate-400 hover:text-slate-700 hover:bg-slate-100 transition"
                  >
                    <XCircle size={20} />
                  </button>
                </div>
              </div>

              {/* Printable Payslip Body */}
              <div className="mt-4 p-6 bg-slate-50/70 border border-slate-200 rounded-2xl print:bg-white print:border print:border-black print:p-8">
                {/* Company Header */}
                <div className="text-center pb-4 border-b border-slate-200 print:border-black">
                  <div className="w-10 h-10 mx-auto rounded-xl bg-[#0F1E36] text-white font-extrabold flex items-center justify-center mb-2">
                    M
                  </div>
                  <h2 className="text-lg font-black tracking-wider uppercase text-[#0F1E36] print:text-black">
                    MotorPH Corporation
                  </h2>
                  <p className="text-xs text-slate-500 print:text-gray-600">
                    7 Jupiter Avenue, Makati Commercial Center, Metro Manila
                  </p>
                  <p className="text-xs font-bold text-[#3456D4] print:text-black mt-1 uppercase tracking-wider">
                    Official Employee Payroll Statement
                  </p>
                </div>

                {/* Employee Info Header */}
                <div className="grid grid-cols-2 gap-4 py-4 text-xs border-b border-slate-200 print:border-black">
                  <div>
                    <span className="text-slate-500 print:text-gray-600 block">Employee Name:</span>
                    <span className="font-bold text-[#0F1E36] print:text-black text-sm">
                      {activePayslip.name}
                    </span>
                    <span className="text-slate-600 print:text-gray-700 block mt-1">
                      Position: <strong>{activePayslip.designation}</strong>
                    </span>
                    <span className="text-slate-600 print:text-gray-700 block">
                      Department: {activePayslip.department}
                    </span>
                  </div>

                  <div className="text-right">
                    <span className="text-slate-500 print:text-gray-600 block">Employee ID:</span>
                    <span className="font-mono font-bold text-[#0F1E36] print:text-black text-sm">
                      #{activePayslip.eid}
                    </span>
                    <span className="text-slate-600 print:text-gray-700 block mt-1">
                      Payroll Period:
                    </span>
                    <span className="font-semibold text-slate-800 print:text-black">
                      {activePayslip.periodStart} to {activePayslip.periodEnd}
                    </span>
                  </div>
                </div>

                {/* Earnings & Deductions Table */}
                <div className="grid grid-cols-2 gap-6 py-4 text-xs">
                  {/* Earnings Column */}
                  <div>
                    <h4 className="font-bold uppercase tracking-wider text-emerald-700 print:text-black pb-2 border-b border-slate-200 print:border-black flex items-center justify-between">
                      <span>Earnings &amp; Allowances</span>
                      <span className="text-[10px] text-emerald-600 font-mono">Amount (PHP)</span>
                    </h4>
                    <div className="mt-2 space-y-1.5 font-mono">
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">Basic Monthly Rate:</span>
                        <span className="text-slate-900 font-semibold">{formatPHP(activePayslip.basicSalary)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">Overtime Pay:</span>
                        <span className="text-slate-900">{formatPHP(activePayslip.overtimePay || 0)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">Rice Subsidy:</span>
                        <span className="text-slate-900">{formatPHP(activePayslip.riceSubsidy || 0)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">Phone Allowance:</span>
                        <span className="text-slate-900">{formatPHP(activePayslip.phoneAllowance || 0)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">Clothing Allowance:</span>
                        <span className="text-slate-900">{formatPHP(activePayslip.clothingAllowance || 0)}</span>
                      </div>
                      <div className="flex justify-between pt-2 border-t border-slate-200 font-bold text-[#0F1E36]">
                        <span>Gross Earnings:</span>
                        <span>{formatPHP((activePayslip.grossIncome || 0) + (activePayslip.totalBenefits || 0))}</span>
                      </div>
                    </div>
                  </div>

                  {/* Deductions Column */}
                  <div>
                    <h4 className="font-bold uppercase tracking-wider text-rose-700 print:text-black pb-2 border-b border-slate-200 print:border-black flex items-center justify-between">
                      <span>Statutory Deductions</span>
                      <span className="text-[10px] text-rose-600 font-mono">Amount (PHP)</span>
                    </h4>
                    <div className="mt-2 space-y-1.5 font-mono">
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">SSS Contribution:</span>
                        <span className="text-slate-900 font-semibold">{formatPHP(activePayslip.sssContribution)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">PhilHealth:</span>
                        <span className="text-slate-900 font-semibold">{formatPHP(activePayslip.philhealthContribution)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">Pag-IBIG:</span>
                        <span className="text-slate-900 font-semibold">{formatPHP(activePayslip.pagibigContribution)}</span>
                      </div>
                      <div className="flex justify-between">
                        <span className="text-slate-600 print:text-gray-700">Withholding Tax (TRAIN):</span>
                        <span className="text-slate-900 font-semibold">{formatPHP(activePayslip.withholdingTax)}</span>
                      </div>
                      <div className="flex justify-between pt-2 border-t border-slate-200 print:border-black font-bold text-rose-700 print:text-black">
                        <span>Total Deductions:</span>
                        <span>{formatPHP(activePayslip.totalDeductions)}</span>
                      </div>
                    </div>
                  </div>
                </div>

                {/* Net Pay Grand Total */}
                <div className="mt-4 p-4 rounded-2xl bg-white border border-slate-200 print:bg-gray-100 print:border-black flex items-center justify-between shadow-xs">
                  <div>
                    <span className="text-xs uppercase font-extrabold text-slate-500 print:text-gray-700 block">
                      Net Take Home Pay
                    </span>
                    <span className="text-[11px] text-slate-400 print:text-gray-500">
                      Disbursed directly via corporate payroll account
                    </span>
                  </div>
                  <div className="text-2xl font-black font-mono text-emerald-700 print:text-black">
                    {formatPHP(activePayslip.netPay)}
                  </div>
                </div>

                {/* Signatures */}
                <div className="mt-8 pt-6 border-t border-slate-200 print:border-black grid grid-cols-2 gap-8 text-center text-xs">
                  <div>
                    <div className="w-48 mx-auto border-b border-slate-400 print:border-black mb-1"></div>
                    <span className="text-slate-500 print:text-black">Prepared By: Payroll Officer</span>
                  </div>
                  <div>
                    <div className="w-48 mx-auto border-b border-slate-400 print:border-black mb-1"></div>
                    <span className="text-slate-500 print:text-black">Received By: Employee Signature</span>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}
      </div>
    </AppShell>
  );
}
