/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.darren.chinesebrand.app.ui;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.base.ui.WrapRecyclerView;

/**
 * The SwipeRefreshLayout should be used whenever the user can refresh the
 * contents of a view via a vertical swipe gesture. The activity that
 * instantiates this view should add an OnRefreshListener to be notified
 * whenever the swipe to refresh gesture is completed. The SwipeRefreshLayout
 * will notify the listener each and every time the gesture is completed again;
 * the listener is responsible for correctly determining when to actually
 * initiate a refresh of its content. If the listener determines there should
 * not be a refresh, it must call setRefreshing(false) to cancel any visual
 * indication of a refresh. If an activity wishes to show just the progress
 * animation, it should call setRefreshing(true). To disable the gesture and
 * progress animation, call setEnabled(false) on the view.
 * <p>
 * This layout should be made the parent of the view that will be refreshed as a
 * result of the gesture and can only support one direct child. This view will
 * also be made the target of the gesture and will be forced to match both the
 * width and the height supplied in this layout. The SwipeRefreshLayout does not
 * provide accessibility events; instead, a menu item must be provided to allow
 * refresh of the content wherever this gesture is used.
 * </p>
 */
public class SwipeRefreshRecyclerView extends SwipeRefreshLayout {
    private WrapRecyclerView mRecyclerView;
    private View mLoadMoreView;
    private TextView mLoadMoreTv;
    private View mLoadMoreIv;
    private boolean mLoadEnable;
    private final int STATUS_LOAD = 2, STATUS_REFRESH = 1;
    private int mStatus = STATUS_REFRESH;

    public SwipeRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRecyclerView = new WrapRecyclerView(context);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        addView(mRecyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                //是否是最后一个显示的item位置
                if (mListener != null && isBottom(mRecyclerView) && mLoadEnable) {
                    mListener.onLoad();
                    mStatus = STATUS_LOAD;
                }
            }
        });

        super.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mListener != null) {
                    mListener.onRefresh();
                    mStatus = STATUS_REFRESH;
                }
            }
        });

        setColorSchemeResources(R.color.color_E84040,R.color.colorPrimaryDark);
    }

    @Override
    public void setOnRefreshListener(OnRefreshListener listener) {
        // 不让用户在外面调用
    }

    /**
     * 判断是否滑动到底部
     */
    private boolean isBottom(View view) {
        return !ViewCompat.canScrollVertically(view, 1);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        mRecyclerView.setAdapter(adapter);
        // 添加底部布局
        if (mLoadMoreView == null) {
            mLoadMoreView = LayoutInflater.from(getContext()).inflate(R.layout.layout_list_load_more, mRecyclerView, false);
            mLoadMoreTv = (TextView) mLoadMoreView.findViewById(R.id.load_tv);
            mLoadMoreIv = mLoadMoreView.findViewById(R.id.load_iv);
        }
        mRecyclerView.addFooterView(mLoadMoreView);
    }

    public void addHeaderView(View headerView) {
        mRecyclerView.addHeaderView(headerView);
    }

    /**
     * 显示加载完成
     */
    public void showLoadComplete() {
        mLoadEnable = false;
        mLoadMoreIv.setVisibility(View.GONE);
        mLoadMoreTv.setText("已全部加载完毕");
    }

    /**
     * 显示加载
     */
    public void showLoad() {
        mLoadEnable = true;
        mLoadMoreIv.setVisibility(View.VISIBLE);
        mLoadMoreTv.setText("加载中...");
    }

    private OnRefreshLoadListener mListener;

    public void setOnRefreshLoadListener(OnRefreshLoadListener listener) {
        this.mListener = listener;
    }

    public interface OnRefreshLoadListener {
        void onRefresh();
        void onLoad();
    }

    public void stop() {
        setRefreshing(false);
    }
}
