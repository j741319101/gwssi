/*     */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.constant.SysStatusCode;
/*     */ import cn.gwssi.ecloudframework.sys.core.dao.DataDictDao;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.DataDictManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.SysTreeManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.SysTreeNodeManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.DataDict;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SysTree;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SysTreeNode;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.poi.ss.usermodel.Cell;
/*     */ import org.apache.poi.ss.usermodel.CellType;
/*     */ import org.apache.poi.xssf.usermodel.XSSFCell;
/*     */ import org.apache.poi.xssf.usermodel.XSSFRow;
/*     */ import org.apache.poi.xssf.usermodel.XSSFSheet;
/*     */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("dataDictManager")
/*     */ public class DataDictManagerImpl
/*     */   extends BaseManager<String, DataDict>
/*     */   implements DataDictManager
/*     */ {
/*     */   @Resource
/*     */   DataDictDao dataDictDao;
/*     */   @Resource
/*     */   SysTreeNodeManager sysTreeNodeMananger;
/*     */   @Resource
/*     */   SysTreeManager sysTreeMananger;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   public List<DataDict> getDictNodeList(String dictKey, Boolean hasRoot) {
/*  56 */     List<DataDict> lst = this.dataDictDao.getDictNodeList(dictKey, hasRoot);
/*  57 */     return lst;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void create(DataDict dataDict) {
/*  63 */     Integer count = Integer.valueOf(0);
/*  64 */     if ("dict".equals(dataDict.getDictType())) {
/*  65 */       dataDict.setDictKey(dataDict.getKey());
/*  66 */       count = this.dataDictDao.isExistDict(dataDict.getKey(), null);
/*     */     } else {
/*  68 */       count = this.dataDictDao.isExistNode(dataDict.getDictKey(), dataDict.getKey(), null);
/*     */     } 
/*     */     
/*  71 */     if (count.intValue() != 0) {
/*  72 */       throw new BusinessMessage(dataDict.getKey() + "字典已经存在", SysStatusCode.PARAM_ILLEGAL);
/*     */     }
/*     */     
/*  75 */     super.create((Serializable)dataDict);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void update(DataDict dataDict) {
/*  81 */     int count = 0;
/*  82 */     if ("dict".equals(dataDict.getDictType())) {
/*  83 */       dataDict.setDictKey(dataDict.getKey());
/*  84 */       count = this.dataDictDao.isExistDict(dataDict.getKey(), dataDict.getId()).intValue();
/*     */     } else {
/*  86 */       count = this.dataDictDao.isExistNode(dataDict.getKey(), dataDict.getDictKey(), dataDict.getId()).intValue();
/*     */     } 
/*     */     
/*  89 */     if (count != 0) {
/*  90 */       throw new BusinessMessage(dataDict.getKey() + "字典Key已经存在", SysStatusCode.PARAM_ILLEGAL);
/*     */     }
/*     */     
/*  93 */     super.update((Serializable)dataDict);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONArray getDictTree() {
/*  99 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 100 */     defaultQueryFilter.addFilter("dict_type_", "dict", QueryOP.EQUAL);
/*     */     
/* 102 */     List<DataDict> dicts = this.dataDictDao.query((QueryFilter)defaultQueryFilter);
/*     */     
/* 104 */     SysTree sysTree = this.sysTreeMananger.getByKey("dict");
/* 105 */     List<SysTreeNode> nodeList = this.sysTreeNodeMananger.getByTreeId(sysTree.getId());
/* 106 */     JSONArray jsonArray = new JSONArray();
/*     */     
/* 108 */     for (SysTreeNode sysTreeNode : nodeList) {
/* 109 */       JSONObject object = new JSONObject();
/* 110 */       object.put("id", sysTreeNode.getId());
/* 111 */       object.put("name", sysTreeNode.getName());
/* 112 */       object.put("parentId", sysTreeNode.getParentId());
/* 113 */       object.put("type", "type");
/* 114 */       object.put("noclick", Boolean.valueOf(true));
/* 115 */       jsonArray.add(object);
/*     */     } 
/*     */     
/* 118 */     for (DataDict dict : dicts) {
/* 119 */       JSONObject object = new JSONObject();
/* 120 */       object.put("id", dict.getId());
/* 121 */       object.put("name", dict.getName());
/* 122 */       object.put("key", dict.getDictKey());
/* 123 */       object.put("icon", "fa-check-square-o");
/* 124 */       object.put("parentId", dict.getTypeId());
/* 125 */       object.put("type", "dict");
/* 126 */       jsonArray.add(object);
/*     */     } 
/*     */     
/* 129 */     return jsonArray;
/*     */   }
/*     */ 
/*     */   
/*     */   public Integer countDictByTypeId(String typeId) {
/* 134 */     return this.dataDictDao.countDictByTypeId(typeId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void importData(XSSFWorkbook workbook, Map<String, Integer> rowConf) {
/* 140 */     String sheetName = "数据字典";
/* 141 */     XSSFSheet sheet = workbook.getSheet(sheetName);
/* 142 */     this.dataDictDao.removeAll();
/* 143 */     SysTree sysTree = this.sysTreeMananger.getByKey("dict");
/* 144 */     Map<String, String> mapSysTreeNode = new HashMap<>();
/* 145 */     String treeId = "";
/* 146 */     if (null != sysTree) {
/* 147 */       treeId = sysTree.getId();
/*     */     }
/* 149 */     for (int i = 1; i <= sheet.getLastRowNum(); i++) {
/* 150 */       XSSFRow row = sheet.getRow(i);
/* 151 */       String msg = "";
/*     */       try {
/* 153 */         DataDict dataDict = new DataDict();
/* 154 */         dataDict.setId(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_ID")).intValue())));
/* 155 */         dataDict.setKey(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_别名")).intValue())));
/* 156 */         dataDict.setDictKey(dataDict.getKey());
/* 157 */         if (StringUtils.isEmpty(dataDict.getKey())) {
/* 158 */           throw new BusinessMessage("_别名-必填");
/*     */         }
/* 160 */         dataDict.setName(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_名称")).intValue())));
/* 161 */         if (StringUtils.isEmpty(dataDict.getName())) {
/* 162 */           throw new BusinessMessage("_名称-必填");
/*     */         }
/* 164 */         dataDict.setDictType("dict");
/* 165 */         dataDict.setDeleteFlag("0");
/* 166 */         dataDict.setSn(Integer.valueOf(0));
/* 167 */         if (StringUtils.isNotEmpty(treeId)) {
/*     */           try {
/* 169 */             String sysTreeNodeId = this.sysTreeNodeMananger.findSysTreeNodeId(mapSysTreeNode, getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_分类")).intValue())), treeId);
/* 170 */             dataDict.setTypeId(sysTreeNodeId);
/* 171 */           } catch (Exception exception) {}
/*     */         }
/*     */ 
/*     */ 
/*     */         
/* 176 */         this.dataDictDao.create(dataDict);
/* 177 */         msg = "操作成功";
/* 178 */       } catch (BusinessMessage e) {
/* 179 */         msg = "操作失败," + e.getMessage();
/* 180 */       } catch (Exception e) {
/* 181 */         e.printStackTrace();
/* 182 */         msg = "操作失败,未知错误," + e.getMessage();
/*     */       } 
/* 184 */       XSSFCell cellResult = row.createCell(((Integer)rowConf.get(sheetName + "_操作结果")).intValue());
/* 185 */       cellResult.setCellValue(msg);
/*     */     } 
/* 187 */     sheetName = "数据字典项";
/* 188 */     sheet = workbook.getSheet(sheetName);
/* 189 */     Map<String, String> mapParentId = new HashMap<>();
/* 190 */     for (int j = 1; j <= sheet.getLastRowNum(); j++) {
/* 191 */       XSSFRow row = sheet.getRow(j);
/* 192 */       String msg = "";
/*     */       try {
/* 194 */         DataDict dataDict = new DataDict();
/* 195 */         dataDict.setId(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_ID")).intValue())));
/* 196 */         dataDict.setKey(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_别名")).intValue())));
/* 197 */         dataDict.setDictKey(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_数据字典别名")).intValue())));
/*     */         
/* 199 */         if (StringUtils.isEmpty(dataDict.getKey())) {
/* 200 */           throw new BusinessMessage("_别名-必填");
/*     */         }
/* 202 */         dataDict.setName(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_名称")).intValue())));
/* 203 */         if (StringUtils.isEmpty(dataDict.getName())) {
/* 204 */           throw new BusinessMessage("_名称-必填");
/*     */         }
/* 206 */         dataDict.setDictType("node");
/* 207 */         dataDict.setDeleteFlag("0");
/* 208 */         String sn = getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_排序")).intValue()));
/* 209 */         if (StringUtils.isNotEmpty(sn)) {
/* 210 */           dataDict.setSn(Integer.valueOf(Integer.parseInt(sn)));
/*     */         }
/* 212 */         String parentId = getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_父节点")).intValue()));
/* 213 */         parentId = findDataDictId(mapParentId, parentId, dataDict.getDictKey());
/* 214 */         dataDict.setParentId(parentId);
/* 215 */         this.dataDictDao.create(dataDict);
/* 216 */         msg = "操作成功";
/* 217 */       } catch (BusinessMessage e) {
/* 218 */         msg = "操作失败," + e.getMessage();
/* 219 */       } catch (Exception e) {
/* 220 */         e.printStackTrace();
/* 221 */         msg = "操作失败,未知错误," + e.getMessage();
/*     */       } 
/* 223 */       XSSFCell cellResult = row.createCell(((Integer)rowConf.get(sheetName + "_操作结果")).intValue());
/* 224 */       cellResult.setCellValue(msg);
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
/*     */   private String getCellStringData(Cell cell) {
/* 237 */     String value = "";
/* 238 */     if (cell != null) {
/* 239 */       cell.setCellType(CellType.STRING);
/* 240 */       value = cell.getStringCellValue();
/* 241 */       if (StringUtils.isNotBlank(value)) {
/* 242 */         value = value.trim();
/*     */       }
/*     */     } 
/* 245 */     return value;
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
/*     */   public String findDataDictId(Map<String, String> mapDataDict, String mainDataDictPath, String dictKey) {
/* 257 */     String dataDictId = mapDataDict.get(mainDataDictPath);
/* 258 */     if (StringUtils.isEmpty(dataDictId)) {
/* 259 */       String[] arrName = mainDataDictPath.split(">");
/*     */       
/* 261 */       List<Map<String, String>> lstData = new ArrayList<>();
/*     */       
/* 263 */       for (String name : arrName) {
/* 264 */         DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 265 */         defaultQueryFilter.addFilter("name_", name, QueryOP.EQUAL);
/* 266 */         defaultQueryFilter.addFilter("dict_key_", dictKey, QueryOP.EQUAL);
/* 267 */         List<DataDict> lstDataDict = query((QueryFilter)defaultQueryFilter);
/* 268 */         if (lstDataDict.size() > 0) {
/* 269 */           Map<String, String> temp = new HashMap<>();
/* 270 */           for (DataDict sysTreeNode : lstDataDict) {
/* 271 */             temp.put(sysTreeNode.getId(), sysTreeNode.getParentId());
/*     */           }
/* 273 */           lstData.add(temp);
/*     */         } else {
/* 275 */           throw new BusinessMessage("数据字典项：" + name + "不存在");
/*     */         } 
/*     */       } 
/*     */       
/* 279 */       label43: for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)((Map)lstData.get(lstData.size() - 1)).entrySet()) {
/* 280 */         String key = entry.getKey();
/* 281 */         String parentId = entry.getValue();
/* 282 */         if (lstData.size() == 1 && StringUtils.isEmpty(parentId)) {
/* 283 */           dataDictId = key;
/*     */           break;
/*     */         } 
/* 286 */         for (int i = lstData.size() - 2; i >= 0; i--) {
/* 287 */           Map<String, String> temp = lstData.get(i);
/* 288 */           String parentIdTemp = temp.get(parentId);
/* 289 */           if (i == 0 && StringUtils.isEmpty(parentIdTemp)) {
/* 290 */             dataDictId = key;
/*     */             break label43;
/*     */           } 
/* 293 */           if (StringUtils.isNotEmpty(parentIdTemp)) {
/* 294 */             parentId = parentIdTemp;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 299 */       if (StringUtils.isEmpty(dataDictId)) {
/* 300 */         throw new BusinessMessage("数据字典项：" + mainDataDictPath + "不存在");
/*     */       }
/* 302 */       mapDataDict.put(mainDataDictPath, dataDictId);
/*     */     } 
/* 304 */     return dataDictId;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<DataDict> query(QueryFilter queryFilter) {
/* 309 */     List<DataDict> lst = this.dao.query(queryFilter);
/* 310 */     return lst;
/*     */   }
/*     */ 
/*     */   
/*     */   public DataDict get(String entityId) {
/* 315 */     DataDict temp = (DataDict)this.dao.get(entityId);
/* 316 */     return temp;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/DataDictManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */