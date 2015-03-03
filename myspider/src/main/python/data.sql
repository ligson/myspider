SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for winning_number
-- ----------------------------
DROP TABLE IF EXISTS `winning_number`;
CREATE TABLE `winning_number` (
  `id` varchar(128) NOT NULL,
  `create_date` varchar(16) NOT NULL COMMENT '开奖日期',
  `serial_num` varchar(16) NOT NULL COMMENT '编号',
  `num1` int(2) NOT NULL,
  `num2` int(2) NOT NULL,
  `num3` int(2) NOT NULL,
  `num4` int(2) NOT NULL,
  `num5` int(2) NOT NULL,
  `num6` int(2) NOT NULL,
  `num7` int(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serial_num` (`serial_num`),
  UNIQUE KEY `create_date` (`create_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;