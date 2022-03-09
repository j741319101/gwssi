/*    */ package com.dstz.bpm.rest.script;
/*    */ 
/*    */ import com.dstz.base.api.response.impl.ResultMsg;
/*    */ import com.dstz.base.rest.ControllerTools;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/script/http"})
/*    */ public class HttpFormHandlerTestController extends ControllerTools {
/*    */   @RequestMapping({"/test"})
/*    */   public ResultMsg<String> test() throws Exception {
/* 16 */     return getSuccessResult("成功");
/*    */   }
/*    */   @RequestMapping({"/aaaa"})
/*    */   public ResultMsg<String> aaaa(@RequestBody Map paramse) {
/* 20 */     System.out.println(paramse);
/*    */     try {
/* 22 */       Thread.sleep(111000L);
/* 23 */     } catch (Exception exception) {}
/* 24 */     return getSuccessResult("aaaa成功");
/*    */   }
/*    */   @RequestMapping({"/bbbb"})
/*    */   public ResultMsg<String> bbbb(@RequestBody Map paramse) {
/* 28 */     Map<String, Object> pa = new HashMap<>();
/* 29 */     return getSuccessResult("bbbb成功");
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/rest/script/HttpFormHandlerTestController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */