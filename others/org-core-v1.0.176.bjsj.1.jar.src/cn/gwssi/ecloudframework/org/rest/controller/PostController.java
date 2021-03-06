/*     */ package com.dstz.org.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.aop.annotion.OperateLog;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.util.FileUtil;
/*     */ import com.dstz.base.core.util.StringUtil;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.org.api.constant.RelationTypeConstant;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.core.manager.GroupManager;
/*     */ import com.dstz.org.core.manager.OrgRelationManager;
/*     */ import com.dstz.org.core.manager.PostManager;
/*     */ import com.dstz.org.core.model.OrgRelation;
/*     */ import com.dstz.org.core.model.Post;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.Serializable;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.compress.utils.IOUtils;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.apache.poi.ss.usermodel.Cell;
/*     */ import org.apache.poi.ss.usermodel.CellType;
/*     */ import org.apache.poi.ss.usermodel.Row;
/*     */ import org.apache.poi.ss.usermodel.Sheet;
/*     */ import org.apache.poi.ss.usermodel.Workbook;
/*     */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.annotation.PostMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/org/post/default"})
/*     */ @Api(description = "??????????????????")
/*     */ public class PostController
/*     */   extends BaseController<Post> {
/*     */   @Resource
/*     */   PostManager postManager;
/*     */   @Resource
/*     */   OrgRelationManager orgRelationManager;
/*     */   @Autowired
/*     */   ICurrentContext currentContext;
/*     */   
/*     */   protected String getModelDesc() {
/*  76 */     return "??????";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private ICache<Object> iCacheFile;
/*     */ 
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private ICurrentContext iCurrentContext;
/*     */ 
/*     */ 
/*     */   
/*     */   @Resource
/*     */   GroupManager groupManager;
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"listJson"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @ApiOperation("??????????????????")
/*     */   @OperateLog
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "Integer", name = "offset", value = "?????????"), @ApiImplicitParam(paramType = "form", dataType = "Integer", name = "limit", value = "?????????????????????10"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "order", value = "????????????????????? ASC"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "sort", value = "????????????(?????????)"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "name", value = "????????????????????????"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "userId", value = "????????????????????????"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "excludeUserId", value = "?????????????????????????????????"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "resultType", value = "???????????? onlyGroupId|onlyGroupName|withUserNum")})
/*     */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) {
/* 102 */     String name = request.getParameter("name");
/* 103 */     String userId = request.getParameter("userId");
/* 104 */     String excludeUserId = request.getParameter("excludeUserId");
/* 105 */     String resultType = request.getParameter("resultType");
/* 106 */     QueryFilter filter = getQueryFilter(request);
/* 107 */     if (StringUtils.isNotEmpty(name)) {
/* 108 */       filter.addFilter("tpost.name_", name, QueryOP.LIKE);
/*     */     }
/* 110 */     if (StringUtils.isNotEmpty(userId)) {
/* 111 */       filter.getParams().put("userId", userId);
/*     */     }
/* 113 */     if (StringUtils.isNotEmpty(excludeUserId)) {
/* 114 */       filter.getParams().put("excludeUserId", excludeUserId);
/*     */     }
/* 116 */     if (StringUtils.isNotEmpty(resultType)) {
/* 117 */       filter.getParams().put("resultType", resultType);
/*     */     }
/* 119 */     List<Post> pageList = this.postManager.query(filter);
/* 120 */     return new PageResult(pageList);
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
/*     */   @RequestMapping({"moveUserPost"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> moveUserPost(HttpServletRequest request, HttpServletResponse response, @RequestBody List<OrgRelation> relations) {
/* 134 */     if (CollectionUtil.isNotEmpty(relations)) {
/* 135 */       relations.forEach(relation -> relation.setType(RelationTypeConstant.POST_USER.getKey()));
/* 136 */       this.orgRelationManager.modifyUserOrg(relations);
/*     */     } 
/* 138 */     return getSuccessResult("????????????!");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @PostMapping({"save"})
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   public ResultMsg<String> save(@RequestBody Post t) {
/*     */     String desc;
/* 150 */     if (StringUtil.isEmpty(t.getId())) {
/* 151 */       desc = "??????%s??????";
/* 152 */       this.postManager.create(t);
/*     */     } else {
/* 154 */       this.postManager.update(t);
/* 155 */       desc = "??????%s??????";
/*     */     } 
/* 157 */     return getSuccessResult(t.getId(), String.format(desc, new Object[] { getModelDesc() }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"remove"})
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   public ResultMsg<String> remove(@RequestParam String id) {
/* 168 */     String[] aryIds = StringUtil.getStringAryByStr(id);
/* 169 */     this.postManager.removeByIds((Serializable[])aryIds);
/* 170 */     return getSuccessResult(String.format("??????%s??????", new Object[] { getModelDesc() }));
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
/*     */   @OperateLog(writeResponse = true)
/*     */   @RequestMapping(value = {"/import"}, method = {RequestMethod.POST})
/*     */   @ResponseBody
/*     */   public ResultMsg<String> postImport(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
/* 186 */     long totalNum = 0L;
/* 187 */     long successNum = 0L;
/* 188 */     if (null != file.getOriginalFilename() && (file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx"))) {
/*     */       try {
/* 190 */         XSSFWorkbook xSSFWorkbook; File realFile = FileUtil.multipartFileToFile(file);
/* 191 */         Workbook workBook = null;
/* 192 */         String type = "";
/* 193 */         if (file.getOriginalFilename().endsWith(".xls")) {
/* 194 */           HSSFWorkbook hSSFWorkbook = new HSSFWorkbook(new FileInputStream(realFile));
/* 195 */           type = "xls";
/* 196 */         } else if (file.getOriginalFilename().endsWith(".xlsx")) {
/* 197 */           xSSFWorkbook = new XSSFWorkbook(new FileInputStream(realFile));
/* 198 */           type = "xlsx";
/*     */         } 
/* 200 */         if (null != xSSFWorkbook) {
/* 201 */           Sheet sheet = xSSFWorkbook.getSheetAt(0);
/* 202 */           if (null != sheet) {
/* 203 */             boolean hasError = false;
/* 204 */             String[] cellName = { "??????", "??????", "??????", "??????", "??????", "??????????????????", "??????" };
/* 205 */             Map<String, String> mapOrg = new HashMap<>();
/* 206 */             int lastRowNum = sheet.getLastRowNum();
/*     */ 
/*     */             
/* 209 */             Map<String, String> mapLevel = new HashMap<>();
/* 210 */             Map<String, String> mapPostType = new HashMap<>();
/*     */ 
/*     */             
/* 213 */             for (int i = 2; i <= lastRowNum; i++) {
/* 214 */               Row data = sheet.getRow(i);
/* 215 */               if (null == data) {
/*     */                 break;
/*     */               }
/* 218 */               StringBuilder errorMsg = new StringBuilder();
/*     */               try {
/* 220 */                 String parentOrgPath = getCellStringData(data.getCell(1));
/* 221 */                 String name = getCellStringData(data.getCell(2));
/* 222 */                 String code = getCellStringData(data.getCell(3));
/* 223 */                 if (StringUtils.isEmpty(parentOrgPath) && StringUtils.isEmpty(name) && StringUtils.isEmpty(code)) {
/*     */                   break;
/*     */                 }
/* 226 */                 totalNum++;
/* 227 */                 if (StringUtils.isEmpty(parentOrgPath)) {
/* 228 */                   errorMsg.append("??????????????????").append("\n ");
/*     */                 }
/* 230 */                 if (StringUtils.isEmpty(name)) {
/* 231 */                   errorMsg.append("??????????????????").append("\n ");
/*     */                 }
/* 233 */                 if (StringUtils.isEmpty(code)) {
/* 234 */                   errorMsg.append("??????????????????").append("\n ");
/*     */                 }
/* 236 */                 String postType = getCellStringData(data.getCell(4));
/* 237 */                 String isCivilServant = getCellStringData(data.getCell(5));
/* 238 */                 String level = getCellStringData(data.getCell(6));
/* 239 */                 String desc = getCellStringData(data.getCell(7));
/* 240 */                 String orgId = this.groupManager.findOrgId(mapOrg, parentOrgPath);
/*     */                 
/* 242 */                 Post post = new Post();
/* 243 */                 Post oldPost = this.postManager.getByAlias(code);
/* 244 */                 post.setName(name);
/* 245 */                 post.setOrgId(orgId);
/* 246 */                 post.setCode(code);
/* 247 */                 if (StringUtils.isNotEmpty(postType)) {
/* 248 */                   if (null == mapPostType.get(postType)) {
/* 249 */                     errorMsg.append("?????????").append(postType).append("?????????").append("\n ");
/*     */                   } else {
/* 251 */                     post.setType(mapPostType.get(postType));
/*     */                   } 
/*     */                 }
/* 254 */                 int iIsCivilServant = 2;
/* 255 */                 if ("???".equals(isCivilServant)) {
/* 256 */                   iIsCivilServant = 1;
/*     */                 }
/* 258 */                 post.setIsCivilServant(Integer.valueOf(iIsCivilServant));
/* 259 */                 if (StringUtils.isNotEmpty(level)) {
/* 260 */                   if (null == mapLevel.get(level)) {
/* 261 */                     errorMsg.append("??????????????????").append(level).append("?????????").append("\n ");
/*     */                   } else {
/* 263 */                     post.setLevel(mapLevel.get(level));
/*     */                   } 
/*     */                 }
/* 266 */                 post.setDesc(desc);
/* 267 */                 if (errorMsg.length() == 0) {
/* 268 */                   if (null != oldPost) {
/* 269 */                     post.setId(oldPost.getId());
/* 270 */                     this.postManager.update(post);
/*     */                   } else {
/* 272 */                     this.postManager.create(post);
/*     */                   } 
/* 274 */                   successNum++;
/* 275 */                   if (i == lastRowNum) {
/* 276 */                     sheet.removeRow(data);
/*     */                   } else {
/* 278 */                     sheet.shiftRows(i + 1, lastRowNum, -1);
/*     */                   } 
/* 280 */                   i--;
/* 281 */                   lastRowNum--;
/*     */                 } 
/* 283 */               } catch (Exception e) {
/* 284 */                 errorMsg.append(e.getMessage()).append("\n ");
/*     */               } 
/* 286 */               if (errorMsg.length() > 0) {
/* 287 */                 data.createCell(cellName.length).setCellValue(errorMsg.toString());
/* 288 */                 hasError = true;
/*     */               } 
/*     */             } 
/* 291 */             if (hasError) {
/* 292 */               sheet.setColumnWidth(cellName.length, 6144);
/* 293 */               xSSFWorkbook.write(new FileOutputStream(realFile));
/* 294 */               String cacheKey = "??????????????????????????????????????????-file_upload_" + this.iCurrentContext.getCurrentUserId() + System.currentTimeMillis() + "." + type;
/* 295 */               this.iCacheFile.add(cacheKey, realFile.getAbsolutePath());
/* 296 */               return ResultMsg.SUCCESS(cacheKey);
/*     */             } 
/*     */           } 
/*     */         } 
/* 300 */       } catch (FileNotFoundException e) {
/* 301 */         throw new BusinessMessage("????????????????????????");
/* 302 */       } catch (IOException e) {
/* 303 */         throw new BusinessMessage("?????????????????????");
/* 304 */       } catch (Exception e) {
/* 305 */         throw new BusinessMessage("???????????????");
/*     */       } 
/*     */     } else {
/* 308 */       throw new BusinessMessage("?????????????????????");
/*     */     } 
/* 310 */     return ResultMsg.SUCCESS("????????????,???" + totalNum + "????????????" + successNum + "???????????????.");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"/exportError"}, method = {RequestMethod.GET})
/*     */   @ResponseBody
/*     */   public ResponseEntity<byte[]> exportError(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "fileKey") String fileKey) {
/*     */     try {
/* 317 */       String path = String.valueOf(this.iCacheFile.getByKey(fileKey));
/* 318 */       this.iCacheFile.delByKey(fileKey);
/* 319 */       File realFile = new File(path);
/* 320 */       HttpHeaders headers = new HttpHeaders();
/* 321 */       headers.setContentDispositionFormData("attachment", "filename=user.xls");
/* 322 */       headers.setContentType(MediaType.MULTIPART_FORM_DATA);
/* 323 */       return new ResponseEntity(IOUtils.toByteArray(new FileInputStream(realFile)), (MultiValueMap)headers, HttpStatus.OK);
/* 324 */     } catch (Exception e) {
/* 325 */       throw new BusinessMessage("???????????????");
/*     */     } 
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
/*     */   private String getCellStringData(Cell cell) {
/* 339 */     String value = "";
/* 340 */     if (cell != null) {
/* 341 */       cell.setCellType(CellType.STRING);
/* 342 */       value = cell.getStringCellValue();
/* 343 */       if (StringUtils.isNotBlank(value)) {
/* 344 */         value = value.trim();
/*     */       }
/*     */     } 
/* 347 */     return value;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/rest/controller/PostController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */