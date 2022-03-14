/*    */ package cn.gwssi.ecloudbpm.platform;
/*    */ 
/*    */ import com.dstz.bpm.api.constant.CarbonCopyStatus;
/*    */ import com.dstz.bpm.api.platform.IBpmCarbonCopyPlatFormService;
/*    */ import com.dstz.bpm.plugin.core.manager.BpmCarbonCopyReceiveManager;
/*    */ import com.dstz.bpm.plugin.core.model.BpmCarbonCopyReceive;
/*    */ import java.util.Arrays;
/*    */ import java.util.Date;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import javax.annotation.Resource;
/*    */ import org.apache.commons.lang3.StringUtils;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ @Service
/*    */ public class BpmCarbonCopyPlatFormServiceImpl
/*    */   implements IBpmCarbonCopyPlatFormService
/*    */ {
/*    */   @Resource
/*    */   private BpmCarbonCopyReceiveManager bpmCarbonCopyReceiveManager;
/*    */   
/*    */   public String updateRead(String id) {
/* 23 */     Set<String> ids = (Set<String>)Arrays.<String>stream(StringUtils.split(id, ",")).collect(Collectors.toSet());
/* 24 */     BpmCarbonCopyReceive bpmCarbonCopyReceive = new BpmCarbonCopyReceive();
/* 25 */     bpmCarbonCopyReceive.setStatus(CarbonCopyStatus.READ.getKey());
/* 26 */     bpmCarbonCopyReceive.setUpdateTime(new Date());
/* 27 */     this.bpmCarbonCopyReceiveManager.updateRead(bpmCarbonCopyReceive, ids);
/* 28 */     return "更新成功";
/*    */   }
/*    */ 
/*    */   
/*    */   public String updateReadUser(String userId) {
/* 33 */     this.bpmCarbonCopyReceiveManager.updateReadByUser(userId);
/* 34 */     return "更新成功";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/platform/BpmCarbonCopyPlatFormServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */