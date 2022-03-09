/*    */ package cn.gwssi.ecloudframework.sys.core.model;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import org.apache.commons.lang3.builder.ToStringBuilder;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Script
/*    */   extends BaseModel
/*    */   implements Cloneable
/*    */ {
/*    */   private static final long serialVersionUID = 1L;
/*    */   protected String name;
/*    */   protected String script;
/*    */   protected String category;
/*    */   protected String memo;
/* 21 */   protected List<String> categorys = new ArrayList<>();
/*    */   
/*    */   public String getId() {
/* 24 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(String id) {
/* 28 */     this.id = id;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 32 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 36 */     this.name = name;
/*    */   }
/*    */   
/*    */   public String getScript() {
/* 40 */     return this.script;
/*    */   }
/*    */   
/*    */   public void setScript(String script) {
/* 44 */     this.script = script;
/*    */   }
/*    */   
/*    */   public String getCategory() {
/* 48 */     return this.category;
/*    */   }
/*    */   
/*    */   public void setCategory(String category) {
/* 52 */     this.category = category;
/*    */   }
/*    */   
/*    */   public String getMemo() {
/* 56 */     return this.memo;
/*    */   }
/*    */   
/*    */   public void setMemo(String memo) {
/* 60 */     this.memo = memo;
/*    */   }
/*    */   
/*    */   public List<String> getCategorys() {
/* 64 */     return this.categorys;
/*    */   }
/*    */   
/*    */   public void setCategorys(List<String> categorys) {
/* 68 */     this.categorys = categorys;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 72 */     return (new ToStringBuilder(this))
/* 73 */       .append("id", this.id)
/* 74 */       .append("name", this.name)
/* 75 */       .append("script", this.script)
/* 76 */       .append("category", this.category)
/* 77 */       .append("memo", this.memo)
/* 78 */       .toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/model/Script.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */