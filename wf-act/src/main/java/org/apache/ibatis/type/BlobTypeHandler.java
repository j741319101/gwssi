/*     */ package org.apache.ibatis.type;
/*     */ 
/*     */ import com.google.common.primitives.Bytes;
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.sql.Blob;
/*     */ import java.sql.CallableStatement;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
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
/*     */ public class BlobTypeHandler
/*     */   extends BaseTypeHandler<byte[]>
/*     */ {
/*     */   public void setNonNullParameter(PreparedStatement ps, int i, byte[] parameter, JdbcType jdbcType) throws SQLException {
/*  42 */     ByteArrayInputStream bis = new ByteArrayInputStream(parameter);
/*  43 */     ps.setBinaryStream(i, bis, parameter.length);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
/*  49 */     Blob blob = rs.getBlob(columnName);
/*  50 */     List<Byte> returnValue = new ArrayList<>();
/*  51 */     if (null != blob) {
/*  52 */       InputStream io = null;
/*     */       try {
/*  54 */         io = blob.getBinaryStream();
/*  55 */         byte[] value = new byte[2048];
/*  56 */         int i = 0;
/*  57 */         while ((i = io.read(value)) != -1) {
/*  58 */           for (int k = 0; k < i; k++) {
/*  59 */             returnValue.add(Byte.valueOf(value[k]));
/*     */           }
/*     */         } 
/*  62 */       } catch (Exception e) {
/*  63 */         e.printStackTrace();
/*     */       } finally {
/*     */         try {
/*  66 */           io.close();
/*  67 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     } 
/*     */     
/*  71 */     return Bytes.toArray(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
/*  77 */     Blob blob = rs.getBlob(columnIndex);
/*  78 */     List<Byte> returnValue = new ArrayList<>();
/*  79 */     if (null != blob) {
/*  80 */       InputStream io = null;
/*     */       try {
/*  82 */         io = blob.getBinaryStream();
/*  83 */         byte[] value = new byte[2048];
/*  84 */         int i = 0;
/*  85 */         while ((i = io.read(value)) != -1) {
/*  86 */           for (int k = 0; k < i; k++) {
/*  87 */             returnValue.add(Byte.valueOf(value[k]));
/*     */           }
/*     */         } 
/*  90 */       } catch (Exception e) {
/*  91 */         e.printStackTrace();
/*     */       } finally {
/*     */         try {
/*  94 */           io.close();
/*  95 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     } 
/*     */     
/*  99 */     return Bytes.toArray(returnValue);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public byte[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
/* 105 */     Blob blob = cs.getBlob(columnIndex);
/* 106 */     List<Byte> returnValue = new ArrayList<>();
/* 107 */     if (null != blob) {
/* 108 */       InputStream io = null;
/*     */       try {
/* 110 */         io = blob.getBinaryStream();
/* 111 */         byte[] value = new byte[2048];
/* 112 */         int i = 0;
/* 113 */         while ((i = io.read(value)) != -1) {
/* 114 */           returnValue.addAll(Bytes.asList(value));
/*     */         }
/* 116 */       } catch (Exception e) {
/* 117 */         e.printStackTrace();
/*     */       } finally {
/*     */         try {
/* 120 */           io.close();
/* 121 */         } catch (IOException iOException) {}
/*     */       } 
/*     */     } 
/*     */     
/* 125 */     return Bytes.toArray(returnValue);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/org/apache/ibatis/type/BlobTypeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */