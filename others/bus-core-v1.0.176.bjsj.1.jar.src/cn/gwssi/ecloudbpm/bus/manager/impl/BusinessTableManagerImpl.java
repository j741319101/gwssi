/*     */ package cn.gwssi.ecloudbpm.bus.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.dao.BusinessTableDao;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusColumnCtrlManager;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessColumnManager;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessTableManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusColumnCtrl;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*     */ import cn.gwssi.ecloudbpm.bus.util.BusinessTableCacheUtil;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.DbType;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessError;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.datasource.DbContextHolder;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Table;
/*     */ import cn.gwssi.ecloudframework.base.db.tableoper.TableOperator;
/*     */ import cn.gwssi.ecloudframework.base.db.tableoper.TableOperatorFactory;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.sys.api.service.ISysDataSourceService;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class BusinessTableManagerImpl
/*     */   extends BaseManager<String, BusinessTable>
/*     */   implements BusinessTableManager
/*     */ {
/*     */   @Resource
/*     */   BusinessTableDao businessTableDao;
/*     */   @Autowired
/*     */   BusinessColumnManager businessColumnManager;
/*     */   @Autowired
/*     */   BusColumnCtrlManager busColCtrlManager;
/*     */   @Autowired
/*     */   ISysDataSourceService sysDataSourceService;
/*     */   @Resource
/*     */   JdbcTemplate jdbcTemplate;
/*     */   
/*     */   public void save(BusinessTable businessTable) {
/*  53 */     if (DbType.ORACLE.equalsWithKey(DbContextHolder.getDataSourceDbType(businessTable.getDsKey()))) {
/*  54 */       businessTable.setName(businessTable.getName().toUpperCase());
/*     */     }
/*  56 */     if (StringUtil.isEmpty(businessTable.getId())) {
/*  57 */       businessTable.setId(IdUtil.getSuid());
/*     */       
/*  59 */       if (!businessTable.isExternal() && newTableOperator(businessTable).isTableCreated()) {
/*  60 */         throw new BusinessMessage("表[" + businessTable.getName() + "]已经存在数据库中");
/*     */       }
/*  62 */       create((Serializable)businessTable);
/*     */     } else {
/*  64 */       update((Serializable)businessTable);
/*  65 */       this.busColCtrlManager.removeByTableId(businessTable.getId());
/*  66 */       this.businessColumnManager.removeByTableId(businessTable.getId());
/*     */     } 
/*     */     
/*  69 */     for (BusinessColumn businessColumn : businessTable.getColumns()) {
/*  70 */       if (StringUtil.isEmpty(businessColumn.getId())) {
/*  71 */         businessColumn.setId(IdUtil.getSuid());
/*     */       }
/*     */       
/*  74 */       if (DbType.ORACLE.equalsWithKey(DbContextHolder.getDataSourceDbType(businessTable.getDsKey()))) {
/*  75 */         businessColumn.setName(businessColumn.getName().toUpperCase());
/*     */       }
/*  77 */       businessColumn.setTable(businessTable);
/*  78 */       businessColumn.setTableId(businessTable.getId());
/*  79 */       this.businessColumnManager.create(businessColumn);
/*  80 */       BusColumnCtrl ctrl = businessColumn.getCtrl();
/*  81 */       if (businessColumn.isPrimary()) {
/*     */         continue;
/*     */       }
/*  84 */       if (ctrl == null) {
/*  85 */         throw new BusinessMessage("字段必须配置控件！");
/*     */       }
/*  87 */       if (StringUtil.isEmpty(ctrl.getId())) {
/*  88 */         ctrl.setId(IdUtil.getSuid());
/*     */       }
/*  90 */       ctrl.setColumnId(businessColumn.getId());
/*  91 */       this.busColCtrlManager.create(businessColumn.getCtrl());
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  98 */     newTableOperator(businessTable).syncColumn();
/*  99 */     BusinessTableCacheUtil.put(businessTable);
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessTable getByKey(String key) {
/* 104 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 105 */     defaultQueryFilter.addFilter("key_", key, QueryOP.EQUAL);
/* 106 */     return (BusinessTable)queryOne((QueryFilter)defaultQueryFilter);
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
/*     */   private void fill(BusinessTable businessTable) {
/* 118 */     if (businessTable == null) {
/*     */       return;
/*     */     }
/*     */     
/* 122 */     List<BusinessColumn> columns = this.businessColumnManager.getByTableId(businessTable.getId());
/* 123 */     for (BusinessColumn column : columns) {
/* 124 */       column.setCtrl(this.busColCtrlManager.getByColumnId(column.getId()));
/* 125 */       column.setTable(businessTable);
/*     */     } 
/* 127 */     businessTable.setColumns(columns);
/*     */     
/* 129 */     TableOperator tableOperator = newTableOperator(businessTable);
/* 130 */     businessTable.setCreatedTable(tableOperator.isTableCreated());
/*     */   }
/*     */ 
/*     */   
/*     */   public TableOperator newTableOperator(BusinessTable businessTable) {
/* 135 */     JdbcTemplate dataSourceJdbcTemplate = this.sysDataSourceService.getJdbcTemplateByKey(businessTable.getDsKey());
/* 136 */     return TableOperatorFactory.newOperator(DbContextHolder.getDataSourceDbType(businessTable.getDsKey()), (Table)businessTable, dataSourceJdbcTemplate);
/*     */   }
/*     */ 
/*     */   
/*     */   public TableOperator newTableOperatorCheckExist(BusinessTable businessTable) {
/* 141 */     JdbcTemplate dataSourceJdbcTemplate = this.sysDataSourceService.getJdbcTemplateByKey(businessTable.getDsKey());
/* 142 */     TableOperator tableOperator = TableOperatorFactory.newOperator(DbContextHolder.getDataSourceDbType(businessTable.getDsKey()), (Table)businessTable, dataSourceJdbcTemplate);
/* 143 */     if (!tableOperator.isTableCreated()) {
/* 144 */       throw new BusinessError("实体【" + businessTable.getComment() + "】对应的表[" + businessTable.getName() + "]不存在数据库中！<br/> 请在实体中点击生成表。或者修改业务对象持久化方式为“实例表”！");
/*     */     }
/* 146 */     return tableOperator;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessTable getFilledByKey(String key) {
/* 151 */     BusinessTable businessTable = BusinessTableCacheUtil.get(key);
/* 152 */     if (businessTable != null) {
/* 153 */       return businessTable;
/*     */     }
/* 155 */     businessTable = getByKey(key);
/* 156 */     fill(businessTable);
/* 157 */     BusinessTableCacheUtil.put(businessTable);
/* 158 */     return businessTable;
/*     */   }
/*     */ 
/*     */   
/*     */   public void remove(String entityId) {
/* 163 */     BusinessTable table = (BusinessTable)get(entityId);
/* 164 */     if (table == null) {
/*     */       return;
/*     */     }
/* 167 */     List<String> boNames = this.jdbcTemplate.queryForList("select name_ from bus_object where relation_json_ like  '%\"tableKey\":\"" + table.getKey() + "\"%'", String.class);
/* 168 */     if (CollectionUtil.isNotEmpty(boNames)) {
/* 169 */       throw new BusinessMessage("业务对象:" + boNames.toString() + "还在使用实体， 删除实体失败！");
/*     */     }
/*     */     
/* 172 */     super.remove(entityId);
/* 173 */     this.businessColumnManager.removeByTableId(entityId);
/* 174 */     this.busColCtrlManager.removeByTableId(entityId);
/* 175 */     BusinessTableCacheUtil.removeByKey(table.getKey());
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BusinessTable> getEntities(String groupId) {
/* 180 */     return this.businessTableDao.getEntities(groupId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BusinessTable> queryByMetadata(QueryFilter queryFilter) {
/* 185 */     return this.businessTableDao.queryByMetadata(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer countByTypeId(String typeId) {
/* 190 */     return this.businessTableDao.countByTypeId(typeId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/impl/BusinessTableManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */