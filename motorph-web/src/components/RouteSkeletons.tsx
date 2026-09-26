import PageSkeleton from '@/components/PageSkeleton';

function SidebarSkeleton() {
  return (
    <div className="hidden md:flex w-60 shrink-0 bg-[#0F1E36] flex-col p-4 gap-3">
      <div className="flex items-center gap-3 px-2 py-3 mb-2">
        <div className="w-10 h-10 rounded-xl bg-white/10" />
        <div className="space-y-1.5">
          <div className="h-3 w-20 bg-white/20 rounded-full" />
          <div className="h-2.5 w-14 bg-white/10 rounded-full" />
        </div>
      </div>
      {[...Array(6)].map((_, i) => (
        <div key={i} className="h-9 rounded-xl bg-white/5" />
      ))}
    </div>
  );
}

function TopNavSkeleton() {
  return (
    <div className="h-14 bg-white border-b border-slate-200 px-6 flex items-center justify-between">
      <div className="h-4 w-32 bg-slate-200 rounded-full animate-pulse" />
      <div className="h-8 w-8 bg-slate-200 rounded-full animate-pulse" />
    </div>
  );
}

export function PayrollLoading() {
  return (
    <div className="flex min-h-screen bg-slate-50">
      <SidebarSkeleton />
      <div className="flex-1 flex flex-col">
        <TopNavSkeleton />
        <div className="flex-1 p-6">
          <PageSkeleton title="Payroll Engine" showTable rows={12} />
        </div>
      </div>
    </div>
  );
}

export function TimesheetLoading() {
  return (
    <div className="flex min-h-screen bg-slate-50">
      <SidebarSkeleton />
      <div className="flex-1 flex flex-col">
        <TopNavSkeleton />
        <div className="flex-1 p-6">
          <PageSkeleton title="Timesheet / DTR" showTable rows={8} />
        </div>
      </div>
    </div>
  );
}

export function LeavesLoading() {
  return (
    <div className="flex min-h-screen bg-slate-50">
      <SidebarSkeleton />
      <div className="flex-1 flex flex-col">
        <TopNavSkeleton />
        <div className="flex-1 p-6">
          <PageSkeleton title="Leave Management" showTable rows={6} />
        </div>
      </div>
    </div>
  );
}

export function PayslipLoading() {
  return (
    <div className="flex min-h-screen bg-slate-50">
      <SidebarSkeleton />
      <div className="flex-1 flex flex-col">
        <TopNavSkeleton />
        <div className="flex-1 p-6">
          <PageSkeleton title="My Payslip" />
        </div>
      </div>
    </div>
  );
}

// Default export for /payroll route
export default PayrollLoading;
