/*     */ package cn.gwssi.ecloudbpm.form.generator;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusColumnCtrlType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessColumn;
/*     */ import cn.gwssi.ecloudbpm.form.api.constant.FormStatusCode;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormColumn;
/*     */ import cn.gwssi.ecloudbpm.form.model.design.FormGroup;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import org.jsoup.Jsoup;
/*     */ import org.jsoup.nodes.Document;
/*     */ import org.jsoup.nodes.Element;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbsFormElementGenerator
/*     */ {
/*  27 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public abstract String getGeneratorName();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getColumn(IBusinessColumn column, IBusTableRel relation) {
/*  38 */     if (column == null) {
/*  39 */       throw new BusinessException(String.format("%s 生成表单异常，column 为 null", new Object[] { (relation != null) ? relation.getTableComment() : "" }));
/*     */     }
/*  41 */     String boCode = relation.getBusObj().getKey();
/*  42 */     ThreadMapUtil.put("boCode", boCode);
/*  43 */     ThreadMapUtil.put("relation", relation);
/*     */     
/*  45 */     if (column.getCtrl() == null) {
/*  46 */       this.LOG.debug(" column [{}]ctrl 配置为空，默认生成 input框，表：{}", column.getComment(), column.getTable().getComment());
/*  47 */       return getColumnOnetext(column);
/*     */     } 
/*     */     
/*  50 */     BusColumnCtrlType columnType = BusColumnCtrlType.getByKey(column.getCtrl().getType());
/*     */ 
/*     */     
/*     */     try {
/*  54 */       switch (columnType) {
/*     */         case ONETEXT:
/*  56 */           return getColumnOnetext(column);
/*     */         case DATE:
/*  58 */           return getColumnDate(column);
/*     */         case DIC:
/*  60 */           return getColumnDic(column);
/*     */         case SERIALNO:
/*  62 */           return getColumnIdentity(column);
/*     */         case MULTITEXT:
/*  64 */           return getColumnMultitext(column);
/*     */         case CHECKBOX:
/*  66 */           return getColumnCheckBox(column);
/*     */ 
/*     */         
/*     */         case RADIO:
/*  70 */           return getColumnRadio(column);
/*     */         case SELECT:
/*  72 */           return getColumnSelect(column, Boolean.valueOf(false));
/*     */         case FILE:
/*  74 */           return getColumnFile(column);
/*     */       } 
/*  76 */       return "";
/*     */ 
/*     */     
/*     */     }
/*  80 */     catch (Exception e) {
/*  81 */       throw new BusinessException(String.format("表单字段 [%s-%s]解析失败 ,控件类型[%s] :%s", new Object[] { column.getTable().getName(), column.getComment(), columnType.getDesc(), e.getMessage() }), FormStatusCode.FORM_ELEMENT_GENERATOR_ERROR, e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getColumn(FormGroup group, FormColumn formColumn) {
/*  87 */     IBusTableRel tableRel = group.getTableRelation();
/*     */ 
/*     */     
/*  90 */     if (StringUtil.isNotEmpty(formColumn.getTableKey()) && !formColumn.getTableKey().equals(tableRel.getTableKey())) {
/*  91 */       IBusTableRel table = tableRel.find(formColumn.getTableKey());
/*  92 */       if (table != null) {
/*  93 */         tableRel = table;
/*     */       }
/*     */     } 
/*     */     
/*  97 */     if (!formColumn.getTableKey().equals(tableRel.getTableKey())) {
/*  98 */       this.LOG.error("生成表单可能存在异常！formColumnTableKey{},tableRelTableKey{}", formColumn.getTableKey(), tableRel.getTableKey());
/*     */     }
/*     */     
/* 101 */     IBusinessColumn businessColumn = tableRel.getTable().getColumnByKey(formColumn.getKey());
/*     */     
/* 103 */     if (businessColumn == null) {
/* 104 */       this.LOG.error("布局模板查找Column配置失败！字段：{}，表：{}", formColumn.getComment(), formColumn.getTableKey());
/*     */     }
/*     */     
/* 107 */     return getColumn(businessColumn, tableRel);
/*     */   }
/*     */ 
/*     */   
/*     */   protected abstract String getColumnOnetext(IBusinessColumn paramIBusinessColumn);
/*     */ 
/*     */   
/*     */   protected abstract String getColumnDate(IBusinessColumn paramIBusinessColumn);
/*     */ 
/*     */   
/*     */   protected abstract String getColumnDic(IBusinessColumn paramIBusinessColumn);
/*     */ 
/*     */   
/*     */   protected abstract String getColumnIdentity(IBusinessColumn paramIBusinessColumn);
/*     */ 
/*     */   
/*     */   protected abstract String getColumnMultitext(IBusinessColumn paramIBusinessColumn);
/*     */   
/*     */   protected abstract String getColumnCheckBox(IBusinessColumn paramIBusinessColumn);
/*     */   
/*     */   protected abstract String getColumnRadio(IBusinessColumn paramIBusinessColumn);
/*     */   
/*     */   protected abstract String getColumnSelect(IBusinessColumn paramIBusinessColumn, Boolean paramBoolean);
/*     */   
/*     */   protected abstract String getColumnFile(IBusinessColumn paramIBusinessColumn);
/*     */   
/*     */   protected Element getElement(String type) {
/* 134 */     Document doc = Jsoup.parse("");
/* 135 */     Element element = doc.createElement(type);
/* 136 */     return element;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handlePermission(Element element, IBusinessColumn column) {
/* 144 */     element.attr("ab-basic-permission", getPermissionPath(column));
/* 145 */     element.attr("desc", column.getComment());
/*     */   }
/*     */   
/*     */   public String getPermissionPath(IBusinessColumn column, IBusTableRel relation) {
/* 149 */     String boCode = relation.getBusObj().getKey();
/* 150 */     return "permission." + boCode + "." + column.getTable().getKey() + "." + column.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String getPermissionPath(IBusinessColumn column) {
/* 159 */     String boCode = (String)ThreadMapUtil.get("boCode");
/* 160 */     return "permission." + boCode + "." + column.getTable().getKey() + "." + column.getKey();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected void handleValidateRules(Element element, IBusinessColumn column) {
/* 169 */     if (column.getCtrl() == null) {
/* 170 */       element.attr("ab-validate", "{}");
/*     */       
/*     */       return;
/*     */     } 
/* 174 */     String rulesStr = column.getCtrl().getValidRule();
/* 175 */     JSONObject validateRule = new JSONObject();
/*     */     
/* 177 */     if (StringUtil.isNotEmpty(rulesStr)) {
/* 178 */       JSONArray rules = JSONArray.parseArray(rulesStr);
/*     */ 
/*     */       
/* 181 */       for (int i = 0; i < rules.size(); i++) {
/* 182 */         JSONObject rule = rules.getJSONObject(i);
/*     */         
/* 184 */         validateRule.put(rule.getString("name"), Boolean.valueOf(true));
/*     */       } 
/*     */       
/* 187 */       if (column.isRequired()) {
/* 188 */         validateRule.put("required", Boolean.valueOf(true));
/*     */       }
/* 190 */       if (column.getLength() > 1) {
/* 191 */         validateRule.put("maxlength", Integer.valueOf(column.getLength()));
/*     */       }
/*     */     } 
/*     */     
/* 195 */     element.attr("ab-validate", validateRule.toJSONString());
/*     */     
/* 197 */     IBusTableRel relation = (IBusTableRel)ThreadMapUtil.get("relation");
/*     */     
/* 199 */     element.attr("desc", relation.getTableComment() + "-" + column.getComment());
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
/*     */ 
/*     */   
/*     */   public String getScopePath(IBusTableRel relation) {
/* 217 */     if (relation.getType().equals(BusTableRelType.MAIN.getKey())) {
/* 218 */       return "data." + relation.getBusObj().getKey();
/*     */     }
/*     */     
/* 221 */     StringBuilder sb = new StringBuilder();
/*     */     
/* 223 */     sb.append(relation.getTableKey());
/*     */     
/* 225 */     if (relation.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
/* 226 */       sb.append("List");
/*     */       
/* 228 */       if (isThreeChildren(relation)) {
/* 229 */         sb.insert(0, "subTempData.");
/* 230 */         return sb.toString();
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 235 */     getParentPath(relation.getParent(), sb);
/*     */     
/* 237 */     return sb.toString();
/*     */   }
/*     */   
/*     */   protected void getParentPath(IBusTableRel parent, StringBuilder sb) {
/* 241 */     if (parent == null)
/*     */       return; 
/* 243 */     if (parent.getType().equals(BusTableRelType.ONE_TO_MANY.getKey())) {
/* 244 */       sb.insert(0, parent.getTableKey() + ".");
/*     */       
/*     */       return;
/*     */     } 
/* 248 */     if (parent.getType().equals(BusTableRelType.ONE_TO_ONE.getKey())) {
/* 249 */       sb.insert(0, parent.getTableKey() + ".");
/*     */     }
/*     */     
/* 252 */     if (parent.getType().equals(BusTableRelType.MAIN.getKey())) {
/* 253 */       sb.insert(0, "data." + parent.getBusObj().getKey() + ".");
/*     */     }
/*     */     
/* 256 */     getParentPath(parent.getParent(), sb);
/*     */   }
/*     */ 
/*     */   
/*     */   public abstract String getSubAttrs(IBusTableRel paramIBusTableRel);
/*     */ 
/*     */   
/*     */   public boolean isThreeChildren(IBusTableRel rel) {
/* 264 */     if (rel.getType().equals(BusTableRelType.ONE_TO_MANY.getKey()) && 
/* 265 */       !rel.getParent().getType().equals(BusTableRelType.MAIN.getKey())) {
/* 266 */       return true;
/*     */     }
/* 268 */     return false;
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
/*     */   public String getDivVIf(IBusTableRel relation) {
/* 280 */     if (isThreeChildren(relation)) {
/* 281 */       return "v-if=\"subTempData." + relation.getParent().getTableKey() + "\"";
/*     */     }
/* 283 */     return "";
/*     */   }
/*     */   
/*     */   protected void handleElementPlaceHolder(IBusinessColumn column, Element element) {
/* 287 */     if (column.getCtrl() == null) {
/*     */       return;
/*     */     }
/* 290 */     String configStr = column.getCtrl().getConfig();
/* 291 */     if (StringUtil.isEmpty(configStr)) {
/*     */       return;
/*     */     }
/*     */     
/* 295 */     JSONObject config = JSON.parseObject(configStr);
/* 296 */     Boolean placeholder = config.getBoolean("placeholder");
/*     */     
/* 298 */     if (placeholder == null || !placeholder.booleanValue())
/* 299 */       return;  element.attr("placeholder", config.getString("placeholderText"));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/generator/AbsFormElementGenerator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */