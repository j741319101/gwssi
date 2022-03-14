/*      */ package com.highgo.jdbc.jdbc;
/*      */ 
/*      */ import com.highgo.jdbc.Driver;
/*      */ import com.highgo.jdbc.core.BaseStatement;
/*      */ import com.highgo.jdbc.core.Field;
/*      */ import com.highgo.jdbc.core.ServerVersion;
/*      */ import com.highgo.jdbc.core.Tuple;
/*      */ import com.highgo.jdbc.core.TypeInfo;
/*      */ import com.highgo.jdbc.core.Version;
/*      */ import com.highgo.jdbc.util.ByteConverter;
/*      */ import com.highgo.jdbc.util.GT;
/*      */ import com.highgo.jdbc.util.JdbcBlackHole;
/*      */ import com.highgo.jdbc.util.PSQLException;
/*      */ import com.highgo.jdbc.util.PSQLState;
/*      */ import java.math.BigInteger;
/*      */ import java.sql.Array;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.ResultSet;
/*      */ import java.sql.RowIdLifetime;
/*      */ import java.sql.SQLException;
/*      */ import java.sql.Statement;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.StringTokenizer;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class PgDatabaseMetaData
/*      */   implements DatabaseMetaData
/*      */ {
/*      */   private String keywords;
/*      */   protected final PgConnection connection;
/*   41 */   private int nameDataLength = 0;
/*   42 */   private int indexMaxKeys = 0;
/*   43 */   private static final Map<String, Map<String, String>> tableTypeClauses = new HashMap<>();
/*      */   
/*      */   public PgDatabaseMetaData(PgConnection conn) {
/*   46 */     this.connection = conn;
/*      */   }
/*      */   
/*      */   protected int getMaxIndexKeys() throws SQLException {
/*   50 */     if (this.indexMaxKeys == 0) {
/*   51 */       String sql = "SELECT setting FROM pg_catalog.pg_settings WHERE name='max_index_keys'";
/*   52 */       Statement stmt = this.connection.createStatement();
/*   53 */       ResultSet rs = null;
/*      */       
/*      */       try {
/*   56 */         rs = stmt.executeQuery(sql);
/*   57 */         if (!rs.next()) {
/*   58 */           stmt.close();
/*   59 */           throw new PSQLException(GT.tr("Unable to determine a value for MaxIndexKeys due to missing system catalog data.", new Object[0]), PSQLState.UNEXPECTED_ERROR);
/*      */         } 
/*      */         
/*   62 */         this.indexMaxKeys = rs.getInt(1);
/*      */       } finally {
/*   64 */         JdbcBlackHole.close(rs);
/*   65 */         JdbcBlackHole.close(stmt);
/*      */       } 
/*      */     } 
/*      */     
/*   69 */     return this.indexMaxKeys;
/*      */   }
/*      */   
/*      */   protected int getMaxNameLength() throws SQLException {
/*   73 */     if (this.nameDataLength == 0) {
/*   74 */       String sql = "SELECT t.typlen FROM pg_catalog.pg_type t, pg_catalog.pg_namespace n WHERE t.typnamespace=n.oid AND t.typname='name' AND n.nspname='pg_catalog'";
/*   75 */       Statement stmt = this.connection.createStatement();
/*   76 */       ResultSet rs = null;
/*      */       
/*      */       try {
/*   79 */         rs = stmt.executeQuery(sql);
/*   80 */         if (!rs.next()) {
/*   81 */           throw new PSQLException(GT.tr("Unable to find name datatype in the system catalogs.", new Object[0]), PSQLState.UNEXPECTED_ERROR);
/*      */         }
/*      */         
/*   84 */         this.nameDataLength = rs.getInt("typlen");
/*      */       } finally {
/*   86 */         JdbcBlackHole.close(rs);
/*   87 */         JdbcBlackHole.close(stmt);
/*      */       } 
/*      */     } 
/*      */     
/*   91 */     return this.nameDataLength - 1;
/*      */   }
/*      */   
/*      */   public boolean allProceduresAreCallable() throws SQLException {
/*   95 */     return true;
/*      */   }
/*      */   
/*      */   public boolean allTablesAreSelectable() throws SQLException {
/*   99 */     return true;
/*      */   }
/*      */   
/*      */   public String getURL() throws SQLException {
/*  103 */     return this.connection.getURL();
/*      */   }
/*      */   
/*      */   public String getUserName() throws SQLException {
/*  107 */     return this.connection.getUserName();
/*      */   }
/*      */   
/*      */   public boolean isReadOnly() throws SQLException {
/*  111 */     return this.connection.isReadOnly();
/*      */   }
/*      */   
/*      */   public boolean nullsAreSortedHigh() throws SQLException {
/*  115 */     return true;
/*      */   }
/*      */   
/*      */   public boolean nullsAreSortedLow() throws SQLException {
/*  119 */     return false;
/*      */   }
/*      */   
/*      */   public boolean nullsAreSortedAtStart() throws SQLException {
/*  123 */     return false;
/*      */   }
/*      */   
/*      */   public boolean nullsAreSortedAtEnd() throws SQLException {
/*  127 */     return false;
/*      */   }
/*      */   
/*      */   public String getDatabaseProductName() throws SQLException {
/*  131 */     return "PostgreSQL";
/*      */   }
/*      */   
/*      */   public String getDatabaseProductVersion() throws SQLException {
/*  135 */     return this.connection.getDBVersionNumber();
/*      */   }
/*      */   
/*      */   public String getDriverName() {
/*  139 */     return "PostgreSQL JDBC Driver";
/*      */   }
/*      */   
/*      */   public String getDriverVersion() {
/*  143 */     return "6.0.3.SNAPSHOT";
/*      */   }
/*      */   
/*      */   public int getDriverMajorVersion() {
/*  147 */     return 6;
/*      */   }
/*      */   
/*      */   public int getDriverMinorVersion() {
/*  151 */     return 0;
/*      */   }
/*      */   
/*      */   public boolean usesLocalFiles() throws SQLException {
/*  155 */     return false;
/*      */   }
/*      */   
/*      */   public boolean usesLocalFilePerTable() throws SQLException {
/*  159 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsMixedCaseIdentifiers() throws SQLException {
/*  163 */     return false;
/*      */   }
/*      */   
/*      */   public boolean storesUpperCaseIdentifiers() throws SQLException {
/*  167 */     return false;
/*      */   }
/*      */   
/*      */   public boolean storesLowerCaseIdentifiers() throws SQLException {
/*  171 */     return true;
/*      */   }
/*      */   
/*      */   public boolean storesMixedCaseIdentifiers() throws SQLException {
/*  175 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
/*  179 */     return true;
/*      */   }
/*      */   
/*      */   public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
/*  183 */     return false;
/*      */   }
/*      */   
/*      */   public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
/*  187 */     return false;
/*      */   }
/*      */   
/*      */   public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
/*  191 */     return false;
/*      */   }
/*      */   
/*      */   public String getIdentifierQuoteString() throws SQLException {
/*  195 */     return "\"";
/*      */   }
/*      */   
/*      */   public String getSQLKeywords() throws SQLException {
/*  199 */     this.connection.checkClosed();
/*  200 */     if (this.keywords == null) {
/*  201 */       if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v9_0)) {
/*  202 */         String sql = "select string_agg(word, ',') from pg_catalog.pg_get_keywords() where word <> ALL ('{a,abs,absolute,action,ada,add,admin,after,all,allocate,alter,always,and,any,are,array,as,asc,asensitive,assertion,assignment,asymmetric,at,atomic,attribute,attributes,authorization,avg,before,begin,bernoulli,between,bigint,binary,blob,boolean,both,breadth,by,c,call,called,cardinality,cascade,cascaded,case,cast,catalog,catalog_name,ceil,ceiling,chain,char,char_length,character,character_length,character_set_catalog,character_set_name,character_set_schema,characteristics,characters,check,checked,class_origin,clob,close,coalesce,cobol,code_units,collate,collation,collation_catalog,collation_name,collation_schema,collect,column,column_name,command_function,command_function_code,commit,committed,condition,condition_number,connect,connection_name,constraint,constraint_catalog,constraint_name,constraint_schema,constraints,constructors,contains,continue,convert,corr,corresponding,count,covar_pop,covar_samp,create,cross,cube,cume_dist,current,current_collation,current_date,current_default_transform_group,current_path,current_role,current_time,current_timestamp,current_transform_group_for_type,current_user,cursor,cursor_name,cycle,data,date,datetime_interval_code,datetime_interval_precision,day,deallocate,dec,decimal,declare,default,defaults,deferrable,deferred,defined,definer,degree,delete,dense_rank,depth,deref,derived,desc,describe,descriptor,deterministic,diagnostics,disconnect,dispatch,distinct,domain,double,drop,dynamic,dynamic_function,dynamic_function_code,each,element,else,end,end-exec,equals,escape,every,except,exception,exclude,excluding,exec,execute,exists,exp,external,extract,false,fetch,filter,final,first,float,floor,following,for,foreign,fortran,found,free,from,full,function,fusion,g,general,get,global,go,goto,grant,granted,group,grouping,having,hierarchy,hold,hour,identity,immediate,implementation,in,including,increment,indicator,initially,inner,inout,input,insensitive,insert,instance,instantiable,int,integer,intersect,intersection,interval,into,invoker,is,isolation,join,k,key,key_member,key_type,language,large,last,lateral,leading,left,length,level,like,ln,local,localtime,localtimestamp,locator,lower,m,map,match,matched,max,maxvalue,member,merge,message_length,message_octet_length,message_text,method,min,minute,minvalue,mod,modifies,module,month,more,multiset,mumps,name,names,national,natural,nchar,nclob,nesting,new,next,no,none,normalize,normalized,not,\"null\",nullable,nullif,nulls,number,numeric,object,octet_length,octets,of,old,on,only,open,option,options,or,order,ordering,ordinality,others,out,outer,output,over,overlaps,overlay,overriding,pad,parameter,parameter_mode,parameter_name,parameter_ordinal_position,parameter_specific_catalog,parameter_specific_name,parameter_specific_schema,partial,partition,pascal,path,percent_rank,percentile_cont,percentile_disc,placing,pli,position,power,preceding,precision,prepare,preserve,primary,prior,privileges,procedure,public,range,rank,read,reads,real,recursive,ref,references,referencing,regr_avgx,regr_avgy,regr_count,regr_intercept,regr_r2,regr_slope,regr_sxx,regr_sxy,regr_syy,relative,release,repeatable,restart,result,return,returned_cardinality,returned_length,returned_octet_length,returned_sqlstate,returns,revoke,right,role,rollback,rollup,routine,routine_catalog,routine_name,routine_schema,row,row_count,row_number,rows,savepoint,scale,schema,schema_name,scope_catalog,scope_name,scope_schema,scroll,search,second,section,security,select,self,sensitive,sequence,serializable,server_name,session,session_user,set,sets,similar,simple,size,smallint,some,source,space,specific,specific_name,specifictype,sql,sqlexception,sqlstate,sqlwarning,sqrt,start,state,statement,static,stddev_pop,stddev_samp,structure,style,subclass_origin,submultiset,substring,sum,symmetric,system,system_user,table,table_name,tablesample,temporary,then,ties,time,timestamp,timezone_hour,timezone_minute,to,top_level_count,trailing,transaction,transaction_active,transactions_committed,transactions_rolled_back,transform,transforms,translate,translation,treat,trigger,trigger_catalog,trigger_name,trigger_schema,trim,true,type,uescape,unbounded,uncommitted,under,union,unique,unknown,unnamed,unnest,update,upper,usage,user,user_defined_type_catalog,user_defined_type_code,user_defined_type_name,user_defined_type_schema,using,value,values,var_pop,var_samp,varchar,varying,view,when,whenever,where,width_bucket,window,with,within,without,work,write,year,zone}'::text[])";
/*  203 */         Statement stmt = null;
/*  204 */         ResultSet rs = null;
/*      */         
/*      */         try {
/*  207 */           stmt = this.connection.createStatement();
/*  208 */           rs = stmt.executeQuery(sql);
/*  209 */           if (!rs.next()) {
/*  210 */             throw new PSQLException(GT.tr("Unable to find keywords in the system catalogs.", new Object[0]), PSQLState.UNEXPECTED_ERROR);
/*      */           }
/*      */           
/*  213 */           this.keywords = rs.getString(1);
/*      */         } finally {
/*  215 */           JdbcBlackHole.close(rs);
/*  216 */           JdbcBlackHole.close(stmt);
/*      */         } 
/*      */       } else {
/*  219 */         this.keywords = "abort,access,aggregate,also,analyse,analyze,backward,bit,cache,checkpoint,class,cluster,comment,concurrently,connection,conversion,copy,csv,database,delimiter,delimiters,disable,do,enable,encoding,encrypted,exclusive,explain,force,forward,freeze,greatest,handler,header,if,ilike,immutable,implicit,index,indexes,inherit,inherits,instead,isnull,least,limit,listen,load,location,lock,mode,move,nothing,notify,notnull,nowait,off,offset,oids,operator,owned,owner,password,prepared,procedural,quote,reassign,recheck,reindex,rename,replace,reset,restrict,returning,rule,setof,share,show,stable,statistics,stdin,stdout,storage,strict,sysid,tablespace,temp,template,truncate,trusted,unencrypted,unlisten,until,vacuum,valid,validator,verbose,volatile";
/*      */       } 
/*      */     }
/*      */     
/*  223 */     return this.keywords;
/*      */   }
/*      */   
/*      */   public String getNumericFunctions() throws SQLException {
/*  227 */     return "abs,acos,asin,atan,atan2,ceiling,cos,cot,degrees,exp,floor,log,log10,mod,pi,power,radians,round,sign,sin,sqrt,tan,truncate";
/*      */   }
/*      */   
/*      */   public String getStringFunctions() throws SQLException {
/*  231 */     String funcs = "ascii,char,concat,lcase,left,length,ltrim,repeat,rtrim,space,substring,ucase";
/*  232 */     funcs = funcs + ",replace";
/*  233 */     return funcs;
/*      */   }
/*      */   
/*      */   public String getSystemFunctions() throws SQLException {
/*  237 */     return "database,ifnull,user";
/*      */   }
/*      */   
/*      */   public String getTimeDateFunctions() throws SQLException {
/*  241 */     String timeDateFuncs = "curdate,curtime,dayname,dayofmonth,dayofweek,dayofyear,hour,minute,month,monthname,now,quarter,second,week,year";
/*  242 */     timeDateFuncs = timeDateFuncs + ",timestampadd";
/*  243 */     return timeDateFuncs;
/*      */   }
/*      */   
/*      */   public String getSearchStringEscape() throws SQLException {
/*  247 */     return "\\";
/*      */   }
/*      */   
/*      */   public String getExtraNameCharacters() throws SQLException {
/*  251 */     return "";
/*      */   }
/*      */   
/*      */   public boolean supportsAlterTableWithAddColumn() throws SQLException {
/*  255 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsAlterTableWithDropColumn() throws SQLException {
/*  259 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsColumnAliasing() throws SQLException {
/*  263 */     return true;
/*      */   }
/*      */   
/*      */   public boolean nullPlusNonNullIsNull() throws SQLException {
/*  267 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsConvert() throws SQLException {
/*  271 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsConvert(int fromType, int toType) throws SQLException {
/*  275 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsTableCorrelationNames() throws SQLException {
/*  279 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsDifferentTableCorrelationNames() throws SQLException {
/*  283 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsExpressionsInOrderBy() throws SQLException {
/*  287 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsOrderByUnrelated() throws SQLException {
/*  291 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsGroupBy() throws SQLException {
/*  295 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsGroupByUnrelated() throws SQLException {
/*  299 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsGroupByBeyondSelect() throws SQLException {
/*  303 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsLikeEscapeClause() throws SQLException {
/*  307 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsMultipleResultSets() throws SQLException {
/*  311 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsMultipleTransactions() throws SQLException {
/*  315 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsNonNullableColumns() throws SQLException {
/*  319 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsMinimumSQLGrammar() throws SQLException {
/*  323 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsCoreSQLGrammar() throws SQLException {
/*  327 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsExtendedSQLGrammar() throws SQLException {
/*  331 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsANSI92EntryLevelSQL() throws SQLException {
/*  335 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsANSI92IntermediateSQL() throws SQLException {
/*  339 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsANSI92FullSQL() throws SQLException {
/*  343 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsIntegrityEnhancementFacility() throws SQLException {
/*  347 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsOuterJoins() throws SQLException {
/*  351 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsFullOuterJoins() throws SQLException {
/*  355 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsLimitedOuterJoins() throws SQLException {
/*  359 */     return true;
/*      */   }
/*      */   
/*      */   public String getSchemaTerm() throws SQLException {
/*  363 */     return "schema";
/*      */   }
/*      */   
/*      */   public String getProcedureTerm() throws SQLException {
/*  367 */     return "function";
/*      */   }
/*      */   
/*      */   public String getCatalogTerm() throws SQLException {
/*  371 */     return "database";
/*      */   }
/*      */   
/*      */   public boolean isCatalogAtStart() throws SQLException {
/*  375 */     return true;
/*      */   }
/*      */   
/*      */   public String getCatalogSeparator() throws SQLException {
/*  379 */     return ".";
/*      */   }
/*      */   
/*      */   public boolean supportsSchemasInDataManipulation() throws SQLException {
/*  383 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSchemasInProcedureCalls() throws SQLException {
/*  387 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSchemasInTableDefinitions() throws SQLException {
/*  391 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSchemasInIndexDefinitions() throws SQLException {
/*  395 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
/*  399 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsCatalogsInDataManipulation() throws SQLException {
/*  403 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsCatalogsInProcedureCalls() throws SQLException {
/*  407 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsCatalogsInTableDefinitions() throws SQLException {
/*  411 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
/*  415 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
/*  419 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsPositionedDelete() throws SQLException {
/*  423 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsPositionedUpdate() throws SQLException {
/*  427 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsSelectForUpdate() throws SQLException {
/*  431 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsStoredProcedures() throws SQLException {
/*  435 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSubqueriesInComparisons() throws SQLException {
/*  439 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSubqueriesInExists() throws SQLException {
/*  443 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSubqueriesInIns() throws SQLException {
/*  447 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSubqueriesInQuantifieds() throws SQLException {
/*  451 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsCorrelatedSubqueries() throws SQLException {
/*  455 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsUnion() throws SQLException {
/*  459 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsUnionAll() throws SQLException {
/*  463 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
/*  467 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
/*  471 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
/*  475 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
/*  479 */     return true;
/*      */   }
/*      */   
/*      */   public int getMaxCharLiteralLength() throws SQLException {
/*  483 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxBinaryLiteralLength() throws SQLException {
/*  487 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxColumnNameLength() throws SQLException {
/*  491 */     return getMaxNameLength();
/*      */   }
/*      */   
/*      */   public int getMaxColumnsInGroupBy() throws SQLException {
/*  495 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxColumnsInIndex() throws SQLException {
/*  499 */     return getMaxIndexKeys();
/*      */   }
/*      */   
/*      */   public int getMaxColumnsInOrderBy() throws SQLException {
/*  503 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxColumnsInSelect() throws SQLException {
/*  507 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxColumnsInTable() throws SQLException {
/*  511 */     return 1600;
/*      */   }
/*      */   
/*      */   public int getMaxConnections() throws SQLException {
/*  515 */     return 8192;
/*      */   }
/*      */   
/*      */   public int getMaxCursorNameLength() throws SQLException {
/*  519 */     return getMaxNameLength();
/*      */   }
/*      */   
/*      */   public int getMaxIndexLength() throws SQLException {
/*  523 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxSchemaNameLength() throws SQLException {
/*  527 */     return getMaxNameLength();
/*      */   }
/*      */   
/*      */   public int getMaxProcedureNameLength() throws SQLException {
/*  531 */     return getMaxNameLength();
/*      */   }
/*      */   
/*      */   public int getMaxCatalogNameLength() throws SQLException {
/*  535 */     return getMaxNameLength();
/*      */   }
/*      */   
/*      */   public int getMaxRowSize() throws SQLException {
/*  539 */     return 1073741824;
/*      */   }
/*      */   
/*      */   public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
/*  543 */     return false;
/*      */   }
/*      */   
/*      */   public int getMaxStatementLength() throws SQLException {
/*  547 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxStatements() throws SQLException {
/*  551 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxTableNameLength() throws SQLException {
/*  555 */     return getMaxNameLength();
/*      */   }
/*      */   
/*      */   public int getMaxTablesInSelect() throws SQLException {
/*  559 */     return 0;
/*      */   }
/*      */   
/*      */   public int getMaxUserNameLength() throws SQLException {
/*  563 */     return getMaxNameLength();
/*      */   }
/*      */   
/*      */   public int getDefaultTransactionIsolation() throws SQLException {
/*  567 */     return 2;
/*      */   }
/*      */   
/*      */   public boolean supportsTransactions() throws SQLException {
/*  571 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsTransactionIsolationLevel(int level) throws SQLException {
/*  575 */     switch (level) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 4:
/*      */       case 8:
/*  580 */         return true;
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  586 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean supportsDataDefinitionAndDataManipulationTransactions() throws SQLException {
/*  591 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsDataManipulationTransactionsOnly() throws SQLException {
/*  595 */     return false;
/*      */   }
/*      */   
/*      */   public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
/*  599 */     return false;
/*      */   }
/*      */   
/*      */   public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
/*  603 */     return false;
/*      */   }
/*      */   
/*      */   protected String escapeQuotes(String s) throws SQLException {
/*  607 */     StringBuilder sb = new StringBuilder();
/*  608 */     if (!this.connection.getStandardConformingStrings()) {
/*  609 */       sb.append("E");
/*      */     }
/*      */     
/*  612 */     sb.append("'");
/*  613 */     sb.append(this.connection.escapeString(s));
/*  614 */     sb.append("'");
/*  615 */     return sb.toString();
/*      */   }
/*      */   
/*      */   public ResultSet getProcedures(String catalog, String schemaPattern, String procedureNamePattern) throws SQLException {
/*  619 */     String sql = "SELECT NULL AS PROCEDURE_CAT, n.nspname AS PROCEDURE_SCHEM, p.proname AS PROCEDURE_NAME, NULL, NULL, NULL, d.description AS REMARKS, 2 AS PROCEDURE_TYPE,  p.proname || '_' || p.oid AS SPECIFIC_NAME  FROM pg_catalog.pg_namespace n, pg_catalog.pg_proc p  LEFT JOIN pg_catalog.pg_description d ON (p.oid=d.objoid)  LEFT JOIN pg_catalog.pg_class c ON (d.classoid=c.oid AND c.relname='pg_proc')  LEFT JOIN pg_catalog.pg_namespace pn ON (c.relnamespace=pn.oid AND pn.nspname='pg_catalog')  WHERE p.pronamespace=n.oid ";
/*  620 */     if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v11)) {
/*  621 */       sql = sql + " AND p.prokind='p'";
/*      */     }
/*      */     
/*  624 */     if (schemaPattern != null && !schemaPattern.isEmpty()) {
/*  625 */       sql = sql + " AND n.nspname LIKE " + escapeQuotes(schemaPattern);
/*      */     } else {
/*  627 */       sql = sql + "and pg_function_is_visible(p.oid)";
/*      */     } 
/*      */     
/*  630 */     if (procedureNamePattern != null && !procedureNamePattern.isEmpty()) {
/*  631 */       sql = sql + " AND p.proname LIKE " + escapeQuotes(procedureNamePattern);
/*      */     }
/*      */     
/*  634 */     if (this.connection.getHideUnprivilegedObjects()) {
/*  635 */       sql = sql + " AND has_function_privilege(p.oid, 'EXECUTE')";
/*      */     }
/*      */     
/*  638 */     sql = sql + " ORDER BY PROCEDURE_SCHEM, PROCEDURE_NAME, p.oid::text ";
/*  639 */     return createMetaDataStatement().executeQuery(sql);
/*      */   }
/*      */   
/*      */   public ResultSet getProcedureColumns(String catalog, String schemaPattern, String procedureNamePattern, String columnNamePattern) throws SQLException {
/*  643 */     int columns = 20;
/*  644 */     Field[] f = new Field[columns];
/*  645 */     List<Tuple> v = new ArrayList<>();
/*  646 */     f[0] = new Field("PROCEDURE_CAT", 1043);
/*  647 */     f[1] = new Field("PROCEDURE_SCHEM", 1043);
/*  648 */     f[2] = new Field("PROCEDURE_NAME", 1043);
/*  649 */     f[3] = new Field("COLUMN_NAME", 1043);
/*  650 */     f[4] = new Field("COLUMN_TYPE", 21);
/*  651 */     f[5] = new Field("DATA_TYPE", 21);
/*  652 */     f[6] = new Field("TYPE_NAME", 1043);
/*  653 */     f[7] = new Field("PRECISION", 23);
/*  654 */     f[8] = new Field("LENGTH", 23);
/*  655 */     f[9] = new Field("SCALE", 21);
/*  656 */     f[10] = new Field("RADIX", 21);
/*  657 */     f[11] = new Field("NULLABLE", 21);
/*  658 */     f[12] = new Field("REMARKS", 1043);
/*  659 */     f[13] = new Field("COLUMN_DEF", 1043);
/*  660 */     f[14] = new Field("SQL_DATA_TYPE", 23);
/*  661 */     f[15] = new Field("SQL_DATETIME_SUB", 23);
/*  662 */     f[16] = new Field("CHAR_OCTET_LENGTH", 23);
/*  663 */     f[17] = new Field("ORDINAL_POSITION", 23);
/*  664 */     f[18] = new Field("IS_NULLABLE", 1043);
/*  665 */     f[19] = new Field("SPECIFIC_NAME", 1043);
/*  666 */     String sql = "SELECT n.nspname,p.proname,p.prorettype,p.proargtypes, t.typtype,t.typrelid,  p.proargnames, p.proargmodes, p.proallargtypes, p.oid  FROM pg_catalog.pg_proc p, pg_catalog.pg_namespace n, pg_catalog.pg_type t  WHERE p.pronamespace=n.oid AND p.prorettype=t.oid ";
/*  667 */     if (schemaPattern != null && !schemaPattern.isEmpty()) {
/*  668 */       sql = sql + " AND n.nspname LIKE " + escapeQuotes(schemaPattern);
/*      */     }
/*      */     
/*  671 */     if (procedureNamePattern != null && !procedureNamePattern.isEmpty()) {
/*  672 */       sql = sql + " AND p.proname LIKE " + escapeQuotes(procedureNamePattern);
/*      */     }
/*      */     
/*  675 */     sql = sql + " ORDER BY n.nspname, p.proname, p.oid::text ";
/*  676 */     byte[] isnullableUnknown = new byte[0];
/*  677 */     Statement stmt = this.connection.createStatement();
/*  678 */     ResultSet rs = stmt.executeQuery(sql);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/*  689 */       if (!rs.next()) {
/*  690 */         rs.close();
/*  691 */         stmt.close();
/*  692 */         return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */       } 
/*      */       
/*  695 */       byte[] schema = rs.getBytes("nspname");
/*  696 */       byte[] procedureName = rs.getBytes("proname");
/*  697 */       byte[] specificName = this.connection.encodeString(rs.getString("proname") + "_" + rs.getString("oid"));
/*  698 */       int returnType = (int)rs.getLong("prorettype");
/*  699 */       String returnTypeType = rs.getString("typtype");
/*  700 */       int returnTypeRelid = (int)rs.getLong("typrelid");
/*  701 */       String strArgTypes = rs.getString("proargtypes");
/*  702 */       StringTokenizer st = new StringTokenizer(strArgTypes);
/*  703 */       ArrayList<Long> argTypes = new ArrayList();
/*      */       
/*  705 */       while (st.hasMoreTokens()) {
/*  706 */         argTypes.add(Long.valueOf(st.nextToken()));
/*      */       }
/*      */       
/*  709 */       String[] argNames = null;
/*  710 */       Array argNamesArray = rs.getArray("proargnames");
/*  711 */       if (argNamesArray != null) {
/*  712 */         argNames = (String[])argNamesArray.getArray();
/*      */       }
/*      */       
/*  715 */       String[] argModes = null;
/*  716 */       Array argModesArray = rs.getArray("proargmodes");
/*  717 */       if (argModesArray != null) {
/*  718 */         argModes = (String[])argModesArray.getArray();
/*      */       }
/*      */       
/*  721 */       int numArgs = argTypes.size();
/*  722 */       Long[] allArgTypes = null;
/*  723 */       Array allArgTypesArray = rs.getArray("proallargtypes");
/*  724 */       if (allArgTypesArray != null) {
/*  725 */         allArgTypes = (Long[])allArgTypesArray.getArray();
/*  726 */         numArgs = allArgTypes.length;
/*      */       } 
/*      */       
/*  729 */       if (returnTypeType.equals("b") || returnTypeType.equals("d") || returnTypeType.equals("e") || (returnTypeType.equals("p") && argModesArray == null)) {
/*  730 */         byte[][] tuple = new byte[columns][];
/*  731 */         tuple[0] = null;
/*  732 */         tuple[1] = schema;
/*  733 */         tuple[2] = procedureName;
/*  734 */         tuple[3] = this.connection.encodeString("returnValue");
/*  735 */         tuple[4] = this.connection.encodeString(Integer.toString(5));
/*  736 */         tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(returnType)));
/*  737 */         tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(returnType));
/*  738 */         tuple[7] = null;
/*  739 */         tuple[8] = null;
/*  740 */         tuple[9] = null;
/*  741 */         tuple[10] = null;
/*  742 */         tuple[11] = this.connection.encodeString(Integer.toString(2));
/*  743 */         tuple[12] = null;
/*  744 */         tuple[17] = this.connection.encodeString(Integer.toString(0));
/*  745 */         tuple[18] = isnullableUnknown;
/*  746 */         tuple[19] = specificName;
/*  747 */         v.add(new Tuple(tuple));
/*      */       } 
/*      */       
/*  750 */       for (int i = 0; i < numArgs; i++) {
/*  751 */         int argOid; byte[][] tuple = new byte[columns][];
/*  752 */         tuple[0] = null;
/*  753 */         tuple[1] = schema;
/*  754 */         tuple[2] = procedureName;
/*  755 */         if (argNames != null) {
/*  756 */           tuple[3] = this.connection.encodeString(argNames[i]);
/*      */         } else {
/*  758 */           tuple[3] = this.connection.encodeString("$" + (i + 1));
/*      */         } 
/*      */         
/*  761 */         int columnMode = 1;
/*  762 */         if (argModes != null && argModes[i].equals("o")) {
/*  763 */           columnMode = 4;
/*  764 */         } else if (argModes != null && argModes[i].equals("b")) {
/*  765 */           columnMode = 2;
/*  766 */         } else if (argModes != null && argModes[i].equals("t")) {
/*  767 */           columnMode = 5;
/*      */         } 
/*      */         
/*  770 */         tuple[4] = this.connection.encodeString(Integer.toString(columnMode));
/*  771 */         if (allArgTypes != null) {
/*  772 */           argOid = allArgTypes[i].intValue();
/*      */         } else {
/*  774 */           argOid = ((Long)argTypes.get(i)).intValue();
/*      */         } 
/*      */         
/*  777 */         tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(argOid)));
/*  778 */         tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
/*  779 */         tuple[7] = null;
/*  780 */         tuple[8] = null;
/*  781 */         tuple[9] = null;
/*  782 */         tuple[10] = null;
/*  783 */         tuple[11] = this.connection.encodeString(Integer.toString(2));
/*  784 */         tuple[12] = null;
/*  785 */         tuple[17] = this.connection.encodeString(Integer.toString(i + 1));
/*  786 */         tuple[18] = isnullableUnknown;
/*  787 */         tuple[19] = specificName;
/*  788 */         v.add(new Tuple(tuple));
/*      */       } 
/*  790 */       if (returnTypeType.equals("c") || (returnTypeType.equals("p") && argModesArray != null)) {
/*      */         
/*  792 */         String columnsql = "SELECT a.attname,a.atttypid FROM pg_catalog.pg_attribute a  WHERE a.attrelid = " + returnTypeRelid + " AND NOT a.attisdropped AND a.attnum > 0 ORDER BY a.attnum ";
/*  793 */         Statement columnstmt = this.connection.createStatement();
/*  794 */         ResultSet columnrs = columnstmt.executeQuery(columnsql);
/*      */         
/*  796 */         while (columnrs.next()) {
/*  797 */           int argOid = (int)columnrs.getLong("atttypid");
/*  798 */           byte[][] tuple = new byte[columns][];
/*  799 */           tuple[0] = null;
/*  800 */           tuple[1] = schema;
/*  801 */           tuple[2] = procedureName;
/*  802 */           tuple[3] = columnrs.getBytes("attname");
/*  803 */           tuple[4] = this.connection.encodeString(Integer.toString(3));
/*  804 */           tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(argOid)));
/*  805 */           tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
/*  806 */           tuple[7] = null;
/*  807 */           tuple[8] = null;
/*  808 */           tuple[9] = null;
/*  809 */           tuple[10] = null;
/*  810 */           tuple[11] = this.connection.encodeString(Integer.toString(2));
/*  811 */           tuple[12] = null;
/*  812 */           tuple[17] = this.connection.encodeString(Integer.toString(0));
/*  813 */           tuple[18] = isnullableUnknown;
/*  814 */           tuple[19] = specificName;
/*  815 */           v.add(new Tuple(tuple));
/*      */         } 
/*      */         
/*  818 */         columnrs.close();
/*  819 */         columnstmt.close();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   public ResultSet getTables(String catalog, String schemaPattern, String tableNamePattern, String[] types) throws SQLException {
/*  824 */     String useSchemas = "SCHEMAS";
/*  825 */     String select = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM, c.relname AS TABLE_NAME,  CASE n.nspname ~ '^pg_' OR n.nspname = 'information_schema'  WHEN true THEN CASE  WHEN n.nspname = 'pg_catalog' OR n.nspname = 'information_schema' THEN CASE c.relkind   WHEN 'r' THEN 'SYSTEM TABLE'   WHEN 'v' THEN 'SYSTEM VIEW'   WHEN 'i' THEN 'SYSTEM INDEX'   ELSE NULL   END  WHEN n.nspname = 'pg_toast' THEN CASE c.relkind   WHEN 'r' THEN 'SYSTEM TOAST TABLE'   WHEN 'i' THEN 'SYSTEM TOAST INDEX'   ELSE NULL   END  ELSE CASE c.relkind   WHEN 'r' THEN 'TEMPORARY TABLE'   WHEN 'p' THEN 'TEMPORARY TABLE'   WHEN 'i' THEN 'TEMPORARY INDEX'   WHEN 'S' THEN 'TEMPORARY SEQUENCE'   WHEN 'v' THEN 'TEMPORARY VIEW'   ELSE NULL   END  END  WHEN false THEN CASE c.relkind  WHEN 'r' THEN 'TABLE'  WHEN 'p' THEN 'PARTITIONED TABLE'  WHEN 'i' THEN 'INDEX'  WHEN 'S' THEN 'SEQUENCE'  WHEN 'v' THEN 'VIEW'  WHEN 'c' THEN 'TYPE'  WHEN 'f' THEN 'FOREIGN TABLE'  WHEN 'm' THEN 'MATERIALIZED VIEW'  ELSE NULL  END  ELSE NULL  END  AS TABLE_TYPE, d.description AS REMARKS,  '' as TYPE_CAT, '' as TYPE_SCHEM, '' as TYPE_NAME,  '' AS SELF_REFERENCING_COL_NAME, '' AS REF_GENERATION  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c  LEFT JOIN pg_catalog.pg_description d ON (c.oid = d.objoid AND d.objsubid = 0)  LEFT JOIN pg_catalog.pg_class dc ON (d.classoid=dc.oid AND dc.relname='pg_class')  LEFT JOIN pg_catalog.pg_namespace dn ON (dn.oid=dc.relnamespace AND dn.nspname='pg_catalog')  WHERE c.relnamespace = n.oid ";
/*  826 */     if (schemaPattern != null && !schemaPattern.isEmpty()) {
/*  827 */       select = select + " AND n.nspname LIKE " + escapeQuotes(schemaPattern);
/*      */     }
/*      */     
/*  830 */     if (this.connection.getHideUnprivilegedObjects()) {
/*  831 */       select = select + " AND has_table_privilege(c.oid,  'SELECT, INSERT, UPDATE, DELETE, RULE, REFERENCES, TRIGGER')";
/*      */     }
/*      */     
/*  834 */     String orderby = " ORDER BY TABLE_TYPE,TABLE_SCHEM,TABLE_NAME ";
/*  835 */     if (tableNamePattern != null && !tableNamePattern.isEmpty()) {
/*  836 */       select = select + " AND lower(c.relname) LIKE lower(" + escapeQuotes(tableNamePattern) + ")";
/*      */     }
/*      */     
/*  839 */     if (types != null) {
/*  840 */       select = select + " AND (false ";
/*  841 */       StringBuilder orclause = new StringBuilder();
/*  842 */       String[] var9 = types;
/*  843 */       int var10 = types.length;
/*      */       
/*  845 */       for (int var11 = 0; var11 < var10; var11++) {
/*  846 */         String type = var9[var11];
/*  847 */         Map<String, String> clauses = tableTypeClauses.get(type);
/*  848 */         if (clauses != null) {
/*  849 */           String clause = clauses.get(useSchemas);
/*  850 */           orclause.append(" OR ( ").append(clause).append(" ) ");
/*      */         } 
/*      */       } 
/*      */       
/*  854 */       select = select + orclause.toString() + ") ";
/*      */     } 
/*      */     
/*  857 */     String sql = select + orderby;
/*  858 */     return createMetaDataStatement().executeQuery(sql);
/*      */   }
/*      */   
/*      */   public ResultSet getSchemas() throws SQLException {
/*  862 */     return getSchemas((String)null, (String)null);
/*      */   }
/*      */   
/*      */   public ResultSet getSchemas(String catalog, String schemaPattern) throws SQLException {
/*  866 */     String sql = "SELECT nspname AS TABLE_SCHEM, NULL AS TABLE_CATALOG FROM pg_catalog.pg_namespace  WHERE nspname <> 'pg_toast' AND (nspname !~ '^pg_temp_'  OR nspname = (pg_catalog.current_schemas(true))[1]) AND (nspname !~ '^pg_toast_temp_'  OR nspname = replace((pg_catalog.current_schemas(true))[1], 'pg_temp_', 'pg_toast_temp_')) ";
/*  867 */     if (schemaPattern != null && !schemaPattern.isEmpty()) {
/*  868 */       sql = sql + " AND nspname LIKE " + escapeQuotes(schemaPattern);
/*      */     }
/*      */     
/*  871 */     if (this.connection.getHideUnprivilegedObjects()) {
/*  872 */       sql = sql + " AND has_schema_privilege(nspname, 'USAGE, CREATE')";
/*      */     }
/*      */     
/*  875 */     sql = sql + " ORDER BY TABLE_SCHEM";
/*  876 */     return createMetaDataStatement().executeQuery(sql);
/*      */   }
/*      */   
/*      */   public ResultSet getCatalogs() throws SQLException {
/*  880 */     Field[] f = new Field[1];
/*  881 */     List<Tuple> v = new ArrayList<>();
/*  882 */     f[0] = new Field("TABLE_CAT", 1043);
/*  883 */     byte[][] tuple = { this.connection.encodeString(this.connection.getCatalog()) };
/*  884 */     v.add(new Tuple(tuple));
/*  885 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */   
/*      */   public ResultSet getTableTypes() throws SQLException {
/*  889 */     String[] types = (String[])tableTypeClauses.keySet().toArray((Object[])new String[0]);
/*  890 */     Arrays.sort((Object[])types);
/*  891 */     Field[] f = new Field[1];
/*  892 */     List<Tuple> v = new ArrayList<>();
/*  893 */     f[0] = new Field("TABLE_TYPE", 1043);
/*  894 */     String[] var4 = types;
/*  895 */     int var5 = types.length;
/*      */     
/*  897 */     for (int var6 = 0; var6 < var5; var6++) {
/*  898 */       String type = var4[var6];
/*  899 */       byte[][] tuple = { this.connection.encodeString(type) };
/*  900 */       v.add(new Tuple(tuple));
/*      */     } 
/*      */     
/*  903 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */   
/*      */   public ResultSet getColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
/*  907 */     int numberOfFields = 24;
/*  908 */     List<Tuple> v = new ArrayList<>();
/*  909 */     Field[] f = new Field[numberOfFields];
/*  910 */     f[0] = new Field("TABLE_CAT", 1043);
/*  911 */     f[1] = new Field("TABLE_SCHEM", 1043);
/*  912 */     f[2] = new Field("TABLE_NAME", 1043);
/*  913 */     f[3] = new Field("COLUMN_NAME", 1043);
/*  914 */     f[4] = new Field("DATA_TYPE", 21);
/*  915 */     f[5] = new Field("TYPE_NAME", 1043);
/*  916 */     f[6] = new Field("COLUMN_SIZE", 23);
/*  917 */     f[7] = new Field("BUFFER_LENGTH", 1043);
/*  918 */     f[8] = new Field("DECIMAL_DIGITS", 23);
/*  919 */     f[9] = new Field("NUM_PREC_RADIX", 23);
/*  920 */     f[10] = new Field("NULLABLE", 23);
/*  921 */     f[11] = new Field("REMARKS", 1043);
/*  922 */     f[12] = new Field("COLUMN_DEF", 1043);
/*  923 */     f[13] = new Field("SQL_DATA_TYPE", 23);
/*  924 */     f[14] = new Field("SQL_DATETIME_SUB", 23);
/*  925 */     f[15] = new Field("CHAR_OCTET_LENGTH", 1043);
/*  926 */     f[16] = new Field("ORDINAL_POSITION", 23);
/*  927 */     f[17] = new Field("IS_NULLABLE", 1043);
/*  928 */     f[18] = new Field("SCOPE_CATALOG", 1043);
/*  929 */     f[19] = new Field("SCOPE_SCHEMA", 1043);
/*  930 */     f[20] = new Field("SCOPE_TABLE", 1043);
/*  931 */     f[21] = new Field("SOURCE_DATA_TYPE", 21);
/*  932 */     f[22] = new Field("IS_AUTOINCREMENT", 1043);
/*  933 */     f[23] = new Field("IS_GENERATEDCOLUMN", 1043);
/*      */     String sql;
/*  935 */     if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v8_4)) {
/*  936 */       sql = "SELECT * FROM (";
/*      */     } else {
/*  938 */       sql = "";
/*      */     } 
/*      */     
/*  941 */     sql = sql + "SELECT n.nspname,c.relname,a.attname,a.atttypid,a.attnotnull OR (t.typtype = 'd' AND t.typnotnull) AS attnotnull,a.atttypmod,a.attlen,t.typtypmod,";
/*  942 */     if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v8_4)) {
/*  943 */       sql = sql + "row_number() OVER (PARTITION BY a.attrelid ORDER BY a.attnum) AS attnum, ";
/*      */     } else {
/*  945 */       sql = sql + "a.attnum,";
/*      */     } 
/*      */     
/*  948 */     if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v10)) {
/*  949 */       sql = sql + "nullif(a.attidentity, '') as attidentity,";
/*      */     } else {
/*  951 */       sql = sql + "null as attidentity,";
/*      */     } 
/*      */     
/*  954 */     sql = sql + "pg_catalog.pg_get_expr(def.adbin, def.adrelid) AS adsrc,dsc.description,t.typbasetype,t.typtype  FROM pg_catalog.pg_namespace n  JOIN pg_catalog.pg_class c ON (c.relnamespace = n.oid)  JOIN pg_catalog.pg_attribute a ON (a.attrelid=c.oid)  JOIN pg_catalog.pg_type t ON (a.atttypid = t.oid)  LEFT JOIN pg_catalog.pg_attrdef def ON (a.attrelid=def.adrelid AND a.attnum = def.adnum)  LEFT JOIN pg_catalog.pg_description dsc ON (c.oid=dsc.objoid AND a.attnum = dsc.objsubid)  LEFT JOIN pg_catalog.pg_class dc ON (dc.oid=dsc.classoid AND dc.relname='pg_class')  LEFT JOIN pg_catalog.pg_namespace dn ON (dc.relnamespace=dn.oid AND dn.nspname='pg_catalog')  WHERE c.relkind in ('r','p','v','f','m') and a.attnum > 0 AND NOT a.attisdropped ";
/*  955 */     if (schemaPattern != null && !schemaPattern.isEmpty()) {
/*  956 */       sql = sql + " AND n.nspname LIKE " + escapeQuotes(schemaPattern);
/*      */     }
/*      */     
/*  959 */     if (tableNamePattern != null && !tableNamePattern.isEmpty()) {
/*  960 */       sql = sql + " AND c.relname LIKE " + escapeQuotes(tableNamePattern);
/*      */     }
/*      */     
/*  963 */     if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v8_4)) {
/*  964 */       sql = sql + ") c WHERE true ";
/*      */     }
/*      */     
/*  967 */     if (columnNamePattern != null && !columnNamePattern.isEmpty()) {
/*  968 */       sql = sql + " AND attname LIKE " + escapeQuotes(columnNamePattern);
/*      */     }
/*      */     
/*  971 */     sql = sql + " ORDER BY nspname,c.relname,attnum ";
/*  972 */     Statement stmt = this.connection.createStatement();
/*  973 */     ResultSet rs = stmt.executeQuery(sql);
/*      */     
/*  975 */     while (rs.next()) {
/*  976 */       int sqlType, decimalDigits, columnSize; byte[][] tuple = new byte[numberOfFields][];
/*  977 */       int typeOid = (int)rs.getLong("atttypid");
/*  978 */       int typeMod = rs.getInt("atttypmod");
/*  979 */       tuple[0] = null;
/*  980 */       tuple[1] = rs.getBytes("nspname");
/*  981 */       tuple[2] = rs.getBytes("relname");
/*  982 */       tuple[3] = rs.getBytes("attname");
/*  983 */       String typtype = rs.getString("typtype");
/*      */       
/*  985 */       if ("c".equals(typtype)) {
/*  986 */         sqlType = 2002;
/*  987 */       } else if ("d".equals(typtype)) {
/*  988 */         sqlType = 2001;
/*  989 */       } else if ("e".equals(typtype)) {
/*  990 */         sqlType = 12;
/*      */       } else {
/*  992 */         sqlType = this.connection.getTypeInfo().getSQLType(typeOid);
/*      */       } 
/*      */       
/*  995 */       tuple[4] = this.connection.encodeString(Integer.toString(sqlType));
/*  996 */       String pgType = this.connection.getTypeInfo().getPGType(typeOid);
/*  997 */       tuple[5] = this.connection.encodeString(pgType);
/*  998 */       tuple[7] = null;
/*  999 */       String defval = rs.getString("adsrc");
/* 1000 */       if (defval != null) {
/* 1001 */         if (pgType.equals("int4")) {
/* 1002 */           if (defval.contains("nextval(")) {
/* 1003 */             tuple[5] = this.connection.encodeString("serial");
/*      */           }
/* 1005 */         } else if (pgType.equals("int8") && defval.contains("nextval(")) {
/* 1006 */           tuple[5] = this.connection.encodeString("bigserial");
/*      */         } 
/*      */       }
/*      */       
/* 1010 */       String identity = rs.getString("attidentity");
/* 1011 */       int baseTypeOid = (int)rs.getLong("typbasetype");
/*      */ 
/*      */       
/* 1014 */       if (sqlType == 2001) {
/* 1015 */         int typtypmod = rs.getInt("typtypmod");
/* 1016 */         decimalDigits = this.connection.getTypeInfo().getScale(baseTypeOid, typeMod);
/* 1017 */         if (typtypmod == -1) {
/* 1018 */           columnSize = this.connection.getTypeInfo().getPrecision(baseTypeOid, typeMod);
/* 1019 */         } else if (baseTypeOid == 1700) {
/* 1020 */           decimalDigits = this.connection.getTypeInfo().getScale(baseTypeOid, typtypmod);
/* 1021 */           columnSize = this.connection.getTypeInfo().getPrecision(baseTypeOid, typtypmod);
/*      */         } else {
/* 1023 */           columnSize = typtypmod;
/*      */         } 
/*      */       } else {
/* 1026 */         decimalDigits = this.connection.getTypeInfo().getScale(typeOid, typeMod);
/* 1027 */         columnSize = this.connection.getTypeInfo().getPrecision(typeOid, typeMod);
/* 1028 */         if (columnSize == 0) {
/* 1029 */           columnSize = this.connection.getTypeInfo().getDisplaySize(typeOid, typeMod);
/*      */         }
/*      */       } 
/*      */       
/* 1033 */       tuple[6] = this.connection.encodeString(Integer.toString(columnSize));
/* 1034 */       if ((sqlType == 2 || sqlType == 3) && typeMod == -1) {
/* 1035 */         tuple[8] = null;
/*      */       } else {
/* 1037 */         tuple[8] = this.connection.encodeString(Integer.toString(decimalDigits));
/*      */       } 
/*      */       
/* 1040 */       tuple[9] = this.connection.encodeString("10");
/* 1041 */       if (pgType.equals("bit") || pgType.equals("varbit")) {
/* 1042 */         tuple[9] = this.connection.encodeString("2");
/*      */       }
/*      */       
/* 1045 */       tuple[10] = this.connection.encodeString(Integer.toString(rs.getBoolean("attnotnull") ? 0 : 1));
/* 1046 */       tuple[11] = rs.getBytes("description");
/* 1047 */       tuple[12] = rs.getBytes("adsrc");
/* 1048 */       tuple[13] = null;
/* 1049 */       tuple[14] = null;
/* 1050 */       tuple[15] = tuple[6];
/* 1051 */       tuple[16] = this.connection.encodeString(String.valueOf(rs.getInt("attnum")));
/* 1052 */       tuple[17] = this.connection.encodeString(rs.getBoolean("attnotnull") ? "NO" : "YES");
/* 1053 */       tuple[18] = null;
/* 1054 */       tuple[19] = null;
/* 1055 */       tuple[20] = null;
/* 1056 */       tuple[21] = (baseTypeOid == 0) ? null : this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(baseTypeOid)));
/* 1057 */       String autoinc = "NO";
/* 1058 */       if ((defval != null && defval.contains("nextval(")) || identity != null) {
/* 1059 */         autoinc = "YES";
/*      */       }
/*      */       
/* 1062 */       tuple[22] = this.connection.encodeString(autoinc);
/* 1063 */       tuple[23] = this.connection.encodeString("");
/* 1064 */       v.add(new Tuple(tuple));
/*      */     } 
/*      */     
/* 1067 */     rs.close();
/* 1068 */     stmt.close();
/* 1069 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */   
/*      */   public ResultSet getColumnPrivileges(String catalog, String schema, String table, String columnNamePattern) throws SQLException {
/* 1073 */     Field[] f = new Field[8];
/* 1074 */     List<Tuple> v = new ArrayList<>();
/* 1075 */     f[0] = new Field("TABLE_CAT", 1043);
/* 1076 */     f[1] = new Field("TABLE_SCHEM", 1043);
/* 1077 */     f[2] = new Field("TABLE_NAME", 1043);
/* 1078 */     f[3] = new Field("COLUMN_NAME", 1043);
/* 1079 */     f[4] = new Field("GRANTOR", 1043);
/* 1080 */     f[5] = new Field("GRANTEE", 1043);
/* 1081 */     f[6] = new Field("PRIVILEGE", 1043);
/* 1082 */     f[7] = new Field("IS_GRANTABLE", 1043);
/* 1083 */     String sql = "SELECT n.nspname,c.relname,r.rolname,c.relacl, " + (this.connection.haveMinimumServerVersion((Version)ServerVersion.v8_4) ? "a.attacl, " : "") + " a.attname  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c,  pg_catalog.pg_roles r, pg_catalog.pg_attribute a  WHERE c.relnamespace = n.oid  AND c.relowner = r.oid  AND c.oid = a.attrelid  AND c.relkind = 'r'  AND a.attnum > 0 AND NOT a.attisdropped ";
/* 1084 */     if (schema != null && !schema.isEmpty()) {
/* 1085 */       sql = sql + " AND n.nspname = " + escapeQuotes(schema);
/*      */     }
/*      */     
/* 1088 */     if (table != null && !table.isEmpty()) {
/* 1089 */       sql = sql + " AND c.relname = " + escapeQuotes(table);
/*      */     }
/*      */     
/* 1092 */     if (columnNamePattern != null && !columnNamePattern.isEmpty()) {
/* 1093 */       sql = sql + " AND a.attname LIKE " + escapeQuotes(columnNamePattern);
/*      */     }
/*      */     
/* 1096 */     sql = sql + " ORDER BY attname ";
/* 1097 */     Statement stmt = this.connection.createStatement();
/* 1098 */     ResultSet rs = stmt.executeQuery(sql);
/*      */     
/* 1100 */     while (rs.next()) {
/* 1101 */       byte[] schemaName = rs.getBytes("nspname");
/* 1102 */       byte[] tableName = rs.getBytes("relname");
/* 1103 */       byte[] column = rs.getBytes("attname");
/* 1104 */       String owner = rs.getString("rolname");
/* 1105 */       String relAcl = rs.getString("relacl");
/* 1106 */       Map<String, Map<String, List<String[]>>> permissions = parseACL(relAcl, owner);
/* 1107 */       if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v8_4)) {
/* 1108 */         String acl = rs.getString("attacl");
/* 1109 */         Map<String, Map<String, List<String[]>>> relPermissions = parseACL(acl, owner);
/* 1110 */         permissions.putAll(relPermissions);
/*      */       } 
/*      */       
/* 1113 */       String[] permNames = (String[])permissions.keySet().toArray((Object[])new String[0]);
/* 1114 */       Arrays.sort((Object[])permNames);
/* 1115 */       String[] var32 = permNames;
/* 1116 */       int var18 = permNames.length;
/*      */       
/* 1118 */       for (int var19 = 0; var19 < var18; var19++) {
/* 1119 */         String permName = var32[var19];
/* 1120 */         byte[] privilege = this.connection.encodeString(permName);
/* 1121 */         Map<String, List<String[]>> grantees = permissions.get(permName);
/* 1122 */         Iterator<Map.Entry<String, List<String[]>>> var23 = grantees.entrySet().iterator();
/*      */         
/* 1124 */         while (var23.hasNext()) {
/* 1125 */           Map.Entry<String, List<String[]>> userToGrantable = var23.next();
/* 1126 */           List<String[]> grantor = userToGrantable.getValue();
/* 1127 */           String grantee = userToGrantable.getKey();
/* 1128 */           Iterator<String[]> var27 = grantor.iterator();
/*      */           
/* 1130 */           while (var27.hasNext()) {
/* 1131 */             String[] grants = (String[])var27.next();
/* 1132 */             String grantable = owner.equals(grantee) ? "YES" : grants[1];
/* 1133 */             byte[][] tuple = { null, schemaName, tableName, column, this.connection.encodeString(grants[0]), this.connection.encodeString(grantee), privilege, this.connection.encodeString(grantable) };
/* 1134 */             v.add(new Tuple(tuple));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1140 */     rs.close();
/* 1141 */     stmt.close();
/* 1142 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */   
/*      */   public ResultSet getTablePrivileges(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
/* 1146 */     Field[] f = new Field[7];
/* 1147 */     List<Tuple> v = new ArrayList<>();
/* 1148 */     f[0] = new Field("TABLE_CAT", 1043);
/* 1149 */     f[1] = new Field("TABLE_SCHEM", 1043);
/* 1150 */     f[2] = new Field("TABLE_NAME", 1043);
/* 1151 */     f[3] = new Field("GRANTOR", 1043);
/* 1152 */     f[4] = new Field("GRANTEE", 1043);
/* 1153 */     f[5] = new Field("PRIVILEGE", 1043);
/* 1154 */     f[6] = new Field("IS_GRANTABLE", 1043);
/* 1155 */     String sql = "SELECT n.nspname,c.relname,r.rolname,c.relacl  FROM pg_catalog.pg_namespace n, pg_catalog.pg_class c, pg_catalog.pg_roles r  WHERE c.relnamespace = n.oid  AND c.relowner = r.oid  AND c.relkind IN ('r','p') ";
/* 1156 */     if (schemaPattern != null && !schemaPattern.isEmpty()) {
/* 1157 */       sql = sql + " AND n.nspname LIKE " + escapeQuotes(schemaPattern);
/*      */     }
/*      */     
/* 1160 */     if (tableNamePattern != null && !tableNamePattern.isEmpty()) {
/* 1161 */       sql = sql + " AND c.relname LIKE " + escapeQuotes(tableNamePattern);
/*      */     }
/*      */     
/* 1164 */     sql = sql + " ORDER BY nspname, relname ";
/* 1165 */     Statement stmt = this.connection.createStatement();
/* 1166 */     ResultSet rs = stmt.executeQuery(sql);
/*      */     
/* 1168 */     while (rs.next()) {
/* 1169 */       byte[] schema = rs.getBytes("nspname");
/* 1170 */       byte[] table = rs.getBytes("relname");
/* 1171 */       String owner = rs.getString("rolname");
/* 1172 */       String acl = rs.getString("relacl");
/* 1173 */       Map<String, Map<String, List<String[]>>> permissions = parseACL(acl, owner);
/* 1174 */       String[] permNames = (String[])permissions.keySet().toArray((Object[])new String[0]);
/* 1175 */       Arrays.sort((Object[])permNames);
/* 1176 */       String[] var15 = permNames;
/* 1177 */       int var16 = permNames.length;
/*      */       
/* 1179 */       for (int var17 = 0; var17 < var16; var17++) {
/* 1180 */         String permName = var15[var17];
/* 1181 */         byte[] privilege = this.connection.encodeString(permName);
/* 1182 */         Map<String, List<String[]>> grantees = permissions.get(permName);
/* 1183 */         Iterator<Map.Entry<String, List<String[]>>> var21 = grantees.entrySet().iterator();
/*      */         
/* 1185 */         while (var21.hasNext()) {
/* 1186 */           Map.Entry<String, List<String[]>> userToGrantable = var21.next();
/* 1187 */           List<String[]> grants = userToGrantable.getValue();
/* 1188 */           String granteeUser = userToGrantable.getKey();
/* 1189 */           Iterator<String[]> var25 = grants.iterator();
/*      */           
/* 1191 */           while (var25.hasNext()) {
/* 1192 */             String[] grantTuple = (String[])var25.next();
/* 1193 */             String grantor = (grantTuple[0] == null) ? owner : grantTuple[0];
/* 1194 */             String grantable = owner.equals(granteeUser) ? "YES" : grantTuple[1];
/* 1195 */             byte[][] tuple = { null, schema, table, this.connection.encodeString(grantor), this.connection.encodeString(granteeUser), privilege, this.connection.encodeString(grantable) };
/* 1196 */             v.add(new Tuple(tuple));
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */     
/* 1202 */     rs.close();
/* 1203 */     stmt.close();
/* 1204 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */   
/*      */   private static List<String> parseACLArray(String aclString) {
/* 1208 */     List<String> acls = new ArrayList<>();
/* 1209 */     if (aclString != null && !aclString.isEmpty()) {
/* 1210 */       boolean inQuotes = false;
/* 1211 */       int beginIndex = 1;
/* 1212 */       char prevChar = ' ';
/*      */       
/*      */       int i;
/* 1215 */       for (i = beginIndex; i < aclString.length(); i++) {
/* 1216 */         char c = aclString.charAt(i);
/* 1217 */         if (c == '"' && prevChar != '\\') {
/* 1218 */           inQuotes = !inQuotes;
/* 1219 */         } else if (c == ',' && !inQuotes) {
/* 1220 */           acls.add(aclString.substring(beginIndex, i));
/* 1221 */           beginIndex = i + 1;
/*      */         } 
/*      */         
/* 1224 */         prevChar = c;
/*      */       } 
/*      */       
/* 1227 */       acls.add(aclString.substring(beginIndex, aclString.length() - 1));
/*      */       
/* 1229 */       for (i = 0; i < acls.size(); i++) {
/* 1230 */         String acl = acls.get(i);
/* 1231 */         if (acl.startsWith("\"") && acl.endsWith("\"")) {
/* 1232 */           acl = acl.substring(1, acl.length() - 1);
/* 1233 */           acls.set(i, acl);
/*      */         } 
/*      */       } 
/*      */       
/* 1237 */       return acls;
/*      */     } 
/* 1239 */     return acls;
/*      */   }
/*      */ 
/*      */   
/*      */   private static void addACLPrivileges(String acl, Map<String, Map<String, List<String[]>>> privileges) {
/* 1244 */     int equalIndex = acl.lastIndexOf("=");
/* 1245 */     int slashIndex = acl.lastIndexOf("/");
/* 1246 */     if (equalIndex != -1) {
/* 1247 */       String privs, user = acl.substring(0, equalIndex);
/* 1248 */       String grantor = null;
/* 1249 */       if (user.isEmpty()) {
/* 1250 */         user = "PUBLIC";
/*      */       }
/*      */ 
/*      */       
/* 1254 */       if (slashIndex != -1) {
/* 1255 */         privs = acl.substring(equalIndex + 1, slashIndex);
/* 1256 */         grantor = acl.substring(slashIndex + 1, acl.length());
/*      */       } else {
/* 1258 */         privs = acl.substring(equalIndex + 1, acl.length());
/*      */       } 
/*      */       
/* 1261 */       for (int i = 0; i < privs.length(); i++) {
/* 1262 */         char c = privs.charAt(i);
/* 1263 */         if (c != '*') {
/*      */           String grantable, sqlpriv;
/* 1265 */           if (i < privs.length() - 1 && privs.charAt(i + 1) == '*') {
/* 1266 */             grantable = "YES";
/*      */           } else {
/* 1268 */             grantable = "NO";
/*      */           } 
/*      */ 
/*      */           
/* 1272 */           switch (c) {
/*      */             case 'C':
/* 1274 */               sqlpriv = "CREATE";
/*      */               break;
/*      */             case 'D':
/* 1277 */               sqlpriv = "TRUNCATE";
/*      */               break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */             
/*      */             default:
/* 1321 */               sqlpriv = "UNKNOWN";
/*      */               break;
/*      */             case 'R':
/* 1324 */               sqlpriv = "RULE";
/*      */               break;
/*      */             case 'T':
/* 1327 */               sqlpriv = "CREATE TEMP";
/*      */               break;
/*      */             case 'U':
/* 1330 */               sqlpriv = "USAGE";
/*      */               break;
/*      */             case 'X':
/* 1333 */               sqlpriv = "EXECUTE";
/*      */               break;
/*      */             case 'a':
/* 1336 */               sqlpriv = "INSERT";
/*      */               break;
/*      */             case 'd':
/* 1339 */               sqlpriv = "DELETE";
/*      */               break;
/*      */             case 'p':
/*      */             case 'r':
/* 1343 */               sqlpriv = "SELECT";
/*      */               break;
/*      */             case 't':
/* 1346 */               sqlpriv = "TRIGGER";
/*      */               break;
/*      */             case 'w':
/* 1349 */               sqlpriv = "UPDATE";
/*      */               break;
/*      */             case 'x':
/* 1352 */               sqlpriv = "REFERENCES";
/*      */               break;
/*      */           } 
/* 1355 */           Map<String, List<String[]>> usersWithPermission = privileges.get(sqlpriv);
/* 1356 */           String[] grant = { grantor, grantable };
/*      */           
/* 1358 */           if (usersWithPermission == null) {
/* 1359 */             Map<String, List<String[]>> usersWithPermission1 = new HashMap<>();
/* 1360 */             ArrayList<String[]> permissionByGrantor = new ArrayList();
/* 1361 */             permissionByGrantor.add(grant);
/* 1362 */             usersWithPermission.put(user, permissionByGrantor);
/* 1363 */             privileges.put(sqlpriv, usersWithPermission);
/*      */           } else {
/* 1365 */             List<String[]> permissionByGrantor2 = usersWithPermission.get(user);
/* 1366 */             if (permissionByGrantor2 == null) {
/* 1367 */               permissionByGrantor2 = (List)new ArrayList<>();
/* 1368 */               permissionByGrantor2.add(grant);
/* 1369 */               usersWithPermission.put(user, permissionByGrantor2);
/*      */             } else {
/* 1371 */               permissionByGrantor2.add(grant);
/*      */             } 
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public Map<String, Map<String, List<String[]>>> parseACL(String aclArray, String owner) {
/* 1381 */     if (aclArray == null) {
/* 1382 */       String perms = this.connection.haveMinimumServerVersion((Version)ServerVersion.v8_4) ? "arwdDxt" : "arwdxt";
/* 1383 */       aclArray = "{" + owner + "=" + perms + "/" + owner + "}";
/*      */     } 
/*      */     
/* 1386 */     List<String> acls = parseACLArray(aclArray);
/* 1387 */     Map<String, Map<String, List<String[]>>> privileges = new HashMap<>();
/* 1388 */     Iterator<String> var5 = acls.iterator();
/*      */     
/* 1390 */     while (var5.hasNext()) {
/* 1391 */       String acl = var5.next();
/* 1392 */       addACLPrivileges(acl, privileges);
/*      */     } 
/*      */     
/* 1395 */     return privileges;
/*      */   }
/*      */   
/*      */   public ResultSet getBestRowIdentifier(String catalog, String schema, String table, int scope, boolean nullable) throws SQLException {
/* 1399 */     Field[] f = new Field[8];
/* 1400 */     List<Tuple> v = new ArrayList<>();
/* 1401 */     f[0] = new Field("SCOPE", 21);
/* 1402 */     f[1] = new Field("COLUMN_NAME", 1043);
/* 1403 */     f[2] = new Field("DATA_TYPE", 21);
/* 1404 */     f[3] = new Field("TYPE_NAME", 1043);
/* 1405 */     f[4] = new Field("COLUMN_SIZE", 23);
/* 1406 */     f[5] = new Field("BUFFER_LENGTH", 23);
/* 1407 */     f[6] = new Field("DECIMAL_DIGITS", 21);
/* 1408 */     f[7] = new Field("PSEUDO_COLUMN", 21);
/* 1409 */     String sql = "SELECT a.attname, a.atttypid, atttypmod FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_attribute a ON (ct.oid = a.attrelid)   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN (SELECT i.indexrelid, i.indrelid, i.indisprimary,              information_schema._pg_expandarray(i.indkey) AS keys         FROM pg_catalog.pg_index i) i     ON (a.attnum = (i.keys).x AND a.attrelid = i.indrelid) WHERE true ";
/* 1410 */     if (schema != null && !schema.isEmpty()) {
/* 1411 */       sql = sql + " AND n.nspname = " + escapeQuotes(schema);
/*      */     }
/*      */     
/* 1414 */     sql = sql + " AND ct.relname = " + escapeQuotes(table) + " AND i.indisprimary  ORDER BY a.attnum ";
/* 1415 */     Statement stmt = this.connection.createStatement();
/* 1416 */     ResultSet rs = stmt.executeQuery(sql);
/*      */     
/* 1418 */     while (rs.next()) {
/* 1419 */       byte[][] tuple = new byte[8][];
/* 1420 */       int typeOid = (int)rs.getLong("atttypid");
/* 1421 */       int typeMod = rs.getInt("atttypmod");
/* 1422 */       int decimalDigits = this.connection.getTypeInfo().getScale(typeOid, typeMod);
/* 1423 */       int columnSize = this.connection.getTypeInfo().getPrecision(typeOid, typeMod);
/* 1424 */       if (columnSize == 0) {
/* 1425 */         columnSize = this.connection.getTypeInfo().getDisplaySize(typeOid, typeMod);
/*      */       }
/*      */       
/* 1428 */       tuple[0] = this.connection.encodeString(Integer.toString(scope));
/* 1429 */       tuple[1] = rs.getBytes("attname");
/* 1430 */       tuple[2] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(typeOid)));
/* 1431 */       tuple[3] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(typeOid));
/* 1432 */       tuple[4] = this.connection.encodeString(Integer.toString(columnSize));
/* 1433 */       tuple[5] = null;
/* 1434 */       tuple[6] = this.connection.encodeString(Integer.toString(decimalDigits));
/* 1435 */       tuple[7] = this.connection.encodeString(Integer.toString(1));
/* 1436 */       v.add(new Tuple(tuple));
/*      */     } 
/*      */     
/* 1439 */     rs.close();
/* 1440 */     stmt.close();
/* 1441 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */   
/*      */   public ResultSet getVersionColumns(String catalog, String schema, String table) throws SQLException {
/* 1445 */     Field[] f = new Field[8];
/* 1446 */     List<Tuple> v = new ArrayList<>();
/* 1447 */     f[0] = new Field("SCOPE", 21);
/* 1448 */     f[1] = new Field("COLUMN_NAME", 1043);
/* 1449 */     f[2] = new Field("DATA_TYPE", 21);
/* 1450 */     f[3] = new Field("TYPE_NAME", 1043);
/* 1451 */     f[4] = new Field("COLUMN_SIZE", 23);
/* 1452 */     f[5] = new Field("BUFFER_LENGTH", 23);
/* 1453 */     f[6] = new Field("DECIMAL_DIGITS", 21);
/* 1454 */     f[7] = new Field("PSEUDO_COLUMN", 21);
/* 1455 */     byte[][] tuple = { null, this.connection.encodeString("ctid"), this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType("tid"))), this.connection.encodeString("tid"), null, null, null, this.connection.encodeString(Integer.toString(2)) };
/* 1456 */     v.add(new Tuple(tuple));
/* 1457 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */   
/*      */   public ResultSet getPrimaryKeys(String catalog, String schema, String table) throws SQLException {
/* 1461 */     String sql = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM,   ct.relname AS TABLE_NAME, a.attname AS COLUMN_NAME,   (information_schema._pg_expandarray(i.indkey)).n AS KEY_SEQ, ci.relname AS PK_NAME,   information_schema._pg_expandarray(i.indkey) AS KEYS, a.attnum AS A_ATTNUM FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_attribute a ON (ct.oid = a.attrelid)   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN pg_catalog.pg_index i ON ( a.attrelid = i.indrelid)   JOIN pg_catalog.pg_class ci ON (ci.oid = i.indexrelid) WHERE true ";
/* 1462 */     if (schema != null && !schema.isEmpty()) {
/* 1463 */       sql = sql + " AND n.nspname = " + escapeQuotes(schema);
/*      */     }
/*      */     
/* 1466 */     if (table != null && !table.isEmpty()) {
/* 1467 */       sql = sql + " AND ct.relname = " + escapeQuotes(table);
/*      */     }
/*      */     
/* 1470 */     sql = sql + " AND i.indisprimary ";
/* 1471 */     sql = "SELECT        result.TABLE_CAT,        result.TABLE_SCHEM,        result.TABLE_NAME,        result.COLUMN_NAME,        result.KEY_SEQ,        result.PK_NAME FROM      (" + sql + " ) result where  result.A_ATTNUM = (result.KEYS).x ";
/* 1472 */     sql = sql + " ORDER BY result.table_name, result.pk_name, result.key_seq";
/* 1473 */     return createMetaDataStatement().executeQuery(sql);
/*      */   }
/*      */   
/*      */   protected ResultSet getImportedExportedKeys(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
/* 1477 */     String sql = "SELECT NULL::text AS PKTABLE_CAT, pkn.nspname AS PKTABLE_SCHEM, pkc.relname AS PKTABLE_NAME, pka.attname AS PKCOLUMN_NAME, NULL::text AS FKTABLE_CAT, fkn.nspname AS FKTABLE_SCHEM, fkc.relname AS FKTABLE_NAME, fka.attname AS FKCOLUMN_NAME, pos.n AS KEY_SEQ, CASE con.confupdtype  WHEN 'c' THEN 0 WHEN 'n' THEN 2 WHEN 'd' THEN 4 WHEN 'r' THEN 1 WHEN 'p' THEN 1 WHEN 'a' THEN 3 ELSE NULL END AS UPDATE_RULE, CASE con.confdeltype  WHEN 'c' THEN 0 WHEN 'n' THEN 2 WHEN 'd' THEN 4 WHEN 'r' THEN 1 WHEN 'p' THEN 1 WHEN 'a' THEN 3 ELSE NULL END AS DELETE_RULE, con.conname AS FK_NAME, pkic.relname AS PK_NAME, CASE  WHEN con.condeferrable AND con.condeferred THEN 5 WHEN con.condeferrable THEN 6 ELSE 7 END AS DEFERRABILITY  FROM  pg_catalog.pg_namespace pkn, pg_catalog.pg_class pkc, pg_catalog.pg_attribute pka,  pg_catalog.pg_namespace fkn, pg_catalog.pg_class fkc, pg_catalog.pg_attribute fka,  pg_catalog.pg_constraint con,  pg_catalog.generate_series(1, " + getMaxIndexKeys() + ") pos(n),  pg_catalog.pg_class pkic";
/* 1478 */     if (!this.connection.haveMinimumServerVersion((Version)ServerVersion.v9_0)) {
/* 1479 */       sql = sql + ", pg_catalog.pg_depend dep ";
/*      */     }
/*      */     
/* 1482 */     sql = sql + " WHERE pkn.oid = pkc.relnamespace AND pkc.oid = pka.attrelid AND pka.attnum = con.confkey[pos.n] AND con.confrelid = pkc.oid  AND fkn.oid = fkc.relnamespace AND fkc.oid = fka.attrelid AND fka.attnum = con.conkey[pos.n] AND con.conrelid = fkc.oid  AND con.contype = 'f' AND pkic.relkind = 'i' ";
/* 1483 */     if (!this.connection.haveMinimumServerVersion((Version)ServerVersion.v9_0)) {
/* 1484 */       sql = sql + " AND con.oid = dep.objid AND pkic.oid = dep.refobjid AND dep.classid = 'pg_constraint'::regclass::oid AND dep.refclassid = 'pg_class'::regclass::oid ";
/*      */     } else {
/* 1486 */       sql = sql + " AND pkic.oid = con.conindid ";
/*      */     } 
/*      */     
/* 1489 */     if (primarySchema != null && !primarySchema.isEmpty()) {
/* 1490 */       sql = sql + " AND pkn.nspname = " + escapeQuotes(primarySchema);
/*      */     }
/*      */     
/* 1493 */     if (foreignSchema != null && !foreignSchema.isEmpty()) {
/* 1494 */       sql = sql + " AND fkn.nspname = " + escapeQuotes(foreignSchema);
/*      */     }
/*      */     
/* 1497 */     if (primaryTable != null && !primaryTable.isEmpty()) {
/* 1498 */       sql = sql + " AND pkc.relname = " + escapeQuotes(primaryTable);
/*      */     }
/*      */     
/* 1501 */     if (foreignTable != null && !foreignTable.isEmpty()) {
/* 1502 */       sql = sql + " AND fkc.relname = " + escapeQuotes(foreignTable);
/*      */     }
/*      */     
/* 1505 */     if (primaryTable != null) {
/* 1506 */       sql = sql + " ORDER BY fkn.nspname,fkc.relname,con.conname,pos.n";
/*      */     } else {
/* 1508 */       sql = sql + " ORDER BY pkn.nspname,pkc.relname, con.conname,pos.n";
/*      */     } 
/*      */     
/* 1511 */     return createMetaDataStatement().executeQuery(sql);
/*      */   }
/*      */   
/*      */   public ResultSet getImportedKeys(String catalog, String schema, String table) throws SQLException {
/* 1515 */     return getImportedExportedKeys((String)null, (String)null, (String)null, catalog, schema, table);
/*      */   }
/*      */   
/*      */   public ResultSet getExportedKeys(String catalog, String schema, String table) throws SQLException {
/* 1519 */     return getImportedExportedKeys(catalog, schema, table, (String)null, (String)null, (String)null);
/*      */   }
/*      */   
/*      */   public ResultSet getCrossReference(String primaryCatalog, String primarySchema, String primaryTable, String foreignCatalog, String foreignSchema, String foreignTable) throws SQLException {
/* 1523 */     return getImportedExportedKeys(primaryCatalog, primarySchema, primaryTable, foreignCatalog, foreignSchema, foreignTable);
/*      */   }
/*      */   
/*      */   public ResultSet getTypeInfo() throws SQLException {
/* 1527 */     Field[] f = new Field[18];
/* 1528 */     List<Tuple> v = new ArrayList<>();
/* 1529 */     f[0] = new Field("TYPE_NAME", 1043);
/* 1530 */     f[1] = new Field("DATA_TYPE", 21);
/* 1531 */     f[2] = new Field("PRECISION", 23);
/* 1532 */     f[3] = new Field("LITERAL_PREFIX", 1043);
/* 1533 */     f[4] = new Field("LITERAL_SUFFIX", 1043);
/* 1534 */     f[5] = new Field("CREATE_PARAMS", 1043);
/* 1535 */     f[6] = new Field("NULLABLE", 21);
/* 1536 */     f[7] = new Field("CASE_SENSITIVE", 16);
/* 1537 */     f[8] = new Field("SEARCHABLE", 21);
/* 1538 */     f[9] = new Field("UNSIGNED_ATTRIBUTE", 16);
/* 1539 */     f[10] = new Field("FIXED_PREC_SCALE", 16);
/* 1540 */     f[11] = new Field("AUTO_INCREMENT", 16);
/* 1541 */     f[12] = new Field("LOCAL_TYPE_NAME", 1043);
/* 1542 */     f[13] = new Field("MINIMUM_SCALE", 21);
/* 1543 */     f[14] = new Field("MAXIMUM_SCALE", 21);
/* 1544 */     f[15] = new Field("SQL_DATA_TYPE", 23);
/* 1545 */     f[16] = new Field("SQL_DATETIME_SUB", 23);
/* 1546 */     f[17] = new Field("NUM_PREC_RADIX", 23);
/* 1547 */     String sql = "SELECT t.typname,t.oid FROM pg_catalog.pg_type t JOIN pg_catalog.pg_namespace n ON (t.typnamespace = n.oid)  WHERE n.nspname  != 'pg_toast' AND  (t.typrelid = 0 OR (SELECT c.relkind = 'c' FROM pg_catalog.pg_class c WHERE c.oid = t.typrelid))";
/* 1548 */     if (this.connection.getHideUnprivilegedObjects() && this.connection.haveMinimumServerVersion((Version)ServerVersion.v9_2)) {
/* 1549 */       sql = sql + " AND has_type_privilege(t.oid, 'USAGE')";
/*      */     }
/*      */     
/* 1552 */     Statement stmt = this.connection.createStatement();
/* 1553 */     ResultSet rs = stmt.executeQuery(sql);
/* 1554 */     byte[] bZero = this.connection.encodeString("0");
/* 1555 */     byte[] b10 = this.connection.encodeString("10");
/* 1556 */     byte[] bf = this.connection.encodeString("f");
/* 1557 */     byte[] bt = this.connection.encodeString("t");
/* 1558 */     byte[] bliteral = this.connection.encodeString("'");
/* 1559 */     byte[] bNullable = this.connection.encodeString(Integer.toString(1));
/* 1560 */     byte[] bSearchable = this.connection.encodeString(Integer.toString(3));
/* 1561 */     TypeInfo ti = this.connection.getTypeInfo();
/* 1562 */     if (ti instanceof TypeInfoCache) {
/* 1563 */       ((TypeInfoCache)ti).cacheSQLTypes();
/*      */     }
/*      */     
/* 1566 */     while (rs.next()) {
/* 1567 */       byte[][] tuple = new byte[19][];
/* 1568 */       String typname = rs.getString(1);
/* 1569 */       int typeOid = (int)rs.getLong(2);
/* 1570 */       tuple[0] = this.connection.encodeString(typname);
/* 1571 */       int sqlType = this.connection.getTypeInfo().getSQLType(typname);
/* 1572 */       tuple[1] = this.connection.encodeString(Integer.toString(sqlType));
/* 1573 */       tuple[18] = BigInteger.valueOf(sqlType).toByteArray();
/* 1574 */       tuple[2] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getMaximumPrecision(typeOid)));
/* 1575 */       if (this.connection.getTypeInfo().requiresQuotingSqlType(sqlType)) {
/* 1576 */         tuple[3] = bliteral;
/* 1577 */         tuple[4] = bliteral;
/*      */       } 
/*      */       
/* 1580 */       tuple[6] = bNullable;
/* 1581 */       tuple[7] = this.connection.getTypeInfo().isCaseSensitive(typeOid) ? bt : bf;
/* 1582 */       tuple[8] = bSearchable;
/* 1583 */       tuple[9] = this.connection.getTypeInfo().isSigned(typeOid) ? bf : bt;
/* 1584 */       tuple[10] = bf;
/* 1585 */       tuple[11] = bf;
/* 1586 */       tuple[13] = bZero;
/* 1587 */       tuple[14] = (typeOid == 1700) ? this.connection.encodeString("1000") : bZero;
/* 1588 */       tuple[17] = b10;
/* 1589 */       v.add(new Tuple(tuple));
/*      */       
/* 1591 */       if (typname.equals("int4")) {
/* 1592 */         byte[][] tuple1 = (byte[][])tuple.clone();
/* 1593 */         tuple1[0] = this.connection.encodeString("serial");
/* 1594 */         tuple1[11] = bt;
/* 1595 */         v.add(new Tuple(tuple1)); continue;
/* 1596 */       }  if (typname.equals("int8")) {
/* 1597 */         byte[][] tuple1 = (byte[][])tuple.clone();
/* 1598 */         tuple1[0] = this.connection.encodeString("bigserial");
/* 1599 */         tuple1[11] = bt;
/* 1600 */         v.add(new Tuple(tuple1));
/*      */       } 
/*      */     } 
/*      */     
/* 1604 */     rs.close();
/* 1605 */     stmt.close();
/* 1606 */     Collections.sort(v, new Comparator<Tuple>() {
/*      */           public int compare(Tuple o1, Tuple o2) {
/* 1608 */             int i1 = ByteConverter.bytesToInt(o1.get(18));
/* 1609 */             int i2 = ByteConverter.bytesToInt(o2.get(18));
/* 1610 */             return (i1 < i2) ? -1 : ((i1 == i2) ? 0 : 1);
/*      */           }
/*      */         });
/* 1613 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getIndexInfo(String catalog, String schema, String tableName, boolean unique, boolean approximate) throws SQLException {
/* 1618 */     String sql;
                if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v8_3)) {
/* 1619 */       sql = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM,   ct.relname AS TABLE_NAME, NOT i.indisunique AS NON_UNIQUE,   NULL AS INDEX_QUALIFIER, ci.relname AS INDEX_NAME,   CASE i.indisclustered     WHEN true THEN 1    ELSE CASE am.amname       WHEN 'hash' THEN 2      ELSE 3    END   END AS TYPE,   (information_schema._pg_expandarray(i.indkey)).n AS ORDINAL_POSITION,   ci.reltuples AS CARDINALITY,   ci.relpages AS PAGES,   pg_catalog.pg_get_expr(i.indpred, i.indrelid) AS FILTER_CONDITION,   ci.oid AS CI_OID,   i.indoption AS I_INDOPTION, " + (this.connection.haveMinimumServerVersion((Version)ServerVersion.v9_6) ? "  am.amname AS AM_NAME " : "  am.amcanorder AS AM_CANORDER ") + "FROM pg_catalog.pg_class ct   JOIN pg_catalog.pg_namespace n ON (ct.relnamespace = n.oid)   JOIN pg_catalog.pg_index i ON (ct.oid = i.indrelid)   JOIN pg_catalog.pg_class ci ON (ci.oid = i.indexrelid)   JOIN pg_catalog.pg_am am ON (ci.relam = am.oid) WHERE true ";
/* 1620 */       if (schema != null && !schema.isEmpty()) {
/* 1621 */         sql = sql + " AND n.nspname = " + escapeQuotes(schema);
/*      */       }
/*      */       
/* 1624 */       sql = sql + " AND ct.relname = " + escapeQuotes(tableName);
/* 1625 */       if (unique) {
/* 1626 */         sql = sql + " AND i.indisunique ";
/*      */       }
/*      */       
/* 1629 */       sql = "SELECT \t\t tmp.TABLE_CAT,     tmp.TABLE_SCHEM,     tmp.TABLE_NAME,     tmp.NON_UNIQUE,     tmp.INDEX_QUALIFIER,     tmp.INDEX_NAME,     tmp.TYPE,     tmp.ORDINAL_POSITION,     trim(both '\"' from pg_catalog.pg_get_indexdef(tmp.CI_OID, tmp.ORDINAL_POSITION, false)) AS COLUMN_NAME, " + (this.connection.haveMinimumServerVersion((Version)ServerVersion.v9_6) ? "  CASE tmp.AM_NAME     WHEN 'btree' THEN CASE tmp.I_INDOPTION[tmp.ORDINAL_POSITION - 1] & 1       WHEN 1 THEN 'D'       ELSE 'A'     END     ELSE NULL   END AS ASC_OR_DESC, " : "  CASE tmp.AM_CANORDER     WHEN true THEN CASE tmp.I_INDOPTION[tmp.ORDINAL_POSITION - 1] & 1       WHEN 1 THEN 'D'       ELSE 'A'     END     ELSE NULL   END AS ASC_OR_DESC, ") + "    tmp.CARDINALITY,     tmp.PAGES,     tmp.FILTER_CONDITION FROM (" + sql + ") AS tmp";
/*      */     } else {
/* 1631 */       String select = "SELECT NULL AS TABLE_CAT, n.nspname AS TABLE_SCHEM, ";
/* 1632 */       String from = " FROM pg_catalog.pg_namespace n, pg_catalog.pg_class ct, pg_catalog.pg_class ci,  pg_catalog.pg_attribute a, pg_catalog.pg_am am ";
/* 1633 */       String where = " AND n.oid = ct.relnamespace ";
/* 1634 */       from = from + ", pg_catalog.pg_index i ";
/* 1635 */       if (schema != null && !schema.isEmpty()) {
/* 1636 */         where = where + " AND n.nspname = " + escapeQuotes(schema);
/*      */       }
/*      */       
/* 1639 */       sql = select + " ct.relname AS TABLE_NAME, NOT i.indisunique AS NON_UNIQUE, NULL AS INDEX_QUALIFIER, ci.relname AS INDEX_NAME,  CASE i.indisclustered  WHEN true THEN " + '\001' + " ELSE CASE am.amname  WHEN 'hash' THEN " + '\002' + " ELSE " + '\003' + " END  END AS TYPE,  a.attnum AS ORDINAL_POSITION,  CASE WHEN i.indexprs IS NULL THEN a.attname  ELSE pg_catalog.pg_get_indexdef(ci.oid,a.attnum,false) END AS COLUMN_NAME,  NULL AS ASC_OR_DESC,  ci.reltuples AS CARDINALITY,  ci.relpages AS PAGES,  pg_catalog.pg_get_expr(i.indpred, i.indrelid) AS FILTER_CONDITION " + from + " WHERE ct.oid=i.indrelid AND ci.oid=i.indexrelid AND a.attrelid=ci.oid AND ci.relam=am.oid " + where;
/* 1640 */       sql = sql + " AND ct.relname = " + escapeQuotes(tableName);
/* 1641 */       if (unique) {
/* 1642 */         sql = sql + " AND i.indisunique ";
/*      */       }
/*      */     } 
/*      */     
/* 1646 */     sql = sql + " ORDER BY NON_UNIQUE, TYPE, INDEX_NAME, ORDINAL_POSITION ";
/* 1647 */     return createMetaDataStatement().executeQuery(sql);
/*      */   }
/*      */   
/*      */   public boolean supportsResultSetType(int type) throws SQLException {
/* 1651 */     return (type != 1005);
/*      */   }
/*      */   
/*      */   public boolean supportsResultSetConcurrency(int type, int concurrency) throws SQLException {
/* 1655 */     if (type == 1005) {
/* 1656 */       return false;
/*      */     }
/* 1658 */     return (concurrency == 1008) ? true : true;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean ownUpdatesAreVisible(int type) throws SQLException {
/* 1663 */     return true;
/*      */   }
/*      */   
/*      */   public boolean ownDeletesAreVisible(int type) throws SQLException {
/* 1667 */     return true;
/*      */   }
/*      */   
/*      */   public boolean ownInsertsAreVisible(int type) throws SQLException {
/* 1671 */     return true;
/*      */   }
/*      */   
/*      */   public boolean othersUpdatesAreVisible(int type) throws SQLException {
/* 1675 */     return false;
/*      */   }
/*      */   
/*      */   public boolean othersDeletesAreVisible(int i) throws SQLException {
/* 1679 */     return false;
/*      */   }
/*      */   
/*      */   public boolean othersInsertsAreVisible(int type) throws SQLException {
/* 1683 */     return false;
/*      */   }
/*      */   
/*      */   public boolean updatesAreDetected(int type) throws SQLException {
/* 1687 */     return false;
/*      */   }
/*      */   
/*      */   public boolean deletesAreDetected(int i) throws SQLException {
/* 1691 */     return false;
/*      */   }
/*      */   
/*      */   public boolean insertsAreDetected(int type) throws SQLException {
/* 1695 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsBatchUpdates() throws SQLException {
/* 1699 */     return true;
/*      */   }
/*      */   
/*      */   public ResultSet getUDTs(String catalog, String schemaPattern, String typeNamePattern, int[] types) throws SQLException {
/* 1703 */     String sql = "select null as type_cat, n.nspname as type_schem, t.typname as type_name,  null as class_name, CASE WHEN t.typtype='c' then 2002 else 2001 end as data_type, pg_catalog.obj_description(t.oid, 'pg_type')  as remarks, CASE WHEN t.typtype = 'd' then  (select CASE";
/* 1704 */     StringBuilder sqlwhen = new StringBuilder();
/* 1705 */     Iterator<String> i = this.connection.getTypeInfo().getPGTypeNamesWithSQLTypes();
/*      */ 
/*      */     
/* 1708 */     while (i.hasNext()) {
/* 1709 */       String pgType = i.next();
/* 1710 */       int secondQualifier = this.connection.getTypeInfo().getSQLType(pgType);
/* 1711 */       sqlwhen.append(" when typname = ").append(escapeQuotes(pgType)).append(" then ").append(secondQualifier);
/*      */     } 
/*      */     
/* 1714 */     sql = sql + sqlwhen.toString();
/* 1715 */     sql = sql + " else 1111 end from pg_type where oid=t.typbasetype) else null end as base_type from pg_catalog.pg_type t, pg_catalog.pg_namespace n where t.typnamespace = n.oid and n.nspname != 'pg_catalog' and n.nspname != 'pg_toast'";
/* 1716 */     StringBuilder toAdd = new StringBuilder();
/* 1717 */     if (types != null) {
/* 1718 */       toAdd.append(" and (false ");
/* 1719 */       int[] var13 = types;
/* 1720 */       int secondQualifier = types.length;
/*      */       
/* 1722 */       for (int var10 = 0; var10 < secondQualifier; var10++) {
/* 1723 */         int type = var13[var10];
/* 1724 */         switch (type) {
/*      */           case 2001:
/* 1726 */             toAdd.append(" or t.typtype = 'd'");
/*      */             break;
/*      */           case 2002:
/* 1729 */             toAdd.append(" or t.typtype = 'c'");
/*      */             break;
/*      */         } 
/*      */       } 
/* 1733 */       toAdd.append(" ) ");
/*      */     } else {
/* 1735 */       toAdd.append(" and t.typtype IN ('c','d') ");
/*      */     } 
/*      */     
/* 1738 */     if (typeNamePattern != null) {
/* 1739 */       int firstQualifier = typeNamePattern.indexOf('.');
/* 1740 */       int secondQualifier = typeNamePattern.lastIndexOf('.');
/* 1741 */       if (firstQualifier != -1) {
/* 1742 */         if (firstQualifier != secondQualifier) {
/* 1743 */           schemaPattern = typeNamePattern.substring(firstQualifier + 1, secondQualifier);
/*      */         } else {
/* 1745 */           schemaPattern = typeNamePattern.substring(0, firstQualifier);
/*      */         } 
/*      */         
/* 1748 */         typeNamePattern = typeNamePattern.substring(secondQualifier + 1);
/*      */       } 
/*      */       
/* 1751 */       toAdd.append(" and t.typname like ").append(escapeQuotes(typeNamePattern));
/*      */     } 
/*      */     
/* 1754 */     if (schemaPattern != null) {
/* 1755 */       toAdd.append(" and n.nspname like ").append(escapeQuotes(schemaPattern));
/*      */     }
/*      */     
/* 1758 */     sql = sql + toAdd.toString();
/* 1759 */     if (this.connection.getHideUnprivilegedObjects() && this.connection.haveMinimumServerVersion((Version)ServerVersion.v9_2)) {
/* 1760 */       sql = sql + " AND has_type_privilege(t.oid, 'USAGE')";
/*      */     }
/*      */     
/* 1763 */     sql = sql + " order by data_type, type_schem, type_name";
/* 1764 */     return createMetaDataStatement().executeQuery(sql);
/*      */   }
/*      */   
/*      */   public Connection getConnection() throws SQLException {
/* 1768 */     return (Connection)this.connection;
/*      */   }
/*      */   
/*      */   protected Statement createMetaDataStatement() throws SQLException {
/* 1772 */     return this.connection.createStatement(1004, 1007);
/*      */   }
/*      */   
/*      */   public long getMaxLogicalLobSize() throws SQLException {
/* 1776 */     return 0L;
/*      */   }
/*      */   
/*      */   public boolean supportsRefCursors() throws SQLException {
/* 1780 */     return true;
/*      */   }
/*      */   
/*      */   public RowIdLifetime getRowIdLifetime() throws SQLException {
/* 1784 */     throw Driver.notImplemented(getClass(), "getRowIdLifetime()");
/*      */   }
/*      */   
/*      */   public boolean supportsStoredFunctionsUsingCallSyntax() throws SQLException {
/* 1788 */     return true;
/*      */   }
/*      */   
/*      */   public boolean autoCommitFailureClosesAllResultSets() throws SQLException {
/* 1792 */     return false;
/*      */   }
/*      */   
/*      */   public ResultSet getClientInfoProperties() throws SQLException {
/* 1796 */     Field[] f = { new Field("NAME", 1043), new Field("MAX_LEN", 23), new Field("DEFAULT_VALUE", 1043), new Field("DESCRIPTION", 1043) };
/* 1797 */     List<Tuple> v = new ArrayList<>();
/* 1798 */     if (this.connection.haveMinimumServerVersion((Version)ServerVersion.v9_0)) {
/* 1799 */       byte[][] tuple = { this.connection.encodeString("ApplicationName"), this.connection.encodeString(Integer.toString(getMaxNameLength())), this.connection.encodeString(""), this.connection.encodeString("The name of the application currently utilizing the connection.") };
/* 1800 */       v.add(new Tuple(tuple));
/*      */     } 
/*      */     
/* 1803 */     return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */   }
/*      */   
/*      */   public boolean isWrapperFor(Class<?> iface) throws SQLException {
/* 1807 */     return iface.isAssignableFrom(getClass());
/*      */   }
/*      */   
/*      */   public <T> T unwrap(Class<T> iface) throws SQLException {
/* 1811 */     if (iface.isAssignableFrom(getClass())) {
/* 1812 */       return iface.cast(this);
/*      */     }
/* 1814 */     throw new SQLException("Cannot unwrap to " + iface.getName());
/*      */   }
/*      */ 
/*      */   
/*      */   public ResultSet getFunctions(String catalog, String schemaPattern, String functionNamePattern) throws SQLException {
/* 1819 */     boolean pgFuncResultExists = this.connection.haveMinimumServerVersion((Version)ServerVersion.v8_4);
/* 1820 */     String funcTypeSql = "0 ";
/* 1821 */     if (pgFuncResultExists) {
/* 1822 */       funcTypeSql = " CASE    WHEN (format_type(p.prorettype, null) = 'unknown') THEN 0   WHEN      (substring(pg_get_function_result(p.oid) from 0 for 6) = 'TABLE') OR      (substring(pg_get_function_result(p.oid) from 0 for 6) = 'SETOF') THEN 2   ELSE 1 END ";
/*      */     }
/*      */     
/* 1825 */     String sql = "SELECT current_database() AS FUNCTION_CAT, n.nspname AS FUNCTION_SCHEM, p.proname AS FUNCTION_NAME,  d.description AS REMARKS, " + funcTypeSql + " AS FUNCTION_TYPE,  p.proname || '_' || p.oid AS SPECIFIC_NAME FROM pg_catalog.pg_proc p INNER JOIN pg_catalog.pg_namespace n ON p.pronamespace=n.oid LEFT JOIN pg_catalog.pg_description d ON p.oid=d.objoid WHERE true ";
/* 1826 */     if (schemaPattern != null && !schemaPattern.isEmpty()) {
/* 1827 */       sql = sql + " AND n.nspname LIKE " + escapeQuotes(schemaPattern);
/*      */     } else {
/* 1829 */       sql = sql + "and pg_function_is_visible(p.oid)";
/*      */     } 
/*      */     
/* 1832 */     if (functionNamePattern != null && !functionNamePattern.isEmpty()) {
/* 1833 */       sql = sql + " AND p.proname LIKE " + escapeQuotes(functionNamePattern);
/*      */     }
/*      */     
/* 1836 */     if (this.connection.getHideUnprivilegedObjects()) {
/* 1837 */       sql = sql + " AND has_function_privilege(p.oid,'EXECUTE')";
/*      */     }
/*      */     
/* 1840 */     sql = sql + " ORDER BY FUNCTION_SCHEM, FUNCTION_NAME, p.oid::text ";
/* 1841 */     return createMetaDataStatement().executeQuery(sql);
/*      */   }
/*      */   
/*      */   public ResultSet getFunctionColumns(String catalog, String schemaPattern, String functionNamePattern, String columnNamePattern) throws SQLException {
/* 1845 */     int columns = 17;
/* 1846 */     Field[] f = new Field[columns];
/* 1847 */     List<Tuple> v = new ArrayList<>();
/* 1848 */     f[0] = new Field("FUNCTION_CAT", 1043);
/* 1849 */     f[1] = new Field("FUNCTION_SCHEM", 1043);
/* 1850 */     f[2] = new Field("FUNCTION_NAME", 1043);
/* 1851 */     f[3] = new Field("COLUMN_NAME", 1043);
/* 1852 */     f[4] = new Field("COLUMN_TYPE", 21);
/* 1853 */     f[5] = new Field("DATA_TYPE", 21);
/* 1854 */     f[6] = new Field("TYPE_NAME", 1043);
/* 1855 */     f[7] = new Field("PRECISION", 21);
/* 1856 */     f[8] = new Field("LENGTH", 23);
/* 1857 */     f[9] = new Field("SCALE", 21);
/* 1858 */     f[10] = new Field("RADIX", 21);
/* 1859 */     f[11] = new Field("NULLABLE", 21);
/* 1860 */     f[12] = new Field("REMARKS", 1043);
/* 1861 */     f[13] = new Field("CHAR_OCTET_LENGTH", 23);
/* 1862 */     f[14] = new Field("ORDINAL_POSITION", 23);
/* 1863 */     f[15] = new Field("IS_NULLABLE", 1043);
/* 1864 */     f[16] = new Field("SPECIFIC_NAME", 1043);
/* 1865 */     String sql = "SELECT n.nspname,p.proname,p.prorettype,p.proargtypes, t.typtype,t.typrelid,  p.proargnames, p.proargmodes, p.proallargtypes, p.oid  FROM pg_catalog.pg_proc p, pg_catalog.pg_namespace n, pg_catalog.pg_type t  WHERE p.pronamespace=n.oid AND p.prorettype=t.oid ";
/* 1866 */     if (schemaPattern != null && !schemaPattern.isEmpty()) {
/* 1867 */       sql = sql + " AND n.nspname LIKE " + escapeQuotes(schemaPattern);
/*      */     }
/*      */     
/* 1870 */     if (functionNamePattern != null && !functionNamePattern.isEmpty()) {
/* 1871 */       sql = sql + " AND p.proname LIKE " + escapeQuotes(functionNamePattern);
/*      */     }
/*      */     
/* 1874 */     sql = sql + " ORDER BY n.nspname, p.proname, p.oid::text ";
/* 1875 */     byte[] isnullableUnknown = new byte[0];
/* 1876 */     Statement stmt = this.connection.createStatement();
/* 1877 */     ResultSet rs = stmt.executeQuery(sql);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     while (true) {
/* 1888 */       if (!rs.next()) {
/* 1889 */         rs.close();
/* 1890 */         stmt.close();
/* 1891 */         return ((BaseStatement)createMetaDataStatement()).createDriverResultSet(f, v);
/*      */       } 
/*      */       
/* 1894 */       byte[] schema = rs.getBytes("nspname");
/* 1895 */       byte[] functionName = rs.getBytes("proname");
/* 1896 */       byte[] specificName = this.connection.encodeString(rs.getString("proname") + "_" + rs.getString("oid"));
/* 1897 */       int returnType = (int)rs.getLong("prorettype");
/* 1898 */       String returnTypeType = rs.getString("typtype");
/* 1899 */       int returnTypeRelid = (int)rs.getLong("typrelid");
/* 1900 */       String strArgTypes = rs.getString("proargtypes");
/* 1901 */       StringTokenizer st = new StringTokenizer(strArgTypes);
/* 1902 */       ArrayList<Long> argTypes = new ArrayList();
/*      */       
/* 1904 */       while (st.hasMoreTokens()) {
/* 1905 */         argTypes.add(Long.valueOf(st.nextToken()));
/*      */       }
/*      */       
/* 1908 */       String[] argNames = null;
/* 1909 */       Array argNamesArray = rs.getArray("proargnames");
/* 1910 */       if (argNamesArray != null) {
/* 1911 */         argNames = (String[])argNamesArray.getArray();
/*      */       }
/*      */       
/* 1914 */       String[] argModes = null;
/* 1915 */       Array argModesArray = rs.getArray("proargmodes");
/* 1916 */       if (argModesArray != null) {
/* 1917 */         argModes = (String[])argModesArray.getArray();
/*      */       }
/*      */       
/* 1920 */       int numArgs = argTypes.size();
/* 1921 */       Long[] allArgTypes = null;
/* 1922 */       Array allArgTypesArray = rs.getArray("proallargtypes");
/* 1923 */       if (allArgTypesArray != null) {
/* 1924 */         allArgTypes = (Long[])allArgTypesArray.getArray();
/* 1925 */         numArgs = allArgTypes.length;
/*      */       } 
/*      */       
/* 1928 */       if (returnTypeType.equals("b") || returnTypeType.equals("d") || returnTypeType.equals("e") || (returnTypeType.equals("p") && argModesArray == null)) {
/* 1929 */         byte[][] tuple = new byte[columns][];
/* 1930 */         tuple[0] = null;
/* 1931 */         tuple[1] = schema;
/* 1932 */         tuple[2] = functionName;
/* 1933 */         tuple[3] = this.connection.encodeString("returnValue");
/* 1934 */         tuple[4] = this.connection.encodeString(Integer.toString(4));
/* 1935 */         tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(returnType)));
/* 1936 */         tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(returnType));
/* 1937 */         tuple[7] = null;
/* 1938 */         tuple[8] = null;
/* 1939 */         tuple[9] = null;
/* 1940 */         tuple[10] = null;
/* 1941 */         tuple[11] = this.connection.encodeString(Integer.toString(2));
/* 1942 */         tuple[12] = null;
/* 1943 */         tuple[14] = this.connection.encodeString(Integer.toString(0));
/* 1944 */         tuple[15] = isnullableUnknown;
/* 1945 */         tuple[16] = specificName;
/* 1946 */         v.add(new Tuple(tuple));
/*      */       } 
/*      */       
/* 1949 */       for (int i = 0; i < numArgs; i++) {
/* 1950 */         int argOid; byte[][] tuple = new byte[columns][];
/* 1951 */         tuple[0] = null;
/* 1952 */         tuple[1] = schema;
/* 1953 */         tuple[2] = functionName;
/* 1954 */         if (argNames != null) {
/* 1955 */           tuple[3] = this.connection.encodeString(argNames[i]);
/*      */         } else {
/* 1957 */           tuple[3] = this.connection.encodeString("$" + (i + 1));
/*      */         } 
/*      */         
/* 1960 */         int columnMode = 1;
/* 1961 */         if (argModes != null && argModes[i] != null) {
/* 1962 */           if (argModes[i].equals("o")) {
/* 1963 */             columnMode = 3;
/* 1964 */           } else if (argModes[i].equals("b")) {
/* 1965 */             columnMode = 2;
/* 1966 */           } else if (argModes[i].equals("t")) {
/* 1967 */             columnMode = 4;
/*      */           } 
/*      */         }
/*      */         
/* 1971 */         tuple[4] = this.connection.encodeString(Integer.toString(columnMode));
/* 1972 */         if (allArgTypes != null) {
/* 1973 */           argOid = allArgTypes[i].intValue();
/*      */         } else {
/* 1975 */           argOid = ((Long)argTypes.get(i)).intValue();
/*      */         } 
/*      */         
/* 1978 */         tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(argOid)));
/* 1979 */         tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
/* 1980 */         tuple[7] = null;
/* 1981 */         tuple[8] = null;
/* 1982 */         tuple[9] = null;
/* 1983 */         tuple[10] = null;
/* 1984 */         tuple[11] = this.connection.encodeString(Integer.toString(2));
/* 1985 */         tuple[12] = null;
/* 1986 */         tuple[14] = this.connection.encodeString(Integer.toString(i + 1));
/* 1987 */         tuple[15] = isnullableUnknown;
/* 1988 */         tuple[16] = specificName;
/* 1989 */         v.add(new Tuple(tuple));
/*      */       } 
/* 1991 */       if (returnTypeType.equals("c") || (returnTypeType.equals("p") && argModesArray != null)) {
/*      */         
/* 1993 */         String columnsql = "SELECT a.attname,a.atttypid FROM pg_catalog.pg_attribute a  WHERE a.attrelid = " + returnTypeRelid + " AND NOT a.attisdropped AND a.attnum > 0 ORDER BY a.attnum ";
/* 1994 */         Statement columnstmt = this.connection.createStatement();
/* 1995 */         ResultSet columnrs = columnstmt.executeQuery(columnsql);
/*      */         
/* 1997 */         while (columnrs.next()) {
/* 1998 */           int argOid = (int)columnrs.getLong("atttypid");
/* 1999 */           byte[][] tuple = new byte[columns][];
/* 2000 */           tuple[0] = null;
/* 2001 */           tuple[1] = schema;
/* 2002 */           tuple[2] = functionName;
/* 2003 */           tuple[3] = columnrs.getBytes("attname");
/* 2004 */           tuple[4] = this.connection.encodeString(Integer.toString(5));
/* 2005 */           tuple[5] = this.connection.encodeString(Integer.toString(this.connection.getTypeInfo().getSQLType(argOid)));
/* 2006 */           tuple[6] = this.connection.encodeString(this.connection.getTypeInfo().getPGType(argOid));
/* 2007 */           tuple[7] = null;
/* 2008 */           tuple[8] = null;
/* 2009 */           tuple[9] = null;
/* 2010 */           tuple[10] = null;
/* 2011 */           tuple[11] = this.connection.encodeString(Integer.toString(2));
/* 2012 */           tuple[12] = null;
/* 2013 */           tuple[14] = this.connection.encodeString(Integer.toString(0));
/* 2014 */           tuple[15] = isnullableUnknown;
/* 2015 */           tuple[16] = specificName;
/* 2016 */           v.add(new Tuple(tuple));
/*      */         } 
/*      */         
/* 2019 */         columnrs.close();
/* 2020 */         columnstmt.close();
/*      */       } 
/*      */     } 
/*      */   }
/*      */   public ResultSet getPseudoColumns(String catalog, String schemaPattern, String tableNamePattern, String columnNamePattern) throws SQLException {
/* 2025 */     throw Driver.notImplemented(getClass(), "getPseudoColumns(String, String, String, String)");
/*      */   }
/*      */   
/*      */   public boolean generatedKeyAlwaysReturned() throws SQLException {
/* 2029 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsSavepoints() throws SQLException {
/* 2033 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsNamedParameters() throws SQLException {
/* 2037 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsMultipleOpenResults() throws SQLException {
/* 2041 */     return false;
/*      */   }
/*      */   
/*      */   public boolean supportsGetGeneratedKeys() throws SQLException {
/* 2045 */     return true;
/*      */   }
/*      */   
/*      */   public ResultSet getSuperTypes(String catalog, String schemaPattern, String typeNamePattern) throws SQLException {
/* 2049 */     throw Driver.notImplemented(getClass(), "getSuperTypes(String,String,String)");
/*      */   }
/*      */   
/*      */   public ResultSet getSuperTables(String catalog, String schemaPattern, String tableNamePattern) throws SQLException {
/* 2053 */     throw Driver.notImplemented(getClass(), "getSuperTables(String,String,String,String)");
/*      */   }
/*      */   
/*      */   public ResultSet getAttributes(String catalog, String schemaPattern, String typeNamePattern, String attributeNamePattern) throws SQLException {
/* 2057 */     throw Driver.notImplemented(getClass(), "getAttributes(String,String,String,String)");
/*      */   }
/*      */   
/*      */   public boolean supportsResultSetHoldability(int holdability) throws SQLException {
/* 2061 */     return true;
/*      */   }
/*      */   
/*      */   public int getResultSetHoldability() throws SQLException {
/* 2065 */     return 1;
/*      */   }
/*      */   
/*      */   public int getDatabaseMajorVersion() throws SQLException {
/* 2069 */     return this.connection.getServerMajorVersion();
/*      */   }
/*      */   
/*      */   public int getDatabaseMinorVersion() throws SQLException {
/* 2073 */     return this.connection.getServerMinorVersion();
/*      */   }
/*      */   
/*      */   public int getJDBCMajorVersion() {
/* 2077 */     return 4;
/*      */   }
/*      */   
/*      */   public int getJDBCMinorVersion() {
/* 2081 */     return 2;
/*      */   }
/*      */   
/*      */   public int getSQLStateType() throws SQLException {
/* 2085 */     return 2;
/*      */   }
/*      */   
/*      */   public boolean locatorsUpdateCopy() throws SQLException {
/* 2089 */     return true;
/*      */   }
/*      */   
/*      */   public boolean supportsStatementPooling() throws SQLException {
/* 2093 */     return false;
/*      */   }
/*      */   
/*      */   static {
/* 2097 */     Map<String, String> ht = new HashMap<>();
/* 2098 */     tableTypeClauses.put("TABLE", ht);
/* 2099 */     ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
/* 2100 */     ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname !~ '^pg_'");
/* 2101 */     ht = new HashMap<>();
/* 2102 */     tableTypeClauses.put("PARTITIONED TABLE", ht);
/* 2103 */     ht.put("SCHEMAS", "c.relkind = 'p' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
/* 2104 */     ht.put("NOSCHEMAS", "c.relkind = 'p' AND c.relname !~ '^pg_'");
/* 2105 */     ht = new HashMap<>();
/* 2106 */     tableTypeClauses.put("VIEW", ht);
/* 2107 */     ht.put("SCHEMAS", "c.relkind = 'v' AND n.nspname <> 'pg_catalog' AND n.nspname <> 'information_schema'");
/* 2108 */     ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname !~ '^pg_'");
/* 2109 */     ht = new HashMap<>();
/* 2110 */     tableTypeClauses.put("INDEX", ht);
/* 2111 */     ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
/* 2112 */     ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname !~ '^pg_'");
/* 2113 */     ht = new HashMap<>();
/* 2114 */     tableTypeClauses.put("SEQUENCE", ht);
/* 2115 */     ht.put("SCHEMAS", "c.relkind = 'S'");
/* 2116 */     ht.put("NOSCHEMAS", "c.relkind = 'S'");
/* 2117 */     ht = new HashMap<>();
/* 2118 */     tableTypeClauses.put("TYPE", ht);
/* 2119 */     ht.put("SCHEMAS", "c.relkind = 'c' AND n.nspname !~ '^pg_' AND n.nspname <> 'information_schema'");
/* 2120 */     ht.put("NOSCHEMAS", "c.relkind = 'c' AND c.relname !~ '^pg_'");
/* 2121 */     ht = new HashMap<>();
/* 2122 */     tableTypeClauses.put("SYSTEM TABLE", ht);
/* 2123 */     ht.put("SCHEMAS", "c.relkind = 'r' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema')");
/* 2124 */     ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname ~ '^pg_' AND c.relname !~ '^pg_toast_' AND c.relname !~ '^pg_temp_'");
/* 2125 */     ht = new HashMap<>();
/* 2126 */     tableTypeClauses.put("SYSTEM TOAST TABLE", ht);
/* 2127 */     ht.put("SCHEMAS", "c.relkind = 'r' AND n.nspname = 'pg_toast'");
/* 2128 */     ht.put("NOSCHEMAS", "c.relkind = 'r' AND c.relname ~ '^pg_toast_'");
/* 2129 */     ht = new HashMap<>();
/* 2130 */     tableTypeClauses.put("SYSTEM TOAST INDEX", ht);
/* 2131 */     ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname = 'pg_toast'");
/* 2132 */     ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname ~ '^pg_toast_'");
/* 2133 */     ht = new HashMap<>();
/* 2134 */     tableTypeClauses.put("SYSTEM VIEW", ht);
/* 2135 */     ht.put("SCHEMAS", "c.relkind = 'v' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema') ");
/* 2136 */     ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_'");
/* 2137 */     ht = new HashMap<>();
/* 2138 */     tableTypeClauses.put("SYSTEM INDEX", ht);
/* 2139 */     ht.put("SCHEMAS", "c.relkind = 'i' AND (n.nspname = 'pg_catalog' OR n.nspname = 'information_schema') ");
/* 2140 */     ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_' AND c.relname !~ '^pg_toast_' AND c.relname !~ '^pg_temp_'");
/* 2141 */     ht = new HashMap<>();
/* 2142 */     tableTypeClauses.put("TEMPORARY TABLE", ht);
/* 2143 */     ht.put("SCHEMAS", "c.relkind IN ('r','p') AND n.nspname ~ '^pg_temp_' ");
/* 2144 */     ht.put("NOSCHEMAS", "c.relkind IN ('r','p') AND c.relname ~ '^pg_temp_' ");
/* 2145 */     ht = new HashMap<>();
/* 2146 */     tableTypeClauses.put("TEMPORARY INDEX", ht);
/* 2147 */     ht.put("SCHEMAS", "c.relkind = 'i' AND n.nspname ~ '^pg_temp_' ");
/* 2148 */     ht.put("NOSCHEMAS", "c.relkind = 'i' AND c.relname ~ '^pg_temp_' ");
/* 2149 */     ht = new HashMap<>();
/* 2150 */     tableTypeClauses.put("TEMPORARY VIEW", ht);
/* 2151 */     ht.put("SCHEMAS", "c.relkind = 'v' AND n.nspname ~ '^pg_temp_' ");
/* 2152 */     ht.put("NOSCHEMAS", "c.relkind = 'v' AND c.relname ~ '^pg_temp_' ");
/* 2153 */     ht = new HashMap<>();
/* 2154 */     tableTypeClauses.put("TEMPORARY SEQUENCE", ht);
/* 2155 */     ht.put("SCHEMAS", "c.relkind = 'S' AND n.nspname ~ '^pg_temp_' ");
/* 2156 */     ht.put("NOSCHEMAS", "c.relkind = 'S' AND c.relname ~ '^pg_temp_' ");
/* 2157 */     ht = new HashMap<>();
/* 2158 */     tableTypeClauses.put("FOREIGN TABLE", ht);
/* 2159 */     ht.put("SCHEMAS", "c.relkind = 'f'");
/* 2160 */     ht.put("NOSCHEMAS", "c.relkind = 'f'");
/* 2161 */     ht = new HashMap<>();
/* 2162 */     tableTypeClauses.put("MATERIALIZED VIEW", ht);
/* 2163 */     ht.put("SCHEMAS", "c.relkind = 'm'");
/* 2164 */     ht.put("NOSCHEMAS", "c.relkind = 'm'");
/*      */   }
/*      */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/com/highgo/jdbc/jdbc/PgDatabaseMetaData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */