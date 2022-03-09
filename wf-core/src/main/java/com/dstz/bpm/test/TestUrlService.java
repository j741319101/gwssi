/*    */ package com.dstz.bpm.test;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class TestUrlService
/*    */ {
/*    */   public void testSave(ActionCmd cmd) {
/* 11 */     JSONObject data = cmd.getBusData();
/*    */     
/* 13 */     System.err.println(data.toJSONString());
/* 14 */     cmd.setBusinessKey("123");
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/test/TestUrlService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */