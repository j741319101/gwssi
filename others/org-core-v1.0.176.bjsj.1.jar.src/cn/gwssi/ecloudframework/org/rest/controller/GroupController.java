/*     */ package cn.gwssi.ecloudframework.org.rest.controller;
/*     */ import cn.gwssi.ecloudframework.base.api.aop.annotion.CatchErr;
/*     */ import cn.gwssi.ecloudframework.base.api.aop.annotion.OperateLog;
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessMessage;
/*     */ import cn.gwssi.ecloudframework.base.api.model.IDModel;
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.ResultMsg;
/*     */ import cn.gwssi.ecloudframework.base.core.cache.ICache;
/*     */ import cn.gwssi.ecloudframework.base.core.util.BeanUtils;
/*     */ import cn.gwssi.ecloudframework.base.core.util.FileUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.base.db.model.page.PageResult;
/*     */ import cn.gwssi.ecloudframework.base.rest.BaseController;
/*     */ import cn.gwssi.ecloudframework.base.rest.util.RequestUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.constant.GroupGradeConstant;
/*     */ import cn.gwssi.ecloudframework.org.api.context.ICurrentContext;
/*     */ import cn.gwssi.ecloudframework.org.core.model.Group;
/*     */ import cn.gwssi.ecloudframework.org.core.model.OrgTree;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import io.swagger.annotations.ApiOperation;
/*     */ import java.io.File;
/*     */ import java.io.FileInputStream;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.annotation.Resource;
/*     */ import javax.servlet.http.HttpServletRequest;
/*     */ import javax.servlet.http.HttpServletResponse;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.apache.poi.ss.usermodel.Cell;
/*     */ import org.apache.poi.ss.usermodel.CellType;
/*     */ import org.apache.poi.ss.usermodel.Row;
/*     */ import org.apache.poi.ss.usermodel.Sheet;
/*     */ import org.apache.poi.ss.usermodel.Workbook;
/*     */ import org.apache.poi.xssf.usermodel.XSSFWorkbook;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.http.HttpHeaders;
/*     */ import org.springframework.http.ResponseEntity;
/*     */ import org.springframework.web.bind.annotation.GetMapping;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.RequestMethod;
/*     */ import org.springframework.web.bind.annotation.RequestParam;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ import org.springframework.web.multipart.MultipartFile;
/*     */ 
/*     */ @RestController
/*     */ @RequestMapping({"/org/group/default"})
/*     */ @Api("组服务接口")
/*     */ public class GroupController extends BaseController<Group> {
/*  56 */   private Logger logger = LoggerFactory.getLogger(getClass());
/*     */ 
/*     */   
/*     */   @Resource
/*     */   GroupManager groupManager;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   UserManager userManager;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   OrgRelationManager orgRelationMananger;
/*     */ 
/*     */   
/*     */   @Resource
/*     */   private ICache<Object> iCacheFile;
/*     */   
/*     */   @Resource
/*     */   private ICurrentContext iCurrentContext;
/*     */ 
/*     */   
/*     */   @RequestMapping({"listJson"})
/*     */   public PageResult listJson(HttpServletRequest request, HttpServletResponse response) {
/*  80 */     QueryFilter queryFilter = getQueryFilter(request);
/*  81 */     String parentId = request.getParameter("parentId");
/*  82 */     if (StringUtil.isNotEmpty(parentId)) {
/*  83 */       queryFilter.addFilter("torg.parent_id_", parentId, QueryOP.EQUAL);
/*     */     }
/*  85 */     List<Group> orgList = this.groupManager.query(queryFilter);
/*  86 */     return new PageResult(orgList);
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"isExist"})
/*     */   public boolean isExist(HttpServletRequest request, HttpServletResponse response) {
/*  92 */     String oldCode = RequestUtil.getString(request, "oldCode");
/*  93 */     String code = RequestUtil.getString(request, "key");
/*  94 */     if (oldCode.equals(code) && StringUtil.isNotEmpty(code)) {
/*  95 */       return false;
/*     */     }
/*  97 */     if (StringUtil.isNotEmpty(code)) {
/*  98 */       Group temp = this.groupManager.getByCode(code);
/*  99 */       return (temp != null);
/*     */     } 
/* 101 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"get"})
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   public ResultMsg<Group> get(@RequestParam String id) {
/* 115 */     Group group = (Group)this.groupManager.get(id);
/* 116 */     if (group != null && !"0".equals(group.getParentId())) {
/* 117 */       String parentOrgName = ((Group)this.groupManager.get(group.getParentId())).getName();
/* 118 */       group.setParentName(parentOrgName);
/*     */     } 
/*     */     
/* 121 */     return getSuccessResult(group);
/*     */   }
/*     */   
/*     */   @RequestMapping({"getTreeData"})
/*     */   public List<OrgTree> getTreeData(HttpServletRequest request, HttpServletResponse response) {
/* 126 */     List<OrgTree> groupTreeList = getGroupTree();
/* 127 */     if (CollectionUtil.isEmpty(groupTreeList)) {
/* 128 */       groupTreeList = new ArrayList<>();
/*     */     }
/* 130 */     if (CollectionUtil.isEmpty(groupTreeList)) {
/* 131 */       OrgTree rootGroup = new OrgTree();
/* 132 */       rootGroup.setName("组织");
/* 133 */       rootGroup.setId("0");
/* 134 */       groupTreeList.add(rootGroup);
/*     */     } 
/* 136 */     return groupTreeList;
/*     */   }
/*     */   
/*     */   private List<OrgTree> getGroupTree() {
/* 140 */     List<OrgTree> groupTreeList = new ArrayList<>();
/* 141 */     List<Group> groupList = this.groupManager.getAll();
/* 142 */     for (Group group : groupList) {
/* 143 */       OrgTree groupTree = new OrgTree(group);
/* 144 */       groupTreeList.add(groupTree);
/*     */     } 
/* 146 */     return groupTreeList;
/*     */   }
/*     */   
/*     */   @RequestMapping({"getOrgTree"})
/*     */   @OperateLog
/*     */   public ResultMsg<List<OrgTree>> getOrgTree() {
/* 152 */     List<OrgTree> groupTreeList = getGroupTree();
/* 153 */     if (CollectionUtil.isEmpty(groupTreeList)) {
/* 154 */       groupTreeList = new ArrayList<>();
/*     */     }
/*     */     
/* 157 */     if (CollectionUtil.isEmpty(groupTreeList)) {
/* 158 */       OrgTree rootGroup = new OrgTree();
/* 159 */       rootGroup.setName("组织");
/* 160 */       rootGroup.setId("0");
/* 161 */       groupTreeList.add(rootGroup);
/*     */     } 
/*     */     
/* 164 */     return getSuccessResult(BeanUtils.listToTree(groupTreeList));
/*     */   }
/*     */ 
/*     */   
/*     */   protected String getModelDesc() {
/* 169 */     return "组织";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping(value = {"changeOrder"}, method = {RequestMethod.POST})
/*     */   public ResultMsg<String> changeOrder(@RequestBody List<Group> param) {
/* 180 */     this.groupManager.chageOrder(param);
/* 181 */     return getSuccessResult();
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
/*     */   @GetMapping({"queryAllGroup"})
/*     */   @ApiOperation(value = "查询当前用户公司下所有机构", notes = "选人组件：查询当前用户公司下所有机构")
/*     */   public ResultMsg<List<OrgTree>> queryAllGroup() {
/* 195 */     return getSuccessResult(BeanUtils.listToTree((List)this.groupManager.queryAllGroup().stream()
/* 196 */           .map(OrgTree::new).collect(Collectors.toList())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @RequestMapping({"save"})
/*     */   @CatchErr
/*     */   @OperateLog
/*     */   public ResultMsg<String> save(@RequestBody Group t) {
/*     */     String desc;
/* 209 */     if (StringUtil.isEmpty(t.getId())) {
/* 210 */       desc = "添加%s成功";
/* 211 */       this.groupManager.create(t);
/*     */     } else {
/* 213 */       this.groupManager.update(t);
/* 214 */       desc = "更新%s成功";
/*     */     } 
/* 216 */     return getSuccessResult(t.getId(), String.format(desc, new Object[] { getModelDesc() }));
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
/* 227 */     String[] aryIds = StringUtil.getStringAryByStr(id);
/* 228 */     this.groupManager.removeByIds((Serializable[])aryIds);
/* 229 */     return getSuccessResult(String.format("删除%s成功", new Object[] { getModelDesc() }));
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
/*     */   public ResultMsg<String> groupImport(@RequestParam("file") MultipartFile file, HttpServletRequest request, HttpServletResponse response) {
/* 245 */     long totalNum = 0L;
/* 246 */     long successNum = 0L;
/* 247 */     if (null != file.getOriginalFilename() && (file.getOriginalFilename().endsWith(".xls") || file.getOriginalFilename().endsWith(".xlsx"))) {
/*     */       try {
/* 249 */         XSSFWorkbook xSSFWorkbook; File realFile = FileUtil.multipartFileToFile(file);
/* 250 */         Workbook workBook = null;
/* 251 */         String type = "";
/* 252 */         if (file.getOriginalFilename().endsWith(".xls")) {
/* 253 */           HSSFWorkbook hSSFWorkbook = new HSSFWorkbook(new FileInputStream(realFile));
/* 254 */           type = "xls";
/* 255 */         } else if (file.getOriginalFilename().endsWith(".xlsx")) {
/* 256 */           xSSFWorkbook = new XSSFWorkbook(new FileInputStream(realFile));
/* 257 */           type = "xlsx";
/*     */         } 
/* 259 */         if (null != xSSFWorkbook) {
/* 260 */           Sheet sheet = xSSFWorkbook.getSheetAt(0);
/* 261 */           if (null != sheet) {
/* 262 */             boolean hasError = false;
/* 263 */             String[] cellName = { "编号", "上级机构", "机构名称", "组织类型", "机构编码", "备注" };
/* 264 */             Map<String, String> mapOrg = new HashMap<>();
/* 265 */             int lastRowNum = sheet.getLastRowNum();
/* 266 */             for (int i = 2; i <= lastRowNum; i++) {
/* 267 */               Row data = sheet.getRow(i);
/* 268 */               if (null == data) {
/*     */                 break;
/*     */               }
/* 271 */               StringBuilder errorMsg = new StringBuilder();
/*     */               try {
/* 273 */                 String parentOrgId, parentOrgPath = getCellStringData(data.getCell(1));
/* 274 */                 String name = getCellStringData(data.getCell(2));
/* 275 */                 String code = getCellStringData(data.getCell(4));
/* 276 */                 if (StringUtils.isEmpty(parentOrgPath) && StringUtils.isEmpty(name) && StringUtils.isEmpty(code)) {
/*     */                   break;
/*     */                 }
/* 279 */                 totalNum++;
/* 280 */                 if (StringUtils.isEmpty(parentOrgPath)) {
/* 281 */                   errorMsg.append("上级机构不能为空").append("\n ");
/*     */                 }
/* 283 */                 if (StringUtils.isEmpty(name)) {
/* 284 */                   errorMsg.append("机构名称不能为空").append("\n ");
/*     */                 }
/* 286 */                 String orgType = getCellStringData(data.getCell(3));
/* 287 */                 if (StringUtils.isEmpty(code)) {
/* 288 */                   errorMsg.append("机构编号不能为空").append("\n ");
/*     */                 }
/* 290 */                 String desc = getCellStringData(data.getCell(5));
/*     */                 
/* 292 */                 if ("0".equals(parentOrgPath)) {
/* 293 */                   parentOrgId = "0";
/*     */                 } else {
/* 295 */                   parentOrgId = this.groupManager.findOrgId(mapOrg, parentOrgPath);
/*     */                 } 
/* 297 */                 Group group = new Group();
/* 298 */                 Group oldGroup = this.groupManager.getByCode(code);
/* 299 */                 group.setName(name);
/* 300 */                 group.setParentId(parentOrgId);
/* 301 */                 group.setCode(code);
/* 302 */                 GroupGradeConstant groupGradeConstant = GroupGradeConstant.getByLabel(orgType);
/* 303 */                 if (null == groupGradeConstant) {
/* 304 */                   errorMsg.append("组织类型：").append(orgType).append("不存在").append("\n ");
/*     */                 }
/* 306 */                 group.setType(Integer.valueOf(groupGradeConstant.key()));
/* 307 */                 group.setDesc(desc);
/* 308 */                 if (errorMsg.length() == 0) {
/* 309 */                   if (null != oldGroup) {
/* 310 */                     group.setId(oldGroup.getId());
/* 311 */                     this.groupManager.update(group);
/*     */                   } else {
/* 313 */                     this.groupManager.create(group);
/*     */                   } 
/* 315 */                   successNum++;
/* 316 */                   if (i == lastRowNum) {
/* 317 */                     sheet.removeRow(data);
/*     */                   } else {
/* 319 */                     sheet.shiftRows(i + 1, lastRowNum, -1);
/*     */                   } 
/* 321 */                   i--;
/* 322 */                   lastRowNum--;
/*     */                 } 
/* 324 */               } catch (Exception e) {
/* 325 */                 errorMsg.append(e.getMessage()).append("\n ");
/*     */               } 
/* 327 */               if (errorMsg.length() > 0) {
/* 328 */                 data.createCell(cellName.length).setCellValue(errorMsg.toString());
/* 329 */                 hasError = true;
/*     */               } 
/*     */             } 
/* 332 */             if (hasError) {
/* 333 */               sheet.setColumnWidth(cellName.length, 6144);
/* 334 */               xSSFWorkbook.write(new FileOutputStream(realFile));
/* 335 */               String cacheKey = "导入失败！有失败记录，请查收-file_upload_" + this.iCurrentContext.getCurrentUserId() + System.currentTimeMillis() + "." + type;
/* 336 */               this.iCacheFile.add(cacheKey, realFile.getAbsolutePath());
/* 337 */               return ResultMsg.SUCCESS(cacheKey);
/*     */             } 
/*     */           } 
/*     */         } 
/* 341 */       } catch (FileNotFoundException e) {
/* 342 */         throw new BusinessMessage("未找到上传文件！");
/* 343 */       } catch (IOException e) {
/* 344 */         throw new BusinessMessage("上传文件失败！");
/* 345 */       } catch (Exception e) {
/* 346 */         throw new BusinessMessage("上传失败！");
/*     */       } 
/*     */     } else {
/* 349 */       throw new BusinessMessage("文件格式错误！");
/*     */     } 
/* 351 */     return ResultMsg.SUCCESS("导入成功,共" + totalNum + "条数据，" + successNum + "条导入成功.");
/*     */   }
/*     */   
/*     */   @RequestMapping(value = {"/exportError"}, method = {RequestMethod.GET})
/*     */   @ResponseBody
/*     */   public ResponseEntity<byte[]> exportError(HttpServletRequest request, HttpServletResponse response, @RequestParam(name = "fileKey") String fileKey) {
/*     */     try {
/* 358 */       String path = String.valueOf(this.iCacheFile.getByKey(fileKey));
/* 359 */       this.iCacheFile.delByKey(fileKey);
/* 360 */       File realFile = new File(path);
/* 361 */       HttpHeaders headers = new HttpHeaders();
/* 362 */       headers.setContentDispositionFormData("attachment", "filename=user.xls");
/* 363 */       headers.setContentType(MediaType.MULTIPART_FORM_DATA);
/* 364 */       return new ResponseEntity(IOUtils.toByteArray(new FileInputStream(realFile)), (MultiValueMap)headers, HttpStatus.OK);
/* 365 */     } catch (Exception e) {
/* 366 */       throw new BusinessMessage("下载失败！");
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
/* 380 */     String value = "";
/* 381 */     if (cell != null) {
/* 382 */       cell.setCellType(CellType.STRING);
/* 383 */       value = cell.getStringCellValue();
/* 384 */       if (StringUtils.isNotBlank(value)) {
/* 385 */         value = value.trim();
/*     */       }
/*     */     } 
/* 388 */     return value;
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
/*     */   @GetMapping({"resetPath"})
/*     */   @ApiOperation("重置组织")
/*     */   public ResultMsg<String> resetPath() {
/* 402 */     List<Group> lstGroup = this.groupManager.getAll();
/* 403 */     Map<String, String> temp = new HashMap<>();
/* 404 */     lstGroup.forEach(group -> (String)temp.put(group.getId(), group.getParentId()));
/* 405 */     lstGroup.forEach(group -> {
/*     */           StringBuilder path = new StringBuilder(group.getId());
/*     */           String parentId = group.getParentId();
/*     */           Set<String> ids = new HashSet<>();
/*     */           ids.add(group.getId());
/*     */           int num = 1;
/*     */           while (num <= 100 && StringUtils.isNotEmpty(parentId) && !"0".equals(parentId)) {
/*     */             if (ids.contains(parentId)) {
/*     */               this.logger.error("id为" + group.getId() + "的组织存在循环父节点的情况");
/*     */               continue;
/*     */             } 
/*     */             ids.add(parentId);
/*     */             path.insert(0, parentId + ".");
/*     */             parentId = (String)temp.get(parentId);
/*     */           } 
/*     */           if ("0".equals(parentId)) {
/*     */             Group groupTemp = new Group();
/*     */             groupTemp.setId(group.getId());
/*     */             groupTemp.setPath(path.toString());
/*     */             if (!path.toString().equals(group.getPath())) {
/*     */               this.logger.info("id为" + group.getId() + "的组织path异常 被重置");
/*     */               this.groupManager.updateByPrimaryKeySelective(groupTemp);
/*     */             } 
/*     */           } else {
/*     */             this.logger.error("id为" + group.getId() + "的组织ptah断裂" + path.toString());
/*     */           } 
/*     */         });
/* 432 */     return getSuccessResult("重置成功");
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/rest/controller/GroupController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */