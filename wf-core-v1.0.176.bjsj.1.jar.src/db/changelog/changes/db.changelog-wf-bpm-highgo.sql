drop function queryStack;
create function queryStack(taskId varchar(64), prior varchar(20), whereSql varchar(4000), orderBySql varchar(4000))
    returns table
            (
                id             varchar,
                task_id        varchar,
                inst_id        varchar,
                parent_id      varchar,
                node_id        varchar,
                node_name      varchar,
                start_time     timestamp,
                end_time       timestamp,
                is_muliti_task int,
                node_type      varchar,
                action_name    varchar,
                trace          varchar,
                level          text
            )
as
$$
declare
    sTemp    varchar(4000);
    sTempChd varchar(4000);
    level    int;
    l_sql    VARCHAR(4000);
begin
    sTemp = '$';
    level = 1;
    if prior = 'FORWARD' then
        select id_ into sTempChd from bpm_task_stack where task_id_ = taskId order by id_ limit 1;
        while sTempChd is not null and LENGTH(sTempChd) > 0
            loop
                sTemp = concat(sTemp, ',', sTempChd, ',', concat(level, '-'));
                level = level + 1;
                select array_to_string(ARRAY(SELECT unnest(array_agg(id_))), ',')
                into sTempChd
                from bpm_task_stack
                where parent_id_ = ANY (string_to_array(sTempChd, ','));
            end loop;
    else
        select id_ into sTempChd from bpm_task_stack where task_id_ = taskId order by id_ limit 1;
        if sTempChd is not null and LENGTH(sTempChd) > 0 then
            sTemp = concat(sTemp, ',', sTempChd, ',', concat(level, '-'));
            level = level + 1;
            select parent_id_ into sTempChd from bpm_task_stack where task_id_ = taskId order by id_ limit 1;
        end if;
        while sTempChd is not null and LENGTH(sTempChd) > 0
            loop
                sTemp = concat(sTemp, ',', sTempChd, ',', concat(level, '-'));
                level = level + 1;
                select array_to_string(ARRAY(SELECT unnest(array_agg(parent_id_))), ',')
                into sTempChd
                from bpm_task_stack
                where id_ = ANY (string_to_array(sTempChd, ','));
            end loop;
    end if;
    l_sql = 'select * from (select stack.*, replace(replace(substring(''' || sTemp || ''',strpos(''' || sTemp || ''',id_) + strpos(substring(''' || sTemp || ''',strpos(''' || sTemp ||
                ''',id_)),''-'') -2, 3), ''-'', ''''), '','', '''') as level from bpm_task_stack stack  where id_ = ANY (string_to_array(''' || sTemp || ''' ,'',''))) qw';
    if whereSql is not null and length(whereSql) > 0 then
        l_sql = concat(l_sql, ' where ', whereSql);
    end if;
    if orderBySql is not null and length(orderBySql) > 0 then
        l_sql = concat(l_sql, ' order by ', orderBySql);
    end if;
    return query execute l_sql;
end
$$ language plpgsql
;