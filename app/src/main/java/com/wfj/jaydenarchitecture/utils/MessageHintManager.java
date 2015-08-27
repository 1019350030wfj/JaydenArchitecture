package com.wfj.jaydenarchitecture.utils;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.model.bean.MessageStatus;
import com.wfj.jaydenarchitecture.model.manager.UserManager;

/**
 * 红点信息管理类
 * Create by：ml_bright on 2015/7/17 17:04
 * Email: 2504509903@qq.com
 */
public class MessageHintManager {

    /**
     * 通知
     */
    private static final String SP_NOTICE = "hint_notice";
    /**
     * 私信
     */
    private static final String SP_MESSAGE = "hint_message";

    public interface OnHasHint {
        void onHasHint();
    }

    public static void saveHint(Context context, MessageStatus messageStatus, OnHasHint onHasHint) {
        if(messageStatus != null) {
            int noticeStatus = messageStatus.hasNotice() ? 1 : 0;
            if(SPHelper.getMessageHint(context, true, SP_NOTICE) != noticeStatus) {
                SPHelper.setMessageHint(context, true ,SP_NOTICE,  noticeStatus);
            }

            int msgStatus = messageStatus.hasMessage() ? 1 : 0;
            if(SPHelper.getMessageHint(context, true , SP_MESSAGE) != msgStatus) {
                SPHelper.setMessageHint(context, true ,SP_MESSAGE,  msgStatus);
            }
            if(getHint(context) && onHasHint != null) {
                onHasHint.onHasHint();
            }
        }
    }

    public static void initMain(Context context, ImageView view) {
        if (context == null || view == null) {
            return;
        }
        int drawable = getHint(context) ? R.mipmap.btn_member_reddot : R.mipmap.btn_member;
        view.setImageResource(drawable);
    }

    public static void initMine(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        int visible = getHint(context) ? View.VISIBLE : View.INVISIBLE;
        view.setVisibility(visible);
    }

    public static void initMessage(Context context, View noticeView, View messageView) {
        if (context == null || messageView == null || noticeView == null) {
            return;
        }
        messageView.setVisibility(getMessageHint(context) ? View.VISIBLE : View.INVISIBLE);
        noticeView.setVisibility(getNoticeHint(context) ? View.VISIBLE : View.INVISIBLE);
    }

    private static boolean getHint(Context context) {
        return getMessageHint(context) || getNoticeHint(context);
    }

    private static boolean getMessageHint(Context context) {
        if(!UserManager.getInstance().isLogin()) {
            return false;
        }
        return SPHelper.getMessageHint(context, true , SP_MESSAGE) != 0;
    }

    private static boolean getNoticeHint(Context context) {
        if(!UserManager.getInstance().isLogin()) {
            return false;
        }
        return SPHelper.getMessageHint(context, true , SP_NOTICE) != 0;
    }

    public static void clearMessage(Context context) {
        SPHelper.setMessageHint(context, true ,SP_MESSAGE,  0);
    }

    public static void clearNotice(Context context) {
        SPHelper.setMessageHint(context, true ,SP_NOTICE,  0);
    }

    /**
     * 是否只有私信
     * @param context
     * @return
     */
    public static boolean isOnlyMessage(Context context){
        return !getNoticeHint(context) && getMessageHint(context);
    }

}
