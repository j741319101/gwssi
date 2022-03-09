/*    */ package cn.gwssi.ecloudbpm.wf.engine.parser.flow;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmVariableDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmVariableDef;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class FlowVariablesParse
/*    */   extends AbsFlowParse<DefaultBpmVariableDef>
/*    */ {
/*    */   public String getKey() {
/* 19 */     return "variableList";
/*    */   }
/*    */ 
/*    */   
/*    */   public String validate(Object object) {
/* 24 */     List<BpmVariableDef> varList = (List<BpmVariableDef>)object;
/*    */     
/* 26 */     Set<String> keys = new HashSet<>();
/* 27 */     for (BpmVariableDef def : varList) {
/* 28 */       String key = def.getKey();
/*    */       
/* 30 */       if (keys.contains(key)) {
/* 31 */         throw new RuntimeException("解析流程变量出错：" + key + "在流程中重复！");
/*    */       }
/* 33 */       keys.add(def.getKey());
/*    */     } 
/* 35 */     return "";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
/* 40 */     List<BpmVariableDef> varList = (List<BpmVariableDef>)object;
/*    */     
/* 42 */     bpmProcessDef.setVarList(varList);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isArray() {
/* 47 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/FlowVariablesParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */