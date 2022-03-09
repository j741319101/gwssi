/*    */ package cn.gwssi.ecloudbpm.wf.engine.parser.flow;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.def.NodeInit;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import java.util.List;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class FlowFormInitParse
/*    */   extends AbsFlowParse<NodeInit>
/*    */ {
/*    */   public String getKey() {
/* 16 */     return "nodeInitList";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
/* 22 */     List<NodeInit> list = (List<NodeInit>)object;
/* 23 */     bpmProcessDef.setNodeInitList(list);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isArray() {
/* 28 */     return true;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/FlowFormInitParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */