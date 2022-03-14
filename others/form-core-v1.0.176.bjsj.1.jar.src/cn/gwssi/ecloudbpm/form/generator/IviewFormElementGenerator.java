/*     */ package cn.gwssi.ecloudbpm.form.generator;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormColumn;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormGroup;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ 
/*     */ @Component
/*     */ public class IviewFormElementGenerator
/*     */   extends AbsFormElementGenerator
/*     */ {
/*     */   public String getGeneratorName() {
/*  25 */     return "iviewGenerator";
/*     */   }
/*     */   
/*     */   private void handleVModel(Element element, IBusinessColumn column) {
/*  29 */     String boCode = (String)ThreadMapUtil.get("boCode");
/*  30 */     IBusTableRel relation = (IBusTableRel)ThreadMapUtil.get("relation");
/*     */     
/*  32 */     if (relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
/*  33 */       element.attr("v-model", column.getTable().getKey() + "." + column.getKey());
/*     */       
/*     */       return;
/*     */     } 
/*  37 */     element.attr("v-model", getScopePath(relation) + "." + column.getKey());
/*     */     
/*  39 */     handleElementPlaceHolder(column, element);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnOnetext(IBusinessColumn column) {
/*  45 */     Element element = getElement("i-input");
/*     */     
/*  47 */     handleVModel(element, column);
/*  48 */     handlePermission(element, column);
/*  49 */     handleValidateRules(element, column);
/*     */     
/*  51 */     return element.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnDate(IBusinessColumn column) {
/*  59 */     Element element = getElement("ab-date");
/*     */     
/*  61 */     handleVModel(element, column);
/*  62 */     element.attr("v-bind:ab-permission", getPermissionPath(column));
/*  63 */     element.attr("desc", column.getTable().getComment() + "-" + column.getComment());
/*     */ 
/*     */     
/*  66 */     String configStr = column.getCtrl().getConfig();
/*  67 */     if (StringUtil.isEmpty(configStr)) {
/*  68 */       throw new BusinessException(String.format("表【%s】日期字段  【%s】解析失败,配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/*  71 */     String format = JSON.parseObject(configStr).getString("format");
/*     */     
/*  73 */     element.attr("format", format);
/*     */     
/*  75 */     return element.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnDic(IBusinessColumn column) {
/*  80 */     Element element = getElement("ab-dict");
/*     */     
/*  82 */     handleVModel(element, column);
/*  83 */     element.attr("v-bind:ab-permission", getPermissionPath(column));
/*  84 */     element.attr("desc", column.getTable().getComment() + "-" + column.getComment());
/*     */ 
/*     */     
/*  87 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/*  88 */     if (!config.containsKey("key")) {
/*  89 */       throw new BusinessException(String.format("表【%s】数据字典  字段【%s】解析失败,alias 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/*  92 */     element.attr("dict-key", config.getString("key"));
/*     */     
/*  94 */     return element.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnIdentity(IBusinessColumn column) {
/* 104 */     Element element = getElement("ab-serial-no");
/* 105 */     handleVModel(element, column);
/* 106 */     element.attr("v-bind:ab-permission", getPermissionPath(column));
/* 107 */     handleValidateRules(element, column);
/*     */     
/* 109 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 110 */     if (!config.containsKey("alias")) {
/* 111 */       throw new BusinessException(String.format("表【%s】流水号  字段【%s】解析失败,alias 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/* 113 */     element.attr("serial-no", config.getString("alias"));
/*     */     
/* 115 */     return element.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getColumnMultitext(IBusinessColumn column) {
/* 121 */     Element element = getElement("i-input").attr("type", "textarea");
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
/*     */   protected String getColumnCheckBox(IBusinessColumn column) {
/* 140 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 141 */     if (!config.containsKey("options")) {
/* 142 */       throw new BusinessException(String.format("表【%s】CheckBox 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 145 */     Element checkbox = getElement("ab-checkbox-group");
/* 146 */     handleVModel(checkbox, column);
/* 147 */     handlePermission(checkbox, column);
/* 148 */     handleValidateRules(checkbox, column);
/*     */     
/* 150 */     JSONArray options = config.getJSONArray("options");
/* 151 */     for (int i = 0; i < options.size(); i++) {
/* 152 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 154 */       Element checkboxEl = checkbox.appendElement("checkbox");
/* 155 */       checkboxEl.attr("label", option.getString("key"));
/* 156 */       checkboxEl.appendText(option.getString("txt"));
/*     */     } 
/* 158 */     return checkbox.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnRadio(IBusinessColumn column) {
/* 163 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 164 */     if (!config.containsKey("options")) {
/* 165 */       throw new BusinessException(String.format("表【%s】Radio 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 168 */     Element radioGroup = getElement("radio-group");
/* 169 */     JSONArray options = config.getJSONArray("options");
/*     */     
/* 171 */     handleVModel(radioGroup, column);
/* 172 */     handlePermission(radioGroup, column);
/* 173 */     handleValidateRules(radioGroup, column);
/*     */     
/* 175 */     for (int i = 0; i < options.size(); i++) {
/* 176 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 178 */       Element label = radioGroup.appendElement("radio");
/* 179 */       label.attr("label", option.getString("key"));
/* 180 */       label.appendText(option.getString("txt"));
/*     */     } 
/*     */     
/* 183 */     return radioGroup.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnSelect(IBusinessColumn column, Boolean isMultiple) {
/* 188 */     JSONObject config = JSON.parseObject(column.getCtrl().getConfig());
/* 189 */     if (!config.containsKey("options")) {
/* 190 */       throw new BusinessException(String.format("表【%s】Select 字段  【%s】解析失败,options 配置信息不能为空", new Object[] { column.getTable().getKey(), column.getComment() }));
/*     */     }
/*     */     
/* 193 */     Element iSelectEl = getElement("i-select");
/* 194 */     handleVModel(iSelectEl, column);
/* 195 */     handlePermission(iSelectEl, column);
/* 196 */     handleValidateRules(iSelectEl, column);
/* 197 */     iSelectEl.attr(":disabled", "'r'==" + getPermissionPath(column));
/*     */     
/* 199 */     if (isMultiple.booleanValue()) {
/* 200 */       iSelectEl.attr("multiple", "true");
/*     */     }
/*     */     
/* 203 */     Element select = iSelectEl.appendElement("i-option");
/* 204 */     select.attr("value", "");
/* 205 */     select.text("请选择");
/*     */     
/* 207 */     JSONArray options = config.getJSONArray("options");
/* 208 */     for (int i = 0; i < options.size(); i++) {
/* 209 */       JSONObject option = options.getJSONObject(i);
/*     */       
/* 211 */       Element element = iSelectEl.appendElement("i-option");
/* 212 */       element.attr("value", option.getString("key"));
/* 213 */       element.text(option.getString("txt"));
/*     */     } 
/*     */     
/* 216 */     return iSelectEl.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getColumnFile(IBusinessColumn column) {
/* 221 */     Element element = getElement("ab-upload");
/* 222 */     handleVModel(element, column);
/* 223 */     element.attr("v-bind:ab-permission", getPermissionPath(column));
/* 224 */     element.attr("desc", column.getTable().getComment() + "-" + column.getComment());
/*     */     
/* 226 */     return element.toString();
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
/*     */   public String getSubAttrs(IBusTableRel rel) {
/* 239 */     if (rel.getType().equals(BusTableRelType.MAIN.getKey())) return ""; 
/* 240 */     StringBuilder sb = new StringBuilder();
/* 241 */     sb.append(" table-key=\"" + rel.getTableKey() + "\" ");
/*     */     
/* 243 */     if (rel.getType().equals(BusTableRelType.ONE_TO_MANY.getKey()) && !rel.getParent().getType().equals(BusTableRelType.MAIN.getKey())) {
/* 244 */       sb.append(" v-model=\"subTempData." + rel.getBusObj().getKey() + "_" + rel.getTableKey() + "_Model\" ");
/* 245 */       sb.append(" @on-ok=\"subTempData." + rel.getBusObj().getKey() + "_" + rel.getTableKey() + "_okFn()\" ");
/* 246 */       sb.append(" @on-cancel=\"subTempData." + rel.getBusObj().getKey() + "_" + rel.getTableKey() + "_Model=false\" ");
/*     */     } 
/* 248 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public String getGroupTableName(FormGroup group) {
/* 252 */     if (CollectionUtil.isEmpty(group.getColumnList())) return ""; 
/* 253 */     String tableKey = null;
/*     */     
/* 255 */     for (FormColumn column : group.getColumnList()) {
/* 256 */       if (tableKey != null && !tableKey.equals(column.getTableKey())) {
/* 257 */         return "";
/*     */       }
/* 259 */       tableKey = column.getTableKey();
/*     */     } 
/* 261 */     if (group.getTableRelation().getTableKey().equals(tableKey)) {
/* 262 */       return "";
/*     */     }
/*     */     
/* 265 */     return " table-key=\"" + tableKey + "\" ";
/*     */   }
/*     */ 
/*     */   
/*     */   public Boolean isMultilayer(IBusTableRel rel) {
/* 270 */     if (rel.getType().equals(BusTableRelType.ONE_TO_MANY.getKey()) && !rel.getParent().getType().equals(BusTableRelType.MAIN.getKey())) {
/* 271 */       return Boolean.valueOf(true);
/*     */     }
/* 273 */     return Boolean.valueOf(false);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handlePermission(Element element, IBusinessColumn column) {
/* 278 */     element.attr("v-ab-permission", getPermissionPath(column));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void handleValidateRules(Element element, IBusinessColumn column) {
/* 283 */     super.handleValidateRules(element, column);
/* 284 */     element.attr("v-ab-validate", element.attr("ab-validate"));
/* 285 */     element.attr("desc", column.getTable().getComment() + "-" + column.getComment());
/* 286 */     element.removeAttr("ab-validate");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getScopePath(IBusTableRel relation) {
/* 291 */     return ((VueFormElementGenerator)AppUtil.getBean(VueFormElementGenerator.class)).getScopePath(relation);
/*     */   }
/*     */   
/*     */   public String getScopePath_old(IBusTableRel relation) {
/* 295 */     return super.getScopePath(relation);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPermissionPath(IBusinessColumn column) {
/* 305 */     return ((VueFormElementGenerator)AppUtil.getBean(VueFormElementGenerator.class)).getPermissionPath(column);
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
/* 317 */     return ((VueFormElementGenerator)AppUtil.getBean(VueFormElementGenerator.class)).getLineColumn(column, relation);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/generator/IviewFormElementGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */