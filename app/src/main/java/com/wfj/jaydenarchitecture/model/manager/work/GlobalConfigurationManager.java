package com.wfj.jaydenarchitecture.model.manager.work;

import android.content.Context;

import com.wfj.jaydenarchitecture.model.dao.ProjectDao;
import com.wfj.jaydenarchitecture.utils.MessageHintManager;


public class GlobalConfigurationManager {

    public static void getGlobalConfiguration(Context context, MessageHintManager.OnHasHint onHasHint, boolean showToast) {
        ProjectDao.getMessageHint(response -> {
            if (response.isSuccess()) {
                MessageHintManager.saveHint(context, response.data, onHasHint);
            }
        }, showToast);
    }
    
}
