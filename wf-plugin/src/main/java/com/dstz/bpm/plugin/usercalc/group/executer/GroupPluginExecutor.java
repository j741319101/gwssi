/*    */ package com.dstz.bpm.plugin.usercalc.group.executer;
/*    */ 
/*    */ import com.dstz.base.core.util.StringUtil;
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmUserCalcPluginDef;
/*    */ import com.dstz.bpm.engine.plugin.runtime.abstact.AbstractUserCalcPlugin;
/*    */ import com.dstz.bpm.engine.plugin.session.BpmUserCalcPluginSession;
/*    */ import com.dstz.bpm.plugin.usercalc.group.def.GroupPluginDef;
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
/*    */ 
/*    */ @Component
/*    */ public class GroupPluginExecutor
/*    */   extends AbstractUserCalcPlugin<GroupPluginDef>
/*    */ {
/*    */   @Resource
/*    */   GroupService userGroupService;
/*    */   
/*    */   public List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession pluginSession, GroupPluginDef def) {
/* 28 */     if (StringUtil.isEmpty(def.getGroupKey())) return null; 
/* 29 */     String groupType = def.getType();
/*    */     
/* 31 */     List<SysIdentity> identityList = new ArrayList<>();
/* 32 */     for (String key : def.getGroupKey().split(",")) {
/* 33 */       if (!StringUtil.isEmpty(key)) {
/* 34 */         IGroup group = this.userGroupService.getByCode(groupType, key);
/* 35 */         if (group != null) {
/* 36 */           DefaultIdentity identity = new DefaultIdentity(group.getGroupId(), group.getGroupName(), group.getGroupType());
/* 37 */           if (group.getSn() != null) {
/* 38 */             identity.setSn(group.getSn());
/*    */           }
/* 40 */           identityList.add(identity);
/*    */         } 
/*    */       } 
/* 43 */     }  return identityList;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/usercalc/group/executer/GroupPluginExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */