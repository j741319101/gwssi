/*    */ package cn.gwssi.ecloudbpm.wf.plugin.usercalc.util;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.constant.LogicType;
/*    */ import cn.gwssi.ecloudbpm.wf.plugin.node.userassign.executer.UserAssignRuleCalc;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*    */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ 
/*    */ public class AtomicUserCalcPreview
/*    */ {
/*    */   public static List<SysIdentity> calcUserPreview(List<Object[]> sysIdentitieAndLogic) {
/* 18 */     UserService userService = AppUtil.getImplInstanceArray(UserService.class).get(0);
/* 19 */     List<SysIdentity> sysIdentities = new ArrayList<>();
/* 20 */     sysIdentitieAndLogic.forEach(identitieAndLogic -> {
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
/*    */             }
/*    */             UserAssignRuleCalc.calc(sysIdentities, identities, LogicType.fromKey(logicType));
/*    */           } 
/*    */         });
/* 36 */     return sysIdentities;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/usercalc/util/AtomicUserCalcPreview.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */