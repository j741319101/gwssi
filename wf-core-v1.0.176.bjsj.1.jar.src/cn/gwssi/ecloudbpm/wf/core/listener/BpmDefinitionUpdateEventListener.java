/*    */ package cn.gwssi.ecloudbpm.wf.core.listener;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.act.cache.ActivitiDefCache;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.event.BpmDefinitionUpdateEvent;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.context.ApplicationEvent;
/*    */ import org.springframework.context.ApplicationListener;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class BpmDefinitionUpdateEventListener
/*    */   implements ApplicationListener<BpmDefinitionUpdateEvent> {
/*    */   @Resource
/*    */   BpmProcessDefService bpmProcessDefService;
/*    */   
/*    */   public void onApplicationEvent(BpmDefinitionUpdateEvent event) {
/* 20 */     BpmDefinition bpmDef = (BpmDefinition)event.getSource();
/* 21 */     if (StringUtil.isEmpty(bpmDef.getActDefId()))
/* 22 */       return;  this.bpmProcessDefService.clean(bpmDef.getId());
/* 23 */     ActivitiDefCache.clearByDefId(bpmDef.getActDefId());
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/listener/BpmDefinitionUpdateEventListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */