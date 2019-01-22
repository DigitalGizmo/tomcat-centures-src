USE artsdb;

ALTER TABLE person CHANGE COLUMN firstName firstNameX varchar(32) default NULL;
ALTER TABLE person CHANGE COLUMN lastName firstName varchar(32) default NULL;
ALTER TABLE person CHANGE COLUMN firstNameX lastName varchar(32) default NULL;
