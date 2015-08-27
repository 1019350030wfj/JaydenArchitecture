package com.wfj.jaydenarchitecture.app;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.wfj.jaydenarchitecture.model.bean.User;
import com.wfj.jaydenarchitecture.model.manager.UserManager;
import com.wfj.jaydenarchitecture.social.SocialManager;
import com.wfj.jaydenarchitecture.utils.DeviceUidGenerator;
import com.wfj.jaydenarchitecture.utils.SPHelper;
import com.wfj.jaydenarchitecture.utils.ToastUtil;

import fbcore.security.Md5;
import fbcore.utils.Utils;

public class AppContext {
    /**
     * 服务器地址
     */
    public static String SERVER_HOST;
    /**
     * 应用版本号
     */
    public static String APP_VERSION_CODE;
    /**
     * 应用版本名
     */
    public static String APP_VERSION_NAME;

    private static Context context;
    private static AppContext appContext;

    public static String MODEL;
    public static String OS_VERSION;
    public static String OS_TYPE = "3";
    public static String DEVICE_ID;

    static {
        SERVER_HOST = Constant.USE_REAL_SEVER ? Constant.REAL_SERVER : Constant.TEST_SERVER;
    }

    private AppContext() {
    }

    public static Context getContext() {
        return context;
    }

    public synchronized static void init(Context context) {
        AppContext.context = context;
        initAppParames(context);
        initUser(context);
        DirContext.getInstance().initCacheDir(context);
        SocialManager.getInstance(context).initSocial();
        context.registerReceiver(new NetworkReceiver(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private static void initUser(Context context) {
        User user = UserManager.getInstance().getUser();
        SPHelper.fillUser(context, user);
    }

    /**
     * 初始化应用变量
     */
    private static void initAppParames(Context context) {
        APP_VERSION_CODE = String.valueOf(Utils.getAppVersionCode(context));
        APP_VERSION_NAME = Utils.getAppVersionName(context);

        MODEL = android.os.Build.MODEL;
        MODEL = MODEL.replace(" ", "");

        OS_VERSION = android.os.Build.VERSION.RELEASE;
        try {
            DEVICE_ID = Md5.digest32(DeviceUidGenerator.generate(context));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static AppContext getInstance() {
        if (appContext == null) {
            synchronized(AppContext.class) {
                if (appContext == null) {
                    appContext = new AppContext();
                }
            }
        }
        return appContext;
    }

    public static boolean isWifiActive() {
        return NetworkReceiver.isWifiActive;
    }

    /**
     * 解决hasAvailableNetwork的bug
     */
    public static boolean isNetworkAvailable() {
        return Utils.isNetworkAvailable(context);
    }

    public static boolean hasAvailableNetwork() {
        return NetworkReceiver.hasAvailableNetwork;
    }

    private static class NetworkReceiver extends BroadcastReceiver {
        private static boolean isWifiActive = false;
        private static boolean hasAvailableNetwork = false;

        @Override
        public void onReceive(Context context, Intent intent) {
            //存在不同进程，同时接收广播，造成多次Toast，所以需要判断
            if (!"com.feibo.art".equals(getCurProcessName(getContext()))) {
                return;
            }
            onReceiveDo(intent);
        }

        private synchronized void onReceiveDo(Intent intent) {
            String action = intent.getAction();
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                updateNetwork();
            }
        }

        private void updateNetwork() {
            ConnectivityManager connectivity = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo mNetworkInfo = connectivity.getActiveNetworkInfo();
                if (mNetworkInfo != null && mNetworkInfo.isConnected()) {
                    hasAvailableNetwork = mNetworkInfo.isAvailable();
                    String type = mNetworkInfo.getTypeName();
                    if (type.equalsIgnoreCase("WIFI")) {
                        isWifiActive = true;
                    } else if (type.equalsIgnoreCase("MOBILE")) {
                        ToastUtil.shortT(getContext(), "正在使用移动蜂窝网络");
                        isWifiActive = false;
                    } else {
                        isWifiActive = false;
                    }
                } else {
                    //ToastUtil.shortT("网络已断开");
                    hasAvailableNetwork = false;
                    isWifiActive = false;
                }
            }
        }

        private static String getCurProcessName(Context context) {
            int pid = android.os.Process.myPid();
            ActivityManager mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo appProcess : mActivityManager.getRunningAppProcesses()) {
                if (appProcess.pid == pid) {
                    return appProcess.processName;
                }
            }
            return null;
        }
    }

}
