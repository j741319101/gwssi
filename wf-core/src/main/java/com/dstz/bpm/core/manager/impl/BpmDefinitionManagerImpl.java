/*     */
package com.dstz.bpm.core.manager.impl;
/*     */

import com.dstz.bpm.core.dao.BpmDefinitionDao;
import com.dstz.bus.manager.BusinessPermissionManager;
/*     */ import com.dstz.bus.model.BusinessPermission;
/*     */ import com.dstz.bpm.act.cache.ActivitiDefCache;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.engine.event.BpmDefinitionUpdateEvent;
/*     */ import com.dstz.bpm.api.model.def.BpmProcessDef;
/*     */ import com.dstz.bpm.api.model.def.IBpmDefinition;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.SubProcessNodeDef;
/*     */ import com.dstz.bpm.api.service.BpmProcessDefService;
/*     */ import com.dstz.bpm.core.eximport.DefinitionExpImport;
/*     */ import com.dstz.bpm.core.manager.BpmDefinitionManager;
/*     */ import com.dstz.bpm.core.manager.BpmInstanceManager;
/*     */ import com.dstz.bpm.core.model.BpmDefinition;
/*     */ import com.dstz.bpm.core.model.BpmDefinitionDuplicateDTO;
/*     */ import com.dstz.bpm.core.util.ProcessDefValidate;
/*     */ import com.dstz.bpm.core.vo.BpmDefinitionVO;
/*     */ import com.dstz.bpm.engine.model.DefaultBpmProcessDef;
/*     */ import com.dstz.base.api.bpmExpImport.BpmExpImport;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.core.id.IdUtil;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import com.dstz.base.core.util.BeanCopierUtils;
/*     */ import com.dstz.base.core.util.FileUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.query.DefaultQueryFilter;
/*     */ import com.dstz.base.manager.impl.BaseManager;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.api.model.dto.BpmUserDTO;
/*     */ import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.constant.EnvironmentConstant;
/*     */ import com.dstz.sys.api.constant.RightsObjectConstants;
import com.dstz.sys.api.service.SysAuthorizationService;
/*     */ import com.dstz.sys.api.service.SysConnectRecordService;
import com.dstz.sys.core.manager.SysAuthorizationManager;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.map.MapBuilder;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.node.ObjectNode;
/*     */ import java.io.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.*;
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */
/*     */ import javax.annotation.Resource;
/*     */ import org.activiti.bpmn.converter.BpmnXMLConverter;
/*     */ import org.activiti.bpmn.model.BpmnModel;
/*     */ import org.activiti.bpmn.model.Process;
/*     */ import org.activiti.editor.language.json.converter.BpmnJsonConverter;
/*     */ import org.activiti.engine.ProcessEngineConfiguration;
/*     */ import org.activiti.engine.RepositoryService;
/*     */ import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
/*     */ import org.activiti.engine.impl.bpmn.parser.BpmnParse;
/*     */ import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
/*     */ import org.activiti.engine.impl.context.Context;
/*     */ import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
/*     */ import org.activiti.engine.impl.persistence.entity.DeploymentEntity;
/*     */ import org.activiti.engine.impl.util.IoUtil;
import org.activiti.engine.repository.Deployment;
/*     */ import org.activiti.engine.repository.Model;
/*     */ import org.activiti.engine.repository.ProcessDefinition;
/*     */ import org.apache.batik.transcoder.TranscoderInput;
/*     */ import org.apache.batik.transcoder.TranscoderOutput;
/*     */ import org.apache.batik.transcoder.image.PNGTranscoder;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.math.NumberUtils;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.context.ApplicationEvent;
/*     */ import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("bpmDefinitionManager")
public class BpmDefinitionManagerImpl extends BaseManager<String, BpmDefinition> implements BpmDefinitionManager {
    protected Logger LOG = LoggerFactory.getLogger(this.getClass());
    @Resource
    BpmDefinitionDao bpmDefinitionDao;
    @Resource
    BpmProcessDefService bpmProcessDefService;
    @Resource
    RepositoryService repositoryService;
    @Resource
    ProcessEngineConfiguration processEngineConfiguration;
    @Resource
    SysAuthorizationService sysAuthorizationService;
    @Resource
    BpmInstanceManager bpmInstanceManager;
    @Resource
    BusinessPermissionManager businessPermissionManager;
    @Resource
    ProcessDefValidate processDefValidate;
    @Resource
    SysConnectRecordService sysConnectRecordService;
    @Resource
    UserService userService;
    @Resource
    SysAuthorizationManager sysAuthorizationManager;
    @Resource
    ICurrentContext iCurrentContext;

    public BpmDefinitionManagerImpl() {
    }

    public void create(BpmDefinition bpmDefinition) {
        if (StringUtil.isNotEmpty(bpmDefinition.getId())) {
            this.update(bpmDefinition);
        } else {
            List<BpmDefinition> defList = this.bpmDefinitionDao.getByKey(bpmDefinition.getKey());
            if (CollectionUtil.isNotEmpty(defList)) {
                throw new BusinessMessage("流程定义Key重复：" + bpmDefinition.getKey());
            } else {
                bpmDefinition.setIsMain("Y");
                bpmDefinition.setStatus("draft");
                bpmDefinition.setVersion(1);
                String defId = IdUtil.getSuid();
                bpmDefinition.setId(defId);
                bpmDefinition.setMainDefId(defId);
                String modelId = this.createActModel(bpmDefinition);
                bpmDefinition.setActModelId(modelId);
                this.bpmDefinitionDao.create(bpmDefinition);
            }
        }
    }

    public String createActModel(BpmDefinition bpmDefinition) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode editorNode = objectMapper.createObjectNode();
            editorNode.put("id", "canvas");
            editorNode.put("resourceId", "canvas");
            ObjectNode stencilSetNode = objectMapper.createObjectNode();
            stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
            editorNode.set("stencilset", stencilSetNode);
            Model modelData = this.repositoryService.newModel();
            ObjectNode modelObjectNode = objectMapper.createObjectNode();
            modelObjectNode.put("name", bpmDefinition.getName());
            modelObjectNode.put("revision", 1);
            modelObjectNode.put("key", bpmDefinition.getKey());
            modelObjectNode.put("description", bpmDefinition.getDesc());
            modelData.setMetaInfo(modelObjectNode.toString());
            modelData.setName(bpmDefinition.getName());
            modelData.setKey(bpmDefinition.getKey());
            this.repositoryService.saveModel(modelData);
            this.repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
            return modelData.getId();
        } catch (UnsupportedEncodingException var7) {
            throw new RuntimeException("创建activiti流程定义失败！", var7);
        }
    }

    public List<Map> getDefNumByTree() {
        return this.bpmDefinitionDao.getDefNumByTree();
    }

    public BpmDefinition updateBpmnModel(Model model, Map<String, String> values) throws Exception {
        String bpmDefSettingJSON = (String) values.get("bpmDefSetting");
        BpmDefinition bpmDef = this.getDefByActModelId(model.getId());
        if (StringUtils.isNotEmpty(bpmDef.getId())) {
            BpmDefinition bpmDefinition = this.get(bpmDef.getId());
            String lockBy = bpmDefinition.getLockBy();
            String currentUserId = this.iCurrentContext.getCurrentUserId();
            if (StringUtils.isNotEmpty(lockBy) && !currentUserId.equals(lockBy)) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                IUser user = this.userService.getUserById(lockBy);
                throw new BusinessException("保存失败,当前已经被用户[" + user.getFullname() + "]在时间[" + simpleDateFormat.format(bpmDefinition.getLockTime()) + "]锁定");
            }
        }

        bpmDef.setName((String) values.get("name"));
        bpmDef.setDesc((String) values.get("description"));
        bpmDef.setRev(Integer.valueOf((String) values.get("rev")));
        bpmDef.setDefSetting(bpmDefSettingJSON);
        byte[] jsonXml = ((String) values.get("json_xml")).getBytes("utf-8");
        ObjectNode modelNode = (ObjectNode) (new ObjectMapper()).readTree(jsonXml);
        BpmnModel bpmnModel = (new BpmnJsonConverter()).convertToBpmnModel(modelNode);
        if (CollectionUtil.isEmpty(bpmnModel.getProcesses())) {
            throw new BusinessMessage("请绘制流程图后再保存！");
        } else {
            if (!bpmnModel.getProcesses().isEmpty()) {
                ((Process) bpmnModel.getProcesses().get(0)).setName(bpmDef.getName());
                ((Process) bpmnModel.getProcesses().get(0)).setDocumentation(bpmDef.getDesc());
                ((Process) bpmnModel.getProcesses().get(0)).setId(bpmDef.getKey());
            }

            byte[] bpmnBytes = (new BpmnXMLConverter()).convertToXML(bpmnModel);
            String oldDefId = bpmDef.getId();
            boolean publish = Boolean.parseBoolean((String) values.get("publish"));
            if (!StringUtil.isEmpty(bpmDef.getActDefId()) && !publish) {
                this.saveModelInfo(model, values);
                this.updateProcessDef(bpmDef, model, bpmnBytes);
            } else {
                bpmDef = this.deploy(bpmDef, model, values, bpmnBytes);
            }

            DefaultBpmProcessDef processDef = (DefaultBpmProcessDef) this.bpmProcessDefService.getBpmProcessDef(bpmDef.getId());
            Set<String> nodeIds = new HashSet();
            nodeIds.add(bpmDef.getKey() + "-global");
            Iterator var13 = processDef.getBpmnNodeDefs().iterator();

            while (var13.hasNext()) {
                BpmNodeDef nodeDef = (BpmNodeDef) var13.next();
                nodeIds.add(bpmDef.getKey() + "-" + nodeDef.getNodeId());
                if (nodeDef instanceof SubProcessNodeDef) {
                    for (BpmNodeDef subNodeDef : ((SubProcessNodeDef) nodeDef).getChildBpmProcessDef().getBpmnNodeDefs()) {
                        nodeIds.add(bpmDef.getKey() + "-" + subNodeDef.getNodeId());
                    }
                }
            }

            this.businessPermissionManager.removeNotInBpmNode(bpmDef.getId(), bpmDef.getKey(), nodeIds);
            if (!StringUtils.equals(oldDefId, bpmDef.getId())) {
                QueryFilter queryFilter = new DefaultQueryFilter(true);
                queryFilter.addFilter("def_id_", oldDefId, QueryOP.EQUAL);
                List<BusinessPermission> businessPermissions = this.businessPermissionManager.query(queryFilter);
                String newDefId = bpmDef.getId();
                businessPermissions.forEach((businessPermission) -> {
                    businessPermission.setId(IdUtil.getSuid());
                    businessPermission.setDefId(newDefId);
                    this.businessPermissionManager.create(businessPermission);
                });
            } else {
                oldDefId = null;
            }

            List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
            Iterator var24 = extendTaskActions.iterator();

            while (var24.hasNext()) {
                IExtendTaskAction iExtendTaskAction = (IExtendTaskAction) var24.next();
                iExtendTaskAction.doSomethingWhenSaveDef(processDef, oldDefId);
            }

            return bpmDef;
        }
    }

    private void saveModelInfo(Model model, Map<String, String> values) {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        try {
            byte[] jsonXml = ((String) values.get("json_xml")).getBytes("utf-8");
            byte[] svg_xml = null;
//            byte[] svg_xml;
            if (values.containsKey("pngByte")) {
                svg_xml = ((String) values.get("pngByte")).getBytes("utf-8");
            } else {
                InputStream svgStream = new ByteArrayInputStream(((String) values.get("svg_xml")).getBytes("utf-8"));
                TranscoderInput input = new TranscoderInput(svgStream);
                PNGTranscoder transcoder = new PNGTranscoder();
                TranscoderOutput output = new TranscoderOutput(outStream);
                transcoder.transcode(input, output);
                svg_xml = outStream.toByteArray();
                outStream.close();
            }

            this.repositoryService.saveModel(model);
            this.repositoryService.addModelEditorSourceExtra(model.getId(), svg_xml);
            this.repositoryService.addModelEditorSource(model.getId(), jsonXml);
        } catch (Exception var10) {
            throw new BusinessException("保存Model信息失败！", var10);
        }
    }

    private void updateProcessDef(BpmDefinition definition, Model model, byte[] bpmnBytes) throws JsonProcessingException, IOException {
        ProcessDefinition bpmnProcessDef = this.repositoryService.getProcessDefinition(definition.getActDefId());
        ProcessEngineConfigurationImpl conf = (ProcessEngineConfigurationImpl) this.processEngineConfiguration;
        Context.setProcessEngineConfiguration(conf);
        DeploymentManager deploymentManager = conf.getDeploymentManager();
        BpmnDeployer deployer = (BpmnDeployer) deploymentManager.getDeployers().get(0);
        DeploymentEntity deploy = (DeploymentEntity) this.repositoryService.createDeploymentQuery().deploymentId(definition.getActDeployId()).list().get(0);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bpmnBytes);
        BpmnParse bpmnParse = deployer.getBpmnParser().createParse().sourceInputStream(inputStream).setSourceSystemId(model.getKey() + ".bpmn20.xml").deployment(deploy).name(model.getKey() + ".bpmn20.xml");
        bpmnParse.execute();
        BpmnModel bpmnModel = bpmnParse.getBpmnModel();
        deploymentManager.getBpmnModelCache().add(bpmnProcessDef.getId(), bpmnModel);
        byte[] diagramBytes = IoUtil.readInputStream(this.processEngineConfiguration.getProcessDiagramGenerator().generateDiagram(bpmnModel, "png", this.processEngineConfiguration.getActivityFontName(), this.processEngineConfiguration.getLabelFontName(), this.processEngineConfiguration.getAnnotationFontName(), this.processEngineConfiguration.getClassLoader()), (String) null);
        this.bpmDefinitionDao.updateActResourceEntity(bpmnProcessDef.getDeploymentId(), model.getKey() + ".bpmn20.xml", bpmnBytes);
        this.bpmDefinitionDao.updateActResourceEntity(bpmnProcessDef.getDeploymentId(), model.getKey() + "." + bpmnProcessDef.getKey() + ".png", diagramBytes);
        this.setDefinitionProp(definition);
        this.update(definition);
        ActivitiDefCache.clearByDefId(definition.getActDefId());
        this.bpmProcessDefService.clean(definition.getId());
        this.bpmProcessDefService.getBpmProcessDef(definition.getId());
        this.publishEvent(definition);
    }

    private void setDefinitionProp(BpmDefinition bpmDef) {
        DefaultBpmProcessDef def = (DefaultBpmProcessDef) this.bpmProcessDefService.initBpmProcessDef(bpmDef);
        if ("deploy".equals(bpmDef.getStatus()) && "deploy".equals(def.getExtProperties().getStatus()) && !AppUtil.getCtxEnvironment().contains(EnvironmentConstant.PROD.key())) {
            throw new BusinessMessage("除了生产环境外，已发布状态的流程禁止修改！");
        } else {
            bpmDef.setStatus(def.getExtProperties().getStatus());
            bpmDef.setSupportMobile(def.getExtProperties().getSupportMobile());
            this.processDefValidate.validate(def);
        }
    }

    private BpmDefinition deploy(BpmDefinition definition, Model preModel, Map<String, String> values, byte[] bpmnModelXml) throws UnsupportedEncodingException {
        String processName = definition.getKey() + ".bpmn20.xml";
        Deployment deployment = this.repositoryService.createDeployment().name(definition.getKey()).addString(processName, new String(bpmnModelXml, "utf-8")).deploy();
        ProcessDefinition proDefinition = (ProcessDefinition) this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
        if (proDefinition == null) {
            throw new RuntimeException("error   ");
        } else if (StringUtil.isEmpty(definition.getActDefId())) {
            definition.setActDefId(proDefinition.getId());
            definition.setActDeployId(deployment.getId());
            this.update(definition);
            this.saveModelInfo(preModel, values);
            return definition;
        } else {
            String modelId = this.createActModel(definition);
            Model model = this.repositoryService.getModel(modelId);
            model.setDeploymentId(deployment.getId());
            this.repositoryService.saveModel(model);
            this.saveModelInfo(model, values);
            String newDefId = IdUtil.getSuid();
            BpmDefinition main = this.bpmDefinitionDao.getMainByDefKey(definition.getKey());
            main.setIsMain("N");
            main.setMainDefId(newDefId);
            this.update(main);
            int maxVersion = this.bpmDefinitionDao.getFlowMaxVersion(definition.getKey());
            String oldDefId = definition.getId();
            definition.setId(newDefId);
            definition.setIsMain("Y");
            definition.setRev(0);
            definition.setMainDefId(newDefId);
            definition.setVersion(maxVersion + 1);
            definition.setCreateBy(ContextUtil.getCurrentUser().getUserId());
            definition.setCreateTime(new Date());
            definition.setActDefId(proDefinition.getId());
            definition.setActDeployId(deployment.getId());
            definition.setActModelId(model.getId());
            this.bpmDefinitionDao.create(definition);
            this.bpmDefinitionDao.updateForMainVersion(newDefId, definition.getKey(), newDefId);
            return definition;
        }
    }

    public BpmDefinition getDefByActModelId(String actModelId) {
        List<BpmDefinition> list = this.bpmDefinitionDao.getDefByActModelId(actModelId);
        if (CollectionUtil.isEmpty(list)) {
            throw new BusinessException("getDefByActModelId 查找失败modelId：" + actModelId);
        } else {
            if (list.size() > 1) {
                this.LOG.warn("getDefByActModelId 查找多条 modelId：" + actModelId);
            }

            Iterator var3 = list.iterator();

            BpmDefinition def;
            do {
                if (!var3.hasNext()) {
                    return (BpmDefinition) list.get(0);
                }

                def = (BpmDefinition) var3.next();
            } while (!"Y".equals(def.getIsMain()));

            return def;
        }
    }

    private void publishEvent(BpmDefinition def) {
        List<BpmDefinition> defList = this.bpmDefinitionDao.getByKey(def.getKey());
        Iterator var3 = defList.iterator();

        while (var3.hasNext()) {
            BpmDefinition defEntity = (BpmDefinition) var3.next();
            AppUtil.publishEvent(new BpmDefinitionUpdateEvent(defEntity));
        }

        AppUtil.publishEvent(new BpmDefinitionUpdateEvent(def));
    }

    public BpmDefinition getDefinitionByActDefId(String actDefId) {
        return this.bpmDefinitionDao.getByActDefId(actDefId);
    }

    public BpmDefinition getByKey(String flowKey) {
        return this.bpmDefinitionDao.getMainByDefKey(flowKey);
    }

    public List<BpmDefinitionVO> getMyDefinitionList(String userId, QueryFilter queryFilter) {
        Map map = this.sysAuthorizationService.getUserRightsSql(RightsObjectConstants.FLOW, userId, "bpm_definition.key_");
        queryFilter.addParams(map);
        queryFilter.addFilter("status_", "forbidden", QueryOP.NOT_EQUAL);
        List<BpmDefinitionVO> lstBpmDefinitionTemp = this.bpmDefinitionDao.getMyDefinitionList(queryFilter);
        List<BpmDefinitionVO> lstBpmDefinition = new ArrayList();
        List<BpmUserDTO> lstOrg = this.userService.getUserOrgInfos(userId);
        if (null != lstBpmDefinitionTemp && null != lstOrg && lstBpmDefinitionTemp.size() > 0 && lstOrg.size() > 0) {
            for (BpmDefinitionVO bpmDefinitionVO : lstBpmDefinitionTemp) {
                List<BpmUserDTO> lstOrgTemp = new ArrayList<>();
                lstOrg.forEach(org -> {
                    DefaultQueryFilter defaultQueryFilter = new DefaultQueryFilter();
                    defaultQueryFilter.addFilter("rights_object_", "FLOW", QueryOP.EQUAL);
                    defaultQueryFilter.addFilter("rights_target_", bpmDefinitionVO.getKey(), QueryOP.EQUAL);
                    defaultQueryFilter.addFilter("rights_type_", "org", QueryOP.EQUAL);
                    List temp = this.sysAuthorizationManager.query((QueryFilter) defaultQueryFilter);
                    if (null != temp && temp.size() > 0) {
                        defaultQueryFilter = new DefaultQueryFilter();
                        defaultQueryFilter.addFilter("rights_object_", "FLOW", QueryOP.EQUAL);
                        defaultQueryFilter.addFilter("rights_target_", bpmDefinitionVO.getKey(), QueryOP.EQUAL);
                        defaultQueryFilter.addFilter("rights_permission_code_", org.getOrgId() + "-org", QueryOP.EQUAL);
                        temp = this.sysAuthorizationManager.query((QueryFilter) defaultQueryFilter);
                        if (null != temp && temp.size() > 0) {
                            lstOrgTemp.add(org);
                        }
                    } else {
                        lstOrgTemp.add(org);
                    }
                });

                if (lstOrgTemp.size() > 1) {
                    handleOrgName(lstOrgTemp);
                }
                lstOrgTemp.forEach(org -> {
                    BpmDefinitionVO temp = (BpmDefinitionVO) BeanCopierUtils.transformBean(bpmDefinitionVO, BpmDefinitionVO.class);
                    temp.setOrgId(org.getOrgId());
                    if (lstOrgTemp.size() > 1) {
                        temp.setOrgName(org.getOrgName());
                    }
                    lstBpmDefinition.add(temp);
                });
            }
        }

        return lstBpmDefinition;
    }

    private void handleOrgName(List<BpmUserDTO> lstOrg) {
        HashSet<String> names = new HashSet();
        HashSet<String> namesTemp = new HashSet();
        lstOrg.forEach((org) -> {
            if (names.contains(org.getOrgName())) {
                namesTemp.add(org.getOrgName());
            }

            names.add(org.getOrgName());
        });
        if (namesTemp.size() > 0) {
            lstOrg.forEach((org) -> {
                if (namesTemp.contains(org.getOrgName()) && "3".equals(org.getOrgType())) {
                    org.setOrgName(org.getParentOrgName() + "/" + org.getOrgName());
                }

            });
        }

    }

    public void remove(String entityId) {
        BpmDefinition definition = (BpmDefinition) this.bpmDefinitionDao.get(entityId);
        if (this.isNotEmptyInstance(definition.getId())) {
            throw new BusinessMessage("该流程定义下存在流程实例，请勿删除！<br> 请清除数据后再来删除");
        } else {
            this.businessPermissionManager.removeByBpmDefKey(definition.getId(), "flow", definition.getKey());
            List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
            BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(definition.getId());
            extendTaskActions.forEach((iExtendTaskAction) -> {
                iExtendTaskAction.doSomethingWhenDeleteDef(bpmProcessDef);
            });
            AppUtil.publishEvent(new BpmDefinitionUpdateEvent(definition));
            this.bpmDefinitionDao.remove(definition.getId());
            if (StringUtil.isNotEmpty(definition.getActDeployId())) {
                this.repositoryService.deleteDeployment(definition.getActDeployId());
            }

            this.sysConnectRecordService.removeBySourceId(definition.getId(), (String) null);
            if (StringUtil.isNotEmpty(definition.getActModelId())) {
                this.repositoryService.deleteModel(definition.getActModelId());
            }

        }
    }

    private boolean isNotEmptyInstance(String defId) {
        DefaultQueryFilter query = new DefaultQueryFilter();
        query.addFilter("def_id_", defId, QueryOP.EQUAL);
        List list = this.bpmInstanceManager.query(query);
        return CollectionUtil.isNotEmpty(list);
    }

    public void update(BpmDefinition entity) {
        entity.setUpdateTime(new Date());
        int updateRows = this.bpmDefinitionDao.update(entity);
        AppUtil.publishEvent(new BpmDefinitionUpdateEvent(entity));
        if (updateRows == 0) {
            throw new RuntimeException("流程定义更新失败，当前版本并非最新版本！已经刷新当前服务器缓存，请刷新页面重新修改再提交。id:" + entity.getId() + " reversion:" + entity.getRev());
        }
    }

    public void setDefinition2Main(String definitionId) {
        BpmDefinition def = this.get(definitionId);
        def.setIsMain("Y");
        BpmDefinition oldDpmDefinition = this.getByKey(def.getKey());
        this.update(def);
        this.bpmDefinitionDao.updateForMainVersion(definitionId, def.getKey(), def.getId());
        BpmProcessDef bpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(def.getId());
        List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
        extendTaskActions.forEach((iExtendTaskAction) -> {
            iExtendTaskAction.doSomethingWhenSaveDef(bpmProcessDef, oldDpmDefinition.getId());
        });
    }

    public void clearBpmnModelCache(String actDefId) {
        ProcessEngineConfigurationImpl conf = (ProcessEngineConfigurationImpl) this.processEngineConfiguration;
        Context.setProcessEngineConfiguration(conf);
        DeploymentManager deploymentManager = conf.getDeploymentManager();
        if (StringUtil.isNotEmpty(actDefId)) {
            deploymentManager.getBpmnModelCache().remove(actDefId);
        } else {
            deploymentManager.getBpmnModelCache().clear();
        }

    }

    public InputStream bpmExport(String defIds) {
        String filePath = "/tmp/export";
        FileUtil.deleteFiles(filePath);
        FileUtil.deleteFiles(filePath + ".zip");
        List expImports = AppUtil.getImplInstanceArray(BpmExpImport.class);

        try {
            Iterator var4 = expImports.iterator();

            while (var4.hasNext()) {
                BpmExpImport expImport = (BpmExpImport) var4.next();
                expImport.bpmExport(defIds, filePath);
            }
        } catch (Exception var7) {
            throw new BusinessException("导出出错", var7);
        }

        FileInputStream in = null;

        try {
            FileUtil.toZipDir(filePath, filePath + ".zip", true);
            in = new FileInputStream(filePath + ".zip");
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return in;
    }

    public void bpmImport(File file) {
        String filePath = "/tmp/export";
        FileUtil.deleteFiles(filePath);
        FileUtil.deleteFiles(filePath + ".zip");

        try {
            FileUtil.unZipFiles(file, filePath);
        } catch (Exception var8) {
            throw new BusinessException(var8);
        }

        List<BpmExpImport> expImports = AppUtil.getImplInstanceArray(BpmExpImport.class);
        Iterator var4 = expImports.iterator();

        while (var4.hasNext()) {
            BpmExpImport expImport = (BpmExpImport) var4.next();

            try {
                expImport.bpmImport(filePath + "/" + filePath.substring(filePath.lastIndexOf("/") + 1));
            } catch (Exception var7) {
                throw new BusinessException("导入出错", var7);
            }
        }

    }

    public String checkImport(File file) {
        String filePath = "/tmp/export";
        FileUtil.deleteFiles(filePath);
        FileUtil.deleteFiles(filePath + ".zip");

        try {
            FileUtil.unZipFiles(file, filePath);
        } catch (Exception var9) {
            throw new BusinessException(var9);
        }

        StringBuffer sb = new StringBuffer();
        List<BpmExpImport> expImports = AppUtil.getImplInstanceArray(BpmExpImport.class);
        Iterator var5 = expImports.iterator();

        while (var5.hasNext()) {
            BpmExpImport expImport = (BpmExpImport) var5.next();

            try {
                sb.append(expImport.checkImport(filePath + "/" + filePath.substring(filePath.lastIndexOf("/") + 1))).append("\n");
            } catch (Exception var8) {
                throw new BusinessException("检查出错", var8);
            }
        }

        return sb.toString();
    }

    @Transactional(
            rollbackFor = {Exception.class}
    )
    public BpmDefinition duplicate(BpmDefinitionDuplicateDTO bpmDefinitionDuplicateDTO) {
        if (this.bpmDefinitionDao.countByKey(bpmDefinitionDuplicateDTO.getNewKey())) {
            throw new BusinessMessage("流程定义【" + bpmDefinitionDuplicateDTO.getNewKey() + "】已存在");
        } else {
            List<BpmDefinition> originBpmDefinitionList = this.bpmDefinitionDao.getByKey(bpmDefinitionDuplicateDTO.getOriginKey());
            if (CollectionUtil.isEmpty(originBpmDefinitionList)) {
                throw new BusinessMessage("流程定义【" + bpmDefinitionDuplicateDTO.getOriginKey() + "】不存在");
            } else {
                BpmDefinition originBpmDefinition = null;
                Iterator var4 = originBpmDefinitionList.iterator();

                while (var4.hasNext()) {
                    BpmDefinition bpmDefinition = (BpmDefinition) var4.next();
                    if (StringUtils.equals(bpmDefinition.getIsMain(), "Y")) {
                        originBpmDefinition = bpmDefinition;
                        break;
                    }
                }

                if (originBpmDefinition == null) {
                    throw new BusinessMessage("流程定义【" + bpmDefinitionDuplicateDTO.getOriginKey() + "】主版本不存在");
                } else {
                    BpmnModel bpmnModel = this.repositoryService.getBpmnModel(originBpmDefinition.getActDefId());
                    bpmnModel.getMainProcess().setId(bpmDefinitionDuplicateDTO.getNewKey());
                    bpmnModel.getMainProcess().setName(bpmDefinitionDuplicateDTO.getNewName());
                    String bpmxXml = new String((new BpmnXMLConverter()).convertToXML(bpmnModel, StandardCharsets.UTF_8.displayName()), StandardCharsets.UTF_8);
                    Deployment deployment = this.repositoryService.createDeployment().name(bpmDefinitionDuplicateDTO.getNewKey()).addString(StringUtils.join(new String[]{bpmDefinitionDuplicateDTO.getNewKey(), ".bpmn20.xml"}), bpmxXml).deploy();
                    ProcessDefinition processDefinition = (ProcessDefinition) this.repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).singleResult();
                    Model model = this.duplicateActModel(bpmDefinitionDuplicateDTO, originBpmDefinition, deployment);
                    BpmDefinition newBpmDefinition = new BpmDefinition();
                    newBpmDefinition.setId(IdUtil.getSuid());
                    newBpmDefinition.setName(bpmDefinitionDuplicateDTO.getNewName());
                    newBpmDefinition.setKey(bpmDefinitionDuplicateDTO.getNewKey());
                    newBpmDefinition.setDesc(bpmDefinitionDuplicateDTO.getNewDesc());
                    newBpmDefinition.setTypeId(bpmDefinitionDuplicateDTO.getNewTypeId());
                    newBpmDefinition.setStatus(originBpmDefinition.getStatus());
                    newBpmDefinition.setActDefId(processDefinition.getId());
                    newBpmDefinition.setActModelId(model.getId());
                    newBpmDefinition.setActDeployId(deployment.getId());
                    newBpmDefinition.setVersion(NumberUtils.INTEGER_ONE);
                    newBpmDefinition.setIsMain("Y");
                    newBpmDefinition.setCreateBy(ContextUtil.getCurrentUserId());
                    newBpmDefinition.setCreateTime(new Date());
                    newBpmDefinition.setCreateOrgId(ContextUtil.getCurrentGroupId());
                    newBpmDefinition.setUpdateBy(newBpmDefinition.getCreateBy());
                    newBpmDefinition.setUpdateTime(newBpmDefinition.getCreateTime());
                    newBpmDefinition.setSupportMobile(originBpmDefinition.getSupportMobile());
                    newBpmDefinition.setRev(NumberUtils.INTEGER_ZERO);
                    JSONObject defSetting = JSON.parseObject(originBpmDefinition.getDefSetting());
                    defSetting.put("bpmDefinition", newBpmDefinition);
                    newBpmDefinition.setDefSetting(defSetting.toJSONString());
                    this.bpmDefinitionDao.create(newBpmDefinition);
                    BpmProcessDef newBpmProcessDef = this.bpmProcessDefService.getBpmProcessDef(newBpmDefinition.getId());
                    String oldDefId = originBpmDefinition.getId();
                    QueryFilter queryFilter = new DefaultQueryFilter(true);
                    queryFilter.addFilter("def_id_", oldDefId, QueryOP.EQUAL);
                    List<BusinessPermission> businessPermissions = this.businessPermissionManager.query(queryFilter);
                    businessPermissions.forEach((businessPermission) -> {
                        businessPermission.setId(IdUtil.getSuid());
                        businessPermission.setDefId(newBpmDefinition.getId());
                        this.businessPermissionManager.create(businessPermission);
                    });
                    AppUtil.getImplInstanceArray(IExtendTaskAction.class).forEach((action) -> {
                        action.doSomethingWhenSaveDef(newBpmProcessDef, oldDefId);
                    });
                    return newBpmDefinition;
                }
            }
        }
    }

    private Model duplicateActModel(BpmDefinitionDuplicateDTO bpmDefinitionDuplicateDTO, BpmDefinition originBpmDefinition, Deployment deployment) {
        Model model = this.repositoryService.newModel();
        model.setKey(bpmDefinitionDuplicateDTO.getNewKey());
        model.setName(bpmDefinitionDuplicateDTO.getNewName());
        model.setDeploymentId(deployment.getId());
        model.setMetaInfo(JSON.toJSONString(MapBuilder.create(new LinkedHashMap()).put("key", bpmDefinitionDuplicateDTO.getNewKey()).put("name", bpmDefinitionDuplicateDTO.getNewName()).put("description", bpmDefinitionDuplicateDTO.getNewDesc()).put("revision", "1").build()));
        this.repositoryService.saveModel(model);
        this.repositoryService.addModelEditorSource(model.getId(), this.repositoryService.getModelEditorSource(originBpmDefinition.getActModelId()));
        this.repositoryService.addModelEditorSourceExtra(model.getId(), this.repositoryService.getModelEditorSourceExtra(originBpmDefinition.getActModelId()));
        return model;
    }

    public InputStream definitionExport(String defIds) {
        String filePath = "/tmp/export";
        FileUtil.deleteFiles(filePath);
        FileUtil.deleteFiles(filePath + ".zip");
        List expImports = AppUtil.getImplInstanceArray(DefinitionExpImport.class);

        try {
            Iterator var4 = expImports.iterator();

            while (var4.hasNext()) {
                BpmExpImport expImport = (BpmExpImport) var4.next();
                expImport.bpmExport(defIds, filePath);
            }
        } catch (Exception var7) {
            throw new BusinessException("导出出错", var7);
        }

        FileInputStream in = null;

        try {
            FileUtil.toZipDir(filePath, filePath + ".zip", true);
            in = new FileInputStream(filePath + ".zip");
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return in;
    }

    public String definitionImport(File file, boolean notPublish) {
        String filePath = "/tmp/export";
        FileUtil.deleteFiles(filePath);
        FileUtil.deleteFiles(filePath + ".zip");

        try {
            FileUtil.unZipFiles(file, filePath);
        } catch (Exception var10) {
            throw new BusinessException(var10);
        }

        List<DefinitionExpImport> expImports = AppUtil.getImplInstanceArray(DefinitionExpImport.class);
        StringBuilder sMsg = new StringBuilder("开始导入\n");
        Iterator var6 = expImports.iterator();

        while (var6.hasNext()) {
            DefinitionExpImport expImport = (DefinitionExpImport) var6.next();

            try {
                sMsg.append(expImport.bpmImportWithLog(filePath + "/" + filePath.substring(filePath.lastIndexOf("/") + 1), notPublish));
            } catch (Exception var9) {
                throw new BusinessException("导入出错", var9);
            }
        }

        sMsg.append("结束导入\n");
        return sMsg.toString();
    }

    public List<BpmDefinition> query(QueryFilter queryFilter) {
        List<BpmDefinition> lst = this.dao.query(queryFilter);
        return lst;
    }

    public BpmDefinition get(String entityId) {
        BpmDefinition temp = (BpmDefinition) this.dao.get(entityId);
        return temp;
    }

    public void lock(String id) {
        String userId = this.iCurrentContext.getCurrentUserId();
        if (StringUtils.isNotEmpty(userId)) {
            BpmDefinition bpmDefinitionBase = (BpmDefinition) this.bpmDefinitionDao.get(id);
            if (StringUtils.isEmpty(bpmDefinitionBase.getLockBy())) {
                BpmDefinition bpmDefinition = new BpmDefinition();
                bpmDefinition.setId(id);
                bpmDefinition.setLockBy(userId);
                bpmDefinition.setLockTime(new Date());
                this.bpmDefinitionDao.lock(bpmDefinition);
            } else if (userId.equals(bpmDefinitionBase.getLockBy())) {
                throw new BusinessMessage("已被您锁定,请勿重复操作");
            } else {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                IUser user = this.userService.getUserById(bpmDefinitionBase.getLockBy());
                throw new BusinessMessage("锁定失败,当前已经被用户[" + user.getFullname() + "]在时间[" + simpleDateFormat.format(bpmDefinitionBase.getLockTime()) + "]锁定");
            }
        } else {
            throw new BusinessMessage("当前用户信息丢失");
        }
    }

    public void unlock(String id) {
        String userId = this.iCurrentContext.getCurrentUserId();
        if (StringUtils.isNotEmpty(userId)) {
            BpmDefinition bpmDefinitionBase = (BpmDefinition) this.bpmDefinitionDao.get(id);
            if (StringUtils.isEmpty(bpmDefinitionBase.getLockBy())) {
                throw new BusinessMessage("解锁失败,当前流程并没有上锁");
            } else if (!userId.equals(bpmDefinitionBase.getLockBy()) && !this.iCurrentContext.isAdmin(this.iCurrentContext.getCurrentUser())) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                IUser user = this.userService.getUserById(bpmDefinitionBase.getLockBy());
                throw new BusinessMessage("解锁失败,当前已经被用户[" + user.getFullname() + "]在时间[" + simpleDateFormat.format(bpmDefinitionBase.getLockTime()) + "]锁定");
            } else {
                BpmDefinition bpmDefinition = new BpmDefinition();
                bpmDefinition.setId(id);
                bpmDefinition.setLockBy("");
                this.bpmDefinitionDao.unlock(bpmDefinition);
            }
        } else {
            throw new BusinessMessage("当前用户信息丢失");
        }
    }
}
