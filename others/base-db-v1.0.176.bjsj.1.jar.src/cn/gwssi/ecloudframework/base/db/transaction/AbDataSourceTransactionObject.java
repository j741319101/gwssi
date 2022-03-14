/*    */ package com.dstz.base.db.transaction;
/*    */ 
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.Map;
/*    */ import org.springframework.transaction.TransactionDefinition;
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
/*    */ public class AbDataSourceTransactionObject
/*    */ {
/*    */   private String serialNumber;
/*    */   private TransactionDefinition definition;
/* 31 */   private Map<String, DataSourceTransactionObject> dsTxObjMap = new LinkedHashMap<>();
/*    */   
/*    */   public AbDataSourceTransactionObject() {
/* 34 */     this.serialNumber = Integer.toHexString(hashCode());
/*    */   }
/*    */   
/*    */   public String getSerialNumber() {
/* 38 */     return this.serialNumber;
/*    */   }
/*    */   
/*    */   public TransactionDefinition getDefinition() {
/* 42 */     return this.definition;
/*    */   }
/*    */   
/*    */   public void setDefinition(TransactionDefinition definition) {
/* 46 */     this.definition = definition;
/*    */   }
/*    */   
/*    */   public Map<String, DataSourceTransactionObject> getDsTxObjMap() {
/* 50 */     return this.dsTxObjMap;
/*    */   }
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
/*    */   public void putDsTxObj(String dsKey, DataSourceTransactionObject dsTxObj) {
/* 64 */     this.dsTxObjMap.put(dsKey, dsTxObj);
/* 65 */     DataSourceTransactionObject txObj = this.dsTxObjMap.remove("dataSource");
/* 66 */     this.dsTxObjMap.put("dataSource", txObj);
/*    */   }
/*    */   
/*    */   public DataSourceTransactionObject getDsTxObj(String dsKey) {
/* 70 */     return this.dsTxObjMap.get(dsKey);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/transaction/AbDataSourceTransactionObject.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */