/*    */ package cn.gwssi.ecloudframework.base.core.util;
/*    */ 
/*    */ import cn.gwssi.ecloudframework.base.api.exception.SerializeException;
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.ByteArrayOutputStream;
/*    */ import java.io.IOException;
/*    */ import java.io.ObjectInputStream;
/*    */ import java.io.ObjectOutputStream;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ public class SerializeUtil
/*    */ {
/* 15 */   protected static Logger LOG = LoggerFactory.getLogger(SerializeUtil.class);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static byte[] serialize(Object object) {
/* 24 */     ObjectOutputStream oos = null;
/* 25 */     ByteArrayOutputStream baos = null;
/*    */     try {
/* 27 */       baos = new ByteArrayOutputStream();
/* 28 */       oos = new ObjectOutputStream(baos);
/* 29 */       oos.writeObject(object);
/* 30 */       byte[] bytes = baos.toByteArray();
/* 31 */       return bytes;
/* 32 */     } catch (Exception e) {
/* 33 */       throw new SerializeException(e.getMessage(), e);
/*    */     } finally {
/*    */       try {
/* 36 */         if (oos != null) {
/* 37 */           oos.close();
/*    */         }
/* 39 */         if (baos != null) {
/* 40 */           baos.close();
/*    */         }
/* 42 */       } catch (IOException e) {
/* 43 */         LOG.warn("序列化异常", e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Object unserialize(byte[] bytes) {
/* 55 */     ByteArrayInputStream bais = null;
/*    */     try {
/* 57 */       bais = new ByteArrayInputStream(bytes);
/* 58 */       ObjectInputStream ois = new ObjectInputStream(bais);
/* 59 */       return ois.readObject();
/* 60 */     } catch (Exception e) {
/* 61 */       throw new SerializeException(e.getMessage(), e);
/*    */     } finally {
/*    */       try {
/* 64 */         if (bais != null) {
/* 65 */           bais.close();
/*    */         }
/* 67 */       } catch (IOException e) {
/* 68 */         LOG.warn("反序列化异常", e);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/SerializeUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */