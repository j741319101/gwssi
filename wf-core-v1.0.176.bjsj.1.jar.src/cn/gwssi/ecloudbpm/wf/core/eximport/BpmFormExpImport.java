/*    */ package com.dstz.bpm.core.eximport;
/*    */ 
/*    */ import com.dstz.base.api.bpmExpImport.BpmExpImport;
/*    */ import com.dstz.base.core.util.DBImportUtil;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*    */ import java.io.File;
/*    */ import java.io.FileOutputStream;
/*    */ import java.io.OutputStream;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.jdbc.core.JdbcTemplate;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class BpmFormExpImport implements BpmExpImport {
/*    */   @Resource
/*    */   JdbcTemplate jdbcTemplate;
/*    */   
/*    */   public void bpmExport(String defIds, String filePath) throws Exception {
/* 24 */     if (StringUtil.isNotEmpty(defIds)) {
/*    */       return;
/*    */     }
/* 27 */     File file = new File(filePath);
/* 28 */     if (!file.exists()) {
/* 29 */       file.mkdirs();
/*    */     }
/* 31 */     String sql = "SELECT DEF_SETTING_ FROM BPM_DEFINITION WHERE INSTR(?, ID_) > 0";
/* 32 */     List<Map<String, Object>> defSettings = this.jdbcTemplate.queryForList(sql, new Object[] { defIds });
/* 33 */     StringBuffer formKeys = new StringBuffer();
/* 34 */     defSettings.forEach(defSetting -> {
/*    */           JSONObject jSONObject = JSON.parseObject(defSetting.get("DEF_SETTING_").toString());
/*    */ 
/*    */           
/*    */           String formKey = (String)((Map)((Map)jSONObject.get("flow")).get("globalForm")).get("formValue");
/*    */ 
/*    */           
/*    */           if (StringUtil.isNotEmpty(formKey)) {
/*    */             formKeys.append(formKey).append(",");
/*    */           }
/*    */ 
/*    */           
/*    */           Map<String, Object> globalMobileForm = (Map<String, Object>)((Map)jSONObject.get("flow")).get("globalMobileForm");
/*    */ 
/*    */           
/*    */           if (globalMobileForm != null) {
/*    */             formKey = (String)globalMobileForm.get("formValue");
/*    */ 
/*    */             
/*    */             if (StringUtil.isNotEmpty(formKey)) {
/*    */               formKeys.append(formKey).append(",");
/*    */             }
/*    */           } 
/*    */ 
/*    */           
/*    */           Map<String, Map<String, Object>> nodes = (Map<String, Map<String, Object>>)jSONObject.get("nodeMap");
/*    */           
/*    */           nodes.forEach(());
/*    */         });
/*    */     
/* 64 */     if (StringUtil.isEmpty(formKeys.toString())) {
/*    */       return;
/*    */     }
/* 67 */     sql = "SELECT * FROM FORM_DEF WHERE INSTR(?,KEY_) > 0 ";
/* 68 */     List<Map<String, Object>> data = this.jdbcTemplate.queryForList(sql, new Object[] { formKeys.toString() });
/* 69 */     OutputStream outputStream = new FileOutputStream(filePath + "/FORM_DEF.txt");
/* 70 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/* 71 */     outputStream.close();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void bpmImport(String filePath) throws Exception {
/* 77 */     DBImportUtil.importTable(filePath, "FORM_DEF", "KEY_");
/*    */   }
/*    */ 
/*    */   
/*    */   public String checkImport(String filePath) throws Exception {
/* 82 */     return DBImportUtil.checkTable(filePath, "FORM_DEF", "KEY_", "NAME_");
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/eximport/BpmFormExpImport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */