/*    */ package cn.gwssi.ecloudbpm.bus.model.permission;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IAbstractPermission;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import java.util.HashMap;
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
/*    */ public abstract class AbstractPermission
/*    */   implements IAbstractPermission
/*    */ {
/* 22 */   protected Map<String, JSONArray> rights = new HashMap<>();
/*    */ 
/*    */ 
/*    */   
/*    */   protected String result;
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResult() {
/* 32 */     return this.result;
/*    */   }
/*    */   
/*    */   public void setResult(String result) {
/* 36 */     this.result = result;
/*    */   }
/*    */   
/*    */   public Map<String, JSONArray> getRights() {
/* 40 */     return this.rights;
/*    */   }
/*    */   
/*    */   public void setRights(Map<String, JSONArray> rights) {
/* 44 */     this.rights = rights;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/permission/AbstractPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */