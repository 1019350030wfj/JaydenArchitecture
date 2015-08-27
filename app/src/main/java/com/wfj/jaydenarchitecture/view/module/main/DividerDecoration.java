package com.wfj.jaydenarchitecture.view.module.main;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wfj.jaydenarchitecture.R;

/**
 * -----------------------------------------------------------
 * 版 权 ： BigTiger 版权所有 (c) 2015
 * 作 者 : BigTiger
 * 版 本 ： 1.0
 * 创建日期 ：2015/6/28 11:34
 * 描 述 ：
 * <p>
 * -------------------------------------------------------------
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {

    // private Drawable mDivider;
    private int mInsets;

    public DividerDecoration(Context context) {
        // mDivider = a.getDrawable(0);
        mInsets = context.getResources().getDimensionPixelSize(R.dimen.divider_main);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        // We can supply forced insets for each item view here in the Rect
        outRect.set(0, 0, 0, mInsets);
    }
}
