package com.dstz.bpm.plugin.core.manager.impl;

import com.dstz.bpm.plugin.core.dao.BpmSubmitDataLogDao;
import com.dstz.bpm.plugin.core.manager.BpmSubmitDataLogManager;
import com.dstz.bpm.plugin.core.model.BpmSubmitDataLog;
import com.dstz.base.manager.impl.BaseManager;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

@Service("bpmSubmitDataLogManager")
public class BpmSubmitDataLogManagerImpl extends BaseManager<String, BpmSubmitDataLog> implements BpmSubmitDataLogManager {
  @Resource
  BpmSubmitDataLogDao bpmSubmitDataLogDao;
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-plugin/v1.0.176.bjsj.1/wf-plugin-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/plugin/core/manager/impl/BpmSubmitDataLogManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */