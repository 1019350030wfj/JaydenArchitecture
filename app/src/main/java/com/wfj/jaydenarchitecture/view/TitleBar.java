package com.wfj.jaydenarchitecture.view;

import android.content.Context;
import android.content.res.Resources;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.utils.UIUtil;


/**
 * -----------------------------------------------------------
 * 版 权 ： BigTiger 版权所有 (c) 2015
 * 作 者 : BigTiger
 * 版 本 ： 1.0
 * 创建日期 ：2015/6/26 17:42
 * <p>
 * 描 述 ：
 * <p>
 * -------------------------------------------------------------
 */
public class TitleBar {
    private RelativeLayout titleView;
    private FrameLayout titleLeft;
    private FrameLayout titleCenter;
    private LinearLayout titleRight;
    private ImageView ivMask;

    private OnCloseListener onCloseListener;

    public enum Position {
        POSITION_LEFT, POSITION_CENTER, POSITION_RIGHT;
    }

    public TitleBar(RelativeLayout title) {
        titleView = title;
        titleLeft = (FrameLayout) titleView.findViewById(R.id.title_left);
        titleCenter = (FrameLayout) titleView.findViewById(R.id.title_center);
        titleRight = (LinearLayout) titleView.findViewById(R.id.title_right);
        ivMask = (ImageView) titleView.findViewById(R.id.iv_title_mask);
        setTitleLeftClose(title.getContext());
    }

    public FrameLayout getTitleLeft() {
        return titleLeft;
    }

    public FrameLayout getTitleCenter() {
        return titleCenter;
    }

    public LinearLayout getTitleRight() {
        return titleRight;
    }

    public RelativeLayout getTitleView() {
        return titleView;
    }

    public void setTitleLeft(View v) {
        setTitleLeft(v, null);
    }

    public void setTitleLeft(View v , ViewGroup.LayoutParams layoutParams) {
        ViewParent parent = v.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(v);
        }
        titleLeft.removeAllViews();

        if(layoutParams != null) {
            titleLeft.addView(v, layoutParams);
        } else {
            titleLeft.addView(v);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) v.getLayoutParams();
            params.height = FrameLayout.LayoutParams.WRAP_CONTENT;
            params.width = FrameLayout.LayoutParams.WRAP_CONTENT;
            params.gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
            v.setLayoutParams(params);
        }
    }

    public void setTitleCenter(View view) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(view);
        }
        titleCenter.removeAllViews();
        titleCenter.addView(view);
    }

    public void setTitleRight(View view) {
        setTitleRight(view, null);
    }

    public void setTitleRight(View view, ViewGroup.LayoutParams layoutParams) {
        ViewParent parent = view.getParent();
        if (parent != null) {
            ((ViewGroup) parent).removeView(view);
        }

        titleRight.removeAllViews();

        if(layoutParams != null) {
            titleRight.addView(view, layoutParams);
        } else {
            titleRight.addView(view);
        }
    }

    public void setTitleLeftClose(Context context) {
        setTitleLeftClose(context, R.mipmap.btn_back);
    }

    /**
     * 当leftResource为0时不显示退出按钮
     * @param context
     * @param leftResource
     */
    public void setTitleLeftClose(Context context, int leftResource) {
        if(leftResource == 0) {
            titleLeft.removeAllViews();
            titleLeft.setOnClickListener(null);
            return;
        }
        ImageView imageView = new ImageView(context);
        imageView.setImageResource(leftResource);
        setTitleLeft(imageView);
        titleLeft.setOnClickListener((view) -> {
            if (onCloseListener != null) {
                onCloseListener.finish();
            }
        });
    }

    public void setTitleText(Context context, String title) {
        setTitleText(Position.POSITION_CENTER, R.color.c1_dark_black, R.dimen.s34, title, context);
    }

    public void setTitleText(Position position, int colorId, int sizeId, String title, Context context) {
        if (context == null) {
            return;
        }
        removeChild(position);

        Resources resources = context.getResources();
        TextView tvTitle = new TextView(context);
        tvTitle.setText(title);
        tvTitle.setTextSize(UIUtil.px2Dp(context, resources.getDimension(sizeId)));
        tvTitle.setTextColor(resources.getColor(colorId));
        tvTitle.setGravity(Gravity.CENTER);
        setTitleViewWithPosition(position, tvTitle);
    }

    public void setLogo(Position position, int resId, Context context) {
        if (context == null) {
            return;
        }
        if (resId != 0){
            ImageView logo = new ImageView(context);
            logo.setImageResource(resId);
            setTitleViewWithPosition(position, logo);
        }else {
            removeChild(position);
        }

    }

    private void removeChild(Position position){
        switch (position){
            case POSITION_LEFT: {
                titleLeft.removeAllViews();
                break;
            }
            case POSITION_CENTER: {
                titleCenter.removeAllViews();
                break;
            }
            case POSITION_RIGHT: {
                titleRight.removeAllViews();
                break;
            }
        }
    }

    public void setTitleViewWithPosition(Position position, View view) {
        switch (position) {
        case POSITION_LEFT: {
            setTitleLeft(view);
            break;
        }
        case POSITION_CENTER: {
            setTitleCenter(view);
            break;
        }
        case POSITION_RIGHT: {
            setTitleRight(view);
            break;
        }
        }
    }

    public ImageView getTitleMask(){
        return ivMask;
    }

    public void setBackgroundColor(int color){
        titleView.setBackgroundColor(color);
    }

    public void setOnCloseListener(OnCloseListener onCloseListener) {
        this.onCloseListener = onCloseListener;
    }

    public interface OnCloseListener{
        public void finish();
    }

}
