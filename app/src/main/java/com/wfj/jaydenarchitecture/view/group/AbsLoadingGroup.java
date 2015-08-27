package com.wfj.jaydenarchitecture.view.group;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.model.dao.ReturnCode;

/**
 * @function 正在加载中，加载数据失败等信息提示的组件
 *
 * Created by Jayden on 2015/8/26.
 */
public abstract class AbsLoadingGroup {

    /**
     * 当前页面的根布局
     */
    private ViewGroup root;
    private ViewGroup contentRoot;

    /**
     * 显示加载状态页面的帮助类
     */
    private LoadingHelper loadingHelper;

    /**
     * group组件的配置信息
     */
    private GroupConfig groupConfig;

    private Handler mHandle;

    public AbsLoadingGroup() {
        mHandle = new Handler(Looper.getMainLooper());
        groupConfig = GroupConfig.create(GroupConfig.DEFAULT);
    }

    /**
     * 当进入当前页面后,去服务器获取数据的时候 , 显示加载状态的页面
     */
    public void launchLoadHelper() {
        launchLoadHelper(0);
    }

    public void launchLoadHelper(int color) {
        onLoadingHelpStateChange(true);
        if (loadingHelper == null) {
            loadingHelper = LoadingHelper.generateOnParentAtPosition(contentRoot, 0);
        }
        if (color != 0) {
            loadingHelper.setBackgroundColor(color);
        }
        loadingHelper.start();
    }

    /**
     * 当前是否处于正在加载中的布局
     */
    public boolean isShowLoading() {
        return loadingHelper == null ? false : loadingHelper.isShowLoading();
    }

    /**
     * 得到loading页的根布局
     */
    public ViewGroup getLoadingRootView() {
        return loadingHelper == null ? null : loadingHelper.getLoadingRootView();
    }

    /**
     * 当根布局内容填充完成后, 隐藏加载状态相关的页面
     */
    public void hideLoadHelper() {
        onLoadingHelpStateChange(false);
        if (loadingHelper != null) {
            loadingHelper.end();
            loadingHelper = null;
        }
    }

    public void showFailMessage(int code) {
        showFailMessage(code, groupConfig.getMessage(code));
    }

    /**
     * 当从服务器获取数据失败的时候显示加载失败的 页面
     */
    public void showFailMessage(int code, String message) {
        if (loadingHelper == null) {
            return;
        }
        loadingHelper.fail(message, code == ReturnCode.NO_NET ? view -> {
            onLoadingHelperFailClick();
        } : null);
    }

    public View getTopView() {
        return null;
    }

    public void setRoot(ViewGroup contentRoot) {
        this.contentRoot = contentRoot;
        if(contentRoot == null) {
            this.root = null;
            return;
        }

        View top = getTopView();
        if(top != null) {
            root = (ViewGroup) LayoutInflater.from(contentRoot.getContext()).inflate(R.layout.group_layout, null);
            LinearLayout topLayout = (LinearLayout) root.findViewById(R.id.top_layout);
            topLayout.addView(top, 0, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            LinearLayout contentLayout = (LinearLayout) root.findViewById(R.id.content_layout);
            contentLayout.addView(contentRoot, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        } else {
            this.root = contentRoot;
        }
    }

    public ViewGroup getRoot() {
        return root;
    }

    public void setGroupConfig(GroupConfig config) {
        this.groupConfig = config;
    }

    public abstract void onLoadingHelpStateChange(boolean loadingHelpVisible);

    public abstract void onLoadingHelperFailClick();

    public abstract void onCreateView();

    public void onResetView() {
    }

    public void onDestroyView() {
        if (mHandle != null) {
            mHandle.removeCallbacksAndMessages(null);
            mHandle = null;
        }
    }

    public void postOnUiThread(Runnable runnable) {
        if (mHandle != null) {
            mHandle.post(runnable);
        }
    }
}
