-- ----------------------------
-- Table structure for prime_shop
-- ----------------------------
DROP TABLE IF EXISTS `prime_shop`;
CREATE TABLE `prime_shop` (
  `ord` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `itemId` int(11) NOT NULL,
  `count` int(11) NOT NULL DEFAULT '1',
  `price` int(11) NOT NULL DEFAULT '0',
  `iCategory2` int(1) NOT NULL DEFAULT '0',
  `onSale` int(1) NOT NULL DEFAULT '1',
  `iStartSale` int(9) NOT NULL DEFAULT '0',
  `iEndSale` int(9) NOT NULL DEFAULT '0',
  `iStartHour` int(2) NOT NULL DEFAULT '0',
  `iStartMin` int(2) NOT NULL DEFAULT '0',
  `iEndHour` int(2) NOT NULL DEFAULT '0',
  `iEndMin` int(2) NOT NULL DEFAULT '0',
  `iStock` int(11) NOT NULL DEFAULT '0',
  `iMaxStock` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`ord`,`itemId`)
) ENGINE=InnoDB AUTO_INCREMENT=191 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- ----------------------------
-- Records 
-- ----------------------------
INSERT INTO `prime_shop` VALUES ('1', 'Small fortuna box', '22000', '1', '135', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('2', 'Middle fortuna box', '22001', '1', '270', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('3', 'Large fortuna box', '22002', '1', '405', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('4', 'small fortuna cube', '22003', '1', '81', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('5', 'Middle fortuna cube', '22004', '1', '216', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('6', 'Large fortuna cube', '22005', '1', '324', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('7', 'Powerful Healing Potion', '22025', '1', '3', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('8', 'Rune of Feather', '22066', '1', '68', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('9', 'High-grade Healing Potion', '22026', '1', '1', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('10', 'secret medicine of Will - D grade', '22027', '1', '4', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('11', 'secret medicine of Will - C grade', '22028', '1', '13', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('12', 'secret medicine of Will - B grade', '22029', '1', '22', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('13', 'secret medicine of Will - A grade', '22030', '1', '34', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('14', 'secret medicine of Will - S grade', '22031', '1', '49', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('15', 'secret medicine of Life - D grade', '22032', '1', '10', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('16', 'secret medicine of Life - C grade', '22033', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('17', 'secret medicine of Life - B grade', '22034', '1', '54', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('18', 'secret medicine of Life - A grade', '22035', '1', '85', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('19', 'secret medicine of Life - S grade', '22036', '1', '122', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('20', 'Potion of Will', '22037', '1', '4', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('21', 'Wind Walk Scroll', '22039', '1', '4', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('22', 'Haste Scroll', '22040', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('23', 'Might Scroll', '22041', '1', '4', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('24', 'Shield Scroll', '22042', '1', '4', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('25', 'Death Whisper Scroll', '22043', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('26', 'Guidance Scroll', '22044', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('27', 'Empower Scroll', '22045', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('28', 'Grater Acumen Scroll', '22046', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('29', 'Vampiric Rage Scroll', '22047', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('30', 'Bless the Body Scroll', '22048', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('31', 'Bless the Soul Scroll', '22051', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('32', 'Berserker Spirit Scroll', '22049', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('33', 'Magic Barrier Scroll', '22050', '1', '4', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('34', 'Clarity Scroll', '22052', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('35', 'Wild Magic Scroll', '22053', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('36', 'A Scroll Bundle of Fighter', '22087', '1', '52', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('37', 'A Scroll Bundle of Mage', '22088', '1', '52', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('38', 'Blessed Spiritshot Pack - D grade', '22094', '1', '31', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('39', 'Blessed Spiritshot Pack - C grade', '22095', '1', '61', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('40', 'Blessed Spiritshot Pack - B grade', '22096', '1', '166', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('41', 'Blessed Spiritshot Pack - A grade', '22097', '1', '196', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('42', 'Blessed Spiritshot Pack - S grade', '22098', '1', '237', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('43', 'spiritshot Pack - D grade', '22099', '1', '8', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('44', 'spiritshot Pack - C grade', '22100', '1', '10', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('45', 'spiritshot Pack -  B grade', '22101', '1', '34', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('46', 'spiritshot Pack - A grade', '22102', '1', '54', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('47', 'spiritshot Pack - S grade', '22103', '1', '68', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('48', 'soulshot Pack - D grade', '22104', '1', '12', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('49', 'soulshot Pack - C grade', '22105', '1', '24', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('50', 'soulshot Pack -  B grade', '22106', '1', '68', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('51', 'soulshot Pack - A grade', '22107', '1', '81', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('52', 'soulshot Pack - S grade', '22108', '1', '102', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('53', 'Blessed Spiritshot Large Pack - D grade', '22109', '1', '61', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('54', 'Blessed Spiritshot Large Pack - C grade', '22110', '1', '122', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('55', 'Blessed Spiritshot Large Pack - B grade', '22111', '1', '331', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('56', 'Blessed Spiritshot Large Pack - A grade', '22112', '1', '392', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('57', 'Blessed Spiritshot Large Pack - S grade', '22113', '1', '473', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('58', 'spiritshot Large Pack - D grade', '22114', '1', '14', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('59', 'spiritshot Large Pack - C grade', '22115', '1', '21', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('60', 'spiritshot Large Pack -  B grade', '22116', '1', '68', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('61', 'spiritshot Large Pack - A grade', '22117', '1', '108', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('62', 'spiritshot Large Pack - S grade', '22118', '1', '135', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('63', 'soulshot Large Pack - D grade', '22119', '1', '24', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('64', 'soulshot Large Pack - C grade', '22120', '1', '48', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('65', 'soulshot Large Pack -  B grade', '22121', '1', '135', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('66', 'soulshot Large Pack - A grade', '22122', '1', '162', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('67', 'soulshot Large Pack - S grade', '22123', '1', '203', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('85', 'Bolt Container of Light', '22153', '1', '68', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('86', 'Mithril Bolt Container', '22152', '1', '54', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('87', 'silver Bolt Container', '22151', '1', '48', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('88', 'steel Bolt Container', '22150', '1', '34', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('89', 'Bone Bolt Container', '22149', '1', '21', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('90', 'Quiver of Light', '22093', '1', '68', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('91', 'Mithril Quiver', '22092', '1', '54', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('92', 'silver Quiver', '22091', '1', '48', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('93', 'steel Quiver', '22090', '1', '34', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('94', 'Bone Quiver', '22089', '1', '21', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('95', 'Rune of Exp. Points 30% - 5 hours', '20335', '1', '33', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('96', 'Rune of Exp. Points 50% - 5 hours', '20336', '1', '54', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('97', 'Rune of Exp. Points 30% - 10 hours', '20337', '1', '52', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('98', 'Rune of Exp. Points 50% - 10 hours', '20338', '1', '87', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('99', 'Rune of Exp. Points 30% - 7 days', '20339', '1', '697', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('100', 'Rune of Exp. Points 50% - 7 days', '20340', '1', '1161', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('101', 'Rune of SP 30% - 5 hours', '20341', '1', '17', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('102', 'Rune of SP 50% - 5 hours', '20342', '1', '27', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('103', 'Rune of SP 30% - 10 hours', '20343', '1', '26', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('104', 'Rune of SP 50% - 10 hours', '20344', '1', '44', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('105', 'Rune of SP 30% - 7 days', '20345', '1', '349', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('106', 'Rune of SP 50% - 7 days', '20346', '1', '581', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('107', 'Rune of Crystal level 3 - 5 hours', '20347', '1', '33', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('108', 'Rune of Crystal level 5 - 5 hours', '20348', '1', '54', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('109', 'Rune of Crystal level 3 - 10 hours', '20349', '1', '52', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('110', 'Rune of Crystal level 5 - 10 hours', '20350', '1', '87', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('111', 'Rune of Crystal level 3 - 7 days', '20351', '1', '697', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('112', 'Rune of Crystal level 5 - 7 days', '20352', '1', '1161', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('113', 'Weapon-type Enhance Backup Stone (D-Grade) ', '12362', '1', '21', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('114', 'Weapon-type Enhance Backup Stone (C-Grade) ', '12363', '1', '45', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('115', 'Weapon-type Enhance Backup Stone (B-Grade) ', '12364', '1', '203', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('116', 'Weapon-type Enhance Backup Stone (A-Grade) ', '12365', '1', '729', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('117', 'Weapon-type Enhance Backup Stone (S-Grade) ', '12366', '1', '2025', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('118', 'Armor-type Enhance Backup Stone (D-Grade) ', '12367', '1', '4', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('119', 'Armor-type Enhance Backup Stone (C-Grade) ', '12368', '1', '7', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('120', 'Armor-type Enhance Backup Stone (B-Grade) ', '12369', '1', '29', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('121', 'Armor-type Enhance Backup Stone (A-Grade) ', '12370', '1', '104', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('122', 'Armor-type Enhance Backup Stone (S-Grade) ', '12371', '1', '290', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('123', 'Beast Soulshot Pack', '20326', '1', '14', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('124', 'Beast Spiritshot Pack', '20327', '1', '11', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('125', 'Blessed Beast Spiritshot Pack', '20328', '1', '68', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('126', 'Beast Soulshot Large Pack', '20329', '1', '27', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('127', 'Beast Spiritshot Large Pack', '20330', '1', '22', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('128', 'Blessed Beast Spiritshot Large Pack', '20331', '1', '135', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('129', 'Omen Beast Transformation Scroll', '20364', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('130', 'Death Blader Transformation Scroll', '20365', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('131', 'Grail Apostle Transformation Scroll', '20366', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('132', 'Unicorn Transformation Scroll', '20367', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('133', 'Lilim Knight Transformation Scroll', '20368', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('134', 'Golem Guardian Transformation Scroll', '20369', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('135', 'Inferno Drake Transformation Scroll', '20370', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('136', 'Dragon Bomber Transformation Scroll', '20371', '1', '30', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('168', 'High Grade Potion of Will', '20353', '1', '14', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('169', 'Potion of Energy Maintenance', '20391', '1', '142', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('170', 'Potion of Energy Replenishing', '20392', '1', '68', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('171', 'Sweet Fruit Cocktail', '20393', '1', '79', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
INSERT INTO `prime_shop` VALUES ('172', 'Fresh Fruit Cocktail', '20394', '1', '91', '0', '1', '0', '0', '0', '0', '0', '0', '0', '0');
