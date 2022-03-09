/*    */ package cn.gwssi.ecloudbpm.activiti.rest.editor.conf;
/*    */ 
/*    */ import com.fasterxml.jackson.databind.ObjectMapper;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public class JacksonConfiguration
/*    */ {
/*    */   @Bean
/*    */   public ObjectMapper objectMapper() {
/* 16 */     ObjectMapper mapper = new ObjectMapper();
/* 17 */     return mapper;
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/activiti/rest/editor/conf/JacksonConfiguration.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */