/*    */ package cn.gwssi.ecloudbpm.bus.deserializer;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.api.model.ISqlRule;
/*    */ import cn.gwssi.ecloudbpm.bus.constant.SqlRuleType;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import com.alibaba.fastjson.parser.DefaultJSONParser;
/*    */ import com.alibaba.fastjson.parser.deserializer.ObjectDeserializer;
/*    */ import java.lang.reflect.Type;
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
/*    */ public class FastJsonLocalISqlRuleDeserializer
/*    */   implements ObjectDeserializer
/*    */ {
/*    */   public static FastJsonLocalISqlRuleDeserializer getGlobalInstance() {
/* 24 */     return global;
/*    */   }
/*    */ 
/*    */   
/* 28 */   public static FastJsonLocalISqlRuleDeserializer global = new FastJsonLocalISqlRuleDeserializer();
/*    */ 
/*    */ 
/*    */   
/*    */   public ISqlRule deserialze(DefaultJSONParser defaultJSONParser, Type type, Object o) {
/* 33 */     JSONObject rule = defaultJSONParser.parseObject();
/* 34 */     if (null != rule) {
/* 35 */       Class clazz = SqlRuleType.getClassByKey(rule.getString("type"));
/* 36 */       if (null != clazz) {
/* 37 */         return (ISqlRule)JSON.parseObject(rule.toJSONString(), SqlRuleType.getClassByKey(rule.getString("type")));
/*    */       }
/*    */     } 
/* 40 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getFastMatchToken() {
/* 45 */     return 0;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/deserializer/FastJsonLocalISqlRuleDeserializer.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */