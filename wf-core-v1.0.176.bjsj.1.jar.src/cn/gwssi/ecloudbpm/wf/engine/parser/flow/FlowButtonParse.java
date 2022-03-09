/*    */ package cn.gwssi.ecloudbpm.wf.engine.parser.flow;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.action.button.ButtonFactory;
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.Button;
/*    */ import cn.gwssi.ecloudbpm.wf.engine.model.DefaultBpmProcessDef;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.List;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class FlowButtonParse
/*    */   extends AbsFlowParse<Button>
/*    */ {
/*    */   public String getKey() {
/* 20 */     return "instanceBtnList";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefParam(DefaultBpmProcessDef bpmdef, Object object) {
/* 25 */     List<Button> btnList = (List<Button>)object;
/* 26 */     bpmdef.setInstanceBtnList(btnList);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isArray() {
/* 33 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void JSONAmend(DefaultBpmProcessDef bpmdef, Object args, JSON configJson) {
/* 39 */     JSONObject jsonConfig = (JSONObject)configJson;
/* 40 */     if (!jsonConfig.containsKey("instanceBtnList")) {
/*    */       
/* 42 */       List<Button> instanceButtons = ButtonFactory.getInstanceButtons(true);
/*    */       
/* 44 */       jsonConfig.put("instanceBtnList", JSON.toJSON(instanceButtons));
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/flow/FlowButtonParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */