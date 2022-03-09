/*    */ package cn.gwssi.ecloudbpm.bus.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IListQueryConf;
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRule;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ListQueryConf
/*    */   implements IListQueryConf
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   ISqlRule rule;
/*    */   
/*    */   public ISqlRule getRule() {
/* 20 */     return this.rule;
/*    */   }
/*    */   
/*    */   public void setRule(ISqlRule rule) {
/* 24 */     this.rule = rule;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/ListQueryConf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */