package com.dstz.bpm.service;

import com.dstz.bpm.api.constant.NodeType;
import com.dstz.bpm.core.manager.BpmDefinitionManager;
import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.model.BpmInstance;
import com.dstz.bpm.core.vo.BpmTaskOpinionVO;
import com.dstz.base.core.util.BeanCopierUtils;
import com.dstz.bpm.wf.rest.vo.*;
import com.dstz.org.api.model.IGroup;
import com.dstz.org.api.model.IUser;
import com.dstz.org.api.service.GroupService;
import com.dstz.org.api.service.UserService;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class BpmExtendService {
   private final Logger logger = LoggerFactory.getLogger(this.getClass());
   @Resource
   private GroupService groupService;
   @Resource
   private UserService userService;
   @Resource
   BpmInstanceManager bpmInstanceManager;
   @Resource
   BpmDefinitionManager bpmDefinitionMananger;
   @Resource
   private ThreadPoolTaskExecutor executor;

   public List<List> packSingleInstanceOpition(List<BpmTaskOpinionVO> taskOpinions) {
      List<BpmInstanceOptionVO> list = new ArrayList(0);
      Iterator var3 = taskOpinions.iterator();

      while(var3.hasNext()) {
         BpmTaskOpinionVO taskOpinionVO = (BpmTaskOpinionVO)var3.next();
         BpmInstanceOptionVO instanceOptionVO = new BpmInstanceOptionVO();
         BeanCopierUtils.copyProperties(taskOpinionVO, instanceOptionVO);
         if (null != taskOpinionVO.getCreateTime()) {
            instanceOptionVO.setCreateTime(String.valueOf(taskOpinionVO.getCreateTime().getTime()));
         }

         if (null != taskOpinionVO.getApproveTime()) {
            instanceOptionVO.setApproveTime(String.valueOf(taskOpinionVO.getApproveTime().getTime()));
         }

         List<String> avilidOpinions = new ArrayList();
         avilidOpinions.add("启动");
         avilidOpinions.add("开始节点跳过");
         avilidOpinions.add("流程结束");
         if (!avilidOpinions.contains(instanceOptionVO.getOpinion()) && StringUtils.hasText(instanceOptionVO.getApprover())) {
            list.add(instanceOptionVO);
         }
      }

      return this.getInstanceOption(list);
   }

   public List<BpmOtherOptionVO> packSingleInstanceOpitionWithOutSub(List<BpmTaskOpinionVO> taskOpinions) {
      List<BpmInstanceOptionVO> list = new ArrayList(0);
      Iterator var3 = taskOpinions.iterator();

      while(true) {
         BpmInstanceOptionVO instanceOptionVO;
         ArrayList avilidOpinions;
         do {
            do {
               if (!var3.hasNext()) {
                  return this.getInstanceOptionExtend(list);
               }

               BpmTaskOpinionVO taskOpinionVO = (BpmTaskOpinionVO)var3.next();
               instanceOptionVO = new BpmInstanceOptionVO();
               BeanCopierUtils.copyProperties(taskOpinionVO, instanceOptionVO);
               if (null != taskOpinionVO.getCreateTime()) {
                  instanceOptionVO.setCreateTime(String.valueOf(taskOpinionVO.getCreateTime().getTime()));
               }

               if (null != taskOpinionVO.getApproveTime()) {
                  instanceOptionVO.setApproveTime(String.valueOf(taskOpinionVO.getApproveTime().getTime()));
               }

               avilidOpinions = new ArrayList();
               avilidOpinions.add("启动");
               avilidOpinions.add("开始节点跳过");
               avilidOpinions.add("流程结束");
            } while(avilidOpinions.contains(instanceOptionVO.getOpinion()));
         } while(!StringUtils.hasText(instanceOptionVO.getApprover()) && !NodeType.CALLACTIVITY.getKey().equals(instanceOptionVO.getTaskType()));

         list.add(instanceOptionVO);
      }
   }

   public List<List> getInstanceOption(List<BpmInstanceOptionVO> list) {
      if (CollectionUtils.isEmpty(list)) {
         return null;
      } else {
         CopyOnWriteArrayList<BpmInstanceOptionVO> cowList = new CopyOnWriteArrayList(list);
         CountDownLatch countDownLatch = new CountDownLatch(cowList.size());
         Long startTime = System.currentTimeMillis();
         this.packUsersOfOpinions(this.executor, countDownLatch, cowList);

         try {
            countDownLatch.await();
            Long endTime = System.currentTimeMillis();
            this.logger.info("审批意见页查询调用feign接口共耗时：{}毫秒", endTime - startTime);
         } catch (InterruptedException var20) {
            this.logger.error("查询人员信息和组织信息阻塞...");
         }

         List<List> totalList = new ArrayList(0);
         CopyOnWriteArrayList<BpmInstanceOptionVO> otherListSource = new CopyOnWriteArrayList(cowList);
         Map<String, String> tasks = new HashMap(0);
         Iterator var8 = otherListSource.iterator();

         while(var8.hasNext()) {
            BpmInstanceOptionVO otherOption = (BpmInstanceOptionVO)var8.next();
            if (!tasks.containsKey(otherOption.getTaskKey())) {
               tasks.put(otherOption.getTaskKey(), otherOption.getTaskName());
            }
         }

         List<BpmOtherOption> otherList = new ArrayList(0);
         if (!CollectionUtils.isEmpty(otherListSource)) {
            Iterator iterator = tasks.entrySet().iterator();

            while(iterator.hasNext()) {
               Entry<String, String> entity = (Entry)iterator.next();
               BpmOtherOption bpmOtherOption = new BpmOtherOption();
               bpmOtherOption.setNodeName((String)entity.getValue());
               bpmOtherOption.setNodeId((String)entity.getKey());
               List<BpmInstanceOptionVO> bpmInstanceOptions = new ArrayList(0);
               Iterator var13 = otherListSource.iterator();

               while(var13.hasNext()) {
                  BpmInstanceOptionVO otherOption = (BpmInstanceOptionVO)var13.next();
                  if (((String)entity.getKey()).equals(otherOption.getTaskKey())) {
                     bpmInstanceOptions.add(otherOption);
                  }
               }

               CopyOnWriteArrayList<BpmInstanceOptionVO> earlyListSource = new CopyOnWriteArrayList(bpmInstanceOptions);
               Collections.sort(earlyListSource, new Comparator<BpmInstanceOptionVO>() {
                  public int compare(BpmInstanceOptionVO o1, BpmInstanceOptionVO o2) {
                     try {
                        return (int)(Long.parseLong(o1.getCreateTime()) - Long.parseLong(o2.getCreateTime()));
                     } catch (Exception var4) {
                        var4.printStackTrace();
                        return 0;
                     }
                  }
               });
               SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
               bpmOtherOption.setTime(fmt.format(new Date(Long.parseLong(((BpmInstanceOptionVO)earlyListSource.get(0)).getCreateTime()))));
               List<BpmMetaPartentOption> partentOptions = new ArrayList(0);
               Iterator var16 = bpmInstanceOptions.iterator();

               while(var16.hasNext()) {
                  BpmInstanceOptionVO option = (BpmInstanceOptionVO)var16.next();
                  List<BpmMetaOption> bpmMetaOptions = new ArrayList(0);
                  BpmMetaPartentOption partentOption = new BpmMetaPartentOption();
                  partentOption.setName(option.getApproverName());
                  partentOption.setOpinion(option.getOpinion());
                  partentOption.setUserSort(option.getUserSort());
                  partentOption.setOrg(this.checkStrNull(option.getOrgName()));
                  partentOption.setOrgCode(this.checkStrNull(option.getOrgCode()));
                  partentOption.setRoleName(this.checkStrNull(option.getRoleName()));
                  partentOption.setRoleCode(this.checkStrNull(option.getRoleCode()));
                  partentOption.setTime(fmt.format(new Date(Long.parseLong(option.getApproveTime()))));
                  partentOption.setChildren(bpmMetaOptions);
                  partentOptions.add(partentOption);
               }

               bpmOtherOption.setChildren(partentOptions);
               otherList.add(bpmOtherOption);
            }
         }

         totalList.add(otherList);
         return totalList;
      }
   }

   private List<BpmOtherOptionVO> getInstanceOptionExtend(List<BpmInstanceOptionVO> list) {
      if (CollectionUtils.isEmpty(list)) {
         return null;
      } else {
         CopyOnWriteArrayList<BpmInstanceOptionVO> cowList = new CopyOnWriteArrayList(list);
         CountDownLatch countDownLatch = new CountDownLatch(cowList.size());
         Long startTime = System.currentTimeMillis();
         this.packUsersOfOpinions(this.executor, countDownLatch, cowList);

         try {
            countDownLatch.await();
            Long endTime = System.currentTimeMillis();
            this.logger.info("审批意见页查询调用feign接口共耗时：{}毫秒", endTime - startTime);
         } catch (InterruptedException var13) {
            this.logger.error("查询人员信息和组织信息阻塞...");
         }

         new ArrayList(0);
         CopyOnWriteArrayList<BpmInstanceOptionVO> otherListSource = new CopyOnWriteArrayList(cowList);
         Map<String, String> tasks = new HashMap(0);
         Iterator var8 = otherListSource.iterator();

         while(var8.hasNext()) {
            BpmInstanceOptionVO otherOption = (BpmInstanceOptionVO)var8.next();
            if (!tasks.containsKey(otherOption.getTaskKey())) {
               tasks.put(otherOption.getTaskKey() + "," + otherOption.getTaskType(), otherOption.getTaskName());
            }
         }

         List<BpmOtherOptionVO> otherList = new ArrayList(0);
         BpmOtherOptionVO bpmOtherOptionVO;
         if (!CollectionUtils.isEmpty(otherListSource)) {
            for(Iterator iterator = tasks.entrySet().iterator(); iterator.hasNext(); otherList.add(bpmOtherOptionVO)) {
               Entry<String, String> entity = (Entry)iterator.next();
               bpmOtherOptionVO = new BpmOtherOptionVO();
               bpmOtherOptionVO.setNodeName((String)entity.getValue());
               String[] entityKey = ((String)entity.getKey()).split(",");
               bpmOtherOptionVO.setNodeId(entityKey[0]);
               bpmOtherOptionVO.setTaskType(entityKey[1]);
               if (NodeType.CALLACTIVITY.getKey().equals(entityKey[1])) {
                  this.handleOpinionsOfOutSub(bpmOtherOptionVO, entityKey[0], otherListSource);
               } else {
                  this.handleDetpOpinions(bpmOtherOptionVO, entityKey[0], otherListSource);
               }
            }
         }

         return otherList;
      }
   }

   private void handleOpinionsOfOutSub(BpmOtherOptionVO bpmOtherOptionVO, String taskKey, List<BpmInstanceOptionVO> otherListSource) {
      Map<String, List<BpmInstanceOptionVO>> sourceMap = (Map)otherListSource.stream().collect(Collectors.toMap(BpmInstanceOption::getTaskKey, (a) -> {
         return new ArrayList(Arrays.asList(a));
      }, (oldList, newList) -> {
         oldList.addAll(newList);
         return oldList;
      }));
      List<BpmInstanceOptionVO> sourceList = (List)sourceMap.get(taskKey);
      BpmInstanceOptionVO outSubInstOpinion = (BpmInstanceOptionVO)sourceList.get(sourceList.size() - 1);
      SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      bpmOtherOptionVO.setInstId(outSubInstOpinion.getInstId());
      bpmOtherOptionVO.setTaskId(outSubInstOpinion.getTaskId());
      bpmOtherOptionVO.setTime(format.format(new Date(Long.parseLong(outSubInstOpinion.getCreateTime()))));
      bpmOtherOptionVO.setDefId(outSubInstOpinion.getDefId());
      bpmOtherOptionVO.setOpinion(outSubInstOpinion.getOpinion());
      List<BpmOtherOptionVO> instances = new ArrayList(0);
      Iterator var9 = sourceList.iterator();

      while(var9.hasNext()) {
         BpmInstanceOptionVO customVO = (BpmInstanceOptionVO)var9.next();
         List<BpmTaskOpinionVO> outSubInstances = customVO.getBpmTaskOpinionVOS();
         Iterator var12 = outSubInstances.iterator();

         while(var12.hasNext()) {
            BpmTaskOpinionVO vo = (BpmTaskOpinionVO)var12.next();
            BpmOtherOptionVO instanceOtherOpinion = new BpmOtherOptionVO();
            instanceOtherOpinion.setOrgSn(vo.getOrgSn());
            instanceOtherOpinion.setDefId(vo.getDefId());
            instanceOtherOpinion.setOpinion(vo.getOpinion());
            instanceOtherOpinion.setOrgName(vo.getOrgName());
            instanceOtherOpinion.setTaskType(vo.getTaskType());
            instanceOtherOpinion.setTaskId(vo.getTaskId());
            instanceOtherOpinion.setTime(format.format(vo.getCreateTime()));
            instanceOtherOpinion.setNodeName(vo.getTaskName());
            instanceOtherOpinion.setNodeId(vo.getTaskKey());
            instanceOtherOpinion.setInstId(vo.getInstId());
            List<BpmTaskOpinionVO> opinions = vo.getBpmTaskOpinionVOS();
            instanceOtherOpinion.setBpmTaskOpinionVOS(this.packSingleInstanceOpitionWithOutSub(opinions));
            instances.add(instanceOtherOpinion);
         }
      }

      bpmOtherOptionVO.setBpmTaskOpinionVOS(instances);
   }

   private void handleDetpOpinions(BpmOtherOptionVO bpmOtherOptionVO, String taskKey, List<BpmInstanceOptionVO> otherListSource) {
      List<BpmInstanceOptionVO> bpmInstanceOptions = new ArrayList(0);
      Iterator var5 = otherListSource.iterator();

      while(var5.hasNext()) {
         BpmInstanceOptionVO otherOption = (BpmInstanceOptionVO)var5.next();
         if (taskKey.equals(otherOption.getTaskKey())) {
            bpmInstanceOptions.add(otherOption);
         }
      }

      CopyOnWriteArrayList<BpmInstanceOptionVO> earlyListSource = new CopyOnWriteArrayList(bpmInstanceOptions);
      Collections.sort(earlyListSource, new Comparator<BpmInstanceOptionVO>() {
         public int compare(BpmInstanceOptionVO o1, BpmInstanceOptionVO o2) {
            try {
               return (int)(Long.parseLong(o1.getCreateTime()) - Long.parseLong(o2.getCreateTime()));
            } catch (Exception var4) {
               var4.printStackTrace();
               return 0;
            }
         }
      });
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      bpmOtherOptionVO.setTime(fmt.format(new Date(Long.parseLong(((BpmInstanceOptionVO)earlyListSource.get(0)).getCreateTime()))));
      bpmOtherOptionVO.setDefId(((BpmInstanceOptionVO)earlyListSource.get(0)).getDefId());
      List<BpmMetaPartentOption> partentOptions = new ArrayList(0);
      Iterator var8 = bpmInstanceOptions.iterator();

      while(var8.hasNext()) {
         BpmInstanceOptionVO option = (BpmInstanceOptionVO)var8.next();
         List<BpmMetaOption> bpmMetaOptions = new ArrayList(0);
         BpmMetaPartentOption partentOption = new BpmMetaPartentOption();
         partentOption.setName(option.getApproverName());
         partentOption.setOpinion(option.getOpinion());
         partentOption.setUserSort(option.getUserSort());
         partentOption.setOrg(this.checkStrNull(option.getOrgName()));
         partentOption.setOrgCode(this.checkStrNull(option.getOrgCode()));
         partentOption.setRoleName(this.checkStrNull(option.getRoleName()));
         partentOption.setRoleCode(this.checkStrNull(option.getRoleCode()));
         partentOption.setTime(fmt.format(new Date(Long.parseLong(option.getApproveTime()))));
         partentOption.setChildren(bpmMetaOptions);
         partentOptions.add(partentOption);
      }

      bpmOtherOptionVO.setChildren(partentOptions);
   }

   private void packUsersOfOpinions(ThreadPoolTaskExecutor executor, CountDownLatch countDownLatch, List<BpmInstanceOptionVO> cowList) {
      Iterator var4 = cowList.iterator();

      while(var4.hasNext()) {
         BpmInstanceOptionVO bpmInstanceOption = (BpmInstanceOptionVO)var4.next();
         if (NodeType.CALLACTIVITY.getKey().equals(bpmInstanceOption.getTaskType())) {
            countDownLatch.countDown();
         } else {
            executor.execute(() -> {
               String orgName = "";
               String orgCode = "";
               String roleName = "";
               String postName = "";
               String roleCode = "";
               List groups = null;

               try {
                  groups = this.groupService.getGroupsByUserId(bpmInstanceOption.getApprover());
                  Iterator var9 = groups.iterator();

                  while(var9.hasNext()) {
                     IGroup iGroup = (IGroup)var9.next();
                     if ("role".equals(iGroup.getGroupType())) {
                        if (!roleName.contains(iGroup.getGroupName())) {
                           roleName = roleName + "," + iGroup.getGroupName();
                        }

                        if (!roleCode.contains(iGroup.getGroupCode())) {
                           roleCode = roleCode + "," + iGroup.getGroupCode();
                        }
                     }

                     if ("org".equals(iGroup.getGroupType())) {
                        if (!orgName.contains(iGroup.getGroupName())) {
                           orgName = orgName + "," + iGroup.getGroupName();
                        }

                        if (!orgCode.contains(iGroup.getGroupCode())) {
                           orgCode = orgCode + "," + iGroup.getGroupCode();
                        }
                     }

                     if ("post".equals(iGroup.getGroupType()) && !postName.contains(iGroup.getGroupName())) {
                        postName = postName + "," + iGroup.getGroupName();
                     }
                  }

                  if (postName.length() > 1) {
                     postName = postName.substring(1, postName.length());
                     bpmInstanceOption.setPostName(postName);
                  }

                  if (orgName.length() > 1) {
                     orgName = orgName.substring(1, orgName.length());
                     bpmInstanceOption.setOrgName(orgName);
                  }

                  if (orgCode.length() > 1) {
                     orgCode = orgCode.substring(1, orgCode.length());
                     bpmInstanceOption.setOrgCode(orgCode);
                  }

                  if (roleName.length() > 1) {
                     roleName = roleName.substring(1, roleName.length());
                     bpmInstanceOption.setRoleName(roleName);
                  }

                  if (roleCode.length() > 1) {
                     roleCode = roleCode.substring(1, roleCode.length());
                     bpmInstanceOption.setRoleCode(roleCode);
                  }

                  IUser user = this.userService.getUserById(bpmInstanceOption.getApprover());
                  if (null != user) {
                     bpmInstanceOption.setUserSort(String.valueOf(null == user.getSn() ? 0 : user.getSn()));
                  } else {
                     this.logger.info("获取审批意见信息接口中查询人员信息为空，查询条件userId={}", bpmInstanceOption.getApprover());
                     bpmInstanceOption.setUserSort("0");
                  }
               } catch (Exception var11) {
                  this.logger.error("审批意见信息接口调用人员信息接口和组织信息接口异常！", var11);
                  countDownLatch.countDown();
               }

               countDownLatch.countDown();
            });
         }
      }

   }

   public JSONObject getInstanceOptionStruct(String instId) {
      BpmInstance inst = (BpmInstance)this.bpmInstanceManager.get(instId);
      if (null != inst) {
         String defId = inst.getDefId();
         BpmDefinition bpmDefinition = (BpmDefinition)this.bpmDefinitionMananger.get(defId);
         if (null != bpmDefinition) {
            String defSetting = bpmDefinition.getDefSetting();
            JSONObject jsonObject = JSON.parseObject(defSetting);
            JSONObject flowObject = (JSONObject)jsonObject.get("flow");
            JSONObject propertiesObject = (JSONObject)flowObject.get("properties");
            JSONObject opinionObject = (JSONObject)propertiesObject.get("opinion");
            return opinionObject;
         } else {
            return null;
         }
      } else {
         return null;
      }
   }

   private String checkStrNull(String str) {
      return StringUtils.isEmpty(str) ? "" : str;
   }
}
