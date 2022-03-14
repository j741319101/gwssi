/*     */ package com.dstz.bpm.core.eximport;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessColumnManager;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessTableManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*     */ import cn.gwssi.ecloudbpm.bus.util.BusinessTableCacheUtil;
/*     */ import com.dstz.base.api.bpmExpImport.BpmExpImport;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.DBImportUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.tableoper.TableOperator;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class BpmBusExpImport
/*     */   implements BpmExpImport
/*     */ {
/*     */   @Resource
/*     */   private JdbcTemplate jdbcTemplate;
/*     */   
/*     */   public void bpmExport(String defIds, String filePath) throws Exception {
/*  42 */     if (StringUtil.isNotEmpty(defIds)) {
/*     */       return;
/*     */     }
/*  45 */     File file = new File(filePath);
/*  46 */     if (!file.exists()) {
/*  47 */       file.mkdirs();
/*     */     }
/*  49 */     String sql = "SELECT DEF_SETTING_ FROM BPM_DEFINITION WHERE INSTR(?, ID_) > 0";
/*  50 */     List<Map<String, Object>> defSettings = this.jdbcTemplate.queryForList(sql, new Object[] { defIds });
/*  51 */     StringBuffer objKeys = new StringBuffer();
/*     */     
/*  53 */     defSettings.forEach(defSetting -> {
/*     */           JSONObject jSONObject = JSON.parseObject(defSetting.get("DEF_SETTING_").toString());
/*     */ 
/*     */ 
/*     */           
/*     */           ((List)((Map)jSONObject.get("flow")).get("dataModelList")).forEach(());
/*     */         });
/*     */ 
/*     */     
/*  62 */     if (StringUtil.isEmpty(objKeys.toString())) {
/*     */       return;
/*     */     }
/*     */     
/*  66 */     String busSql = "SELECT * FROM BUS_OBJECT WHERE INSTR(?, KEY_) > 0 ";
/*  67 */     List<Map<String, Object>> data = this.jdbcTemplate.queryForList(busSql, new Object[] { objKeys.toString() });
/*  68 */     OutputStream outputStream = new FileOutputStream(filePath + "/BUS_OBJECT.txt");
/*  69 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  70 */     outputStream.close();
/*     */     
/*  72 */     StringBuffer tableKeys = new StringBuffer();
/*  73 */     Arrays.<String>asList(objKeys.toString().split(","))
/*  74 */       .stream().filter(objKey -> StringUtil.isNotEmpty(objKey)).forEach(objKey -> {
/*     */           BusinessObject businessObject = this.businessObjectManager.getFilledByKey(objKey);
/*     */           
/*     */           appendBusTableKey(tableKeys, businessObject.getRelation());
/*     */         });
/*     */     
/*  80 */     busSql = "SELECT * FROM BUS_TABLE WHERE INSTR(?, KEY_) > 0";
/*  81 */     data = this.jdbcTemplate.queryForList(busSql, new Object[] { tableKeys.toString() });
/*  82 */     outputStream = new FileOutputStream(filePath + "/BUS_TABLE.txt");
/*  83 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  84 */     outputStream.close();
/*     */ 
/*     */     
/*  87 */     data = this.jdbcTemplate.queryForList("SELECT * FROM SYS_TREE_NODE START WITH ID_ IN (SELECT TAB.GROUP_ID_ FROM BUS_TABLE TAB WHERE INSTR(?,TAB.KEY_) > 0 UNION SELECT OBJ.GROUP_ID_ FROM BUS_OBJECT OBJ WHERE INSTR(?,OBJ.KEY_) > 0 UNION SELECT DEF.TYPE_ID_ FROM BPM_DEFINITION DEF WHERE INSTR(?,DEF.ID_) > 0 )CONNECT BY ID_  = PRIOR PARENT_ID_", new Object[] { tableKeys
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  92 */           .toString(), objKeys.toString(), defIds });
/*  93 */     outputStream = new FileOutputStream(filePath + "/SYS_TREE_NODE.txt");
/*  94 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  95 */     outputStream.close();
/*     */ 
/*     */     
/*  98 */     busSql = "SELECT * FROM BUS_COLUMN WHERE TABLE_ID_ IN ((SELECT ID_ FROM BUS_TABLE WHERE INSTR(?, KEY_) > 0))";
/*  99 */     data = this.jdbcTemplate.queryForList(busSql, new Object[] { tableKeys.toString() });
/* 100 */     outputStream = new FileOutputStream(filePath + "/BUS_COLUMN.txt");
/* 101 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/* 102 */     outputStream.close();
/*     */ 
/*     */     
/* 105 */     busSql = "SELECT * FROM BUS_COLUMN_CTRL WHERE COLUMN_ID_ IN (SELECT ID_ FROM BUS_COLUMN WHERE TABLE_ID_ IN ((SELECT ID_ FROM BUS_TABLE WHERE INSTR(?, KEY_) > 0)))";
/* 106 */     data = this.jdbcTemplate.queryForList(busSql, new Object[] { tableKeys.toString() });
/* 107 */     outputStream = new FileOutputStream(filePath + "/BUS_COLUMN_CTRL.txt");
/* 108 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/* 109 */     outputStream.close();
/*     */   }
/*     */   @Resource
/*     */   private BusinessObjectManager businessObjectManager; @Resource
/*     */   private BusinessTableManager businessTableManager; @Resource
/*     */   private BusinessColumnManager businessColumnManager;
/*     */   public void bpmImport(String filePath) throws Exception {
/* 116 */     DBImportUtil.importTable(filePath, "BUS_OBJECT", "key_");
/* 117 */     DBImportUtil.importTable(filePath, "BUS_TABLE", "key_");
/* 118 */     DBImportUtil.cleanTable(filePath, "BUS_COLUMN", "table_id_");
/* 119 */     DBImportUtil.importTable(filePath, "BUS_COLUMN");
/* 120 */     DBImportUtil.importTable(filePath, "BUS_COLUMN_CTRL");
/* 121 */     DBImportUtil.importTable(filePath, "SYS_TREE_NODE");
/* 122 */     rebuildTable(filePath);
/*     */   }
/*     */   private void rebuildTable(String filePath) throws Exception {
/* 125 */     File file = new File(filePath + "/BUS_TABLE.txt");
/* 126 */     if (!file.exists()) {
/*     */       return;
/*     */     }
/* 129 */     BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/* 130 */     StringBuffer buffer = new StringBuffer();
/* 131 */     String line = "";
/* 132 */     while ((line = in.readLine()) != null) {
/* 133 */       buffer.append(line);
/*     */     }
/* 135 */     in.close();
/* 136 */     String bpmDef = buffer.toString();
/* 137 */     JSONArray jsonArray = JSON.parseArray(bpmDef);
/* 138 */     List<Map> params = jsonArray.toJavaList(Map.class);
/* 139 */     params.forEach(map -> {
/*     */           for (Object key : map.keySet()) {
/*     */             if (StringUtils.equalsIgnoreCase("ID_", key.toString())) {
/*     */               BusinessTable businessTable = (BusinessTable)this.businessTableManager.get((String)map.get(key.toString()));
/*     */               businessTable.setColumns(this.businessColumnManager.getByTableId(businessTable.getId()));
/*     */               TableOperator tableOperator = this.businessTableManager.newTableOperator(businessTable);
/*     */               try {
/*     */                 if (tableOperator.isTableCreated()) {
/*     */                   tableOperator.syncColumn();
/*     */                 } else {
/*     */                   tableOperator.createTable();
/*     */                   businessTable.setCreatedTable(true);
/*     */                 } 
/* 152 */               } catch (Exception e) {
/*     */                 throw new BusinessException(e.getMessage() + " tableId: " + key.toString());
/*     */               } 
/*     */               BusinessTableCacheUtil.removeByKey(businessTable.getKey());
/*     */               this.businessTableManager.getFilledByKey(businessTable.getKey());
/*     */             } 
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String checkImport(String filePath) throws Exception {
/* 169 */     StringBuffer returnStr = (new StringBuffer(DBImportUtil.checkTable(filePath, "BUS_OBJECT", "key_", "NAME_"))).append("\n").append(DBImportUtil.checkTable(filePath, "BUS_TABLE", "key_", "NAME_")).append("\n").append(DBImportUtil.checkTable(filePath, "BUS_COLUMN", "ID_", "ID_")).append("\n").append(DBImportUtil.checkTable(filePath, "BUS_COLUMN_CTRL", "ID_", "ID_")).append("\n").append(DBImportUtil.checkTable(filePath, "BUS_PERMISSION", "ID_", "OBJ_VAL_"));
/* 170 */     return returnStr.toString();
/*     */   }
/*     */   
/*     */   private void appendBusTableKey(StringBuffer sb, BusTableRel busTableRel) {
/* 174 */     sb.append(busTableRel.getTableKey()).append(",");
/* 175 */     busTableRel.getChildren().forEach(rel -> appendBusTableKey(sb, rel));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/eximport/BpmBusExpImport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */