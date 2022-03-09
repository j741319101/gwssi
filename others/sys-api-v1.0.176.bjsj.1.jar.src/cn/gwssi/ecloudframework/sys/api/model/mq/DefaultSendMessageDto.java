/*     */ package cn.gwssi.ecloudframework.sys.api.model.mq;
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
/*     */ public class DefaultSendMessageDto
/*     */ {
/*     */   private String productGroupName;
/*     */   private String topic;
/*     */   private String key;
/*     */   private String tag;
/*     */   private String message;
/*     */   
/*     */   public DefaultSendMessageDto() {}
/*     */   
/*     */   public DefaultSendMessageDto(String topic, String key, String tag, String message, String productGroupName) {
/*  35 */     this.key = key;
/*  36 */     this.tag = tag;
/*  37 */     this.message = message;
/*  38 */     this.topic = topic;
/*  39 */     this.productGroupName = productGroupName;
/*     */   }
/*     */   
/*     */   public DefaultSendMessageDto(String topic, String tag, String message, String productGroupName) {
/*  43 */     this.key = "";
/*  44 */     this.tag = tag;
/*  45 */     this.message = message;
/*  46 */     this.topic = topic;
/*  47 */     this.productGroupName = productGroupName;
/*     */   }
/*     */   
/*     */   public DefaultSendMessageDto(String topic, String tag, String message) {
/*  51 */     this.key = "";
/*  52 */     this.tag = tag;
/*  53 */     this.message = message;
/*  54 */     this.topic = topic;
/*  55 */     this.productGroupName = "defaultProductGroupName";
/*     */   }
/*     */ 
/*     */   
/*     */   public DefaultSendMessageDto(String topic, String message) {
/*  60 */     this.message = message;
/*  61 */     this.topic = topic;
/*  62 */     this.productGroupName = "defaultProductGroupName";
/*     */   }
/*     */   
/*     */   public String getProductGroupName() {
/*  66 */     return this.productGroupName;
/*     */   }
/*     */   
/*     */   public void setProductGroupName(String productGroupName) {
/*  70 */     this.productGroupName = productGroupName;
/*     */   }
/*     */   
/*     */   public String getTopic() {
/*  74 */     return this.topic;
/*     */   }
/*     */   
/*     */   public void setTopic(String topic) {
/*  78 */     this.topic = topic;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  82 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  86 */     this.key = key;
/*     */   }
/*     */   
/*     */   public String getTag() {
/*  90 */     return this.tag;
/*     */   }
/*     */   
/*     */   public void setTag(String tag) {
/*  94 */     this.tag = tag;
/*     */   }
/*     */   
/*     */   public String getMessage() {
/*  98 */     return this.message;
/*     */   }
/*     */   
/*     */   public void setMessage(String message) {
/* 102 */     this.message = message;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/mq/DefaultSendMessageDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */