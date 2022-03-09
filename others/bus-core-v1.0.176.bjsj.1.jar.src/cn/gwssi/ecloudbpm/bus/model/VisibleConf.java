/*    */ package cn.gwssi.ecloudbpm.bus.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IVisibleConf;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VisibleConf
/*    */   implements IVisibleConf
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   List<String> cols;
/*    */   
/*    */   public List<String> getCols() {
/* 21 */     return this.cols;
/*    */   }
/*    */   
/*    */   public void setCols(List<String> cols) {
/* 25 */     this.cols = cols;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/VisibleConf.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */