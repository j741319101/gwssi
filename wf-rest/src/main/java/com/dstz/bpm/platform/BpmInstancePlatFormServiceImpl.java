package com.dstz.bpm.platform;

import com.dstz.bpm.api.engine.action.cmd.FlowRequestParam;
import com.dstz.bpm.api.model.inst.IBpmInstance;
import com.dstz.bpm.api.model.task.IBpmTaskApprove;
import com.dstz.bpm.api.platform.IBpmInstancePlatFormService;
import com.dstz.bpm.api.service.BpmImageService;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
import com.dstz.base.api.Page;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.api.query.QueryOP;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.db.model.page.PageResult;
import com.dstz.base.db.model.query.DefaultPage;
import com.dstz.base.db.model.query.DefaultQueryFilter;
import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Resource;

import com.dstz.bpm.service.BpmSomeService;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BpmInstancePlatFormServiceImpl implements IBpmInstancePlatFormService {
   @Resource
   private BpmInstanceManager bpmInstanceManager;
   @Resource
   private BpmDefinitionManager bpmDefinitionManager;
   @Resource
   private BpmSomeService bpmSomeService;
   @Resource
   private BpmDefinitionManager bpmDefinitionMananger;
   @Resource
   private BpmImageService bpmImageService;

   public IBpmInstance getBpmInstance(String instId) {
      return (IBpmInstance)this.bpmInstanceManager.get(instId);
   }

   public List<IBpmInstance> getBpmInstanceByParentId(String instId) {
      List<BpmInstance> bpmInstances = this.bpmInstanceManager.getByParentId(instId);
      List<IBpmInstance> iBpmInstances = new ArrayList();
      if (CollectionUtil.isNotEmpty(bpmInstances)) {
         bpmInstances.forEach((bpmInstance) -> {
            iBpmInstances.add(bpmInstance);
         });
      }

      return iBpmInstances;
   }

   public ResponseEntity<byte[]> flowImage(String instId, String defId) {
      String actInstId = null;
      String actDefId;
      if (StringUtil.isNotEmpty(instId)) {
         BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
         actInstId = inst.getActInstId();
         actDefId = inst.getActDefId();
      } else {
         BpmDefinition def = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
         actDefId = def.getActDefId();
      }

      ByteArrayOutputStream swapStream = new ByteArrayOutputStream();

      try {
         InputStream inputStream = this.bpmImageService.draw(actDefId, actInstId);
         byte[] buff = new byte[100];
         boolean var8 = false;

         int rc;
         while((rc = inputStream.read(buff, 0, 100)) > 0) {
            swapStream.write(buff, 0, rc);
         }
      } catch (Exception var9) {
         ;
      }

      byte[] in2b = swapStream.toByteArray();
      return new ResponseEntity(in2b, HttpStatus.OK);
   }

   public JSONObject getInstanceOpinionStruct(String instId) {
      BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
      if (null != inst) {
         String defId = inst.getDefId();
         BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(defId);
         if (null != bpmDefinition) {
            String defSetting = bpmDefinition.getDefSetting();
            JSONObject jsonObject = JSON.parseObject(defSetting);
            JSONObject flowObject = (JSONObject)jsonObject.get("flow");
            JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
            JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
            return opinionObject;
         }
      }

      return null;
   }

   public JSONObject getFlowImageInfo(String instanceId, String defId, String taskId) {
      return this.bpmSomeService.getFlowImageInfo(instanceId, defId, taskId);
   }

   public String doAction(FlowRequestParam flowParam, String userId) {
      DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
      return instanceCmd.executeCmd();
   }

   public PageResult<List<IBpmTaskApprove>> getApproveHistoryList(String userId, String instId, String typeId, Integer offset, Integer limit, String sort, String order, String noPage, String defKey) {
      if (StringUtils.isEmpty(userId)) {
         throw new BusinessMessage("用户id不能为空");
      } else {
         QueryFilter queryFilter = new DefaultQueryFilter();
         if (StringUtils.isNotEmpty(noPage)) {
            queryFilter.setPage((Page)null);
         } else if (limit != null && offset != null) {
            RowBounds rowBounds = new RowBounds(offset, limit);
            DefaultPage page = new DefaultPage(rowBounds);
            queryFilter.setPage(page);
         }

         if (StringUtils.isNotEmpty(sort) && StringUtils.isNotEmpty(order)) {
            queryFilter.addFieldSort(sort, order);
         }

         if (StringUtils.isNotEmpty(instId)) {
            queryFilter.addFilter("inst.id_", instId, QueryOP.IN);
         }

         if (StringUtils.isNotEmpty(typeId)) {
            queryFilter.addFilter("inst.type_id_", typeId, QueryOP.EQUAL);
         }

         if (StringUtils.isNotEmpty(defKey)) {
            queryFilter.addFilter("inst.def_key_", defKey, QueryOP.EQUAL);
         }

         return new PageResult(this.bpmInstanceManager.getApproveHistoryList(userId, queryFilter));
      }
   }
}
