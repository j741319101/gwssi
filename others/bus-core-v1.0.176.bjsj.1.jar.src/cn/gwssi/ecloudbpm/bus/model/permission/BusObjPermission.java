/*    */ package cn.gwssi.ecloudbpm.bus.model.permission;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.constant.RightsType;
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusColumnPermission;
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusObjPermission;
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.permission.IBusTablePermission;
/*    */ import com.alibaba.fastjson.JSONObject;
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
/*    */ public class BusObjPermission
/*    */   extends AbstractPermission
/*    */   implements IBusObjPermission
/*    */ {
/*    */   private String key;
/*    */   private String name;
/* 39 */   private Map<String, BusTablePermission> tableMap = new HashMap<>();
/*    */   
/*    */   public String getKey() {
/* 42 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 46 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 50 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 54 */     this.name = name;
/*    */   }
/*    */   
/*    */   public Map<String, BusTablePermission> getTableMap() {
/* 58 */     return this.tableMap;
/*    */   }
/*    */   
/*    */   public void setTableMap(Map<String, BusTablePermission> tableMap) {
/* 62 */     this.tableMap = tableMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public void handlePermission(JSONObject tablePermission, JSONObject permission, Boolean isReadonly) {
/* 67 */     permission.put(getKey(), new JSONObject());
/* 68 */     tablePermission.put(getKey(), new JSONObject());
/* 69 */     for (Map.Entry<String, ? extends IBusTablePermission> etry : getTableMap().entrySet()) {
/* 70 */       IBusTablePermission busTablePermission = etry.getValue();
/* 71 */       permission.getJSONObject(getKey()).put(busTablePermission.getKey(), new JSONObject());
/* 72 */       tablePermission.getJSONObject(getKey()).put(busTablePermission.getKey(), handleReadonlyResult(busTablePermission.getResult(), isReadonly));
/*    */       
/* 74 */       for (Map.Entry<String, ? extends IBusColumnPermission> ery : (Iterable<Map.Entry<String, ? extends IBusColumnPermission>>)busTablePermission.getColumnMap().entrySet()) {
/* 75 */         IBusColumnPermission busColumnPermission = ery.getValue();
/* 76 */         permission.getJSONObject(getKey()).getJSONObject(busTablePermission.getKey()).put(busColumnPermission.getKey(), handleReadonlyResult(busColumnPermission.getResult(), isReadonly));
/*    */       } 
/*    */     } 
/*    */   }
/*    */   
/*    */   private String handleReadonlyResult(String result, Boolean isReadonly) {
/* 82 */     if (!isReadonly.booleanValue()) {
/* 83 */       return result;
/*    */     }
/* 85 */     if (RightsType.REQUIRED.getKey().equals(result) || RightsType.WRITE.getKey().equals(result)) {
/* 86 */       return RightsType.READ.getKey();
/*    */     }
/*    */     
/* 89 */     return result;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/permission/BusObjPermission.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */