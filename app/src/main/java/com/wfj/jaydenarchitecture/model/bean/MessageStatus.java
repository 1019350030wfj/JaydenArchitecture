package com.wfj.jaydenarchitecture.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 描 述 ：当前用户是否有推送消息
 */
public class MessageStatus {

    @SerializedName("notice")
    public int haveNotice;			// 通知红点 0：没有 1：有

    @SerializedName("chat")
    public int havePersonalMessage;    	 		// 私信红点 0：没有 1：有

    public boolean hasNotice() {
        return haveNotice == 1;
    }

    public boolean hasMessage() {
        return havePersonalMessage == 1;
    }

}
