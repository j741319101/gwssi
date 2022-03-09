/*     */ package cn.gwssi.ecloudbpm.form.constants;
/*     */ 
/*     */ import cn.gwssi.ecloudbpm.form.util.Util;
/*     */ import java.util.Set;
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
/*     */ public interface ViewConstants
/*     */ {
/*     */   public static interface SearchParamsType
/*     */   {
/*     */     public static final int SQL_APPEND = 1;
/*     */     public static final int FREEMARKER_PARAMS = 2;
/*     */   }
/*     */   
/*     */   public static interface FieldsOperTypeCode
/*     */   {
/*     */     public static final int NO_NOTSHOW = 0;
/*     */     public static final int NO_SHOW = 1;
/*     */     public static final int YES_SHOW = 2;
/*     */     public static final int YES_NOTSHOW = 3;
/*     */     public static final int YES_SHOW_DISABLED = 4;
/*     */     public static final int NO_INCREMENT = 5;
/*  70 */     public static final Set<Integer> OPERABLE = Util.set((Object[])new Integer[] { Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4) });
/*     */     
/*  72 */     public static final Set<Integer> NOTOPERABLE = Util.set((Object[])new Integer[] { Integer.valueOf(2), Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface SqlIdTypeCode
/*     */   {
/*     */     public static final int GUID = 1;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int IDENTITY = 2;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int TIMEGEN = 3;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface MethodParamsType
/*     */   {
/*     */     public static final String NORMOL = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String AUTO = "2";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String SYSTEM_DATA = "3";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String PARAM_QUERY = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String PARAM_UPDATE = "2";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ViewExcuConstants
/*     */   {
/*     */     public static final String ADD = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String UPDATA = "2";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DEL = "3";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String BUTTON_METHOD = "4";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String EXPORT = "5";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String METHOD = "6";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String SEARCH = "7";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String SORT = "8";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String IMPORT = "9";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ViewTuoMinConstants
/*     */   {
/*     */     public static final int TuoMin_PHONE = 1;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int TuoMin_TEL_PHONE = 2;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int TuoMin_IDCARD = 3;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int TuoMin_NAME = 4;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int TuoMin_BANKCARD = 5;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int TuoMin_ADDRESS = 6;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int TuoMin_QQ = 7;
/*     */ 
/*     */ 
/*     */     
/*     */     public static final int TuoMin_WEIXIN = 8;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ViewSetDataConstants
/*     */   {
/*     */     public static final String FRONT = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String BACKGROUND = "2";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ViewMethodTypeConstants
/*     */   {
/*     */     public static final String METHOD_ADD = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String METHOD_DEL = "2";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String METHOD_UPDATE = "3";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String METHOD_SEARCH = "4";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String RETURN_TYPE_JSON = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String RETURN_TYPE_ARRAY = "2";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ViewDataTypeConstants
/*     */   {
/*     */     public static final String BIND_FIELD = "0";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_NOMAL = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_DATE = "2";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_USER_ID = "3";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_USER_NAME = "4";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_SYS_LSH = "5";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_RANDOM_NUM = "6";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_USER_DEPT_ID = "7";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_USER_DEPT_NAME = "8";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_USER_POS_ID = "9";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_USER_POS_NAME = "10";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_USER_CREATERID = "11";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_CUR_MAIN_TABLE_ID = "12";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String GROOVY_SCRIPT = "13";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String ENV = "15";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface FieldTypeConstants
/*     */   {
/*     */     public static final String FIELD_TYPE_COMMON = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String FIELD_TYPE_EXPAND = "2";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String FIELD_TYPE_VIR = "3";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ViewButtonTypeConstants
/*     */   {
/*     */     public static final String INLINE = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String GROBLE = "2";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String HREF = "3";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String METHOD = "4";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_TYPE_NOMAL = "1";
/*     */ 
/*     */ 
/*     */     
/*     */     public static final String DATA_TYPE_AUTO = "2";
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface SqlTypeConstants
/*     */   {
/*     */     public static final String TYPE_SQL = "1";
/*     */ 
/*     */     
/*     */     public static final String TYPE_TABLE = "2";
/*     */ 
/*     */     
/*     */     public static final String TYPE__MUTI_TABLE = "3";
/*     */ 
/*     */     
/*     */     public static final String TYPE_SQL_TABLE = "4";
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface SqlTableTypeConstants
/*     */   {
/*     */     public static final String TABLE_MAIN = "1";
/*     */ 
/*     */     
/*     */     public static final String TABLE_SUB = "2";
/*     */ 
/*     */     
/*     */     public static final boolean IS_SUB_TRUE = true;
/*     */ 
/*     */     
/*     */     public static final boolean IS_SUB_FALSE = false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static interface ConditionShowType
/*     */   {
/* 376 */     public static final Integer SHOW = Integer.valueOf(1);
/*     */     
/* 378 */     public static final Integer HIDE = Integer.valueOf(2);
/*     */     
/* 380 */     public static final Integer DISABLED = Integer.valueOf(3);
/*     */     
/* 382 */     public static final Integer REMOVE = Integer.valueOf(4);
/*     */   }
/*     */   
/*     */   public static interface GroupByType {
/*     */     public static final int NORMAL = 1;
/*     */     public static final int TIME = 2;
/*     */     public static final int CUSTOMER = 3;
/*     */   }
/*     */   
/*     */   public static interface DataFromType {
/*     */     public static final int DATAFROM_INTERFACE = 1;
/*     */     public static final int DATAFROM_BACKUP = 2;
/*     */   }
/*     */   
/*     */   public static interface ParamNameConstants {
/*     */     public static final String CUR_USERID = "\\$\\{currentUserId\\}";
/*     */     public static final String CUR_CREATERID = "\\$\\{curCreaterId\\}";
/*     */     public static final String CUR_USERNAME = "\\$\\{currentUserName\\}";
/*     */     public static final String CUR_ORG_ID = "\\$\\{currentOrgId\\}";
/*     */     public static final String CUR_ORG_NAME = "\\$\\{currentOrgName\\}";
/*     */     public static final String CUR_POST_ID = "\\$\\{curPosId\\}";
/*     */     public static final String CUR_POST_NAME = "\\$\\{curPostName\\}";
/*     */     public static final String CUR_DATE = "\\$\\{curDate\\}";
/*     */     public static final String CUR_TIME = "\\$\\{curTime\\}";
/*     */     public static final String CUR_DATE_TIME = "\\$\\{curDateTime\\}";
/*     */     public static final String MAIN_TABLE_ID = "\\$\\{mainTableId\\}";
/*     */     public static final String ENV = "\\$\\{env\\}";
/*     */     public static final String MAIN_TABLE_ID_NOMARL = "${mainTableId}";
/*     */   }
/*     */   
/*     */   public static interface BtnClickTypeConstants {
/*     */     public static final int BTNCLICKTYPE_DIRECT = 0;
/*     */     public static final int BTNCLICKTYPE_EVENT = 1;
/*     */     public static final int BTNCLICKTYPE_TAB = 2;
/*     */     public static final int BTNCLICKTYPE_DIALOG = 3;
/*     */     public static final int BTNCLICKTYPE_DIALOG_TOP = 4;
/*     */     public static final int BTNCLICKTYPE_NEW_PAGE = 5;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/form-core/v1.0.176.bjsj.1/form-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudbpm/form/constants/ViewConstants.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */