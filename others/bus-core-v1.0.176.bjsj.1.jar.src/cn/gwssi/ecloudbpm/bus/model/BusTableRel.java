/*     */ package cn.gwssi.ecloudbpm.bus.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessObject;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessTable;
/*     */ import cn.hutool.core.collection.CollectionUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collections;
/*     */ import java.util.List;
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
/*     */ public class BusTableRel
/*     */   implements Serializable, IBusTableRel
/*     */ {
/*     */   private List<BusTableRel> children;
/*     */   private String tableKey;
/*     */   private String tableComment;
/*     */   private String type;
/*     */   private List<BusTableRelFk> fks;
/*     */   private BusinessTable table;
/*     */   private BusTableRel parent;
/*     */   private BusinessObject busObj;
/*     */   private List<BusDataFlowRel> dataFlowLinks;
/*     */   
/*     */   public List<BusTableRel> getChildren() {
/*  61 */     if (this.children == null) {
/*  62 */       return Collections.emptyList();
/*     */     }
/*  64 */     return this.children;
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
/*     */   public List<IBusTableRel> getChildren(String type) {
/*  76 */     List<IBusTableRel> list = new ArrayList<>();
/*  77 */     if (CollectionUtil.isNotEmpty(this.children)) {
/*  78 */       for (BusTableRel rel : this.children) {
/*  79 */         if (type.equals(rel.getType())) {
/*  80 */           list.add(rel);
/*     */         }
/*     */       } 
/*     */     }
/*  84 */     return list;
/*     */   }
/*     */   
/*     */   public void setChildren(List<BusTableRel> children) {
/*  88 */     this.children = children;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableKey() {
/*  93 */     return this.tableKey;
/*     */   }
/*     */   
/*     */   public void setTableKey(String tableKey) {
/*  97 */     this.tableKey = tableKey;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getTableComment() {
/* 102 */     return this.tableComment;
/*     */   }
/*     */   
/*     */   public void setTableComment(String tableComment) {
/* 106 */     this.tableComment = tableComment;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/* 111 */     return this.type;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/* 115 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BusTableRelFk> getFks() {
/* 120 */     return this.fks;
/*     */   }
/*     */   
/*     */   public void setFks(List<BusTableRelFk> fks) {
/* 124 */     this.fks = fks;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusTableRel find(String tableKey) {
/* 129 */     if (this.tableKey.equals(tableKey)) {
/* 130 */       return this;
/*     */     }
/* 132 */     if (this.children != null) {
/* 133 */       for (BusTableRel rel : this.children) {
/* 134 */         BusTableRel r = rel.find(tableKey);
/* 135 */         if (r != null) {
/* 136 */           return r;
/*     */         }
/*     */       } 
/*     */     }
/* 140 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BusTableRel> list() {
/* 145 */     List<BusTableRel> rels = new ArrayList<>();
/* 146 */     rels.add(this);
/* 147 */     if (this.children != null) {
/* 148 */       for (BusTableRel rel : this.children) {
/* 149 */         rels.addAll(rel.list());
/*     */       }
/*     */     }
/* 152 */     return rels;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessTable getTable() {
/* 157 */     return this.table;
/*     */   }
/*     */   
/*     */   public void setTable(BusinessTable table) {
/* 161 */     this.table = table;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusTableRel getParent() {
/* 166 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(BusTableRel parent) {
/* 170 */     this.parent = parent;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessObject getBusObj() {
/* 175 */     return this.busObj;
/*     */   }
/*     */   
/*     */   public void setBusObj(BusinessObject busObj) {
/* 179 */     this.busObj = busObj;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<BusDataFlowRel> getDataFlowLinks() {
/* 184 */     return this.dataFlowLinks;
/*     */   }
/*     */   
/*     */   public BusTableRel setDataFlowLinks(List<BusDataFlowRel> dataFlowLinks) {
/* 188 */     this.dataFlowLinks = dataFlowLinks;
/* 189 */     return this;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusTableRel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */