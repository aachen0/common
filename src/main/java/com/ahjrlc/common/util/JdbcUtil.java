package com.ahjrlc.common.util;

import java.util.regex.Pattern;

/**
 * @author aachen0
 * @date 2019/11/26 22:58
 */
public class JdbcUtil {
    /**
     * 从jdbc.url中解析出dbName
     * @param jdbcUrl
     * @return
     */
    public static String parseDbName(String jdbcUrl) {
        String[] split = jdbcUrl.split("[/?]");
        return split.length >= 4 ? split[3] : null;
    }
}
