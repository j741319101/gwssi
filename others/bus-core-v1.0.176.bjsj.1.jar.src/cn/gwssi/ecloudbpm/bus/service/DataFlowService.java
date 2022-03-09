/*     */ package cn.gwssi.ecloudbpm.bus.service;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IDataFlowConf;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IDataFlowSql;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusDataFlowRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusFormula;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*     */ import cn.gwssi.ecloudbpm.bus.model.DataFlowConf;
/*     */ import cn.gwssi.ecloudbpm.bus.model.DataFlowSql;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.ColumnType;
/*     */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.datasource.DbContextHolder;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.ISysDataSource;
/*     */ import cn.gwssi.infrastructure.grammar.interpret.api.ColumnTypeEnum;
/*     */ import cn.gwssi.infrastructure.grammar.interpret.api.DataTypeEnum;
/*     */ import cn.gwssi.infrastructure.grammar.interpret.api.InputParam;
/*     */ import cn.gwssi.infrastructure.grammar.interpret.api.Table;
/*     */ import cn.gwssi.infrastructure.grammar.interpret.api.Translator;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ @Service
/*     */ public class DataFlowService implements IDataFlowService {
/*  41 */   protected final Logger LOGGER = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Autowired
/*     */   BusinessDataService businessDataService;
/*     */   @Resource
/*     */   BusinessObjectManager businessObjectManager;
/*     */   @Resource
/*     */   BusinessTableManager businessTableManager;
/*     */   @Autowired
/*     */   ISysDataSourceService iSysDataSourceService;
/*     */   @Autowired
/*     */   BusinessColumnManager businessColumnManager;
/*     */   @Autowired
/*     */   CommonDao<?> commonDao;
/*     */   @Autowired
/*     */   ICurrentContext iCurrentContext;
/*     */   
/*     */   public List<IDataFlowSql> analysis(JSONObject data, Set<String> opr) {
/*  59 */     DataFlowConf dataFlowConf = new DataFlowConf();
/*  60 */     dataFlowConf.setAddOpr(opr);
/*  61 */     dataFlowConf.setDelOpr(opr);
/*  62 */     dataFlowConf.setModOpr(opr);
/*  63 */     return analysis(data, (IDataFlowConf)dataFlowConf);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IDataFlowSql> analysis(JSONObject data, IDataFlowConf dataFlowConf) {
/*  68 */     List<IDataFlowSql> lstDataFlowSql = new ArrayList<>();
/*     */     
/*  70 */     if (null != dataFlowConf) {
/*  71 */       Map<String, BusinessData> mapOldData = SearchOldData(data);
/*     */       
/*  73 */       for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)data.entrySet()) {
/*  74 */         String boKey = entry.getKey();
/*  75 */         JSONObject boData = (JSONObject)entry.getValue();
/*  76 */         BusinessData businessData = (BusinessData)this.businessDataService.parseBusinessData(boData, boKey);
/*  77 */         findDataFlowSql(businessData, lstDataFlowSql, mapOldData, dataFlowConf);
/*     */       } 
/*     */       
/*  80 */       for (Map.Entry<String, BusinessData> entry : mapOldData.entrySet()) {
/*  81 */         BusinessData businessData = entry.getValue();
/*  82 */         makeDataFlowSql(businessData, lstDataFlowSql, dataFlowConf.getDelOpr());
/*     */       } 
/*     */     } 
/*  85 */     return lstDataFlowSql;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<IDataFlowSql> analysisDelete(JSONObject data, IDataFlowConf dataFlowConf) {
/*  96 */     List<IDataFlowSql> lstDataFlowSql = new ArrayList<>();
/*     */     
/*  98 */     if (null != dataFlowConf) {
/*  99 */       Map<String, BusinessData> mapOldData = SearchOldData(data);
/*     */       
/* 101 */       for (Map.Entry<String, BusinessData> entry : mapOldData.entrySet()) {
/* 102 */         BusinessData businessData = entry.getValue();
/* 103 */         makeDataFlowSql(businessData, lstDataFlowSql, dataFlowConf.getDelOpr());
/*     */       } 
/*     */     } 
/* 106 */     return lstDataFlowSql;
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
/*     */   private Map<String, BusinessData> SearchOldData(JSONObject data) {
/* 119 */     String id = "";
/* 120 */     String boKeyMain = "";
/* 121 */     JSONObject boDataMain = null;
/* 122 */     Iterator<Map.Entry<String, Object>> iterator = data.entrySet().iterator(); if (iterator.hasNext()) { Map.Entry<String, Object> entry = iterator.next();
/* 123 */       boKeyMain = entry.getKey();
/* 124 */       boDataMain = (JSONObject)entry.getValue();
/* 125 */       id = boDataMain.getString("ID"); }
/*     */ 
/*     */ 
/*     */     
/* 129 */     Map<String, BusinessData> mapOldData = new HashMap<>();
/*     */     
/* 131 */     if (StringUtils.isNotEmpty(id)) {
/* 132 */       BusinessObject businessObject = this.businessObjectManager.getFilledByKey(boKeyMain);
/* 133 */       BusinessData businessData = (BusinessData)this.businessDataService.parseBusinessData(boDataMain, boKeyMain);
/*     */       
/* 135 */       findOldData(businessData, mapOldData);
/*     */     } 
/* 137 */     return mapOldData;
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute(List<IDataFlowSql> lstDataFlowSql) {
/* 142 */     if (null != lstDataFlowSql && lstDataFlowSql.size() > 0) {
/* 143 */       lstDataFlowSql.forEach(dataFlowSql -> {
/*     */             DbContextHolder.setDataSource(dataFlowSql.getDsKey(), dataFlowSql.getDbType());
/*     */             String[] sqls = dataFlowSql.getSql();
/*     */             for (String sql : sqls) {
/*     */               int num = this.commonDao.execute(sql);
/*     */               if (num != 0) {
/*     */                 break;
/*     */               }
/*     */             } 
/*     */           });
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Object saveData(JSONObject data, IDataFlowConf dataFlowConf) {
/* 159 */     Object pk = null;
/* 160 */     this.LOGGER.info("-----------------------数据流sql解析开始-----------------------");
/* 161 */     List<IDataFlowSql> lstDataFlowSql = analysis(data, dataFlowConf);
/* 162 */     this.LOGGER.info("-----------------------数据流sql解析结束-----------------------");
/* 163 */     this.LOGGER.info("-----------------------保存数据开始-----------------------");
/* 164 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)data.entrySet()) {
/* 165 */       String boKey = entry.getKey();
/* 166 */       JSONObject boData = (JSONObject)entry.getValue();
/* 167 */       BusinessData businessData = (BusinessData)this.businessDataService.parseBusinessData(boData, boKey);
/* 168 */       BusinessDataPersistenceServiceFactory.saveData(businessData);
/*     */       
/* 170 */       pk = businessData.getPk();
/*     */     } 
/* 172 */     this.LOGGER.info("-----------------------保存数据结束-----------------------");
/* 173 */     this.LOGGER.info("-----------------------执行sql开始-----------------------");
/* 174 */     execute(lstDataFlowSql);
/* 175 */     this.LOGGER.info("-----------------------执行sql结束-----------------------");
/* 176 */     return pk;
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeData(IBusinessObject businessObject, String id, IDataFlowConf dataFlowConf) {
/* 181 */     JSONObject data = new JSONObject();
/* 182 */     data.put(businessObject.getKey(), this.businessDataService.getFormDefData(businessObject, id));
/* 183 */     this.LOGGER.info("-----------------------数据流sql解析开始-----------------------");
/* 184 */     List<IDataFlowSql> lstDataFlowSql = analysisDelete(data, dataFlowConf);
/* 185 */     this.LOGGER.info("-----------------------数据流sql解析结束-----------------------");
/* 186 */     this.LOGGER.info("-----------------------删除数据开始-----------------------");
/* 187 */     this.businessDataService.removeData(businessObject, id);
/* 188 */     this.LOGGER.info("-----------------------删除数据结束-----------------------");
/* 189 */     this.LOGGER.info("-----------------------执行sql开始-----------------------");
/* 190 */     execute(lstDataFlowSql);
/* 191 */     this.LOGGER.info("-----------------------执行sql结束-----------------------");
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
/*     */   private void findOldData(BusinessData businessData, Map<String, BusinessData> mapOldData) {
/* 203 */     String id = "";
/* 204 */     if (businessData.getData().containsKey("ID")) {
/* 205 */       id = String.valueOf(businessData.getData().get("ID"));
/*     */     }
/* 207 */     if (StringUtils.isNotEmpty(id)) {
/* 208 */       String tableKey = businessData.getBusTableRel().getTableKey();
/* 209 */       mapOldData.put(tableKey + "_" + id, businessData);
/* 210 */       if (businessData.getChildren().size() > 0) {
/* 211 */         for (String key : businessData.getChildren().keySet()) {
/* 212 */           List<BusinessData> children = (List<BusinessData>)businessData.getChildren().get(key);
/* 213 */           if (children.size() > 0) {
/* 214 */             for (BusinessData temp : children) {
/* 215 */               findOldData(temp, mapOldData);
/*     */             }
/*     */           }
/*     */         } 
/*     */       }
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
/*     */   private void findDataFlowSql(BusinessData businessData, List<IDataFlowSql> lstDataFlowSql, Map<String, BusinessData> mapOldData, IDataFlowConf dataFlowConf) {
/* 234 */     Set<String> formulaIds = dataFlowConf.getAddOpr();
/*     */     
/* 236 */     if (businessData.getData().containsKey("ID")) {
/* 237 */       String id = String.valueOf(businessData.getData().get("ID"));
/* 238 */       String tableKey = businessData.getBusTableRel().getTableKey();
/* 239 */       mapOldData.remove(tableKey + "_" + id);
/* 240 */       formulaIds = dataFlowConf.getModOpr();
/*     */     } 
/*     */     
/* 243 */     makeDataFlowSql(businessData, lstDataFlowSql, formulaIds);
/* 244 */     if (businessData.getChildren().size() > 0) {
/* 245 */       for (String key : businessData.getChildren().keySet()) {
/* 246 */         List<BusinessData> children = (List<BusinessData>)businessData.getChildren().get(key);
/* 247 */         if (children.size() > 0) {
/* 248 */           for (BusinessData temp : children) {
/* 249 */             findDataFlowSql(temp, lstDataFlowSql, mapOldData, dataFlowConf);
/*     */           }
/*     */         }
/*     */       } 
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
/*     */   private void makeDataFlowSql(BusinessData businessData, List<IDataFlowSql> lstDataFlowSql, Set<String> formulaIds) {
/* 267 */     if (null != formulaIds && formulaIds.size() > 0) {
/* 268 */       BusTableRel busTableRel = businessData.getBusTableRel();
/* 269 */       List<BusDataFlowRel> dataFlowLinks = busTableRel.getDataFlowLinks();
/*     */       
/* 271 */       if (null != dataFlowLinks && dataFlowLinks.size() > 0) {
/* 272 */         String tableKey = busTableRel.getTableKey();
/* 273 */         BusinessTable businessTable = this.businessTableManager.getFilledByKey(tableKey);
/* 274 */         List<BusinessColumn> lstBusinessColumn = this.businessColumnManager.getByTableId(businessTable.getId());
/*     */         
/* 276 */         Map<String, DataTypeEnum> mapDataTypeEnum = new HashMap<>();
/* 277 */         lstBusinessColumn.forEach(businessColumn -> {
/*     */               DataTypeEnum dataTypeEnum;
/*     */               switch (ColumnType.getByKey(businessColumn.getType())) {
/*     */                 case NUMBER:
/*     */                   dataTypeEnum = DataTypeEnum.NUM;
/*     */                   break;
/*     */                 
/*     */                 default:
/*     */                   dataTypeEnum = DataTypeEnum.TEXT;
/*     */                   break;
/*     */               } 
/*     */               
/*     */               mapDataTypeEnum.put(businessColumn.getKey().toUpperCase(), dataTypeEnum);
/*     */             });
/* 291 */         for (Iterator<BusDataFlowRel> iterator = dataFlowLinks.iterator(); iterator.hasNext(); ) { BusDataFlowRel busDataFlowRel = iterator.next();
/* 292 */           List<BusFormula> formulas = busDataFlowRel.getFormulas();
/* 293 */           formulas.forEach(formula -> {
/*     */                 if (formulaIds.contains(formula.getId())) {
/*     */                   Table sourceTable = new Table(tableKey.toUpperCase());
/*     */                   Map<String, InputParam> sourceColumnProps = new HashMap<>();
/*     */                   for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)businessData.getData().entrySet()) {
/*     */                     String columnKey = ((String)entry.getKey()).toUpperCase();
/*     */                     String columnValue = String.valueOf((entry.getValue() == null) ? "" : entry.getValue());
/*     */                     DataTypeEnum DataTypeEnum = (DataTypeEnum)mapDataTypeEnum.get(columnKey);
/*     */                     sourceColumnProps.put(columnKey, new InputParam(columnKey, columnValue, DataTypeEnum));
/*     */                   } 
/*     */                   sourceTable.setColumnProps(sourceColumnProps);
/*     */                   Table targetTable = new Table(busDataFlowRel.getTableKey().toUpperCase());
/*     */                   BusinessTable targetBusinessTable = this.businessTableManager.getFilledByKey(busDataFlowRel.getTableKey());
/*     */                   Map<String, InputParam> targetColumnProps = new HashMap<>();
/*     */                   List<BusinessColumn> lstTargetBusinessColumn = this.businessColumnManager.getByTableId(targetBusinessTable.getId());
/*     */                   lstTargetBusinessColumn.forEach(());
/*     */                   targetTable.setColumnProps(targetColumnProps);
/*     */                   String[] sqls = Translator.translateFormula(formula.getFormula().toUpperCase(), sourceTable, targetTable);
/*     */                   String dsKey = businessTable.getDsKey();
/*     */                   ISysDataSource sysDataSource = this.iSysDataSourceService.getByKey(dsKey);
/*     */                   String dbType = sysDataSource.getDbType();
/*     */                   lstDataFlowSql.add(new DataFlowSql(sqls, dsKey, dbType));
/*     */                 } 
/*     */               }); }
/*     */       
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/service/DataFlowService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */