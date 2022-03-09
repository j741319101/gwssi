/*    */ package cn.gwssi.ecloudframework.base.core.ext;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.core.model.EnumExtraData;
/*    */ 
/*    */ 
/*    */ public class DefaultEnumExtraData
/*    */   implements EnumExtraData
/*    */ {
/*    */   private String name;
/*    */   private String key;
/*    */   private String desc;
/*    */   
/*    */   public static DefaultEnumExtraData newInstance(String name, String key, String desc) {
/* 14 */     DefaultEnumExtraData defaultEnumExtraData = new DefaultEnumExtraData();
/* 15 */     defaultEnumExtraData.name = name;
/* 16 */     defaultEnumExtraData.key = key;
/* 17 */     defaultEnumExtraData.desc = desc;
/* 18 */     return defaultEnumExtraData;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getName() {
/* 23 */     return this.name;
/*    */   }
/*    */   
/*    */   public void setName(String name) {
/* 27 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getKey() {
/* 32 */     return this.key;
/*    */   }
/*    */   
/*    */   public void setKey(String key) {
/* 36 */     this.key = key;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getDesc() {
/* 41 */     return this.desc;
/*    */   }
/*    */   
/*    */   public void setDesc(String desc) {
/* 45 */     this.desc = desc;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/ext/DefaultEnumExtraData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */