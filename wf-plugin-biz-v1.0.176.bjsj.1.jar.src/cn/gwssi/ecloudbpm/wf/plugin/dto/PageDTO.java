/*    */ package com.dstz.bpm.plugin.dto;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class PageDTO
/*    */   implements Serializable
/*    */ {
/* 13 */   private Integer pageNo = Integer.valueOf(1);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 18 */   private Integer pageSize = Integer.valueOf(20);
/*    */   
/*    */   public Integer getPageNo() {
/* 21 */     return this.pageNo;
/*    */   }
/*    */   
/*    */   public void setPageNo(Integer pageNo) {
/* 25 */     this.pageNo = pageNo;
/*    */   }
/*    */   
/*    */   public Integer getPageSize() {
/* 29 */     return this.pageSize;
/*    */   }
/*    */   
/*    */   public void setPageSize(Integer pageSize) {
/* 33 */     this.pageSize = pageSize;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/dto/PageDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */