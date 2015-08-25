package com.wfj.jaydenarchitecture.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Jayden on 2015/8/25.
 */
public class UpdateInfo {

    @SerializedName("title")
    public String title;// 更新显示标题

    @SerializedName("desc")
    public String desc;// 更新文案信息

    @SerializedName("url")
    public String url;// 更新链接
}
