/*    */ package com.dstz.bpm.engine.parser.node;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmDef;
/*    */ import com.dstz.bpm.api.model.form.BpmForm;
/*    */ import com.dstz.bpm.api.model.form.DefaultForm;
/*    */ import com.dstz.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class PcFormParse
/*    */   extends AbsNodeParse<DefaultForm>
/*    */ {
/*    */   public String getKey() {
/* 14 */     return "form";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefParam(BaseBpmNodeDef userNodeDef, Object object) {
/* 19 */     DefaultForm form = (DefaultForm)object;
/* 20 */     userNodeDef.setForm((BpmForm)form);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/node/PcFormParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */