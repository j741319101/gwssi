/*     */ package com.dstz.bpm.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.core.util.ThreadMsgUtil;
/*     */ import com.dstz.base.rest.ControllerTools;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.bpm.core.manager.BpmDefOverallManager;
/*     */ import com.dstz.bpm.core.model.overallview.BpmOverallView;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import cn.hutool.core.io.FileUtil;
/*     */ import cn.hutool.core.util.ArrayUtil;
/*     */ import cn.hutool.core.util.ClassUtil;
/*     */ import cn.hutool.core.util.ZipUtil;
/*     */ import java.io.File;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.dom4j.Document;
/*     */ import org.dom4j.DocumentHelper;
/*     */ import org.dom4j.Element;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ import org.springframework.web.multipart.MultipartHttpServletRequest;
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
/*     */   public ResultMsg<BpmOverallView> getOverallView(@RequestParam String defId) throws Exception {
/*  56 */     BpmOverallView ovrallView = this.bpmDefOverallManager.getBpmOverallView(defId);
/*     */     
/*  58 */     return getSuccessResult(ovrallView);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"overallViewSave"})
/*     */   @CatchErr
/*     */   public ResultMsg overallViewSave(@RequestBody BpmOverallView overAllView) throws Exception {
/*  66 */     this.bpmDefOverallManager.saveBpmOverallView(overAllView);
/*  67 */     return getSuccessResult("保存成功！");
/*     */   }
/*     */   
/*     */   @RequestMapping({"exportBpmDefinition"})
/*     */   @CatchErr("下载失败")
/*     */   public void exportXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*  73 */     String[] defIds = RequestUtil.getStringAryByStr(request, "defIds");
/*  74 */     if (ArrayUtil.isEmpty((Object[])defIds))
/*     */       return; 
/*  76 */     String zipName = "agileBpmDefs" + DateUtil.format(new Date(), "yyyy_MMdd_HHmm");
/*     */     
/*  78 */     Map<String, String> exprotXml = this.bpmDefOverallManager.exportBpmDefinitions(defIds);
/*     */     
/*  80 */     downLoadFile(request, response, exprotXml, zipName);
/*     */   }
/*     */   
/*     */   private static String getWebContextPath() {
/*  84 */     String path = ClassUtil.getClassPath();
/*     */     
/*  86 */     if (path.indexOf("WEB-INF") != -1) {
/*  87 */       path = path.substring(0, path.lastIndexOf("WEB-INF"));
/*     */     } else {
/*  89 */       path = path.substring(0, path.lastIndexOf("classes"));
/*     */     } 
/*  91 */     path = StringUtil.trimSuffix(path, "/");
/*  92 */     return path;
/*     */   }
/*     */   
/*  95 */   private static final String ROOT_PATH = "temp" + File.separator + "tempZip";
/*     */   private static void downLoadFile(HttpServletRequest request, HttpServletResponse response, Map<String, String> fileContentMap, String zipName) throws Exception {
/*  97 */     String zipPath = getWebContextPath() + ROOT_PATH + File.separator + zipName;
/*  98 */     for (Map.Entry<String, String> ent : fileContentMap.entrySet()) {
/*  99 */       String fileName = ent.getKey();
/* 100 */       String content = ent.getValue();
/*     */       
/* 102 */       String filePath = zipPath + File.separator + fileName;
/*     */       
/* 104 */       FileUtil.writeUtf8String(content, filePath);
/*     */     } 
/*     */     
/* 107 */     File file = ZipUtil.zip(zipPath);
/* 108 */     FileUtil.del(zipPath);
/*     */     
/* 110 */     RequestUtil.downLoadFile(response, file);
/* 111 */     FileUtil.del(file);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"importPreview"})
/*     */   @CatchErr
/*     */   public ResultMsg<Map<String, List<BpmOverallView>>> importPreview(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
/* 124 */     MultipartFile fileLoad = request.getFile("xmlFile");
/* 125 */     String name = fileLoad.getOriginalFilename();
/* 126 */     String unZipFilePath = getWebContextPath() + File.separator + "temp" + File.separator + "unzip";
/*     */     try {
/* 128 */       String fileDir = StringUtils.substringBeforeLast(name, ".");
/*     */ 
/*     */       
/* 131 */       String originalFilename = fileLoad.getOriginalFilename();
/* 132 */       String destPath = unZipFilePath + File.separator + originalFilename;
/* 133 */       File file = FileUtil.file(destPath);
/* 134 */       FileUtil.mkParentDirs(file);
/* 135 */       fileLoad.transferTo(file);
/* 136 */       ZipUtil.unzip(file);
/* 137 */       unZipFilePath = unZipFilePath + File.separator + fileDir;
/*     */       
/* 139 */       String flowXml = FileUtil.readUtf8String(unZipFilePath + "/agilebpm-flow.xml");
/* 140 */       if (StringUtil.isEmpty(flowXml)) {
/* 141 */         throw new BusinessException("导入的文件缺少 流程信息 agilebpm-flow.xml");
/*     */       }
/*     */       
/* 144 */       checkXmlFormat(flowXml);
/* 145 */       Map<String, List<BpmOverallView>> perviewMaps = this.bpmDefOverallManager.importPreview(flowXml);
/*     */       
/* 147 */       return getSuccessResult(perviewMaps);
/*     */     }
/* 149 */     catch (Exception e) {
/* 150 */       throw new RuntimeException(e);
/*     */     } finally {
/* 152 */       File unzipfile = new File(unZipFilePath);
/* 153 */       if (unzipfile.exists()) {
/* 154 */         FileUtil.del(unzipfile);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   @RequestMapping({"importSave"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> importSave(@RequestBody List<BpmOverallView> overAllView) throws Exception {
/* 162 */     if (CollectionUtil.isEmpty(overAllView)) {
/* 163 */       throw new RuntimeException("导入的数据不能为空！");
/*     */     }
/*     */     
/* 166 */     this.bpmDefOverallManager.importSave(overAllView);
/*     */     
/* 168 */     return getSuccessResult("导入成功!<br/>" + ThreadMsgUtil.getMessage(true, "<br/>"));
/*     */   }
/*     */ 
/*     */   
/*     */   private void checkXmlFormat(String xml) throws Exception {
/* 173 */     String firstName = "agilebpmXmlList";
/* 174 */     String nextName = "agilebpmXml";
/* 175 */     Document doc = DocumentHelper.parseText(xml);
/* 176 */     Element root = doc.getRootElement();
/* 177 */     String msg = "导入文件格式不对";
/* 178 */     if (!root.getName().equals(firstName))
/* 179 */       throw new Exception(msg); 
/* 180 */     List<Element> itemLists = root.elements();
/* 181 */     for (Element elm : itemLists) {
/* 182 */       if (!elm.getName().equals(nextName))
/* 183 */         throw new Exception(msg); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloudbpm/wf-rest/0.2-SNAPSHOT/wf-rest-0.2-SNAPSHOT.jar!/cn/gwssi/ecloudbpm/bpm/rest/controller/BpmOverallViewController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */