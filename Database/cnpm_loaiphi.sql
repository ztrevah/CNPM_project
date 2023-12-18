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
-- Table structure for table `loaiphi`
--

DROP TABLE IF EXISTS `loaiphi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `loaiphi` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `TenPhi` varchar(255) NOT NULL,
  `Loai` varchar(15) NOT NULL,
  `NgayBatDauThu` date NOT NULL,
  PRIMARY KEY (`ID`,`TenPhi`),
  UNIQUE KEY `TenPhi_UNIQUE` (`TenPhi`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `loaiphi`
--

LOCK TABLES `loaiphi` WRITE;
/*!40000 ALTER TABLE `loaiphi` DISABLE KEYS */;
INSERT INTO `loaiphi` VALUES (1,'Phí vệ sinh 2022','Phí thu','2023-01-01'),(2,'Chung tay đẩy lùi Covid-19','Đóng góp','2023-01-01'),(3,'Giúp đỡ học sinh nghèo vượt khó','Đóng góp','2023-01-01'),(4,'Giúp đỡ người cao tuổi','Đóng góp','2023-01-01'),(5,'Khen thưởng học sinh giỏi','Đóng góp','2023-01-01'),(6,'Trợ giúp đồng bào bị ảnh hưởng bão lụt','Đóng góp','2023-01-01'),(7,'Ủng hộ ngày tết thiếu nhi','Đóng góp','2023-01-01'),(8,'Ủng hộ ngày thương binh-liệt sỹ 27/07','Đóng góp','2023-01-01'),(9,'Ủng hộ vì người nghèo','Đóng góp','2023-01-01');
/*!40000 ALTER TABLE `loaiphi` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-19  0:13:35
