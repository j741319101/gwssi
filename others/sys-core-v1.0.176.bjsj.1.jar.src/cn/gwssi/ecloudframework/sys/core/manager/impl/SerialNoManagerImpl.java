/*     */ package cn.gwssi.ecloudframework.sys.core.manager.impl;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.query.QueryFilter;
/*     */ import cn.gwssi.ecloudframework.base.manager.impl.BaseManager;
/*     */ import cn.gwssi.ecloudframework.org.api.service.UserService;
/*     */ import cn.gwssi.ecloudframework.sys.core.dao.SerialNoDao;
/*     */ import cn.gwssi.ecloudframework.sys.core.manager.SerialNoManager;
/*     */ import cn.gwssi.ecloudframework.sys.core.model.SerialNo;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import java.io.Serializable;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Calendar;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import javax.annotation.Resource;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service("serialNoManager")
/*     */ public class SerialNoManagerImpl
/*     */   extends BaseManager<String, SerialNo>
/*     */   implements SerialNoManager {
/*     */   @Resource
/*     */   SerialNoDao serialNoDao;
/*     */   @Autowired
/*     */   UserService userService;
/*     */   
/*     */   public void create(SerialNo entity) {
/*  29 */     entity.setCurDate(getCurDate());
/*  30 */     super.create((Serializable)entity);
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
/*     */   public boolean isAliasExisted(String id, String alias) {
/*  42 */     return (this.serialNoDao.isAliasExisted(id, alias).intValue() > 0);
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
/*     */   public String getCurIdByAlias(String alias) {
/*  54 */     SerialNo SerialNo = this.serialNoDao.getByAlias(alias);
/*  55 */     Integer curValue = SerialNo.getCurValue();
/*  56 */     if (curValue == null) {
/*  57 */       curValue = SerialNo.getInitValue();
/*     */     }
/*  59 */     String rtn = getByRule(SerialNo.getRegulation(), SerialNo.getNoLength().intValue(), curValue.intValue());
/*  60 */     return rtn;
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
/*     */   private String getByRule(String rule, int length, int curValue) {
/*  72 */     Calendar now = Calendar.getInstance();
/*  73 */     int month = now.get(2) + 1;
/*  74 */     int day = now.get(5);
/*     */     
/*  76 */     StringBuilder serialNo = new StringBuilder();
/*  77 */     int fillLength = length - String.valueOf(curValue).length();
/*  78 */     for (int i = 0; i < fillLength; i++) {
/*  79 */       serialNo.append("0");
/*     */     }
/*  81 */     serialNo.append(curValue);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  89 */     String rtn = rule.replace("{yyyy}", String.valueOf(now.get(1))).replace("{MM}", (month < 10) ? ("0" + month) : ("" + month)).replace("{mm}", String.valueOf(month)).replace("{DD}", (day < 10) ? ("0" + day) : ("" + day)).replace("{dd}", String.valueOf(day)).replace("{NO}", serialNo.toString()).replace("{no}", String.valueOf(curValue));
/*     */ 
/*     */     
/*  92 */     return rtn;
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
/*     */   public synchronized String nextId(String alias) {
/* 104 */     SerialNo SerialNo = this.serialNoDao.getByAlias(alias);
/* 105 */     if (SerialNo == null) {
/* 106 */       throw new RuntimeException("流水号【" + alias + "】缺失！请联系系统管理员！");
/*     */     }
/*     */     
/* 109 */     Result result = genResult(SerialNo);
/*     */     
/* 111 */     int tryTimes = 0;
/* 112 */     while (result.getRtn() == 0) {
/* 113 */       tryTimes++;
/* 114 */       if (tryTimes > 100) {
/* 115 */         throw new RuntimeException("获取流水号失败！ " + SerialNo.getAlias());
/*     */       }
/*     */       
/*     */       try {
/* 119 */         Thread.sleep(50L);
/* 120 */       } catch (InterruptedException e) {
/* 121 */         e.printStackTrace();
/*     */       } 
/* 123 */       SerialNo.setCurValue(Integer.valueOf(result.getCurValue()));
/* 124 */       result = genResult(SerialNo);
/*     */     } 
/* 126 */     return result.getIdNo();
/*     */   }
/*     */ 
/*     */   
/*     */   public Result genResult(SerialNo serialNo) {
/* 131 */     String rule = serialNo.getRegulation();
/* 132 */     int step = serialNo.getStep().shortValue();
/* 133 */     int genEveryDay = serialNo.getGenType().shortValue();
/*     */ 
/*     */     
/* 136 */     Integer curValue = serialNo.getCurValue();
/*     */ 
/*     */     
/* 139 */     if (curValue.intValue() == 0) {
/* 140 */       curValue = serialNo.getInitValue();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 145 */     if (genEveryDay == 1) {
/* 146 */       String curDate = getCurDate();
/* 147 */       String oldDate = serialNo.getCurDate();
/* 148 */       if (!curDate.equals(oldDate)) {
/* 149 */         serialNo.setCurDate(curDate);
/* 150 */         curValue = serialNo.getInitValue();
/*     */       } else {
/* 152 */         curValue = Integer.valueOf(curValue.intValue() + step);
/*     */       } 
/*     */     } else {
/* 155 */       curValue = Integer.valueOf(curValue.intValue() + step);
/*     */     } 
/* 157 */     serialNo.setNewCurValue(curValue);
/* 158 */     int i = 0;
/* 159 */     i = this.serialNoDao.updByAlias(serialNo);
/* 160 */     Result result = new Result(0, "", curValue.intValue());
/* 161 */     if (i > 0) {
/* 162 */       String rtn = getByRule(rule, serialNo.getNoLength().intValue(), curValue.intValue());
/* 163 */       result.setIdNo(rtn);
/* 164 */       result.setRtn(1);
/*     */     } 
/* 166 */     return result;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String getCurDate() {
/* 175 */     Date date = new Date();
/* 176 */     return DateUtil.format(date, "yyyyMMdd");
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
/*     */   public List<SerialNo> getPreviewIden(String alias) {
/* 188 */     int genNum = 10;
/* 189 */     SerialNo SerialNo = this.serialNoDao.getByAlias(alias);
/* 190 */     String rule = SerialNo.getRegulation();
/* 191 */     int step = SerialNo.getStep().shortValue();
/* 192 */     Integer curValue = SerialNo.getCurValue();
/* 193 */     if (curValue == null) {
/* 194 */       curValue = SerialNo.getInitValue();
/*     */     }
/* 196 */     List<SerialNo> tempList = new ArrayList<>();
/* 197 */     for (int i = 0; i < genNum; i++) {
/* 198 */       SerialNo SerialNotemp = new SerialNo();
/* 199 */       if (i > 0) {
/* 200 */         curValue = Integer.valueOf(curValue.intValue() + step);
/*     */       }
/* 202 */       String rtn = getByRule(rule, SerialNo.getNoLength().intValue(), curValue.intValue());
/* 203 */       SerialNotemp.setId(curValue.toString());
/* 204 */       SerialNotemp.setCurIdenValue(rtn);
/* 205 */       tempList.add(SerialNotemp);
/*     */     } 
/* 207 */     return tempList;
/*     */   }
/*     */ 
/*     */   
/*     */   public class Result
/*     */   {
/* 213 */     private int rtn = 0;
/* 214 */     private String idNo = "";
/* 215 */     private int curValue = 0;
/*     */     
/*     */     public Result(int rtn, String idNo, int curValue) {
/* 218 */       this.rtn = rtn;
/* 219 */       this.idNo = idNo;
/* 220 */       setCurValue(curValue);
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRtn() {
/* 225 */       return this.rtn;
/*     */     }
/*     */     
/*     */     public void setRtn(int rtn) {
/* 229 */       this.rtn = rtn;
/*     */     }
/*     */     
/*     */     public String getIdNo() {
/* 233 */       return this.idNo;
/*     */     }
/*     */     
/*     */     public void setIdNo(String idNo) {
/* 237 */       this.idNo = idNo;
/*     */     }
/*     */     
/*     */     public int getCurValue() {
/* 241 */       return this.curValue;
/*     */     }
/*     */     
/*     */     public void setCurValue(int curValue) {
/* 245 */       this.curValue = curValue;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SerialNo> query(QueryFilter queryFilter) {
/* 253 */     List<SerialNo> lst = this.dao.query(queryFilter);
/* 254 */     return lst;
/*     */   }
/*     */ 
/*     */   
/*     */   public SerialNo get(String entityId) {
/* 259 */     SerialNo temp = (SerialNo)this.dao.get(entityId);
/* 260 */     return temp;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-core/v1.0.176.bjsj.1/sys-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/core/manager/impl/SerialNoManagerImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */