package cn.gwssi.ecloudframework.base.api;

public interface Page {
  public static final int DEFAULT_PAGE_SIZE = 10;
  
  Integer getPageSize();
  
  Integer getTotal();
  
  Integer getPageNo();
  
  boolean isShowTotal();
  
  Integer getStartIndex();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-api/v1.0.176.bjsj.1/base-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/api/Page.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */