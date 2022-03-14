/*    */ package com.dstz.bpm.api.engine.action.cmd;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class FlowBatchRequestParam
/*    */ {
/*    */   private String option;
/*    */   private List<Map> param;
/*    */   private String action;
/*    */   private JSONObject extendConf;
/*    */   
/*    */   public String getOption() {
/* 15 */     return this.option;
/*    */   }
/*    */   
/*    */   public void setOption(String option) {
/* 19 */     this.option = option;
/*    */   }
/*    */   
/*    */   public List<Map> getParam() {
/* 23 */     return this.param;
/*    */   }
/*    */   
/*    */   public void setParam(List<Map> param) {
/* 27 */     this.param = param;
/*    */   }
/*    */   
/*    */   public String getAction() {
/* 31 */     return this.action;
/*    */   }
/*    */   
/*    */   public void setAction(String action) {
/* 35 */     this.action = action;
/*    */   }
/*    */   
/*    */   public JSONObject getExtendConf() {
/* 39 */     return this.extendConf;
/*    */   }
/*    */   
/*    */   public void setExtendConf(JSONObject extendConf) {
/* 43 */     this.extendConf = extendConf;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/cmd/FlowBatchRequestParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */