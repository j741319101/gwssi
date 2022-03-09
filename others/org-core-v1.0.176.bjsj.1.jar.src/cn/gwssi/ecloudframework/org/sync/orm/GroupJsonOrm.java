/*    */ package cn.gwssi.ecloudframework.org.sync.orm;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import com.alibaba.fastjson.annotation.JSONField;
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
/*    */ public class GroupJsonOrm
/*    */ {
/*    */   protected String id;
/*    */   protected String parentId;
/*    */   protected String name;
/*    */   protected String code;
/* 21 */   protected Integer type = Integer.valueOf(2);
/*    */   
/* 23 */   protected Integer sn = Integer.valueOf(1);
/*    */   
/*    */   public String getId() {
/* 26 */     return this.id;
/*    */   }
/*    */   
/*    */   @JSONField(name = "orgId")
/*    */   public void setId(String id) {
/* 31 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getParentId() {
/* 35 */     return this.parentId;
/*    */   }
/*    */   
/*    */   @JSONField(name = "pid")
/*    */   public void setParentId(String parentId) {
/* 40 */     this.parentId = parentId;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 44 */     return this.name;
/*    */   }
/*    */   
/*    */   @JSONField(name = "orgName")
/*    */   public void setName(String name) {
/* 49 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getCode() {
/* 53 */     if (StringUtil.isEmpty(this.code)) {
/* 54 */       return "code-".concat(this.id);
/*    */     }
/* 56 */     return this.code;
/*    */   }
/*    */   
/*    */   @JSONField(name = "orgCode")
/*    */   public void setCode(String code) {
/* 61 */     this.code = code;
/*    */   }
/*    */   
/*    */   public Integer getType() {
/* 65 */     return this.type;
/*    */   }
/*    */   
/*    */   @JSONField(name = "type")
/*    */   public void setType(Integer type) {
/* 70 */     this.type = type;
/*    */   }
/*    */   
/*    */   public Integer getSn() {
/* 74 */     return this.sn;
/*    */   }
/*    */   
/*    */   @JSONField(name = "sn")
/*    */   public void setSn(Integer sn) {
/* 79 */     this.sn = sn;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/sync/orm/GroupJsonOrm.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */