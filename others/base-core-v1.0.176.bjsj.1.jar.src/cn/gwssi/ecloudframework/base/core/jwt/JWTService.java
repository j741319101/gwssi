/*     */ package cn.gwssi.ecloudframework.base.core.jwt;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.core.cache.ICache;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import cn.hutool.core.lang.Assert;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import io.jsonwebtoken.Claims;
/*     */ import io.jsonwebtoken.Jwts;
/*     */ import io.jsonwebtoken.SignatureAlgorithm;
/*     */ import java.util.Date;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class JWTService
/*     */ {
/*  24 */   private final Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   public static final String JWT_CACHE_REGION = "jwtToken";
/*     */   
/*     */   @Value("${ecloud.jwt.header:Authorization}")
/*     */   private String header;
/*     */   
/*     */   @Value("${ecloud.jwt.param:authorization}")
/*     */   private String param;
/*     */   
/*     */   public String getJwtHeader() {
/*  35 */     return this.header;
/*     */   }
/*     */ 
/*     */   
/*     */   @Value("${ecloud.jwt.tokenHead:Bearer-}")
/*     */   private String tokenHead;
/*     */   @Value("${ecloud.jwt.enabled:false}")
/*     */   private Boolean enabled;
/*     */   
/*     */   public String getJwtParam() {
/*  45 */     return this.param;
/*     */   }
/*     */ 
/*     */   
/*     */   @Value("${ecloud.jwt.secret:asd%WE^@&fas156dfa}")
/*     */   private String secret;
/*     */   @Value("${ecloud.jwt.issuer:ecloud}")
/*     */   private String issuer;
/*     */   
/*     */   public String getJwtTokenHead() {
/*  55 */     return this.tokenHead;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean getJwtEnabled() {
/*  63 */     return this.enabled;
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
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${ecloud.jwt.expiration: 86400}")
/*  82 */   private Long expirationSecond = Long.valueOf(86400L);
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Value("${ecloud.jwt.:15}")
/*     */   private Long notBeforeMinute;
/*     */ 
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private ICache<String> icache;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private Claims getClaimsFromToken(String token) {
/*  99 */     return (Claims)Jwts.parser().setSigningKey(this.secret).parseClaimsJws(token).getBody();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getSubjectFromToken(String token) {
/*     */     String username;
/*     */     try {
/* 112 */       Claims claims = getClaimsFromToken(token);
/* 113 */       username = claims.getSubject();
/* 114 */     } catch (Exception e) {
/* 115 */       this.logger.debug("从令牌中获取认证失败", e);
/* 116 */       username = null;
/*     */     } 
/* 118 */     return username;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Boolean validateToken(String token) {
/*     */     try {
/* 130 */       Claims claims = getClaimsFromToken(token);
/* 131 */       Date expiration = claims.getExpiration();
/* 132 */       Date notBefore = claims.getNotBefore();
/* 133 */       return Boolean.valueOf(((new Date()).after(notBefore) && (new Date()).before(expiration)));
/* 134 */     } catch (Exception e) {
/* 135 */       this.logger.debug("验证令牌是否时间有效失败", e);
/* 136 */       return Boolean.valueOf(false);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getValidSubjectFromRedisToken(String authToken) {
/* 146 */     if (StringUtil.isEmpty(authToken)) return null; 
/*     */     try {
/* 148 */       Claims claims = getClaimsFromToken(authToken);
/*     */       
/* 150 */       if (claims != null) {
/* 151 */         String token = (String)this.icache.getByKey("jwtToken", String.format("jwt:%s:%s", new Object[] { claims.getAudience(), claims.getSubject() }));
/* 152 */         if (StringUtil.isEmpty(token)) {
/* 153 */           this.logger.debug("JWT token 校验失败，token 已过期,签发时间 " + DateUtil.formatDateTime(claims.getIssuedAt()));
/* 154 */           return null;
/*     */         } 
/*     */ 
/*     */         
/* 158 */         if (authToken.equals(token)) {
/* 159 */           this.icache.add2Region("jwtToken", String.format("jwt:%s:%s", new Object[] { claims.getAudience(), claims.getSubject() }), token);
/* 160 */           return claims.getSubject();
/*     */         } 
/* 162 */         this.logger.info("JWT token 校验失败，服务器 token 与 被校验 token 不一致！ 同一签发对象的 token 不支持多地登录 {}", claims.toString());
/*     */       
/*     */       }
/*     */     
/*     */     }
/* 167 */     catch (Exception e) {
/* 168 */       this.logger.warn("解析令牌失败", e);
/* 169 */       return null;
/*     */     } 
/* 171 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public void logoutRedisToken(String authToken) {
/* 176 */     if (StringUtil.isEmpty(authToken))
/*     */       return;  try {
/* 178 */       Claims claims = getClaimsFromToken(authToken);
/*     */       
/* 180 */       if (claims != null) {
/* 181 */         this.icache.delByKey("jwtToken", String.format("jwt:%s:%s", new Object[] { claims.getAudience(), claims.getSubject() }));
/*     */       }
/* 183 */     } catch (Exception e) {
/* 184 */       this.logger.warn("解析令牌失败", e);
/*     */     } 
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
/*     */   public String generateToken(String username, String audience) {
/* 200 */     Assert.notBlank(audience, "生成token 签发对象 不能为空", new Object[0]);
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
/* 212 */     String token = Jwts.builder().setIssuer(this.issuer).setSubject(username).setAudience(audience).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS512, this.secret).compact();
/*     */ 
/*     */ 
/*     */     
/* 216 */     if (this.icache != null) {
/* 217 */       this.icache.add2Region("jwtToken", String.format("jwt:%s:%s", new Object[] { audience, username }), token);
/*     */     }
/*     */     
/* 220 */     return token;
/*     */   }
/*     */ 
/*     */   
/*     */   public static void main(String[] args) {
/* 225 */     JWTService jwtService = new JWTService();
/* 226 */     jwtService.tokenHead = "Bearer-";
/* 227 */     jwtService.header = "Authorization";
/* 228 */     jwtService.header = "Bearer-";
/* 229 */     jwtService.issuer = "ecloudframework";
/* 230 */     jwtService.secret = "asd%WE^@&fas156dfa";
/* 231 */     jwtService.notBeforeMinute = Long.valueOf(15L);
/*     */     
/* 233 */     System.out.println(JSON.toJSONString(jwtService));
/*     */     
/* 235 */     String token = jwtService.generateToken("admin", "pc");
/* 236 */     System.out.println(token);
/* 237 */     Claims claims = jwtService.getClaimsFromToken(token);
/* 238 */     System.out.println(claims);
/* 239 */     System.out.println(DateUtil.formatDateTime(claims.getIssuedAt()));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/jwt/JWTService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */