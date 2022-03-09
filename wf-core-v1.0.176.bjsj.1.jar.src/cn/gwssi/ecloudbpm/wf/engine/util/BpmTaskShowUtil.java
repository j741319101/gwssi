/*     */ package cn.gwssi.ecloudbpm.wf.engine.util;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.nodedef.BpmNodeDef;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.BpmOrgDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.model.dto.BpmUserDTO;
/*     */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.groovy.IGroovyScriptEngine;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ public class BpmTaskShowUtil
/*     */ {
/*     */   public static List<SysIdentity> appendOrgUser(List<SysIdentity> sysIdentities, BpmNodeDef bpmNodeDef) {
/*  22 */     return appendOrgUser(sysIdentities, bpmNodeDef, "--");
/*     */   }
/*     */ 
/*     */   
/*     */   public static List<SysIdentity> appendOrgUser(List<SysIdentity> sysIdentities, BpmNodeDef bpmNodeDef, String appendStr) {
/*  27 */     if (bpmNodeDef == null) {
/*  28 */       return sysIdentities;
/*     */     }
/*     */     
/*  31 */     if (CollectionUtil.isEmpty(sysIdentities)) {
/*  32 */       Collections.emptyList();
/*     */     }
/*     */     
/*  35 */     List<String> rules = bpmNodeDef.getNodeProperties().getShowTaskSysIdentityRule();
/*  36 */     if (CollectionUtil.isEmpty(rules)) {
/*  37 */       return sysIdentities;
/*     */     }
/*  39 */     if (rules.contains("other")) {
/*  40 */       IGroovyScriptEngine groovyScriptEngine = (IGroovyScriptEngine)AppUtil.getImplInstance(IGroovyScriptEngine.class).get(Integer.valueOf(0));
/*  41 */       sysIdentities.forEach(user -> {
/*     */             String str = groovyScriptEngine.executeString(((String)rules.get(0)).substring(6), null);
/*     */             user.setName(str);
/*     */           });
/*     */     } else {
/*  46 */       String compareStr = "user";
/*  47 */       AtomicBoolean userShowOrg = new AtomicBoolean(false);
/*  48 */       if (rules.contains("org")) {
/*  49 */         compareStr = "org";
/*     */       }
/*  51 */       if (rules.contains("parentOrg")) {
/*  52 */         compareStr = "parentOrg";
/*     */       }
/*  54 */       if (rules.contains("gsOrg")) {
/*  55 */         compareStr = "gsOrg";
/*     */       }
/*  57 */       if (rules.contains("user")) {
/*  58 */         compareStr = "user";
/*     */       }
/*  60 */       StringBuffer userIds = new StringBuffer();
/*  61 */       StringBuffer orgIds = new StringBuffer();
/*  62 */       AtomicBoolean needSn = new AtomicBoolean(false);
/*  63 */       sysIdentities.forEach(user -> {
/*     */             if (StringUtils.equals(user.getType(), "user")) {
/*     */               if (((DefaultIdentity)user).getSn().intValue() == -1) {
/*     */                 needSn.set(true);
/*     */               }
/*     */               
/*     */               if (rules.contains("org") || rules.contains("parentOrg") || rules.contains("gsOrg")) {
/*     */                 userShowOrg.set(true);
/*     */               }
/*     */               userIds.append(user.getId()).append(",");
/*     */             } else if (StringUtils.equals(user.getType(), "org")) {
/*     */               if (((DefaultIdentity)user).getSn().intValue() == -1) {
/*     */                 needSn.set(true);
/*     */               }
/*     */               orgIds.append(user.getId()).append(",");
/*     */             } 
/*     */           });
/*  80 */       if (!userShowOrg.get() && !needSn.get()) {
/*  81 */         return sysIdentities;
/*     */       }
/*  83 */       UserService userService = AppUtil.getImplInstanceArray(UserService.class).get(0);
/*  84 */       GroupService groupService = AppUtil.getImplInstanceArray(GroupService.class).get(0);
/*  85 */       if (userIds.length() > 0) {
/*  86 */         List<BpmUserDTO> bpmUserDTOS = userService.getUserOrgInfos(userIds.substring(0, userIds.length() - 1));
/*  87 */         if (null != bpmUserDTOS && bpmUserDTOS.size() > 0) {
/*  88 */           for (BpmUserDTO user : bpmUserDTOS) {
/*  89 */             String finalCompareStr = compareStr;
/*  90 */             sysIdentities.stream().filter(sysIdentity -> (StringUtils.equals(sysIdentity.getType(), "user") && StringUtils.equals(sysIdentity.getId(), user.getUserId()) && StringUtils.equals(sysIdentity.getOrgId(), user.getOrgId())))
/*     */               
/*  92 */               .forEach(sysIdentity -> {
/*     */                   if (StringUtils.equals(finalCompareStr, "user")) {
/*     */                     ((DefaultIdentity)sysIdentity).setSn(Integer.valueOf(user.getUserSn()));
/*     */                   }
/*     */ 
/*     */                   
/*     */                   if (StringUtils.equals(finalCompareStr, "org")) {
/*     */                     ((DefaultIdentity)sysIdentity).setSn(user.getOrgSn());
/*     */ 
/*     */                     
/*     */                     ((DefaultIdentity)sysIdentity).setCompareOrgId(user.getOrgId());
/*     */                   } 
/*     */ 
/*     */                   
/*     */                   if (StringUtils.equals(finalCompareStr, "parentOrg")) {
/*     */                     if (StringUtils.isEmpty(user.getParentOrgId())) {
/*     */                       ((DefaultIdentity)sysIdentity).setSn(user.getOrgSn());
/*     */ 
/*     */                       
/*     */                       ((DefaultIdentity)sysIdentity).setCompareOrgId(user.getOrgId());
/*     */                     } else {
/*     */                       ((DefaultIdentity)sysIdentity).setSn(user.getParentOrgSn());
/*     */ 
/*     */                       
/*     */                       ((DefaultIdentity)sysIdentity).setCompareOrgId(user.getParentOrgId());
/*     */                     } 
/*     */                   }
/*     */ 
/*     */                   
/*     */                   if (StringUtils.equals(finalCompareStr, "gsOrg")) {
/*     */                     if (StringUtils.equals(user.getOrgType(), "1")) {
/*     */                       ((DefaultIdentity)sysIdentity).setSn(user.getOrgSn());
/*     */ 
/*     */                       
/*     */                       ((DefaultIdentity)sysIdentity).setCompareOrgId(user.getOrgId());
/*     */                     } else {
/*     */                       ((DefaultIdentity)sysIdentity).setSn(user.getParentOrgSn());
/*     */ 
/*     */                       
/*     */                       ((DefaultIdentity)sysIdentity).setCompareOrgId(user.getParentOrgId());
/*     */                     } 
/*     */                   }
/*     */ 
/*     */                   
/*     */                   StringBuffer userName = new StringBuffer();
/*     */                   
/*     */                   rules.forEach(());
/*     */                   
/*     */                   if (userName.length() > 0) {
/*     */                     sysIdentity.setName(userName.substring(0, userName.length() - appendStr.length()));
/*     */                   }
/*     */                 });
/*     */           } 
/*     */         }
/*     */       } 
/*     */       
/* 148 */       if (orgIds.length() > 0) {
/* 149 */         List<BpmOrgDTO> bpmUserDTOS = groupService.getOrgInfos(orgIds.substring(0, orgIds.length() - 1));
/* 150 */         for (BpmOrgDTO user : bpmUserDTOS) {
/* 151 */           String finalCompareStr = compareStr;
/* 152 */           sysIdentities.stream().filter(sysIdentity -> StringUtils.equals(sysIdentity.getId(), user.getOrgId()))
/* 153 */             .forEach(sysIdentity -> {
/*     */                 if (StringUtils.equals(finalCompareStr, "org")) {
/*     */                   ((DefaultIdentity)sysIdentity).setSn(user.getOrgSn());
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 if (StringUtils.equals(finalCompareStr, "parentOrg")) {
/*     */                   if (StringUtils.isEmpty(user.getParentOrgId())) {
/*     */                     ((DefaultIdentity)sysIdentity).setSn(user.getOrgSn());
/*     */                   } else {
/*     */                     ((DefaultIdentity)sysIdentity).setSn(user.getParentOrgSn());
/*     */                   } 
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 if (StringUtils.equals(finalCompareStr, "gsOrg")) {
/*     */                   if (StringUtils.equals(user.getOrgType(), "1")) {
/*     */                     ((DefaultIdentity)sysIdentity).setSn(user.getOrgSn());
/*     */                   } else {
/*     */                     ((DefaultIdentity)sysIdentity).setSn(user.getParentOrgSn());
/*     */                   } 
/*     */                 }
/*     */ 
/*     */ 
/*     */                 
/*     */                 StringBuffer userName = new StringBuffer();
/*     */ 
/*     */ 
/*     */                 
/*     */                 rules.forEach(());
/*     */ 
/*     */ 
/*     */                 
/*     */                 if (userName.length() > 0) {
/*     */                   sysIdentity.setName(userName.substring(0, userName.length() - appendStr.length()));
/*     */                 }
/*     */               });
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 197 */     return sysIdentities;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/util/BpmTaskShowUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */