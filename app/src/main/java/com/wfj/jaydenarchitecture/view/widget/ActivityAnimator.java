package com.wfj.jaydenarchitecture.view.widget;

import android.app.Activity;
import android.content.Context;

import com.wfj.jaydenarchitecture.R;


/**
 * 描 述 ：Activity切换动画
 */
public class ActivityAnimator {

    private static ActivityAnimator animator = new ActivityAnimator();

    private ActivityAnimator() {
    }

    public static ActivityAnimator getInstance() {
        return animator;
    }

    public void flipHorizontalAnimation(Activity a) {
        a.overridePendingTransition(R.anim.flip_horizontal_in, R.anim.flip_horizontal_out);
    }

    public void flipVerticalAnimation(Activity a) {
        a.overridePendingTransition(R.anim.flip_vertical_in, R.anim.flip_vertical_out);
    }

    public void flipTopBottomEnterAnimation(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_from_bottom, R.anim.activity_slide_in_from_top);
    }

    public void flipTopBottomExistAnimation(Context context) {
        ((Activity) context).overridePendingTransition(R.anim.activity_slide_in_from_top, R.anim.activity_login_out);
    }

    public void fadeAnimation(Activity a) {
        a.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void disappearTopLeftAnimation(Activity a) {
        a.overridePendingTransition(R.anim.disappear_top_left_in, R.anim.disappear_top_left_out);
    }

    public void appearTopLeftAnimation(Activity a) {
        a.overridePendingTransition(R.anim.appear_top_left_in, R.anim.appear_top_left_out);
    }

    public void disappearBottomRightAnimation(Activity a) {
        a.overridePendingTransition(R.anim.disappear_bottom_right_in, R.anim.disappear_bottom_right_out);
    }

    public void appearBottomRightAnimation(Activity a) {
        a.overridePendingTransition(R.anim.appear_bottom_right_in, R.anim.appear_bottom_right_out);
    }

    public void unzoomAnimation(Activity a) {
        a.overridePendingTransition(R.anim.unzoom_in, R.anim.unzoom_out);
    }

    public void PullRightPushLeft(Activity a) {
        a.overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
    }

    public void PullLeftPushRight(Activity a) {
        a.overridePendingTransition(R.anim.pull_in_left, R.anim.push_out_right);
    }

    public void transBackLeft(Activity a) {
        a.overridePendingTransition(R.anim.open_next, R.anim.close_main);
    }

    public void transBackRight(Activity a) {
        a.overridePendingTransition(R.anim.open_main, R.anim.close_next);
    }
}