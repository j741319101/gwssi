/*     */ package cn.gwssi.ecloudframework.sys.util;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.context.ICurrentContext;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import java.util.Locale;
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
/*     */ public class ContextUtil
/*     */ {
/*     */   private static ContextUtil contextUtil;
/*     */   private ICurrentContext currentContext;
/*     */   
/*     */   public void setCurrentContext(ICurrentContext _currentContext) {
/*  24 */     contextUtil = this;
/*  25 */     contextUtil.currentContext = _currentContext;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IUser getCurrentUser() {
/*  36 */     return contextUtil.currentContext.getCurrentUser();
/*     */   }
/*     */   
/*     */   public static String getCurrentUserId() {
/*  40 */     return contextUtil.currentContext.getCurrentUserId();
/*     */   }
/*     */   
/*     */   public static String getCurrentUserName() {
/*  44 */     IUser currentUser = getCurrentUser();
/*  45 */     if (currentUser != null) {
/*  46 */       return currentUser.getFullname();
/*     */     }
/*  48 */     return null;
/*     */   }
/*     */   
/*     */   public static String getCurrentUserAccount() {
/*  52 */     IUser currentUser = getCurrentUser();
/*  53 */     if (currentUser != null) {
/*  54 */       return currentUser.getAccount();
/*     */     }
/*  56 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static IGroup getCurrentGroup() {
/*  63 */     return contextUtil.currentContext.getCurrentGroup();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCurrentGroupId() {
/*  70 */     IGroup iGroup = getCurrentGroup();
/*  71 */     if (iGroup != null) {
/*  72 */       return iGroup.getGroupId();
/*     */     }
/*  74 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getCurrentGroupName() {
/*  82 */     IGroup iGroup = getCurrentGroup();
/*  83 */     if (iGroup != null) {
/*  84 */       return iGroup.getGroupName();
/*     */     }
/*  86 */     return "";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Locale getLocale() {
/*  98 */     return contextUtil.currentContext.getLocale();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearCurrentUser() {
/* 109 */     contextUtil.currentContext.clearCurrentUser();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setCurrentUser(IUser user) {
/* 121 */     contextUtil.currentContext.setCurrentUser(user);
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setCurrentUserByAccount(String account) {
/* 126 */     contextUtil.currentContext.setCurrentUserByAccount(account);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setCurrentOrg(IGroup group) {
/* 138 */     contextUtil.currentContext.cacheCurrentGroup(group);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearUserRedisCache(String userId) {
/* 147 */     if (StringUtil.isEmpty(userId)) {
/* 148 */       userId = getCurrentUserId();
/*     */     }
/* 150 */     contextUtil.currentContext.clearUserRedisCache(userId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setLocale(Locale locale) {
/* 161 */     contextUtil.currentContext.setLocale(locale);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void cleanLocale() {
/* 172 */     contextUtil.currentContext.clearLocale();
/*     */   }
/*     */   
/*     */   public static void clearAll() {
/* 176 */     cleanLocale();
/* 177 */     clearCurrentUser();
/*     */   }
/*     */   
/*     */   public static boolean isAdmin(IUser user) {
/* 181 */     return contextUtil.currentContext.isAdmin(user);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean currentUserIsAdmin() {
/* 186 */     IUser user = getCurrentUser();
/* 187 */     return isAdmin(user);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/util/ContextUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */