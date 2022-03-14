/*     */ package com.dstz.sys.core.manager.impl;
/*     */ 
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import com.dstz.sys.core.dao.SysTreeDao;
/*     */ import com.dstz.sys.core.manager.SysTreeManager;
/*     */ import com.dstz.sys.core.manager.SysTreeNodeManager;
/*     */ import com.dstz.sys.core.model.SysTree;
/*     */ import java.io.Serializable;
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
/*     */ @Service("sysTreeManager")
/*     */ public class SysTreeManagerImpl
/*     */   extends BaseManager<String, SysTree>
/*     */   implements SysTreeManager
/*     */ {
/*     */   @Resource
/*     */   SysTreeDao sysTreeDao;
/*     */   @Autowired
/*     */   SysTreeNodeManager sysTreeNodeManager;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   public SysTree getByKey(String key) {
/*  46 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  47 */     defaultQueryFilter.addFilter("key_", key, QueryOP.EQUAL);
/*  48 */     return (SysTree)queryOne((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeContainNode(String id) {
/*  53 */     remove(id);
/*  54 */     this.sysTreeNodeManager.removeByTreeId(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void importData(XSSFWorkbook workbook, Map<String, Integer> rowConf) {
/*  59 */     Map<String, Boolean> yesOrNo = new HashMap<>();
/*  60 */     yesOrNo.put("是", Boolean.TRUE);
/*  61 */     yesOrNo.put("否", Boolean.FALSE);
/*  62 */     String sheetName = "系统树";
/*  63 */     XSSFSheet sheet = workbook.getSheet(sheetName);
/*  64 */     this.sysTreeDao.removeAll();
/*  65 */     for (int i = 1; i <= sheet.getLastRowNum(); i++) {
/*  66 */       XSSFRow row = sheet.getRow(i);
/*  67 */       String msg = "";
/*     */       try {
/*  69 */         SysTree sysTree = new SysTree();
/*  70 */         sysTree.setId(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_ID")).intValue())));
/*  71 */         sysTree.setKey(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_别名")).intValue())));
/*  72 */         if (StringUtils.isEmpty(sysTree.getKey())) {
/*  73 */           throw new BusinessMessage("_别名-必填");
/*     */         }
/*  75 */         sysTree.setName(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_名称")).intValue())));
/*  76 */         if (StringUtils.isEmpty(sysTree.getName())) {
/*  77 */           throw new BusinessMessage("_名称-必填");
/*     */         }
/*  79 */         sysTree.setDesc(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_描述")).intValue())));
/*  80 */         sysTree.setSystem(((Boolean)yesOrNo.get(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_是否系统内置树")).intValue())))).booleanValue());
/*  81 */         sysTree.setMultiSelect(((Boolean)yesOrNo.get(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_是否支持多选")).intValue())))).booleanValue());
/*  82 */         sysTree.setLeafStore(((Boolean)yesOrNo.get(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_叶子结点是否存储数据")).intValue())))).booleanValue());
/*  83 */         sysTree.setIconShow(((Boolean)yesOrNo.get(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_是否显示图标")).intValue())))).booleanValue());
/*  84 */         sysTree.setEnableEdit(((Boolean)yesOrNo.get(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_是否可编辑")).intValue())))).booleanValue());
/*  85 */         sysTree.setDrag(((Boolean)yesOrNo.get(getCellStringData((Cell)row.getCell(((Integer)rowConf.get(sheetName + "_是否支持拖拽")).intValue())))).booleanValue());
/*  86 */         this.sysTreeDao.create(sysTree);
/*  87 */         msg = "操作成功";
/*  88 */       } catch (BusinessMessage e) {
/*  89 */         msg = "操作失败," + e.getMessage();
/*  90 */       } catch (Exception e) {
/*  91 */         e.printStackTrace();
/*  92 */         msg = "操作失败,未知错误," + e.getMessage();
/*     */       } 
/*  94 */       XSSFCell cellResult = row.createCell(((Integer)rowConf.get(sheetName + "_操作结果")).intValue());
/*  95 */       cellResult.setCellValue(msg);
/*     */     } 
/*  97 */     this.sysTreeNodeManager.importData(workbook, rowConf);
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
/* 109 */     String value = "";
/* 110 */     if (cell != null) {
/* 111 */       cell.setCellType(CellType.STRING);
/* 112 */       value = cell.getStringCellValue();
/* 113 */       if (StringUtils.isNotBlank(value)) {
/* 114 */         value = value.trim();
/*     */       }
/*     */     } 
/* 117 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<SysTree> query(QueryFilter queryFilter) {
/* 122 */     List<SysTree> lst = this.dao.query(queryFilter);
/* 123 */     return lst;
/*     */   }
/*     */ 
/*     */   
/*     */   public SysTree get(String entityId) {
/* 128 */     SysTree temp = (SysTree)this.dao.get(entityId);
/* 129 */     return temp;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysTreeManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */