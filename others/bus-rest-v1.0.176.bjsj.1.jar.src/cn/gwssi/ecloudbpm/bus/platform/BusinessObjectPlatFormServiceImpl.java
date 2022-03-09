/*    */ package cn.gwssi.ecloudbpm.bus.platform;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*    */ import cn.gwssi.ecloudbpm.bus.api.platform.IBusinessObjectPlatFormService;
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusinessObjectManager;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessObject;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class BusinessObjectPlatFormServiceImpl
/*    */   implements IBusinessObjectPlatFormService {
/*    */   @Resource
/*    */   private BusinessObjectManager businessObjectManager;
/*    */   
/*    */   public IBusinessObject getObject(String id, String key, Boolean fill) {
/* 18 */     BusinessObject object = null;
/* 19 */     if (StringUtil.isNotEmpty(id)) {
/* 20 */       object = (BusinessObject)this.businessObjectManager.get(id);
/* 21 */     } else if (StringUtil.isNotEmpty(key)) {
/* 22 */       object = this.businessObjectManager.getByKey(key);
/*    */     } 
/* 24 */     if (fill.booleanValue() && object != null) {
/* 25 */       object = this.businessObjectManager.getFilledByKey(object.getKey());
/*    */     }
/* 27 */     return (IBusinessObject)object;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-rest/v1.0.176.bjsj.1/bus-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/platform/BusinessObjectPlatFormServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */