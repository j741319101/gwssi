/*    */ package cn.gwssi.ecloudbpm.bus.executor.parseval;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class ParseValTypeExecutor
/*    */   extends ParseValExecuteChain
/*    */ {
/*    */   public int getSn() {
/* 22 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void run(ParseValParam param) {
/* 27 */     String key = param.getKey();
/* 28 */     Object value = param.getValue();
/*    */     
/* 30 */     if (value == null || StringUtil.isEmpty(value.toString())) {
/*    */       return;
/*    */     }
/*    */     
/* 34 */     BusinessColumn column = param.getBusTableRel().getTable().getColumnByKey(key);
/* 35 */     if (column == null) {
/* 36 */       param.getData().put(key, value);
/*    */       
/*    */       return;
/*    */     } 
/* 40 */     param.getData().put(column.getKey(), column.value(value.toString()));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/executor/parseval/ParseValTypeExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */