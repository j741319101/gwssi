/*    */ package cn.gwssi.ecloudbpm.bus.rest.controller;
/*    */ 
/*    */ import cn.gwssi.ecloudbpm.bus.manager.BusTableDiagramManager;
/*    */ import cn.gwssi.ecloudbpm.bus.model.BusTableDiagram;
/*    */ import com.dstz.base.api.aop.annotion.CatchErr;
/*    */ import com.dstz.base.api.model.IDModel;
/*    */ import com.dstz.base.api.response.impl.ResultMsg;
/*    */ import com.dstz.base.rest.BaseController;
/*    */ import javax.annotation.Resource;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @RequestMapping({"/bus/BusTableDiagram/"})
/*    */ public class BusTableDiagramController
/*    */   extends BaseController<BusTableDiagram>
/*    */ {
/*    */   @Resource
/*    */   BusTableDiagramManager busTableDiagramManager;
/*    */   
/*    */   @RequestMapping({"save"})
/*    */   @CatchErr("保存业务表失败")
/*    */   public ResultMsg<String> save(@RequestBody BusTableDiagram busTableDiagram) throws Exception {
/* 34 */     String id = this.busTableDiagramManager.save(busTableDiagram);
/* 35 */     if (id != null) {
/* 36 */       return getSuccessResult(id, "保存业务图表成功");
/*    */     }
/* 38 */     return getSuccessResult("保存业务图表成功");
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected String getModelDesc() {
/* 45 */     return "业务实体图表";
/*    */   }
/*    */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-rest/v1.0.176.bjsj.1/bus-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/rest/controller/BusTableDiagramController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */