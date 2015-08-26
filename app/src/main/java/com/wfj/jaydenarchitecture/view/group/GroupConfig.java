package com.wfj.jaydenarchitecture.view.group;

import android.util.SparseArray;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.model.dao.ReturnCode;

import java.util.ArrayList;
import java.util.List;

import fbcore.utils.Strings;

/**
 * 信息加载失败，根据这个配置类，显示错误loading页
 * Created by Jayden on 2015/8/26.
 */
public class GroupConfig {

    public static final int DEFAULT = 0; //默认
    public static final int ART_DETAIL = 1; //艺术品详情页
    public static final int MESSAGE_NOTICE = 2; //消息-通知列表
    public static final int MESSAGE_MESSAGE = 3; //消息-私信列表
    public static final int PERSON_PUBLISH = 4; //个人中心-发布
    public static final int PERSON_COLLECT = 5; //个人中心-收藏

    //所有返回码的集合
    private static List<Integer> allCodeList = new ArrayList<>();

    private SparseArray<String> sparseArray;

    public static GroupConfig create(int type) {
        GroupConfig groupConfig = new GroupConfig();

        //添加默认的一些错误码键值对
        groupConfig.addFailCodeToSA(ReturnCode.NO_NET, getString(R.string.loading_default_fail_text));
        groupConfig.addFailCodeToSA(ReturnCode.RS_EMPTY_ERROR, getString(R.string.fail_msg_1004));
        switch (type) {
            case ART_DETAIL:
                groupConfig.addFailCodeToSA(ReturnCode.RS_IS_DELETE_OR_INEXISTENCE, getString(R.string.fail_msg_1015));
                break;
            case MESSAGE_NOTICE:
                groupConfig.addFailCodeToSA(ReturnCode.RS_EMPTY_ERROR, "暂无通知");
                break;
            case MESSAGE_MESSAGE:
                groupConfig.addFailCodeToSA(ReturnCode.RS_EMPTY_ERROR, "暂无私信");
                break;
            case PERSON_PUBLISH:
                groupConfig.addFailCodeToSA(ReturnCode.RS_EMPTY_ERROR, "暂无发布");
                break;
            case PERSON_COLLECT:
                groupConfig.addFailCodeToSA(ReturnCode.RS_EMPTY_ERROR, "暂无收藏");
                break;
        }

        return groupConfig;
    }

    //TODO
    private static String getString(int resId) {
//        return AppContext.getContext().getString(resId);
        return "";
    }

    private void addFailCodeToSA(int failCode, String failMsg) {
        if (Strings.isEmpty(sparseArray.get(failCode))) {
            sparseArray.append(failCode,failMsg);
        }
    }
}
