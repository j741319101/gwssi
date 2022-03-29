package com.dstz.bpm.wf.rest.script;

import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.rest.ControllerTools;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/script/http"})
public class HttpFormHandlerTestController extends ControllerTools {
   @RequestMapping({"/test"})
   public ResultMsg<String> test() throws Exception {
      return this.getSuccessResult("成功");
   }

   @RequestMapping({"/aaaa"})
   public ResultMsg<String> aaaa(@RequestBody Map paramse) {
      System.out.println(paramse);

      try {
         Thread.sleep(111000L);
      } catch (Exception var3) {
         ;
      }

      return this.getSuccessResult("aaaa成功");
   }

   @RequestMapping({"/bbbb"})
   public ResultMsg<String> bbbb(@RequestBody Map paramse) {
      new HashMap();
      return this.getSuccessResult("bbbb成功");
   }
}
