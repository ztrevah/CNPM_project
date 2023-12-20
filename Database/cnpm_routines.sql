CREATE DATABASE  IF NOT EXISTS `cnpm` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `cnpm`;
-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: localhost    Database: cnpm
-- ------------------------------------------------------
-- Server version	8.2.0

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
-- Dumping events for database 'cnpm'
--

--
-- Dumping routines for database 'cnpm'
--
/*!50003 DROP FUNCTION IF EXISTS `age` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`chien`@`localhost` FUNCTION `age`(dob date) RETURNS int
BEGIN
	declare tmp int;
	declare age int;
	set tmp = year(curdate())-year(dob);
	if month(dob) > month(curdate()) or (month(dob) = month(curdate()) and day(dob) > day(curdate()))
	then set age = tmp - 1;
	else set age = tmp;
	end if;
	return age;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `chiphivesinh` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`chien`@`localhost` FUNCTION `chiphivesinh`(IDHo char(50),nam char(4)) RETURNS int
BEGIN
	declare sum int;
    declare loaiho varchar(45);
    declare sonhankhautrongthang int;
    declare songuoitamvangtrongthang int;
    declare ngaybatdautrongthang char(10);
	declare ngaycuoicungtrongthang char(10);
    set sum = 0;
    set loaiho = (select LoaiSo from hokhau where SoHK = IDHo);
    
    set ngaybatdautrongthang = concat(nam,'-01-01');
    set ngaycuoicungtrongthang = concat(nam,'-01-31');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-02-01');
    set ngaycuoicungtrongthang = concat(nam,'-02-28');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-03-01');
    set ngaycuoicungtrongthang = concat(nam,'-03-31');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-04-01');
    set ngaycuoicungtrongthang = concat(nam,'-04-30');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-05-01');
    set ngaycuoicungtrongthang = concat(nam,'-05-31');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-06-01');
    set ngaycuoicungtrongthang = concat(nam,'-06-30');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-07-01');
    set ngaycuoicungtrongthang = concat(nam,'-07-31');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-08-01');
    set ngaycuoicungtrongthang = concat(nam,'-08-31');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-09-01');
    set ngaycuoicungtrongthang = concat(nam,'-09-30');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-10-01');
    set ngaycuoicungtrongthang = concat(nam,'-10-31');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-11-01');
    set ngaycuoicungtrongthang = concat(nam,'-11-31');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    set ngaybatdautrongthang = concat(nam,'-12-01');
    set ngaycuoicungtrongthang = concat(nam,'-12-31');
    select count(distinct NhanKhauID) into sonhankhautrongthang from nhankhau_hokhau where LoaiLuuTru = loaiho and HoKhauID = IDHo 
		and ( (NgayBatDau <=  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaybatdautrongthang,date))
			or (NgayBatDau >=  convert(ngaybatdautrongthang,date) and NgayKetThuc <=  convert(ngaycuoicungtrongthang,date))
            or (NgayKetThuc >=  convert(ngaycuoicungtrongthang,date) and NgayBatDau <=  convert(ngaycuoicungtrongthang,date)) );
	select count(distinct NhanKhauID) into songuoitamvangtrongthang from nhankhau_hokhau where LoaiLuuTru = N'Tạm vắng' and HoKhauID = IDHo 
		and NgayBatDau <  convert(ngaybatdautrongthang,date) and NgayKetThuc >=  convert(ngaycuoicungtrongthang,date);
	set sum = sum + sonhankhautrongthang - songuoitamvangtrongthang;
    
    return sum * 6000;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `tonghopphivesinh` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`chien`@`localhost` PROCEDURE `tonghopphivesinh`(idphi int,nam char(4))
BEGIN
	insert into dongphi(IDPhi,HoKhauID,DaDong,PhaiDong) 
		select idphi,SoHK,0,chiphivesinh(SoHK,nam)
        from hokhau
        where deleted = 0;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-20 11:37:33
