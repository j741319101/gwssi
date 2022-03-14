package com.dstz.bpm.plugin.usercalc.user.def;

public class ExecutorVar {
   public static final String SOURCE_BO = "BO";
   public static final String SOURCE_FLOW_VAR = "flowVar";
   public static final String EXECUTOR_TYPE_USER = "user";
   public static final String EXECUTOR_TYPE_GROUP = "group";
   private String source = "";
   private String name = "";
   private String executorType = "";
   private String userValType = "";
   private String groupValType = "";
   private String dimension = "";
   private String value;

   public ExecutorVar() {
   }

   public ExecutorVar(String source, String name, String executorType, String userValType, String groupValType, String dimension) {
      this.source = source;
      this.name = name;
      this.executorType = executorType;
      this.userValType = userValType;
      this.groupValType = groupValType;
      this.dimension = dimension;
   }

   public String getSource() {
      return this.source;
   }

   public void setSource(String source) {
      this.source = source;
   }

   public String getName() {
      return this.name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public String getExecutorType() {
      return this.executorType;
   }

   public void setExecutorType(String executorType) {
      this.executorType = executorType;
   }

   public String getUserValType() {
      return this.userValType;
   }

   public void setUserValType(String userValType) {
      this.userValType = userValType;
   }

   public String getGroupValType() {
      return this.groupValType;
   }

   public void setGroupValType(String groupValType) {
      this.groupValType = groupValType;
   }

   public String getDimension() {
      return this.dimension;
   }

   public void setDimension(String dimension) {
      this.dimension = dimension;
   }

   public String getValue() {
      return this.value;
   }

   public void setValue(String value) {
      this.value = value;
   }
}
