/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50712
Source Host           : localhost:3306
Source Database       : example

Target Server Type    : MYSQL
Target Server Version : 50712
File Encoding         : 65001

Date: 2016-07-12 19:50:05
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `t_blog`
-- ----------------------------
DROP TABLE IF EXISTS `t_blog`;
CREATE TABLE `t_blog` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) NOT NULL,
  `content` mediumtext NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_blog
-- ----------------------------
INSERT INTO `t_blog` VALUES ('1', 'JFinal Demo Title here', 'JFinal Demo Content here');
INSERT INTO `t_blog` VALUES ('2', 'test 1', 'test 1');
INSERT INTO `t_blog` VALUES ('3', 'test 2', 'test 2');
INSERT INTO `t_blog` VALUES ('4', 'test 3', 'test 3');
INSERT INTO `t_blog` VALUES ('5', 'test 4', 'test 4');

-- ----------------------------
-- Table structure for `t_options`
-- ----------------------------
DROP TABLE IF EXISTS `t_options`;
CREATE TABLE `t_options` (
  `optionId` int(11) NOT NULL,
  `key` varchar(64) NOT NULL DEFAULT '' COMMENT '键',
  `value` varchar(128) DEFAULT '' COMMENT '值',
  PRIMARY KEY (`optionId`),
  UNIQUE KEY `unique_option_key` (`key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_options
-- ----------------------------

-- ----------------------------
-- Table structure for `t_user`
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user` (
  `userId` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(32) NOT NULL COMMENT '用户名/11111',
  `email` varchar(64) DEFAULT NULL COMMENT 'email',
  `phone` varchar(11) DEFAULT NULL COMMENT '手机号',
  `pwd` varchar(32) NOT NULL COMMENT '密码',
  `realName` varchar(32) DEFAULT NULL COMMENT '真实姓名',
  `type` int(2) DEFAULT '2' COMMENT '类型//select/1,管理员,2,普通用户,3,前台用户',
  `address` varchar(64) DEFAULT NULL COMMENT '地址',
  `gravatar` varchar(200) DEFAULT NULL COMMENT '头像地址',
  `score` int(11) NOT NULL COMMENT '积分',
  `signature` varchar(1000) DEFAULT NULL COMMENT '个性签名',
  `url` varchar(255) DEFAULT NULL COMMENT '个人主页',
  `remark` varchar(1000) DEFAULT NULL COMMENT '说明',
  `theme` varchar(64) DEFAULT 'default' COMMENT '主题',
  `enable` tinyint(1) DEFAULT '10' COMMENT '是否启用',
  `createTime` datetime DEFAULT NULL COMMENT '创建时间',
  `loginTime` datetime DEFAULT NULL COMMENT '最后登录时间',
  PRIMARY KEY (`userId`),
  UNIQUE KEY `unique_user_name` (`name`) USING BTREE,
  KEY `index_user_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';

-- ----------------------------
-- Records of t_user
-- ----------------------------
