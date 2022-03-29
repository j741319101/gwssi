package com.dstz.bpm.platform;

import com.dstz.bpm.api.model.def.IBpmDefinition;
import com.dstz.bpm.api.platform.IBpmDefinitionPlatFormService;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.db.model.query.DefaultPage;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.UserService;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Service;

@Service
public class BpmDefinitionPlatFormServiceImpl implements IBpmDefinitionPlatFormService {
   @Resource
   private BpmDefinitionManager bpmDefinitionManager;
   @Resource
   private UserService userService;

   public PageResult<List<IBpmDefinition>> listJson(Integer offset, Integer limit, String defId, String defKey) {
      QueryFilter queryFilter = new DefaultQueryFilter();
      RowBounds rowBounds = new RowBounds(offset, limit);
      DefaultPage page = new DefaultPage(rowBounds);
      queryFilter.setPage(page);
      if (StringUtils.isNotEmpty(defId)) {
         queryFilter.addFilter("id_", defId, QueryOP.EQUAL);
      }

      if (StringUtils.isNotEmpty(defKey)) {
         queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
         queryFilter.addFilter("key_", defKey, QueryOP.EQUAL);
      }

      return new PageResult(this.bpmDefinitionManager.query(queryFilter));
   }

   public PageResult<List<IBpmDefinition>> listMyJson(Integer offset, Integer limit, String defId, String defKey, String userId) {
      IUser user = this.userService.getUserById(userId);
      QueryFilter queryFilter = new DefaultQueryFilter();
      RowBounds rowBounds = new RowBounds(offset, limit);
      DefaultPage page = new DefaultPage(rowBounds);
      queryFilter.setPage(page);
      if (StringUtils.isNotEmpty(defId)) {
         queryFilter.addFilter("id_", defId, QueryOP.EQUAL);
      }

      if (StringUtils.isNotEmpty(defKey)) {
         queryFilter.addFilter("is_main_", "Y", QueryOP.EQUAL);
         queryFilter.addFilter("key_", defKey, QueryOP.EQUAL);
      }

      List<String> lstOrgId = new ArrayList();
      if (null != user) {
         lstOrgId = user.getManagerGroupIdList();
      }

      if (null != lstOrgId && ((List)lstOrgId).size() > 0) {
         queryFilter.addFilter("bpm_definition.org_id_", lstOrgId, QueryOP.IN);
      } else {
         queryFilter.addFilter("bpm_definition.org_id_", "", QueryOP.EQUAL);
      }

      return new PageResult(this.bpmDefinitionManager.getMyDefinitionList(userId, queryFilter));
   }
}
