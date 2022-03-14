/*     */ package cn.gwssi.ecloudbpm.form.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessObjectService;
/*     */ import cn.gwssi.ecloudbpm.form.api.constant.FormTemplateType;
/*     */ import cn.gwssi.ecloudbpm.form.api.model.FormType;
/*     */ import cn.gwssi.ecloudbpm.form.dao.FormTemplateDao;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormTemplateManager;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormTemplate;
/*     */ import com.dstz.base.api.exception.BusinessError;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import cn.hutool.core.io.FileUtil;
/*     */ import cn.hutool.core.util.ClassUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.io.File;
/*     */ import java.io.InputStream;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.io.IOUtils;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.DocumentException;
/*     */ import org.dom4j.DocumentHelper;
/*     */ import org.dom4j.Element;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
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
/*     */ @Service
/*     */ public class FormTemplateManagerImpl
/*     */   extends BaseManager<String, FormTemplate>
/*     */   implements FormTemplateManager
/*     */ {
/*     */   @Resource
/*     */   FormTemplateDao formTemplateDao;
/*     */   @Autowired
/*     */   IBusinessObjectService businessObjectService;
/*     */   
/*     */   public static String getFormTemplatePath() {
/*     */     try {
/*  65 */       return ClassUtil.getClassPath() + File.separator + "template" + File.separator + "form" + File.separator;
/*  66 */     } catch (Exception e) {
/*  67 */       throw new BusinessException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public FormTemplate getByKey(String key) {
/*  74 */     if (StringUtil.isEmpty(key)) return null;
/*     */     
/*  76 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  77 */     defaultQueryFilter.addFilter("key_", key, QueryOP.EQUAL);
/*  78 */     return (FormTemplate)queryOne((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initAllTemplate() {
/*  84 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/*  85 */     defaultQueryFilter.addFilter("editable_", Boolean.valueOf(false), QueryOP.EQUAL);
/*  86 */     defaultQueryFilter.setPage(null);
/*  87 */     for (FormTemplate template : query((QueryFilter)defaultQueryFilter)) {
/*  88 */       remove(template.getId());
/*     */     }
/*     */     
/*  91 */     initTemplate();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() {
/*  98 */     if (getAll().isEmpty()) {
/*  99 */       initTemplate();
/*     */     }
/*     */   }
/*     */   
/*     */   private void initTemplate() {
/*     */     try {
/* 105 */       String templatePath = "/template/formDef/";
/* 106 */       InputStream instream = getClass().getResourceAsStream(templatePath + "templates.xml");
/* 107 */       String xml = IOUtils.toString(instream, "UTF-8");
/* 108 */       Document document = DocumentHelper.parseText(xml);
/* 109 */       Element root = document.getRootElement();
/* 110 */       List<Element> list = root.elements();
/* 111 */       for (Element element : list) {
/* 112 */         String key = element.attributeValue("key");
/* 113 */         String name = element.attributeValue("name");
/* 114 */         String type = element.attributeValue("type");
/* 115 */         String desc = element.attributeValue("desc");
/* 116 */         String dir = element.attributeValue("dir");
/*     */         
/* 118 */         String fileName = templatePath + dir + "/" + key + ".ftl";
/* 119 */         System.out.println(fileName);
/* 120 */         String html = IOUtils.toString(getClass().getResourceAsStream(fileName), "UTF-8");
/*     */         
/* 122 */         FormTemplate formTemplate = new FormTemplate();
/* 123 */         formTemplate.setId(IdUtil.getSuid());
/* 124 */         formTemplate.setHtml(html);
/* 125 */         formTemplate.setName(name);
/* 126 */         formTemplate.setKey(key);
/* 127 */         formTemplate.setEditable(false);
/* 128 */         formTemplate.setType(type);
/* 129 */         formTemplate.setFormType(dir);
/* 130 */         formTemplate.setDesc(desc);
/* 131 */         this.formTemplateDao.create(formTemplate);
/*     */       } 
/* 133 */     } catch (Exception e) {
/* 134 */       e.printStackTrace();
/* 135 */       throw new BusinessException(e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isExist(String key) {
/* 142 */     return (getByKey(key) != null);
/*     */   }
/*     */ 
/*     */   
/*     */   public void backUpTemplate(String id) {
/* 147 */     FormTemplate formTemplate = (FormTemplate)this.formTemplateDao.get(id);
/* 148 */     String alias = formTemplate.getKey();
/* 149 */     String name = formTemplate.getName();
/* 150 */     String desc = formTemplate.getDesc();
/* 151 */     String html = formTemplate.getHtml();
/* 152 */     String type = formTemplate.getType();
/*     */     
/* 154 */     String templatePath = getFormTemplatePath();
/*     */     
/* 156 */     String xmlPath = templatePath + "templates.xml";
/* 157 */     String xml = FileUtil.readUtf8String(xmlPath);
/* 158 */     Document document = null;
/*     */     try {
/* 160 */       document = DocumentHelper.parseText(xml);
/* 161 */     } catch (DocumentException documentException) {
/* 162 */       throw new BusinessError("解析文件出错", documentException);
/*     */     } 
/* 164 */     Element root = document.getRootElement();
/*     */     
/* 166 */     Element e = root.addElement("template");
/* 167 */     e.addAttribute("alias", alias);
/* 168 */     e.addAttribute("name", name);
/* 169 */     e.addAttribute("type", type);
/* 170 */     e.addAttribute("templateDesc", desc);
/* 171 */     String content = document.asXML();
/*     */     
/* 173 */     FileUtil.writeUtf8String(content, xmlPath);
/* 174 */     FileUtil.writeUtf8String(html, templatePath + alias + ".ftl");
/*     */     
/* 176 */     formTemplate.setEditable(false);
/* 177 */     this.formTemplateDao.update(formTemplate);
/*     */   }
/*     */   
/*     */   public List<FormTemplate> getByType(String type, String formType, Boolean hasDesignForm) {
/* 181 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 182 */     defaultQueryFilter.setPage(null);
/* 183 */     defaultQueryFilter.addFilter("type_", type, QueryOP.EQUAL);
/* 184 */     defaultQueryFilter.addFilter("form_type_", formType, QueryOP.EQUAL);
/* 185 */     if (hasDesignForm.booleanValue()) {
/* 186 */       defaultQueryFilter.addFilter("type_", type.concat("FormOverallArrangement"), QueryOP.EQUAL);
/*     */     }
/* 188 */     return query((QueryFilter)defaultQueryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONArray templateData(String boKey, String type) {
/* 193 */     IBusinessObject bo = this.businessObjectService.getByKey(boKey);
/* 194 */     if (bo == null) {
/* 195 */       throw new BusinessException(String.format("业务对象丢失，请检查业务对象：%s", new Object[] { boKey }));
/*     */     }
/* 197 */     boolean hasDesignForm = false;
/* 198 */     if (StringUtil.isNotEmpty(this.businessObjectService.getBoOverallArrangement(boKey)) && !FormType.MOBILE.value().equals(type)) {
/* 199 */       hasDesignForm = true;
/*     */     }
/*     */     
/* 202 */     List<IBusTableRel> rels = bo.getRelation().list();
/* 203 */     List<FormTemplate> mainTemplates = getByType(FormTemplateType.MAIN.getKey(), type, Boolean.valueOf(hasDesignForm));
/* 204 */     List<FormTemplate> subTableTemplates = getByType(FormTemplateType.SUB_TABLE.getKey(), type, Boolean.valueOf(hasDesignForm));
/* 205 */     for (FormTemplate template : mainTemplates) {
/* 206 */       template.setHtml(null);
/*     */     }
/* 208 */     for (FormTemplate template : subTableTemplates) {
/* 209 */       template.setHtml(null);
/*     */     }
/*     */     
/* 212 */     JSONArray jsonArray = new JSONArray();
/* 213 */     for (IBusTableRel rel : rels) {
/* 214 */       JSONObject jsonObject = new JSONObject();
/* 215 */       jsonObject.put("tableKey", rel.getTableKey());
/* 216 */       jsonObject.put("tableComment", rel.getTableComment());
/* 217 */       jsonObject.put("typeDesc", BusTableRelType.getByKey(rel.getType()).getDesc());
/* 218 */       if (BusTableRelType.MAIN.equalsWithKey(rel.getType())) {
/* 219 */         jsonObject.put("templates", JSON.toJSON(mainTemplates));
/*     */       } else {
/* 221 */         jsonObject.put("templates", JSON.toJSON(subTableTemplates));
/*     */       } 
/* 223 */       jsonArray.add(jsonObject);
/*     */     } 
/*     */     
/* 226 */     return jsonArray;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/impl/FormTemplateManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */