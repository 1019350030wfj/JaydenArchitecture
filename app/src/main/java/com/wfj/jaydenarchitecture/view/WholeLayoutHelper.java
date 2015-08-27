package com.wfj.jaydenarchitecture.view;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.GravityCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.wfj.jaydenarchitecture.R;


public class WholeLayoutHelper {
    /* 上下文，创建view的时候需要用到 */
    private Context mContext;
    /* base view */
    // private CoordinatorLayout mContentView;
    private RelativeLayout mContentView;
    /* 用户定义的view */
    private View mUserView;

    private RelativeLayout mContainer;
    private FrameLayout mTitleLayout;

    private TitleBar mTitleBar;
    /* 视图构造器 */
    private LayoutInflater mInflater;

    private boolean isTitleBarNeedShow;

    public WholeLayoutHelper(Context context, View contentView, boolean isTitleBarNeedShow) {
        this.mContext = context;
        this.isTitleBarNeedShow = isTitleBarNeedShow;
        this.mUserView = contentView;

        mInflater = LayoutInflater.from(mContext);
        /* 初始化整个内容 */
        initContentView();
        /* 初始化toolbar */
        initTitlebar();
        initUserView(contentView);
    }

    private void initContentView() {
        /* 直接创建一个帧布局，作为视图容器的父容器 */
        // mContentView = (CoordinatorLayout)
        // mInflater.inflate(R.layout.base_layout,null);
        /*直接创建一个帧布局，作为视图容器的父容器*/
        //mContentView = (CoordinatorLayout) mInflater.inflate(R.layout.base_layout,null);
        mContentView = (RelativeLayout) mInflater.inflate(R.layout.base_linearlayout, null);
        mContainer = (RelativeLayout) mContentView.findViewById(R.id.container);
        mTitleLayout = (FrameLayout) mContentView.findViewById(R.id.frame_title);
    }

    private void initTitlebar() {
        if (isTitleBarNeedShow) {
            /* 通过inflater获取toolbar的布局文件 */
            View view = mInflater.inflate(R.layout.base_titlebar, mTitleLayout);
            mTitleBar = new TitleBar((RelativeLayout) view.findViewById(R.id.title));
            // mTitleLayout.addView(view);
        }
    }

    private void initUserView(View contentView) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);
        params.gravity = Gravity.BOTTOM | GravityCompat.END;
        /*
         * if (isTitleBarNeedShow) { float topMargin =
         * mContext.getResources().getDimension(R.dimen.title_height);
         * params.topMargin = (int) (topMargin + 0.5f); }
         */
        mContainer.addView(contentView, params);
    }

    public View getContentView() {
        return mUserView;
    }

    public TitleBar getTitle() {
        return mTitleBar;
    }

    public RelativeLayout getBaseView() {
        return mContentView;
    }

    public RelativeLayout getContainer() {
        return mContainer;
    }

    public CoordinatorLayout.LayoutParams getTitleParams() {
        CoordinatorLayout.LayoutParams params = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.TOP | GravityCompat.START;
        return params;
    }
}
