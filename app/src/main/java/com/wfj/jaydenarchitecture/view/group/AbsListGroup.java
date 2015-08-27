package com.wfj.jaydenarchitecture.view.group;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.wfj.jaydenarchitecture.model.dao.ReturnCode;
import com.wfj.jaydenarchitecture.model.manager.IListManager;
import com.wfj.jaydenarchitecture.model.manager.LoadListener;

/**
 * 基于ListView或者GridView组件基础模块
 *
 * Created by Jayden on 2015/8/26.
 */
public abstract class AbsListGroup<E> extends AbsLoadingGroup{

    private static final String DEFAULT_LOAD_MORE_OVER_TEXT = "没有更多了";

    private IListManager<E> mManager;
    private IListAdapter<E> mAdapter;

    private ViewGroup preparedView;
    private Context context;
    private View afterView;

    public boolean needRefresh = false;
    protected int divisionPadding = -1; //item之间的间距
    protected int backgroundColor = -1; //列表背景色

    private String loadMoreOverText;

    /**
     *判断是否已经获取完刷新的数据了，
     * 这个标记用来做有时候获取第一页的数据还没有拿到就触发了加载更多
     */
    private boolean canLoadMore;

    /**
     * 判断是否已经调用过 listView.setAdapter() 方法
     */
    private boolean hasSetListAdapter;

    /**
     * 显示错误Loading，停止加载数据
     */
    private boolean failFlag;

    public abstract ViewGroup containChildView();

    public abstract void initWidget();

    public abstract void initListener();

    public abstract void hideRefreshLoading();

    public abstract void hideLoadMoreLoading(boolean loadMoreSuccess);

    protected Context getContext() {
        return context;
    }

    public IListAdapter getListAdapter() {
        return mAdapter;
    }

    public String getFooterLoadMoreOverText() {
        return this.loadMoreOverText;
    }

    public boolean isShouldLoadMore() {
        return canLoadMore;
    }

    /**
     * 设置List列表的背景颜色
     */
    public abstract void setBackgroundColor(int backgroundColor);

    /**
     * 设置List列表item之间的间距
     */
    public abstract void setDivisionPadding(int padding);

    public AbsListGroup(Context context) {
        this.context = context;
        this.loadMoreOverText = DEFAULT_LOAD_MORE_OVER_TEXT;
        this.canLoadMore = false;
        this.hasSetListAdapter = false;
    }

    @Override
    public void onLoadingHelperFailClick() {
        needRefresh = true;
        initData();
    }

    public void initData() {
        if (!needRefresh) {
            return;
        }
        if (needShowLoadingViewIfFreshData()) {
            launchLoadHelper(backgroundColor == -1 ? 0 : backgroundColor);
        }
        if(!getPreparedDataIfRefreshData(() -> {
            refreshData();
        })) {
            refreshData();
        }
    }

    public boolean needShowLoadingViewIfFreshData() {
        return true;
    }

    public interface OnGetTopData {
        void onGetTopDataSuccess();
    }

    /**
     * 获取列表数据前的准备数据,比如个人主页中。要先获取完头部数据才获取下面的列表数据
     */
    protected boolean getPreparedDataIfRefreshData(OnGetTopData onGetTopData) {
        return false;
    }

    public void refreshData() {
        canLoadMore = false;
        mManager.refresh(new LoadListener() {
            @Override
            public void onSuccess() {
                canLoadMore = true;
                hideRefreshLoading();
                showView();
            }

            @Override
            public void onFail(int code) {
                canLoadMore = false;
                hideRefreshLoading();

                if (onErrorConsumption(code)) {
                    return;
                }

                //判断该错误码是否需要显示出错误的Loading页
                if (GroupConfig.shouldShowFailLoadingPage(code)) {
                    refreshDataFail(code);
                }
            }
        });
    }

    public void loadMoreData() {
        if (!canLoadMore) {
            return;
        }
        mManager.loadMore(new LoadListener() {
            @Override
            public void onSuccess() {
                hideLoadMoreLoading(false);
                showView();
            }

            @Override
            public void onFail(int code) {
                if (code == ReturnCode.RS_LOCAL_NO_MORE_DATA || code == ReturnCode.RS_EMPTY_ERROR) {
                    // 加载成功，没有更多数据
                    hideLoadMoreLoading(true);
                    return;
                }
                // 其他错误
                hideLoadMoreLoading(false);
            }
        });
    }

    /**
     * 获取到数据
     */
    public void showView() {
        postOnUiThread(() -> {
            if (failFlag) {
                return;
            }

            setListAdapter();

            if (mAdapter != null && mAdapter.getCount() > 0) {
                hideLoadHelper();
            }
        });
    }

    private void setListAdapter() {
        hasSetListAdapter = true;
        mAdapter.setItems(mManager.getDatas());
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 子类复写消费掉onFail()
     */
    public boolean onErrorConsumption(int code) {
        return false;
    }

    private void refreshDataFail(int code) {
        if (!isShowLoading()) {
            launchLoadHelper();
        }
        if (code == ReturnCode.RS_EMPTY_ERROR) {
            afterView = onAfterView();
            if (afterView != null) {
                showEmptyView();
            }
        }
        showFailMessage(code);
    }

    //加载失败后不想直接显示Loading页，想显示部分信息
    public ViewGroup onAfterView() {
        return null;
    }

    private void showEmptyView() {
        if (afterView == null) {
            afterView = onAfterView();
        }
        getLoadingRootView().removeAllViews();
        getLoadingRootView().addView(afterView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    @Override
    public void onCreateView() {
        onCreateView(false);
    }

    private void onCreateView(boolean fresh) {
        if (fresh) {
            setRoot(null);
        }
        if (getRoot() == null) {
            preparedView = onPrepareView();
            if (preparedView == null) {
                setRoot(containChildView());
                initWidget();
                initListener();
                initView();
                initData();
            } else {
                setRoot(preparedView);
            }
        }
    }

    @Override
    public void onResetView() {
        onCreateView(true);
    }

    // 准备View。比如在动态页当中要先判断是否登陆
    public ViewGroup onPrepareView() {
        return null;
    }

    private void initView() {
        if (mManager == null || !mManager.hasData()) {
            needRefresh = true;
        }
    }

    /**
     * 删除或者替换Item (当 t 为Null 时代表删除)
     *
     * @param positionId
     * @param t
     */
    public void refreshItemLocal(int positionId, E t) {
//        if (mAdapter instanceof BaseRecycleGroupAdapter) {
            if(t == null) {
//                ((BaseRecycleGroupAdapter) mAdapter).removeItem(positionId);
//                mManager.refreshItemLocal(positionId, t);
//                if(!mManager.hasData()) {
//                    refreshDataFail(ReturnCode.RS_EMPTY_ERROR);
//                }
            } else {
//                mManager.refreshItemLocal(positionId, t);
//                ((BaseRecycleGroupAdapter) mAdapter).replaceItem(positionId, t);
            }
//        } else {
            mManager.refreshItemLocal(positionId, t);
            if (mManager.hasData()) {
                mAdapter.notifyDataSetChanged();
            } else {
                refreshDataFail(ReturnCode.RS_EMPTY_ERROR);
            }
//        }
    }

    public void addItemLocal(int positionId, E t) {
        mManager.addItemLocal(positionId, t);
        if (mManager.hasData()) {
            if(hasSetListAdapter) {
                mAdapter.notifyDataSetChanged();
            } else {
                setListAdapter();
            }
        } else {
            refreshDataFail(ReturnCode.RS_EMPTY_ERROR);
        }
    }
}
