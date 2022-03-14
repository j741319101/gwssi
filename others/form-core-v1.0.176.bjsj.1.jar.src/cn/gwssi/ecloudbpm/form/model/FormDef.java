/*     */ package cn.gwssi.ecloudbpm.form.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.api.model.IFormDef;
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import java.util.Date;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FormDef
/*     */   extends BaseModel
/*     */   implements IFormDef
/*     */ {
/*     */   private static final long serialVersionUID = -700694295167942753L;
/*     */   private String type;
/*     */   @NotEmpty
/*     */   private String key;
/*     */   @NotEmpty
/*     */   private String name;
/*     */   private String desc;
/*     */   private String modifyDesc;
/*     */   private String groupId;
/*     */   private String groupName;
/*     */   private String boKey;
/*     */   private String boName;
/*     */   private String html;
/*     */   protected String orgId;
/*     */   protected Date lockTime;
/*     */   protected String lockBy;
/*     */   protected String lockUser;
/*     */   
/*     */   public String getKey() {
/*  89 */     return this.key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setKey(String key) {
/*  97 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 105 */     return this.name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String name) {
/* 113 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDesc() {
/* 121 */     return this.desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDesc(String desc) {
/* 129 */     this.desc = desc;
/*     */   }
/*     */   
/*     */   public String getModifyDesc() {
/* 133 */     return this.modifyDesc;
/*     */   }
/*     */   
/*     */   public void setModifyDesc(String modifyDesc) {
/* 137 */     this.modifyDesc = modifyDesc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroupId() {
/* 145 */     return this.groupId;
/*     */   }
/*     */   
/*     */   public void setGroupId(String groupId) {
/* 149 */     this.groupId = groupId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGroupName() {
/* 157 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public void setGroupName(String groupName) {
/* 161 */     this.groupName = groupName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBoKey() {
/* 169 */     return this.boKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setBoKey(String boKey) {
/* 177 */     this.boKey = boKey;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getBoName() {
/* 185 */     return this.boName;
/*     */   }
/*     */   
/*     */   public void setBoName(String boName) {
/* 189 */     this.boName = boName;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getHtml() {
/* 197 */     if (StringUtil.isNotEmpty(this.html)) {
/*     */       
/* 199 */       String content = this.html.replaceAll("&apos;", "'").replaceAll("&#39;", "'").replaceAll("#ctx#", "ctx");
/* 200 */       return content;
/*     */     } 
/*     */     
/* 203 */     return this.html;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHtml(String html) {
/* 211 */     this.html = html;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/* 216 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setType(String type) {
/* 221 */     this.type = type;
/*     */   }
/*     */   
/*     */   public String getOrgId() {
/* 225 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 229 */     this.orgId = orgId;
/*     */   }
/*     */   
/*     */   public Date getLockTime() {
/* 233 */     return this.lockTime;
/*     */   }
/*     */   
/*     */   public void setLockTime(Date lockTime) {
/* 237 */     this.lockTime = lockTime;
/*     */   }
/*     */   
/*     */   public String getLockBy() {
/* 241 */     return this.lockBy;
/*     */   }
/*     */   
/*     */   public void setLockBy(String lockBy) {
/* 245 */     this.lockBy = lockBy;
/*     */   }
/*     */   
/*     */   public String getLockUser() {
/* 249 */     return this.lockUser;
/*     */   }
/*     */   
/*     */   public void setLockUser(String lockUser) {
/* 253 */     this.lockUser = lockUser;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */