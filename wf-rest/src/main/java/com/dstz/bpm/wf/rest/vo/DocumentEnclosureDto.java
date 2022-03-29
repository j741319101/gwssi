package com.dstz.bpm.wf.rest.vo;

import java.io.Serializable;

public class DocumentEnclosureDto implements Serializable {
   private String enclosureId;
   private String enclosureName;
   private Integer enclosureSize;
   private String path;
   private String e_flag;

   public String getEnclosureId() {
      return this.enclosureId;
   }

   public void setEnclosureId(String enclosureId) {
      this.enclosureId = enclosureId;
   }

   public String getEnclosureName() {
      return this.enclosureName;
   }

   public void setEnclosureName(String enclosureName) {
      this.enclosureName = enclosureName;
   }

   public Integer getEnclosureSize() {
      return this.enclosureSize;
   }

   public void setEnclosureSize(Integer enclosureSize) {
      this.enclosureSize = enclosureSize;
   }

   public String getPath() {
      return this.path;
   }

   public void setPath(String path) {
      this.path = path;
   }

   public String getE_flag() {
      return this.e_flag;
   }

   public void setE_flag(String e_flag) {
      this.e_flag = e_flag;
   }
}
