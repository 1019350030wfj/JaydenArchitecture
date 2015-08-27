package com.wfj.jaydenarchitecture.model.manager;

import android.content.Context;

import com.feibo.social.base.Platform;
import com.wfj.jaydenarchitecture.app.AppContext;
import com.wfj.jaydenarchitecture.model.bean.Response;
import com.wfj.jaydenarchitecture.model.bean.User;
import com.wfj.jaydenarchitecture.model.dao.IEntityListener;
import com.wfj.jaydenarchitecture.model.dao.ProjectDao;
import com.wfj.jaydenarchitecture.model.dao.ReturnCode;
import com.wfj.jaydenarchitecture.utils.EncryptUtil;
import com.wfj.jaydenarchitecture.utils.SPHelper;

/**
 * 自己相关
 */
public class UserManager {

    private static final String SALT = "cea403c952b";

    private static UserManager manager;

    private User mUser;

    private UserManager() {
        mUser = new User();
    }

    public static UserManager getInstance() {
        if (manager == null) {
            synchronized (UserManager.class) {
                if (manager == null) {
                    manager = new UserManager();
                }
            }
        }
        return manager;
    }

    public User getUser() {
        return mUser;
    }

    public boolean isLogin() {
        return mUser != null && mUser.id > 0;
    }

    public boolean isFromMe(long userid) {
        return userid == mUser.id;
    }

    public void loginPlatform(final Platform platform, String openId, String accessToken,
                              final IEntityListener<User> listener) {
        int platformType = getPlatformType(platform);
        ProjectDao.loginPlatform(platformType, openId, accessToken, (response) -> {
            loginResult(AppContext.getContext(), platformType, listener, response);
        });
    }

    public void loginOut(Context context) {
        mUser = new User();
        SPHelper.clearUser(context);
    }

    public Platform getPlatForm() {
        if (!isLogin()) {
            return null;
        }
        return getPlatForm(mUser.loginPlatform);
    }

    public Platform getPlatForm(int type) {
        return type == User.PLATFORM_SINA ? Platform.SINA :
                (type == User.PLATFORM_TENCENT ? Platform.QQ :
                        (type == User.PLATFORM_WECHAT ? Platform.WEIXIN : null));
    }

    public int getPlatformType(Platform platform) {
        return platform == Platform.SINA ? User.PLATFORM_SINA :
                platform == Platform.QQ ? User.PLATFORM_TENCENT :
                        platform == Platform.WEIXIN ? User.PLATFORM_WECHAT : User.PLATFORM_PHONE;
    }

    /** 手机登陆 */
    public void loginPhone(Context context, String phone, String pwd, IEntityListener<User> listener) {
        ProjectDao.loginPhone(phone, EncryptUtil.md5(SALT + pwd), (response) -> {
            loginResult(context, User.PLATFORM_PHONE, listener, response);
        });
    }

    private void loginResult(Context context, int platformType, IEntityListener<User> listener, Response<User> response) {
        if (response.isSuccess()) {
            mUser = response.data;
            mUser.loginPlatform = platformType;
            SPHelper.setLoginTime(context);
            SPHelper.setAuthWSKey(context, mUser.wskey);
            SPHelper.saveUser(context, mUser);
        }
        returnCode2Toast(response);
        listener.result(response);
    }

    /**
     * 手机号注册
     */
    public void registerPhone(String phone, String pwd, String validateCode, IEntityListener<User> listener) {
        ProjectDao.registerPhone(phone, EncryptUtil.md5(SALT + pwd), validateCode, response -> {
            returnCode2Toast(response);
            listener.result(response);
        });
    }

    /**
     * 找回密码
     */
    public void retrievePassword(String phone, String pwd, String validateCode, IEntityListener<User> listener) {
        ProjectDao.retrievePassword(phone, EncryptUtil.md5(SALT + pwd), validateCode, response -> {
            loginResult(AppContext.getContext(), User.PLATFORM_PHONE, listener, response);
        });
    }

    private void returnCode2Toast(Response<User> response) {
        switch (response.code) {
            case ReturnCode.RS_MOBILE_ALREADY_REGISTER:
//                ToastUtil.shortT("手机号已注册,请直接登录");
                break;
            case ReturnCode.RS_MOBILE_NOT_EXIST:
            case ReturnCode.RS_PASSWORD_ERROR:
//                ToastUtil.shortT("手机号/密码不正确,请重新输入");
                break;
            case ReturnCode.RS_VALIDATE_ERROR:
//                ToastUtil.shortT("请输入正确验证码");
                break;
            case ReturnCode.NO_NET:
//                ToastUtil.shortT(AppContext.getContext().getString(R.string.not_network));
                break;
            default:
                if (!response.isSuccess()) {
//                    ToastUtil.shortT("错误码：" + response.code);
                }
                break;
        }
    }

    /**
     * 发送验证码
     */
    public void sendValidate(String phone, IEntityListener<Object> listener) {
        ProjectDao.sendValidate(phone, listener);
    }
}
