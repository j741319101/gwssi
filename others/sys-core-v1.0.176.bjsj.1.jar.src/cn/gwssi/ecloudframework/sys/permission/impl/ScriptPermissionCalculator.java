/*    */ package cn.gwssi.ecloudframework.sys.permission.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*    */ import cn.gwssi.ecloudframework.sys.api.permission.IPermissionCalculator;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class ScriptPermissionCalculator
/*    */   implements IPermissionCalculator
/*    */ {
/*    */   @Resource
/*    */   IGroovyScriptEngine groovyScriptEngine;
/*    */   
/*    */   public String getTitle() {
/* 27 */     return "脚本";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getType() {
/* 32 */     return "script";
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean haveRights(JSONObject json) {
/* 37 */     String script = json.getString("id");
/* 38 */     return this.groovyScriptEngine.executeBoolean(script, null);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/permission/impl/ScriptPermissionCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */