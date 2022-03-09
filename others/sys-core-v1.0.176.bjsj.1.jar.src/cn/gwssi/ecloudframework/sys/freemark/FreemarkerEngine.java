/*    */ package cn.gwssi.ecloudframework.sys.freemark;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessError;
/*    */ import cn.gwssi.ecloudframework.base.core.util.PropertyUtil;
/*    */ import cn.gwssi.ecloudframework.sys.api.freemark.IFreemarkerEngine;
/*    */ import freemarker.cache.StringTemplateLoader;
/*    */ import freemarker.cache.TemplateLoader;
/*    */ import freemarker.template.Configuration;
/*    */ import freemarker.template.Template;
/*    */ import java.io.File;
/*    */ import java.io.StringWriter;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ @Component
/*    */ public class FreemarkerEngine
/*    */   implements IFreemarkerEngine
/*    */ {
/*    */   private Configuration formTemplateConfig;
/* 22 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*    */   
/*    */   public Configuration getFormTemplateConfiguration() {
/*    */     try {
/* 26 */       if (this.formTemplateConfig == null) {
/* 27 */         String templatePath = PropertyUtil.getProperty("formTemplateUrl", "src/main/resources/templates");
/* 28 */         this.formTemplateConfig = new Configuration();
/* 29 */         this.formTemplateConfig.setDefaultEncoding("UTF-8");
/* 30 */         this.formTemplateConfig.setDirectoryForTemplateLoading(new File(templatePath));
/*    */       } 
/* 32 */       return this.formTemplateConfig;
/* 33 */     } catch (Exception e) {
/* 34 */       throw new BusinessError(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String genFormByTemplateName(String templateName, Object model) {
/*    */     try {
/* 42 */       Template template = getFormTemplateConfiguration().getTemplate(templateName);
/*    */       
/* 44 */       StringWriter writer = new StringWriter();
/* 45 */       template.process(model, writer);
/* 46 */       return writer.toString();
/* 47 */     } catch (Exception e) {
/* 48 */       throw new BusinessError(e);
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String parseByString(String templateSource, Object model) {
/*    */     try {
/* 56 */       Configuration cfg = new Configuration();
/* 57 */       StringTemplateLoader loader = new StringTemplateLoader();
/* 58 */       cfg.setTemplateLoader((TemplateLoader)loader);
/* 59 */       cfg.setClassicCompatible(true);
/* 60 */       loader.putTemplate("freemaker", templateSource);
/* 61 */       Template template = cfg.getTemplate("freemaker");
/* 62 */       StringWriter writer = new StringWriter();
/* 63 */       template.process(model, writer);
/* 64 */       return writer.toString();
/* 65 */     } catch (Exception e) {
/* 66 */       this.LOG.error(String.format("freemaker模板【%s】解析失败：%s", new Object[] { templateSource, e.getMessage() }));
/* 67 */       throw new BusinessError(String.format("freemaker模板【%s】解析失败", new Object[] { templateSource }), e);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/freemark/FreemarkerEngine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */