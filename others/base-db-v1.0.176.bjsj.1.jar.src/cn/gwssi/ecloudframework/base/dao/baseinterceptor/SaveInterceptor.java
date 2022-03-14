/*     */ package com.dstz.base.dao.baseinterceptor;
/*     */ 
/*     */ import com.dstz.base.api.context.ICurrentContext;
/*     */ import com.dstz.base.api.model.CreateInfoModel;
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import com.dstz.base.api.model.IGBaseModel;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import cn.hutool.core.util.ArrayUtil;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.Properties;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.ibatis.executor.Executor;
/*     */ import org.apache.ibatis.mapping.MappedStatement;
/*     */ import org.apache.ibatis.plugin.Interceptor;
/*     */ import org.apache.ibatis.plugin.Intercepts;
/*     */ import org.apache.ibatis.plugin.Invocation;
/*     */ import org.apache.ibatis.plugin.Plugin;
/*     */ import org.apache.ibatis.plugin.Signature;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class})})
/*     */ public class SaveInterceptor
/*     */   implements Interceptor
/*     */ {
/*     */   @Autowired
/*     */   ICurrentContext currentContext;
/*     */   
/*     */   public Object intercept(Invocation invocation) throws Throwable {
/*  35 */     Object[] args = invocation.getArgs();
/*  36 */     if (ArrayUtil.isEmpty(args) || args.length < 2) {
/*  37 */       return invocation.proceed();
/*     */     }
/*     */     
/*  40 */     Object param = args[1];
/*  41 */     MappedStatement statement = (MappedStatement)args[0];
/*  42 */     String currentUserId = this.currentContext.getCurrentUserId();
/*     */ 
/*     */     
/*  45 */     if (statement.getId().endsWith(".update")) {
/*  46 */       if (param instanceof CreateInfoModel) {
/*  47 */         CreateInfoModel model = (CreateInfoModel)param;
/*  48 */         model.setUpdateTime(new Date());
/*  49 */         model.setUpdateBy(currentUserId);
/*     */         
/*  51 */         if (param instanceof BaseModel) {
/*  52 */           BaseModel baseModel = (BaseModel)param;
/*  53 */           if (null != baseModel.getVersion()) {
/*  54 */             baseModel.setVersion(Integer.valueOf(baseModel.getVersion().intValue() + 1));
/*     */           } else {
/*  56 */             baseModel.setVersion(Integer.valueOf(1));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  62 */       if (param instanceof IGBaseModel) {
/*  63 */         ((IGBaseModel)param).setUpdateUser(this.currentContext.getCurrentUserName());
/*     */       
/*     */       }
/*     */     
/*     */     }
/*  68 */     else if (StringUtils.endsWithAny(statement.getId(), new CharSequence[] { ".create", ".insertSelective" })) {
/*     */       
/*  70 */       if (param instanceof IDModel) {
/*  71 */         IDModel model = (IDModel)param;
/*  72 */         if (model.getId() == null) {
/*  73 */           model.setId(IdUtil.getSuid());
/*     */         }
/*     */       } 
/*     */       
/*  77 */       if (param instanceof CreateInfoModel) {
/*  78 */         CreateInfoModel model = (CreateInfoModel)param;
/*  79 */         if (model.getCreateTime() == null) {
/*  80 */           model.setCreateTime(new Date());
/*  81 */           model.setCreateBy(currentUserId);
/*     */         } 
/*  83 */         if (model.getUpdateTime() == null) {
/*  84 */           model.setUpdateTime(new Date());
/*  85 */           model.setUpdateBy(currentUserId);
/*     */         } 
/*  87 */         if (param instanceof BaseModel) {
/*  88 */           BaseModel baseModel = (BaseModel)param;
/*  89 */           baseModel.setDelete(Boolean.valueOf(false));
/*  90 */           baseModel.setVersion(Integer.valueOf(0));
/*     */         } 
/*     */       } 
/*     */ 
/*     */       
/*  95 */       if (param instanceof IGBaseModel) {
/*  96 */         IGBaseModel model = (IGBaseModel)param;
/*  97 */         String currentUserName = this.currentContext.getCurrentUserName();
/*  98 */         model.setCreateUser(currentUserName);
/*  99 */         model.setUpdateUser(currentUserName);
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 104 */     else if (StringUtils.endsWithAny(statement.getId(), new CharSequence[] { ".insertBatch" })) {
/* 105 */       Object params = ((Map)param).get("param1");
/* 106 */       if (params instanceof Collection) {
/* 107 */         Collection collection = (Collection)params;
/* 108 */         for (Object object : collection) {
/* 109 */           if (object instanceof IDModel) {
/* 110 */             IDModel model = (IDModel)object;
/* 111 */             if (model.getId() == null) {
/* 112 */               model.setId(IdUtil.getSuid());
/*     */             }
/*     */           } 
/*     */           
/* 116 */           if (object instanceof CreateInfoModel) {
/* 117 */             CreateInfoModel model = (CreateInfoModel)object;
/* 118 */             if (model.getCreateTime() == null) {
/* 119 */               model.setCreateTime(new Date());
/* 120 */               model.setCreateBy(currentUserId);
/*     */             } 
/* 122 */             if (model.getUpdateTime() == null) {
/* 123 */               model.setUpdateTime(new Date());
/* 124 */               model.setUpdateBy(currentUserId);
/*     */             } 
/* 126 */             if (param instanceof BaseModel) {
/* 127 */               BaseModel baseModel = (BaseModel)param;
/* 128 */               baseModel.setDelete(Boolean.valueOf(false));
/* 129 */               baseModel.setVersion(Integer.valueOf(0));
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 134 */           if (object instanceof IGBaseModel) {
/* 135 */             IGBaseModel model = (IGBaseModel)object;
/* 136 */             String currentUserName = this.currentContext.getCurrentUserName();
/* 137 */             model.setCreateUser(currentUserName);
/* 138 */             model.setUpdateUser(currentUserName);
/*     */           }
/*     */         
/*     */         }
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 146 */     else if (StringUtils.endsWithAny(statement.getId(), new CharSequence[] { ".updateBatch" })) {
/* 147 */       Object params = ((Map)param).get("param1");
/* 148 */       if (params instanceof Collection) {
/* 149 */         Collection collection = (Collection)params;
/* 150 */         for (Object object : collection) {
/* 151 */           if (object instanceof CreateInfoModel) {
/* 152 */             CreateInfoModel model = (CreateInfoModel)object;
/* 153 */             model.setUpdateTime(new Date());
/* 154 */             model.setUpdateBy(currentUserId);
/* 155 */             if (object instanceof BaseModel) {
/* 156 */               BaseModel baseModel = (BaseModel)object;
/* 157 */               if (null != baseModel.getVersion()) {
/* 158 */                 baseModel.setVersion(Integer.valueOf(baseModel.getVersion().intValue() + 1));
/*     */               } else {
/* 160 */                 baseModel.setVersion(Integer.valueOf(1));
/*     */               } 
/*     */             } 
/*     */           } 
/*     */ 
/*     */           
/* 166 */           if (object instanceof IGBaseModel) {
/* 167 */             IGBaseModel model = (IGBaseModel)object;
/* 168 */             model.setUpdateUser(this.currentContext.getCurrentUserName());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 175 */     return invocation.proceed();
/*     */   }
/*     */ 
/*     */   
/*     */   public Object plugin(Object target) {
/* 180 */     return Plugin.wrap(target, this);
/*     */   }
/*     */   
/*     */   public void setProperties(Properties properties) {}
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/dao/baseinterceptor/SaveInterceptor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */