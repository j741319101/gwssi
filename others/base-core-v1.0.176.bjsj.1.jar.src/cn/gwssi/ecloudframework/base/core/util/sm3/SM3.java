/*     */ package cn.gwssi.ecloudframework.base.core.util.sm3;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SM3
/*     */ {
/*  12 */   public static final byte[] iv = new byte[] { 115, Byte.MIN_VALUE, 22, 111, 73, 20, -78, -71, 23, 36, 66, -41, -38, -118, 6, 0, -87, 111, 48, -68, 22, 49, 56, -86, -29, -115, -18, 77, -80, -5, 14, 78 };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  19 */   public static int[] Tj = new int[64];
/*     */   
/*     */   static {
/*     */     int i;
/*  23 */     for (i = 0; i < 16; i++)
/*     */     {
/*  25 */       Tj[i] = 2043430169;
/*     */     }
/*     */     
/*  28 */     for (i = 16; i < 64; i++)
/*     */     {
/*  30 */       Tj[i] = 2055708042;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static byte[] CF(byte[] V, byte[] B) {
/*  37 */     int[] v = convert(V);
/*  38 */     int[] b = convert(B);
/*  39 */     return convert(CF(v, b));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[] convert(byte[] arr) {
/*  44 */     int[] out = new int[arr.length / 4];
/*  45 */     byte[] tmp = new byte[4];
/*  46 */     for (int i = 0; i < arr.length; i += 4) {
/*     */       
/*  48 */       System.arraycopy(arr, i, tmp, 0, 4);
/*  49 */       out[i / 4] = bigEndianByteToInt(tmp);
/*     */     } 
/*  51 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] convert(int[] arr) {
/*  56 */     byte[] out = new byte[arr.length * 4];
/*  57 */     byte[] tmp = null;
/*  58 */     for (int i = 0; i < arr.length; i++) {
/*     */       
/*  60 */       tmp = bigEndianIntToByte(arr[i]);
/*  61 */       System.arraycopy(tmp, 0, out, i * 4, 4);
/*     */     } 
/*  63 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static int[] CF(int[] V, int[] B) {
/*  70 */     int a = V[0];
/*  71 */     int b = V[1];
/*  72 */     int c = V[2];
/*  73 */     int d = V[3];
/*  74 */     int e = V[4];
/*  75 */     int f = V[5];
/*  76 */     int g = V[6];
/*  77 */     int h = V[7];
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
/*  99 */     int[][] arr = expand(B);
/* 100 */     int[] w = arr[0];
/* 101 */     int[] w1 = arr[1];
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 111 */     for (int j = 0; j < 64; j++) {
/*     */       
/* 113 */       int ss1 = bitCycleLeft(a, 12) + e + bitCycleLeft(Tj[j], j);
/* 114 */       ss1 = bitCycleLeft(ss1, 7);
/* 115 */       int ss2 = ss1 ^ bitCycleLeft(a, 12);
/* 116 */       int tt1 = FFj(a, b, c, j) + d + ss2 + w1[j];
/* 117 */       int tt2 = GGj(e, f, g, j) + h + ss1 + w[j];
/* 118 */       d = c;
/* 119 */       c = bitCycleLeft(b, 9);
/* 120 */       b = a;
/* 121 */       a = tt1;
/* 122 */       h = g;
/* 123 */       g = bitCycleLeft(f, 19);
/* 124 */       f = e;
/* 125 */       e = P0(tt2);
/*     */     } 
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
/* 140 */     int[] out = new int[8];
/* 141 */     out[0] = a ^ V[0];
/* 142 */     out[1] = b ^ V[1];
/* 143 */     out[2] = c ^ V[2];
/* 144 */     out[3] = d ^ V[3];
/* 145 */     out[4] = e ^ V[4];
/* 146 */     out[5] = f ^ V[5];
/* 147 */     out[6] = g ^ V[6];
/* 148 */     out[7] = h ^ V[7];
/*     */     
/* 150 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int[][] expand(int[] B) {
/* 155 */     int[] W = new int[68];
/* 156 */     int[] W1 = new int[64]; int i;
/* 157 */     for (i = 0; i < B.length; i++)
/*     */     {
/* 159 */       W[i] = B[i];
/*     */     }
/*     */     
/* 162 */     for (i = 16; i < 68; i++)
/*     */     {
/* 164 */       W[i] = P1(W[i - 16] ^ W[i - 9] ^ bitCycleLeft(W[i - 3], 15)) ^ 
/* 165 */         bitCycleLeft(W[i - 13], 7) ^ W[i - 6];
/*     */     }
/*     */     
/* 168 */     for (i = 0; i < 64; i++)
/*     */     {
/* 170 */       W1[i] = W[i] ^ W[i + 4];
/*     */     }
/*     */     
/* 173 */     int[][] arr = { W, W1 };
/* 174 */     return arr;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] bigEndianIntToByte(int num) {
/* 179 */     return back(Util.intToBytes(num));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int bigEndianByteToInt(byte[] bytes) {
/* 184 */     return Util.byteToInt(back(bytes));
/*     */   }
/*     */ 
/*     */   
/*     */   private static int FFj(int X, int Y, int Z, int j) {
/* 189 */     if (j >= 0 && j <= 15)
/*     */     {
/* 191 */       return FF1j(X, Y, Z);
/*     */     }
/*     */ 
/*     */     
/* 195 */     return FF2j(X, Y, Z);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private static int GGj(int X, int Y, int Z, int j) {
/* 201 */     if (j >= 0 && j <= 15)
/*     */     {
/* 203 */       return GG1j(X, Y, Z);
/*     */     }
/*     */ 
/*     */     
/* 207 */     return GG2j(X, Y, Z);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static int FF1j(int X, int Y, int Z) {
/* 214 */     int tmp = X ^ Y ^ Z;
/* 215 */     return tmp;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int FF2j(int X, int Y, int Z) {
/* 220 */     int tmp = X & Y | X & Z | Y & Z;
/* 221 */     return tmp;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int GG1j(int X, int Y, int Z) {
/* 226 */     int tmp = X ^ Y ^ Z;
/* 227 */     return tmp;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int GG2j(int X, int Y, int Z) {
/* 232 */     int tmp = X & Y | (X ^ 0xFFFFFFFF) & Z;
/* 233 */     return tmp;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int P0(int X) {
/* 238 */     int y = rotateLeft(X, 9);
/* 239 */     y = bitCycleLeft(X, 9);
/* 240 */     int z = rotateLeft(X, 17);
/* 241 */     z = bitCycleLeft(X, 17);
/* 242 */     int t = X ^ y ^ z;
/* 243 */     return t;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int P1(int X) {
/* 248 */     int t = X ^ bitCycleLeft(X, 15) ^ bitCycleLeft(X, 23);
/* 249 */     return t;
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
/*     */   public static byte[] padding(byte[] in, int bLen) {
/* 262 */     int k = 448 - (8 * in.length + 1) % 512;
/* 263 */     if (k < 0)
/*     */     {
/* 265 */       k = 960 - (8 * in.length + 1) % 512;
/*     */     }
/* 267 */     k++;
/* 268 */     byte[] padd = new byte[k / 8];
/* 269 */     padd[0] = Byte.MIN_VALUE;
/* 270 */     long n = (in.length * 8 + bLen * 512);
/* 271 */     byte[] out = new byte[in.length + k / 8 + 8];
/* 272 */     int pos = 0;
/* 273 */     System.arraycopy(in, 0, out, 0, in.length);
/* 274 */     pos += in.length;
/* 275 */     System.arraycopy(padd, 0, out, pos, padd.length);
/* 276 */     pos += padd.length;
/* 277 */     byte[] tmp = back(Util.longToBytes(n));
/* 278 */     System.arraycopy(tmp, 0, out, pos, tmp.length);
/* 279 */     return out;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static byte[] back(byte[] in) {
/* 290 */     byte[] out = new byte[in.length];
/* 291 */     for (int i = 0; i < out.length; i++)
/*     */     {
/* 293 */       out[i] = in[out.length - i - 1];
/*     */     }
/*     */     
/* 296 */     return out;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int rotateLeft(int x, int n) {
/* 301 */     return x << n | x >> 32 - n;
/*     */   }
/*     */ 
/*     */   
/*     */   private static int bitCycleLeft(int n, int bitLen) {
/* 306 */     bitLen %= 32;
/* 307 */     byte[] tmp = bigEndianIntToByte(n);
/* 308 */     int byteLen = bitLen / 8;
/* 309 */     int len = bitLen % 8;
/* 310 */     if (byteLen > 0)
/*     */     {
/* 312 */       tmp = byteCycleLeft(tmp, byteLen);
/*     */     }
/*     */     
/* 315 */     if (len > 0)
/*     */     {
/* 317 */       tmp = bitSmall8CycleLeft(tmp, len);
/*     */     }
/*     */     
/* 320 */     return bigEndianByteToInt(tmp);
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] bitSmall8CycleLeft(byte[] in, int len) {
/* 325 */     byte[] tmp = new byte[in.length];
/*     */     
/* 327 */     for (int i = 0; i < tmp.length; i++) {
/*     */       
/* 329 */       int t1 = (byte)((in[i] & 0xFF) << len);
/* 330 */       int t2 = (byte)((in[(i + 1) % tmp.length] & 0xFF) >> 8 - len);
/* 331 */       int t3 = (byte)(t1 | t2);
/* 332 */       tmp[i] = (byte)t3;
/*     */     } 
/*     */     
/* 335 */     return tmp;
/*     */   }
/*     */ 
/*     */   
/*     */   private static byte[] byteCycleLeft(byte[] in, int byteLen) {
/* 340 */     byte[] tmp = new byte[in.length];
/* 341 */     System.arraycopy(in, byteLen, tmp, 0, in.length - byteLen);
/* 342 */     System.arraycopy(in, 0, tmp, in.length - byteLen, byteLen);
/* 343 */     return tmp;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/sm3/SM3.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */