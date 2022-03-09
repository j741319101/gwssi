/*    */ package cn.gwssi.ecloudbpm.wf.engine.parser.flow;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.form.BpmForm;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.form.DefaultForm;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ public class GlobalMobileFormParse
/*    */   extends AbsFlowParse<DefaultForm>
/*    */ {
/*    */   public String getKey() {
/* 14 */     return "globalMobileForm";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmProcessDef, Object object) {
/* 20 */     DefaultForm form = (DefaultForm)object;
/* 21 */     bpmProcessDef.setGlobalMobileForm((BpmForm)form);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/GlobalMobileFormParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */