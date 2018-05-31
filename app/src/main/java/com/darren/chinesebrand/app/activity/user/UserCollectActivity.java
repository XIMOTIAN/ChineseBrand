package com.darren.chinesebrand.app.activity.user;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.HistoryDao;
import com.darren.chinesebrand.app.activity.BrandDetailActivity;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.BrandListResult;
import com.darren.chinesebrand.app.ui.SwipeRefreshRecyclerView;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.adapter.CommonRecyclerAdapter;
import com.darren.chinesebrand.base.adapter.MultiTypeSupport;
import com.darren.chinesebrand.base.adapter.ViewHolder;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: 我的收藏
 * created by darren on 2017/11/26 14:03
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserCollectActivity extends BrandBaseActivity implements SwipeRefreshRecyclerView.OnRefreshLoadListener {
    @ViewById(R.id.recycler_view)
    private SwipeRefreshRecyclerView mRecyclerView;
    @ViewById(R.id.root_view)
    private LinearLayout mRootView;
    private int mPage = 1, mPageSize = 8;
    private CommonRecyclerAdapter<BrandListResult.BrandBeanData.DataBean> mAdapter;
    private List<BrandListResult.BrandBeanData.DataBean> mBrands;

    @Override
    protected void initData() {
        mBrands = new ArrayList<>();
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        String listUrl = Constant.UrlConstant.MINE_COLLECT_URL;
        Map<String, Object> params = new HashMap<>();
        if (LoginSession.getLoginSession().isLogin()) {
            params.put("token", LoginSession.getLoginSession().getUserInfo().token);
        }

        params.put("rows", mPageSize);
        params.put("page", mPage);

        GoHttp.create(this).url(listUrl)
                .addParams(params)
                .get().cache(mPage == 1)
                .execute(new HttpCallback<BrandListResult>() {

                    @Override
                    public void onPreExecute() {

                    }

                    @Override
                    protected void onSuccess(BrandListResult result) {
                        mRecyclerView.stop();
                        if (result.isOk()) {
                            showListData(result.getData().getData());
                        }
                    }
                });
    }

    private void showListData(List<BrandListResult.BrandBeanData.DataBean> data) {
        if (mPage == 1) {
            mBrands.clear();
        }

        mBrands.addAll(data);

        if (mAdapter == null) {
            mAdapter = new CommonRecyclerAdapter<BrandListResult.BrandBeanData.DataBean>(this, mBrands,
                    new MultiTypeSupport<BrandListResult.BrandBeanData.DataBean>() {

                        @Override
                        public int getLayoutId(BrandListResult.BrandBeanData.DataBean item, int position) {
                            switch (item.getAi_smimg().size()) {
                                case 1:
                                    return R.layout.item_brand_style1;
                                case 2:
                                    return R.layout.item_brand_style2;
                                case 3:
                                    return R.layout.item_brand_style3;
                            }
                            return R.layout.item_brand_style1;
                        }
                    }) {
                @Override
                public void convert(ViewHolder holder, final BrandListResult.BrandBeanData.DataBean item, int position) {
                    switch (item.getAi_smimg().size()) {
                        case 1:
                            ImageView image1 = holder.getView(R.id.image_1);
                            Glide.with(mContext)
                                    .load(item.getAi_smimg().get(0))
                                    .centerCrop()
                                    .error(R.drawable.pic_error_icon)
                                    .placeholder(R.drawable.pic_loding_icon)
                                    .into(image1);
                            break;
                        case 2:
                            image1 = holder.getView(R.id.image_1);
                            Glide.with(mContext)
                                    .load(item.getAi_smimg().get(0))
                                    .centerCrop()
                                    .error(R.drawable.pic_error_icon)
                                    .placeholder(R.drawable.pic_loding_icon)
                                    .into(image1);
                            ImageView image2 = holder.getView(R.id.image_2);
                            Glide.with(mContext)
                                    .load(item.getAi_smimg().get(1))
                                    .centerCrop()
                                    .error(R.drawable.pic_error_icon)
                                    .placeholder(R.drawable.pic_loding_icon)
                                    .into(image2);
                            break;

                        case 3:
                            image1 = holder.getView(R.id.image_1);
                            Glide.with(mContext)
                                    .load(item.getAi_smimg().get(0))
                                    .centerCrop()
                                    .error(R.drawable.pic_error_icon)
                                    .placeholder(R.drawable.pic_loding_icon)
                                    .into(image1);
                            image2 = holder.getView(R.id.image_2);
                            Glide.with(mContext)
                                    .load(item.getAi_smimg().get(1))
                                    .centerCrop()
                                    .error(R.drawable.pic_error_icon)
                                    .placeholder(R.drawable.pic_loding_icon)
                                    .into(image2);
                            ImageView image3 = holder.getView(R.id.image_3);
                            Glide.with(mContext)
                                    .load(item.getAi_smimg().get(2))
                                    .centerCrop()
                                    .error(R.drawable.pic_error_icon)
                                    .placeholder(R.drawable.pic_loding_icon)
                                    .into(image3);
                            break;
                    }

                    holder.setText(R.id.title_tv, item.getAi_topic());

                    holder.setText(R.id.desc_tv, item.getIs_name() + "  " + item.getComments() + "  " + item.getAi_ctime());

                    holder.setOnIntemClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, BrandDetailActivity.class);
                            intent.putExtra(BrandDetailActivity.EXTRA_ID, item.ai_id + "");
                            startActivity(intent);

                            // 插入一条历史记录
                            HistoryDao.getInstance().insertBrandHistory(item);
                        }
                    });
                }
            };

            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        mRecyclerView.showLoadComplete();
    }

    @Override
    protected void initView() {
        mRecyclerView.setOnRefreshLoadListener(this);
    }

    @Override
    protected void initTitle() {
        StatusBarUtil.setStatusBarTrans(this, true);
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, mRootView)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppManagerUtil.instance().finishActivity(UserCollectActivity.this);
                    }
                })
                .setTitle("我的收藏")
                .setLeftIcon(R.drawable.comment_back)
                .hideRightIcon()
                .create();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_collect);
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        mRecyclerView.stop();
    }

    @Override
    public void onLoad() {
        mPage += 1;
        requestData();
    }
}
