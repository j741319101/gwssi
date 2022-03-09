/*     */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.datasource.DbContextHolder;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.sys.api.constant.RightsObjectConstants;
/*     */ import cn.gwssi.ecloudframework.sys.core.dao.WorkbenchPanelDao;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.SysAuthorizationManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.WorkbenchLayoutManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.WorkbenchPanelManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.WorkbenchPanel;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Random;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("workbenchPanelManager")
/*     */ public class WorkbenchPanelManagerImpl
/*     */   extends BaseManager<String, WorkbenchPanel>
/*     */   implements WorkbenchPanelManager
/*     */ {
/*     */   @Resource
/*     */   WorkbenchPanelDao workbenchPanelDao;
/*     */   @Resource
/*     */   WorkbenchLayoutManager workbenchLayoutManager;
/*     */   @Resource
/*     */   SysAuthorizationManager sysAuthorizationManager;
/*     */   
/*     */   public List<WorkbenchPanel> getByUserId(String userId) {
/*  47 */     Map<String, Object> userPermission = this.sysAuthorizationManager.getUserRightsSql(RightsObjectConstants.WORKBENCH, userId, "p.id_");
/*  48 */     userPermission.put("userId", userId);
/*     */     
/*  50 */     userPermission.put("dbType", DbContextHolder.getDbType());
/*     */     
/*  52 */     List<WorkbenchPanel> layOut = this.workbenchPanelDao.getByUser(userPermission);
/*     */     
/*  54 */     if (CollectionUtil.isEmpty(layOut)) {
/*  55 */       userPermission.put("userId", "default_layout");
/*  56 */       layOut = this.workbenchPanelDao.getByUser(userPermission);
/*     */     } 
/*     */     
/*  59 */     return layOut;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WorkbenchPanel> getBylayoutKey(String layoutKey) {
/*  65 */     String dbType = DbContextHolder.getDbType();
/*  66 */     return this.workbenchPanelDao.getBylayoutKey(layoutKey, dbType);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<WorkbenchPanel> getMyUsablePanels(QueryFilter query) {
/*  72 */     String layoutKey = (String)query.getParams().get("layoutKey");
/*  73 */     if (StringUtil.isNotEmpty(layoutKey)) {
/*  74 */       return this.workbenchPanelDao.query();
/*     */     }
/*     */     
/*  77 */     String userId = ContextUtil.getCurrentUserId();
/*  78 */     Map<String, Object> userPermission = this.sysAuthorizationManager.getUserRightsSql(RightsObjectConstants.WORKBENCH, userId, null);
/*  79 */     query.getParams().putAll(userPermission);
/*     */     
/*  81 */     DefaultQueryFilter queryFilter = (DefaultQueryFilter)query;
/*  82 */     queryFilter.setPage(null);
/*     */     
/*  84 */     return this.workbenchPanelDao.getUsablePanelsByUserRight((QueryFilter)queryFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JSON getPanelData(Map<String, String> requstParam) {
/*  90 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSON getDataByInterFace(QueryFilter filter, String dataSource) {
/*  97 */     if (StringUtil.isEmpty(dataSource)) throw new RuntimeException("自定义对话框数据服务接口不能为空！");
/*     */     
/*  99 */     String[] aryHandler = dataSource.split("[.]");
/* 100 */     if (aryHandler == null || aryHandler.length != 2) throw new RuntimeException("自定义对话框数据服务接口格式不正确！" + dataSource);
/*     */     
/* 102 */     String beanId = aryHandler[0];
/* 103 */     String method = aryHandler[1];
/*     */ 
/*     */     
/* 106 */     Object serviceBean = AppUtil.getBean(beanId);
/* 107 */     if (serviceBean == null) return null; 
/* 108 */     Object objct = null;
/*     */     try {
/* 110 */       objct = invokeMethod(serviceBean, method, filter);
/* 111 */     } catch (Exception e) {
/* 112 */       throw new RuntimeException("查询异常！" + e.getMessage(), e);
/*     */     } 
/*     */     
/* 115 */     return (JSON)JSON.toJSON(objct);
/*     */   }
/*     */ 
/*     */   
/*     */   private Object invokeMethod(Object serviceBean, String method, QueryFilter filter) throws SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
/* 120 */     Class<?> clazz = serviceBean.getClass();
/*     */     
/* 122 */     Method invokeMethod = null;
/* 123 */     Object o = null;
/*     */     
/*     */     try {
/* 126 */       invokeMethod = clazz.getMethod(method, new Class[0]);
/* 127 */       o = invokeMethod.invoke(serviceBean, new Object[0]);
/*     */     }
/* 129 */     catch (NoSuchMethodException noSuchMethodException) {}
/*     */     
/* 131 */     if (invokeMethod == null) {
/*     */       try {
/* 133 */         invokeMethod = clazz.getMethod(method, new Class[] { QueryFilter.class });
/* 134 */         o = invokeMethod.invoke(serviceBean, new Object[] { filter });
/* 135 */       } catch (NoSuchMethodException e) {
/* 136 */         throw new BusinessException(String.format("被调用方法%s.%s 入参为QueryFilter,或者为空", new Object[] { serviceBean, method }));
/*     */       } 
/*     */     }
/*     */     
/* 140 */     if (void.class == invokeMethod.getReturnType()) {
/* 141 */       throw new BusinessException(String.format("被调用方法%s.%s 返回值不能为void！", new Object[] { serviceBean, method }));
/*     */     }
/*     */     
/* 144 */     return o;
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
/*     */   
/*     */   public JSON getTestData() {
/* 157 */     String json = "[[\"product\", \"纯牛奶\", \"咖啡\", \"矿泉水\"],[\"2015\"," + getRandomInt() + ", " + getRandomInt() + ", " + getRandomInt() + "], [\"2016\", " + getRandomInt() + ", " + getRandomInt() + ", " + getRandomInt() + "],[\"2017\", " + getRandomInt() + ", " + getRandomInt() + ", " + getRandomInt() + "], [\"2018\"," + getRandomInt() + ", " + getRandomInt() + ", " + getRandomInt() + "]]";
/* 158 */     JSONArray jsonArray = JSONArray.parseArray(json);
/*     */     
/* 160 */     return (JSON)jsonArray;
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
/*     */   
/*     */   public JSON getPieData() {
/* 173 */     String json = "[[\"纯牛奶\"," + getRandomInt() + "],[\"咖啡\"," + getRandomInt() + "],[\"矿泉水\"," + getRandomInt() + "],[\"碳酸饮料\"," + getRandomInt() + "]]";
/* 174 */     JSONArray jsonArray = JSONArray.parseArray(json);
/* 175 */     return (JSON)jsonArray;
/*     */   }
/*     */   
/*     */   private int getRandomInt() {
/* 179 */     Random rand = new Random();
/* 180 */     return rand.nextInt(6000);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/WorkbenchPanelManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */