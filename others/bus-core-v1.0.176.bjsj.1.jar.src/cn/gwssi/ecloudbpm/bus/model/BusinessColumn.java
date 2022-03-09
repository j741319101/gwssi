/*     */ package cn.gwssi.ecloudbpm.bus.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusColumnCtrl;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessTable;
/*     */ import cn.gwssi.ecloudframework.base.api.constant.ColumnType;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.api.model.IBaseModel;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.table.Column;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.gwssi.ecloudframework.sys.util.ContextUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import javax.validation.Valid;
/*     */ import org.hibernate.validator.constraints.NotEmpty;
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
/*     */ 
/*     */ public class BusinessColumn
/*     */   extends Column
/*     */   implements IBaseModel, IBusinessColumn
/*     */ {
/*     */   @NotEmpty
/*     */   private String id;
/*     */   @NotEmpty
/*     */   private String key;
/*     */   @NotEmpty
/*     */   private String tableId;
/*     */   private boolean searchFlag;
/*     */   @Valid
/*     */   private BusColumnCtrl ctrl;
/*     */   private BusinessTable table;
/*     */   protected Date createTime;
/*     */   protected String createBy;
/*     */   protected Date updateTime;
/*     */   protected String updateBy;
/*     */   protected String metadataId;
/*     */   
/*     */   public String getId() {
/*  68 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  72 */     this.id = id;
/*     */   }
/*     */   
/*     */   public String getKey() {
/*  76 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/*  80 */     this.key = key;
/*     */   }
/*     */   
/*     */   public BusColumnCtrl getCtrl() {
/*  84 */     return this.ctrl;
/*     */   }
/*     */   
/*     */   public void setCtrl(BusColumnCtrl ctrl) {
/*  88 */     this.ctrl = ctrl;
/*     */   }
/*     */   
/*     */   public String getTableId() {
/*  92 */     return this.tableId;
/*     */   }
/*     */   
/*     */   public void setTableId(String tableId) {
/*  96 */     this.tableId = tableId;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessTable getTable() {
/* 101 */     return this.table;
/*     */   }
/*     */   
/*     */   public void setTable(BusinessTable table) {
/* 105 */     this.table = table;
/*     */   }
/*     */ 
/*     */   
/*     */   public Object initValue() {
/* 110 */     return value(this.defaultValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object value(String str) {
/* 115 */     if (StringUtil.isEmpty(str)) {
/* 116 */       return null;
/*     */     }
/*     */     
/* 119 */     if (str.startsWith("#{") && str.endsWith("}")) {
/* 120 */       IGroovyScriptEngine engine = (IGroovyScriptEngine)AppUtil.getBean(IGroovyScriptEngine.class);
/* 121 */       String script = str.substring(2, str.length() - 1);
/* 122 */       return engine.executeObject(script, new HashMap<>());
/*     */     } 
/*     */     
/* 125 */     if (str.startsWith("${") && str.endsWith("}")) {
/* 126 */       return handleInitValue(str.substring(2, str.length() - 1));
/*     */     }
/*     */     
/* 129 */     Object value = null;
/*     */     try {
/* 131 */       if (ColumnType.VARCHAR.equalsWithKey(this.type) || ColumnType.CLOB.equalsWithKey(this.type)) {
/* 132 */         value = str;
/* 133 */       } else if (ColumnType.NUMBER.equalsWithKey(this.type)) {
/* 134 */         if ("false".equals(str)) {
/* 135 */           str = "0";
/*     */         }
/* 137 */         if ("true".equals(str)) {
/* 138 */           str = "1";
/*     */         }
/* 140 */         BigDecimal bigDecimal = new BigDecimal(str);
/*     */         
/* 142 */         value = bigDecimal.setScale(getDecimal(), RoundingMode.HALF_UP);
/* 143 */       } else if (ColumnType.DATE.equalsWithKey(this.type)) {
/* 144 */         JSONObject config = JSON.parseObject(getCtrl().getConfig());
/*     */         try {
/* 146 */           value = DateUtil.parse(str, config.getString("format"));
/* 147 */         } catch (Exception e) {
/* 148 */           value = DateUtil.parse(str);
/*     */         } 
/*     */       } 
/* 151 */     } catch (Exception e) {
/* 152 */       ColumnType columnType = ColumnType.getByKey(this.type);
/* 153 */       throw new BusinessException("字段值解析失败，无法把字符串[" + str + "]转化成" + columnType.getDesc() + "[" + columnType.getKey() + "]");
/*     */     } 
/* 155 */     return value;
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
/*     */   private static Object handleInitValue(String constant) {
/* 167 */     Map<String, Object> constantMap = new HashMap<>();
/* 168 */     constantMap.put("currentUserId", ContextUtil.getCurrentUserId());
/* 169 */     constantMap.put("currentUserName", ContextUtil.getCurrentUserName());
/* 170 */     constantMap.put("currentUserAccount", ContextUtil.getCurrentUserAccount());
/* 171 */     if (ContextUtil.getCurrentGroup() != null) {
/* 172 */       constantMap.put("currentOrgId", ContextUtil.getCurrentGroupId());
/* 173 */       constantMap.put("currentOrgName", ContextUtil.getCurrentGroup().getGroupName());
/* 174 */       constantMap.put("currentOrgCode", ContextUtil.getCurrentGroup().getGroupCode());
/*     */     } 
/* 176 */     constantMap.put("currentDateTime", new Date());
/* 177 */     return constantMap.get(constant);
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getCreateTime() {
/* 182 */     return this.createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateTime(Date createTime) {
/* 187 */     this.createTime = createTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCreateBy() {
/* 192 */     return this.createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCreateBy(String createBy) {
/* 197 */     this.createBy = createBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public Date getUpdateTime() {
/* 202 */     return this.updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateTime(Date updateTime) {
/* 207 */     this.updateTime = updateTime;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getUpdateBy() {
/* 212 */     return this.updateBy;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateBy(String updateBy) {
/* 217 */     this.updateBy = updateBy;
/*     */   }
/*     */   
/*     */   public boolean isSearchFlag() {
/* 221 */     return this.searchFlag;
/*     */   }
/*     */   
/*     */   public void setSearchFlag(boolean searchFlag) {
/* 225 */     this.searchFlag = searchFlag;
/*     */   }
/*     */   
/*     */   public String getMetadataId() {
/* 229 */     return this.metadataId;
/*     */   }
/*     */   
/*     */   public void setMetadataId(String metadataId) {
/* 233 */     this.metadataId = metadataId;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusinessColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */