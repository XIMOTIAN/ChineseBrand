package com.darren.chinesebrand.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.HistoryDao;
import com.darren.chinesebrand.app.activity.BrandDetailActivity;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.BannerListResult;
import com.darren.chinesebrand.app.http.response.BrandListResult;
import com.darren.chinesebrand.app.ui.SwipeRefreshRecyclerView;
import com.darren.chinesebrand.base.adapter.CommonRecyclerAdapter;
import com.darren.chinesebrand.base.adapter.MultiTypeSupport;
import com.darren.chinesebrand.base.adapter.ViewHolder;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseFragment;
import com.darren.chinesebrand.framework.ui.BannerAdapter;
import com.darren.chinesebrand.framework.ui.BannerView;
import com.darren.chinesebrand.framework.ui.BannerViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: Darren on 2017/11/14 15:17
 * email: 240336124@qq.com
 * version: 1.0
 */
public class BrandItemFragment extends BrandBaseFragment implements SwipeRefreshRecyclerView.OnRefreshLoadListener {
    @ViewById(R.id.wrap_rv)
    private SwipeRefreshRecyclerView mWrapRv;
    private String mItemStr;
    private int mPage = 1;
    private BannerView mBannerView;

    private List<BannerListResult.Banner> mBanners;

    private List<BrandListResult.BrandBeanData.DataBean> mBrandList;

    private CommonRecyclerAdapter<BrandListResult.BrandBeanData.DataBean> mAdapter;

    @Override
    protected void initView(){
        mWrapRv.setEnabled(false);
        mWrapRv.setOnRefreshLoadListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mItemStr = bundle.getString("key");
        mPage = 1;
        mBrandList = new ArrayList<>();
        requestBanner();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        String listUrl = Constant.UrlConstant.URL_BASE + "list";
        GoHttp.create(mContext).url(listUrl)
                .addParam("id", mItemStr)
                .addParam("page", mPage)
                .get().cache(mPage == 1)
                .execute(new HttpCallback<BrandListResult>() {

                    @Override
                    public void onPreExecute() {

                    }

                    @Override
                    protected void onSuccess(BrandListResult result) {
                        mWrapRv.stop();
                        if (result.isOk()) {
                            showListData(result.getData().getData());
                        }
                    }
                });
    }

    /**
     * 显示列表数九
     *
     * @param data
     */
    private void showListData(List<BrandListResult.BrandBeanData.DataBean> data) {

        if (mPage == 1) {
            mBrandList.clear();
        }

        mBrandList.addAll(data);

        if (mAdapter == null) {
            mAdapter = new CommonRecyclerAdapter<BrandListResult.BrandBeanData.DataBean>(mContext, data,
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
            mWrapRv.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        if (mBannerView == null && mBanners != null) {
            mBannerView = (BannerView) LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_list_banner, mWrapRv, false);

            mBannerView.setAdapter(new BannerAdapter() {
                @Override
                public View getView(int position, View convertView) {

                    ImageView imageView = null;
                    if (convertView != null) {
                        imageView = (ImageView) convertView;
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                    } else {
                        imageView = new ImageView(mContext);
                    }

                    Glide.with(mContext)
                            .load(mBanners.get(position).img)
                            .centerCrop()
                            .into(imageView);
                    return imageView;
                }

                @Override
                public int getCount() {
                    return mBanners.size();
                }

                @Override
                public String getBannerDesc(int position) {
                    return mBanners.get(position).title;
                }
            });

            mBannerView.startRoll();
            mWrapRv.addHeaderView(mBannerView);

            mBannerView.setOnBannerItemClickListener(new BannerViewPager.BannerItemClickListener() {
                @Override
                public void click(int position) {
                    if (mBanners.get(position).ai_id == 0) return;
                    Intent intent = new Intent(mContext, BrandDetailActivity.class);
                    intent.putExtra(BrandDetailActivity.EXTRA_ID, mBanners.get(position).ai_id);
                    startActivity(intent);
                }
            });
        }

        mWrapRv.showLoad();
        if(data.size() < Constant.VALUE.PAGE_SIZE){
            mWrapRv.showLoadComplete();
        }
    }

    /**
     * 请求banner数据
     */
    private void requestBanner() {
        GoHttp.create(mContext).url(Constant.UrlConstant.BANNER_LIST_URL)
                .get()
                .cache(true)
                .execute(new HttpCallback<BannerListResult>() {
                    @Override
                    protected void onSuccess(BannerListResult result) {
                        if (result.isOk()) {
                            mBanners = result.data;
                        }
                        requestData();
                    }
                });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_item_brand;
    }

    @Override
    public void onRefresh() {
        mPage = 1;
        requestData();
    }

    @Override
    public void onLoad() {
        mPage += 1;
        requestData();
    }
}
