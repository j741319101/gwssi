
INSERT INTO ORG_USER (ID_, FULLNAME_, ACCOUNT_, PASSWORD_, EMAIL_, MOBILE_, WEIXIN_, ADDRESS_, PHOTO_, SEX_, FROM_, STATUS_, CREATE_TIME_, CREATE_BY_, UPDATE_BY_, UPDATE_TIME_, sn_, TELEPHONE_, ACTIVE_STATUS_, SECRET_LEVEL_, TYPE_, DESC_) VALUES ('1', '系统管理员', 'admin', 'vLFfghR5tNV3K9DKhmwArV+SbjWAcgZZzIDTnJ0JgCo=', '', '', 'test', '', '', '0', 'system', 1, '2019-08-15 03:04:50', null, '1', '2020-12-10 17:51:09', 2000, '', 1, null, '1', null);

INSERT INTO ORG_GROUP(ID_, NAME_, PARENT_ID_, SN_, CODE_, TYPE_, DESC_, CREATE_TIME_, CREATE_BY_, UPDATE_TIME_, UPDATE_BY_, PATH_, SIMPLE_) VALUES ('1', '组织架构树', '0', 1, 'zzjgs', '0', 'eCloudBPM', null, null, '2020-12-25 13:57:35', '1', '1', '');

INSERT INTO ORG_ROLE(ID_, NAME_, ALIAS_, ENABLED_, DESCRIPTION, CREATE_TIME_, CREATE_BY_, UPDATE_TIME_, UPDATE_BY_, TYPE_ID_, TYPE_NAME_, sn_, ORG_ID_) VALUES ('410475066098974721', '管理员', 'gly', 1, '', '2019-08-15 03:01:52', '1', '2019-08-15 03:01:52', '1', null, null, 1, '410054569125740545');
INSERT INTO ORG_ROLE(ID_, NAME_, ALIAS_, ENABLED_, DESCRIPTION, CREATE_TIME_, CREATE_BY_, UPDATE_TIME_, UPDATE_BY_, TYPE_ID_, TYPE_NAME_, sn_, ORG_ID_) VALUES ('410475069931782145', '普通用户', 'ptyh', 1, '', '2019-08-15 03:02:07', '1', '2019-08-15 03:02:07', '1', null, null, 1, '410054569125740545');

INSERT INTO ORG_RELATION(ID_, GROUP_ID_, USER_ID_, IS_MASTER_, STATUS_, TYPE_, CREATE_TIME_, CREATE_BY_, UPDATE_TIME_, UPDATE_BY_, SN_, HAS_CHILD_) VALUES ('419698756484857857', '410475066098974721', '1', 1, 1, 'userRole', '2020-09-25 16:48:19', '1', '2020-09-25 16:48:19', '1', null, null);

INSERT INTO ORG_RELATION(ID_, GROUP_ID_, USER_ID_, IS_MASTER_, STATUS_, TYPE_, CREATE_TIME_, CREATE_BY_, UPDATE_TIME_, UPDATE_BY_, SN_, HAS_CHILD_) VALUES ('421350890945904641', '1', '1', 1, 1, 'groupUser', '2020-12-07 15:28:12', '1', '2020-12-07 15:28:12', '1', null, null);