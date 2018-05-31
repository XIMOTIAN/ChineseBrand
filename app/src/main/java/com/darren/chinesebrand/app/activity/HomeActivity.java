package com.darren.chinesebrand.app.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.activity.user.UserCollectActivity;
import com.darren.chinesebrand.app.activity.user.UserCommentActivity;
import com.darren.chinesebrand.app.activity.user.UserHistoryActivity;
import com.darren.chinesebrand.app.activity.user.UserMessageActivity;
import com.darren.chinesebrand.app.activity.user.UserSuggestActivity;
import com.darren.chinesebrand.app.aspectj.CheckLogin;
import com.darren.chinesebrand.app.fragment.BrandFragment;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.UserLoginResult;
import com.darren.chinesebrand.app.ui.MainBottomTabItem;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.app.user.UserData;
import com.darren.chinesebrand.app.util.DelayedUtil;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.BaseActivity;
import com.darren.chinesebrand.base.BaseFragment;
import com.darren.chinesebrand.base.adapter.CommonRecyclerAdapter;
import com.darren.chinesebrand.base.adapter.ViewHolder;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.OnClick;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.bottom.TabBottomNavigation;
import com.darren.chinesebrand.framework.bottom.iterator.TabListIterator;
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.FragmentManagerHelper;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * description:
 * author: Darren on 2017/11/14 10:10
 * email: 240336124@qq.com
 * version: 1.0
 */
public class HomeActivity extends BrandBaseActivity implements TabBottomNavigation.TabBottomClickListener {
    @ViewById(R.id.root_view)
    private ViewGroup mRootView;
    @ViewById(R.id.drawer_layout)
    private DrawerLayout mDrawerLayout;
    @ViewById(R.id.tab_bottom_navigation)
    private TabBottomNavigation mTabBottomNavigation;
    private FragmentManagerHelper mManagerHelper;
    private List<BaseFragment> mBrandFragments;
    @ViewById(R.id.menu_rv)
    private RecyclerView mMenuRv;

    private List<MenuItem> mMenuItems;
    @ViewById(R.id.user_header_iv)
    private CircleImageView mUserHeaderIv;
    @ViewById(R.id.user_name_tv)
    private TextView mUserNameTv;
    @ViewById(R.id.user_desc)
    private TextView mUserDesc;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        // 底部
        TabListIterator<MainBottomTabItem> tabIterator = new TabListIterator<>();
        tabIterator.addItem(new MainBottomTabItem.Builder(this).resIcon(R.drawable.main_tab_information).text("资讯").create());
        tabIterator.addItem(new MainBottomTabItem.Builder(this).resIcon(R.drawable.main_tab_comment).text("评价").create());
        tabIterator.addItem(new MainBottomTabItem.Builder(this).resIcon(R.drawable.main_tab_index).text("指数").create());
        tabIterator.addItem(new MainBottomTabItem.Builder(this).resIcon(R.drawable.main_tab_tendency).text("趋势").create());
        tabIterator.addItem(new MainBottomTabItem.Builder(this).resIcon(R.drawable.main_tab_cyclopedia).text("百科").create());
        mTabBottomNavigation.setTabIterator(tabIterator);
        mTabBottomNavigation.setOnTabBottomClickListener(this);
        // Fragment + FrameLayout
        initFragments();
        mManagerHelper = new FragmentManagerHelper(getSupportFragmentManager(), R.id.frame_layout);
        mManagerHelper.add(mBrandFragments.get(0));


        mMenuItems = new ArrayList<>();
        mMenuItems.add(new MenuItem("我的消息", R.drawable.menu_message));
        mMenuItems.add(new MenuItem("互动评论", R.drawable.menu_comment));
        mMenuItems.add(new MenuItem("浏览历史", R.drawable.menu_history));
        mMenuItems.add(new MenuItem("我的收藏", R.drawable.menu_collect));
        mMenuItems.add(new MenuItem("意见反馈", R.drawable.menu_suggest));

        mMenuRv.setLayoutManager(new LinearLayoutManager(this));
        mMenuRv.setAdapter(new CommonRecyclerAdapter<MenuItem>(this, mMenuItems, R.layout.item_menu_rv) {
            @Override
            public void convert(ViewHolder holder, final MenuItem item, int position) {
                holder.setText(R.id.menu_text, item.text);
                ImageView imageView = holder.getView(R.id.menu_icon);
                imageView.setImageResource(item.icon);

                holder.setOnIntemClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switchJump(item.text);
                    }
                });
            }
        });
    }

    private void switchJump(String text) {
        Class<? extends BaseActivity> clazz = null;
        switch (text) {
            case "我的消息":
                clazz = UserMessageActivity.class;
                break;
            case "互动评论":
                clazz = UserCommentActivity.class;
                break;
            case "浏览历史":
                clazz = UserHistoryActivity.class;
                break;
            case "我的收藏":
                clazz = UserCollectActivity.class;
                break;
            case "意见反馈":
                clazz = UserSuggestActivity.class;
                break;
        }

        if (clazz != null) {
            startActivity(clazz);
        }
    }

    private void initFragments() {
        mBrandFragments = new ArrayList<>();
        BaseFragment brandFragment = getFragment("资讯");
        mBrandFragments.add(brandFragment);
        brandFragment = getFragment("评价");
        mBrandFragments.add(brandFragment);
        brandFragment = getFragment("指数");
        mBrandFragments.add(brandFragment);
        brandFragment = getFragment("趋势");
        mBrandFragments.add(brandFragment);
        brandFragment = getFragment("百科");
        mBrandFragments.add(brandFragment);
    }

    private BaseFragment getFragment(String key) {
        BaseFragment brandFragment = new BrandFragment();
        Bundle bundle = new Bundle();
        bundle.putString("key", key);
        brandFragment.setArguments(bundle);
        return brandFragment;
    }

    @Override
    protected void initTitle() {
        StatusBarUtil.setStatusBarTrans(this, true);
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, mRootView)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    @CheckLogin
                    public void onClick(View v) {
                        mDrawerLayout.openDrawer(GravityCompat.START);
                        requestUserInfo();
                    }
                })
                .setRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        showToast("搜索");
                    }
                })
                .create();
    }

    /**
     * 请求用户信息
     */
    private void requestUserInfo() {
        GoHttp.create(this).url(Constant.UrlConstant.USER_INFO_URL)
                .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                .post()
                .cache(true)
                .execute(new HttpCallback<UserLoginResult>() {
                    @Override
                    protected void onSuccess(UserLoginResult result) {
                        if (result.isOk()) {
                            showUserInfo(result.data);
                        }
                    }
                });
    }

    private void showUserInfo(UserData data) {
        Glide.with(this)
                .load(data.au_logo)
                .error(R.drawable.default_user_header)
                .into(mUserHeaderIv);

        mUserNameTv.setText(data.au_nickname);

        mUserDesc.setText(data.au_sex + "•" + data.au_place);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_home);
    }

    @Override
    public void onBackPressed() {
        if (DelayedUtil.isFastClick(2000)) {
            showToast("再按一次退出");
        } else {
            AppManagerUtil.instance().AppExit(this);
        }
    }

    @Override
    public void click(int position) {
        BaseFragment brandFragment = mBrandFragments.get(position);
        if (brandFragment != null) {
            mManagerHelper.switchFragment(brandFragment);
        }
    }


    @OnClick(R.id.home_menu)
    private void homeMenuClick() {
        // 防止菜单打开时点击到主内容
    }

    @OnClick(R.id.menu_set)
    private void menuSetClick() {
        startActivity(UserSettingActivity.class);
    }

    @OnClick(R.id.menu_edit_info)
    private void menuEditInfoClick() {
        startActivity(UserEditInfoActivity.class);
    }


    private static final class MenuItem {
        public MenuItem(String text, int icon) {
            this.text = text;
            this.icon = icon;
        }

        String text;
        int icon;
    }


}
