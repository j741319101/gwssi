/*     */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.core.cache.ICache;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.constant.EnvironmentConstant;
/*     */ import cn.gwssi.ecloudframework.sys.api.service.PropertyService;
/*     */ import cn.gwssi.ecloudframework.sys.core.dao.SysPropertiesDao;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.SysPropertiesManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SysProperties;
/*     */ import cn.hutool.core.map.MapUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("sysPropertiesManager")
/*     */ public class SysPropertiesManagerImpl
/*     */   extends BaseManager<String, SysProperties>
/*     */   implements SysPropertiesManager, PropertyService
/*     */ {
/*     */   @Resource
/*     */   SysPropertiesDao sysPropertiesDao;
/*     */   @Resource
/*     */   ICache cache;
/*     */   @Autowired
/*     */   UserService userService;
/*  38 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   private static final String PROPERTIES_CACHE_KEY = "PROPERTIES_CACHE_";
/*     */ 
/*     */   
/*     */   public List<String> getGroups() {
/*  44 */     return this.sysPropertiesDao.getGroups();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isExist(SysProperties sysProperties) {
/*  49 */     return (this.sysPropertiesDao.isExist(sysProperties).intValue() > 0);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Map<String, String>> reloadProperty() {
/*  57 */     List<SysProperties> list = getAll();
/*     */     
/*  59 */     Map<String, Map<String, String>> propertiesCache = new HashMap<>();
/*  60 */     for (SysProperties property : list) {
/*  61 */       String environment = property.getEnvironment();
/*  62 */       if (!EnvironmentConstant.contain(environment)) {
/*  63 */         this.LOG.warn("当前系统属性的环境参数“{}”非系统定义参数{}请注意！", environment, EnvironmentConstant.getKes());
/*     */       }
/*     */       
/*  66 */       Map<String, String> proerties = propertiesCache.get(environment);
/*     */       
/*  68 */       if (proerties == null) {
/*  69 */         proerties = new HashMap<>();
/*  70 */         propertiesCache.put(environment, proerties);
/*     */       } 
/*     */       
/*  73 */       proerties.put(property.getAlias().toLowerCase(), property.getRealVal());
/*     */     } 
/*     */     
/*  76 */     this.cache.add("PROPERTIES_CACHE_", propertiesCache);
/*  77 */     return propertiesCache;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getPropertyByAlias(String alias) {
/*  82 */     return getByAlias(alias);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateByAlias(SysProperties sysProperties) {
/*  87 */     this.sysPropertiesDao.updateByAlias(sysProperties);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getByAlias(String alias) {
/*  92 */     alias = alias.toLowerCase();
/*  93 */     Map<String, Map<String, String>> enviromentProps = (Map<String, Map<String, String>>)this.cache.getByKey("PROPERTIES_CACHE_");
/*  94 */     if (MapUtil.isEmpty(enviromentProps)) {
/*  95 */       enviromentProps = reloadProperty();
/*     */     }
/*     */     
/*  98 */     String currentEnviroment = AppUtil.getCtxEnvironment();
/*     */     
/* 100 */     if (enviromentProps.containsKey(currentEnviroment)) {
/* 101 */       Map<String, String> currentEnviromentProp = enviromentProps.get(currentEnviroment);
/* 102 */       if (currentEnviromentProp.containsKey(alias)) {
/* 103 */         return currentEnviromentProp.get(alias);
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 108 */     if (!enviromentProps.containsKey(EnvironmentConstant.DEV.key())) {
/* 109 */       return null;
/*     */     }
/*     */     
/* 112 */     return (String)((Map)enviromentProps.get(EnvironmentConstant.DEV.key())).get(alias);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getByAlias(String alias, String defaultValue) {
/* 117 */     String val = getByAlias(alias);
/* 118 */     if (StringUtil.isEmpty(val)) {
/* 119 */       return defaultValue;
/*     */     }
/* 121 */     return val;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getIntByAlias(String alias) {
/* 126 */     String val = getByAlias(alias);
/* 127 */     if (StringUtil.isEmpty(val)) {
/* 128 */       return Integer.valueOf(0);
/*     */     }
/* 130 */     Integer rtn = Integer.valueOf(Integer.parseInt(val));
/* 131 */     return rtn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer getIntByAlias(String alias, Integer defaulValue) {
/* 136 */     String val = getByAlias(alias);
/* 137 */     if (StringUtil.isEmpty(val)) {
/* 138 */       return defaulValue;
/*     */     }
/* 140 */     Integer rtn = Integer.valueOf(Integer.parseInt(val));
/* 141 */     return rtn;
/*     */   }
/*     */ 
/*     */   
/*     */   public Long getLongByAlias(String alias) {
/* 146 */     String val = getByAlias(alias);
/* 147 */     if (StringUtil.isEmpty(val)) {
/* 148 */       return Long.valueOf(0L);
/*     */     }
/* 150 */     Long rtn = Long.valueOf(Long.parseLong(val));
/* 151 */     return rtn;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBooleanByAlias(String alias) {
/* 156 */     String val = getByAlias(alias);
/* 157 */     return Boolean.parseBoolean(val);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getBooleanByAlias(String alias, boolean defaulValue) {
/* 162 */     String val = getByAlias(alias);
/* 163 */     if (StringUtil.isEmpty(val)) {
/* 164 */       return defaulValue;
/*     */     }
/* 166 */     if ("1".equals(val)) {
/* 167 */       return true;
/*     */     }
/* 169 */     return Boolean.parseBoolean(val);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SysProperties> query(QueryFilter queryFilter) {
/* 174 */     List<SysProperties> lst = this.dao.query(queryFilter);
/* 175 */     return lst;
/*     */   }
/*     */ 
/*     */   
/*     */   public SysProperties get(String entityId) {
/* 180 */     SysProperties temp = (SysProperties)this.dao.get(entityId);
/* 181 */     return temp;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysPropertiesManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */