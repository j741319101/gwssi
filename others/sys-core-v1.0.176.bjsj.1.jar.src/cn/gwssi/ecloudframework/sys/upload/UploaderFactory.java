/*    */ package cn.gwssi.ecloudframework.sys.upload;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*    */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*    */ import cn.gwssi.ecloudframework.base.core.util.PropertyUtil;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UploaderFactory
/*    */ {
/*    */   public static IUploader getUploader(String type) {
/* 33 */     Map<String, IUploader> map = AppUtil.getImplInstance(IUploader.class);
/* 34 */     for (Map.Entry<String, IUploader> entry : map.entrySet()) {
/* 35 */       if (((IUploader)entry.getValue()).type().equals(type)) {
/* 36 */         return entry.getValue();
/*    */       }
/*    */     } 
/* 39 */     throw new BusinessException(String.format("找不到类型[%s]的上传器的实现类", new Object[] { type }));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static IUploader getDefault() {
/* 50 */     return getUploader(PropertyUtil.getProperty("ecloud.uploader.default"));
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/upload/UploaderFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */