/*    */ package cn.gwssi.ecloudbpm.wf.api.constant;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum TaskStatus
/*    */ {
/*  7 */   NORMAL("NORMAL", "普通", "普通订单"),
/*  8 */   SUSPEND("SUSPEND", "挂起", "超管挂起任务"),
/*  9 */   LOCK("LOCK", "锁定", "个人将任务锁定至个人名下"),
/* 10 */   TURN("TURN", "转办", "个人将任务转办给其他人"),
/* 11 */   AGENCY("AGENCY", "代理", "代理其他人的任务"),
/* 12 */   BACK("BACK", "驳回", "被驳回的任务", 1),
/* 13 */   DESIGNATE("DESIGNATE", "指派", "个人将任务指派到某个人名下"),
/* 14 */   ADDDOING("ADDDOING", "加办中", "个人将任务加办给某人名下"),
/* 15 */   ADDDOED("ADDDOED", "加办结束", "加办已被处理所以任务回归"),
/* 16 */   DRAG("DRAG", "捞单", "从捞单池中获取的订单");
/*    */ 
/*    */   
/* 19 */   private String key = "";
/*    */   
/* 21 */   private String value = "";
/*    */   
/* 23 */   private String desc = "";
/*    */   
/* 25 */   private int priority = 0;
/*    */   
/*    */   TaskStatus(String key, String value, String desc) {
/* 28 */     this.key = key;
/* 29 */     this.value = value;
/*    */   }
/*    */   TaskStatus(String key, String value, String desc, int priority) {
/* 32 */     this.key = key;
/* 33 */     this.value = value;
/* 34 */     this.priority = priority;
/*    */   }
/*    */   
/*    */   public String getKey() {
/* 38 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 42 */     this.key = key;
/*    */   }
/*    */   
/*    */   public String getValue() {
/* 46 */     return this.value;
/*    */   }
/*    */   
/*    */   public void setValue(String value) {
/* 50 */     this.value = value;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 55 */     return this.key;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static TaskStatus fromKey(String key) {
/* 65 */     for (TaskStatus c : values()) {
/* 66 */       if (c.getKey().equalsIgnoreCase(key))
/* 67 */         return c; 
/*    */     } 
/* 69 */     throw new IllegalArgumentException(key);
/*    */   }
/*    */   
/*    */   public String getDesc() {
/* 73 */     return this.desc;
/*    */   }
/*    */   
/*    */   public void setDesc(String desc) {
/* 77 */     this.desc = desc;
/*    */   }
/*    */   
/*    */   public int getPriority() {
/* 81 */     return this.priority;
/*    */   }
/*    */   
/*    */   public void setPriority(int priority) {
/* 85 */     this.priority = priority;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/constant/TaskStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */