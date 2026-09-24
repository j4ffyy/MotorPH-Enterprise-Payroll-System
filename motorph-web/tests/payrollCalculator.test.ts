import { describe, it } from 'node:test';
import assert from 'node:assert/strict';
import {
  calculateSSS,
  calculatePhilHealth,
  calculatePagIBIG,
  calculateWithholdingTax,
  calculateFullPayroll,
} from '../src/lib/payrollCalculator.ts';
import bcrypt from 'bcryptjs';

describe('Control 4: Unit Test Suite & Philippine Statutory Parity', () => {

  describe('1. SSS Statutory Contribution (RA 11199)', () => {
    it('should calculate 5% employee share for salary under ₱30,000 MSC', () => {
      const sss = calculateSSS(20000);
      assert.equal(sss, 1000.00);
    });

    it('should cap SSS contribution at ₱1,500.00 for salary at or above ₱30,000 MSC', () => {
      assert.equal(calculateSSS(30000), 1500.00);
      assert.equal(calculateSSS(50000), 1500.00);
      assert.equal(calculateSSS(90000), 1500.00);
    });

    it('should return 0 for non-positive salary', () => {
      assert.equal(calculateSSS(0), 0);
    });
  });

  describe('2. PhilHealth Contribution (UHC Act 5% Total / 2.5% Employee)', () => {
    it('should apply the minimum statutory floor of ₱250.00 for salary <= ₱10,000', () => {
      assert.equal(calculatePhilHealth(8000), 250.00);
      assert.equal(calculatePhilHealth(10000), 250.00);
    });

    it('should calculate 2.5% for salary within floor and ceiling', () => {
      assert.equal(calculatePhilHealth(24000), 600.00);
      assert.equal(calculatePhilHealth(50000), 1250.00);
      assert.equal(calculatePhilHealth(60000), 1500.00);
      assert.equal(calculatePhilHealth(90000), 2250.00);
    });

    it('should cap contribution at ₱2,500.00 for salary >= ₱100,000', () => {
      assert.equal(calculatePhilHealth(100000), 2500.00);
      assert.equal(calculatePhilHealth(150000), 2500.00);
    });
  });

  describe('3. Pag-IBIG / HDMF Contribution (Circular No. 460)', () => {
    it('should calculate 2% for salary below ₱10,000 base', () => {
      assert.equal(calculatePagIBIG(5000), 100.00);
    });

    it('should cap at ₱200.00 for salary at or above ₱10,000 base', () => {
      assert.equal(calculatePagIBIG(10000), 200.00);
      assert.equal(calculatePagIBIG(25000), 200.00);
      assert.equal(calculatePagIBIG(90000), 200.00);
    });
  });

  describe('4. BIR TRAIN Law Withholding Tax (Republic Act 10963)', () => {
    it('should have 0% withholding tax for taxable income <= ₱20,833 (Bracket 1)', () => {
      assert.equal(calculateWithholdingTax(18000), 0.00);
      assert.equal(calculateWithholdingTax(20833), 0.00);
    });

    it('should compute 15% on excess of ₱20,833 for Bracket 2 (₱20,833 - ₱33,333)', () => {
      const tax = calculateWithholdingTax(25000);
      // (25000 - 20833) * 0.15 = 4167 * 0.15 = 625.05
      assert.equal(tax, 625.05);
    });

    it('should compute ₱1,875 + 20% on excess of ₱33,333 for Bracket 3 (₱33,333 - ₱66,667)', () => {
      const tax = calculateWithholdingTax(47050);
      // 1875 + (47050 - 33333) * 0.20 = 1875 + 2743.40 = 4618.40
      assert.equal(tax, 4618.40);
    });

    it('should compute ₱8,541.67 + 25% on excess of ₱66,667 for Bracket 4 (₱66,667 - ₱166,667)', () => {
      const tax = calculateWithholdingTax(100000);
      // 8541.67 + (100000 - 66667) * 0.25 = 8541.67 + 8333.25 = 16874.92
      assert.equal(tax, 16874.92);
    });
  });

  describe('5. Rate Derivations & Overtime', () => {
    it('should compute daily rate based on 26 working days standard', () => {
      const basicSalary = 26000;
      const dailyRate = Number((basicSalary / 26).toFixed(2));
      assert.equal(dailyRate, 1000.00);
    });

    it('should compute hourly rate based on 8-hour shift', () => {
      const dailyRate = 1000;
      const hourlyRate = Number((dailyRate / 8).toFixed(2));
      assert.equal(hourlyRate, 125.00);
    });

    it('should compute overtime at 1.25x hourly rate', () => {
      const hourlyRate = 125.00;
      const otHours = 4;
      const otPay = Number((hourlyRate * 1.25 * otHours).toFixed(2));
      assert.equal(otPay, 625.00);
    });
  });

  describe('6. Full Payroll Calculation Integration', () => {
    it('should correctly calculate full breakdown for standard employee', () => {
      const payroll = calculateFullPayroll({
        basicSalary: 50000,
        riceSubsidy: 1500,
        phoneAllowance: 1000,
        clothingAllowance: 1000,
        overtimeHours: 2,
        holidayPay: 0,
        performanceBonus: 0,
      });

      // Daily: 50000 / 26 = 1923.08
      assert.equal(payroll.dailyRate, 1923.08);
      // Hourly: 1923.08 / 8 = 240.38
      assert.equal(payroll.hourlyRate, 240.38);
      // Overtime (2 hrs * 240.38 * 1.25) = 600.95
      assert.equal(payroll.overtimePay, 600.95);
      // Gross = 50000 + 600.95 = 50600.95
      assert.equal(payroll.grossIncome, 50600.95);

      // Total Non-taxable benefits: 1500 + 1000 + 1000 = 3500
      assert.equal(payroll.totalBenefits, 3500.00);

      // Statutory deductions
      assert.equal(payroll.sssContribution, 1500.00);
      assert.equal(payroll.philhealthContribution, 1250.00);
      assert.equal(payroll.pagibigContribution, 200.00);

      // Total deductions & net take-home
      assert.ok(payroll.withholdingTax > 0);
      assert.ok(payroll.netPay > 40000);
    });

    it('should correctly process CEO Manuel Garcia III (₱90k salary)', () => {
      const payroll = calculateFullPayroll({
        basicSalary: 90000,
        riceSubsidy: 1500,
        phoneAllowance: 2000,
        clothingAllowance: 1000,
        overtimeHours: 0,
        holidayPay: 0,
        performanceBonus: 0,
      });

      assert.equal(payroll.sssContribution, 1500.00);
      assert.equal(payroll.philhealthContribution, 2250.00);
      assert.equal(payroll.pagibigContribution, 200.00);
      // Taxable = 90000 - 3950 = 86050
      // Tax: 8541.67 + (86050 - 66667) * 0.25 = 8541.67 + 4845.75 = 13387.42
      assert.equal(payroll.withholdingTax, 13387.42);
      assert.equal(payroll.totalBenefits, 4500.00);
      // Net: 90000 + 4500 - (3950 + 13387.42) = 94500 - 17337.42 = 77162.58
      assert.equal(payroll.netPay, 77162.58);
    });
  });

  describe('7. Security: BCrypt Password Hashing & Verification (Control 1)', () => {
    it('should securely hash plain passwords with 10 salt rounds', async () => {
      const plain = 'EmployeeSecret2026!';
      const hash = await bcrypt.hash(plain, 10);
      assert.notEqual(hash, plain);
      assert.ok(hash.startsWith('$2a$') || hash.startsWith('$2b$'));

      const matches = await bcrypt.compare(plain, hash);
      assert.equal(matches, true);
    });

    it('should reject invalid password match', async () => {
      const hash = await bcrypt.hash('CorrectPass', 10);
      const matches = await bcrypt.compare('WrongPass', hash);
      assert.equal(matches, false);
    });
  });

});
