/*     */ package cn.gwssi.ecloudbpm.form.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.dao.FormCustomConfDao;
/*     */ import cn.gwssi.ecloudbpm.form.manager.FormCustomConfManager;
/*     */ import cn.gwssi.ecloudbpm.form.model.FormCustomConf;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.model.BaseModel;
/*     */ import com.dstz.base.core.util.UserUtil;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.service.UserService;
/*     */ import java.io.Serializable;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class FormCustomConfManagerImpl
/*     */   extends BaseManager<String, FormCustomConf>
/*     */   implements FormCustomConfManager
/*     */ {
/*  33 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   public static final String FORM_CUSTOM_CONF_CACHE_KEY = "ecloudframework:formCustomConf:";
/*     */   @Resource
/*     */   FormCustomConfDao formCustomConfDao;
/*     */   @Autowired
/*     */   ICache<FormCustomConf> icache;
/*     */   @Resource
/*     */   ICurrentContext iCurrentContext;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   public FormCustomConf getByFormKey(String formKey) {
/*  46 */     FormCustomConf formCustomConf = (FormCustomConf)this.icache.getByKey("ecloudframework:formCustomConf:".concat(formKey));
/*  47 */     if (null == formCustomConf) {
/*  48 */       formCustomConf = this.formCustomConfDao.getByFormKey(formKey);
/*  49 */       makeUserInfo(formCustomConf);
/*  50 */       if (null != formCustomConf) {
/*  51 */         formCustomConf.initFormCustomConfEntity();
/*  52 */         this.icache.add("ecloudframework:formCustomConf:".concat(formKey), formCustomConf);
/*     */       } 
/*     */     } 
/*  55 */     return formCustomConf;
/*     */   }
/*     */ 
/*     */   
/*     */   public String save(FormCustomConf formCustomConf) {
/*  60 */     String formCustomConfId = "";
/*     */     
/*  62 */     String formKey = formCustomConf.getFormKey();
/*  63 */     if (StringUtils.isEmpty(formKey)) {
/*  64 */       throw new BusinessException("表单key不能为空");
/*     */     }
/*  66 */     String mobileFormKey = formKey + "_mobile";
/*  67 */     saveEntity(formCustomConf);
/*  68 */     formCustomConfId = formCustomConf.getId();
/*  69 */     FormCustomConf mobileFormCustomConf = this.formCustomConfDao.getByFormKey(mobileFormKey);
/*  70 */     if (null != mobileFormCustomConf) {
/*  71 */       formCustomConf.setId(mobileFormCustomConf.getId());
/*     */     }
/*  73 */     formCustomConf.setFormKey(mobileFormKey);
/*  74 */     saveEntity(formCustomConf);
/*  75 */     this.icache.delByKey("ecloudframework:formCustomConf:".concat(formKey));
/*  76 */     this.icache.delByKey("ecloudframework:formCustomConf:".concat(mobileFormKey));
/*  77 */     return formCustomConfId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateByPrimaryKeySelective(FormCustomConf entity) {
/*  82 */     entity.setUpdateBy(this.iCurrentContext.getCurrentUserId());
/*  83 */     this.formCustomConfDao.updateByPrimaryKeySelective(entity);
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
/*     */   public void saveEntity(FormCustomConf formCustomConf) {
/*  95 */     FormCustomConf formCustomConfTemp = getByFormKey(formCustomConf.getFormKey());
/*  96 */     if (null != formCustomConfTemp) {
/*  97 */       formCustomConf.setId(formCustomConfTemp.getId());
/*  98 */       updateByPrimaryKeySelective(formCustomConf);
/*     */     } else {
/* 100 */       formCustomConf.setId(null);
/* 101 */       this.formCustomConfDao.create(formCustomConf);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<FormCustomConf> query(QueryFilter queryFilter) {
/* 107 */     List<FormCustomConf> lst = this.dao.query(queryFilter);
/* 108 */     Map<String, String> mapUser = this.userService.getUserMapByUserIds(UserUtil.getUserIdSet(lst));
/* 109 */     UserUtil.makeUserInfo(mapUser, lst);
/* 110 */     return lst;
/*     */   }
/*     */ 
/*     */   
/*     */   public FormCustomConf get(String entityId) {
/* 115 */     FormCustomConf temp = (FormCustomConf)this.dao.get(entityId);
/* 116 */     makeUserInfo(temp);
/* 117 */     return temp;
/*     */   }
/*     */   
/*     */   public void makeUserInfo(FormCustomConf temp) {
/* 121 */     Map<String, String> mapUser = this.userService.getUserMapByUserIds(UserUtil.getUserIdSet((BaseModel)temp));
/* 122 */     UserUtil.makeUserInfo(mapUser, (BaseModel)temp);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/manager/impl/FormCustomConfManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */