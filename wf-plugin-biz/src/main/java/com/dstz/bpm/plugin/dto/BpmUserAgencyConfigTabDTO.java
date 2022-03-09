/*    */ package com.dstz.bpm.plugin.dto;
/*    */ 
/*    */ import java.io.Serializable;
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
/*    */ public class BpmUserAgencyConfigTabDTO
/*    */   extends PageDTO
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = 1712345490619029539L;
/*    */   private String tab;
/*    */   private String configUserId;
/*    */   private String name;
/*    */   
/*    */   public String getTab() {
/* 30 */     return this.tab;
/*    */   }
/*    */   
/*    */   public void setTab(String tab) {
/* 34 */     this.tab = tab;
/*    */   }
/*    */   
/*    */   public String getConfigUserId() {
/* 38 */     return this.configUserId;
/*    */   }
/*    */   
/*    */   public void setConfigUserId(String configUserId) {
/* 42 */     this.configUserId = configUserId;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 46 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 50 */     this.name = name;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-plugin-biz/0.2-SNAPSHOT/wf-plugin-biz-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/plugin/dto/BpmUserAgencyConfigTabDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */