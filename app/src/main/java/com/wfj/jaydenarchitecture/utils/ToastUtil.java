package com.wfj.jaydenarchitecture.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtil {
    private static Toast sToast;


    public static void shortT(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT);
            sToast.show();
        } else {
            sToast.setText(msg);
            sToast.setDuration(Toast.LENGTH_SHORT);
            sToast.show();
        }
    }

    public static void longT(Context context, int msgResId) {
        longT(context, context.getResources().getString(msgResId));
    }

    public static void longT(Context context, String msg) {
        if (sToast == null) {
            sToast = Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_LONG);
            sToast.show();
        } else {
            sToast.setText(msg);
            sToast.setDuration(Toast.LENGTH_LONG);
            sToast.show();
        }
    }

    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
        }
    }
}
