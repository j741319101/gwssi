/*    */ package cn.gwssi.ecloudframework.sys.permission.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.base.core.util.ThreadMapUtil;
/*    */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*    */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*    */ import cn.gwssi.ecloudframework.sys.api.permission.IPermissionCalculator;
/*    */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class GroupPermissionCalculator
/*    */   implements IPermissionCalculator
/*    */ {
/* 27 */   private static String threadMapKey = "cn.gwssi.ecloudframework.sys.permission.impl.GroupPermission";
/*    */ 
/*    */   
/*    */   public boolean haveRights(JSONObject json) {
/* 31 */     Map<String, List<? extends IGroup>> allGroup = (Map<String, List<? extends IGroup>>)ThreadMapUtil.get(threadMapKey);
/* 32 */     if (allGroup == null) {
/* 33 */       GroupService groupService = (GroupService)AppUtil.getBean(GroupService.class);
/* 34 */       allGroup = groupService.getAllGroupByUserId(ContextUtil.getCurrentUserId());
/* 35 */       ThreadMapUtil.put(threadMapKey, allGroup);
/*    */     } 
/*    */     
/* 38 */     List<? extends IGroup> groups = allGroup.get(getType());
/* 39 */     if (null == groups) return false; 
/* 40 */     for (IGroup group : groups) {
/* 41 */       if (json.getString("id").contains(group.getGroupId())) {
/* 42 */         return true;
/*    */       }
/*    */     } 
/*    */     
/* 46 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/permission/impl/GroupPermissionCalculator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */