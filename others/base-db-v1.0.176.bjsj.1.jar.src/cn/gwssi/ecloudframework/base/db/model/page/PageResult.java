/*     */ package cn.gwssi.ecloudframework.base.db.model.page;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.response.impl.BaseResult;
/*     */ import com.github.pagehelper.Page;
/*     */ import com.github.pagehelper.PageInfo;
/*     */ import io.swagger.annotations.ApiModel;
/*     */ import io.swagger.annotations.ApiModelProperty;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ @ApiModel(description = "通用分页结果包装类")
/*     */ public class PageResult<T>
/*     */   extends BaseResult
/*     */ {
/*     */   private static final long serialVersionUID = 1L;
/*     */   @ApiModelProperty("分页大小")
/*  17 */   private Integer pageSize = Integer.valueOf(0);
/*     */   @ApiModelProperty("当前页")
/*  19 */   private Integer page = Integer.valueOf(1);
/*     */   @ApiModelProperty("总条数")
/*  21 */   private Integer total = Integer.valueOf(0);
/*     */   
/*     */   @ApiModelProperty("分页列表数据")
/*  24 */   private List rows = null;
/*     */ 
/*     */ 
/*     */   
/*     */   public PageResult() {}
/*     */ 
/*     */   
/*     */   public PageResult(List<T> rows, Integer total) {
/*  32 */     this.rows = rows;
/*  33 */     this.total = total;
/*     */   }
/*     */   
/*     */   public PageResult(List<T> arrayList) {
/*  37 */     this.rows = arrayList;
/*     */ 
/*     */     
/*  40 */     if (arrayList instanceof Page) {
/*  41 */       Page pageList = (Page)arrayList;
/*  42 */       Integer total = Integer.valueOf((new Long(pageList.getTotal())).intValue());
/*  43 */       this.pageSize = Integer.valueOf(pageList.getPageSize());
/*  44 */       setPage(Integer.valueOf(pageList.getPages()));
/*  45 */       setTotal(total);
/*     */     } else {
/*     */       
/*  48 */       this.total = Integer.valueOf(arrayList.size());
/*     */     } 
/*  50 */     setOk(Boolean.TRUE);
/*     */   }
/*     */ 
/*     */   
/*     */   public PageResult(T t) {
/*  55 */     if (t instanceof Page) {
/*  56 */       Page pageList = (Page)t;
/*  57 */       Integer total = Integer.valueOf((new Long(pageList.getTotal())).intValue());
/*  58 */       this.pageSize = Integer.valueOf(pageList.getPageSize());
/*  59 */       this.rows = pageList.getResult();
/*  60 */       setPage(Integer.valueOf(pageList.getPages()));
/*  61 */       setTotal(total);
/*  62 */     } else if (t instanceof PageInfo) {
/*  63 */       PageInfo pageInfo = (PageInfo)t;
/*  64 */       Integer total = Integer.valueOf((new Long(pageInfo.getTotal())).intValue());
/*  65 */       this.pageSize = Integer.valueOf(pageInfo.getPageSize());
/*  66 */       this.rows = pageInfo.getList();
/*  67 */       setPage(Integer.valueOf(pageInfo.getPages()));
/*  68 */       setTotal(total);
/*     */     } else {
/*  70 */       this.rows = (List)t;
/*  71 */       this.total = Integer.valueOf(((List)t).size());
/*     */     } 
/*  73 */     setOk(Boolean.TRUE);
/*     */   }
/*     */   
/*     */   public Integer getPageSize() {
/*  77 */     return this.pageSize;
/*     */   }
/*     */   
/*     */   public void setPageSize(Integer pageSize) {
/*  81 */     this.pageSize = pageSize;
/*     */   }
/*     */   
/*     */   public List<T> getRows() {
/*  85 */     return this.rows;
/*     */   }
/*     */   
/*     */   public void setRows(List<T> rows) {
/*  89 */     this.rows = rows;
/*     */   }
/*     */   
/*     */   public Integer getPage() {
/*  93 */     return this.page;
/*     */   }
/*     */   
/*     */   public void setPage(Integer page) {
/*  97 */     this.page = page;
/*     */   }
/*     */   
/*     */   public Integer getTotal() {
/* 101 */     return this.total;
/*     */   }
/*     */   
/*     */   public void setTotal(Integer total) {
/* 105 */     this.total = total;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-db/v1.0.176.bjsj.1/base-db-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/db/model/page/PageResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */