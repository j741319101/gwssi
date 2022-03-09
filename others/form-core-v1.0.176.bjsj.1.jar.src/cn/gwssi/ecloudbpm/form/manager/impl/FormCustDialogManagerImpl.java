/*     */ package cn.gwssi.ecloudbpm.form.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.api.constant.FormCustDialogConditionFieldValueSource;
/*     */ import cn.gwssi.ecloudbpm.form.api.constant.FormCustDialogObjType;
/*     */ import cn.gwssi.ecloudbpm.form.api.constant.FormCustDialogStyle;
/*     */ import cn.gwssi.ecloudbpm.form.dao.FormCustDialogDao;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormCustDialogManager;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormCustDialog;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogConditionField;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogDisplayField;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogReturnField;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogSortField;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.BeanUtils;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.ThreadMapUtil;
/*     */ import cn.gwssi.ecloudframework.base.dao.CommonDao;
/*     */ import cn.gwssi.ecloudframework.base.db.dboper.DbOperator;
/*     */ import cn.gwssi.ecloudframework.base.db.dboper.DbOperatorFactory;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Column;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Table;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.ISysDataSource;
/*     */ import cn.gwssi.ecloudframework.sys.api.service.ISysDataSourceService;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import java.lang.reflect.Method;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("formCustDialogManager")
/*     */ public class FormCustDialogManagerImpl
/*     */   extends BaseManager<String, FormCustDialog>
/*     */   implements FormCustDialogManager
/*     */ {
/*     */   @Resource
/*     */   FormCustDialogDao formCustDialogDao;
/*     */   @Autowired
/*     */   ISysDataSourceService sysDataSourceService;
/*     */   @Autowired
/*     */   CommonDao<?> commonDao;
/*     */   @Autowired
/*     */   IGroovyScriptEngine groovyScriptEngine;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   public FormCustDialog getByKey(String key) {
/*  61 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  62 */     defaultQueryFilter.addFilter("key_", key, QueryOP.EQUAL);
/*  63 */     return (FormCustDialog)queryOne((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> searchObjName(FormCustDialog formCustDialog) {
/*  68 */     ISysDataSource sysDataSource = this.sysDataSourceService.getByKey(formCustDialog.getDsKey());
/*  69 */     JdbcTemplate jdbcTemplate = this.sysDataSourceService.getJdbcTemplateByKey(formCustDialog.getDsKey());
/*  70 */     Map<String, String> map = new HashMap<>();
/*  71 */     DbOperator dbOperator = DbOperatorFactory.newOperator(sysDataSource.getDbType(), jdbcTemplate);
/*     */     
/*  73 */     if (FormCustDialogObjType.TABLE.equalsWithKey(formCustDialog.getObjType())) {
/*  74 */       map = dbOperator.getTableNames(formCustDialog.getObjName());
/*  75 */     } else if (FormCustDialogObjType.VIEW.equalsWithKey(formCustDialog.getObjType())) {
/*  76 */       List<String> viewNames = dbOperator.getViewNames(formCustDialog.getObjName());
/*  77 */       for (String viewName : viewNames) {
/*  78 */         map.put(viewName, viewName);
/*     */       }
/*     */     } 
/*  81 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Table<Column> getTable(FormCustDialog formCustDialog) {
/*     */     try {
/*  87 */       ISysDataSource sysDataSource = this.sysDataSourceService.getByKey(formCustDialog.getDsKey());
/*  88 */       JdbcTemplate jdbcTemplate = this.sysDataSourceService.getJdbcTemplateByKey(formCustDialog.getDsKey());
/*  89 */       DbOperator dbOperator = DbOperatorFactory.newOperator(sysDataSource.getDbType(), jdbcTemplate);
/*  90 */       Table<Column> table = null;
/*     */       
/*  92 */       if (FormCustDialogObjType.TABLE.equalsWithKey(formCustDialog.getObjType())) {
/*  93 */         table = dbOperator.getTable(formCustDialog.getObjName());
/*  94 */       } else if (FormCustDialogObjType.VIEW.equalsWithKey(formCustDialog.getObjType())) {
/*  95 */         table = dbOperator.getView(formCustDialog.getObjName());
/*     */       } 
/*  97 */       return table;
/*  98 */     } catch (Exception e) {
/*  99 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<?> data(FormCustDialog formCustDialog, QueryFilter queryFilter) {
/*     */     List<?> list;
/* 105 */     String sql = analyseSql(formCustDialog);
/* 106 */     handleQueryFilter(formCustDialog, queryFilter);
/* 107 */     if ("interface".equals(formCustDialog.getDataSource())) {
/* 108 */       return getDataByInterface(formCustDialog, queryFilter);
/*     */     }
/*     */ 
/*     */     
/* 112 */     boolean isPage = ((Boolean)ThreadMapUtil.getOrDefault("isPage", Boolean.valueOf(true))).booleanValue();
/* 113 */     if (isPage) {
/* 114 */       list = this.commonDao.queryForListPage(sql, queryFilter);
/*     */     } else {
/* 116 */       queryFilter.setPage(null);
/* 117 */       list = this.commonDao.queryForListPage(sql, queryFilter);
/*     */     } 
/* 119 */     return list;
/*     */   }
/*     */   
/*     */   private List getDataByInterface(FormCustDialog customDialog, QueryFilter queryFilter) {
/* 123 */     String beanMethod = customDialog.getObjName();
/* 124 */     if (StringUtil.isEmpty(beanMethod)) throw new RuntimeException("自定义对话框数据服务接口不能为空！");
/*     */     
/* 126 */     String[] aryHandler = beanMethod.split("[.]");
/* 127 */     if (aryHandler == null || aryHandler.length != 2) throw new RuntimeException("自定义对话框数据服务接口格式不正确！" + beanMethod);
/*     */ 
/*     */     
/* 130 */     String beanId = aryHandler[0];
/* 131 */     String method = aryHandler[1];
/*     */     
/* 133 */     Object serviceBean = AppUtil.getBean(beanId);
/* 134 */     if (serviceBean == null) return null; 
/*     */     try {
/* 136 */       Method invokeMethod = serviceBean.getClass().getDeclaredMethod(method, new Class[] { QueryFilter.class });
/* 137 */       return (List)invokeMethod.invoke(serviceBean, new Object[] { queryFilter });
/* 138 */     } catch (Exception e) {
/* 139 */       throw new RuntimeException("查询异常！" + e.getMessage(), e);
/*     */     } 
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
/*     */ 
/*     */   
/*     */   private String analyseSql(FormCustDialog formCustDialog) {
/* 155 */     Set<String> columnNameSet = new HashSet<>();
/* 156 */     if (FormCustDialogStyle.LIST.equalsWithKey(formCustDialog.getStyle())) {
/* 157 */       for (FormCustDialogDisplayField field : formCustDialog.getDisplayFields()) {
/* 158 */         columnNameSet.add(field.getColumnName());
/*     */       }
/*     */     }
/* 161 */     if (FormCustDialogStyle.TREE.equalsWithKey(formCustDialog.getStyle())) {
/* 162 */       columnNameSet.add(formCustDialog.getTreeConfig().getPid());
/* 163 */       columnNameSet.add(formCustDialog.getTreeConfig().getId());
/* 164 */       columnNameSet.add(formCustDialog.getTreeConfig().getShowColumn());
/*     */     } 
/*     */     
/* 167 */     for (FormCustDialogReturnField field : formCustDialog.getReturnFields()) {
/* 168 */       columnNameSet.add(field.getColumnName());
/*     */     }
/* 170 */     for (FormCustDialogSortField field : formCustDialog.getSortFields()) {
/* 171 */       columnNameSet.add(field.getColumnName());
/*     */     }
/*     */ 
/*     */     
/* 175 */     StringBuilder displaySql = new StringBuilder();
/* 176 */     for (String columnName : columnNameSet) {
/* 177 */       if (displaySql.length() > 0) {
/* 178 */         displaySql.append(",");
/*     */       }
/* 180 */       displaySql.append(columnName);
/*     */     } 
/*     */     
/* 183 */     return "select " + displaySql.toString() + " from " + formCustDialog.getObjName();
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
/*     */   
/*     */   private QueryFilter handleQueryFilter(FormCustDialog formCustDialog, QueryFilter queryFilter) {
/* 197 */     System.out.println(ContextUtil.getCurrentGroupId());
/* 198 */     for (FormCustDialogConditionField field : formCustDialog.getConditionFields()) {
/* 199 */       if (FormCustDialogConditionFieldValueSource.FIXED_VALUE.equalsWithKey(field.getValueSource())) {
/* 200 */         Object value = BeanUtils.getValue(field.getDbType(), QueryOP.getByVal(field.getCondition()), field.getValue().getText());
/* 201 */         queryFilter.addFilter(field.getColumnName(), value, QueryOP.getByVal(field.getCondition()));
/*     */       } 
/* 203 */       if (FormCustDialogConditionFieldValueSource.SCRIPT.equalsWithKey(field.getValueSource())) {
/* 204 */         Object value = this.groovyScriptEngine.executeObject(field.getValue().getText(), queryFilter.getParams());
/* 205 */         queryFilter.addFilter(field.getColumnName(), value, QueryOP.getByVal(field.getCondition()));
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 210 */     for (FormCustDialogSortField field : formCustDialog.getSortFields()) {
/* 211 */       queryFilter.addFieldSort(field.getColumnName(), field.getSortType());
/*     */     }
/*     */     
/* 214 */     return queryFilter;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean existsByKey(String key) {
/* 219 */     return this.formCustDialogDao.existsByKey(key);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FormCustDialog> query(QueryFilter queryFilter) {
/* 225 */     List<FormCustDialog> lst = this.dao.query(queryFilter);
/* 226 */     return lst;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/impl/FormCustDialogManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */