/*    */ package cn.gwssi.ecloudbpm.wf.plugin.global.carboncopy.def;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.engine.plugin.plugindef.AbstractBpmExecutionPluginDef;
/*    */ import java.util.List;
/*    */ import java.util.Map;
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
/*    */ public class CarbonCopyPluginDef
/*    */   extends AbstractBpmExecutionPluginDef
/*    */ {
/*    */   private static final long serialVersionUID = 4987507273918000937L;
/*    */   private Map<String, List<BpmCarbonCopy>> nodeEventCarbonCopyMap;
/*    */   
/*    */   public Map<String, List<BpmCarbonCopy>> getNodeEventCarbonCopyMap() {
/* 23 */     return this.nodeEventCarbonCopyMap;
/*    */   }
/*    */   
/*    */   public void setNodeEventCarbonCopyMap(Map<String, List<BpmCarbonCopy>> nodeEventCarbonCopyMap) {
/* 27 */     this.nodeEventCarbonCopyMap = nodeEventCarbonCopyMap;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String getMapKey(String nodeId, String event) {
/* 38 */     return nodeId + "_" + event;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin-biz/v1.0.176.bjsj.1/wf-plugin-biz-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/global/carboncopy/def/CarbonCopyPluginDef.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */