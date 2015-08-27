package com.wfj.jaydenarchitecture.view.widget.recyclelistview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.app.Constant;
import com.wfj.jaydenarchitecture.utils.UIUtil;
import com.wfj.jaydenarchitecture.view.widget.CircleRipplesDrawable;


/**
 * -----------------------------------------------------------
 * 版 权 ： BigTiger 版权所有 (c) 2015
 * 作 者 : BigTiger
 * 版 本 ： 1.0
 * 创建日期 ：2015/6/29 10:02
 * 描 述 ：加载更多的FootView 的控制器
 * <p>
 * -------------------------------------------------------------
 */
public class DRecyclerViewPrompt extends FrameLayout {

    /** footView 的根布局  **/
    View frameView;

    /** footView中的图片 (可以没有) **/
    ImageView mPromptImageView;

    /**
     * footView 中的进度条 (可以没有)
     */
    //ProgressBar mPromptProgressBar;

    ImageView ivProgress;

    /**
     * footView中的 文本提示(可以没有)
     */
    TextView mPromptTextView;

    private CircleRipplesDrawable mDrawable;

    private String loadingMoreText = "THE END";

    public DRecyclerViewPrompt(@NonNull Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.d_recycler_footer_layout, this);
        setMinimumWidth(UIUtil.getScreenWidth());
        frameView = this.findViewById(R.id.load_more_frame);
        mPromptImageView = (ImageView) findViewById(R.id.footer_image);
        ivProgress = (ImageView) findViewById(R.id.iv_progress);
        //mPromptProgressBar = (ProgressBar) findViewById(R.id.footer_progressbar);
        mPromptTextView = (TextView) findViewById(R.id.footer_text);
        mDrawable = new CircleRipplesDrawable();
        mDrawable.setMaxWidth(Constant.LOADING_CIRCLE_VIEW_MAX_SIZE);
        ivProgress.setImageDrawable(mDrawable);
    }

    public DRecyclerViewPrompt(@NonNull Context context, @LayoutRes int layoutId) {
        super(context);
        LayoutInflater.from(context).inflate(layoutId, this);
        frameView = this.findViewById(R.id.load_more_frame);
        initFootFrame(frameView);
    }

    public  void initFootFrame(View frameView){
        mPromptImageView = (ImageView) findViewById(R.id.footer_image);
        ivProgress = (ImageView) findViewById(R.id.iv_progress);
        //mPromptProgressBar = (ProgressBar) findViewById(R.id.footer_progressbar);
        mPromptTextView = (TextView) findViewById(R.id.footer_text);
    }

    public void resizeLarge() {
        frameView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.d_recycler_prompt_large_height)));
    }

    public void resizeFooter() {
        frameView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, getResources().getDimensionPixelSize(R.dimen.d_recycler_prompt_footer_height)));
    }

    public void hide() {
        frameView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, 0));
    }

    /**
     * 显示正在加载更多的视图(大视图)
     */
    public void loading() {
        resizeLarge();
        setClickable(false);
        setViewVisibilityStatus(mPromptImageView, View.GONE);
        //setViewVisibilityStatus(mPromptProgressBar, View.VISIBLE);
        setViewVisibilityStatus(ivProgress, View.VISIBLE);
        mDrawable.startBreath();
        setViewVisibilityStatus(mPromptTextView, View.GONE);
    }

    /**
     * 没有加载到数据,或者加载失败了
     * @param hint 加载失败后提示的信息
     */
    public void empty(String hint) {
        resizeLarge();
        setClickable(false);
        setViewVisibilityStatus(mPromptImageView, View.VISIBLE);
        //setViewVisibilityStatus(mPromptProgressBar, View.GONE);
        setViewVisibilityStatus(ivProgress, View.GONE);
        mDrawable.stopBreath();
        setViewVisibilityStatus(mPromptTextView, View.VISIBLE);

        mPromptTextView.setText(hint);
        mPromptTextView.invalidate();
    }

    /**
     * 正在加载的时候的视图(小视图)
     */
    public void moreLoading() {
        resizeFooter();
        setClickable(false);
        setViewVisibilityStatus(mPromptImageView, View.GONE);
        //setViewVisibilityStatus(mPromptProgressBar, View.VISIBLE);
        setViewVisibilityStatus(ivProgress, View.VISIBLE);
        mDrawable.startBreath();
        setViewVisibilityStatus(mPromptTextView, View.GONE);
    }

    /**
     * 一般是在加载失败之后让加载更多的footView可以被点击,然后重新加载
     */
    public void moreButton() {
        resizeFooter();
        setClickable(true);
        setViewVisibilityStatus(mPromptImageView, View.GONE);
        //setViewVisibilityStatus(mPromptProgressBar, View.GONE);
        setViewVisibilityStatus(ivProgress, View.GONE);
        mDrawable.stopBreath();
        setViewVisibilityStatus(mPromptTextView, View.VISIBLE);
        mPromptTextView.setText(loadingMoreText);
    }

    /**
     * 已经到数据集的结尾,无法加载更多数据
     */
    public void moreEnd() {
        resizeFooter();
        setClickable(false);
        setViewVisibilityStatus(mPromptImageView, View.GONE);
        //setViewVisibilityStatus(mPromptProgressBar, View.GONE);
        setViewVisibilityStatus(ivProgress, View.GONE);
        mDrawable.stopBreath();
        setViewVisibilityStatus(mPromptTextView, View.VISIBLE);
        mPromptTextView.setText(loadingMoreText);
    }

    private void setViewVisibilityStatus(View v, int visibility){
        if (v != null){
            v.setVisibility(visibility);
        }
    }

    public void setLoadingMoreText(String text){
        loadingMoreText = text;
    }
}
