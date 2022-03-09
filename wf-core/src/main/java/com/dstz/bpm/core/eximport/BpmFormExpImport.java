//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.dstz.bpm.core.eximport;

import com.dstz.base.api.bpmExpImport.BpmExpImport;
import com.dstz.base.core.util.DBImportUtil;
import com.dstz.base.core.util.StringUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class BpmFormExpImport implements BpmExpImport {
    @Resource
    JdbcTemplate jdbcTemplate;

    public BpmFormExpImport() {
    }

    public void bpmExport(String defIds, String filePath) throws Exception {
        if (!StringUtil.isNotEmpty(defIds)) {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }

            String sql = "SELECT DEF_SETTING_ FROM BPM_DEFINITION WHERE INSTR(?, ID_) > 0";
            List<Map<String, Object>> defSettings = this.jdbcTemplate.queryForList(sql, new Object[]{defIds});
            StringBuffer formKeys = new StringBuffer();
            defSettings.forEach((defSetting) -> {
                Map<String, Object> setJson = JSON.parseObject(defSetting.get("DEF_SETTING_").toString());
                String formKey = (String)((Map)((Map)setJson.get("flow")).get("globalForm")).get("formValue");
                if (StringUtil.isNotEmpty(formKey)) {
                    formKeys.append(formKey).append(",");
                }

                Map<String, Object> globalMobileForm = (Map)((Map)setJson.get("flow")).get("globalMobileForm");
                if (globalMobileForm != null) {
                    formKey = (String)globalMobileForm.get("formValue");
                    if (StringUtil.isNotEmpty(formKey)) {
                        formKeys.append(formKey).append(",");
                    }
                }

                Map<String, Map<String, Object>> nodes = (Map)setJson.get("nodeMap");
                nodes.forEach((nodeName, nodeConfig) -> {
                    Map<String, String> form = (Map)nodeConfig.get("form");
                    String key = (String)form.get("formValue");
                    if (StringUtil.isNotEmpty(key)) {
                        formKeys.append(key).append(",");
                    }

                    Map<String, String> mobileForm = (Map)nodeConfig.get("mobileForm");
                    key = (String)mobileForm.get("formValue");
                    if (StringUtil.isNotEmpty(key)) {
                        formKeys.append(key).append(",");
                    }

                });
            });
            if (!StringUtil.isEmpty(formKeys.toString())) {
                sql = "SELECT * FROM FORM_DEF WHERE INSTR(?,KEY_) > 0 ";
                List<Map<String, Object>> data = this.jdbcTemplate.queryForList(sql, new Object[]{formKeys.toString()});
                OutputStream outputStream = new FileOutputStream(filePath + "/FORM_DEF.txt");
                outputStream.write(JSON.toJSONString(data, new SerializerFeature[]{SerializerFeature.WriteDateUseDateFormat, SerializerFeature.WriteMapNullValue}).getBytes("UTF-8"));
                outputStream.close();
            }
        }
    }

    public void bpmImport(String filePath) throws Exception {
        DBImportUtil.importTable(filePath, "FORM_DEF", "KEY_");
    }

    public String checkImport(String filePath) throws Exception {
        return DBImportUtil.checkTable(filePath, "FORM_DEF", "KEY_", "NAME_");
    }
}
