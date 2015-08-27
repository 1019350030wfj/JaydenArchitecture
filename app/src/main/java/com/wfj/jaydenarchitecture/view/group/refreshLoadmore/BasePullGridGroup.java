package com.wfj.jaydenarchitecture.view.group.refreshLoadmore;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.view.widget.pullToRefresh.PullToRefreshGridView;


/**
 * Gridview列表组件
 *
 */
public class BasePullGridGroup<T> extends BaseListGroup<T> {

    private PullToRefreshGridView mPullGridView;
    private GridView gridView;

    public BasePullGridGroup(Context context) {
        this(context, true, true);
    }

    public BasePullGridGroup(Context context, boolean needPullReflesh, boolean needLoadMore) {
        super(context, needPullReflesh, needLoadMore);
    }

    @Override
    public ViewGroup containChildView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.main_pull_gridview, null);
        return parent;
    }

    @Override
    public void initWidget() {
        mPullGridView = (PullToRefreshGridView) getRoot().findViewById(R.id.list);
        mPullGridView.setRefreshEnable(isNeedPullRefresh());
        gridView = mPullGridView.getRefreshableView();
        gridView.setAdapter((ListAdapter) getListAdapter());
    }

    @Override
    public void initListener() {
        mPullGridView.setOnRefreshListener((refreshView) -> {
            refreshData();
        });
        mPullGridView.setOnLastItemVisibleListener(() -> {

        });

        gridView.setOnItemClickListener((parent, view, position, id) -> {
            if (getOnItemClickListener() != null) {
                getOnItemClickListener().onItemClick(parent, view, position, id);
            }
        });
    }

    @Override
    public void onLoadingHelpStateChange(boolean loadingHelpVisible) {
        mPullGridView.setVisibility(loadingHelpVisible ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void hideRefreshLoading() {
        if (mPullGridView.isRefreshing()) {
            mPullGridView.onRefreshComplete();
        }
    }

    @Override
    public void hideLoadMoreLoading(boolean loadMoreSuccess) {

    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        if(mPullGridView != null && backgroundColor != -1) {
            mPullGridView.setBackgroundColor(backgroundColor);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setDivisionPadding(int padding) {
        this.divisionPadding = padding;
        if(mPullGridView != null && padding != -1) {
            mPullGridView.setDividerPadding(padding);
        }
    }

}
