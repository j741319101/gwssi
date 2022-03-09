/*     */ package cn.gwssi.ecloudbpm.wf.core.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.IBpmDefinition;
/*     */ import com.alibaba.fastjson.annotation.JSONField;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.Date;
/*     */ import javax.xml.bind.annotation.XmlAccessType;
/*     */ import javax.xml.bind.annotation.XmlAccessorType;
/*     */ import javax.xml.bind.annotation.XmlAttribute;
/*     */ import javax.xml.bind.annotation.XmlElement;
/*     */ import javax.xml.bind.annotation.XmlRootElement;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @XmlRootElement(name = "BpmDefinition")
/*     */ @XmlAccessorType(XmlAccessType.NONE)
/*     */ @ApiModel(description = "流程定义")
/*     */ public class BpmDefinition
/*     */   implements IBpmDefinition
/*     */ {
/*     */   @ApiModelProperty("流程定义ID")
/*     */   protected String id;
/*     */   @XmlAttribute(name = "name")
/*     */   @ApiModelProperty("流程定义名字")
/*     */   protected String name;
/*     */   @ApiModelProperty("文档管理中对应的目录id")
/*     */   protected String contentId;
/*     */   @ApiModelProperty("文档管理中对应的目录描述")
/*     */   protected String contentDes;
/*     */   @XmlAttribute(name = "key")
/*     */   @ApiModelProperty("流程定义KEY")
/*     */   protected String key;
/*     */   @XmlAttribute(name = "desc")
/*     */   @ApiModelProperty("描述信息")
/*     */   protected String desc;
/*     */   @XmlAttribute(name = "typeId")
/*     */   @ApiModelProperty("流程分类")
/*     */   protected String typeId;
/*     */   @XmlAttribute(name = "status")
/*     */   @ApiModelProperty("流程状态")
/*  74 */   protected String status = "draft";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("原生流程定义ID")
/*     */   protected String actDefId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("原生流程设计MOdelID")
/*     */   protected String actModelId;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("原生DeployId")
/*     */   protected String actDeployId;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("版本号")
/*     */   protected Integer version;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty(hidden = true)
/*     */   protected String mainDefId;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @ApiModelProperty("是否主版本，Y/N")
/*     */   protected String isMain;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String createOrgId;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @XmlAttribute(name = "supportMobile")
/*     */   @ApiModelProperty("是否支持移动端")
/* 124 */   protected Integer supportMobile = Integer.valueOf(0);
/*     */ 
/*     */   
/*     */   @XmlElement(name = "defSetting")
/*     */   @JSONField(serialize = false)
/*     */   @ApiModelProperty(hidden = true)
/*     */   protected String defSetting;
/*     */ 
/*     */   
/*     */   @ApiModelProperty("乐观锁版本判断")
/*     */   protected Integer rev;
/*     */ 
/*     */   
/*     */   protected String orgId;
/*     */ 
/*     */   
/*     */   protected String orgName;
/*     */ 
/*     */   
/*     */   protected Integer order;
/*     */ 
/*     */   
/*     */   private String updateBy;
/*     */ 
/*     */   
/*     */   private String createBy;
/*     */ 
/*     */   
/*     */   private Date updateTime;
/*     */ 
/*     */   
/*     */   private Date createTime;
/*     */ 
/*     */   
/*     */   protected Date lockTime;
/*     */ 
/*     */   
/*     */   protected String lockBy;
/*     */ 
/*     */   
/*     */   public void setId(String id) {
/* 165 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getId() {
/* 175 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 179 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getName() {
/* 189 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/* 193 */     this.key = key;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getKey() {
/* 203 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/* 207 */     this.desc = desc;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDesc() {
/* 217 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setTypeId(String typeId) {
/* 221 */     this.typeId = typeId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getTypeId() {
/* 231 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setStatus(String status) {
/* 235 */     this.status = status;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getStatus() {
/* 245 */     return this.status;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActDefId(String actDefId) {
/* 250 */     this.actDefId = actDefId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActDefId() {
/* 260 */     return this.actDefId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActModelId(String actModelId) {
/* 265 */     this.actModelId = actModelId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActModelId() {
/* 275 */     return this.actModelId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setActDeployId(String actDeployId) {
/* 280 */     this.actDeployId = actDeployId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getActDeployId() {
/* 290 */     return this.actDeployId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setVersion(Integer version) {
/* 295 */     this.version = version;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getVersion() {
/* 305 */     return this.version;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setMainDefId(String mainDefId) {
/* 310 */     this.mainDefId = mainDefId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getMainDefId() {
/* 320 */     return this.mainDefId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setIsMain(String isMain) {
/* 325 */     this.isMain = isMain;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getIsMain() {
/* 335 */     return this.isMain;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateOrgId(String createOrgId) {
/* 340 */     this.createOrgId = createOrgId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCreateOrgId() {
/* 350 */     return this.createOrgId;
/*     */   }
/*     */   
/*     */   public void setSupportMobile(Integer supportMobile) {
/* 354 */     this.supportMobile = supportMobile;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getSupportMobile() {
/* 364 */     return this.supportMobile;
/*     */   }
/*     */   
/*     */   public void setDefSetting(String defSetting) {
/* 368 */     this.defSetting = defSetting;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getDefSetting() {
/* 378 */     return this.defSetting;
/*     */   }
/*     */   
/*     */   public void setRev(Integer rev) {
/* 382 */     this.rev = rev;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentId() {
/* 387 */     return this.contentId;
/*     */   }
/*     */   
/*     */   public void setContentId(String contentId) {
/* 391 */     this.contentId = contentId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getContentDes() {
/* 396 */     return this.contentDes;
/*     */   }
/*     */   
/*     */   public void setContentDes(String contentDes) {
/* 400 */     this.contentDes = contentDes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Integer getRev() {
/* 410 */     return this.rev;
/*     */   }
/*     */   
/*     */   public String getOrgId() {
/* 414 */     return this.orgId;
/*     */   }
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 418 */     this.orgId = orgId;
/*     */   }
/*     */   
/*     */   public String getOrgName() {
/* 422 */     return this.orgName;
/*     */   }
/*     */   
/*     */   public void setOrgName(String orgName) {
/* 426 */     this.orgName = orgName;
/*     */   }
/*     */   
/*     */   public Integer getOrder() {
/* 430 */     return this.order;
/*     */   }
/*     */   
/*     */   public void setOrder(Integer order) {
/* 434 */     this.order = order;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 439 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 444 */     this.updateBy = updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 449 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 454 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 459 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 464 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 469 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 474 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String toString() {
/* 482 */     return (new ToStringBuilder(this))
/* 483 */       .append("id", this.id)
/* 484 */       .append("name", this.name)
/* 485 */       .append("key", this.key)
/* 486 */       .append("desc", this.desc)
/* 487 */       .append("typeId", this.typeId)
/* 488 */       .append("status", this.status)
/* 489 */       .append("actDefId", this.actDefId)
/* 490 */       .append("actModelId", this.actModelId)
/* 491 */       .append("actDeployId", this.actDeployId)
/* 492 */       .append("version", this.version)
/* 493 */       .append("mainDefId", this.mainDefId)
/* 494 */       .append("isMain", this.isMain)
/* 495 */       .append("createBy", this.createBy)
/* 496 */       .append("createTime", this.createTime)
/* 497 */       .append("createOrgId", this.createOrgId)
/* 498 */       .append("updateBy", this.updateBy)
/* 499 */       .append("updateTime", this.updateTime)
/* 500 */       .append("supportMobile", this.supportMobile)
/* 501 */       .append("defSetting", this.defSetting)
/* 502 */       .append("rev", this.rev)
/* 503 */       .append("contentId", this.contentId)
/* 504 */       .append("contentDes", this.contentDes)
/* 505 */       .append("orgId", this.orgId)
/* 506 */       .append("orgName", this.orgName)
/* 507 */       .toString();
/*     */   }
/*     */   
/*     */   public Date getLockTime() {
/* 511 */     return this.lockTime;
/*     */   }
/*     */   
/*     */   public void setLockTime(Date lockTime) {
/* 515 */     this.lockTime = lockTime;
/*     */   }
/*     */   
/*     */   public String getLockBy() {
/* 519 */     return this.lockBy;
/*     */   }
/*     */   
/*     */   public void setLockBy(String lockBy) {
/* 523 */     this.lockBy = lockBy;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmDefinition.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */