/*     */ package cn.gwssi.ecloudbpm.form.generator;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormColumn;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormGroup;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class VueFormElementGenerator
/*     */   extends AbsFormElementGenerator
/*     */ {
/*     */   private void handleVModel(Element element, IBusinessColumn column) {
/*  31 */     String boCode = (String)ThreadMapUtil.get("boCode");
/*  32 */     IBusTableRel relation = (IBusTableRel)ThreadMapUtil.get("relation");
/*     */     
/*  34 */     if (relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
/*  35 */       element.attr("v-model", column.getTable().getKey() + "." + column.getKey());
/*     */       
/*     */       return;
/*     */     } 
/*  39 */     element.attr("v-model", getScopePath(relation) + "." + column.getKey());
/*     */ 
/*     */     
/*  42 */     handleElementPlaceHolder(column, element);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnOnetext(IBusinessColumn column) {
/*  48 */     Element element = getElement("input").attr("type", "text").addClass("form-control");
/*     */     
/*  50 */     handleVModel(element, column);
/*  51 */     handlePermission(element, column);
/*  52 */     handleValidateRules(element, column);
/*     */     
/*  54 */     return element.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnDate(IBusinessColumn column) {
/*  62 */     Element element = getElement("ab-date").attr("type", "text").addClass("form-control");
/*     */     
/*  64 */     handleVModel(element, column);
/*  65 */     handlePermission(element, column);
/*  66 */     handleValidateRules(element, column);
/*     */     
/*  68 */     String configStr = column.getCtrl().getConfig();
/*  69 */     if (StringUtil.isEmpty(configStr)) {
/*  70 */       throw new BusinessException(String.format("表【%s】日期字段  【%s】解析失败,配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/*  73 */     String format = JSON.parseObject(configStr).getString("format");
/*     */     
/*  75 */     element.attr("format", format);
/*     */     
/*  77 */     return element.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnDic(IBusinessColumn column) {
/*  82 */     Element element = getElement("ab-dict").attr("type", "text").addClass("input-div");
/*     */     
/*  84 */     handleVModel(element, column);
/*  85 */     handlePermission(element, column);
/*  86 */     handleValidateRules(element, column);
/*     */     
/*  88 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/*  89 */     if (!config.containsKey("key")) {
/*  90 */       throw new BusinessException(String.format("表【%s】数据字典  字段【%s】解析失败,alias 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/*  93 */     element.attr("dict-key", config.getString("key"));
/*     */     
/*  95 */     return element.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnIdentity(IBusinessColumn column) {
/* 105 */     Element element = getElement("ab-serial-no").attr("type", "text").addClass("form-control");
/* 106 */     handleVModel(element, column);
/* 107 */     element.attr("v-bind:ab-permission", getPermissionPath(column));
/* 108 */     handleValidateRules(element, column);
/*     */     
/* 110 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 111 */     if (!config.containsKey("alias")) {
/* 112 */       throw new BusinessException(String.format("表【%s】流水号  字段【%s】解析失败,alias 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/* 114 */     element.attr("serial-no", config.getString("alias"));
/*     */     
/* 116 */     return element.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnMultitext(IBusinessColumn column) {
/* 121 */     Element element = getElement("textarea").attr("type", "text").addClass("form-control");
/*     */     
/* 123 */     handleVModel(element, column);
/* 124 */     handlePermission(element, column);
/* 125 */     handleValidateRules(element, column);
/*     */     
/* 127 */     return element.toString();
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
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnCheckBox(IBusinessColumn column) {
/* 143 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 144 */     if (!config.containsKey("options")) {
/* 145 */       throw new BusinessException(String.format("表【%s】CheckBox 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 148 */     Element checkbox = getElement("ab-checkbox");
/* 149 */     handleVModel(checkbox, column);
/* 150 */     handlePermission(checkbox, column);
/* 151 */     handleValidateRules(checkbox, column);
/* 152 */     Element template = checkbox.appendElement("div").attr("slot-scope", "checkboxScope");
/*     */     
/* 154 */     JSONArray options = config.getJSONArray("options");
/* 155 */     for (int i = 0; i < options.size(); i++) {
/* 156 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 158 */       Element element = template.appendElement("label").addClass("checkbox-inline");
/* 159 */       Element child = element.appendElement("input").attr("type", "checkbox");
/* 160 */       child.attr("v-model", "checkboxScope.tempData.currentValue");
/* 161 */       child.attr("value", option.getString("key"));
/* 162 */       child.attr("name", String.format("%s-%s-%s", new Object[] { ThreadMapUtil.get("boCode"), column.getTable().getKey(), column.getName() }));
/* 163 */       handlePermission(child, column);
/* 164 */       element.appendText(option.getString("txt"));
/*     */     } 
/*     */     
/* 167 */     return checkbox.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnRadio(IBusinessColumn column) {
/* 172 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 173 */     if (!config.containsKey("options")) {
/* 174 */       throw new BusinessException(String.format("表【%s】Radio 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 177 */     Element permissionElement = getElement("div");
/* 178 */     JSONArray options = config.getJSONArray("options");
/* 179 */     for (int i = 0; i < options.size(); i++) {
/* 180 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 182 */       Element label = permissionElement.appendElement("label").addClass("radio-inline");
/* 183 */       Element child = label.appendElement("input").attr("type", "radio");
/*     */       
/* 185 */       handleVModel(child, column);
/* 186 */       child.attr("v-ab-permission", getPermissionPath(column));
/* 187 */       handleValidateRules(child, column);
/*     */       
/* 189 */       child.attr("value", option.getString("key"));
/* 190 */       label.appendText(option.getString("txt"));
/*     */     } 
/*     */     
/* 193 */     return permissionElement.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnSelect(IBusinessColumn column, Boolean isMultiple) {
/* 198 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 199 */     if (!config.containsKey("options")) {
/* 200 */       throw new BusinessException(String.format("表【%s】Select 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 203 */     Element permissionElement = getElement("select").addClass("form-control");
/* 204 */     handleVModel(permissionElement, column);
/* 205 */     permissionElement.attr("v-ab-permission", getPermissionPath(column));
/* 206 */     handleValidateRules(permissionElement, column);
/*     */     
/* 208 */     if (isMultiple.booleanValue()) {
/* 209 */       permissionElement.attr("multiple", "true");
/*     */     }
/*     */     
/* 212 */     Element select = permissionElement.appendElement("option");
/* 213 */     select.attr("value", "");
/* 214 */     select.text("请选择");
/*     */     
/* 216 */     JSONArray options = config.getJSONArray("options");
/* 217 */     for (int i = 0; i < options.size(); i++) {
/* 218 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 220 */       Element element = permissionElement.appendElement("option");
/* 221 */       element.attr("value", option.getString("key"));
/* 222 */       element.text(option.getString("txt"));
/*     */     } 
/*     */     
/* 225 */     return permissionElement.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnFile(IBusinessColumn column) {
/* 230 */     Element element = getElement("ab-upload").attr("href", "javascript:void(0)").addClass("btn btn-primary fa-upload");
/* 231 */     handleVModel(element, column);
/* 232 */     element.attr(":ab-permission", getPermissionPath(column));
/*     */     
/* 234 */     IBusTableRel relation = (IBusTableRel)ThreadMapUtil.get("relation");
/*     */     
/* 236 */     element.attr("desc", relation.getTableComment() + "-" + column.getComment());
/* 237 */     return element.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubAttrs(IBusTableRel rel) {
/* 243 */     StringBuilder sb = new StringBuilder();
/* 244 */     sb.append(" id=\"" + rel.getBusObj().getKey() + "-" + rel.getTableKey() + "\" ");
/*     */ 
/*     */     
/* 247 */     if (rel.getType().equals(BusTableRelType.ONE_TO_MANY.getKey()) && !rel.getParent().getType().equals(BusTableRelType.MAIN.getKey())) {
/* 248 */       sb.append(" hide ");
/*     */     }
/*     */     
/* 251 */     sb.append(" table-key=\"" + rel.getTableKey() + "\" ");
/* 252 */     return sb.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handlePermission(Element element, IBusinessColumn column) {
/* 257 */     element.attr("v-ab-permission", getPermissionPath(column));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleValidateRules(Element element, IBusinessColumn column) {
/* 262 */     super.handleValidateRules(element, column);
/*     */     
/* 264 */     element.attr("v-ab-validate", element.attr("ab-validate"));
/* 265 */     element.removeAttr("ab-validate");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getGeneratorName() {
/* 270 */     return "vueGenerator";
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScopePath(IBusTableRel relation) {
/* 275 */     if (relation.getType().equals(BusTableRelType.MAIN.getKey())) {
/* 276 */       return "data." + relation.getBusObj().getKey();
/*     */     }
/*     */     
/* 279 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 281 */     sb.append(relation.getTableKey());
/*     */     
/* 283 */     if (relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
/* 284 */       sb.append("List");
/*     */       
/* 286 */       if (isThreeChildren(relation)) {
/* 287 */         sb.insert(0, "subTempData." + relation.getParent().getTableKey() + ".");
/* 288 */         return sb.toString();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 293 */     getParentPath(relation.getParent(), sb);
/*     */     
/* 295 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPermissionPath(IBusinessColumn column) {
/* 305 */     String boCode = (String)ThreadMapUtil.get("boCode");
/* 306 */     IBusTableRel relation = (IBusTableRel)ThreadMapUtil.get("relation");
/* 307 */     boolean isLine = ((Boolean)ThreadMapUtil.getOrDefault("isLine", Boolean.valueOf(false))).booleanValue();
/* 308 */     if (!isLine && relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
/* 309 */       return "scope.permission." + boCode + "." + column.getTable().getKey() + "." + column.getKey();
/*     */     }
/* 311 */     return "permission." + boCode + "." + column.getTable().getKey() + "." + column.getKey();
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
/*     */   public String getLineColumn(IBusinessColumn column, IBusTableRel relation) {
/* 323 */     ThreadMapUtil.put("isLine", Boolean.valueOf(true));
/* 324 */     String str = getColumn(column, relation);
/* 325 */     ThreadMapUtil.remove("isLine");
/* 326 */     return str;
/*     */   }
/*     */   
/*     */   public String getLineColumn(FormGroup group, FormColumn formColumn) {
/* 330 */     IBusTableRel tableRel = group.getTableRelation();
/*     */ 
/*     */     
/* 333 */     if (StringUtil.isNotEmpty(formColumn.getTableKey()) && !formColumn.getTableKey().equals(tableRel.getTableKey())) {
/* 334 */       IBusTableRel table = tableRel.find(formColumn.getTableKey());
/* 335 */       if (table != null) {
/* 336 */         tableRel = table;
/*     */       }
/*     */     } 
/*     */     
/* 340 */     if (!formColumn.getTableKey().equals(tableRel.getTableKey())) {
/* 341 */       this.LOG.error("生成表单可能存在异常！formColumnTableKey{},tableRelTableKey{}", formColumn.getTableKey(), tableRel.getTableKey());
/*     */     }
/*     */     
/* 344 */     IBusinessColumn businessColumn = tableRel.getTable().getColumnByKey(formColumn.getKey());
/*     */     
/* 346 */     if (businessColumn == null) {
/* 347 */       this.LOG.error("布局模板查找Column配置失败！字段：{}，表：{}", formColumn.getComment(), formColumn.getTableKey());
/*     */     }
/*     */     
/* 350 */     return getLineColumn(businessColumn, tableRel);
/*     */   }
/*     */   
/*     */   public String getScopePath_old(IBusTableRel relation) {
/* 354 */     return super.getScopePath(relation);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/generator/VueFormElementGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */