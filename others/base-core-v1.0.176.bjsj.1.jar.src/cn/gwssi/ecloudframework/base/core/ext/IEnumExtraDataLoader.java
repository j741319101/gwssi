package cn.gwssi.ecloudframework.base.core.ext;

import cn.gwssi.ecloudframework.base.core.model.EnumExtraData;
import java.util.List;

public interface IEnumExtraDataLoader {
  Class<?> tag();
  
  List<EnumExtraData> load();
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/base-core/v1.0.176.bjsj.1/base-core-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/base/core/ext/IEnumExtraDataLoader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */