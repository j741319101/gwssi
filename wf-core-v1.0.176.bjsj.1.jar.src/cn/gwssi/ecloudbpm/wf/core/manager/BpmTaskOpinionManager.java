package cn.gwssi.ecloudbpm.wf.core.manager;

import cn.gwssi.ecloudbpm.wf.api.constant.OpinionStatus;
import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.InstanceActionCmd;
import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.TaskActionCmd;
import cn.gwssi.ecloudbpm.wf.api.model.inst.IBpmInstance;
import cn.gwssi.ecloudbpm.wf.api.model.task.IBpmTask;
import cn.gwssi.ecloudbpm.wf.core.model.BpmTaskOpinion;
import cn.gwssi.ecloudbpm.wf.core.vo.BpmTaskOpinionVO;
import cn.gwssi.ecloudframework.base.manager.Manager;
import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
import java.util.Collection;
import java.util.List;

public interface BpmTaskOpinionManager extends Manager<String, BpmTaskOpinion> {
  BpmTaskOpinion getByTaskId(String paramString);
  
  void createOpinionByTask(TaskActionCmd paramTaskActionCmd);
  
  List<BpmTaskOpinion> getByInstAndNode(String paramString1, String paramString2);
  
  List<BpmTaskOpinion> getByInstId(String paramString);
  
  void createOpinion(IBpmTask paramIBpmTask, IBpmInstance paramIBpmInstance, List<SysIdentity> paramList, String paramString1, String paramString2, String paramString3);
  
  void createOpinionByInstance(InstanceActionCmd paramInstanceActionCmd, boolean paramBoolean);
  
  List<BpmTaskOpinion> getByInstAndSignId(String paramString1, String paramString2);
  
  void removeByInstId(String paramString);
  
  void removeByTaskId(String paramString);
  
  void createOpinion(IBpmTask paramIBpmTask, IBpmInstance paramIBpmInstance, List<SysIdentity> paramList, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);
  
  void createOpinion(IBpmTask paramIBpmTask, IBpmInstance paramIBpmInstance, List<SysIdentity> paramList, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);
  
  List<BpmTaskOpinion> getByInst(String paramString1, String paramString2, String paramString3, String paramString4);
  
  List<BpmTaskOpinionVO> getByInstsOpinion(String paramString1, String paramString2, String paramString3, String paramString4, Boolean paramBoolean);
  
  void commonUpdate(String paramString1, OpinionStatus paramOpinionStatus, String paramString2);
  
  List<BpmTaskOpinion> getByInstAndNodeVersion(String paramString1, String paramString2);
  
  List<BpmTaskOpinion> selectByTaskIds(Collection<String> paramCollection);
  
  List<BpmTaskOpinion> getByParam(String paramString1, String paramString2, String paramString3, String paramString4);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/core/manager/BpmTaskOpinionManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */