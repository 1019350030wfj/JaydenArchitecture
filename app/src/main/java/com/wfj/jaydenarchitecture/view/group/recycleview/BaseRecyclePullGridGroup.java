package com.wfj.jaydenarchitecture.view.group.recycleview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;

import com.wfj.jaydenarchitecture.view.widget.recyclelistview.PullRefreshAndLoadMoreRecycleListView;

public class BaseRecyclePullGridGroup<T> extends BaseRecyclePullListGroup<T> {

    public BaseRecyclePullGridGroup(Context context) {
        super(context);
    }

    public BaseRecyclePullGridGroup(Context context, boolean needPullRefresh, boolean needLoadMore) {
        super(context, needPullRefresh, needLoadMore);
    }

    @Override
    public void setListViewType(PullRefreshAndLoadMoreRecycleListView pullToRefreshListView) {
        pullToRefreshListView.getRecyclerView().setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));
    }

}
