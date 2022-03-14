package com.dstz.bpm.core.manager;

import com.dstz.bpm.core.model.BpmDefinition;
import com.dstz.bpm.core.model.BpmDefinitionDuplicateDTO;
import com.dstz.bpm.core.vo.BpmDefinitionVO;
import com.dstz.base.api.query.QueryFilter;
import com.dstz.base.manager.Manager;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import org.activiti.engine.repository.Model;

public interface BpmDefinitionManager extends Manager<String, BpmDefinition> {
  BpmDefinition updateBpmnModel(Model paramModel, Map<String, String> paramMap) throws Exception;
  
  BpmDefinition getDefByActModelId(String paramString);
  
  BpmDefinition getDefinitionByActDefId(String paramString);
  
  BpmDefinition getByKey(String paramString);
  
  List<BpmDefinitionVO> getMyDefinitionList(String paramString, QueryFilter paramQueryFilter);
  
  String createActModel(BpmDefinition paramBpmDefinition);
  
  List<Map> getDefNumByTree();
  
  void setDefinition2Main(String paramString);
  
  void clearBpmnModelCache(String paramString);
  
  InputStream bpmExport(String paramString);
  
  void bpmImport(File paramFile);
  
  String checkImport(File paramFile);
  
  BpmDefinition duplicate(BpmDefinitionDuplicateDTO paramBpmDefinitionDuplicateDTO);
  
  InputStream definitionExport(String paramString);
  
  String definitionImport(File paramFile, boolean paramBoolean);
  
  void lock(String paramString);
  
  void unlock(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/BpmDefinitionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */