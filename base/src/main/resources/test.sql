-- MySQL dump 10.13  Distrib 5.6.42, for Win64 (x86_64)
--
-- Host: localhost    Database: test
-- ------------------------------------------------------
-- Server version	5.6.42-log

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
-- Table structure for table `tb_dict_entry`
--

DROP TABLE IF EXISTS `tb_dict_entry`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_dict_entry` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_CODE` varchar(255) COLLATE utf8_bin NOT NULL,
  `ENTRY_CODE` varchar(255) COLLATE utf8_bin NOT NULL,
  `ENTRY_NAME` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_dict_entry`
--

LOCK TABLES `tb_dict_entry` WRITE;
/*!40000 ALTER TABLE `tb_dict_entry` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_dict_entry` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_dict_type`
--

DROP TABLE IF EXISTS `tb_dict_type`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_dict_type` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_CODE` varchar(255) COLLATE utf8_bin NOT NULL,
  `TYPE_NAME` varchar(255) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_dict_type`
--

LOCK TABLES `tb_dict_type` WRITE;
/*!40000 ALTER TABLE `tb_dict_type` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_dict_type` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tb_sys_config`
--

DROP TABLE IF EXISTS `tb_sys_config`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tb_sys_config` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONFIG_KEY` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '配置项的KEY',
  `CONFIG_NAME` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '配置项的名称',
  `CONFIG_VALUE` varchar(255) COLLATE utf8_bin NOT NULL COMMENT '配置项的值',
  `REMARK` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tb_sys_config`
--

LOCK TABLES `tb_sys_config` WRITE;
/*!40000 ALTER TABLE `tb_sys_config` DISABLE KEYS */;
/*!40000 ALTER TABLE `tb_sys_config` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tu_menu`
--

DROP TABLE IF EXISTS `tu_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tu_menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_ID` int(11) DEFAULT NULL,
  `MENU_CODE` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `MENU_NAME` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `MENU_SHOW_NAME` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `MENU_COMPONENT` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '因为前后分离，不认url，只认component',
  `ID_FULL` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'JSONArray',
  `MENU_NAME_FULL` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '用/隔开',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tu_menu`
--

LOCK TABLES `tu_menu` WRITE;
/*!40000 ALTER TABLE `tu_menu` DISABLE KEYS */;
INSERT INTO `tu_menu` VALUES (0,NULL,NULL,'root','root',NULL,'[0]','root'),(1,0,'01','系统管理','系统管理','','[0,1]','root/系统管理'),(2,1,'0101','组织结构','组织结构','org','[0,1,2]','root/系统管理/组织结构'),(3,1,'0102','菜单管理','菜单管理','menua','[0,1,3]','root/系统管理/菜单管理'),(4,1,'0103','角色管理','角色管理','role','[0,1,4]','root/系统管理/角色管理'),(5,1,'0104','用户管理','用户管理','user','[0,1,5]','root/系统管理/用户管理');
/*!40000 ALTER TABLE `tu_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tu_org`
--

DROP TABLE IF EXISTS `tu_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tu_org` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_CODE` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '组织代码',
  `ORG_NAME` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '组织名称',
  `PARENT_ID` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '上级组织ID，顶级组织的该字段设置为null',
  `ID_FULL` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT 'JSONArray',
  `ORG_NAME_FULL` varchar(2000) COLLATE utf8_bin DEFAULT NULL COMMENT '用/隔开',
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tu_org`
--

LOCK TABLES `tu_org` WRITE;
/*!40000 ALTER TABLE `tu_org` DISABLE KEYS */;
INSERT INTO `tu_org` VALUES (0,NULL,'root',NULL,'[0]','root'),(1,'01','大无限股份公司','0','[0,1]','root/大无限股份公司'),(3,'02','超牛股份有限公司','0','[0,3]','root/超牛股份有限公司'),(4,'03','超人股份有限公司','0','[0,4]','root/超人股份有限公司'),(5,'04','机器人有限公司','0','[0,5]','root/机器人有限公司'),(6,'05','宇宙能源有限公司','0','[0,6]','root/宇宙能源有限公司'),(7,'06','x company','0','[0,7]','root/x company'),(8,'07','测试','0','[0,8]','root/测试'),(12,'0101','大无限中国公司','1','[0,1,12]','root/大无限股份公司/大无限中国公司'),(13,'0102','大无限美国公司','1','[0,1,13]','root/大无限股份公司/大无限美国公司'),(14,'0103','大无限俄罗斯公司','1','[0,1,14]','root/大无限股份公司/大无限俄罗斯公司'),(17,'08','测试2','0','[0,17]','root/测试2');
/*!40000 ALTER TABLE `tu_org` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tu_org_menu`
--

DROP TABLE IF EXISTS `tu_org_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tu_org_menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tu_org_menu`
--

LOCK TABLES `tu_org_menu` WRITE;
/*!40000 ALTER TABLE `tu_org_menu` DISABLE KEYS */;
/*!40000 ALTER TABLE `tu_org_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tu_org_user`
--

DROP TABLE IF EXISTS `tu_org_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tu_org_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tu_org_user`
--

LOCK TABLES `tu_org_user` WRITE;
/*!40000 ALTER TABLE `tu_org_user` DISABLE KEYS */;
INSERT INTO `tu_org_user` VALUES (1,1,9);
/*!40000 ALTER TABLE `tu_org_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tu_role`
--

DROP TABLE IF EXISTS `tu_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tu_role` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` int(11) DEFAULT NULL,
  `ROLE_CODE` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `ROLE_NAME` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tu_role`
--

LOCK TABLES `tu_role` WRITE;
/*!40000 ALTER TABLE `tu_role` DISABLE KEYS */;
INSERT INTO `tu_role` VALUES (1,1,'root','root'),(3,13,'test2','test2'),(4,14,'test3','test3');
/*!40000 ALTER TABLE `tu_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tu_role_menu`
--

DROP TABLE IF EXISTS `tu_role_menu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tu_role_menu` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` int(11) NOT NULL,
  `MENU_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tu_role_menu`
--

LOCK TABLES `tu_role_menu` WRITE;
/*!40000 ALTER TABLE `tu_role_menu` DISABLE KEYS */;
INSERT INTO `tu_role_menu` VALUES (1,1,1),(2,1,2),(3,1,3),(4,1,4),(5,1,5),(9,3,2),(10,4,2),(11,4,3),(12,4,4),(13,4,5);
/*!40000 ALTER TABLE `tu_role_menu` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tu_role_user`
--

DROP TABLE IF EXISTS `tu_role_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tu_role_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tu_role_user`
--

LOCK TABLES `tu_role_user` WRITE;
/*!40000 ALTER TABLE `tu_role_user` DISABLE KEYS */;
INSERT INTO `tu_role_user` VALUES (1,1,9);
/*!40000 ALTER TABLE `tu_role_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tu_user`
--

DROP TABLE IF EXISTS `tu_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tu_user` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(255) NOT NULL,
  `USER_FULL_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PASSWORD` varchar(255) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tu_user`
--

LOCK TABLES `tu_user` WRITE;
/*!40000 ALTER TABLE `tu_user` DISABLE KEYS */;
INSERT INTO `tu_user` VALUES (9,'smz','smz','$2a$10$TTBLDslUXNbOvwle4BleF.KNigHhbBrq/5l41L3Su0S13kfhrUhPi');
/*!40000 ALTER TABLE `tu_user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-01-06 11:47:45
