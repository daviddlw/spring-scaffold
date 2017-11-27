use spider_man;
CREATE TABLE member_info
(
	id INT NOT NULL auto_increment,
  member_no CHAR(32) NOT NULL DEFAULT '' COMMENT '会员号',
  username CHAR(32) NOT NULL DEFAULT '' COMMENT '用户名',
  realname CHAR(32) NOT NULL DEFAULT '' COMMENT '真实姓名',
  age INT NOT NULL DEFAULT 0 COMMENT '年龄',
  mobile CHAR(32) NOT NULL DEFAULT '' COMMENT '注册手机',
  email CHAR(32) NOT NULL DEFAULT '' COMMENT '注册邮箱',
  create_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT '创建时间',
  modify_time TIMESTAMP NOT NULL DEFAULT NOW() COMMENT '更新时间',
  `status` INT(2) NOT NULL DEFAULT 0 COMMENT '记录状态1-有效,0-无效',
  PRIMARY KEY (id),
  UNIQUE KEY uk_member_no (member_no) USING BTREE,
  KEY idx_mobile(mobile) USING BTREE
) ENGINE=INNODB COMMENT '用户表'