import React from 'react';
import { redirect } from 'next/navigation';
import { getSession, getCurrentEmployee } from '@/lib/auth';
import { prisma } from '@/lib/prisma';
import AppShell from '@/components/AppShell';
import { formatPHP } from '@/lib/payrollCalculator';
import {
  Users,
  CalendarCheck,
  Clock,
  CreditCard,
  Shield,
  CheckCircle2,
  AlertCircle,
} from 'lucide-react';
import DashboardInteractive from './DashboardInteractive';

export default async function DashboardPage() {
  const session = await getSession();
  if (!session) redirect('/');

  // Fetch employee profile + today's date in parallel with the session check
  const today = new Date().toISOString().split('T')[0];
  const [employee, totalEmployees, pendingLeaves, userTimesheetToday] = await Promise.all([
    getCurrentEmployee(),
    prisma.employee.count({ where: { isActive: true } }),
    prisma.leave.count({ where: { leaveStatus: 'Pending' } }),
    prisma.timesheet.findFirst({
      where: { eid: session.eid, logDate: today },
    }),
  ]);


  return (
    <AppShell user={session}>
      <div className="space-y-6">
        {/* Welcome Banner - Night Blue Hero */}
        <div className="bg-[#0F1E36] text-white p-6 sm:p-7 rounded-2xl border border-[#1A2E4E] shadow-lg shadow-[#0F1E36]/15 relative overflow-hidden">
          <div className="relative z-10 flex flex-col md:flex-row md:items-center justify-between gap-4">
            <div>
              <div className="flex items-center gap-2">
                <span className="text-xs font-semibold px-2.5 py-0.5 rounded-full bg-white/10 text-cyan-200 border border-white/15">
                  {session.role} PORTAL
                </span>
                <span className="text-xs text-slate-300">MotorPH Hub</span>
              </div>
              <h1 className="text-2xl font-bold text-white mt-2">
                Welcome back, {employee?.firstName} {employee?.lastName}
              </h1>
              <p className="text-sm text-slate-300 mt-1">
                {employee?.designation?.name} &bull; {employee?.designation?.department?.name} Department
              </p>
            </div>

            {/* Interactive Clock-In Button */}
            <DashboardInteractive
              hasClockedInToday={!!userTimesheetToday}
              clockInTime={userTimesheetToday?.logTime}
            />
          </div>
        </div>

        {/* Top Metric Cards - Clean White with Night Blue Accent */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
            <div className="flex items-center justify-between">
              <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                Total Workforce
              </span>
              <div className="w-8 h-8 rounded-lg bg-[#EBF0FF] text-[#4166F5] flex items-center justify-center">
                <Users size={18} />
              </div>
            </div>
            <div className="mt-3 text-2xl font-black text-[#0F1E36]">{totalEmployees}</div>
            <span className="text-[11px] text-emerald-600 font-medium flex items-center gap-1 mt-1">
              <CheckCircle2 size={12} /> Active payroll employees
            </span>
          </div>

          <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
            <div className="flex items-center justify-between">
              <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                Pending Leaves
              </span>
              <div className="w-8 h-8 rounded-lg bg-amber-50 text-amber-600 flex items-center justify-center">
                <CalendarCheck size={18} />
              </div>
            </div>
            <div className="mt-3 text-2xl font-black text-[#0F1E36]">{pendingLeaves}</div>
            <span className="text-[11px] text-amber-600 font-medium flex items-center gap-1 mt-1">
              <AlertCircle size={12} /> Awaiting manager review
            </span>
          </div>

          <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
            <div className="flex items-center justify-between">
              <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                Basic Monthly Rate
              </span>
              <div className="w-8 h-8 rounded-lg bg-emerald-50 text-emerald-600 flex items-center justify-center">
                <CreditCard size={18} />
              </div>
            </div>
            <div className="mt-3 text-2xl font-black text-[#0F1E36]">
              {formatPHP(employee?.components?.basicSalary || 0)}
            </div>
            <span className="text-[11px] text-slate-500 mt-1 block">
              Hourly: {formatPHP(employee?.components?.hourlyRate || 0)}
            </span>
          </div>

          <div className="bg-white p-5 rounded-2xl border border-slate-200 shadow-sm">
            <div className="flex items-center justify-between">
              <span className="text-xs font-semibold text-slate-500 uppercase tracking-wider">
                Semi-Monthly Rate
              </span>
              <div className="w-8 h-8 rounded-lg bg-blue-50 text-[#4166F5] flex items-center justify-center">
                <CreditCard size={18} />
              </div>
            </div>
            <div className="mt-3 text-2xl font-black text-[#0F1E36]">
              {formatPHP(employee?.components?.halfMonthRate || 0)}
            </div>
            <span className="text-[11px] text-slate-500 mt-1 block">
              Pay Cycle: 15th &amp; 30th
            </span>
          </div>
        </div>

        {/* Two-Column Layout: Employee Details & Statutory IDs */}
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {/* Employee Profile Overview - Crisp White Card */}
          <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
            <h2 className="text-base font-bold text-[#0F1E36] mb-4 flex items-center gap-2">
              <Users size={18} className="text-[#4166F5]" />
              Employee Profile Information
            </h2>
            <div className="grid grid-cols-2 gap-y-3 gap-x-4 text-xs">
              <div>
                <span className="text-slate-500 block">Employee ID</span>
                <span className="font-semibold text-[#0F1E36]">#{employee?.eid}</span>
              </div>
              <div>
                <span className="text-slate-500 block">Employment Status</span>
                <span className="font-semibold text-emerald-700">{employee?.status}</span>
              </div>
              <div>
                <span className="text-slate-500 block">Work Email</span>
                <span className="font-semibold text-[#0F1E36] truncate block">{employee?.username}</span>
              </div>
              <div>
                <span className="text-slate-500 block">Contact Number</span>
                <span className="font-semibold text-[#0F1E36]">{employee?.phoneNumber || 'N/A'}</span>
              </div>
              <div className="col-span-2">
                <span className="text-slate-500 block">Residential Address</span>
                <span className="font-semibold text-slate-700">{employee?.address || 'N/A'}</span>
              </div>
              <div>
                <span className="text-slate-500 block">Immediate Supervisor</span>
                <span className="font-semibold text-slate-700">{employee?.supervisor?.name || 'Direct Executive'}</span>
              </div>
              <div>
                <span className="text-slate-500 block">Rice Subsidy / Allowances</span>
                <span className="font-semibold text-slate-700">
                  {formatPHP((employee?.components?.riceSubsidy || 0) + (employee?.components?.phoneAllowance || 0) + (employee?.components?.clothingAllowance || 0))}
                </span>
              </div>
            </div>
          </div>

          {/* Statutory Government Identifiers - Crisp White Card */}
          <div className="bg-white p-6 rounded-2xl border border-slate-200 shadow-sm">
            <h2 className="text-base font-bold text-[#0F1E36] mb-4 flex items-center gap-2">
              <Shield size={18} className="text-emerald-600" />
              Statutory Government Information
            </h2>
            <div className="space-y-3 text-xs">
              <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50 border border-slate-200">
                <span className="text-slate-600 font-medium">Social Security System (SSS)</span>
                <span className="font-mono font-semibold text-[#0F1E36]">
                  {employee?.govIds?.sssNum || 'Not registered'}
                </span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50 border border-slate-200">
                <span className="text-slate-600 font-medium">PhilHealth ID</span>
                <span className="font-mono font-semibold text-[#0F1E36]">
                  {employee?.govIds?.philhealthNum || 'Not registered'}
                </span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50 border border-slate-200">
                <span className="text-slate-600 font-medium">Tax Identification Number (TIN)</span>
                <span className="font-mono font-semibold text-[#0F1E36]">
                  {employee?.govIds?.tinNum || 'Not registered'}
                </span>
              </div>
              <div className="flex items-center justify-between p-3 rounded-xl bg-slate-50 border border-slate-200">
                <span className="text-slate-600 font-medium">Pag-IBIG / HDMF Number</span>
                <span className="font-mono font-semibold text-[#0F1E36]">
                  {employee?.govIds?.pagibigNum || 'Not registered'}
                </span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </AppShell>
  );
}
