package com.wfj.jaydenarchitecture.view.module.launch;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.model.manager.work.GlobalConfigurationManager;
import com.wfj.jaydenarchitecture.utils.SPHelper;
import com.wfj.jaydenarchitecture.view.TitleBar;
import com.wfj.jaydenarchitecture.view.module.BaseTitleBarActivity;
import com.wfj.jaydenarchitecture.view.widget.ActivityAnimator;

/**
 * Created by Jayden on 2015/8/27.
 */
public class LaunchActivity extends BaseTitleBarActivity{

    private static final int DELAY_MILLIS = 1000;

    public LaunchActivity() {
        super(false);
    }

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_launch,null);
    }

    @Override
    public void setTitleBar(TitleBar titlebar) {

    }

    @Override
    public void initWidget(View view) {

    }

    @Override
    public void initContentView() {
        GlobalConfigurationManager.getGlobalConfiguration(this,null,true);
        postDelayed(() -> {
            Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
            startActivity(intent);

            ActivityAnimator.getInstance().transBackLeft(LaunchActivity.this);

            if (SPHelper.needGuide(LaunchActivity.this)) {
                SPHelper.cancelGuide(LaunchActivity.this);
                Intent intent2 = new Intent(LaunchActivity.this, GuideActivity.class);
                startActivity(intent2);
            } else {
//                int type = getIntent().getIntExtra(AppLauncherReceiver.IK_TYPE, -1);
//                int messageType = getIntent().getIntExtra(AppLauncherReceiver.IK_MESSAGE_TYPE, -1);
//                int id = getIntent().getIntExtra(AppLauncherReceiver.IK_TAG, -1);
//                if (type != -1 && messageType != -1 && id != -1) {
////                    AppLauncherReceiver.launcherSwitch(LaunchActivity.this, type, messageType, id);
//                }
            }

            finish();
        }, DELAY_MILLIS);
    }

    @Override
    public void initListener() {

    }
}
