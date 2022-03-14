/*    */ package com.dstz.bpm.plugin.global.formulas.def;
/*    */ 
/*    */ import com.dstz.bpm.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ 
/*    */ public class FormulasPluginDef extends AbstractBpmExecutionPluginDef {
/*    */   private List<OprFormula> oprFormulas;
/*    */   
/*    */   public Set<String> getDataFlowConf() {
/* 13 */     Set<String> opr = new HashSet<>();
/* 14 */     if (CollectionUtil.isNotEmpty(this.oprFormulas)) {
/* 15 */       this.oprFormulas.forEach(def -> opr.add(def.getId()));
/*    */     }
/* 17 */     return opr;
/*    */   }
/*    */   
/*    */   public List<OprFormula> getOprFormulas() {
/* 21 */     return this.oprFormulas;
/*    */   }
/*    */   
/*    */   public void setOprFormulas(List<OprFormula> oprFormulas) {
/* 25 */     this.oprFormulas = oprFormulas;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/formulas/def/FormulasPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */