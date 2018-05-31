package com.darren.chinesebrand.framework;

import android.widget.Toast;

import com.darren.chinesebrand.base.BaseFragment;

/**
 * description:
 * author: Darren on 2017/11/14 09:33
 * email: 240336124@qq.com
 * version: 1.0
 */
public abstract class BrandBaseFragment extends BaseFragment {
    protected void showToast(String text) {
        Toast.makeText(mContext,text,Toast.LENGTH_LONG).show();
    }


}
