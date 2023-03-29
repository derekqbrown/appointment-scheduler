-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: localhost    Database: client_schedule
-- ------------------------------------------------------
-- Server version	8.0.25

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
-- Table structure for table `first_level_divisions`
--

DROP TABLE IF EXISTS `first_level_divisions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `first_level_divisions` (
  `Division_ID` int NOT NULL AUTO_INCREMENT,
  `Division` varchar(50) DEFAULT NULL,
  `Create_Date` datetime DEFAULT NULL,
  `Created_By` varchar(50) DEFAULT NULL,
  `Last_Update` timestamp NULL DEFAULT NULL,
  `Last_Updated_By` varchar(50) DEFAULT NULL,
  `Country_ID` int NOT NULL,
  PRIMARY KEY (`Division_ID`),
  KEY `fk_country_id_idx` (`Country_ID`),
  CONSTRAINT `fk_country_id` FOREIGN KEY (`Country_ID`) REFERENCES `countries` (`Country_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=3979 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `first_level_divisions`
--

LOCK TABLES `first_level_divisions` WRITE;
/*!40000 ALTER TABLE `first_level_divisions` DISABLE KEYS */;
INSERT INTO `first_level_divisions` VALUES (1,'Alabama','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(2,'Arizona','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(3,'Arkansas','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(4,'California','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(5,'Colorado','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(6,'Connecticut','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(7,'Delaware','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(8,'District of Columbia','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(9,'Florida','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(10,'Georgia','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(11,'Idaho','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(12,'Illinois','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(13,'Indiana','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(14,'Iowa','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(15,'Kansas','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(16,'Kentucky','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(17,'Louisiana','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(18,'Maine','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(19,'Maryland','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(20,'Massachusetts','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(21,'Michigan','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(22,'Minnesota','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(23,'Mississippi','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(24,'Missouri','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(25,'Montana','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(26,'Nebraska','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(27,'Nevada','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(28,'New Hampshire','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(29,'New Jersey','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(30,'New Mexico','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(31,'New York','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(32,'North Carolina','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(33,'North Dakota','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(34,'Ohio','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(35,'Oklahoma','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(36,'Oregon','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(37,'Pennsylvania','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(38,'Rhode Island','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(39,'South Carolina','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(40,'South Dakota','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(41,'Tennessee','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(42,'Texas','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(43,'Utah','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(44,'Vermont','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(45,'Virginia','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(46,'Washington','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(47,'West Virginia','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(48,'Wisconsin','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(49,'Wyoming','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',1),(52,'Hawaii','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(54,'Alaska','2021-09-10 13:14:09','script','2021-09-10 17:14:09','script',1),(60,'Northwest Territories','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(61,'Alberta','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(62,'British Columbia','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(63,'Manitoba','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(64,'New Brunswick','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(65,'Nova Scotia','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(66,'Prince Edward Island','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(67,'Ontario','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(68,'Québec','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(69,'Saskatchewan','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(70,'Nunavut','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(71,'Yukon','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(72,'Newfoundland and Labrador','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',3),(101,'England','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',2),(102,'Wales','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',2),(103,'Scotland','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',2),(104,'Northern Ireland','2021-09-10 13:14:10','script','2021-09-10 17:14:10','script',2);
/*!40000 ALTER TABLE `first_level_divisions` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-03-14 20:32:12
