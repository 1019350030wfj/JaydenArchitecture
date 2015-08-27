package com.wfj.jaydenarchitecture.view.module;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.umeng.analytics.MobclickAgent;
import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.social.SocialManager;
import com.wfj.jaydenarchitecture.view.widget.SystemBarTintManager;

/**
 * Activity基类
 *
 * Created by Jayden on 2015/8/27.
 */
public abstract class BaseActivity extends AppCompatActivity {

    private Handler mHandler;

    public BaseActivity() {
        mHandler = new Handler(getMainLooper());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MobclickAgent.openActivityDurationTrack(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        initStatusBar(initStatueBarColor());
    }

    // 设置沉浸式状态栏
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 初始化状态栏
     *
     * @param color 状态栏颜色
     */
    private void initStatusBar(int color) {
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    /**
     * 返回状态栏颜色值
     *
     * @return
     */
    protected int initStatueBarColor() {
        return R.color.primaryDark;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SocialManager.getInstance(this).onActivityResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        SocialManager.getInstance(this).onNewIntent(this, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    public void postDelayed(Runnable run, int delayMillis) {
        if (mHandler == null) {
            return;
        }
        mHandler.postDelayed(run, delayMillis);
    }

    public void postOnUiThread(Runnable run) {
        if (mHandler == null) {
            return;
        }
        mHandler.post(run);
    }

    public void removeHandleRunnable(Runnable run) {
        if (mHandler == null || run == null) {
            return;
        }
        mHandler.removeCallbacks(run);
    }
}
