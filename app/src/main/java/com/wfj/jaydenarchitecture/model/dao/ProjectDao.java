package com.wfj.jaydenarchitecture.model.dao;

import com.google.gson.reflect.TypeToken;
import com.wfj.jaydenarchitecture.model.bean.MessageStatus;
import com.wfj.jaydenarchitecture.model.bean.Response;
import com.wfj.jaydenarchitecture.model.bean.UpdateInfo;
import com.wfj.jaydenarchitecture.model.bean.User;

import java.util.ArrayList;
import java.util.List;

import fbcore.conn.http.HttpParams;
import fbcore.log.LogUtil;

/**
 * @function 对外暴露，用于获取Dao层的数据
 * Created by Jayden on 2015/8/25.
 */
public class ProjectDao {

    /**
     * @function 构造请求参数的StringBuilder
     * @param srv
     * @return
     */
    private static StringBuilder getStringBuilder(int srv) {
        return new StringBuilder().append("&srv=").append(srv);
    }

    /**
     * 检查更新 1001
     * @param type
     * @param listener
     */
    public static void getUpdateInfo(int type, IEntityListener<UpdateInfo> listener) {
        String url = getStringBuilder(1001).append("&type=").append(type).toString();
        Dao.getEntity(url, new TypeToken<Response<UpdateInfo>>() {}, listener, false);
    }

    /**
     * 获取红点消息
     * @param listener
     */
    public static void getMessageHint(IEntityListener<MessageStatus> listener, boolean showToast) {
        Dao.getEntity(getStringBuilder(1009).toString(), new TypeToken<Response<MessageStatus>>() {
        }, listener, false, showToast);
    }

    /*******************************  用户相关  ***************************************/

    /**
     * 手机号注册 *
     */
    public static void registerPhone(String phone, String pwd, String validateCode, IEntityListener<User> listener) {
        String url = getStringBuilder(2308).toString();

        List<HttpParams.NameValue> list = new ArrayList<>();
        list.add(new HttpParams.NameValue("mobile", phone));
        list.add(new HttpParams.NameValue("password", pwd));
        list.add(new HttpParams.NameValue("validate_code", validateCode));

        Dao.putDatas(url, list, new TypeToken<Response<User>>() {
        }, listener);
    }

    /**
     * 找回密码 *
     */
    public static void retrievePassword(String phone, String pwd, String validateCode, IEntityListener<User> listener) {
        String url = getStringBuilder(2310).toString();

        List<HttpParams.NameValue> list = new ArrayList<>();
        list.add(new HttpParams.NameValue("mobile", phone));
        list.add(new HttpParams.NameValue("password", pwd));
        list.add(new HttpParams.NameValue("validate_code", validateCode));

        Dao.putDatas(url, list, new TypeToken<Response<User>>() {
        }, listener);
    }

    /**
     * 登陆 *
     */
    public static void loginPlatform(int platformType, String openId, String token, IEntityListener<User> listener) {
        String url = getStringBuilder(2301)
                .append("&pf_type=").append(platformType)
                .append("&openid=").append(openId)
                .append("&token=").append(token)
                .toString();
        Dao.getEntity(url, new TypeToken<Response<User>>() {
        }, listener, false);
    }

    /**
     * 登陆 *
     */
    public static void loginPhone(String phone, String pwd, IEntityListener<User> listener) {
        String url = getStringBuilder(2311).toString();

        List<HttpParams.NameValue> list = new ArrayList<>();
        list.add(new HttpParams.NameValue("mobile", phone));
        list.add(new HttpParams.NameValue("password", pwd));

        Dao.putDatas(url, list, new TypeToken<Response<User>>() {
        }, listener);
    }

    /**
     * 登陆 *
     */
    public static void loginOut(String phone, String pwd, IEntityListener<User> listener) {
        String url = getStringBuilder(2311).toString();

        List<HttpParams.NameValue> list = new ArrayList<>();
        list.add(new HttpParams.NameValue("mobile", phone));
        list.add(new HttpParams.NameValue("password", pwd));

        Dao.putDatas(url, list, new TypeToken<Response<User>>() {
        }, listener);
    }

    /**
     * 发送验证码
     */
    public static void sendValidate(String phone, IEntityListener<Object> listener) {
        String url = getStringBuilder(2309).toString();

        List<HttpParams.NameValue> list = new ArrayList<>();
        list.add(new HttpParams.NameValue("mobile", phone));
        Dao.putDatas(url, list, new TypeToken<Response<Object>>() {
        }, listener);
    }

    /**
     * 编辑用户资料 *
     */
    public static void updateUserInfo(User user, IEntityListener<User> listener) {
        LogUtil.d("updateUserInfo", "updateUserInfo: id=" + user.id + ", avatar = " + user.avatar);

        String url = getStringBuilder(2302).toString();
        List<HttpParams.NameValue> list = new ArrayList<>();
        list.add(new HttpParams.NameValue("user_id", user.id + ""));
        list.add(new HttpParams.NameValue("icon", user.avatar));
        list.add(new HttpParams.NameValue("nickname", user.nickname));
        list.add(new HttpParams.NameValue("gender", user.detail.sex + ""));
        list.add(new HttpParams.NameValue("province", user.detail.province));
        list.add(new HttpParams.NameValue("city", user.detail.city));

        Dao.putDatas(url, list, new TypeToken<Response<User>>() {
        }, listener);
    }

    /******************************  用户相关 END *************************************/

}
