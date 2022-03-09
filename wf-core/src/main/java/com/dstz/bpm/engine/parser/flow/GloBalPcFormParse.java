/*    */ package com.dstz.bpm.engine.parser.flow;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmDef;
/*    */ import com.dstz.bpm.api.model.form.BpmForm;
/*    */ import com.dstz.bpm.api.model.form.DefaultForm;
/*    */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*    */
import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class GloBalPcFormParse
/*    */   extends AbsFlowParse<DefaultForm>
/*    */ {
/*    */   public String getKey() {
/* 14 */     return "globalForm";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
/* 19 */     DefaultForm form = (DefaultForm)object;
/* 20 */     bpmProcessDef.setGlobalForm((BpmForm)form);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/GloBalPcFormParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */