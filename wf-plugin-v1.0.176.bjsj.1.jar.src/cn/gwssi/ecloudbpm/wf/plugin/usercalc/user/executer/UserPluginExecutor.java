/*    */ package cn.gwssi.ecloudbpm.wf.plugin.usercalc.user.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.context.BpmContext;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*    */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*    */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmTaskOpinionManager;
/*    */ import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.user.def.UserPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*    */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*    */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class UserPluginExecutor
/*    */   extends AbstractUserCalcPlugin<UserPluginDef>
/*    */ {
/*    */   @Resource
/*    */   BpmTaskOpinionManager taskOpinionManager;
/*    */   @Resource
/*    */   private UserService userService;
/*    */   @Resource
/*    */   BpmProcessDefService bpmProcessDefService;
/*    */   
/*    */   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, UserPluginDef def) {
/* 41 */     List<SysIdentity> list = new ArrayList<>();
/*    */     
/* 43 */     String source = def.getSource();
/* 44 */     String actionName = (String)pluginSession.get("actionName");
/* 45 */     if (StringUtil.isNotEmpty(actionName) && StringUtils.equals(actionName, "start") && StringUtils.equals(source, "start")) {
/* 46 */       source = "currentUser";
/*    */     }
/* 48 */     if ("start".equals(source)) {
/*    */       
/* 50 */       IBpmInstance instance = pluginSession.getBpmInstance();
/* 51 */       BpmProcessDef processDef = this.bpmProcessDefService.getBpmProcessDef(instance.getDefId());
/* 52 */       String startNodeId = processDef.getStartEvent().getNodeId();
/*    */       
/* 54 */       List<BpmTaskOpinion> opinions = this.taskOpinionManager.getByInstAndNode(instance.getId(), startNodeId);
/* 55 */       if (CollectionUtil.isEmpty(opinions)) {
/* 56 */         if (StringUtil.isEmpty(instance.getCreateBy())) {
/* 57 */           throw new BusinessException("流程数据异常，无法获取发起人！", BpmStatusCode.USER_CALC_ERROR);
/*    */         }
/* 59 */         DefaultIdentity defaultIdentity = new DefaultIdentity(instance.getCreateBy(), instance.getCreator(), "user", instance.getCreateOrgId());
/* 60 */         list.add(defaultIdentity);
/*    */       } else {
/* 62 */         BpmTaskOpinion firstNode = opinions.get(0);
/* 63 */         DefaultIdentity defaultIdentity = new DefaultIdentity(firstNode.getApprover(), firstNode.getApproverName(), "user", firstNode.getTaskOrgId());
/* 64 */         list.add(defaultIdentity);
/*    */       } 
/*    */     } 
/* 67 */     if ("currentUser".equals(source)) {
/* 68 */       DefaultIdentity defaultIdentity = new DefaultIdentity(ContextUtil.getCurrentUser());
/* 69 */       defaultIdentity.setOrgId(((BaseActionCmd)BpmContext.getActionModel()).getApproveOrgId());
/* 70 */       list.add(defaultIdentity);
/* 71 */     } else if ("spec".equals(source)) {
/* 72 */       String userKeys = def.getAccount();
/* 73 */       String[] aryInfo = userKeys.split(",");
/* 74 */       for (String info : aryInfo) {
/* 75 */         String[] userInfo = info.split("-");
/* 76 */         IUser user = this.userService.getUserByAccount(userInfo[0]);
/* 77 */         if (user == null) {
/* 78 */           throw new BusinessException(userInfo[0] + "用户丢失", BpmStatusCode.USER_CALC_ERROR);
/*    */         }
/* 80 */         DefaultIdentity defaultIdentity = new DefaultIdentity(user);
/* 81 */         defaultIdentity.setOrgId(userInfo[1]);
/* 82 */         list.add(defaultIdentity);
/*    */       } 
/*    */     } 
/* 85 */     return list;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/user/executer/UserPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */