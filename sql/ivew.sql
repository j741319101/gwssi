

show variables where Variable_name like 'collation%';

show variables like "%char%"


CREATE VIEW USER_GROUP_ROLE AS  SELECT tuser.ID_ AS user_id,
    tuser.FULLNAME_ AS user_name,
    tuser.ACCOUNT_ AS account,
    tuser.sn_ AS sn,
    torg.groupid AS org_id,
    torg.groupname AS org_name,
    torg.grouppath AS org_path,
    trole.roleid AS role_id,
    trole.rolename AS role_name,
    tpost.postid AS duty_id,
    tpost.postname AS duty_name,
    tpost.postcode AS duty_code,
    torgm.groupid AS org_master_id,
    torgm.groupname AS org_master_name,
    torgm.grouppath AS org_master_path,
    tpostm.postid AS duty_master_id,
    tpostm.postname AS duty_master_name,
    tpostm.postcode AS duty_master_code
   FROM org_user tuser
     LEFT JOIN ( SELECT relationorg.USER_ID_,
            CONCAT(tgroup.NAME_  ) AS groupname,
            CONCAT(tgroup.ID_  ) AS groupid,
            CONCAT(tgroup.PATH_  ) AS grouppath
           FROM org_relation relationorg,
            ORG_GROUP tgroup
          WHERE relationorg.GROUP_ID_ = tgroup.ID_ AND relationorg.TYPE_ = 'groupUser'  AND relationorg.IS_MASTER_ <> '1' 
          GROUP BY relationorg.USER_ID_) torg ON tuser.ID_ = torg.USER_ID_
     LEFT JOIN ( SELECT relationrole.USER_ID_,
            CONCAT(trole_1.NAME_  ) AS rolename,
            CONCAT(trole_1.ID_  ) AS roleid
           FROM org_relation relationrole,
            org_role trole_1
          WHERE relationrole.ROLE_ID_ = trole_1.ID_ AND relationrole.TYPE_ = 'userRole'  AND relationrole.IS_MASTER_ <> '1' 
          GROUP BY relationrole.USER_ID_) trole ON tuser.ID_ = trole.USER_ID_
     LEFT JOIN ( SELECT relationpost.USER_ID_,
            CONCAT(tpost_1.NAME_  ) AS postname,
            CONCAT(tpost_1.ID_  ) AS postid,
            CONCAT(tpost_1.CODE_  ) AS postcode
           FROM org_relation relationpost,
            org_post tpost_1
          WHERE relationpost.GROUP_ID_ = tpost_1.ID_ AND relationpost.TYPE_ = 'postUser'  AND relationpost.IS_MASTER_ <> '1' 
          GROUP BY relationpost.USER_ID_) tpost ON tuser.ID_ = tpost.USER_ID_
     LEFT JOIN ( SELECT relationorg.USER_ID_,
            tgroup.NAME_ AS groupname,
            tgroup.ID_ AS groupid,
            tgroup.PATH_ AS grouppath
           FROM org_relation relationorg,
            ORG_GROUP tgroup
          WHERE relationorg.GROUP_ID_ = tgroup.ID_ AND relationorg.TYPE_ = 'groupUser'  AND relationorg.IS_MASTER_ = '1' ) torgm ON tuser.ID_ = torgm.USER_ID_
     LEFT JOIN ( SELECT relationpost.USER_ID_,
            tpost_1.NAME_ AS postname,
            tpost_1.ID_ AS postid,
            tpost_1.CODE_ AS postcode
           FROM org_relation relationpost,
            org_post tpost_1
          WHERE relationpost.GROUP_ID_ = tpost_1.ID_ AND relationpost.TYPE_ = 'postUser'  AND relationpost.IS_MASTER_ = '1' ) tpostm ON tuser.ID_ = tpostm.USER_ID_
  WHERE tuser.STATUS_ = '1' AND tuser.ACTIVE_STATUS_ = '1' ;


show create table org_relation  ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC
show create table ORG_GROUP  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织架构'
show create table org_post   ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
show create table org_role  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='角色管理'
show create table org_user  ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表'

alter table 表二 default character set utf8mb4 collate=utf8mb4_0900_ai_ci ;

alter table 表二 default character set utf8mb4 collate=utf8mb4_0900_ai_ci ;
