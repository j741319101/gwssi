alter table SYS_LOG_ERR add column request_param_  varchar(6000);

CREATE TABLE `sys_connect_record` (
  `id_` VARCHAR(64) NOT NULL,
  `type_` VARCHAR(64) NULL,
  `source_id_` VARCHAR(64) NOT NULL,
  `target_id_` VARCHAR(64) NOT NULL,
  `notice` VARCHAR(8188) NULL
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table bus_permission add column def_id_  varchar(64);

CREATE TABLE org_post (
	ID_ VARCHAR(192 ) NOT NULL,
	NAME_ VARCHAR(192 ) NULL,
	CODE_ VARCHAR(192 ) NULL,
	IS_CIVIL_SERVANT_ VARCHAR(192 ) NULL,
	DESC_ VARCHAR(1500 ) NULL,
	LEVEL_ VARCHAR(192 ) NULL,
	CREATE_TIME_ TIMESTAMP(6) NULL,
	CREATE_BY_ VARCHAR(192 ) NULL,
	UPDATE_TIME_ TIMESTAMP(6) NULL,
	UPDATE_BY_ VARCHAR(192 ) NULL,
	TYPE_ VARCHAR(192 ) NULL,
	ORG_ID_ VARCHAR(192 ) NULL,
	CONSTRAINT org_post_PKEY PRIMARY KEY (ID_)
);

alter table bpm_task_identitylink add column check_status_  varchar(2) default '1';
alter table bpm_task_identitylink add column check_time_  TIMESTAMP(6);
alter table bpm_task_identitylink add column task_type_  varchar(64);
alter table bpm_task_identitylink add column org_id_  varchar(64);

alter table bpm_instance add column is_test_data_  smallint(1) default 0;

alter table bpm_task_opinion add column sign_id_  varchar(64);
alter table bpm_task_opinion add column version_  varchar(20);
alter table bpm_task_opinion add column trace_  varchar(64);
alter table bpm_task_opinion add column act_execution_id_  varchar(64);
alter table bpm_task_opinion add column TASK_ORG_ID_  varchar(64);

alter table bpm_task_stack add column trace_  varchar(64);

alter table org_relation add column sn_  int(10);
alter table org_relation add column HAS_CHILD_  varchar(64);

alter table bpm_user_agency_config add column comment_  varchar(200);

 alter table BPM_PLUGIN_REMINDER_LOG add column feedback_  varchar(200);
 alter table BPM_PLUGIN_REMINDER_LOG add column statue_  varchar(1);
 alter table BPM_PLUGIN_REMINDER_LOG add column from_users_  varchar(50);
 alter table BPM_PLUGIN_REMINDER_LOG add column from_userids_  varchar(50);
 alter table BPM_PLUGIN_REMINDER_LOG add column node_name_  varchar(64);
 alter table BPM_PLUGIN_REMINDER_LOG add column task_id_  varchar(64);
 alter table BPM_PLUGIN_REMINDER_LOG add column is_urgent_  varchar(1);


 alter table bus_column add column search_flag_ int(3)  ;
 alter table bus_column add column metadata_id_  varchar(100);

 alter table bus_object add column DIAGRAM_JSON_ longtext;
 alter table bus_object add column save_index_  varchar(255);
 alter table bus_object add column ORG_ID_  varchar(192);

 alter table bus_table add column ORG_ID_  varchar(192);


 alter table form_cust_dialog add column primary_table_config_json_  longtext;
 alter table form_cust_dialog add column data_relation_  varchar(64);
 alter table form_cust_dialog add column dialog_layout_  varchar(64);
 alter table form_cust_dialog add column other_config_json_  varchar(1000);
 alter table form_cust_dialog add column type_name_  varchar(500);
 alter table form_cust_dialog add column create_time_  TIMESTAMP(6);
 alter table form_cust_dialog add column create_by_  varchar(50);
 alter table form_cust_dialog add column update_time_  TIMESTAMP(6);
 alter table form_cust_dialog add column update_by_  varchar(50);
 alter table form_cust_dialog add column type_id_  varchar(50);


 alter table form_def add column ORG_ID_  varchar(192);
 alter table form_def add column modify_desc_  varchar(6000);
 alter table form_def add column lock_by_  varchar(50);
 alter table form_def add column lock_time_  TIMESTAMP(6);

 alter table ORG_GROUP add column SIMPLE_  VARCHAR(60);

 alter table org_role add column sn_  int(4);
 alter table org_role add column ORG_ID_  VARCHAR(60);

 alter table org_user add column sn_  int(4);
 alter table org_user add column TELEPHONE_  VARCHAR(96);
 alter table org_user add column ACTIVE_STATUS_ int(10);
 alter table org_user add column SECRET_LEVEL_  int(10);
 alter table org_user add column TYPE_  VARCHAR(96);
 alter table org_user add column DESC_  VARCHAR(1000);

 alter table sys_data_dict add column create_by_  VARCHAR(255);
 alter table sys_data_dict add column update_time_  TIMESTAMP(6);
 alter table sys_data_dict add column update_by_  VARCHAR(255);

 alter table sys_file add column remark_  VARCHAR(64);
 alter table sys_file add column inst_id_  VARCHAR(64);
 alter table sys_file add column type_  VARCHAR(100);


 alter table sys_log_err add column heads_  longtext;

 alter table sys_subSystem add column type_  VARCHAR(50);

 alter table sys_tree add column MULTI_SELECT  int(3);
 alter table sys_tree add column ICON_SHOW  int(3);
 alter table sys_tree add column DRAG int(3);
 alter table sys_tree add column LEAF_STORE  int(3);
 alter table sys_tree add column ENABLE_EDIT  int(3);


 alter table SYS_TREE_NODE add column icon_  VARCHAR(255);
 alter table SYS_TREE_NODE add column application_name_  VARCHAR(128);
 alter table SYS_TREE_NODE add column create_by_  VARCHAR(64);
 alter table SYS_TREE_NODE add column create_time_  TIMESTAMP(6);



CREATE TABLE `app_station_letter` (
	`id` VARCHAR(64 ) NOT NULL,
	`type` VARCHAR(2000 ) NULL,
	`title` VARCHAR(2000 ) NULL,
	`subject` VARCHAR(200 ) NULL,
	`tag` VARCHAR(50 ) NULL,
	`content` VARCHAR(1000 ) NULL,
	`mkey` VARCHAR(64 ) NULL,
	`status` VARCHAR(4 ) NULL,
	`create_by` VARCHAR(50 ) NULL,
	`create_time` TIMESTAMP(6) NULL,
	`update_by` VARCHAR(50 ) NULL,
	`update_time` TIMESTAMP(6) NULL,
	`recevier_id` VARCHAR(50 ) NULL,
	`recevier_group_type` VARCHAR(500 ) NULL,
	`recevier_group_code` VARCHAR(500 ) NULL,
	`detail_url` VARCHAR(200 ) NULL,
	`detail_id` VARCHAR(50 ) NULL,
	`name` VARCHAR(50 ) NULL,
	`start_time` VARCHAR(20 ) NULL,
	`end_time` VARCHAR(20 ) NULL,
	`meeting_room_name` VARCHAR(50 ) NULL,
	`meeting_name` VARCHAR(80 ) NULL,
	`request_method` VARCHAR(50 ) NULL,
	`schedule_name` VARCHAR(200 ) NULL,
	`list_url` VARCHAR(200 ) NULL,
	`head` VARCHAR(50 ) NULL,
	`statue` VARCHAR(50 ) NULL,
	`is_delete` VARCHAR(50 ) NULL,
	`leader_id` VARCHAR(200 ) NULL,
	`secretary_id` VARCHAR(200 ) NULL,
	`report_leader` VARCHAR(50 ) NULL,
	`duty_end_time` VARCHAR(20 ) NULL,
	`duty_start_time` VARCHAR(20 ) NULL,
	`duty_schedulin_title` VARCHAR(500 ) NULL,
	`effective_time` VARCHAR(20 ) NULL,
	CONSTRAINT `SYS_C0012424` CHECK ((id IS NOT NULL)),
	CONSTRAINT `app_station_letter_PKEY` PRIMARY KEY (`id`)
)

CREATE TABLE `app_station_letter_histroy` (
	`id` VARCHAR(64 ) NOT NULL,
	`type` VARCHAR(2000 ) NULL,
	`title` VARCHAR(2000 ) NULL,
	`subject` VARCHAR(200 ) NULL,
	`tag` VARCHAR(50 ) NULL,
	`content` VARCHAR(2000 ) NULL,
	`mkey` VARCHAR(64 ) NULL,
	`status` VARCHAR(4 ) NULL,
	`create_by` VARCHAR(50 ) NULL,
	`create_time` TIMESTAMP(6) NULL,
	`update_by` VARCHAR(50 ) NULL,
	`update_time` TIMESTAMP(6) NULL,
	`recevier_id` VARCHAR(50 ) NULL,
	`recevier_group_type` VARCHAR(500 ) NULL,
	`recevier_group_code` VARCHAR(500 ) NULL,
	`detail_url` VARCHAR(200 ) NULL,
	`detail_id` VARCHAR(50 ) NULL,
	`name` VARCHAR(50 ) NULL,
	`start_time` VARCHAR(20 ) NULL,
	`end_time` VARCHAR(20 ) NULL,
	`meeting_room_name` VARCHAR(50 ) NULL,
	`meeting_name` VARCHAR(80 ) NULL,
	`request_method` VARCHAR(50 ) NULL,
	`schedule_name` VARCHAR(200 ) NULL,
	`list_url` VARCHAR(200 ) NULL,
	`head` VARCHAR(50 ) NULL,
	`statue` VARCHAR(50 ) NULL,
	`is_delete` VARCHAR(50 ) NULL,
	`leader_id` VARCHAR(200 ) NULL,
	`secretary_id` VARCHAR(200 ) NULL,
	`report_leader` VARCHAR(50 ) NULL,
	`duty_end_time` VARCHAR(20 ) NULL,
	`duty_start_time` VARCHAR(20 ) NULL,
	`duty_schedulin_title` VARCHAR(500 ) NULL,
	`effective_time` VARCHAR(20 ) NULL,
	`parent_id` VARCHAR(64 ) NULL,
	`operate_by` VARCHAR(64 ) NULL,
	`operate_type` VARCHAR(64 ) NULL,
	CONSTRAINT `SYS_C0012423` CHECK ((id IS NOT NULL)),
	CONSTRAINT `app_station_letter_histroy_PKEY` PRIMARY KEY (`id`)
)

CREATE TABLE `bpm_free_branch_definition` (
	`id_` VARCHAR(32 ) NOT NULL,
	`branch_setting_` VARCHAR(4000 ) NULL
)

CREATE TABLE `BPM_TASK_CHECK` (
	`ID_` VARCHAR(50 ) NULL,
	`TASK_ID_` VARCHAR(100 ) NULL,
	`INST_ID_` VARCHAR(100 ) NULL,
	`USER_NAME_` VARCHAR(50 ) NULL,
	`USER_ID_` VARCHAR(50 ) NULL,
	`CHECK_TIME_` TIMESTAMP(6) NULL,
	`TASK_IDENTITYLINK_ID_` VARCHAR(50 ) NULL,
	`CHECK_STATUS_` VARCHAR(50 ) NULL
)
 CREATE TABLE `bus_table_diagram` (
	`id_` VARCHAR(64 ) NOT NULL,
	`diagram_json_` longtext NULL,
	`group_id_` VARCHAR(64 ) NULL DEFAULT NULL,
	`group_name_` VARCHAR(128 ) NULL DEFAULT NULL,
	`create_time_` TIMESTAMP(6) NULL,
	`create_by_` VARCHAR(64 ) NULL DEFAULT NULL,
	`update_time_` TIMESTAMP(6) NULL,
	`update_by_` VARCHAR(64 ) NULL DEFAULT NULL,
	CONSTRAINT `SYS_C0012367` CHECK (id_ IS NOT NULL),
	CONSTRAINT `bus_table_diagram_PKEY` PRIMARY KEY (`id_`)
)

CREATE TABLE `sys_menu` (
	`ID_` VARCHAR(192 ) NOT NULL,
	`SYSTEM_ID_` VARCHAR(192 ) NULL,
	`NAME_` VARCHAR(192 ) NULL,
	`CODE_` VARCHAR(192 ) NULL,
	`TYPE_` VARCHAR(192 ) NULL,
	`ICON_` VARCHAR(192 ) NULL,
	`PARENT_ID_` VARCHAR(192 ) NULL,
	`URL_` VARCHAR(360 ) NULL,
	`SN_` NUMERIC(10) NULL,
	`ENABLE_` NUMERIC(10) NULL,
	`CREATE_TIME_` TIMESTAMP(6) NULL,
	`CREATE_BY_` VARCHAR(192 ) NULL,
	`UPDATE_TIME_` TIMESTAMP(6) NULL,
	`UPDATE_BY_` VARCHAR(192 ) NULL,
	`ICON_COLOR_` VARCHAR(192 ) NULL,
	CONSTRAINT `SYS_C0012248` CHECK ((ID_ IS NOT NULL)),
	CONSTRAINT `sys_menu_PKEY` PRIMARY KEY (`ID_`)
)

CREATE TABLE `sys_menu_role` (
	`ID_` VARCHAR(150 ) NOT NULL ,
	`SYSTEM_ID_` VARCHAR(150 ) NULL,
	`MENU_ID_` VARCHAR(150 ) NULL,
	`ROLE_ID_` VARCHAR(150 ) NULL,
	CONSTRAINT `SYS_C0012247` CHECK ((ID_ IS NOT NULL)),
	CONSTRAINT `sys_menu_role_PKEY` PRIMARY KEY (`ID_`)
)

CREATE TABLE `SYS_LOG_OPERATE` (
	`ID_` VARCHAR(50 ) NOT NULL,
	`USER_ID_` VARCHAR(50 ) NULL,
	`ACCOUNT_` VARCHAR(50 ) NULL,
	`REQUEST_HEAD_` longtext NULL,
	`REQUEST_PARAM_` longtext NULL,
	`OPERATE_TIME_` TIMESTAMP(6) NULL,
	`IP_` VARCHAR(50 ) NULL,
	`PATH_` VARCHAR(50 ) NULL,
	`RESULT_` NUMERIC(10) NULL,
	`RESPONSE_RESULT_DATA_` longtext NULL,
	`CREATE_TIME_` TIMESTAMP(6) NULL,
	`BACKUP_FILE_NAME_` VARCHAR(50 ) NULL,
	`BACKUP_FILE_TYPE_` VARCHAR(50 ) NULL,
	`LOG_TYPE_` NUMERIC(10) NULL,
	`OPERATE_ITEM_` VARCHAR(192 ) NULL,
	`OPERATE_ITEM_NAME_KEY_` VARCHAR(192 ) NULL,
	`OPERATE_TYPE_` VARCHAR(192 ) NULL,
	`OPERATE_OBJECT_TYPE_` NUMERIC NULL,
	`SYSTEM_` VARCHAR(192 ) NULL,
	`TYPE_` NUMERIC NULL,
	CONSTRAINT `SYS_C0012250` CHECK ((ID_ IS NOT NULL)),
	CONSTRAINT `SYS_LOG_OPERATE_PKEY` PRIMARY KEY (`ID_`)
)

CREATE TABLE `SYS_LOG_OPERATE_CONFIG` (
	`ID_` VARCHAR(50 ) NOT NULL,
	`PATH_` VARCHAR(50 ) NULL,
	`OPERATE_TYPE_` VARCHAR(50 ) NULL,
	`OPERATE_ITEM_` VARCHAR(50 ) NULL,
	`OPERATE_ITEM_NAME_KEY_` VARCHAR(50 ) NULL,
	`SYSTEM_` VARCHAR(50 ) NULL,
	`OPERATE_OBJECT_TYPE_` NUMERIC(10) NULL,
	`CREATE_TIME_` TIMESTAMP(6) NULL,
	`TYPE_` NUMERIC(10) NULL,
	CONSTRAINT `SYS_C0012249` CHECK ((ID_ IS NOT NULL)),
	CONSTRAINT `SYS_LOG_OPERATE_CONFIG_PKEY` PRIMARY KEY (`ID_`)
)

CREATE TABLE `org_user_security_config` (
	`ID_` VARCHAR(64 ) NULL,
	`USER_ID_` VARCHAR(64 ) NULL,
	`FIRST_LOGIN_` VARCHAR(4 ) NULL,
	`CREATE_TIME_` TIMESTAMP(6) NULL,
	`CREATE_BY_` VARCHAR(64 ) NULL,
	`UPDATE_TIME_` TIMESTAMP(6) NULL,
	`UPDATE_BY_` VARCHAR(64 ) NULL,
	`PWD_ERR_TIMES_` NUMERIC(22,6) NULL,
	`PWD_ERR_LAST_TIME_` TIMESTAMP(6) NULL,
	`PWD_MODIFY_LAST_TIME_` TIMESTAMP(6) NULL,
	`LOGIN_LAST_TIME_` TIMESTAMP(6) NULL
)

CREATE TABLE `org_user_collect` (
	`ID_` VARCHAR(192 ) NOT NULL,
	`USER_ID_` VARCHAR(192 ) NULL,
	`PAGE_JSON_` longtext NULL,
	`JSON_TYPE_` VARCHAR(192 ) NULL,
	`CREATE_TIME_` TIMESTAMP(6) NULL,
	`CREATE_BY_` VARCHAR(192 ) NULL,
	`UPDATE_TIME_` TIMESTAMP(6) NULL,
	`UPDATE_BY_` VARCHAR(192 ) NULL,
	CONSTRAINT `SYS_C0012298` CHECK ((ID_ IS NOT NULL)),
	CONSTRAINT `org_user_collect_PKEY` PRIMARY KEY (`ID_`)
)
CREATE TABLE `org_user_custom_column` (
	`ID_` VARCHAR(192 ) NOT NULL,
	`MENU_ROUTER_` VARCHAR(400 ) NULL,
	`COLUMN_JSON_` longtext NULL,
	`USER_ID_` VARCHAR(192 ) NULL,
	`CREATE_TIME_` TIMESTAMP(6) NULL,
	`CREATE_BY_` VARCHAR(192 ) NULL,
	`UPDATE_TIME_` TIMESTAMP(6) NULL,
	`UPDATE_BY_` VARCHAR(192 ) NULL,
	CONSTRAINT `SYS_C0012297` CHECK ((ID_ IS NOT NULL)),
	CONSTRAINT `org_user_custom_column_PKEY` PRIMARY KEY (`ID_`)
)