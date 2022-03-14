package com.dstz.sys.api.platform;

import com.dstz.sys.api.model.ISysTreeNode;
import java.util.List;

public interface ISysTreeNodePlatFormService {
  List<? extends ISysTreeNode> getChildNodesByKey(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/platform/ISysTreeNodePlatFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */