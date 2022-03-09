/*    */ package cn.gwssi.ecloudbpm.wf.engine.listener;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.act.listener.ActEventListener;
/*    */ import cn.gwssi.ecloudbpm.wf.api.constant.ActionType;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefualtTaskActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.data.handle.IBpmBusDataHandle;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.hutool.core.lang.Assert;
/*    */ import javax.annotation.Resource;
/*    */ import org.activiti.engine.delegate.event.ActivitiEvent;
/*    */ import org.activiti.engine.delegate.event.impl.ActivitiEntityEventImpl;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ @Component
/*    */ public class AutomaticTaskListener
/*    */   implements ActEventListener
/*    */ {
/*    */   @Resource
/*    */   private BpmInstanceManager bpmInstanceMananger;
/*    */   @Autowired
/*    */   IBpmBusDataHandle bpmBusDataHandle;
/*    */   @Autowired
/*    */   BpmProcessDefService bpmProcessDefService;
/*    */   
/*    */   public void notify(ActivitiEvent event) {
/* 42 */     ActivitiEntityEventImpl activitEvent = (ActivitiEntityEventImpl)event;
/* 43 */     if (StringUtil.isNotEmpty(activitEvent.getProcessInstanceId())) {
/* 44 */       BpmInstance instance = this.bpmInstanceMananger.getByActInstId(activitEvent.getProcessInstanceId());
/* 45 */       Assert.notNull(instance, "TIMER_FIRED Event 流程实例丢失! Activiti ProcessInstanceId %s", new Object[] { activitEvent.getProcessInstanceId() });
/*    */       
/* 47 */       DefualtTaskActionCmd taskComplateCmd = new DefualtTaskActionCmd();
/* 48 */       taskComplateCmd.setActionName(ActionType.TASKCANCELLED.getKey());
/*    */       
/* 50 */       taskComplateCmd.setBpmInstance((IBpmInstance)instance);
/* 51 */       taskComplateCmd.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(instance.getDefId()));
/*    */       
/* 53 */       taskComplateCmd.setBizDataMap(this.bpmBusDataHandle.getInstanceBusData(instance.getId(), null));
/*    */       
/* 55 */       taskComplateCmd.setOpinion("任务进入定时事件");
/*    */ 
/*    */       
/* 58 */       BpmContext.cleanTread();
/* 59 */       BpmContext.setActionModel((ActionCmd)taskComplateCmd);
/*    */     } 
/*    */   }
/*    */   
/*    */   public void systemMessage(ActionCmd cmd) {}
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/AutomaticTaskListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */