package com.wfj.jaydenarchitecture.view.group;

import java.util.List;

/**
 * 列表数据适配器
 * Created by Jayden on 2015/8/26.
 */
public interface IListAdapter<T> {

    void setItems(List<T> items);

    void addItems(List<T> items);

    T getItem(int position);

    void notifyDataSetChanged();

    int getCount();
}
