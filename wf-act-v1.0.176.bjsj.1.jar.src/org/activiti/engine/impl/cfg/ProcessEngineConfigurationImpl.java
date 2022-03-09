/*      */ package org.activiti.engine.impl.cfg;
/*      */ import com.fasterxml.jackson.databind.ObjectMapper;
/*      */ import java.io.InputStream;
/*      */ import java.io.Reader;
/*      */ import java.net.URL;
/*      */ import java.sql.Connection;
/*      */ import java.sql.DatabaseMetaData;
/*      */ import java.sql.SQLException;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Collection;
/*      */ import java.util.Comparator;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Properties;
/*      */ import java.util.ServiceLoader;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.BlockingQueue;
/*      */ import java.util.concurrent.ConcurrentMap;
/*      */ import javax.sql.DataSource;
/*      */ import javax.xml.namespace.QName;
/*      */ import org.activiti.bpmn.model.BpmnModel;
/*      */ import org.activiti.engine.ActivitiException;
/*      */ import org.activiti.engine.DynamicBpmnService;
/*      */ import org.activiti.engine.FormService;
/*      */ import org.activiti.engine.HistoryService;
/*      */ import org.activiti.engine.IdentityService;
/*      */ import org.activiti.engine.ManagementService;
/*      */ import org.activiti.engine.ProcessEngine;
/*      */ import org.activiti.engine.ProcessEngineConfiguration;
/*      */ import org.activiti.engine.RepositoryService;
/*      */ import org.activiti.engine.RuntimeService;
/*      */ import org.activiti.engine.TaskService;
/*      */ import org.activiti.engine.cfg.ProcessEngineConfigurator;
/*      */ import org.activiti.engine.delegate.event.ActivitiEventDispatcher;
/*      */ import org.activiti.engine.delegate.event.ActivitiEventListener;
/*      */ import org.activiti.engine.delegate.event.ActivitiEventType;
/*      */ import org.activiti.engine.form.AbstractFormType;
/*      */ import org.activiti.engine.impl.asyncexecutor.DefaultAsyncJobExecutor;
/*      */ import org.activiti.engine.impl.asyncexecutor.ExecuteAsyncRunnableFactory;
/*      */ import org.activiti.engine.impl.bpmn.deployer.BpmnDeployer;
/*      */ import org.activiti.engine.impl.bpmn.parser.BpmnParseHandlers;
/*      */ import org.activiti.engine.impl.bpmn.parser.BpmnParser;
/*      */ import org.activiti.engine.impl.bpmn.parser.factory.AbstractBehaviorFactory;
/*      */ import org.activiti.engine.impl.bpmn.parser.factory.ActivityBehaviorFactory;
/*      */ import org.activiti.engine.impl.bpmn.parser.factory.DefaultActivityBehaviorFactory;
/*      */ import org.activiti.engine.impl.bpmn.parser.factory.DefaultListenerFactory;
/*      */ import org.activiti.engine.impl.bpmn.parser.factory.ListenerFactory;
/*      */ import org.activiti.engine.impl.calendar.BusinessCalendar;
/*      */ import org.activiti.engine.impl.calendar.BusinessCalendarManager;
/*      */ import org.activiti.engine.impl.calendar.CycleBusinessCalendar;
/*      */ import org.activiti.engine.impl.calendar.DurationBusinessCalendar;
/*      */ import org.activiti.engine.impl.calendar.MapBusinessCalendarManager;
/*      */ import org.activiti.engine.impl.db.DbIdGenerator;
/*      */ import org.activiti.engine.impl.db.DbSqlSessionFactory;
/*      */ import org.activiti.engine.impl.el.ExpressionManager;
/*      */ import org.activiti.engine.impl.event.CompensationEventHandler;
/*      */ import org.activiti.engine.impl.event.EventHandler;
/*      */ import org.activiti.engine.impl.event.MessageEventHandler;
/*      */ import org.activiti.engine.impl.event.SignalEventHandler;
/*      */ import org.activiti.engine.impl.form.FormEngine;
/*      */ import org.activiti.engine.impl.form.FormTypes;
/*      */ import org.activiti.engine.impl.form.JuelFormEngine;
/*      */ import org.activiti.engine.impl.interceptor.CommandConfig;
/*      */ import org.activiti.engine.impl.interceptor.CommandContextFactory;
/*      */ import org.activiti.engine.impl.interceptor.CommandExecutor;
/*      */ import org.activiti.engine.impl.interceptor.CommandInterceptor;
/*      */ import org.activiti.engine.impl.interceptor.DelegateInterceptor;
/*      */ import org.activiti.engine.impl.interceptor.SessionFactory;
/*      */ import org.activiti.engine.impl.jobexecutor.AsyncContinuationJobHandler;
/*      */ import org.activiti.engine.impl.jobexecutor.FailedJobCommandFactory;
/*      */ import org.activiti.engine.impl.jobexecutor.JobHandler;
/*      */ import org.activiti.engine.impl.jobexecutor.ProcessEventJobHandler;
/*      */ import org.activiti.engine.impl.jobexecutor.RejectedJobsHandler;
/*      */ import org.activiti.engine.impl.jobexecutor.TimerActivateProcessDefinitionHandler;
/*      */ import org.activiti.engine.impl.jobexecutor.TimerCatchIntermediateEventJobHandler;
/*      */ import org.activiti.engine.impl.jobexecutor.TimerExecuteNestedActivityJobHandler;
/*      */ import org.activiti.engine.impl.jobexecutor.TimerStartEventJobHandler;
/*      */ import org.activiti.engine.impl.jobexecutor.TimerSuspendProcessDefinitionHandler;
/*      */ import org.activiti.engine.impl.persistence.GenericManagerFactory;
/*      */ import org.activiti.engine.impl.persistence.deploy.DefaultDeploymentCache;
/*      */ import org.activiti.engine.impl.persistence.deploy.Deployer;
/*      */ import org.activiti.engine.impl.persistence.deploy.DeploymentCache;
/*      */ import org.activiti.engine.impl.persistence.deploy.DeploymentManager;
/*      */ import org.activiti.engine.impl.persistence.deploy.ProcessDefinitionInfoCache;
/*      */ import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
/*      */ import org.activiti.engine.impl.scripting.ResolverFactory;
/*      */ import org.activiti.engine.impl.scripting.ScriptingEngines;
/*      */ import org.activiti.engine.impl.util.ReflectUtil;
/*      */ import org.activiti.engine.impl.variable.CustomObjectType;
/*      */ import org.activiti.engine.impl.variable.JPAEntityVariableType;
/*      */ import org.activiti.engine.impl.variable.VariableType;
/*      */ import org.activiti.engine.impl.variable.VariableTypes;
/*      */ import org.activiti.engine.parse.BpmnParseHandler;
/*      */ import org.activiti.engine.runtime.ClockReader;
/*      */ import org.activiti.validation.ProcessValidator;
/*      */ import org.apache.ibatis.builder.xml.XMLConfigBuilder;
/*      */ import org.apache.ibatis.builder.xml.XMLMapperBuilder;
/*      */ import org.apache.ibatis.datasource.pooled.PooledDataSource;
/*      */ import org.apache.ibatis.mapping.Environment;
/*      */ import org.apache.ibatis.session.Configuration;
/*      */ import org.apache.ibatis.session.SqlSessionFactory;
/*      */ import org.apache.ibatis.transaction.TransactionFactory;
/*      */ 
/*      */ public abstract class ProcessEngineConfigurationImpl extends ProcessEngineConfiguration {
/*  106 */   private static Logger log = LoggerFactory.getLogger(ProcessEngineConfigurationImpl.class);
/*      */   
/*      */   public static final int DEFAULT_GENERIC_MAX_LENGTH_STRING = 4000;
/*      */   
/*      */   public static final int DEFAULT_ORACLE_MAX_LENGTH_STRING = 2000;
/*      */   
/*      */   public static final String DB_SCHEMA_UPDATE_CREATE = "create";
/*      */   
/*      */   public static final String DB_SCHEMA_UPDATE_DROP_CREATE = "drop-create";
/*      */   
/*      */   public static final String DEFAULT_WS_SYNC_FACTORY = "org.activiti.engine.impl.webservice.CxfWebServiceClientFactory";
/*      */   
/*      */   public static final String DEFAULT_MYBATIS_MAPPING_FILE = "org/activiti/db/mapping/mappings.xml";
/*      */   
/*  120 */   protected RepositoryService repositoryService = (RepositoryService)new RepositoryServiceImpl();
/*  121 */   protected RuntimeService runtimeService = (RuntimeService)new RuntimeServiceImpl();
/*  122 */   protected HistoryService historyService = (HistoryService)new HistoryServiceImpl(this);
/*  123 */   protected IdentityService identityService = (IdentityService)new IdentityServiceImpl();
/*  124 */   protected TaskService taskService = (TaskService)new TaskServiceImpl(this);
/*  125 */   protected FormService formService = (FormService)new FormServiceImpl();
/*  126 */   protected ManagementService managementService = (ManagementService)new ManagementServiceImpl();
/*  127 */   protected DynamicBpmnService dynamicBpmnService = (DynamicBpmnService)new DynamicBpmnServiceImpl(this);
/*      */ 
/*      */   
/*      */   protected CommandConfig defaultCommandConfig;
/*      */ 
/*      */   
/*      */   protected CommandConfig schemaCommandConfig;
/*      */ 
/*      */   
/*      */   protected CommandInterceptor commandInvoker;
/*      */   
/*      */   protected List<CommandInterceptor> customPreCommandInterceptors;
/*      */   
/*      */   protected List<CommandInterceptor> customPostCommandInterceptors;
/*      */   
/*      */   protected List<CommandInterceptor> commandInterceptors;
/*      */   
/*      */   protected CommandExecutor commandExecutor;
/*      */   
/*      */   protected List<SessionFactory> customSessionFactories;
/*      */   
/*      */   protected DbSqlSessionFactory dbSqlSessionFactory;
/*      */   
/*      */   protected Map<Class<?>, SessionFactory> sessionFactories;
/*      */   
/*      */   protected boolean enableConfiguratorServiceLoader = true;
/*      */   
/*      */   protected List<ProcessEngineConfigurator> configurators;
/*      */   
/*      */   protected List<ProcessEngineConfigurator> allConfigurators;
/*      */   
/*      */   protected BpmnDeployer bpmnDeployer;
/*      */   
/*      */   protected BpmnParser bpmnParser;
/*      */   
/*      */   protected List<Deployer> customPreDeployers;
/*      */   
/*      */   protected List<Deployer> customPostDeployers;
/*      */   
/*      */   protected List<Deployer> deployers;
/*      */   
/*      */   protected DeploymentManager deploymentManager;
/*      */   
/*  170 */   protected int processDefinitionCacheLimit = -1;
/*      */   protected DeploymentCache<ProcessDefinitionEntity> processDefinitionCache;
/*  172 */   protected int bpmnModelCacheLimit = -1;
/*      */   protected DeploymentCache<BpmnModel> bpmnModelCache;
/*  174 */   protected int processDefinitionInfoCacheLimit = -1;
/*      */   
/*      */   protected ProcessDefinitionInfoCache processDefinitionInfoCache;
/*  177 */   protected int knowledgeBaseCacheLimit = -1;
/*      */ 
/*      */ 
/*      */   
/*      */   protected DeploymentCache<Object> knowledgeBaseCache;
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<JobHandler> customJobHandlers;
/*      */ 
/*      */   
/*      */   protected Map<String, JobHandler> jobHandlers;
/*      */ 
/*      */   
/*  191 */   protected int asyncExecutorCorePoolSize = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  197 */   protected int asyncExecutorMaxPoolSize = 10;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  207 */   protected long asyncExecutorThreadKeepAliveTime = 5000L;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  213 */   protected int asyncExecutorThreadPoolQueueSize = 100;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected BlockingQueue<Runnable> asyncExecutorThreadPoolQueue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  233 */   protected long asyncExecutorSecondsToWaitOnShutdown = 60L;
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
/*  244 */   protected int asyncExecutorMaxTimerJobsPerAcquisition = 1;
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
/*  255 */   protected int asyncExecutorMaxAsyncJobsDuePerAcquisition = 1;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  264 */   protected int asyncExecutorDefaultTimerJobAcquireWaitTime = 10000;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  273 */   protected int asyncExecutorDefaultAsyncJobAcquireWaitTime = 10000;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  279 */   protected int asyncExecutorDefaultQueueSizeFullWaitTime = 0;
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
/*      */   protected String asyncExecutorLockOwner;
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
/*  302 */   protected int asyncExecutorTimerLockTimeInMillis = 300000;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  312 */   protected int asyncExecutorAsyncJobLockTimeInMillis = 300000;
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
/*  323 */   protected int asyncExecutorLockRetryWaitTimeInMillis = 500;
/*      */   
/*      */   protected ExecuteAsyncRunnableFactory asyncExecutorExecuteAsyncRunnableFactory;
/*      */   
/*      */   protected SqlSessionFactory sqlSessionFactory;
/*      */   
/*      */   protected TransactionFactory transactionFactory;
/*      */   
/*      */   protected Set<Class<?>> customMybatisMappers;
/*      */   
/*      */   protected Set<String> customMybatisXMLMappers;
/*      */   
/*      */   protected IdGenerator idGenerator;
/*      */   
/*      */   protected DataSource idGeneratorDataSource;
/*      */   
/*      */   protected String idGeneratorDataSourceJndiName;
/*      */   
/*      */   protected List<BpmnParseHandler> preBpmnParseHandlers;
/*      */   
/*      */   protected List<BpmnParseHandler> postBpmnParseHandlers;
/*      */   
/*      */   protected List<BpmnParseHandler> customDefaultBpmnParseHandlers;
/*      */   
/*      */   protected ActivityBehaviorFactory activityBehaviorFactory;
/*      */   
/*      */   protected ListenerFactory listenerFactory;
/*      */   
/*      */   protected BpmnParseFactory bpmnParseFactory;
/*      */   
/*      */   protected ProcessValidator processValidator;
/*      */   
/*      */   protected List<FormEngine> customFormEngines;
/*      */   
/*      */   protected Map<String, FormEngine> formEngines;
/*      */   
/*      */   protected List<AbstractFormType> customFormTypes;
/*      */   
/*      */   protected FormTypes formTypes;
/*      */   
/*      */   protected List<VariableType> customPreVariableTypes;
/*      */   
/*      */   protected List<VariableType> customPostVariableTypes;
/*      */   
/*      */   protected VariableTypes variableTypes;
/*      */   
/*      */   protected ExpressionManager expressionManager;
/*      */   
/*      */   protected List<String> customScriptingEngineClasses;
/*      */   
/*      */   protected ScriptingEngines scriptingEngines;
/*      */   
/*      */   protected List<ResolverFactory> resolverFactories;
/*      */   
/*      */   protected BusinessCalendarManager businessCalendarManager;
/*  378 */   protected int executionQueryLimit = 20000;
/*  379 */   protected int taskQueryLimit = 20000;
/*  380 */   protected int historicTaskQueryLimit = 20000;
/*  381 */   protected int historicProcessInstancesQueryLimit = 20000;
/*      */   
/*  383 */   protected String wsSyncFactoryClassName = "org.activiti.engine.impl.webservice.CxfWebServiceClientFactory";
/*  384 */   protected ConcurrentMap<QName, URL> wsOverridenEndpointAddresses = new ConcurrentHashMap<>();
/*      */ 
/*      */ 
/*      */   
/*      */   protected CommandContextFactory commandContextFactory;
/*      */ 
/*      */ 
/*      */   
/*      */   protected TransactionContextFactory transactionContextFactory;
/*      */ 
/*      */ 
/*      */   
/*      */   protected Map<Object, Object> beans;
/*      */ 
/*      */   
/*      */   protected DelegateInterceptor delegateInterceptor;
/*      */ 
/*      */   
/*      */   protected RejectedJobsHandler customRejectedJobsHandler;
/*      */ 
/*      */   
/*      */   protected Map<String, EventHandler> eventHandlers;
/*      */ 
/*      */   
/*      */   protected List<EventHandler> customEventHandlers;
/*      */ 
/*      */   
/*      */   protected FailedJobCommandFactory failedJobCommandFactory;
/*      */ 
/*      */   
/*      */   protected boolean enableSafeBpmnXml = false;
/*      */ 
/*      */   
/*  417 */   protected int batchSizeProcessInstances = 25;
/*  418 */   protected int batchSizeTasks = 25;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean isBulkInsertEnabled = true;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  433 */   protected int maxNrOfStatementsInBulkInsert = 100;
/*      */ 
/*      */ 
/*      */   
/*      */   protected boolean enableEventDispatcher = true;
/*      */ 
/*      */ 
/*      */   
/*      */   protected ActivitiEventDispatcher eventDispatcher;
/*      */ 
/*      */   
/*      */   protected List<ActivitiEventListener> eventListeners;
/*      */ 
/*      */   
/*      */   protected Map<String, List<ActivitiEventListener>> typedEventListeners;
/*      */ 
/*      */   
/*      */   protected boolean enableDatabaseEventLogging = false;
/*      */ 
/*      */   
/*  453 */   protected DelegateExpressionFieldInjectionMode delegateExpressionFieldInjectionMode = DelegateExpressionFieldInjectionMode.COMPATIBILITY;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*  459 */   protected int maxLengthStringVariableType = -1;
/*      */   
/*  461 */   protected ObjectMapper objectMapper = new ObjectMapper();
/*      */ 
/*      */ 
/*      */   
/*      */   public ProcessEngine buildProcessEngine() {
/*  466 */     init();
/*  467 */     return (ProcessEngine)new ProcessEngineImpl(this);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void init() {
/*  473 */     initConfigurators();
/*  474 */     configuratorsBeforeInit();
/*  475 */     initProcessDiagramGenerator();
/*  476 */     initHistoryLevel();
/*  477 */     initExpressionManager();
/*  478 */     initDataSource();
/*  479 */     initVariableTypes();
/*  480 */     initBeans();
/*  481 */     initFormEngines();
/*  482 */     initFormTypes();
/*  483 */     initScriptingEngines();
/*  484 */     initClock();
/*  485 */     initBusinessCalendarManager();
/*  486 */     initCommandContextFactory();
/*  487 */     initTransactionContextFactory();
/*  488 */     initCommandExecutors();
/*  489 */     initServices();
/*  490 */     initIdGenerator();
/*  491 */     initDeployers();
/*  492 */     initJobHandlers();
/*  493 */     initJobExecutor();
/*  494 */     initAsyncExecutor();
/*  495 */     initTransactionFactory();
/*  496 */     initSqlSessionFactory();
/*  497 */     initSessionFactories();
/*  498 */     initJpa();
/*  499 */     initDelegateInterceptor();
/*  500 */     initEventHandlers();
/*  501 */     initFailedJobCommandFactory();
/*  502 */     initEventDispatcher();
/*  503 */     initProcessValidator();
/*  504 */     initDatabaseEventLogging();
/*  505 */     configuratorsAfterInit();
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initFailedJobCommandFactory() {
/*  511 */     if (this.failedJobCommandFactory == null) {
/*  512 */       this.failedJobCommandFactory = (FailedJobCommandFactory)new DefaultFailedJobCommandFactory();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initCommandExecutors() {
/*  519 */     initDefaultCommandConfig();
/*  520 */     initSchemaCommandConfig();
/*  521 */     initCommandInvoker();
/*  522 */     initCommandInterceptors();
/*  523 */     initCommandExecutor();
/*      */   }
/*      */   
/*      */   protected void initDefaultCommandConfig() {
/*  527 */     if (this.defaultCommandConfig == null) {
/*  528 */       this.defaultCommandConfig = new CommandConfig();
/*      */     }
/*      */   }
/*      */   
/*      */   private void initSchemaCommandConfig() {
/*  533 */     if (this.schemaCommandConfig == null) {
/*  534 */       this.schemaCommandConfig = (new CommandConfig()).transactionNotSupported();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initCommandInvoker() {
/*  539 */     if (this.commandInvoker == null) {
/*  540 */       this.commandInvoker = (CommandInterceptor)new CommandInvoker();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initCommandInterceptors() {
/*  545 */     if (this.commandInterceptors == null) {
/*  546 */       this.commandInterceptors = new ArrayList<>();
/*  547 */       if (this.customPreCommandInterceptors != null) {
/*  548 */         this.commandInterceptors.addAll(this.customPreCommandInterceptors);
/*      */       }
/*  550 */       this.commandInterceptors.addAll(getDefaultCommandInterceptors());
/*  551 */       if (this.customPostCommandInterceptors != null) {
/*  552 */         this.commandInterceptors.addAll(this.customPostCommandInterceptors);
/*      */       }
/*  554 */       this.commandInterceptors.add(this.commandInvoker);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Collection<? extends CommandInterceptor> getDefaultCommandInterceptors() {
/*  559 */     List<CommandInterceptor> interceptors = new ArrayList<>();
/*  560 */     interceptors.add(new LogInterceptor());
/*      */     
/*  562 */     CommandInterceptor transactionInterceptor = createTransactionInterceptor();
/*  563 */     if (transactionInterceptor != null) {
/*  564 */       interceptors.add(transactionInterceptor);
/*      */     }
/*      */     
/*  567 */     interceptors.add(new CommandContextInterceptor(this.commandContextFactory, this));
/*  568 */     return interceptors;
/*      */   }
/*      */   
/*      */   protected void initCommandExecutor() {
/*  572 */     if (this.commandExecutor == null) {
/*  573 */       CommandInterceptor first = initInterceptorChain(this.commandInterceptors);
/*  574 */       this.commandExecutor = (CommandExecutor)new CommandExecutorImpl(getDefaultCommandConfig(), first);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected CommandInterceptor initInterceptorChain(List<CommandInterceptor> chain) {
/*  579 */     if (chain == null || chain.isEmpty()) {
/*  580 */       throw new ActivitiException("invalid command interceptor chain configuration: " + chain);
/*      */     }
/*  582 */     for (int i = 0; i < chain.size() - 1; i++) {
/*  583 */       ((CommandInterceptor)chain.get(i)).setNext(chain.get(i + 1));
/*      */     }
/*  585 */     return chain.get(0);
/*      */   }
/*      */ 
/*      */   
/*      */   protected abstract CommandInterceptor createTransactionInterceptor();
/*      */ 
/*      */   
/*      */   protected void initServices() {
/*  593 */     initService(this.repositoryService);
/*  594 */     initService(this.runtimeService);
/*  595 */     initService(this.historyService);
/*  596 */     initService(this.identityService);
/*  597 */     initService(this.taskService);
/*  598 */     initService(this.formService);
/*  599 */     initService(this.managementService);
/*  600 */     initService(this.dynamicBpmnService);
/*      */   }
/*      */   
/*      */   protected void initService(Object service) {
/*  604 */     if (service instanceof ServiceImpl) {
/*  605 */       ((ServiceImpl)service).setCommandExecutor(this.commandExecutor);
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initDataSource() {
/*  612 */     if (this.dataSource == null) {
/*  613 */       if (this.dataSourceJndiName != null) {
/*      */         try {
/*  615 */           this.dataSource = (DataSource)(new InitialContext()).lookup(this.dataSourceJndiName);
/*  616 */         } catch (Exception e) {
/*  617 */           throw new ActivitiException("couldn't lookup datasource from " + this.dataSourceJndiName + ": " + e.getMessage(), e);
/*      */         }
/*      */       
/*  620 */       } else if (this.jdbcUrl != null) {
/*  621 */         if (this.jdbcDriver == null || this.jdbcUrl == null || this.jdbcUsername == null) {
/*  622 */           throw new ActivitiException("DataSource or JDBC properties have to be specified in a process engine configuration");
/*      */         }
/*      */         
/*  625 */         log.debug("initializing datasource to db: {}", this.jdbcUrl);
/*      */ 
/*      */         
/*  628 */         PooledDataSource pooledDataSource = new PooledDataSource(ReflectUtil.getClassLoader(), this.jdbcDriver, this.jdbcUrl, this.jdbcUsername, this.jdbcPassword);
/*      */         
/*  630 */         if (this.jdbcMaxActiveConnections > 0) {
/*  631 */           pooledDataSource.setPoolMaximumActiveConnections(this.jdbcMaxActiveConnections);
/*      */         }
/*  633 */         if (this.jdbcMaxIdleConnections > 0) {
/*  634 */           pooledDataSource.setPoolMaximumIdleConnections(this.jdbcMaxIdleConnections);
/*      */         }
/*  636 */         if (this.jdbcMaxCheckoutTime > 0) {
/*  637 */           pooledDataSource.setPoolMaximumCheckoutTime(this.jdbcMaxCheckoutTime);
/*      */         }
/*  639 */         if (this.jdbcMaxWaitTime > 0) {
/*  640 */           pooledDataSource.setPoolTimeToWait(this.jdbcMaxWaitTime);
/*      */         }
/*  642 */         if (this.jdbcPingEnabled == true) {
/*  643 */           pooledDataSource.setPoolPingEnabled(true);
/*  644 */           if (this.jdbcPingQuery != null) {
/*  645 */             pooledDataSource.setPoolPingQuery(this.jdbcPingQuery);
/*      */           }
/*  647 */           pooledDataSource.setPoolPingConnectionsNotUsedFor(this.jdbcPingConnectionNotUsedFor);
/*      */         } 
/*  649 */         if (this.jdbcDefaultTransactionIsolationLevel > 0) {
/*  650 */           pooledDataSource.setDefaultTransactionIsolationLevel(Integer.valueOf(this.jdbcDefaultTransactionIsolationLevel));
/*      */         }
/*  652 */         this.dataSource = (DataSource)pooledDataSource;
/*      */       } 
/*      */       
/*  655 */       if (this.dataSource instanceof PooledDataSource)
/*      */       {
/*  657 */         ((PooledDataSource)this.dataSource).forceCloseAll();
/*      */       }
/*      */     } 
/*      */     
/*  661 */     if (this.databaseType == null) {
/*  662 */       initDatabaseType();
/*      */     }
/*      */   }
/*      */   
/*  666 */   protected static Properties databaseTypeMappings = getDefaultDatabaseTypeMappings();
/*      */   
/*      */   public static final String DATABASE_TYPE_H2 = "h2";
/*      */   public static final String DATABASE_TYPE_HSQL = "hsql";
/*      */   public static final String DATABASE_TYPE_MYSQL = "mysql";
/*      */   public static final String DATABASE_TYPE_ORACLE = "oracle";
/*      */   public static final String DATABASE_TYPE_POSTGRES = "postgres";
/*      */   public static final String DATABASE_TYPE_MSSQL = "mssql";
/*      */   public static final String DATABASE_TYPE_DB2 = "db2";
/*      */   public static final String DATABASE_TYPE_DMSQL = "dmsql";
/*      */   public static final String DATABASE_TYPE_DRDS = "drds";
/*      */   public static final String DATABASE_TYPE_KINGBASE = "kingbase";
/*      */   public static final String DATABASE_TYPE_HIGHGO = "highgo";
/*      */   
/*      */   protected static Properties getDefaultDatabaseTypeMappings() {
/*  681 */     Properties databaseTypeMappings = new Properties();
/*  682 */     databaseTypeMappings.setProperty("H2", "h2");
/*  683 */     databaseTypeMappings.setProperty("HSQL Database Engine", "hsql");
/*  684 */     databaseTypeMappings.setProperty("MySQL", "mysql");
/*  685 */     databaseTypeMappings.setProperty("Oracle", "oracle");
/*  686 */     databaseTypeMappings.setProperty("PostgreSQL", "postgres");
/*  687 */     databaseTypeMappings.setProperty("Microsoft SQL Server", "mssql");
/*  688 */     databaseTypeMappings.setProperty("db2", "db2");
/*  689 */     databaseTypeMappings.setProperty("DB2", "db2");
/*  690 */     databaseTypeMappings.setProperty("DB2/NT", "db2");
/*  691 */     databaseTypeMappings.setProperty("DB2/NT64", "db2");
/*  692 */     databaseTypeMappings.setProperty("DB2 UDP", "db2");
/*  693 */     databaseTypeMappings.setProperty("DB2/LINUX", "db2");
/*  694 */     databaseTypeMappings.setProperty("DB2/LINUX390", "db2");
/*  695 */     databaseTypeMappings.setProperty("DB2/LINUXX8664", "db2");
/*  696 */     databaseTypeMappings.setProperty("DB2/LINUXZ64", "db2");
/*  697 */     databaseTypeMappings.setProperty("DB2/LINUXPPC64", "db2");
/*  698 */     databaseTypeMappings.setProperty("DB2/LINUXPPC64LE", "db2");
/*  699 */     databaseTypeMappings.setProperty("DB2/400 SQL", "db2");
/*  700 */     databaseTypeMappings.setProperty("DB2/6000", "db2");
/*  701 */     databaseTypeMappings.setProperty("DB2 UDB iSeries", "db2");
/*  702 */     databaseTypeMappings.setProperty("DB2/AIX64", "db2");
/*  703 */     databaseTypeMappings.setProperty("DB2/HPUX", "db2");
/*  704 */     databaseTypeMappings.setProperty("DB2/HP64", "db2");
/*  705 */     databaseTypeMappings.setProperty("DB2/SUN", "db2");
/*  706 */     databaseTypeMappings.setProperty("DB2/SUN64", "db2");
/*  707 */     databaseTypeMappings.setProperty("DB2/PTX", "db2");
/*  708 */     databaseTypeMappings.setProperty("DB2/2", "db2");
/*  709 */     databaseTypeMappings.setProperty("DB2 UDB AS400", "db2");
/*  710 */     databaseTypeMappings.setProperty("dmsql", "dmsql");
/*  711 */     databaseTypeMappings.setProperty("drds", "drds");
/*  712 */     databaseTypeMappings.setProperty("kingbase", "kingbase");
/*  713 */     databaseTypeMappings.setProperty("highgo", "highgo");
/*  714 */     return databaseTypeMappings;
/*      */   }
/*      */   
/*      */   public void initDatabaseType() {
/*  718 */     Connection connection = null;
/*      */     try {
/*  720 */       connection = this.dataSource.getConnection();
/*  721 */       DatabaseMetaData databaseMetaData = connection.getMetaData();
/*  722 */       String databaseProductName = databaseMetaData.getDatabaseProductName();
/*  723 */       log.debug("database product name: '{}'", databaseProductName);
/*  724 */       this.databaseType = databaseTypeMappings.getProperty(databaseProductName);
/*  725 */       if (this.databaseType == null) {
/*  726 */         throw new ActivitiException("couldn't deduct database type from database product name '" + databaseProductName + "'");
/*      */       }
/*  728 */       log.debug("using database type: {}", this.databaseType);
/*      */     }
/*  730 */     catch (SQLException e) {
/*  731 */       log.error("Exception while initializing Database connection", e);
/*      */     } finally {
/*      */       try {
/*  734 */         if (connection != null) {
/*  735 */           connection.close();
/*      */         }
/*  737 */       } catch (SQLException e) {
/*  738 */         log.error("Exception while closing the Database connection", e);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initTransactionFactory() {
/*  746 */     if (this.transactionFactory == null) {
/*  747 */       if (this.transactionsExternallyManaged) {
/*  748 */         this.transactionFactory = (TransactionFactory)new ManagedTransactionFactory();
/*      */       } else {
/*  750 */         this.transactionFactory = (TransactionFactory)new JdbcTransactionFactory();
/*      */       } 
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initSqlSessionFactory() {
/*  756 */     if (this.sqlSessionFactory == null) {
/*  757 */       InputStream inputStream = null;
/*      */       try {
/*  759 */         inputStream = getMyBatisXmlConfigurationSteam();
/*      */ 
/*      */         
/*  762 */         Environment environment = new Environment("default", this.transactionFactory, this.dataSource);
/*  763 */         Reader reader = new InputStreamReader(inputStream);
/*  764 */         Properties properties = new Properties();
/*  765 */         properties.put("prefix", this.databaseTablePrefix);
/*  766 */         String wildcardEscapeClause = "";
/*  767 */         if (this.databaseWildcardEscapeCharacter != null && this.databaseWildcardEscapeCharacter.length() != 0) {
/*  768 */           wildcardEscapeClause = " escape '" + this.databaseWildcardEscapeCharacter + "'";
/*      */         }
/*  770 */         properties.put("wildcardEscapeClause", wildcardEscapeClause);
/*  771 */         if (this.databaseType != null) {
/*  772 */           properties.put("limitBefore", DbSqlSessionFactory.databaseSpecificLimitBeforeStatements.get(this.databaseType));
/*  773 */           properties.put("limitAfter", DbSqlSessionFactory.databaseSpecificLimitAfterStatements.get(this.databaseType));
/*  774 */           properties.put("limitBetween", DbSqlSessionFactory.databaseSpecificLimitBetweenStatements.get(this.databaseType));
/*  775 */           properties.put("limitOuterJoinBetween", DbSqlSessionFactory.databaseOuterJoinLimitBetweenStatements.get(this.databaseType));
/*  776 */           properties.put("orderBy", DbSqlSessionFactory.databaseSpecificOrderByStatements.get(this.databaseType));
/*  777 */           properties.put("limitBeforeNativeQuery", ObjectUtils.toString(DbSqlSessionFactory.databaseSpecificLimitBeforeNativeQueryStatements.get(this.databaseType)));
/*      */         } 
/*      */         
/*  780 */         Configuration configuration = initMybatisConfiguration(environment, reader, properties);
/*  781 */         this.sqlSessionFactory = (SqlSessionFactory)new DefaultSqlSessionFactory(configuration);
/*      */       }
/*  783 */       catch (Exception e) {
/*  784 */         throw new ActivitiException("Error while building ibatis SqlSessionFactory: " + e.getMessage(), e);
/*      */       } finally {
/*  786 */         IoUtil.closeSilently(inputStream);
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Configuration initMybatisConfiguration(Environment environment, Reader reader, Properties properties) {
/*  792 */     XMLConfigBuilder parser = new XMLConfigBuilder(reader, "", properties);
/*  793 */     Configuration configuration = parser.getConfiguration();
/*  794 */     configuration.setEnvironment(environment);
/*      */     
/*  796 */     initMybatisTypeHandlers(configuration);
/*  797 */     initCustomMybatisMappers(configuration);
/*      */     
/*  799 */     configuration = parseMybatisConfiguration(configuration, parser);
/*  800 */     return configuration;
/*      */   }
/*      */   
/*      */   protected void initMybatisTypeHandlers(Configuration configuration) {
/*  804 */     configuration.getTypeHandlerRegistry().register(VariableType.class, JdbcType.VARCHAR, (TypeHandler)new IbatisVariableTypeHandler());
/*      */   }
/*      */   
/*      */   protected void initCustomMybatisMappers(Configuration configuration) {
/*  808 */     if (getCustomMybatisMappers() != null) {
/*  809 */       for (Class<?> clazz : getCustomMybatisMappers()) {
/*  810 */         configuration.addMapper(clazz);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected Configuration parseMybatisConfiguration(Configuration configuration, XMLConfigBuilder parser) {
/*  816 */     return parseCustomMybatisXMLMappers(parser.parse());
/*      */   }
/*      */   
/*      */   protected Configuration parseCustomMybatisXMLMappers(Configuration configuration) {
/*  820 */     if (getCustomMybatisXMLMappers() != null)
/*      */     {
/*  822 */       for (String resource : getCustomMybatisXMLMappers()) {
/*      */         
/*  824 */         XMLMapperBuilder mapperParser = new XMLMapperBuilder(getResourceAsStream(resource), configuration, resource, configuration.getSqlFragments());
/*  825 */         mapperParser.parse();
/*      */       }  } 
/*  827 */     return configuration;
/*      */   }
/*      */   
/*      */   protected InputStream getResourceAsStream(String resource) {
/*  831 */     return ReflectUtil.getResourceAsStream(resource);
/*      */   }
/*      */   
/*      */   protected InputStream getMyBatisXmlConfigurationSteam() {
/*  835 */     return getResourceAsStream("org/activiti/db/mapping/mappings.xml");
/*      */   }
/*      */   
/*      */   public Set<Class<?>> getCustomMybatisMappers() {
/*  839 */     return this.customMybatisMappers;
/*      */   }
/*      */   
/*      */   public void setCustomMybatisMappers(Set<Class<?>> customMybatisMappers) {
/*  843 */     this.customMybatisMappers = customMybatisMappers;
/*      */   }
/*      */   
/*      */   public Set<String> getCustomMybatisXMLMappers() {
/*  847 */     return this.customMybatisXMLMappers;
/*      */   }
/*      */   
/*      */   public void setCustomMybatisXMLMappers(Set<String> customMybatisXMLMappers) {
/*  851 */     this.customMybatisXMLMappers = customMybatisXMLMappers;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initSessionFactories() {
/*  858 */     if (this.sessionFactories == null) {
/*  859 */       this.sessionFactories = new HashMap<>();
/*      */       
/*  861 */       if (this.dbSqlSessionFactory == null) {
/*  862 */         this.dbSqlSessionFactory = new DbSqlSessionFactory();
/*      */       }
/*  864 */       this.dbSqlSessionFactory.setDatabaseType(this.databaseType);
/*  865 */       this.dbSqlSessionFactory.setIdGenerator(this.idGenerator);
/*  866 */       this.dbSqlSessionFactory.setSqlSessionFactory(this.sqlSessionFactory);
/*  867 */       this.dbSqlSessionFactory.setDbIdentityUsed(this.isDbIdentityUsed);
/*  868 */       this.dbSqlSessionFactory.setDbHistoryUsed(this.isDbHistoryUsed);
/*  869 */       this.dbSqlSessionFactory.setDatabaseTablePrefix(this.databaseTablePrefix);
/*  870 */       this.dbSqlSessionFactory.setTablePrefixIsSchema(this.tablePrefixIsSchema);
/*  871 */       this.dbSqlSessionFactory.setDatabaseCatalog(this.databaseCatalog);
/*  872 */       this.dbSqlSessionFactory.setDatabaseSchema(this.databaseSchema);
/*  873 */       this.dbSqlSessionFactory.setBulkInsertEnabled(this.isBulkInsertEnabled, this.databaseType);
/*  874 */       this.dbSqlSessionFactory.setMaxNrOfStatementsInBulkInsert(this.maxNrOfStatementsInBulkInsert);
/*  875 */       addSessionFactory((SessionFactory)this.dbSqlSessionFactory);
/*      */       
/*  877 */       addSessionFactory((SessionFactory)new GenericManagerFactory(AttachmentEntityManager.class));
/*  878 */       addSessionFactory((SessionFactory)new GenericManagerFactory(CommentEntityManager.class));
/*  879 */       addSessionFactory((SessionFactory)new GenericManagerFactory(DeploymentEntityManager.class));
/*  880 */       addSessionFactory((SessionFactory)new GenericManagerFactory(ModelEntityManager.class));
/*  881 */       addSessionFactory((SessionFactory)new GenericManagerFactory(ExecutionEntityManager.class));
/*  882 */       addSessionFactory((SessionFactory)new GenericManagerFactory(HistoricActivityInstanceEntityManager.class));
/*  883 */       addSessionFactory((SessionFactory)new GenericManagerFactory(HistoricDetailEntityManager.class));
/*  884 */       addSessionFactory((SessionFactory)new GenericManagerFactory(HistoricProcessInstanceEntityManager.class));
/*  885 */       addSessionFactory((SessionFactory)new GenericManagerFactory(HistoricVariableInstanceEntityManager.class));
/*  886 */       addSessionFactory((SessionFactory)new GenericManagerFactory(HistoricTaskInstanceEntityManager.class));
/*  887 */       addSessionFactory((SessionFactory)new GenericManagerFactory(HistoricIdentityLinkEntityManager.class));
/*  888 */       addSessionFactory((SessionFactory)new GenericManagerFactory(IdentityInfoEntityManager.class));
/*  889 */       addSessionFactory((SessionFactory)new GenericManagerFactory(IdentityLinkEntityManager.class));
/*  890 */       addSessionFactory((SessionFactory)new GenericManagerFactory(JobEntityManager.class));
/*  891 */       addSessionFactory((SessionFactory)new GenericManagerFactory(ProcessDefinitionEntityManager.class));
/*  892 */       addSessionFactory((SessionFactory)new GenericManagerFactory(ProcessDefinitionInfoEntityManager.class));
/*  893 */       addSessionFactory((SessionFactory)new GenericManagerFactory(PropertyEntityManager.class));
/*  894 */       addSessionFactory((SessionFactory)new GenericManagerFactory(ResourceEntityManager.class));
/*  895 */       addSessionFactory((SessionFactory)new GenericManagerFactory(ByteArrayEntityManager.class));
/*  896 */       addSessionFactory((SessionFactory)new GenericManagerFactory(TableDataManager.class));
/*  897 */       addSessionFactory((SessionFactory)new GenericManagerFactory(TaskEntityManager.class));
/*  898 */       addSessionFactory((SessionFactory)new GenericManagerFactory(VariableInstanceEntityManager.class));
/*  899 */       addSessionFactory((SessionFactory)new GenericManagerFactory(EventSubscriptionEntityManager.class));
/*  900 */       addSessionFactory((SessionFactory)new GenericManagerFactory(EventLogEntryEntityManager.class));
/*      */       
/*  902 */       addSessionFactory((SessionFactory)new DefaultHistoryManagerSessionFactory());
/*      */       
/*  904 */       addSessionFactory((SessionFactory)new UserEntityManagerFactory());
/*  905 */       addSessionFactory((SessionFactory)new GroupEntityManagerFactory());
/*  906 */       addSessionFactory((SessionFactory)new MembershipEntityManagerFactory());
/*      */     } 
/*      */     
/*  909 */     if (this.customSessionFactories != null) {
/*  910 */       for (SessionFactory sessionFactory : this.customSessionFactories) {
/*  911 */         addSessionFactory(sessionFactory);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void addSessionFactory(SessionFactory sessionFactory) {
/*  917 */     this.sessionFactories.put(sessionFactory.getSessionType(), sessionFactory);
/*      */   }
/*      */ 
/*      */   
/*      */   protected void initConfigurators() {
/*  922 */     this.allConfigurators = new ArrayList<>();
/*      */ 
/*      */     
/*  925 */     if (this.configurators != null) {
/*  926 */       for (ProcessEngineConfigurator configurator : this.configurators) {
/*  927 */         this.allConfigurators.add(configurator);
/*      */       }
/*      */     }
/*      */ 
/*      */     
/*  932 */     if (this.enableConfiguratorServiceLoader) {
/*  933 */       ClassLoader classLoader = getClassLoader();
/*  934 */       if (classLoader == null) {
/*  935 */         classLoader = ReflectUtil.getClassLoader();
/*      */       }
/*      */ 
/*      */       
/*  939 */       ServiceLoader<ProcessEngineConfigurator> configuratorServiceLoader = ServiceLoader.load(ProcessEngineConfigurator.class, classLoader);
/*  940 */       int nrOfServiceLoadedConfigurators = 0;
/*  941 */       for (ProcessEngineConfigurator configurator : configuratorServiceLoader) {
/*  942 */         this.allConfigurators.add(configurator);
/*  943 */         nrOfServiceLoadedConfigurators++;
/*      */       } 
/*      */       
/*  946 */       if (nrOfServiceLoadedConfigurators > 0) {
/*  947 */         log.info("Found {} auto-discoverable Process Engine Configurator{}", Integer.valueOf(nrOfServiceLoadedConfigurators++), (nrOfServiceLoadedConfigurators > 1) ? "s" : "");
/*      */       }
/*      */       
/*  950 */       if (!this.allConfigurators.isEmpty()) {
/*      */ 
/*      */         
/*  953 */         Collections.sort(this.allConfigurators, new Comparator<ProcessEngineConfigurator>()
/*      */             {
/*      */               public int compare(ProcessEngineConfigurator configurator1, ProcessEngineConfigurator configurator2) {
/*  956 */                 int priority1 = configurator1.getPriority();
/*  957 */                 int priority2 = configurator2.getPriority();
/*      */                 
/*  959 */                 if (priority1 < priority2)
/*  960 */                   return -1; 
/*  961 */                 if (priority1 > priority2) {
/*  962 */                   return 1;
/*      */                 }
/*  964 */                 return 0;
/*      */               }
/*      */             });
/*      */ 
/*      */         
/*  969 */         log.info("Found {} Process Engine Configurators in total:", Integer.valueOf(this.allConfigurators.size()));
/*  970 */         for (ProcessEngineConfigurator configurator : this.allConfigurators) {
/*  971 */           log.info("{} (priority:{})", configurator.getClass(), Integer.valueOf(configurator.getPriority()));
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void configuratorsBeforeInit() {
/*  980 */     for (ProcessEngineConfigurator configurator : this.allConfigurators) {
/*  981 */       log.info("Executing beforeInit() of {} (priority:{})", configurator.getClass(), Integer.valueOf(configurator.getPriority()));
/*  982 */       configurator.beforeInit(this);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void configuratorsAfterInit() {
/*  987 */     for (ProcessEngineConfigurator configurator : this.allConfigurators) {
/*  988 */       log.info("Executing configure() of {} (priority:{})", configurator.getClass(), Integer.valueOf(configurator.getPriority()));
/*  989 */       configurator.configure(this);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initDeployers() {
/*  996 */     if (this.deployers == null) {
/*  997 */       this.deployers = new ArrayList<>();
/*  998 */       if (this.customPreDeployers != null) {
/*  999 */         this.deployers.addAll(this.customPreDeployers);
/*      */       }
/* 1001 */       this.deployers.addAll(getDefaultDeployers());
/* 1002 */       if (this.customPostDeployers != null) {
/* 1003 */         this.deployers.addAll(this.customPostDeployers);
/*      */       }
/*      */     } 
/* 1006 */     if (this.deploymentManager == null) {
/* 1007 */       this.deploymentManager = new DeploymentManager();
/* 1008 */       this.deploymentManager.setDeployers(this.deployers);
/*      */ 
/*      */       
/* 1011 */       if (this.processDefinitionCache == null) {
/* 1012 */         if (this.processDefinitionCacheLimit <= 0) {
/* 1013 */           this.processDefinitionCache = (DeploymentCache<ProcessDefinitionEntity>)new DefaultDeploymentCache();
/*      */         } else {
/* 1015 */           this.processDefinitionCache = (DeploymentCache<ProcessDefinitionEntity>)new DefaultDeploymentCache(this.processDefinitionCacheLimit);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1020 */       if (this.bpmnModelCache == null) {
/* 1021 */         if (this.bpmnModelCacheLimit <= 0) {
/* 1022 */           this.bpmnModelCache = (DeploymentCache<BpmnModel>)new DefaultDeploymentCache();
/*      */         } else {
/* 1024 */           this.bpmnModelCache = (DeploymentCache<BpmnModel>)new DefaultDeploymentCache(this.bpmnModelCacheLimit);
/*      */         } 
/*      */       }
/*      */       
/* 1028 */       if (this.processDefinitionInfoCache == null) {
/* 1029 */         if (this.processDefinitionInfoCacheLimit <= 0) {
/* 1030 */           this.processDefinitionInfoCache = new ProcessDefinitionInfoCache(this.commandExecutor);
/*      */         } else {
/* 1032 */           this.processDefinitionInfoCache = new ProcessDefinitionInfoCache(this.commandExecutor, this.processDefinitionInfoCacheLimit);
/*      */         } 
/*      */       }
/*      */ 
/*      */       
/* 1037 */       if (this.knowledgeBaseCache == null) {
/* 1038 */         if (this.knowledgeBaseCacheLimit <= 0) {
/* 1039 */           this.knowledgeBaseCache = (DeploymentCache<Object>)new DefaultDeploymentCache();
/*      */         } else {
/* 1041 */           this.knowledgeBaseCache = (DeploymentCache<Object>)new DefaultDeploymentCache(this.knowledgeBaseCacheLimit);
/*      */         } 
/*      */       }
/*      */       
/* 1045 */       this.deploymentManager.setProcessDefinitionCache(this.processDefinitionCache);
/* 1046 */       this.deploymentManager.setBpmnModelCache(this.bpmnModelCache);
/* 1047 */       this.deploymentManager.setProcessDefinitionInfoCache(this.processDefinitionInfoCache);
/* 1048 */       this.deploymentManager.setKnowledgeBaseCache(this.knowledgeBaseCache);
/*      */     } 
/*      */   }
/*      */   
/*      */   protected Collection<? extends Deployer> getDefaultDeployers() {
/* 1053 */     List<Deployer> defaultDeployers = new ArrayList<>();
/*      */     
/* 1055 */     if (this.bpmnDeployer == null) {
/* 1056 */       this.bpmnDeployer = new BpmnDeployer();
/*      */     }
/*      */     
/* 1059 */     this.bpmnDeployer.setExpressionManager(this.expressionManager);
/* 1060 */     this.bpmnDeployer.setIdGenerator(this.idGenerator);
/*      */     
/* 1062 */     if (this.bpmnParseFactory == null) {
/* 1063 */       this.bpmnParseFactory = (BpmnParseFactory)new DefaultBpmnParseFactory();
/*      */     }
/*      */     
/* 1066 */     if (this.activityBehaviorFactory == null) {
/* 1067 */       DefaultActivityBehaviorFactory defaultActivityBehaviorFactory = new DefaultActivityBehaviorFactory();
/* 1068 */       defaultActivityBehaviorFactory.setExpressionManager(this.expressionManager);
/* 1069 */       this.activityBehaviorFactory = (ActivityBehaviorFactory)defaultActivityBehaviorFactory;
/* 1070 */     } else if (this.activityBehaviorFactory instanceof AbstractBehaviorFactory && ((AbstractBehaviorFactory)this.activityBehaviorFactory)
/* 1071 */       .getExpressionManager() == null) {
/* 1072 */       ((AbstractBehaviorFactory)this.activityBehaviorFactory).setExpressionManager(this.expressionManager);
/*      */     } 
/*      */     
/* 1075 */     if (this.listenerFactory == null) {
/* 1076 */       DefaultListenerFactory defaultListenerFactory = new DefaultListenerFactory();
/* 1077 */       defaultListenerFactory.setExpressionManager(this.expressionManager);
/* 1078 */       this.listenerFactory = (ListenerFactory)defaultListenerFactory;
/* 1079 */     } else if (this.listenerFactory instanceof AbstractBehaviorFactory && ((AbstractBehaviorFactory)this.listenerFactory)
/* 1080 */       .getExpressionManager() == null) {
/* 1081 */       ((AbstractBehaviorFactory)this.listenerFactory).setExpressionManager(this.expressionManager);
/*      */     } 
/*      */     
/* 1084 */     if (this.bpmnParser == null) {
/* 1085 */       this.bpmnParser = new BpmnParser();
/*      */     }
/*      */     
/* 1088 */     this.bpmnParser.setExpressionManager(this.expressionManager);
/* 1089 */     this.bpmnParser.setBpmnParseFactory(this.bpmnParseFactory);
/* 1090 */     this.bpmnParser.setActivityBehaviorFactory(this.activityBehaviorFactory);
/* 1091 */     this.bpmnParser.setListenerFactory(this.listenerFactory);
/*      */     
/* 1093 */     List<BpmnParseHandler> parseHandlers = new ArrayList<>();
/* 1094 */     if (getPreBpmnParseHandlers() != null) {
/* 1095 */       parseHandlers.addAll(getPreBpmnParseHandlers());
/*      */     }
/* 1097 */     parseHandlers.addAll(getDefaultBpmnParseHandlers());
/* 1098 */     if (getPostBpmnParseHandlers() != null) {
/* 1099 */       parseHandlers.addAll(getPostBpmnParseHandlers());
/*      */     }
/*      */     
/* 1102 */     BpmnParseHandlers bpmnParseHandlers = new BpmnParseHandlers();
/* 1103 */     bpmnParseHandlers.addHandlers(parseHandlers);
/* 1104 */     this.bpmnParser.setBpmnParserHandlers(bpmnParseHandlers);
/*      */     
/* 1106 */     this.bpmnDeployer.setBpmnParser(this.bpmnParser);
/*      */     
/* 1108 */     defaultDeployers.add(this.bpmnDeployer);
/* 1109 */     return defaultDeployers;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected List<BpmnParseHandler> getDefaultBpmnParseHandlers() {
/* 1115 */     List<BpmnParseHandler> bpmnParserHandlers = new ArrayList<>();
/* 1116 */     bpmnParserHandlers.add(new BoundaryEventParseHandler());
/* 1117 */     bpmnParserHandlers.add(new BusinessRuleParseHandler());
/* 1118 */     bpmnParserHandlers.add(new CallActivityParseHandler());
/* 1119 */     bpmnParserHandlers.add(new CancelEventDefinitionParseHandler());
/* 1120 */     bpmnParserHandlers.add(new CompensateEventDefinitionParseHandler());
/* 1121 */     bpmnParserHandlers.add(new EndEventParseHandler());
/* 1122 */     bpmnParserHandlers.add(new ErrorEventDefinitionParseHandler());
/* 1123 */     bpmnParserHandlers.add(new EventBasedGatewayParseHandler());
/* 1124 */     bpmnParserHandlers.add(new ExclusiveGatewayParseHandler());
/* 1125 */     bpmnParserHandlers.add(new InclusiveGatewayParseHandler());
/* 1126 */     bpmnParserHandlers.add(new IntermediateCatchEventParseHandler());
/* 1127 */     bpmnParserHandlers.add(new IntermediateThrowEventParseHandler());
/* 1128 */     bpmnParserHandlers.add(new ManualTaskParseHandler());
/* 1129 */     bpmnParserHandlers.add(new MessageEventDefinitionParseHandler());
/* 1130 */     bpmnParserHandlers.add(new ParallelGatewayParseHandler());
/* 1131 */     bpmnParserHandlers.add(new ProcessParseHandler());
/* 1132 */     bpmnParserHandlers.add(new ReceiveTaskParseHandler());
/* 1133 */     bpmnParserHandlers.add(new ScriptTaskParseHandler());
/* 1134 */     bpmnParserHandlers.add(new SendTaskParseHandler());
/* 1135 */     bpmnParserHandlers.add(new SequenceFlowParseHandler());
/* 1136 */     bpmnParserHandlers.add(new ServiceTaskParseHandler());
/* 1137 */     bpmnParserHandlers.add(new SignalEventDefinitionParseHandler());
/* 1138 */     bpmnParserHandlers.add(new StartEventParseHandler());
/* 1139 */     bpmnParserHandlers.add(new SubProcessParseHandler());
/* 1140 */     bpmnParserHandlers.add(new EventSubProcessParseHandler());
/* 1141 */     bpmnParserHandlers.add(new TaskParseHandler());
/* 1142 */     bpmnParserHandlers.add(new TimerEventDefinitionParseHandler());
/* 1143 */     bpmnParserHandlers.add(new TransactionParseHandler());
/* 1144 */     bpmnParserHandlers.add(new UserTaskParseHandler());
/*      */ 
/*      */     
/* 1147 */     if (this.customDefaultBpmnParseHandlers != null) {
/*      */       
/* 1149 */       Map<Class<?>, BpmnParseHandler> customParseHandlerMap = new HashMap<>();
/* 1150 */       for (BpmnParseHandler bpmnParseHandler : this.customDefaultBpmnParseHandlers) {
/* 1151 */         for (Class<?> handledType : (Iterable<Class<?>>)bpmnParseHandler.getHandledTypes()) {
/* 1152 */           customParseHandlerMap.put(handledType, bpmnParseHandler);
/*      */         }
/*      */       } 
/*      */       
/* 1156 */       for (int i = 0; i < bpmnParserHandlers.size(); i++) {
/*      */         
/* 1158 */         BpmnParseHandler defaultBpmnParseHandler = bpmnParserHandlers.get(i);
/* 1159 */         if (defaultBpmnParseHandler.getHandledTypes().size() != 1) {
/* 1160 */           StringBuilder supportedTypes = new StringBuilder();
/* 1161 */           for (Class<?> type : (Iterable<Class<?>>)defaultBpmnParseHandler.getHandledTypes()) {
/* 1162 */             supportedTypes.append(" ").append(type.getCanonicalName()).append(" ");
/*      */           }
/* 1164 */           throw new ActivitiException("The default BPMN parse handlers should only support one type, but " + defaultBpmnParseHandler.getClass() + " supports " + supportedTypes
/* 1165 */               .toString() + ". This is likely a programmatic error");
/*      */         } 
/* 1167 */         Class<?> handledType = defaultBpmnParseHandler.getHandledTypes().iterator().next();
/* 1168 */         if (customParseHandlerMap.containsKey(handledType)) {
/* 1169 */           BpmnParseHandler newBpmnParseHandler = customParseHandlerMap.get(handledType);
/* 1170 */           log.info("Replacing default BpmnParseHandler " + defaultBpmnParseHandler.getClass().getName() + " with " + newBpmnParseHandler.getClass().getName());
/* 1171 */           bpmnParserHandlers.set(i, newBpmnParseHandler);
/*      */         } 
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1178 */     for (BpmnParseHandler handler : getDefaultHistoryParseHandlers()) {
/* 1179 */       bpmnParserHandlers.add(handler);
/*      */     }
/*      */     
/* 1182 */     return bpmnParserHandlers;
/*      */   }
/*      */   
/*      */   protected List<BpmnParseHandler> getDefaultHistoryParseHandlers() {
/* 1186 */     List<BpmnParseHandler> parseHandlers = new ArrayList<>();
/* 1187 */     parseHandlers.add(new FlowNodeHistoryParseHandler());
/* 1188 */     parseHandlers.add(new ProcessHistoryParseHandler());
/* 1189 */     parseHandlers.add(new StartEventHistoryParseHandler());
/* 1190 */     parseHandlers.add(new UserTaskHistoryParseHandler());
/* 1191 */     return parseHandlers;
/*      */   }
/*      */   
/*      */   private void initClock() {
/* 1195 */     if (this.clock == null) {
/* 1196 */       this.clock = (Clock)new DefaultClockImpl();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initProcessDiagramGenerator() {
/* 1201 */     if (this.processDiagramGenerator == null) {
/* 1202 */       this.processDiagramGenerator = (ProcessDiagramGenerator)new DefaultProcessDiagramGenerator();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initJobHandlers() {
/* 1207 */     this.jobHandlers = new HashMap<>();
/* 1208 */     TimerExecuteNestedActivityJobHandler timerExecuteNestedActivityJobHandler = new TimerExecuteNestedActivityJobHandler();
/* 1209 */     this.jobHandlers.put(timerExecuteNestedActivityJobHandler.getType(), timerExecuteNestedActivityJobHandler);
/*      */     
/* 1211 */     TimerCatchIntermediateEventJobHandler timerCatchIntermediateEvent = new TimerCatchIntermediateEventJobHandler();
/* 1212 */     this.jobHandlers.put(timerCatchIntermediateEvent.getType(), timerCatchIntermediateEvent);
/*      */     
/* 1214 */     TimerStartEventJobHandler timerStartEvent = new TimerStartEventJobHandler();
/* 1215 */     this.jobHandlers.put(timerStartEvent.getType(), timerStartEvent);
/*      */     
/* 1217 */     AsyncContinuationJobHandler asyncContinuationJobHandler = new AsyncContinuationJobHandler();
/* 1218 */     this.jobHandlers.put(asyncContinuationJobHandler.getType(), asyncContinuationJobHandler);
/*      */     
/* 1220 */     ProcessEventJobHandler processEventJobHandler = new ProcessEventJobHandler();
/* 1221 */     this.jobHandlers.put(processEventJobHandler.getType(), processEventJobHandler);
/*      */     
/* 1223 */     TimerSuspendProcessDefinitionHandler suspendProcessDefinitionHandler = new TimerSuspendProcessDefinitionHandler();
/* 1224 */     this.jobHandlers.put(suspendProcessDefinitionHandler.getType(), suspendProcessDefinitionHandler);
/*      */     
/* 1226 */     TimerActivateProcessDefinitionHandler activateProcessDefinitionHandler = new TimerActivateProcessDefinitionHandler();
/* 1227 */     this.jobHandlers.put(activateProcessDefinitionHandler.getType(), activateProcessDefinitionHandler);
/*      */ 
/*      */     
/* 1230 */     if (getCustomJobHandlers() != null) {
/* 1231 */       for (JobHandler customJobHandler : getCustomJobHandlers()) {
/* 1232 */         this.jobHandlers.put(customJobHandler.getType(), customJobHandler);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initJobExecutor() {
/* 1240 */     if (!isAsyncExecutorEnabled()) {
/* 1241 */       if (this.jobExecutor == null) {
/* 1242 */         this.jobExecutor = (JobExecutor)new DefaultJobExecutor();
/*      */       }
/*      */       
/* 1245 */       this.jobExecutor.setClockReader((ClockReader)this.clock);
/*      */       
/* 1247 */       this.jobExecutor.setCommandExecutor(this.commandExecutor);
/* 1248 */       this.jobExecutor.setAutoActivate(this.jobExecutorActivate);
/*      */       
/* 1250 */       if (this.jobExecutor.getRejectedJobsHandler() == null) {
/* 1251 */         if (this.customRejectedJobsHandler != null) {
/* 1252 */           this.jobExecutor.setRejectedJobsHandler(this.customRejectedJobsHandler);
/*      */         } else {
/* 1254 */           this.jobExecutor.setRejectedJobsHandler((RejectedJobsHandler)new CallerRunsRejectedJobsHandler());
/*      */         } 
/*      */       }
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initAsyncExecutor() {
/* 1263 */     if (isAsyncExecutorEnabled()) {
/* 1264 */       if (this.asyncExecutor == null) {
/* 1265 */         DefaultAsyncJobExecutor defaultAsyncExecutor = new DefaultAsyncJobExecutor();
/*      */ 
/*      */         
/* 1268 */         defaultAsyncExecutor.setCorePoolSize(this.asyncExecutorCorePoolSize);
/* 1269 */         defaultAsyncExecutor.setMaxPoolSize(this.asyncExecutorMaxPoolSize);
/* 1270 */         defaultAsyncExecutor.setKeepAliveTime(this.asyncExecutorThreadKeepAliveTime);
/*      */ 
/*      */         
/* 1273 */         if (this.asyncExecutorThreadPoolQueue != null) {
/* 1274 */           defaultAsyncExecutor.setThreadPoolQueue(this.asyncExecutorThreadPoolQueue);
/*      */         }
/* 1276 */         defaultAsyncExecutor.setQueueSize(this.asyncExecutorThreadPoolQueueSize);
/*      */ 
/*      */         
/* 1279 */         defaultAsyncExecutor.setDefaultTimerJobAcquireWaitTimeInMillis(this.asyncExecutorDefaultTimerJobAcquireWaitTime);
/* 1280 */         defaultAsyncExecutor.setDefaultAsyncJobAcquireWaitTimeInMillis(this.asyncExecutorDefaultAsyncJobAcquireWaitTime);
/*      */ 
/*      */         
/* 1283 */         defaultAsyncExecutor.setDefaultQueueSizeFullWaitTimeInMillis(this.asyncExecutorDefaultQueueSizeFullWaitTime);
/*      */ 
/*      */         
/* 1286 */         defaultAsyncExecutor.setTimerLockTimeInMillis(this.asyncExecutorTimerLockTimeInMillis);
/* 1287 */         defaultAsyncExecutor.setAsyncJobLockTimeInMillis(this.asyncExecutorAsyncJobLockTimeInMillis);
/* 1288 */         if (this.asyncExecutorLockOwner != null) {
/* 1289 */           defaultAsyncExecutor.setLockOwner(this.asyncExecutorLockOwner);
/*      */         }
/*      */ 
/*      */         
/* 1293 */         defaultAsyncExecutor.setRetryWaitTimeInMillis(this.asyncExecutorLockRetryWaitTimeInMillis);
/*      */ 
/*      */         
/* 1296 */         defaultAsyncExecutor.setSecondsToWaitOnShutdown(this.asyncExecutorSecondsToWaitOnShutdown);
/*      */         
/* 1298 */         this.asyncExecutor = (AsyncExecutor)defaultAsyncExecutor;
/*      */       } 
/*      */       
/* 1301 */       this.asyncExecutor.setCommandExecutor(this.commandExecutor);
/* 1302 */       this.asyncExecutor.setAutoActivate(this.asyncExecutorActivate);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void initHistoryLevel() {
/* 1309 */     if (this.historyLevel == null) {
/* 1310 */       this.historyLevel = HistoryLevel.getHistoryLevelForKey(getHistory());
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initIdGenerator() {
/* 1317 */     if (this.idGenerator == null) {
/* 1318 */       CommandExecutor idGeneratorCommandExecutor = null;
/* 1319 */       if (this.idGeneratorDataSource != null) {
/* 1320 */         StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration = new StandaloneProcessEngineConfiguration();
/* 1321 */         standaloneProcessEngineConfiguration.setDataSource(this.idGeneratorDataSource);
/* 1322 */         standaloneProcessEngineConfiguration.setDatabaseSchemaUpdate("false");
/* 1323 */         standaloneProcessEngineConfiguration.init();
/* 1324 */         idGeneratorCommandExecutor = standaloneProcessEngineConfiguration.getCommandExecutor();
/* 1325 */       } else if (this.idGeneratorDataSourceJndiName != null) {
/* 1326 */         StandaloneProcessEngineConfiguration standaloneProcessEngineConfiguration = new StandaloneProcessEngineConfiguration();
/* 1327 */         standaloneProcessEngineConfiguration.setDataSourceJndiName(this.idGeneratorDataSourceJndiName);
/* 1328 */         standaloneProcessEngineConfiguration.setDatabaseSchemaUpdate("false");
/* 1329 */         standaloneProcessEngineConfiguration.init();
/* 1330 */         idGeneratorCommandExecutor = standaloneProcessEngineConfiguration.getCommandExecutor();
/*      */       } else {
/* 1332 */         idGeneratorCommandExecutor = getCommandExecutor();
/*      */       } 
/*      */       
/* 1335 */       DbIdGenerator dbIdGenerator = new DbIdGenerator();
/* 1336 */       dbIdGenerator.setIdBlockSize(this.idBlockSize);
/* 1337 */       dbIdGenerator.setCommandExecutor(idGeneratorCommandExecutor);
/* 1338 */       dbIdGenerator.setCommandConfig(getDefaultCommandConfig().transactionRequiresNew());
/* 1339 */       this.idGenerator = (IdGenerator)dbIdGenerator;
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initCommandContextFactory() {
/* 1346 */     if (this.commandContextFactory == null) {
/* 1347 */       this.commandContextFactory = new CommandContextFactory();
/*      */     }
/* 1349 */     this.commandContextFactory.setProcessEngineConfiguration(this);
/*      */   }
/*      */   
/*      */   protected void initTransactionContextFactory() {
/* 1353 */     if (this.transactionContextFactory == null) {
/* 1354 */       this.transactionContextFactory = (TransactionContextFactory)new StandaloneMybatisTransactionContextFactory();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initVariableTypes() {
/* 1359 */     if (this.variableTypes == null) {
/* 1360 */       this.variableTypes = (VariableTypes)new DefaultVariableTypes();
/* 1361 */       if (this.customPreVariableTypes != null) {
/* 1362 */         for (VariableType customVariableType : this.customPreVariableTypes) {
/* 1363 */           this.variableTypes.addType(customVariableType);
/*      */         }
/*      */       }
/* 1366 */       this.variableTypes.addType((VariableType)new NullType());
/* 1367 */       this.variableTypes.addType((VariableType)new StringType(getMaxLengthString()));
/* 1368 */       this.variableTypes.addType((VariableType)new LongStringType(getMaxLengthString() + 1));
/* 1369 */       this.variableTypes.addType((VariableType)new BooleanType());
/* 1370 */       this.variableTypes.addType((VariableType)new ShortType());
/* 1371 */       this.variableTypes.addType((VariableType)new IntegerType());
/* 1372 */       this.variableTypes.addType((VariableType)new LongType());
/* 1373 */       this.variableTypes.addType((VariableType)new DateType());
/* 1374 */       this.variableTypes.addType((VariableType)new DoubleType());
/* 1375 */       this.variableTypes.addType((VariableType)new UUIDType());
/* 1376 */       this.variableTypes.addType((VariableType)new JsonType(getMaxLengthString(), this.objectMapper));
/* 1377 */       this.variableTypes.addType((VariableType)new LongJsonType(getMaxLengthString() + 1, this.objectMapper));
/* 1378 */       this.variableTypes.addType((VariableType)new ByteArrayType());
/* 1379 */       this.variableTypes.addType((VariableType)new SerializableType());
/* 1380 */       this.variableTypes.addType((VariableType)new CustomObjectType("item", ItemInstance.class));
/* 1381 */       this.variableTypes.addType((VariableType)new CustomObjectType("message", MessageInstance.class));
/* 1382 */       if (this.customPostVariableTypes != null) {
/* 1383 */         for (VariableType customVariableType : this.customPostVariableTypes) {
/* 1384 */           this.variableTypes.addType(customVariableType);
/*      */         }
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected int getMaxLengthString() {
/* 1391 */     if (this.maxLengthStringVariableType == -1) {
/* 1392 */       if ("oracle".equalsIgnoreCase(this.databaseType) == true) {
/* 1393 */         return 2000;
/*      */       }
/* 1395 */       return 4000;
/*      */     } 
/*      */     
/* 1398 */     return this.maxLengthStringVariableType;
/*      */   }
/*      */ 
/*      */   
/*      */   protected void initFormEngines() {
/* 1403 */     if (this.formEngines == null) {
/* 1404 */       this.formEngines = new HashMap<>();
/* 1405 */       JuelFormEngine juelFormEngine = new JuelFormEngine();
/* 1406 */       this.formEngines.put(null, juelFormEngine);
/* 1407 */       this.formEngines.put(juelFormEngine.getName(), juelFormEngine);
/*      */     } 
/* 1409 */     if (this.customFormEngines != null) {
/* 1410 */       for (FormEngine formEngine : this.customFormEngines) {
/* 1411 */         this.formEngines.put(formEngine.getName(), formEngine);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initFormTypes() {
/* 1417 */     if (this.formTypes == null) {
/* 1418 */       this.formTypes = new FormTypes();
/* 1419 */       this.formTypes.addFormType((AbstractFormType)new StringFormType());
/* 1420 */       this.formTypes.addFormType((AbstractFormType)new LongFormType());
/* 1421 */       this.formTypes.addFormType((AbstractFormType)new DateFormType("dd/MM/yyyy"));
/* 1422 */       this.formTypes.addFormType((AbstractFormType)new BooleanFormType());
/* 1423 */       this.formTypes.addFormType((AbstractFormType)new DoubleFormType());
/*      */     } 
/* 1425 */     if (this.customFormTypes != null) {
/* 1426 */       for (AbstractFormType customFormType : this.customFormTypes) {
/* 1427 */         this.formTypes.addFormType(customFormType);
/*      */       }
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initScriptingEngines() {
/* 1433 */     if (this.resolverFactories == null) {
/* 1434 */       this.resolverFactories = new ArrayList<>();
/* 1435 */       this.resolverFactories.add(new VariableScopeResolverFactory());
/* 1436 */       this.resolverFactories.add(new BeansResolverFactory());
/*      */     } 
/* 1438 */     if (this.scriptingEngines == null) {
/* 1439 */       this.scriptingEngines = new ScriptingEngines(new ScriptBindingsFactory(this.resolverFactories));
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initExpressionManager() {
/* 1444 */     if (this.expressionManager == null) {
/* 1445 */       this.expressionManager = new ExpressionManager(this.beans);
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initBusinessCalendarManager() {
/* 1450 */     if (this.businessCalendarManager == null) {
/* 1451 */       MapBusinessCalendarManager mapBusinessCalendarManager = new MapBusinessCalendarManager();
/* 1452 */       mapBusinessCalendarManager.addBusinessCalendar(DurationBusinessCalendar.NAME, (BusinessCalendar)new DurationBusinessCalendar((ClockReader)this.clock));
/* 1453 */       mapBusinessCalendarManager.addBusinessCalendar("dueDate", (BusinessCalendar)new DueDateBusinessCalendar((ClockReader)this.clock));
/* 1454 */       mapBusinessCalendarManager.addBusinessCalendar(CycleBusinessCalendar.NAME, (BusinessCalendar)new CycleBusinessCalendar((ClockReader)this.clock));
/*      */       
/* 1456 */       this.businessCalendarManager = (BusinessCalendarManager)mapBusinessCalendarManager;
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void initDelegateInterceptor() {
/* 1461 */     if (this.delegateInterceptor == null) {
/* 1462 */       this.delegateInterceptor = (DelegateInterceptor)new DefaultDelegateInterceptor();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initEventHandlers() {
/* 1467 */     if (this.eventHandlers == null) {
/* 1468 */       this.eventHandlers = new HashMap<>();
/*      */       
/* 1470 */       SignalEventHandler signalEventHander = new SignalEventHandler();
/* 1471 */       this.eventHandlers.put(signalEventHander.getEventHandlerType(), signalEventHander);
/*      */       
/* 1473 */       CompensationEventHandler compensationEventHandler = new CompensationEventHandler();
/* 1474 */       this.eventHandlers.put(compensationEventHandler.getEventHandlerType(), compensationEventHandler);
/*      */       
/* 1476 */       MessageEventHandler messageEventHandler = new MessageEventHandler();
/* 1477 */       this.eventHandlers.put(messageEventHandler.getEventHandlerType(), messageEventHandler);
/*      */     } 
/*      */     
/* 1480 */     if (this.customEventHandlers != null) {
/* 1481 */       for (EventHandler eventHandler : this.customEventHandlers) {
/* 1482 */         this.eventHandlers.put(eventHandler.getEventHandlerType(), eventHandler);
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   protected void initJpa() {
/* 1490 */     if (this.jpaPersistenceUnitName != null) {
/* 1491 */       this.jpaEntityManagerFactory = JpaHelper.createEntityManagerFactory(this.jpaPersistenceUnitName);
/*      */     }
/* 1493 */     if (this.jpaEntityManagerFactory != null) {
/* 1494 */       this.sessionFactories.put(EntityManagerSession.class, new EntityManagerSessionFactory(this.jpaEntityManagerFactory, this.jpaHandleTransaction, this.jpaCloseEntityManager));
/* 1495 */       VariableType jpaType = this.variableTypes.getVariableType("jpa-entity");
/*      */       
/* 1497 */       if (jpaType == null) {
/*      */         
/* 1499 */         int serializableIndex = this.variableTypes.getTypeIndex("serializable");
/* 1500 */         if (serializableIndex > -1) {
/* 1501 */           this.variableTypes.addType((VariableType)new JPAEntityVariableType(), serializableIndex);
/*      */         } else {
/* 1503 */           this.variableTypes.addType((VariableType)new JPAEntityVariableType());
/*      */         } 
/*      */       } 
/*      */       
/* 1507 */       jpaType = this.variableTypes.getVariableType("jpa-entity-list");
/*      */ 
/*      */       
/* 1510 */       if (jpaType == null) {
/* 1511 */         this.variableTypes.addType((VariableType)new JPAEntityListVariableType(), this.variableTypes.getTypeIndex("jpa-entity"));
/*      */       }
/*      */     } 
/*      */   }
/*      */   
/*      */   protected void initBeans() {
/* 1517 */     if (this.beans == null) {
/* 1518 */       this.beans = new HashMap<>();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initEventDispatcher() {
/* 1523 */     if (this.eventDispatcher == null) {
/* 1524 */       this.eventDispatcher = (ActivitiEventDispatcher)new ActivitiEventDispatcherImpl();
/*      */     }
/*      */     
/* 1527 */     this.eventDispatcher.setEnabled(this.enableEventDispatcher);
/*      */     
/* 1529 */     if (this.eventListeners != null) {
/* 1530 */       for (ActivitiEventListener listenerToAdd : this.eventListeners) {
/* 1531 */         this.eventDispatcher.addEventListener(listenerToAdd);
/*      */       }
/*      */     }
/*      */     
/* 1535 */     if (this.typedEventListeners != null) {
/* 1536 */       for (Map.Entry<String, List<ActivitiEventListener>> listenersToAdd : this.typedEventListeners.entrySet()) {
/*      */         
/* 1538 */         ActivitiEventType[] types = ActivitiEventType.getTypesFromString(listenersToAdd.getKey());
/*      */         
/* 1540 */         for (ActivitiEventListener listenerToAdd : listenersToAdd.getValue()) {
/* 1541 */           this.eventDispatcher.addEventListener(listenerToAdd, types);
/*      */         }
/*      */       } 
/*      */     }
/*      */   }
/*      */ 
/*      */   
/*      */   protected void initProcessValidator() {
/* 1549 */     if (this.processValidator == null) {
/* 1550 */       this.processValidator = (new ProcessValidatorFactory()).createDefaultProcessValidator();
/*      */     }
/*      */   }
/*      */   
/*      */   protected void initDatabaseEventLogging() {
/* 1555 */     if (this.enableDatabaseEventLogging)
/*      */     {
/*      */       
/* 1558 */       getEventDispatcher().addEventListener((ActivitiEventListener)new EventLogger(this.clock, this.objectMapper));
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandConfig getDefaultCommandConfig() {
/* 1565 */     return this.defaultCommandConfig;
/*      */   }
/*      */   
/*      */   public void setDefaultCommandConfig(CommandConfig defaultCommandConfig) {
/* 1569 */     this.defaultCommandConfig = defaultCommandConfig;
/*      */   }
/*      */   
/*      */   public CommandConfig getSchemaCommandConfig() {
/* 1573 */     return this.schemaCommandConfig;
/*      */   }
/*      */   
/*      */   public void setSchemaCommandConfig(CommandConfig schemaCommandConfig) {
/* 1577 */     this.schemaCommandConfig = schemaCommandConfig;
/*      */   }
/*      */   
/*      */   public CommandInterceptor getCommandInvoker() {
/* 1581 */     return this.commandInvoker;
/*      */   }
/*      */   
/*      */   public void setCommandInvoker(CommandInterceptor commandInvoker) {
/* 1585 */     this.commandInvoker = commandInvoker;
/*      */   }
/*      */   
/*      */   public List<CommandInterceptor> getCustomPreCommandInterceptors() {
/* 1589 */     return this.customPreCommandInterceptors;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomPreCommandInterceptors(List<CommandInterceptor> customPreCommandInterceptors) {
/* 1593 */     this.customPreCommandInterceptors = customPreCommandInterceptors;
/* 1594 */     return this;
/*      */   }
/*      */   
/*      */   public List<CommandInterceptor> getCustomPostCommandInterceptors() {
/* 1598 */     return this.customPostCommandInterceptors;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomPostCommandInterceptors(List<CommandInterceptor> customPostCommandInterceptors) {
/* 1602 */     this.customPostCommandInterceptors = customPostCommandInterceptors;
/* 1603 */     return this;
/*      */   }
/*      */   
/*      */   public List<CommandInterceptor> getCommandInterceptors() {
/* 1607 */     return this.commandInterceptors;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCommandInterceptors(List<CommandInterceptor> commandInterceptors) {
/* 1611 */     this.commandInterceptors = commandInterceptors;
/* 1612 */     return this;
/*      */   }
/*      */   
/*      */   public CommandExecutor getCommandExecutor() {
/* 1616 */     return this.commandExecutor;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCommandExecutor(CommandExecutor commandExecutor) {
/* 1620 */     this.commandExecutor = commandExecutor;
/* 1621 */     return this;
/*      */   }
/*      */   
/*      */   public RepositoryService getRepositoryService() {
/* 1625 */     return this.repositoryService;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setRepositoryService(RepositoryService repositoryService) {
/* 1629 */     this.repositoryService = repositoryService;
/* 1630 */     return this;
/*      */   }
/*      */   
/*      */   public RuntimeService getRuntimeService() {
/* 1634 */     return this.runtimeService;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setRuntimeService(RuntimeService runtimeService) {
/* 1638 */     this.runtimeService = runtimeService;
/* 1639 */     return this;
/*      */   }
/*      */   
/*      */   public HistoryService getHistoryService() {
/* 1643 */     return this.historyService;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setHistoryService(HistoryService historyService) {
/* 1647 */     this.historyService = historyService;
/* 1648 */     return this;
/*      */   }
/*      */   
/*      */   public IdentityService getIdentityService() {
/* 1652 */     return this.identityService;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setIdentityService(IdentityService identityService) {
/* 1656 */     this.identityService = identityService;
/* 1657 */     return this;
/*      */   }
/*      */   
/*      */   public TaskService getTaskService() {
/* 1661 */     return this.taskService;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setTaskService(TaskService taskService) {
/* 1665 */     this.taskService = taskService;
/* 1666 */     return this;
/*      */   }
/*      */   
/*      */   public FormService getFormService() {
/* 1670 */     return this.formService;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setFormService(FormService formService) {
/* 1674 */     this.formService = formService;
/* 1675 */     return this;
/*      */   }
/*      */   
/*      */   public ManagementService getManagementService() {
/* 1679 */     return this.managementService;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setManagementService(ManagementService managementService) {
/* 1683 */     this.managementService = managementService;
/* 1684 */     return this;
/*      */   }
/*      */   
/*      */   public DynamicBpmnService getDynamicBpmnService() {
/* 1688 */     return this.dynamicBpmnService;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setDynamicBpmnService(DynamicBpmnService dynamicBpmnService) {
/* 1692 */     this.dynamicBpmnService = dynamicBpmnService;
/* 1693 */     return this;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfiguration getProcessEngineConfiguration() {
/* 1697 */     return this;
/*      */   }
/*      */   
/*      */   public Map<Class<?>, SessionFactory> getSessionFactories() {
/* 1701 */     return this.sessionFactories;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setSessionFactories(Map<Class<?>, SessionFactory> sessionFactories) {
/* 1705 */     this.sessionFactories = sessionFactories;
/* 1706 */     return this;
/*      */   }
/*      */   
/*      */   public List<ProcessEngineConfigurator> getConfigurators() {
/* 1710 */     return this.configurators;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl addConfigurator(ProcessEngineConfigurator configurator) {
/* 1714 */     if (this.configurators == null) {
/* 1715 */       this.configurators = new ArrayList<>();
/*      */     }
/* 1717 */     this.configurators.add(configurator);
/* 1718 */     return this;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setConfigurators(List<ProcessEngineConfigurator> configurators) {
/* 1722 */     this.configurators = configurators;
/* 1723 */     return this;
/*      */   }
/*      */   
/*      */   public void setEnableConfiguratorServiceLoader(boolean enableConfiguratorServiceLoader) {
/* 1727 */     this.enableConfiguratorServiceLoader = enableConfiguratorServiceLoader;
/*      */   }
/*      */   
/*      */   public List<ProcessEngineConfigurator> getAllConfigurators() {
/* 1731 */     return this.allConfigurators;
/*      */   }
/*      */   
/*      */   public BpmnDeployer getBpmnDeployer() {
/* 1735 */     return this.bpmnDeployer;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setBpmnDeployer(BpmnDeployer bpmnDeployer) {
/* 1739 */     this.bpmnDeployer = bpmnDeployer;
/* 1740 */     return this;
/*      */   }
/*      */   
/*      */   public BpmnParser getBpmnParser() {
/* 1744 */     return this.bpmnParser;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setBpmnParser(BpmnParser bpmnParser) {
/* 1748 */     this.bpmnParser = bpmnParser;
/* 1749 */     return this;
/*      */   }
/*      */   
/*      */   public List<Deployer> getDeployers() {
/* 1753 */     return this.deployers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setDeployers(List<Deployer> deployers) {
/* 1757 */     this.deployers = deployers;
/* 1758 */     return this;
/*      */   }
/*      */   
/*      */   public IdGenerator getIdGenerator() {
/* 1762 */     return this.idGenerator;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setIdGenerator(IdGenerator idGenerator) {
/* 1766 */     this.idGenerator = idGenerator;
/* 1767 */     return this;
/*      */   }
/*      */   
/*      */   public String getWsSyncFactoryClassName() {
/* 1771 */     return this.wsSyncFactoryClassName;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setWsSyncFactoryClassName(String wsSyncFactoryClassName) {
/* 1775 */     this.wsSyncFactoryClassName = wsSyncFactoryClassName;
/* 1776 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ProcessEngineConfiguration addWsEndpointAddress(QName endpointName, URL address) {
/* 1786 */     this.wsOverridenEndpointAddresses.put(endpointName, address);
/* 1787 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ProcessEngineConfiguration removeWsEndpointAddress(QName endpointName) {
/* 1796 */     this.wsOverridenEndpointAddresses.remove(endpointName);
/* 1797 */     return this;
/*      */   }
/*      */   
/*      */   public ConcurrentMap<QName, URL> getWsOverridenEndpointAddresses() {
/* 1801 */     return this.wsOverridenEndpointAddresses;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfiguration setWsOverridenEndpointAddresses(ConcurrentMap<QName, URL> wsOverridenEndpointAdress) {
/* 1805 */     this.wsOverridenEndpointAddresses.putAll(wsOverridenEndpointAdress);
/* 1806 */     return this;
/*      */   }
/*      */   
/*      */   public Map<String, FormEngine> getFormEngines() {
/* 1810 */     return this.formEngines;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setFormEngines(Map<String, FormEngine> formEngines) {
/* 1814 */     this.formEngines = formEngines;
/* 1815 */     return this;
/*      */   }
/*      */   
/*      */   public FormTypes getFormTypes() {
/* 1819 */     return this.formTypes;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setFormTypes(FormTypes formTypes) {
/* 1823 */     this.formTypes = formTypes;
/* 1824 */     return this;
/*      */   }
/*      */   
/*      */   public ScriptingEngines getScriptingEngines() {
/* 1828 */     return this.scriptingEngines;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setScriptingEngines(ScriptingEngines scriptingEngines) {
/* 1832 */     this.scriptingEngines = scriptingEngines;
/* 1833 */     return this;
/*      */   }
/*      */   
/*      */   public VariableTypes getVariableTypes() {
/* 1837 */     return this.variableTypes;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setVariableTypes(VariableTypes variableTypes) {
/* 1841 */     this.variableTypes = variableTypes;
/* 1842 */     return this;
/*      */   }
/*      */   
/*      */   public ExpressionManager getExpressionManager() {
/* 1846 */     return this.expressionManager;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setExpressionManager(ExpressionManager expressionManager) {
/* 1850 */     this.expressionManager = expressionManager;
/* 1851 */     return this;
/*      */   }
/*      */   
/*      */   public BusinessCalendarManager getBusinessCalendarManager() {
/* 1855 */     return this.businessCalendarManager;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setBusinessCalendarManager(BusinessCalendarManager businessCalendarManager) {
/* 1859 */     this.businessCalendarManager = businessCalendarManager;
/* 1860 */     return this;
/*      */   }
/*      */   
/*      */   public int getExecutionQueryLimit() {
/* 1864 */     return this.executionQueryLimit;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setExecutionQueryLimit(int executionQueryLimit) {
/* 1868 */     this.executionQueryLimit = executionQueryLimit;
/* 1869 */     return this;
/*      */   }
/*      */   
/*      */   public int getTaskQueryLimit() {
/* 1873 */     return this.taskQueryLimit;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setTaskQueryLimit(int taskQueryLimit) {
/* 1877 */     this.taskQueryLimit = taskQueryLimit;
/* 1878 */     return this;
/*      */   }
/*      */   
/*      */   public int getHistoricTaskQueryLimit() {
/* 1882 */     return this.historicTaskQueryLimit;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setHistoricTaskQueryLimit(int historicTaskQueryLimit) {
/* 1886 */     this.historicTaskQueryLimit = historicTaskQueryLimit;
/* 1887 */     return this;
/*      */   }
/*      */   
/*      */   public int getHistoricProcessInstancesQueryLimit() {
/* 1891 */     return this.historicProcessInstancesQueryLimit;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setHistoricProcessInstancesQueryLimit(int historicProcessInstancesQueryLimit) {
/* 1895 */     this.historicProcessInstancesQueryLimit = historicProcessInstancesQueryLimit;
/* 1896 */     return this;
/*      */   }
/*      */   
/*      */   public CommandContextFactory getCommandContextFactory() {
/* 1900 */     return this.commandContextFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCommandContextFactory(CommandContextFactory commandContextFactory) {
/* 1904 */     this.commandContextFactory = commandContextFactory;
/* 1905 */     return this;
/*      */   }
/*      */   
/*      */   public TransactionContextFactory getTransactionContextFactory() {
/* 1909 */     return this.transactionContextFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setTransactionContextFactory(TransactionContextFactory transactionContextFactory) {
/* 1913 */     this.transactionContextFactory = transactionContextFactory;
/* 1914 */     return this;
/*      */   }
/*      */   
/*      */   public List<Deployer> getCustomPreDeployers() {
/* 1918 */     return this.customPreDeployers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomPreDeployers(List<Deployer> customPreDeployers) {
/* 1922 */     this.customPreDeployers = customPreDeployers;
/* 1923 */     return this;
/*      */   }
/*      */   
/*      */   public List<Deployer> getCustomPostDeployers() {
/* 1927 */     return this.customPostDeployers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomPostDeployers(List<Deployer> customPostDeployers) {
/* 1931 */     this.customPostDeployers = customPostDeployers;
/* 1932 */     return this;
/*      */   }
/*      */   
/*      */   public Map<String, JobHandler> getJobHandlers() {
/* 1936 */     return this.jobHandlers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setJobHandlers(Map<String, JobHandler> jobHandlers) {
/* 1940 */     this.jobHandlers = jobHandlers;
/* 1941 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorCorePoolSize() {
/* 1945 */     return this.asyncExecutorCorePoolSize;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorCorePoolSize(int asyncExecutorCorePoolSize) {
/* 1949 */     this.asyncExecutorCorePoolSize = asyncExecutorCorePoolSize;
/* 1950 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorMaxPoolSize() {
/* 1954 */     return this.asyncExecutorMaxPoolSize;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorMaxPoolSize(int asyncExecutorMaxPoolSize) {
/* 1958 */     this.asyncExecutorMaxPoolSize = asyncExecutorMaxPoolSize;
/* 1959 */     return this;
/*      */   }
/*      */   
/*      */   public long getAsyncExecutorThreadKeepAliveTime() {
/* 1963 */     return this.asyncExecutorThreadKeepAliveTime;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorThreadKeepAliveTime(long asyncExecutorThreadKeepAliveTime) {
/* 1967 */     this.asyncExecutorThreadKeepAliveTime = asyncExecutorThreadKeepAliveTime;
/* 1968 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorThreadPoolQueueSize() {
/* 1972 */     return this.asyncExecutorThreadPoolQueueSize;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorThreadPoolQueueSize(int asyncExecutorThreadPoolQueueSize) {
/* 1976 */     this.asyncExecutorThreadPoolQueueSize = asyncExecutorThreadPoolQueueSize;
/* 1977 */     return this;
/*      */   }
/*      */   
/*      */   public BlockingQueue<Runnable> getAsyncExecutorThreadPoolQueue() {
/* 1981 */     return this.asyncExecutorThreadPoolQueue;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorThreadPoolQueue(BlockingQueue<Runnable> asyncExecutorThreadPoolQueue) {
/* 1985 */     this.asyncExecutorThreadPoolQueue = asyncExecutorThreadPoolQueue;
/* 1986 */     return this;
/*      */   }
/*      */   
/*      */   public long getAsyncExecutorSecondsToWaitOnShutdown() {
/* 1990 */     return this.asyncExecutorSecondsToWaitOnShutdown;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorSecondsToWaitOnShutdown(long asyncExecutorSecondsToWaitOnShutdown) {
/* 1994 */     this.asyncExecutorSecondsToWaitOnShutdown = asyncExecutorSecondsToWaitOnShutdown;
/* 1995 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorMaxTimerJobsPerAcquisition() {
/* 1999 */     return this.asyncExecutorMaxTimerJobsPerAcquisition;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorMaxTimerJobsPerAcquisition(int asyncExecutorMaxTimerJobsPerAcquisition) {
/* 2003 */     this.asyncExecutorMaxTimerJobsPerAcquisition = asyncExecutorMaxTimerJobsPerAcquisition;
/* 2004 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorMaxAsyncJobsDuePerAcquisition() {
/* 2008 */     return this.asyncExecutorMaxAsyncJobsDuePerAcquisition;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorMaxAsyncJobsDuePerAcquisition(int asyncExecutorMaxAsyncJobsDuePerAcquisition) {
/* 2012 */     this.asyncExecutorMaxAsyncJobsDuePerAcquisition = asyncExecutorMaxAsyncJobsDuePerAcquisition;
/* 2013 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorTimerJobAcquireWaitTime() {
/* 2017 */     return this.asyncExecutorDefaultTimerJobAcquireWaitTime;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorDefaultTimerJobAcquireWaitTime(int asyncExecutorDefaultTimerJobAcquireWaitTime) {
/* 2021 */     this.asyncExecutorDefaultTimerJobAcquireWaitTime = asyncExecutorDefaultTimerJobAcquireWaitTime;
/* 2022 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorDefaultAsyncJobAcquireWaitTime() {
/* 2026 */     return this.asyncExecutorDefaultAsyncJobAcquireWaitTime;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorDefaultAsyncJobAcquireWaitTime(int asyncExecutorDefaultAsyncJobAcquireWaitTime) {
/* 2030 */     this.asyncExecutorDefaultAsyncJobAcquireWaitTime = asyncExecutorDefaultAsyncJobAcquireWaitTime;
/* 2031 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorDefaultQueueSizeFullWaitTime() {
/* 2035 */     return this.asyncExecutorDefaultQueueSizeFullWaitTime;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorDefaultQueueSizeFullWaitTime(int asyncExecutorDefaultQueueSizeFullWaitTime) {
/* 2039 */     this.asyncExecutorDefaultQueueSizeFullWaitTime = asyncExecutorDefaultQueueSizeFullWaitTime;
/* 2040 */     return this;
/*      */   }
/*      */   
/*      */   public String getAsyncExecutorLockOwner() {
/* 2044 */     return this.asyncExecutorLockOwner;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorLockOwner(String asyncExecutorLockOwner) {
/* 2048 */     this.asyncExecutorLockOwner = asyncExecutorLockOwner;
/* 2049 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorTimerLockTimeInMillis() {
/* 2053 */     return this.asyncExecutorTimerLockTimeInMillis;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorTimerLockTimeInMillis(int asyncExecutorTimerLockTimeInMillis) {
/* 2057 */     this.asyncExecutorTimerLockTimeInMillis = asyncExecutorTimerLockTimeInMillis;
/* 2058 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorAsyncJobLockTimeInMillis() {
/* 2062 */     return this.asyncExecutorAsyncJobLockTimeInMillis;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorAsyncJobLockTimeInMillis(int asyncExecutorAsyncJobLockTimeInMillis) {
/* 2066 */     this.asyncExecutorAsyncJobLockTimeInMillis = asyncExecutorAsyncJobLockTimeInMillis;
/* 2067 */     return this;
/*      */   }
/*      */   
/*      */   public int getAsyncExecutorLockRetryWaitTimeInMillis() {
/* 2071 */     return this.asyncExecutorLockRetryWaitTimeInMillis;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorLockRetryWaitTimeInMillis(int asyncExecutorLockRetryWaitTimeInMillis) {
/* 2075 */     this.asyncExecutorLockRetryWaitTimeInMillis = asyncExecutorLockRetryWaitTimeInMillis;
/* 2076 */     return this;
/*      */   }
/*      */   
/*      */   public ExecuteAsyncRunnableFactory getAsyncExecutorExecuteAsyncRunnableFactory() {
/* 2080 */     return this.asyncExecutorExecuteAsyncRunnableFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setAsyncExecutorExecuteAsyncRunnableFactory(ExecuteAsyncRunnableFactory asyncExecutorExecuteAsyncRunnableFactory) {
/* 2084 */     this.asyncExecutorExecuteAsyncRunnableFactory = asyncExecutorExecuteAsyncRunnableFactory;
/* 2085 */     return this;
/*      */   }
/*      */   
/*      */   public SqlSessionFactory getSqlSessionFactory() {
/* 2089 */     return this.sqlSessionFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
/* 2093 */     this.sqlSessionFactory = sqlSessionFactory;
/* 2094 */     return this;
/*      */   }
/*      */   
/*      */   public DbSqlSessionFactory getDbSqlSessionFactory() {
/* 2098 */     return this.dbSqlSessionFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setDbSqlSessionFactory(DbSqlSessionFactory dbSqlSessionFactory) {
/* 2102 */     this.dbSqlSessionFactory = dbSqlSessionFactory;
/* 2103 */     return this;
/*      */   }
/*      */   
/*      */   public TransactionFactory getTransactionFactory() {
/* 2107 */     return this.transactionFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setTransactionFactory(TransactionFactory transactionFactory) {
/* 2111 */     this.transactionFactory = transactionFactory;
/* 2112 */     return this;
/*      */   }
/*      */   
/*      */   public List<SessionFactory> getCustomSessionFactories() {
/* 2116 */     return this.customSessionFactories;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomSessionFactories(List<SessionFactory> customSessionFactories) {
/* 2120 */     this.customSessionFactories = customSessionFactories;
/* 2121 */     return this;
/*      */   }
/*      */   
/*      */   public List<JobHandler> getCustomJobHandlers() {
/* 2125 */     return this.customJobHandlers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomJobHandlers(List<JobHandler> customJobHandlers) {
/* 2129 */     this.customJobHandlers = customJobHandlers;
/* 2130 */     return this;
/*      */   }
/*      */   
/*      */   public List<FormEngine> getCustomFormEngines() {
/* 2134 */     return this.customFormEngines;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomFormEngines(List<FormEngine> customFormEngines) {
/* 2138 */     this.customFormEngines = customFormEngines;
/* 2139 */     return this;
/*      */   }
/*      */   
/*      */   public List<AbstractFormType> getCustomFormTypes() {
/* 2143 */     return this.customFormTypes;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomFormTypes(List<AbstractFormType> customFormTypes) {
/* 2147 */     this.customFormTypes = customFormTypes;
/* 2148 */     return this;
/*      */   }
/*      */   
/*      */   public List<String> getCustomScriptingEngineClasses() {
/* 2152 */     return this.customScriptingEngineClasses;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomScriptingEngineClasses(List<String> customScriptingEngineClasses) {
/* 2156 */     this.customScriptingEngineClasses = customScriptingEngineClasses;
/* 2157 */     return this;
/*      */   }
/*      */   
/*      */   public List<VariableType> getCustomPreVariableTypes() {
/* 2161 */     return this.customPreVariableTypes;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomPreVariableTypes(List<VariableType> customPreVariableTypes) {
/* 2165 */     this.customPreVariableTypes = customPreVariableTypes;
/* 2166 */     return this;
/*      */   }
/*      */   
/*      */   public List<VariableType> getCustomPostVariableTypes() {
/* 2170 */     return this.customPostVariableTypes;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomPostVariableTypes(List<VariableType> customPostVariableTypes) {
/* 2174 */     this.customPostVariableTypes = customPostVariableTypes;
/* 2175 */     return this;
/*      */   }
/*      */   
/*      */   public List<BpmnParseHandler> getPreBpmnParseHandlers() {
/* 2179 */     return this.preBpmnParseHandlers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setPreBpmnParseHandlers(List<BpmnParseHandler> preBpmnParseHandlers) {
/* 2183 */     this.preBpmnParseHandlers = preBpmnParseHandlers;
/* 2184 */     return this;
/*      */   }
/*      */   
/*      */   public List<BpmnParseHandler> getCustomDefaultBpmnParseHandlers() {
/* 2188 */     return this.customDefaultBpmnParseHandlers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomDefaultBpmnParseHandlers(List<BpmnParseHandler> customDefaultBpmnParseHandlers) {
/* 2192 */     this.customDefaultBpmnParseHandlers = customDefaultBpmnParseHandlers;
/* 2193 */     return this;
/*      */   }
/*      */   
/*      */   public List<BpmnParseHandler> getPostBpmnParseHandlers() {
/* 2197 */     return this.postBpmnParseHandlers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setPostBpmnParseHandlers(List<BpmnParseHandler> postBpmnParseHandlers) {
/* 2201 */     this.postBpmnParseHandlers = postBpmnParseHandlers;
/* 2202 */     return this;
/*      */   }
/*      */   
/*      */   public ActivityBehaviorFactory getActivityBehaviorFactory() {
/* 2206 */     return this.activityBehaviorFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setActivityBehaviorFactory(ActivityBehaviorFactory activityBehaviorFactory) {
/* 2210 */     this.activityBehaviorFactory = activityBehaviorFactory;
/* 2211 */     return this;
/*      */   }
/*      */   
/*      */   public ListenerFactory getListenerFactory() {
/* 2215 */     return this.listenerFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setListenerFactory(ListenerFactory listenerFactory) {
/* 2219 */     this.listenerFactory = listenerFactory;
/* 2220 */     return this;
/*      */   }
/*      */   
/*      */   public BpmnParseFactory getBpmnParseFactory() {
/* 2224 */     return this.bpmnParseFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setBpmnParseFactory(BpmnParseFactory bpmnParseFactory) {
/* 2228 */     this.bpmnParseFactory = bpmnParseFactory;
/* 2229 */     return this;
/*      */   }
/*      */   
/*      */   public Map<Object, Object> getBeans() {
/* 2233 */     return this.beans;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setBeans(Map<Object, Object> beans) {
/* 2237 */     this.beans = beans;
/* 2238 */     return this;
/*      */   }
/*      */   
/*      */   public List<ResolverFactory> getResolverFactories() {
/* 2242 */     return this.resolverFactories;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setResolverFactories(List<ResolverFactory> resolverFactories) {
/* 2246 */     this.resolverFactories = resolverFactories;
/* 2247 */     return this;
/*      */   }
/*      */   
/*      */   public DeploymentManager getDeploymentManager() {
/* 2251 */     return this.deploymentManager;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setDeploymentManager(DeploymentManager deploymentManager) {
/* 2255 */     this.deploymentManager = deploymentManager;
/* 2256 */     return this;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setDelegateInterceptor(DelegateInterceptor delegateInterceptor) {
/* 2260 */     this.delegateInterceptor = delegateInterceptor;
/* 2261 */     return this;
/*      */   }
/*      */   
/*      */   public DelegateInterceptor getDelegateInterceptor() {
/* 2265 */     return this.delegateInterceptor;
/*      */   }
/*      */   
/*      */   public RejectedJobsHandler getCustomRejectedJobsHandler() {
/* 2269 */     return this.customRejectedJobsHandler;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomRejectedJobsHandler(RejectedJobsHandler customRejectedJobsHandler) {
/* 2273 */     this.customRejectedJobsHandler = customRejectedJobsHandler;
/* 2274 */     return this;
/*      */   }
/*      */   
/*      */   public EventHandler getEventHandler(String eventType) {
/* 2278 */     return this.eventHandlers.get(eventType);
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setEventHandlers(Map<String, EventHandler> eventHandlers) {
/* 2282 */     this.eventHandlers = eventHandlers;
/* 2283 */     return this;
/*      */   }
/*      */   
/*      */   public Map<String, EventHandler> getEventHandlers() {
/* 2287 */     return this.eventHandlers;
/*      */   }
/*      */   
/*      */   public List<EventHandler> getCustomEventHandlers() {
/* 2291 */     return this.customEventHandlers;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setCustomEventHandlers(List<EventHandler> customEventHandlers) {
/* 2295 */     this.customEventHandlers = customEventHandlers;
/* 2296 */     return this;
/*      */   }
/*      */   
/*      */   public FailedJobCommandFactory getFailedJobCommandFactory() {
/* 2300 */     return this.failedJobCommandFactory;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setFailedJobCommandFactory(FailedJobCommandFactory failedJobCommandFactory) {
/* 2304 */     this.failedJobCommandFactory = failedJobCommandFactory;
/* 2305 */     return this;
/*      */   }
/*      */   
/*      */   public DataSource getIdGeneratorDataSource() {
/* 2309 */     return this.idGeneratorDataSource;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setIdGeneratorDataSource(DataSource idGeneratorDataSource) {
/* 2313 */     this.idGeneratorDataSource = idGeneratorDataSource;
/* 2314 */     return this;
/*      */   }
/*      */   
/*      */   public String getIdGeneratorDataSourceJndiName() {
/* 2318 */     return this.idGeneratorDataSourceJndiName;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setIdGeneratorDataSourceJndiName(String idGeneratorDataSourceJndiName) {
/* 2322 */     this.idGeneratorDataSourceJndiName = idGeneratorDataSourceJndiName;
/* 2323 */     return this;
/*      */   }
/*      */   
/*      */   public int getBatchSizeProcessInstances() {
/* 2327 */     return this.batchSizeProcessInstances;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setBatchSizeProcessInstances(int batchSizeProcessInstances) {
/* 2331 */     this.batchSizeProcessInstances = batchSizeProcessInstances;
/* 2332 */     return this;
/*      */   }
/*      */   
/*      */   public int getBatchSizeTasks() {
/* 2336 */     return this.batchSizeTasks;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setBatchSizeTasks(int batchSizeTasks) {
/* 2340 */     this.batchSizeTasks = batchSizeTasks;
/* 2341 */     return this;
/*      */   }
/*      */   
/*      */   public int getProcessDefinitionCacheLimit() {
/* 2345 */     return this.processDefinitionCacheLimit;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setProcessDefinitionCacheLimit(int processDefinitionCacheLimit) {
/* 2349 */     this.processDefinitionCacheLimit = processDefinitionCacheLimit;
/* 2350 */     return this;
/*      */   }
/*      */   
/*      */   public DeploymentCache<ProcessDefinitionEntity> getProcessDefinitionCache() {
/* 2354 */     return this.processDefinitionCache;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setProcessDefinitionCache(DeploymentCache<ProcessDefinitionEntity> processDefinitionCache) {
/* 2358 */     this.processDefinitionCache = processDefinitionCache;
/* 2359 */     return this;
/*      */   }
/*      */   
/*      */   public int getKnowledgeBaseCacheLimit() {
/* 2363 */     return this.knowledgeBaseCacheLimit;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setKnowledgeBaseCacheLimit(int knowledgeBaseCacheLimit) {
/* 2367 */     this.knowledgeBaseCacheLimit = knowledgeBaseCacheLimit;
/* 2368 */     return this;
/*      */   }
/*      */   
/*      */   public DeploymentCache<Object> getKnowledgeBaseCache() {
/* 2372 */     return this.knowledgeBaseCache;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setKnowledgeBaseCache(DeploymentCache<Object> knowledgeBaseCache) {
/* 2376 */     this.knowledgeBaseCache = knowledgeBaseCache;
/* 2377 */     return this;
/*      */   }
/*      */   
/*      */   public boolean isEnableSafeBpmnXml() {
/* 2381 */     return this.enableSafeBpmnXml;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setEnableSafeBpmnXml(boolean enableSafeBpmnXml) {
/* 2385 */     this.enableSafeBpmnXml = enableSafeBpmnXml;
/* 2386 */     return this;
/*      */   }
/*      */   
/*      */   public ActivitiEventDispatcher getEventDispatcher() {
/* 2390 */     return this.eventDispatcher;
/*      */   }
/*      */   
/*      */   public void setEventDispatcher(ActivitiEventDispatcher eventDispatcher) {
/* 2394 */     this.eventDispatcher = eventDispatcher;
/*      */   }
/*      */   
/*      */   public void setEnableEventDispatcher(boolean enableEventDispatcher) {
/* 2398 */     this.enableEventDispatcher = enableEventDispatcher;
/*      */   }
/*      */   
/*      */   public void setTypedEventListeners(Map<String, List<ActivitiEventListener>> typedListeners) {
/* 2402 */     this.typedEventListeners = typedListeners;
/*      */   }
/*      */   
/*      */   public void setEventListeners(List<ActivitiEventListener> eventListeners) {
/* 2406 */     this.eventListeners = eventListeners;
/*      */   }
/*      */   
/*      */   public ProcessValidator getProcessValidator() {
/* 2410 */     return this.processValidator;
/*      */   }
/*      */   
/*      */   public void setProcessValidator(ProcessValidator processValidator) {
/* 2414 */     this.processValidator = processValidator;
/*      */   }
/*      */   
/*      */   public boolean isEnableEventDispatcher() {
/* 2418 */     return this.enableEventDispatcher;
/*      */   }
/*      */   
/*      */   public boolean isEnableDatabaseEventLogging() {
/* 2422 */     return this.enableDatabaseEventLogging;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setEnableDatabaseEventLogging(boolean enableDatabaseEventLogging) {
/* 2426 */     this.enableDatabaseEventLogging = enableDatabaseEventLogging;
/* 2427 */     return this;
/*      */   }
/*      */   
/*      */   public int getMaxLengthStringVariableType() {
/* 2431 */     return this.maxLengthStringVariableType;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setMaxLengthStringVariableType(int maxLengthStringVariableType) {
/* 2435 */     this.maxLengthStringVariableType = maxLengthStringVariableType;
/* 2436 */     return this;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setBulkInsertEnabled(boolean isBulkInsertEnabled) {
/* 2440 */     this.isBulkInsertEnabled = isBulkInsertEnabled;
/* 2441 */     return this;
/*      */   }
/*      */   
/*      */   public boolean isBulkInsertEnabled() {
/* 2445 */     return this.isBulkInsertEnabled;
/*      */   }
/*      */   
/*      */   public int getMaxNrOfStatementsInBulkInsert() {
/* 2449 */     return this.maxNrOfStatementsInBulkInsert;
/*      */   }
/*      */   
/*      */   public ProcessEngineConfigurationImpl setMaxNrOfStatementsInBulkInsert(int maxNrOfStatementsInBulkInsert) {
/* 2453 */     this.maxNrOfStatementsInBulkInsert = maxNrOfStatementsInBulkInsert;
/* 2454 */     return this;
/*      */   }
/*      */   
/*      */   public DelegateExpressionFieldInjectionMode getDelegateExpressionFieldInjectionMode() {
/* 2458 */     return this.delegateExpressionFieldInjectionMode;
/*      */   }
/*      */   
/*      */   public void setDelegateExpressionFieldInjectionMode(DelegateExpressionFieldInjectionMode delegateExpressionFieldInjectionMode) {
/* 2462 */     this.delegateExpressionFieldInjectionMode = delegateExpressionFieldInjectionMode;
/*      */   }
/*      */   
/*      */   public ObjectMapper getObjectMapper() {
/* 2466 */     return this.objectMapper;
/*      */   }
/*      */ }


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/wf-act/v1.0.176.bjsj.1/wf-act-v1.0.176.bjsj.1.jar!/org/activiti/engine/impl/cfg/ProcessEngineConfigurationImpl.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */