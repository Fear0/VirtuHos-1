-- MySQL dump 10.13  Distrib 8.0.22, for Win64 (x86_64)
--
-- Host: localhost    Database: wb_interaktion_1
-- ------------------------------------------------------
-- Server version	8.0.22

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
-- Table structure for table `vh_group`
--

DROP TABLE IF EXISTS `vh_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vh_group` (
  `groupID` int NOT NULL,
  `size` int NOT NULL,
  `members` longtext NOT NULL,
  PRIMARY KEY (`groupID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vh_group`
--

LOCK TABLES `vh_group` WRITE;
/*!40000 ALTER TABLE `vh_group` DISABLE KEYS */;
INSERT INTO `vh_group` VALUES (0,3,'[\'ala.dryaev\', \'dav.sasse\', \'dav.sebode\']'),(1,3,'[\'jos.berger\', \'mar.deichsel\', \'mei.kokowski\']');
/*!40000 ALTER TABLE `vh_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vh_is_inside`
--

DROP TABLE IF EXISTS `vh_is_inside`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vh_is_inside` (
  `personalID` varchar(255) DEFAULT NULL,
  `roomID` int DEFAULT NULL,
  KEY `personalID` (`personalID`),
  KEY `roomID` (`roomID`),
  CONSTRAINT `vh_is_inside_ibfk_1` FOREIGN KEY (`personalID`) REFERENCES `vh_user` (`personalID`),
  CONSTRAINT `vh_is_inside_ibfk_2` FOREIGN KEY (`roomID`) REFERENCES `vh_room` (`roomID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vh_is_inside`
--

LOCK TABLES `vh_is_inside` WRITE;
/*!40000 ALTER TABLE `vh_is_inside` DISABLE KEYS */;
INSERT INTO `vh_is_inside` VALUES ('ala.dryaev',0),('dav.sasse',0),('dav.sebode',0),('jos.berger',1),('mar.deichsel',1),('mei.kokowski',1);
/*!40000 ALTER TABLE `vh_is_inside` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vh_room`
--

DROP TABLE IF EXISTS `vh_room`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vh_room` (
  `roomID` int NOT NULL,
  `type` varchar(255) NOT NULL,
  `size` int NOT NULL,
  `members` longtext,
  PRIMARY KEY (`roomID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vh_room`
--

LOCK TABLES `vh_room` WRITE;
/*!40000 ALTER TABLE `vh_room` DISABLE KEYS */;
INSERT INTO `vh_room` VALUES (0,'office',3,NULL),(1,'conference',3,NULL),(2,'hall',200,NULL);
/*!40000 ALTER TABLE `vh_room` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vh_user`
--

DROP TABLE IF EXISTS `vh_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vh_user` (
  `personalID` varchar(255) NOT NULL,
  `online_status` int NOT NULL,
  `groupID` int DEFAULT NULL,
  PRIMARY KEY (`personalID`),
  KEY `groupID` (`groupID`),
  CONSTRAINT `vh_user_ibfk_1` FOREIGN KEY (`groupID`) REFERENCES `vh_group` (`groupID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vh_user`
--

LOCK TABLES `vh_user` WRITE;
/*!40000 ALTER TABLE `vh_user` DISABLE KEYS */;
INSERT INTO `vh_user` VALUES ('ala.dryaev',1,0),('dav.sasse',1,0),('dav.sebode',1,0),('jos.berger',1,1),('mar.deichsel',1,1),('mei.kokowski',1,1);
/*!40000 ALTER TABLE `vh_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-11-26 23:29:12
