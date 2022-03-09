/*     */ package cn.gwssi.ecloudbpm.wf.core.eximport;
/*     */ import cn.gwssi.ecloudbpm.wf.act.cache.ActivitiDefCache;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.event.BpmDefinitionUpdateEvent;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.IBpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*     */ import cn.gwssi.ecloudframework.base.api.bpmExpImport.BpmExpImport;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.DBImportUtil;
/*     */ import cn.hutool.core.io.IoUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.transaction.annotation.Transactional;
/*     */ 
/*     */ @Component
/*     */ public class BpmDefinitionExpImport implements BpmExpImport {
/*     */   @Resource
/*     */   JdbcTemplate jdbcTemplate;
/*     */   
/*     */   public void bpmExport(String defIds, String filePath) throws Exception {
/*  37 */     File file = new File(filePath);
/*  38 */     if (!file.exists()) {
/*  39 */       file.mkdirs();
/*     */     }
/*     */     
/*  42 */     List<Map<String, Object>> data = this.jdbcTemplate.queryForList("select * from BPM_DEFINITION where instr(?, ID_) > 0 ", new Object[] { defIds });
/*     */     
/*  44 */     OutputStream outputStream = new FileOutputStream(filePath + "/BPM_DEFINITION.txt");
/*  45 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  46 */     outputStream.close();
/*     */     
/*  48 */     data = this.jdbcTemplate.queryForList("select * from ACT_RE_MODEL where  ID_ in (select ACT_MODEL_ID_ from BPM_DEFINITION where instr(?, ID_) > 0) ", new Object[] { defIds });
/*     */     
/*  50 */     outputStream = new FileOutputStream(filePath + "/ACT_RE_MODEL.txt");
/*  51 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  52 */     outputStream.close();
/*     */ 
/*     */     
/*  55 */     data = this.jdbcTemplate.queryForList("select * from ACT_RE_DEPLOYMENT where ID_ in (select ACT_DEPLOY_ID_ from BPM_DEFINITION where instr(?, ID_) > 0)", new Object[] { defIds });
/*     */     
/*  57 */     outputStream = new FileOutputStream(filePath + "/ACT_RE_DEPLOYMENT.txt");
/*  58 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  59 */     outputStream.close();
/*     */ 
/*     */     
/*  62 */     data = this.jdbcTemplate.queryForList("select * from ACT_RE_PROCDEF where ID_ in (select ACT_DEF_ID_ from BPM_DEFINITION where instr(?, ID_) > 0)", new Object[] { defIds });
/*     */     
/*  64 */     outputStream = new FileOutputStream(filePath + "/ACT_RE_PROCDEF.txt");
/*  65 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/*  66 */     outputStream.close();
/*     */ 
/*     */     
/*  69 */     File ACT_GE_BYTEARRAY = new File(filePath + "/ACT_GE_BYTEARRAY");
/*  70 */     ACT_GE_BYTEARRAY.mkdir();
/*  71 */     List<Map<String, Object>> actByteArray = new ArrayList<>();
/*  72 */     String sql = "select * from ACT_GE_BYTEARRAY where ID_ in (select EDITOR_SOURCE_VALUE_ID_ from ACT_RE_MODEL where ID_ in (select ACT_MODEL_ID_ from BPM_DEFINITION where instr(?, ID_) > 0) union select EDITOR_SOURCE_EXTRA_VALUE_ID_ from ACT_RE_MODEL where ID_ in (select ACT_MODEL_ID_ from BPM_DEFINITION where instr(?, ID_) > 0))or DEPLOYMENT_ID_ in (select ACT_DEPLOY_ID_ from BPM_DEFINITION where instr(?, ID_) > 0)";
/*     */ 
/*     */ 
/*     */     
/*  76 */     this.jdbcTemplate.query(sql, (Object[])new String[] { defIds, defIds, defIds }, rs -> {
/*     */           try {
/*     */             do {
/*     */               Map<String, Object> actByte = new HashMap<>();
/*     */               
/*     */               String id = rs.getString("ID_");
/*     */               actByte.put("ID_", id);
/*     */               actByte.put("REV_", Integer.valueOf(rs.getInt("REV_")));
/*     */               actByte.put("NAME_", rs.getString("NAME_"));
/*     */               actByte.put("DEPLOYMENT_ID_", rs.getString("DEPLOYMENT_ID_"));
/*     */               actByte.put("GENERATED_", rs.getString("GENERATED_"));
/*     */               actByte.put("BLOB#BYTES_", id);
/*     */               InputStream inputStream = rs.getBinaryStream("BYTES_");
/*     */               FileOutputStream out = new FileOutputStream(filePath + "/ACT_GE_BYTEARRAY/" + id);
/*     */               IoUtil.copy(inputStream, out);
/*     */               out.close();
/*     */               actByteArray.add(actByte);
/*     */             } while (rs.next());
/*  94 */           } catch (Exception e) {
/*     */             throw new RuntimeException();
/*     */           } 
/*     */         });
/*     */     
/*  99 */     outputStream = new FileOutputStream(filePath + "/ACT_GE_BYTEARRAY.txt");
/* 100 */     outputStream.write(JSON.toJSONString(actByteArray, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/* 101 */     outputStream.close();
/*     */ 
/*     */     
/* 104 */     sql = "SELECT * FROM BUS_PERMISSION WHERE INSTR(?, DEF_ID_) > 0";
/* 105 */     data = this.jdbcTemplate.queryForList(sql, new Object[] { defIds });
/* 106 */     outputStream = new FileOutputStream(filePath + "/BUS_PERMISSION.txt");
/* 107 */     outputStream.write(JSON.toJSONString(data, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/* 108 */     outputStream.close();
/*     */   } @Resource
/*     */   private BpmDefinitionManager bpmDefinitionManager; @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Transactional
/*     */   public void bpmImport(String filePath) throws Exception {
/* 114 */     DBImportUtil.importTable(filePath, "ACT_RE_DEPLOYMENT");
/* 115 */     DBImportUtil.importTable(filePath, "BPM_DEFINITION");
/* 116 */     DBImportUtil.importTable(filePath, "ACT_GE_BYTEARRAY");
/* 117 */     DBImportUtil.importTable(filePath, "ACT_RE_MODEL");
/* 118 */     DBImportUtil.importTable(filePath, "ACT_RE_PROCDEF");
/* 119 */     DBImportUtil.cleanTable(filePath, "BUS_PERMISSION", "def_id_");
/* 120 */     DBImportUtil.importTable(filePath, "BUS_PERMISSION");
/* 121 */     updateCatch(filePath);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String checkImport(String filePath) throws Exception {
/* 130 */     StringBuffer returnStr = (new StringBuffer(DBImportUtil.checkTable(filePath, "BPM_DEFINITION", "ID_", "NAME_"))).append("\n").append(DBImportUtil.checkTable(filePath, "ACT_RE_MODEL", "ID_", "NAME_")).append("\n").append(DBImportUtil.checkTable(filePath, "ACT_GE_BYTEARRAY", "ID_", "ID_")).append("\n").append(DBImportUtil.checkTable(filePath, "ACT_RE_PROCDEF", "ID_", "NAME_")).append("\n").append(DBImportUtil.checkTable(filePath, "ACT_RE_DEPLOYMENT", "ID_", "NAME_"));
/* 131 */     return returnStr.toString();
/*     */   }
/*     */   
/*     */   private void updateCatch(String filePath) throws Exception {
/* 135 */     File file = new File(filePath + "/BPM_DEFINITION.txt");
/* 136 */     if (!file.exists()) {
/*     */       return;
/*     */     }
/* 139 */     BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
/* 140 */     StringBuffer buffer = new StringBuffer();
/* 141 */     String line = "";
/* 142 */     while ((line = in.readLine()) != null) {
/* 143 */       buffer.append(line);
/*     */     }
/* 145 */     in.close();
/* 146 */     String bpmDef = buffer.toString();
/* 147 */     JSONArray jsonArray = JSON.parseArray(bpmDef);
/* 148 */     List<Map> params = jsonArray.toJavaList(Map.class);
/* 149 */     params.forEach(param -> {
/*     */           BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get((String)param.get("id_"));
/*     */           ActivitiDefCache.clearByDefId(bpmDefinition.getActDefId());
/*     */           this.bpmProcessDefService.clean(bpmDefinition.getId());
/*     */           this.bpmProcessDefService.getBpmProcessDef(bpmDefinition.getId());
/*     */           AppUtil.publishEvent((ApplicationEvent)new BpmDefinitionUpdateEvent((IBpmDefinition)bpmDefinition));
/*     */         });
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/eximport/BpmDefinitionExpImport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */