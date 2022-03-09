/*    */ package cn.gwssi.ecloudbpm.wf.api.engine.plugin.def;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class DynamicTaskIdentitys
/*    */ {
/*    */   private String taskName;
/*    */   private List<SysIdentity> nodeIdentitys;
/*    */   
/*    */   public DynamicTaskIdentitys(String taskName, List<SysIdentity> nodeIdentitys) {
/* 25 */     this.taskName = taskName;
/* 26 */     this.nodeIdentitys = nodeIdentitys;
/*    */   }
/*    */   
/*    */   public String getTaskName() {
/* 30 */     return this.taskName;
/*    */   }
/*    */   
/*    */   public void setTaskName(String taskName) {
/* 34 */     this.taskName = taskName;
/*    */   }
/*    */   
/*    */   public List<SysIdentity> getNodeIdentitys() {
/* 38 */     return this.nodeIdentitys;
/*    */   }
/*    */   
/*    */   public void setNodeIdentitys(List<SysIdentity> nodeIdentitys) {
/* 42 */     this.nodeIdentitys = nodeIdentitys;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/plugin/def/DynamicTaskIdentitys.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */