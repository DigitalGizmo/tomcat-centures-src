USE artsdb;

DROP TABLE IF EXISTS `craft`;
CREATE TABLE `craft` (
  `ID` mediumint(9) NOT NULL,
  `shortName` varchar(32) NOT NULL,
  `title` varchar(64) default NULL,
  PRIMARY KEY  (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `craft`  VALUES 
('1','Fabrics','Woven Fabrics'),
('2','Pottery','Pottery'),
('3','Furniture','Furniture'),
('4','Needlework','Needlework'),
('5','Ephemera','Ephemera'),
('6','Netting','Netting and Tufted Work'),
('7','Metal','Metal Work'),
('8','Basketry','Basketry'),
('9','Jewelry','Jewelry'),
('10','Photography','Photography');

