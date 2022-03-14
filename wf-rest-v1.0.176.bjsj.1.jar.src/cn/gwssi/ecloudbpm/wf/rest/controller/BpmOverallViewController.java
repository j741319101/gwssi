/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import com.dstz.bpm.core.manager.BpmDefOverallManager;
/*     */ import com.dstz.bpm.core.model.overallview.BpmOverallView;
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.constant.BaseStatusCode;
/*     */ import com.dstz.base.api.constant.IStatusCode;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMsgUtil;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import cn.hutool.core.io.FileUtil;
/*     */ import cn.hutool.core.util.ArrayUtil;
/*     */ import cn.hutool.core.util.ZipUtil;
/*     */ import cn.hutool.crypto.SecureUtil;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.net.URLEncoder;
/*     */ import java.nio.charset.StandardCharsets;
/*     */ import java.nio.file.Files;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import org.apache.commons.collections.CollectionUtils;
/*     */ import org.apache.commons.io.FileUtils;
/*     */ import org.apache.commons.io.FilenameUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.commons.lang3.time.DateFormatUtils;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.DocumentException;
/*     */ import org.dom4j.DocumentHelper;
/*     */ import org.dom4j.Element;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ import org.springframework.web.multipart.MultipartFile;
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
/*     */ @RestController
/*     */ @RequestMapping({"/bpm/overallView"})
/*     */ public class BpmOverallViewController
/*     */   extends ControllerTools
/*     */ {
/*     */   @Resource
/*     */   BpmDefOverallManager bpmDefOverallManager;
/*     */   
/*     */   @RequestMapping({"getOverallView"})
/*     */   public ResultMsg<BpmOverallView> getOverallView(@RequestParam String defId) {
/*  69 */     BpmOverallView ovrallView = this.bpmDefOverallManager.getBpmOverallView(defId);
/*     */     
/*  71 */     return getSuccessResult(ovrallView);
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"overallViewSave"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> overallViewSave(@RequestBody BpmOverallView overAllView) {
/*  78 */     this.bpmDefOverallManager.saveBpmOverallView(overAllView);
/*  79 */     return getSuccessResult("保存成功！");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"exportBpmDefinition"}, method = {RequestMethod.GET})
/*     */   @CatchErr("下载失败")
/*     */   public ResponseEntity<byte[]> exportXml(HttpServletRequest request) throws Exception {
/*  90 */     String[] defIds = RequestUtil.getStringAryByStr(request, "defIds");
/*  91 */     if (ArrayUtil.isEmpty((Object[])defIds)) {
/*  92 */       return ResponseEntity.notFound().build();
/*     */     }
/*  94 */     String zipName = "BpmDefs" + DateFormatUtils.format(new Date(), "yyyy_MMdd_HHmm") + ".zip";
/*     */     
/*  96 */     Map<String, String> exportXmlMap = this.bpmDefOverallManager.exportBpmDefinitions(defIds);
/*  97 */     File zipDirectory = new File(FileUtils.getTempDirectory(), SecureUtil.simpleUUID());
/*  98 */     if (!zipDirectory.mkdir()) {
/*  99 */       throw new IOException("Can't create directory " + zipDirectory.getPath());
/*     */     }
/* 101 */     File zipFile = null;
/*     */     try {
/* 103 */       for (Map.Entry<String, String> entry : exportXmlMap.entrySet()) {
/* 104 */         FileUtil.writeUtf8String(entry.getValue(), new File(zipDirectory, entry.getKey()));
/*     */       }
/* 106 */       zipFile = ZipUtil.zip(zipDirectory);
/* 107 */       return ((ResponseEntity.BodyBuilder)ResponseEntity.ok()
/* 108 */         .header("Content-Disposition", new String[] { "attachment;filename=" + URLEncoder.encode(zipName, StandardCharsets.UTF_8.displayName())
/* 109 */           })).contentType(MediaType.APPLICATION_OCTET_STREAM)
/* 110 */         .contentLength(zipFile.length())
/* 111 */         .body(Files.readAllBytes(zipFile.toPath()));
/*     */     } finally {
/* 113 */       FileUtils.deleteQuietly(zipFile);
/* 114 */       FileUtils.deleteDirectory(zipDirectory);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"importPreview"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   public ResultMsg<Map<String, List<BpmOverallView>>> importPreview(@RequestParam("xmlFile") MultipartFile uploadFile) throws Exception {
/* 127 */     String flowXml, uploadFileExtension = FilenameUtils.getExtension(uploadFile.getOriginalFilename());
/* 128 */     if (!StringUtils.equalsIgnoreCase(uploadFileExtension, "zip")) {
/* 129 */       return new ResultMsg((IStatusCode)BaseStatusCode.PARAM_ILLEGAL, String.format("上传文件(%s)类型非(zip)", new Object[] { uploadFile.getOriginalFilename() }));
/*     */     }
/* 131 */     File uploadZipFile = new File(FileUtils.getTempDirectory(), StringUtils.join((Object[])new Serializable[] { SecureUtil.simpleUUID(), Character.valueOf('.'), uploadFileExtension }));
/* 132 */     File unzipDirectory = null;
/*     */     
/*     */     try {
/* 135 */       uploadFile.transferTo(uploadZipFile);
/* 136 */       unzipDirectory = ZipUtil.unzip(uploadZipFile);
/* 137 */       flowXml = FileUtil.readUtf8String(new File(unzipDirectory, "bpm-flow.xml"));
/* 138 */       if (StringUtil.isEmpty(flowXml)) {
/* 139 */         return ResultMsg.ERROR("导入的文件缺少 流程信息 bpm-flow.xml");
/*     */       }
/*     */     } finally {
/* 142 */       FileUtils.deleteQuietly(uploadZipFile);
/* 143 */       FileUtils.deleteQuietly(unzipDirectory);
/*     */     } 
/* 145 */     checkXmlFormat(flowXml);
/* 146 */     Map<String, List<BpmOverallView>> perviewMaps = this.bpmDefOverallManager.importPreview(flowXml);
/* 147 */     return getSuccessResult(perviewMaps);
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"importSave"}, method = {RequestMethod.POST})
/*     */   @CatchErr
/*     */   public ResultMsg<String> importSave(@RequestBody List<BpmOverallView> overAllView) {
/* 153 */     if (CollectionUtils.isEmpty(overAllView)) {
/* 154 */       throw new BusinessMessage("导入的数据不能为空！");
/*     */     }
/* 156 */     this.bpmDefOverallManager.importSave(overAllView);
/* 157 */     return getSuccessResult("导入成功!<br/>" + ThreadMsgUtil.getMessage(true, "<br/>"));
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkXmlFormat(String xml) throws DocumentException {
/* 162 */     String firstName = "bpmXmlList";
/* 163 */     String nextName = "bpmXml";
/* 164 */     Document doc = DocumentHelper.parseText(xml);
/* 165 */     Element root = doc.getRootElement();
/* 166 */     String msg = "导入文件格式不对";
/* 167 */     if (!root.getName().equals(firstName)) {
/* 168 */       throw new BusinessMessage(msg);
/*     */     }
/* 170 */     List<Element> itemLists = root.elements();
/* 171 */     for (Element elm : itemLists) {
/* 172 */       if (!elm.getName().equals(nextName))
/* 173 */         throw new BusinessMessage(msg); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-rest/v1.0.176.bjsj.1/wf-rest-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/wf/rest/controller/BpmOverallViewController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */