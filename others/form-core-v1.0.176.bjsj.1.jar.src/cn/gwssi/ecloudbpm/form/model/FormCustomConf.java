/*    */ package cn.gwssi.ecloudbpm.form.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRule;
/*    */ import cn.gwssi.ecloudbpm.bus.deserializer.FastJsonLocalISqlRuleDeserializer;
/*    */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustomConf;
/*    */ import com.dstz.base.core.model.BaseModel;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.annotation.JSONField;
/*    */ import com.alibaba.fastjson.parser.ParserConfig;
/*    */ import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
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
/*    */ public class FormCustomConf
/*    */   extends BaseModel
/*    */   implements IFormCustomConf
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   private String formKey;
/*    */   private String modifyDesc;
/*    */   @JSONField(serialize = false)
/*    */   private String conf;
/*    */   private FormCustomConfEntity formCustomConfEntity;
/*    */   
/*    */   public String getFormKey() {
/* 41 */     return this.formKey;
/*    */   }
/*    */   
/*    */   public void setFormKey(String formKey) {
/* 45 */     this.formKey = formKey;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getModifyDesc() {
/* 50 */     return this.modifyDesc;
/*    */   }
/*    */   
/*    */   public void setModifyDesc(String modifyDesc) {
/* 54 */     this.modifyDesc = modifyDesc;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getConf() {
/* 59 */     return this.conf;
/*    */   }
/*    */   
/*    */   public void setConf(String conf) {
/* 63 */     this.conf = conf;
/*    */   }
/*    */   
/*    */   public FormCustomConfEntity getFormCustomConfEntity() {
/* 67 */     return this.formCustomConfEntity;
/*    */   }
/*    */   
/*    */   public void setFormCustomConfEntity(FormCustomConfEntity formCustomConfEntity) {
/* 71 */     this.formCustomConfEntity = formCustomConfEntity;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void initFormCustomConfEntity() {
/* 78 */     if (null == this.formCustomConfEntity) {
/* 79 */       ParserConfig.getGlobalInstance().putDeserializer(ISqlRule.class, (ObjectDeserializer)FastJsonLocalISqlRuleDeserializer.getGlobalInstance());
/* 80 */       this.formCustomConfEntity = (FormCustomConfEntity)JSON.parseObject(this.conf, FormCustomConfEntity.class);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormCustomConf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */