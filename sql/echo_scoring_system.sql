/*
 Navicat Premium Data Transfer

 Source Server         : 本地数据库
 Source Server Type    : MySQL
 Source Server Version : 50736
 Source Host           : localhost:3306
 Source Schema         : echo_scoring_system

 Target Server Type    : MySQL
 Target Server Version : 50736
 File Encoding         : 65001

 Date: 05/01/2025 11:56:59
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for account
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_name`(`username`) USING BTREE,
  UNIQUE INDEX `unique_email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES (9, '2178740980@qq.com', '星开祈灵', '$2a$10$2P0oP1zgcVQhbjmxElOYj.QiDQjU02yDGNRsQjHM9HQpYYfw.9ova');

-- ----------------------------
-- Table structure for echos
-- ----------------------------
DROP TABLE IF EXISTS `echos`;
CREATE TABLE `echos`  (
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `echo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 176 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of echos
-- ----------------------------
INSERT INTO `echos` VALUES ('星开祈灵', '椿', '{\"cost\":\"4\",\"echo\":[[\"暴击伤害\",\"19.8\",\"47\"],[\"暴击率\",\"6.3\",\"30\"],[\"普攻伤害加成\",\"6.4\",\"13\"],[\"共鸣解放伤害加成\",\"9.4\",\"10\"],[\"固定防御\",\"60\",\"1\"]],\"main\":\"暴击率 22%\",\"score\":\"61\"}', 127);
INSERT INTO `echos` VALUES ('星开祈灵', '椿', '{\"cost\":\"1\",\"echo\":[[\"暴击伤害\",\"21\",\"41\"],[\"暴击率\",\"9.3\",\"37\"],[\"普攻伤害加成\",\"8.6\",\"14\"],[\"共鸣解放伤害加成\",\"7.9\",\"7\"],[\"固定防御\",\"60\",\"1\"]],\"main\":\"百分比攻击 18%\",\"score\":\"73\"}', 131);
INSERT INTO `echos` VALUES ('星开祈灵', '丹瑾', '{\"cost\":\"1\",\"echo\":[[\"暴击率\",\"7.5\",\"35\"],[\"百分比攻击\",\"9.4\",\"22\"],[\"重击伤害加成\",\"9.4\",\"18\"],[\"固定攻击\",\"50\",\"14\"],[\"共鸣技能伤害加成\",\"10.1\",\"11\"]],\"main\":\"百分比攻击 18%\",\"score\":\"61\"}', 132);
INSERT INTO `echos` VALUES ('星开祈灵', '椿', '{\"cost\":\"1\",\"echo\":[[\"暴击伤害\",\"13.8\",\"29\"],[\"暴击率\",\"6.3\",\"26\"],[\"百分比攻击\",\"10.1\",\"22\"],[\"普攻伤害加成\",\"10.1\",\"18\"],[\"重击伤害加成\",\"10.1\",\"6\"]],\"main\":\"百分比攻击 18%\",\"score\":\"69\"}', 133);
INSERT INTO `echos` VALUES ('星开祈灵', '丹瑾', '{\"cost\":\"3\",\"echo\":[[\"暴击率\",\"7.5\",\"37\"],[\"暴击伤害\",\"12.6\",\"31\"],[\"重击伤害加成\",\"8.6\",\"18\"],[\"共鸣技能伤害加成\",\"7.9\",\"9\"],[\"共鸣效率\",\"10.8\",\"5\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"57\"}', 137);
INSERT INTO `echos` VALUES ('星开祈灵', '丹瑾', '{\"cost\":\"3\",\"echo\":[[\"暴击率\",\"8.7\",\"41\"],[\"暴击伤害\",\"12.6\",\"30\"],[\"百分比攻击\",\"10.9\",\"27\"],[\"固定防御\",\"50\",\"1\"],[\"固定生命\",\"430\",\"1\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"59\"}', 138);
INSERT INTO `echos` VALUES ('星开祈灵', '椿', '{\"cost\":\"3\",\"echo\":[[\"暴击率\",\"8.1\",\"39\"],[\"暴击伤害\",\"15\",\"36\"],[\"百分比攻击\",\"9.4\",\"23\"],[\"百分比生命\",\"7.9\",\"2\"],[\"固定生命\",\"430\",\"1\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"60\"}', 139);
INSERT INTO `echos` VALUES ('星开祈灵', '椿', '{\"cost\":\"3\",\"echo\":[[\"暴击率\",\"7.5\",\"38\"],[\"暴击伤害\",\"13.8\",\"35\"],[\"普攻伤害加成\",\"8.6\",\"18\"],[\"共鸣解放伤害加成\",\"7.1\",\"8\"],[\"共鸣技能伤害加成\",\"11.6\",\"0\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"57\"}', 140);
INSERT INTO `echos` VALUES ('星开祈灵', '维里奈', '{\"cost\":\"4\",\"echo\":[[\"共鸣效率\",\"10.8\",\"54\"],[\"固定攻击\",\"50\",\"33\"],[\"百分比生命\",\"7.9\",\"6\"],[\"暴击率\",\"7.5\",\"4\"],[\"共鸣技能伤害加成\",\"9.4\",\"2\"]],\"main\":\"治疗效果加成 26.4%\",\"score\":\"58\"}', 141);
INSERT INTO `echos` VALUES ('星开祈灵', '维里奈', '{\"cost\":\"3\",\"echo\":[[\"共鸣效率\",\"8.4\",\"35\"],[\"百分比攻击\",\"8.6\",\"33\"],[\"固定攻击\",\"50\",\"28\"],[\"重击伤害加成\",\"8.6\",\"2\"],[\"固定生命\",\"390\",\"2\"]],\"main\":\"共鸣效率 32%\",\"score\":\"69\"}', 142);
INSERT INTO `echos` VALUES ('星开祈灵', '维里奈', '{\"cost\":\"3\",\"echo\":[[\"共鸣效率\",\"10.8\",\"52\"],[\"百分比攻击\",\"9.4\",\"41\"],[\"暴击率\",\"9.3\",\"5\"],[\"固定生命\",\"510\",\"3\"],[\"共鸣解放伤害加成\",\"9.4\",\"0\"]],\"main\":\"百分比攻击 30%\",\"score\":\"60\"}', 143);
INSERT INTO `echos` VALUES ('星开祈灵', '维里奈', '{\"cost\":\"1\",\"echo\":[[\"共鸣效率\",\"11.6\",\"44\"],[\"百分比攻击\",\"8.6\",\"30\"],[\"固定攻击\",\"40\",\"20\"],[\"暴击伤害\",\"19.8\",\"4\"],[\"普攻伤害加成\",\"7.9\",\"2\"]],\"main\":\"百分比攻击 18%\",\"score\":\"76\"}', 144);
INSERT INTO `echos` VALUES ('星开祈灵', '维里奈', '{\"cost\":\"1\",\"echo\":[[\"共鸣效率\",\"9.2\",\"40\"],[\"百分比攻击\",\"7.9\",\"31\"],[\"固定攻击\",\"40\",\"23\"],[\"暴击伤害\",\"15\",\"4\"],[\"固定生命\",\"320\",\"2\"]],\"main\":\"百分比攻击 18%\",\"score\":\"66\"}', 145);
INSERT INTO `echos` VALUES ('星开祈灵', '守岸人', '{\"cost\":\"4\",\"echo\":[[\"共鸣效率\",\"10\",\"47\"],[\"暴击伤害\",\"17.4\",\"37\"],[\"固定生命\",\"470\",\"10\"],[\"百分比攻击\",\"7.1\",\"4\"],[\"固定攻击\",\"40\",\"3\"]],\"main\":\"暴击伤害 44%\",\"score\":\"54\"}', 146);
INSERT INTO `echos` VALUES ('星开祈灵', '守岸人', '{\"cost\":\"3\",\"echo\":[[\"共鸣效率\",\"10\",\"40\"],[\"暴击伤害\",\"17.4\",\"32\"],[\"共鸣解放伤害加成\",\"8.6\",\"19\"],[\"固定生命\",\"430\",\"8\"],[\"固定防御\",\"60\",\"1\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"63\"}', 147);
INSERT INTO `echos` VALUES ('星开祈灵', '守岸人', '{\"cost\":\"1\",\"echo\":[[\"共鸣效率\",\"10.8\",\"41\"],[\"暴击伤害\",\"19.8\",\"34\"],[\"百分比生命\",\"7.9\",\"22\"],[\"共鸣技能伤害加成\",\"9.4\",\"2\"],[\"固定防御\",\"60\",\"1\"]],\"main\":\"百分比生命 22.8%\",\"score\":\"67\"}', 148);
INSERT INTO `echos` VALUES ('星开祈灵', '守岸人', '{\"cost\":\"1\",\"echo\":[[\"共鸣效率\",\"11.6\",\"45\"],[\"暴击伤害\",\"18.6\",\"33\"],[\"共鸣解放伤害加成\",\"8.6\",\"18\"],[\"普攻伤害加成\",\"7.9\",\"2\"],[\"固定攻击\",\"40\",\"2\"]],\"main\":\"百分比生命 22.8%\",\"score\":\"65\"}', 149);
INSERT INTO `echos` VALUES ('星开祈灵', '守岸人', '{\"cost\":\"1\",\"echo\":[[\"共鸣效率\",\"11.6\",\"46\"],[\"暴击伤害\",\"13.8\",\"25\"],[\"百分比生命\",\"8.6\",\"25\"],[\"百分比防御\",\"10.9\",\"2\"],[\"重击伤害加成\",\"7.9\",\"2\"]],\"main\":\"百分比生命 22.8%\",\"score\":\"64\"}', 150);
INSERT INTO `echos` VALUES ('星开祈灵', '长离', '{\"cost\":\"4\",\"echo\":[[\"暴击率\",\"8.7\",\"46\"],[\"暴击伤害\",\"12.6\",\"33\"],[\"固定攻击\",\"50\",\"13\"],[\"共鸣效率\",\"10\",\"7\"],[\"固定生命\",\"470\",\"1\"]],\"main\":\"暴击率 22%\",\"score\":\"55\"}', 151);
INSERT INTO `echos` VALUES ('星开祈灵', '安可', '{\"cost\":\"4\",\"echo\":[[\"暴击率\",\"10.5\",\"45\"],[\"暴击伤害\",\"15\",\"32\"],[\"普攻伤害加成\",\"10.1\",\"18\"],[\"共鸣解放伤害加成\",\"8.6\",\"5\"],[\"固定生命\",\"320\",\"1\"]],\"main\":\"暴击伤害 44%\",\"score\":\"67\"}', 152);
INSERT INTO `echos` VALUES ('星开祈灵', '长离', '{\"cost\":\"3\",\"echo\":[[\"暴击率\",\"10.5\",\"40\"],[\"暴击伤害\",\"19.8\",\"38\"],[\"共鸣技能伤害加成\",\"9.4\",\"13\"],[\"固定攻击\",\"40\",\"7\"],[\"百分比防御\",\"10.9\",\"2\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"77\"}', 153);
INSERT INTO `echos` VALUES ('星开祈灵', '长离', '{\"cost\":\"3\",\"echo\":[[\"暴击率\",\"6.3\",\"32\"],[\"暴击伤害\",\"12.6\",\"32\"],[\"共鸣技能伤害加成\",\"8.6\",\"16\"],[\"共鸣解放伤害加成\",\"10.1\",\"12\"],[\"重击伤害加成\",\"9.4\",\"7\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"57\"}', 154);
INSERT INTO `echos` VALUES ('星开祈灵', '安可', '{\"cost\":\"3\",\"echo\":[[\"暴击率\",\"8.7\",\"42\"],[\"暴击伤害\",\"16.2\",\"40\"],[\"共鸣技能伤害加成\",\"10.1\",\"14\"],[\"重击伤害加成\",\"7.1\",\"3\"],[\"固定生命\",\"470\",\"1\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"58\"}', 155);
INSERT INTO `echos` VALUES ('星开祈灵', '安可', '{\"cost\":\"3\",\"echo\":[[\"暴击伤害\",\"17.4\",\"39\"],[\"暴击率\",\"7.5\",\"33\"],[\"普攻伤害加成\",\"10.1\",\"19\"],[\"共鸣解放伤害加成\",\"8.6\",\"5\"],[\"重击伤害加成\",\"9.4\",\"4\"]],\"main\":\"属性伤害加成 30%\",\"score\":\"64\"}', 156);
INSERT INTO `echos` VALUES ('星开祈灵', '长离', '{\"cost\":\"1\",\"echo\":[[\"暴击伤害\",\"13.8\",\"34\"],[\"暴击率\",\"6.3\",\"31\"],[\"百分比攻击\",\"8.6\",\"21\"],[\"共鸣解放伤害加成\",\"7.1\",\"8\"],[\"重击伤害加成\",\"9.4\",\"6\"]],\"main\":\"百分比攻击 18%\",\"score\":\"60\"}', 158);
INSERT INTO `echos` VALUES ('星开祈灵', '安可', '{\"cost\":\"1\",\"echo\":[[\"暴击伤害\",\"13.8\",\"33\"],[\"暴击率\",\"6.3\",\"30\"],[\"普攻伤害加成\",\"10.1\",\"20\"],[\"共鸣技能伤害加成\",\"8.6\",\"11\"],[\"共鸣解放伤害加成\",\"8.6\",\"6\"]],\"main\":\"百分比攻击 18%\",\"score\":\"60\"}', 160);
INSERT INTO `echos` VALUES ('星开祈灵', '长离', '{\"cost\":\"1\",\"echo\":[[\"暴击伤害\",\"21\",\"49\"],[\"暴击率\",\"6.9\",\"32\"],[\"固定攻击\",\"50\",\"11\"],[\"共鸣解放伤害加成\",\"6.4\",\"7\"],[\"固定生命\",\"430\",\"1\"]],\"main\":\"百分比攻击 18%\",\"score\":\"63\"}', 161);
INSERT INTO `echos` VALUES ('星开祈灵', '丹瑾', '{\"cost\":\"1\",\"echo\":[[\"暴击伤害\",\"18.6\",\"43\"],[\"暴击率\",\"6.3\",\"29\"],[\"固定攻击\",\"50\",\"14\"],[\"共鸣技能伤害加成\",\"9.4\",\"10\"],[\"共鸣效率\",\"11.6\",\"5\"]],\"main\":\"百分比攻击 18%\",\"score\":\"62\"}', 166);
INSERT INTO `echos` VALUES ('星开祈灵', '安可', '{\"cost\":\"1\",\"echo\":[[\"暴击伤害\",\"17.4\",\"37\"],[\"暴击率\",\"7.5\",\"32\"],[\"普攻伤害加成\",\"10.1\",\"18\"],[\"固定攻击\",\"30\",\"7\"],[\"共鸣解放伤害加成\",\"11.6\",\"7\"]],\"main\":\"百分比攻击 18%\",\"score\":\"67\"}', 167);
INSERT INTO `echos` VALUES ('星开祈灵', '丹瑾', '{\"cost\":\"4\",\"echo\":[[\"暴击率\",\"9.3\",\"37\"],[\"暴击伤害\",\"12.6\",\"25\"],[\"重击伤害加成\",\"10.1\",\"17\"],[\"百分比攻击\",\"7.1\",\"15\"],[\"普攻伤害加成\",\"10.9\",\"6\"]],\"main\":\"暴击伤害 44%\",\"score\":\"71\"}', 170);
INSERT INTO `echos` VALUES ('星开祈灵', '散华', '{\"main\":\"暴击率 22%\",\"cost\":4,\"echo\":[[\"暴击率\",8.7,39],[\"暴击伤害\",16.2,37],[\"百分比攻击\",8.6,20],[\"普攻伤害加成\",7.9,3],[\"固定防御\",50,1]],\"score\":66}', 171);
INSERT INTO `echos` VALUES ('星开祈灵', '散华', '{\"main\":\"属性伤害加成 30%\",\"cost\":3,\"echo\":[[\"暴击率\",9.9,42],[\"暴击伤害\",13.8,30],[\"百分比攻击\",7.1,16],[\"固定攻击\",40,10],[\"百分比防御\",13.8,2]],\"score\":69}', 172);
INSERT INTO `echos` VALUES ('星开祈灵', '散华', '{\"main\":\"属性伤害加成 30%\",\"cost\":3,\"echo\":[[\"暴击率\",9.3,45],[\"暴击伤害\",17.4,42],[\"固定攻击\",40,11],[\"百分比防御\",9,2],[\"固定生命\",320,1]],\"score\":62}', 173);
INSERT INTO `echos` VALUES ('星开祈灵', '散华', '{\"main\":\"百分比攻击 18%\",\"cost\":1,\"echo\":[[\"暴击率\",9.3,46],[\"暴击伤害\",12.6,31],[\"共鸣解放伤害加成\",11.6,11],[\"重击伤害加成\",8.6,10],[\"百分比防御\",10.9,2]],\"score\":60}', 174);
INSERT INTO `echos` VALUES ('星开祈灵', '散华', '{\"main\":\"百分比攻击 18%\",\"cost\":1,\"echo\":[[\"暴击伤害\",15,45],[\"暴击率\",6.9,41],[\"共鸣解放伤害加成\",8.6,10],[\"百分比防御\",11.8,3],[\"百分比生命\",8.6,2]],\"score\":50}', 175);

-- ----------------------------
-- Table structure for persistent_logins
-- ----------------------------
DROP TABLE IF EXISTS `persistent_logins`;
CREATE TABLE `persistent_logins`  (
  `username` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `series` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `token` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `last_used` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`series`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of persistent_logins
-- ----------------------------
INSERT INTO `persistent_logins` VALUES ('星开祈灵', 'rebIwzwgpJxsqIcSA53F1A==', 'NRQ8B2CdJtsscWl6qx/awQ==', '2025-01-05 11:48:59');

-- ----------------------------
-- Table structure for temp_echos
-- ----------------------------
DROP TABLE IF EXISTS `temp_echos`;
CREATE TABLE `temp_echos`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `echo` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name_list` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of temp_echos
-- ----------------------------
INSERT INTO `temp_echos` VALUES (1, '星开祈灵', '{\"main\":\"暴击率 22%\",\"cost\":4,\"score\":0,\"echo\":[[\"暴击率\",10.5,\"\"],[\"暴击伤害\",21,\"\"],[\"固定防御\",60,\"\"],[\"固定生命\",390,\"\"],[\"百分比攻击\",7.9,\"\"]]}', '[\"椿\",\"丹瑾\",\"守岸人\",\"维里奈\"]');
INSERT INTO `temp_echos` VALUES (2, '星开祈灵', '{\"main\":\"属性伤害加成 30%\",\"cost\":3,\"score\":0,\"echo\":[[\"暴击伤害\",17.4,\"\"],[\"共鸣效率\",10,\"\"],[\"普攻伤害加成\",9.4,\"\"],[\"重击伤害加成\",9.4,\"\"],[\"共鸣解放伤害加成\",10.9,\"\"]]}', '[\"椿\",\"丹瑾\",\"长离\",\"安可\",\"守岸人\"]');

-- ----------------------------
-- Table structure for weapons
-- ----------------------------
DROP TABLE IF EXISTS `weapons`;
CREATE TABLE `weapons`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `weapon` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `a1`(`username`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weapons
-- ----------------------------
INSERT INTO `weapons` VALUES (2, '星开祈灵', '维里奈', '奇幻变奏');
INSERT INTO `weapons` VALUES (4, '星开祈灵', '安可', '漪澜浮录');
INSERT INTO `weapons` VALUES (5, '星开祈灵', '散华', '千古洑流');

-- ----------------------------
-- Table structure for weights
-- ----------------------------
DROP TABLE IF EXISTS `weights`;
CREATE TABLE `weights`  (
  `username` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `weight` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `id` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ab`(`username`, `name`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of weights
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
