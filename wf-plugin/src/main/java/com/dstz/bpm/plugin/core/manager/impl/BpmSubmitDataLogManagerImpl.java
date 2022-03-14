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
