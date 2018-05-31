package com.darren.chinesebrand.app.activity;

import android.view.View;
import android.view.ViewGroup;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.inject.OnClick;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

/**
 * description: 设置页面
 * author: Darren on 2017/11/27 09:32
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserSettingActivity extends BrandBaseActivity {
    @ViewById(R.id.root_view)
    private ViewGroup mRootView;

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
                        AppManagerUtil.instance().finishActivity(UserSettingActivity.this);
                    }
                })
                .setTitle("设置")
                .setLeftIcon(R.drawable.comment_back)
                .hideRightIcon()
                .create();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_setting);
    }

    @OnClick(R.id.clear_cache)
    private void clearCacheClick() {
        showToast("清除缓存成功");
    }

    @OnClick(R.id.about_us)
    private void aboutUsClick() {
        startActivity(AboutUsActivity.class);
    }

    @OnClick(R.id.user_exit_login)
    private void userExitLoginClick() {
        LoginSession.getLoginSession().exitUser();
        startActivity(HomeActivity.class);
    }
}
