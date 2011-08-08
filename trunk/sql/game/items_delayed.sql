CREATE TABLE IF NOT EXISTS `items_delayed` (
  `payment_id` int NOT NULL auto_increment,
  `owner_id` int NOT NULL,
  `item_id` smallint unsigned NOT NULL,
  `count` int unsigned NOT NULL DEFAULT 1,
  `enchant_level` smallint unsigned NOT NULL DEFAULT 0,
  `attribute` smallint NOT NULL DEFAULT -1,
  `attribute_level` smallint NOT NULL DEFAULT -1,
  `flags` int NOT NULL default 0,
  `payment_status` tinyint unsigned NOT NULL DEFAULT 0,
  `description` varchar(255) default NULL,
  PRIMARY KEY (`payment_id`),
  KEY `key_owner_id` (`owner_id`),
  KEY `key_item_id` (`item_id`)
) TYPE=MyISAM;