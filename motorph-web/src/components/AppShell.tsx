'use client';

import React, { useState } from 'react';
import Link from 'next/link';
import { usePathname, useRouter } from 'next/navigation';
import {
  LayoutDashboard,
  Users,
  Calculator,
  CalendarCheck,
  Clock,
  LogOut,
  User,
  Menu,
  X,
  FileText,
} from 'lucide-react';
import MotorPHLogo from './MotorPHLogo';

interface AppShellProps {
  children: React.ReactNode;
  user: {
    eid: number;
    name: string;
    username: string;
    role: string;
    designation?: string;
    department?: string;
  };
}

export default function AppShell({ children, user }: AppShellProps) {
  const pathname = usePathname();
  const router = useRouter();
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const [loggingOut, setLoggingOut] = useState(false);

  const isAdminOrHR = user.role === 'ADMIN' || user.role === 'HR';
  const isPayrollAdmin = user.role === 'ADMIN' || user.role === 'PAYROLL';

  const navItems = [
    {
      label: 'Dashboard',
      href: '/dashboard',
      icon: LayoutDashboard,
      visible: true,
    },
    {
      label: 'Employees',
      href: '/employees',
      icon: Users,
      visible: isAdminOrHR,
    },
    {
      label: 'Payroll Engine',
      href: '/payroll',
      icon: Calculator,
      visible: isPayrollAdmin,
    },
    {
      label: 'My Payslip',
      href: '/payslip',
      icon: FileText,
      visible: true,
    },
    {
      label: 'Leave Requests',
      href: '/leaves',
      icon: CalendarCheck,
      visible: true,
    },
    {
      label: 'Timesheet / DTR',
      href: '/timesheet',
      icon: Clock,
      visible: true,
    },
  ];

  const handleLogout = async () => {
    setLoggingOut(true);
    try {
      await fetch('/api/auth/logout', { method: 'POST' });
      router.push('/');
      router.refresh();
    } catch (err) {
      console.error(err);
      setLoggingOut(false);
    }
  };

  const getRoleBadgeColor = (role: string) => {
    switch (role) {
      case 'ADMIN':
        return 'bg-rose-100 text-rose-700 border-rose-200';
      case 'HR':
        return 'bg-amber-100 text-amber-700 border-amber-200';
      case 'PAYROLL':
        return 'bg-[#EBF0FF] text-[#3456D4] border-[#C7D3FF]';
      default:
        return 'bg-emerald-100 text-emerald-700 border-emerald-200';
    }
  };

  return (
    <div className="min-h-screen flex flex-col bg-slate-50 text-slate-900">
      {/* Top Navbar - Deep Night Blue */}
      <header className="sticky top-0 z-40 bg-[#0F1E36] text-white shadow-md border-b border-[#1A2E4E]">
        <div className="w-full px-4 sm:px-6 lg:px-8 h-16 flex items-center justify-between">
          <div className="flex items-center gap-3">
            <button
              onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
              className="lg:hidden p-2 rounded-lg text-slate-300 hover:text-white hover:bg-white/10"
            >
              {mobileMenuOpen ? <X size={20} /> : <Menu size={20} />}
            </button>
            <Link href="/dashboard" className="flex items-center gap-2.5">
              <div className="w-9 h-9 rounded-xl overflow-hidden shadow-md flex items-center justify-center">
                <MotorPHLogo size={36} />
              </div>
              <div>
                <span className="font-bold tracking-tight text-lg text-white">
                  Motor<span className="text-[#a5baff]">PH</span>
                </span>
                <span className="hidden sm:inline-block ml-2 text-xs px-2 py-0.5 rounded-full bg-white/10 text-[#a5baff] border border-white/15">
                  CTRL+ALT+ELITE
                </span>
              </div>
            </Link>
          </div>

          {/* Right Header: User Info & Logout */}
          <div className="flex items-center gap-3">
            <div className="hidden sm:flex flex-col text-right">
              <span className="text-sm font-semibold text-white">{user.name}</span>
              <div className="flex items-center justify-end gap-1.5 mt-0.5">
                <span className={`text-[10px] font-semibold px-2 py-0.5 rounded-full border ${getRoleBadgeColor(user.role)}`}>
                  {user.role}
                </span>
                <span className="text-xs text-slate-300">ID: #{user.eid}</span>
              </div>
            </div>
            <div className="w-9 h-9 rounded-full bg-white/10 border border-white/20 flex items-center justify-center text-white">
              <User size={18} />
            </div>
            <button
              onClick={handleLogout}
              disabled={loggingOut}
              className="p-2 rounded-lg text-slate-300 hover:text-rose-300 hover:bg-rose-500/20 transition-colors ml-1"
              title="Sign Out"
            >
              <LogOut size={18} />
            </button>
          </div>
        </div>
      </header>

      {/* Main Container */}
      <div className="flex-1 w-full px-4 sm:px-6 lg:px-8 py-6 flex gap-5">
        {/* Desktop Sidebar Navigation - Clean White Card with Night Blue Accents */}
        <aside className="hidden lg:block w-52 shrink-0">
          <nav className="sticky top-24 space-y-1 bg-white p-3 rounded-2xl border border-slate-200/90 shadow-sm">
            {navItems
              .filter((item) => item.visible)
              .map((item) => {
                const Icon = item.icon;
                const isActive = pathname === item.href;
                return (
                  <Link
                    key={item.href}
                    href={item.href}
                    className={`flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-sm font-medium transition-all ${
                      isActive
                        ? 'bg-[#0F1E36] text-white shadow-md shadow-[#0F1E36]/20'
                        : 'text-slate-600 hover:text-[#0F1E36] hover:bg-slate-100'
                    }`}
                  >
                    <Icon size={18} className={isActive ? 'text-[#a5baff]' : 'text-slate-500'} />
                    <span>{item.label}</span>
                  </Link>
                );
              })}

          </nav>
        </aside>

        {/* Mobile Navigation Drawer */}
        {mobileMenuOpen && (
          <div className="fixed inset-0 z-50 lg:hidden flex">
            <div
              className="fixed inset-0 bg-black/50 backdrop-blur-sm"
              onClick={() => setMobileMenuOpen(false)}
            />
            <div className="relative w-72 max-w-xs bg-white h-full p-4 border-r border-slate-200 flex flex-col z-10 shadow-2xl">
              <div className="flex items-center justify-between pb-4 border-b border-slate-100">
                <span className="font-bold text-[#0F1E36]">Navigation</span>
                <button
                  onClick={() => setMobileMenuOpen(false)}
                  className="p-1 rounded-lg text-slate-500 hover:text-slate-900"
                >
                  <X size={18} />
                </button>
              </div>
              <nav className="mt-4 space-y-1 flex-1">
                {navItems
                  .filter((item) => item.visible)
                  .map((item) => {
                    const Icon = item.icon;
                    const isActive = pathname === item.href;
                    return (
                      <Link
                        key={item.href}
                        href={item.href}
                        onClick={() => setMobileMenuOpen(false)}
                        className={`flex items-center gap-3 px-3.5 py-2.5 rounded-xl text-sm font-medium transition-all ${
                          isActive
                            ? 'bg-[#0F1E36] text-white'
                            : 'text-slate-600 hover:text-[#0F1E36] hover:bg-slate-100'
                        }`}
                      >
                        <Icon size={18} />
                        <span>{item.label}</span>
                      </Link>
                    );
                  })}
              </nav>
            </div>
          </div>
        )}

        {/* Main Content Area */}
        <main className="flex-1 min-w-0">{children}</main>
      </div>
    </div>
  );
}
