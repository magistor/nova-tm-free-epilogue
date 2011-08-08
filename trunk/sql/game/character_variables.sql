DROP TABLE IF EXISTS `character_variables`;
CREATE TABLE `character_variables` (
  `obj_id` int(11) NOT NULL default '0',
  `type` varchar(86) NOT NULL default '0',
  `name` varchar(86) NOT NULL default '0',
  `value` text NOT NULL,
  `expire_time` int(11) NOT NULL default '0',
  UNIQUE KEY `prim` (`obj_id`,`type`,`name`),
  KEY `obj_id` (`obj_id`),
  KEY `type` (`type`),
  KEY `name` (`name`),
  KEY `expire_time` (`expire_time`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;