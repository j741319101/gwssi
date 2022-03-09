/*     */ package cn.gwssi.ecloudframework.base.core.util;
/*     */ 
/*     */ import net.sourceforge.pinyin4j.PinyinHelper;
/*     */ import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
/*     */ import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
/*     */ import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
/*     */ import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
/*     */ import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class HanyuPinyinHelper
/*     */ {
/*     */   public String toHanyuPinyin(String ChineseLanguage) {
/*  16 */     char[] cl_chars = ChineseLanguage.trim().toCharArray();
/*  17 */     String hanyupinyin = "";
/*  18 */     HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
/*  19 */     defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
/*  20 */     defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
/*  21 */     defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
/*     */     try {
/*  23 */       for (int i = 0; i < cl_chars.length; i++) {
/*  24 */         if (String.valueOf(cl_chars[i]).matches("[一-龥]+")) {
/*  25 */           hanyupinyin = hanyupinyin + PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
/*     */         } else {
/*  27 */           hanyupinyin = hanyupinyin + cl_chars[i];
/*     */         } 
/*     */       } 
/*  30 */     } catch (BadHanyuPinyinOutputFormatCombination e) {
/*  31 */       System.out.println("字符不能转成汉语拼音");
/*     */     } 
/*  33 */     return hanyupinyin;
/*     */   }
/*     */   
/*     */   public static String getFirstLettersUp(String ChineseLanguage) {
/*  37 */     return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.UPPERCASE);
/*     */   }
/*     */   
/*     */   public static String getFirstLettersLo(String ChineseLanguage) {
/*  41 */     return getFirstLetters(ChineseLanguage, HanyuPinyinCaseType.LOWERCASE);
/*     */   }
/*     */   
/*     */   public static String getFirstLetters(String ChineseLanguage, HanyuPinyinCaseType caseType) {
/*  45 */     char[] cl_chars = ChineseLanguage.trim().toCharArray();
/*  46 */     String hanyupinyin = "";
/*  47 */     HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
/*  48 */     defaultFormat.setCaseType(caseType);
/*  49 */     defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
/*     */     try {
/*  51 */       for (int i = 0; i < cl_chars.length; i++) {
/*  52 */         String str = String.valueOf(cl_chars[i]);
/*  53 */         if (str.matches("[一-龥]+")) {
/*  54 */           hanyupinyin = hanyupinyin + PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0].substring(0, 1);
/*  55 */         } else if (str.matches("[0-9]+")) {
/*  56 */           hanyupinyin = hanyupinyin + cl_chars[i];
/*  57 */         } else if (str.matches("[a-zA-Z]+")) {
/*  58 */           hanyupinyin = hanyupinyin + cl_chars[i];
/*     */         } else {
/*  60 */           hanyupinyin = hanyupinyin + cl_chars[i];
/*     */         } 
/*     */       } 
/*  63 */     } catch (BadHanyuPinyinOutputFormatCombination e) {
/*  64 */       System.out.println("字符不能转成汉语拼音");
/*     */     } 
/*  66 */     return hanyupinyin;
/*     */   }
/*     */   
/*     */   public static String getPinyinString(String ChineseLanguage) {
/*  70 */     char[] cl_chars = ChineseLanguage.trim().toCharArray();
/*  71 */     String hanyupinyin = "";
/*  72 */     HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
/*  73 */     defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
/*  74 */     defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
/*     */     try {
/*  76 */       for (int i = 0; i < cl_chars.length; i++) {
/*  77 */         String str = String.valueOf(cl_chars[i]);
/*  78 */         if (str.matches("[一-龥]+")) {
/*  79 */           hanyupinyin = hanyupinyin + PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
/*     */         }
/*  81 */         else if (str.matches("[0-9]+")) {
/*  82 */           hanyupinyin = hanyupinyin + cl_chars[i];
/*  83 */         } else if (str.matches("[a-zA-Z]+")) {
/*     */           
/*  85 */           hanyupinyin = hanyupinyin + cl_chars[i];
/*     */         }
/*     */       
/*     */       } 
/*  89 */     } catch (BadHanyuPinyinOutputFormatCombination e) {
/*  90 */       System.out.println("字符不能转成汉语拼音");
/*     */     } 
/*  92 */     return hanyupinyin;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getFirstLetter(String ChineseLanguage) {
/* 102 */     char[] cl_chars = ChineseLanguage.trim().toCharArray();
/* 103 */     String hanyupinyin = "";
/* 104 */     HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
/* 105 */     defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
/* 106 */     defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
/*     */     try {
/* 108 */       String str = String.valueOf(cl_chars[0]);
/* 109 */       if (str.matches("[一-龥]+")) {
/*     */         
/* 111 */         hanyupinyin = PinyinHelper.toHanyuPinyinStringArray(cl_chars[0], defaultFormat)[0].substring(0, 1);
/* 112 */       } else if (str.matches("[0-9]+")) {
/* 113 */         hanyupinyin = hanyupinyin + cl_chars[0];
/* 114 */       } else if (str.matches("[a-zA-Z]+")) {
/*     */         
/* 116 */         hanyupinyin = hanyupinyin + cl_chars[0];
/*     */       }
/*     */     
/*     */     }
/* 120 */     catch (BadHanyuPinyinOutputFormatCombination e) {
/* 121 */       System.out.println("字符不能转成汉语拼音");
/*     */     } 
/* 123 */     return hanyupinyin;
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/* 127 */     HanyuPinyinHelper hanyuPinyinHelper = new HanyuPinyinHelper();
/* 128 */     System.out.println(getFirstLetters("多发的发独守空房阿道夫打发第三方", HanyuPinyinCaseType.LOWERCASE));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/HanyuPinyinHelper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */