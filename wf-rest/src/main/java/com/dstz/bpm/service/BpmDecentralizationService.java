package com.dstz.bpm.service;

import com.dstz.org.api.context.ICurrentContext;
import com.dstz.org.api.model.IUser;
import com.dstz.sys.api.platform.ISysPropertiesPlatFormService;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class BpmDecentralizationService {
   private Logger logger = LoggerFactory.getLogger(this.getClass());
   @Resource
   ICurrentContext iCurrentContext;
   @Resource
   ISysPropertiesPlatFormService iSysPropertiesPlatFormService;

   public boolean decentralizationEnable(String type) {
      IUser user = this.iCurrentContext.getCurrentUser();
      boolean ifDecentralization = false;
      if (null == user || !this.iCurrentContext.isAdmin(user)) {
         try {
            String decentralizationEnable = this.iSysPropertiesPlatFormService.getByAlias("decentralization.enable");
            if ("true".equals(decentralizationEnable)) {
               String decentralizationOrgEnable = this.iSysPropertiesPlatFormService.getByAlias("decentralization." + type + ".enable");
               if ("true".equals(decentralizationOrgEnable)) {
                  ifDecentralization = true;
               }
            }
         } catch (Exception var6) {
            this.logger.error("获取系统属性失败", var6);
         }
      }

      return ifDecentralization;
   }
}
