package com.dstz.bpm.wf.rest.controller;

import com.dstz.bpm.core.manager.BpmInstanceManager;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.rest.ControllerTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/bpm/instance/statis/"})
@Api(
   description = "流程实例统计服务接口"
)
public class BpmInstanceStatisController extends ControllerTools {
   @Resource
   private BpmInstanceManager bpmInstanceManager;

   @ApiOperation(
      value = "获取公文类型数量统计",
      notes = ""
   )
   @GetMapping({"getInstanceStatusStatis"})
   @ApiImplicitParams({@ApiImplicitParam(
   paramType = "form",
   dataType = "String",
   name = "instId",
   value = "流程实例id"
)})
   public ResultMsg<Map> getInstanceStatusStatis(@RequestParam(value = "instId",required = true) String instId) {
      return this.getSuccessResult(this.bpmInstanceManager.getInstanceStatusStatis(instId));
   }
}
