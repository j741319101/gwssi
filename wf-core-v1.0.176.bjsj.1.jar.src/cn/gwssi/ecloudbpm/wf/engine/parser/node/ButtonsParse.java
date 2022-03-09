/*    */ package cn.gwssi.ecloudbpm.wf.engine.parser.node;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmDef;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.Button;
/*    */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.impl.BaseBpmNodeDef;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.hutool.core.collection.CollectionUtil;
/*    */ import cn.hutool.core.map.MapUtil;
/*    */ import cn.hutool.core.util.ArrayUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONArray;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class ButtonsParse
/*    */   extends AbsNodeParse<Button>
/*    */ {
/*    */   public String getKey() {
/* 27 */     return "btnList";
/*    */   }
/*    */ 
/*    */   
/*    */   public void setDefParam(BaseBpmNodeDef userNodeDef, Object object) {
/* 32 */     List<Button> btnList = (List<Button>)object;
/*    */     
/* 34 */     userNodeDef.setButtons(btnList);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isArray() {
/* 39 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void JSONAmend(BaseBpmNodeDef userNodeDef, Object object, JSON thisJson) {
/* 47 */     JSONObject jsonConfig = (JSONObject)thisJson;
/*    */     
/* 49 */     if (isEmpty(object)) {
/* 50 */       jsonConfig.put("btnList", JSON.toJSON(userNodeDef.getButtons()));
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean isEmpty(Object object) {
/* 55 */     if (object == null) return true;
/*    */     
/* 57 */     if (object instanceof String) return StringUtil.isEmpty((String)object);
/*    */     
/* 59 */     if (object instanceof Collection) {
/* 60 */       return CollectionUtil.isEmpty((Collection)object);
/*    */     }
/*    */     
/* 63 */     if (object.getClass().isArray()) {
/* 64 */       return ArrayUtil.isEmpty((Object[])object);
/*    */     }
/*    */     
/* 67 */     if (object instanceof Map) {
/* 68 */       return MapUtil.isEmpty((Map)object);
/*    */     }
/*    */     
/* 71 */     if (object instanceof JSON) {
/* 72 */       return ((JSONObject)object).isEmpty();
/*    */     }
/*    */     
/* 75 */     if (object instanceof JSONArray) {
/* 76 */       return ((JSONArray)object).isEmpty();
/*    */     }
/* 78 */     return false;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/parser/node/ButtonsParse.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */