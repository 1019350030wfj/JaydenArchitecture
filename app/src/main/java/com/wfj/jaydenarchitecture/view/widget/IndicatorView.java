package com.wfj.jaydenarchitecture.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.wfj.jaydenarchitecture.R;


public class IndicatorView extends View{

    private int count;
    private int currentPos = 0;
    private Drawable drawable;
    private int space;
    private Rect bounds;

    private static final int[] FOCUSED_STATE = new int[]{android.R.attr.state_focused};
    private static final int[] NORMAL_STATE = new int[]{};

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        super(context,attrs);
        init(attrs, 0);
    }

    public IndicatorView(Context context) {
        super(context);
        init(null, 0);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IndicatorView, defStyleAttr, 0);
        drawable = a.getDrawable(R.styleable.IndicatorView_indicatorView_drawable);
        space = a.getDimensionPixelOffset(R.styleable.IndicatorView_indicatorView_space, 0);
        bounds = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int drawableWidth = drawable == null ? 0 : drawable.getIntrinsicWidth();
        int drawableHeight = drawable == null ? 0 : drawable.getIntrinsicHeight();
        int width = count == 0 ? 0 : drawableWidth * count + space * (count -1) + getPaddingLeft() + getPaddingRight();
        int height = count == 0 ? 0 : drawableHeight + getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (count <= 0) {
            return;
        }
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        bounds.set(0, 0, width, height);
        for (int i = 0; i < count; i++) {
            drawable.setState(NORMAL_STATE);
            if (i == currentPos) {
                drawable.setState(FOCUSED_STATE);
            }
            bounds.offsetTo((width + space)* i + getPaddingLeft(), getPaddingTop());
            drawable.setBounds(bounds);
            drawable.draw(canvas);
        }
    }

    public void setCurrentPosition(int position) {
        this.currentPos = position;
        invalidate();
    }

    public void setCount(int count) {
        this.count = count;
        requestLayout();
    }
}
