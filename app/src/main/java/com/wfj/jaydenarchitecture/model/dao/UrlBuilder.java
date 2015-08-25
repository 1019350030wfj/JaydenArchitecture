package com.wfj.jaydenarchitecture.model.dao;

import android.annotation.SuppressLint;

import fbcore.security.Md5;
import fbcore.utils.Strings;

/**
 * @function 构造所有请求链接的公共参数
 * Created by Jayden on 2015/8/25.
 */
public class UrlBuilder {

    public static StringBuilder getPublicParamUrl() {
        if(!isDeviceVerified()){
            verifyDevice();
        }
        return buildPrefix();
    }

    private static StringBuilder buildPrefix() {
//        String key = SPHelper.getAuthKey(AppContext.getContext());
//        long cidInt = SPHelper.getAuthDeviceId(AppContext.getContext());

        // 这两个来自设备
//        String cid = (cidInt == 0) ? "10002" : (String.valueOf(cidInt));

        // 未注册设备时默认第一次用这个Key
//        key = key == null ? "uYz1ZS6AXNQGNlV8" : key;

//        String tms = TimeUtil.generateTimestamp();
//        String sig = getMd5Backward16(key + tms);

        // 这两个来自用户
        // String uid = "0";
//        String uid = UserManager.getInstance().getUser().id + "";
//        String wssig = getMd5Backward16(SPHelper.getAuthWSKey(AppContext.getContext()) + tms);
//        StringBuilder sb = new StringBuilder(AppContext.SERVER_HOST);
//        sb.append("?cid=").append(cid).append("&uid=").append(uid).append("&tms=").append(tms).append("&sig=")
//                .append(sig).append("&wssig=").append(wssig).append("&os_type=").append(AppContext.OS_TYPE)
//                .append("&version=").append(AppContext.APP_VERSION_CODE);
//        return sb;
        return new StringBuilder();
    }

    private static boolean isDeviceVerified() {
//        long cid = SPHelper.getAuthDeviceId(AppContext.getContext());
//        return cid != 0;
        return false;
    }

    private static void verifyDevice() {
//        String url = new StringBuilder(buildPrefix().append("&srv=1002").append("&device_id=")
//                .append(AppContext.DEVICE_ID).append("&os_version=").append(AppContext.OS_VERSION).append("&device_name=")
//                .append(AppContext.MODEL)).toString();
//
//        String result = new GetStringTask(url).execute();
//        TypeToken<Response<DeviceInfo>> token = new TypeToken<Response<DeviceInfo>>(){};
//        Response<DeviceInfo> deviceEntity = new Gson().fromJson(result, token.getType());
//
//        if (deviceEntity != null && deviceEntity.code == ReturnCode.RS_SUCCESS) {
//            SPHelper.setAuthDeviceInfo(AppContext.getContext(), deviceEntity.data.cid, deviceEntity.data.key);
//        }
    }

    /**
     * 返回md5值的后16位.
     *
     * @param src
     * @return
     */
    @SuppressLint("DefaultLocale")
    private static String getMd5Backward16(String src) {
        String md5 = Md5.digest32(src);
        if (!Strings.isMeaningful(md5)) {
            return null;
        }
        return md5.toLowerCase().substring(16);
    }
}
