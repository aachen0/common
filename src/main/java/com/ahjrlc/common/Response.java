package com.ahjrlc.common;

/**
 * 后台响应前端json规范
 *
 * @author aachen0
 */
public interface Response {
    /**
     * 响应代码
     * @return 
     */
    int getCode();

    /**
     * 响应说明
     * @return 
     */
    String getMsg();

    /**
     * 响应实体，如果错误，则为错误详情
     * @return
     */
    Object getData();
}
