package com.wfj.jaydenarchitecture.view.widget.recyclelistview;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.utils.UIUtil;
import com.wfj.jaydenarchitecture.view.module.main.DividerDecoration;


/**
 * -----------------------------------------------------------
 * 版 权 ： BigTiger 版权所有 (c) 2015
 * 作 者 : BigTiger
 * 版 本 ： 1.0
 * 创建日期 ：2015/6/29 9:59
 * 描 述 ：基于SwipeRefreshLayout的带有下拉刷新和加载更多功能的RecyclerView
 * <p>
 * -------------------------------------------------------------
 */
public class DRecyclerView extends RelativeLayout {
    private static final String TAG = DRecyclerView.class.getSimpleName();
    private static final int DEFAULT_SPAN_COUNT = 1;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    //还不清楚GridLayoutManager与LinearLayoutManager的性能差别。假如性能有区别，之后需要根据SpanCount使用不同的LayoutManager
    private GridLayoutManager mLayoutManager;
    private HeaderAdapter mAdapter;
    private OnLoadListener mListener;
    private RecyclerView.OnScrollListener mScrollListener; // 用户的 scroll listener

    // load more
    private boolean mEnableLoadMore = true; // 是否开启列表加载更多功能
    private boolean mEnableAutoLoadMore = true; // 是否开启列表到底自动加载更多
    private boolean mLoading = false; // 是否加载中
    //加载更多的FootView 的控制器
    private DRecyclerViewPrompt mPromptView;

    public DRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }

        initView();
        parseAttrs(attrs);
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.d_recycler_layout, this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.d_refresh_layout);
        mRecyclerView = (RecyclerView) findViewById(R.id.d_recycler);

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListener != null) {
                    mListener.onRefresh();
                }
            }
        });

        // recycler
        mLayoutManager = new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mScrollListener != null) {
                    mScrollListener.onScrollStateChanged(recyclerView, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mScrollListener != null) {
                    mScrollListener.onScrolled(recyclerView, dx, dy);
                }
                RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
                int last = 0;
                if (layoutManager instanceof LinearLayoutManager){
                    last = ((LinearLayoutManager)layoutManager).findLastVisibleItemPosition();
                }else if (layoutManager instanceof GridLayoutManager){
                    last = ((GridLayoutManager)layoutManager).findLastVisibleItemPosition();
                }
                if (mEnableLoadMore && mEnableAutoLoadMore && !mLoading && last == layoutManager.getItemCount() - 1 ) {
                    // 开启加载更多，并且列表滑动到最后一项
                    performLoadMore();
                }
            }
        });

        // 加载更多
        mPromptView = new DRecyclerViewPrompt(getContext());
        mPromptView.setMinimumWidth(UIUtil.getScreenWidth());

        //mFooter.addView(mPromptView);
        mPromptView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mEnableLoadMore && !mLoading) {
                    performLoadMore();
                }
            }
        });
    }

    private void parseAttrs(AttributeSet attrs) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.DRecyclerView);

        int spanCount = a.getInt(R.styleable.DRecyclerView_spanCount, 1);
        setSpanCount(spanCount);

        int overScrollMode = a.getInt(R.styleable.DRecyclerView_android_overScrollMode, OVER_SCROLL_NEVER);
        setRecyclerOverScrollMode(overScrollMode);

        a.recycle();
    }

    /**
     * 设置条目之间的间隔
     * @param divider
     */
    public void addItemDecoration(DividerDecoration divider){
        mRecyclerView.addItemDecoration(divider);
    }

    public interface OnLoadListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * 设置数据加载和刷新状态的监听
     * @param listener
     */
    public void setOnLoadListener(OnLoadListener listener) {
        mListener = listener;
    }

    public void setRecyclerOverScrollMode(int overScrollMode) {
        mRecyclerView.setOverScrollMode(overScrollMode);
    }

    /**
     * 设置跨度数量
     */
    public void setSpanCount(int spanCount) {
        mLayoutManager.setSpanCount(spanCount);
    }

    /**
     * 获取跨度数量
     * @return
     */
    public int getSpanCount() {
        return mLayoutManager.getSpanCount();
    }

    /**
     * 设置跨度大小查找器，提供每项的宽度数据
     */
    public void setSpanSizeLookup(final GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        mLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return mAdapter.getItemViewType(position) == HeaderAdapter.ITEM_VIEW_TYPE_FOOTER ? mLayoutManager.getSpanCount() : spanSizeLookup.getSpanSize(position);
            }
        });
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        mRecyclerView.setOnTouchListener(l);
    }

    /**
     * 设置Adapter，Adapter将被内部类引用来提供功能
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (adapter == null) {
            throw new IllegalArgumentException("adapter not null");
        }
        mAdapter = new HeaderAdapter(adapter, mPromptView);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 添加头布局
     * @param header
     */
    public void addHeader(View header){
        if (mAdapter != null){
            mAdapter.addHeader(header);
        }
    }

    /**
     * 移除头布局
     */
    public void removeHead(){
        if (mAdapter != null){
            mAdapter.removeHeader();
        }
    }

    public boolean isHeaderAdd(){
        return mAdapter == null? false: mAdapter.haveHeader();
    }

    /**
     *  设置每个数据集里面的item 都是相同的类型
     * @param
     */
    public void setAdapterHasStableIds(boolean hasStableIds) {
        if (mAdapter != null) {
            mAdapter.setHasStableIds(hasStableIds);
        }
    }

    /**
     * 设置滚动监听
     * @param listener
     */
    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        mScrollListener = listener;
    }

    /**
     * 获取内部真正的RecyclerView
     * @return
     */
    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    /**
     * 获取RecyclerView 的布局管理器
     * @return
     */
    public LinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    /**
     * 获取真正用于下拉刷新的SwipeRefreshLayout
     * @return
     */
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return mSwipeRefreshLayout;
    }

    /**
     * 设置 View是否可以下拉刷新
     * @param enabled
     */
    public void setRefreshEnabled(boolean enabled) {
        mSwipeRefreshLayout.setEnabled(enabled);
    }

    public void setColorSchemeColors(int... colors) {
        mSwipeRefreshLayout.setColorSchemeColors(colors);
    }

    public void setColorSchemeResources(int... colorResIds) {
        mSwipeRefreshLayout.setColorSchemeResources(colorResIds);
    }

    /**
     * 直接设置显示刷新状态
     * @param refreshing true:显示刷新 , false: 取消刷新
     */
    public void setRefreshing(boolean refreshing) {
        mSwipeRefreshLayout.setRefreshing(refreshing);
    }

    public void setLoading(boolean loading) {
        mLoading = loading;
        if (!mEnableAutoLoadMore) {
            setFooterLoading(loading);
        }
    }

    /**
     * 获取当前DRecyclerView是否可以加载更多
     * @return true,表示可以加载更多  false 表示不可以加载更多
     */
    public boolean isLoadMoreEnable(){
        return mEnableLoadMore;
    }

    /**
     * 当加载完成后发现数据为空,代表不可以再加载更多
     * @param prompt 提示信息
     */
    public void promptEmpty(String prompt) {
        mEnableLoadMore = false;
        mLoading = false;
        mPromptView.empty(prompt);
    }

    /**
     * 显示正在加载中的状态
     */
    public void promptLoading() {
        mEnableLoadMore = false;
        mLoading = false;
        mPromptView.loading();
    }

    /**
     * 设置加载到数据末尾状态(之后就不会再显示加载更多状态)
     */
    public void promptEnd() {
        mEnableLoadMore = false;
        mLoading = false;
        mPromptView.moreEnd();
    }

    /**
     * 设置加载更多提示
     *
     * @param loading
     */
    private void setFooterLoading(boolean loading) {
        if (loading) {
            mPromptView.moreLoading();
        } else {
            mPromptView.moreButton();
        }
    }

    /**
     * 是否开启加载更多
     * 开启时，列表末尾显示一个Footer，展示加载状态
     * 关闭时，列表末尾隐藏Footer
     */
    public void setLoadMoreEnable(boolean enable) {
        mEnableLoadMore = enable;
        mLoading = false;
        if (mEnableLoadMore) {
            if (mEnableAutoLoadMore) {
                mPromptView.moreLoading();
            } else {
                mPromptView.moreButton();
            }
        } else {
            mPromptView.hide();
        }
    }

    /**
     * 是否开启列表到底自动加载更多
     * 开启时，列表滑动到底自动加载更多，这时Footer长显progress不变
     * 关闭时，手动点击执行加载更多
     */
    public void setAutoLoadMore(boolean enable) {
        mEnableAutoLoadMore = enable;
        if (enable) {
            setFooterLoading(true);
        }
    }

    /**
     * 执行加载更多，改变Footer显示样式，执行监听回调
     */
    private void performLoadMore() {
        setLoading(true);
        if (mListener != null) {
            mListener.onLoadMore();
        }
    }

/*    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.e("mSwipeRefreshLayout",mSwipeRefreshLayout.onInterceptTouchEvent(ev)+"");
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mRecyclerView.requestDisallowInterceptTouchEvent(true);
        return mRecyclerView.onTouchEvent(event);
    }*/

    /**
     * 提供一个固定的FooterView，并通过引用外部Adapter，提供外部Adapter的绝大部分功能。
     * hasStableIds方法无法覆盖，所以对外提供setAdapterHasStableIds方法
     *
     * @see #setHasStableIds
     */
    private static class HeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private static final int ITEM_VIEW_TYPE_FOOTER = -1; // 特别注意，WrappedAdapter中的ItemType不能为-1或者-2，否则会造成冲突。建议从0开始递增
        private static final int ITEM_VIEW_TYPE_HEADER = -2;

        private RecyclerView.Adapter<RecyclerView.ViewHolder> adapter; // 暂时限制adapter不为null
        private View loadMoreView;
        private View headerView;

        private HeaderAdapter(RecyclerView.Adapter<RecyclerView.ViewHolder> adapter, View footerView) {
            this.adapter = adapter;
            this.loadMoreView = footerView;
        }

        private void addHeader(View headerView){
            this.headerView = headerView;
        }

        private void removeHeader(){
            notifyItemRemoved(0);
            headerView = null;
        }

        private boolean haveHeader(){
            return headerView != null;
        }

        public RecyclerView.Adapter getWrappedAdapter() {
            return adapter;
        }

        @Override
        public int getItemCount() {
            return headerView == null? adapter.getItemCount() + 1 : adapter.getItemCount() + 2;
        }

        @Override
        public int getItemViewType(int position) {
            if (position == getItemCount() - 1) {
                return ITEM_VIEW_TYPE_FOOTER;
            } else if (position == 0 && headerView != null){
                return ITEM_VIEW_TYPE_HEADER;
            }else {
                return adapter.getItemViewType(headerView == null? position : position -1);
            }
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == ITEM_VIEW_TYPE_FOOTER) {
                return new HeaderViewHolder(loadMoreView);
            } else if (viewType == ITEM_VIEW_TYPE_HEADER){
                return new HeaderViewHolder(headerView);
            }else{
                return adapter.onCreateViewHolder(parent, viewType);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            if (holder.getItemViewType() != ITEM_VIEW_TYPE_FOOTER && holder.getItemViewType() != ITEM_VIEW_TYPE_HEADER) {
                adapter.onBindViewHolder(holder, headerView == null? position : position -1);
            }
        }

        @Override
        public long getItemId(int position) {
            if (getItemViewType(position) != ITEM_VIEW_TYPE_FOOTER && getItemViewType(position) != ITEM_VIEW_TYPE_HEADER) {
                return adapter.getItemId(headerView == null? position : position -1);
            } else {
                return super.getItemId(position);
            }
        }

        @Override
        public void onViewRecycled(RecyclerView.ViewHolder holder) {
            if (holder.getItemViewType() != ITEM_VIEW_TYPE_FOOTER && holder.getItemViewType() != ITEM_VIEW_TYPE_FOOTER) {
                adapter.onViewRecycled(holder);
            }
        }

        @Override
        public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
            if (holder.getItemViewType() != ITEM_VIEW_TYPE_FOOTER && holder.getItemViewType() != ITEM_VIEW_TYPE_FOOTER) {
                adapter.onViewAttachedToWindow(holder);
            }
        }

        @Override
        public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
            if (holder.getItemViewType() != ITEM_VIEW_TYPE_FOOTER && holder.getItemViewType() != ITEM_VIEW_TYPE_FOOTER) {
                adapter.onViewDetachedFromWindow(holder);
            }
        }

        @Override
        public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.registerAdapterDataObserver(observer);
            }
        }

        @Override
        public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
            if (adapter != null) {
                adapter.unregisterAdapterDataObserver(observer);
            }
        }

        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public HeaderViewHolder(View itemView) {
            super(itemView);
        }
    }


    public void setFooterLoadMoreOverText(String loadMoreOverText) {
        mPromptView.setLoadingMoreText(loadMoreOverText);
    }

}
