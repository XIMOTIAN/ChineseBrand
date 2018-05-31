package com.darren.chinesebrand.app.activity.user;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.MessageListResult;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.adapter.CommonRecyclerAdapter;
import com.darren.chinesebrand.base.adapter.ViewHolder;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

import java.util.List;

/**
 * description: 我的消息
 * created by darren on 2017/11/26 14:03
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserMessageActivity extends BrandBaseActivity {
    @ViewById(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    @ViewById(R.id.root_view)
    private LinearLayout mRootView;

    @Override
    protected void initData() {
        GoHttp.create(this)
                .get()
                .cache(true)
                .url(Constant.UrlConstant.MESSAGE_PUBLIC_URL)
                .execute(new HttpCallback<MessageListResult>() {
                    @Override
                    protected void onSuccess(MessageListResult result) {
                        if (result.isOk()) {
                            showMessageList(result.data.data);
                        }
                    }
                });
    }

    private void showMessageList(List<MessageListResult.Message> list) {
        CommonRecyclerAdapter<MessageListResult.Message> adapter = new CommonRecyclerAdapter<MessageListResult.Message>(this, list, R.layout.item_message_rv) {
            @Override
            public void convert(ViewHolder holder, MessageListResult.Message item, int position) {
                holder.setText(R.id.message_time_tv, item.getCtime()+ "");
                holder.setText(R.id.message_content_tv, item.getContent());
                holder.setText(R.id.message_title_tv, item.getTopic());
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
                        AppManagerUtil.instance().finishActivity(UserMessageActivity.this);
                    }
                })
                .setTitle("我的消息")
                .setLeftIcon(R.drawable.comment_back)
                .hideRightIcon()
                .create();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_message);
    }
}
