/*    */ package cn.gwssi.ecloudbpm.wf.engine.util;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*    */ import java.lang.reflect.Method;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class HandlerUtil
/*    */ {
/* 17 */   protected static Logger LOG = LoggerFactory.getLogger(HandlerUtil.class);
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
/*    */   public static void invokeHandler(ActionCmd actionModel, String handler) throws Exception {
/* 29 */     if (StringUtil.isEmpty(handler)) {
/*    */       return;
/*    */     }
/* 32 */     if (handler.indexOf("(") != -1) {
/* 33 */       invokeGroovyHandler(handler);
/*    */       
/*    */       return;
/*    */     } 
/*    */     
/* 38 */     String[] aryHandler = handler.split("[.]");
/* 39 */     if (aryHandler == null || aryHandler.length != 2)
/* 40 */       return;  String beanId = aryHandler[0];
/* 41 */     String method = aryHandler[1];
/*    */     
/* 43 */     Object serviceBean = AppUtil.getBean(beanId);
/*    */     
/* 45 */     if (serviceBean == null)
/*    */       return; 
/* 47 */     Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[] { ActionCmd.class });
/* 48 */     invokeMethod.invoke(serviceBean, new Object[] { actionModel });
/*    */   }
/*    */ 
/*    */   
/*    */   private static void invokeGroovyHandler(String script) {
/* 53 */     IGroovyScriptEngine groovyEngine = (IGroovyScriptEngine)AppUtil.getBean(IGroovyScriptEngine.class);
/* 54 */     groovyEngine.execute(script);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/util/HandlerUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */