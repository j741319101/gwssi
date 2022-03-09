/*    */ package com.dstz.bpm.plugin.usercalc.user.executer;
/*    */ 
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.bpm.api.exception.BpmStatusCode;
/*    */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*    */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*    */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*    */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*    */ import com.dstz.bpm.core.model.BpmTaskOpinion;
/*    */ import com.dstz.bpm.engine.model.BpmIdentity;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.plugin.usercalc.user.def.UserPluginDef;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.org.api.service.UserService;
/*    */ import com.dstz.sys.api.model.SysIdentity;
/*    */ import com.dstz.sys.util.ContextUtil;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class UserPluginExecutor
/*    */   extends AbstractUserCalcPlugin<UserPluginDef>
/*    */ {
/*    */   @Resource
/*    */   BpmTaskOpinionManager taskOpinionManager;
/*    */   @Resource
/*    */   UserService UserService;
/*    */   @Resource
/*    */   BpmProcessDefService bpmProcessDefService;
/*    */   
/*    */   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, UserPluginDef def) {
/* 43 */     List<SysIdentity> list = new ArrayList<>();
/*    */     
/* 45 */     String source = def.getSource();
/* 46 */     String actionName = (String)pluginSession.get("actionName");
/* 47 */     if (StringUtil.isNotEmpty(actionName) && StringUtils.equals(actionName, "start") && StringUtils.equals(source, "start")) {
/* 48 */       source = "currentUser";
/*    */     }
/* 50 */     if ("start".equals(source)) {
/*    */       
/* 52 */       IBpmInstance instance = pluginSession.getBpmInstance();
/* 53 */       BpmProcessDef processDef = this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/* 54 */       String startNodeId = processDef.getStartEvent().getNodeId();
/*    */       
/* 56 */       List<BpmTaskOpinion> opinions = this.taskOpinionManager.getByInstAndNode(instance.getId(), startNodeId);
/* 57 */       if (CollectionUtil.isEmpty(opinions)) {
/* 58 */         if (StringUtil.isEmpty(instance.getCreateBy())) {
/* 59 */           throw new BusinessException("流程数据异常，无法获取发起人！", BpmStatusCode.USER_CALC_ERROR);
/*    */         }
/* 61 */         BpmIdentity bpmIdentity = new BpmIdentity(instance.getCreateBy(), instance.getCreator(), "user");
/* 62 */         list.add(bpmIdentity);
/*    */       } else {
/* 64 */         BpmTaskOpinion firstNode = opinions.get(0);
/*    */         
/* 66 */         BpmIdentity bpmIdentity = new BpmIdentity(firstNode.getApprover(), firstNode.getApproverName(), "user");
/* 67 */         list.add(bpmIdentity);
/*    */       } 
/*    */     } 
/* 70 */     if ("currentUser".equals(source)) {
/* 71 */       BpmIdentity bpmIdentity = new BpmIdentity(ContextUtil.getCurrentUser());
/* 72 */       list.add(bpmIdentity);
/* 73 */     } else if ("spec".equals(source)) {
/* 74 */       String userKeys = def.getAccount();
/* 75 */       String[] aryAccount = userKeys.split(",");
/* 76 */       for (String account : aryAccount) {
/* 77 */         IUser user = this.UserService.getUserByAccount(account);
/* 78 */         if (user == null) {
/* 79 */           throw new BusinessException(account + "用户丢失", BpmStatusCode.USER_CALC_ERROR);
/*    */         }
/*    */         
/* 82 */         BpmIdentity bpmIdentity = new BpmIdentity(user);
/* 83 */         list.add(bpmIdentity);
/*    */       } 
/*    */     } 
/* 86 */     return list;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/usercalc/user/executer/UserPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */