// Shared skeleton loading UI — shown instantly while server pages fetch data.
// Mirrors the AppShell layout (sidebar + header + content area) so there's no flash.

export function SkeletonCard({ className = '' }: { className?: string }) {
  return (
    <div className={`bg-white border border-slate-200 rounded-2xl p-5 shadow-sm animate-pulse ${className}`}>
      <div className="flex items-center justify-between mb-3">
        <div className="h-3 w-24 bg-slate-200 rounded-full" />
        <div className="h-8 w-8 bg-slate-100 rounded-lg" />
      </div>
      <div className="h-7 w-16 bg-slate-200 rounded-md mt-3" />
      <div className="h-3 w-32 bg-slate-100 rounded-full mt-2" />
    </div>
  );
}

export function SkeletonRow() {
  return (
    <div className="flex items-center gap-4 py-3 px-4 border-b border-slate-100 animate-pulse">
      <div className="h-3 w-10 bg-slate-200 rounded-full" />
      <div className="flex-1 space-y-1.5">
        <div className="h-3 w-40 bg-slate-200 rounded-full" />
        <div className="h-2.5 w-28 bg-slate-100 rounded-full" />
      </div>
      <div className="h-3 w-24 bg-slate-100 rounded-full" />
      <div className="h-5 w-16 bg-slate-100 rounded-full" />
      <div className="h-3 w-20 bg-slate-100 rounded-full" />
    </div>
  );
}

export default function PageSkeleton({
  title,
  subtitle,
  showCards = false,
  showTable = false,
  rows = 8,
}: {
  title: string;
  subtitle?: string;
  showCards?: boolean;
  showTable?: boolean;
  rows?: number;
}) {
  return (
    <div className="space-y-6 animate-pulse">
      {/* Page header */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
        <div>
          <div className="h-7 w-56 bg-slate-200 rounded-lg" />
          {subtitle && <div className="h-3 w-72 bg-slate-100 rounded-full mt-2" />}
        </div>
        <div className="h-9 w-36 bg-slate-200 rounded-xl" />
      </div>

      {/* Metric cards */}
      {showCards && (
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
          {[...Array(4)].map((_, i) => (
            <SkeletonCard key={i} />
          ))}
        </div>
      )}

      {/* Table or list */}
      {showTable && (
        <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
          {/* Table header */}
          <div className="flex items-center gap-4 py-3 px-4 border-b border-slate-200 bg-slate-50">
            {[10, 28, 32, 12, 18].map((w, i) => (
              <div key={i} className={`h-2.5 w-${w} bg-slate-200 rounded-full`} />
            ))}
          </div>
          {[...Array(rows)].map((_, i) => (
            <SkeletonRow key={i} />
          ))}
        </div>
      )}

      {/* Generic content block */}
      {!showCards && !showTable && (
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          {[0, 1].map((i) => (
            <div key={i} className="bg-white border border-slate-200 rounded-2xl p-6 shadow-sm space-y-3">
              <div className="h-4 w-40 bg-slate-200 rounded-md" />
              {[...Array(5)].map((_, j) => (
                <div key={j} className="flex justify-between py-1">
                  <div className="h-3 w-28 bg-slate-100 rounded-full" />
                  <div className="h-3 w-24 bg-slate-200 rounded-full" />
                </div>
              ))}
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
