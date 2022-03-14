/*    */ package cn.gwssi.ecloudbpm.bus.executor.assemblyval;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusinessData;
/*    */ import com.dstz.base.api.constant.ColumnType;
/*    */ import com.dstz.base.api.exception.BusinessException;
/*    */ import cn.hutool.core.date.DateUtil;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class AssemblyValTypeExecutor
/*    */   extends AssemblyValExecuteChain
/*    */ {
/*    */   public int getSn() {
/* 31 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   protected void run(AssemblyValParam param) {
/* 36 */     BusinessData businessData = param.getBusinessData();
/* 37 */     JSONObject data = param.getData();
/*    */     
/* 39 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)businessData.getData().entrySet()) {
/* 40 */       BusinessColumn column = businessData.getBusTableRel().getTable().getColumnByKey(entry.getKey());
/* 41 */       if (column == null) {
/* 42 */         throw new BusinessException("businessData中出现了非业务对象的字段，请核查：" + (String)entry.getKey());
/*    */       }
/*    */       
/* 45 */       if (ColumnType.DATE.equalsWithKey(column.getType()) && entry.getValue() != null) {
/* 46 */         JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 47 */         data.put(column.getKey(), DateUtil.format((Date)entry.getValue(), config.getString("format"))); continue;
/*    */       } 
/* 49 */       data.put(entry.getKey(), entry.getValue());
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/executor/assemblyval/AssemblyValTypeExecutor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */