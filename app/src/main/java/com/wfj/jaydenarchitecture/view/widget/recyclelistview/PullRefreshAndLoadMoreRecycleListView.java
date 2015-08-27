package com.wfj.jaydenarchitecture.view.widget.recyclelistview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;


/**
 *
 * create by: ml_bright on 2015/7/1 17:48
 */
public class PullRefreshAndLoadMoreRecycleListView extends DRecyclerView implements IRecycleView {

    private boolean isNoMoreData = false;

    private boolean needLoadMoreEnable;

    public PullRefreshAndLoadMoreRecycleListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void initCustomHeaderAndFooter(View headerView, View footerView) {

    }

    @Override
    public void resetLoadMoreStatus() {
        if(needLoadMoreEnable) {
            setLoadMoreEnable(true);
        }
    }

    @Override
    public boolean isNoMoreDataStatus() {
        return isLoadMoreEnable();
    }

    @Override
    public boolean isRefreshing() {
        return getSwipeRefreshLayout().isRefreshing();
    }

    @Override
    public void onRefreshComplete() {
        setRefreshing(false);
    }

    @Override
    public void setLoadMoreStart() {

    }

    @Override
    public void setLoadMoreEnable(boolean enable) {
        super.setLoadMoreEnable(enable);
        needLoadMoreEnable = enable;
    }

    @Override
    public void setLoadMoreNoMoreData() {
        promptEnd();
//        setLoadMoreEnable(false);
    }

    @Override
    public void setLoadMoreSuccess() {
        setLoading(false);
    }

    @Override
    public void setDividerPadding(int padding) {

    }

}
