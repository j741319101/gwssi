package com.dstz.bpm.engine.util;

import com.dstz.bpm.api.model.nodedef.BpmNodeDef;
import com.dstz.base.core.util.AppUtil;
import com.dstz.org.api.model.dto.BpmOrgDTO;
import com.dstz.org.api.model.dto.BpmUserDTO;
import com.dstz.org.api.service.GroupService;
import com.dstz.org.api.service.UserService;
import com.dstz.sys.api.groovy.IGroovyScriptEngine;
import com.dstz.sys.api.model.DefaultIdentity;
import com.dstz.sys.api.model.SysIdentity;
import cn.hutool.core.collection.CollectionUtil;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang3.StringUtils;


public class BpmTaskShowUtil {
    public static List<SysIdentity> appendOrgUser(List<SysIdentity> sysIdentities, BpmNodeDef bpmNodeDef) {
        return appendOrgUser(sysIdentities, bpmNodeDef, "--");
    }


    public static List<SysIdentity> appendOrgUser(List<SysIdentity> sysIdentities, BpmNodeDef bpmNodeDef, String appendStr) {
        if (bpmNodeDef == null) {
            return sysIdentities;
        }

        if (CollectionUtil.isEmpty(sysIdentities)) {
            Collections.emptyList();
        }

        List<String> rules = bpmNodeDef.getNodeProperties().getShowTaskSysIdentityRule();
        if (CollectionUtil.isEmpty(rules)) {
            return sysIdentities;
        }
        if (rules.contains("other")) {
            IGroovyScriptEngine groovyScriptEngine = (IGroovyScriptEngine) AppUtil.getImplInstance(IGroovyScriptEngine.class).get(Integer.valueOf(0));
            sysIdentities.forEach(user -> {
                String str = groovyScriptEngine.executeString(((String) rules.get(0)).substring(6), null);
                user.setName(str);
            });
        } else {
            String compareStr = "user";
            AtomicBoolean userShowOrg = new AtomicBoolean(false);
            if (rules.contains("org")) {
                compareStr = "org";
            }
            if (rules.contains("parentOrg")) {
                compareStr = "parentOrg";
            }
            if (rules.contains("gsOrg")) {
                compareStr = "gsOrg";
            }
            if (rules.contains("user")) {
                compareStr = "user";
            }
            StringBuffer userIds = new StringBuffer();
            StringBuffer orgIds = new StringBuffer();
            AtomicBoolean needSn = new AtomicBoolean(false);
            sysIdentities.forEach(user -> {
                if (StringUtils.equals(user.getType(), "user")) {
                    if (((DefaultIdentity) user).getSn().intValue() == -1) {
                        needSn.set(true);
                    }

                    if (rules.contains("org") || rules.contains("parentOrg") || rules.contains("gsOrg")) {
                        userShowOrg.set(true);
                    }
                    userIds.append(user.getId()).append(",");
                } else if (StringUtils.equals(user.getType(), "org")) {
                    if (((DefaultIdentity) user).getSn().intValue() == -1) {
                        needSn.set(true);
                    }
                    orgIds.append(user.getId()).append(",");
                }
            });
            if (!userShowOrg.get() && !needSn.get()) {
                return sysIdentities;
            }
            UserService userService = AppUtil.getImplInstanceArray(UserService.class).get(0);
            GroupService groupService = AppUtil.getImplInstanceArray(GroupService.class).get(0);
            if (userIds.length() > 0) {
                List<BpmUserDTO> bpmUserDTOS = userService.getUserOrgInfos(userIds.substring(0, userIds.length() - 1));
                if (null != bpmUserDTOS && bpmUserDTOS.size() > 0) {
                    for (BpmUserDTO user : bpmUserDTOS) {
                        String finalCompareStr = compareStr;
                        sysIdentities.stream().filter(sysIdentity -> (StringUtils.equals(sysIdentity.getType(), "user") && StringUtils.equals(sysIdentity.getId(), user.getUserId()) && StringUtils.equals(sysIdentity.getOrgId(), user.getOrgId())))

                                .forEach(sysIdentity -> {
                                    if (StringUtils.equals(finalCompareStr, "user")) {
                                        ((DefaultIdentity) sysIdentity).setSn(Integer.valueOf(user.getUserSn()));
                                    }


                                    if (StringUtils.equals(finalCompareStr, "org")) {
                                        ((DefaultIdentity) sysIdentity).setSn(user.getOrgSn());


                                        ((DefaultIdentity) sysIdentity).setCompareOrgId(user.getOrgId());
                                    }


                                    if (StringUtils.equals(finalCompareStr, "parentOrg")) {
                                        if (StringUtils.isEmpty(user.getParentOrgId())) {
                                            ((DefaultIdentity) sysIdentity).setSn(user.getOrgSn());


                                            ((DefaultIdentity) sysIdentity).setCompareOrgId(user.getOrgId());
                                        } else {
                                            ((DefaultIdentity) sysIdentity).setSn(user.getParentOrgSn());


                                            ((DefaultIdentity) sysIdentity).setCompareOrgId(user.getParentOrgId());
                                        }
                                    }


                                    if (StringUtils.equals(finalCompareStr, "gsOrg")) {
                                        if (StringUtils.equals(user.getOrgType(), "1")) {
                                            ((DefaultIdentity) sysIdentity).setSn(user.getOrgSn());


                                            ((DefaultIdentity) sysIdentity).setCompareOrgId(user.getOrgId());
                                        } else {
                                            ((DefaultIdentity) sysIdentity).setSn(user.getParentOrgSn());


                                            ((DefaultIdentity) sysIdentity).setCompareOrgId(user.getParentOrgId());
                                        }
                                    }


                                    StringBuffer userName = new StringBuffer();

                                    rules.forEach((rule) -> {
                                        if (StringUtils.equals("user", rule)) {
                                            userName.append(sysIdentity.getName()).append(appendStr);
                                        } else if (StringUtils.equals("org", rule)) {
                                            userName.append(user.getOrgName()).append(appendStr);
                                        } else if (StringUtils.equals("parentOrg", rule)) {
                                            if (StringUtils.isNotEmpty(user.getParentOrgName())) {
                                                userName.append(user.getParentOrgName()).append(appendStr);
                                            }
                                        } else if (StringUtils.equals("gsOrg", rule)) {
                                            if (StringUtils.equals(user.getOrgType(), "1")) {
                                                userName.append(user.getOrgName()).append(appendStr);
                                            } else {
                                                userName.append(user.getParentOrgName()).append(appendStr);
                                            }
                                        }

                                    });

                                    if (userName.length() > 0) {
                                        sysIdentity.setName(userName.substring(0, userName.length() - appendStr.length()));
                                    }
                                });
                    }
                }
            }


            if (orgIds.length() > 0) {

                List<BpmOrgDTO> bpmUserDTOS = groupService.getOrgInfos(orgIds.substring(0, orgIds.length() - 1));

                for (BpmOrgDTO user : bpmUserDTOS) {

                    String finalCompareStr = compareStr;

                    sysIdentities.stream().filter(sysIdentity -> StringUtils.equals(sysIdentity.getId(), user.getOrgId()))
                            .forEach(sysIdentity -> {
                                if (StringUtils.equals(finalCompareStr, "org")) {
                                    ((DefaultIdentity) sysIdentity).setSn(user.getOrgSn());
                                }
                                if (StringUtils.equals(finalCompareStr, "parentOrg")) {
                                    if (StringUtils.isEmpty(user.getParentOrgId())) {
                                        ((DefaultIdentity) sysIdentity).setSn(user.getOrgSn());
                                    } else {
                                        ((DefaultIdentity) sysIdentity).setSn(user.getParentOrgSn());
                                    }
                                }
                                if (StringUtils.equals(finalCompareStr, "gsOrg")) {
                                    if (StringUtils.equals(user.getOrgType(), "1")) {
                                        ((DefaultIdentity) sysIdentity).setSn(user.getOrgSn());
                                    } else {
                                        ((DefaultIdentity) sysIdentity).setSn(user.getParentOrgSn());
                                    }
                                }
                                StringBuffer userName = new StringBuffer();
                                rules.forEach((rule) -> {
                                    if (StringUtils.equals("org", rule)) {
                                        userName.append(user.getOrgName()).append(appendStr);
                                    } else if (StringUtils.equals("parentOrg", rule)) {
                                        if (StringUtils.isNotEmpty(user.getParentOrgName())) {
                                            userName.append(user.getParentOrgName()).append(appendStr);
                                        }
                                    } else if (StringUtils.equals("gsOrg", rule)) {
                                        if (StringUtils.equals(user.getOrgType(), "1")) {
                                            userName.append(user.getOrgName()).append(appendStr);
                                        } else {
                                            userName.append(user.getParentOrgName()).append(appendStr);
                                        }
                                    }

                                });

                                if (userName.length() > 0) {
                                    sysIdentity.setName(userName.substring(0, userName.length() - appendStr.length()));
                                }
                            });
                }
            }
        }

        return sysIdentities;
    }
}
