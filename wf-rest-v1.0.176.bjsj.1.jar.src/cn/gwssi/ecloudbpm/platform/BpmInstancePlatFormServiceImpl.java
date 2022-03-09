/*     */ package cn.gwssi.ecloudbpm.platform;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.service.BpmSomeService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.FlowRequestParam;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTaskApprove;
/*     */ import cn.gwssi.ecloudbpm.wf.api.platform.IBpmInstancePlatFormService;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmImageService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmDefinitionManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.manager.BpmInstanceManager;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmInstance;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import cn.gwssi.ecloudframework.base.api.Page;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultPage;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.ibatis.session.RowBounds;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service
/*     */ public class BpmInstancePlatFormServiceImpl
/*     */   implements IBpmInstancePlatFormService
/*     */ {
/*     */   @Resource
/*     */   private BpmInstanceManager bpmInstanceManager;
/*     */   @Resource
/*     */   private BpmDefinitionManager bpmDefinitionManager;
/*     */   @Resource
/*     */   private BpmSomeService bpmSomeService;
/*     */   @Resource
/*     */   private BpmDefinitionManager bpmDefinitionMananger;
/*     */   @Resource
/*     */   private BpmImageService bpmImageService;
/*     */   
/*     */   public IBpmInstance getBpmInstance(String instId) {
/*  52 */     return (IBpmInstance)this.bpmInstanceManager.get(instId);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IBpmInstance> getBpmInstanceByParentId(String instId) {
/*  57 */     List<BpmInstance> bpmInstances = this.bpmInstanceManager.getByParentId(instId);
/*  58 */     List<IBpmInstance> iBpmInstances = new ArrayList<>();
/*  59 */     if (CollectionUtil.isNotEmpty(bpmInstances)) {
/*  60 */       bpmInstances.forEach(bpmInstance -> iBpmInstances.add(bpmInstance));
/*     */     }
/*  62 */     return iBpmInstances;
/*     */   }
/*     */ 
/*     */   
/*     */   public ResponseEntity<byte[]> flowImage(String instId, String defId) {
/*  67 */     String actDefId, actInstId = null;
/*  68 */     if (StringUtil.isNotEmpty(instId)) {
/*  69 */       BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
/*  70 */       actInstId = inst.getActInstId();
/*  71 */       actDefId = inst.getActDefId();
/*     */     } else {
/*  73 */       BpmDefinition def = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
/*  74 */       actDefId = def.getActDefId();
/*     */     } 
/*  76 */     ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
/*     */     try {
/*  78 */       InputStream inputStream = this.bpmImageService.draw(actDefId, actInstId);
/*  79 */       byte[] buff = new byte[100];
/*  80 */       int rc = 0;
/*  81 */       while ((rc = inputStream.read(buff, 0, 100)) > 0) {
/*  82 */         swapStream.write(buff, 0, rc);
/*     */       }
/*  84 */     } catch (Exception exception) {}
/*     */ 
/*     */     
/*  87 */     byte[] in2b = swapStream.toByteArray();
/*  88 */     return new ResponseEntity(in2b, HttpStatus.OK);
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getInstanceOpinionStruct(String instId) {
/*  93 */     BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
/*  94 */     if (null != inst) {
/*  95 */       String defId = inst.getDefId();
/*  96 */       BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/*  97 */       if (null != bpmDefinition) {
/*  98 */         String defSetting = bpmDefinition.getDefSetting();
/*  99 */         JSONObject jsonObject = JSON.parseObject(defSetting);
/* 100 */         JSONObject flowObject = (JSONObject)jsonObject.get("flow");
/* 101 */         JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
/* 102 */         JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
/* 103 */         return opinionObject;
/*     */       } 
/*     */     } 
/* 106 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getFlowImageInfo(String instanceId, String defId, String taskId) {
/* 111 */     return this.bpmSomeService.getFlowImageInfo(instanceId, defId, taskId);
/*     */   }
/*     */ 
/*     */   
/*     */   public String doAction(FlowRequestParam flowParam, String userId) {
/* 116 */     DefaultInstanceActionCmd instanceCmd = new DefaultInstanceActionCmd(flowParam);
/* 117 */     return instanceCmd.executeCmd();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public PageResult<List<IBpmTaskApprove>> getApproveHistoryList(String userId, String instId, String typeId, Integer offset, Integer limit, String sort, String order, String noPage, String defKey) {
/* 123 */     if (StringUtils.isEmpty(userId)) {
/* 124 */       throw new BusinessMessage("用户id不能为空");
/*     */     }
/* 126 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
/* 127 */     if (StringUtils.isNotEmpty(noPage)) {
/* 128 */       defaultQueryFilter.setPage(null);
/* 129 */     } else if (limit != null && offset != null) {
/* 130 */       RowBounds rowBounds = new RowBounds(offset.intValue(), limit.intValue());
/* 131 */       DefaultPage page = new DefaultPage(rowBounds);
/* 132 */       defaultQueryFilter.setPage((Page)page);
/*     */     } 
/* 134 */     if (StringUtils.isNotEmpty(sort) && StringUtils.isNotEmpty(order)) {
/* 135 */       defaultQueryFilter.addFieldSort(sort, order);
/*     */     }
/* 137 */     if (StringUtils.isNotEmpty(instId)) {
/* 138 */       defaultQueryFilter.addFilter("inst.id_", instId, QueryOP.IN);
/*     */     }
/* 140 */     if (StringUtils.isNotEmpty(typeId)) {
/* 141 */       defaultQueryFilter.addFilter("inst.type_id_", typeId, QueryOP.EQUAL);
/*     */     }
/* 143 */     if (StringUtils.isNotEmpty(defKey)) {
/* 144 */       defaultQueryFilter.addFilter("inst.def_key_", defKey, QueryOP.EQUAL);
/*     */     }
/* 146 */     return new PageResult(this.bpmInstanceManager.getApproveHistoryList(userId, (QueryFilter)defaultQueryFilter));
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/platform/BpmInstancePlatFormServiceImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */