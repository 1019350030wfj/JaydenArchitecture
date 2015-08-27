package com.wfj.jaydenarchitecture.utils;

import android.content.Context;
import android.util.TypedValue;
import android.view.WindowManager;

import com.wfj.jaydenarchitecture.app.AppContext;

public class UIUtil {
    private UIUtil() {
    }

    public static int getScreenWidth() {
        Context context = AppContext.getContext();
        WindowManager wManager =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wManager.getDefaultDisplay().getWidth();
    }

    public static int getScreenHeight(){
        Context context = AppContext.getContext();
        WindowManager wManager =  (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        return wManager.getDefaultDisplay().getHeight();
    }

    public static int px2Dp(Context context, float px) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (px / scale + 0.5f);
    }

    public static int dp2Px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, AppContext.getContext().getResources()
                .getDisplayMetrics());
    }
}
