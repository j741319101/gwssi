/*     */ package cn.gwssi.ecloudbpm.bus.model;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.bus.api.constant.BusTableRelType;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusTableRel;
/*     */ import cn.gwssi.ecloudbpm.bus.api.model.IBusinessData;
/*     */ import com.alibaba.fastjson.JSONObject;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
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
/*     */ public class BusinessData
/*     */   implements IBusinessData
/*     */ {
/*     */   private BusTableRel busTableRel;
/*  35 */   private Map<String, Object> data = new HashMap<>();
/*     */ 
/*     */ 
/*     */   
/*  39 */   private Map<String, List<BusinessData>> children = new HashMap<>();
/*     */ 
/*     */   
/*     */   private BusinessData parent;
/*     */ 
/*     */ 
/*     */   
/*     */   public BusTableRel getBusTableRel() {
/*  47 */     return this.busTableRel;
/*     */   }
/*     */   
/*     */   public void setBusTableRel(BusTableRel busTableRel) {
/*  51 */     this.busTableRel = busTableRel;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, Object> getData() {
/*  56 */     return this.data;
/*     */   }
/*     */   
/*     */   public void setData(Map<String, Object> data) {
/*  60 */     this.data = data;
/*     */   }
/*     */   
/*     */   public Map<String, List<BusinessData>> getChildren() {
/*  64 */     return this.children;
/*     */   }
/*     */   
/*     */   public void setChildren(Map<String, List<BusinessData>> children) {
/*  68 */     this.children = children;
/*     */   }
/*     */ 
/*     */   
/*     */   public BusinessData getParent() {
/*  73 */     return this.parent;
/*     */   }
/*     */   
/*     */   public void setParent(BusinessData parent) {
/*  77 */     this.parent = parent;
/*     */   }
/*     */   
/*     */   public void setPk(Object id) {
/*  81 */     this.data.put(this.busTableRel.getTable().getPkKey(), id);
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getPk() {
/*  86 */     return this.data.get(this.busTableRel.getTable().getPkKey());
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
/*     */   public void put(String key, Object value) {
/* 100 */     this.data.put(key, value);
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
/*     */   public Object get(String key) {
/* 114 */     return this.data.get(key);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getString(String key) {
/* 119 */     Object obj = this.data.get(key);
/* 120 */     if (obj == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     return obj.toString();
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
/*     */   public Map<String, Object> getDbData() {
/* 138 */     Map<String, Object> dbData = new HashMap<>();
/* 139 */     for (BusinessColumn column : this.busTableRel.getTable().getColumns()) {
/* 140 */       if (column.isPrimary() || this.busTableRel.getBusObj().haveColumnDbEditRights(this.busTableRel.getTableKey(), column.getKey())) {
/* 141 */         Object val = this.data.get(column.getKey());
/* 142 */         dbData.put(column.getName(), val);
/*     */       } 
/*     */     } 
/* 145 */     return dbData;
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
/*     */   public void setDbData(Map<String, Object> dbData) {
/* 158 */     for (BusinessColumn column : this.busTableRel.getTable().getColumns()) {
/* 159 */       if (this.busTableRel.getBusObj().haveColumnDbReadRights(this.busTableRel.getTableKey(), column.getKey())) {
/* 160 */         this.data.put(column.getKey(), dbData.get(column.getName()));
/*     */       }
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
/*     */   public void addChildren(BusinessData businessData) {
/* 173 */     String tableKey = businessData.getBusTableRel().getTable().getKey();
/* 174 */     List<BusinessData> list = this.children.computeIfAbsent(tableKey, k -> new ArrayList());
/* 175 */     businessData.setParent(this);
/* 176 */     list.add(businessData);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, List<IBusinessData>> getChilds() {
/* 181 */     Map<String, List<IBusinessData>> map = new HashMap<>();
/* 182 */     for (Map.Entry<String, List<BusinessData>> entry : this.children.entrySet()) {
/* 183 */       List<IBusinessData> list = new ArrayList<>();
/* 184 */       list.addAll((Collection<? extends IBusinessData>)entry.getValue());
/* 185 */       map.put(entry.getKey(), list);
/*     */     } 
/* 187 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<IBusinessData> getChild(String subKey) {
/* 192 */     Map<String, List<IBusinessData>> subDatas = getChilds();
/* 193 */     if (subDatas.containsKey(subKey)) {
/* 194 */       return subDatas.get(subKey);
/*     */     }
/* 196 */     return Collections.emptyList();
/*     */   }
/*     */   
/*     */   public List<IBusinessData> getChildren(String subKey) {
/* 200 */     return getChild(subKey);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public JSONObject fullBusDataInitData(JSONObject initData) {
/* 209 */     if (initData == null) {
/* 210 */       initData = new JSONObject();
/*     */     }
/* 212 */     JSONObject initTables = new JSONObject();
/* 213 */     for (IBusTableRel rel : getBusTableRel().list()) {
/* 214 */       initTables.put(rel.getTableKey(), getInitData(rel));
/*     */     }
/* 216 */     initData.put(getBusTableRel().getBusObj().getKey(), initTables);
/*     */     
/* 218 */     return initData;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private JSONObject getInitData(IBusTableRel busTableRel) {
/* 227 */     JSONObject table = new JSONObject();
/* 228 */     table.putAll(busTableRel.getTable().initData());
/* 229 */     for (IBusTableRel rel : busTableRel.getChildren()) {
/* 230 */       if (BusTableRelType.ONE_TO_ONE.equalsWithKey(rel.getType()))
/*     */       {
/* 232 */         table.put(rel.getTableKey(), getInitData(rel));
/*     */       }
/*     */     } 
/* 235 */     return table;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/bus-core/v1.0.176.bjsj.1/bus-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/bus/model/BusinessData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */