create table catissue_bulk_operation (
	IDENTIFIER BIGINT(20) not null auto_increment,
	OPERATION VARCHAR(100) not null,
	CSV_TEMPLATE blob,
	XML_TEMPALTE blob,
	DROPDOWN_NAME VARCHAR(100) not null,
	PRIMARY KEY  (`IDENTIFIER`), UNIQUE KEY OPERATION (`OPERATION`), UNIQUE KEY DROPDOWN_NAME (`DROPDOWN_NAME`)
);
CREATE TABLE JOB_DETAILS (
	IDENTIFIER bigint(20) NOT NULL auto_increment,
	JOB_NAME varchar(255) NOT NULL,
	JOB_STATUS varchar(50) default NULL,
	TOTAL_RECORDS_COUNT bigint(20) default NULL,
	FAILED_RECORDS_COUNT bigint(20) default NULL,
	TIME_TAKEN bigint(20) default NULL,
	LOG_FILE blob,
	JOB_STARTED_BY bigint(20) default NULL,
	START_TIME datetime default NULL,
	CURRENT_RECORDS_PROCESSED bigint(20) default NULL,
	LOG_FILE_NAME varchar(50) default NULL,
	PRIMARY KEY (`IDENTIFIER`)
);