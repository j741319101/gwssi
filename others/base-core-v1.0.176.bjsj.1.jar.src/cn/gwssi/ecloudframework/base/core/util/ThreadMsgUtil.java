/*    */ package cn.gwssi.ecloudframework.base.core.util;
/*    */ 
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ThreadMsgUtil
/*    */ {
/* 14 */   private static ThreadLocal<List<String>> localMsg = new ThreadLocal<>();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void addMsg(String msg) {
/* 22 */     List<String> list = localMsg.get();
/* 23 */     if (CollectionUtil.isEmpty(list)) {
/* 24 */       list = new ArrayList<>();
/* 25 */       localMsg.set(list);
/*    */     } 
/* 27 */     list.add(msg);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<String> getMsg() {
/* 36 */     return getMsg(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static List<String> getMsg(boolean clean) {
/* 46 */     List<String> list = localMsg.get();
/* 47 */     if (clean) {
/* 48 */       localMsg.remove();
/*    */     }
/* 50 */     return list;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getMessage() {
/* 59 */     return getMessage(true);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getMessage(boolean clean) {
/* 69 */     return getMessage(clean, "\r\n");
/*    */   }
/*    */   
/*    */   public static String getMessage(boolean clean, String lineBreak) {
/* 73 */     List<String> list = getMsg(clean);
/* 74 */     if (CollectionUtil.isEmpty(list)) {
/* 75 */       return "";
/*    */     }
/* 77 */     StringBuilder sb = new StringBuilder();
/* 78 */     for (String msg : list) {
/* 79 */       sb.append(msg).append(lineBreak);
/*    */     }
/* 81 */     return sb.toString();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void clean() {
/* 88 */     localMsg.remove();
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/ThreadMsgUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */