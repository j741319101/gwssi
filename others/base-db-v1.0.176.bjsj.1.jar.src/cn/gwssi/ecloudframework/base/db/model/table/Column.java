/*     */ package cn.gwssi.ecloudframework.base.db.model.table;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.constant.ColumnType;
/*     */ import cn.gwssi.ecloudframework.base.core.util.EnumUtil;
/*     */ import java.io.Serializable;
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
/*     */ public class Column
/*     */   implements Serializable
/*     */ {
/*     */   @NotEmpty
/*  58 */   protected String type = ColumnType.VARCHAR.toString();
/*  59 */   protected int length = 50;
/*  60 */   protected int decimal = 0;
/*     */   
/*     */   protected boolean primary = false;
/*     */   protected boolean required = false;
/*     */   
/*     */   public String getName() {
/*  66 */     return this.name;
/*     */   } @NotEmpty
/*     */   protected String name; protected String defaultValue; protected String comment;
/*     */   public void setName(String name) {
/*  70 */     this.name = name;
/*     */   }
/*     */   
/*     */   public String getType() {
/*  74 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  78 */     this.type = type;
/*     */   }
/*     */   
/*     */   public int getLength() {
/*  82 */     return this.length;
/*     */   }
/*     */   
/*     */   public void setLength(int length) {
/*  86 */     this.length = length;
/*     */   }
/*     */   
/*     */   public int getDecimal() {
/*  90 */     return this.decimal;
/*     */   }
/*     */   
/*     */   public void setDecimal(int decimal) {
/*  94 */     this.decimal = decimal;
/*     */   }
/*     */   
/*     */   public boolean isRequired() {
/*  98 */     return this.required;
/*     */   }
/*     */   
/*     */   public void setRequired(boolean required) {
/* 102 */     this.required = required;
/*     */   }
/*     */   
/*     */   public boolean isPrimary() {
/* 106 */     return this.primary;
/*     */   }
/*     */   
/*     */   public void setPrimary(boolean primary) {
/* 110 */     this.primary = primary;
/*     */   }
/*     */   
/*     */   public String getDefaultValue() {
/* 114 */     return this.defaultValue;
/*     */   }
/*     */   
/*     */   public void setDefaultValue(String defaultValue) {
/* 118 */     this.defaultValue = defaultValue;
/*     */   }
/*     */   
/*     */   public String getComment() {
/* 122 */     return this.comment;
/*     */   }
/*     */   
/*     */   public void setComment(String comment) {
/* 126 */     this.comment = comment;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 131 */     if (obj == this) {
/* 132 */       return true;
/*     */     }
/* 134 */     if (!(obj instanceof Column)) {
/* 135 */       return false;
/*     */     }
/* 137 */     Column c = (Column)obj;
/* 138 */     if (!c.getComment().equals(getComment())) {
/* 139 */       return false;
/*     */     }
/* 141 */     if (c.getDecimal() != getDecimal()) {
/* 142 */       return false;
/*     */     }
/* 144 */     if ((c.getDefaultValue() == null && getDefaultValue() != null) || (c.getDefaultValue() != null && !c.getDefaultValue().equals(getDefaultValue()))) {
/* 145 */       return false;
/*     */     }
/* 147 */     if (c.getLength() != getLength()) {
/* 148 */       return false;
/*     */     }
/* 150 */     if (!c.getName().equalsIgnoreCase(getName())) {
/* 151 */       return false;
/*     */     }
/* 153 */     if (!c.getType().equalsIgnoreCase(getType())) {
/* 154 */       return false;
/*     */     }
/* 156 */     if ((!c.isPrimary()) == isPrimary()) {
/* 157 */       return false;
/*     */     }
/* 159 */     if ((!c.isRequired()) == isRequired()) {
/* 160 */       return false;
/*     */     }
/* 162 */     return true;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 166 */     System.out.println(EnumUtil.toJSON("cn.gwssi.ecloudframework.base.db.model.Column$TYPE"));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/model/table/Column.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */