/*     */ package com.dstz.org.rest.controller;
/*     */ 
/*     */ import com.dstz.base.api.aop.annotion.CatchErr;
/*     */ import com.dstz.base.api.aop.annotion.OperateLog;
/*     */ import com.dstz.base.api.aop.annotion.ParamValidate;
/*     */ import com.dstz.base.api.constant.BaseStatusCode;
/*     */ import com.dstz.base.api.constant.IStatusCode;
/*     */ import com.dstz.base.api.exception.BusinessError;
/*     */ import com.dstz.base.api.exception.BusinessException;
/*     */ import com.dstz.base.api.exception.BusinessMessage;
/*     */ import com.dstz.base.api.model.IDModel;
/*     */ import com.dstz.base.api.query.QueryFilter;
/*     */ import com.dstz.base.api.query.QueryOP;
/*     */ import com.dstz.base.api.response.impl.ResultMsg;
/*     */ import com.dstz.base.core.cache.ICache;
/*     */ import com.dstz.base.core.util.FileUtil;
/*     */ import com.dstz.base.core.util.sm3.SM3Util;
/*     */ import com.dstz.base.db.model.page.PageResult;
/*     */ import com.dstz.base.rest.BaseController;
/*     */ import com.dstz.base.rest.util.RequestUtil;
/*     */ import com.dstz.org.api.constant.RelationTypeConstant;
/*     */ import com.dstz.org.api.context.ICurrentContext;
/*     */ import com.dstz.org.api.model.IUser;
/*     */ import com.dstz.org.core.manager.GroupManager;
/*     */ import com.dstz.org.core.manager.UserManager;
/*     */ import com.dstz.org.core.model.OrgRelation;
/*     */ import com.dstz.org.core.model.User;
/*     */ import com.dstz.sys.api.platform.ISysPropertiesPlatFormService;
/*     */ import com.dstz.sys.util.ContextUtil;
/*     */ import io.swagger.annotations.Api;
/*     */ import io.swagger.annotations.ApiImplicitParam;
/*     */ import io.swagger.annotations.ApiImplicitParams;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileNotFoundException;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.util.ArrayList;
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
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.HttpStatus;
/*     */ import org.springframework.http.MediaType;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.util.MultiValueMap;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.bind.annotation.RestController;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/org/user/default"})
/*     */ @Api(description = "??????????????????")
/*     */ public class UserController
/*     */   extends BaseController<User>
/*     */ {
/*     */   @Resource
/*     */   UserManager userManager;
/*     */   @Resource
/*     */   GroupManager groupManager;
/*     */   @Resource
/*     */   private ICache<IUser> iCache;
/*     */   
/*     */   @RequestMapping({"save"})
/*     */   @CatchErr("?????????????????????")
/*     */   @ParamValidate
/*     */   @OperateLog
/*     */   public ResultMsg<String> save(@RequestBody User user) {
/*  86 */     if (this.userManager.isUserExist(user)) {
/*  87 */       throw new BusinessMessage("???????????????????????????!");
/*     */     }
/*     */     
/*  90 */     this.userManager.saveUserInfo(user);
/*  91 */     return getSuccessResult(user.getId(), "????????????");
/*     */   }
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private ICache<Object> iCacheFile;
/*     */   
/*     */   @RequestMapping(value = {"updateUserPassWorld"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr("??????????????????")
/*     */   @ApiOperation("????????????")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "oldPassword", value = "?????????"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "newPassword", value = "?????????"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "id", value = "????????????ID"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "isPasswordEncrypt", value = "?????????????????????1:??????0:??? ?????????")})
/*     */   public ResultMsg<String> updateUserPsw(HttpServletRequest request, HttpServletResponse response) {
/* 103 */     String oldPassWorld = RequestUtil.getRQString(request, "oldPassword", "???????????????");
/* 104 */     String newPassword = RequestUtil.getRQString(request, "newPassword", "???????????????");
/* 105 */     String userId = RequestUtil.getRQString(request, "id", "????????????ID");
/* 106 */     String isPasswordEncrypt = RequestUtil.getString(request, "isPasswordEncrypt");
/*     */     
/* 108 */     if (!userId.equals(ContextUtil.getCurrentUserId())) {
/* 109 */       throw new BusinessException("???????????????????????????");
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     if (!"1".equals(isPasswordEncrypt)) {
/* 116 */       oldPassWorld = SM3Util.MD5Encode(oldPassWorld);
/* 117 */       newPassword = SM3Util.MD5Encode(newPassword);
/*     */     } 
/* 119 */     User user = (User)this.userManager.get(ContextUtil.getCurrentUserId());
/* 120 */     if (!user.getPassword().equals(SM3Util.SM3Encode(oldPassWorld))) {
/* 121 */       throw new BusinessMessage("?????????????????????");
/*     */     }
/*     */     
/* 124 */     String decentralizationEnable = this.iSysPropertiesPlatFormService.getByAlias("security.pwd.weakDisable");
/* 125 */     if (StringUtils.isNotEmpty(decentralizationEnable) && "true".equals(decentralizationEnable) && 
/* 126 */       StringUtils.isNotEmpty(newPassword) && newPassword.length() < 6) {
/* 127 */       throw new BusinessMessage("??????????????????6??????????????????????????????");
/*     */     }
/*     */     
/* 130 */     user.setPassword(SM3Util.SM3Encode(newPassword));
/* 131 */     this.userManager.update(user);
/* 132 */     return getSuccessResult("??????????????????");
/*     */   }
/*     */   
/*     */   @Resource
/*     */   private ICurrentContext iCurrentContext;
/*     */   @Resource
/*     */   ISysPropertiesPlatFormService iSysPropertiesPlatFormService;
/*     */   
/*     */   @RequestMapping({"remove"})
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   public ResultMsg<String> remove(@RequestParam String id) throws Exception {
/* 144 */     User user = (User)this.userManager.get(id);
/* 145 */     if (null == user)
/* 146 */       throw new BusinessError("???????????????"); 
/* 147 */     if (user.getActiveStatus().intValue() == 1 && user.getStatus().intValue() == 1)
/*     */     {
/* 149 */       throw new BusinessError("?????????????????????????????????????????????");
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 154 */     return super.remove(id);
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getModelDesc() {
/* 159 */     return "??????";
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
/*     */   @RequestMapping({"status"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> status(@RequestParam(name = "userId") String userId, @RequestParam(name = "status") int status) {
/* 174 */     User user = new User();
/* 175 */     user.setId(userId);
/* 176 */     user.setStatus(Integer.valueOf(status));
/* 177 */     this.userManager.updateByPrimaryKeySelective(user);
/* 178 */     return getSuccessResult();
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
/*     */   @RequestMapping({"reset"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> reset(@RequestParam(name = "userId") String userId) {
/* 192 */     User user = new User();
/* 193 */     user.setId(userId);
/* 194 */     user.setPassword(SM3Util.SM3EncodePws("111111"));
/* 195 */     this.userManager.updateByPrimaryKeySelective(user);
/* 196 */     return getSuccessResult();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"getCurrentUserInfo"})
/*     */   @CatchErr
/*     */   public ResultMsg<User> getCurrentUserInfo(HttpServletRequest request, HttpServletResponse response) {
/* 209 */     String userId = ContextUtil.getCurrentUserId();
/* 210 */     User user = new User();
/*     */     try {
/* 212 */       if (StringUtils.isNotEmpty(userId)) {
/* 213 */         user = (User)this.userManager.get(userId);
/*     */         
/* 215 */         if (null != user) {
/* 216 */           if (null == user.getOrgRelationList()) {
/* 217 */             user.setOrgRelationList(new ArrayList());
/*     */           }
/* 219 */           OrgRelation orgRelation = new OrgRelation();
/* 220 */           orgRelation.setRoleAlias("ROLE_USER");
/* 221 */           orgRelation.setRoleName("????????????");
/* 222 */           orgRelation.setType(RelationTypeConstant.USER_ROLE.getKey());
/* 223 */           user.getOrgRelationList().add(orgRelation);
/*     */         } else {
/* 225 */           throw new BusinessException("??????????????????????????????????????????");
/*     */         } 
/*     */       } else {
/* 228 */         throw new BusinessException("???????????????????????????");
/*     */       } 
/* 230 */     } catch (Exception e) {
/*     */       try {
/* 232 */         ResultMsg<User> resultMsg = new ResultMsg((IStatusCode)BaseStatusCode.TIMEOUT, e.getMessage());
/* 233 */         response.setStatus(Integer.parseInt(BaseStatusCode.TIMEOUT.getCode()));
/* 234 */         return resultMsg;
/* 235 */       } catch (Exception exception) {}
/*     */     } 
/*     */ 
/*     */     
/* 239 */     return getSuccessResult(user);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"getAllEnableUserNum"})
/*     */   @CatchErr
/*     */   public ResultMsg<Integer> getAllEnableUserNum() {
/* 252 */     return getSuccessResult(this.userManager.getAllEnableUserNum());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"onLineUser"})
/*     */   @CatchErr
/*     */   public ResultMsg<Integer> onLineUser() {
/* 265 */     return getSuccessResult(Integer.valueOf(this.iCache.keys("jwtToken:jwt:pc").size()));
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
/*     */   @RequestMapping({"listJson"})
/*     */   @OperateLog
/*     */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) {
/* 281 */     String name = request.getParameter("name");
/* 282 */     String orgIds = request.getParameter("orgIds");
/* 283 */     String roleIds = request.getParameter("roleIds");
/* 284 */     String postIds = request.getParameter("postIds");
/* 285 */     QueryFilter filter = getQueryFilter(request);
/* 286 */     if (StringUtils.isNotEmpty(name)) {
/* 287 */       filter.addFilter("tuser.fullname_", name, QueryOP.LIKE);
/*     */     }
/* 289 */     Map<String, Object> params = new HashMap<>();
/* 290 */     if (StringUtils.isNotEmpty(orgIds)) {
/* 291 */       params.put("orgIds", orgIds.split(","));
/*     */     }
/* 293 */     if (StringUtils.isNotEmpty(roleIds)) {
/* 294 */       params.put("roleIds", roleIds.split(","));
/*     */     }
/* 296 */     if (StringUtils.isNotEmpty(postIds)) {
/* 297 */       params.put("postIds", postIds.split(","));
/*     */     }
/* 299 */     filter.addParams(params);
/* 300 */     List<User> pageList = this.userManager.query(filter);
/* 301 */     return new PageResult(pageList);
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
/*     */   @RequestMapping({"active"})
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   public ResultMsg<String> active(@RequestParam(name = "userId") String userId) {
/* 316 */     User user = new User();
/* 317 */     user.setId(userId);
/* 318 */     user.setActiveStatus(Integer.valueOf(1));
/* 319 */     this.userManager.updateByPrimaryKeySelective(user);
/* 320 */     return getSuccessResult();
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
/*     */   @RequestMapping({"secretLevel"})
/*     */   @CatchErr
/*     */   public ResultMsg<String> secretLevel(@RequestParam(name = "userId") String userId, @RequestParam(name = "secretLevel") Integer secretLevel) {
/* 335 */     User user = new User();
/* 336 */     user.setId(userId);
/* 337 */     user.setSecretLevel(secretLevel);
/* 338 */     this.userManager.updateByPrimaryKeySelective(user);
/* 339 */     return getSuccessResult();
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
/*     */   @RequestMapping(value = {"updatePhoto"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr("??????????????????")
/*     */   @ApiOperation("????????????")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "photoId", value = "??????id")})
/*     */   public ResultMsg<String> updatePhoto(HttpServletRequest request, HttpServletResponse response) {
/* 355 */     String photoId = RequestUtil.getRQString(request, "photoId", "??????id??????");
/* 356 */     String userId = ContextUtil.getCurrentUserId();
/* 357 */     User user = new User();
/* 358 */     user.setId(userId);
/* 359 */     user.setPhoto(photoId);
/* 360 */     this.userManager.updateByPrimaryKeySelective(user);
/* 361 */     return getSuccessResult("??????????????????");
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
/*     */   
/*     */   @RequestMapping(value = {"updateInfo"}, method = {RequestMethod.POST, RequestMethod.GET})
/*     */   @CatchErr("????????????????????????")
/*     */   @ApiOperation("??????????????????????????????")
/*     */   @ApiImplicitParams({@ApiImplicitParam(paramType = "form", dataType = "String", name = "email", value = "??????"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "mobile", value = "??????"), @ApiImplicitParam(paramType = "form", dataType = "String", name = "telephone", value = "??????")})
/*     */   public ResultMsg<String> updateInfo(HttpServletRequest request, HttpServletResponse response) {
/* 381 */     String email = RequestUtil.getString(request, "email", null);
/* 382 */     String mobile = RequestUtil.getString(request, "mobile", null);
/* 383 */     String telephone = RequestUtil.getString(request, "telephone", null);
/* 384 */     String userId = ContextUtil.getCurrentUserId();
/* 385 */     User user = new User();
/* 386 */     user.setId(userId);
/* 387 */     user.setEmail(email);
/* 388 */     user.setMobile(mobile);
/* 389 */     user.setTelephone(telephone);
/* 390 */     this.userManager.updateByPrimaryKeySelective(user);
/* 391 */     return getSuccessResult("????????????????????????");
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
/*     */   public ResultMsg<String> userImport(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
/* 407 */     long totalNum = 0L;
/* 408 */     long successNum = 0L;
/* 409 */     if (null != file.getOriginalFilename() && (file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx"))) {
/*     */       try {
/* 411 */         XSSFWorkbook xSSFWorkbook; File realFile = FileUtil.multipartFileToFile(file);
/* 412 */         Workbook workBook = null;
/* 413 */         String type = "";
/* 414 */         if (file.getOriginalFilename().endsWith(".xls")) {
/* 415 */           HSSFWorkbook hSSFWorkbook = new HSSFWorkbook(new FileInputStream(realFile));
/* 416 */           type = "xls";
/* 417 */         } else if (file.getOriginalFilename().endsWith(".xlsx")) {
/* 418 */           xSSFWorkbook = new XSSFWorkbook(new FileInputStream(realFile));
/* 419 */           type = "xlsx";
/*     */         } 
/* 421 */         if (null != xSSFWorkbook) {
/* 422 */           Sheet sheet = xSSFWorkbook.getSheetAt(0);
/* 423 */           if (null != sheet) {
/* 424 */             boolean hasError = false;
/* 425 */             String[] cellName = { "??????", "?????????", "??????", "??????", "??????", "??????", "????????????", "????????????", "??????", "????????????", "??????", "??????" };
/* 426 */             Map<String, String> mapOrg = new HashMap<>();
/* 427 */             int lastRowNum = sheet.getLastRowNum();
/* 428 */             for (int i = 2; i <= lastRowNum; i++) {
/* 429 */               Row data = sheet.getRow(i);
/* 430 */               if (null == data) {
/*     */                 break;
/*     */               }
/* 433 */               StringBuilder errorMsg = new StringBuilder();
/*     */               try {
/* 435 */                 String mainOrgPath = getCellStringData(data.getCell(1));
/* 436 */                 String name = getCellStringData(data.getCell(2));
/* 437 */                 String account = getCellStringData(data.getCell(3));
/* 438 */                 if (StringUtils.isEmpty(mainOrgPath) && StringUtils.isEmpty(name) && StringUtils.isEmpty(account)) {
/*     */                   break;
/*     */                 }
/* 441 */                 totalNum++;
/* 442 */                 if (StringUtils.isEmpty(mainOrgPath)) {
/* 443 */                   errorMsg.append("?????????????????????").append("\n ");
/*     */                 }
/* 445 */                 if (StringUtils.isEmpty(name)) {
/* 446 */                   errorMsg.append("??????????????????").append("\n ");
/*     */                 }
/* 448 */                 if (StringUtils.isEmpty(account)) {
/* 449 */                   errorMsg.append("??????????????????").append("\n ");
/*     */                 }
/* 451 */                 String password = getCellStringData(data.getCell(4));
/* 452 */                 if (StringUtils.isEmpty(password)) {
/* 453 */                   errorMsg.append("??????????????????").append("\n ");
/*     */                 }
/* 455 */                 String email = getCellStringData(data.getCell(5));
/* 456 */                 String mobile = getCellStringData(data.getCell(6));
/* 457 */                 if (StringUtils.isEmpty(mobile)) {
/* 458 */                   errorMsg.append("????????????????????????").append("\n ");
/*     */                 }
/* 460 */                 String telephone = getCellStringData(data.getCell(7));
/* 461 */                 String address = getCellStringData(data.getCell(8));
/* 462 */                 String sn = getCellStringData(data.getCell(9));
/* 463 */                 int iSn = 0;
/* 464 */                 if (StringUtils.isNotEmpty(sn)) {
/*     */                   try {
/* 466 */                     iSn = Integer.parseInt(sn);
/* 467 */                   } catch (Exception e) {
/* 468 */                     errorMsg.append("???").append(sn).append("????????????").append("\n ");
/*     */                   } 
/*     */                 }
/* 471 */                 String sex = getCellStringData(data.getCell(10));
/* 472 */                 if ("???".equals(sex)) {
/* 473 */                   sex = "0";
/* 474 */                 } else if ("???".equals(sex)) {
/* 475 */                   sex = "1";
/*     */                 } else {
/* 477 */                   sex = "";
/*     */                 } 
/* 479 */                 String orgPaths = getCellStringData(data.getCell(11));
/* 480 */                 if (errorMsg.length() == 0) {
/* 481 */                   String mainOrgId = this.groupManager.findOrgId(mapOrg, mainOrgPath);
/* 482 */                   User user = new User();
/* 483 */                   User oldUser = this.userManager.getByAccount(account);
/* 484 */                   if (null != oldUser) {
/* 485 */                     user.setId(oldUser.getId());
/*     */                   }
/* 487 */                   user.setFullname(name);
/* 488 */                   user.setAccount(account);
/* 489 */                   user.setPassword(password);
/* 490 */                   user.setEmail(email);
/* 491 */                   user.setMobile(mobile);
/* 492 */                   user.setTelephone(telephone);
/* 493 */                   user.setAddress(address);
/* 494 */                   user.setSn(Integer.valueOf(iSn));
/* 495 */                   user.setSex(sex);
/* 496 */                   List<OrgRelation> orgRelationList = new ArrayList<>();
/* 497 */                   OrgRelation orgRelation = new OrgRelation(mainOrgId, "", RelationTypeConstant.GROUP_USER.getKey());
/* 498 */                   orgRelation.setIsMaster(Integer.valueOf(1));
/* 499 */                   orgRelationList.add(orgRelation);
/* 500 */                   if (StringUtils.isNotEmpty(orgPaths)) {
/* 501 */                     orgPaths = orgPaths.replaceAll("???", ";");
/* 502 */                     String[] arrPath = orgPaths.split(";");
/* 503 */                     for (String path : arrPath) {
/* 504 */                       path = path.trim();
/* 505 */                       String orgId = this.groupManager.findOrgId(mapOrg, path);
/* 506 */                       if (!orgId.equals(mainOrgId)) {
/* 507 */                         orgRelation = new OrgRelation(orgId, "", RelationTypeConstant.GROUP_USER.getKey());
/* 508 */                         orgRelation.setIsMaster(Integer.valueOf(0));
/* 509 */                         orgRelationList.add(orgRelation);
/*     */                       } 
/*     */                     } 
/*     */                   } 
/* 513 */                   user.setOrgRelationList(orgRelationList);
/* 514 */                   this.userManager.saveUserInfo(user);
/* 515 */                   successNum++;
/* 516 */                   if (i == lastRowNum) {
/* 517 */                     sheet.removeRow(data);
/*     */                   } else {
/* 519 */                     sheet.shiftRows(i + 1, lastRowNum, -1);
/*     */                   } 
/* 521 */                   i--;
/* 522 */                   lastRowNum--;
/*     */                 } 
/* 524 */               } catch (Exception e) {
/* 525 */                 errorMsg.append(e.getMessage()).append("\n ");
/*     */               } 
/* 527 */               if (errorMsg.length() > 0) {
/* 528 */                 data.createCell(cellName.length).setCellValue(errorMsg.toString());
/* 529 */                 hasError = true;
/*     */               } 
/*     */             } 
/* 532 */             if (hasError) {
/* 533 */               sheet.setColumnWidth(cellName.length, 6144);
/* 534 */               xSSFWorkbook.write(new FileOutputStream(realFile));
/* 535 */               String cacheKey = "??????????????????????????????????????????-file_upload_" + this.iCurrentContext.getCurrentUserId() + System.currentTimeMillis() + "." + type;
/* 536 */               this.iCacheFile.add(cacheKey, realFile.getAbsolutePath());
/* 537 */               return ResultMsg.SUCCESS(cacheKey);
/*     */             } 
/*     */           } 
/*     */         } 
/* 541 */       } catch (FileNotFoundException e) {
/* 542 */         throw new BusinessMessage("????????????????????????");
/* 543 */       } catch (IOException e) {
/* 544 */         throw new BusinessMessage("?????????????????????");
/* 545 */       } catch (Exception e) {
/* 546 */         throw new BusinessMessage("???????????????");
/*     */       } 
/*     */     } else {
/* 549 */       throw new BusinessMessage("?????????????????????");
/*     */     } 
/* 551 */     return ResultMsg.SUCCESS("????????????,???" + totalNum + "????????????" + successNum + "???????????????.");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"/exportError"}, method = {RequestMethod.GET})
/*     */   @ResponseBody
/*     */   public ResponseEntity<byte[]> exportError(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "fileKey") String fileKey) {
/*     */     try {
/* 558 */       String path = String.valueOf(this.iCacheFile.getByKey(fileKey));
/* 559 */       this.iCacheFile.delByKey(fileKey);
/* 560 */       File realFile = new File(path);
/* 561 */       HttpHeaders headers = new HttpHeaders();
/* 562 */       headers.setContentDispositionFormData("attachment", "filename=user.xls");
/* 563 */       headers.setContentType(MediaType.MULTIPART_FORM_DATA);
/* 564 */       return new ResponseEntity(IOUtils.toByteArray(new FileInputStream(realFile)), (MultiValueMap)headers, HttpStatus.OK);
/* 565 */     } catch (Exception e) {
/* 566 */       throw new BusinessMessage("???????????????");
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
/* 580 */     String value = "";
/* 581 */     if (cell != null) {
/* 582 */       cell.setCellType(CellType.STRING);
/* 583 */       value = cell.getStringCellValue();
/* 584 */       if (StringUtils.isNotBlank(value)) {
/* 585 */         value = value.trim();
/*     */       }
/*     */     } 
/* 588 */     return value;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_??????/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/rest/controller/UserController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */