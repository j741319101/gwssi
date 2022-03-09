/*    */ package cn.gwssi.ecloudbpm.wf.engine.listener;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.act.listener.ActEventListener;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import org.activiti.engine.delegate.event.ActivitiEvent;
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
/*    */ public class JobExecutedListener
/*    */   implements ActEventListener
/*    */ {
/*    */   public void notify(ActivitiEvent event) {
/* 21 */     BpmContext.cleanTread();
/*    */   }
/*    */   
/*    */   public void systemMessage(ActionCmd cmd) {}
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/listener/JobExecutedListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */