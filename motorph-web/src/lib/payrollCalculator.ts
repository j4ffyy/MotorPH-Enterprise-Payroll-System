/**
 * MotorPH Payroll Calculation Engine
 * Replaces legacy Java SalaryCalculator.java with 100% mathematical parity.
 * Aligned with Philippine Labor Code & MO-IT153 Security Implementation Plan (Control 4: Accuracy & Testability).
 */

export interface EmployeePayrollInput {
  basicSalary: number;
  riceSubsidy?: number;
  phoneAllowance?: number;
  clothingAllowance?: number;
  daysWorked?: number;
  overtimeHours?: number;
  holidayPay?: number;
  performanceBonus?: number;
}

export interface PayrollBreakdown {
  basicSalary: number;
  dailyRate: number;
  hourlyRate: number;
  overtimePay: number;
  holidayPay: number;
  performanceBonus: number;
  grossIncome: number;
  riceSubsidy: number;
  phoneAllowance: number;
  clothingAllowance: number;
  totalBenefits: number;
  sssContribution: number;
  philhealthContribution: number;
  pagibigContribution: number;
  withholdingTax: number;
  totalDeductions: number;
  netPay: number;
}

/**
 * Calculates SSS Contribution (5% employee share, capped at ₱30,000 Monthly Salary Credit = max ₱1,500.00)
 * Aligned with Republic Act 11199 & Philippine Tax Microservice Design Guide
 */
export function calculateSSS(monthlyBasic: number): number {
  const sssRate = 0.05;
  const maxSalaryCredit = 30000;
  const cappedSalary = Math.min(Math.max(0, monthlyBasic), maxSalaryCredit);
  return Number((cappedSalary * sssRate).toFixed(2));
}

/**
 * Calculates PhilHealth Contribution (2.5% employee share under Universal Health Care Act)
 * Minimum: ₱250.00 (floor at ₱10,000)
 * Maximum: ₱2,500.00 (ceiling at ₱100,000)
 */
export function calculatePhilHealth(monthlyBasic: number): number {
  const rate = 0.025; // 2.5% Employee share (5% total premium split 50/50)
  if (monthlyBasic <= 10000) return 250.0;
  if (monthlyBasic >= 100000) return 2500.0;
  return Number((monthlyBasic * rate).toFixed(2));
}

/**
 * Calculates Pag-IBIG Contribution (2% employee share, capped at ₱200.00)
 * Aligned with Pag-IBIG Circular No. 460 (effective 2024, max salary base ₱10,000)
 */
export function calculatePagIBIG(monthlyBasic: number): number {
  const rate = 0.02;
  const maxSalaryBase = 10000;
  const cappedSalary = Math.min(Math.max(0, monthlyBasic), maxSalaryBase);
  return Number(Math.min(cappedSalary * rate, 200.0).toFixed(2));
}

/**
 * Calculates Withholding Tax under Philippine TRAIN Law (Republic Act 10963)
 */
export function calculateWithholdingTax(taxableIncome: number): number {
  if (taxableIncome <= 20833.0) {
    return 0.0;
  } else if (taxableIncome <= 33333.0) {
    return Number(((taxableIncome - 20833.0) * 0.15).toFixed(2));
  } else if (taxableIncome <= 66667.0) {
    return Number((1875.0 + (taxableIncome - 33333.0) * 0.20).toFixed(2));
  } else if (taxableIncome <= 166667.0) {
    return Number((8541.67 + (taxableIncome - 66667.0) * 0.25).toFixed(2));
  } else if (taxableIncome <= 666667.0) {
    return Number((33541.67 + (taxableIncome - 166667.0) * 0.30).toFixed(2));
  } else {
    return Number((183541.67 + (taxableIncome - 666667.0) * 0.35).toFixed(2));
  }
}

/**
 * Complete payroll calculation function
 */
export function calculateFullPayroll(input: EmployeePayrollInput): PayrollBreakdown {
  const basicSalary = Number(input.basicSalary) || 0;
  const riceSubsidy = Number(input.riceSubsidy) || 0;
  const phoneAllowance = Number(input.phoneAllowance) || 0;
  const clothingAllowance = Number(input.clothingAllowance) || 0;
  const overtimeHours = Number(input.overtimeHours) || 0;
  const holidayPay = Number(input.holidayPay) || 0;
  const performanceBonus = Number(input.performanceBonus) || 0;

  // Rate calculations (26 working days standard)
  const dailyRate = Number((basicSalary / 26).toFixed(2));
  const hourlyRate = Number((dailyRate / 8).toFixed(2));

  // Overtime pay (1.25x hourly rate)
  const overtimePay = Number((hourlyRate * 1.25 * overtimeHours).toFixed(2));

  // Gross Income
  const grossIncome = Number((basicSalary + overtimePay + holidayPay + performanceBonus).toFixed(2));

  // Total Non-taxable Benefits
  const totalBenefits = Number((riceSubsidy + phoneAllowance + clothingAllowance).toFixed(2));

  // Statutory Deductions
  const sssContribution = Number(calculateSSS(basicSalary).toFixed(2));
  const philhealthContribution = Number(calculatePhilHealth(basicSalary).toFixed(2));
  const pagibigContribution = Number(calculatePagIBIG(basicSalary).toFixed(2));

  // Taxable Income = Gross Income - (SSS + PhilHealth + Pag-IBIG)
  const mandatoryDeductions = sssContribution + philhealthContribution + pagibigContribution;
  const taxableIncome = Math.max(0, grossIncome - mandatoryDeductions);
  const withholdingTax = Number(calculateWithholdingTax(taxableIncome).toFixed(2));

  const totalDeductions = Number((mandatoryDeductions + withholdingTax).toFixed(2));
  const netPay = Number((grossIncome + totalBenefits - totalDeductions).toFixed(2));

  return {
    basicSalary,
    dailyRate,
    hourlyRate,
    overtimePay,
    holidayPay,
    performanceBonus,
    grossIncome,
    riceSubsidy,
    phoneAllowance,
    clothingAllowance,
    totalBenefits,
    sssContribution,
    philhealthContribution,
    pagibigContribution,
    withholdingTax,
    totalDeductions,
    netPay,
  };
}

export function formatPHP(value: number): string {
  return new Intl.NumberFormat('en-PH', {
    style: 'currency',
    currency: 'PHP',
    minimumFractionDigits: 2,
  }).format(value);
}
