package com.dstz.bpm.plugin.dto;

import java.io.Serializable;

public class PageDTO implements Serializable {
   private Integer pageNo = 1;
   private Integer pageSize = 20;

   public Integer getPageNo() {
      return this.pageNo;
   }

   public void setPageNo(Integer pageNo) {
      this.pageNo = pageNo;
   }

   public Integer getPageSize() {
      return this.pageSize;
   }

   public void setPageSize(Integer pageSize) {
      this.pageSize = pageSize;
   }
}
