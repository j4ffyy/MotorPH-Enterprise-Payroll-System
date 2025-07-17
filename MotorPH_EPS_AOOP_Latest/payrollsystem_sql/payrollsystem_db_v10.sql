CREATE DATABASE  IF NOT EXISTS `payrollsystem_db` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `payrollsystem_db`;
-- MySQL dump 10.13  Distrib 8.0.40, for Win64 (x86_64)
--
-- Host: localhost    Database: payrollsystem_db
-- ------------------------------------------------------
-- Server version	8.0.40

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `deduction_types`
--

DROP TABLE IF EXISTS `deduction_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deduction_types` (
  `Deduction_ID` int NOT NULL,
  `Deduction_Name` varchar(50) NOT NULL,
  PRIMARY KEY (`Deduction_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deduction_types`
--

LOCK TABLES `deduction_types` WRITE;
/*!40000 ALTER TABLE `deduction_types` DISABLE KEYS */;
INSERT INTO `deduction_types` VALUES (1,'SSS'),(2,'PhilHealth'),(3,'PagIBIG'),(4,'Withholding_Tax');
/*!40000 ALTER TABLE `deduction_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `departments`
--

DROP TABLE IF EXISTS `departments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `departments` (
  `Department_ID` int NOT NULL,
  `Department_Name` varchar(50) NOT NULL,
  PRIMARY KEY (`Department_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `departments`
--

LOCK TABLES `departments` WRITE;
/*!40000 ALTER TABLE `departments` DISABLE KEYS */;
INSERT INTO `departments` VALUES (1,'Executive'),(2,'Human Resources'),(3,'Accounting'),(4,'Finance'),(5,'IT'),(6,'Sales');
/*!40000 ALTER TABLE `departments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `designations`
--

DROP TABLE IF EXISTS `designations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `designations` (
  `Designation_ID` int NOT NULL AUTO_INCREMENT,
  `Designation_Name` varchar(50) NOT NULL,
  `Department_ID` int NOT NULL,
  PRIMARY KEY (`Designation_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `designations`
--

LOCK TABLES `designations` WRITE;
/*!40000 ALTER TABLE `designations` DISABLE KEYS */;
INSERT INTO `designations` VALUES (1,'Chief Executive Officer',1),(2,'Chief Operating Officer',1),(3,'Chief Finance Officer',1),(4,'Chief Marketing Officer',1),(5,'IT Operations and Systems',5),(6,'HR Manager',2),(7,'HR Team Leader',2),(8,'HR Rank and File',2),(9,'Accounting Head',3),(10,'Payroll Manager',4),(11,'Payroll Team Leader',4),(12,'Payroll Rank and File',4),(13,'Account Manager',3),(14,'Account Team Leader',3),(15,'Account Rank and File',3),(16,'Sales & Marketing',6),(17,'Supply Chain and Logistics',6),(18,'Customer Service and Relations',6);
/*!40000 ALTER TABLE `designations` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee`
--

DROP TABLE IF EXISTS `employee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee` (
  `EID` int NOT NULL,
  `Last_Name` varchar(50) NOT NULL,
  `First_Name` varchar(50) NOT NULL,
  `Birthday` date DEFAULT NULL,
  `Address` varchar(255) DEFAULT NULL,
  `Phone_Number` varchar(20) DEFAULT NULL,
  `Username` varchar(50) DEFAULT NULL,
  `Password` varchar(50) DEFAULT NULL,
  `Status` varchar(20) DEFAULT NULL,
  `Designation_ID` int DEFAULT NULL,
  `Supervisor_ID` int DEFAULT NULL,
  `is_active` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`EID`),
  KEY `Designation_ID` (`Designation_ID`),
  KEY `Supervisor_ID` (`Supervisor_ID`),
  CONSTRAINT `employee_ibfk_1` FOREIGN KEY (`Designation_ID`) REFERENCES `designations` (`Designation_ID`),
  CONSTRAINT `employee_ibfk_2` FOREIGN KEY (`Supervisor_ID`) REFERENCES `supervisors` (`Supervisor_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee`
--

LOCK TABLES `employee` WRITE;
/*!40000 ALTER TABLE `employee` DISABLE KEYS */;
INSERT INTO `employee` VALUES (10001,'Garcia','Manuel III','1983-10-11','Valero Carpark Building Valero Street 1227, Makati City','966-860-270','mgarcia@motorph.com','123abc','Regular',1,NULL,1),(10002,'Lim','Antonio','1988-06-19','San Antonio De Padua 2, Block 1 Lot 8 and 2, Dasmarinas, Cavite','171-867-411','alim@motorph.com','adminako','Regular',2,1,1),(10003,'Aquino','Bianca Sofia','1989-08-04','Rm. 402 4/F Jiao Building Timog Avenue Cor. Quezon Avenue 1100, Quezon City','966-889-370','bsaquino@motorph.com','123abc','Regular',3,1,1),(10004,'Reyes','Isabella','1994-06-16','460 Solanda Street Intramuros 1000, Manila','786-868-477','ireyes@motorph.com','admin','Regular',4,1,1),(10005,'Hernandez','Eduard','1989-09-23','National Highway, Gingoog,  Misamis Occidental','088-861-012','ehernandez@motorph.com','admin','Regular',5,2,1),(10006,'Villanueva','Andrea Mae','1988-02-14','17/85 Stracke Via Suite 042, Poblacion, Las Piñas 4783 Dinagat Islands ','918-621-603','amvillanueva@motorph.com','123abc','Regular',6,2,1),(10007,'San Jose','Brad','1996-03-15','99 Strosin Hills, Poblacion, Bislig 5340 Tawi-Tawi','797-009-261','bsanjose@motorph.com','123abc','Regular',7,3,1),(10008,'Romualdez','Alice','1992-05-14','12A/33 Upton Isle Apt. 420, Roxas City 1814 Surigao del Norte ','983-606-799','aromualdez@motorph.com','123abc','Regular',8,4,1),(10009,'Atienza','Rosie ','1948-09-24','90A Dibbert Terrace Apt. 190, San Lorenzo 6056 Davao del Norte','266-036-427','ratienza@motorph.com','emp123','Regular',8,4,1),(10010,'Alvaro','Roderick','1988-03-30','#284 T. Morato corner, Scout Rallos Street, Quezon City','053-381-386','ralvaro@motorph.com','123abc','Regular',9,5,1),(10011,'Salcedo','Anthony','1993-09-14','93/54 Shanahan Alley Apt. 183, Santo Tomas 1572 Masbate','070-766-300','asalcedo@motorph.com','123abc','Regular',10,6,1),(10012,'Lopez','Josie ','1987-01-14','49 Springs Apt. 266, Poblacion, Taguig 3200 Occidental Mindoro','478-355-427','jlopez@motorph.com','123abc','Regular',11,7,1),(10013,'Farala','Martha','1942-01-11','42/25 Sawayn Stream, Ubay 1208 Zamboanga del Norte ','329-034-366','mfarala@motorph.com','123abc','Regular',12,7,1),(10014,'Martinez','Leila','1970-07-11','37/46 Kulas Roads, Maragondon 0962 Quirino ','877-110-749','lmartinez@motorph.com','employee','Regular',12,7,1),(10015,'Romualdez','Fredrick ','1985-03-10','22A/52 Lubowitz Meadows, Pililla 4895 Zambales','023-079-009','fromualdez@motorph.com','123abc','Regular',13,2,1),(10016,'Mata','Christian','1987-10-21','90 O\'Keefe Spur Apt. 379, Catigbian 2772 Sulu ','783-776-744','cmata@motorph.com','123abc','Regular',14,8,1),(10017,'De Leon','Selena ','1975-02-20','89A Armstrong Trace, Compostela 7874 Maguindanao','975-432-139','sdeleon@motorph.com','123abc','Regular',14,8,1),(10018,'San Jose','Allison ','1986-06-24','08 Grant Drive Suite 406, Poblacion, Iloilo City 9186 La Union','179-075-129','asanjose@motorph.com','123abc','Regular',15,9,1),(10019,'Rosario','Cydney ','1996-10-06','93A/21 Berge Points, Tapaz 2180 Quezon','868-819-912','crosario@motorph.com','123abc','Regular',15,9,1),(10020,'Bautista','Mark ','1991-02-12','65 Murphy Center Suite 094, Poblacion, Palayan 5636 Quirino','683-725-348','mbautista@motorph.com','123abc','Regular',15,9,1),(10021,'Lazaro','Darlene ','1985-11-25','47A/94 Larkin Plaza Apt. 179, Poblacion, Caloocan 2751 Quirino','740-721-558','dlazaro@motorph.com','123abc','Probationary',15,9,1),(10022,'Delos Santos','Kolby ','1980-02-26','06A Gulgowski Extensions, Bongabon 6085 Zamboanga del Sur','739-443-033','kdelosantos@motorph.com','123abc','Probationary',15,9,1),(10023,'Santos','Vella ','1983-12-31','99A Padberg Spring, Poblacion, Mabalacat 3959 Lanao del Sur','955-879-269','vsantos@motorph.com','123abc','Probationary',15,9,1),(10024,'Del Rosario','Tomas','1978-12-18','80A/48 Ledner Ridges, Poblacion, Kabankalan 8870 Marinduque','882-550-989','tdelrosario@motorph.com','123abc','Probationary',15,9,1),(10025,'Tolentino','Jacklyn ','1984-05-19','96/48 Watsica Flats Suite 734, Poblacion, Malolos 1844 Ifugao','675-757-366','jtolentino@motorph.com','123abc','Probationary',15,10,1),(10026,'Gutierrez','Percival ','1970-12-18','58A Wilderman Walks, Poblacion, Digos 5822 Davao del Sur','512-899-876','pgutierrez@motorph.com','123abc','Probationary',15,10,1),(10027,'Manalaysay','Garfield ','1986-08-28','60 Goyette Valley Suite 219, Poblacion, Tabuk 3159 Lanao del Sur','948-628-136','gmanalaysay@motorph.com','123abc','Probationary',15,10,1),(10028,'Villegas','Lizeth ','1981-12-12','66/77 Mann Views, Luisiana 1263 Dinagat Islands','332-372-215','lvillegas@motorph.com','123abc','Probationary',15,10,1),(10029,'Ramos','Carol ','1978-08-20','72/70 Stamm Spurs, Bustos 4550 Iloilo','250-700-389','cramos@motorph.com','123abc','Probationary',15,10,1),(10030,'Maceda','Emelia ','1973-04-14','50A/83 Bahringer Oval Suite 145, Kiamba 7688 Nueva Ecija','973-358-041','emelia@motorph.com','123abc','Probationary',15,10,1),(10031,'Aguilar','Delia ','1989-01-27','95 Cremin Junction, Surallah 2809 Cotabato','529-705-439','daguilar@motorph.com','123abc','Probationary',15,10,1),(10032,'Castro','John Rafael','1992-02-09','Hi-way, Yati, Liloan Cebu','332-424-955 ','jrcastro@motorph.com','123abc','Regular',16,11,1),(10033,'Martinez','Carlos Ian','1990-11-16','Bulala, Camalaniugan','078-854-208','cimartinez@motorph.com','123abc','Regular',17,11,1),(10034,'Santos','Beatriz','1990-08-07','Agapita Building, Metro Manila','526-639-511','bsantos@motorph.com','123abc','Regular',18,11,1),(10035,'NewEmployee1','First','1995-01-01','New Address 1','111-222-333','new1@motorph.com','pass1','Regular',1,1,1),(10036,'NewEmployee2','Second','1996-02-02','New Address 2','444-555-666','new2@motorph.com','pass2','Probationary',2,2,1);
/*!40000 ALTER TABLE `employee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_deductions`
--

DROP TABLE IF EXISTS `employee_deductions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_deductions` (
  `EID` int NOT NULL,
  `Deduction_ID` int NOT NULL,
  `Amount` decimal(10,2) NOT NULL,
  PRIMARY KEY (`EID`,`Deduction_ID`),
  KEY `fk_employee_deductions_deduction_types` (`Deduction_ID`),
  CONSTRAINT `fk_employee_deductions_deduction_types` FOREIGN KEY (`Deduction_ID`) REFERENCES `deduction_types` (`Deduction_ID`),
  CONSTRAINT `fk_employee_deductions_employee` FOREIGN KEY (`EID`) REFERENCES `employee` (`EID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_deductions`
--

LOCK TABLES `employee_deductions` WRITE;
/*!40000 ALTER TABLE `employee_deductions` DISABLE KEYS */;
INSERT INTO `employee_deductions` VALUES (10001,1,1125.00),(10001,2,1350.00),(10001,3,1800.00),(10001,4,16871.83),(10002,1,1125.00),(10002,2,900.00),(10002,3,1200.00),(10002,4,8539.07),(10003,1,1125.00),(10003,2,900.00),(10003,3,1200.00),(10003,4,8628.36),(10004,1,1125.00),(10004,2,900.00),(10004,3,1200.00),(10004,4,8449.79),(10005,1,1125.00),(10005,2,790.05),(10005,3,1053.40),(10005,4,6827.27),(10006,1,1125.00),(10006,2,790.05),(10006,3,1053.40),(10006,4,6748.89),(10007,1,1125.00),(10007,2,644.63),(10007,3,859.50),(10007,4,4445.07),(10008,1,1012.50),(10008,2,337.50),(10008,3,450.00),(10008,4,53.76),(10009,1,1012.50),(10009,2,337.50),(10009,3,450.00),(10009,4,26.97),(10010,1,1125.00),(10010,2,790.05),(10010,3,1053.40),(10010,4,6670.52),(10011,1,1125.00),(10011,2,762.38),(10011,3,1016.50),(10011,4,6373.93),(10012,1,1125.00),(10012,2,577.13),(10012,3,769.50),(10012,4,3282.10),(10013,1,1080.00),(10013,2,360.00),(10013,3,480.00),(10013,4,335.12),(10014,1,1080.00),(10014,2,360.00),(10014,3,480.00),(10014,4,335.12),(10015,1,1125.00),(10015,2,802.50),(10015,3,1070.00),(10015,4,6951.60),(10016,1,1125.00),(10016,2,644.63),(10016,3,859.50),(10016,4,4317.17),(10017,1,1125.00),(10017,2,627.75),(10017,3,837.00),(10017,4,4106.37),(10018,1,1012.50),(10018,2,337.50),(10018,3,450.00),(10018,4,53.76),(10019,1,1012.50),(10019,2,337.50),(10019,3,450.00),(10019,4,53.76),(10020,1,1057.50),(10020,2,348.75),(10020,3,465.00),(10020,4,192.18),(10021,1,1057.50),(10021,2,348.75),(10021,3,465.00),(10021,4,164.51),(10022,1,1080.00),(10022,2,360.00),(10022,3,480.00),(10022,4,277.97),(10023,1,1012.50),(10023,2,337.50),(10023,3,450.00),(10023,4,26.97),(10024,1,1012.50),(10024,2,337.50),(10024,3,450.00),(10024,4,26.97),(10025,1,1080.00),(10025,2,360.00),(10025,3,480.00),(10025,4,277.97),(10026,1,1125.00),(10026,2,371.25),(10026,3,495.00),(10026,4,473.54),(10027,1,1125.00),(10027,2,371.25),(10027,3,495.00),(10027,4,414.61),(10028,1,1080.00),(10028,2,360.00),(10028,3,480.00),(10028,4,335.12),(10029,1,1012.50),(10029,2,337.50),(10029,3,450.00),(10029,4,26.97),(10030,1,1012.50),(10030,2,337.50),(10030,3,450.00),(10030,4,26.97),(10031,1,1012.50),(10031,2,337.50),(10031,3,450.00),(10031,4,26.97),(10032,1,1125.00),(10032,2,790.05),(10032,3,1053.40),(10032,4,6670.52),(10033,1,1125.00),(10033,2,790.05),(10033,3,1053.40),(10033,4,6670.52),(10034,1,1125.00),(10034,2,790.05),(10034,3,1053.40),(10034,4,6670.52);
/*!40000 ALTER TABLE `employee_deductions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_government_ids`
--

DROP TABLE IF EXISTS `employee_government_ids`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_government_ids` (
  `EID` int NOT NULL,
  `SSS_Num` varchar(20) DEFAULT NULL,
  `Philhealth_Num` varchar(20) DEFAULT NULL,
  `TIN_Num` varchar(20) DEFAULT NULL,
  `Pagibig_Num` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`EID`),
  CONSTRAINT `fk_govt_ids` FOREIGN KEY (`EID`) REFERENCES `employee` (`EID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_government_ids`
--

LOCK TABLES `employee_government_ids` WRITE;
/*!40000 ALTER TABLE `employee_government_ids` DISABLE KEYS */;
INSERT INTO `employee_government_ids` VALUES (10001,'44-4506057-3','820126853951','442-605-657-000','691295330870'),(10002,'52-2061274-9','331735646338','683-102-776-000','663904995411'),(10003,'30-8870406-2','177451189665','971-711-280-000','171519773969'),(10004,'40-2511815-0','341911411254','876-809-437-000','416946776041'),(10005,'50-5577638-1','957436191812','031-702-374-000','952347222457'),(10006,'49-1632020-8','382189453145','317-674-022-000','441093369646'),(10007,'40-2400714-1','239192926939','672-474-690-000','210850209964'),(10008,'55-4476527-2','545652640232','888-572-294-000','211385556888'),(10009,'41-0644692-3','708988234853','604-997-793-000','260107732354'),(10010,'64-7605054-4','578114853194','525-420-419-000','799254095212'),(10011,'26-9647608-3','126445315651','210-805-911-000','218002473454'),(10012,'44-8563448-3','431709011012','218-489-737-000','113071293354'),(10013,'45-5656375-0','233693897247','210-835-851-000','631130283546'),(10014,'27-2090996-4','515741057496','275-792-513-000','101205445886'),(10015,'26-8768374-1','308366860059','598-065-761-000','223057707853'),(10016,'49-2959312-6','824187961962','103-100-522-000','631052853464'),(10017,'27-2090208-8','587272469938','482-259-498-000','719007608464'),(10018,'45-3251383-0','745148459521','121-203-336-000','114901859343'),(10019,'49-1629900-2','579253435499','122-244-511-000','265104358643'),(10020,'49-1647342-5','399665157135','273-970-941-000','260054585575'),(10021,'45-5617168-2','606386917510','354-650-951-000','104907708845'),(10022,'52-0109570-6','357451271274','187-500-345-000','113017988667'),(10023,'52-9883524-3','548670482885','101-558-994-000','360028104576'),(10024,'45-5866331-6','953901539995','560-735-732-000','913108649964'),(10025,'47-1692793-0','753800654114','841-177-857-000','210546661243'),(10026,'40-9504657-8','797639382265','502-995-671-000','210897095686'),(10027,'45-3298166-4','810909286264','336-676-445-000','211274476563'),(10028,'40-2400719-4','934389652994','332-372-215-000','211385556888'),(10029,'40-2400719-4','934389652994','332-372-215-000','211385556888'),(10030,'40-2400719-4','934389652994','332-372-215-000','211385556888'),(10031,'40-2400719-4','934389652994','332-372-215-000','211385556888'),(10032,'40-2400719-4','934389652994','332-372-215-000','211385556888'),(10033,'40-2400719-4','934389652994','332-372-215-000','211385556888'),(10034,'40-2400719-4','934389652994','332-372-215-000','211385556888');
/*!40000 ALTER TABLE `employee_government_ids` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `employee_payroll_components`
--

DROP TABLE IF EXISTS `employee_payroll_components`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_payroll_components` (
  `EID` int NOT NULL,
  `Basic_Salary` decimal(10,2) DEFAULT NULL,
  `Rice_Subsidy` decimal(10,2) DEFAULT NULL,
  `Phone_Allowance` decimal(10,2) DEFAULT NULL,
  `Clothing_Allowance` decimal(10,2) DEFAULT NULL,
  `Half_Month_Rate` decimal(10,2) DEFAULT NULL,
  `Hourly_Rate` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`EID`),
  CONSTRAINT `fk_payroll_components` FOREIGN KEY (`EID`) REFERENCES `employee` (`EID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_payroll_components`
--

LOCK TABLES `employee_payroll_components` WRITE;
/*!40000 ALTER TABLE `employee_payroll_components` DISABLE KEYS */;
INSERT INTO `employee_payroll_components` VALUES (10001,90000.00,1500.00,2000.00,1000.00,45000.00,535.71),(10002,60000.00,1500.00,2000.00,1000.00,30000.00,357.14),(10003,60000.00,1500.00,2000.00,1000.00,30000.00,357.14),(10004,60000.00,1500.00,2000.00,1000.00,30000.00,357.14),(10005,52670.00,1500.00,1000.00,1000.00,26335.00,313.51),(10006,52670.00,1500.00,1000.00,1000.00,26335.00,313.51),(10007,42975.00,1500.00,800.00,800.00,21488.00,255.80),(10008,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10009,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10010,52670.00,1500.00,1000.00,1000.00,26335.00,313.51),(10011,50825.00,1500.00,1000.00,1000.00,25413.00,302.53),(10012,38475.00,1500.00,800.00,800.00,19238.00,229.02),(10013,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10014,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10015,53500.00,1500.00,1000.00,1000.00,26750.00,318.45),(10016,42975.00,1500.00,800.00,800.00,21488.00,255.80),(10017,41850.00,1500.00,800.00,800.00,20925.00,249.11),(10018,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10019,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10020,23250.00,1500.00,500.00,500.00,11625.00,138.39),(10021,23250.00,1500.00,500.00,500.00,11625.00,138.39),(10022,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10023,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10024,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10025,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10026,24750.00,1500.00,500.00,500.00,12375.00,147.32),(10027,24750.00,1500.00,500.00,500.00,12375.00,147.32),(10028,24000.00,1500.00,500.00,500.00,12000.00,142.86),(10029,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10030,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10031,22500.00,1500.00,500.00,500.00,11250.00,133.93),(10032,52670.00,1500.00,1000.00,1000.00,26335.00,313.51),(10033,52670.00,1500.00,1000.00,1000.00,26335.00,313.51),(10034,52670.00,1500.00,1000.00,1000.00,26335.00,313.51);
/*!40000 ALTER TABLE `employee_payroll_components` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `employee_payroll_summary`
--

DROP TABLE IF EXISTS `employee_payroll_summary`;
/*!50001 DROP VIEW IF EXISTS `employee_payroll_summary`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `employee_payroll_summary` AS SELECT 
 1 AS `employee_id`,
 1 AS `Employee_Name`,
 1 AS `Position`,
 1 AS `Department`,
 1 AS `Monthly_Rate`,
 1 AS `Daily_Rate`,
 1 AS `Days_Worked`,
 1 AS `Overtime_Hours`,
 1 AS `Overtime_Pay`,
 1 AS `Gross_Income`,
 1 AS `Rice_Subsidy`,
 1 AS `Phone_Allowance`,
 1 AS `Clothing_Allowance`,
 1 AS `Total_Benefits`,
 1 AS `SSS_Contribution`,
 1 AS `Philhealth_Contribution`,
 1 AS `Pagibig_Contribution`,
 1 AS `Withholding_Tax`,
 1 AS `Total_Deductions`,
 1 AS `Take_Home_Pay`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `employee_payroll_variables`
--

DROP TABLE IF EXISTS `employee_payroll_variables`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `employee_payroll_variables` (
  `EID` int NOT NULL,
  `Over_Time` int DEFAULT NULL,
  `Performance_Bonus` decimal(10,2) DEFAULT NULL,
  `Holiday_Pay` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`EID`),
  CONSTRAINT `fk_payroll_variables` FOREIGN KEY (`EID`) REFERENCES `employee` (`EID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `employee_payroll_variables`
--

LOCK TABLES `employee_payroll_variables` WRITE;
/*!40000 ALTER TABLE `employee_payroll_variables` DISABLE KEYS */;
INSERT INTO `employee_payroll_variables` VALUES (10001,2,5000.00,8571.36),(10002,2,5000.00,5714.24),(10003,3,5000.00,5714.24),(10004,1,5000.00,5714.24),(10005,3,5000.00,5016.16),(10006,2,5000.00,5016.16),(10007,3,5000.00,4092.80),(10008,3,5000.00,2142.88),(10009,2,5000.00,2142.88),(10010,1,5000.00,5016.16),(10011,3,5000.00,4840.48),(10012,2,5000.00,3664.32),(10013,3,5000.00,2285.76),(10014,3,5000.00,2285.76),(10015,2,5000.00,5095.20),(10016,1,5000.00,4092.80),(10017,2,5000.00,3985.76),(10018,3,5000.00,2142.88),(10019,3,5000.00,2142.88),(10020,3,5000.00,2214.24),(10021,2,5000.00,2214.24),(10022,1,5000.00,2285.76),(10023,2,5000.00,2142.88),(10024,2,5000.00,2142.88),(10025,1,5000.00,2285.76),(10026,3,5000.00,2357.12),(10027,1,5000.00,2357.12),(10028,3,5000.00,2285.76),(10029,1,5000.00,2142.88),(10030,3,5000.00,2142.88),(10031,1,5000.00,2142.88),(10032,3,5000.00,5016.16),(10033,2,5000.00,5016.16),(10034,3,5000.00,5016.16);
/*!40000 ALTER TABLE `employee_payroll_variables` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `leaves`
--

DROP TABLE IF EXISTS `leaves`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `leaves` (
  `Leave_ID` varchar(10) NOT NULL,
  `EID` int NOT NULL,
  `Date_Filed` date DEFAULT NULL,
  `Date_From` date DEFAULT NULL,
  `Date_To` date DEFAULT NULL,
  `Reason_For_Leave` varchar(50) DEFAULT NULL,
  `Leave_Status` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`Leave_ID`),
  KEY `EID` (`EID`),
  CONSTRAINT `fk_employee_leave` FOREIGN KEY (`EID`) REFERENCES `employee` (`EID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `leaves`
--

LOCK TABLES `leaves` WRITE;
/*!40000 ALTER TABLE `leaves` DISABLE KEYS */;
INSERT INTO `leaves` VALUES ('L1001',10005,'2024-12-22','2025-01-05','2025-01-10','Vacation Leave','Rejected'),('L1002',10012,'2025-01-21','2025-01-15','2025-01-20','Medical Leave','Approved'),('L1003',10023,'2025-01-18','2025-02-01','2025-02-05','Bereavement Leave','Approved'),('L1004',10009,'2025-02-16','2025-02-10','2025-02-15','Medical Leave','Rejected'),('L1005',10031,'2025-02-06','2025-02-20','2025-02-28','Vacation Leave','Approved'),('L1006',10018,'2025-02-15','2025-03-01','2025-03-02','Personal Time Off','Pending'),('L1007',10003,'2025-02-24','2025-03-10','2025-03-17','Vacation Leave','Approved'),('L1008',10027,'2025-03-06','2025-03-20','2025-03-25','Vacation Leave','Pending'),('L1009',10014,'2025-03-18','2025-04-01','2025-04-03','Compensatory Time Off','Approved'),('L1010',10030,'2025-03-27','2025-04-10','2025-04-17','Parental Leave','Approved'),('L1011',10008,'2025-04-08','2025-04-22','2025-04-23','Bereavement Leave','Approved'),('L1012',10021,'2025-04-17','2025-05-01','2025-05-05','Personal Time Off','Rejected'),('L1013',10002,'2025-04-28','2025-05-12','2025-05-16','Vacation Leave','Approved'),('L1014',10019,'2025-05-26','2025-05-20','2025-05-25','Medical Leave','Approved'),('L1015',10026,'2025-05-18','2025-06-01','2025-06-10','Leave Without Pay','Approved'),('L1016',10011,'2025-06-01','2025-06-15','2025-06-16','Personal Time Off','Approved'),('L1017',10033,'2025-06-08','2025-06-22','2025-06-30','Compensatory Time Off','Approved');
/*!40000 ALTER TABLE `leaves` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `payroll_summary_db`
--

DROP TABLE IF EXISTS `payroll_summary_db`;
/*!50001 DROP VIEW IF EXISTS `payroll_summary_db`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `payroll_summary_db` AS SELECT 
 1 AS `Employee_No`,
 1 AS `Employee_Full_Name`,
 1 AS `Position`,
 1 AS `Department`,
 1 AS `Period_Start_Date`,
 1 AS `Period_End_Date`,
 1 AS `Gross_Income`,
 1 AS `SSS_Num`,
 1 AS `SSS_Contribution`,
 1 AS `PhilHealth_Num`,
 1 AS `PhilHealth_Contribution`,
 1 AS `Pag_Ibig_Num`,
 1 AS `Pag_Ibig_Contribution`,
 1 AS `TIN`,
 1 AS `Withholding_Tax`,
 1 AS `Net_Pay`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `payrollmph`
--

DROP TABLE IF EXISTS `payrollmph`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payrollmph` (
  `Payroll_ID` int NOT NULL AUTO_INCREMENT,
  `EID` int NOT NULL,
  `Month` varchar(20) DEFAULT NULL,
  `Year` int DEFAULT NULL,
  `Gross_Salary` decimal(10,2) DEFAULT NULL,
  `Deductions` decimal(10,2) DEFAULT NULL,
  `Net_Salary` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`Payroll_ID`),
  KEY `fk_payrollmph` (`EID`),
  CONSTRAINT `fk_payrollmph` FOREIGN KEY (`EID`) REFERENCES `employee` (`EID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payrollmph`
--

LOCK TABLES `payrollmph` WRITE;
/*!40000 ALTER TABLE `payrollmph` DISABLE KEYS */;
/*!40000 ALTER TABLE `payrollmph` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `payslipview_db`
--

DROP TABLE IF EXISTS `payslipview_db`;
/*!50001 DROP VIEW IF EXISTS `payslipview_db`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `payslipview_db` AS SELECT 
 1 AS `Period_Start_Date`,
 1 AS `Period_End_Date`,
 1 AS `Employee_ID`,
 1 AS `Employee_Name`,
 1 AS `Designation`,
 1 AS `Monthly_Rate`,
 1 AS `Daily_Rate`,
 1 AS `Overtime_Hours`,
 1 AS `Overtime_Pay`,
 1 AS `Gross_Income`,
 1 AS `Rice_Subsidy`,
 1 AS `Phone_Allowance`,
 1 AS `Clothing_Allowance`,
 1 AS `Total_Benefits`,
 1 AS `Days_Worked`,
 1 AS `SSS_Contribution`,
 1 AS `Philhealth_Contribution`,
 1 AS `Pagibig_Contribution`,
 1 AS `Withholding_Tax`,
 1 AS `Total_Deductions`,
 1 AS `Take_Home_Pay`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `supervisors`
--

DROP TABLE IF EXISTS `supervisors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `supervisors` (
  `Supervisor_ID` int NOT NULL AUTO_INCREMENT,
  `Supervisor_Name` varchar(100) NOT NULL,
  PRIMARY KEY (`Supervisor_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `supervisors`
--

LOCK TABLES `supervisors` WRITE;
/*!40000 ALTER TABLE `supervisors` DISABLE KEYS */;
INSERT INTO `supervisors` VALUES (1,'Garcia, Manuel III'),(2,'Lim, Antonio'),(3,'Villanueva, Andrea Mae'),(4,'San, Jose Brad'),(5,'Aquino, Bianca Sofia '),(6,'Alvaro, Roderick'),(7,'Salcedo, Anthony'),(8,'Romualdez, Fredrick '),(9,'Mata, Christian'),(10,'De Leon, Selena'),(11,'Reyes, Isabella');
/*!40000 ALTER TABLE `supervisors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `test_timesheet`
--

DROP TABLE IF EXISTS `test_timesheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `test_timesheet` (
  `Attendance_ID` int NOT NULL AUTO_INCREMENT,
  `EID` int NOT NULL,
  `LogDate` varchar(10) NOT NULL,
  `LogTime` varchar(8) NOT NULL,
  `AttStatus` varchar(10) NOT NULL,
  PRIMARY KEY (`Attendance_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `test_timesheet`
--

LOCK TABLES `test_timesheet` WRITE;
/*!40000 ALTER TABLE `test_timesheet` DISABLE KEYS */;
INSERT INTO `test_timesheet` VALUES (5,10009,'2023-01-01','17:00:00','Present');
/*!40000 ALTER TABLE `test_timesheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timesheet`
--

DROP TABLE IF EXISTS `timesheet`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `timesheet` (
  `Attendance_ID` int NOT NULL AUTO_INCREMENT,
  `EID` int DEFAULT NULL,
  `LogDate` date DEFAULT NULL,
  `LogTime` time DEFAULT NULL,
  `AttStatus` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`Attendance_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timesheet`
--

LOCK TABLES `timesheet` WRITE;
/*!40000 ALTER TABLE `timesheet` DISABLE KEYS */;
INSERT INTO `timesheet` VALUES (1,10001,'2023-12-18','08:30:00','Present'),(2,10009,'2023-01-01','17:00:00','Present'),(3,10009,'2023-01-01','17:00:00','Present'),(4,10009,'2023-01-01','17:00:00','Present'),(5,10009,'2023-01-01','17:00:00','Present'),(6,10009,'2023-01-01','17:00:00','Present'),(7,10007,'2023-12-18','08:40:00','Present'),(8,10008,'2023-12-18','09:10:00','Late'),(9,10009,'2023-12-18','08:25:00','Present'),(10,10010,'2023-12-18','08:30:00','Present'),(11,10011,'2023-12-18','09:05:00','Late'),(12,10012,'2023-12-18','08:50:00','Present'),(13,10013,'2023-12-18','08:45:00','Present'),(14,10014,'2023-12-18','08:55:00','Present'),(15,10015,'2023-12-18','08:40:00','Present'),(16,10016,'2023-12-18','09:00:00','Late'),(17,10017,'2023-12-18','08:35:00','Present'),(18,10018,'2023-12-18','08:50:00','Present'),(19,10019,'2023-12-18','08:30:00','Present'),(20,10020,'2023-12-18','09:10:00','Late'),(21,10021,'2023-12-18','08:45:00','Present'),(22,10022,'2023-12-18','08:55:00','Present'),(23,10023,'2023-12-18','08:40:00','Present'),(24,10024,'2023-12-18','09:05:00','Late'),(25,10025,'2023-12-18','08:50:00','Present'),(26,10026,'2023-12-18','08:30:00','Present'),(27,10027,'2023-12-18','09:00:00','Late'),(28,10028,'2023-12-18','08:35:00','Present'),(29,10029,'2023-12-18','08:55:00','Present'),(30,10030,'2023-12-18','08:40:00','Present'),(31,10031,'2023-12-18','09:10:00','Late'),(32,10032,'2023-12-18','08:50:00','Present'),(33,10033,'2023-12-18','08:30:00','Present'),(34,10034,'2023-12-18','08:45:00','Present'),(35,10001,'2023-12-19','08:30:00','Present'),(36,10002,'2023-12-19','08:45:00','Present'),(37,10003,'2023-12-19','08:50:00','Present'),(38,10004,'2023-12-19','08:55:00','Late'),(39,10005,'2023-12-19','09:00:00','Late'),(40,10006,'2023-12-19','08:35:00','Present');
/*!40000 ALTER TABLE `timesheet` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'payrollsystem_db'
--

--
-- Final view structure for view `employee_payroll_summary`
--

/*!50001 DROP VIEW IF EXISTS `employee_payroll_summary`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `employee_payroll_summary` AS select `e`.`EID` AS `employee_id`,concat(`e`.`Last_Name`,', ',`e`.`First_Name`) AS `Employee_Name`,`d`.`Designation_Name` AS `Position`,`dep`.`Department_Name` AS `Department`,`pc`.`Basic_Salary` AS `Monthly_Rate`,(`pc`.`Hourly_Rate` * 8) AS `Daily_Rate`,coalesce((select count(distinct `timesheet`.`LogDate`) from `timesheet` where (`timesheet`.`EID` = `e`.`EID`)),0) AS `Days_Worked`,`pv`.`Over_Time` AS `Overtime_Hours`,(`pc`.`Hourly_Rate` * `pv`.`Over_Time`) AS `Overtime_Pay`,(`pc`.`Basic_Salary` + (`pc`.`Hourly_Rate` * `pv`.`Over_Time`)) AS `Gross_Income`,`pc`.`Rice_Subsidy` AS `Rice_Subsidy`,`pc`.`Phone_Allowance` AS `Phone_Allowance`,`pc`.`Clothing_Allowance` AS `Clothing_Allowance`,((`pc`.`Rice_Subsidy` + `pc`.`Phone_Allowance`) + `pc`.`Clothing_Allowance`) AS `Total_Benefits`,coalesce((select `ed_sss`.`Amount` from (`employee_deductions` `ed_sss` join `deduction_types` `dt_sss` on((`ed_sss`.`Deduction_ID` = `dt_sss`.`Deduction_ID`))) where ((`ed_sss`.`EID` = `e`.`EID`) and (`dt_sss`.`Deduction_Name` = 'SSS'))),0) AS `SSS_Contribution`,coalesce((select `ed_phil`.`Amount` from (`employee_deductions` `ed_phil` join `deduction_types` `dt_phil` on((`ed_phil`.`Deduction_ID` = `dt_phil`.`Deduction_ID`))) where ((`ed_phil`.`EID` = `e`.`EID`) and (`dt_phil`.`Deduction_Name` = 'PhilHealth'))),0) AS `Philhealth_Contribution`,coalesce((select `ed_pag`.`Amount` from (`employee_deductions` `ed_pag` join `deduction_types` `dt_pag` on((`ed_pag`.`Deduction_ID` = `dt_pag`.`Deduction_ID`))) where ((`ed_pag`.`EID` = `e`.`EID`) and (`dt_pag`.`Deduction_Name` = 'PagIBIG'))),0) AS `Pagibig_Contribution`,coalesce((select `ed_wt`.`Amount` from (`employee_deductions` `ed_wt` join `deduction_types` `dt_wt` on((`ed_wt`.`Deduction_ID` = `dt_wt`.`Deduction_ID`))) where ((`ed_wt`.`EID` = `e`.`EID`) and (`dt_wt`.`Deduction_Name` = 'Withholding_Tax'))),0) AS `Withholding_Tax`,coalesce((select sum(`ed_sum`.`Amount`) from `employee_deductions` `ed_sum` where (`ed_sum`.`EID` = `e`.`EID`)),0) AS `Total_Deductions`,(((`pc`.`Basic_Salary` + (`pc`.`Hourly_Rate` * `pv`.`Over_Time`)) + ((`pc`.`Rice_Subsidy` + `pc`.`Phone_Allowance`) + `pc`.`Clothing_Allowance`)) - coalesce((select sum(`ed_sum`.`Amount`) from `employee_deductions` `ed_sum` where (`ed_sum`.`EID` = `e`.`EID`)),0)) AS `Take_Home_Pay` from ((((`employee` `e` join `designations` `d` on((`e`.`Designation_ID` = `d`.`Designation_ID`))) join `departments` `dep` on((`d`.`Department_ID` = `dep`.`Department_ID`))) join `employee_payroll_components` `pc` on((`e`.`EID` = `pc`.`EID`))) join `employee_payroll_variables` `pv` on((`e`.`EID` = `pv`.`EID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `payroll_summary_db`
--

/*!50001 DROP VIEW IF EXISTS `payroll_summary_db`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `payroll_summary_db` AS select `e`.`EID` AS `Employee_No`,concat(`e`.`Last_Name`,', ',`e`.`First_Name`) AS `Employee_Full_Name`,`d`.`Designation_Name` AS `Position`,`dp`.`Department_Name` AS `Department`,'2023-12-01' AS `Period_Start_Date`,'2023-12-31' AS `Period_End_Date`,(`pc`.`Basic_Salary` + (`pc`.`Hourly_Rate` * `pv`.`Over_Time`)) AS `Gross_Income`,`g`.`SSS_Num` AS `SSS_Num`,coalesce(sum((case when (`ed`.`Deduction_ID` = 1) then `ed`.`Amount` end)),0) AS `SSS_Contribution`,`g`.`Philhealth_Num` AS `PhilHealth_Num`,least(coalesce(sum((case when (`ed`.`Deduction_ID` = 2) then `ed`.`Amount` end)),0),900.00) AS `PhilHealth_Contribution`,`g`.`Pagibig_Num` AS `Pag_Ibig_Num`,least(coalesce(sum((case when (`ed`.`Deduction_ID` = 3) then `ed`.`Amount` end)),0),100.00) AS `Pag_Ibig_Contribution`,`g`.`TIN_Num` AS `TIN`,coalesce(sum((case when (`ed`.`Deduction_ID` = 4) then `ed`.`Amount` end)),0) AS `Withholding_Tax`,((`pc`.`Basic_Salary` + (`pc`.`Hourly_Rate` * `pv`.`Over_Time`)) - (((coalesce(sum((case when (`ed`.`Deduction_ID` = 1) then `ed`.`Amount` end)),0) + coalesce(sum(least((case when (`ed`.`Deduction_ID` = 2) then `ed`.`Amount` end),900.00)),0)) + coalesce(sum(least((case when (`ed`.`Deduction_ID` = 3) then `ed`.`Amount` end),100.00)),0)) + coalesce(sum((case when (`ed`.`Deduction_ID` = 4) then `ed`.`Amount` end)),0))) AS `Net_Pay` from ((((((`employee` `e` join `employee_payroll_components` `pc` on((`e`.`EID` = `pc`.`EID`))) join `employee_payroll_variables` `pv` on((`e`.`EID` = `pv`.`EID`))) join `employee_government_ids` `g` on((`e`.`EID` = `g`.`EID`))) join `designations` `d` on((`e`.`Designation_ID` = `d`.`Designation_ID`))) join `departments` `dp` on((`d`.`Department_ID` = `dp`.`Department_ID`))) left join `employee_deductions` `ed` on((`e`.`EID` = `ed`.`EID`))) group by `e`.`EID`,`d`.`Designation_Name`,`dp`.`Department_Name`,`g`.`SSS_Num`,`g`.`Philhealth_Num`,`g`.`Pagibig_Num`,`g`.`TIN_Num` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;

--
-- Final view structure for view `payslipview_db`
--

/*!50001 DROP VIEW IF EXISTS `payslipview_db`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `payslipview_db` AS select '2023-12-01' AS `Period_Start_Date`,'2023-12-31' AS `Period_End_Date`,`e`.`EID` AS `Employee_ID`,concat(`e`.`Last_Name`,', ',`e`.`First_Name`) AS `Employee_Name`,`d`.`Designation_Name` AS `Designation`,`pc`.`Basic_Salary` AS `Monthly_Rate`,(`pc`.`Hourly_Rate` * 8) AS `Daily_Rate`,`pv`.`Over_Time` AS `Overtime_Hours`,(`pc`.`Hourly_Rate` * `pv`.`Over_Time`) AS `Overtime_Pay`,(`pc`.`Basic_Salary` + (`pc`.`Hourly_Rate` * `pv`.`Over_Time`)) AS `Gross_Income`,`pc`.`Rice_Subsidy` AS `Rice_Subsidy`,`pc`.`Phone_Allowance` AS `Phone_Allowance`,`pc`.`Clothing_Allowance` AS `Clothing_Allowance`,((`pc`.`Rice_Subsidy` + `pc`.`Phone_Allowance`) + `pc`.`Clothing_Allowance`) AS `Total_Benefits`,(select count(distinct `timesheet`.`LogDate`) from `timesheet` where ((`timesheet`.`EID` = `e`.`EID`) and (`timesheet`.`LogDate` between '2023-12-01' and '2023-12-31'))) AS `Days_Worked`,coalesce(sum((case when (`dt`.`Deduction_Name` = 'SSS') then `ed`.`Amount` end)),0) AS `SSS_Contribution`,least(coalesce(sum((case when (`dt`.`Deduction_Name` = 'PhilHealth') then `ed`.`Amount` end)),0),900.00) AS `Philhealth_Contribution`,least(coalesce(sum((case when (`dt`.`Deduction_Name` = 'PagIBIG') then `ed`.`Amount` end)),0),100.00) AS `Pagibig_Contribution`,coalesce(sum((case when (`dt`.`Deduction_Name` = 'Withholding_Tax') then `ed`.`Amount` end)),0) AS `Withholding_Tax`,coalesce(sum(`ed`.`Amount`),0) AS `Total_Deductions`,((`pc`.`Basic_Salary` + (`pc`.`Hourly_Rate` * `pv`.`Over_Time`)) - coalesce(sum(`ed`.`Amount`),0)) AS `Take_Home_Pay` from (((((`employee` `e` join `employee_payroll_components` `pc` on((`e`.`EID` = `pc`.`EID`))) join `employee_payroll_variables` `pv` on((`e`.`EID` = `pv`.`EID`))) join `designations` `d` on((`e`.`Designation_ID` = `d`.`Designation_ID`))) left join `employee_deductions` `ed` on((`e`.`EID` = `ed`.`EID`))) left join `deduction_types` `dt` on((`ed`.`Deduction_ID` = `dt`.`Deduction_ID`))) group by `e`.`EID`,`d`.`Designation_Name` */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-07-18  3:30:22
