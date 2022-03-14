/*    */ package com.dstz.sys.service.impl;
/*    */ 
/*    */ import com.dstz.base.core.util.BeanCopierUtils;
/*    */ import com.dstz.sys.api.model.dto.DataDictDTO;
/*    */ import com.dstz.sys.api.service.DataDictService;
/*    */ import com.dstz.sys.core.dao.DataDictDao;
/*    */ import com.dstz.sys.core.manager.SysTreeManager;
/*    */ import com.dstz.sys.core.manager.SysTreeNodeManager;
/*    */ import com.dstz.sys.core.model.DataDict;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("dataDictService")
/*    */ public class DataDictServiceImpl
/*    */   implements DataDictService
/*    */ {
/*    */   @Resource
/*    */   DataDictDao dataDictDao;
/*    */   @Resource
/*    */   SysTreeNodeManager sysTreeNodeMananger;
/*    */   @Resource
/*    */   SysTreeManager sysTreeMananger;
/*    */   
/*    */   public List<DataDictDTO> getDictNodeList(String dictKey, Boolean hasRoot) {
/* 40 */     List<DataDict> dictNodeList = this.dataDictDao.getDictNodeList(dictKey, hasRoot);
/* 41 */     if (dictNodeList == null) {
/* 42 */       return null;
/*    */     }
/* 44 */     List<DataDictDTO> dictList = new ArrayList<>();
/* 45 */     for (DataDict dataDict : dictNodeList) {
/* 46 */       DataDictDTO dto = new DataDictDTO();
/* 47 */       BeanCopierUtils.copyProperties(dataDict, dto);
/* 48 */       dictList.add(dto);
/*    */     } 
/* 50 */     return dictList;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/DataDictServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */