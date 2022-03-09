/*    */ package cn.gwssi.ecloudbpm.bus.platform;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessTable;
/*    */ import cn.gwssi.ecloudbpm.bus.api.platform.IBusinessTablePlatFormService;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessTableManager;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessTable;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class BusinessTablePlatFormServiceImpl
/*    */   implements IBusinessTablePlatFormService
/*    */ {
/*    */   @Resource
/*    */   private BusinessTableManager businessTableManager;
/*    */   
/*    */   public IBusinessTable getObject(String id, String key, Boolean fill) {
/* 22 */     BusinessTable object = null;
/* 23 */     if (StringUtil.isNotEmpty(id)) {
/* 24 */       object = (BusinessTable)this.businessTableManager.get(id);
/* 25 */     } else if (StringUtil.isNotEmpty(key)) {
/* 26 */       object = this.businessTableManager.getByKey(key);
/*    */     } 
/* 28 */     if (fill.booleanValue() && object != null) {
/* 29 */       object = this.businessTableManager.getFilledByKey(object.getKey());
/*    */     }
/* 31 */     return (IBusinessTable)object;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-rest/v1.0.176.bjsj.1/bus-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/platform/BusinessTablePlatFormServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */