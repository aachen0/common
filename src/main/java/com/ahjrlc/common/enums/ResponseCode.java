package com.ahjrlc.common.enums;


import com.ahjrlc.common.Response;

/**
 * 响应状态码规定
 *
 * @author aachen0
 * @date
 */
public enum ResponseCode implements Response {
    /**
     * 响应结果代码规范
     * code=401001 引导用户重新登录
     * code=401002 token 过期刷新token
     * code=401008 无权限访问
     */
    SUCCESS(0, "操作成功"),
    //    服务器故障
    SYSTEM_BUSY(500001, "系统繁忙，请稍候再试"),
    OPERATION_ERRO(500002, "操作失败"),
    REDIS_ERROR(500003, "Redis服务故障"),
    //    业务异常
    TOKEN_PARSE_ERROR(401001, "登录凭证已过期，请重新登录"),
    TOKEN_ERROR(401001, "登录凭证已过期，请重新登录"),
    ACCOUNT_ERROR(401001, "该账号异常，请联系运营人员"),
    ACCOUNT_LOCK_ERROR(401001, "该用户已被锁定，请联系运营人员"),
    TOKEN_PAST_DUE(401002, "授权信息已过期，请刷新token"),
    DATA_ERROR(401003, "传入数据异常"),
    NOT_ACCOUNT(401004, "该用户不存在,请先注册"),
    USER_LOCK(401005, "该用户已被锁定，请联系运营人员"),
    PASSWORD_ERROR(401006, "用户名或密码错误"),
    ARGUMENT_ERROR(401007, "方法参数校验异常"),
    UNAUTHORIZED_ERROR(401008, "权鉴校验不通过"),
    ROLE_PERMISSION_RELATION(401009, "该菜单权限存在子集关联，不允许删除"),
    OLD_PASSWORD_ERROR(401010, "旧密码不正确"),
    NOT_PERMISSION_DELETED_DEPT(401011, "该组织机构下还关联着用户，不允许删除"),
    OPERATION_MENU_PERMISSION_CATALOG_ERROR(401012, "操作后的菜单类型是目录，所属菜单必须为默认顶级菜单或者目录"),
    OPERATION_MENU_PERMISSION_MENU_ERROR(401013, "操作后的菜单类型是菜单，所属菜单必须为目录类型"),
    OPERATION_MENU_PERMISSION_BTN_ERROR(401013, "操作后的菜单类型是按钮，所属菜单必须为菜单类型"),
    OPERATION_MENU_PERMISSION_UPDATE(401014, "操作的菜单权限存在子集关联不允许变更"),
    OPERATION_MENU_PERMISSION_URL_NOT_NULL(401015, "菜单权限的url不能为空"),
    OPERATION_MENU_PERMISSION_URL_PERMS_NULL(401016, "菜单权限的标识符不能为空"),
    OPERATION_MENU_PERMISSION_URL_METHOD_NULL(401017, "菜单权限的请求方式不能为空"),
    LOGIN_NONE(401300, "未登录"),
    USERNAME_HAD_BEEN_SET(401301, "已设置过用户名"),
    USERNAME_ILLEGAL(401302, "用户名不合法"),
    USERNAME_HAD_BEEN_USED(401303, "用户名被他人占用"),
    USERNAME_SET_FAIL(401304, "用户名设置失败"),
    MOBILE_NOT_SAME(401305, "手机号码与预留手机号不一致"),
    USER_NOT_EXIST(401306, "此账号未注册"),
    VERIFY_CODE_CAPTCHA_CHECK_ERROR(401307, "图形验证码错误"),
    VERIFY_CODE_PHONE_CHECK_ERROR(401308, "手机验证码错误"),
    VERIFY_CODE_PHONE_SEND_ERROR(401309, "手机验证码发送失败，请稍后再试"),
    BANK_ACCOUNT_ERROR(401310, "银行账号有误，请核对后重新提交"),
    READ_ONLY(401311, "该记录为只读状态"),
    NO_RECORD_FOUND(401401, "记录不存在");
    private int code;
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 响应代码
     *
     * @return
     */
    @Override
    public int getCode() {
        return code;
    }

    /**
     * 响应说明
     *
     * @return
     */
    @Override
    public String getMsg() {
        return msg;
    }

    /**
     * 响应实体，如果错误，则为错误详情
     *
     * @return
     */
    @Override
    public Object getData() {
        return this;
    }
}
