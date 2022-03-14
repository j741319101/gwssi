/*    */ package com.dstz.bpm.engine.parser.node;
/*    */ 
/*    */ import com.dstz.bpm.api.engine.plugin.def.BpmDef;
/*    */ import com.dstz.bpm.api.model.def.NodeProperties;
/*    */ import com.dstz.bpm.api.model.nodedef.impl.BaseBpmNodeDef;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class NodePropertiesParse
/*    */   extends AbsNodeParse<NodeProperties>
/*    */ {
/*    */   public String getKey() {
/* 14 */     return "propertie";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefParam(BaseBpmNodeDef userNodeDef, Object object) {
/* 19 */     NodeProperties prop = (NodeProperties)object;
/*    */     
/* 21 */     userNodeDef.setNodeProperties(prop);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/node/NodePropertiesParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */