package com.example.customanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator;

public class PulseAnimationView extends View {

    private float mRadius;
    private Paint paint = new Paint();
    public static final int COLOR_ADJUSTER = 5;

    private float mX;
    private float mY;

    private static final int ANIMATION_DURATION = 4000;
    private static final long ANIMATION_DELAY = 1000;

    private AnimatorSet mPulseAnimatorSet = new AnimatorSet();

    public void setRadius(float radius) {
        this.mRadius = radius;
        paint.setColor(Color.BLACK + (int) radius/COLOR_ADJUSTER);
        invalidate();
    }

    public PulseAnimationView(Context context) {
        super(context);
    }

    public PulseAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN){
            mX = event.getX();
            mY = event.getY();

            if (mPulseAnimatorSet != null && mPulseAnimatorSet.isRunning() ){
                mPulseAnimatorSet.cancel();
            }
            mPulseAnimatorSet.start();
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        ObjectAnimator grow = ObjectAnimator.ofFloat(this,"radius",0,getWidth());
        grow.setDuration(ANIMATION_DURATION);
        grow.setInterpolator(new LinearInterpolator());

        ObjectAnimator shrink = ObjectAnimator.ofFloat(this,"radius",getWidth(),0);
        shrink.setDuration(ANIMATION_DURATION);
        shrink.setInterpolator(new LinearOutSlowInInterpolator());

        shrink.setStartDelay(ANIMATION_DELAY);

        ObjectAnimator repeat = ObjectAnimator.ofFloat(this,"radius", 0, getWidth());
        repeat.setStartDelay(ANIMATION_DELAY);
        repeat.setDuration(ANIMATION_DURATION);
        repeat.setRepeatCount(1);
        repeat.setRepeatMode(ValueAnimator.REVERSE);

        mPulseAnimatorSet.play(grow).before(shrink);
        mPulseAnimatorSet.play(repeat).before(shrink);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mX,mY,mRadius,paint);
    }
}
