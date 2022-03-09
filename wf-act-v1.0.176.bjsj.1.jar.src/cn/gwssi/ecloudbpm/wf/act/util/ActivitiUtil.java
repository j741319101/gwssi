/*     */ package cn.gwssi.ecloudbpm.wf.act.util;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.util.ObjectUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.activiti.engine.impl.RepositoryServiceImpl;
/*     */ import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
/*     */ import org.activiti.engine.impl.pvm.PvmActivity;
/*     */ import org.activiti.engine.impl.pvm.PvmTransition;
/*     */ import org.activiti.engine.impl.pvm.process.ActivityImpl;
/*     */ import org.activiti.engine.impl.pvm.process.TransitionImpl;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ActivitiUtil
/*     */ {
/*     */   public static Map<String, Object> skipPrepare(String actDefId, String nodeId, String[] aryDestination) {
/*  35 */     Map<String, Object> map = new HashMap<>();
/*     */     
/*  37 */     RepositoryService repositoryService = (RepositoryService)AppUtil.getBean(RepositoryService.class);
/*     */ 
/*     */     
/*  40 */     ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity)((RepositoryServiceImpl)repositoryService).getDeployedProcessDefinition(actDefId);
/*     */     
/*  42 */     ActivityImpl curAct = processDefinition.findActivity(nodeId);
/*  43 */     List<PvmTransition> outTrans = curAct.getOutgoingTransitions();
/*  44 */     List<PvmTransition> cloneOutTrans = null;
/*     */     try {
/*  46 */       cloneOutTrans = (List<PvmTransition>)ObjectUtil.cloneByStream(outTrans);
/*  47 */       map.put("outTrans", cloneOutTrans);
/*     */       
/*  49 */       if (aryDestination.length > 1 && outTrans.size() == 1) {
/*  50 */         BpmContext.setThreadMulSequenceLine(((PvmTransition)outTrans.get(0)).getId());
/*     */       }
/*  52 */     } catch (Exception ex) {
/*  53 */       ex.printStackTrace();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  60 */     for (Iterator<PvmTransition> it = outTrans.iterator(); it.hasNext(); ) {
/*  61 */       PvmTransition transition = it.next();
/*  62 */       PvmActivity activity = transition.getDestination();
/*  63 */       List<PvmTransition> inTrans = activity.getIncomingTransitions();
/*  64 */       for (Iterator<PvmTransition> itIn = inTrans.iterator(); itIn.hasNext(); ) {
/*  65 */         PvmTransition inTransition = itIn.next();
/*  66 */         if (inTransition.getSource().getId().equals(curAct.getId())) {
/*  67 */           itIn.remove();
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/*  73 */     curAct.getOutgoingTransitions().clear();
/*     */     
/*  75 */     if (aryDestination != null && aryDestination.length > 0) {
/*  76 */       for (String dest : aryDestination) {
/*     */         
/*  78 */         ActivityImpl destAct = processDefinition.findActivity(dest);
/*  79 */         if (destAct == null) {
/*  80 */           throw new BusinessException(BpmStatusCode.BPM_SKIP_TARGET_NODE_LOSE);
/*     */         }
/*  82 */         TransitionImpl transitionImpl = curAct.createOutgoingTransition();
/*  83 */         transitionImpl.setDestination(destAct);
/*     */       } 
/*     */     }
/*     */     
/*  87 */     map.put("activity", curAct);
/*  88 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String handleOutgoinSequenceFlow(ActivityImpl curAct, List<PvmTransition> oldOutTrans, ActivityImpl destAct) {
/* 101 */     if (CollectionUtil.isEmpty(oldOutTrans)) return null;
/*     */ 
/*     */     
/* 104 */     for (PvmTransition outGoing : oldOutTrans) {
/* 105 */       if (outGoing.getDestination().getId().equals(destAct.getId())) {
/* 106 */         for (PvmTransition currentOutGoing : curAct.getOutgoingTransitions()) {
/* 107 */           if (currentOutGoing.getId().equals(outGoing.getId())) {
/* 108 */             return null;
/*     */           }
/*     */         } 
/* 111 */         return outGoing.getId();
/*     */       } 
/*     */     } 
/* 114 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void restoreActivity(Map<String, Object> map) {
/* 123 */     ActivityImpl curAct = (ActivityImpl)map.get("activity");
/* 124 */     List<PvmTransition> outTrans = (List<PvmTransition>)map.get("outTrans");
/* 125 */     curAct.getOutgoingTransitions().clear();
/* 126 */     curAct.getOutgoingTransitions().addAll(outTrans);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/util/ActivitiUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */