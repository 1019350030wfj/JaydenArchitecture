package com.wfj.jaydenarchitecture.model.manager;

/**
 * Created by Jayden on 2015/8/26.
 */
public abstract class BaseListHeaderManager<E> extends BaseManager {

    private E headerData;

    public void setHeaderData(E headerData) {
        this.headerData = headerData;
    }

    public E getHeaderData() {
        return headerData;
    }

    public abstract void getHeaderData(LoadListener listener);
}
