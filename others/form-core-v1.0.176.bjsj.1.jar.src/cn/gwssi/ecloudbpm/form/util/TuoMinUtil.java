/*     */ package cn.gwssi.ecloudbpm.form.util;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessColumn;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import cn.hutool.core.exceptions.UtilException;
/*     */ import com.alibaba.fastjson.JSONArray;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.lang.reflect.Field;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class TuoMinUtil
/*     */ {
/*  20 */   private static final Integer PHONE_LEN = Integer.valueOf(11);
/*     */   
/*  22 */   private static final Integer TELPHONE_LEN = Integer.valueOf(10);
/*     */   
/*  24 */   private static final Integer ID_CARD_LEN = Integer.valueOf(18);
/*     */   
/*  26 */   private static final Integer NEED_DESENSITIZATION_ADDRESS_LEN = Integer.valueOf(5);
/*     */   
/*  28 */   private static final Integer NEED_DESENSITIZATION_QQ_LEN = Integer.valueOf(5);
/*     */   
/*  30 */   private static final Integer NEED_DESENSITIZATION_WECHAT_LEN = Integer.valueOf(5);
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
/*     */   public static void TuoMin(List<BusinessColumn> commonFieldList, JSONArray listData) {
/*  48 */     for (BusinessColumn fieldSetting : commonFieldList) {
/*     */ 
/*     */       
/*  51 */       String fuzz = "";
/*  52 */       if (StringUtil.isNotEmpty(fuzz))
/*     */       {
/*  54 */         customerFuzz(listData, fieldSetting.getName(), fuzz);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static void customerFuzz(JSONArray listData, String fieldName, String fuzz) {
/*  63 */     String[] fuzzSplit = fuzz.split(",");
/*     */     
/*  65 */     int fuzzSize = fuzzSplit.length;
/*     */     
/*  67 */     int left = Integer.parseInt(fuzzSplit[0]);
/*  68 */     if (left <= 0) {
/*  69 */       left = 0;
/*     */     }
/*     */ 
/*     */     
/*  73 */     int right = 0;
/*  74 */     String xing = "**";
/*     */ 
/*     */     
/*  77 */     if (fuzzSize == 2) {
/*  78 */       right = Integer.parseInt(fuzzSplit[1]);
/*  79 */       if (right <= 0) {
/*  80 */         right = 0;
/*     */       }
/*  82 */       xing = "****";
/*     */     } 
/*     */     
/*  85 */     if (fuzzSize == 3) {
/*  86 */       right = Integer.parseInt(fuzzSplit[1]);
/*  87 */       if (right <= 0) {
/*  88 */         right = 0;
/*     */       }
/*  90 */       xing = "********************".substring(0, Integer.parseInt(fuzzSplit[2]));
/*     */     } 
/*     */     
/*  93 */     for (Object dataJson : listData) {
/*  94 */       JSONObject dataJo = (JSONObject)dataJson;
/*  95 */       if (dataJo.containsKey(fieldName) && StringUtils.isNotEmpty(dataJo.getString(fieldName))) {
/*  96 */         String value = dataJo.getString(fieldName);
/*  97 */         String valueStr = value.toString();
/*  98 */         int valueStrLength = valueStr.length();
/*     */ 
/*     */         
/* 101 */         if (left >= valueStrLength) {
/*     */           continue;
/*     */         }
/*     */ 
/*     */         
/* 106 */         int size = left + right;
/* 107 */         if (size >= valueStrLength) {
/*     */           
/* 109 */           valueStr = valueStr.substring(0, left) + xing;
/* 110 */           dataJo.put(fieldName, valueStr);
/*     */           
/*     */           continue;
/*     */         } 
/* 114 */         if (right == 0) {
/* 115 */           valueStr = valueStr.substring(0, left) + xing;
/*     */         } else {
/*     */           
/* 118 */           valueStr = valueStr.substring(0, left) + xing + valueStr.substring(valueStrLength - right, valueStrLength);
/*     */         } 
/*     */         
/* 121 */         dataJo.put(fieldName, valueStr);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public static String TuoMin(String data, int TuoMinType) {
/* 128 */     switch (TuoMinType) {
/*     */       
/*     */       case 1:
/* 131 */         return desensPhone(data);
/*     */       
/*     */       case 2:
/* 134 */         return desensTelPhone(data);
/*     */       
/*     */       case 3:
/* 137 */         return desensIdCard(data);
/*     */       
/*     */       case 4:
/* 140 */         return desensName(data);
/*     */       
/*     */       case 5:
/* 143 */         return desensBankCardNo(data);
/*     */       
/*     */       case 6:
/* 146 */         return desensAddress(data);
/*     */       
/*     */       case 7:
/* 149 */         return desensQQ(data);
/*     */       
/*     */       case 8:
/* 152 */         return desensWechat(data);
/*     */     } 
/* 154 */     throw new UtilException("暂不支持该脱敏类型" + TuoMinType);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String desensPhone(String phone) {
/* 165 */     if (StringUtils.isBlank(phone)) {
/* 166 */       return phone;
/*     */     }
/* 168 */     phone = phone.replaceAll(" ", "");
/* 169 */     if (StringUtils.isBlank(phone) || phone.length() != PHONE_LEN.intValue()) {
/* 170 */       return phone;
/*     */     }
/* 172 */     return (new StringBuilder(phone)).replace(3, 7, "****").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String desensName(String bankCardNo) {
/* 182 */     if (StringUtils.isBlank(bankCardNo)) {
/* 183 */       return bankCardNo;
/*     */     }
/* 185 */     return (new StringBuilder(bankCardNo)).replace(1, bankCardNo.length(), "****").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String desensBankCardNo(String bankCardNo) {
/* 195 */     if (StringUtils.isBlank(bankCardNo) || bankCardNo.length() < 11) {
/* 196 */       return bankCardNo;
/*     */     }
/* 198 */     return (new StringBuilder(bankCardNo)).replace(3, 7, "****").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String desensTelPhone(String phone) {
/* 208 */     if (StringUtils.isBlank(phone) || phone.length() < TELPHONE_LEN.intValue()) {
/* 209 */       return phone;
/*     */     }
/* 211 */     return (new StringBuilder(phone)).replace(7, 9, "***").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String desensIdCard(String idCard) {
/* 221 */     if (StringUtils.isBlank(idCard) || idCard.length() != ID_CARD_LEN.intValue()) {
/* 222 */       return idCard;
/*     */     }
/* 224 */     return (new StringBuilder(idCard)).replace(6, 14, "********").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String desensAddress(String address) {
/*     */     int addressLen;
/* 235 */     if (StringUtils.isBlank(address) || (addressLen = address.length()) < NEED_DESENSITIZATION_ADDRESS_LEN.intValue()) {
/* 236 */       return address;
/*     */     }
/* 238 */     return (new StringBuilder(address)).replace(addressLen - 5, addressLen, "*****").toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String desensQQ(String qq) {
/*     */     int qqLen;
/* 249 */     if (StringUtils.isBlank(qq) || (qqLen = qq.length()) < NEED_DESENSITIZATION_QQ_LEN.intValue()) {
/* 250 */       return qq;
/*     */     }
/* 252 */     return (new StringBuilder(qq)).replace(2, qqLen - 3, makeRepeatableStar(qqLen - 5)).toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String desensWechat(String wechat) {
/*     */     int wechatLen;
/* 263 */     if (StringUtils.isBlank(wechat) || (wechatLen = wechat.length()) < NEED_DESENSITIZATION_WECHAT_LEN.intValue()) {
/* 264 */       return wechat;
/*     */     }
/* 266 */     return (wechatLen == PHONE_LEN.intValue()) ? desensPhone(wechat) : desensQQ(wechat);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String makeRepeatableStar(int repeatCount) {
/* 276 */     return makeRepeatableStr("*", repeatCount);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static String makeRepeatableStr(String str, int repeatCount) {
/* 287 */     if (StringUtils.isBlank(str)) {
/* 288 */       return "";
/*     */     }
/* 290 */     StringBuilder sb = new StringBuilder(repeatCount);
/* 291 */     for (int i = 0; i < repeatCount; i++) {
/* 292 */       sb.append(str);
/*     */     }
/* 294 */     return sb.toString();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 300 */   private static final List<String> PHONE_FIELD_LIST = Arrays.asList(new String[] { "phone", "telephoneNumber", "telephone", "mobile", "mobileNumber", "mobilePhone", "debtorPhone", "storeContactPhone", "phoneNumber" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 306 */   private static final List<String> ID_CARD_FIELD_LIST = Arrays.asList(new String[] { "idCard", "certificateId", "ecodeOrIdcard", "idNumber", "idcard", "splitIdCard" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 312 */   private static final List<String> ADDRESS_LIST = Arrays.asList(new String[] { "address", "debtorAddress" });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 317 */   private static final List<String> QQ_LIST = Arrays.asList(new String[] { "qq" });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 322 */   private static final List<String> TELPHONE_LIST = Arrays.asList(new String[] { "companyPhoneNumber", "workPhone" });
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 327 */   private static final List<String> WECHAT_LIST = Arrays.asList(new String[] { "wechat" });
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void doKeywordsDesensitization(List<Object> data) throws IllegalAccessException {
/* 336 */     for (Object obj : data) {
/* 337 */       Field[] fields = obj.getClass().getDeclaredFields();
/* 338 */       for (Field field : fields) {
/* 339 */         field.setAccessible(true);
/* 340 */         String fieldName = field.getName();
/*     */         
/* 342 */         if (PHONE_FIELD_LIST.contains(fieldName)) {
/* 343 */           field.set(obj, desensPhone((String)field.get(obj)));
/*     */ 
/*     */         
/*     */         }
/* 347 */         else if (ID_CARD_FIELD_LIST.contains(fieldName)) {
/* 348 */           field.set(obj, desensIdCard((String)field.get(obj)));
/*     */ 
/*     */         
/*     */         }
/* 352 */         else if (ADDRESS_LIST.contains(fieldName)) {
/* 353 */           field.set(obj, desensAddress((String)field.get(obj)));
/*     */ 
/*     */         
/*     */         }
/* 357 */         else if (QQ_LIST.contains(fieldName)) {
/* 358 */           field.set(obj, desensQQ((String)field.get(obj)));
/*     */ 
/*     */         
/*     */         }
/* 362 */         else if (WECHAT_LIST.contains(fieldName)) {
/* 363 */           field.set(obj, desensWechat((String)field.get(obj)));
/*     */ 
/*     */ 
/*     */         
/*     */         }
/* 368 */         else if (TELPHONE_LIST.contains(fieldName)) {
/* 369 */           field.set(obj, desensTelPhone((String)field.get(obj)));
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/util/TuoMinUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */