/*     */ package cn.gwssi.ecloudbpm.form.generator;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.ThreadMapUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class PcFormElementGenerator
/*     */   extends AbsFormElementGenerator
/*     */ {
/*     */   private void handleNgModel(Element element, IBusinessColumn column) {
/*  25 */     String boCode = (String)ThreadMapUtil.get("boCode");
/*  26 */     IBusTableRel relation = (IBusTableRel)ThreadMapUtil.get("relation");
/*     */     
/*  28 */     if (relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
/*  29 */       element.attr("ng-model", column.getTable().getKey() + "." + column.getKey());
/*     */       
/*     */       return;
/*     */     } 
/*  33 */     element.attr("ng-model", getScopePath(relation) + "." + column.getKey());
/*     */ 
/*     */     
/*  36 */     String configStr = column.getCtrl().getConfig();
/*  37 */     if (StringUtil.isEmpty(configStr)) {
/*     */       return;
/*     */     }
/*     */     
/*  41 */     JSONObject config = JSON.parseObject(configStr);
/*  42 */     Boolean placeholder = config.getBoolean("placeholder");
/*     */     
/*  44 */     if (placeholder == null || !placeholder.booleanValue())
/*  45 */       return;  element.attr("placeholder", config.getString("placeholderText"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnOnetext(IBusinessColumn column) {
/*  51 */     Element element = getElement("input").attr("type", "text").addClass("form-control");
/*     */     
/*  53 */     handleNgModel(element, column);
/*  54 */     handlePermission(element, column);
/*  55 */     handleValidateRules(element, column);
/*     */     
/*  57 */     return element.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnDate(IBusinessColumn column) {
/*  62 */     Element element = getElement("input").addClass("form-control");
/*     */     
/*  64 */     handleNgModel(element, column);
/*  65 */     handleValidateRules(element, column);
/*  66 */     handlePermission(element, column);
/*     */     
/*  68 */     String configStr = column.getCtrl().getConfig();
/*  69 */     if (StringUtil.isEmpty(configStr)) {
/*  70 */       throw new BusinessException(String.format("表【%s】日期字段  【%s】解析失败,配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*  72 */     element.attr("ab-date", JSON.parseObject(configStr).getString("format"));
/*     */     
/*  74 */     return element.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnDic(IBusinessColumn column) {
/*  78 */     Element element = getElement("span").attr("type", "text").addClass("input-div");
/*     */     
/*  80 */     handleNgModel(element, column);
/*  81 */     handleValidateRules(element, column);
/*  82 */     handlePermission(element, column);
/*     */     
/*  84 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/*  85 */     if (!config.containsKey("key")) {
/*  86 */       throw new BusinessException(String.format("表【%s】数据字典  字段【%s】解析失败,alias 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/*  89 */     element.attr("ab-combox", element.attr("ng-model"));
/*  90 */     element.attr("dict-key", config.getString("key"));
/*     */     
/*  92 */     return element.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnIdentity(IBusinessColumn column) {
/*  96 */     Element element = getElement("input").attr("type", "text").addClass("form-control");
/*  97 */     handleNgModel(element, column);
/*  98 */     handlePermission(element, column);
/*  99 */     handleValidateRules(element, column);
/*     */     
/* 101 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 102 */     if (!config.containsKey("alias")) {
/* 103 */       throw new BusinessException(String.format("表【%s】流水号  字段【%s】解析失败,alias 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/* 105 */     element.attr("ab-serial-no", config.getString("alias"));
/* 106 */     return element.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnMultitext(IBusinessColumn column) {
/* 110 */     Element element = getElement("textarea").attr("type", "text").addClass("form-control");
/*     */     
/* 112 */     handleNgModel(element, column);
/* 113 */     handlePermission(element, column);
/* 114 */     handleValidateRules(element, column);
/*     */     
/* 116 */     return element.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnCheckBox(IBusinessColumn column) {
/* 120 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 121 */     if (!config.containsKey("options")) {
/* 122 */       throw new BusinessException(String.format("表【%s】CheckBox 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 125 */     Element permissionElement = getElement("div");
/* 126 */     handleNgModel(permissionElement, column);
/* 127 */     permissionElement.attr("ab-checked-permission", getPermissionPath(column));
/* 128 */     handleValidateRules(permissionElement, column);
/*     */     
/* 130 */     JSONArray options = config.getJSONArray("options");
/* 131 */     for (int i = 0; i < options.size(); i++) {
/* 132 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 134 */       Element element = permissionElement.appendElement("label").addClass("checkbox-inline");
/* 135 */       Element child = element.appendElement("input").attr("type", "checkbox");
/* 136 */       child.attr("ab-checkbox", "");
/* 137 */       handleNgModel(child, column);
/* 138 */       child.attr("value", option.getString("key"));
/* 139 */       element.appendText(option.getString("txt"));
/*     */     } 
/*     */     
/* 142 */     return permissionElement.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnRadio(IBusinessColumn column) {
/* 146 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 147 */     if (!config.containsKey("options")) {
/* 148 */       throw new BusinessException(String.format("表【%s】Radio 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 151 */     Element permissionElement = getElement("div");
/* 152 */     handleNgModel(permissionElement, column);
/* 153 */     permissionElement.attr("ab-checked-permission", getPermissionPath(column));
/* 154 */     handleValidateRules(permissionElement, column);
/*     */     
/* 156 */     JSONArray options = config.getJSONArray("options");
/* 157 */     for (int i = 0; i < options.size(); i++) {
/* 158 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 160 */       Element element = permissionElement.appendElement("label").addClass("radio-inline");
/* 161 */       Element child = element.appendElement("input").attr("type", "radio");
/* 162 */       handleNgModel(child, column);
/* 163 */       child.attr("value", option.getString("key"));
/* 164 */       element.appendText(option.getString("txt"));
/*     */     } 
/*     */     
/* 167 */     return permissionElement.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnSelect(IBusinessColumn column, Boolean isMultiple) {
/* 171 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 172 */     if (!config.containsKey("options")) {
/* 173 */       throw new BusinessException(String.format("表【%s】Select 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 176 */     Element permissionElement = getElement("select").addClass("form-control");
/* 177 */     handleNgModel(permissionElement, column);
/* 178 */     permissionElement.attr("ab-checked-permission", getPermissionPath(column));
/* 179 */     handleValidateRules(permissionElement, column);
/*     */     
/* 181 */     if (isMultiple.booleanValue()) {
/* 182 */       permissionElement.attr("multiple", "true");
/* 183 */       permissionElement.attr("ab-array-str", "");
/*     */     } 
/*     */     
/* 186 */     JSONArray options = config.getJSONArray("options");
/* 187 */     for (int i = 0; i < options.size(); i++) {
/* 188 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 190 */       Element element = permissionElement.appendElement("option");
/* 191 */       element.attr("value", option.getString("key"));
/* 192 */       element.text(option.getString("txt"));
/*     */     } 
/*     */     
/* 195 */     return permissionElement.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnFile(IBusinessColumn column) {
/* 200 */     Element element = getElement("a").attr("href", "javascript:void(0)").addClass("btn btn-primary fa-upload");
/* 201 */     element.attr("ab-upload", "");
/* 202 */     handleNgModel(element, column);
/* 203 */     element.attr("ab-edit-permission", getPermissionPath(column));
/*     */     
/* 205 */     return element.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSubAttrs(IBusTableRel rel) {
/* 210 */     StringBuilder sb = new StringBuilder();
/* 211 */     sb.append(" id=\"" + rel.getBusObj().getKey() + "-" + rel.getTableKey() + "\" ");
/*     */ 
/*     */     
/* 214 */     if (rel.getType().equals(BusTableRelType.ONE_TO_MANY.getKey()) && 
/* 215 */       !rel.getParent().getType().equals(BusTableRelType.MAIN.getKey())) {
/* 216 */       sb.append(" hide ");
/*     */     }
/*     */     
/* 219 */     sb.append(" table-key=\"" + rel.getTableKey() + "\" ");
/*     */     
/* 221 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGeneratorName() {
/* 227 */     return "generator";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/generator/PcFormElementGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */