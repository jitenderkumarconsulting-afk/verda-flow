CREATE DATABASE  IF NOT EXISTS `verdaflow` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `verdaflow`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 103.212.235.82    Database: verdaflow
-- ------------------------------------------------------
-- Server version	5.7.25-0ubuntu0.18.04.2

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
-- Table structure for table `audit_driver_location`
--

DROP TABLE IF EXISTS `audit_driver_location`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_driver_location` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `driver_location_detail_id` int(11) unsigned NOT NULL,
  `lat` decimal(12,9) DEFAULT NULL,
  `lng` decimal(12,9) DEFAULT NULL,
  `rotation` decimal(12,9) DEFAULT NULL,
  `route` mediumtext,
  `order_id` int(11) unsigned DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_audit_driver_location_driver_id_idx` (`driver_location_detail_id`),
  KEY `fk_audit_driver_location_order_id_idx` (`order_id`),
  CONSTRAINT `fk_audit_driver_location_driver_id` FOREIGN KEY (`driver_location_detail_id`) REFERENCES `driver_location_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_driver_location_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_login`
--

DROP TABLE IF EXISTS `audit_login`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_login` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `status` enum('FAILED','SUCCESS') DEFAULT 'FAILED',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_ audit_login_user_id_idx` (`user_id`),
  CONSTRAINT `fk_ audit_login_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_order_status`
--

DROP TABLE IF EXISTS `audit_order_status`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_order_status` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `order_id` int(11) unsigned NOT NULL,
  `order_status` enum('PROCESSING','PLACED','CONFIRMED','PREPARED','DRIVER_ASSIGNED','REASSIGN_DRIVER','OUT_FOR_DELIVERY','DELIVERY_FAILED','COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_audit_order_status_user_id_idx` (`user_id`),
  KEY `fk_audit_order_status_order_id_idx` (`order_id`),
  CONSTRAINT `fk_audit_order_status_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_order_status_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_promo_code`
--

DROP TABLE IF EXISTS `audit_promo_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_promo_code` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `promo_code_id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned NOT NULL,
  `order_id` int(11) unsigned NOT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_audit_promo_code_promo_code_id_idx` (`promo_code_id`),
  KEY `fk_audit_promo_code_user_id_idx` (`user_id`),
  KEY `fk_audit_promo_code_order_id_idx` (`order_id`),
  CONSTRAINT `fk_audit_promo_code_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_promo_code_promo_code_id` FOREIGN KEY (`promo_code_id`) REFERENCES `promo_codes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_promo_code_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `audit_rating`
--

DROP TABLE IF EXISTS `audit_rating`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `audit_rating` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_rating_detail_id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned NOT NULL,
  `by_user_id` int(11) unsigned NOT NULL,
  `rating` float DEFAULT NULL,
  `note` varchar(255) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_audit_rating_order_rating_detail_id_idx` (`order_rating_detail_id`),
  KEY `fk_audit_rating_user_id_idx` (`user_id`),
  KEY `fk_audit_rating_by_user_id_idx` (`by_user_id`),
  CONSTRAINT `fk_audit_rating_by_user_id` FOREIGN KEY (`by_user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_rating_order_rating_detail_id` FOREIGN KEY (`order_rating_detail_id`) REFERENCES `order_rating_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_audit_rating_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `cart_details`
--

DROP TABLE IF EXISTS `cart_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `cart_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `product_id` int(11) unsigned NOT NULL,
  `product_price_detail_id` int(11) unsigned NOT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_cart_details_user_id_idx` (`user_id`),
  KEY `fk_cart_details_product_id_idx` (`product_id`),
  KEY `fk_cart_details_product_price_detail_id_idx` (`product_price_detail_id`),
  CONSTRAINT `fk_cart_details_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_details_product_price_detail_id` FOREIGN KEY (`product_price_detail_id`) REFERENCES `product_price_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_cart_details_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `customer_address_details`
--

DROP TABLE IF EXISTS `customer_address_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer_address_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `customer_id` int(11) unsigned NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `country_code` varchar(50) DEFAULT NULL,
  `phone_number` varchar(50) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `lat` decimal(12,9) NOT NULL DEFAULT '0.000000000',
  `lng` decimal(12,9) NOT NULL DEFAULT '0.000000000',
  `is_default` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_customer_address_details_customer_id_idx` (`customer_id`),
  CONSTRAINT `fk_customer_address_details_customer_id` FOREIGN KEY (`customer_id`) REFERENCES `user_customer_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `device_details`
--

DROP TABLE IF EXISTS `device_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `device_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `device_token` varchar(255) DEFAULT NULL,
  `device_type` enum('IOS','ANDROID','WEB') DEFAULT 'WEB',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_device_details_user_id_idx` (`user_id`),
  CONSTRAINT `fk_device_details_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `driver_location_details`
--

DROP TABLE IF EXISTS `driver_location_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `driver_location_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) unsigned NOT NULL,
  `lat` decimal(12,9) NOT NULL DEFAULT '0.000000000',
  `lng` decimal(12,9) NOT NULL DEFAULT '0.000000000',
  `rotation` decimal(12,9) NOT NULL DEFAULT '0.000000000',
  `route` mediumtext,
  `order_id` int(11) unsigned DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_driver_location_details_driver_id_idx` (`driver_id`),
  KEY `fk_driver_location_details_order_id_idx` (`order_id`),
  CONSTRAINT `fk_driver_location_details_driver_id` FOREIGN KEY (`driver_id`) REFERENCES `user_driver_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_driver_location_details_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `driver_order_mapping`
--

DROP TABLE IF EXISTS `driver_order_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `driver_order_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) unsigned NOT NULL,
  `order_id` int(11) unsigned NOT NULL,
  `is_rejected` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_driver_order_mapping_driver_id_idx` (`driver_id`),
  KEY `fk_driver_order_mapping_order_id_idx` (`order_id`),
  CONSTRAINT `fk_driver_order_mapping_driver_id` FOREIGN KEY (`driver_id`) REFERENCES `user_driver_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_driver_order_mapping_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favorite_dispatcher_mapping`
--

DROP TABLE IF EXISTS `favorite_dispatcher_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favorite_dispatcher_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `dispatcher_id` int(11) unsigned NOT NULL,
  `is_fav` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_favorite_dispatcher_mapping_user_id_idx` (`user_id`),
  KEY `fk_favorite_dispatcher_mapping_dispatcher_id_idx` (`dispatcher_id`),
  CONSTRAINT `fk_favorite_dispatcher_mapping_dispatcher_id` FOREIGN KEY (`dispatcher_id`) REFERENCES `user_dispatcher_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_favorite_dispatcher_mapping_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favorite_order_mapping`
--

DROP TABLE IF EXISTS `favorite_order_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favorite_order_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `order_id` int(11) unsigned NOT NULL,
  `is_fav` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_favorite_order_mapping_user_id_idx` (`user_id`),
  KEY `fk_favorite_order_mapping_order_id_idx` (`order_id`),
  CONSTRAINT `fk_favorite_order_mapping_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_favorite_order_mapping_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `favorite_product_mapping`
--

DROP TABLE IF EXISTS `favorite_product_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `favorite_product_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `product_id` int(11) unsigned NOT NULL,
  `is_fav` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_favorite_product_mapping_user_id_idx` (`user_id`),
  KEY `fk_favorite_product_mapping_product_id_idx` (`product_id`),
  CONSTRAINT `fk_favorite_product_mapping_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_favorite_product_mapping_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `forgot_password`
--

DROP TABLE IF EXISTS `forgot_password`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `forgot_password` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `verification_key` varchar(100) DEFAULT NULL,
  `expires_at` timestamp NULL DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `is_admin` tinyint(1) DEFAULT '0',
  `status` enum('REQUESTED','RESET') DEFAULT 'REQUESTED',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_forgot_password_user_idx_idx` (`user_id`),
  CONSTRAINT `fk_forgot_password_user_idx` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `master_categories`
--

DROP TABLE IF EXISTS `master_categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `master_categories` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `master_effects`
--

DROP TABLE IF EXISTS `master_effects`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `master_effects` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `master_etas`
--

DROP TABLE IF EXISTS `master_etas`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `master_etas` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `master_roles`
--

DROP TABLE IF EXISTS `master_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `master_roles` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `role` enum('ADMIN','DISPATCHER','DRIVER','CUSTOMER') DEFAULT NULL,
  `is_deleted` tinyint(1) NOT NULL DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `master_types`
--

DROP TABLE IF EXISTS `master_types`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `master_types` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `notifications` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(255) DEFAULT NULL,
  `body` varchar(255) DEFAULT NULL,
  `body_formatted` varchar(255) DEFAULT NULL,
  `is_read` tinyint(1) DEFAULT '0',
  `order_id` int(11) unsigned DEFAULT NULL,
  `user_id` int(11) unsigned DEFAULT NULL,
  `created_by` int(11) unsigned NOT NULL,
  `notification_type` enum('CUSTOM','TERMS_UPDATED','PRIVACY_UPDATED','ORDER_PLACED','ORDER_CONFIRMED','ORDER_PREPARED','ORDER_DRIVER_ASSIGNED','ORDER_REASSIGN_DRIVER','ORDER_OUT_FOR_DELIVERY','ORDER_DELIVERY_FAILED','ORDER_COMPLETED','ORDER_CANCELLED_BY_USER','ORDER_CANCELLED_BY_STORE') DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_notifications_order_id_idx` (`order_id`),
  KEY `fk_notifications_user_id_idx` (`user_id`),
  KEY `fk_notifications_created_by_idx` (`created_by`),
  CONSTRAINT `fk_notifications_created_by` FOREIGN KEY (`created_by`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_notifications_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_notifications_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_item_details`
--

DROP TABLE IF EXISTS `order_item_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_item_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) unsigned NOT NULL,
  `product_id` int(11) unsigned NOT NULL,
  `price_type` int(11) DEFAULT NULL,
  `price` decimal(9,2) unsigned NOT NULL DEFAULT '0.00',
  `is_discounted` tinyint(1) DEFAULT '0',
  `discounted_price` decimal(9,2) unsigned DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_order_item_details_order_id_idx` (`order_id`),
  KEY `fk_order_item_details_product_id_idx` (`product_id`),
  CONSTRAINT `fk_order_item_details_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_item_details_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_price_details`
--

DROP TABLE IF EXISTS `order_price_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_price_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) unsigned NOT NULL,
  `sub_total` decimal(9,2) unsigned NOT NULL DEFAULT '0.00',
  `tax` decimal(9,2) unsigned NOT NULL DEFAULT '0.00',
  `delivery_charges` decimal(9,2) unsigned NOT NULL DEFAULT '0.00',
  `total` decimal(9,2) unsigned NOT NULL DEFAULT '0.00',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_order_price_details_order_id_idx` (`order_id`),
  CONSTRAINT `fk_order_price_details_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `order_rating_details`
--

DROP TABLE IF EXISTS `order_rating_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `order_rating_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `order_id` int(11) unsigned NOT NULL,
  `is_dispatcher_pending_by_customer` tinyint(1) DEFAULT '0',
  `is_driver_pending_by_customer` tinyint(1) DEFAULT '0',
  `is_customer_pending_by_dispatcher` tinyint(1) DEFAULT '0',
  `is_customer_pending_by_driver` tinyint(1) DEFAULT '0',
  `dispatcher_rating_by_customer` float DEFAULT NULL,
  `driver_rating_by_customer` float DEFAULT NULL,
  `customer_rating_by_dispatcher` float DEFAULT NULL,
  `customer_rating_by_driver` float DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_order_rating_details_order_id_idx` (`order_id`),
  CONSTRAINT `fk_order_rating_details_order_id` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `orders` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `dispatcher_id` int(11) unsigned NOT NULL,
  `promo_code_id` int(11) unsigned DEFAULT NULL,
  `address_id` int(11) unsigned DEFAULT NULL,
  `delivery_type` enum('PICKUP','DELIVERY') DEFAULT NULL,
  `eta_id` int(11) unsigned NOT NULL,
  `order_status` enum('PROCESSING','PLACED','CONFIRMED','PREPARED','DRIVER_ASSIGNED','REASSIGN_DRIVER','OUT_FOR_DELIVERY','DELIVERY_FAILED','COMPLETED','CANCELLED_BY_USER','CANCELLED_BY_STORE') DEFAULT NULL,
  `cancel_expiry_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `is_placed` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_orders_user_id_idx` (`user_id`),
  KEY `fk_orders_dispatcher_id_idx` (`dispatcher_id`),
  KEY `fk_orders_promo_code_id_idx` (`promo_code_id`),
  KEY `fk_orders_address_id_idx` (`address_id`),
  KEY `fk_orders_etas_id_idx` (`eta_id`),
  CONSTRAINT `fk_orders_address_id` FOREIGN KEY (`address_id`) REFERENCES `customer_address_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_dispatcher_id` FOREIGN KEY (`dispatcher_id`) REFERENCES `user_dispatcher_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_etas_id` FOREIGN KEY (`eta_id`) REFERENCES `master_etas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_promo_code_id` FOREIGN KEY (`promo_code_id`) REFERENCES `promo_codes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_aggregate_details`
--

DROP TABLE IF EXISTS `product_aggregate_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_aggregate_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_aggregate_id` int(11) unsigned NOT NULL,
  `user_id` int(11) unsigned NOT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_product_aggregate_details_product_aggregate_id_idx` (`product_aggregate_id`),
  KEY `fk_product_aggregate_details_user_id_idx` (`user_id`),
  CONSTRAINT `fk_product_aggregate_details_product_aggregate_id` FOREIGN KEY (`product_aggregate_id`) REFERENCES `product_aggregates` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_product_aggregate_details_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_aggregates`
--

DROP TABLE IF EXISTS `product_aggregates`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_aggregates` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `product_aggregate_type` enum('SOLD','LIKED','VIEWED') DEFAULT NULL,
  `count` int(11) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_product_aggregates_product_id_idx` (`product_id`),
  CONSTRAINT `fk_product_aggregates_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_effect_mapping`
--

DROP TABLE IF EXISTS `product_effect_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_effect_mapping` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `effect_id` int(11) unsigned NOT NULL,
  `value` int(11) NOT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_mapping_product_id_idx` (`product_id`),
  KEY `fk_mapping_effect_id_idx` (`effect_id`),
  CONSTRAINT `fk_mapping_effect_id` FOREIGN KEY (`effect_id`) REFERENCES `master_effects` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_mapping_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `product_price_details`
--

DROP TABLE IF EXISTS `product_price_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `product_price_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `product_id` int(11) unsigned NOT NULL,
  `priceType` int(11) DEFAULT NULL,
  `price` decimal(9,2) unsigned NOT NULL DEFAULT '0.00',
  `is_available` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_product_price_details_product_id_idx` (`product_id`),
  CONSTRAINT `fk_product_price_details_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dispatcher_id` int(11) unsigned NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `groupType` int(11) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `image_thumb` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `thc` float DEFAULT NULL,
  `cbd` float DEFAULT NULL,
  `category_id` int(11) unsigned NOT NULL,
  `type_id` int(11) unsigned NOT NULL,
  `promo_code_id` int(11) unsigned DEFAULT NULL,
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_products_dispatcher_id_idx` (`dispatcher_id`),
  KEY `fk_products_category_id_idx` (`category_id`),
  KEY `fk_products_type_id_idx` (`type_id`),
  KEY `fk_products_promo_code_id_idx` (`promo_code_id`),
  CONSTRAINT `fk_products_category_id` FOREIGN KEY (`category_id`) REFERENCES `master_categories` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_dispatcher_id` FOREIGN KEY (`dispatcher_id`) REFERENCES `user_dispatcher_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_promo_code_id` FOREIGN KEY (`promo_code_id`) REFERENCES `promo_codes` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_products_type_id` FOREIGN KEY (`type_id`) REFERENCES `master_types` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `promo_codes`
--

DROP TABLE IF EXISTS `promo_codes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `promo_codes` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `dispatcher_id` int(11) unsigned NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `discount` float DEFAULT NULL,
  `start_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `end_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `promo_code_type` enum('MULTIPLE_USE','SINGLE_USE','SINGLE_USE_PER_USER') DEFAULT 'MULTIPLE_USE',
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_promo_codes_dispatcher_id_idx` (`dispatcher_id`),
  CONSTRAINT `fk_promo_codes_dispatcher_id` FOREIGN KEY (`dispatcher_id`) REFERENCES `user_dispatcher_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `registered_scheduler`
--

DROP TABLE IF EXISTS `registered_scheduler`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `registered_scheduler` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `scheduler_name` varchar(45) DEFAULT NULL,
  `scheduler_type` varchar(45) DEFAULT NULL,
  `heart_beat` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `country_code` varchar(7) DEFAULT NULL,
  `mobile_number` varchar(20) DEFAULT NULL,
  `password` varchar(255) NOT NULL,
  `push_notification_status` tinyint(1) DEFAULT '1',
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_customer_details`
--

DROP TABLE IF EXISTS `user_customer_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_customer_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `first_name` varchar(255) DEFAULT NULL,
  `last_name` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `image_thumb` varchar(255) DEFAULT NULL,
  `application_status` enum('APPROVED','PENDING','REJECTED') DEFAULT 'PENDING',
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_user_customer_details_user_id_idx` (`user_id`),
  CONSTRAINT `fk_user_customer_details_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_dispatcher_details`
--

DROP TABLE IF EXISTS `user_dispatcher_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_dispatcher_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `store_name` varchar(255) DEFAULT NULL,
  `manager_name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL,
  `lat` decimal(12,9) NOT NULL DEFAULT '0.000000000',
  `lng` decimal(12,9) NOT NULL DEFAULT '0.000000000',
  `image` varchar(255) DEFAULT NULL,
  `image_thumb` varchar(255) DEFAULT NULL,
  `eta_id` int(11) unsigned NOT NULL,
  `delivery_charges` decimal(9,2) unsigned NOT NULL DEFAULT '0.00',
  `application_status` enum('APPROVED','PENDING','REJECTED') DEFAULT 'PENDING',
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_user_dispatcher_details_user_id_idx` (`user_id`),
  KEY `fk_user_dispatcher_details_etas_id_idx` (`eta_id`),
  CONSTRAINT `fk_user_dispatcher_details_etas_id` FOREIGN KEY (`eta_id`) REFERENCES `master_etas` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_dispatcher_details_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_driver_details`
--

DROP TABLE IF EXISTS `user_driver_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_driver_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `dispatcher_id` int(11) unsigned NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `year` varchar(4) DEFAULT NULL,
  `model` varchar(50) DEFAULT NULL,
  `make` varchar(50) DEFAULT NULL,
  `vehicle_registration_photo` varchar(255) DEFAULT NULL,
  `driver_license_photo` varchar(255) DEFAULT NULL,
  `car_license_plate_photo` varchar(255) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `image_thumb` varchar(255) DEFAULT NULL,
  `application_status` enum('APPROVED','PENDING','REJECTED') DEFAULT 'PENDING',
  `is_online` tinyint(1) DEFAULT '0',
  `active` tinyint(1) DEFAULT '1',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_user_driver_details_user_id_idx` (`user_id`),
  KEY `fk_user_driver_details_dispatcher_id_idx` (`dispatcher_id`),
  CONSTRAINT `fk_user_driver_details_dispatcher_id` FOREIGN KEY (`dispatcher_id`) REFERENCES `user_dispatcher_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_driver_details_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_rating_details`
--

DROP TABLE IF EXISTS `user_rating_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_rating_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(11) unsigned NOT NULL,
  `avg` float DEFAULT NULL,
  `total` float DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `fk_user_rating_details_user_id_idx` (`user_id`),
  CONSTRAINT `fk_user_rating_details_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_role_mapping`
--

DROP TABLE IF EXISTS `user_role_mapping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role_mapping` (
  `user_id` int(11) unsigned NOT NULL,
  `role_id` int(11) unsigned NOT NULL,
  `application_status` enum('APPROVED','PENDING','REJECTED') DEFAULT 'PENDING',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `fk_role_id_idx` (`role_id`),
  CONSTRAINT `fk_role_id` FOREIGN KEY (`role_id`) REFERENCES `master_roles` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_id` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `driver_inventory`
--

DROP TABLE IF EXISTS `driver_inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `driver_inventory` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `driver_id` int(11) unsigned NOT NULL,
  `product_id` int(11) unsigned NOT NULL,
  `is_in_stock` tinyint(1) DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   KEY `fk_driver_inventory_driver_id_idx` (`driver_id`),
   KEY `fk_driver_inventory_product_id_idx` (`product_id`),
   CONSTRAINT `fk_driver_inventory_driver_id` FOREIGN KEY (`driver_id`) REFERENCES `user_driver_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `fk_driver_inventory_product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `driver_inventory_details`
--

DROP TABLE IF EXISTS `driver_inventory_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `driver_inventory_details` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `driver_inventory_id` int(11) unsigned NOT NULL,
  `product_price_detail_id` int(11) unsigned NOT NULL,
  `quantity` int(11) NOT NULL DEFAULT '0',
  `is_deleted` tinyint(1) DEFAULT '0',
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modified_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`),
   KEY `fk_driver_inventory_details_driver_inventory_id_idx` (`driver_inventory_id`),
   KEY `fk_driver_inventory_details_product_price_detail_id_idx` (`product_price_detail_id`),
   CONSTRAINT `fk_driver_inventory_details_driver_inventory_id` FOREIGN KEY (`driver_inventory_id`) REFERENCES `driver_inventory` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `fk_driver_inventory_details_product_price_detail_id` FOREIGN KEY (`product_price_detail_id`) REFERENCES `product_price_details` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Dumping events for database 'verdaflow'
--

--
-- Dumping routines for database 'verdaflow'
--
/*!50003 DROP FUNCTION IF EXISTS `haversine` */;
ALTER DATABASE `verdaflow` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = '' */ ;
DELIMITER ;;
CREATE DEFINER=`D1sp3nsary`@`%` FUNCTION `haversine`(
        lat1 FLOAT, lon1 FLOAT,
        lat2 FLOAT, lon2 FLOAT
     ) RETURNS float
    NO SQL
    DETERMINISTIC
    COMMENT 'Returns the distance in degrees on the Earth between two known points of latitude and longitude'
BEGIN
    RETURN DEGREES(ACOS(
              COS(RADIANS(lat1)) *
              COS(RADIANS(lat2)) *
              COS(RADIANS(lon2) - RADIANS(lon1)) +
              SIN(RADIANS(lat1)) * SIN(RADIANS(lat2))
            ));
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
ALTER DATABASE `verdaflow` CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-02-28 14:13:41
