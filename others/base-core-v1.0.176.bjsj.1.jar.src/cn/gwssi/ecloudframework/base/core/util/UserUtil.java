/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
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
/*     */ public class UserUtil
/*     */ {
/*     */   public static Set<String> getUserIdSet(List<? extends BaseModel> lstModel) {
/*  21 */     Set<String> set = new HashSet<>();
/*  22 */     if (null != lstModel && lstModel.size() > 0) {
/*  23 */       lstModel.forEach(temp -> {
/*     */             if (null != temp) {
/*     */               set.add(temp.getCreateBy());
/*     */               set.add(temp.getUpdateBy());
/*     */             } 
/*     */           });
/*     */     }
/*  30 */     set.remove(null);
/*  31 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getUserIdSetByMapList(List<Map> lstModel) {
/*  41 */     Set<String> set = new HashSet<>();
/*  42 */     if (null != lstModel && lstModel.size() > 0) {
/*  43 */       lstModel.forEach(temp -> {
/*     */             if (null != temp) {
/*     */               set.add((String)temp.get("createBy"));
/*     */               set.add((String)temp.get("updateBy"));
/*     */             } 
/*     */           });
/*     */     }
/*  50 */     set.remove(null);
/*  51 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static Set<String> getUserIdSet(BaseModel model) {
/*  61 */     Set<String> set = new HashSet<>();
/*  62 */     if (null != model) {
/*  63 */       set.add(model.getCreateBy());
/*  64 */       set.add(model.getUpdateBy());
/*     */     } 
/*  66 */     set.remove(null);
/*  67 */     return set;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeUserInfo(Map<String, String> mapUser, List<? extends BaseModel> lstModel) {
/*  77 */     if (null != mapUser && mapUser.size() > 0 && null != lstModel && lstModel.size() > 0) {
/*  78 */       lstModel.forEach(temp -> {
/*     */             if (null != temp) {
/*     */               temp.setCreateUser((String)mapUser.get(temp.getCreateBy()));
/*     */               temp.setUpdateUser((String)mapUser.get(temp.getUpdateBy()));
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeUserInfoByMapList(Map<String, String> mapUser, List<Map> lstModel) {
/*  94 */     if (null != mapUser && mapUser.size() > 0 && null != lstModel && lstModel.size() > 0) {
/*  95 */       lstModel.forEach(temp -> {
/*     */             if (null != temp) {
/*     */               temp.put("createUser", mapUser.get(temp.get("createBy")));
/*     */               temp.put("updateUser", mapUser.get(temp.get("updateBy")));
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void makeUserInfo(Map<String, String> mapUser, BaseModel model) {
/* 111 */     if (null != model) {
/* 112 */       model.setCreateUser(mapUser.get(model.getCreateBy()));
/* 113 */       model.setUpdateUser(mapUser.get(model.getUpdateBy()));
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/UserUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */