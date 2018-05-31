package com.darren.chinesebrand.base.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.ViewGroup;

import java.util.List;

/**
 * description: viewpager通用适配器
 * Created by 谢光亚 on  2016/10/14
 * QQ/E-mail：409918544
 * Version：1.0
 */

public class CommonPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;

    public CommonPageAdapter(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        Log.e("TAG", "destroyItem");
    }
}
