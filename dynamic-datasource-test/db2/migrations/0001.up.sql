DROP TABLE IF EXISTS information;

CREATE TABLE `information` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `uid` bigint(20) NOT NULL COMMENT '用户/司机id',
  `ep_id` bigint(20) DEFAULT NULL COMMENT '企业id',
  `user_type` varchar(16) NOT NULL COMMENT '用户类型 USER DRIVER COOPERATE',
  `information` varchar(512) NOT NULL COMMENT '发票信息json',
  `selected_type` varchar(16) DEFAULT 'UNSET' COMMENT '已选择的发票类型 UNSET DUPLICATE TRIPLICATE DONATION',
  `hcountry` int(10) unsigned NOT NULL COMMENT '国家编码',
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `ix_hcountry` (`hcountry`),
  KEY `ix_updated_at` (`updated_at`),
  KEY `ix_created_at` (`created_at`),
  KEY `ix_uid_ep_id_user_type` (`uid`,`ep_id`,`user_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户发票信息'

DELETE FROM information;

INSERT INTO user (id, name, age, email) VALUES
(1, 1, 1, 'USER', 'this is information for 1', 'UNSET', 90000);