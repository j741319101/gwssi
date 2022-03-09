package cn.gwssi.ecloudframework.sys.api.platform;

import cn.gwssi.ecloudframework.sys.api.model.ISysFile;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ISysFilePlatFormService {
  Map<String, String> uploaderOfd(MultipartFile paramMultipartFile, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6) throws Exception;
  
  Map<String, String> uploaderOfd(MultipartFile paramMultipartFile, String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6, String paramString7) throws Exception;
  
  ISysFile upload(MultipartFile paramMultipartFile, String paramString1, String paramString2, String paramString3);
  
  ISysFile upload(MultipartFile paramMultipartFile, String paramString1, String paramString2, String paramString3, String paramString4);
  
  ISysFile upload(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5, String paramString6);
  
  ISysFile upload(MultipartFile paramMultipartFile);
  
  ResponseEntity<byte[]> download(String paramString);
  
  List<? extends ISysFile> listJson(String paramString1, String paramString2, String paramString3);
  
  String delete(String paramString);
}


/* Location:              /Users/wangchenliang/Documents/workspace/ecloud/cn_分卷/cn/gwssi/ecloud/sys-api/v1.0.176.bjsj.1/sys-api-v1.0.176.bjsj.1.jar!/cn/gwssi/ecloudframework/sys/api/platform/ISysFilePlatFormService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */