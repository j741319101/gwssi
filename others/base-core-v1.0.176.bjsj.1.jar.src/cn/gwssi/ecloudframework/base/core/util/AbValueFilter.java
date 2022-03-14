/*    */ package com.dstz.base.core.util;
/*    */ 
/*    */ import com.alibaba.fastjson.serializer.ValueFilter;
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
/*    */ public class AbValueFilter
/*    */   implements ValueFilter
/*    */ {
/*    */   public Object process(Object object, String name, Object value) {
/* 18 */     if (value instanceof Long) {
/* 19 */       Long l = (Long)value;
/* 20 */       return (l.longValue() > 9007199254740992L) ? l.toString() : l;
/*    */     } 
/* 22 */     return value;
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {}
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/AbValueFilter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */