/*    */ package cn.gwssi.ecloudbpm.bus.api.remote;
/*    */ 
/*    */ import com.alibaba.fastjson.JSONObject;
/*    */ import io.swagger.annotations.ApiModel;
/*    */ import io.swagger.annotations.ApiModelProperty;
/*    */ import java.io.Serializable;
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
/*    */ @ApiModel(description = "远程业务对象DTO")
/*    */ public class RemoteBusinessData<T>
/*    */   implements Serializable
/*    */ {
/*    */   private static final long serialVersionUID = -6612311363502701439L;
/*    */   @ApiModelProperty("业务对象KEY")
/*    */   protected String boKey;
/*    */   @ApiModelProperty("业务对象JSON")
/*    */   protected T data;
/*    */   protected JSONObject bpmInstanceData;
/*    */   @ApiModelProperty("业务对象id")
/*    */   protected Object id;
/*    */   
/*    */   public RemoteBusinessData(String boKey, Object id) {
/* 48 */     this.boKey = boKey;
/* 49 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public RemoteBusinessData() {}
/*    */ 
/*    */   
/*    */   public RemoteBusinessData(T data, String boKey) {
/* 57 */     this.boKey = boKey;
/* 58 */     this.data = data;
/*    */   }
/*    */   
/*    */   public String getBoKey() {
/* 62 */     return this.boKey;
/*    */   }
/*    */   
/*    */   public void setBoKey(String boKey) {
/* 66 */     this.boKey = boKey;
/*    */   }
/*    */   
/*    */   public T getData() {
/* 70 */     return this.data;
/*    */   }
/*    */   
/*    */   public void setData(T data) {
/* 74 */     this.data = data;
/*    */   }
/*    */   
/*    */   public Object getId() {
/* 78 */     return this.id;
/*    */   }
/*    */   
/*    */   public void setId(Object id) {
/* 82 */     this.id = id;
/*    */   }
/*    */   
/*    */   public JSONObject getBpmInstanceData() {
/* 86 */     return this.bpmInstanceData;
/*    */   }
/*    */   
/*    */   public void setBpmInstanceData(JSONObject bpmInstanceData) {
/* 90 */     this.bpmInstanceData = bpmInstanceData;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-api/v1.0.176.bjsj.1/bus-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/api/remote/RemoteBusinessData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */