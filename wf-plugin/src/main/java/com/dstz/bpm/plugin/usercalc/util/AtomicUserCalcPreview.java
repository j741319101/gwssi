/*    */ package com.dstz.bpm.plugin.usercalc.util;
/*    */ 
/*    */ import com.dstz.base.core.util.AppUtil;
/*    */ import com.dstz.bpm.api.engine.constant.LogicType;
/*    */ import com.dstz.bpm.plugin.node.userassign.executer.UserAssignRuleCalc;
/*    */ import com.dstz.org.api.model.IUser;
/*    */ import com.dstz.org.api.service.UserService;
/*    */ import com.dstz.sys.api.model.DefaultIdentity;
/*    */ import com.dstz.sys.api.model.SysIdentity;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AtomicUserCalcPreview
/*    */ {
/*    */   public static List<SysIdentity> calcUserPreview(List<Object[]> sysIdentitieAndLogic) {
/* 21 */     UserService userService = AppUtil.getImplInstanceArray(UserService.class).get(0);
/* 22 */     List<SysIdentity> sysIdentities = new ArrayList<>();
/* 23 */     sysIdentitieAndLogic.forEach(identitieAndLogic -> {
/*    */           SysIdentity sysIdentity = (SysIdentity)identitieAndLogic[0];
/*    */           String logicType = (String)identitieAndLogic[1];
/*    */           if (StringUtils.equals(sysIdentity.getType(), "user")) {
/*    */             List<SysIdentity> sysIdentities1 = new ArrayList<>();
/*    */             sysIdentities1.add(sysIdentity);
/*    */             UserAssignRuleCalc.calc(sysIdentities, sysIdentities1, LogicType.fromKey(logicType));
/*    */           } else {
/*    */             List<IUser> users = userService.getUserListByGroup(sysIdentity.getType(), sysIdentity.getId());
/*    */             List<SysIdentity> identities = new ArrayList<>();
/*    */             if (CollectionUtil.isNotEmpty(users)) {
/*    */               users.forEach(());
/*    */               UserAssignRuleCalc.calc(sysIdentities, identities, LogicType.fromKey(logicType));
/*    */             } 
/*    */           } 
/*    */         });
/* 39 */     return sysIdentities;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin/0.2-SNAPSHOT/wf-plugin-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/usercalc/util/AtomicUserCalcPreview.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */