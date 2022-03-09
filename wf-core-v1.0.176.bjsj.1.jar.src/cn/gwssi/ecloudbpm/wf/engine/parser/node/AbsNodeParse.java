/*   */ package cn.gwssi.ecloudbpm.wf.engine.parser.node;
/*   */ 
/*   */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*   */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.BaseBpmNodeDef;
/*   */ import cn.gwssi.ecloudbpm.wf.engine.parser.BaseBpmDefParser;
/*   */ 
/*   */ public abstract class AbsNodeParse<T> extends BaseBpmDefParser<T, BaseBpmNodeDef> {
/*   */   public boolean isSupport(BpmNodeDef nodeDef) {
/* 9 */     return true;
/*   */   }
/*   */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/node/AbsNodeParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */