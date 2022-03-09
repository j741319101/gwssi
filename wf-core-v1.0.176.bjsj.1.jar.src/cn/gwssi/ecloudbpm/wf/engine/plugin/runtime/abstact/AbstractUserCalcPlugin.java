/*     */ package cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.abstact;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.constant.ExtractType;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmUserCalcPluginDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.plugindef.AbstractUserCalcPluginDef;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.runtime.BpmUserCalcPlugin;
/*     */ import cn.gwssi.ecloudbpm.wf.engine.plugin.session.BpmUserCalcPluginSession;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.DefaultIdentity;
/*     */ import cn.gwssi.ecloudframework.sys.api.model.SysIdentity;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.LinkedHashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import javax.annotation.Resource;
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
/*     */ 
/*     */ 
/*     */ public abstract class AbstractUserCalcPlugin<M extends BpmUserCalcPluginDef>
/*     */   implements BpmUserCalcPlugin<M>
/*     */ {
/*     */   @Resource
/*     */   protected UserService userService;
/*     */   
/*     */   public List<SysIdentity> execute(BpmUserCalcPluginSession pluginSession, M pluginDef) {
/*  39 */     if (pluginSession.isPreViewModel().booleanValue() && 
/*  40 */       !supportPreView()) return Collections.emptyList();
/*     */ 
/*     */     
/*  43 */     List<SysIdentity> list = queryByPluginDef(pluginSession, pluginDef);
/*  44 */     if (CollectionUtil.isEmpty(list)) return list;
/*     */     
/*  46 */     ExtractType extractType = ((AbstractUserCalcPluginDef)pluginDef).getExtract();
/*     */     
/*  48 */     Set<SysIdentity> set = new LinkedHashSet<>();
/*  49 */     List<SysIdentity> rtnList = new ArrayList<>();
/*     */ 
/*     */     
/*  52 */     list = extract(list, extractType);
/*     */     
/*  54 */     set.addAll(list);
/*     */     
/*  56 */     rtnList.addAll(set);
/*     */     
/*  58 */     return rtnList;
/*     */   }
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
/*     */   protected List<SysIdentity> extract(List<SysIdentity> bpmIdentities, ExtractType extractType) {
/*  71 */     if (CollectionUtil.isEmpty(bpmIdentities)) return Collections.EMPTY_LIST;
/*     */     
/*  73 */     if (extractType == ExtractType.EXACT_NOEXACT) {
/*  74 */       return bpmIdentities;
/*     */     }
/*     */     
/*  77 */     return extractBpmIdentity(bpmIdentities);
/*     */   }
/*     */   
/*     */   protected List<SysIdentity> extractBpmIdentity(List<SysIdentity> bpmIdentities) {
/*  81 */     List<SysIdentity> results = new ArrayList<>();
/*  82 */     for (SysIdentity bpmIdentity : bpmIdentities) {
/*     */       
/*  84 */       if ("user".equals(bpmIdentity.getType())) {
/*  85 */         results.add(bpmIdentity);
/*     */         
/*     */         continue;
/*     */       } 
/*  89 */       List<IUser> users = this.userService.getUserListByGroup(bpmIdentity.getType(), bpmIdentity.getId());
/*  90 */       for (IUser user : users) {
/*  91 */         results.add(new DefaultIdentity(user));
/*     */       }
/*     */     } 
/*     */     
/*  95 */     return results;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean supportPreView() {
/* 104 */     return true;
/*     */   }
/*     */   
/*     */   protected abstract List<SysIdentity> queryByPluginDef(BpmUserCalcPluginSession paramBpmUserCalcPluginSession, M paramM);
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/runtime/abstact/AbstractUserCalcPlugin.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */