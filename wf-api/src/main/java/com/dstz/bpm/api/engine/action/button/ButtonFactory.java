/*     */ package com.dstz.bpm.api.engine.action.button;
/*     */ 
/*     */ import com.dstz.bpm.api.constant.ActionType;
/*     */ import com.dstz.bpm.api.constant.TaskType;
/*     */ import com.dstz.bpm.api.engine.action.cmd.ActionCmd;
/*     */ import com.dstz.bpm.api.engine.action.handler.ActionDisplayHandler;
/*     */ import com.dstz.bpm.api.engine.action.handler.ActionHandler;
/*     */ import com.dstz.bpm.api.engine.action.handler.BuiltinActionHandler;
/*     */ import com.dstz.bpm.api.engine.action.handler.IExtendTaskAction;
/*     */ import com.dstz.bpm.api.engine.action.handler.InstanceDetailHandler;
/*     */ import com.dstz.bpm.api.engine.data.result.BpmFlowData;
/*     */ import com.dstz.bpm.api.engine.data.result.BpmFlowTaskData;
/*     */ import com.dstz.bpm.api.model.def.NodeProperties;
/*     */ import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
/*     */ import com.dstz.bpm.api.model.nodedef.Button;
/*     */ import com.dstz.bpm.api.model.nodedef.impl.UserTaskNodeDef;
/*     */ import com.dstz.bpm.api.model.task.IBpmTask;
/*     */ import com.dstz.base.core.util.AppUtil;
/*     */ import cn.hutool.core.map.MapUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.atomic.AtomicBoolean;
/*     */ import java.util.stream.Collectors;
/*     */
import org.apache.commons.lang3.StringUtils;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ButtonFactory
/*     */ {
/*     */   public static List<Button> generateButtons(BpmNodeDef nodeDef, boolean isDefault) throws ClassNotFoundException {
/*  42 */     NodeProperties prop = nodeDef.getNodeProperties();
/*     */     
/*  44 */     List<Button> btns = new ArrayList<>();
/*     */     
/*  46 */     Map<String, ActionHandler> actionMap = AppUtil.getImplInstance(ActionHandler.class);
/*     */     
/*  48 */     List<ActionHandler> list = new ArrayList<>(actionMap.values());
/*  49 */     sortActionList(list);
/*     */     
/*  51 */     for (ActionHandler actionHandler : list) {
/*     */       
/*  53 */       if (isDefault && !actionHandler.isDefault().booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/*  57 */       if (!actionHandler.isSupport(nodeDef).booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/*  61 */       ActionType actionType = actionHandler.getActionType();
/*  62 */       Button button = new Button(actionType.getName(), actionType.getKey(), actionHandler.getDefaultGroovyScript(), actionHandler.getConfigPage());
/*  63 */       button.setBeforeScript(actionHandler.getDefaultBeforeScript());
/*     */       
/*  65 */       btns.add(button);
/*     */     } 
/*     */     
/*  68 */     return btns;
/*     */   }
/*     */ 
/*     */   
/*     */   private static void sortActionList(List<ActionHandler> list) {
/*  73 */     Collections.sort(list, new Comparator<ActionHandler>()
/*     */         {
/*     */           public int compare(ActionHandler o1, ActionHandler o2) {
/*  76 */             return o1.getSn() - o2.getSn();
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Button> getInstanceButtons(boolean isDefault) {
/*  87 */     List<Button> btns = new ArrayList<>();
/*     */     
/*  89 */     Map<String, ActionHandler> actionMap = AppUtil.getImplInstance(ActionHandler.class);
/*  90 */     List<ActionHandler> list = new ArrayList<>(actionMap.values());
/*     */     
/*  92 */     for (ActionHandler actionHandler : list) {
/*  93 */       ActionType actionType = actionHandler.getActionType();
/*  94 */       UserTaskNodeDef userTaskNodeDef = new UserTaskNodeDef();
/*     */       
/*  96 */       if (!actionHandler.isSupport((BpmNodeDef)userTaskNodeDef).booleanValue()) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 101 */       if (isDefault && !actionHandler.isDefault().booleanValue()) {
/*     */         continue;
/*     */       }
/*     */       
/* 105 */       Button button = new Button(actionType.getName(), actionType.getKey(), actionHandler.getDefaultGroovyScript(), actionHandler.getConfigPage());
/* 106 */       button.setBeforeScript(actionHandler.getDefaultBeforeScript());
/*     */       
/* 108 */       btns.add(button);
/*     */     } 
/*     */     
/* 111 */     return btns;
/*     */   }
/*     */   
/*     */   public static List<Button> specialTaskBtnByUserHandler(List<Button> btns, BpmFlowData flowData) {
/* 115 */     if (!(flowData instanceof BpmFlowTaskData))
/* 116 */       return btns; 
/* 117 */     IBpmTask task = ((BpmFlowTaskData)flowData).getTask();
/* 118 */     String name = task.getTaskType() + "UserButtonChecker";
/* 119 */     List<IExtendTaskAction> extendTaskActions = AppUtil.getImplInstanceArray(IExtendTaskAction.class);
/* 120 */     boolean isLeaderTask = false;
/* 121 */     for (IExtendTaskAction extendTaskAction : extendTaskActions) {
/* 122 */       if (extendTaskAction.isLeaderTask(task.getId())) {
/* 123 */         isLeaderTask = true;
/*     */         break;
/*     */       } 
/*     */     } 
/* 127 */     if (isLeaderTask) {
/* 128 */       name = "LeaderButtonChecker";
/*     */     }
/* 130 */     ButtonUserChecker buttonUserChecker = (ButtonUserChecker)AppUtil.getBean(name);
/* 131 */     if (buttonUserChecker == null)
/* 132 */       return btns; 
/* 133 */     List<Button> buttons = new ArrayList<>();
/* 134 */     for (Button btn : btns) {
/* 135 */       if (buttonUserChecker.isSupport(btn, task)) {
/* 136 */         buttons.add(btn);
/*     */       }
/*     */     } 
/*     */     
/* 140 */     buttonUserChecker.specialBtnByUserHandler(buttons, task);
/* 141 */     return buttons;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Button> specialTaskBtnHandler(List<Button> btns, BpmFlowData flowData) {
/* 152 */     if (!(flowData instanceof BpmFlowTaskData)) {
/* 153 */       return btns;
/*     */     }
/* 155 */     IBpmTask task = ((BpmFlowTaskData)flowData).getTask();
/* 156 */     String name = task.getTaskType() + "ButtonChecker";
/*     */     
/* 158 */     if (TaskType.ADD_SIGN.equalsWithKey(task.getTaskType())) {
/* 159 */       name = "SIGNButtonChecker";
/*     */     }
/* 161 */     ButtonChecker checker = (ButtonChecker)AppUtil.getBean(name);
/* 162 */     if (checker == null)
/* 163 */       return btns; 
/* 164 */     List<Button> buttons = new ArrayList<>();
/* 165 */     String checkerBtns = "decreaseDynamic,increaseDynamic";
/* 166 */     AtomicBoolean isComplete = new AtomicBoolean(true);
/* 167 */     btns.forEach(btn -> {
/*     */           if (checkerBtns.contains(btn.getAlias())) {
/*     */             isComplete.set(false);
/*     */           }
/*     */         });
/* 172 */     for (Button btn : btns) {
/* 173 */       if (StringUtils.equals("SUPERVISEButtonChecker", name)) {
/* 174 */         if (!isComplete.get()) {
/* 175 */           if (checker.isSupport(btn))
/* 176 */             buttons.add(btn); 
/*     */           continue;
/*     */         } 
/* 179 */         buttons.add(btn); continue;
/*     */       } 
/* 181 */       if (checker.isSupport(btn)) {
/* 182 */         buttons.add(btn);
/*     */       }
/*     */     } 
/*     */     
/* 186 */     checker.specialBtnHandler(buttons);
/* 187 */     return buttons;
/*     */   }
/*     */   
/* 190 */   private static Map<String, InstanceDetailHandler> instanceDetailHandlerMap = null;
/*     */   
/*     */   public static InstanceDetailHandler getButtonInstanceDetailHandler(String alias) {
/* 193 */     if (instanceDetailHandlerMap == null) {
/* 194 */       instanceDetailHandlerMap = (Map<String, InstanceDetailHandler>)AppUtil.getApplicaitonContext().getBeansOfType(InstanceDetailHandler.class).values().stream().collect(Collectors.toMap(item -> item.getActionType().getKey(), item -> item));
/*     */     }
/* 196 */     return instanceDetailHandlerMap.get(alias);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/* 202 */   private static Map<String, ActionDisplayHandler<? extends ActionCmd>> actionDisplayHandlerMap = null;
/*     */   private static List<Button> builtButtons;
/*     */   
/*     */   public static ActionDisplayHandler<? extends ActionCmd> getActionDisplayHandler(String alias) {
/* 206 */     if (actionDisplayHandlerMap == null) {
/* 207 */       synchronized (ButtonFactory.class) {
/* 208 */         if (actionDisplayHandlerMap == null) {
/* 209 */           Map<String, ActionDisplayHandler> beanMap = AppUtil.getApplicaitonContext().getBeansOfType(ActionDisplayHandler.class);
/* 210 */           if (MapUtil.isEmpty(beanMap)) {
/* 211 */             actionDisplayHandlerMap = Collections.emptyMap();
/*     */           } else {
/* 213 */             actionDisplayHandlerMap = (Map<String, ActionDisplayHandler<? extends ActionCmd>>)beanMap.values().stream().filter(o -> o.isDefault().booleanValue()).collect(Collectors.toMap(o -> o.getActionType().getKey(), o -> o));
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/* 218 */     return actionDisplayHandlerMap.get(alias);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static List<Button> getBuiltButtons() {
/* 228 */     builtButtons = null;
/* 229 */     if (builtButtons != null) {
/* 230 */       return builtButtons;
/*     */     }
/* 232 */     if (builtButtons == null) {
/* 233 */       synchronized (ButtonFactory.class) {
/* 234 */         if (builtButtons != null) {
/* 235 */           return builtButtons;
/*     */         }
/* 237 */         List<BuiltinActionHandler> builtinActionHandlerList = AppUtil.getImplInstanceArray(BuiltinActionHandler.class);
/* 238 */         List<Button> btns = new ArrayList<>(builtinActionHandlerList.size());
/* 239 */         for (BuiltinActionHandler builtinActionHandler : builtinActionHandlerList) {
/* 240 */           if (!builtinActionHandler.isDefault().booleanValue()) {
/*     */             continue;
/*     */           }
/* 243 */           ActionType actionType = builtinActionHandler.getActionType();
/* 244 */           Button button = new Button(actionType.getName(), actionType.getKey(), builtinActionHandler.getDefaultGroovyScript(), builtinActionHandler.getConfigPage());
/* 245 */           button.setBeforeScript(builtinActionHandler.getDefaultBeforeScript());
/* 246 */           btns.add(button);
/*     */         } 
/* 248 */         builtButtons = btns;
/*     */       } 
/*     */     }
/* 251 */     return builtButtons;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-api/v1.0.176.bjsj.1/wf-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/api/engine/action/button/ButtonFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */