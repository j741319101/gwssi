/*    */ package com.dstz.sys.api.model;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SysNodeOrderParam
/*    */   implements Serializable
/*    */ {
/*    */   @NotNull(message = "ID不能为空!")
/*    */   private String id;
/*    */   private int sn;
/*    */   
/*    */   public String getId() {
/* 17 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(String id) {
/* 21 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int getSn() {
/* 25 */     return this.sn;
/*    */   }
/*    */   
/*    */   public void setSn(int sn) {
/* 29 */     this.sn = sn;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/SysNodeOrderParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */