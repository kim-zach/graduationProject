package com.kimi.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public enum ResponseEnum {

    SUCCESS(0,"成功"),
    ERROR(-1,"服务器内部错误"),

    //-1xx 服务器错误
    BAD_SQL_GRAMMAR_ERROR(-101, "sql语法错误"),
    SERVLET_ERROR(-102, "servlet请求异常"), //-2xx 参数校验
    UPLOAD_ERROR(-103, "文件上传错误"),
    EXPORT_DATA_ERROR(104, "数据导出失败"),


    //-2xx 参数校验
    WORD_TAG_NULL_ERROR(-201, "模块标签不能为空"),
    WORD_MODULE_NAME_NULL_ERROR(-212, "模块名不能为空"),
    MOBILE_NULL_ERROR(-202, "手机号码不能为空"),
    MOBILE_ERROR(-203, "手机号码不正确"),
    PASSWORD_NULL_ERROR(204, "密码不能为空"),
    CODE_NULL_ERROR(205, "验证码不能为空"),
    CODE_ERROR(206, "验证码错误"),
    MOBILE_EXIST_ERROR(207, "手机号已被注册"),
    LOGIN_MOBILE_ERROR(208, "用户不存在"),
    LOGIN_PASSWORD_ERROR(209, "密码错误"),
    LOGIN_LOKED_ERROR(210, "用户被锁定"),
    LOGIN_AUTH_ERROR(-211, "未登录"),
    NICK_NAME_NULL_ERROR(-212,"昵称不能为空" ),
    AVATAR_URL_NULL_ERROR(-212,"头像URL不能为空" ),
    WORD_SPELL_NOT_NULL_ERROR(-213,"搜索单词不能为空" ),
    WORD_NOT_NULL_ERROR(-214,"存储单词不能为空" ),
    WORD_ID_NOT_NULL_ERROR(-215,"删除单词id不能为空" ),
    USER_ID_NOT_NULL_ERROR(-215,"上传视频用户id不能为空" ),


    TAG_NOT_NULL_ERROR(-216,"上传视频标签不能为空" ),
    TITLE_NOT_NULL_ERROR(-217,"上传视频标题不能为空" ),
    BRIEF_NOT_NULL_ERROR(-218,"上传视频简介不能为空" ),
    VIDEO_SOURCE_ID_NOT_NULL_ERROR(-219,"上传视频不能为空" ),
    VIDEO_ID_NOT_NULL_ERROR(-219,"查询视频id不能为空" ),


    USERNAME_NOT_NULL_ERROR(-300,"管理员用户名不能为空" ),
    PASSWORD_NOT_NULL_ERROR(-301,"管理员密码不能为空" ),
    PASSWORD_LENGTH_SHOULD_BE_LONGER_THAN_SIX(-302,"管理员密码不能小于六位数"),


    USER_BIND_IDCARD_EXIST_ERROR(-301, "身份证号码已绑定"),
    USER_NO_BIND_ERROR(302, "用户未绑定"),


    ALIYUN_RESPONSE_FAIL(-501, "阿里云响应失败"),
    ALIYUN_SMS_LIMIT_CONTROL_ERROR(-502, "短信发送过于频繁"),//业务限流
    ALIYUN_SMS_ERROR(-503, "短信发送失败"),//其他失败

    WEIXIN_CALLBACK_PARAM_ERROR(-601, "回调参数不正确"),
    WEIXIN_FETCH_ACCESSTOKEN_ERROR(-602, "获取access_token失败"),
    WEIXIN_FETCH_USERINFO_ERROR(-603, "获取用户信息失败");




    //响应状态码
    private Integer code;
    //响应信息
    private String message;

}
