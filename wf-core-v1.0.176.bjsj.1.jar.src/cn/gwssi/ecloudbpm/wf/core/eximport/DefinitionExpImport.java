/*     */ package cn.gwssi.ecloudbpm.wf.core.eximport;
/*     */ import cn.gwssi.ecloudbpm.bus.manager.BusinessPermissionManager;
/*     */ import cn.gwssi.ecloudbpm.bus.model.BusinessPermission;
/*     */ import cn.gwssi.ecloudbpm.wf.api.service.BpmProcessDefService;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.BpmDefinition;
/*     */ import cn.gwssi.ecloudbpm.wf.core.model.DefinitionImportModel;
/*     */ import cn.gwssi.ecloudframework.base.api.bpmExpImport.BpmExpImport;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryOP;
/*     */ import cn.gwssi.ecloudframework.base.db.model.query.DefaultQueryFilter;
/*     */ import cn.gwssi.ecloudframework.org.api.constant.GroupTypeConstant;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.GroupService;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.constant.RightsObjectConstants;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.SysAuthorizationManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.SysTreeNodeManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SysAuthorization;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SysTree;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SysTreeNode;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.serializer.SerializerFeature;
/*     */ import java.io.BufferedReader;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.OutputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.bpmn.exceptions.XMLException;
/*     */ import org.activiti.engine.repository.Model;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.stereotype.Component;
/*     */ 
/*     */ @Component
/*     */ public class DefinitionExpImport implements BpmExpImport {
/*  44 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   @Resource
/*     */   JdbcTemplate jdbcTemplate;
/*     */   
/*     */   @Resource
/*     */   private BpmDefinitionManager bpmDefinitionManager;
/*     */   @Resource
/*     */   private BpmProcessDefService bpmProcessDefService;
/*     */   @Resource
/*     */   RepositoryService repositoryService;
/*     */   @Resource
/*     */   SysAuthorizationManager sysAuthorizationManager;
/*     */   @Resource
/*     */   SysTreeNodeManager sysTreeNodeManager;
/*     */   @Resource
/*     */   SysTreeManager sysTreeManager;
/*     */   @Resource
/*     */   UserService userService;
/*     */   @Resource
/*     */   GroupService groupService;
/*     */   @Resource
/*     */   BusinessPermissionManager businessPermissionManager;
/*     */   
/*     */   public void bpmExport(String defIds, String filePath) throws Exception {
/*  69 */     if (StringUtils.isNotEmpty(defIds)) {
/*  70 */       File file = new File(filePath);
/*  71 */       if (!file.exists()) {
/*  72 */         file.mkdirs();
/*     */       }
/*  74 */       String[] arrDefId = defIds.split(",");
/*  75 */       for (String defId : arrDefId) {
/*  76 */         BpmDefinition def = (BpmDefinition)this.bpmDefinitionManager.get(defId);
/*  77 */         if (null != def) {
/*  78 */           DefinitionImportModel importModel = new DefinitionImportModel();
/*  79 */           SysTreeNode sysTreeNode = (SysTreeNode)this.sysTreeNodeManager.get(def.getTypeId());
/*  80 */           if (null != sysTreeNode) {
/*  81 */             SysTree sysTree = (SysTree)this.sysTreeManager.get(sysTreeNode.getTreeId());
/*  82 */             importModel.setDefTypeKey(sysTreeNode.getKey());
/*  83 */             if (null != sysTree) {
/*  84 */               importModel.setDefTypeTreeKey(sysTree.getKey());
/*     */             }
/*     */           } 
/*  87 */           importModel.setDef(def);
/*  88 */           byte[] sourceExtra = this.repositoryService.getModelEditorSourceExtra(def.getActModelId());
/*  89 */           String pngByte = "";
/*  90 */           if (null != sourceExtra) {
/*  91 */             pngByte = new String(sourceExtra, "utf-8");
/*     */           }
/*  93 */           byte[] source = this.repositoryService.getModelEditorSource(def.getActModelId());
/*  94 */           String editorJson = "";
/*  95 */           if (null != source) {
/*  96 */             editorJson = new String(this.repositoryService.getModelEditorSource(def.getActModelId()), "utf-8");
/*     */           }
/*  98 */           importModel.setJsonXml(editorJson);
/*  99 */           importModel.setPngByte(pngByte);
/* 100 */           importModel.setBpmDefSetting(def.getDefSetting());
/* 101 */           List<SysAuthorization> list = this.sysAuthorizationManager.getByTarget(RightsObjectConstants.FLOW, def.getKey());
/*     */           
/* 103 */           if (null != list)
/* 104 */             list.forEach(authorization -> {
/*     */                   IUser user;
/*     */                   IGroup role;
/*     */                   IGroup post;
/*     */                   IGroup org;
/*     */                   authorization.setId(null);
/*     */                   switch (authorization.getRightsType()) {
/*     */                     case "user":
/*     */                       user = this.userService.getUserById(authorization.getRightsIdentity());
/*     */                       if (null != user) {
/*     */                         authorization.setRightsIdentity(user.getAccount());
/*     */                       }
/*     */                       break;
/*     */                     case "role":
/*     */                       role = this.groupService.getById(GroupTypeConstant.ROLE.key(), authorization.getRightsIdentity());
/*     */                       if (null != role) {
/*     */                         authorization.setRightsIdentity(role.getGroupCode());
/*     */                       }
/*     */                       break;
/*     */                     case "post":
/*     */                       post = this.groupService.getById(GroupTypeConstant.POST.key(), authorization.getRightsIdentity());
/*     */                       if (null != post) {
/*     */                         authorization.setRightsIdentity(post.getGroupCode());
/*     */                       }
/*     */                       break;
/*     */                     case "org":
/*     */                       org = this.groupService.getById(GroupTypeConstant.ORG.key(), authorization.getRightsIdentity());
/*     */                       if (null != org)
/*     */                         authorization.setRightsIdentity(org.getGroupCode()); 
/*     */                       break;
/*     */                   } 
/*     */                 }); 
/* 136 */           importModel.setLstAuthorization(list);
/* 137 */           DefaultQueryFilter filter = new DefaultQueryFilter(true);
/* 138 */           filter.addFilter("def_id_", def.getId(), QueryOP.EQUAL);
/* 139 */           List<BusinessPermission> lstBusinessPermission = this.businessPermissionManager.query((QueryFilter)filter);
/* 140 */           if (null != lstBusinessPermission) {
/* 141 */             lstBusinessPermission.forEach(businessPermission -> businessPermission.cleanBusObjMap());
/*     */           }
/*     */ 
/*     */           
/* 145 */           importModel.setLstBusinessPermission(lstBusinessPermission);
/*     */           
/* 147 */           OutputStream outputStream = new FileOutputStream(filePath + "/" + def.getKey() + ".txt");
/* 148 */           outputStream.write(JSON.toJSONString(importModel, new SerializerFeature[] { SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue }).getBytes("UTF-8"));
/* 149 */           outputStream.close();
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void bpmImport(String filePath) throws Exception {}
/*     */ 
/*     */ 
/*     */   
/*     */   public String bpmImportWithLog(String filePath, boolean notPublish) throws Exception {
/* 162 */     StringBuilder sMsg = new StringBuilder("");
/* 163 */     File fileDirectory = new File(filePath);
/* 164 */     if (fileDirectory.exists()) {
/* 165 */       File[] files = fileDirectory.listFiles();
/* 166 */       for (File file : files) {
/* 167 */         try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"))) {
/* 168 */           StringBuffer buffer = new StringBuffer();
/* 169 */           String line = "";
/* 170 */           while ((line = in.readLine()) != null) {
/* 171 */             buffer.append(line);
/*     */           }
/* 173 */           DefinitionImportModel importModel = (DefinitionImportModel)JSON.parseObject(buffer.toString(), DefinitionImportModel.class);
/* 174 */           sMsg.append("开始导入流程[" + importModel.getDef().getKey() + "]\n");
/* 175 */           SysTree sysTree = this.sysTreeManager.getByKey(importModel.getDefTypeTreeKey());
/* 176 */           String sysTreeNodeId = "";
/* 177 */           if (null != sysTree) {
/* 178 */             SysTreeNode sysTreeNode = this.sysTreeNodeManager.getByTreeIdAndKey(sysTree.getId(), importModel.getDefTypeKey());
/* 179 */             if (null != sysTreeNode) {
/* 180 */               sysTreeNodeId = sysTreeNode.getId();
/*     */             } else {
/* 182 */               sMsg.append("分类树[").append(importModel.getDefTypeTreeKey()).append("]分类节点[").append(importModel.getDefTypeKey()).append("]不存在\n");
/*     */             } 
/*     */           } else {
/* 185 */             sMsg.append("分类树[").append(importModel.getDefTypeTreeKey()).append("]不存在\n");
/*     */           } 
/* 187 */           BpmDefinition bpmDefinition = this.bpmDefinitionManager.getByKey(importModel.getDef().getKey());
/* 188 */           boolean ifNew = false;
/* 189 */           if (null == bpmDefinition) {
/* 190 */             ifNew = true;
/* 191 */             bpmDefinition = new BpmDefinition();
/* 192 */             bpmDefinition.setTypeId(sysTreeNodeId);
/* 193 */             bpmDefinition.setOrder(importModel.getDef().getOrder());
/* 194 */             bpmDefinition.setName(importModel.getDef().getName());
/* 195 */             bpmDefinition.setKey(importModel.getDef().getKey());
/* 196 */             bpmDefinition.setDesc(importModel.getDef().getDesc());
/* 197 */             bpmDefinition.setOrgId(importModel.getDef().getOrgId());
/* 198 */             this.bpmDefinitionManager.create(bpmDefinition);
/*     */           } 
/* 200 */           Model model = this.repositoryService.getModel(bpmDefinition.getActModelId());
/*     */           try {
/* 202 */             Map<String, String> params = new HashMap<>();
/* 203 */             params.put("json_xml", importModel.getJsonXml());
/* 204 */             params.put("pngByte", importModel.getPngByte());
/* 205 */             params.put("name", importModel.getDef().getName());
/* 206 */             params.put("description", importModel.getDef().getDesc());
/* 207 */             params.put("bpmDefSetting", importModel.getBpmDefSetting());
/* 208 */             params.put("rev", "1");
/* 209 */             if (!notPublish && !ifNew) {
/* 210 */               params.put("publish", "true");
/*     */             }
/* 212 */             bpmDefinition = this.bpmDefinitionManager.updateBpmnModel(model, params);
/*     */             
/* 214 */             List<SysAuthorization> lstAuthorization = new ArrayList<>();
/* 215 */             if (null != lstAuthorization) {
/* 216 */               for (int i = lstAuthorization.size() - 1; i >= 0; i--) {
/* 217 */                 IUser user; IGroup role, post, org; SysAuthorization authorization = lstAuthorization.get(i);
/* 218 */                 authorization.setId(null);
/* 219 */                 switch (authorization.getRightsType()) {
/*     */                   case "user":
/* 221 */                     user = this.userService.getUserByAccount(authorization.getRightsIdentity());
/* 222 */                     if (null != user) {
/* 223 */                       authorization.setRightsIdentity(user.getUserId()); break;
/*     */                     } 
/* 225 */                     lstAuthorization.remove(i);
/* 226 */                     sMsg.append("导入权限失败:人员[").append(authorization.getRightsIdentity()).append(":").append(authorization.getRightsIdentityName()).append("]不存在\n");
/*     */                     break;
/*     */                   
/*     */                   case "role":
/* 230 */                     role = this.groupService.getByCode(GroupTypeConstant.ROLE.key(), authorization.getRightsIdentity());
/* 231 */                     if (null != role) {
/* 232 */                       authorization.setRightsIdentity(role.getGroupId()); break;
/*     */                     } 
/* 234 */                     lstAuthorization.remove(i);
/* 235 */                     sMsg.append("导入权限失败:角色[").append(authorization.getRightsIdentity()).append(":").append(authorization.getRightsIdentityName()).append("]不存在\n");
/*     */                     break;
/*     */                   
/*     */                   case "post":
/* 239 */                     post = this.groupService.getByCode(GroupTypeConstant.POST.key(), authorization.getRightsIdentity());
/* 240 */                     if (null != post) {
/* 241 */                       authorization.setRightsIdentity(post.getGroupId()); break;
/*     */                     } 
/* 243 */                     lstAuthorization.remove(i);
/* 244 */                     sMsg.append("导入权限失败:岗位[").append(authorization.getRightsIdentity()).append(":").append(authorization.getRightsIdentityName()).append("]不存在\n");
/*     */                     break;
/*     */                   
/*     */                   case "org":
/* 248 */                     org = this.groupService.getByCode(GroupTypeConstant.ORG.key(), authorization.getRightsIdentity());
/* 249 */                     if (null != org) {
/* 250 */                       authorization.setRightsIdentity(org.getGroupId()); break;
/*     */                     } 
/* 252 */                     lstAuthorization.remove(i);
/* 253 */                     sMsg.append("导入权限失败:机构[").append(authorization.getRightsIdentity()).append(":").append(authorization.getRightsIdentityName()).append("]不存在\n");
/*     */                     break;
/*     */                 } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               } 
/*     */             }
/* 263 */             List<BusinessPermission> lstBusinessPermission = importModel.getLstBusinessPermission();
/* 264 */             this.businessPermissionManager.removeByDefId(bpmDefinition.getId());
/* 265 */             if (null != lstBusinessPermission && lstBusinessPermission.size() > 0) {
/* 266 */               BpmDefinition finalBpmDefinition = bpmDefinition;
/* 267 */               lstBusinessPermission.forEach(permission -> {
/*     */                     permission.setId(null);
/*     */                     permission.setDefId(finalBpmDefinition.getId());
/*     */                     this.businessPermissionManager.create(permission);
/*     */                   });
/*     */             } 
/* 273 */           } catch (XMLException e) {
/* 274 */             sMsg.append("导入失败:流程图解析失败！不合法的流程图：[").append(e.getMessage()).append("]\n");
/*     */           } 
/* 276 */           sMsg.append("结束导入流程[" + importModel.getDef().getKey() + "]\n");
/* 277 */         } catch (Exception e) {
/* 278 */           this.LOG.error("导入流程失败", e);
/* 279 */           sMsg.append("导入失败:流程[").append(file.getName()).append("]失败\n");
/*     */         } 
/*     */       } 
/*     */     } else {
/* 283 */       sMsg.append("导入失败：文件不存在").append(filePath).append("\n");
/*     */     } 
/* 285 */     return sMsg.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public String checkImport(String filePath) throws Exception {
/* 290 */     return null;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/eximport/DefinitionExpImport.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */