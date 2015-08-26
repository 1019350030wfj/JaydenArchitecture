package com.wfj.jaydenarchitecture.view.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.animation.DecelerateInterpolator;

import com.nineoldandroids.animation.ValueAnimator;

/**
 * y
 * Created by Jayden on 2015/8/26.
 */
public class CircleRipplesDrawable extends Drawable {
    private static final Long  DEFAULT_DURATION = 1300L;
    private Paint paint;
    private int minWidth;
    private int maxWidth;
    private int color;
    private float progress;
    private ValueAnimator animator;
    private long duration = DEFAULT_DURATION;

    public CircleRipplesDrawable(){
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        color = Color.BLACK;
        maxWidth = 50;
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(color);
    }
    @Override
    public void draw(Canvas canvas) {
        Rect bounds = getBounds();
        canvas.drawCircle(bounds.centerX(), bounds.centerY(), (maxWidth - minWidth) * progress + minWidth, paint);
    }

    @Override
    public void setAlpha(int alpha) {
        paint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        paint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return 1 - paint.getAlpha();
    }

    public void setProgress(float progress){
        this.progress = progress;
        setAlpha((int) (255 * (1 - progress)));
        invalidateSelf();
    }

    public CircleRipplesDrawable startBreath(){
        if (animator == null) {
            animator = ValueAnimator.ofFloat(1.0f);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Float progress = (Float) animation.getAnimatedValue();
                    setProgress(progress);
                }
            });
            animator.setInterpolator(new DecelerateInterpolator());
            animator.setDuration(duration);
        }
        animator.setRepeatCount(ValueAnimator.INFINITE);
        if (!animator.isRunning()) animator.start();
        return this;
    }

    public void stopBreath(){
        if (animator != null){
            animator.end();
            animator.cancel();
        }
    }

    public void fillAfter(){
        if (animator.isRunning()){
            animator.end();
            animator.cancel();
        }
        animator.setRepeatCount(0);
        animator.start();
    }

    /**
     * 设置呼吸圈的最小半径
     * @param minWidth
     */
    public CircleRipplesDrawable setMinWidth(int minWidth){
        this.minWidth = minWidth < 0 ? 0: minWidth;
        return this;
    }

    /**
     * 设置呼吸圈的最大半径
     * @param maxWidth
     */
    public CircleRipplesDrawable setMaxWidth(int maxWidth){
        this.maxWidth = maxWidth < 0 ? 0: maxWidth;
        return this;
    }

    public int getMinWidth(){
        return minWidth;
    }

    public int getMaxWidth(){
        return maxWidth;
    }

    /**
     * 设置呼吸圈的颜色
     * @param color
     */
    public CircleRipplesDrawable setColor(int color){
        this.color = color;
        paint.setColor(color);
        return this;
    }

    public CircleRipplesDrawable setDuration(long duration){
        this.duration = duration;
        return this;
    }
}
