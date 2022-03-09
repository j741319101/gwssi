/*    */ package cn.gwssi.ecloudbpm.bus.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.dao.BusTableDiagramDao;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusTableDiagramManager;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusTableDiagram;
/*    */ import cn.gwssi.ecloudframework.base.core.id.IdUtil;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import java.io.Serializable;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class BusTableDiagramManagerImpl
/*    */   extends BaseManager<String, BusTableDiagram> implements BusTableDiagramManager {
/*    */   @Autowired
/*    */   BusTableDiagramDao busTableDiagramDao;
/*    */   
/*    */   public String save(BusTableDiagram busTableDiagram) {
/* 20 */     String suid = null;
/* 21 */     if (StringUtil.isEmpty(busTableDiagram.getId())) {
/* 22 */       suid = IdUtil.getSuid();
/* 23 */       busTableDiagram.setId(suid);
/* 24 */       create((Serializable)busTableDiagram);
/*    */     } else {
/* 26 */       update((Serializable)busTableDiagram);
/*    */     } 
/* 28 */     return suid;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/manager/impl/BusTableDiagramManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */