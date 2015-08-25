package com.wfj.jaydenarchitecture.model.dao;

import com.google.gson.reflect.TypeToken;
import com.wfj.jaydenarchitecture.model.bean.Response;
import com.wfj.jaydenarchitecture.model.bean.UpdateInfo;

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
}
