package com.darren.chinesebrand.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.darren.chinesebrand.base.inject.ViewUtils;

/**
 * description:
 * author: Darren on 2017/11/14 09:33
 * email: 240336124@qq.com
 * version: 1.0
 */
public abstract class BaseFragment extends Fragment {
    private View mRootView;
    protected Context mContext;

    @Nullable
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.mContext = this.getActivity();
        this.mRootView = inflater.inflate(this.getLayoutId(), container, false);
        return this.mRootView;
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ViewUtils.inject(mRootView, this);
        this.initView();
        this.initData(savedInstanceState);
    }

    protected abstract void initView();

    protected abstract void initData(Bundle savedInstanceState);

    protected abstract int getLayoutId();

    protected <T extends View> T findViewById(int viewId) {
        return (T) this.mRootView.findViewById(viewId);
    }

    /**
     * 启动Activity
     *
     * @param activity
     */
    protected void startActivity(Class<? extends BaseActivity> activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
    }
}
