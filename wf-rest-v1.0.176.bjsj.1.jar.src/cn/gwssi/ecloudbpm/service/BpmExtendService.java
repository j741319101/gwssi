/*     */ package cn.gwssi.ecloudbpm.service;
/*     */ import com.dstz.bpm.api.constant.NodeType;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmInstance;
/*     */ import com.dstz.bpm.core.vo.BpmTaskOpinionVO;
/*     */ import com.dstz.bpm.rest.vo.BpmInstanceOptionVO;
/*     */ import com.dstz.bpm.rest.vo.BpmMetaOption;
/*     */ import com.dstz.bpm.rest.vo.BpmMetaPartentOption;
/*     */ import com.dstz.bpm.rest.vo.BpmOtherOption;
/*     */ import com.dstz.bpm.rest.vo.BpmOtherOptionVO;
/*     */ import com.dstz.base.core.util.BeanCopierUtils;
/*     */ import com.dstz.org.api.model.IGroup;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.service.GroupService;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.CopyOnWriteArrayList;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/*     */ import org.springframework.util.CollectionUtils;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ @Service
/*     */ public class BpmExtendService {
/*  36 */   private final Logger logger = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   private GroupService groupService;
/*     */   
/*     */   @Resource
/*     */   private UserService userService;
/*     */   
/*     */   @Resource
/*     */   BpmInstanceManager bpmInstanceManager;
/*     */   
/*     */   @Resource
/*     */   BpmDefinitionManager bpmDefinitionMananger;
/*     */   
/*     */   @Resource
/*     */   private ThreadPoolTaskExecutor executor;
/*     */   
/*     */   public List<List> packSingleInstanceOpition(List<BpmTaskOpinionVO> taskOpinions) {
/*  54 */     List<BpmInstanceOptionVO> list = new ArrayList<>(0);
/*  55 */     for (BpmTaskOpinionVO taskOpinionVO : taskOpinions) {
/*  56 */       BpmInstanceOptionVO instanceOptionVO = new BpmInstanceOptionVO();
/*  57 */       BeanCopierUtils.copyProperties(taskOpinionVO, instanceOptionVO);
/*  58 */       if (null != taskOpinionVO.getCreateTime()) {
/*  59 */         instanceOptionVO.setCreateTime(String.valueOf(taskOpinionVO.getCreateTime().getTime()));
/*     */       }
/*  61 */       if (null != taskOpinionVO.getApproveTime()) {
/*  62 */         instanceOptionVO.setApproveTime(String.valueOf(taskOpinionVO.getApproveTime().getTime()));
/*     */       }
/*     */       
/*  65 */       List<String> avilidOpinions = new ArrayList<>();
/*  66 */       avilidOpinions.add("启动");
/*  67 */       avilidOpinions.add("开始节点跳过");
/*  68 */       avilidOpinions.add("流程结束");
/*  69 */       if (!avilidOpinions.contains(instanceOptionVO.getOpinion()) && StringUtils.hasText(instanceOptionVO.getApprover())) {
/*  70 */         list.add(instanceOptionVO);
/*     */       }
/*     */     } 
/*  73 */     return getInstanceOption(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<BpmOtherOptionVO> packSingleInstanceOpitionWithOutSub(List<BpmTaskOpinionVO> taskOpinions) {
/*  83 */     List<BpmInstanceOptionVO> list = new ArrayList<>(0);
/*  84 */     for (BpmTaskOpinionVO taskOpinionVO : taskOpinions) {
/*  85 */       BpmInstanceOptionVO instanceOptionVO = new BpmInstanceOptionVO();
/*  86 */       BeanCopierUtils.copyProperties(taskOpinionVO, instanceOptionVO);
/*  87 */       if (null != taskOpinionVO.getCreateTime()) {
/*  88 */         instanceOptionVO.setCreateTime(String.valueOf(taskOpinionVO.getCreateTime().getTime()));
/*     */       }
/*  90 */       if (null != taskOpinionVO.getApproveTime()) {
/*  91 */         instanceOptionVO.setApproveTime(String.valueOf(taskOpinionVO.getApproveTime().getTime()));
/*     */       }
/*     */       
/*  94 */       List<String> avilidOpinions = new ArrayList<>();
/*  95 */       avilidOpinions.add("启动");
/*  96 */       avilidOpinions.add("开始节点跳过");
/*  97 */       avilidOpinions.add("流程结束");
/*  98 */       if (!avilidOpinions.contains(instanceOptionVO.getOpinion()) && (
/*  99 */         StringUtils.hasText(instanceOptionVO.getApprover()) || NodeType.CALLACTIVITY.getKey().equals(instanceOptionVO.getTaskType()))) {
/* 100 */         list.add(instanceOptionVO);
/*     */       }
/*     */     } 
/* 103 */     return getInstanceOptionExtend(list);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<List> getInstanceOption(List<BpmInstanceOptionVO> list) {
/* 113 */     if (!CollectionUtils.isEmpty(list)) {
/* 114 */       CopyOnWriteArrayList<BpmInstanceOptionVO> cowList = new CopyOnWriteArrayList<>(list);
/* 115 */       CountDownLatch countDownLatch = new CountDownLatch(cowList.size());
/* 116 */       Long startTime = Long.valueOf(System.currentTimeMillis());
/*     */       
/* 118 */       packUsersOfOpinions(this.executor, countDownLatch, cowList);
/*     */       try {
/* 120 */         countDownLatch.await();
/* 121 */         Long endTime = Long.valueOf(System.currentTimeMillis());
/* 122 */         this.logger.info("审批意见页查询调用feign接口共耗时：{}毫秒", Long.valueOf(endTime.longValue() - startTime.longValue()));
/* 123 */       } catch (InterruptedException e) {
/* 124 */         this.logger.error("查询人员信息和组织信息阻塞...");
/*     */       } 
/*     */       
/* 127 */       List<List> totalList = new ArrayList<>(0);
/*     */       
/* 129 */       CopyOnWriteArrayList<BpmInstanceOptionVO> otherListSource = new CopyOnWriteArrayList<>(cowList);
/*     */       
/* 131 */       Map<String, String> tasks = new HashMap<>(0);
/* 132 */       for (BpmInstanceOptionVO otherOption : otherListSource) {
/* 133 */         if (!tasks.containsKey(otherOption.getTaskKey())) {
/* 134 */           tasks.put(otherOption.getTaskKey(), otherOption.getTaskName());
/*     */         }
/*     */       } 
/* 137 */       List<BpmOtherOption> otherList = new ArrayList<>(0);
/*     */       
/* 139 */       if (!CollectionUtils.isEmpty(otherListSource)) {
/* 140 */         Iterator<Map.Entry<String, String>> iterator = tasks.entrySet().iterator();
/* 141 */         while (iterator.hasNext()) {
/* 142 */           Map.Entry<String, String> entity = iterator.next();
/* 143 */           BpmOtherOption bpmOtherOption = new BpmOtherOption();
/* 144 */           bpmOtherOption.setNodeName(entity.getValue());
/* 145 */           bpmOtherOption.setNodeId(entity.getKey());
/* 146 */           List<BpmInstanceOptionVO> bpmInstanceOptions = new ArrayList<>(0);
/*     */           
/* 148 */           for (BpmInstanceOptionVO otherOption : otherListSource) {
/* 149 */             if (((String)entity.getKey()).equals(otherOption.getTaskKey())) {
/* 150 */               bpmInstanceOptions.add(otherOption);
/*     */             }
/*     */           } 
/*     */           
/* 154 */           CopyOnWriteArrayList<BpmInstanceOptionVO> earlyListSource = new CopyOnWriteArrayList<>(bpmInstanceOptions);
/* 155 */           Collections.sort(earlyListSource, new Comparator<BpmInstanceOptionVO>()
/*     */               {
/*     */                 public int compare(BpmInstanceOptionVO o1, BpmInstanceOptionVO o2) {
/*     */                   try {
/* 159 */                     return (int)(Long.parseLong(o1.getCreateTime()) - Long.parseLong(o2.getCreateTime()));
/* 160 */                   } catch (Exception e) {
/* 161 */                     e.printStackTrace();
/* 162 */                     return 0;
/*     */                   } 
/*     */                 }
/*     */               });
/*     */ 
/*     */ 
/*     */           
/* 169 */           SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 170 */           bpmOtherOption.setTime(fmt.format(new Date(Long.parseLong(((BpmInstanceOptionVO)earlyListSource.get(0)).getCreateTime()))));
/*     */           
/* 172 */           List<BpmMetaPartentOption> partentOptions = new ArrayList<>(0);
/* 173 */           for (BpmInstanceOptionVO option : bpmInstanceOptions) {
/* 174 */             List<BpmMetaOption> bpmMetaOptions = new ArrayList<>(0);
/* 175 */             BpmMetaPartentOption partentOption = new BpmMetaPartentOption();
/* 176 */             partentOption.setName(option.getApproverName());
/* 177 */             partentOption.setOpinion(option.getOpinion());
/* 178 */             partentOption.setUserSort(option.getUserSort());
/* 179 */             partentOption.setOrg(checkStrNull(option.getOrgName()));
/* 180 */             partentOption.setOrgCode(checkStrNull(option.getOrgCode()));
/* 181 */             partentOption.setRoleName(checkStrNull(option.getRoleName()));
/* 182 */             partentOption.setRoleCode(checkStrNull(option.getRoleCode()));
/* 183 */             partentOption.setTime(fmt.format(new Date(Long.parseLong(option.getApproveTime()))));
/* 184 */             partentOption.setChildren(bpmMetaOptions);
/* 185 */             partentOptions.add(partentOption);
/*     */           } 
/*     */           
/* 188 */           bpmOtherOption.setChildren(partentOptions);
/* 189 */           otherList.add(bpmOtherOption);
/*     */         } 
/*     */       } 
/*     */       
/* 193 */       totalList.add(otherList);
/* 194 */       return totalList;
/*     */     } 
/* 196 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private List<BpmOtherOptionVO> getInstanceOptionExtend(List<BpmInstanceOptionVO> list) {
/* 206 */     if (!CollectionUtils.isEmpty(list)) {
/* 207 */       CopyOnWriteArrayList<BpmInstanceOptionVO> cowList = new CopyOnWriteArrayList<>(list);
/* 208 */       CountDownLatch countDownLatch = new CountDownLatch(cowList.size());
/* 209 */       Long startTime = Long.valueOf(System.currentTimeMillis());
/*     */       
/* 211 */       packUsersOfOpinions(this.executor, countDownLatch, cowList);
/*     */       try {
/* 213 */         countDownLatch.await();
/* 214 */         Long endTime = Long.valueOf(System.currentTimeMillis());
/* 215 */         this.logger.info("审批意见页查询调用feign接口共耗时：{}毫秒", Long.valueOf(endTime.longValue() - startTime.longValue()));
/* 216 */       } catch (InterruptedException e) {
/* 217 */         this.logger.error("查询人员信息和组织信息阻塞...");
/*     */       } 
/*     */       
/* 220 */       List<List> totalList = new ArrayList<>(0);
/*     */       
/* 222 */       CopyOnWriteArrayList<BpmInstanceOptionVO> otherListSource = new CopyOnWriteArrayList<>(cowList);
/*     */       
/* 224 */       Map<String, String> tasks = new HashMap<>(0);
/* 225 */       for (BpmInstanceOptionVO otherOption : otherListSource) {
/* 226 */         if (!tasks.containsKey(otherOption.getTaskKey())) {
/* 227 */           tasks.put(otherOption.getTaskKey() + "," + otherOption.getTaskType(), otherOption.getTaskName());
/*     */         }
/*     */       } 
/* 230 */       List<BpmOtherOptionVO> otherList = new ArrayList<>(0);
/*     */       
/* 232 */       if (!CollectionUtils.isEmpty(otherListSource)) {
/* 233 */         Iterator<Map.Entry<String, String>> iterator = tasks.entrySet().iterator();
/* 234 */         while (iterator.hasNext()) {
/* 235 */           Map.Entry<String, String> entity = iterator.next();
/* 236 */           BpmOtherOptionVO bpmOtherOptionVO = new BpmOtherOptionVO();
/* 237 */           bpmOtherOptionVO.setNodeName(entity.getValue());
/* 238 */           String[] entityKey = ((String)entity.getKey()).split(",");
/* 239 */           bpmOtherOptionVO.setNodeId(entityKey[0]);
/* 240 */           bpmOtherOptionVO.setTaskType(entityKey[1]);
/* 241 */           if (NodeType.CALLACTIVITY.getKey().equals(entityKey[1])) {
/*     */             
/* 243 */             handleOpinionsOfOutSub(bpmOtherOptionVO, entityKey[0], otherListSource);
/*     */           } else {
/*     */             
/* 246 */             handleDetpOpinions(bpmOtherOptionVO, entityKey[0], otherListSource);
/*     */           } 
/* 248 */           otherList.add(bpmOtherOptionVO);
/*     */         } 
/*     */       } 
/* 251 */       return otherList;
/*     */     } 
/* 253 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleOpinionsOfOutSub(BpmOtherOptionVO bpmOtherOptionVO, String taskKey, List<BpmInstanceOptionVO> otherListSource) {
/* 264 */     Map<String, List<BpmInstanceOptionVO>> sourceMap = (Map<String, List<BpmInstanceOptionVO>>)otherListSource.stream().collect(Collectors.toMap(BpmInstanceOption::getTaskKey, a -> new ArrayList(Arrays.asList((Object[])new BpmInstanceOptionVO[] { a }, )), (oldList, newList) -> {
/*     */             oldList.addAll(newList);
/*     */             
/*     */             return oldList;
/*     */           }));
/* 269 */     List<BpmInstanceOptionVO> sourceList = sourceMap.get(taskKey);
/*     */     
/* 271 */     BpmInstanceOptionVO outSubInstOpinion = sourceList.get(sourceList.size() - 1);
/* 272 */     SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 273 */     bpmOtherOptionVO.setInstId(outSubInstOpinion.getInstId());
/* 274 */     bpmOtherOptionVO.setTaskId(outSubInstOpinion.getTaskId());
/* 275 */     bpmOtherOptionVO.setTime(format.format(new Date(Long.parseLong(outSubInstOpinion.getCreateTime()))));
/* 276 */     bpmOtherOptionVO.setDefId(outSubInstOpinion.getDefId());
/* 277 */     bpmOtherOptionVO.setOpinion(outSubInstOpinion.getOpinion());
/*     */     
/* 279 */     List<BpmOtherOptionVO> instances = new ArrayList<>(0);
/*     */     
/* 281 */     for (BpmInstanceOptionVO customVO : sourceList) {
/* 282 */       List<BpmTaskOpinionVO> outSubInstances = customVO.getBpmTaskOpinionVOS();
/* 283 */       for (BpmTaskOpinionVO vo : outSubInstances) {
/* 284 */         BpmOtherOptionVO instanceOtherOpinion = new BpmOtherOptionVO();
/* 285 */         instanceOtherOpinion.setOrgSn(vo.getOrgSn());
/* 286 */         instanceOtherOpinion.setDefId(vo.getDefId());
/* 287 */         instanceOtherOpinion.setOpinion(vo.getOpinion());
/* 288 */         instanceOtherOpinion.setOrgName(vo.getOrgName());
/* 289 */         instanceOtherOpinion.setTaskType(vo.getTaskType());
/* 290 */         instanceOtherOpinion.setTaskId(vo.getTaskId());
/* 291 */         instanceOtherOpinion.setTime(format.format(vo.getCreateTime()));
/* 292 */         instanceOtherOpinion.setNodeName(vo.getTaskName());
/* 293 */         instanceOtherOpinion.setNodeId(vo.getTaskKey());
/* 294 */         instanceOtherOpinion.setInstId(vo.getInstId());
/*     */         
/* 296 */         List<BpmTaskOpinionVO> opinions = vo.getBpmTaskOpinionVOS();
/* 297 */         instanceOtherOpinion.setBpmTaskOpinionVOS(packSingleInstanceOpitionWithOutSub(opinions));
/* 298 */         instances.add(instanceOtherOpinion);
/*     */       } 
/*     */     } 
/* 301 */     bpmOtherOptionVO.setBpmTaskOpinionVOS(instances);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private void handleDetpOpinions(BpmOtherOptionVO bpmOtherOptionVO, String taskKey, List<BpmInstanceOptionVO> otherListSource) {
/* 311 */     List<BpmInstanceOptionVO> bpmInstanceOptions = new ArrayList<>(0);
/*     */     
/* 313 */     for (BpmInstanceOptionVO otherOption : otherListSource) {
/* 314 */       if (taskKey.equals(otherOption.getTaskKey())) {
/* 315 */         bpmInstanceOptions.add(otherOption);
/*     */       }
/*     */     } 
/*     */     
/* 319 */     CopyOnWriteArrayList<BpmInstanceOptionVO> earlyListSource = new CopyOnWriteArrayList<>(bpmInstanceOptions);
/* 320 */     Collections.sort(earlyListSource, new Comparator<BpmInstanceOptionVO>()
/*     */         {
/*     */           public int compare(BpmInstanceOptionVO o1, BpmInstanceOptionVO o2) {
/*     */             try {
/* 324 */               return (int)(Long.parseLong(o1.getCreateTime()) - Long.parseLong(o2.getCreateTime()));
/* 325 */             } catch (Exception e) {
/* 326 */               e.printStackTrace();
/* 327 */               return 0;
/*     */             } 
/*     */           }
/*     */         });
/*     */ 
/*     */ 
/*     */     
/* 334 */     SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
/* 335 */     bpmOtherOptionVO.setTime(fmt.format(new Date(Long.parseLong(((BpmInstanceOptionVO)earlyListSource.get(0)).getCreateTime()))));
/* 336 */     bpmOtherOptionVO.setDefId(((BpmInstanceOptionVO)earlyListSource.get(0)).getDefId());
/*     */     
/* 338 */     List<BpmMetaPartentOption> partentOptions = new ArrayList<>(0);
/* 339 */     for (BpmInstanceOptionVO option : bpmInstanceOptions) {
/* 340 */       List<BpmMetaOption> bpmMetaOptions = new ArrayList<>(0);
/* 341 */       BpmMetaPartentOption partentOption = new BpmMetaPartentOption();
/* 342 */       partentOption.setName(option.getApproverName());
/* 343 */       partentOption.setOpinion(option.getOpinion());
/* 344 */       partentOption.setUserSort(option.getUserSort());
/* 345 */       partentOption.setOrg(checkStrNull(option.getOrgName()));
/* 346 */       partentOption.setOrgCode(checkStrNull(option.getOrgCode()));
/* 347 */       partentOption.setRoleName(checkStrNull(option.getRoleName()));
/* 348 */       partentOption.setRoleCode(checkStrNull(option.getRoleCode()));
/* 349 */       partentOption.setTime(fmt.format(new Date(Long.parseLong(option.getApproveTime()))));
/* 350 */       partentOption.setChildren(bpmMetaOptions);
/* 351 */       partentOptions.add(partentOption);
/*     */     } 
/*     */     
/* 354 */     bpmOtherOptionVO.setChildren(partentOptions);
/*     */   }
/*     */   
/*     */   private void packUsersOfOpinions(ThreadPoolTaskExecutor executor, CountDownLatch countDownLatch, List<BpmInstanceOptionVO> cowList) {
/* 358 */     for (Iterator<BpmInstanceOptionVO> iterator = cowList.iterator(); iterator.hasNext(); ) { BpmInstanceOptionVO bpmInstanceOption = iterator.next();
/*     */       
/* 360 */       if (NodeType.CALLACTIVITY.getKey().equals(bpmInstanceOption.getTaskType())) {
/* 361 */         countDownLatch.countDown();
/*     */         continue;
/*     */       } 
/* 364 */       executor.execute(() -> {
/*     */             String orgName = "";
/*     */             
/*     */             String orgCode = "";
/*     */             
/*     */             String roleName = "";
/*     */             
/*     */             String postName = "";
/*     */             
/*     */             String roleCode = "";
/*     */             
/*     */             List<? extends IGroup> groups = null;
/*     */             try {
/*     */               groups = this.groupService.getGroupsByUserId(bpmInstanceOption.getApprover());
/*     */               for (IGroup iGroup : groups) {
/*     */                 if ("role".equals(iGroup.getGroupType())) {
/*     */                   if (!roleName.contains(iGroup.getGroupName())) {
/*     */                     roleName = roleName + "," + iGroup.getGroupName();
/*     */                   }
/*     */                   if (!roleCode.contains(iGroup.getGroupCode())) {
/*     */                     roleCode = roleCode + "," + iGroup.getGroupCode();
/*     */                   }
/*     */                 } 
/*     */                 if ("org".equals(iGroup.getGroupType())) {
/*     */                   if (!orgName.contains(iGroup.getGroupName())) {
/*     */                     orgName = orgName + "," + iGroup.getGroupName();
/*     */                   }
/*     */                   if (!orgCode.contains(iGroup.getGroupCode())) {
/*     */                     orgCode = orgCode + "," + iGroup.getGroupCode();
/*     */                   }
/*     */                 } 
/*     */                 if ("post".equals(iGroup.getGroupType()) && !postName.contains(iGroup.getGroupName())) {
/*     */                   postName = postName + "," + iGroup.getGroupName();
/*     */                 }
/*     */               } 
/*     */               if (postName.length() > 1) {
/*     */                 postName = postName.substring(1, postName.length());
/*     */                 bpmInstanceOption.setPostName(postName);
/*     */               } 
/*     */               if (orgName.length() > 1) {
/*     */                 orgName = orgName.substring(1, orgName.length());
/*     */                 bpmInstanceOption.setOrgName(orgName);
/*     */               } 
/*     */               if (orgCode.length() > 1) {
/*     */                 orgCode = orgCode.substring(1, orgCode.length());
/*     */                 bpmInstanceOption.setOrgCode(orgCode);
/*     */               } 
/*     */               if (roleName.length() > 1) {
/*     */                 roleName = roleName.substring(1, roleName.length());
/*     */                 bpmInstanceOption.setRoleName(roleName);
/*     */               } 
/*     */               if (roleCode.length() > 1) {
/*     */                 roleCode = roleCode.substring(1, roleCode.length());
/*     */                 bpmInstanceOption.setRoleCode(roleCode);
/*     */               } 
/*     */               IUser user = this.userService.getUserById(bpmInstanceOption.getApprover());
/*     */               if (null != user) {
/*     */                 bpmInstanceOption.setUserSort(String.valueOf((null == user.getSn()) ? 0 : user.getSn().intValue()));
/*     */               } else {
/*     */                 this.logger.info("获取审批意见信息接口中查询人员信息为空，查询条件userId={}", bpmInstanceOption.getApprover());
/*     */                 bpmInstanceOption.setUserSort("0");
/*     */               } 
/* 426 */             } catch (Exception e) {
/*     */               this.logger.error("审批意见信息接口调用人员信息接口和组织信息接口异常！", e);
/*     */               countDownLatch.countDown();
/*     */             } 
/*     */             countDownLatch.countDown();
/*     */           }); }
/*     */   
/*     */   }
/*     */ 
/*     */   
/*     */   public JSONObject getInstanceOptionStruct(String instId) {
/* 437 */     BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
/* 438 */     if (null != inst) {
/* 439 */       String defId = inst.getDefId();
/* 440 */       BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
/* 441 */       if (null != bpmDefinition) {
/* 442 */         String defSetting = bpmDefinition.getDefSetting();
/* 443 */         JSONObject jsonObject = JSON.parseObject(defSetting);
/* 444 */         JSONObject flowObject = (JSONObject)jsonObject.get("flow");
/* 445 */         JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
/* 446 */         JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
/* 447 */         return opinionObject;
/*     */       } 
/* 449 */       return null;
/*     */     } 
/*     */     
/* 452 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   private String checkStrNull(String str) {
/* 457 */     if (StringUtils.isEmpty(str)) {
/* 458 */       return "";
/*     */     }
/* 460 */     return str;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/service/BpmExtendService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */