package com.darren.chinesebrand.framework.ui.image;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.darren.chinesebrand.R;

/**
 * description:
 * author: Darren on 2017/11/14 16:56
 * email: 240336124@qq.com
 * version: 1.0
 */
public class ProportionImageView extends AppCompatImageView {
    // 用float可能更好 获取宽高比例
    private float mWidthPro;
    private float mHeightPro;

    public ProportionImageView(Context context) {
        this(context, null);
    }

    public ProportionImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProportionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttribute(context, attrs);
    }

    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,
                R.styleable.ProportionImageView);
        mWidthPro = array.getFloat(
                R.styleable.ProportionImageView_widthProportion, mWidthPro);
        mHeightPro = array.getFloat(
                R.styleable.ProportionImageView_heightProportion, mHeightPro);
        array.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 先测量一下
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 用户没有设置宽高比就不处理
        if (mWidthPro == 0 || mHeightPro == 0) {
            return;
        }

        // 拿到宽
        int width = getMeasuredWidth();

        // 如果宽高没有的情况下，不做任何的处理
        if (width <= 0) {
            return;
        }
        // 宽度有值，求高度
        int height = (int) (width * (mHeightPro / mWidthPro));
        // 宽和高按比例设置
        setMeasuredDimension(width, height);
    }
}
