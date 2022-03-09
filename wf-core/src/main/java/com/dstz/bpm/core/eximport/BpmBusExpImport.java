//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.dstz.bpm.core.eximport;

import com.dstz.bus.manager.BusinessColumnManager;
import com.dstz.bus.manager.BusinessObjectManager;
import com.dstz.bus.manager.BusinessTableManager;
import com.dstz.bus.model.BusTableRel;
import com.dstz.bus.model.BusinessObject;
import com.dstz.bus.model.BusinessTable;
import com.dstz.bus.util.BusinessTableCacheUtil;
import com.dstz.base.api.bpmExpImport.BpmExpImport;
import com.dstz.base.api.exception.BusinessException;
import com.dstz.base.core.util.DBImportUtil;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.tableoper.TableOperator;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BpmBusExpImport implements BpmExpImport {
    @Resource
    private JdbcTemplate jdbcTemplate;
    @Resource
    private BusinessObjectManager businessObjectManager;
    @Resource
    private BusinessTableManager businessTableManager;
    @Resource
    private BusinessColumnManager businessColumnManager;

    public BpmBusExpImport() {
    }

    public void bpmExport(String defIds, String filePath) throws Exception {
        if (!StringUtil.isNotEmpty(defIds)) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            String sql = "SELECT DEF_SETTING_ FROM BPM_DEFINITION WHERE INSTR(?, ID_) > 0";
            List<Map<String, Object>> defSettings = this.jdbcTemplate.queryForList(sql, new Object[]{defIds});
            StringBuffer objKeys = new StringBuffer();
            defSettings.forEach((defSetting) -> {
                Map<String, Object> setJson = JSON.parseObject(defSetting.get("DEF_SETTING_").toString());
                ((List<Map>)((Map)setJson.get("flow")).get("dataModelList")).forEach((objKey) -> {
                    String code = (String)objKey.get("code");
                    if (StringUtil.isNotEmpty(code)) {
                        objKeys.append(code).append(",");
                    }

                });
            });
            if (!StringUtil.isEmpty(objKeys.toString())) {
                String busSql = "SELECT * FROM BUS_OBJECT WHERE INSTR(?, KEY_) > 0 ";
                List<Map<String, Object>> data = this.jdbcTemplate.queryForList(busSql, new Object[]{objKeys.toString()});
                OutputStream outputStream = new FileOutputStream(filePath + "/BUS_OBJECT.txt");
                outputStream.write(JSON.toJSONString(data, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue}).getBytes("UTF-8"));
                outputStream.close();
                StringBuffer tableKeys = new StringBuffer();
                Arrays.asList(objKeys.toString().split(",")).stream().filter((objKey) -> {
                    return StringUtil.isNotEmpty(objKey);
                }).forEach((objKey) -> {
                    BusinessObject businessObject = this.businessObjectManager.getFilledByKey(objKey);
                    this.appendBusTableKey(tableKeys, businessObject.getRelation());
                });
                busSql = "SELECT * FROM BUS_TABLE WHERE INSTR(?, KEY_) > 0";
                data = this.jdbcTemplate.queryForList(busSql, new Object[]{tableKeys.toString()});
                outputStream = new FileOutputStream(filePath + "/BUS_TABLE.txt");
                outputStream.write(JSON.toJSONString(data, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue}).getBytes("UTF-8"));
                outputStream.close();
                data = this.jdbcTemplate.queryForList("SELECT * FROM SYS_TREE_NODE START WITH ID_ IN (SELECT TAB.GROUP_ID_ FROM BUS_TABLE TAB WHERE INSTR(?,TAB.KEY_) > 0 UNION SELECT OBJ.GROUP_ID_ FROM BUS_OBJECT OBJ WHERE INSTR(?,OBJ.KEY_) > 0 UNION SELECT DEF.TYPE_ID_ FROM BPM_DEFINITION DEF WHERE INSTR(?,DEF.ID_) > 0 )CONNECT BY ID_  = PRIOR PARENT_ID_", new Object[]{tableKeys.toString(), objKeys.toString(), defIds});
                outputStream = new FileOutputStream(filePath + "/SYS_TREE_NODE.txt");
                outputStream.write(JSON.toJSONString(data, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue}).getBytes("UTF-8"));
                outputStream.close();
                busSql = "SELECT * FROM BUS_COLUMN WHERE TABLE_ID_ IN ((SELECT ID_ FROM BUS_TABLE WHERE INSTR(?, KEY_) > 0))";
                data = this.jdbcTemplate.queryForList(busSql, new Object[]{tableKeys.toString()});
                outputStream = new FileOutputStream(filePath + "/BUS_COLUMN.txt");
                outputStream.write(JSON.toJSONString(data, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue}).getBytes("UTF-8"));
                outputStream.close();
                busSql = "SELECT * FROM BUS_COLUMN_CTRL WHERE COLUMN_ID_ IN (SELECT ID_ FROM BUS_COLUMN WHERE TABLE_ID_ IN ((SELECT ID_ FROM BUS_TABLE WHERE INSTR(?, KEY_) > 0)))";
                data = this.jdbcTemplate.queryForList(busSql, new Object[]{tableKeys.toString()});
                outputStream = new FileOutputStream(filePath + "/BUS_COLUMN_CTRL.txt");
                outputStream.write(JSON.toJSONString(data, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue}).getBytes("UTF-8"));
                outputStream.close();
            }
        }
    }

    public void bpmImport(String filePath) throws Exception {
        DBImportUtil.importTable(filePath, "BUS_OBJECT", "key_");
        DBImportUtil.importTable(filePath, "BUS_TABLE", "key_");
        DBImportUtil.cleanTable(filePath, "BUS_COLUMN", "table_id_");
        DBImportUtil.importTable(filePath, "BUS_COLUMN");
        DBImportUtil.importTable(filePath, "BUS_COLUMN_CTRL");
        DBImportUtil.importTable(filePath, "SYS_TREE_NODE");
        this.rebuildTable(filePath);
    }

    private void rebuildTable(String filePath) throws Exception {
        File file = new File(filePath + "/BUS_TABLE.txt");
        if (file.exists()) {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
            StringBuffer buffer = new StringBuffer();
            String line = "";

            while((line = in.readLine()) != null) {
                buffer.append(line);
            }

            in.close();
            String bpmDef = buffer.toString();
            JSONArray jsonArray = JSON.parseArray(bpmDef);
            List<Map> params = jsonArray.toJavaList(Map.class);
            params.forEach((map) -> {
                Iterator var2 = map.keySet().iterator();

                while(var2.hasNext()) {
                    Object key = var2.next();
                    if (StringUtils.equalsIgnoreCase("ID_", key.toString())) {
                        BusinessTable businessTable = (BusinessTable)this.businessTableManager.get((String)map.get(key.toString()));
                        businessTable.setColumns(this.businessColumnManager.getByTableId(businessTable.getId()));
                        TableOperator tableOperator = this.businessTableManager.newTableOperator(businessTable);

                        try {
                            if (tableOperator.isTableCreated()) {
                                tableOperator.syncColumn();
                            } else {
                                tableOperator.createTable();
                                businessTable.setCreatedTable(true);
                            }
                        } catch (Exception var7) {
                            throw new BusinessException(var7.getMessage() + " tableId: " + key.toString());
                        }

                        BusinessTableCacheUtil.removeByKey(businessTable.getKey());
                        this.businessTableManager.getFilledByKey(businessTable.getKey());
                    }
                }

            });
        }
    }

    public String checkImport(String filePath) throws Exception {
        StringBuffer returnStr = (new StringBuffer(DBImportUtil.checkTable(filePath, "BUS_OBJECT", "key_", "NAME_"))).append("\n").append(DBImportUtil.checkTable(filePath, "BUS_TABLE", "key_", "NAME_")).append("\n").append(DBImportUtil.checkTable(filePath, "BUS_COLUMN", "ID_", "ID_")).append("\n").append(DBImportUtil.checkTable(filePath, "BUS_COLUMN_CTRL", "ID_", "ID_")).append("\n").append(DBImportUtil.checkTable(filePath, "BUS_PERMISSION", "ID_", "OBJ_VAL_"));
        return returnStr.toString();
    }

    private void appendBusTableKey(StringBuffer sb, BusTableRel busTableRel) {
        sb.append(busTableRel.getTableKey()).append(",");
        busTableRel.getChildren().forEach((rel) -> {
            this.appendBusTableKey(sb, rel);
        });
    }
}
