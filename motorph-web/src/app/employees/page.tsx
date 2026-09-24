'use client';

import React, { useState, useEffect } from 'react';
import { useRouter } from 'next/navigation';
import AppShell from '@/components/AppShell';
import {
  Users,
  Search,
  Plus,
  Filter,
  CheckCircle2,
  XCircle,
  Eye,
  Shield,
  CreditCard,
  Building,
  Lock,
} from 'lucide-react';
import { formatPHP } from '@/lib/payrollCalculator';

export default function EmployeesPage() {
  const router = useRouter();
  const [currentUser, setCurrentUser] = useState<any>(null);
  const [employees, setEmployees] = useState<any[]>([]);
  const [search, setSearch] = useState('');
  const [selectedDept, setSelectedDept] = useState('');
  const [loading, setLoading] = useState(true);
  const [selectedEmp, setSelectedEmp] = useState<any | null>(null);
  const [showAddModal, setShowAddModal] = useState(false);

  // New employee form state
  const [newForm, setNewForm] = useState({
    eid: '',
    firstName: '',
    lastName: '',
    username: '',
    password: 'Password123!',
    status: 'Regular',
    role: 'EMPLOYEE',
    designationId: '15',
    basicSalary: '25000',
    riceSubsidy: '1500',
    phoneAllowance: '500',
    clothingAllowance: '500',
    sssNum: '',
    philhealthNum: '',
    tinNum: '',
    pagibigNum: '',
  });

  useEffect(() => {
    fetchSession();
    fetchEmployees();
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
    } catch {
      router.push('/');
    }
  };

  const fetchEmployees = async () => {
    setLoading(true);
    try {
      const res = await fetch('/api/employees');
      const data = await res.json();
      if (res.ok) {
        setEmployees(data.employees || []);
      }
    } catch (err) {
      console.error(err);
    } finally {
      setLoading(false);
    }
  };

  const openAddModal = () => {
    const maxEid = employees.length > 0 ? Math.max(...employees.map((e) => Number(e.eid) || 0)) : 10000;
    const nextEid = maxEid + 1;
    setNewForm({
      eid: String(nextEid),
      firstName: '',
      lastName: '',
      username: '',
      password: 'Password123!',
      status: 'Regular',
      role: 'EMPLOYEE',
      designationId: '15',
      basicSalary: '25000',
      riceSubsidy: '1500',
      phoneAllowance: '500',
      clothingAllowance: '500',
      sssNum: '',
      philhealthNum: '',
      tinNum: '',
      pagibigNum: '',
    });
    setShowAddModal(true);
  };

  const handleCreateEmployee = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const res = await fetch('/api/employees', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newForm),
      });
      const data = await res.json();
      if (!res.ok) throw new Error(data.error || 'Failed to create employee');

      setShowAddModal(false);
      fetchEmployees();
      alert('Employee created successfully!');
    } catch (err: any) {
      alert(err.message || 'Error creating employee');
    }
  };

  const filteredEmployees = employees.filter((emp) => {
    const query = search.toLowerCase();
    const fullName = `${emp.firstName} ${emp.lastName}`.toLowerCase();
    const matchSearch =
      fullName.includes(query) ||
      String(emp.eid).includes(query) ||
      (emp.username && emp.username.toLowerCase().includes(query));

    const matchDept = selectedDept
      ? emp.designation?.department?.name === selectedDept
      : true;

    return matchSearch && matchDept;
  });

  if (!currentUser) return null;

  return (
    <AppShell user={currentUser}>
      <div className="space-y-6">
        {/* Header Bar */}
        <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div>
            <h1 className="text-2xl font-bold text-[#0F1E36] flex items-center gap-2.5">
              <Users className="text-[#4166F5]" size={26} />
              Employee Directory
            </h1>
            <p className="text-xs text-slate-500 mt-1">
              Manage workforce records, position assignments, and statutory credentials.
            </p>
          </div>

          <button
            onClick={openAddModal}
            className="flex items-center gap-2 px-4 py-2.5 rounded-xl bg-[#0F1E36] hover:bg-[#1A2E4E] text-white font-medium text-xs shadow-md shadow-[#0F1E36]/20 transition-all self-start sm:self-auto cursor-pointer"
          >
            <Plus size={16} />
            <span>Add New Employee</span>
          </button>
        </div>

        {/* Filter & Search Bar */}
        <div className="flex flex-col sm:flex-row gap-3">
          <div className="relative flex-1">
            <Search
              size={16}
              className="absolute left-3.5 top-1/2 -translate-y-1/2 text-slate-400"
            />
            <input
              type="text"
              value={search}
              onChange={(e) => setSearch(e.target.value)}
              placeholder="Search by name, EID, or work email..."
              className="w-full pl-9 pr-4 py-2 rounded-xl bg-white border border-slate-200 text-slate-900 placeholder:text-slate-400 text-xs focus:outline-none focus:border-[#4166F5] shadow-xs"
            />
          </div>

          <div className="flex items-center gap-2">
            <Filter size={14} className="text-slate-400" />
            <select
              value={selectedDept}
              onChange={(e) => setSelectedDept(e.target.value)}
              className="px-3 py-2 rounded-xl bg-white border border-slate-200 text-slate-800 text-xs focus:outline-none focus:border-[#4166F5] shadow-xs"
            >
              <option value="">All Departments</option>
              <option value="Executive">Executive</option>
              <option value="Human Resources">Human Resources</option>
              <option value="Accounting">Accounting</option>
              <option value="Finance">Finance</option>
              <option value="IT">IT</option>
              <option value="Sales">Sales</option>
            </select>
          </div>
        </div>

        {/* Employee Table - Clean White */}
        <div className="bg-white border border-slate-200 rounded-2xl overflow-hidden shadow-sm">
          <div className="overflow-x-auto">
            <table className="w-full text-left border-collapse text-xs">
              <thead>
                <tr className="border-b border-slate-200 bg-slate-50 text-slate-600 font-semibold uppercase tracking-wider text-[10px]">
                  <th className="py-3 px-4">EID</th>
                  <th className="py-3 px-4">Employee Name</th>
                  <th className="py-3 px-4">Department & Position</th>
                  <th className="py-3 px-4">Status</th>
                  <th className="py-3 px-4">Basic Salary</th>
                  <th className="py-3 px-4">Role</th>
                  <th className="py-3 px-4 text-right">Actions</th>
                </tr>
              </thead>
              <tbody className="divide-y divide-slate-100 text-slate-700">
                {loading ? (
                  <tr>
                    <td colSpan={7} className="py-8 text-center text-slate-400">
                      Loading workforce directory...
                    </td>
                  </tr>
                ) : filteredEmployees.length === 0 ? (
                  <tr>
                    <td colSpan={7} className="py-8 text-center text-slate-400">
                      No matching employee records found.
                    </td>
                  </tr>
                ) : (
                  filteredEmployees.map((emp) => (
                    <tr key={emp.eid} className="hover:bg-slate-50 transition">
                      <td className="py-3 px-4 font-mono font-semibold text-slate-500">
                        #{emp.eid}
                      </td>
                      <td className="py-3 px-4">
                        <div className="font-semibold text-[#0F1E36]">
                          {emp.lastName}, {emp.firstName}
                        </div>
                        <div className="text-[11px] text-slate-400 truncate max-w-[180px]">
                          {emp.username}
                        </div>
                      </td>
                      <td className="py-3 px-4">
                        <div className="text-slate-800 font-medium">{emp.designation?.name}</div>
                        <div className="text-[11px] text-slate-500">
                          {emp.designation?.department?.name}
                        </div>
                      </td>
                      <td className="py-3 px-4">
                        <span className="inline-flex items-center gap-1 px-2 py-0.5 rounded-full text-[10px] font-medium bg-emerald-50 text-emerald-700 border border-emerald-200">
                          <CheckCircle2 size={10} />
                          {emp.status}
                        </span>
                      </td>
                      <td className="py-3 px-4 font-mono font-medium text-[#0F1E36]">
                        {formatPHP(emp.components?.basicSalary || 0)}
                      </td>
                      <td className="py-3 px-4">
                        <span className="px-2 py-0.5 rounded-full text-[10px] font-semibold bg-slate-100 text-slate-700 border border-slate-200">
                          {emp.role}
                        </span>
                      </td>
                      <td className="py-3 px-4 text-right">
                        <button
                          onClick={() => setSelectedEmp(emp)}
                          className="px-2.5 py-1 rounded-lg bg-slate-100 hover:bg-[#0F1E36] text-slate-700 hover:text-white transition flex items-center gap-1.5 ml-auto text-[11px] font-medium"
                        >
                          <Eye size={12} />
                          <span>View Details</span>
                        </button>
                      </td>
                    </tr>
                  ))
                )}
              </tbody>
            </table>
          </div>
        </div>

        {/* Employee Detail Modal - Crisp White */}
        {selectedEmp && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-xs">
            <div className="bg-white border border-slate-200 rounded-2xl w-full max-w-xl max-h-[90vh] overflow-y-auto p-6 shadow-2xl">
              <div className="flex items-center justify-between pb-4 border-b border-slate-100">
                <div>
                  <h3 className="text-lg font-bold text-[#0F1E36]">
                    {selectedEmp.firstName} {selectedEmp.lastName}
                  </h3>
                  <p className="text-xs text-slate-500">
                    EID: #{selectedEmp.eid} &bull; {selectedEmp.designation?.name}
                  </p>
                </div>
                <button
                  onClick={() => setSelectedEmp(null)}
                  className="p-1 rounded-lg text-slate-400 hover:text-slate-700"
                >
                  <XCircle size={20} />
                </button>
              </div>

              <div className="mt-4 space-y-4 text-xs">
                {/* Contact & Personal */}
                <div>
                  <h4 className="font-bold text-[#0F1E36] mb-2 flex items-center gap-1.5">
                    <Building size={14} className="text-[#4166F5]" />
                    Personal & Organization Details
                  </h4>
                  <div className="grid grid-cols-2 gap-2 bg-slate-50 p-3 rounded-xl border border-slate-200">
                    <div>
                      <span className="text-slate-500 block">Department</span>
                      <span className="text-slate-800 font-medium">{selectedEmp.designation?.department?.name}</span>
                    </div>
                    <div>
                      <span className="text-slate-500 block">Supervisor</span>
                      <span className="text-slate-800 font-medium">{selectedEmp.supervisor?.name || 'Executive Level'}</span>
                    </div>
                    <div>
                      <span className="text-slate-500 block">Work Email</span>
                      <span className="text-slate-800 font-medium truncate block">{selectedEmp.username}</span>
                    </div>
                    <div>
                      <span className="text-slate-500 block">Phone</span>
                      <span className="text-slate-800 font-medium">{selectedEmp.phoneNumber || 'N/A'}</span>
                    </div>
                    <div className="col-span-2">
                      <span className="text-slate-500 block">Address</span>
                      <span className="text-slate-800 font-medium">{selectedEmp.address || 'N/A'}</span>
                    </div>
                  </div>
                </div>

                {/* Payroll Components */}
                <div>
                  <h4 className="font-bold text-[#0F1E36] mb-2 flex items-center gap-1.5">
                    <CreditCard size={14} className="text-emerald-600" />
                    Compensation Breakdown
                  </h4>
                  <div className="grid grid-cols-3 gap-2 bg-slate-50 p-3 rounded-xl border border-slate-200">
                    <div>
                      <span className="text-slate-500 block">Basic Salary</span>
                      <span className="font-mono text-emerald-700 font-bold">
                        {formatPHP(selectedEmp.components?.basicSalary || 0)}
                      </span>
                    </div>
                    <div>
                      <span className="text-slate-500 block">Daily Rate</span>
                      <span className="font-mono text-slate-700 font-medium">
                        {formatPHP((selectedEmp.components?.basicSalary || 0) / 26)}
                      </span>
                    </div>
                    <div>
                      <span className="text-slate-500 block">Hourly Rate</span>
                      <span className="font-mono text-slate-700 font-medium">
                        {formatPHP(selectedEmp.components?.hourlyRate || 0)}
                      </span>
                    </div>
                    <div>
                      <span className="text-slate-500 block">Rice Subsidy</span>
                      <span className="text-slate-700">{formatPHP(selectedEmp.components?.riceSubsidy || 0)}</span>
                    </div>
                    <div>
                      <span className="text-slate-500 block">Phone Allowance</span>
                      <span className="text-slate-700">{formatPHP(selectedEmp.components?.phoneAllowance || 0)}</span>
                    </div>
                    <div>
                      <span className="text-slate-500 block">Clothing Allowance</span>
                      <span className="text-slate-700">{formatPHP(selectedEmp.components?.clothingAllowance || 0)}</span>
                    </div>
                  </div>
                </div>

                {/* Government IDs */}
                <div>
                  <h4 className="font-bold text-[#0F1E36] mb-2 flex items-center gap-1.5">
                    <Shield size={14} className="text-cyan-600" />
                    Philippine Statutory Identifiers
                  </h4>
                  <div className="grid grid-cols-2 gap-2 bg-slate-50 p-3 rounded-xl border border-slate-200 font-mono">
                    <div>
                      <span className="text-slate-500 font-sans block">SSS Number</span>
                      <span className="text-slate-800 font-semibold">{selectedEmp.govIds?.sssNum || 'None'}</span>
                    </div>
                    <div>
                      <span className="text-slate-500 font-sans block">PhilHealth ID</span>
                      <span className="text-slate-800 font-semibold">{selectedEmp.govIds?.philhealthNum || 'None'}</span>
                    </div>
                    <div>
                      <span className="text-slate-500 font-sans block">TIN</span>
                      <span className="text-slate-800 font-semibold">{selectedEmp.govIds?.tinNum || 'None'}</span>
                    </div>
                    <div>
                      <span className="text-slate-500 font-sans block">Pag-IBIG Number</span>
                      <span className="text-slate-800 font-semibold">{selectedEmp.govIds?.pagibigNum || 'None'}</span>
                    </div>
                  </div>
                </div>
              </div>

              <div className="mt-6 flex justify-end">
                <button
                  onClick={() => setSelectedEmp(null)}
                  className="px-4 py-2 rounded-xl bg-slate-100 hover:bg-slate-200 text-slate-700 text-xs font-semibold"
                >
                  Close
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Add Employee Modal - Crisp White */}
        {showAddModal && (
          <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/50 backdrop-blur-xs">
            <div className="bg-white border border-slate-200 rounded-2xl w-full max-w-xl max-h-[90vh] overflow-y-auto p-6 shadow-2xl">
              <div className="flex items-center justify-between pb-4 border-b border-slate-100">
                <h3 className="text-lg font-bold text-[#0F1E36] flex items-center gap-2">
                  <Plus size={18} className="text-[#4166F5]" />
                  Add New Employee
                </h3>
                <button
                  onClick={() => setShowAddModal(false)}
                  className="p-1 rounded-lg text-slate-400 hover:text-slate-700"
                >
                  <XCircle size={20} />
                </button>
              </div>

              <form onSubmit={handleCreateEmployee} className="mt-4 space-y-4 text-xs">
                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <div className="flex items-center justify-between mb-1">
                      <label className="text-slate-600 font-medium">Employee ID</label>
                      <span className="text-[10px] font-semibold text-emerald-700 bg-emerald-50 px-2 py-0.5 rounded-full border border-emerald-200 flex items-center gap-1">
                        <Lock size={10} /> Auto-assigned (+1)
                      </span>
                    </div>
                    <input
                      type="number"
                      readOnly
                      disabled
                      value={newForm.eid}
                      className="w-full px-3 py-2 rounded-xl bg-slate-100 text-slate-500 border border-slate-200 font-mono font-bold text-xs cursor-not-allowed select-none shadow-xs"
                      title="Employee ID is locked and auto-incremented based on the latest workforce record."
                    />
                  </div>
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">Role *</label>
                    <select
                      value={newForm.role}
                      onChange={(e) => setNewForm({ ...newForm, role: e.target.value })}
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    >
                      <option value="EMPLOYEE">Employee (ESS)</option>
                      <option value="PAYROLL">Payroll Officer</option>
                      <option value="HR">HR Manager</option>
                      <option value="ADMIN">System Administrator</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">First Name *</label>
                    <input
                      type="text"
                      required
                      value={newForm.firstName}
                      onChange={(e) => setNewForm({ ...newForm, firstName: e.target.value })}
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">Last Name *</label>
                    <input
                      type="text"
                      required
                      value={newForm.lastName}
                      onChange={(e) => setNewForm({ ...newForm, lastName: e.target.value })}
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">Work Email / Username *</label>
                    <input
                      type="email"
                      required
                      value={newForm.username}
                      onChange={(e) => setNewForm({ ...newForm, username: e.target.value })}
                      placeholder="user@motorph.com"
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">Initial Password *</label>
                    <input
                      type="text"
                      required
                      value={newForm.password}
                      onChange={(e) => setNewForm({ ...newForm, password: e.target.value })}
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">Basic Monthly Salary (PHP) *</label>
                    <input
                      type="number"
                      required
                      value={newForm.basicSalary}
                      onChange={(e) => setNewForm({ ...newForm, basicSalary: e.target.value })}
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">Employment Status</label>
                    <select
                      value={newForm.status}
                      onChange={(e) => setNewForm({ ...newForm, status: e.target.value })}
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    >
                      <option value="Regular">Regular</option>
                      <option value="Probationary">Probationary</option>
                    </select>
                  </div>
                </div>

                <div className="grid grid-cols-2 gap-3">
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">SSS Number</label>
                    <input
                      type="text"
                      value={newForm.sssNum}
                      onChange={(e) => setNewForm({ ...newForm, sssNum: e.target.value })}
                      placeholder="XX-XXXXXXX-X"
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                  <div>
                    <label className="text-slate-600 font-medium block mb-1">TIN</label>
                    <input
                      type="text"
                      value={newForm.tinNum}
                      onChange={(e) => setNewForm({ ...newForm, tinNum: e.target.value })}
                      placeholder="XXX-XXX-XXX-000"
                      className="w-full px-3 py-2 rounded-xl bg-slate-50 border border-slate-200 text-slate-900 focus:outline-none focus:border-[#4166F5]"
                    />
                  </div>
                </div>

                <div className="pt-4 flex justify-end gap-2 border-t border-slate-100">
                  <button
                    type="button"
                    onClick={() => setShowAddModal(false)}
                    className="px-4 py-2 rounded-xl bg-slate-100 text-slate-700 hover:bg-slate-200 font-medium"
                  >
                    Cancel
                  </button>
                  <button
                    type="submit"
                    className="px-5 py-2 rounded-xl bg-[#0F1E36] hover:bg-[#1A2E4E] text-white font-medium shadow-md shadow-[#0F1E36]/20"
                  >
                    Save Employee
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
