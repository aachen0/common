package com.ahjrlc.common;

/**
 * 正则常量
 *
 * @author Administrator
 */
public interface CommonConst {
    /**
     * 身份证号正则
     */
    String ID_REGEX = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
    /**
     * 手机号正则
     */
    String MOBILE_PHONE_REGEX = "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$";

    /**
     * 用户名正则
     */
    String USERNAME_REGEX = "^[a-zA-Z]\\w{4,14}$";

    /**
     * 银行卡账号正则
     */
    String BANK_ACCOUNT_REGEX = "^([1-9]{1})(\\d{14}|\\d{18})$";

    /**
     * 密码正则包含 数字,英文,字符中的两种以上，长度8-20
     */
    String PASSWORD_REGEX = "^(?![0-9]+$)(?![a-z]+$)(?![A-Z]+$)(?!([^(0-9a-zA-Z)])+$).{8,20}$";

    /**
     * 登录常量
     */
    String LOGIN_SUCCESS = "登录成功";
    String LOGIN_UNKNOWN_ACCOUNT = "账号不存在";
    String LOGIN_LOCKED_ACCOUNT = "账号被锁定";
    String LOGIN_WRONG_PASSWORD = "密码错误";
    String IP_BANNED = "IP禁用";
    String LOGIN_CONCURRENT_ACCESS = "重复登录";
    String LOGIN_FAILED = "登录失败";
    /**
     * 系统配置字典名常量
     */
    String LOGIN_FAILED_TIMES_KEY = "LOGIN_FAILED_TIMES_KEY";
    String LOGIN_IP_BANNED_MINUTES_KEY = "LOGIN_IP_BANNED_MINUTES_KEY";

    /**
     * 操作常量
     */
    String OPERATE_ADD_SUCCESS = "添加成功";
    String OPERATE_EDIT_SUCCESS = "修改成功";
    String OPERATE_ANSWER_SUCCESS = "回复成功";
    String OPERATE_FAILED = "操作失败";

    /**
     * 时间模板
     */
    String DATE_PATTERN = "yyyy-MM-dd";
    String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";


    /**
     * 用来加密已登录用户的活动key
     */
    String ACTIVE_KEY = "活动用户";
}
