/*    */ package cn.gwssi.ecloudframework.sys.api.model;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
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
/*    */ public class SysConnectRecordDTO
/*    */   extends BaseModel
/*    */ {
/*    */   @NotEmpty(message = "关联类型不能为空")
/*    */   protected String type;
/*    */   @NotEmpty(message = "关联源头ID不能为空")
/*    */   protected String sourceId;
/*    */   @NotEmpty(message = "关联目标ID不能为空")
/*    */   protected String targetId;
/*    */   @NotEmpty(message = "关联提示信息不能为空")
/*    */   protected String notice;
/*    */   
/*    */   public void setType(String type) {
/* 36 */     this.type = type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getType() {
/* 44 */     return this.type;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setSourceId(String sourceId) {
/* 51 */     this.sourceId = sourceId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSourceId() {
/* 59 */     return this.sourceId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setTargetId(String targetId) {
/* 66 */     this.targetId = targetId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTargetId() {
/* 74 */     return this.targetId;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void setNotice(String notice) {
/* 81 */     this.notice = notice;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getNotice() {
/* 89 */     return this.notice;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/SysConnectRecordDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */