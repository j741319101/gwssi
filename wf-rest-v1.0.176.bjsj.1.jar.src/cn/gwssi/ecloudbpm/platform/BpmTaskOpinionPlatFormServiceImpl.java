/*    */ package cn.gwssi.ecloudbpm.platform;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTaskOpinion;
/*    */ import cn.gwssi.ecloudbpm.wf.api.platform.IBpmTaskOpinionPlatFormService;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.vo.BpmTaskOpinionVO;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class BpmTaskOpinionPlatFormServiceImpl
/*    */   implements IBpmTaskOpinionPlatFormService
/*    */ {
/*    */   @Resource
/*    */   private BpmTaskOpinionManager bpmTaskOpinionManager;
/*    */   
/*    */   public IBpmTaskOpinion getBpmTaskOpinion(String id) {
/* 19 */     return (IBpmTaskOpinion)this.bpmTaskOpinionManager.get(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<BpmTaskOpinionVO> getInstanceOpinion(String instId, String taskId, String orgId, String status, Boolean extend) {
/* 24 */     return this.bpmTaskOpinionManager.getByInstsOpinion(instId, taskId, orgId, status, extend);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/platform/BpmTaskOpinionPlatFormServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */