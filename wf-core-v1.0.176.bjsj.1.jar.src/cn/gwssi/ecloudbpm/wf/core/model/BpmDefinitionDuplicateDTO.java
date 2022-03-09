/*    */ package cn.gwssi.ecloudbpm.wf.core.model;
/*    */ 
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.io.Serializable;
/*    */ import org.hibernate.validator.constraints.NotEmpty;
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
/*    */ 
/*    */ public class BpmDefinitionDuplicateDTO
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -2291180172308486504L;
/*    */   @NotEmpty(message = "来源流程定义KEY不能为空")
/*    */   @ApiModelProperty("来源流程定义KEY")
/*    */   private String originKey;
/*    */   @NotEmpty(message = "新流程定义KEY不能为空")
/*    */   @ApiModelProperty("新流程定义KEY")
/*    */   private String newKey;
/*    */   @NotEmpty(message = "新流程定义名称不能为空")
/*    */   @ApiModelProperty("新流程定义名称")
/*    */   private String newName;
/*    */   @NotEmpty(message = "新流程分类编号不能为空")
/*    */   @ApiModelProperty("新流程分类编号")
/*    */   private String newTypeId;
/*    */   @NotEmpty(message = "新流程描述不能为空")
/*    */   @ApiModelProperty("新流程描述")
/*    */   private String newDesc;
/*    */   
/*    */   public String getOriginKey() {
/* 46 */     return this.originKey;
/*    */   }
/*    */   
/*    */   public void setOriginKey(String originKey) {
/* 50 */     this.originKey = originKey;
/*    */   }
/*    */   
/*    */   public String getNewKey() {
/* 54 */     return this.newKey;
/*    */   }
/*    */   
/*    */   public void setNewKey(String newKey) {
/* 58 */     this.newKey = newKey;
/*    */   }
/*    */   
/*    */   public String getNewName() {
/* 62 */     return this.newName;
/*    */   }
/*    */   
/*    */   public void setNewName(String newName) {
/* 66 */     this.newName = newName;
/*    */   }
/*    */   
/*    */   public String getNewTypeId() {
/* 70 */     return this.newTypeId;
/*    */   }
/*    */   
/*    */   public void setNewTypeId(String newTypeId) {
/* 74 */     this.newTypeId = newTypeId;
/*    */   }
/*    */   
/*    */   public String getNewDesc() {
/* 78 */     return this.newDesc;
/*    */   }
/*    */   
/*    */   public void setNewDesc(String newDesc) {
/* 82 */     this.newDesc = newDesc;
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 87 */     return "BpmDefinitionDuplicateDTO{originDefKey='" + this.originKey + '\'' + ", newDefKey='" + this.newKey + '\'' + ", newDefName='" + this.newName + '\'' + ", newTypeId='" + this.newTypeId + '\'' + ", newDesc='" + this.newDesc + '\'' + '}';
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/model/BpmDefinitionDuplicateDTO.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */