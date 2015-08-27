package com.wfj.jaydenarchitecture.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.wfj.jaydenarchitecture.model.bean.Artwork;
import com.wfj.jaydenarchitecture.model.bean.User;
import com.wfj.jaydenarchitecture.model.manager.UserManager;

public class SPHelper {
    public enum Pref {
        APP("app"), USER("user");

        private String ns;

        private Pref(String ns) {
            this.ns = ns;
        }

        public String getNameSpace() {
            return this.ns;
        }
    }

    public static final SharedPreferences getPref(Context context, Pref pref) {
        return context.getSharedPreferences(pref.getNameSpace(), Context.MODE_PRIVATE);
    }

    public static boolean needGuide(Context context) {
        SharedPreferences sp = getPref(context, Pref.APP);
        return sp.getBoolean("needGuide", true);
    }

    public static void cancelGuide(Context context) {
        SharedPreferences sp = getPref(context, Pref.APP);
        sp.edit().putBoolean("needGuide", false).commit();
    }

    /** 版本更新提醒 */
    public static void setRemindeVersion(Context context, boolean reminde) {
        SharedPreferences sp = getPref(context, Pref.USER);
        if (reminde) {
            sp.edit().putLong("remide_time", 0).commit();
        } else {
            sp.edit().putLong("remide_time", System.currentTimeMillis()).commit();
        }
        sp.edit().putBoolean("remide_version", reminde).commit();
    }

    /** 是否提示自动更新 */
    public static boolean getCanRemideVersion(Context context) {
        SharedPreferences sp = getPref(context, Pref.USER);
        boolean b = sp.getBoolean("remide_version", true);
        if (!b) {
            long old = sp.getLong("remide_time", System.currentTimeMillis() - 5000);
            boolean over = TimeUtil.overOneDay(old, System.currentTimeMillis());
            if (over) {
                setRemindeVersion(context, true);
            }
            return over;
        }
        return true;
    }

    public static final String getAuthUid(Context context) {
        SharedPreferences sp = getPref(context, Pref.APP);
        return sp.getString("auth_uid", "0");
    }

    public static final long getAuthDeviceId(Context context) {
        SharedPreferences sp = getPref(context, Pref.APP);
        return sp.getLong("auth_device_id", 0);
    }

    public static final String getAuthKey(Context context) {
        SharedPreferences sp = getPref(context, Pref.APP);
        return sp.getString("auth_key", null);
    }

    public static void setAuthDeviceInfo(Context context, long deviceId, String key) {
        SharedPreferences sp = getPref(context, Pref.APP);
        sp.edit().putLong("auth_device_id", deviceId).putString("auth_key", key).commit();
    }

    /**
     * 第三方授权/登录成功时，保存用户信息.
     *
     * @param context
     * @param user 用户
     */
    public static void saveUser(Context context, User user) {
        SharedPreferences sp = getPref(context, Pref.USER);
        String gson = new Gson().toJson(user);
        sp.edit().putString("user_gson", gson).commit();
    }

    public static void clearUser(Context context) {
        SharedPreferences sp = getPref(context, Pref.USER);
        sp.edit().remove("user_gson")
                .remove("user_last_login_time")
                .remove("auth_wskey")
                .remove("login_time").commit();
        sp.edit().clear().commit();
    }

    /**
     * 应用启动时初始化用户数据.
     *
     * @param context
     * @param user
     */
    public static void fillUser(Context context, User user) {
        SharedPreferences sp = getPref(context, Pref.USER);
        User u = new Gson().fromJson(sp.getString("user_gson", ""), User.class);
        if(u == null) {
            return;
        }
        user.avatar = u.avatar;
        user.nickname = u.nickname;
        user.authName = u.authName;
        user.detail = u.detail;
        user.id = u.id;
        user.isAuth = u.isAuth;
        user.shareUrl = u.shareUrl;
        user.wskey = u.wskey;
        user.loginPlatform = u.loginPlatform;
    }

    /**
     * 保存用户登录时间
     * @param context
     */
    public static void saveLoginTime(Context context){
        SharedPreferences sp = getPref(context, Pref.USER);
        sp.edit().putLong("user_last_login_time", System.currentTimeMillis());
    }

    /**
     * 获取用户上次登录时间
     * @param context
     * @return
     */
    public static long getUserLastLoginTime(Context context){
        SharedPreferences sp = getPref(context, Pref.USER);
        return sp.getLong("user_last_login_time", System.currentTimeMillis());
    }


    /**
     * 获取用户秘钥
     * @param context
     * @return
     */
    public static final String getAuthWSKey(Context context) {
        SharedPreferences sp = getPref(context, Pref.USER);
        return sp.getString("auth_wskey", "0");
    }

    /**
     * 获取用户秘钥
     * @param context
     * @return
     */
    public static final void setAuthWSKey(Context context, String wskey) {
        SharedPreferences sp = getPref(context, Pref.USER);
        sp.edit().putString("auth_wskey", wskey).commit();
    }

    /**
     * 设置登陆时间
     * @param context
     */
    public static void setLoginTime(Context context) {
        SharedPreferences sp = getPref(context, Pref.USER);
        sp.edit().putLong("login_time", System.currentTimeMillis()).commit();
    }

    /**
     * 获取最后一次登陆时间
     * @param context
     * @return
     */
    public static long getLoginTime(Context context) {
        SharedPreferences sp = getPref(context, Pref.USER);
        return sp.getLong("login_time", 0);
    }

    /** 设置红点数据保存 */
    public static void setMessageHint(Context context, boolean aboutUser, String spName, int hintNum) {
        SharedPreferences sp = getPref(context, Pref.APP);
        if(hintNum < 0) {
            hintNum = 0;
        }
        if(aboutUser && !UserManager.getInstance().isLogin()) {
            if(!UserManager.getInstance().isLogin()) {
                return;
            } else {
                spName = UserManager.getInstance().getUser().id + spName;
            }
        }
        sp.edit().putInt(spName, hintNum).commit();
    }

    /** 获取红点数据保存 */
    public static int getMessageHint(Context context, boolean aboutUser, String spName) {
        SharedPreferences sp = getPref(context, Pref.APP);
        if(aboutUser && !UserManager.getInstance().isLogin()) {
            if(!UserManager.getInstance().isLogin()) {
                return 0;
            } else {
                spName = UserManager.getInstance().getUser().id + spName;
            }
        }
        return sp.getInt(spName, 0);
    }

    /**
     * 保存用户发布失败的艺术品信息
     * @param context
     * @param artwork 允许为空 为空时清空保存的信息
     */
    public static void savePublishArtInfo(Context context, Artwork artwork){
        SharedPreferences sp = getPref(context, Pref.APP);
        sp.edit().putString("publish_art", "").commit();
        sp.edit().putString("publish_art", artwork == null ? "" : new Gson().toJson(artwork)).commit();
    }

    /**
     * 获取保存的艺术品信息
     * @param context
     * @return
     */
    public static Artwork getPublishArtInfo(Context context){
        SharedPreferences sp = getPref(context, Pref.APP);
        String publishArt = sp.getString("publish_art", "");
        return TextUtils.isEmpty(publishArt)? null : new Gson().fromJson(publishArt, Artwork.class);
    }

    /**
     * 保存用户信息编辑引导界面已经显示
     * @param context
     */
    public static void saveGuideShow(Context context){
        SharedPreferences sp = getPref(context, Pref.APP);
        sp.edit().putBoolean("edit_guide_showed", true).commit();
    }

    /**
     * 获取用户信息编辑引导界面是否已经显示
     * @param context
     * @return
     */
    public static boolean isUserEditGuideShow(Context context){
        SharedPreferences sp = getPref(context, Pref.APP);
        return sp.getBoolean("edit_guide_showed", false);
    }
}
