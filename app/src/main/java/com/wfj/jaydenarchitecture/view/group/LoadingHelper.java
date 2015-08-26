package com.wfj.jaydenarchitecture.view.group;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.app.Constant;
import com.wfj.jaydenarchitecture.view.widget.CircleRipplesDrawable;

/**
 * Created by Jayden on 2015/8/26.
 */
public class LoadingHelper {

    private View group;

    private ImageView loadingImageView;
    private TextView failTextView;

    private CircleRipplesDrawable loadingDrawable;

    private LoadingHelper(View loadingGroup) {
        this.group = loadingGroup;
        loadingImageView = (ImageView) loadingGroup.findViewById(R.id.loading_img);
        failTextView = (TextView) loadingGroup.findViewById(R.id.fail_text_img);

        loadingDrawable = new CircleRipplesDrawable();
        loadingDrawable.setMaxWidth(Constant.LOADING_CIRCLE_VIEW_MAX_SIZE);
        loadingImageView.setImageDrawable(loadingDrawable);
    }

    public static LoadingHelper geenrateOnParentAtPosition(ViewGroup parent, int position) {
        if (parent == null) {
            return null;
        }
        if (position < 0) {
            position = 0;
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loading_help, null);
        parent.addView(view, position, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        return new LoadingHelper(view);
    }

    public ViewGroup getLoadingRootView() {
        return (ViewGroup) group;
    }

    public void setBackgroundColor(int color) {
        this.group.setBackgroundColor(color);
    }

    public void start() {
        group.setVisibility(View.VISIBLE);
        loadingImageView.setVisibility(View.VISIBLE);
        failTextView.setVisibility(View.GONE);
        loadingDrawable.startBreath();

        group.setOnClickListener(null);
    }

    public void end() {
        group.setVisibility(View.GONE);
        loadingDrawable.stopBreath();
    }

    /**
     * 当前是否处于正在加载中
     *
     * @return
     */
    public boolean isShowLoading() {
        return group.getVisibility() == View.VISIBLE;
    }

    public void fail(String failMsg, View.OnClickListener clickListener) {
        group.setVisibility(View.VISIBLE);
        failTextView.setVisibility(View.VISIBLE);
        loadingDrawable.stopBreath();
        loadingImageView.setVisibility(View.GONE);

        failTextView.setText(failMsg);

        if (clickListener != null) {
            group.setOnClickListener(clickListener);
        }
    }
}
