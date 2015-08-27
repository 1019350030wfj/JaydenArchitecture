package com.wfj.jaydenarchitecture.view.group.refreshLoadmore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wfj.jaydenarchitecture.view.group.IListAdapter;

import fbcore.widget.BaseSingleTypeAdapter;

public abstract class BaseGroupAdapter<T> extends BaseSingleTypeAdapter<T> implements IListAdapter<T> {

    public LayoutInflater mInflate;

    public BaseGroupAdapter(Context context) {
        super(context);
        mInflate = LayoutInflater.from(mContext);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        return getItemView(position, convertView, parent);
    }

    public abstract View getItemView(int position, View contentView, ViewGroup parent);

    public <T extends View> T findView(View parent, int id) {
        return (T) parent.findViewById(id);
    }

}
