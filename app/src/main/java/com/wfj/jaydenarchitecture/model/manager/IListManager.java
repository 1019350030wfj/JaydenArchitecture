package com.wfj.jaydenarchitecture.model.manager;

import java.util.List;

public interface IListManager<E>{
    void refresh(LoadListener listener);
    void loadMore(LoadListener listener);
    void refreshItemLocal(int position, E e);
    void addItemLocal(int position, E e);
    boolean hasData();
    List<E> getDatas();
}
