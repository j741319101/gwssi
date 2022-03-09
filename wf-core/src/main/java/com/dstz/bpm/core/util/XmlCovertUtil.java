/*    */ package com.dstz.bpm.core.util;
/*    */ 
/*    */ import java.io.ByteArrayInputStream;
/*    */ import java.io.InputStream;
/*    */ import java.io.StringWriter;
/*    */ import java.io.UnsupportedEncodingException;
/*    */ import javax.xml.bind.JAXBContext;
/*    */ import javax.xml.bind.JAXBException;
/*    */ import javax.xml.bind.Marshaller;
/*    */ import javax.xml.bind.Unmarshaller;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class XmlCovertUtil
/*    */ {
/*    */   @SafeVarargs
/*    */   public static <T> T covert2Object(String xml, Class<? extends Object>... classes) throws JAXBException, UnsupportedEncodingException {
/* 21 */     JAXBContext jAXBContext = JAXBContext.newInstance((Class[])classes);
/*    */     
/* 23 */     Unmarshaller unmarshaller = jAXBContext.createUnmarshaller();
/* 24 */     InputStream is = new ByteArrayInputStream(xml.getBytes("utf-8"));
/* 25 */     return (T)unmarshaller.unmarshal(is);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static String covert2Xml(Object serObj) throws JAXBException {
/* 32 */     JAXBContext jc = JAXBContext.newInstance(new Class[] { serObj.getClass() });
/*    */     
/* 34 */     StringWriter out = new StringWriter();
/* 35 */     Marshaller m = jc.createMarshaller();
/* 36 */     m.setProperty("jaxb.formatted.output", Boolean.valueOf(true));
/* 37 */     m.setProperty("jaxb.encoding", "utf-8");
/* 38 */     m.marshal(serObj, out);
/* 39 */     String tmp = out.toString();
/* 40 */     return tmp;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/util/XmlCovertUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */