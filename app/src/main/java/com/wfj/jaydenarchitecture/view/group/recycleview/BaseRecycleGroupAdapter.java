package com.wfj.jaydenarchitecture.view.group.recycleview;

import android.support.v7.widget.RecyclerView.ViewHolder;

import com.wfj.jaydenarchitecture.view.group.IListAdapter;
import com.wfj.jaydenarchitecture.view.widget.recyclelistview.RecyclerAdapter;


public abstract class BaseRecycleGroupAdapter<T, VH extends ViewHolder> extends RecyclerAdapter<T, VH> implements IListAdapter<T> {

}
