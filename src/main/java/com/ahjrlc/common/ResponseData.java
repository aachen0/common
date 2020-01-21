package com.ahjrlc.common;

/**
 * 标准响应包装类
 *
 * @author Administrator
 */
public class ResponseData {

    /**
     * 响应编码
     */
    private Integer code = 0;

    /**
     * 响应说明信息
     */
    private String msg;

    /**
     * 响应主体数据
     */
    private Object data;

    /**
     * layui查询列表分页时所需记录总数
     */
    private Long count;

    /**
     * layui执行的分页大小回调
     */
    private Integer limit;

    public Integer getCode() {
        return code;
    }

    public ResponseData setCode(int code) {
        this.code = code;
        return this;

    }

    public String getMsg() {
        return msg;
    }

    public ResponseData setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public Long getCount() {
        return count;
    }

    public ResponseData setCount(Long count) {
        this.count = count;
        return this;

    }

    public Object getData() {
        return data;
    }

    public ResponseData setData(Object data) {
        this.data = data;
        return this;

    }

    public Integer getLimit() {
        return limit;
    }

    public ResponseData setLimit(Integer limit) {
        this.limit = limit;
        return this;
    }
    public ResponseData setError(EnumError error) {
        code = error.getCode();
        msg = error.getMsg();
        return this;
    }
}
