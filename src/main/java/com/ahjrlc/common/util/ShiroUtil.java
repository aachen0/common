package com.ahjrlc.common.util;

import org.apache.shiro.subject.Subject;

/**
 * @author aachen0
 * @date 2019/12/31 16:31
 */
public class ShiroUtil {
    public static boolean hasAnyRole(Subject subject, String... roles) {
        if (roles != null && roles.length > 0) {
            for (String role : roles) {
                if (subject.hasRole(role)) {
                    return true;
                }
            }
        }
        return false;
    }
}
