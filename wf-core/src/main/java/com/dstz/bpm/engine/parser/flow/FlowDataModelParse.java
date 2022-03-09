/*    */ package com.dstz.bpm.engine.parser.flow;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmDef;
/*    */ import com.dstz.bpm.api.model.def.BpmDataModel;
/*    */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */
import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class FlowDataModelParse
/*    */   extends AbsFlowParse<BpmDataModel>
/*    */ {
/*    */   public String getKey() {
/* 18 */     return "dataModelList";
/*    */   }
/*    */ 
/*    */   
/*    */   public String validate(Object object) {
/* 23 */     List<BpmDataModel> list = (List<BpmDataModel>)object;
/*    */     
/* 25 */     Set<String> keys = new HashSet<>();
/* 26 */     for (BpmDataModel def : list) {
/* 27 */       String key = def.getCode();
/*    */       
/* 29 */       if (keys.contains(key)) {
/* 30 */         throw new RuntimeException("解析流程数据模型出错code：" + key + "在流程中重复配置！");
/*    */       }
/* 32 */       keys.add(def.getCode());
/*    */     } 
/* 34 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
/* 39 */     List<BpmDataModel> list = (List<BpmDataModel>)object;
/*    */     
/* 41 */     bpmProcessDef.setDataModelList(list);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isArray() {
/* 46 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/FlowDataModelParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */