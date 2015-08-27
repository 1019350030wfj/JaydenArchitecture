package com.wfj.jaydenarchitecture.social;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.feibo.social.ResultListener;
import com.feibo.social.base.Config;
import com.feibo.social.base.Platform;
import com.feibo.social.base.Platform.Extra;
import com.feibo.social.manager.SocialComponent;
import com.feibo.social.model.PlatformInfo;
import com.feibo.social.model.ShareEntity;
import com.feibo.social.model.ShareEntityBuilder;
import com.feibo.social.utils.AccessTokenManager;
import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.app.AppContext;
import com.wfj.jaydenarchitecture.model.bean.User;
import com.wfj.jaydenarchitecture.model.cache.DataProvider;
import com.wfj.jaydenarchitecture.model.dao.ReturnCode;
import com.wfj.jaydenarchitecture.model.manager.LoadListener;
import com.wfj.jaydenarchitecture.utils.ToastUtil;

import java.util.ArrayList;

import fbcore.cache.image.ImageLoader;
import fbcore.log.LogUtil;
import fbcore.utils.Strings;

public class SocialManager {

    /**
     * 新浪微博Token过期
     */
    public static final int WEIBO_TOKEN_TIMEOUT = 1;


    private static SocialManager manager;
    private Context context;

    private SocialManager(Context context) {
        this.context = context;
    }

    public static SocialManager getInstance(Context context) {
        if (manager == null) {
            synchronized (SocialManager.class) {
                if (manager == null) {
                    manager = new SocialManager(context);
                }
            }
        }
        return manager;
    }

    public void initSocial() {
        Config.config(
                Platform.SINA,
                new Config().buildAppKey("4009683743")
                        .buildRedirectUrl("http://dl.yiwaiart.com").buildScope("all"));

        Config.config(Platform.QQ, new Config().buildAppId("1104677021").buildScope("all"));

        Config.config(Platform.WEIXIN,
                new Config().buildAppId("wxa2dfc1c2a50ea86e").buildAppSecret("3a06d857be4deec8572133440d0d1ecd")
                        .buildScope("snsapi_userinfo"));
    }

    public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        SocialComponent.onActivityResult(activity, requestCode, resultCode, data);
    }

    public void onNewIntent(Activity activity, Intent intent) {
        SocialComponent.onNewIntent(activity, intent);
    }

    public void login(final Activity activity, final Platform platform,
                      final SocialResultListener listener) {
        if (!AppContext.hasAvailableNetwork()) {
            listener.onError(0, activity.getResources().getString(R.string.not_network));
            return;
        }
        reset(activity, platform, null);
        loginSocial(activity, platform, (isSuccess, result) -> {
            if (isSuccess) {
                listener.onStart();
                getUserInfoAndLogin(activity, platform, listener);
            } else {
                listener.onError(0, result);
            }
        });
    }

    public void loginSocial(Activity activity, Platform platform, ResultListener listener) {
        SocialComponent.create(platform, activity).login(listener);
    }

    public boolean isLogin(Platform platform) {
        boolean result = false;
        // 验证是否过期
        switch (platform) {
            case QQ:
                result = (AccessTokenManager.readTencentAccessToken(context) != null)
                        && AccessTokenManager.validateQQToken(context);
                break;
            case SINA:
                result = (AccessTokenManager.readSinaAccessToken(context) != null)
                        && AccessTokenManager.validateSinaToken(context);
                break;
            case WEIXIN:
                result = (AccessTokenManager.readWXAccessToken(context) != null)
                        && AccessTokenManager.validateWXAccessToken(context);
                break;
        }
        return result;
    }

    public void reset(final Activity activity, final Platform platform, final ILoginOutListener listener) {
        if (isLogin(platform)) {
            SocialComponent.create(platform, activity).logout((isSuccess, result) -> {
                if (isSuccess) {
                    LogUtil.d("重置第三方登陆", "第三方：" + platform.toString() + ", 取消授权成功");
                } else {
                    LogUtil.d("重置第三方登陆", "第三方：" + platform.toString() + ", 取消授权失败");
                }

                if (listener != null) {
                    listener.onLoginOutResult(isSuccess);
                }
            });
        }
    }

    public interface ILoginOutListener {
        void onLoginOutResult(boolean success);
    }

    private void getUserInfoAndLogin(final Activity activity, Platform platform,
                                     final SocialResultListener onLoaded) {
        PlatformInfo platformInfo = SocialComponent.create(platform, activity).getPlatformUserInfo();
//        String nickname = platformInfo.getNickName();
//        String avatar = platformInfo.getHeadImgUrl();
        String accessToken = platformInfo.getAccessToken();
        String openId = platformInfo.getOpenid();

//        UserManager.getInstance().loginPlatform(platform, openId, accessToken,
//                response -> {
//                    if (response.code == ReturnCode.RS_SUCCESS && response.data != null) {
//                        onLoaded.onSuccess(response.data);
//                    } else {
//                        onLoaded.onError(response.code, response.msg);
//                    }
//                });
    }

    /**
     * ********************************* 下面是分享 *******************************************
     */
    public void shareQQ(Activity activity, String title, String content, String imgUrls, String webUrl, String videoUrl) {
        ShareEntityBuilder entityBuilder = getEntity(title, webUrl, content);
        entityBuilder.setNetworkImageUrl(imgUrls);
        if (!Strings.isEmpty(videoUrl)) {
            entityBuilder.setVideoUrl(webUrl);
            entityBuilder.setWebpageUrl(null);
        }
        ToastUtil.shortT(activity.getApplicationContext(), "分享中");
        share(activity, Platform.Extra.QQ_FRIEND, entityBuilder.create());
    }

    public void shareQzone(Activity activity, String title, String content, String imgUrl, String webUrl,
                           String videoUrl) {
        shareQzone(activity, title, content, new String[]{imgUrl}, webUrl, videoUrl);
    }

    public void shareQzone(final Activity activity, String title, String content, String[] imgUrls, String webUrl,
                           String videoUrl) {
        ShareEntityBuilder entityBuilder = getEntity(title, webUrl, content);
        setImgUrlsBuilder(entityBuilder, imgUrls);
        if (!Strings.isEmpty(videoUrl)) {
            entityBuilder.setVideoUrl(webUrl);
//            entityBuilder.setWebpageUrl(null);
        }
//        ToastUtil.shortT("分享中");
        share(activity, Extra.QQ_QZONE, entityBuilder.create());
    }

    public void shareSina(final Activity activity, String content, String imgUrl, String webUrl, String videoUrl,
                          final LoadListener listener, boolean isToast) {
        if (Strings.isEmpty(imgUrl) && isToast) {
//            ToastUtil.shortT("分享图片不能为空");
        } else {
            if (isToast) {
//                ToastUtil.shortT("分享中");
            }
            shareWithBitmap(activity, Extra.SINA, getEntity(null, webUrl, content), imgUrl, webUrl, videoUrl, listener, isToast);
        }
    }

    public void shareWX(final Activity activity, final String title, final String content, final Extra extra,
                        final String webUrl, String imgUrl, String videoUrl, final LoadListener listener) {
        if (Strings.isEmpty(imgUrl)) {
//            ToastUtil.shortT("分享图片不能为空");
            return;
        }
//        ToastUtil.shortT("分享中");

        shareWithBitmap(activity, extra, getEntity(title, webUrl, content), imgUrl, videoUrl, webUrl, listener, true);
    }

    /**
     * ******************************** 分享公共方法 **************************************
     */
    private void share(Activity activity, Platform.Extra extra, ShareEntity entity) {
        share(activity, extra, entity, true);
    }

    private void share(Activity activity, Extra extra, ShareEntity entity, final boolean isToast) {
        share(activity, extra, entity, null, isToast);
    }

    private void share(Activity activity, Extra extra, ShareEntity entity, final LoadListener listener, final boolean isToast) {
        SocialComponent.create(extra.belong(), activity).share(entity, extra, new ResultListener() {
            @Override
            public void onResult(boolean isSuccess, String result) {
                LogUtil.e("SocialManager1", "isSuccess -> " + isSuccess + ", result = " + result);
                if (isSuccess) {
//                    ToastUtil.shortT("分享成功");
                    if (listener != null) {
                        listener.onSuccess();
                    }
                } else {
                    if (listener != null) {
                        if (!Strings.isEmpty(result)) {
                            if (result.contains("21332")) {
                                //新浪token过期
                                listener.onFail(WEIBO_TOKEN_TIMEOUT);
                            } else {
//                                ToastUtil.shortT(result);
                                listener.onFail(0);
                            }
                        } else {
//                            ToastUtil.shortT(result);
                        }
                    } else {
//                        ToastUtil.shortT(result);
                    }
                }
            }
        });
    }

    private ShareEntityBuilder getEntity(String title, String webUrl, String content) {
        ShareEntityBuilder entityBuilder = new ShareEntityBuilder().builder();
        if (title != null) {
            entityBuilder.setTitle(title);
        }
        if (content != null) {
            entityBuilder.setContent(content);
        }
        if (webUrl != null) {
            entityBuilder.setWebpageUrl(webUrl);
        }
        return entityBuilder;
    }

    private void shareWithBitmap(final Activity activity, final Extra extra, final ShareEntityBuilder builder,
                                 String imgUrl, final String shareUrl, final String videoUrl, final LoadListener listener, final boolean isToast) {
        // 下面这个方法会自动去判断网络和文件中是否有缓存
        DataProvider.getInstance().getImageLoader().loadImage(imgUrl, new ImageLoader.OnLoadListener() {
            @Override
            public void onSuccess(Drawable drawable, boolean immediate) {
                if (drawable == null) {
                    return;
                }

                Bitmap origin;

                if (drawable instanceof BitmapDrawable) {
                    origin = ((BitmapDrawable) drawable).getBitmap();
                } else {
                    origin = BitmapFactory.decodeResource(activity.getResources(), R.mipmap.ic_launcher);
                }

                Bitmap thumbBmp;
                if (extra == Extra.SINA) {
                    thumbBmp = Bitmap.createScaledBitmap(origin, origin.getWidth() - 1, origin.getHeight() - 1, true);
                } else {
                    int width = 100;
                    int height = 100;
                    if (origin.getWidth() == 100 && origin.getHeight() == 100) {
                        width = 101;
                        height = 101;
                    }
                    thumbBmp = Bitmap.createScaledBitmap(origin, width, height, true);
                }
                builder.setBitmap(thumbBmp);
                if (!Strings.isEmpty(videoUrl) && extra != Extra.WX_SESSION && extra != Extra.WX_TIMELINE) {
                    builder.setVideoUrl(videoUrl);
                    builder.setWebpageUrl(null);
                }
                share(activity, extra, builder.create(), listener, isToast);
//                if (listener != null) {
//                    listener.onSuccess();
//                }
            }

            @Override
            public void onFail() {
                if (listener != null) {
                    listener.onFail(ReturnCode.NO_NET);
                }
            }
        });
    }

    private void setImgUrlsBuilder(ShareEntityBuilder entityBuilder, String[] imgUrls) {
        ArrayList<String> list = new ArrayList<String>();
        if (imgUrls != null && imgUrls.length > 0) {
            for (int i = 0; i < imgUrls.length; i++) {
                list.add(imgUrls[i]);
            }
        }
        if (list.size() > 0) {
            entityBuilder.setNetworkImageUrls(list);
        }
    }

    /**
     * ******************************** 回调接口 ******************************************
     */
    public interface SocialResultListener {
        void onError(int code, String error);

        void onSuccess(User info);

        void dialogDismiss();

        void onStart();
    }

}
