package com.wfj.jaydenarchitecture.view.group.refreshLoadmore;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.view.widget.pullToRefresh.PullToRefreshLoadmoreListView;


/**
 * 下拉刷新基础部分
 */
public class BasePullListGroup<T> extends BaseListGroup<T> {

    private PullToRefreshLoadmoreListView pullToRefreshListView;

    public BasePullListGroup(Context context) {
        this(context, true, true);
    }

    public BasePullListGroup(Context context, boolean needPullRefresh, boolean needLoadMore) {
        super(context, needPullRefresh, needLoadMore);
    }

    @Override
    public ViewGroup containChildView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup parent = (ViewGroup) inflater.inflate(R.layout.main_pull_listview, null);
        return parent;
    }

    @Override
    public void initWidget() {
        pullToRefreshListView = (PullToRefreshLoadmoreListView) getRoot().findViewById(R.id.list);
        pullToRefreshListView.setRefleshLoadMoreEnable(isNeedPullRefresh(), isNeedLoadMore());
        pullToRefreshListView.setFooterLoadMoreOverText(getFooterLoadMoreOverText());
        pullToRefreshListView.initView(initHeaderView(), initFooterView());
        try {
            pullToRefreshListView.setAdapter((BaseGroupAdapter<T>) getListAdapter());
        } catch (Exception e) {
            throw new RuntimeException("必须BasePullListGroup的adapter必须继承BaseGroupAdapter");
        }

        setBackgroundColor(backgroundColor);
        setDivisionPadding(divisionPadding);
    }

    @Override
    public void initListener() {
        if (isNeedPullRefresh()) {
            pullToRefreshListView.setOnRefreshListener((refreshView) -> {
                pullToRefreshListView.resetLoadmoreEnable();
                refreshData();
            });
        }
        if (isNeedLoadMore()) {
            pullToRefreshListView.setOnLastItemVisibleListener(() -> {
                if (!pullToRefreshListView.isNoMoreDataStatus() && isShouldLoadMore()) {
                    pullToRefreshListView.setLoadMoreSatu(PullToRefreshLoadmoreListView.STATU_LOAD_MORE_START);
                    loadMoreData();
                }
            });
        }
        pullToRefreshListView.setOnItemClickListener((parent, view, position, id) -> {
            if (position - 1 < 0) {
                return;
            }
            if (getListAdapter().getCount() + 1 <= position) {
                return;
            }
            if (getOnItemClickListener() != null) {
                getOnItemClickListener().onItemClick(parent, view, position - 1, id);
            }
        });
    }

    @Override
    public void hideRefreshLoading() {
        if (pullToRefreshListView.isRefreshing()) {
            pullToRefreshListView.onRefreshComplete();
        }
    }

    @Override
    public void onLoadingHelpStateChange(boolean loadingHelpVisible) {
        pullToRefreshListView.setVisibility(loadingHelpVisible ? View.INVISIBLE : View.VISIBLE);
    }

    @Override
    public void hideLoadMoreLoading(boolean loadMoreSuccess) {
        if (loadMoreSuccess) {
            pullToRefreshListView.setLoadMoreSatu(PullToRefreshLoadmoreListView.STATU_LOAD_MORE_NO_DATA);
        } else {
            pullToRefreshListView.setLoadMoreSatu(PullToRefreshLoadmoreListView.STATU_LOAD_MORE_SUCCESS);
        }
    }

    public PullToRefreshLoadmoreListView getPullToRefreshListView() {
        return pullToRefreshListView;
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        if(pullToRefreshListView != null && color != -1) {
            pullToRefreshListView.setBackgroundColor(color);
        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void setDivisionPadding(int padding) {
        this.divisionPadding = padding;
        if(pullToRefreshListView != null && padding != -1) {
            pullToRefreshListView.setDividerPadding(padding);
        }
    }

    public View initHeaderView() {
        return null;
    }

    public View initFooterView() {
        return null;
    }

}
