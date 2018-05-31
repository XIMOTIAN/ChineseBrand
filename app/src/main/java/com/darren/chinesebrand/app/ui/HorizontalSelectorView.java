package com.darren.chinesebrand.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.darren.chinesebrand.R;

import java.util.List;

/**
 * description:
 * author: Darren on 2017/12/1 10:43
 * email: 240336124@qq.com
 * version: 1.0
 */
public class HorizontalSelectorView extends View {
    // 颜色，字体大小，圆角，字体颜色，边框大小
    private int mSolidColor = Color.WHITE;
    private int mTextNormalColor = Color.GRAY;
    private int mTextSelectColor = Color.GRAY;
    private int mTextSize = 14;
    private int mBorderWidth = 2;
    private int mBorderCorner = 3;
    private Paint mBgPaint, mTextPaint;
    // 当前选中的文字角标
    private int mCurrentIndex = 0;
    // 所有显示的文字
    private List<String> mSelectorStrs;
    private int mItemWidth;

    public HorizontalSelectorView(Context context) {
        this(context, null);
    }

    public HorizontalSelectorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HorizontalSelectorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HorizontalSelectorView);
        mSolidColor = array.getColor(R.styleable.HorizontalSelectorView_solidColor, mSolidColor);
        mTextNormalColor = array.getColor(R.styleable.HorizontalSelectorView_textNormalColor, mTextNormalColor);
        mTextSelectColor = array.getColor(R.styleable.HorizontalSelectorView_textSelectColor, mTextSelectColor);
        mTextSize = (int) array.getDimension(R.styleable.HorizontalSelectorView_textSize, mTextSize);
        mBorderWidth = (int) array.getDimension(R.styleable.HorizontalSelectorView_borderWidth, mBorderWidth);
        mBorderCorner = (int) array.getDimension(R.styleable.HorizontalSelectorView_borderCorner, mBorderCorner);
        array.recycle();

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setDither(true);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setDither(true);

        mBgPaint.setStyle(Paint.Style.STROKE);
        mBgPaint.setColor(mSolidColor);
        mBgPaint.setStrokeWidth(mBorderWidth);
        mTextPaint.setTextSize(mTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mSelectorStrs == null || mSelectorStrs.size() == 0) {
            return;
        }
        mItemWidth = getMeasuredWidth() / mSelectorStrs.size();
        // 画背景

        for (int i = 0; i < mSelectorStrs.size(); i++) {

            drawBackground(canvas, i);

            String text = mSelectorStrs.get(i);
            // 获取文字的宽度
            Rect bounds = new Rect();
            mTextPaint.getTextBounds(text, 0, text.length(), bounds);
            float x = i * mItemWidth + (mItemWidth - bounds.width()) / 2;

            // 获取文字的宽高
            Paint.FontMetricsInt fontMetrics = mTextPaint.getFontMetricsInt();
            int fontTotalHeight = fontMetrics.bottom - fontMetrics.top;
            // 计算基线到中心点的位置
            int offY = fontTotalHeight / 2 - fontMetrics.bottom;
            // 计算基线位置
            int baseline = (getMeasuredHeight() + fontTotalHeight) / 2 - offY;

            if (i == mCurrentIndex) {
                // 画选中
                mTextPaint.setColor(mTextSelectColor);
            } else {
                // 画未选中
                mTextPaint.setColor(mTextNormalColor);
            }

            canvas.drawText(text, x, baseline, mTextPaint);
        }
    }

    /**
     * 画背景
     *
     * @param canvas
     * @param index
     */
    private void drawBackground(Canvas canvas, int index) {
        // 判断位置开始或者结尾
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int startX = 0;
        int endX = 0;

        // 中间
        if (mCurrentIndex == index)
            mBgPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        else
            mBgPaint.setStyle(Paint.Style.STROKE);

        canvas.save();
        if (index == 0) {
            endX = mItemWidth;
            // 开始
            RectF rect = new RectF(0 + mBorderWidth / 2, 0 + mBorderWidth / 2, endX + mItemWidth / 2, height - mBorderWidth / 2);

            canvas.clipRect(0, 0, mItemWidth, height);

            canvas.drawRoundRect(rect, mBorderCorner, mBorderCorner, mBgPaint);
        } else if (index == mSelectorStrs.size() - 1) {
            // 结束
            startX = width - mItemWidth;
            RectF rect = new RectF(startX - mItemWidth / 2, 0 + mBorderWidth / 2, width - mBorderWidth / 2, height - mBorderWidth / 2);

            canvas.clipRect(startX, 0, width, height);
            canvas.drawRoundRect(rect, mBorderCorner, mBorderCorner, mBgPaint);
        } else {
            startX = mItemWidth * index;
            endX = startX + mItemWidth;
            RectF rect = new RectF(startX, 0 + mBorderWidth / 2, endX - mBorderWidth / 2, height - mBorderWidth / 2);
            canvas.drawRect(rect, mBgPaint);
        }

        canvas.restore();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // 处理手指按下
            int currentIndex = (int) (event.getX() / mItemWidth);
            if (currentIndex != mCurrentIndex) {
                mCurrentIndex = currentIndex;
                invalidate();
                if (mListener != null)
                    mListener.select(mCurrentIndex);
            }
        }
        return true;
    }

    public void addSelectorStrs(List<String> selectorStrs) {
        this.mSelectorStrs = selectorStrs;
        invalidate();
    }

    private OnSelectorChangeListener mListener;

    public void setOnSelectorChangeListener(OnSelectorChangeListener listener) {
        this.mListener = listener;
    }

    public interface OnSelectorChangeListener {
        void select(int position);
    }
}
