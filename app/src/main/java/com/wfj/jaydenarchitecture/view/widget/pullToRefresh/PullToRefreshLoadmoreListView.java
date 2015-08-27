package com.wfj.jaydenarchitecture.view.widget.pullToRefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.app.Constant;
import com.wfj.jaydenarchitecture.view.widget.CircleRipplesDrawable;

import fbcore.log.LogUtil;
import fbcore.utils.Strings;

public class PullToRefreshLoadmoreListView extends PullToRefreshListView {

    /** 一开始的状态， 为不可见状态 **/
    public static final int STATU_LOAD_MORE_DEFAULT = 0;
    /** 开始加载数据 */
    public static final int STATU_LOAD_MORE_START = 1;
    /** 加载成功，但是还是有数据（还有下一页） **/
    public static final int STATU_LOAD_MORE_SUCCESS = 2;
    /** 加载成功，但是没有更多数据了 **/
    public static final int STATU_LOAD_MORE_NO_DATA = 3;
    /** 加载数据失败(比如网络原因，服务器无返回值或者返回值错误) */
    public static final int STATU_LOAD_MORE_FALIUR = 4;

    private ListView listView;

    private View footerView;
    private TextView loadMoreText;
    private ImageView progress;

    private boolean loadMoreEnable = true;
    private int loadMoreStatu = -1;

    private String loadMoreOverText;

    /** 使用自定义的脚 */
    private boolean useDefaultFooterView = false;

    private boolean nomoreDataFlag;

    private final int ROTATE_ANIM_DURATION = 500;

    private CircleRipplesDrawable loadingDrawable;

    public PullToRefreshLoadmoreListView(Context context) {
        super(context);
        initAnimation();
    }

    public PullToRefreshLoadmoreListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnimation();
    }

    public PullToRefreshLoadmoreListView(Context context, Mode mode) {
        super(context, mode);
        initAnimation();
    }

    public PullToRefreshLoadmoreListView(Context context, Mode mode, AnimationStyle style) {
        super(context, mode, style);
        initAnimation();
    }

    private void initAnimation() {
        nomoreDataFlag = false;
    }

    public void setRefleshLoadMoreEnable(boolean refleshEnable, boolean loadMoreEnable) {
        this.loadMoreEnable = loadMoreEnable;
        if (!refleshEnable) {
            this.setMode(Mode.DISABLED); // 不开启下拉刷新
        }
    }

    public void initView(View headView, View footView) {
        this.footerView = footView;
        listView = this.getRefreshableView();
        if(headView != null){
            listView.addHeaderView(headView);
        }
        if(!loadMoreEnable) {
            return;
        }
        if(footView != null) {
            useDefaultFooterView = true;
            listView.addFooterView(footView);
        } else {
            listView.addFooterView(initFooterView());
            setLoadMoreSatu(STATU_LOAD_MORE_DEFAULT);
        }
    }

    public boolean isNoMoreDataStatus() {
        return nomoreDataFlag;
    }

    /** 恢复加载更多 */
    public void resetLoadmoreEnable() {
        nomoreDataFlag = false;
    }

    public void setLoadMoreSatu(int statu) {
        if(loadMoreStatu == statu || useDefaultFooterView) {
            return;
        }
        LogUtil.d("loadmoreview", "setLoadMoreSatu -> " + statu);
        footerView.setVisibility(View.VISIBLE);
        if(!loadMoreEnable) {
            statu = STATU_LOAD_MORE_NO_DATA;
        }
        switch (statu) {
            case STATU_LOAD_MORE_DEFAULT:
                loadingDrawable.stopBreath();
                footerView.setVisibility(View.INVISIBLE);
                break;
            case STATU_LOAD_MORE_START:
                nomoreDataFlag = false;
                setLoadMoreStart(true);
                break;
            case STATU_LOAD_MORE_SUCCESS:
                footerView.setVisibility(View.VISIBLE);
                setLoadMoreStart(false, "");
                break;
            case STATU_LOAD_MORE_NO_DATA:
                nomoreDataFlag = true;
                setLoadMoreStart(false, Strings.isEmpty(loadMoreOverText) ? "" : loadMoreOverText);
//                listView.removeFooterView(footerView);
                break;
            case STATU_LOAD_MORE_FALIUR:
                setLoadMoreStart(false, "加载数据失败[点击重试]");
                break;
        }
        loadMoreStatu = statu;
    }


    private void setLoadMoreStart(boolean loadMoreStart) {
        setLoadMoreStart(loadMoreStart, "");
    }

    private void setLoadMoreStart(boolean loadMoreStart, String text) {
        footerView.setVisibility(View.VISIBLE);
        if(loadMoreStart) {
            progress.setVisibility(View.VISIBLE);
            loadingDrawable.startBreath();
            loadMoreText.setVisibility(View.GONE);
        } else {
            loadingDrawable.stopBreath();
            progress.setVisibility(View.GONE);
            loadMoreText.setVisibility(View.VISIBLE);
            loadMoreText.setText(text);
        }
    }

    public int getLoadMoreStatu() {
        return loadMoreStatu;
    }

    private View initFooterView(){
        footerView = LayoutInflater.from(this.getContext()).inflate(R.layout.layout_load_more, null);
        loadMoreText = (TextView) footerView.findViewById(R.id.load_more_text);
        progress = (ImageView) footerView.findViewById(R.id.pull_to_refresh_progress);
        loadingDrawable = new CircleRipplesDrawable();
        loadingDrawable.setMaxWidth(Constant.LOADING_CIRCLE_VIEW_MAX_SIZE);
        progress.setImageDrawable(loadingDrawable);
        return footerView;
    }

    public void setFooterLoadMoreOverText(String loadMoreOverText) {
        this.loadMoreOverText = loadMoreOverText;
    }

    public ListView getListView(){
        return listView;
    }
    
}
