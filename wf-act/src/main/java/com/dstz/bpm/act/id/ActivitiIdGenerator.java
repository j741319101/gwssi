/*    */ package com.dstz.bpm.act.id;
/*    */ 
/*    */ import com.dstz.base.core.id.IdGenerator;
/*    */
/*    */ 
/*    */ public class ActivitiIdGenerator
/*    */   implements IdGenerator
/*    */ {
/* 10 */   private IdGenerator idGenerator = null;
/*    */   
/*    */   public void setIdGenerator(IdGenerator idGenerator) {
/* 13 */     this.idGenerator = idGenerator;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getNextId() {
/* 18 */     return this.idGenerator.getSuid();
/*    */   }

    @Override
    public Long getUId() {
        return idGenerator.getUId();
    }

    @Override
    public String getSuid() {
        return idGenerator.getSuid();
    }
    /*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/act/id/ActivitiIdGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */