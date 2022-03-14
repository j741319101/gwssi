/*    */ package com.dstz.bpm.engine.action.helper;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*    */ import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
/*    */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.util.concurrent.CountDownLatch;
/*    */ import java.util.concurrent.ExecutorService;
/*    */ import java.util.concurrent.Executors;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class NewThreadActionUtil
/*    */ {
/* 23 */   protected static Logger LOG = LoggerFactory.getLogger(NewThreadActionUtil.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void newThreadDoAction(ActionCmd newFlowCmd, IUser startUser) {
/* 31 */     StartNewFlow triggerNewFlow = (StartNewFlow)AppUtil.getBean(StartNewFlow.class);
/* 32 */     ExecutorService executor = Executors.newCachedThreadPool();
/*    */     
/*    */     try {
/* 35 */       CountDownLatch latch = new CountDownLatch(1);
/* 36 */       triggerNewFlow.setLatch(latch);
/* 37 */       triggerNewFlow.setUser(startUser);
/* 38 */       triggerNewFlow.setActionCmd(newFlowCmd);
/*    */       
/* 40 */       triggerNewFlow.setTransactionResource(TransactionSynchronizationManager.getResourceMap());
/*    */       
/* 42 */       executor.execute(triggerNewFlow);
/*    */       
/* 44 */       latch.await();
/*    */       
/* 46 */       if (triggerNewFlow.getException() != null) {
/* 47 */         throw new RuntimeException(triggerNewFlow.getException());
/*    */       }
/* 49 */     } catch (Exception e) {
/* 50 */       e.printStackTrace();
/* 51 */       throw new BusinessException("触发新流程失败 ！ ：" + e.getMessage());
/*    */     } finally {
/* 53 */       executor.shutdown();
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void testStartScript() {
/* 61 */     String jsonData = "{\"Demo\":{\"bmId\":\"20000000280001\",\"bm\":\"科技部\",\"zd1\":\"JS初始化\",\"DemoSubList\":[{\"ms\":\"请开启控制台，或者查看表单源码，来查看我是如何初始化的\"}],\"mz\":\"测试新线程处理任务！\"}}";
/* 62 */     FlowRequestParam param = new FlowRequestParam("404120998984024065", "start", JSON.parseObject(jsonData));
/* 63 */     DefaultInstanceActionCmd defaultInstanceActionCmd = new DefaultInstanceActionCmd(param);
/*    */     
/* 65 */     newThreadDoAction((ActionCmd)defaultInstanceActionCmd, ContextUtil.getCurrentUser());
/* 66 */     LOG.debug("启动成功！");
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/helper/NewThreadActionUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */