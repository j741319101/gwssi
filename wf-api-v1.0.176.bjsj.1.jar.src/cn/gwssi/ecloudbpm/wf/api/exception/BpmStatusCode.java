/*     */ package cn.gwssi.ecloudbpm.wf.api.exception;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.constant.IStatusCode;
/*     */ 
/*     */ 
/*     */ 
/*     */ public enum BpmStatusCode
/*     */   implements IStatusCode
/*     */ {
/*  10 */   SUCCESS("200", "成功"),
/*  11 */   SYSTEM_ERROR("500", "系统异常"),
/*  12 */   TIMEOUT("401", "访问超时"),
/*  13 */   NO_ACCESS("403", "访问受限"),
/*  14 */   PARAM_ILLEGAL("100", "参数校验不通过"),
/*     */   
/*  16 */   ACTIONCMD_ERROR("20000", "线程ActionCmd 数据异常"),
/*     */   
/*  18 */   NO_PERMISSION("20001", "没有操作权限！"),
/*     */   
/*  20 */   DEF_FORBIDDEN("30000", "流程定义被禁用"),
/*  21 */   DEF_LOST("30001", "流程定义丢失"),
/*  22 */   TASK_NOT_FOUND("30001", "查找不到当前任务，任务可能已经被处理完成"),
/*  23 */   INST_NOT_FOUND("30001", "查找不到当前流程"),
/*  24 */   NO_TASK_USER("30002", "任务候选执行人为空"),
/*  25 */   USER_CALC_ERROR("30003", "人员计算出现异常"),
/*     */   
/*  27 */   NO_TASK_ACTION("30004", "任务处理ACTION查找不到"),
/*  28 */   HANDLER_ERROR("30005", "执行URL表单处理器处理器失败，请检查"),
/*  29 */   TASK_ACTION_BTN_ERROR("30006", "任务处理器生成按钮异常"),
/*     */   
/*  31 */   VARIABLES_NO_SYNC("30007", "流程变量尚未同步,不应该存在的获取时机"),
/*  32 */   NO_ASSIGN_USER("30008", "任务尚未分配候选人"),
/*  33 */   INSTANCE_NOT_RUNNING("30009", "实例状态未在运行中"),
/*  34 */   INSTANCE_NOT_EXISTS("30010", "实例不存在"),
/*     */ 
/*     */   
/*  37 */   NO_BACK_TARGET("30101", "驳回节点未知"),
/*  38 */   CANNOT_BACK_NODE("30102", "不支持的驳回节点"),
/*  39 */   PLUGIN_ERROR("31000", "执行插件异常"),
/*     */ 
/*     */   
/*  42 */   PLUGIN_USERCALC_RULE_CONDITION_ERROR("31100", "用户计算插件前置条件解析异常"),
/*     */ 
/*     */ 
/*     */   
/*  46 */   GATEWAY_ERROR("30051", "网关分支判断脚本异常"),
/*     */ 
/*     */   
/*  49 */   PARSER_FLOW_ERROR("30601", "流程解析器异常"),
/*  50 */   PARSER_NODE_ERROR("30602", "流程节点解析器异常"),
/*     */ 
/*     */   
/*  53 */   FLOW_DATA_GET_BUTTONS_ERROR("30701", "获取流程按钮失败"),
/*  54 */   FLOW_DATA_EXECUTE_SHOWSCRIPT_ERROR("30702", "执行初始化脚本失败"),
/*     */   
/*  56 */   FLOW_BUS_DATA_PK_LOSE("40101", "流程数据保存异常主键缺失"),
/*     */   
/*  58 */   FLOW_FORM_LOSE("50101", "流程配置的表单查找不到"),
/*     */ 
/*     */   
/*  61 */   FLOW_BUS_DATA_LOSE("50101", "流程关联的业务数据丢失"),
/*     */   
/*  63 */   BPM_SKIP_TARGET_NODE_LOSE("50101", "流程跳转目标节点查找不到"),
/*     */   
/*  65 */   BPM_MULT_INST_CONFIRMR_ECYCLE("50103", "将会回收未完成分发任务，请确认:"),
/*     */   
/*  67 */   ERROR_UNKNOWN("30100", "未知异常");
/*     */   
/*     */   private String code;
/*     */   private String desc;
/*     */   private String system;
/*     */   
/*     */   BpmStatusCode(String code, String description) {
/*  74 */     setCode(code);
/*  75 */     setDesc(description);
/*  76 */     setSystem("BASE");
/*     */   }
/*     */   
/*     */   public String getCode() {
/*  80 */     return this.code;
/*     */   }
/*     */   
/*     */   public void setCode(String code) {
/*  84 */     this.code = code;
/*     */   }
/*     */   
/*     */   public String getDesc() {
/*  88 */     return this.desc;
/*     */   }
/*     */   
/*     */   public void setDesc(String msg) {
/*  92 */     this.desc = msg;
/*     */   }
/*     */   
/*     */   public String getSystem() {
/*  96 */     return this.system;
/*     */   }
/*     */   
/*     */   public void setSystem(String system) {
/* 100 */     this.system = system;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/exception/BpmStatusCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */