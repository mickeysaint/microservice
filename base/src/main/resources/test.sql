/*
 Navicat Premium Data Transfer

 Source Server         : local
 Source Server Type    : MySQL
 Source Server Version : 50642
 Source Host           : localhost:3306
 Source Schema         : test

 Target Server Type    : MySQL
 Target Server Version : 50642
 File Encoding         : 65001

 Date: 13/05/2020 08:14:28
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tb_dict_entry
-- ----------------------------
DROP TABLE IF EXISTS `tb_dict_entry`;
CREATE TABLE `tb_dict_entry`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ENTRY_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `ENTRY_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_dict_entry
-- ----------------------------
INSERT INTO `tb_dict_entry` VALUES (1, 'test2', 'test23', '测试23');

-- ----------------------------
-- Table structure for tb_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `tb_dict_type`;
CREATE TABLE `tb_dict_type`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `TYPE_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `TYPE_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_dict_type
-- ----------------------------
INSERT INTO `tb_dict_type` VALUES (2, 'test2', '测试2');
INSERT INTO `tb_dict_type` VALUES (4, 'test3', '测试3');

-- ----------------------------
-- Table structure for tb_sys_config
-- ----------------------------
DROP TABLE IF EXISTS `tb_sys_config`;
CREATE TABLE `tb_sys_config`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `CONFIG_KEY` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '配置项的KEY',
  `CONFIG_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '配置项的名称',
  `CONFIG_VALUE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '配置项的值',
  `REMARK` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tb_sys_config
-- ----------------------------
INSERT INTO `tb_sys_config` VALUES (2, 'testA', 'testA1', 'testA1', 'testA1');

-- ----------------------------
-- Table structure for tu_menu
-- ----------------------------
DROP TABLE IF EXISTS `tu_menu`;
CREATE TABLE `tu_menu`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `PARENT_ID` int(11) NULL DEFAULT NULL,
  `MENU_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `MENU_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `MENU_SHOW_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `MENU_COMPONENT` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '因为前后分离，不认url，只认component',
  `ID_FULL` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'JSONArray',
  `MENU_NAME_FULL` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用/隔开',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tu_menu
-- ----------------------------
INSERT INTO `tu_menu` VALUES (0, NULL, NULL, 'root', 'root', NULL, '[0]', 'root');
INSERT INTO `tu_menu` VALUES (1, 0, '01', '系统管理', '系统管理', '', '[0,1]', 'root/系统管理');
INSERT INTO `tu_menu` VALUES (2, 1, '0101', '组织结构', '组织结构', 'org', '[0,1,2]', 'root/系统管理/组织结构');
INSERT INTO `tu_menu` VALUES (3, 1, '0102', '菜单管理', '菜单管理', 'menua', '[0,1,3]', 'root/系统管理/菜单管理');
INSERT INTO `tu_menu` VALUES (4, 1, '0103', '角色管理', '角色管理', 'role', '[0,1,4]', 'root/系统管理/角色管理');
INSERT INTO `tu_menu` VALUES (5, 1, '0104', '用户管理', '用户管理', 'user', '[0,1,5]', 'root/系统管理/用户管理');
INSERT INTO `tu_menu` VALUES (7, 1, '0105', '系统参数', '系统参数', 'sysconfig', '[0,1,7]', 'root/系统管理/系统参数');
INSERT INTO `tu_menu` VALUES (8, 1, '0106', '字典类型管理', '字典类型管理', 'dictType', '[0,1,8]', 'root/系统管理/字典类型管理');
INSERT INTO `tu_menu` VALUES (9, 1, '0107', '字典项管理', '字典项管理', 'dictEntry', '[0,1,9]', 'root/系统管理/字典项管理');

-- ----------------------------
-- Table structure for tu_org
-- ----------------------------
DROP TABLE IF EXISTS `tu_org`;
CREATE TABLE `tu_org`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '组织代码',
  `ORG_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '组织名称',
  `PARENT_ID` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '上级组织ID，顶级组织的该字段设置为null',
  `ID_FULL` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT 'JSONArray',
  `ORG_NAME_FULL` varchar(2000) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '用/隔开',
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tu_org
-- ----------------------------
INSERT INTO `tu_org` VALUES (0, NULL, 'root', NULL, '[0]', 'root');
INSERT INTO `tu_org` VALUES (1, '01', '大无限股份公司', '0', '[0,1]', 'root/大无限股份公司');
INSERT INTO `tu_org` VALUES (3, '02', '超牛股份有限公司', '0', '[0,3]', 'root/超牛股份有限公司');
INSERT INTO `tu_org` VALUES (4, '03', '超人股份有限公司', '0', '[0,4]', 'root/超人股份有限公司');
INSERT INTO `tu_org` VALUES (5, '04', '机器人有限公司', '0', '[0,5]', 'root/机器人有限公司');
INSERT INTO `tu_org` VALUES (6, '05', '宇宙能源有限公司', '0', '[0,6]', 'root/宇宙能源有限公司');
INSERT INTO `tu_org` VALUES (7, '06', 'x company', '0', '[0,7]', 'root/x company');
INSERT INTO `tu_org` VALUES (8, '07', '测试', '0', '[0,8]', 'root/测试');
INSERT INTO `tu_org` VALUES (12, '0101', '大无限中国公司', '1', '[0,1,12]', 'root/大无限股份公司/大无限中国公司');
INSERT INTO `tu_org` VALUES (13, '0102', '大无限美国公司', '1', '[0,1,13]', 'root/大无限股份公司/大无限美国公司');
INSERT INTO `tu_org` VALUES (14, '0103', '大无限俄罗斯公司', '1', '[0,1,14]', 'root/大无限股份公司/大无限俄罗斯公司');
INSERT INTO `tu_org` VALUES (17, '08', '测试2', '0', '[0,17]', 'root/测试2');

-- ----------------------------
-- Table structure for tu_org_menu
-- ----------------------------
DROP TABLE IF EXISTS `tu_org_menu`;
CREATE TABLE `tu_org_menu`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for tu_org_user
-- ----------------------------
DROP TABLE IF EXISTS `tu_org_user`;
CREATE TABLE `tu_org_user`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tu_org_user
-- ----------------------------
INSERT INTO `tu_org_user` VALUES (1, 1, 9);
INSERT INTO `tu_org_user` VALUES (4, 1, 11);

-- ----------------------------
-- Table structure for tu_role
-- ----------------------------
DROP TABLE IF EXISTS `tu_role`;
CREATE TABLE `tu_role`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ORG_ID` int(11) NULL DEFAULT NULL,
  `ROLE_CODE` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `ROLE_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tu_role
-- ----------------------------
INSERT INTO `tu_role` VALUES (1, 1, 'root', 'root');
INSERT INTO `tu_role` VALUES (3, 13, 'test2', 'test2');
INSERT INTO `tu_role` VALUES (4, 14, 'test3', 'test3');

-- ----------------------------
-- Table structure for tu_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `tu_role_menu`;
CREATE TABLE `tu_role_menu`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` int(11) NOT NULL,
  `MENU_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 37 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tu_role_menu
-- ----------------------------
INSERT INTO `tu_role_menu` VALUES (9, 3, 2);
INSERT INTO `tu_role_menu` VALUES (10, 4, 2);
INSERT INTO `tu_role_menu` VALUES (11, 4, 3);
INSERT INTO `tu_role_menu` VALUES (12, 4, 4);
INSERT INTO `tu_role_menu` VALUES (13, 4, 5);
INSERT INTO `tu_role_menu` VALUES (28, 1, 0);
INSERT INTO `tu_role_menu` VALUES (29, 1, 1);
INSERT INTO `tu_role_menu` VALUES (30, 1, 2);
INSERT INTO `tu_role_menu` VALUES (31, 1, 3);
INSERT INTO `tu_role_menu` VALUES (32, 1, 4);
INSERT INTO `tu_role_menu` VALUES (33, 1, 5);
INSERT INTO `tu_role_menu` VALUES (34, 1, 7);
INSERT INTO `tu_role_menu` VALUES (35, 1, 8);
INSERT INTO `tu_role_menu` VALUES (36, 1, 9);

-- ----------------------------
-- Table structure for tu_role_user
-- ----------------------------
DROP TABLE IF EXISTS `tu_role_user`;
CREATE TABLE `tu_role_user`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `ROLE_ID` int(11) NOT NULL,
  `USER_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tu_role_user
-- ----------------------------
INSERT INTO `tu_role_user` VALUES (1, 1, 9);
INSERT INTO `tu_role_user` VALUES (4, 1, 11);

-- ----------------------------
-- Table structure for tu_user
-- ----------------------------
DROP TABLE IF EXISTS `tu_user`;
CREATE TABLE `tu_user`  (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  `USER_FULL_NAME` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
  `PASSWORD` varchar(255) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL,
  PRIMARY KEY (`ID`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 12 CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;

-- ----------------------------
-- Records of tu_user
-- ----------------------------
INSERT INTO `tu_user` VALUES (9, 'smz', 'smz', '$2a$10$TTBLDslUXNbOvwle4BleF.KNigHhbBrq/5l41L3Su0S13kfhrUhPi');
INSERT INTO `tu_user` VALUES (11, 'smz2', 'smz211', '$2a$10$LDQ5qifGayZcBxUznOvdMOlqyc9ijzDTQDA7Yn5g1VkrNxOg0PLBG');

SET FOREIGN_KEY_CHECKS = 1;
