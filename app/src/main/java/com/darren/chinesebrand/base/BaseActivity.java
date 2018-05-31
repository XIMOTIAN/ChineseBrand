package com.darren.chinesebrand.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.darren.chinesebrand.base.inject.ViewUtils;

/**
 * description:
 * author: Darren on 2017/11/14 09:11
 * email: 240336124@qq.com
 * version: 1.0
 */
public abstract class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManagerUtil.instance().attachActivity(this);
        setContentView();
        ViewUtils.inject(this);
        initTitle();
        initView();
        initData();
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 初始化界面
     */
    protected abstract void initView();

    /**
     * 初始化头部
     */
    protected abstract void initTitle();

    /**
     * 设置内容布局
     */
    protected abstract void setContentView();

    /**
     * 启动Activity
     *
     * @param activity
     */
    protected void startActivity(Class<? extends BaseActivity> activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        AppManagerUtil.instance().detachActivity(this);
        super.onDestroy();
    }
}
