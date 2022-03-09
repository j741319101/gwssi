/*    */ package cn.gwssi.ecloudframework.sys.service.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.sys.api.model.ISysTreeNode;
/*    */ import cn.gwssi.ecloudframework.sys.api.service.ISysTreeNodeService;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.SysTreeManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.SysTreeNodeManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.SysTree;
/*    */ import java.util.Collections;
/*    */ import java.util.List;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
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
/*    */ @Service
/*    */ public class SysTreeNodeService
/*    */   implements ISysTreeNodeService
/*    */ {
/*    */   @Autowired
/*    */   SysTreeNodeManager sysTreeNodeManager;
/*    */   @Autowired
/*    */   SysTreeManager sysTreeManager;
/*    */   
/*    */   public ISysTreeNode getById(String id) {
/* 32 */     return (ISysTreeNode)this.sysTreeNodeManager.get(id);
/*    */   }
/*    */ 
/*    */   
/*    */   public List<? extends ISysTreeNode> getTreeNodesByType(String treeKey) {
/* 37 */     SysTree tree = this.sysTreeManager.getByKey(treeKey);
/* 38 */     if (tree == null) return Collections.emptyList();
/*    */     
/* 40 */     return this.sysTreeNodeManager.getByTreeId(tree.getId());
/*    */   }
/*    */ 
/*    */   
/*    */   public List<? extends ISysTreeNode> getTreeNodesByNodeId(String nodeId) {
/* 45 */     ISysTreeNode node = (ISysTreeNode)this.sysTreeNodeManager.get(nodeId);
/* 46 */     return this.sysTreeNodeManager.getStartWithPath(node.getPath());
/*    */   }
/*    */ 
/*    */   
/*    */   public String creatByTreeKey(String treeKey, String nodeName) {
/* 51 */     return this.sysTreeNodeManager.creatByTreeKey(treeKey, nodeName);
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/service/impl/SysTreeNodeService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */