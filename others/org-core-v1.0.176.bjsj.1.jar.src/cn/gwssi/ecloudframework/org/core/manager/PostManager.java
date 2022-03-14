package com.dstz.org.core.manager;

import com.dstz.base.manager.Manager;
import com.dstz.org.core.model.Post;
import java.util.List;

public interface PostManager extends Manager<String, Post> {
  Post getByAlias(String paramString);
  
  List<Post> getByUserId(String paramString);
  
  Post getMasterByUserId(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/org-core/v1.0.176.bjsj.1/org-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/org/core/manager/PostManager.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */