package com.wfj.jaydenarchitecture.model.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Create by：ml_bright on 2015/7/1 10:39
 * Email: 2504509903@qq.com
 */
public class UserDetail implements Serializable {

    @SerializedName("province")
    public String province;

    @SerializedName("city")
    public String city;

    /** 0女 1男 2未知 */
    @SerializedName("gender")
    public int sex;

    @SerializedName("mobile")
    public String mobile;

    /** 'active','inactive','locked','deleted' */
    @SerializedName("status")
    public String status;

}
