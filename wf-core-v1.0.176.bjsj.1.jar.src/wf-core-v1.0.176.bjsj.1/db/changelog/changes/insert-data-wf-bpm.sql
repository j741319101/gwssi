create procedure queryStack(taskId varchar(64), prior varchar(20), whereSql varchar(4000), orderBySql varchar(4000))
begin
    declare sTemp varchar(4000);
    declare sTempChd varchar(4000);
    declare level int;
    DECLARE l_sql VARCHAR(4000);
    set sTemp = '$';
    set level = 1;
    if prior = 'FORWARD' then
        select id_ into sTempChd from bpm_task_stack where task_id_ = taskId order by id_ limit 1;
        while sTempChd is not null
            do
                set sTemp = concat(sTemp, ',', sTempChd, ',', concat(level, '-'));
                set level = level + 1;
                select group_concat(id_) into sTempChd from bpm_task_stack where find_in_set(parent_id_, sTempChd) > 0;
            end while;
    else
        select parent_id_ into sTempChd from bpm_task_stack where task_id_ = taskId order by id_ limit 1;
        while sTempChd is not null
            do
                set sTemp = concat(sTemp, ',', sTempChd, ',', concat(level, '-'));
                set level = level + 1;
                select group_concat(parent_id_) into sTempChd from bpm_task_stack where find_in_set(id_, sTempChd) > 0;
            end while;
    end if;
    set l_sql =
            concat('select * from (select stack.*, replace(replace(substring(''' , sTemp ,
                ''', locate(id_, ''' , sTemp ,
                ''') + length(id_) + 1, 3), ''-'', ''''), '','', '''') as level from bpm_task_stack stack  where find_in_set(id_, ''' ,
                sTemp ,''') ) qw') ;
    if whereSql is not null and length(whereSql) > 0 then
        set l_sql = concat(l_sql, ' where ', whereSql);
    end if;
    if orderBySql is not null and length(orderBySql) > 0 then
        set l_sql = concat(l_sql, ' order by ', orderBySql);
    end if;
    SET @sql = l_sql;
    PREPARE s1 FROM @sql;
    EXECUTE s1;
    DEALLOCATE PREPARE s1;
end;