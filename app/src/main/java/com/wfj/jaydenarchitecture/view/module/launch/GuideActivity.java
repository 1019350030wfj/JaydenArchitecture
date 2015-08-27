package com.wfj.jaydenarchitecture.view.module.launch;

import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.wfj.jaydenarchitecture.R;
import com.wfj.jaydenarchitecture.view.TitleBar;
import com.wfj.jaydenarchitecture.view.adapter.LaunchPageAdapter;
import com.wfj.jaydenarchitecture.view.module.BaseTitleBarActivity;
import com.wfj.jaydenarchitecture.view.widget.IndicatorView;

/**
 * Created by Jayden on 2015/8/27.
 */
public class GuideActivity extends BaseTitleBarActivity implements View.OnClickListener{

    private int[] guides;

    private ImageView[] views;
    private ViewPager viewPager;
    private IndicatorView indicatorView;

    private int curPage = 0;

    private ImageView btnStart;

    public GuideActivity() {
        super(false);
    }

    @Override
    public View getContentView() {
        return LayoutInflater.from(this).inflate(R.layout.activity_guide, null);
    }

    @Override
    public void setTitleBar(TitleBar titlebar) {

    }

    @Override
    public void initWidget(View view) {
        indicatorView = (IndicatorView) findViewById(R.id.guide_indicator);
        btnStart = (ImageView) findViewById(R.id.btn_start);
        btnStart.setVisibility(View.GONE);
        viewPager = (ViewPager) findViewById(R.id.pager);
    }

    @Override
    public void initContentView() {
        guides = new int[]{R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};
        views = new ImageView[guides.length];
        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < guides.length; i++) {
            views[i] = new ImageView(this);
            views[i].setBackgroundResource(guides[i]);
            views[i].setLayoutParams(lp);
        }

        indicatorView.setCount(guides.length);
        indicatorView.setCurrentPosition(0);

        LaunchPageAdapter adapter = new LaunchPageAdapter();
        adapter.setGroups(views);
        viewPager.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        btnStart.setOnClickListener(this);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                indicatorView.setCurrentPosition(position);
                if (position != guides.length - 1) {
//                    indicatorView.setVisibility(View.VISIBLE);
                    btnStart.setVisibility(View.GONE);
                } else {
//                    indicatorView.setVisibility(View.GONE);
                    btnStart.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                curPage = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                if (arg0 == ViewPager.SCROLL_STATE_IDLE) {
//                    if (curPage == views.length - 1) {
//                        finish();
//                        overridePendingTransition(0, 0);
//                    }
//                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (curPage == guides.length - 1) {
            finish();
            overridePendingTransition(0, 0);
        }
    }
}
