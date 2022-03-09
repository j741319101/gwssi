/*     */ package com.dstz.bpm.core.eximport;
/*     */ 
/*     */ import com.dstz.base.api.bpmExpImport.BpmExpImport;
/*     */ import com.dstz.base.core.util.DBImportUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import cn.hutool.core.io.IoUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */
/*     */
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class BpmTempExpImport
/*     */   implements BpmExpImport
/*     */ {
/*     */   public void bpmExport(String defIds, String filePath) throws Exception {
/*  29 */     File file = new File(filePath);
/*  30 */     if (!file.exists()) {
/*  31 */       file.mkdirs();
/*     */     }
/*  33 */     String sql = "SELECT DEF_SETTING_ FROM BPM_DEFINITION WHERE INSTR(?, ID_) > 0";
/*  34 */     List<Map<String, Object>> defSettings = this.jdbcTemplate.queryForList(sql, new Object[] { defIds });
/*  35 */     StringBuffer tempKeys = new StringBuffer();
/*  36 */     defSettings.forEach(config -> {
/*     */           JSONObject jSONObject = JSON.parseObject((String)config.get("DEF_SETTING_"));
/*     */           Map<String, Object> flow = (Map<String, Object>)jSONObject.get("flow");
/*     */           Map<String, String> properties = (Map<String, String>)flow.get("properties");
/*     */           String officialDocumentTemplate = properties.get("officialDocumentTemplate");
/*     */           String officialPrintTemplate = properties.get("officialPrintTemplate");
/*     */           if (StringUtil.isNotEmpty(officialDocumentTemplate)) {
/*     */             tempKeys.append(officialDocumentTemplate).append(",");
/*     */           }
/*     */           if (StringUtil.isNotEmpty(officialPrintTemplate)) {
/*     */             tempKeys.append(officialPrintTemplate).append(",");
/*     */           }
/*     */         });
/*  49 */     if (StringUtil.isEmpty(tempKeys.toString())) {
/*     */       return;
/*     */     }
/*     */     
/*  53 */     sql = "SELECT * FROM MODULE_TEMPLATE WHERE INSTR(?, ID_) > 0 ";
/*  54 */     List<Map<String, Object>> data = this.jdbcTemplate.queryForList(sql, new Object[] { tempKeys.toString() });
/*  55 */     OutputStream outputStream = new FileOutputStream(filePath + "/MODULE_TEMPLATE.txt");
/*  56 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  57 */     outputStream.close();
/*     */ 
/*     */     
/*  60 */     sql = "SELECT * FROM SYS_FILE WHERE ID_ IN (SELECT FILE_ID_ FROM MODULE_TEMPLATE WHERE INSTR(?, ID_) > 0)";
/*  61 */     data = this.jdbcTemplate.queryForList(sql, new Object[] { tempKeys.toString() });
/*  62 */     outputStream = new FileOutputStream(filePath + "/SYS_FILE.txt");
/*  63 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  64 */     outputStream.close();
/*     */ 
/*     */     
/*  67 */     sql = "SELECT * FROM DB_UPLOADER WHERE ID_ IN (SELECT PATH_ FROM SYS_FILE WHERE ID_ IN (SELECT FILE_ID_ FROM MODULE_TEMPLATE WHERE INSTR(?,ID_) > 0))";
/*  68 */     File ACT_GE_BYTEARRAY = new File(filePath + "/DB_UPLOADER");
/*  69 */     ACT_GE_BYTEARRAY.mkdir();
/*  70 */     List<Map<String, Object>> actByteArray = new ArrayList<>();
/*  71 */     this.jdbcTemplate.query(sql, new Object[] { tempKeys }, rs -> {
/*     */           try {
/*     */             do {
/*     */               Map<String, Object> actByte = new HashMap<>();
/*     */               String id = rs.getString("ID_");
/*     */               actByte.put("ID_", id);
/*     */               actByte.put("BLOB#BYTES_", id);
/*     */               InputStream inputStream = rs.getAsciiStream("BYTES_");
/*     */               FileOutputStream out = new FileOutputStream(filePath + "/DB_UPLOADER/" + id);
/*     */               IoUtil.copy(inputStream, out);
/*     */               out.close();
/*     */               actByteArray.add(actByte);
/*     */             } while (rs.next());
/*  84 */           } catch (Exception e) {
/*     */             throw new RuntimeException();
/*     */           } 
/*     */         });
/*  88 */     outputStream = new FileOutputStream(filePath + "/DB_UPLOADER.txt");
/*  89 */     outputStream.write(JSON.toJSONString(actByteArray, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  90 */     outputStream.close();
/*     */   }
/*     */   @Resource
/*     */   JdbcTemplate jdbcTemplate;
/*     */   public void bpmImport(String filePath) throws Exception {
/*  95 */     DBImportUtil.importTable(filePath, "MODULE_TEMPLATE");
/*  96 */     DBImportUtil.importTable(filePath, "SYS_FILE");
/*  97 */     DBImportUtil.importTable(filePath, "DB_UPLOADER");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String checkImport(String filePath) throws Exception {
/* 104 */     StringBuffer returnStr = (new StringBuffer(DBImportUtil.checkTable(filePath, "MODULE_TEMPLATE", "ID_", "NAME_"))).append("\n").append(DBImportUtil.checkTable(filePath, "SYS_FILE", "ID_", "NAME_")).append("\n").append(DBImportUtil.checkTable(filePath, "DB_UPLOADER", "ID_", "ID_"));
/* 105 */     return returnStr.toString();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/eximport/BpmTempExpImport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */