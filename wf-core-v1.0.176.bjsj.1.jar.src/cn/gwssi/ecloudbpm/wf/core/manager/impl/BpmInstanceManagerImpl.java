/*     */ package com.dstz.bpm.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessDataService;
/*     */ import cn.gwssi.ecloudbpm.bus.api.service.IBusinessObjectService;
/*     */ import com.dstz.bpm.act.service.ActInstanceService;
/*     */ import com.dstz.bpm.api.constant.EventType;
/*     */ import com.dstz.bpm.api.constant.InstanceStatus;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.cmd.InstanceActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.engine.context.BpmContext;
/*     */ import com.dstz.bpm.api.engine.plugin.cmd.ExecutionCommand;
/*     */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.inst.IBpmInstance;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.dao.BpmInstanceDao;
/*     */ import com.dstz.bpm.core.manager.BpmBusLinkManager;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskOpinionManager;
/*     */ import com.dstz.bpm.core.manager.BpmTaskStackManager;
/*     */ import com.dstz.bpm.core.manager.TaskIdentityLinkManager;
/*     */ import com.dstz.bpm.core.model.BpmBusLink;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.model.BpmTask;
/*     */ import com.dstz.bpm.core.model.BpmTaskApprove;
/*     */ import com.dstz.bpm.core.model.BpmTypeTreeCountVO;
/*     */ import com.dstz.bpm.core.vo.BpmInstanceVO;
/*     */ import com.dstz.bpm.core.vo.BpmTaskApproveVO;
/*     */ import com.dstz.bpm.engine.action.cmd.DefaultInstanceActionCmd;
/*     */ import com.dstz.bpm.engine.data.handle.IBpmBusDataHandle;
/*     */ import com.dstz.base.api.query.FieldRelation;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.query.WhereClause;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMapUtil;
/*     */ import com.dstz.base.db.model.query.DefaultFieldLogic;
/*     */ import com.dstz.base.db.model.query.DefaultQueryField;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.model.dto.UserDTO;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import com.dstz.sys.util.DateUtil;
/*     */ import com.dstz.sys.util.ExeclUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service("bpmInstanceManager")
/*     */ public class BpmInstanceManagerImpl
/*     */   extends BaseManager<String, BpmInstance>
/*     */   implements BpmInstanceManager
/*     */ {
/*     */   @Resource
/*     */   BpmInstanceDao bpmInstanceDao;
/*     */   @Autowired
/*     */   BpmTaskManager bpmTaskManager;
/*     */   @Autowired
/*     */   ActInstanceService actInstanceService;
/*     */   @Autowired
/*     */   BpmTaskOpinionManager bpmTaskOpinionManager;
/*     */   @Autowired
/*     */   BpmTaskStackManager bpmTaskStackManager;
/*     */   @Autowired
/*     */   BpmBusLinkManager bpmBusLinkManager;
/*     */   
/*     */   public boolean isSuspendByInstId(String instId) {
/*  92 */     return false; } @Autowired IBusinessDataService businessDataService; @Autowired IBusinessObjectService businessObjectService; @Resource
/*     */   BpmDefinitionManager bpmDefinitionMananger; @Resource
/*     */   IBpmBusDataHandle bpmBusDataHandle; @Autowired
/*     */   private BpmProcessDefService bpmProcessDefService; @Resource
/*     */   private ExecutionCommand executionCommand; @Resource
/*  97 */   private TaskIdentityLinkManager taskIdentityLinkManager; public List<BpmInstance> getApplyList(String userId, QueryFilter queryFilter) { queryFilter.addParamsFilter("userId", userId);
/*  98 */     return this.bpmInstanceDao.getApplyList(queryFilter); }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmTaskApprove> getApproveHistoryList(String userId, QueryFilter queryFilter) {
/* 103 */     queryFilter.addParamsFilter("approver", userId);
/* 104 */     return this.bpmInstanceDao.getApproveHistoryList(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTaskApproveVO> getApproveInstHistoryList(QueryFilter queryFilter) {
/* 109 */     queryFilter.addParamsFilter("approver", ContextUtil.getCurrentUserId());
/* 110 */     List<BpmTaskApproveVO> bpmTaskApproveDocVOS = this.bpmInstanceDao.getApproveInstHistoryList(queryFilter);
/* 111 */     bpmTaskApproveDocVOS.forEach(app -> {
/*     */           String str = app.getNodeName();
/*     */           if (StringUtils.isNotEmpty(str)) {
/*     */             String[] strs = str.split("@");
/*     */             if (strs.length == 3) {
/*     */               app.setDurMs(Long.valueOf(strs[0]));
/*     */               app.setApproveStatus(strs[1]);
/*     */               app.setApproveTime(DateUtil.convertStr(strs[2]));
/*     */               app.setNodeName("");
/*     */             } 
/*     */           } 
/*     */         });
/* 123 */     return bpmTaskApproveDocVOS;
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmInstance getTopInstance(BpmInstance instance) {
/* 128 */     if (instance == null || StringUtil.isZeroEmpty(instance.getParentInstId())) {
/* 129 */       return null;
/*     */     }
/* 131 */     BpmInstance parentInstance = (BpmInstance)get(instance.getParentInstId());
/*     */     
/* 133 */     BpmInstance topInstance = getTopInstance(parentInstance);
/* 134 */     if (topInstance != null) {
/* 135 */       return topInstance;
/*     */     }
/*     */     
/* 138 */     return parentInstance;
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmInstance genInstanceByDefinition(IBpmDefinition bpmDefinition) {
/*     */     UserDTO userDTO;
/* 144 */     BpmInstance instance = new BpmInstance();
/* 145 */     instance.setId(IdUtil.getSuid());
/* 146 */     instance.setSubject(bpmDefinition.getName());
/*     */     
/* 148 */     instance.setDefId(bpmDefinition.getId());
/* 149 */     instance.setTypeId(bpmDefinition.getTypeId());
/* 150 */     instance.setDefKey(bpmDefinition.getKey());
/* 151 */     instance.setActDefId(bpmDefinition.getActDefId());
/* 152 */     instance.setDefName(bpmDefinition.getName());
/* 153 */     instance.setStatus(InstanceStatus.STATUS_RUNNING.getKey());
/*     */     
/* 155 */     IUser user = ContextUtil.getCurrentUser();
/* 156 */     if (user == null) {
/* 157 */       userDTO = new UserDTO();
/* 158 */       userDTO.setFullname("系统");
/* 159 */       userDTO.setUserId("system");
/*     */     } 
/* 161 */     instance.setCreateBy(userDTO.getUserId());
/* 162 */     instance.setCreator(userDTO.getFullname());
/* 163 */     instance.setCreateTime(new Date());
/*     */     
/* 165 */     instance.setSupportMobile(bpmDefinition.getSupportMobile());
/*     */     
/* 167 */     instance.setParentInstId("0");
/*     */     
/* 169 */     if (bpmDefinition.getStatus().equals("deploy")) {
/* 170 */       instance.setIsFormmal("Y");
/* 171 */     } else if (bpmDefinition.getStatus().equals("draft")) {
/* 172 */       instance.setIsFormmal("N");
/*     */     } 
/*     */     
/* 175 */     instance.setHasCreate(Boolean.valueOf(false));
/* 176 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmInstance> getByPId(String parentInstId) {
/* 181 */     return this.bpmInstanceDao.getByPId(parentInstId);
/*     */   }
/*     */ 
/*     */   
/*     */   public BpmInstance getByActInstId(String actInstId) {
/* 186 */     return this.bpmInstanceDao.getByActInstId(actInstId);
/*     */   }
/*     */ 
/*     */   
/*     */   public void delete(String arrIds) {
/* 191 */     if (StringUtil.isEmpty(arrIds))
/*     */       return; 
/* 193 */     String[] ids = arrIds.split(",");
/* 194 */     for (String id : ids) {
/* 195 */       if (!StringUtil.isEmpty(id)) {
/*     */         
/* 197 */         BpmInstance inst = (BpmInstance)get(id);
/* 198 */         if (inst != null) {
/*     */           
/* 200 */           BpmInstance topInst = getTopInstance(inst);
/* 201 */           if (topInst != null) {
/* 202 */             delete(topInst);
/*     */           } else {
/* 204 */             delete(inst);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */   private void delete(BpmInstance instance) {
/* 211 */     String instId = instance.getId();
/* 212 */     remove(instId);
/* 213 */     this.bpmTaskManager.removeByInstId(instId);
/* 214 */     this.taskIdentityLinkManager.removeByInstId(instId);
/* 215 */     this.bpmTaskOpinionManager.removeByInstId(instId);
/* 216 */     this.bpmTaskStackManager.removeByInstanceId(instId);
/*     */     
/* 218 */     List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/* 219 */     extendTaskActions.forEach(iExtendTaskAction -> iExtendTaskAction.deleteDataByInstId(instId));
/*     */     
/* 221 */     InstanceActionCmd instanceActionCmd = getInstCmd(instance);
/* 222 */     BpmContext.setActionModel((ActionCmd)instanceActionCmd);
/* 223 */     this.executionCommand.execute(EventType.DELETE_EVENT, instanceActionCmd);
/*     */     
/* 225 */     List<BpmBusLink> links = this.bpmBusLinkManager.getByInstanceId(instId);
/* 226 */     for (BpmBusLink link : links) {
/* 227 */       IBusinessObject bo = this.businessObjectService.getFilledByKey(link.getBizCode());
/* 228 */       if (bo == null) {
/*     */         continue;
/*     */       }
/* 231 */       this.businessDataService.removeData(bo, link.getBizId());
/* 232 */       this.bpmBusLinkManager.remove(link.getId());
/*     */     } 
/*     */ 
/*     */     
/* 236 */     ThreadMapUtil.put("EcloudBPMDeleteInstance", Boolean.valueOf(true));
/* 237 */     if (StringUtil.isNotEmpty(instance.getActInstId()) && this.actInstanceService.getProcessInstance(instance.getActInstId()) != null) {
/* 238 */       this.actInstanceService.deleteProcessInstance(instance.getActInstId(), String.format("用户：%s[%s]执行删除实例", new Object[] { ContextUtil.getCurrentUser().getFullname(), ContextUtil.getCurrentUser().getAccount() }));
/*     */     }
/*     */ 
/*     */     
/* 242 */     List<BpmInstance> subInsts = getByPId(instId);
/* 243 */     for (BpmInstance subInst : subInsts) {
/* 244 */       delete(subInst);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmInstance> getByParentId(String id) {
/* 250 */     return this.bpmInstanceDao.getByPId(id);
/*     */   }
/*     */ 
/*     */   
/*     */   public void toForbidden(String instId, boolean forbidden) {
/* 255 */     List<BpmInstance> bpmInstanceList = listParentAndSubById(instId);
/* 256 */     if (CollectionUtils.isNotEmpty(bpmInstanceList)) {
/* 257 */       Date updateTime = new Date();
/* 258 */       String updateBy = ContextUtil.getCurrentUserId();
/* 259 */       for (BpmInstance bpmInstance : bpmInstanceList) {
/* 260 */         bpmInstance.setUpdateBy(updateBy);
/* 261 */         bpmInstance.setUpdateTime(updateTime);
/* 262 */         bpmInstance.setIsForbidden(forbidden ? BpmInstance.INSTANCE_FORBIDDEN : BpmInstance.INSTANCE_NO_FORBIDDEN);
/* 263 */         this.bpmInstanceDao.update(bpmInstance);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmInstance> listParentAndSubById(String instId) {
/* 270 */     BpmInstance bpmInstance = (BpmInstance)this.bpmInstanceDao.get(instId);
/* 271 */     if (bpmInstance == null) {
/* 272 */       return new ArrayList<>();
/*     */     }
/* 274 */     List<BpmInstance> bpmInstanceList = new ArrayList<>();
/* 275 */     bpmInstanceList.add(bpmInstance);
/* 276 */     getParentAndSiblingsInst(bpmInstanceList, bpmInstanceList);
/* 277 */     getChildrenInst(bpmInstanceList, bpmInstanceList);
/* 278 */     return bpmInstanceList;
/*     */   }
/*     */   
/*     */   private void getParentAndSiblingsInst(List<BpmInstance> insts, List<BpmInstance> foundInsts) {
/* 282 */     List<String> toFindInstIds = new ArrayList<>();
/* 283 */     insts.stream().filter(inst -> 
/* 284 */         (StringUtils.isNotEmpty(inst.getParentInstId()) && !StringUtils.equals(inst.getParentInstId(), "0") && !toFindInstIds.contains(inst.getParentInstId())))
/*     */       
/* 286 */       .forEach(foundInst -> toFindInstIds.add(foundInst.getParentInstId()));
/*     */     
/* 288 */     if (CollectionUtils.isEmpty(toFindInstIds)) {
/*     */       return;
/*     */     }
/* 291 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 292 */     List<WhereClause> whereClauses = defaultQueryFilter.getFieldLogic().getWhereClauses();
/* 293 */     DefaultFieldLogic orFieldLogic = new DefaultFieldLogic();
/* 294 */     orFieldLogic.setFieldRelation(FieldRelation.OR);
/* 295 */     orFieldLogic.getWhereClauses().add(new DefaultQueryField("id_", QueryOP.IN, toFindInstIds));
/* 296 */     orFieldLogic.getWhereClauses().add(new DefaultQueryField("parent_inst_id_", QueryOP.IN, toFindInstIds));
/* 297 */     whereClauses.add(orFieldLogic);
/* 298 */     List<BpmInstance> foundParentInst = this.bpmInstanceDao.query((QueryFilter)defaultQueryFilter);
/* 299 */     if (CollectionUtils.isEmpty(foundParentInst)) {
/*     */       return;
/*     */     }
/* 302 */     for (int i = 0; i < foundParentInst.size(); i++) {
/* 303 */       Iterator<BpmInstance> iterator = foundInsts.iterator(); while (true) { if (iterator.hasNext()) { BpmInstance fInst = iterator.next();
/* 304 */           if (StringUtils.equals(fInst.getId(), ((BpmInstance)foundParentInst.get(i)).getId())) {
/* 305 */             foundParentInst.remove(i);
/* 306 */             i--; break;
/*     */           } 
/*     */           continue; }
/*     */         
/* 310 */         foundInsts.add(foundParentInst.get(i)); break; }
/*     */     
/* 312 */     }  getParentAndSiblingsInst(foundParentInst, foundInsts);
/*     */   }
/*     */   
/*     */   private void getChildrenInst(List<BpmInstance> insts, List<BpmInstance> foundInsts) {
/* 316 */     List<String> toFindInstIds = new ArrayList<>();
/* 317 */     insts.stream().filter(inst -> !toFindInstIds.contains(inst.getParentInstId())).forEach(foundInst -> toFindInstIds.add(foundInst.getId()));
/*     */     
/* 319 */     if (CollectionUtils.isEmpty(toFindInstIds)) {
/*     */       return;
/*     */     }
/* 322 */     DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter(true);
/* 323 */     defaultQueryFilter.addFilter("parent_inst_id_", toFindInstIds, QueryOP.IN);
/* 324 */     List<BpmInstance> foundParentInst = this.bpmInstanceDao.query((QueryFilter)defaultQueryFilter);
/* 325 */     if (CollectionUtils.isEmpty(foundParentInst)) {
/*     */       return;
/*     */     }
/* 328 */     foundParentInst.stream().filter(inst -> !foundInsts.contains(inst)).forEach(inst -> foundInsts.add(inst));
/* 329 */     getChildrenInst(foundParentInst, foundInsts);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<Map> getInstNum() {
/* 335 */     return this.bpmInstanceDao.getInstNum();
/*     */   }
/*     */ 
/*     */   
/*     */   public int updateStatus(String ids, String status) {
/* 340 */     if (StringUtil.isEmpty(ids)) {
/* 341 */       return 0;
/*     */     }
/* 343 */     return this.bpmInstanceDao.updateStatus(Arrays.asList(ids.split(",")), status, ContextUtil.getCurrentUserId());
/*     */   }
/*     */ 
/*     */   
/*     */   public Map getInstIdByBusId(String busId) {
/* 348 */     return this.bpmInstanceDao.getInstIdByBusId(busId);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BpmInstance createInstanceByExecution(ExecutionEntity executionEntity) {
/* 354 */     BpmDefinition bpmDefinition = this.bpmDefinitionMananger.getByKey(executionEntity.getProcessDefinitionKey());
/*     */     
/* 356 */     BpmInstance instance = genInstanceByDefinition((IBpmDefinition)bpmDefinition);
/*     */     
/* 358 */     instance.setActInstId(executionEntity.getProcessInstanceId());
/* 359 */     instance.setBizKey(executionEntity.getBusinessKey());
/* 360 */     instance.setHasCreate(Boolean.valueOf(true));
/*     */     
/* 362 */     create((Serializable)instance);
/* 363 */     return instance;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTypeTreeCountVO> getApproveHistoryListTypeCount(String userId, QueryFilter queryFilter) {
/* 368 */     queryFilter.addParamsFilter("approver", userId);
/* 369 */     queryFilter.setPage(null);
/* 370 */     return this.bpmInstanceDao.getApproveHistoryListTypeCount(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmTypeTreeCountVO> applyTaskListTypeCount(String userId, QueryFilter queryFilter) {
/* 375 */     queryFilter.addParamsFilter("userId", userId);
/* 376 */     queryFilter.setPage(null);
/* 377 */     return this.bpmInstanceDao.getApplyListTypeCount(queryFilter);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BpmInstanceVO> listInstTaskJson(QueryFilter queryFilter) {
/* 382 */     List<BpmInstanceVO> bpmInstanceVOS = this.bpmInstanceDao.listInstTaskJson(queryFilter);
/* 383 */     bpmInstanceVOS.forEach(bpmInstanceVO -> {
/*     */           String taskInfo = bpmInstanceVO.getTaskInfo();
/*     */           if (StringUtils.isNotEmpty(taskInfo)) {
/*     */             String[] tasks = taskInfo.split("￥,");
/*     */             List<BpmTask> bpmTasks = new ArrayList<>();
/*     */             for (String task : tasks) {
/*     */               String[] info = task.split("@");
/*     */               BpmTask bpmTask = new BpmTask();
/*     */               bpmTask.setId(info[0]);
/*     */               bpmTask.setName(info[1]);
/*     */               bpmTask.setAssigneeNames(info[2]);
/*     */               bpmTask.setCreateTime((Date)DateUtil.parse(info[3], "yyyy-MM-dd hh:mm:ss"));
/*     */               bpmTask.setTaskType(info[4]);
/*     */               if (StringUtil.isNotEmpty(info[5]) && info[5].endsWith("￥")) {
/*     */                 bpmTask.setStatus(info[5].substring(0, info[5].length() - 1));
/*     */               } else {
/*     */                 bpmTask.setStatus(info[5]);
/*     */               } 
/*     */               bpmTasks.add(bpmTask);
/*     */             } 
/*     */             bpmInstanceVO.setBpmTasks(bpmTasks);
/*     */             bpmInstanceVO.setTaskInfo("");
/*     */           } 
/*     */         });
/* 407 */     return bpmInstanceVOS;
/*     */   }
/*     */ 
/*     */   
/*     */   public void listInstTaskJsonToExcel(QueryFilter queryFilter, OutputStream out) throws Exception {
/* 412 */     List<BpmInstanceVO> data = listInstTaskJson(queryFilter);
/* 413 */     LinkedHashMap<String, String> fields = new LinkedHashMap<>();
/* 414 */     fields.put("BT", "流程标题");
/* 415 */     fields.put("MC", "流程名称");
/* 416 */     fields.put("ZT", "流程状态");
/* 417 */     fields.put("ZJR", "创建人");
/* 418 */     fields.put("WCSJ", "完成时间");
/* 419 */     fields.put("CXSJ", "持续时间");
/* 420 */     List<Map> map = new ArrayList<>();
/* 421 */     data.forEach(bpmInstanceVO -> {
/*     */           Map<String, Object> d = new HashMap<>();
/*     */           d.put("BT", bpmInstanceVO.getSubject());
/*     */           d.put("MC", bpmInstanceVO.getDefName());
/*     */           d.put("ZT", InstanceStatus.fromKey(bpmInstanceVO.getStatus()).getValue());
/*     */           d.put("ZJR", bpmInstanceVO.getCreator());
/*     */           Date date = bpmInstanceVO.getEndTime();
/*     */           if (date != null) {
/*     */             d.put("WCSJ", DateUtil.format(date, "yyyy-MM-dd HH:mm:ss"));
/*     */           }
/*     */           Long duration = bpmInstanceVO.getDuration();
/*     */           duration = Long.valueOf((duration == null) ? 0L : duration.longValue());
/*     */           if (duration.longValue() > 86400000L) {
/*     */             d.put("CXSJ", (duration.longValue() / 86400000L) + "天");
/*     */           } else if (duration.longValue() > 3600000L) {
/*     */             d.put("CXSJ", (duration.longValue() / 3600000L) + "小时");
/*     */           } else if (duration.longValue() > 60000L) {
/*     */             d.put("CXSJ", (duration.longValue() / 60000L) + "分钟");
/*     */           } else if (duration.longValue() > 1000L) {
/*     */             d.put("CXSJ", (duration.longValue() / 1000L) + "秒");
/*     */           } 
/*     */           map.add(d);
/*     */         });
/* 444 */     ExeclUtil.ListtoExecl(map, out, fields);
/*     */   }
/*     */   
/*     */   private InstanceActionCmd getInstCmd(BpmInstance bpmInstance) {
/* 448 */     DefaultInstanceActionCmd instanceActionCmd = new DefaultInstanceActionCmd();
/* 449 */     instanceActionCmd.setActionName("end");
/* 450 */     Map<String, IBusinessData> data = this.bpmBusDataHandle.getInstanceBusData(bpmInstance.getId(), null);
/* 451 */     instanceActionCmd.setBizDataMap(data);
/* 452 */     instanceActionCmd.setBpmDefinition(this.bpmProcessDefService.getDefinitionById(bpmInstance.getDefId()));
/* 453 */     instanceActionCmd.setBpmInstance((IBpmInstance)bpmInstance);
/* 454 */     instanceActionCmd.setDefId(bpmInstance.getDefId());
/* 455 */     return (InstanceActionCmd)instanceActionCmd;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map getInstanceStatusStatis(String instId) {
/* 460 */     return this.bpmInstanceDao.getInstanceStatusStatis(instId);
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/impl/BpmInstanceManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */