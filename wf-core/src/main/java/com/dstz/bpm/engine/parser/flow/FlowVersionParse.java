/*     */ package com.dstz.bpm.engine.parser.flow;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.plugin.def.BpmDef;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmVariableDef;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.hutool.core.date.DateTime;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */
import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class FlowVersionParse
/*     */   extends AbsFlowParse<DefaultBpmVariableDef>
/*     */ {
/*     */   @Resource
/*     */   IGroovyScriptEngine groovyScriptEngine;
/*     */   private static boolean hasInit = false;
/*  26 */   private static String v = "b";
/*     */   
/*     */   public void parse(DefaultBpmProcessDef def, JSONObject flowConf) {
/*  29 */     if (hasInit) {
/*  30 */       flowConf.put("v", v);
/*  31 */       if (v.equals("b")) {
/*  32 */         removeUperFunctions(flowConf);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/*  37 */     sign(v);
/*  38 */     v = "ecloudbpm";
/*  39 */     if (v.equals("b"));
/*     */ 
/*     */ 
/*     */     
/*  43 */     hasInit = true;
/*  44 */     flowConf.put("v", v);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  50 */   String getPropStr = "import com.dstz.base.core.util.AppUtil; "
/*  51 */     .concat("return AppUtil.getBean(\"sysPropertiesManager\")")
/*  52 */     .concat(".getByAlias(\"s").concat(".").concat("k\");");
/*  53 */   String encryptStr = "import com.dstz.base.cor".concat("e.enc").concat("rypt.Encr").concat("yptUtil; ")
/*  54 */     .concat("return Encr").concat("ypt").concat("Util.aes").concat("Decry").concat("pt(theKey,").concat("\"Not afraid of infringement you will be free\")");
/*     */   private String analysisKey(String key) {
/*     */     try {
/*  57 */       Map<String, Object> param = new HashMap<>(1, 1.0F);
/*     */       
/*  59 */       String theKey = this.groovyScriptEngine.executeString(this.getPropStr, null).replaceFirst(key, "");
/*  60 */       param.put("theKey", theKey);
/*  61 */       String str = this.groovyScriptEngine.executeString(this.encryptStr, param);
/*     */       
/*  63 */       if (StringUtil.isEmpty(str)) {
/*  64 */         return v;
/*     */       }
/*  66 */       String[] msg = str.split("_");
/*  67 */       if (msg.length != 3) {
/*  68 */         return v;
/*     */       }
/*  70 */       DateTime dateTime = DateUtil.parse(msg[2]);
/*  71 */       if (dateTime.before(new Date())) {
/*  72 */         return v;
/*     */       }
/*     */       
/*  75 */       return msg[0];
/*     */     }
/*  77 */     catch (Exception e) {
/*  78 */       return v;
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void removeUperFunctions(JSONObject flowConf) {}
/*     */ 
/*     */ 
/*     */   
/*     */   private void sign(String string) {
/*  90 */     (new Thread(new Runnable()
/*     */         {
/*     */ 
/*     */           
/*     */           public void run() {}
/*  95 */         })).run();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/* 101 */     return null;
/*     */   }
/*     */   
/*     */   public void setDefParam(DefaultBpmProcessDef bpmdef, Object object) {}
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/FlowVersionParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */