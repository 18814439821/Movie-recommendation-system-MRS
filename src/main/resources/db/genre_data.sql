/*
 Navicat Premium Data Transfer

 Source Server         : admit
 Source Server Type    : MySQL
 Source Server Version : 80300
 Source Host           : localhost:3306
 Source Schema         : mrs

 Target Server Type    : MySQL
 Target Server Version : 80300
 File Encoding         : 65001

 Date: 19/09/2024 12:27:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for genre
-- ----------------------------
DROP TABLE IF EXISTS `genre`;
CREATE TABLE `genre`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '主键',
  `genre_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类别名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of genre
-- ----------------------------
INSERT INTO `genre` VALUES (251, '剧情');
INSERT INTO `genre` VALUES (252, '犯罪');
INSERT INTO `genre` VALUES (253, '爱情');
INSERT INTO `genre` VALUES (254, '同性');
INSERT INTO `genre` VALUES (255, '灾难');
INSERT INTO `genre` VALUES (256, '动画');
INSERT INTO `genre` VALUES (257, '奇幻');
INSERT INTO `genre` VALUES (258, '动作');
INSERT INTO `genre` VALUES (259, '喜剧');
INSERT INTO `genre` VALUES (260, '战争');
INSERT INTO `genre` VALUES (261, '科幻');
INSERT INTO `genre` VALUES (262, '冒险');
INSERT INTO `genre` VALUES (263, '悬疑');
INSERT INTO `genre` VALUES (264, '历史');
INSERT INTO `genre` VALUES (265, '音乐');
INSERT INTO `genre` VALUES (266, '歌舞');
INSERT INTO `genre` VALUES (267, '惊悚');
INSERT INTO `genre` VALUES (268, '古装');
INSERT INTO `genre` VALUES (269, '家庭');
INSERT INTO `genre` VALUES (270, '传记');
INSERT INTO `genre` VALUES (271, '西部');
INSERT INTO `genre` VALUES (272, '运动');
INSERT INTO `genre` VALUES (273, '情色');
INSERT INTO `genre` VALUES (274, '儿童');
INSERT INTO `genre` VALUES (275, '纪录片');
INSERT INTO `genre` VALUES (276, '武侠');
INSERT INTO `genre` VALUES (277, '恐怖');

SET FOREIGN_KEY_CHECKS = 1;
