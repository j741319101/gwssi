/*     */ package cn.gwssi.ecloudframework.sys.api.model;
/*     */ 
/*     */ import cn.gwssi.ecloudframework.base.api.exception.BusinessException;
/*     */ import cn.gwssi.ecloudframework.base.core.util.AppUtil;
/*     */ import cn.gwssi.ecloudframework.base.core.util.StringUtil;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IGroup;
/*     */ import cn.gwssi.ecloudframework.org.api.model.IUser;
/*     */ import org.apache.commons.lang3.StringUtils;
/*     */ import org.springframework.core.env.Environment;
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
/*     */ public class DefaultIdentity
/*     */   implements SysIdentity<DefaultIdentity>
/*     */ {
/*     */   private static final long serialVersionUID = -5039484524490929855L;
/*     */   private String id;
/*     */   private String name;
/*     */   private String type;
/*  27 */   private Integer sn = Integer.valueOf(-1);
/*  28 */   private static Integer order = Integer.valueOf(0);
/*     */   private String orgId;
/*  30 */   private String clazz = "DefaultIdentity";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private String compareOrgId;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DefaultIdentity(String id, String name, String type, String orgId) {
/*  45 */     this.id = id;
/*  46 */     this.name = name;
/*  47 */     this.type = getSysIdentityType(type);
/*  48 */     this.orgId = orgId;
/*     */   }
/*     */   
/*     */   public DefaultIdentity(String id, String name, String type, String orgId, Integer sn) {
/*  52 */     this.id = id;
/*  53 */     this.name = name;
/*  54 */     this.type = getSysIdentityType(type);
/*  55 */     this.sn = sn;
/*  56 */     this.orgId = orgId;
/*     */   }
/*     */   
/*     */   public DefaultIdentity(IUser user) {
/*  60 */     this.id = user.getUserId();
/*  61 */     this.name = user.getFullname();
/*  62 */     this.type = "user";
/*  63 */     this.sn = Integer.valueOf((user.getSn() == null) ? -1 : user.getSn().intValue());
/*  64 */     this.orgId = user.getOrgId();
/*     */   }
/*     */   
/*     */   public DefaultIdentity(IGroup group) {
/*  68 */     this.id = group.getGroupId();
/*  69 */     this.name = group.getGroupName();
/*  70 */     this.type = group.getGroupType();
/*  71 */     this.sn = Integer.valueOf((group.getSn() == null) ? -1 : group.getSn().intValue());
/*  72 */     this.orgId = group.getGroupId();
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  76 */     this.id = id;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getId() {
/*  81 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setName(String name) {
/*  85 */     this.name = name;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/*  90 */     return this.name;
/*     */   }
/*     */   
/*     */   public void setType(String type) {
/*  94 */     if (StringUtils.indexOf("user,role,group,org,post", type) == -1) {
/*  95 */       throw new BusinessException("候选人类型错误，检查候选人类型");
/*     */     }
/*  97 */     this.type = type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getType() {
/* 102 */     return this.type;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getOrgId() {
/* 107 */     return this.orgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setOrgId(String orgId) {
/* 112 */     this.orgId = orgId;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 117 */     return this.id.hashCode() + this.type.hashCode() + this.orgId.hashCode();
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 122 */     return "DefaultIdentity{id='" + this.id + '\'' + ", name='" + this.name + '\'' + ", type='" + this.type + '\'' + ", sn=" + this.sn + ", orgId=" + this.orgId + '}';
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean equals(Object obj) {
/* 133 */     if (!(obj instanceof SysIdentity)) {
/* 134 */       return false;
/*     */     }
/*     */     
/* 137 */     if (StringUtil.isEmpty(this.id) || StringUtil.isEmpty(this.name)) {
/* 138 */       return false;
/*     */     }
/*     */     
/* 141 */     SysIdentity identity = (SysIdentity)obj;
/*     */     
/* 143 */     if (this.type.equals(identity.getType()) && this.id
/* 144 */       .equals(identity.getId()) && this.orgId
/* 145 */       .equals(identity.getOrgId())) {
/* 146 */       return true;
/*     */     }
/*     */     
/* 149 */     return false;
/*     */   }
/*     */   
/*     */   public String getCompareOrgId() {
/* 153 */     return this.compareOrgId;
/*     */   }
/*     */   
/*     */   public void setCompareOrgId(String compareOrgId) {
/* 157 */     this.compareOrgId = compareOrgId;
/*     */   }
/*     */   
/*     */   public Integer getSn() {
/* 161 */     return this.sn;
/*     */   }
/*     */   
/*     */   public void setSn(Integer sn) {
/* 165 */     this.sn = sn;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(DefaultIdentity identity) {
/* 170 */     if (DefaultIdentity.order.intValue() == 0) {
/* 171 */       Environment environment = AppUtil.getImplInstanceArray(Environment.class).get(0);
/* 172 */       String order = environment.getProperty("ecloud.identity.order");
/* 173 */       if (StringUtils.equals(order, "desc")) {
/* 174 */         this; DefaultIdentity.order = Integer.valueOf(-1);
/*     */       } else {
/* 176 */         this; DefaultIdentity.order = Integer.valueOf(1);
/*     */       } 
/*     */     } 
/* 179 */     if (this.sn == null || identity.sn == null) {
/* 180 */       return 0;
/*     */     }
/* 182 */     if (getSn() == identity.getSn()) {
/* 183 */       return 0;
/*     */     }
/*     */     
/* 186 */     if (getSn().intValue() > identity.getSn().intValue()) {
/* 187 */       return DefaultIdentity.order.intValue();
/*     */     }
/* 189 */     return -DefaultIdentity.order.intValue();
/*     */   }
/*     */   
/*     */   public String getClazz() {
/* 193 */     return this.clazz;
/*     */   }
/*     */   
/*     */   public void setClazz(String clazz) {
/* 197 */     this.clazz = clazz;
/*     */   }
/*     */   
/*     */   private String getSysIdentityType(String type) {
/* 201 */     switch (type) {
/*     */       case "user":
/* 203 */         return "user";
/*     */       case "role":
/* 205 */         return "role";
/*     */       case "post":
/* 207 */         return "post";
/*     */       case "org":
/* 209 */         return "org";
/*     */     } 
/* 211 */     return "user";
/*     */   }
/*     */   
/*     */   public DefaultIdentity() {}
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/model/DefaultIdentity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */