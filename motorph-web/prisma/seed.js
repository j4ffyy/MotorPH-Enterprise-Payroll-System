// Seeding script for MotorPH Web Migration
// Implements Control 1 (Externalized config) and Security Best Practice: Bcrypt Hashing for all accounts
// Full Data Parity with 2024 Legacy Project (MotorPH_EPS_AOOP_Latest / payrollsystem_db_v10.sql)
const { PrismaClient } = require('@prisma/client');
const bcrypt = require('bcryptjs');

const prisma = new PrismaClient();

async function main() {
  console.log('--- Starting MotorPH Database Seeding ---');

  // 1. Departments
  const departments = [
    { id: 1, name: 'Executive' },
    { id: 2, name: 'Human Resources' },
    { id: 3, name: 'Accounting' },
    { id: 4, name: 'Finance' },
    { id: 5, name: 'IT' },
    { id: 6, name: 'Sales' },
  ];

  for (const dep of departments) {
    await prisma.department.upsert({
      where: { id: dep.id },
      update: { name: dep.name },
      create: dep,
    });
  }
  console.log('✓ Departments seeded');

  // 2. Designations
  const designations = [
    { id: 1, name: 'Chief Executive Officer', departmentId: 1 },
    { id: 2, name: 'Chief Operating Officer', departmentId: 1 },
    { id: 3, name: 'Chief Finance Officer', departmentId: 1 },
    { id: 4, name: 'Chief Marketing Officer', departmentId: 1 },
    { id: 5, name: 'IT Operations and Systems', departmentId: 5 },
    { id: 6, name: 'HR Manager', departmentId: 2 },
    { id: 7, name: 'HR Team Leader', departmentId: 2 },
    { id: 8, name: 'HR Rank and File', departmentId: 2 },
    { id: 9, name: 'Accounting Head', departmentId: 3 },
    { id: 10, name: 'Payroll Manager', departmentId: 4 },
    { id: 11, name: 'Payroll Team Leader', departmentId: 4 },
    { id: 12, name: 'Payroll Rank and File', departmentId: 4 },
    { id: 13, name: 'Account Manager', departmentId: 3 },
    { id: 14, name: 'Account Team Leader', departmentId: 3 },
    { id: 15, name: 'Account Rank and File', departmentId: 3 },
    { id: 16, name: 'Sales & Marketing', departmentId: 6 },
    { id: 17, name: 'Supply Chain and Logistics', departmentId: 6 },
    { id: 18, name: 'Customer Service and Relations', departmentId: 6 },
  ];

  for (const des of designations) {
    await prisma.designation.upsert({
      where: { id: des.id },
      update: { name: des.name, departmentId: des.departmentId },
      create: des,
    });
  }
  console.log('✓ Designations seeded');

  // 3. Supervisors
  const supervisors = [
    { id: 1, name: 'Garcia, Manuel III' },
    { id: 2, name: 'Lim, Antonio' },
    { id: 3, name: 'Villanueva, Andrea Mae' },
    { id: 4, name: 'San, Jose Brad' },
    { id: 5, name: 'Aquino, Bianca Sofia ' },
    { id: 6, name: 'Alvaro, Roderick' },
    { id: 7, name: 'Salcedo, Anthony' },
    { id: 8, name: 'Romualdez, Fredrick ' },
    { id: 9, name: 'Mata, Christian' },
    { id: 10, name: 'De Leon, Selena' },
    { id: 11, name: 'Reyes, Isabella' },
  ];

  for (const sup of supervisors) {
    await prisma.supervisor.upsert({
      where: { id: sup.id },
      update: { name: sup.name },
      create: sup,
    });
  }
  console.log('✓ Supervisors seeded');

  // 4. Deduction Types
  const deductionTypes = [
    { id: 1, name: 'SSS' },
    { id: 2, name: 'PhilHealth' },
    { id: 3, name: 'PagIBIG' },
    { id: 4, name: 'Withholding_Tax' },
  ];

  for (const dt of deductionTypes) {
    await prisma.deductionType.upsert({
      where: { id: dt.id },
      update: { name: dt.name },
      create: dt,
    });
  }
  console.log('✓ Deduction Types seeded');

  // 5. Pre-compute standard password hash (salt rounds 10)
  const defaultHash = await bcrypt.hash('123abc', 10);
  const adminHash = await bcrypt.hash('admin123', 10);

  // 6. Employees Data (All 34 Employees from the 2024 database)
  const rawEmployees = [
    { eid: 10001, lastName: 'Garcia', firstName: 'Manuel III', birthday: '1983-10-11', address: 'Valero Carpark Building Valero Street 1227, Makati City', phone: '966-860-270', username: 'mgarcia@motorph.com', plainPass: '123abc', status: 'Regular', desId: 1, supId: null, role: 'ADMIN' },
    { eid: 10002, lastName: 'Lim', firstName: 'Antonio', birthday: '1988-06-19', address: 'San Antonio De Padua 2, Dasmarinas, Cavite', phone: '171-867-411', username: 'alim@motorph.com', plainPass: 'adminako', status: 'Regular', desId: 2, supId: 1, role: 'ADMIN' },
    { eid: 10003, lastName: 'Aquino', firstName: 'Bianca Sofia', birthday: '1989-08-04', address: 'Timog Avenue Cor. Quezon Avenue 1100, Quezon City', phone: '966-889-370', username: 'bsaquino@motorph.com', plainPass: '123abc', status: 'Regular', desId: 3, supId: 1, role: 'ADMIN' },
    { eid: 10004, lastName: 'Reyes', firstName: 'Isabella', birthday: '1994-06-16', address: '460 Solanda Street Intramuros 1000, Manila', phone: '786-868-477', username: 'ireyes@motorph.com', plainPass: 'admin', status: 'Regular', desId: 4, supId: 1, role: 'ADMIN' },
    { eid: 10005, lastName: 'Hernandez', firstName: 'Eduard', birthday: '1989-09-23', address: 'National Highway, Gingoog, Misamis Occidental', phone: '088-861-012', username: 'ehernandez@motorph.com', plainPass: 'admin', status: 'Regular', desId: 5, supId: 2, role: 'ADMIN' },
    { eid: 10006, lastName: 'Villanueva', firstName: 'Andrea Mae', birthday: '1988-02-14', address: 'Poblacion, Las Piñas 4783 Dinagat Islands', phone: '918-621-603', username: 'amvillanueva@motorph.com', plainPass: '123abc', status: 'Regular', desId: 6, supId: 2, role: 'HR' },
    { eid: 10007, lastName: 'San Jose', firstName: 'Brad', birthday: '1996-03-15', address: '99 Strosin Hills, Poblacion, Bislig 5340 Tawi-Tawi', phone: '797-009-261', username: 'bsanjose@motorph.com', plainPass: '123abc', status: 'Regular', desId: 7, supId: 3, role: 'HR' },
    { eid: 10008, lastName: 'Romualdez', firstName: 'Alice', birthday: '1992-05-14', address: '12A/33 Upton Isle Apt. 420, Roxas City', phone: '983-606-799', username: 'aromualdez@motorph.com', plainPass: '123abc', status: 'Regular', desId: 8, supId: 4, role: 'HR' },
    { eid: 10009, lastName: 'Atienza', firstName: 'Rosie', birthday: '1948-09-24', address: '90A Dibbert Terrace, San Lorenzo Davao del Norte', phone: '266-036-427', username: 'ratienza@motorph.com', plainPass: 'emp123', status: 'Regular', desId: 8, supId: 4, role: 'EMPLOYEE' },
    { eid: 10010, lastName: 'Alvaro', firstName: 'Roderick', birthday: '1988-03-30', address: '#284 T. Morato corner, Scout Rallos, QC', phone: '053-381-386', username: 'ralvaro@motorph.com', plainPass: '123abc', status: 'Regular', desId: 9, supId: 5, role: 'PAYROLL' },
    { eid: 10011, lastName: 'Salcedo', firstName: 'Anthony', birthday: '1993-09-14', address: '93/54 Shanahan Alley Apt. 183, Santo Tomas Masbate', phone: '070-766-300', username: 'asalcedo@motorph.com', plainPass: '123abc', status: 'Regular', desId: 10, supId: 6, role: 'PAYROLL' },
    { eid: 10012, lastName: 'Lopez', firstName: 'Josie', birthday: '1987-01-14', address: '49 Springs Apt. 266, Poblacion, Taguig', phone: '478-355-427', username: 'jlopez@motorph.com', plainPass: '123abc', status: 'Regular', desId: 11, supId: 7, role: 'PAYROLL' },
    { eid: 10013, lastName: 'Farala', firstName: 'Martha', birthday: '1942-01-11', address: '42/25 Sawayn Stream, Ubay Zamboanga', phone: '329-034-366', username: 'mfarala@motorph.com', plainPass: '123abc', status: 'Regular', desId: 12, supId: 7, role: 'PAYROLL' },
    { eid: 10014, lastName: 'Martinez', firstName: 'Leila', birthday: '1970-07-11', address: '37/46 Kulas Roads, Maragondon Quirino', phone: '877-110-749', username: 'lmartinez@motorph.com', plainPass: 'employee', status: 'Regular', desId: 12, supId: 7, role: 'PAYROLL' },
    { eid: 10015, lastName: 'Romualdez', firstName: 'Fredrick', birthday: '1985-03-10', address: '22A/52 Lubowitz Meadows, Pililla Zambales', phone: '023-079-009', username: 'fromualdez@motorph.com', plainPass: '123abc', status: 'Regular', desId: 13, supId: 2, role: 'EMPLOYEE' },
    { eid: 10016, lastName: 'Mata', firstName: 'Christian', birthday: '1987-10-21', address: '90 OKeefe Spur Apt. 379, Catigbian Sulu', phone: '783-776-744', username: 'cmata@motorph.com', plainPass: '123abc', status: 'Regular', desId: 14, supId: 8, role: 'EMPLOYEE' },
    { eid: 10017, lastName: 'De Leon', firstName: 'Selena', birthday: '1975-02-20', address: '89A Armstrong Trace, Compostela Maguindanao', phone: '975-432-139', username: 'sdeleon@motorph.com', plainPass: '123abc', status: 'Regular', desId: 14, supId: 8, role: 'EMPLOYEE' },
    { eid: 10018, lastName: 'San Jose', firstName: 'Allison', birthday: '1986-06-24', address: '08 Grant Drive Suite 406, Iloilo City', phone: '179-075-129', username: 'asanjose@motorph.com', plainPass: '123abc', status: 'Regular', desId: 15, supId: 9, role: 'EMPLOYEE' },
    { eid: 10019, lastName: 'Rosario', firstName: 'Cydney', birthday: '1996-10-06', address: '93A/21 Berge Points, Tapaz Quezon', phone: '868-819-912', username: 'crosario@motorph.com', plainPass: '123abc', status: 'Regular', desId: 15, supId: 9, role: 'EMPLOYEE' },
    { eid: 10020, lastName: 'Bautista', firstName: 'Mark', birthday: '1991-02-12', address: '65 Murphy Center, Palayan Quirino', phone: '683-725-348', username: 'mbautista@motorph.com', plainPass: '123abc', status: 'Regular', desId: 15, supId: 9, role: 'EMPLOYEE' },
    { eid: 10021, lastName: 'Lazaro', firstName: 'Darlene', birthday: '1985-11-25', address: '47A/94 Larkin Plaza, Caloocan', phone: '740-721-558', username: 'dlazaro@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 9, role: 'EMPLOYEE' },
    { eid: 10022, lastName: 'Delos Santos', firstName: 'Kolby', birthday: '1980-02-26', address: '06A Gulgowski Extensions, Bongabon', phone: '739-443-033', username: 'kdelosantos@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 9, role: 'EMPLOYEE' },
    { eid: 10023, lastName: 'Santos', firstName: 'Vella', birthday: '1983-12-31', address: '99A Padberg Spring, Mabalacat', phone: '955-879-269', username: 'vsantos@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 9, role: 'EMPLOYEE' },
    { eid: 10024, lastName: 'Del Rosario', firstName: 'Tomas', birthday: '1978-12-18', address: '80A/48 Ledner Ridges, Kabankalan', phone: '882-550-989', username: 'tdelrosario@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 9, role: 'EMPLOYEE' },
    { eid: 10025, lastName: 'Tolentino', firstName: 'Jacklyn', birthday: '1984-05-19', address: '96/48 Watsica Flats, Malolos', phone: '675-757-366', username: 'jtolentino@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 10, role: 'EMPLOYEE' },
    { eid: 10026, lastName: 'Gutierrez', firstName: 'Percival', birthday: '1970-12-18', address: '58A Wilderman Walks, Digos', phone: '512-899-876', username: 'pgutierrez@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 10, role: 'EMPLOYEE' },
    { eid: 10027, lastName: 'Manalaysay', firstName: 'Garfield', birthday: '1986-08-28', address: '60 Goyette Valley, Tabuk', phone: '948-628-136', username: 'gmanalaysay@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 10, role: 'EMPLOYEE' },
    { eid: 10028, lastName: 'Villegas', firstName: 'Lizeth', birthday: '1981-12-12', address: '66/77 Mann Views, Luisiana', phone: '332-372-215', username: 'lvillegas@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 10, role: 'EMPLOYEE' },
    { eid: 10029, lastName: 'Ramos', firstName: 'Carol', birthday: '1978-08-20', address: '72/70 Stamm Spurs, Bustos', phone: '250-700-389', username: 'cramos@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 10, role: 'EMPLOYEE' },
    { eid: 10030, lastName: 'Maceda', firstName: 'Emelia', birthday: '1973-04-14', address: '50A/83 Bahringer Oval, Kiamba', phone: '973-358-041', username: 'emelia@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 10, role: 'EMPLOYEE' },
    { eid: 10031, lastName: 'Aguilar', firstName: 'Delia', birthday: '1989-01-27', address: '95 Cremin Junction, Surallah', phone: '529-705-439', username: 'daguilar@motorph.com', plainPass: '123abc', status: 'Probationary', desId: 15, supId: 10, role: 'EMPLOYEE' },
    { eid: 10032, lastName: 'Castro', firstName: 'John Rafael', birthday: '1992-02-09', address: 'Hi-way, Yati, Liloan Cebu', phone: '332-424-955', username: 'jrcastro@motorph.com', plainPass: '123abc', status: 'Regular', desId: 16, supId: 11, role: 'EMPLOYEE' },
    { eid: 10033, lastName: 'Martinez', firstName: 'Carlos Ian', birthday: '1990-11-16', address: 'Bulala, Camalaniugan', phone: '078-854-208', username: 'cimartinez@motorph.com', plainPass: '123abc', status: 'Regular', desId: 17, supId: 11, role: 'EMPLOYEE' },
    { eid: 10034, lastName: 'Santos', firstName: 'Beatriz', birthday: '1991-07-22', address: 'Poblacion, Surigao Del Norte', phone: '098-765-432', username: 'bsantos@motorph.com', plainPass: '123abc', status: 'Regular', desId: 18, supId: 11, role: 'EMPLOYEE' },
  ];

  for (const emp of rawEmployees) {
    const hashedPassword = emp.plainPass === 'admin' || emp.plainPass === 'adminako' ? adminHash : defaultHash;
    await prisma.employee.upsert({
      where: { eid: emp.eid },
      update: {
        lastName: emp.lastName,
        firstName: emp.firstName,
        birthday: emp.birthday,
        address: emp.address,
        phoneNumber: emp.phone,
        username: emp.username,
        password: hashedPassword,
        status: emp.status,
        designationId: emp.desId,
        supervisorId: emp.supId,
        role: emp.role,
        isActive: true,
      },
      create: {
        eid: emp.eid,
        lastName: emp.lastName,
        firstName: emp.firstName,
        birthday: emp.birthday,
        address: emp.address,
        phoneNumber: emp.phone,
        username: emp.username,
        password: hashedPassword,
        status: emp.status,
        designationId: emp.desId,
        supervisorId: emp.supId,
        role: emp.role,
        isActive: true,
      },
    });
  }
  console.log('✓ All 34 Employees seeded with BCrypt password hashes');

  // 7. Payroll Components for ALL 34 Employees (Exact from payrollsystem_db_v10.sql)
  const components = [
    { eid: 10001, basicSalary: 90000.00, riceSubsidy: 1500.00, phoneAllowance: 2000.00, clothingAllowance: 1000.00, halfMonthRate: 45000.00, hourlyRate: 535.71 },
    { eid: 10002, basicSalary: 60000.00, riceSubsidy: 1500.00, phoneAllowance: 2000.00, clothingAllowance: 1000.00, halfMonthRate: 30000.00, hourlyRate: 357.14 },
    { eid: 10003, basicSalary: 60000.00, riceSubsidy: 1500.00, phoneAllowance: 2000.00, clothingAllowance: 1000.00, halfMonthRate: 30000.00, hourlyRate: 357.14 },
    { eid: 10004, basicSalary: 60000.00, riceSubsidy: 1500.00, phoneAllowance: 2000.00, clothingAllowance: 1000.00, halfMonthRate: 30000.00, hourlyRate: 357.14 },
    { eid: 10005, basicSalary: 52670.00, riceSubsidy: 1500.00, phoneAllowance: 1000.00, clothingAllowance: 1000.00, halfMonthRate: 26335.00, hourlyRate: 313.51 },
    { eid: 10006, basicSalary: 52670.00, riceSubsidy: 1500.00, phoneAllowance: 1000.00, clothingAllowance: 1000.00, halfMonthRate: 26335.00, hourlyRate: 313.51 },
    { eid: 10007, basicSalary: 42975.00, riceSubsidy: 1500.00, phoneAllowance: 800.00, clothingAllowance: 800.00, halfMonthRate: 21488.00, hourlyRate: 255.80 },
    { eid: 10008, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10009, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10010, basicSalary: 52670.00, riceSubsidy: 1500.00, phoneAllowance: 1000.00, clothingAllowance: 1000.00, halfMonthRate: 26335.00, hourlyRate: 313.51 },
    { eid: 10011, basicSalary: 50825.00, riceSubsidy: 1500.00, phoneAllowance: 1000.00, clothingAllowance: 1000.00, halfMonthRate: 25413.00, hourlyRate: 302.53 },
    { eid: 10012, basicSalary: 38475.00, riceSubsidy: 1500.00, phoneAllowance: 800.00, clothingAllowance: 800.00, halfMonthRate: 19238.00, hourlyRate: 229.02 },
    { eid: 10013, basicSalary: 24000.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 12000.00, hourlyRate: 142.86 },
    { eid: 10014, basicSalary: 24000.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 12000.00, hourlyRate: 142.86 },
    { eid: 10015, basicSalary: 53500.00, riceSubsidy: 1500.00, phoneAllowance: 1000.00, clothingAllowance: 1000.00, halfMonthRate: 26750.00, hourlyRate: 318.45 },
    { eid: 10016, basicSalary: 42975.00, riceSubsidy: 1500.00, phoneAllowance: 800.00, clothingAllowance: 800.00, halfMonthRate: 21488.00, hourlyRate: 255.80 },
    { eid: 10017, basicSalary: 41850.00, riceSubsidy: 1500.00, phoneAllowance: 800.00, clothingAllowance: 800.00, halfMonthRate: 20925.00, hourlyRate: 249.11 },
    { eid: 10018, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10019, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10020, basicSalary: 23250.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11625.00, hourlyRate: 138.39 },
    { eid: 10021, basicSalary: 23250.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11625.00, hourlyRate: 138.39 },
    { eid: 10022, basicSalary: 24000.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 12000.00, hourlyRate: 142.86 },
    { eid: 10023, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10024, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10025, basicSalary: 24000.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 12000.00, hourlyRate: 142.86 },
    { eid: 10026, basicSalary: 24750.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 12375.00, hourlyRate: 147.32 },
    { eid: 10027, basicSalary: 24750.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 12375.00, hourlyRate: 147.32 },
    { eid: 10028, basicSalary: 24000.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 12000.00, hourlyRate: 142.86 },
    { eid: 10029, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10030, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10031, basicSalary: 22500.00, riceSubsidy: 1500.00, phoneAllowance: 500.00, clothingAllowance: 500.00, halfMonthRate: 11250.00, hourlyRate: 133.93 },
    { eid: 10032, basicSalary: 52670.00, riceSubsidy: 1500.00, phoneAllowance: 1000.00, clothingAllowance: 1000.00, halfMonthRate: 26335.00, hourlyRate: 313.51 },
    { eid: 10033, basicSalary: 52670.00, riceSubsidy: 1500.00, phoneAllowance: 1000.00, clothingAllowance: 1000.00, halfMonthRate: 26335.00, hourlyRate: 313.51 },
    { eid: 10034, basicSalary: 52670.00, riceSubsidy: 1500.00, phoneAllowance: 1000.00, clothingAllowance: 1000.00, halfMonthRate: 26335.00, hourlyRate: 313.51 },
  ];

  for (const c of components) {
    await prisma.employeePayrollComponents.upsert({
      where: { eid: c.eid },
      update: c,
      create: c,
    });
  }
  console.log('✓ All 34 Employee Payroll Components seeded');

  // 8. Government IDs for ALL 34 Employees (Exact from payrollsystem_db_v10.sql)
  const govIds = [
    { eid: 10001, sssNum: '44-4506057-3', philhealthNum: '820126853951', tinNum: '442-605-657-000', pagibigNum: '691295330870' },
    { eid: 10002, sssNum: '52-2061274-9', philhealthNum: '331735646338', tinNum: '683-102-776-000', pagibigNum: '663904995411' },
    { eid: 10003, sssNum: '30-8870406-2', philhealthNum: '177451189665', tinNum: '971-711-280-000', pagibigNum: '171519773969' },
    { eid: 10004, sssNum: '40-2511815-0', philhealthNum: '341911411254', tinNum: '876-809-437-000', pagibigNum: '416946776041' },
    { eid: 10005, sssNum: '50-5577638-1', philhealthNum: '957436191812', tinNum: '031-702-374-000', pagibigNum: '952347222457' },
    { eid: 10006, sssNum: '49-1632020-8', philhealthNum: '382189453145', tinNum: '317-674-022-000', pagibigNum: '441093369646' },
    { eid: 10007, sssNum: '40-2400714-1', philhealthNum: '239192926939', tinNum: '672-474-690-000', pagibigNum: '210850209964' },
    { eid: 10008, sssNum: '55-4476527-2', philhealthNum: '545652640232', tinNum: '888-572-294-000', pagibigNum: '211385556888' },
    { eid: 10009, sssNum: '41-0644692-3', philhealthNum: '708988234853', tinNum: '604-997-793-000', pagibigNum: '260107732354' },
    { eid: 10010, sssNum: '64-7605054-4', philhealthNum: '578114853194', tinNum: '525-420-419-000', pagibigNum: '799254095212' },
    { eid: 10011, sssNum: '26-9647608-3', philhealthNum: '126445315651', tinNum: '210-805-911-000', pagibigNum: '218002473454' },
    { eid: 10012, sssNum: '44-8563448-3', philhealthNum: '431709011012', tinNum: '218-489-737-000', pagibigNum: '113071293354' },
    { eid: 10013, sssNum: '45-5656375-0', philhealthNum: '233693897247', tinNum: '210-835-851-000', pagibigNum: '631130283546' },
    { eid: 10014, sssNum: '27-2090996-4', philhealthNum: '515741057496', tinNum: '275-792-513-000', pagibigNum: '101205445886' },
    { eid: 10015, sssNum: '26-8768374-1', philhealthNum: '308366860059', tinNum: '598-065-761-000', pagibigNum: '223057707853' },
    { eid: 10016, sssNum: '49-2959312-6', philhealthNum: '824187961962', tinNum: '103-100-522-000', pagibigNum: '631052853464' },
    { eid: 10017, sssNum: '27-2090208-8', philhealthNum: '587272469938', tinNum: '482-259-498-000', pagibigNum: '719007608464' },
    { eid: 10018, sssNum: '45-3251383-0', philhealthNum: '745148459521', tinNum: '121-203-336-000', pagibigNum: '114901859343' },
    { eid: 10019, sssNum: '49-1629900-2', philhealthNum: '579253435499', tinNum: '122-244-511-000', pagibigNum: '265104358643' },
    { eid: 10020, sssNum: '49-1647342-5', philhealthNum: '399665157135', tinNum: '273-970-941-000', pagibigNum: '260054585575' },
    { eid: 10021, sssNum: '45-5617168-2', philhealthNum: '606386917510', tinNum: '354-650-951-000', pagibigNum: '104907708845' },
    { eid: 10022, sssNum: '52-0109570-6', philhealthNum: '357451271274', tinNum: '187-500-345-000', pagibigNum: '113017988667' },
    { eid: 10023, sssNum: '52-9883524-3', philhealthNum: '548670482885', tinNum: '101-558-994-000', pagibigNum: '360028104576' },
    { eid: 10024, sssNum: '45-5866331-6', philhealthNum: '953901539995', tinNum: '560-735-732-000', pagibigNum: '913108649964' },
    { eid: 10025, sssNum: '47-1692793-0', philhealthNum: '753800654114', tinNum: '841-177-857-000', pagibigNum: '210546661243' },
    { eid: 10026, sssNum: '40-9504657-8', philhealthNum: '797639382265', tinNum: '502-995-671-000', pagibigNum: '210897095686' },
    { eid: 10027, sssNum: '45-3298166-4', philhealthNum: '810909286264', tinNum: '336-676-445-000', pagibigNum: '211274476563' },
    { eid: 10028, sssNum: '40-2400719-4', philhealthNum: '934389652994', tinNum: '332-372-215-000', pagibigNum: '211385556888' },
    { eid: 10029, sssNum: '40-2400719-4', philhealthNum: '934389652994', tinNum: '332-372-215-000', pagibigNum: '211385556888' },
    { eid: 10030, sssNum: '40-2400719-4', philhealthNum: '934389652994', tinNum: '332-372-215-000', pagibigNum: '211385556888' },
    { eid: 10031, sssNum: '40-2400719-4', philhealthNum: '934389652994', tinNum: '332-372-215-000', pagibigNum: '211385556888' },
    { eid: 10032, sssNum: '40-2400719-4', philhealthNum: '934389652994', tinNum: '332-372-215-000', pagibigNum: '211385556888' },
    { eid: 10033, sssNum: '40-2400719-4', philhealthNum: '934389652994', tinNum: '332-372-215-000', pagibigNum: '211385556888' },
    { eid: 10034, sssNum: '40-2400719-4', philhealthNum: '934389652994', tinNum: '332-372-215-000', pagibigNum: '211385556888' },
  ];

  for (const g of govIds) {
    await prisma.employeeGovernmentIds.upsert({
      where: { eid: g.eid },
      update: g,
      create: g,
    });
  }
  console.log('✓ All 34 Employee Government IDs seeded');

  // 9. Payroll Variables for ALL 34 Employees (Exact from payrollsystem_db_v10.sql)
  const variables = [
    { eid: 10001, overTime: 2, holidayPay: 5000.00, performanceBonus: 8571.36 },
    { eid: 10002, overTime: 2, holidayPay: 5000.00, performanceBonus: 5714.24 },
    { eid: 10003, overTime: 3, holidayPay: 5000.00, performanceBonus: 5714.24 },
    { eid: 10004, overTime: 1, holidayPay: 5000.00, performanceBonus: 5714.24 },
    { eid: 10005, overTime: 3, holidayPay: 5000.00, performanceBonus: 5016.16 },
    { eid: 10006, overTime: 2, holidayPay: 5000.00, performanceBonus: 5016.16 },
    { eid: 10007, overTime: 3, holidayPay: 5000.00, performanceBonus: 4092.80 },
    { eid: 10008, overTime: 3, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10009, overTime: 2, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10010, overTime: 1, holidayPay: 5000.00, performanceBonus: 5016.16 },
    { eid: 10011, overTime: 3, holidayPay: 5000.00, performanceBonus: 4840.48 },
    { eid: 10012, overTime: 2, holidayPay: 5000.00, performanceBonus: 3664.32 },
    { eid: 10013, overTime: 3, holidayPay: 5000.00, performanceBonus: 2285.76 },
    { eid: 10014, overTime: 3, holidayPay: 5000.00, performanceBonus: 2285.76 },
    { eid: 10015, overTime: 2, holidayPay: 5000.00, performanceBonus: 5095.20 },
    { eid: 10016, overTime: 1, holidayPay: 5000.00, performanceBonus: 4092.80 },
    { eid: 10017, overTime: 2, holidayPay: 5000.00, performanceBonus: 3985.76 },
    { eid: 10018, overTime: 3, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10019, overTime: 3, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10020, overTime: 3, holidayPay: 5000.00, performanceBonus: 2214.24 },
    { eid: 10021, overTime: 2, holidayPay: 5000.00, performanceBonus: 2214.24 },
    { eid: 10022, overTime: 1, holidayPay: 5000.00, performanceBonus: 2285.76 },
    { eid: 10023, overTime: 2, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10024, overTime: 2, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10025, overTime: 1, holidayPay: 5000.00, performanceBonus: 2285.76 },
    { eid: 10026, overTime: 3, holidayPay: 5000.00, performanceBonus: 2357.12 },
    { eid: 10027, overTime: 1, holidayPay: 5000.00, performanceBonus: 2357.12 },
    { eid: 10028, overTime: 3, holidayPay: 5000.00, performanceBonus: 2285.76 },
    { eid: 10029, overTime: 1, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10030, overTime: 3, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10031, overTime: 1, holidayPay: 5000.00, performanceBonus: 2142.88 },
    { eid: 10032, overTime: 3, holidayPay: 5000.00, performanceBonus: 5016.16 },
    { eid: 10033, overTime: 2, holidayPay: 5000.00, performanceBonus: 5016.16 },
    { eid: 10034, overTime: 3, holidayPay: 5000.00, performanceBonus: 5016.16 },
  ];

  for (const v of variables) {
    await prisma.employeePayrollVariables.upsert({
      where: { eid: v.eid },
      update: v,
      create: v,
    });
  }
  console.log('✓ All 34 Employee Payroll Variables seeded');

  // 10. Deductions for ALL 34 Employees (Exact from payrollsystem_db_v10.sql)
  const deductions = [
    { eid: 10001, deductionId: 1, amount: 1125.00 }, { eid: 10001, deductionId: 2, amount: 1350.00 }, { eid: 10001, deductionId: 3, amount: 1800.00 }, { eid: 10001, deductionId: 4, amount: 16871.83 },
    { eid: 10002, deductionId: 1, amount: 1125.00 }, { eid: 10002, deductionId: 2, amount: 900.00 }, { eid: 10002, deductionId: 3, amount: 1200.00 }, { eid: 10002, deductionId: 4, amount: 8539.07 },
    { eid: 10003, deductionId: 1, amount: 1125.00 }, { eid: 10003, deductionId: 2, amount: 900.00 }, { eid: 10003, deductionId: 3, amount: 1200.00 }, { eid: 10003, deductionId: 4, amount: 8628.36 },
    { eid: 10004, deductionId: 1, amount: 1125.00 }, { eid: 10004, deductionId: 2, amount: 900.00 }, { eid: 10004, deductionId: 3, amount: 1200.00 }, { eid: 10004, deductionId: 4, amount: 8449.79 },
    { eid: 10005, deductionId: 1, amount: 1125.00 }, { eid: 10005, deductionId: 2, amount: 790.05 }, { eid: 10005, deductionId: 3, amount: 1053.40 }, { eid: 10005, deductionId: 4, amount: 6827.27 },
    { eid: 10006, deductionId: 1, amount: 1125.00 }, { eid: 10006, deductionId: 2, amount: 790.05 }, { eid: 10006, deductionId: 3, amount: 1053.40 }, { eid: 10006, deductionId: 4, amount: 6748.89 },
    { eid: 10007, deductionId: 1, amount: 1125.00 }, { eid: 10007, deductionId: 2, amount: 644.63 }, { eid: 10007, deductionId: 3, amount: 859.50 }, { eid: 10007, deductionId: 4, amount: 4445.07 },
    { eid: 10008, deductionId: 1, amount: 1012.50 }, { eid: 10008, deductionId: 2, amount: 337.50 }, { eid: 10008, deductionId: 3, amount: 450.00 }, { eid: 10008, deductionId: 4, amount: 53.76 },
    { eid: 10009, deductionId: 1, amount: 1012.50 }, { eid: 10009, deductionId: 2, amount: 337.50 }, { eid: 10009, deductionId: 3, amount: 450.00 }, { eid: 10009, deductionId: 4, amount: 26.97 },
    { eid: 10010, deductionId: 1, amount: 1125.00 }, { eid: 10010, deductionId: 2, amount: 790.05 }, { eid: 10010, deductionId: 3, amount: 1053.40 }, { eid: 10010, deductionId: 4, amount: 6670.52 },
    { eid: 10011, deductionId: 1, amount: 1125.00 }, { eid: 10011, deductionId: 2, amount: 762.38 }, { eid: 10011, deductionId: 3, amount: 1016.50 }, { eid: 10011, deductionId: 4, amount: 6373.93 },
    { eid: 10012, deductionId: 1, amount: 1125.00 }, { eid: 10012, deductionId: 2, amount: 577.13 }, { eid: 10012, deductionId: 3, amount: 769.50 }, { eid: 10012, deductionId: 4, amount: 3282.10 },
    { eid: 10013, deductionId: 1, amount: 1080.00 }, { eid: 10013, deductionId: 2, amount: 360.00 }, { eid: 10013, deductionId: 3, amount: 480.00 }, { eid: 10013, deductionId: 4, amount: 335.12 },
    { eid: 10014, deductionId: 1, amount: 1080.00 }, { eid: 10014, deductionId: 2, amount: 360.00 }, { eid: 10014, deductionId: 3, amount: 480.00 }, { eid: 10014, deductionId: 4, amount: 335.12 },
    { eid: 10015, deductionId: 1, amount: 1125.00 }, { eid: 10015, deductionId: 2, amount: 802.50 }, { eid: 10015, deductionId: 3, amount: 1070.00 }, { eid: 10015, deductionId: 4, amount: 6951.60 },
    { eid: 10016, deductionId: 1, amount: 1125.00 }, { eid: 10016, deductionId: 2, amount: 644.63 }, { eid: 10016, deductionId: 3, amount: 859.50 }, { eid: 10016, deductionId: 4, amount: 4317.17 },
    { eid: 10017, deductionId: 1, amount: 1125.00 }, { eid: 10017, deductionId: 2, amount: 627.75 }, { eid: 10017, deductionId: 3, amount: 837.00 }, { eid: 10017, deductionId: 4, amount: 4106.37 },
    { eid: 10018, deductionId: 1, amount: 1012.50 }, { eid: 10018, deductionId: 2, amount: 337.50 }, { eid: 10018, deductionId: 3, amount: 450.00 }, { eid: 10018, deductionId: 4, amount: 53.76 },
    { eid: 10019, deductionId: 1, amount: 1012.50 }, { eid: 10019, deductionId: 2, amount: 337.50 }, { eid: 10019, deductionId: 3, amount: 450.00 }, { eid: 10019, deductionId: 4, amount: 53.76 },
    { eid: 10020, deductionId: 1, amount: 1057.50 }, { eid: 10020, deductionId: 2, amount: 348.75 }, { eid: 10020, deductionId: 3, amount: 465.00 }, { eid: 10020, deductionId: 4, amount: 192.18 },
    { eid: 10021, deductionId: 1, amount: 1057.50 }, { eid: 10021, deductionId: 2, amount: 348.75 }, { eid: 10021, deductionId: 3, amount: 465.00 }, { eid: 10021, deductionId: 4, amount: 164.51 },
    { eid: 10022, deductionId: 1, amount: 1080.00 }, { eid: 10022, deductionId: 2, amount: 360.00 }, { eid: 10022, deductionId: 3, amount: 480.00 }, { eid: 10022, deductionId: 4, amount: 277.97 },
    { eid: 10023, deductionId: 1, amount: 1012.50 }, { eid: 10023, deductionId: 2, amount: 337.50 }, { eid: 10023, deductionId: 3, amount: 450.00 }, { eid: 10023, deductionId: 4, amount: 26.97 },
    { eid: 10024, deductionId: 1, amount: 1012.50 }, { eid: 10024, deductionId: 2, amount: 337.50 }, { eid: 10024, deductionId: 3, amount: 450.00 }, { eid: 10024, deductionId: 4, amount: 26.97 },
    { eid: 10025, deductionId: 1, amount: 1080.00 }, { eid: 10025, deductionId: 2, amount: 360.00 }, { eid: 10025, deductionId: 3, amount: 480.00 }, { eid: 10025, deductionId: 4, amount: 277.97 },
    { eid: 10026, deductionId: 1, amount: 1125.00 }, { eid: 10026, deductionId: 2, amount: 371.25 }, { eid: 10026, deductionId: 3, amount: 495.00 }, { eid: 10026, deductionId: 4, amount: 473.54 },
    { eid: 10027, deductionId: 1, amount: 1125.00 }, { eid: 10027, deductionId: 2, amount: 371.25 }, { eid: 10027, deductionId: 3, amount: 495.00 }, { eid: 10027, deductionId: 4, amount: 414.61 },
    { eid: 10028, deductionId: 1, amount: 1080.00 }, { eid: 10028, deductionId: 2, amount: 360.00 }, { eid: 10028, deductionId: 3, amount: 480.00 }, { eid: 10028, deductionId: 4, amount: 335.12 },
    { eid: 10029, deductionId: 1, amount: 1012.50 }, { eid: 10029, deductionId: 2, amount: 337.50 }, { eid: 10029, deductionId: 3, amount: 450.00 }, { eid: 10029, deductionId: 4, amount: 26.97 },
    { eid: 10030, deductionId: 1, amount: 1012.50 }, { eid: 10030, deductionId: 2, amount: 337.50 }, { eid: 10030, deductionId: 3, amount: 450.00 }, { eid: 10030, deductionId: 4, amount: 26.97 },
    { eid: 10031, deductionId: 1, amount: 1012.50 }, { eid: 10031, deductionId: 2, amount: 337.50 }, { eid: 10031, deductionId: 3, amount: 450.00 }, { eid: 10031, deductionId: 4, amount: 26.97 },
    { eid: 10032, deductionId: 1, amount: 1125.00 }, { eid: 10032, deductionId: 2, amount: 790.05 }, { eid: 10032, deductionId: 3, amount: 1053.40 }, { eid: 10032, deductionId: 4, amount: 6670.52 },
    { eid: 10033, deductionId: 1, amount: 1125.00 }, { eid: 10033, deductionId: 2, amount: 790.05 }, { eid: 10033, deductionId: 3, amount: 1053.40 }, { eid: 10033, deductionId: 4, amount: 6670.52 },
    { eid: 10034, deductionId: 1, amount: 1125.00 }, { eid: 10034, deductionId: 2, amount: 790.05 }, { eid: 10034, deductionId: 3, amount: 1053.40 }, { eid: 10034, deductionId: 4, amount: 6670.52 },
  ];

  for (const d of deductions) {
    await prisma.employeeDeduction.upsert({
      where: {
        eid_deductionId: { eid: d.eid, deductionId: d.deductionId },
      },
      update: { amount: d.amount },
      create: d,
    });
  }
  console.log('✓ All 34 Employee Deductions seeded');

  // 11. Sample Leaves
  const sampleLeaves = [
    { leaveId: 'L1001', eid: 10005, dateFiled: '2026-01-05', dateFrom: '2026-01-15', dateTo: '2026-01-18', reasonForLeave: 'Vacation Leave', leaveStatus: 'Pending' },
    { leaveId: 'L1002', eid: 10006, dateFiled: '2026-01-10', dateFrom: '2026-01-20', dateTo: '2026-01-22', reasonForLeave: 'Medical Leave', leaveStatus: 'Approved' },
    { leaveId: 'L1003', eid: 10009, dateFiled: '2026-01-12', dateFrom: '2026-02-01', dateTo: '2026-02-03', reasonForLeave: 'Personal Time Off', leaveStatus: 'Pending' },
  ];

  for (const l of sampleLeaves) {
    await prisma.leave.upsert({
      where: { leaveId: l.leaveId },
      update: l,
      create: l,
    });
  }

  // 12. Sample Timesheets
  const sampleTimesheets = [
    { eid: 10001, logDate: '2026-01-02', logTime: '08:30:00', attStatus: 'Present' },
    { eid: 10001, logDate: '2026-01-03', logTime: '08:45:00', attStatus: 'Present' },
    { eid: 10005, logDate: '2026-01-02', logTime: '09:15:00', attStatus: 'Late' },
    { eid: 10006, logDate: '2026-01-02', logTime: '08:20:00', attStatus: 'Present' },
    { eid: 10009, logDate: '2026-01-02', logTime: '08:35:00', attStatus: 'Present' },
  ];

  for (const ts of sampleTimesheets) {
    const existing = await prisma.timesheet.findFirst({
      where: { eid: ts.eid, logDate: ts.logDate },
    });
    if (!existing) {
      await prisma.timesheet.create({ data: ts });
    }
  }

  console.log('--- MotorPH Seeding Completed Successfully ---');
}

main()
  .catch((e) => {
    console.error(e);
    process.exit(1);
  })
  .finally(async () => {
    await prisma.$disconnect();
  });
