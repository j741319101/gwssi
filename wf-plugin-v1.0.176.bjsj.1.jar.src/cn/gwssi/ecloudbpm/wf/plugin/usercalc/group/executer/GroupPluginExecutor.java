/*    */ package cn.gwssi.ecloudbpm.wf.plugin.usercalc.group.executer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.usercalc.group.def.GroupPluginDef;
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*    */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class GroupPluginExecutor
/*    */   extends AbstractUserCalcPlugin<GroupPluginDef>
/*    */ {
/*    */   @Resource
/*    */   GroupService userGroupService;
/*    */   
/*    */   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, GroupPluginDef def) {
/* 28 */     if (StringUtil.isEmpty(def.getGroupKey())) {
/* 29 */       throw new BusinessException("流程定义候选人配置错误：" + def.getGroupName());
/*    */     }
/* 31 */     String groupType = def.getType();
/*    */     
/* 33 */     List<SysIdentity> identityList = new ArrayList<>();
/* 34 */     for (String key : def.getGroupKey().split(",")) {
/* 35 */       if (!StringUtil.isEmpty(key)) {
/* 36 */         IGroup group = this.userGroupService.getByCode(groupType, key);
/* 37 */         if (group != null) {
/* 38 */           DefaultIdentity identity = new DefaultIdentity(group.getGroupId(), group.getGroupName(), group.getGroupType(), group.getGroupId());
/* 39 */           if (group.getSn() != null) {
/* 40 */             identity.setSn(group.getSn());
/*    */           }
/* 42 */           identityList.add(identity);
/*    */         } 
/*    */       } 
/* 45 */     }  return identityList;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/group/executer/GroupPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */