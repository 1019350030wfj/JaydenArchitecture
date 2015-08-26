package com.wfj.jaydenarchitecture.model.manager;

import com.wfj.jaydenarchitecture.model.bean.BaseListData;
import com.wfj.jaydenarchitecture.model.bean.Response;
import com.wfj.jaydenarchitecture.model.dao.IEntityListener;
import com.wfj.jaydenarchitecture.model.dao.ReturnCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 抽象列表管理类
 *
 * Created by Jayden on 2015/8/26.
 */
public abstract class AbsListManager<T> extends BaseManager implements IListManager<T> {

    private static final int STATE_RESET = 0;
    private static final int STATE_REFRESHING = 1;
    private static final int STATE_LOADING = 2;

    private List<T> items;
    private int currentState;

    private boolean hasInit;

    public AbsListManager() {
        items = new ArrayList<T>();
        switchState(STATE_RESET);
        hasInit = false;
    }

    private void switchState(int state) {
        currentState = state;
    }

    @Override
    public void refresh(LoadListener listener) {
        //刷新只能当状态为：STATE_RESET
        if (currentState != STATE_RESET) {
            handleFail(listener, ReturnCode.RS_GESTURE_CANCEL);
            return;
        }

        switchState(STATE_REFRESHING);
        refresh(new IEntityListener<BaseListData<T>>() {
            @Override
            public void result(Response<BaseListData<T>> response) {
                //返回的不是正确数据：返回码不是1000
                if (response.code != ReturnCode.RS_SUCCESS) {
                    //返回空数据 1004
                    if (response.code == ReturnCode.RS_EMPTY_ERROR) {
                        if (items != null) {
                            items.clear();
                        }
                    }
                    //返回的其他类型的错误
                    if (items != null && items.size() > 0) {
                        //本身有数据，返回成功，本身数据不变
                        handleSuccess(listener);
                    } else {
                        handleFail(listener,response.code);
                    }
                    switchState(STATE_RESET);
                    return;
                }

                //返回正确码，1000,获取最新的数据，更新items
                BaseListData<T> data = response.data;
                List<T> list = data.items;

                if (list.size() > 0 && items != null) {
                    items.clear();
                    items.addAll(list);
                    hasInit = true;
                    handleSuccess(listener);
                    switchState(STATE_RESET);
                    return;
                }

                if (items != null && items.size() == 0) {
                    // Dao层返回数据为空，本身也没有数据，返回本地数据空
                    handleFail(listener, ReturnCode.RS_EMPTY_ERROR);
                    switchState(STATE_RESET);
                }

            }
        });
    }

    @Override
    public void loadMore(LoadListener listener) {
        if (!hasInit || currentState != STATE_RESET) {
            handleFail(listener, ReturnCode.RS_GESTURE_CANCEL);
            return;
        }

        switchState(STATE_LOADING);
        loadMore(new IEntityListener<BaseListData<T>>() {
            @Override
            public void result(Response<BaseListData<T>> response) {
                if (response.code != ReturnCode.RS_SUCCESS) {
                    //直接把服务器的错误类型抛出
                    handleFail(listener, response.code);
                    switchState(STATE_RESET);
                    return;
                }
            }
        });
    }

    @Override
    public void refreshItemLocal(int position, Object o) {

    }

    @Override
    public void addItemLocal(int position, Object o) {

    }

    @Override
    public boolean hasData() {
        return items.size() > 0;
    }

    @Override
    public List getDatas() {
        return items;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        items.clear();
        items = null;
    }

    protected abstract void refresh(IEntityListener<BaseListData<T>> listener);

    protected abstract void loadMore(IEntityListener<BaseListData<T>> listener);
}
