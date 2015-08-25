package com.wfj.jaydenarchitecture.model.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.wfj.jaydenarchitecture.model.dao.ReturnCode;


/**
 * Created by Jayden on 2015/8/25.
 */
public class Response <T> {
    @SerializedName("rs_code")
    public int code;
    @Expose
    @SerializedName("rs_msg")
    public String msg;
    @Expose
    @SerializedName("data")
    public T data;

    public Response(int rsCode) {
        this.code = rsCode;
    }

    public boolean isSuccess() {
        return code == ReturnCode.RS_SUCCESS;
    }

}
