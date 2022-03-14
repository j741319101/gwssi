package com.dstz.bpm.plugin.core.manager.impl;

import com.dstz.bpm.api.constant.OpinionStatus;
import com.dstz.bpm.plugin.core.dao.BpmUserAgencyLogDao;
import com.dstz.bpm.plugin.core.manager.BpmUserAgencyLogManager;
import com.dstz.bpm.plugin.core.model.BpmUserAgencyLog;
import com.dstz.bpm.plugin.vo.BpmUserAgencyLogVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.manager.impl.BaseManager;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.util.ContextUtil;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmUserAgencyLogManager")
public class BpmUserAgencyLogManagerImpl extends BaseManager<String, BpmUserAgencyLog> implements BpmUserAgencyLogManager {
   @Resource
   private BpmUserAgencyLogDao bpmUserAgencyLogDao;
   @Resource
   private UserService userService;

   public int insertSelective(BpmUserAgencyLog record) {
      return this.bpmUserAgencyLogDao.insertSelective(record);
   }

   public int updateByPrimaryKeySelective(BpmUserAgencyLog record) {
      return this.bpmUserAgencyLogDao.updateByPrimaryKeySelective(record);
   }

   public List<BpmUserAgencyLogVO> selectBpmUserAgencyLogList(QueryFilter queryFilter) {
      queryFilter.addFilter("config.CONFIG_USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
      return this.selectBpmAgencyLogList(queryFilter);
   }

   public List<BpmUserAgencyLogVO> selectBpmTargetUserAgencyLogList(QueryFilter queryFilter) {
      queryFilter.addFilter("config.TARGET_USER_ID_", ContextUtil.getCurrentUserId(), QueryOP.EQUAL);
      return this.selectBpmAgencyLogList(queryFilter);
   }

   private List<BpmUserAgencyLogVO> selectBpmAgencyLogList(QueryFilter queryFilter) {
      List<BpmUserAgencyLogVO> list = this.bpmUserAgencyLogDao.selectBpmUserAgencyLogList(queryFilter);
      list.forEach((logVo) -> {
         IUser user = this.userService.getUserById(logVo.getConfigUserId());
         if (null == user) {
            logVo.setConfigUserName("用户丢失");
         } else {
            logVo.setConfigUserName(user.getFullname());
         }

         if (OpinionStatus.AWAITING_CHECK.getKey().equals(logVo.getApproveStatus()) && StringUtil.isNotEmpty(logVo.getAssignInfo())) {
            StringBuilder assignNames = new StringBuilder();
            String[] assingInfoArr = logVo.getAssignInfo().split(",");
            String[] var5 = assingInfoArr;
            int var6 = assingInfoArr.length;

            for(int var7 = 0; var7 < var6; ++var7) {
               String assingnInfo = var5[var7];
               if (StringUtil.isEmpty(assingnInfo)) {
                  return;
               }

               String[] userInfo = assingnInfo.split("-");
               if (userInfo.length > 2) {
                  if (assignNames.length() > 0) {
                     assignNames.append(",");
                  }

                  assignNames.append(userInfo[1]);
               }
            }

            logVo.setApproverName(assignNames.toString());
         }

      });
      return list;
   }
}
