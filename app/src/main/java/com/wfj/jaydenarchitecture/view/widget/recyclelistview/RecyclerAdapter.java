package com.wfj.jaydenarchitecture.view.widget.recyclelistview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * -----------------------------------------------------------
 * 版 权 ： BigTiger 版权所有 (c) 2015
 * 作 者 : BigTiger
 * 版 本 ： 1.0
 * 创建日期 ：2015/7/3 17:14
 * 描 述 ：RecyclerView.Adapter 的基类
 * <p>
 * -------------------------------------------------------------
 */
public abstract class RecyclerAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    protected List<T> mData;

    public RecyclerAdapter(){
        mData = new ArrayList<T>();
    }

    @Override
    public abstract VH onCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        setBindViewHolder((VH) holder, position);
    };

    public abstract void setBindViewHolder(VH holder, int position);

    public int getCount(){
        return getItemCount();
    }

    /**
     * 设置数据
     * @param data
     */
    public void setItems(List<T> data) {
        if (mData == null) {
            mData = new ArrayList<T>();
        }
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    /**
     * 添加Item集合
     * @param data
     */
    public void addItems(List<T> data){
        if (mData == null){
            setItems(data);
        }else {
            mData.addAll(mData.size(),data);
        }
        notifyItemRangeInserted(mData.size() - data.size(), data.size());
    }

    /**
     * 指定位置添加Item
     * @param position
     * @param data
     */
    public void addItem(int position, T data) {
        if (position > mData.size()) {
            return;
        }
        mData.add(position, data);
        notifyItemInserted(position);
    }

    public T getItem(int position) {
        return mData.get(position);
    }

    /**
     * 删除指定位置的数据
     * @param position
     */
    public void removeItem(int position) {
        if (position >= mData.size()) {
            return;
        }
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void replaceItem(int position, T t) {
        if(mData.size() - 1 < position) {
            return;
        }
        mData.remove(position);
        mData.add(position, t);
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return mData == null? 0:mData.size();
    }

    public interface RecyclerItemClickListener{
        void onItemClicked(View view, int position);
    }

    protected static abstract class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        protected RecyclerAdapter mAdapter;
        public RecyclerViewHolder(View itemView, RecyclerAdapter adapter) {
            super(itemView);
            itemView.setTag(this);
            mAdapter = adapter;
            findItemView(itemView);
        }

        /**
         * 找到子View, 设置点击监听
         * @param itemView
         */
        public abstract void findItemView(View itemView);

        @Override
        public void onClick(View view) {

        }
    }

    public static <T extends View> T findView(View parent, int id) {
        return (T) parent.findViewById(id);
    }
}
