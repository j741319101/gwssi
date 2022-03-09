/*    */ package cn.gwssi.ecloudframework.sys.groovy;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*    */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*    */ import cn.gwssi.ecloudframework.sys.api.groovy.IScript;
/*    */ import cn.gwssi.ecloudframework.sys.api.service.SerialNoService;
/*    */ import cn.gwssi.ecloudframework.sys.api.service.SysFileService;
/*    */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*    */ import cn.gwssi.ecloudframework.sys.util.SysPropertyUtil;
/*    */ import cn.hutool.core.util.ArrayUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Component;
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
/*    */ @Component
/*    */ public class SysScript
/*    */   implements IScript
/*    */ {
/*    */   @Resource
/*    */   SerialNoService serialNoService;
/*    */   @Resource
/*    */   SysFileService sysFileService;
/*    */   
/*    */   public String getNextSerialNo(String alias) {
/* 37 */     return this.serialNoService.genNextNo(alias);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getProperty(String key) {
/* 46 */     return SysPropertyUtil.getByAlias(key);
/*    */   }
/*    */ 
/*    */   
/*    */   public IUser getCurrentUser() {
/* 51 */     IUser user = ContextUtil.getCurrentUser();
/* 52 */     return user;
/*    */   }
/*    */   
/*    */   public String getCurrentGroupName() {
/* 56 */     IGroup iGroup = ContextUtil.getCurrentGroup();
/* 57 */     if (iGroup != null) {
/* 58 */       return iGroup.getGroupName();
/*    */     }
/* 60 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCurrentUserName() {
/* 65 */     return ContextUtil.getCurrentUser().getFullname();
/*    */   }
/*    */   
/*    */   public void updateFileInstID(String instId, String... files) {
/* 69 */     if (StringUtil.isEmpty(instId) || ArrayUtil.isEmpty((Object[])files)) {
/*    */       return;
/*    */     }
/* 72 */     for (String fileJson : files) {
/* 73 */       JSONArray objects = JSON.parseArray(fileJson);
/* 74 */       if (objects != null && !objects.isEmpty())
/* 75 */         objects.forEach(obj -> {
/*    */               String fileId = ((JSONObject)obj).getString("id");
/*    */               this.sysFileService.updateInstid(instId, fileId);
/*    */             }); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/groovy/SysScript.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */