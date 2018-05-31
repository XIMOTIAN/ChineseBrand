package com.darren.chinesebrand.app.activity;

import android.os.Handler;

import com.darren.chinesebrand.framework.BrandBaseActivity;

/**
 * 欢迎页
 */
public class WelcomeActivity extends BrandBaseActivity {

    @Override
    protected void initData() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(HomeActivity.class);
                finish();
            }
        }, 500);
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {
        //setContentView(R.layout.activity_welcome);
    }

    @Override
    public void onBackPressed() {

    }
}
