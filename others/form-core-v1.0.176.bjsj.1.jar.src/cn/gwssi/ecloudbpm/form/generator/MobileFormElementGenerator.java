/*     */ package cn.gwssi.ecloudbpm.form.generator;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusColumnCtrlType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
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
/*     */ @Component
/*     */ public class MobileFormElementGenerator
/*     */   extends AbsFormElementGenerator
/*     */ {
/*     */   public String getColumn(IBusinessColumn column, IBusTableRel relation) {
/*  25 */     if ("1".equals("1")) {
/*  26 */       return super.getColumn(column, relation);
/*     */     }
/*     */     
/*  29 */     BusColumnCtrlType columnType = BusColumnCtrlType.getByKey(column.getCtrl().getType());
/*  30 */     String boCode = relation.getBusObj().getKey();
/*  31 */     ThreadMapUtil.put("boCode", boCode);
/*  32 */     ThreadMapUtil.put("relation", relation);
/*     */     
/*  34 */     return getColumnOnetext(column);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleVModel(Element element, IBusinessColumn column) {
/*  40 */     String boCode = (String)ThreadMapUtil.get("boCode");
/*  41 */     IBusTableRel relation = (IBusTableRel)ThreadMapUtil.get("relation");
/*     */     
/*  43 */     if (relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
/*  44 */       element.attr("v-model", column.getTable().getKey() + "." + column.getKey());
/*     */       
/*     */       return;
/*     */     } 
/*  48 */     element.attr("v-model", getScopePath(relation) + "." + column.getKey());
/*     */ 
/*     */     
/*  51 */     String configStr = column.getCtrl().getConfig();
/*  52 */     if (StringUtil.isEmpty(configStr)) {
/*     */       return;
/*     */     }
/*     */     
/*  56 */     JSONObject config = JSON.parseObject(configStr);
/*  57 */     Boolean placeholder = config.getBoolean("placeholder");
/*     */     
/*  59 */     if (placeholder == null || !placeholder.booleanValue())
/*  60 */       return;  element.attr("placeholder", config.getString("placeholderText"));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnOnetext(IBusinessColumn column) {
/*  66 */     Element element = getElement("input").attr("type", "text").addClass("weui-input");
/*     */     
/*  68 */     handleVModel(element, column);
/*  69 */     handlePermission(element, column);
/*  70 */     handleValidateRules(element, column);
/*     */     
/*  72 */     return element.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnDate(IBusinessColumn column) {
/*  77 */     Element element = getElement("ab-date");
/*     */     
/*  79 */     handleVModel(element, column);
/*  80 */     handleValidateRules(element, column);
/*  81 */     handlePermission(element, column);
/*  82 */     element.attr(":ab-permission", getPermissionPath(column));
/*     */     
/*  84 */     String configStr = column.getCtrl().getConfig();
/*  85 */     if (StringUtil.isEmpty(configStr)) {
/*  86 */       throw new BusinessException(String.format("表【%s】日期字段  【%s】解析失败,配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/*  89 */     String format = JSON.parseObject(configStr).getString("format");
/*     */     
/*  91 */     element.attr("format", format.replace("yyyy", "YYYY").replace("dd", "DD").replace(":ss", ""));
/*     */     
/*  93 */     return element.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnDic(IBusinessColumn column) {
/*  97 */     Element element = getElement("ab-dict").attr("type", "text").addClass("input-div");
/*     */     
/*  99 */     handleVModel(element, column);
/* 100 */     handleValidateRules(element, column);
/* 101 */     handlePermission(element, column);
/*     */     
/* 103 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 104 */     if (!config.containsKey("key")) {
/* 105 */       throw new BusinessException(String.format("表【%s】数据字典  字段【%s】解析失败,alias 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 108 */     element.attr("dict-key", config.getString("key"));
/*     */     
/* 110 */     return element.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnIdentity(IBusinessColumn column) {
/* 114 */     Element element = getElement("input").attr("type", "text").addClass("weui-input");
/* 115 */     handleVModel(element, column);
/* 116 */     handlePermission(element, column);
/* 117 */     handleValidateRules(element, column);
/*     */     
/* 119 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 120 */     if (!config.containsKey("alias")) {
/* 121 */       throw new BusinessException(String.format("表【%s】流水号  字段【%s】解析失败,alias 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/* 123 */     element.attr("ab-serial-no", config.getString("alias"));
/* 124 */     return element.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnMultitext(IBusinessColumn column) {
/* 128 */     Element element = getElement("textarea").attr("type", "text").addClass("weui-textarea");
/*     */     
/* 130 */     handleVModel(element, column);
/* 131 */     handlePermission(element, column);
/* 132 */     handleValidateRules(element, column);
/*     */     
/* 134 */     return element.toString();
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
/*     */   
/*     */   protected String getColumnCheckBox(IBusinessColumn column) {
/* 147 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 148 */     if (!config.containsKey("options")) {
/* 149 */       throw new BusinessException(String.format("表【%s】CheckBox 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 152 */     Element checkbox = getElement("ab-checkbox");
/* 153 */     handleVModel(checkbox, column);
/* 154 */     handleValidateRules(checkbox, column);
/* 155 */     handlePermission(checkbox, column);
/* 156 */     Element template = checkbox.appendElement("div").attr("slot-scope", "scope");
/*     */     
/* 158 */     JSONArray options = config.getJSONArray("options");
/* 159 */     for (int i = 0; i < options.size(); i++) {
/* 160 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 162 */       Element element = template.appendElement("label").addClass("checkbox-inline");
/* 163 */       Element child = element.appendElement("input").attr("type", "checkbox");
/* 164 */       child.attr("v-model", "scope.tempData.currentValue");
/* 165 */       child.attr("value", option.getString("key"));
/* 166 */       child.attr("name", String.format("%s-%s-%s", new Object[] { ThreadMapUtil.get("boCode"), column.getTable().getKey(), column.getName() }));
/* 167 */       handlePermission(child, column);
/* 168 */       element.appendText(option.getString("txt"));
/*     */     } 
/*     */     
/* 171 */     return checkbox.toString();
/*     */   }
/*     */   
/*     */   protected String getColumnRadio(IBusinessColumn column) {
/* 175 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 176 */     if (!config.containsKey("options")) {
/* 177 */       throw new BusinessException(String.format("表【%s】Radio 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 180 */     Element permissionElement = getElement("div");
/* 181 */     JSONArray options = config.getJSONArray("options");
/* 182 */     for (int i = 0; i < options.size(); i++) {
/* 183 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 185 */       Element label = permissionElement.appendElement("label").addClass("radio-inline");
/* 186 */       Element child = label.appendElement("input").attr("type", "radio");
/*     */       
/* 188 */       handleVModel(child, column);
/* 189 */       child.attr("v-ab-permission", getPermissionPath(column));
/* 190 */       handleValidateRules(child, column);
/*     */       
/* 192 */       child.attr("value", option.getString("key"));
/* 193 */       label.appendText(option.getString("txt"));
/*     */     } 
/*     */     
/* 196 */     return permissionElement.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnSelect(IBusinessColumn column, Boolean isMultiple) {
/* 201 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 202 */     if (!config.containsKey("options")) {
/* 203 */       throw new BusinessException(String.format("表【%s】Select 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 206 */     Element permissionElement = getElement("select").addClass("weui-input");
/* 207 */     handleVModel(permissionElement, column);
/* 208 */     permissionElement.attr("v-ab-permission", getPermissionPath(column));
/* 209 */     handleValidateRules(permissionElement, column);
/*     */     
/* 211 */     if (isMultiple.booleanValue()) {
/* 212 */       permissionElement.attr("multiple", "true");
/*     */     }
/*     */     
/* 215 */     Element select = permissionElement.appendElement("option");
/* 216 */     select.attr("value", "");
/* 217 */     select.text("请选择");
/*     */     
/* 219 */     JSONArray options = config.getJSONArray("options");
/* 220 */     for (int i = 0; i < options.size(); i++) {
/* 221 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 223 */       Element element = permissionElement.appendElement("option");
/* 224 */       element.attr("value", option.getString("key"));
/* 225 */       element.text(option.getString("txt"));
/*     */     } 
/*     */     
/* 228 */     return permissionElement.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnFile(IBusinessColumn column) {
/* 239 */     Element element = getElement("ab-upload").attr("v-bind:permission", getPermissionPath(column));
/* 240 */     handleVModel(element, column);
/* 241 */     element.append("<a href=\"javascript:;\" class=\"weui-btn weui-btn_mini weui-btn_primary fa fa-upload\">" + column.getComment() + "</a>");
/* 242 */     return element.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSubAttrs(IBusTableRel rel) {
/* 247 */     StringBuilder sb = new StringBuilder();
/* 248 */     sb.append(" id=\"" + rel.getBusObj().getKey() + "-" + rel.getTableKey() + "\" ");
/*     */ 
/*     */     
/* 251 */     if (rel.getType().equals(BusTableRelType.ONE_TO_MANY.getKey()) && 
/* 252 */       !rel.getParent().getType().equals(BusTableRelType.MAIN.getKey())) {
/*     */       
/* 254 */       sb.append(" v-transfer-dom ");
/* 255 */       sb.append(" v-if=\"subTempData." + rel.getTableKey() + "List\" ");
/*     */     } 
/*     */     
/* 258 */     sb.append(" table-key=\"" + rel.getTableKey() + "\" ");
/* 259 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected void handlePermission(Element element, IBusinessColumn column) {
/* 263 */     element.attr("v-ab-permission", getPermissionPath(column));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleValidateRules(Element element, IBusinessColumn column) {
/* 268 */     super.handleValidateRules(element, column);
/*     */     
/* 270 */     element.attr("v-ab-validate", element.attr("ab-validate"));
/* 271 */     element.removeAttr("ab-validate");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getGeneratorName() {
/* 277 */     return "mobileGenerator";
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/generator/MobileFormElementGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */