package com.wfj.jaydenarchitecture.model.dao;

/**
 * Created by Jayden on 2015/8/25.
 */
public class ReturnCode {
    /**
     * 无网络
     */
    public static final int NO_NET = -1;

    /**
     * 没有更多数据
     */
    public static final int RS_LOCAL_NO_MORE_DATA = 1;

    /**
     * 上传数据出错
     */
    public static final int RS_POST_ERROR = 2;

    /**
     * 取消手势
     */
    public static final int RS_GESTURE_CANCEL = 3;

    /**
     * 返回数据成功
     */
    public static final int RS_SUCCESS = 1000;

    /**
     * 内部错误
     */
    public static final int RS_INTERNAL_ERROR = 1001;

    /**
     * 已经点赞过了
     */
    public static final int RS_ALREADY_CLICK = 1002;

    /**
     * 添加失败
     */
    public static final int RS_ADD_ERROR = 1003;

    /**
     * 空数据错误
     */
    public static final int RS_EMPTY_ERROR = 1004;

    /**
     * 手机号已经被注册
     */
    public static final int RS_MOBILE_ALREADY_REGISTER = 1005;

    /**
     * 传参错误
     */
    public static final int RS_PARAMETER_ERROR = 1006;

    /**
     * 验证错误
     */
    public static final int RS_VALIDATE_ERROR = 1007;

    /**
     * 无权限
     */
    public static final int RS_NO_PRIVILEGE = 1008;

    /**
     * 手机号不存在
     */
    public static final int RS_MOBILE_NOT_EXIST = 1009;

    /**
     * 密码错误
     */
    public static final int RS_PASSWORD_ERROR = 1010;

    /**
     * 编辑用户失败
     */
    public static final int RS_SAVE_USER_ERROR = 1011;

    /**
     * 艺术品名称已存在
     */
    public static final int RS_WORKNAME_EXIST = 1012;

    /**
     * 用户已经登录
     */
    public static final int RS_USER_STATUS_IS_LOGING = 1013;

    /**
     * 存在敏感词
     */
    public static final int RS_IS_SENSITIVE_WORD = 1014;

    /**
     * 艺术品已删除或不存在
     */
    public static final int RS_IS_DELETE_OR_INEXISTENCE = 1015;

}


