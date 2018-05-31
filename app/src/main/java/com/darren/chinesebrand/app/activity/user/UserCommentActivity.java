package com.darren.chinesebrand.app.activity.user;

import android.os.Bundle;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.fragment.CommentFragment;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.ui.HorizontalSelectorView;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.BaseFragment;
import com.darren.chinesebrand.base.inject.OnClick;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.util.FragmentManagerHelper;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description: 我的评论
 * created by darren on 2017/11/26 14:03
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserCommentActivity extends BrandBaseActivity implements HorizontalSelectorView.OnSelectorChangeListener {
    @ViewById(R.id.selector_view)
    private HorizontalSelectorView mSelectorView;
    private List<BaseFragment> mFragments;
    private FragmentManagerHelper mManagerHelper;

    @Override
    protected void initData() {
        mFragments = new ArrayList<>();
        BaseFragment commentFragment = getFragment(Constant.UrlConstant.MINE_COMMENT_URL);
        mFragments.add(commentFragment);
        commentFragment = getFragment(Constant.UrlConstant.COVER_COMMENT_URL);
        mFragments.add(commentFragment);

        mManagerHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.comment_container);
        mManagerHelper.add(mFragments.get(0));
    }

    private BaseFragment getFragment(String url) {
        BaseFragment brandFragment = new CommentFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", url);
        brandFragment.setArguments(bundle);
        return brandFragment;
    }

    @Override
    protected void initView() {
        List<String> items = new ArrayList<>();
        items.add("我的回复");
        items.add("我的评论");
        mSelectorView.addSelectorStrs(items);
        mSelectorView.setOnSelectorChangeListener(this);
    }

    @Override
    protected void initTitle() {
        StatusBarUtil.setStatusBarTrans(this, true);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_comment);
    }

    @Override
    public void select(int position) {
        BaseFragment brandFragment = mFragments.get(position);
        if (brandFragment != null) {
            mManagerHelper.switchFragment(brandFragment);
        }
    }

    @OnClick(R.id.left_iv)
    private void leftIvClick() {
        AppManagerUtil.instance().finishActivity(this);
    }
}
