/*    */ package cn.gwssi.ecloudframework.sys.core.model;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.model.IDModel;
/*    */ import org.apache.commons.lang3.builder.ToStringBuilder;
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
/*    */ public class ScheduleBiz
/*    */   implements IDModel
/*    */ {
/*    */   protected String id;
/*    */   protected String scheduleId;
/*    */   protected String bizId;
/*    */   protected String source;
/*    */   
/*    */   public void setId(String id) {
/* 35 */     this.id = id;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getId() {
/* 43 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setScheduleId(String scheduleId) {
/* 47 */     this.scheduleId = scheduleId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getScheduleId() {
/* 55 */     return this.scheduleId;
/*    */   }
/*    */   
/*    */   public void setBizId(String bizId) {
/* 59 */     this.bizId = bizId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getBizId() {
/* 67 */     return this.bizId;
/*    */   }
/*    */   
/*    */   public void setSource(String source) {
/* 71 */     this.source = source;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSource() {
/* 79 */     return this.source;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 85 */     return (new ToStringBuilder(this))
/* 86 */       .append("id", this.id)
/* 87 */       .append("scheduleId", this.scheduleId)
/* 88 */       .append("bizId", this.bizId)
/* 89 */       .append("source", this.source)
/* 90 */       .toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/ScheduleBiz.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */