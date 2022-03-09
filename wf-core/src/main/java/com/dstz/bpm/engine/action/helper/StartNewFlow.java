/*     */ package com.dstz.bpm.engine.action.helper;
/*     */ 
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import org.apache.commons.collections.MapUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.context.annotation.Scope;
/*     */ import org.springframework.stereotype.Component;
/*     */ import org.springframework.transaction.support.TransactionSynchronizationManager;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ @Scope("prototype")
/*     */ public class StartNewFlow
/*     */   implements Runnable
/*     */ {
/*  28 */   private static Logger logger = LoggerFactory.getLogger(StartNewFlow.class);
/*  29 */   private static String startingNewFlow = "startingNewFlow";
/*     */ 
/*     */   
/*     */   private CountDownLatch latch;
/*     */ 
/*     */   
/*     */   private Exception exception;
/*     */ 
/*     */   
/*     */   ActionCmd actionCmd;
/*     */ 
/*     */   
/*     */   IUser user;
/*     */   
/*     */   private Map<Object, Object> transactionResource;
/*     */ 
/*     */   
/*     */   public void run() {
/*  47 */     setCurrentTransactionResource();
/*  48 */     if (this.actionCmd == null || this.user == null) throw new BusinessException("启动新流程失败！ new flow Cmd or starUser cannot be null"); 
/*     */     try {
/*  50 */       ContextUtil.setCurrentUser(this.user);
/*     */       
/*  52 */       this.actionCmd.executeCmd();
/*     */       
/*  54 */       logger.debug("新流程启动成功！ ");
/*  55 */       this.latch.countDown();
/*  56 */     } catch (Exception e) {
/*  57 */       this.exception = e;
/*  58 */       this.latch.countDown();
/*  59 */       logger.error("启动新流程流程失败！" + e.getMessage());
/*  60 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isInStartNewFlow() {
/*  71 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public CountDownLatch getLatch() {
/*  76 */     return this.latch;
/*     */   }
/*     */   
/*     */   public void setLatch(CountDownLatch latch) {
/*  80 */     this.latch = latch;
/*     */   }
/*     */ 
/*     */   
/*     */   public Exception getException() {
/*  85 */     return this.exception;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setException(Exception exception) {
/*  91 */     this.exception = exception;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setUser(IUser user) {
/*  98 */     this.user = user;
/*     */   }
/*     */ 
/*     */   
/*     */   public ActionCmd getActionCmd() {
/* 103 */     return this.actionCmd;
/*     */   }
/*     */   
/*     */   public void setActionCmd(ActionCmd actionCmd) {
/* 107 */     this.actionCmd = actionCmd;
/*     */   }
/*     */   
/*     */   private void setCurrentTransactionResource() {
/* 111 */     if (MapUtils.isEmpty(this.transactionResource)) {
/*     */       return;
/*     */     }
/*     */     
/* 115 */     for (Map.Entry<Object, Object> entry : this.transactionResource.entrySet()) {
/* 116 */       TransactionSynchronizationManager.bindResource(entry.getKey(), entry.getValue());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void setTransactionResource(Map<Object, Object> transactionResource) {
/* 122 */     this.transactionResource = transactionResource;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/action/helper/StartNewFlow.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */