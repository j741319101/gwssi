/*     */ package cn.gwssi.ecloudframework.base.api.request;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import java.math.BigDecimal;
/*     */ import java.text.DateFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RequestParam
/*     */ {
/*     */   private RequestHead head;
/*  26 */   private Map<String, Object> params = new HashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addParam(String key, Object val) {
/*  35 */     this.params.put(key, val);
/*     */   }
/*     */   
/*     */   public Map<String, Object> getParams() {
/*  39 */     return this.params;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setParams(Map<String, Object> params) {
/*  44 */     this.params = params;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getParam(String key) {
/*  49 */     return this.params.get(key);
/*     */   }
/*     */   
/*     */   public String getStringParam(String key) {
/*  53 */     if (!this.params.containsKey(key)) return ""; 
/*  54 */     return (String)this.params.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getRQString(String key, String errorMsg) {
/*  66 */     if (isParamEmpty(key)) {
/*  67 */       throw new BusinessMessage(errorMsg + "[" + key + "]");
/*     */     }
/*  69 */     return (String)this.params.get(key);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Object getRQObject(String key, String errorMsg) {
/*  76 */     if (isParamEmpty(key)) {
/*  77 */       throw new BusinessMessage(errorMsg + "[" + key + "]");
/*     */     }
/*     */     
/*  80 */     return this.params.get(key);
/*     */   }
/*     */   
/*     */   public BigDecimal getBigDecimal(String key) {
/*  84 */     Object val = this.params.get(key);
/*  85 */     if (val instanceof String) {
/*  86 */       BigDecimal bd = new BigDecimal((String)val);
/*  87 */       return bd;
/*     */     } 
/*     */     
/*  90 */     return (BigDecimal)this.params.get(key);
/*     */   }
/*     */   
/*     */   public Date getRQDate(String key, String errorMsg) {
/*  94 */     if (isParamEmpty(key)) {
/*  95 */       throw new BusinessMessage(errorMsg + "[" + key + "]");
/*     */     }
/*     */     
/*  98 */     return getDate(key);
/*     */   }
/*     */   public Date getDate(String key) {
/*     */     DateFormat dateFormat;
/* 102 */     Object date = this.params.get(key);
/* 103 */     if (date instanceof Date) {
/* 104 */       return (Date)date;
/*     */     }
/*     */     
/* 107 */     String dateStr = (String)date;
/*     */ 
/*     */     
/* 110 */     if (dateStr.indexOf("/") != -1) {
/* 111 */       dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
/* 112 */     } else if (dateStr.indexOf("-") != -1) {
/* 113 */       dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/*     */     } else {
/* 115 */       return null;
/*     */     } 
/*     */     
/*     */     try {
/* 119 */       Date date1 = null;
/*     */       try {
/* 121 */         date1 = dateFormat.parse(dateStr);
/* 122 */       } catch (Exception e) {
/*     */         
/* 124 */         date1 = dateFormat.parse(dateStr);
/*     */       } 
/* 126 */       return date1;
/* 127 */     } catch (Exception e) {
/* 128 */       throw new BusinessException(key + "日期解析出现异常" + e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isParamEmpty(String key) {
/* 133 */     if (!this.params.containsKey(key)) return true;
/*     */     
/* 135 */     Object o = this.params.get(key);
/* 136 */     if (o == null) {
/* 137 */       return true;
/*     */     }
/*     */     
/* 140 */     if (o instanceof String) {
/* 141 */       if (((String)o).trim().length() == 0)
/* 142 */         return true; 
/* 143 */     } else if (o instanceof Collection) {
/* 144 */       if (((Collection)o).size() == 0)
/* 145 */         return true; 
/* 146 */     } else if (o.getClass().isArray()) {
/* 147 */       if (((Object[])o).length == 0)
/* 148 */         return true; 
/* 149 */     } else if (o instanceof Map && (
/* 150 */       (Map)o).size() == 0) {
/* 151 */       return true;
/*     */     } 
/*     */     
/* 154 */     return false;
/*     */   }
/*     */   
/*     */   public RequestHead getHead() {
/* 158 */     return this.head;
/*     */   }
/*     */   
/*     */   public void setHead(RequestHead head) {
/* 162 */     this.head = head;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/request/RequestParam.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */