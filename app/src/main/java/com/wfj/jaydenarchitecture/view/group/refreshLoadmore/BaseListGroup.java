package com.wfj.jaydenarchitecture.view.group.refreshLoadmore;

import android.content.Context;
import android.widget.AdapterView;

import com.wfj.jaydenarchitecture.view.group.AbsListGroup;


public abstract class BaseListGroup<T> extends AbsListGroup<T> {

    private boolean needPullRefresh;
    private boolean needLoadMore;

    private AdapterView.OnItemClickListener onItemClickListener;

    public BaseListGroup(Context context) {
        this(context, true, true);
    }

    public BaseListGroup(Context context, boolean needPullRefresh, boolean needLoadMore) {
        super(context);
        this.needPullRefresh = needPullRefresh;
        this.needLoadMore = needLoadMore;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected boolean isNeedPullRefresh() {
        return needPullRefresh;
    }

    protected boolean isNeedLoadMore() {
        return needLoadMore;
    }

    protected AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

}
