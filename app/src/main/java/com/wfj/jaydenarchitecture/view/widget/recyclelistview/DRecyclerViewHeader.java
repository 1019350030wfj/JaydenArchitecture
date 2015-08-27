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
import android.widget.ScrollView;

import com.nineoldandroids.view.ViewHelper;

/**
 * -----------------------------------------------------------
 * 版 权 ： BigTiger 版权所有 (c) 2015
 * 作 者 : BigTiger
 * 版 本 ： 1.0
 * 创建日期 ：2015/7/7 18:38
 * 描 述 ：
 * <p>
 * -------------------------------------------------------------
 */
public class DRecyclerViewHeader extends ScrollView{
    private DRecyclerView mRecycler;

    private int mDownScroll;
    private int mCurrentScroll;
    private boolean mReversed;
    private boolean mAlreadyAligned;
    private boolean mRecyclerWantsTouchEvent = true;

    /**
     * Inflates layout from xml
     *
     * @param context
     * @param layoutRes layout resource to be inflated.
     * @return  DRecyclerViewHeader view object.
     */
    public static DRecyclerViewHeader fromXml(Context context, @LayoutRes int layoutRes) {
        DRecyclerViewHeader header = new DRecyclerViewHeader(context);
        View.inflate(context, layoutRes, header);
        return header;
    }

    public DRecyclerViewHeader(Context context) {
        super(context);
    }

    public DRecyclerViewHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DRecyclerViewHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 将DRecyclerViewHeader 添加到DRecyclerView的头部
     * 但是该方法必须要在DRecyclerView 设置LayoutManager之后调用 (如果要设置OnScrollerListener 也是一样)
     * @param recycler
     */
    public void attachTo(DRecyclerView recycler) {
        attachTo(recycler, false);
    }

    /**
     * @param recycler
     * @param headerAlreadyAligned false:  就会正常的把RecyclerViewHeader设置在RecyclerView的头部.
     *                             true:  该方法就会认为用户已经正确的将RecyclerViewHeader添加到RecyclerView的头部了
     */
    public void attachTo(DRecyclerView recycler, boolean headerAlreadyAligned) {
        validateRecycler(recycler, headerAlreadyAligned);

        mRecycler = recycler;
        mAlreadyAligned = headerAlreadyAligned;
        mReversed = isLayoutManagerReversed(recycler);

        setupAlignment(recycler);
        setupHeader(recycler);
    }

    private boolean isLayoutManagerReversed(DRecyclerView recycler) {
        boolean reversed = false;
        RecyclerView.LayoutManager manager = recycler.getRecyclerView().getLayoutManager();
        if (manager instanceof LinearLayoutManager) {
            reversed = ((LinearLayoutManager) manager).getReverseLayout();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            reversed = ((StaggeredGridLayoutManager) manager).getReverseLayout();
        }
        return reversed;
    }

    private void setupAlignment(DRecyclerView recycler) {
        if (!mAlreadyAligned) {
            //设置 header的 alignment
            ViewGroup.LayoutParams currentParams = getLayoutParams();
            LayoutParams newHeaderParams;
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            int gravity = (mReversed ? Gravity.BOTTOM : Gravity.TOP) | Gravity.CENTER_HORIZONTAL;
            if (currentParams != null) {
                newHeaderParams = new LayoutParams(getLayoutParams()); //to copy all the margins
                newHeaderParams.width = width;
                newHeaderParams.height = height;
                newHeaderParams.gravity = gravity;
            } else {
                newHeaderParams = new LayoutParams(width, height, gravity);
            }
            DRecyclerViewHeader.this.setLayoutParams(newHeaderParams);

            //设置 recycler alignment
            FrameLayout newRootParent = new FrameLayout(recycler.getContext());
            newRootParent.setLayoutParams(recycler.getLayoutParams());
            ViewParent currentParent = recycler.getParent();
            if (currentParent instanceof ViewGroup) {
                int indexWithinParent = ((ViewGroup) currentParent).indexOfChild(recycler);
                ((ViewGroup) currentParent).removeViewAt(indexWithinParent);
                recycler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                newRootParent.addView(DRecyclerViewHeader.this);
                newRootParent.addView(recycler);
                ((ViewGroup) currentParent).addView(newRootParent, indexWithinParent);
            }
        }
    }

    @SuppressLint("NewApi")
    private void setupHeader(final DRecyclerView recycler) {
        recycler.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mCurrentScroll += dy;
                Log.i("SCROLL","dy:"+dy+"  header:"+getHeight()+"   mCurrentScroll:"+mCurrentScroll);
                //RecyclerViewHeader.this.setTranslationY(-mCurrentScroll);
                ViewHelper.setTranslationY(DRecyclerViewHeader.this, -mCurrentScroll);
            }
        });

        DRecyclerViewHeader.this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int height = DRecyclerViewHeader.this.getHeight();
                if (height > 0) {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        DRecyclerViewHeader.this.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    } else {
                        DRecyclerViewHeader.this.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    }

                    if (mAlreadyAligned) {
                        MarginLayoutParams params = (MarginLayoutParams) getLayoutParams();
                        height += params.topMargin;
                        height += params.bottomMargin;
                    }
                    recycler.getRecyclerView().addItemDecoration(new HeaderItemDecoration(recycler.getRecyclerView().getLayoutManager(), height), 0);
                }
            }
        });
    }

    public void detachHeader(){
        if (mRecycler == null)
            return;

        ViewParent parent = getParent();
        ViewParent root = parent.getParent();
        if (root != null && root instanceof ViewGroup){
            ViewGroup.LayoutParams params = getLayoutParams();
            params.height = 1;
            setMinimumHeight(1);
            setLayoutParams(params);
            invalidate();
            ((ViewGroup) root).invalidate();
        }

    }

    private void validateRecycler(DRecyclerView recycler, boolean headerAlreadyAligned) {
        RecyclerView.LayoutManager layoutManager = recycler.getRecyclerView().getLayoutManager();
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
    public boolean onTouchEvent(MotionEvent event) {
        if (mRecyclerWantsTouchEvent) {
            int scrollDiff = mCurrentScroll - mDownScroll;
            MotionEvent recyclerEvent = MotionEvent.obtain(event.getDownTime(),
                                                        event.getEventTime(),
                                                        event.getAction(),
                                                        event.getX(),
                                                        event.getY() - scrollDiff,
                                                        event.getMetaState());
            mRecycler.onTouchEvent(recyclerEvent);
            return false;
        }
        return super.onTouchEvent(event);
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

}
