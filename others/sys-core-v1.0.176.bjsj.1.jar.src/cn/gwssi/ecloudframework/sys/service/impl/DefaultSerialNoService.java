/*    */ package com.dstz.sys.service.impl;
/*    */ 
/*    */ import com.dstz.sys.api.service.SerialNoService;
/*    */ import com.dstz.sys.core.manager.SerialNoManager;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service("serialNoService")
/*    */ public class DefaultSerialNoService
/*    */   implements SerialNoService
/*    */ {
/*    */   @Resource
/*    */   SerialNoManager serialNoManager;
/*    */   
/*    */   public String genNextNo(String alias) {
/* 21 */     return this.serialNoManager.nextId(alias);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getPreviewNo(String alias) {
/* 26 */     return this.serialNoManager.getCurIdByAlias(alias);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/DefaultSerialNoService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */