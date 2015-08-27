package com.wfj.jaydenarchitecture.view.widget.recyclelistview;

import android.view.View;

/**
 * Create by：ml_bright on 2015/7/1 17:21
 * Email: 2504509903@qq.com
 */
public interface IRecycleView {

    /**
     * 使用自定义的头部和尾部
     * @param headerView
     * @param footerView
     */
    void initCustomHeaderAndFooter(View headerView, View footerView);

    /**
     * 重置加载更多状态（恢复加载更多）
     */
    void resetLoadMoreStatus();

    /**
     * 加载更多的状态是否是没有更多数据了
     */
    boolean isNoMoreDataStatus();

    /**
     * 是否正在处于下拉刷新中
     */
    boolean isRefreshing();

    /**
     * 完成下拉刷新动作
     */
    void onRefreshComplete();

    /**
     * 设置加载更多状态为正在加载更多状态
     */
    void setLoadMoreStart();

    /**
     * 设置加载更多状态为唯有更多数据
     */
    void setLoadMoreNoMoreData();

    /**
     * 设置加载更多状态为成功加载数据
     */
    void setLoadMoreSuccess();

    /**
     * 设置分割线间距
     * @param padding
     */
    void setDividerPadding(int padding);

}
