/*     */ package cn.gwssi.ecloudbpm.bus.model;
/*     */ 
/*     */ import com.dstz.base.api.model.IBaseModel;
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
/*     */ public class BusTableDiagram
/*     */   implements IBaseModel
/*     */ {
/*     */   @NotEmpty
/*     */   private String id;
/*     */   private String diagramJson;
/*     */   private String groupId;
/*     */   private String groupName;
/*     */   protected Date createTime;
/*     */   protected String createBy;
/*     */   protected Date updateTime;
/*     */   protected String updateBy;
/*     */   
/*     */   public String getDiagramJson() {
/*  40 */     return this.diagramJson;
/*     */   }
/*     */   
/*     */   public void setDiagramJson(String diagramJson) {
/*  44 */     this.diagramJson = diagramJson;
/*     */   }
/*     */   
/*     */   public String getGroupId() {
/*  48 */     return this.groupId;
/*     */   }
/*     */   
/*     */   public void setGroupId(String groupId) {
/*  52 */     this.groupId = groupId;
/*     */   }
/*     */   
/*     */   public String getGroupName() {
/*  56 */     return this.groupName;
/*     */   }
/*     */   
/*     */   public void setGroupName(String groupName) {
/*  60 */     this.groupName = groupName;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/*  65 */     return this.createTime;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  69 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  73 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/*  78 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/*  83 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/*  88 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/*  93 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/*  98 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 103 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 108 */     this.updateBy = updateBy;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusTableDiagram.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */