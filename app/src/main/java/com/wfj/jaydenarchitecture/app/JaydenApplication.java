package com.wfj.jaydenarchitecture.app;

import android.app.Application;

import com.wfj.jaydenarchitecture.model.cache.DataProvider;

import fbcore.log.LogUtil;

/**
 * Created by Jayden on 2015/8/27.
 */
public class JaydenApplication extends Application {

    public static JaydenApplication APP;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.setDebuggable(Constant.DEBUG);
        LogUtil.trace(Constant.TRACE);
        AppContext.init(this);
        DataProvider.init(this);
        APP = this;
    }

    public static JaydenApplication app() {
        return APP;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
