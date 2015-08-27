package com.wfj.jaydenarchitecture.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Artist implements Serializable {

    @SerializedName("id")
    public int id;// 艺术家ID

    @SerializedName("name")
    public String name;// 艺术家

    @SerializedName("icon")
    public String avatar;// 头像

    // 内容，可选(列表项不需要返回)
    @SerializedName("content")
    public String content;

    @SerializedName("share_url")
    public String shareUrl; //分享连接

}
