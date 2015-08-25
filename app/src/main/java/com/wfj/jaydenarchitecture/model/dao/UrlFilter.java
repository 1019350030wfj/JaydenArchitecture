package com.wfj.jaydenarchitecture.model.dao;

/**
 * 过滤URL中与实践或者用户相关的字段，便于缓存下来
 * create by: ml_bright on 2015/6/30 18:15
 */
public class UrlFilter {

    // 与时间或者用户等相关属性过滤字段
    private static final String FIELD_SIG = "sig";
    private static final String FIELD_WSSIG = "wssig";
    private static final String FIELD_UID = "uid";
    private static final String FIELD_TMS = "tms";
    private static final String FIELD_OS_TYPE = "os_type";

    public static String getFileNameFromUrl(String url) {
        StringBuilder result = new StringBuilder();
        String[] temps = url.split("&");
        for (int i = 0; i < temps.length; i++) {
            if(i == 0) {
                result.append(temps[i]);
                continue;
            }
            String pre = temps[i].split("=")[0];
            if (pre.equals(FIELD_SIG) || pre.equals(FIELD_WSSIG) 
                    || pre.equals(FIELD_UID) || pre.equals(FIELD_TMS)
                    || pre.equals(FIELD_OS_TYPE)) {
                continue;
            }
            result.append("&" + temps[i]);
        }
        return result.toString();
    }

}
