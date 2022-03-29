package com.dstz.bpm.wf.rest.controller;

import com.dstz.bpm.core.manager.BpmDefOverallManager;
import com.dstz.bpm.core.model.overallview.BpmOverallView;
import com.dstz.base.api.aop.annotion.CatchErr;
import com.dstz.base.api.constant.BaseStatusCode;
import com.dstz.base.api.exception.BusinessMessage;
import com.dstz.base.api.response.impl.ResultMsg;
import com.dstz.base.core.util.StringUtil;
import com.dstz.base.core.util.ThreadMsgUtil;
import com.dstz.base.rest.ControllerTools;
import com.dstz.base.rest.util.RequestUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.crypto.SecureUtil;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping({"/bpm/overallView"})
public class BpmOverallViewController extends ControllerTools {
   @Resource
   BpmDefOverallManager bpmDefOverallManager;

   @RequestMapping({"getOverallView"})
   public ResultMsg<BpmOverallView> getOverallView(@RequestParam String defId) {
      BpmOverallView ovrallView = this.bpmDefOverallManager.getBpmOverallView(defId);
      return this.getSuccessResult(ovrallView);
   }

   @RequestMapping({"overallViewSave"})
   @CatchErr
   public ResultMsg<String> overallViewSave(@RequestBody BpmOverallView overAllView) {
      this.bpmDefOverallManager.saveBpmOverallView(overAllView);
      return this.getSuccessResult("保存成功！");
   }

   @RequestMapping(
      value = {"exportBpmDefinition"},
      method = {RequestMethod.GET}
   )
   @CatchErr("下载失败")
   public ResponseEntity<byte[]> exportXml(HttpServletRequest request) throws Exception {
      String[] defIds = RequestUtil.getStringAryByStr(request, "defIds");
      if (ArrayUtil.isEmpty(defIds)) {
         return ResponseEntity.notFound().build();
      } else {
         String zipName = "BpmDefs" + DateFormatUtils.format(new Date(), "yyyy_MMdd_HHmm") + ".zip";
         Map<String, String> exportXmlMap = this.bpmDefOverallManager.exportBpmDefinitions(defIds);
         File zipDirectory = new File(FileUtils.getTempDirectory(), SecureUtil.simpleUUID());
         if (!zipDirectory.mkdir()) {
            throw new IOException("Can't create directory " + zipDirectory.getPath());
         } else {
            File zipFile = null;

            try {
               Iterator var7 = exportXmlMap.entrySet().iterator();

               while(var7.hasNext()) {
                  Entry<String, String> entry = (Entry)var7.next();
                  FileUtil.writeUtf8String((String)entry.getValue(), new File(zipDirectory, (String)entry.getKey()));
               }

               zipFile = ZipUtil.zip(zipDirectory);
               ResponseEntity var12 = ((BodyBuilder)ResponseEntity.ok().header("Content-Disposition", new String[]{"attachment;filename=" + URLEncoder.encode(zipName, StandardCharsets.UTF_8.displayName())})).contentType(MediaType.APPLICATION_OCTET_STREAM).contentLength(zipFile.length()).body(Files.readAllBytes(zipFile.toPath()));
               return var12;
            } finally {
               FileUtils.deleteQuietly(zipFile);
               FileUtils.deleteDirectory(zipDirectory);
            }
         }
      }
   }

   @RequestMapping(
      value = {"importPreview"},
      method = {RequestMethod.POST}
   )
   @CatchErr
   public ResultMsg<Map<String, List<BpmOverallView>>> importPreview(@RequestParam("xmlFile") MultipartFile uploadFile) throws Exception {
      String uploadFileExtension = FilenameUtils.getExtension(uploadFile.getOriginalFilename());
      if (!StringUtils.equalsIgnoreCase(uploadFileExtension, "zip")) {
         return new ResultMsg(BaseStatusCode.PARAM_ILLEGAL, String.format("上传文件(%s)类型非(zip)", uploadFile.getOriginalFilename()));
      } else {
         File uploadZipFile = new File(FileUtils.getTempDirectory(), StringUtils.join(new Serializable[]{SecureUtil.simpleUUID(), '.', uploadFileExtension}));
         File unzipDirectory = null;

         String flowXml;
         label44: {
            ResultMsg var6;
            try {
               uploadFile.transferTo(uploadZipFile);
               unzipDirectory = ZipUtil.unzip(uploadZipFile);
               flowXml = FileUtil.readUtf8String(new File(unzipDirectory, "bpm-flow.xml"));
               if (!StringUtil.isEmpty(flowXml)) {
                  break label44;
               }

               var6 = ResultMsg.ERROR("导入的文件缺少 流程信息 bpm-flow.xml");
            } finally {
               FileUtils.deleteQuietly(uploadZipFile);
               FileUtils.deleteQuietly(unzipDirectory);
            }

            return var6;
         }

         this.checkXmlFormat(flowXml);
         Map perviewMaps = this.bpmDefOverallManager.importPreview(flowXml);
         return this.getSuccessResult(perviewMaps);
      }
   }

   @RequestMapping(
      value = {"importSave"},
      method = {RequestMethod.POST}
   )
   @CatchErr
   public ResultMsg<String> importSave(@RequestBody List<BpmOverallView> overAllView) {
      if (CollectionUtils.isEmpty(overAllView)) {
         throw new BusinessMessage("导入的数据不能为空！");
      } else {
         this.bpmDefOverallManager.importSave(overAllView);
         return this.getSuccessResult("导入成功!<br/>" + ThreadMsgUtil.getMessage(true, "<br/>"));
      }
   }

   private void checkXmlFormat(String xml) throws DocumentException {
      String firstName = "bpmXmlList";
      String nextName = "bpmXml";
      Document doc = DocumentHelper.parseText(xml);
      Element root = doc.getRootElement();
      String msg = "导入文件格式不对";
      if (!root.getName().equals(firstName)) {
         throw new BusinessMessage(msg);
      } else {
         List<Element> itemLists = root.elements();
         Iterator var8 = itemLists.iterator();

         Element elm;
         do {
            if (!var8.hasNext()) {
               return;
            }

            elm = (Element)var8.next();
         } while(elm.getName().equals(nextName));

         throw new BusinessMessage(msg);
      }
   }
}
