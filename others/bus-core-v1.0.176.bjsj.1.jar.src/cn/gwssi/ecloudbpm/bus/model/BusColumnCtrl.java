/*    */ package cn.gwssi.ecloudbpm.bus.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusColumnCtrl;
/*    */ import com.dstz.base.core.model.BaseModel;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BusColumnCtrl
/*    */   extends BaseModel
/*    */   implements IBusColumnCtrl
/*    */ {
/*    */   @NotEmpty
/*    */   private String columnId;
/*    */   @NotEmpty
/*    */   private String type;
/*    */   private String config;
/*    */   private String validRule;
/*    */   
/*    */   public String getId() {
/* 24 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(String id) {
/* 28 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getColumnId() {
/* 32 */     return this.columnId;
/*    */   }
/*    */   
/*    */   public void setColumnId(String columnId) {
/* 36 */     this.columnId = columnId;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 40 */     return this.type;
/*    */   }
/*    */   
/*    */   public void setType(String type) {
/* 44 */     this.type = type;
/*    */   }
/*    */   
/*    */   public String getConfig() {
/* 48 */     return this.config;
/*    */   }
/*    */   
/*    */   public void setConfig(String config) {
/* 52 */     this.config = config;
/*    */   }
/*    */   
/*    */   public String getValidRule() {
/* 56 */     return this.validRule;
/*    */   }
/*    */   
/*    */   public void setValidRule(String validRule) {
/* 60 */     this.validRule = validRule;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusColumnCtrl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */