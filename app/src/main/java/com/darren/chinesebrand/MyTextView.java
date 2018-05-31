package com.darren.chinesebrand;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

/**
 * description:
 * author: Darren on 2017/11/23 14:42
 * email: 240336124@qq.com
 * version: 1.0
 */
public class MyTextView extends AppCompatTextView {
    private Paint mPaint = new Paint();
    public MyTextView(Context context) {
        this(context,null);
    }

    public MyTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.e("TAG", "----->");

        canvas.drawText("1111111111111",10,10,mPaint);
    }

    public void set() {
        invalidate();
    }
}
