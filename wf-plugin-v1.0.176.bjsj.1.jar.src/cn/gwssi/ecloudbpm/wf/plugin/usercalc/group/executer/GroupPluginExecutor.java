/*    */ package com.dstz.bpm.plugin.usercalc.group.executer;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.plugin.usercalc.group.def.GroupPluginDef;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.org.api.model.IGroup;
/*    */ import com.dstz.org.api.service.GroupService;
/*    */ import com.dstz.sys.api.model.DefaultIdentity;
/*    */ import com.dstz.sys.api.model.SysIdentity;
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