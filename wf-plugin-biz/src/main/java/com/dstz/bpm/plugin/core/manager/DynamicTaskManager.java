package com.dstz.bpm.plugin.core.manager;

import com.dstz.bpm.core.model.BpmTaskStack;
import com.dstz.bpm.plugin.core.model.DynamicTask;
import com.dstz.base.manager.Manager;
import java.util.List;
import java.util.Map;

public interface DynamicTaskManager extends Manager<String, DynamicTask> {
   DynamicTask getByStatus(String var1, String var2, String var3);

   DynamicTask getDynamicTaskSettingByInstanceId(String var1, String var2);

   List<BpmTaskStack> getTaskStackByInstIdAndNodeId(String var1, String var2);

   List<Map> getDynamicTaskByInstIdAndNodeId(String var1, String var2);

   void removeByInstId(String var1);

   List<DynamicTask> getByTaskId(String var1);

   void updateEndByInstId(String var1);
}
