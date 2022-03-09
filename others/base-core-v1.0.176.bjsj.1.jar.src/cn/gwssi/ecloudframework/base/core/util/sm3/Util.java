/*     */ package cn.gwssi.ecloudframework.base.core.util.sm3;
/*     */ 
/*     */ import java.math.BigInteger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Util
/*     */ {
/*     */   public static byte[] intToBytes(int num) {
/*  15 */     byte[] bytes = new byte[4];
/*  16 */     bytes[0] = (byte)(0xFF & num >> 0);
/*  17 */     bytes[1] = (byte)(0xFF & num >> 8);
/*  18 */     bytes[2] = (byte)(0xFF & num >> 16);
/*  19 */     bytes[3] = (byte)(0xFF & num >> 24);
/*  20 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int byteToInt(byte[] bytes) {
/*  31 */     int num = 0;
/*     */     
/*  33 */     int temp = (0xFF & bytes[0]) << 0;
/*  34 */     num |= temp;
/*  35 */     temp = (0xFF & bytes[1]) << 8;
/*  36 */     num |= temp;
/*  37 */     temp = (0xFF & bytes[2]) << 16;
/*  38 */     num |= temp;
/*  39 */     temp = (0xFF & bytes[3]) << 24;
/*  40 */     num |= temp;
/*  41 */     return num;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] longToBytes(long num) {
/*  52 */     byte[] bytes = new byte[8];
/*  53 */     for (int i = 0; i < 8; i++)
/*     */     {
/*  55 */       bytes[i] = (byte)(int)(0xFFL & num >> i * 8);
/*     */     }
/*     */     
/*  58 */     return bytes;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] byteConvert32Bytes(BigInteger n) {
/*  69 */     byte[] tmpd = (byte[])null;
/*  70 */     if (n == null)
/*     */     {
/*  72 */       return null;
/*     */     }
/*     */     
/*  75 */     if ((n.toByteArray()).length == 33) {
/*     */       
/*  77 */       tmpd = new byte[32];
/*  78 */       System.arraycopy(n.toByteArray(), 1, tmpd, 0, 32);
/*     */     }
/*  80 */     else if ((n.toByteArray()).length == 32) {
/*     */       
/*  82 */       tmpd = n.toByteArray();
/*     */     }
/*     */     else {
/*     */       
/*  86 */       tmpd = new byte[32];
/*  87 */       for (int i = 0; i < 32 - (n.toByteArray()).length; i++)
/*     */       {
/*  89 */         tmpd[i] = 0;
/*     */       }
/*  91 */       System.arraycopy(n.toByteArray(), 0, tmpd, 32 - (n.toByteArray()).length, (n.toByteArray()).length);
/*     */     } 
/*  93 */     return tmpd;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigInteger byteConvertInteger(byte[] b) {
/* 104 */     if (b[0] < 0) {
/*     */       
/* 106 */       byte[] temp = new byte[b.length + 1];
/* 107 */       temp[0] = 0;
/* 108 */       System.arraycopy(b, 0, temp, 1, b.length);
/* 109 */       return new BigInteger(temp);
/*     */     } 
/* 111 */     return new BigInteger(b);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getHexString(byte[] bytes) {
/* 122 */     return getHexString(bytes, true);
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
/*     */   public static String getHexString(byte[] bytes, boolean upperCase) {
/* 134 */     String ret = "";
/* 135 */     for (int i = 0; i < bytes.length; i++)
/*     */     {
/* 137 */       ret = ret + Integer.toString((bytes[i] & 0xFF) + 256, 16).substring(1);
/*     */     }
/* 139 */     return upperCase ? ret.toUpperCase() : ret;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void printHexString(byte[] bytes) {
/* 149 */     for (int i = 0; i < bytes.length; i++) {
/*     */       
/* 151 */       String hex = Integer.toHexString(bytes[i] & 0xFF);
/* 152 */       if (hex.length() == 1)
/*     */       {
/* 154 */         hex = '0' + hex;
/*     */       }
/* 156 */       System.out.print("0x" + hex.toUpperCase() + ",");
/*     */     } 
/* 158 */     System.out.println("");
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
/*     */   public static byte[] hexStringToBytes(String hexString) {
/* 170 */     if (hexString == null || hexString.equals(""))
/*     */     {
/* 172 */       return null;
/*     */     }
/*     */     
/* 175 */     hexString = hexString.toUpperCase();
/* 176 */     int length = hexString.length() / 2;
/* 177 */     char[] hexChars = hexString.toCharArray();
/* 178 */     byte[] d = new byte[length];
/* 179 */     for (int i = 0; i < length; i++) {
/*     */       
/* 181 */       int pos = i * 2;
/* 182 */       d[i] = (byte)(charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
/*     */     } 
/* 184 */     return d;
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
/*     */   public static byte charToByte(char c) {
/* 196 */     return (byte)"0123456789ABCDEF".indexOf(c);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   private static final char[] DIGITS_LOWER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 208 */   private static final char[] DIGITS_UPPER = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] encodeHex(byte[] data) {
/* 218 */     return encodeHex(data, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static char[] encodeHex(byte[] data, boolean toLowerCase) {
/* 229 */     return encodeHex(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static char[] encodeHex(byte[] data, char[] toDigits) {
/* 240 */     int l = data.length;
/* 241 */     char[] out = new char[l << 1];
/*     */     
/* 243 */     for (int i = 0, j = 0; i < l; i++) {
/* 244 */       out[j++] = toDigits[(0xF0 & data[i]) >>> 4];
/* 245 */       out[j++] = toDigits[0xF & data[i]];
/*     */     } 
/* 247 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeHexString(byte[] data) {
/* 257 */     return encodeHexString(data, true);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String encodeHexString(byte[] data, boolean toLowerCase) {
/* 268 */     return encodeHexString(data, toLowerCase ? DIGITS_LOWER : DIGITS_UPPER);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected static String encodeHexString(byte[] data, char[] toDigits) {
/* 279 */     return new String(encodeHex(data, toDigits));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] decodeHex(char[] data) {
/* 290 */     int len = data.length;
/*     */     
/* 292 */     if ((len & 0x1) != 0) {
/* 293 */       throw new RuntimeException("Odd number of characters.");
/*     */     }
/*     */     
/* 296 */     byte[] out = new byte[len >> 1];
/*     */ 
/*     */     
/* 299 */     for (int i = 0, j = 0; j < len; i++) {
/* 300 */       int f = toDigit(data[j], j) << 4;
/* 301 */       j++;
/* 302 */       f |= toDigit(data[j], j);
/* 303 */       j++;
/* 304 */       out[i] = (byte)(f & 0xFF);
/*     */     } 
/*     */     
/* 307 */     return out;
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
/*     */   protected static int toDigit(char ch, int index) {
/* 319 */     int digit = Character.digit(ch, 16);
/* 320 */     if (digit == -1) {
/* 321 */       throw new RuntimeException("Illegal hexadecimal character " + ch + " at index " + index);
/*     */     }
/*     */     
/* 324 */     return digit;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String StringToAsciiString(String content) {
/* 335 */     String result = "";
/* 336 */     int max = content.length();
/* 337 */     for (int i = 0; i < max; i++) {
/* 338 */       char c = content.charAt(i);
/* 339 */       String b = Integer.toHexString(c);
/* 340 */       result = result + b;
/*     */     } 
/* 342 */     return result;
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
/*     */   
/*     */   public static String hexStringToString(String hexString, int encodeType) {
/* 355 */     String result = "";
/* 356 */     int max = hexString.length() / encodeType;
/* 357 */     for (int i = 0; i < max; i++) {
/* 358 */       char c = (char)hexStringToAlgorism(hexString
/* 359 */           .substring(i * encodeType, (i + 1) * encodeType));
/* 360 */       result = result + c;
/*     */     } 
/* 362 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int hexStringToAlgorism(String hex) {
/* 373 */     hex = hex.toUpperCase();
/* 374 */     int max = hex.length();
/* 375 */     int result = 0;
/* 376 */     for (int i = max; i > 0; i--) {
/* 377 */       char c = hex.charAt(i - 1);
/* 378 */       int algorism = 0;
/* 379 */       if (c >= '0' && c <= '9') {
/* 380 */         algorism = c - 48;
/*     */       } else {
/* 382 */         algorism = c - 55;
/*     */       } 
/* 384 */       result = (int)(result + Math.pow(16.0D, (max - i)) * algorism);
/*     */     } 
/* 386 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String hexStringToBinary(String hex) {
/* 397 */     hex = hex.toUpperCase();
/* 398 */     String result = "";
/* 399 */     int max = hex.length();
/* 400 */     for (int i = 0; i < max; i++) {
/* 401 */       char c = hex.charAt(i);
/* 402 */       switch (c) {
/*     */         case '0':
/* 404 */           result = result + "0000";
/*     */           break;
/*     */         case '1':
/* 407 */           result = result + "0001";
/*     */           break;
/*     */         case '2':
/* 410 */           result = result + "0010";
/*     */           break;
/*     */         case '3':
/* 413 */           result = result + "0011";
/*     */           break;
/*     */         case '4':
/* 416 */           result = result + "0100";
/*     */           break;
/*     */         case '5':
/* 419 */           result = result + "0101";
/*     */           break;
/*     */         case '6':
/* 422 */           result = result + "0110";
/*     */           break;
/*     */         case '7':
/* 425 */           result = result + "0111";
/*     */           break;
/*     */         case '8':
/* 428 */           result = result + "1000";
/*     */           break;
/*     */         case '9':
/* 431 */           result = result + "1001";
/*     */           break;
/*     */         case 'A':
/* 434 */           result = result + "1010";
/*     */           break;
/*     */         case 'B':
/* 437 */           result = result + "1011";
/*     */           break;
/*     */         case 'C':
/* 440 */           result = result + "1100";
/*     */           break;
/*     */         case 'D':
/* 443 */           result = result + "1101";
/*     */           break;
/*     */         case 'E':
/* 446 */           result = result + "1110";
/*     */           break;
/*     */         case 'F':
/* 449 */           result = result + "1111";
/*     */           break;
/*     */       } 
/*     */     } 
/* 453 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String AsciiStringToString(String content) {
/* 464 */     String result = "";
/* 465 */     int length = content.length() / 2;
/* 466 */     for (int i = 0; i < length; i++) {
/* 467 */       String c = content.substring(i * 2, i * 2 + 2);
/* 468 */       int a = hexStringToAlgorism(c);
/* 469 */       char b = (char)a;
/* 470 */       String d = String.valueOf(b);
/* 471 */       result = result + d;
/*     */     } 
/* 473 */     return result;
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
/*     */   
/*     */   public static String algorismToHexString(int algorism, int maxLength) {
/* 486 */     String result = "";
/* 487 */     result = Integer.toHexString(algorism);
/*     */     
/* 489 */     if (result.length() % 2 == 1) {
/* 490 */       result = "0" + result;
/*     */     }
/* 492 */     return patchHexString(result.toUpperCase(), maxLength);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String byteToString(byte[] bytearray) {
/* 503 */     String result = "";
/*     */ 
/*     */     
/* 506 */     int length = bytearray.length;
/* 507 */     for (int i = 0; i < length; i++) {
/* 508 */       char temp = (char)bytearray[i];
/* 509 */       result = result + temp;
/*     */     } 
/* 511 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int binaryToAlgorism(String binary) {
/* 522 */     int max = binary.length();
/* 523 */     int result = 0;
/* 524 */     for (int i = max; i > 0; i--) {
/* 525 */       char c = binary.charAt(i - 1);
/* 526 */       int algorism = c - 48;
/* 527 */       result = (int)(result + Math.pow(2.0D, (max - i)) * algorism);
/*     */     } 
/* 529 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String algorismToHEXString(int algorism) {
/* 540 */     String result = "";
/* 541 */     result = Integer.toHexString(algorism);
/*     */     
/* 543 */     if (result.length() % 2 == 1) {
/* 544 */       result = "0" + result;
/*     */     }
/*     */     
/* 547 */     result = result.toUpperCase();
/*     */     
/* 549 */     return result;
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
/*     */   
/*     */   public static String patchHexString(String str, int maxLength) {
/* 562 */     String temp = "";
/* 563 */     for (int i = 0; i < maxLength - str.length(); i++) {
/* 564 */       temp = "0" + temp;
/*     */     }
/* 566 */     str = (temp + str).substring(0, maxLength);
/* 567 */     return str;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public static int parseToInt(String s, int defaultInt, int radix) {
/* 582 */     int i = 0;
/*     */     try {
/* 584 */       i = Integer.parseInt(s, radix);
/* 585 */     } catch (NumberFormatException ex) {
/* 586 */       i = defaultInt;
/*     */     } 
/* 588 */     return i;
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
/*     */   
/*     */   public static int parseToInt(String s, int defaultInt) {
/* 601 */     int i = 0;
/*     */     try {
/* 603 */       i = Integer.parseInt(s);
/* 604 */     } catch (NumberFormatException ex) {
/* 605 */       i = defaultInt;
/*     */     } 
/* 607 */     return i;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] hexToByte(String hex) throws IllegalArgumentException {
/* 617 */     if (hex.length() % 2 != 0) {
/* 618 */       throw new IllegalArgumentException();
/*     */     }
/* 620 */     char[] arr = hex.toCharArray();
/* 621 */     byte[] b = new byte[hex.length() / 2];
/* 622 */     for (int i = 0, j = 0, l = hex.length(); i < l; i++, j++) {
/* 623 */       String swap = "" + arr[i++] + arr[i];
/* 624 */       int byteint = Integer.parseInt(swap, 16) & 0xFF;
/* 625 */       b[j] = (new Integer(byteint)).byteValue();
/*     */     } 
/* 627 */     return b;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String byteToHex(byte[] b) {
/* 638 */     if (b == null) {
/* 639 */       throw new IllegalArgumentException("Argument b ( byte array ) is null! ");
/*     */     }
/*     */     
/* 642 */     String hs = "";
/* 643 */     String stmp = "";
/* 644 */     for (int n = 0; n < b.length; n++) {
/* 645 */       stmp = Integer.toHexString(b[n] & 0xFF);
/* 646 */       if (stmp.length() == 1) {
/* 647 */         hs = hs + "0" + stmp;
/*     */       } else {
/* 649 */         hs = hs + stmp;
/*     */       } 
/*     */     } 
/* 652 */     return hs.toUpperCase();
/*     */   }
/*     */   
/*     */   public static byte[] subByte(byte[] input, int startIndex, int length) {
/* 656 */     byte[] bt = new byte[length];
/* 657 */     for (int i = 0; i < length; i++) {
/* 658 */       bt[i] = input[i + startIndex];
/*     */     }
/* 660 */     return bt;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/sm3/Util.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */