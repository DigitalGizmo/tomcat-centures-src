
/* ---------------------------------------------------------------------- */
/* Visitor                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='Visitor_FK_1')
    ALTER TABLE Visitor DROP CONSTRAINT Visitor_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'Visitor')
BEGIN
     DECLARE @reftable_1 nvarchar(60), @constraintname_1 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'Visitor'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_1, @constraintname_1
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_1+' drop constraint '+@constraintname_1)
       FETCH NEXT from refcursor into @reftable_1, @constraintname_1
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE Visitor
END


CREATE TABLE Visitor
(
                    visitorID INT NOT NULL IDENTITY (1,1),
                    visitorName VARCHAR (20) NOT NULL,
                    password VARCHAR (20) NOT NULL,
                    roleID INT NOT NULL,
                    lastName VARCHAR (50) NULL,
                    firstName VARCHAR (50) NULL,

    CONSTRAINT Visitor_PK PRIMARY KEY(visitorID),
    UNIQUE (visitorName));





/* ---------------------------------------------------------------------- */
/* Role                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'Role')
BEGIN
     DECLARE @reftable_2 nvarchar(60), @constraintname_2 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'Role'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_2, @constraintname_2
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_2+' drop constraint '+@constraintname_2)
       FETCH NEXT from refcursor into @reftable_2, @constraintname_2
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE Role
END


CREATE TABLE Role
(
                    roleID INT NOT NULL,
                    label VARCHAR (20) NOT NULL,

    CONSTRAINT Role_PK PRIMARY KEY(roleID));





/* ---------------------------------------------------------------------- */
/* CollectionItem                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='CollectionItem_FK_1')
    ALTER TABLE CollectionItem DROP CONSTRAINT CollectionItem_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'CollectionItem')
BEGIN
     DECLARE @reftable_3 nvarchar(60), @constraintname_3 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'CollectionItem'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_3, @constraintname_3
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_3+' drop constraint '+@constraintname_3)
       FETCH NEXT from refcursor into @reftable_3, @constraintname_3
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE CollectionItem
END


CREATE TABLE CollectionItem
(
                    visitorID INT NOT NULL,
                    itemID INT NOT NULL,
);

CREATE  INDEX IX_CollectionItem ON CollectionItem (visitorID);




/* ---------------------------------------------------------------------- */
/* Activity                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='Activity_FK_1')
    ALTER TABLE Activity DROP CONSTRAINT Activity_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='Activity_FK_2')
    ALTER TABLE Activity DROP CONSTRAINT Activity_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'Activity')
BEGIN
     DECLARE @reftable_4 nvarchar(60), @constraintname_4 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'Activity'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_4, @constraintname_4
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_4+' drop constraint '+@constraintname_4)
       FETCH NEXT from refcursor into @reftable_4, @constraintname_4
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE Activity
END


CREATE TABLE Activity
(
                    activityID INT NOT NULL,
                    statusID INT default 0 NOT NULL,
                    visitorID INT NOT NULL,
                    title VARCHAR (100) NULL,
                    shortDescription VARCHAR (255) NULL,
                    longDescription TEXT NULL,
                    submittedOn DATETIME NULL,

    CONSTRAINT Activity_PK PRIMARY KEY(activityID));





/* ---------------------------------------------------------------------- */
/* ActivityItem                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='ActivityItem_FK_1')
    ALTER TABLE ActivityItem DROP CONSTRAINT ActivityItem_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'ActivityItem')
BEGIN
     DECLARE @reftable_5 nvarchar(60), @constraintname_5 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'ActivityItem'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_5, @constraintname_5
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_5+' drop constraint '+@constraintname_5)
       FETCH NEXT from refcursor into @reftable_5, @constraintname_5
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE ActivityItem
END


CREATE TABLE ActivityItem
(
                    activityID INT NOT NULL,
                    itemID INT NOT NULL,
);

CREATE  INDEX IX_ActivityItem ON ActivityItem (activityID);




/* ---------------------------------------------------------------------- */
/* Status                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'Status')
BEGIN
     DECLARE @reftable_6 nvarchar(60), @constraintname_6 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'Status'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_6, @constraintname_6
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_6+' drop constraint '+@constraintname_6)
       FETCH NEXT from refcursor into @reftable_6, @constraintname_6
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE Status
END


CREATE TABLE Status
(
                    statusID INT NOT NULL,
                    label VARCHAR (50) NOT NULL,

    CONSTRAINT Status_PK PRIMARY KEY(statusID));





/* ---------------------------------------------------------------------- */
/* HistoricalEraLabel                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'HistoricalEraLabel')
BEGIN
     DECLARE @reftable_7 nvarchar(60), @constraintname_7 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'HistoricalEraLabel'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_7, @constraintname_7
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_7+' drop constraint '+@constraintname_7)
       FETCH NEXT from refcursor into @reftable_7, @constraintname_7
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE HistoricalEraLabel
END


CREATE TABLE HistoricalEraLabel
(
                    eraID INT NOT NULL,
                    label VARCHAR (50) NOT NULL,

    CONSTRAINT HistoricalEraLabel_PK PRIMARY KEY(eraID));





/* ---------------------------------------------------------------------- */
/* HistoricalEra                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='HistoricalEra_FK_1')
    ALTER TABLE HistoricalEra DROP CONSTRAINT HistoricalEra_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='HistoricalEra_FK_2')
    ALTER TABLE HistoricalEra DROP CONSTRAINT HistoricalEra_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'HistoricalEra')
BEGIN
     DECLARE @reftable_8 nvarchar(60), @constraintname_8 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'HistoricalEra'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_8, @constraintname_8
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_8+' drop constraint '+@constraintname_8)
       FETCH NEXT from refcursor into @reftable_8, @constraintname_8
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE HistoricalEra
END


CREATE TABLE HistoricalEra
(
                    activityID INT NOT NULL,
                    eraID INT NOT NULL,
);

CREATE  INDEX IX_HistoricalEra ON HistoricalEra (activityID);




/* ---------------------------------------------------------------------- */
/* GradeLevelLabel                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'GradeLevelLabel')
BEGIN
     DECLARE @reftable_9 nvarchar(60), @constraintname_9 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'GradeLevelLabel'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_9, @constraintname_9
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_9+' drop constraint '+@constraintname_9)
       FETCH NEXT from refcursor into @reftable_9, @constraintname_9
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE GradeLevelLabel
END


CREATE TABLE GradeLevelLabel
(
                    levelID INT NOT NULL,
                    label VARCHAR (50) NOT NULL,

    CONSTRAINT GradeLevelLabel_PK PRIMARY KEY(levelID));





/* ---------------------------------------------------------------------- */
/* GradeLevel                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='GradeLevel_FK_1')
    ALTER TABLE GradeLevel DROP CONSTRAINT GradeLevel_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='GradeLevel_FK_2')
    ALTER TABLE GradeLevel DROP CONSTRAINT GradeLevel_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'GradeLevel')
BEGIN
     DECLARE @reftable_10 nvarchar(60), @constraintname_10 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'GradeLevel'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_10, @constraintname_10
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_10+' drop constraint '+@constraintname_10)
       FETCH NEXT from refcursor into @reftable_10, @constraintname_10
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE GradeLevel
END


CREATE TABLE GradeLevel
(
                    activityID INT NOT NULL,
                    levelID INT NOT NULL,
);

CREATE  INDEX IX_GradeLevel ON GradeLevel (activityID);




/* ---------------------------------------------------------------------- */
/* ContentAreaLabel                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'ContentAreaLabel')
BEGIN
     DECLARE @reftable_11 nvarchar(60), @constraintname_11 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'ContentAreaLabel'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_11, @constraintname_11
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_11+' drop constraint '+@constraintname_11)
       FETCH NEXT from refcursor into @reftable_11, @constraintname_11
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE ContentAreaLabel
END


CREATE TABLE ContentAreaLabel
(
                    contentID INT NOT NULL,
                    label VARCHAR (50) NOT NULL,

    CONSTRAINT ContentAreaLabel_PK PRIMARY KEY(contentID));





/* ---------------------------------------------------------------------- */
/* ContentArea                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='ContentArea_FK_1')
    ALTER TABLE ContentArea DROP CONSTRAINT ContentArea_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='ContentArea_FK_2')
    ALTER TABLE ContentArea DROP CONSTRAINT ContentArea_FK_2;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'ContentArea')
BEGIN
     DECLARE @reftable_12 nvarchar(60), @constraintname_12 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'ContentArea'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_12, @constraintname_12
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_12+' drop constraint '+@constraintname_12)
       FETCH NEXT from refcursor into @reftable_12, @constraintname_12
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE ContentArea
END


CREATE TABLE ContentArea
(
                    activityID INT NOT NULL,
                    contentID INT NOT NULL,
);

CREATE  INDEX IX_ContentArea ON ContentArea (activityID);




/* ---------------------------------------------------------------------- */
/* TeachingPlanStep                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='TeachingPlanStep_FK_1')
    ALTER TABLE TeachingPlanStep DROP CONSTRAINT TeachingPlanStep_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'TeachingPlanStep')
BEGIN
     DECLARE @reftable_13 nvarchar(60), @constraintname_13 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'TeachingPlanStep'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_13, @constraintname_13
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_13+' drop constraint '+@constraintname_13)
       FETCH NEXT from refcursor into @reftable_13, @constraintname_13
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE TeachingPlanStep
END


CREATE TABLE TeachingPlanStep
(
                    activityID INT NOT NULL,
                    stepNum INT NOT NULL,
                    description TEXT NULL,
);

CREATE  INDEX IX_TeachingPlanStep ON TeachingPlanStep (activityID);




/* ---------------------------------------------------------------------- */
/* WebLink                                                      */
/* ---------------------------------------------------------------------- */

IF EXISTS (SELECT 1 FROM sysobjects WHERE type ='RI' AND name='WebLink_FK_1')
    ALTER TABLE WebLink DROP CONSTRAINT WebLink_FK_1;
IF EXISTS (SELECT 1 FROM sysobjects WHERE type = 'U' AND name = 'WebLink')
BEGIN
     DECLARE @reftable_14 nvarchar(60), @constraintname_14 nvarchar(60)
     DECLARE refcursor CURSOR FOR
     select reftables.name tablename, cons.name constraintname
      from sysobjects tables,
           sysobjects reftables,
           sysobjects cons,
           sysreferences ref
       where tables.id = ref.rkeyid
         and cons.id = ref.constid
         and reftables.id = ref.fkeyid
         and tables.name = 'WebLink'
     OPEN refcursor
     FETCH NEXT from refcursor into @reftable_14, @constraintname_14
     while @@FETCH_STATUS = 0
     BEGIN
       exec ('alter table '+@reftable_14+' drop constraint '+@constraintname_14)
       FETCH NEXT from refcursor into @reftable_14, @constraintname_14
     END
     CLOSE refcursor
     DEALLOCATE refcursor
     DROP TABLE WebLink
END


CREATE TABLE WebLink
(
                    activityID INT NOT NULL,
                    linkNum INT NOT NULL,
                    title VARCHAR (150) NULL,
                    url VARCHAR (180) NULL,
);

CREATE  INDEX IX_WebLink ON WebLink (activityID);




/* ---------------------------------------------------------------------- */
/* WebLink                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE Visitor
    ADD CONSTRAINT Visitor_FK_1 FOREIGN KEY (roleID)
    REFERENCES Role (roleID)
END    
;




/* ---------------------------------------------------------------------- */
/* Visitor                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* Role                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE CollectionItem
    ADD CONSTRAINT CollectionItem_FK_1 FOREIGN KEY (visitorID)
    REFERENCES Visitor (visitorID)
END    
;




/* ---------------------------------------------------------------------- */
/* CollectionItem                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE Activity
    ADD CONSTRAINT Activity_FK_1 FOREIGN KEY (statusID)
    REFERENCES Status (statusID)
END    
;

BEGIN
ALTER TABLE Activity
    ADD CONSTRAINT Activity_FK_2 FOREIGN KEY (visitorID)
    REFERENCES Visitor (visitorID)
END    
;




/* ---------------------------------------------------------------------- */
/* Activity                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE ActivityItem
    ADD CONSTRAINT ActivityItem_FK_1 FOREIGN KEY (activityID)
    REFERENCES Activity (activityID)
END    
;




/* ---------------------------------------------------------------------- */
/* ActivityItem                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* Status                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* HistoricalEraLabel                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE HistoricalEra
    ADD CONSTRAINT HistoricalEra_FK_1 FOREIGN KEY (activityID)
    REFERENCES Activity (activityID)
END    
;

BEGIN
ALTER TABLE HistoricalEra
    ADD CONSTRAINT HistoricalEra_FK_2 FOREIGN KEY (eraID)
    REFERENCES HistoricalEraLabel (eraID)
END    
;




/* ---------------------------------------------------------------------- */
/* HistoricalEra                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* GradeLevelLabel                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE GradeLevel
    ADD CONSTRAINT GradeLevel_FK_1 FOREIGN KEY (activityID)
    REFERENCES Activity (activityID)
END    
;

BEGIN
ALTER TABLE GradeLevel
    ADD CONSTRAINT GradeLevel_FK_2 FOREIGN KEY (levelID)
    REFERENCES GradeLevelLabel (levelID)
END    
;




/* ---------------------------------------------------------------------- */
/* GradeLevel                                                      */
/* ---------------------------------------------------------------------- */




/* ---------------------------------------------------------------------- */
/* ContentAreaLabel                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE ContentArea
    ADD CONSTRAINT ContentArea_FK_1 FOREIGN KEY (activityID)
    REFERENCES Activity (activityID)
END    
;

BEGIN
ALTER TABLE ContentArea
    ADD CONSTRAINT ContentArea_FK_2 FOREIGN KEY (contentID)
    REFERENCES ContentAreaLabel (contentID)
END    
;




/* ---------------------------------------------------------------------- */
/* ContentArea                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE TeachingPlanStep
    ADD CONSTRAINT TeachingPlanStep_FK_1 FOREIGN KEY (activityID)
    REFERENCES Activity (activityID)
END    
;




/* ---------------------------------------------------------------------- */
/* TeachingPlanStep                                                      */
/* ---------------------------------------------------------------------- */

BEGIN
ALTER TABLE WebLink
    ADD CONSTRAINT WebLink_FK_1 FOREIGN KEY (activityID)
    REFERENCES Activity (activityID)
END    
;



