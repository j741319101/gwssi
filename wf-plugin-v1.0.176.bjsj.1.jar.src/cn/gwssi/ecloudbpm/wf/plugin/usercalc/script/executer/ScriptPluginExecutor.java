/*    */ package cn.gwssi.ecloudbpm.wf.plugin.usercalc.script.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.script.def.ScriptPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.api.constant.BaseStatusCode;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.Set;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ScriptPluginExecutor
/*    */   extends AbstractUserCalcPlugin<ScriptPluginDef>
/*    */ {
/*    */   @Resource
/*    */   IGroovyScriptEngine groovyScriptEngine;
/*    */   
/*    */   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, ScriptPluginDef def) {
/* 36 */     String script = def.getScript();
/* 37 */     if (StringUtil.isEmpty(script)) {
/* 38 */       return Collections.EMPTY_LIST;
/*    */     }
/* 40 */     List<SysIdentity> list = new ArrayList<>();
/*    */     try {
/* 42 */       Set<SysIdentity> set = (Set<SysIdentity>)this.groovyScriptEngine.executeObject(script, (Map)pluginSession);
/* 43 */       if (CollectionUtil.isEmpty(set)) return list; 
/* 44 */       list.addAll(set);
/* 45 */     } catch (Exception e) {
/* 46 */       throw new BusinessException(script + " 脚本执行错误: " + e.getMessage(), BaseStatusCode.SYSTEM_ERROR);
/*    */     } 
/* 48 */     return list;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean supportPreView() {
/* 53 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/script/executer/ScriptPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */