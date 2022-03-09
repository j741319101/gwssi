/*     */ package cn.gwssi.ecloudbpm.wf.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.model.IDModel;
/*     */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BpmBusLink
/*     */   implements IDModel
/*     */ {
/*     */   protected String id;
/*     */   protected String defId;
/*     */   protected String instId;
/*     */   protected String bizId;
/*     */   protected String bizCode;
/*     */   
/*     */   public void setId(String id) {
/*  42 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/*  50 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setDefId(String defId) {
/*  54 */     this.defId = defId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefId() {
/*  62 */     return this.defId;
/*     */   }
/*     */   
/*     */   public void setInstId(String instId) {
/*  66 */     this.instId = instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInstId() {
/*  74 */     return this.instId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/*  84 */     return (new ToStringBuilder(this))
/*  85 */       .append("id", this.id)
/*  86 */       .append("defId", this.defId)
/*  87 */       .append("instId", this.instId)
/*  88 */       .append("bizKey", this.bizId)
/*  89 */       .append("bizIdentify", this.bizCode)
/*  90 */       .toString();
/*     */   }
/*     */   
/*     */   public String getBizId() {
/*  94 */     return this.bizId;
/*     */   }
/*     */   
/*     */   public void setBizId(String bizId) {
/*  98 */     this.bizId = bizId;
/*     */   }
/*     */   
/*     */   public String getBizCode() {
/* 102 */     return this.bizCode;
/*     */   }
/*     */   
/*     */   public void setBizCode(String bizCode) {
/* 106 */     this.bizCode = bizCode;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmBusLink.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */