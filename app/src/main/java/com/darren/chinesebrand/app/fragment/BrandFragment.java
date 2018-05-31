package com.darren.chinesebrand.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextPaint;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.SortListResult;
import com.darren.chinesebrand.app.ui.indicator.IndicatorAdapter;
import com.darren.chinesebrand.app.ui.indicator.TrackIndicatorView;
import com.darren.chinesebrand.base.adapter.CommonPageAdapter;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseFragment;
import com.darren.chinesebrand.framework.util.GeneralUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: Darren on 2017/11/14 15:17
 * email: 240336124@qq.com
 * version: 1.0
 */
public class BrandFragment extends BrandBaseFragment {
    @ViewById(R.id.indicator_view)
    private TrackIndicatorView mIndicatorView;
    @ViewById(R.id.view_pager)
    private ViewPager mViewPager;
    private String mItemStr;

    @Override
    protected void initView() {

    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mItemStr = bundle.getString("key");
        requestIndicatorData();
    }

    /**
     * 请求头部指示器的数据
     */
    private void requestIndicatorData() {
        GoHttp.create(mContext).url(Constant.UrlConstant.HOME_SORT_URL)
                .get().cache(true)
                .execute(new HttpCallback<SortListResult>() {

                    @Override
                    protected void onSuccess(SortListResult result) {
                        if (result.isOk()) {
                            showListData(result.getData());
                        } else {
                            showToast(result.msg);
                        }
                    }
                });
    }

    /**
     * 显示列表数九
     *
     * @param data
     */
    private void showListData(List<SortListResult.SortEntity> data) {
        final List<SortListResult.SortEntity> childSorts = childForData(data);
        if (childSorts != null && childSorts.size() > 0) {
            // 初始化头部数据和Fragment
            IndicatorAdapter<TextView> indicatorAdapter = new IndicatorAdapter<TextView>() {
                @Override
                public int getCount() {
                    return childSorts.size();
                }

                @Override
                public TextView getView(int position, ViewGroup parent) {
                    TextView itemTv = new TextView(mContext);
                    itemTv.setText(childSorts.get(position).getIs_name());
                    itemTv.setGravity(Gravity.CENTER);
                    itemTv.setTextSize(17);
                    int dip10 = GeneralUtil.dip2px(mContext, 10);
                    itemTv.setPadding(dip10, dip10, dip10, dip10);
                    itemTv.setTextColor(Color.parseColor("#999999"));
                    return itemTv;
                }

                @Override
                public void highLightIndicator(TextView itemTv) {
                    itemTv.setTextSize(17);
                    TextPaint tp = itemTv.getPaint();
                    tp.setFakeBoldText(true);
                    itemTv.setTextColor(Color.parseColor("#E84040"));
                }

                @Override
                public void restoreIndicator(TextView itemTv) {
                    itemTv.setTextSize(17);
                    TextPaint tp = itemTv.getPaint();
                    tp.setFakeBoldText(false);
                    itemTv.setTextColor(Color.parseColor("#999999"));
                }
            };

            mIndicatorView.setAdapter(indicatorAdapter, mViewPager);

            // 给ViewPager 设置 Adapter
            List<Fragment> itemFragments = new ArrayList<>();
            for (SortListResult.SortEntity childSort : childSorts) {
                BrandItemFragment itemFragment = new BrandItemFragment();
                Bundle bundle = new Bundle();
                bundle.putString("key", childSort.getIs_id() + "");
                itemFragment.setArguments(bundle);
                itemFragments.add(itemFragment);
            }

            mViewPager.setAdapter(new CommonPageAdapter(getChildFragmentManager(), itemFragments));
        }
    }

    private List<SortListResult.SortEntity> childForData(List<SortListResult.SortEntity> data) {
        for (SortListResult.SortEntity sortEntity : data) {
            if (mItemStr.equals(sortEntity.getIs_name())) {
                return sortEntity.pnames;
            }
        }
        return data.get(0).pnames;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.fragment_brand;
    }
}
