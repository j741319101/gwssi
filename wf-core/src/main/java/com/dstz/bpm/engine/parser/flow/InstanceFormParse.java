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
/*    */ public class InstanceFormParse
/*    */   extends AbsFlowParse<DefaultForm>
/*    */ {
/*    */   public String getKey() {
/* 14 */     return "instForm";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
/* 20 */     DefaultForm form = (DefaultForm)object;
/* 21 */     bpmProcessDef.setInstForm((BpmForm)form);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/InstanceFormParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */