/*     */ package cn.gwssi.ecloudbpm.form.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustDialog;
/*     */ import cn.gwssi.ecloudbpm.form.api.model.IFormCustDialogTreeConfig;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogConditionField;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogDisplayField;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogReturnField;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogSortField;
/*     */ import cn.gwssi.ecloudbpm.form.model.custdialog.FormCustDialogTreeConfig;
/*     */ import cn.gwssi.ecloudframework.base.core.model.BaseModel;
/*     */ import cn.gwssi.ecloudframework.base.core.util.JsonUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.TypeReference;
/*     */ import com.alibaba.fastjson.annotation.JSONField;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
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
/*     */ public class FormCustDialog
/*     */   extends BaseModel
/*     */   implements IFormCustDialog
/*     */ {
/*     */   public static final String DATA_SOURCE_INTERFACE = "interface";
/*     */   @NotEmpty
/*     */   private String key;
/*     */   @NotEmpty
/*     */   private String name;
/*     */   private String desc;
/*     */   @NotEmpty
/*     */   private String style;
/*     */   @NotEmpty
/*     */   private String dsKey;
/*     */   @NotEmpty
/*     */   private String dsName;
/*     */   @NotEmpty
/*     */   private String objType;
/*     */   @NotEmpty
/*     */   private String objName;
/*     */   private boolean page;
/*     */   private int pageSize;
/*     */   private int width;
/*     */   private int height;
/*     */   private boolean system;
/*     */   private boolean multiple;
/*     */   @JSONField(serialize = false)
/*     */   private String treeConfigJson;
/*     */   @JSONField(serialize = false)
/*     */   private String displayFieldsJson;
/*     */   @JSONField(serialize = false)
/*     */   private String conditionFieldsJson;
/*     */   @JSONField(serialize = false)
/*     */   private String returnFieldsJson;
/*     */   @JSONField(serialize = false)
/*     */   private String sortFieldsJson;
/*     */   private String dataSource;
/*     */   @JSONField(serialize = false)
/*     */   private String primaryTableConfigJson;
/*     */   @JSONField(serialize = false)
/*     */   private String otherConfigJson;
/*     */   @Valid
/*     */   private FormCustDialogTreeConfig treeConfig;
/*     */   @Valid
/*     */   private List<FormCustDialogDisplayField> displayFields;
/*     */   @Valid
/*     */   private List<FormCustDialogConditionField> conditionFields;
/*     */   @Valid
/*     */   private List<FormCustDialogReturnField> returnFields;
/*     */   @Valid
/*     */   private List<FormCustDialogSortField> sortFields;
/*     */   private FormCustDialog primaryTableConfig;
/*     */   private Map<String, Object> otherConfig;
/*     */   private String dataRelation;
/*     */   private String dialogLayout;
/*     */   private Integer primaryTablePercent;
/*     */   private String typeId;
/*     */   protected String typeName;
/*     */   
/*     */   public String getKey() {
/* 191 */     return this.key;
/*     */   }
/*     */   
/*     */   public void setKey(String key) {
/* 195 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 200 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/* 204 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDesc() {
/* 209 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String desc) {
/* 213 */     this.desc = desc;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStyle() {
/* 218 */     return this.style;
/*     */   }
/*     */   
/*     */   public void setStyle(String style) {
/* 222 */     this.style = style;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDsKey() {
/* 227 */     return this.dsKey;
/*     */   }
/*     */   
/*     */   public void setDsKey(String dsKey) {
/* 231 */     this.dsKey = dsKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDsName() {
/* 236 */     return this.dsName;
/*     */   }
/*     */   
/*     */   public void setDsName(String dsName) {
/* 240 */     this.dsName = dsName;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getObjType() {
/* 245 */     return this.objType;
/*     */   }
/*     */   
/*     */   public void setObjType(String objType) {
/* 249 */     this.objType = objType;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getObjName() {
/* 254 */     return this.objName;
/*     */   }
/*     */   
/*     */   public void setObjName(String objName) {
/* 258 */     this.objName = objName;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPage() {
/* 263 */     return this.page;
/*     */   }
/*     */   
/*     */   public void setPage(boolean page) {
/* 267 */     this.page = page;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getPageSize() {
/* 272 */     return this.pageSize;
/*     */   }
/*     */   
/*     */   public void setPageSize(int pageSize) {
/* 276 */     this.pageSize = pageSize;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getWidth() {
/* 281 */     return this.width;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDataSource() {
/* 286 */     return this.dataSource;
/*     */   }
/*     */   
/*     */   public void setDataSource(String dataSource) {
/* 290 */     this.dataSource = dataSource;
/*     */   }
/*     */   
/*     */   public void setWidth(int width) {
/* 294 */     this.width = width;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getHeight() {
/* 299 */     return this.height;
/*     */   }
/*     */   
/*     */   public void setHeight(int height) {
/* 303 */     this.height = height;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSystem() {
/* 308 */     return this.system;
/*     */   }
/*     */   
/*     */   public void setSystem(boolean system) {
/* 312 */     this.system = system;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isMultiple() {
/* 317 */     return this.multiple;
/*     */   }
/*     */   
/*     */   public void setMultiple(boolean multiple) {
/* 321 */     this.multiple = multiple;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTreeConfigJson() {
/* 326 */     return this.treeConfigJson;
/*     */   }
/*     */   
/*     */   public void setTreeConfigJson(String treeConfigJson) {
/* 330 */     this.treeConfig = (FormCustDialogTreeConfig)JsonUtil.parseObject(treeConfigJson, FormCustDialogTreeConfig.class);
/* 331 */     this.treeConfigJson = treeConfigJson;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDisplayFieldsJson() {
/* 336 */     return this.displayFieldsJson;
/*     */   }
/*     */   
/*     */   public void setDisplayFieldsJson(String displayFieldsJson) {
/* 340 */     this.displayFields = JsonUtil.parseArray(displayFieldsJson, FormCustDialogDisplayField.class);
/* 341 */     this.displayFieldsJson = displayFieldsJson;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getConditionFieldsJson() {
/* 346 */     return this.conditionFieldsJson;
/*     */   }
/*     */   
/*     */   public void setConditionFieldsJson(String conditionFieldsJson) {
/* 350 */     this.conditionFields = JsonUtil.parseArray(conditionFieldsJson, FormCustDialogConditionField.class);
/* 351 */     this.conditionFieldsJson = conditionFieldsJson;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getReturnFieldsJson() {
/* 356 */     return this.returnFieldsJson;
/*     */   }
/*     */   
/*     */   public void setReturnFieldsJson(String returnFieldsJson) {
/* 360 */     this.returnFields = JsonUtil.parseArray(returnFieldsJson, FormCustDialogReturnField.class);
/* 361 */     this.returnFieldsJson = returnFieldsJson;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSortFieldsJson() {
/* 366 */     return this.sortFieldsJson;
/*     */   }
/*     */   
/*     */   public void setSortFieldsJson(String sortFieldsJson) {
/* 370 */     this.sortFields = JsonUtil.parseArray(sortFieldsJson, FormCustDialogSortField.class);
/* 371 */     this.sortFieldsJson = sortFieldsJson;
/*     */   }
/*     */ 
/*     */   
/*     */   public FormCustDialogTreeConfig getTreeConfig() {
/* 376 */     return this.treeConfig;
/*     */   }
/*     */   
/*     */   public void setTreeConfig(FormCustDialogTreeConfig treeConfig) {
/* 380 */     this.treeConfigJson = JsonUtil.toJSONString(treeConfig);
/* 381 */     this.treeConfig = treeConfig;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FormCustDialogDisplayField> getDisplayFields() {
/* 386 */     return this.displayFields;
/*     */   }
/*     */   
/*     */   public void setDisplayFields(List<FormCustDialogDisplayField> displayFields) {
/* 390 */     this.displayFieldsJson = JsonUtil.toJSONString(displayFields);
/* 391 */     this.displayFields = displayFields;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FormCustDialogConditionField> getConditionFields() {
/* 396 */     return this.conditionFields;
/*     */   }
/*     */   
/*     */   public void setConditionFields(List<FormCustDialogConditionField> conditionFields) {
/* 400 */     this.conditionFieldsJson = JsonUtil.toJSONString(conditionFields);
/* 401 */     this.conditionFields = conditionFields;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FormCustDialogReturnField> getReturnFields() {
/* 406 */     return this.returnFields;
/*     */   }
/*     */   
/*     */   public void setReturnFields(List<FormCustDialogReturnField> returnFields) {
/* 410 */     this.returnFieldsJson = JsonUtil.toJSONString(returnFields);
/* 411 */     this.returnFields = returnFields;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FormCustDialogSortField> getSortFields() {
/* 416 */     return this.sortFields;
/*     */   }
/*     */   
/*     */   public void setSortFields(List<FormCustDialogSortField> sortFields) {
/* 420 */     this.sortFieldsJson = JsonUtil.toJSONString(sortFields);
/* 421 */     this.sortFields = sortFields;
/*     */   }
/*     */ 
/*     */   
/*     */   public FormCustDialog getPrimaryTableConfig() {
/* 426 */     return this.primaryTableConfig;
/*     */   }
/*     */   
/*     */   public void setPrimaryTableConfig(FormCustDialog primaryTableConfig) {
/* 430 */     this.primaryTableConfigJson = JsonUtil.toJSONString(primaryTableConfig);
/* 431 */     this.primaryTableConfig = primaryTableConfig;
/*     */   }
/*     */   
/*     */   public String getPrimaryTableConfigJson() {
/* 435 */     return this.primaryTableConfigJson;
/*     */   }
/*     */   
/*     */   public void setPrimaryTableConfigJson(String primaryTableConfigJson) {
/* 439 */     this.primaryTableConfig = (FormCustDialog)JsonUtil.parseObject(primaryTableConfigJson, FormCustDialog.class);
/* 440 */     this.primaryTableConfigJson = primaryTableConfigJson;
/*     */   }
/*     */   
/*     */   public String getDataRelation() {
/* 444 */     return this.dataRelation;
/*     */   }
/*     */   
/*     */   public void setDataRelation(String dataRelation) {
/* 448 */     this.dataRelation = dataRelation;
/*     */   }
/*     */   
/*     */   public String getDialogLayout() {
/* 452 */     return this.dialogLayout;
/*     */   }
/*     */   
/*     */   public void setDialogLayout(String dialogLayout) {
/* 456 */     this.dialogLayout = dialogLayout;
/*     */   }
/*     */   
/*     */   public Integer getPrimaryTablePercent() {
/* 460 */     return this.primaryTablePercent;
/*     */   }
/*     */   
/*     */   public void setPrimaryTablePercent(Integer primaryTablePercent) {
/* 464 */     this.primaryTablePercent = primaryTablePercent;
/*     */   }
/*     */   
/*     */   public String getOtherConfigJson() {
/* 468 */     return this.otherConfigJson;
/*     */   }
/*     */   
/*     */   public void setOtherConfigJson(String otherConfigJson) {
/* 472 */     if (!StringUtil.isEmpty(otherConfigJson)) {
/* 473 */       this.otherConfig = (Map<String, Object>)JSON.parseObject(otherConfigJson, new TypeReference<HashMap<String, Object>>() {  }, new com.alibaba.fastjson.parser.Feature[0]);
/*     */     }
/*     */     
/* 476 */     this.otherConfigJson = otherConfigJson;
/*     */   }
/*     */   
/*     */   public Map<String, Object> getOtherConfig() {
/* 480 */     return this.otherConfig;
/*     */   }
/*     */   
/*     */   public void setOtherConfig(Map<String, Object> otherConfig) {
/* 484 */     this.otherConfigJson = JsonUtil.toJSONString(otherConfig);
/* 485 */     this.otherConfig = otherConfig;
/*     */   }
/*     */   
/*     */   public String getTypeId() {
/* 489 */     return this.typeId;
/*     */   }
/*     */   
/*     */   public void setTypeId(String typeId) {
/* 493 */     this.typeId = typeId;
/*     */   }
/*     */   
/*     */   public String getTypeName() {
/* 497 */     return this.typeName;
/*     */   }
/*     */   
/*     */   public void setTypeName(String typeName) {
/* 501 */     this.typeName = typeName;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/model/FormCustDialog.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */