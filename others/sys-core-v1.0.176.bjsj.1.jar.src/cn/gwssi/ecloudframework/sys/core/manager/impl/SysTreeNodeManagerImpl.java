/*     */ package com.dstz.sys.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.api.constant.Direction;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.HanyuPinyinHelper;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.sys.api.model.SysNodeOrderParam;
/*     */ import com.dstz.sys.core.dao.SysTreeNodeDao;
/*     */ import com.dstz.sys.core.manager.SysTreeManager;
/*     */ import com.dstz.sys.core.manager.SysTreeNodeManager;
/*     */ import com.dstz.sys.core.model.SysTree;
/*     */ import com.dstz.sys.core.model.SysTreeNode;
/*     */ import com.dstz.sys.util.CustomDefaultQueryFilterUtil;
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
/*     */ @Service("sysTreeNodeManager")
/*     */ public class SysTreeNodeManagerImpl
/*     */   extends BaseManager<String, SysTreeNode>
/*     */   implements SysTreeNodeManager
/*     */ {
/*     */   @Resource
/*     */   SysTreeNodeDao sysTreeNodeDao;
/*     */   @Autowired
/*     */   SysTreeManager sysTreeManager;
/*     */   
/*     */   public List<SysTreeNode> getByTreeId(String treeId) {
/*  51 */     QueryFilter filter = CustomDefaultQueryFilterUtil.setDefaultQueryFilter();
/*  52 */     filter.addFilter("tree_id_", treeId, QueryOP.EQUAL);
/*  53 */     filter.addFieldSort("sn_", Direction.ASC.getKey());
/*  54 */     return query(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public SysTreeNode getByTreeIdAndKey(String treeId, String key) {
/*  59 */     QueryFilter filter = CustomDefaultQueryFilterUtil.setDefaultQueryFilter();
/*  60 */     filter.addFilter("tree_id_", treeId, QueryOP.EQUAL);
/*  61 */     filter.addFilter("key_", key, QueryOP.EQUAL);
/*  62 */     return (SysTreeNode)queryOne(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SysTreeNode> getByParentId(String parentId) {
/*  67 */     QueryFilter filter = CustomDefaultQueryFilterUtil.setDefaultQueryFilter();
/*  68 */     filter.addFilter("parent_id_", parentId, QueryOP.EQUAL);
/*  69 */     filter.addFieldSort("sn_", Direction.ASC.getKey());
/*  70 */     return query(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SysTreeNode> getByParentKey(String parentKey) {
/*  75 */     QueryFilter filter = CustomDefaultQueryFilterUtil.setDefaultQueryFilter();
/*  76 */     filter.addParamsFilter("parentkey", parentKey);
/*  77 */     filter.addFieldSort("sn_", Direction.ASC.getKey());
/*  78 */     return query(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SysTreeNode> getStartWithPath(String path) {
/*  83 */     QueryFilter filter = CustomDefaultQueryFilterUtil.setDefaultQueryFilter();
/*  84 */     filter.addFilter("path_", path, QueryOP.RIGHT_LIKE);
/*  85 */     return query(filter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByTreeId(String treeId) {
/*  90 */     this.sysTreeNodeDao.removeByTreeId(treeId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeByPath(String path) {
/*  95 */     this.sysTreeNodeDao.removeByPath(path);
/*     */   }
/*     */ 
/*     */   
/*     */   public int chageOrder(SysNodeOrderParam sysNodeOrderParam) {
/* 100 */     return this.sysTreeNodeDao.chageOrder(sysNodeOrderParam);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCountByStartWithPath(String path) {
/* 105 */     return this.sysTreeNodeDao.getCountByStartWithPath(path + "%").intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public void importData(XSSFWorkbook workbook, Map<String, Integer> rowConf) {
/* 110 */     String sheetName = "系统树节点";
/* 111 */     XSSFSheet sheet = workbook.getSheet(sheetName);
/* 112 */     this.sysTreeNodeDao.removeAll();
/* 113 */     Map<String, String> mapSysTreeNode = new HashMap<>();
/* 114 */     for (int i = 1; i <= sheet.getLastRowNum(); i++) {
/* 115 */       XSSFRow row = sheet.getRow(i);
/* 116 */       String msg = "";
/*     */       try {
/* 118 */         SysTreeNode sysTreeNode = new SysTreeNode();
/* 119 */         sysTreeNode.setId(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_ID")).intValue())));
/* 120 */         if (StringUtils.isEmpty(sysTreeNode.getId())) {
/* 121 */           sysTreeNode.setId(IdUtil.getSuid());
/*     */         }
/* 123 */         sysTreeNode.setKey(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_别名")).intValue())));
/* 124 */         sysTreeNode.setName(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_名称")).intValue())));
/* 125 */         if (StringUtils.isEmpty(sysTreeNode.getName())) {
/* 126 */           throw new BusinessMessage("_名称-必填");
/*     */         }
/* 128 */         sysTreeNode.setDesc(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_描述")).intValue())));
/* 129 */         String treeKey = getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_树别名")).intValue()));
/* 130 */         SysTree sysTree = this.sysTreeManager.getByKey(treeKey);
/* 131 */         if (null == sysTree) {
/* 132 */           throw new BusinessMessage("系统树[" + treeKey + "]不存在");
/*     */         }
/* 134 */         sysTreeNode.setTreeId(sysTree.getId());
/* 135 */         String sn = getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_排序")).intValue()));
/* 136 */         if (StringUtils.isNotEmpty(sn)) {
/* 137 */           sysTreeNode.setSn(Integer.parseInt(sn));
/*     */         }
/* 139 */         String parentNodeId = "";
/* 140 */         String parentOrgPath = getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_父节点")).intValue()));
/* 141 */         if ("".equals(parentOrgPath)) {
/* 142 */           parentNodeId = "0";
/* 143 */           sysTreeNode.setPath(sysTreeNode.getId() + ".");
/*     */         } else {
/* 145 */           parentNodeId = findSysTreeNodeId(mapSysTreeNode, parentOrgPath, sysTreeNode.getTreeId());
/* 146 */           if (StringUtils.isEmpty(parentNodeId)) {
/* 147 */             throw new BusinessException("父节点[" + parentOrgPath + "]不存在");
/*     */           }
/* 149 */           SysTreeNode temp = (SysTreeNode)get(parentNodeId);
/* 150 */           sysTreeNode.setPath(temp.getPath() + sysTreeNode.getId() + ".");
/*     */         } 
/* 152 */         sysTreeNode.setParentId(parentNodeId);
/*     */         
/* 154 */         sysTreeNode.setIcon(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_图标")).intValue())));
/* 155 */         sysTreeNode.setAppName(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_所属应用")).intValue())));
/* 156 */         this.sysTreeNodeDao.create(sysTreeNode);
/* 157 */         msg = "操作成功";
/* 158 */       } catch (BusinessMessage e) {
/* 159 */         msg = "操作失败," + e.getMessage();
/* 160 */       } catch (Exception e) {
/* 161 */         e.printStackTrace();
/* 162 */         msg = "操作失败,未知错误," + e.getMessage();
/*     */       } 
/* 164 */       XSSFCell cellResult = row.createCell(((Integer)rowConf.get(sheetName + "_操作结果")).intValue());
/* 165 */       cellResult.setCellValue(msg);
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
/* 178 */     String value = "";
/* 179 */     if (cell != null) {
/* 180 */       cell.setCellType(CellType.STRING);
/* 181 */       value = cell.getStringCellValue();
/* 182 */       if (StringUtils.isNotBlank(value)) {
/* 183 */         value = value.trim();
/*     */       }
/*     */     } 
/* 186 */     return value;
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
/*     */   public String findSysTreeNodeId(Map<String, String> mapSysTreeNode, String mainSysTreeNodePath, String treeId) {
/* 201 */     String sysTreeNodeId = mapSysTreeNode.get(mainSysTreeNodePath);
/* 202 */     if (StringUtils.isEmpty(sysTreeNodeId)) {
/* 203 */       String[] arrName = mainSysTreeNodePath.split(">");
/*     */       
/* 205 */       List<Map<String, String>> lstData = new ArrayList<>();
/*     */       
/* 207 */       for (String name : arrName) {
/* 208 */         DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 209 */         defaultQueryFilter.addFilter("name_", name, QueryOP.EQUAL);
/* 210 */         defaultQueryFilter.addFilter("tree_id_", treeId, QueryOP.EQUAL);
/* 211 */         List<SysTreeNode> lstSysTreeNode = query((QueryFilter)defaultQueryFilter);
/* 212 */         if (lstSysTreeNode.size() > 0) {
/* 213 */           Map<String, String> temp = new HashMap<>();
/* 214 */           for (SysTreeNode sysTreeNode : lstSysTreeNode) {
/* 215 */             temp.put(sysTreeNode.getId(), sysTreeNode.getParentId());
/*     */           }
/* 217 */           lstData.add(temp);
/*     */         } else {
/* 219 */           throw new BusinessMessage("分类树节点：" + name + "不存在");
/*     */         } 
/*     */       } 
/*     */       
/* 223 */       label43: for (Map.Entry<String, String> entry : (Iterable<Map.Entry<String, String>>)((Map)lstData.get(lstData.size() - 1)).entrySet()) {
/* 224 */         String key = entry.getKey();
/* 225 */         String parentId = entry.getValue();
/* 226 */         if (lstData.size() == 1 && "0".equals(parentId)) {
/* 227 */           sysTreeNodeId = key;
/*     */           break;
/*     */         } 
/* 230 */         for (int i = lstData.size() - 2; i >= 0; i--) {
/* 231 */           Map<String, String> temp = lstData.get(i);
/* 232 */           String parentIdTemp = temp.get(parentId);
/* 233 */           if (i == 0 && "0".equals(parentIdTemp)) {
/* 234 */             sysTreeNodeId = key;
/*     */             break label43;
/*     */           } 
/* 237 */           if (StringUtils.isNotEmpty(parentIdTemp)) {
/* 238 */             parentId = parentIdTemp;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 243 */       if (StringUtils.isEmpty(sysTreeNodeId)) {
/* 244 */         throw new BusinessMessage("分类树节点：" + mainSysTreeNodePath + "不存在");
/*     */       }
/* 246 */       mapSysTreeNode.put(mainSysTreeNodePath, sysTreeNodeId);
/*     */     } 
/* 248 */     return sysTreeNodeId;
/*     */   }
/*     */ 
/*     */   
/*     */   public String creatByTreeKey(String treeKey, String nodeName) {
/* 253 */     SysTree sysTree = this.sysTreeManager.getByKey(treeKey);
/* 254 */     if (null == sysTree) {
/* 255 */       throw new BusinessException("分类树[" + treeKey + "]不存在");
/*     */     }
/* 257 */     DefaultQueryFilter filter = new DefaultQueryFilter();
/* 258 */     filter.addFilter("name_", nodeName, QueryOP.EQUAL);
/* 259 */     filter.addFilter("tree_id_", sysTree.getId(), QueryOP.EQUAL);
/* 260 */     List<SysTreeNode> lstSysTreeNode = query((QueryFilter)filter);
/* 261 */     if (null != lstSysTreeNode && lstSysTreeNode.size() > 0) {
/* 262 */       return ((SysTreeNode)lstSysTreeNode.get(0)).getId();
/*     */     }
/*     */     
/* 265 */     SysTreeNode sysTreeNode = new SysTreeNode();
/* 266 */     sysTreeNode.setId(IdUtil.getSuid());
/* 267 */     sysTreeNode.setName(nodeName);
/* 268 */     sysTreeNode.setKey(HanyuPinyinHelper.getPinyinString(nodeName));
/* 269 */     sysTreeNode.setPath(sysTreeNode.getId() + ".");
/* 270 */     sysTreeNode.setTreeId(sysTree.getId());
/* 271 */     sysTreeNode.setParentId("0");
/* 272 */     create((Serializable)sysTreeNode);
/* 273 */     return sysTreeNode.getId();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysTreeNodeManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */