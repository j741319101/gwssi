/*     */ package cn.gwssi.ecloudbpm.wf.engine.plugin.context;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.context.BpmPluginContext;
/*     */ import cn.gwssi.ecloudbpm.wf.api.engine.plugin.def.BpmPluginDef;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import com.alibaba.fastjson.JSON;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public abstract class AbstractBpmPluginContext<T extends BpmPluginDef>
/*     */   implements BpmPluginContext<T>
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*  19 */   protected Logger LOG = LoggerFactory.getLogger(getClass());
/*     */   
/*     */   private T bpmPluginDef;
/*     */ 
/*     */   
/*     */   public T getBpmPluginDef() {
/*  25 */     return this.bpmPluginDef;
/*     */   }
/*     */   
/*     */   public void setBpmPluginDef(T bpmPluginDef) {
/*  29 */     this.bpmPluginDef = bpmPluginDef;
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
/*     */ 
/*     */   
/*     */   public JSON getJson() {
/*  44 */     return (JSON)JSON.toJSON(this.bpmPluginDef);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public T parse(JSON json) {
/*  53 */     T def = parseFromJson(json);
/*  54 */     setBpmPluginDef(def);
/*  55 */     return this.bpmPluginDef;
/*     */   }
/*     */   
/*     */   public T parse(String json) {
/*  59 */     if (StringUtil.isEmpty(json)) return null;
/*     */     
/*  61 */     JSON j = (JSON)JSON.parse(json);
/*  62 */     return parse(j);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/*  67 */     return StringUtil.lowerFirst(getClass().getSimpleName().replaceAll("PluginContext", ""));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String persistnce(String defId) {
/*  78 */     String msg = null;
/*  79 */     if (getJson() == null) msg = "清空改插件";
/*     */ 
/*     */     
/*  82 */     return msg;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  89 */   protected int sn = 100;
/*     */ 
/*     */   
/*     */   public Integer getSn() {
/*  93 */     return Integer.valueOf(this.sn);
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(BpmPluginContext pluginContext) {
/*  98 */     if (getSn() == pluginContext.getSn()) {
/*  99 */       return 0;
/*     */     }
/*     */     
/* 102 */     if (getSn().intValue() > pluginContext.getSn().intValue()) {
/* 103 */       return 1;
/*     */     }
/*     */     
/* 106 */     return -1;
/*     */   }
/*     */   
/*     */   protected abstract T parseFromJson(JSON paramJSON);
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-core/v1.0.176.bjsj.1/wf-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/engine/plugin/context/AbstractBpmPluginContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */