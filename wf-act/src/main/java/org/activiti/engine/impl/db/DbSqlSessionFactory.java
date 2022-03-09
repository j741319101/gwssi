/*     */ package org.activiti.engine.impl.db;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import org.activiti.engine.impl.cfg.IdGenerator;
/*     */ import org.activiti.engine.impl.interceptor.Session;
/*     */ import org.activiti.engine.impl.interceptor.SessionFactory;
/*     */ import org.activiti.engine.impl.persistence.entity.EventLogEntryEntity;
/*     */ import org.apache.ibatis.session.SqlSessionFactory;
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
/*     */ public class DbSqlSessionFactory
/*     */   implements SessionFactory
/*     */ {
/*  32 */   protected static final Map<String, Map<String, String>> databaseSpecificStatements = new HashMap<>();
/*     */   
/*  34 */   public static final Map<String, String> databaseSpecificLimitBeforeStatements = new HashMap<>();
/*  35 */   public static final Map<String, String> databaseSpecificLimitAfterStatements = new HashMap<>();
/*  36 */   public static final Map<String, String> databaseSpecificLimitBetweenStatements = new HashMap<>();
/*  37 */   public static final Map<String, String> databaseSpecificOrderByStatements = new HashMap<>();
/*  38 */   public static final Map<String, String> databaseOuterJoinLimitBetweenStatements = new HashMap<>();
/*  39 */   public static final Map<String, String> databaseSpecificLimitBeforeNativeQueryStatements = new HashMap<>();
/*     */   protected static Map<Class<? extends PersistentObject>, Boolean> bulkInsertableMap;
/*     */   
/*     */   static {
/*  43 */     String defaultOrderBy = " order by ${orderByColumns} ";
/*     */ 
/*     */     
/*  46 */     databaseSpecificLimitBeforeStatements.put("h2", "");
/*  47 */     databaseSpecificLimitAfterStatements.put("h2", "LIMIT #{maxResults} OFFSET #{firstResult}");
/*  48 */     databaseSpecificLimitBetweenStatements.put("h2", "");
/*  49 */     databaseOuterJoinLimitBetweenStatements.put("h2", "");
/*  50 */     databaseSpecificOrderByStatements.put("h2", defaultOrderBy);
/*     */ 
/*     */     
/*  53 */     databaseSpecificLimitBeforeStatements.put("hsql", "");
/*  54 */     databaseSpecificLimitAfterStatements.put("hsql", "LIMIT #{maxResults} OFFSET #{firstResult}");
/*  55 */     databaseSpecificLimitBetweenStatements.put("hsql", "");
/*  56 */     databaseOuterJoinLimitBetweenStatements.put("hsql", "");
/*  57 */     databaseSpecificOrderByStatements.put("hsql", defaultOrderBy);
/*     */ 
/*     */     
/*  60 */     databaseSpecificLimitBeforeStatements.put("drds", "");
/*  61 */     databaseSpecificLimitAfterStatements.put("drds", "LIMIT #{maxResults} OFFSET #{firstResult}");
/*  62 */     databaseSpecificLimitBetweenStatements.put("drds", "");
/*  63 */     databaseOuterJoinLimitBetweenStatements.put("drds", "");
/*  64 */     databaseSpecificOrderByStatements.put("drds", defaultOrderBy);
/*  65 */     addDatabaseSpecificStatement("drds", "selectProcessInstanceByQueryCriteria", "selectProcessInstanceByQueryCriteria_drds");
/*     */ 
/*     */     
/*  68 */     databaseSpecificLimitBeforeStatements.put("kingbase", "");
/*  69 */     databaseSpecificLimitAfterStatements.put("kingbase", "LIMIT #{maxResults} OFFSET #{firstResult}");
/*  70 */     databaseSpecificLimitBetweenStatements.put("kingbase", "");
/*  71 */     databaseOuterJoinLimitBetweenStatements.put("kingbase", "");
/*  72 */     databaseSpecificOrderByStatements.put("kingbase", defaultOrderBy);
/*     */ 
/*     */ 
/*     */     
/*  76 */     databaseSpecificLimitBeforeStatements.put("mysql", "");
/*  77 */     databaseSpecificLimitAfterStatements.put("mysql", "LIMIT #{maxResults} OFFSET #{firstResult}");
/*  78 */     databaseSpecificLimitBetweenStatements.put("mysql", "");
/*  79 */     databaseOuterJoinLimitBetweenStatements.put("mysql", "");
/*  80 */     databaseSpecificOrderByStatements.put("mysql", defaultOrderBy);
/*  81 */     addDatabaseSpecificStatement("mysql", "selectProcessDefinitionsByQueryCriteria", "selectProcessDefinitionsByQueryCriteria_mysql");
/*  82 */     addDatabaseSpecificStatement("mysql", "selectProcessDefinitionCountByQueryCriteria", "selectProcessDefinitionCountByQueryCriteria_mysql");
/*  83 */     addDatabaseSpecificStatement("mysql", "selectDeploymentsByQueryCriteria", "selectDeploymentsByQueryCriteria_mysql");
/*  84 */     addDatabaseSpecificStatement("mysql", "selectDeploymentCountByQueryCriteria", "selectDeploymentCountByQueryCriteria_mysql");
/*  85 */     addDatabaseSpecificStatement("mysql", "selectModelCountByQueryCriteria", "selectModelCountByQueryCriteria_mysql");
/*  86 */     addDatabaseSpecificStatement("mysql", "updateExecutionTenantIdForDeployment", "updateExecutionTenantIdForDeployment_mysql");
/*  87 */     addDatabaseSpecificStatement("mysql", "updateTaskTenantIdForDeployment", "updateTaskTenantIdForDeployment_mysql");
/*  88 */     addDatabaseSpecificStatement("mysql", "updateJobTenantIdForDeployment", "updateJobTenantIdForDeployment_mysql");
/*     */ 
/*     */     
/*  91 */     databaseSpecificLimitBeforeStatements.put("postgres", "");
/*  92 */     databaseSpecificLimitAfterStatements.put("postgres", "LIMIT #{maxResults} OFFSET #{firstResult}");
/*  93 */     databaseSpecificLimitBetweenStatements.put("postgres", "");
/*  94 */     databaseOuterJoinLimitBetweenStatements.put("postgres", "");
/*  95 */     databaseSpecificOrderByStatements.put("postgres", defaultOrderBy);
/*  96 */     addDatabaseSpecificStatement("postgres", "insertByteArray", "insertByteArray_postgres");
/*  97 */     addDatabaseSpecificStatement("postgres", "bulkInsertByteArray", "bulkInsertByteArray_postgres");
/*  98 */     addDatabaseSpecificStatement("postgres", "updateByteArray", "updateByteArray_postgres");
/*  99 */     addDatabaseSpecificStatement("postgres", "selectByteArray", "selectByteArray_postgres");
/* 100 */     addDatabaseSpecificStatement("postgres", "selectResourceByDeploymentIdAndResourceName", "selectResourceByDeploymentIdAndResourceName_postgres");
/* 101 */     addDatabaseSpecificStatement("postgres", "selectResourcesByDeploymentId", "selectResourcesByDeploymentId_postgres");
/* 102 */     addDatabaseSpecificStatement("postgres", "insertIdentityInfo", "insertIdentityInfo_postgres");
/* 103 */     addDatabaseSpecificStatement("postgres", "bulkInsertIdentityInfo", "bulkInsertIdentityInfo_postgres");
/* 104 */     addDatabaseSpecificStatement("postgres", "updateIdentityInfo", "updateIdentityInfo_postgres");
/* 105 */     addDatabaseSpecificStatement("postgres", "selectIdentityInfoById", "selectIdentityInfoById_postgres");
/* 106 */     addDatabaseSpecificStatement("postgres", "selectIdentityInfoByUserIdAndKey", "selectIdentityInfoByUserIdAndKey_postgres");
/* 107 */     addDatabaseSpecificStatement("postgres", "selectIdentityInfoByUserId", "selectIdentityInfoByUserId_postgres");
/* 108 */     addDatabaseSpecificStatement("postgres", "selectIdentityInfoDetails", "selectIdentityInfoDetails_postgres");
/* 109 */     addDatabaseSpecificStatement("postgres", "insertComment", "insertComment_postgres");
/* 110 */     addDatabaseSpecificStatement("postgres", "bulkInsertComment", "bulkInsertComment_postgres");
/* 111 */     addDatabaseSpecificStatement("postgres", "selectComment", "selectComment_postgres");
/* 112 */     addDatabaseSpecificStatement("postgres", "selectCommentsByTaskId", "selectCommentsByTaskId_postgres");
/* 113 */     addDatabaseSpecificStatement("postgres", "selectCommentsByProcessInstanceId", "selectCommentsByProcessInstanceId_postgres");
/* 114 */     addDatabaseSpecificStatement("postgres", "selectCommentsByProcessInstanceIdAndType", "selectCommentsByProcessInstanceIdAndType_postgres");
/* 115 */     addDatabaseSpecificStatement("postgres", "selectCommentsByType", "selectCommentsByType_postgres");
/* 116 */     addDatabaseSpecificStatement("postgres", "selectCommentsByTaskIdAndType", "selectCommentsByTaskIdAndType_postgres");
/* 117 */     addDatabaseSpecificStatement("postgres", "selectEventsByTaskId", "selectEventsByTaskId_postgres");
/* 118 */     addDatabaseSpecificStatement("postgres", "insertEventLogEntry", "insertEventLogEntry_postgres");
/* 119 */     addDatabaseSpecificStatement("postgres", "bulkInsertEventLogEntry", "bulkInsertEventLogEntry_postgres");
/* 120 */     addDatabaseSpecificStatement("postgres", "selectAllEventLogEntries", "selectAllEventLogEntries_postgres");
/* 121 */     addDatabaseSpecificStatement("postgres", "selectEventLogEntries", "selectEventLogEntries_postgres");
/* 122 */     addDatabaseSpecificStatement("postgres", "selectEventLogEntriesByProcessInstanceId", "selectEventLogEntriesByProcessInstanceId_postgres");
/*     */ 
/*     */     
/* 125 */     databaseSpecificLimitBeforeStatements.put("oracle", "select * from ( select a.*, ROWNUM rnum from (");
/* 126 */     databaseSpecificLimitAfterStatements.put("oracle", "  ) a where ROWNUM < #{lastRow}) where rnum  >= #{firstRow}");
/* 127 */     databaseSpecificLimitBetweenStatements.put("oracle", "");
/* 128 */     databaseOuterJoinLimitBetweenStatements.put("oracle", "");
/* 129 */     databaseSpecificOrderByStatements.put("oracle", defaultOrderBy);
/* 130 */     addDatabaseSpecificStatement("oracle", "selectExclusiveJobsToExecute", "selectExclusiveJobsToExecute_integerBoolean");
/* 131 */     addDatabaseSpecificStatement("oracle", "selectUnlockedTimersByDuedate", "selectUnlockedTimersByDuedate_oracle");
/* 132 */     addDatabaseSpecificStatement("oracle", "insertEventLogEntry", "insertEventLogEntry_oracle");
/* 133 */     addDatabaseSpecificStatement("oracle", "bulkInsertVariableInstance", "bulkInsertVariableInstance_oracle");
/* 134 */     addDatabaseSpecificStatement("oracle", "bulkInsertUser", "bulkInsertUser_oracle");
/* 135 */     addDatabaseSpecificStatement("oracle", "bulkInsertTask", "bulkInsertTask_oracle");
/* 136 */     addDatabaseSpecificStatement("oracle", "bulkInsertResource", "bulkInsertResource_oracle");
/* 137 */     addDatabaseSpecificStatement("oracle", "bulkInsertProperty", "bulkInsertProperty_oracle");
/* 138 */     addDatabaseSpecificStatement("oracle", "bulkInsertProcessDefinition", "bulkInsertProcessDefinition_oracle");
/* 139 */     addDatabaseSpecificStatement("oracle", "bulkInsertModel", "bulkInsertModel_oracle");
/* 140 */     addDatabaseSpecificStatement("oracle", "bulkInsertMembership", "bulkInsertMembership_oracle");
/* 141 */     addDatabaseSpecificStatement("oracle", "bulkInsertTimer", "bulkInsertTimer_oracle");
/* 142 */     addDatabaseSpecificStatement("oracle", "bulkInsertMessage", "bulkInsertMessage_oracle");
/* 143 */     addDatabaseSpecificStatement("oracle", "bulkInsertIdentityInfo", "bulkInsertIdentityInfo_oracle");
/* 144 */     addDatabaseSpecificStatement("oracle", "bulkInsertIdentityLink", "bulkInsertIdentityLink_oracle");
/* 145 */     addDatabaseSpecificStatement("oracle", "bulkInsertMembership", "bulkInsertMembership_oracle");
/* 146 */     addDatabaseSpecificStatement("oracle", "bulkInsertTimer", "bulkInsertTimer_oracle");
/* 147 */     addDatabaseSpecificStatement("oracle", "bulkInsertMessage", "bulkInsertMessage_oracle");
/* 148 */     addDatabaseSpecificStatement("oracle", "bulkInsertHistoricVariableInstance", "bulkInsertHistoricVariableInstance_oracle");
/* 149 */     addDatabaseSpecificStatement("oracle", "bulkInsertHistoricTaskInstance", "bulkInsertHistoricTaskInstance_oracle");
/* 150 */     addDatabaseSpecificStatement("oracle", "bulkInsertHistoricProcessInstance", "bulkInsertHistoricProcessInstance_oracle");
/* 151 */     addDatabaseSpecificStatement("oracle", "bulkInsertHistoricIdentityLink", "bulkInsertHistoricIdentityLink_oracle");
/* 152 */     addDatabaseSpecificStatement("oracle", "bulkInsertHistoricDetailVariableInstanceUpdate", "bulkInsertHistoricDetailVariableInstanceUpdate_oracle");
/* 153 */     addDatabaseSpecificStatement("oracle", "bulkInsertHistoricFormProperty", "bulkInsertHistoricFormProperty_oracle");
/* 154 */     addDatabaseSpecificStatement("oracle", "bulkInsertHistoricActivityInstance", "bulkInsertHistoricActivityInstance_oracle");
/* 155 */     addDatabaseSpecificStatement("oracle", "bulkInsertGroup", "bulkInsertGroup_oracle");
/* 156 */     addDatabaseSpecificStatement("oracle", "bulkInsertExecution", "bulkInsertExecution_oracle");
/* 157 */     addDatabaseSpecificStatement("oracle", "bulkInsertMessageEventSubscription", "bulkInsertMessageEventSubscription_oracle");
/* 158 */     addDatabaseSpecificStatement("oracle", "bulkInsertSignalEventSubscription", "bulkInsertSignalEventSubscription_oracle");
/* 159 */     addDatabaseSpecificStatement("oracle", "bulkInsertCompensateEventSubscription", "bulkInsertCompensateEventSubscription_oracle");
/* 160 */     addDatabaseSpecificStatement("oracle", "bulkInsertEventLogEntry", "bulkInsertEventLogEntry_oracle");
/* 161 */     addDatabaseSpecificStatement("oracle", "bulkInsertDeployment", "bulkInsertDeployment_oracle");
/* 162 */     addDatabaseSpecificStatement("oracle", "bulkInsertComment", "bulkInsertComment_oracle");
/* 163 */     addDatabaseSpecificStatement("oracle", "bulkInsertByteArray", "bulkInsertByteArray_oracle");
/* 164 */     addDatabaseSpecificStatement("oracle", "bulkInsertEventLogEntry", "bulkInsertEventLogEntry_oracle");
/* 165 */     addDatabaseSpecificStatement("oracle", "bulkInsertDeployment", "bulkInsertDeployment_oracle");
/* 166 */     addDatabaseSpecificStatement("oracle", "bulkInsertComment", "bulkInsertComment_oracle");
/* 167 */     addDatabaseSpecificStatement("oracle", "bulkInsertByteArray", "bulkInsertByteArray_oracle");
/* 168 */     addDatabaseSpecificStatement("oracle", "bulkInsertAttachment", "bulkInsertAttachment_oracle");
/*     */ 
/*     */     
/* 171 */     databaseSpecificLimitBeforeStatements.put("dmsql", "select * from ( select a.*, ROWNUM rnum from (");
/* 172 */     databaseSpecificLimitAfterStatements.put("dmsql", "  ) a where ROWNUM < #{lastRow}) where rnum  >= #{firstRow}");
/* 173 */     databaseSpecificLimitBetweenStatements.put("dmsql", "");
/* 174 */     databaseOuterJoinLimitBetweenStatements.put("dmsql", "");
/* 175 */     databaseSpecificOrderByStatements.put("dmsql", defaultOrderBy);
/*     */ 
/*     */     
/* 178 */     databaseSpecificLimitBeforeStatements.put("highgo", "");
/* 179 */     databaseSpecificLimitAfterStatements.put("highgo", "LIMIT #{maxResults} OFFSET #{firstResult}");
/* 180 */     databaseSpecificLimitBetweenStatements.put("highgo", "");
/* 181 */     databaseOuterJoinLimitBetweenStatements.put("highgo", "");
/* 182 */     databaseSpecificOrderByStatements.put("highgo", defaultOrderBy);
/*     */ 
/*     */     
/* 185 */     databaseSpecificLimitBeforeStatements.put("db2", "SELECT SUB.* FROM (");
/* 186 */     databaseSpecificLimitAfterStatements.put("db2", ")RES ) SUB WHERE SUB.rnk >= #{firstRow} AND SUB.rnk < #{lastRow}");
/* 187 */     databaseSpecificLimitBetweenStatements.put("db2", ", row_number() over (ORDER BY ${orderByColumns}) rnk FROM ( select distinct RES.* ");
/* 188 */     databaseOuterJoinLimitBetweenStatements.put("db2", ", row_number() over (ORDER BY ${mssqlOrDB2OrderBy}) rnk FROM ( select distinct ");
/* 189 */     databaseSpecificOrderByStatements.put("db2", "");
/* 190 */     databaseSpecificLimitBeforeNativeQueryStatements.put("db2", "SELECT SUB.* FROM ( select RES.* , row_number() over (ORDER BY ${orderByColumns}) rnk FROM (");
/* 191 */     addDatabaseSpecificStatement("db2", "selectExclusiveJobsToExecute", "selectExclusiveJobsToExecute_integerBoolean");
/* 192 */     addDatabaseSpecificStatement("db2", "selectExecutionByNativeQuery", "selectExecutionByNativeQuery_mssql_or_db2");
/* 193 */     addDatabaseSpecificStatement("db2", "selectHistoricActivityInstanceByNativeQuery", "selectHistoricActivityInstanceByNativeQuery_mssql_or_db2");
/* 194 */     addDatabaseSpecificStatement("db2", "selectHistoricProcessInstanceByNativeQuery", "selectHistoricProcessInstanceByNativeQuery_mssql_or_db2");
/* 195 */     addDatabaseSpecificStatement("db2", "selectHistoricTaskInstanceByNativeQuery", "selectHistoricTaskInstanceByNativeQuery_mssql_or_db2");
/* 196 */     addDatabaseSpecificStatement("db2", "selectTaskByNativeQuery", "selectTaskByNativeQuery_mssql_or_db2");
/* 197 */     addDatabaseSpecificStatement("db2", "selectProcessDefinitionByNativeQuery", "selectProcessDefinitionByNativeQuery_mssql_or_db2");
/* 198 */     addDatabaseSpecificStatement("db2", "selectDeploymentByNativeQuery", "selectDeploymentByNativeQuery_mssql_or_db2");
/* 199 */     addDatabaseSpecificStatement("db2", "selectGroupByNativeQuery", "selectGroupByNativeQuery_mssql_or_db2");
/* 200 */     addDatabaseSpecificStatement("db2", "selectUserByNativeQuery", "selectUserByNativeQuery_mssql_or_db2");
/* 201 */     addDatabaseSpecificStatement("db2", "selectModelByNativeQuery", "selectModelByNativeQuery_mssql_or_db2");
/* 202 */     addDatabaseSpecificStatement("db2", "selectHistoricDetailByNativeQuery", "selectHistoricDetailByNativeQuery_mssql_or_db2");
/* 203 */     addDatabaseSpecificStatement("db2", "selectHistoricVariableInstanceByNativeQuery", "selectHistoricVariableInstanceByNativeQuery_mssql_or_db2");
/* 204 */     addDatabaseSpecificStatement("db2", "selectTaskWithVariablesByQueryCriteria", "selectTaskWithVariablesByQueryCriteria_mssql_or_db2");
/* 205 */     addDatabaseSpecificStatement("db2", "selectProcessInstanceWithVariablesByQueryCriteria", "selectProcessInstanceWithVariablesByQueryCriteria_mssql_or_db2");
/* 206 */     addDatabaseSpecificStatement("db2", "selectHistoricProcessInstancesWithVariablesByQueryCriteria", "selectHistoricProcessInstancesWithVariablesByQueryCriteria_mssql_or_db2");
/* 207 */     addDatabaseSpecificStatement("db2", "selectHistoricTaskInstancesWithVariablesByQueryCriteria", "selectHistoricTaskInstancesWithVariablesByQueryCriteria_mssql_or_db2");
/*     */ 
/*     */     
/* 210 */     databaseSpecificLimitBeforeStatements.put("mssql", "SELECT SUB.* FROM (");
/* 211 */     databaseSpecificLimitAfterStatements.put("mssql", ")RES ) SUB WHERE SUB.rnk >= #{firstRow} AND SUB.rnk < #{lastRow}");
/* 212 */     databaseSpecificLimitBetweenStatements.put("mssql", ", row_number() over (ORDER BY ${orderByColumns}) rnk FROM ( select distinct RES.* ");
/* 213 */     databaseOuterJoinLimitBetweenStatements.put("mssql", ", row_number() over (ORDER BY ${mssqlOrDB2OrderBy}) rnk FROM ( select distinct ");
/* 214 */     databaseSpecificOrderByStatements.put("mssql", "");
/* 215 */     databaseSpecificLimitBeforeNativeQueryStatements.put("mssql", "SELECT SUB.* FROM ( select RES.* , row_number() over (ORDER BY ${orderByColumns}) rnk FROM (");
/* 216 */     addDatabaseSpecificStatement("mssql", "selectExclusiveJobsToExecute", "selectExclusiveJobsToExecute_integerBoolean");
/* 217 */     addDatabaseSpecificStatement("mssql", "selectExecutionByNativeQuery", "selectExecutionByNativeQuery_mssql_or_db2");
/* 218 */     addDatabaseSpecificStatement("mssql", "selectHistoricActivityInstanceByNativeQuery", "selectHistoricActivityInstanceByNativeQuery_mssql_or_db2");
/* 219 */     addDatabaseSpecificStatement("mssql", "selectHistoricProcessInstanceByNativeQuery", "selectHistoricProcessInstanceByNativeQuery_mssql_or_db2");
/* 220 */     addDatabaseSpecificStatement("mssql", "selectHistoricTaskInstanceByNativeQuery", "selectHistoricTaskInstanceByNativeQuery_mssql_or_db2");
/* 221 */     addDatabaseSpecificStatement("mssql", "selectTaskByNativeQuery", "selectTaskByNativeQuery_mssql_or_db2");
/* 222 */     addDatabaseSpecificStatement("mssql", "selectProcessDefinitionByNativeQuery", "selectProcessDefinitionByNativeQuery_mssql_or_db2");
/* 223 */     addDatabaseSpecificStatement("mssql", "selectDeploymentByNativeQuery", "selectDeploymentByNativeQuery_mssql_or_db2");
/* 224 */     addDatabaseSpecificStatement("mssql", "selectGroupByNativeQuery", "selectGroupByNativeQuery_mssql_or_db2");
/* 225 */     addDatabaseSpecificStatement("mssql", "selectUserByNativeQuery", "selectUserByNativeQuery_mssql_or_db2");
/* 226 */     addDatabaseSpecificStatement("mssql", "selectModelByNativeQuery", "selectModelByNativeQuery_mssql_or_db2");
/* 227 */     addDatabaseSpecificStatement("mssql", "selectHistoricDetailByNativeQuery", "selectHistoricDetailByNativeQuery_mssql_or_db2");
/* 228 */     addDatabaseSpecificStatement("mssql", "selectHistoricVariableInstanceByNativeQuery", "selectHistoricVariableInstanceByNativeQuery_mssql_or_db2");
/* 229 */     addDatabaseSpecificStatement("mssql", "selectTaskWithVariablesByQueryCriteria", "selectTaskWithVariablesByQueryCriteria_mssql_or_db2");
/* 230 */     addDatabaseSpecificStatement("mssql", "selectProcessInstanceWithVariablesByQueryCriteria", "selectProcessInstanceWithVariablesByQueryCriteria_mssql_or_db2");
/* 231 */     addDatabaseSpecificStatement("mssql", "selectHistoricProcessInstancesWithVariablesByQueryCriteria", "selectHistoricProcessInstancesWithVariablesByQueryCriteria_mssql_or_db2");
/* 232 */     addDatabaseSpecificStatement("mssql", "selectHistoricTaskInstancesWithVariablesByQueryCriteria", "selectHistoricTaskInstancesWithVariablesByQueryCriteria_mssql_or_db2");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   protected String databaseType;
/*     */ 
/*     */ 
/*     */   
/* 242 */   protected String databaseTablePrefix = "";
/*     */   
/*     */   private boolean tablePrefixIsSchema;
/*     */   
/*     */   protected String databaseCatalog;
/*     */   
/*     */   protected String databaseSchema;
/*     */   
/*     */   protected SqlSessionFactory sqlSessionFactory;
/*     */   
/*     */   protected IdGenerator idGenerator;
/*     */   
/*     */   protected Map<String, String> statementMappings;
/*     */   
/* 256 */   protected Map<Class<?>, String> insertStatements = new ConcurrentHashMap<>();
/* 257 */   protected Map<Class<?>, String> bulkInsertStatements = new ConcurrentHashMap<>();
/* 258 */   protected Map<Class<?>, String> updateStatements = new ConcurrentHashMap<>();
/* 259 */   protected Map<Class<?>, String> deleteStatements = new ConcurrentHashMap<>();
/* 260 */   protected Map<Class<?>, String> bulkDeleteStatements = new ConcurrentHashMap<>();
/* 261 */   protected Map<Class<?>, String> selectStatements = new ConcurrentHashMap<>();
/*     */   protected boolean isDbIdentityUsed = true;
/*     */   protected boolean isDbHistoryUsed = true;
/* 264 */   protected int maxNrOfStatementsInBulkInsert = 100;
/*     */ 
/*     */   
/*     */   public Class<?> getSessionType() {
/* 268 */     return DbSqlSession.class;
/*     */   }
/*     */   
/*     */   public Session openSession() {
/* 272 */     return (Session)new DbSqlSession(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public String getInsertStatement(PersistentObject object) {
/* 278 */     return getStatement(object.getClass(), this.insertStatements, "insert");
/*     */   }
/*     */ 
/*     */   
/*     */   public String getInsertStatement(Class<? extends PersistentObject> clazz) {
/* 283 */     return getStatement(clazz, this.insertStatements, "insert");
/*     */   }
/*     */   
/*     */   public String getBulkInsertStatement(Class<?> clazz) {
/* 287 */     return getStatement(clazz, this.bulkInsertStatements, "bulkInsert");
/*     */   }
/*     */   
/*     */   public String getUpdateStatement(PersistentObject object) {
/* 291 */     return getStatement(object.getClass(), this.updateStatements, "update");
/*     */   }
/*     */   
/*     */   public String getDeleteStatement(Class<?> persistentObjectClass) {
/* 295 */     return getStatement(persistentObjectClass, this.deleteStatements, "delete");
/*     */   }
/*     */   
/*     */   public String getBulkDeleteStatement(Class<?> persistentObjectClass) {
/* 299 */     return getStatement(persistentObjectClass, this.bulkDeleteStatements, "bulkDelete");
/*     */   }
/*     */   
/*     */   public String getSelectStatement(Class<?> persistentObjectClass) {
/* 303 */     return getStatement(persistentObjectClass, this.selectStatements, "select");
/*     */   }
/*     */   
/*     */   private String getStatement(Class<?> persistentObjectClass, Map<Class<?>, String> cachedStatements, String prefix) {
/* 307 */     String statement = cachedStatements.get(persistentObjectClass);
/* 308 */     if (statement != null) {
/* 309 */       return statement;
/*     */     }
/* 311 */     statement = prefix + persistentObjectClass.getSimpleName();
/* 312 */     statement = statement.substring(0, statement.length() - 6);
/* 313 */     cachedStatements.put(persistentObjectClass, statement);
/* 314 */     return statement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected static void addDatabaseSpecificStatement(String databaseType, String activitiStatement, String ibatisStatement) {
/* 320 */     Map<String, String> specificStatements = databaseSpecificStatements.get(databaseType);
/* 321 */     if (specificStatements == null) {
/* 322 */       specificStatements = new HashMap<>();
/* 323 */       databaseSpecificStatements.put(databaseType, specificStatements);
/*     */     } 
/* 325 */     specificStatements.put(activitiStatement, ibatisStatement);
/*     */   }
/*     */   
/*     */   public String mapStatement(String statement) {
/* 329 */     if (this.statementMappings == null) {
/* 330 */       return statement;
/*     */     }
/* 332 */     String mappedStatement = this.statementMappings.get(statement);
/* 333 */     return (mappedStatement != null) ? mappedStatement : statement;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setDatabaseType(String databaseType) {
/* 339 */     this.databaseType = databaseType;
/* 340 */     this.statementMappings = databaseSpecificStatements.get(databaseType);
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBulkInsertEnabled(boolean isBulkInsertEnabled, String databaseType) {
/* 345 */     if (isBulkInsertEnabled) {
/* 346 */       initBulkInsertEnabledMap(databaseType);
/*     */     }
/*     */   }
/*     */   
/*     */   protected void initBulkInsertEnabledMap(String databaseType) {
/* 351 */     bulkInsertableMap = new HashMap<>();
/*     */     
/* 353 */     for (Class<? extends PersistentObject> clazz : (Iterable<Class<? extends PersistentObject>>)EntityDependencyOrder.INSERT_ORDER) {
/* 354 */       bulkInsertableMap.put(clazz, Boolean.TRUE);
/*     */     }
/*     */ 
/*     */     
/* 358 */     if ("oracle".equals(databaseType)) {
/* 359 */       bulkInsertableMap.put(EventLogEntryEntity.class, Boolean.FALSE);
/*     */     }
/*     */   }
/*     */   
/*     */   public Boolean isBulkInsertable(Class<? extends PersistentObject> persistentObjectClass) {
/* 364 */     return Boolean.valueOf((bulkInsertableMap != null && bulkInsertableMap.containsKey(persistentObjectClass) && ((Boolean)bulkInsertableMap.get(persistentObjectClass)).booleanValue() == true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SqlSessionFactory getSqlSessionFactory() {
/* 370 */     return this.sqlSessionFactory;
/*     */   }
/*     */   
/*     */   public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
/* 374 */     this.sqlSessionFactory = sqlSessionFactory;
/*     */   }
/*     */   
/*     */   public IdGenerator getIdGenerator() {
/* 378 */     return this.idGenerator;
/*     */   }
/*     */   
/*     */   public void setIdGenerator(IdGenerator idGenerator) {
/* 382 */     this.idGenerator = idGenerator;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getDatabaseType() {
/* 387 */     return this.databaseType;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, String> getStatementMappings() {
/* 392 */     return this.statementMappings;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setStatementMappings(Map<String, String> statementMappings) {
/* 397 */     this.statementMappings = statementMappings;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Class<?>, String> getInsertStatements() {
/* 402 */     return this.insertStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setInsertStatements(Map<Class<?>, String> insertStatements) {
/* 407 */     this.insertStatements = insertStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Class<?>, String> getBulkInsertStatements() {
/* 412 */     return this.bulkInsertStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setBulkInsertStatements(Map<Class<?>, String> bulkInsertStatements) {
/* 417 */     this.bulkInsertStatements = bulkInsertStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Class<?>, String> getUpdateStatements() {
/* 422 */     return this.updateStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setUpdateStatements(Map<Class<?>, String> updateStatements) {
/* 427 */     this.updateStatements = updateStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Class<?>, String> getDeleteStatements() {
/* 432 */     return this.deleteStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setDeleteStatements(Map<Class<?>, String> deleteStatements) {
/* 437 */     this.deleteStatements = deleteStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<Class<?>, String> getBulkDeleteStatements() {
/* 442 */     return this.bulkDeleteStatements;
/*     */   }
/*     */   
/*     */   public void setBulkDeleteStatements(Map<Class<?>, String> bulkDeleteStatements) {
/* 446 */     this.bulkDeleteStatements = bulkDeleteStatements;
/*     */   }
/*     */   
/*     */   public Map<Class<?>, String> getSelectStatements() {
/* 450 */     return this.selectStatements;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setSelectStatements(Map<Class<?>, String> selectStatements) {
/* 455 */     this.selectStatements = selectStatements;
/*     */   }
/*     */   
/*     */   public boolean isDbIdentityUsed() {
/* 459 */     return this.isDbIdentityUsed;
/*     */   }
/*     */   
/*     */   public void setDbIdentityUsed(boolean isDbIdentityUsed) {
/* 463 */     this.isDbIdentityUsed = isDbIdentityUsed;
/*     */   }
/*     */   
/*     */   public boolean isDbHistoryUsed() {
/* 467 */     return this.isDbHistoryUsed;
/*     */   }
/*     */   
/*     */   public void setDbHistoryUsed(boolean isDbHistoryUsed) {
/* 471 */     this.isDbHistoryUsed = isDbHistoryUsed;
/*     */   }
/*     */   
/*     */   public void setDatabaseTablePrefix(String databaseTablePrefix) {
/* 475 */     this.databaseTablePrefix = databaseTablePrefix;
/*     */   }
/*     */   
/*     */   public String getDatabaseTablePrefix() {
/* 479 */     return this.databaseTablePrefix;
/*     */   }
/*     */   
/*     */   public String getDatabaseCatalog() {
/* 483 */     return this.databaseCatalog;
/*     */   }
/*     */   
/*     */   public void setDatabaseCatalog(String databaseCatalog) {
/* 487 */     this.databaseCatalog = databaseCatalog;
/*     */   }
/*     */   
/*     */   public String getDatabaseSchema() {
/* 491 */     return this.databaseSchema;
/*     */   }
/*     */   
/*     */   public void setDatabaseSchema(String databaseSchema) {
/* 495 */     this.databaseSchema = databaseSchema;
/*     */   }
/*     */   
/*     */   public void setTablePrefixIsSchema(boolean tablePrefixIsSchema) {
/* 499 */     this.tablePrefixIsSchema = tablePrefixIsSchema;
/*     */   }
/*     */   
/*     */   public boolean isTablePrefixIsSchema() {
/* 503 */     return this.tablePrefixIsSchema;
/*     */   }
/*     */   
/*     */   public int getMaxNrOfStatementsInBulkInsert() {
/* 507 */     return this.maxNrOfStatementsInBulkInsert;
/*     */   }
/*     */   
/*     */   public void setMaxNrOfStatementsInBulkInsert(int maxNrOfStatementsInBulkInsert) {
/* 511 */     this.maxNrOfStatementsInBulkInsert = maxNrOfStatementsInBulkInsert;
/*     */   }
/*     */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/org/activiti/engine/impl/db/DbSqlSessionFactory.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */