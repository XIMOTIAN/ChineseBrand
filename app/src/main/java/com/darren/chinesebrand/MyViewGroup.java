package com.darren.chinesebrand;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;

/**
 * description:
 * author: Darren on 2017/11/23 14:43
 * email: 240336124@qq.com
 * version: 1.0
 */
public class MyViewGroup extends LinearLayout{
    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyViewGroup(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        Log.e("MyViewGroup", "dispatchDraw----->");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e("MyViewGroup", "onDraw----->");
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.e("MyViewGroup", "draw----->");
    }

    public void set(){
        invalidate();
    }
}
