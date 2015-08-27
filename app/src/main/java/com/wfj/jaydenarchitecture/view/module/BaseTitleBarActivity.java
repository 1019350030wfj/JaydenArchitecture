package com.wfj.jaydenarchitecture.view.module;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.wfj.jaydenarchitecture.view.TitleBar;
import com.wfj.jaydenarchitecture.view.WholeLayoutHelper;

/**
 * Created by Jayden on 2015/8/27.
 */
public abstract class BaseTitleBarActivity extends BaseActivity {

    private WholeLayoutHelper mTitleHelper;
    private TitleBar mTitle;

    private boolean isTitleBarNeedShow;

    public BaseTitleBarActivity() {
        this(true);
    }

    public BaseTitleBarActivity(boolean isTitleBarNeedShow) {
        this.isTitleBarNeedShow = isTitleBarNeedShow;
    }

    protected RelativeLayout getRoot() {
        return mTitleHelper.getBaseView();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTitleHelper = new WholeLayoutHelper(this, getContentView(), isTitleBarNeedShow);
        mTitle = mTitleHelper.getTitle();

        setContentView(mTitleHelper.getBaseView());
        initWidget(mTitleHelper.getContentView());
        initContentView();
        initListener();

        if (isTitleBarNeedShow) {
            setTitleBar(mTitle);
            mTitle.setOnCloseListener(() -> {
                if(needCloseActivity()) {
                    this.finish();
                }
            });
        }
    }

    public boolean needCloseActivity() {
        return true;
    }

    protected <T extends View> T findView(int id) {
        return (T) findViewById(id);
    }

    protected <T extends View> T findView(View parent, int id) {
        return (T) parent.findViewById(id);
    }

    public abstract View getContentView();
    public abstract void setTitleBar(TitleBar titlebar);
    public abstract void initWidget(View view);
    public abstract void initContentView();
    public abstract void initListener();
}
