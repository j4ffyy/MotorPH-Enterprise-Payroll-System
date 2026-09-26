import PageSkeleton from '@/components/PageSkeleton';

export default function DashboardLoading() {
  return (
    <div className="flex min-h-screen bg-slate-50">
      {/* Sidebar skeleton */}
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

      {/* Main content */}
      <div className="flex-1 flex flex-col">
        {/* Top nav skeleton */}
        <div className="h-14 bg-white border-b border-slate-200 px-6 flex items-center justify-between">
          <div className="h-4 w-32 bg-slate-200 rounded-full animate-pulse" />
          <div className="h-8 w-8 bg-slate-200 rounded-full animate-pulse" />
        </div>

        <div className="flex-1 p-6">
          {/* Welcome banner skeleton */}
          <div className="bg-[#0F1E36]/80 rounded-2xl p-6 mb-6 animate-pulse">
            <div className="h-5 w-24 bg-white/20 rounded-full mb-3" />
            <div className="h-7 w-64 bg-white/30 rounded-lg mb-2" />
            <div className="h-4 w-48 bg-white/15 rounded-full" />
          </div>

          <PageSkeleton title="Dashboard" showCards />
        </div>
      </div>
    </div>
  );
}
