-- MySQL dump 10.13  Distrib 5.7.12, for Win32 (AMD64)
--
-- Host: localhost    Database: verdaflow
-- ------------------------------------------------------
-- Server version	5.6.10

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Dumping data for table `master_roles`
--

LOCK TABLES `master_roles` WRITE;
/*!40000 ALTER TABLE `master_roles` DISABLE KEYS */;
INSERT INTO master_roles VALUES (1,'ADMIN',0,'2018-06-19 09:07:50','2018-06-19 09:08:10'),(2,'DISPATCHER',0,'2018-06-19 09:07:50','2018-06-19 09:08:10'),(3,'DRIVER',0,'2018-06-19 09:07:50','2018-06-19 09:08:10'),(4,'CUSTOMER',0,'2018-06-19 09:07:50','2018-06-19 09:08:10');
/*!40000 ALTER TABLE `master_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'dev.orgit@gmail.com',NULL,NULL,'$2a$10$2.ijjURd6LOBclAI.10Ca.GwdJgev6rFnFzsDxjZvQIO3RL82Ulzm',1,1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `user_role_mapping`
--

LOCK TABLES `user_role_mapping` WRITE;
/*!40000 ALTER TABLE `user_role_mapping` DISABLE KEYS */;
INSERT INTO `user_role_mapping` VALUES (1,1,'APPROVED',0,'2018-10-10 01:21:23','2018-10-10 01:21:23');
/*!40000 ALTER TABLE `user_role_mapping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `master_categories`
--

LOCK TABLES `master_categories` WRITE;
/*!40000 ALTER TABLE `master_categories` DISABLE KEYS */;
INSERT INTO `master_categories` VALUES (1,'Indica',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(2,'Sativa',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(3,'Hybrid',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(4,'Vapes',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23');
/*!40000 ALTER TABLE `master_categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `master_effects`
--

LOCK TABLES `master_effects` WRITE;
/*!40000 ALTER TABLE `master_effects` DISABLE KEYS */;
INSERT INTO `master_effects` VALUES (1,'Euphoric',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(2,'Happy',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(3,'Hungry',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(4,'Relaxed',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23');
/*!40000 ALTER TABLE `master_effects` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `master_types`
--

LOCK TABLES `master_types` WRITE;
/*!40000 ALTER TABLE `master_types` DISABLE KEYS */;
INSERT INTO `master_types` VALUES (1,'Flower',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(2,'Pre Rolls',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(3,'Concentrate',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(4,'Edibles',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(5,'Tincture',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(6,'Topicals',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23');
/*!40000 ALTER TABLE `master_types` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `master_etas`
--

LOCK TABLES `master_etas` WRITE;
/*!40000 ALTER TABLE `master_etas` DISABLE KEYS */;
INSERT INTO `master_etas` VALUES (1,'15 mins',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(2,'30 mins',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(3,'45 mins',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(4,'1 hour',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23'),(5,'1+ hours',1,0,'2018-10-10 01:21:23','2018-10-10 01:21:23');
/*!40000 ALTER TABLE `master_etas` ENABLE KEYS */;
UNLOCK TABLES;

/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-09-25 16:29:07
