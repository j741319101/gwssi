/*    */ package cn.gwssi.ecloudbpm.form.model;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.IDataFlowConf;
/*    */ import cn.gwssi.ecloudbpm.bus.model.DataFlowConf;
/*    */ import cn.gwssi.ecloudbpm.bus.model.ListQueryConf;
/*    */ import cn.gwssi.ecloudbpm.bus.model.VisibleConf;
/*    */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustomConfEntity;
/*    */ import com.alibaba.fastjson.annotation.JSONField;
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
/*    */ public class FormCustomConfEntity
/*    */   implements IFormCustomConfEntity
/*    */ {
/*    */   private static final long serialVersionUID = 700694295167942753L;
/*    */   public static final String QUERY_TYPE_ALL = "all";
/*    */   public static final String QUERY_TYPE_CURRENT_USER = "currentUser";
/*    */   public static final String QUERY_TYPE_CURRENT_ORG = "currentOrg";
/*    */   private DataFlowConf dataFlowConf;
/*    */   @JSONField(serialize = false)
/*    */   private ListQueryConf listQueryConf;
/*    */   private VisibleConf visibleConf;
/*    */   private String queryType;
/*    */   
/*    */   public DataFlowConf getDataFlowConf() {
/* 40 */     return this.dataFlowConf;
/*    */   }
/*    */   
/*    */   public void setDataFlowConf(DataFlowConf dataFlowConf) {
/* 44 */     this.dataFlowConf = dataFlowConf;
/*    */   }
/*    */   
/*    */   public ListQueryConf getListQueryConf() {
/* 48 */     return this.listQueryConf;
/*    */   }
/*    */   
/*    */   public void setListQueryConf(ListQueryConf listQueryConf) {
/* 52 */     this.listQueryConf = listQueryConf;
/*    */   }
/*    */   
/*    */   public VisibleConf getVisibleConf() {
/* 56 */     return this.visibleConf;
/*    */   }
/*    */   
/*    */   public void setVisibleConf(VisibleConf visibleConf) {
/* 60 */     this.visibleConf = visibleConf;
/*    */   }
/*    */   
/*    */   public String getQueryType() {
/* 64 */     return this.queryType;
/*    */   }
/*    */   
/*    */   public void setQueryType(String queryType) {
/* 68 */     this.queryType = queryType;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormCustomConfEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */