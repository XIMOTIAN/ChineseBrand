package com.darren.chinesebrand.app.activity.user;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.HistoryDao;
import com.darren.chinesebrand.app.activity.BrandDetailActivity;
import com.darren.chinesebrand.app.http.response.BrandListResult;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.adapter.CommonRecyclerAdapter;
import com.darren.chinesebrand.base.adapter.MultiTypeSupport;
import com.darren.chinesebrand.base.adapter.ViewHolder;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

import java.util.List;

/**
 * description: 浏览历史
 * created by darren on 2017/11/26 14:03
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserHistoryActivity extends BrandBaseActivity {
    @ViewById(R.id.root_view)
    private LinearLayout mRootView;
    @ViewById(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @Override
    protected void initData() {

        List<BrandListResult.BrandBeanData.DataBean> brandHistory = HistoryDao.getInstance().getBrandHistory();
        if (brandHistory == null || brandHistory.size() == 0) {
            return;
        }


        CommonRecyclerAdapter<BrandListResult.BrandBeanData.DataBean> adapter = new CommonRecyclerAdapter<BrandListResult.BrandBeanData.DataBean>(this, brandHistory,
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
                    }
                });
            }
        };

        mRecyclerView.setAdapter(adapter);
    }

    @Override
    protected void initView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initTitle() {
        StatusBarUtil.setStatusBarTrans(this, true);
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, mRootView)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppManagerUtil.instance().finishActivity(UserHistoryActivity.this);
                    }
                })
                .setTitle("浏览历史")
                .setLeftIcon(R.drawable.comment_back)
                .hideRightIcon()
                .create();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_history);
    }
}
