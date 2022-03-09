/*    */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*    */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.dao.SysDataSourceDefDao;
/*    */ import cn.gwssi.ecloudframework.sys.core.manager.SysDataSourceDefManager;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.SysDataSourceDef;
/*    */ import cn.gwssi.ecloudframework.sys.core.model.def.SysDataSourceDefAttribute;
/*    */ import com.alibaba.fastjson.JSON;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Parameter;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import javax.sql.DataSource;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class SysDataSourceDefManagerImpl
/*    */   extends BaseManager<String, SysDataSourceDef>
/*    */   implements SysDataSourceDefManager
/*    */ {
/*    */   @Autowired
/*    */   SysDataSourceDefDao sysDataSourceDefDao;
/*    */   
/*    */   public List<SysDataSourceDefAttribute> initAttributes(String classPath) {
/*    */     try {
/* 36 */       List<SysDataSourceDefAttribute> attributes = new ArrayList<>();
/* 37 */       Class<?> cls = Class.forName(classPath);
/* 38 */       if (!DataSource.class.isAssignableFrom(cls)) {
/* 39 */         throw new Exception("classPath[" + classPath + "]不是javax.sql.DataSource的子类");
/*    */       }
/* 41 */       for (Method method : cls.getMethods()) {
/* 42 */         if (method.getName().startsWith("set") && (method.getParameters()).length == 1) {
/*    */ 
/*    */           
/* 45 */           Parameter param = method.getParameters()[0];
/* 46 */           SysDataSourceDefAttribute attribute = new SysDataSourceDefAttribute();
/* 47 */           String fieldName = StringUtil.lowerFirst(method.getName().replace("set", ""));
/* 48 */           attribute.setComment(fieldName);
/* 49 */           attribute.setName(fieldName);
/* 50 */           attribute.setRequired(false);
/* 51 */           attribute.setType(param.getType().getName());
/* 52 */           attributes.add(attribute);
/*    */         } 
/* 54 */       }  return attributes;
/* 55 */     } catch (Exception e) {
/* 56 */       throw new BusinessException("根据classPath[" + classPath + "]获取参数", e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 61 */     SysDataSourceDefManagerImpl impl = new SysDataSourceDefManagerImpl();
/*    */ 
/*    */     
/* 64 */     System.out.println(JSON.toJSONString(impl.initAttributes("org.apache.commons.dbcp.BasicDataSource")));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SysDataSourceDefManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */