package com.wfj.jaydenarchitecture.view.widget.recyclelistview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.LayoutRes;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nineoldandroids.view.ViewHelper;

/**
 * -----------------------------------------------------------
 * 版 权 ： BigTiger 版权所有 (c) 2015
 * 作 者 : BigTiger
 * 版 本 ： 1.0
 * 创建日期 ：2015/7/07 9:59
 * 描 述 ：
 * <p>
 * -------------------------------------------------------------
 */
public class RecyclerViewHeader extends RelativeLayout {

    private RecyclerView mRecycler;

    private int mDownScroll;
    private int mCurrentScroll;
    private boolean mReversed;
    private boolean mAlreadyAligned;
    private boolean mRecyclerWantsTouchEvent;

    /**
     * Inflates layout from xml
     *
     * @param context
     * @param layoutRes layout resource to be inflated.
     * @return  RecyclerViewHeader view object.
     */
    public static RecyclerViewHeader fromXml(Context context, @LayoutRes int layoutRes) {
        RecyclerViewHeader header = new RecyclerViewHeader(context);
        View.inflate(context, layoutRes, header);
        return header;
    }

    public RecyclerViewHeader(Context context) {
        super(context);
    }

    public RecyclerViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerViewHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 将RecyclerViewHeader 添加到RecyclerView的头部
     * 但是该方法必须要在RecyclerView 设置LayoutManager之后调用 (如果要设置OnScrollerListener 也是一样)
     * @param recycler
     */
    public void attachTo(RecyclerView recycler) {
        attachTo(recycler, false);
    }

    /**
     * @param recycler
     * @param headerAlreadyAligned false:  就会正常的把RecyclerViewHeader设置在RecyclerView的头部.
     *                             true:  该方法就会认为用户已经正确的将RecyclerViewHeader添加到RecyclerView的头部了
     */
    public void attachTo(RecyclerView recycler, boolean headerAlreadyAligned) {
        validateRecycler(recycler, headerAlreadyAligned);

        mRecycler = recycler;
        mAlreadyAligned = headerAlreadyAligned;
        mReversed = isLayoutManagerReversed(recycler);

        setupAlignment(recycler);
        setupHeader(recycler);
    }

    private boolean isLayoutManagerReversed(RecyclerView recycler) {
        boolean reversed = false;
        RecyclerView.LayoutManager manager = recycler.getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            reversed = ((LinearLayoutManager) manager).getReverseLayout();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            reversed = ((StaggeredGridLayoutManager) manager).getReverseLayout();
        }
        return reversed;
    }

    private void setupAlignment(RecyclerView recycler) {
        if (!mAlreadyAligned) {
            //设置 header的 alignment
            ViewGroup.LayoutParams currentParams = getLayoutParams();
            FrameLayout.LayoutParams newHeaderParams;
            int width = ViewGroup.LayoutParams.WRAP_CONTENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int gravity = (mReversed ? Gravity.BOTTOM : Gravity.TOP) | Gravity.CENTER_HORIZONTAL;
            if (currentParams != null) {
                newHeaderParams = new FrameLayout.LayoutParams(getLayoutParams()); //to copy all the margins
                newHeaderParams.width = width;
                newHeaderParams.height = height;
                newHeaderParams.gravity = gravity;
            } else {
                newHeaderParams = new FrameLayout.LayoutParams(width, height, gravity);
            }
            RecyclerViewHeader.this.setLayoutParams(newHeaderParams);

            //设置 recycler alignment
            FrameLayout newRootParent = new FrameLayout(recycler.getContext());
            newRootParent.setLayoutParams(recycler.getLayoutParams());
            ViewParent currentParent = recycler.getParent();
            if (currentParent instanceof ViewGroup) {
                int indexWithinParent = ((ViewGroup) currentParent).indexOfChild(recycler);

                ((ViewGroup) currentParent).removeViewAt(indexWithinParent);
                recycler.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                newRootParent.addView(recycler);
                newRootParent.addView(RecyclerViewHeader.this);
                ((ViewGroup) currentParent).addView(newRootParent, indexWithinParent);
            }
        }
    }

    @SuppressLint("NewApi")
    private void  setupHeader(final RecyclerView recycler) {
        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCurrentScroll += dy;
                //RecyclerViewHeader.this.setTranslationY(-mCurrentScroll);
                ViewHelper.setTranslationY(RecyclerViewHeader.this, -mCurrentScroll);
            }
        });

        RecyclerViewHeader.this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = RecyclerViewHeader.this.getHeight();
                if (height > 0) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        RecyclerViewHeader.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        RecyclerViewHeader.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    if (mAlreadyAligned) {
                        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
                        height += params.topMargin;
                        height += params.bottomMargin;
                    }

                    recycler.addItemDecoration(new HeaderItemDecoration(recycler.getLayoutManager(), height), 0);
                }
            }
        });
    }

    private void validateRecycler(RecyclerView recycler, boolean headerAlreadyAligned) {
        RecyclerView.LayoutManager layoutManager = recycler.getLayoutManager();
        if (layoutManager == null) {
            throw new IllegalStateException("Be sure to call RecyclerViewHeader constructor after setting your RecyclerView's LayoutManager.");
        } else if (layoutManager.getClass() != LinearLayoutManager.class    //不要使用 instanceof
                && layoutManager.getClass() != GridLayoutManager.class
                && !(layoutManager instanceof StaggeredGridLayoutManager)) {
            throw new IllegalArgumentException("Currently RecyclerViewHeader supports only LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager.");
        }

        if (layoutManager instanceof LinearLayoutManager) {
            if (((LinearLayoutManager) layoutManager).getOrientation() != LinearLayoutManager.VERTICAL) {
                throw new IllegalArgumentException("Currently RecyclerViewHeader supports only VERTICAL orientation LayoutManagers.");
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            if (((StaggeredGridLayoutManager) layoutManager).getOrientation() != StaggeredGridLayoutManager.VERTICAL) {
                throw new IllegalArgumentException("Currently RecyclerViewHeader supports only VERTICAL orientation StaggeredGridLayoutManagers.");
            }
        }

        if (!headerAlreadyAligned) {
            ViewParent parent = recycler.getParent();
            if (parent != null &&
                    !(parent instanceof LinearLayout) &&
                    !(parent instanceof FrameLayout) &&
                    !(parent instanceof RelativeLayout) &&
                    !(parent instanceof SwipeRefreshLayout)) {
                throw new IllegalStateException("Currently, NOT already aligned RecyclerViewHeader " +
                        "can only be used for RecyclerView with a parent of one of types: LinearLayout, FrameLayout, RelativeLayout.");
            }
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        mRecyclerWantsTouchEvent = mRecycler.onInterceptTouchEvent(ev);
                //mSwipeRefreshLayout == null? mRecycler.onInterceptTouchEvent(ev) : mSwipeRefreshLayout.onInterceptTouchEvent(ev);

        if (mRecyclerWantsTouchEvent && ev.getAction() == MotionEvent.ACTION_DOWN) {
            mDownScroll = mCurrentScroll;
        }
        return mRecyclerWantsTouchEvent || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.i("touchEvent","begin");
        /*if (mRecyclerWantsTouchEvent) {
            int scrollDiff = mCurrentScroll - mDownScroll;
            MotionEvent recyclerEvent = MotionEvent.obtain(event.getDownTime(),
                                                        event.getEventTime(),
                                                        event.getAction(),
                                                        event.getX(),
                                                        event.getY() - scrollDiff,
                                                        event.getMetaState());
            if (mSwipeRefreshLayout != null){
                mSwipeRefreshLayout.onTouchEvent(recyclerEvent);
                Log.i("touchEvent","mSwipeRefreshLayout");
            }else {
                Log.i("touchEvent","mRecycler");
                mRecycler.onTouchEvent(recyclerEvent);
            }
            return false;
        }*/
        return mRecycler.onTouchEvent(event);//super.onTouchEvent(event);
    }

    private class HeaderItemDecoration extends RecyclerView.ItemDecoration {
        private int mHeaderHeight;
        private int mNumberOfChildren;

        public HeaderItemDecoration(RecyclerView.LayoutManager layoutManager, int height) {
            if (layoutManager.getClass() == LinearLayoutManager.class) {
                mNumberOfChildren = 1;
            } else if (layoutManager.getClass() == GridLayoutManager.class) {
                mNumberOfChildren = ((GridLayoutManager) layoutManager).getSpanCount();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                mNumberOfChildren = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            }
            mHeaderHeight = height;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            int value = (parent.getChildLayoutPosition(view) < mNumberOfChildren) ? mHeaderHeight : 0;
            if (mReversed) {
                outRect.bottom = value;
            } else {
                outRect.top = value;
            }
        }
    }

    private SwipeRefreshLayout mSwipeRefreshLayout;
    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout){
        mSwipeRefreshLayout = swipeRefreshLayout;
    }
}