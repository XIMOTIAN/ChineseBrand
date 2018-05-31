package com.darren.chinesebrand.app.activity;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

/**
 * description: 用户协议
 * created by darren on 2017/11/26 14:03
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserProtocolActivity extends BrandBaseActivity {
    @ViewById(R.id.root_view)
    private LinearLayout mRootView;
    @ViewById(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        StatusBarUtil.setStatusBarTrans(this, true);
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, mRootView)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppManagerUtil.instance().finishActivity(UserProtocolActivity.this);
                    }
                })
                .setTitle("用户协议")
                .setLeftIcon(R.drawable.comment_back)
                .hideRightIcon()
                .create();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_protocol);
    }
}
