/*     */ package com.dstz.base.core.util;
/*     */ 
/*     */ import com.dstz.base.api.constant.BaseStatusCode;
/*     */ import com.dstz.base.api.exception.BusinessError;
/*     */ import com.dstz.base.core.jwt.JWTService;
/*     */ import cn.hutool.core.map.MapUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
/*     */ import java.nio.charset.Charset;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.core.ParameterizedTypeReference;
/*     */ import org.springframework.http.HttpEntity;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpMethod;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.http.converter.StringHttpMessageConverter;
/*     */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.client.RestTemplate;
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
/*     */ public class RestTemplateUtil
/*     */ {
/*     */   private static RestTemplate restTemplate;
/*  42 */   private static Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static RestTemplate restTemplate() {
/*  49 */     if (restTemplate == null) {
/*  50 */       FastJsonHttpMessageConverter fastJsonHttpMessageConverter = new FastJsonHttpMessageConverter();
/*  51 */       List<MediaType> mediaTypes = new ArrayList<>();
/*  52 */       mediaTypes.add(MediaType.valueOf("text/html;charset=UTF-8"));
/*  53 */       mediaTypes.add(MediaType.valueOf("application/json;charset=UTF-8"));
/*  54 */       fastJsonHttpMessageConverter.setSupportedMediaTypes(mediaTypes);
/*  55 */       fastJsonHttpMessageConverter.getFastJsonConfig().setSerializerFeatures(new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat });
/*     */       
/*  57 */       restTemplate = new RestTemplate();
/*  58 */       restTemplate.getMessageConverters().add(new StringHttpMessageConverter(Charset.forName("UTF-8")));
/*  59 */       restTemplate.getMessageConverters().add(fastJsonHttpMessageConverter);
/*     */     } 
/*  61 */     return restTemplate;
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
/*     */   public static <T> T post(String url, Object data, Class<T> responseType) {
/*  74 */     HttpHeaders headers = new HttpHeaders();
/*  75 */     HttpServletRequest request = RequestContext.getHttpServletRequest();
/*  76 */     JWTService jwtService = AppUtil.<JWTService>getBean(JWTService.class);
/*  77 */     String authHeader = request.getHeader(jwtService.getJwtHeader());
/*     */     
/*  79 */     if (StringUtil.isEmpty(authHeader)) {
/*  80 */       authHeader = CookieUitl.getValueByName(jwtService.getJwtHeader(), request);
/*  81 */       headers.add("Cookie", jwtService.getJwtHeader() + "=" + authHeader);
/*     */     } else {
/*  83 */       headers.add(jwtService.getJwtHeader(), authHeader);
/*     */     } 
/*  85 */     HttpEntity<Object> entity = new HttpEntity(data, (MultiValueMap)headers);
/*  86 */     String urlTemp = RequestContext.getUrl(url);
/*  87 */     logger.info("entity: {}", JSON.toJSONString(entity));
/*  88 */     logger.info("url:{}", urlTemp);
/*  89 */     ResponseEntity<T> responseEntity = restTemplate().postForEntity(urlTemp, entity, responseType, new Object[0]);
/*  90 */     logger.info("responseEntity:{}", JSON.toJSONString(responseEntity));
/*  91 */     if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
/*  92 */       throw new BusinessError(JSON.toJSONString(responseEntity), BaseStatusCode.REMOTE_ERROR);
/*     */     }
/*     */     
/*  95 */     return (T)responseEntity.getBody();
/*     */   }
/*     */   
/*     */   public static void main(String[] args) {
/*  99 */     logger.info("123{}", "1");
/*     */   }
/*     */   
/*     */   public static <T> T post(String url, Object data, ParameterizedTypeReference<T> typeReference) {
/* 103 */     HttpHeaders headers = new HttpHeaders();
/* 104 */     HttpServletRequest request = RequestContext.getHttpServletRequest();
/* 105 */     JWTService jwtService = AppUtil.<JWTService>getBean(JWTService.class);
/* 106 */     String authHeader = request.getHeader(jwtService.getJwtHeader());
/*     */     
/* 108 */     if (StringUtil.isEmpty(authHeader)) {
/* 109 */       authHeader = CookieUitl.getValueByName(jwtService.getJwtHeader(), request);
/* 110 */       headers.add("Cookie", jwtService.getJwtHeader() + "=" + authHeader);
/*     */     } else {
/* 112 */       headers.add(jwtService.getJwtHeader(), authHeader);
/*     */     } 
/* 114 */     HttpEntity<Object> entity = new HttpEntity(data, (MultiValueMap)headers);
/* 115 */     String urlTemp = RequestContext.getUrl(url);
/* 116 */     logger.info("entity: {}", JSON.toJSONString(entity));
/* 117 */     logger.info("url:{}", urlTemp);
/* 118 */     ResponseEntity<T> responseEntity = restTemplate().exchange(url, HttpMethod.POST, entity, typeReference, new Object[0]);
/* 119 */     logger.info("responseEntity:{}", JSON.toJSONString(responseEntity));
/* 120 */     if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
/* 121 */       throw new BusinessError(JSON.toJSONString(responseEntity), BaseStatusCode.REMOTE_ERROR);
/*     */     }
/* 123 */     return (T)responseEntity.getBody();
/*     */   }
/*     */   
/*     */   public static <T> void asynPost(String url, Object data, ParameterizedTypeReference<T> typeReference) {
/* 127 */     ThreadPoolTaskExecutor threadPoolTaskExecutor = (ThreadPoolTaskExecutor)AppUtil.getBean("taskExecutor");
/* 128 */     HttpHeaders headers = new HttpHeaders();
/* 129 */     HttpServletRequest request = RequestContext.getHttpServletRequest();
/* 130 */     JWTService jwtService = AppUtil.<JWTService>getBean(JWTService.class);
/* 131 */     String authHeader = request.getHeader(jwtService.getJwtHeader());
/*     */     
/* 133 */     if (StringUtil.isEmpty(authHeader)) {
/* 134 */       authHeader = CookieUitl.getValueByName(jwtService.getJwtHeader(), request);
/* 135 */       headers.add("Cookie", jwtService.getJwtHeader() + "=" + authHeader);
/*     */     } else {
/* 137 */       headers.add(jwtService.getJwtHeader(), authHeader);
/*     */     } 
/* 139 */     HttpEntity<Object> entity = new HttpEntity(data, (MultiValueMap)headers);
/* 140 */     String urlTemp = RequestContext.getUrl(url);
/* 141 */     logger.info("entity: {}", JSON.toJSONString(entity));
/* 142 */     logger.info("url:{}", urlTemp);
/* 143 */     threadPoolTaskExecutor.execute(() -> {
/*     */           ResponseEntity<T> responseEntity = restTemplate().exchange(url, HttpMethod.POST, entity, typeReference, new Object[0]);
/*     */           logger.info("responseEntity:{}", JSON.toJSONString(responseEntity));
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T> T get(String url, Map<String, String> heads, Class<T> responseType) {
/* 151 */     HttpHeaders headers = new HttpHeaders();
/* 152 */     if (MapUtil.isNotEmpty(heads)) {
/* 153 */       for (Map.Entry<String, String> param : heads.entrySet()) {
/* 154 */         headers.add(param.getKey(), param.getValue());
/*     */       }
/*     */     }
/*     */     
/* 158 */     HttpEntity<Object> entity = new HttpEntity(null, (MultiValueMap)headers);
/* 159 */     String urlTemp = RequestContext.getUrl(url);
/* 160 */     logger.info("entity: {}", JSON.toJSONString(entity));
/* 161 */     logger.info("url:{}", urlTemp);
/* 162 */     ResponseEntity<T> responseEntity = restTemplate().exchange(url, HttpMethod.GET, entity, responseType, new Object[0]);
/* 163 */     logger.info("responseEntity:{}", JSON.toJSONString(responseEntity));
/* 164 */     if (!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
/* 165 */       throw new BusinessError(JSON.toJSONString(responseEntity), BaseStatusCode.REMOTE_ERROR);
/*     */     }
/*     */     
/* 168 */     return (T)responseEntity.getBody();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/util/RestTemplateUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */