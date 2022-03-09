/*    */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.dao.HolidayConfDao;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.HolidayConfManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.WorkCalenDarManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.HolidayConf;
/*    */ import java.io.Serializable;
/*    */ import java.util.Date;
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
/*    */ @Service("holidayConfManager")
/*    */ public class HolidayConfManagerImpl
/*    */   extends BaseManager<String, HolidayConf>
/*    */   implements HolidayConfManager
/*    */ {
/*    */   @Resource
/*    */   HolidayConfDao holidayConfDao;
/*    */   @Resource
/*    */   WorkCalenDarManager workCalenDarManager;
/*    */   
/*    */   public HolidayConf queryOne(String name, Date startDay, Date endDay) {
/* 33 */     return this.holidayConfDao.queryOne(name, startDay, endDay);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void removeByIds(String... ids) {
/* 39 */     for (String id : ids) {
/* 40 */       HolidayConf conf = (HolidayConf)get(id);
/* 41 */       this.workCalenDarManager.clearWorkCalenDarByHoliday(conf);
/* 42 */       remove(id);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/HolidayConfManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */