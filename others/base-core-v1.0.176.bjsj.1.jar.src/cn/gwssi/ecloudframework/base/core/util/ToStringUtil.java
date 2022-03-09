/*    */ package cn.gwssi.ecloudframework.base.core.util;
/*    */ 
/*    */ import java.lang.reflect.Method;
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
/*    */ public class ToStringUtil
/*    */ {
/*    */   public String toString() {
/* 18 */     return toString(this);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <C> String toString(C c) {
/* 30 */     Class<?> cls = c.getClass();
/* 31 */     if (c instanceof String) {
/* 32 */       return (new StringBuilder()).append(c).append("").toString();
/*    */     }
/*    */     try {
/* 35 */       Method toStringMethod = cls.getMethod("toString", new Class[0]);
/* 36 */       if (toStringMethod == null) {
/* 37 */         return (new StringBuilder()).append(c).append("").toString();
/*    */       }
/* 39 */     } catch (Exception exception) {}
/*    */ 
/*    */     
/* 42 */     StringBuilder sb = new StringBuilder();
/* 43 */     sb.append("" + cls.getSimpleName() + " [");
/*    */     
/* 45 */     for (Method method : cls.getMethods()) {
/*    */       
/* 47 */       if (method.getName().startsWith("get") && (method.getParameters()).length == 0 && !"getClass".equals(method.getName())) {
/*    */         
/*    */         try {
/*    */           
/* 51 */           Object obj = method.invoke(c, new Object[0]);
/* 52 */           String str = "";
/* 53 */           if (obj != null) {
/* 54 */             str = obj + "";
/*    */           }
/* 56 */           if (!sb.toString().endsWith("[")) {
/* 57 */             sb.append(", ");
/*    */           }
/* 59 */           sb.append(StringUtil.toFirst(method.getName().replace("get", ""), false) + "=" + str);
/* 60 */         } catch (Exception exception) {}
/*    */       }
/*    */     } 
/* 63 */     sb.append("]");
/* 64 */     return sb.toString();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/ToStringUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */