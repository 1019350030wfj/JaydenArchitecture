package com.wfj.jaydenarchitecture.view.group.recycleview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.view.group.AbsListGroup;
import com.wfj.jaydenarchitecture.view.utils.BundleUtil;
import com.wfj.jaydenarchitecture.view.widget.recyclelistview.DRecyclerView;
import com.wfj.jaydenarchitecture.view.widget.recyclelistview.PullRefreshAndLoadMoreRecycleListView;


public class BaseRecyclePullListGroup<T> extends AbsListGroup<T> {

    private RecyclerView.ItemDecoration itemDecoration;

    private boolean needPullRefresh;
    private boolean needLoadMore;

    private ViewGroup mBackGroundView;

    private PullRefreshAndLoadMoreRecycleListView pullToRefreshListView;

    public BaseRecyclePullListGroup(Context context){
        this(context, true, true);
    }

    public BaseRecyclePullListGroup(Context context, boolean needPullRefresh, boolean needLoadMore) {
        super(context);
        this.needPullRefresh = needPullRefresh;
        this.needLoadMore = needLoadMore;
    }

    @Override
    public ViewGroup containChildView() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        ViewGroup mParent = (ViewGroup) inflater.inflate(R.layout.main_recycle_listview, null);
        mBackGroundView = (ViewGroup) mParent.findViewById(R.id.container);
        return mParent;
    }

    public PullRefreshAndLoadMoreRecycleListView getPullToRefreshListView() {
        return pullToRefreshListView;
    }

    @Override
    public void initWidget() {
        pullToRefreshListView = (PullRefreshAndLoadMoreRecycleListView) getRoot().findViewById(R.id.listview);
        pullToRefreshListView.setLoadMoreEnable(needLoadMore);
        pullToRefreshListView.setRefreshEnabled(needPullRefresh);
        pullToRefreshListView.setFooterLoadMoreOverText(getFooterLoadMoreOverText());
        pullToRefreshListView.initCustomHeaderAndFooter(initHeaderView(), initFooterView());
        try {
            pullToRefreshListView.setAdapter((BaseRecycleGroupAdapter) getListAdapter());
        } catch (Exception e) {
            throw new RuntimeException("必须BasePullRecycleGroup的adapter必须继承BaseRecycleGroupAdapter");
        }
        setListViewType(pullToRefreshListView);

        setBackgroundColor(backgroundColor);
        setDivisionPadding(divisionPadding);
        setDecoration(itemDecoration);
        //addOnScrollListener(mScrollListener, l);
    }

    public void setListViewType(PullRefreshAndLoadMoreRecycleListView pullToRefreshListView){
    }

    @Override
    public void initListener() {
        pullToRefreshListView.setOnLoadListener(new DRecyclerView.OnLoadListener() {
            @Override
            public void onRefresh() {
                if (needPullRefresh) {
                    //这里要恢复加载更多
                    pullToRefreshListView.resetLoadMoreStatus();
                    refreshData();
                }
            }

            @Override
            public void onLoadMore() {
                if(!isShouldLoadMore()) {
                    return;
                }
                pullToRefreshListView.setLoadMoreStart();
                loadMoreData();
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
    public void hideLoadMoreLoading(boolean loadMoreSuccess) {
        if (loadMoreSuccess) {
            pullToRefreshListView.setLoadMoreNoMoreData();
        } else {
            pullToRefreshListView.setLoadMoreSuccess();
        }
    }

    @Override
    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        if(pullToRefreshListView != null && backgroundColor != -1) {
//            pullToRefreshListView.setBackgroundColor(backgroundColor);
            mBackGroundView.setBackgroundColor(backgroundColor);
        }
    }

    @Override
    public void setDivisionPadding(int padding) {
        this.divisionPadding = padding;
        if(pullToRefreshListView != null && padding != -1) {
            pullToRefreshListView.setDividerPadding(padding);
        }
    }

    public void setDecoration(RecyclerView.ItemDecoration decoration){
        this.itemDecoration = decoration;
        if(pullToRefreshListView != null && itemDecoration != null) {
            pullToRefreshListView.getRecyclerView().addItemDecoration(itemDecoration);
        }
    }

    public void onDataChange(Bundle bundle) {
        if(bundle == null) {
            return;
        }
        int adapterPosition = bundle.getInt(BundleUtil.KEY_ART_DETAIL_ADAPTER_POSITION);
        T artwork = (T) bundle.getSerializable(BundleUtil.KEY_ART_DETAIL_ARTWORK);
        refreshItemLocal(adapterPosition, artwork);
    }

    @Override
    public void onLoadingHelpStateChange(boolean loadingHelpVisible) {
        pullToRefreshListView.setVisibility(loadingHelpVisible ? View.INVISIBLE : View.VISIBLE);
    }

    public View initHeaderView() {
        return null;
    }

    public View initFooterView() {
        return null;
    }

}
