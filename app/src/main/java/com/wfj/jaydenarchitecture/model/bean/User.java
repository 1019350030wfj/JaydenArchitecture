package com.wfj.jaydenarchitecture.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    public static final int SEX_WOMAN = 0;
    public static final int SEX_MAN = 1;
    public static final int SEX_UNKONW = 2;

    /** 新浪 */
    public static final int PLATFORM_SINA = 1;
    /** 腾讯 */
    public static final int PLATFORM_TENCENT = 2;
    /** 微信 */
    public static final int PLATFORM_WECHAT = 3;
    /** 手机 */
    public static final int PLATFORM_PHONE = 4;

    @SerializedName("id")
    public long id;

    @SerializedName("nickname")
    public String nickname;

    @SerializedName("icon")
    public String avatar;

    /** 是否认证 */
    @SerializedName("is_auth")
    public int isAuth;

    /** 认证名称 */
    @SerializedName("auth_name")
    public String authName;

    @SerializedName("detail")
    public UserDetail detail;

    @SerializedName("share_url")
    public String shareUrl; //分享连接

    @SerializedName("wskey")
    public String wskey;  //用户秘钥

    /**
     * 登陆平台
     */
    public int loginPlatform;

}
