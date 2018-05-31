package com.darren.chinesebrand.app.ui;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * description:验证码按钮
 * author: Darren on 2017/11/23 09:20
 * email: 240336124@qq.com
 * version: 1.0
 */
public class VerificationCodeButton extends AppCompatButton {
    private String mDefaultText;
    private int mDefaultWidth;

    public VerificationCodeButton(Context context) {
        this(context, null);
    }

    public VerificationCodeButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerificationCodeButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDefaultText = getText().toString().trim();
    }

    public void waitSend() {
        mDefaultWidth = getMeasuredWidth();

        setText("请稍后...");
        setEnabled(false);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = mDefaultWidth;
        setLayoutParams(params);
    }

    public void restoreDefault() {
        setText(mDefaultText);
        setEnabled(true);
    }

    /**
     * 开始倒计时
     *
     * @param format
     * @param number
     */
    public void startCountDown(String format, int number) {
        setEnabled(false);
        CountDown countDown = new CountDown(number * 1000, 1000, format);
        countDown.start();
    }

    private class CountDown extends CountDownTimer {
        private String format;

        public CountDown(long millisInFuture, long countDownInterval, String format) {
            super(millisInFuture, countDownInterval);
            this.format = format;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            setText(String.format(format, millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            restoreDefault();
        }
    }
}
