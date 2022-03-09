/*     */ package cn.gwssi.ecloudbpm.wf.api.engine.context;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.ActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.action.cmd.BaseActionCmd;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.event.BpmActivitiDefCache;
/*     */ import cn.gwssi.ecloudbpm.wf.api.exception.BpmStatusCode;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.def.BpmProcessDef;
/*     */ import cn.gwssi.ecloudbpm.wf.api.model.inst.BpmExecutionStack;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import java.util.ArrayDeque;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BpmContext
/*     */ {
/*  29 */   private static ThreadLocal<Stack<ActionCmd>> threadActionModel = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */   
/*  33 */   private static ThreadLocal<Map<String, BpmProcessDef>> threadBpmProcessDef = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */   
/*  37 */   private static ThreadLocal<Stack<SysIdentity>> threadMulInstIdentities = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */   
/*  41 */   private static ThreadLocal<Stack<String>> threadMulInstOpTrace = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  46 */   private static ThreadLocal<Map<String, BpmExecutionStack>> threadDynamictaskStack = new ThreadLocal<>();
/*     */   
/*  48 */   private static ThreadLocal<String> threadMulSequenceLine = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */   
/*  52 */   private static ThreadLocal<List<SysIdentity>> threadNextIdentitys = new ThreadLocal<>();
/*     */   
/*     */   public static void setActionModel(ActionCmd actionModel) {
/*  55 */     if (actionModel == null || StringUtil.isEmpty(actionModel.getActionName())) {
/*  56 */       throw new BusinessException("set thread actionModel error , actionModel canot be null", BpmStatusCode.ACTIONCMD_ERROR);
/*     */     }
/*  58 */     getStack(threadActionModel).push(actionModel);
/*     */   }
/*     */   
/*  61 */   private static ThreadLocal<String> threadOptionVersion = new ThreadLocal<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ActionCmd getActionModel() {
/*  69 */     Stack<ActionCmd> stack = getStack(threadActionModel);
/*  70 */     if (stack.isEmpty()) {
/*  71 */       return null;
/*     */     }
/*  73 */     return stack.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BaseActionCmd getActionModel(String processInstanceId) {
/*  83 */     if (StringUtil.isEmpty(processInstanceId)) return (BaseActionCmd)getActionModel();
/*     */     
/*  85 */     Stack<ActionCmd> stack = getStack(threadActionModel);
/*  86 */     if (stack.size() <= 1) return (BaseActionCmd)stack.peek();
/*     */ 
/*     */     
/*  89 */     for (int i = stack.size() - 1; i >= 0; i--) {
/*  90 */       ActionCmd cmd = stack.get(i);
/*  91 */       if (cmd.getBpmInstance() != null && cmd.getBpmInstance().getActInstId().equals(processInstanceId)) {
/*  92 */         return (BaseActionCmd)cmd;
/*     */       }
/*     */     } 
/*     */     
/*  96 */     return null;
/*     */   }
/*     */   
/*     */   public static ActionCmd getTopActionModel() {
/* 100 */     Stack<ActionCmd> stack = getStack(threadActionModel);
/* 101 */     if (stack.isEmpty()) {
/* 102 */       return null;
/*     */     }
/* 104 */     return stack.firstElement();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ActionCmd submitActionModel() {
/* 113 */     Stack<ActionCmd> stack = getStack(threadActionModel);
/* 114 */     if (stack.isEmpty()) {
/* 115 */       return null;
/*     */     }
/* 117 */     return stack.firstElement();
/*     */   }
/*     */   
/*     */   public static void removeActionModel() {
/* 121 */     Stack<ActionCmd> stack = getStack(threadActionModel);
/* 122 */     if (!stack.isEmpty()) {
/* 123 */       stack.pop();
/*     */     }
/*     */   }
/*     */   
/*     */   public static BpmProcessDef getProcessDef(String defId) {
/* 128 */     Map<String, BpmProcessDef> map = getThreadMap(threadBpmProcessDef);
/* 129 */     return map.get(defId);
/*     */   }
/*     */   
/*     */   public static void addProcessDef(String defId, BpmProcessDef processDef) {
/* 133 */     getThreadMap(threadBpmProcessDef).put(defId, processDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void cleanTread() {
/* 142 */     threadActionModel.remove();
/* 143 */     threadBpmProcessDef.remove();
/* 144 */     threadMulInstIdentities.remove();
/* 145 */     threadMulInstOpTrace.remove();
/* 146 */     threadMulSequenceLine.remove();
/* 147 */     threadOptionVersion.remove();
/* 148 */     threadDynamictaskStack.remove();
/* 149 */     ((BpmActivitiDefCache)AppUtil.getBean(BpmActivitiDefCache.class)).clearLocal();
/*     */   }
/*     */   
/*     */   protected static <T> Stack<T> getStack(ThreadLocal<Stack<T>> threadLocal) {
/* 153 */     Stack<T> stack = threadLocal.get();
/* 154 */     if (stack == null) {
/* 155 */       stack = new Stack<>();
/* 156 */       threadLocal.set(stack);
/*     */     } 
/* 158 */     return stack;
/*     */   }
/*     */   
/*     */   protected static <T> ArrayDeque<T> getArrayDeque(ThreadLocal<ArrayDeque<T>> threadLocal) {
/* 162 */     ArrayDeque<T> arrayDeque = threadLocal.get();
/* 163 */     if (arrayDeque == null) {
/* 164 */       arrayDeque = new ArrayDeque<>();
/* 165 */       threadLocal.set(arrayDeque);
/*     */     } 
/* 167 */     return arrayDeque;
/*     */   }
/*     */   
/*     */   protected static <T> Map<String, T> getThreadMap(ThreadLocal<Map<String, T>> threadMap) {
/* 171 */     Map<String, T> processDefMap = threadMap.get();
/* 172 */     if (processDefMap == null) {
/* 173 */       processDefMap = new HashMap<>();
/* 174 */       threadMap.set(processDefMap);
/*     */     } 
/*     */     
/* 177 */     return processDefMap;
/*     */   }
/*     */   
/*     */   public static BpmExecutionStack getThreadDynamictaskStack(String nodeId) {
/* 181 */     Map<String, BpmExecutionStack> bpmExecutionStackMap = threadDynamictaskStack.get();
/* 182 */     if (bpmExecutionStackMap == null) {
/* 183 */       return null;
/*     */     }
/* 185 */     return bpmExecutionStackMap.get(nodeId);
/*     */   }
/*     */   
/*     */   public static void setThreadDynamictaskStack(String nodeId, BpmExecutionStack threadDynamictaskStack) {
/* 189 */     Map<String, BpmExecutionStack> bpmExecutionStackMap = BpmContext.threadDynamictaskStack.get();
/* 190 */     if (bpmExecutionStackMap == null) {
/* 191 */       bpmExecutionStackMap = new HashMap<>();
/* 192 */       BpmContext.threadDynamictaskStack.set(bpmExecutionStackMap);
/*     */     } 
/* 194 */     bpmExecutionStackMap.put(nodeId, threadDynamictaskStack);
/*     */   }
/*     */   
/*     */   public static Map<String, BpmExecutionStack> getAllThreadDynamictaskStack() {
/* 198 */     return threadDynamictaskStack.get();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static SysIdentity popMulInstIdentities() {
/* 209 */     Stack<SysIdentity> stack = getStack(threadMulInstIdentities);
/* 210 */     if (stack.isEmpty()) {
/* 211 */       return null;
/*     */     }
/* 213 */     return stack.pop();
/*     */   }
/*     */   
/*     */   public static void pushMulInstIdentities(SysIdentity inIdentity) {
/* 217 */     getStack(threadMulInstIdentities).push(inIdentity);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void pushMulInstOpTrace(String trace) {
/* 228 */     getStack(threadMulInstOpTrace).push(trace);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void clearMulInstOpTrace() {
/* 235 */     threadMulInstOpTrace.remove();
/* 236 */     threadMulInstIdentities.remove();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String popMulInstOpTrace() {
/* 247 */     Stack<String> stack = getStack(threadMulInstOpTrace);
/* 248 */     if (stack.isEmpty()) {
/* 249 */       return null;
/*     */     }
/* 251 */     return stack.pop();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String peekMulInstOpTrace() {
/* 262 */     Stack<String> stack = getStack(threadMulInstOpTrace);
/* 263 */     if (stack.isEmpty()) {
/* 264 */       return null;
/*     */     }
/* 266 */     return stack.peek();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isMulInstOpTraceEmpty() {
/* 277 */     return getStack(threadMulInstOpTrace).isEmpty();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void setThreadMulSequenceLine(String sequenceLine) {
/* 284 */     threadMulSequenceLine.set(sequenceLine);
/*     */   }
/*     */   
/*     */   public static String getThreadMulSequenceLine() {
/* 288 */     return threadMulSequenceLine.get();
/*     */   }
/*     */   
/*     */   public static String getOptionVersion() {
/* 292 */     return threadOptionVersion.get();
/*     */   }
/*     */   
/*     */   public static void setOptionVersion(String version) {
/* 296 */     threadOptionVersion.set(version);
/*     */   }
/*     */   
/*     */   public static List<SysIdentity> getThreadNSysIdentities() {
/* 300 */     return threadNextIdentitys.get();
/*     */   }
/*     */   
/*     */   public static void setThreadNextIdentitys(SysIdentity identity) {
/* 304 */     List<SysIdentity> sysIdentities = threadNextIdentitys.get();
/* 305 */     if (sysIdentities == null) {
/* 306 */       sysIdentities = new ArrayList<>();
/*     */     }
/* 308 */     if (!sysIdentities.contains(identity)) {
/* 309 */       sysIdentities.add(identity);
/*     */     }
/* 311 */     threadNextIdentitys.set(sysIdentities);
/*     */   }
/*     */   
/*     */   public static void setThreadNextIdentitys(List<SysIdentity> identitys) {
/* 315 */     if (identitys != null) {
/* 316 */       List<SysIdentity> sysIdentities = new ArrayList<>();
/* 317 */       List<SysIdentity> threadIdentitys = threadNextIdentitys.get();
/* 318 */       if (threadIdentitys != null) {
/* 319 */         sysIdentities.addAll(threadIdentitys);
/*     */       }
/* 321 */       identitys.forEach(sysIdentity -> {
/*     */             if (!sysIdentities.contains(sysIdentity)) {
/*     */               sysIdentities.add(sysIdentity);
/*     */             }
/*     */           });
/* 326 */       threadNextIdentitys.set(sysIdentities);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void removeThreadNextIdentitys() {
/* 331 */     threadNextIdentitys.remove();
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/context/BpmContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */