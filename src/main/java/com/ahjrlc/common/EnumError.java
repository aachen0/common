package com.ahjrlc.common;


/**
 * 异常分类
 *
 * @author Administrator
 */
public enum EnumError {
    LOGIN_NONE(300, "未登录")
    , USERNAME_HAD_BEEN_SET(301,"已设置过用户名")
    , USERNAME_ILLEGAL(302,"用户名不合法")
    , USERNAME_HAD_BEEN_USED(303,"用户名被他人占用")
    , USERNAME_SET_FAIL(304,"用户名设置失败")
    , MOBILE_NOT_SAME(305,"手机号码与预留手机号不一致")
    , USER_NOT_EXIST(306,"此账号未注册")
    , VERIFY_CODE_CAPTCHA_CHECK_ERROR(307,"图形验证码错误")
    , VERIFY_CODE_PHONE_CHECK_ERROR(308,"手机验证码错误")
    , VERIFY_CODE_PHONE_SEND_ERROR(309,"手机验证码发送失败，请稍后再试")
    , BANK_ACCOUNT_ERROR(310,"银行账号有误，请核对后重新提交")
    , READ_ONLY(311,"该记录为只读状态")
    , NO_RECORD_FOUND(401,"记录不存在")
    ;
    private Integer code;
    private String msg;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    EnumError(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
