package com.darren.chinesebrand.app.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.activity.user.UserLoginActivity;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.CommentListResult;
import com.darren.chinesebrand.app.ui.SwipeRefreshRecyclerView;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.base.adapter.CommonRecyclerAdapter;
import com.darren.chinesebrand.base.adapter.ViewHolder;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseFragment;
import com.darren.chinesebrand.framework.http.BaseResult;
import com.darren.chinesebrand.framework.util.GeneralUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * author: Darren on 2017/11/14 15:17
 * email: 240336124@qq.com
 * version: 1.0
 */
public class CommentFragment extends BrandBaseFragment implements SwipeRefreshRecyclerView.OnRefreshLoadListener {
    @ViewById(R.id.wrap_rv)
    private SwipeRefreshRecyclerView mCommentRv;
    private String mCommentUrl;
    private int mPage = 1;
    private int mPageSize = 8;

    private CommonRecyclerAdapter<CommentListResult.DataBeanX.DataBean> mAdapter;
    private List<CommentListResult.DataBeanX.DataBean> mComments;

    @Override
    protected void initView() {
        mCommentRv.setOnRefreshLoadListener(this);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mCommentUrl = bundle.getString("key");
        mPage = 1;
        mPageSize = 8;
        mComments = new ArrayList<>();
        requestData();
    }

    /**
     * 请求数据
     */
    private void requestData() {
        Map<String, Object> params = new HashMap<>();
        if (LoginSession.getLoginSession().isLogin()) {
            params.put("token", LoginSession.getLoginSession().getUserInfo().token);
        }

        params.put("rows", mPageSize);
        params.put("page", mPage);

        GoHttp.create(mContext)
                .url(mCommentUrl)
                .addParams(params)
                .get()
                .execute(new HttpCallback<CommentListResult>() {
                    @Override
                    protected void onSuccess(CommentListResult result) {
                        if (result.isOk()) {
                            showCommentList(result.getData().getData());
                        }
                    }
                });
    }

    private void showCommentList(List<CommentListResult.DataBeanX.DataBean> comments) {
        if (mPage == 1) {
            mComments.clear();
        }
        mComments.addAll(comments);
        if (mAdapter == null) {
            mAdapter = new CommonRecyclerAdapter<CommentListResult.DataBeanX.DataBean>
                    (mContext, mComments, R.layout.item_brand_comment_rv) {
                @Override
                public void convert(ViewHolder holder, final CommentListResult.DataBeanX.DataBean parentItem, int position) {
                    holder.setText(R.id.user_name_tv, parentItem.getAu_nickname());
                    final ImageView headerIv = holder.getView(R.id.user_header_iv);
                    Glide.with(mContext).load(parentItem.getAu_logo())
                            .error(R.drawable.default_user_header)
                            .into(headerIv);

                    holder.setText(R.id.comment_number_tv, parentItem.getComments() + "");
                    holder.setText(R.id.like_number_tv, parentItem.getLikes() + "");
                    holder.setText(R.id.comment_content_tv, parentItem.getAc_content());
                    holder.setText(R.id.time_tv, parentItem.aac_ctime);
                    final CheckBox likeCb = holder.getView(R.id.like_comment_cb);
                    likeCb.setChecked(parentItem.is_like == 1);

                    final TextView likeNumberTv = holder.getView(R.id.like_number_tv);

                    holder.setOnClickListener(R.id.like_comment_cb, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (!LoginSession.getLoginSession().isLogin()) {
                                startActivity(UserLoginActivity.class);
                                likeCb.setChecked(false);
                                return;
                            }
                            likeCb.setChecked(true);

                            // 有没有被点赞，如果有不允许取消点赞
                            if (parentItem.is_like != 1) {
                                // 调用接口点赞
                                parentItem.is_like = 1;
                                parentItem.setLikes(parentItem.getLikes() + 1);
                                likeNumberTv.setText(parentItem.getLikes() + "");

                                GoHttp.create(mContext)
                                        .url(Constant.UrlConstant.COMMENT_LIKE_URL)
                                        .post()
                                        .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                                        .addParam("pid", parentItem.getAc_id())
                                        .execute(new HttpCallback<BaseResult>() {
                                            @Override
                                            protected void onSuccess(BaseResult result) {

                                            }
                                        });
                            }
                        }
                    });

                    /*holder.setOnIntemClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, CommentDetailActivity.class);
                            intent.putExtra(CommentDetailActivity.EXTRA_ID, parentItem.getAc_id());
                            startActivity(intent);
                        }
                    });*/

                    if (parentItem.getComment() == null || parentItem.getComment().size() == 0) {
                        holder.setViewVisibility(R.id.comment_rv, View.GONE);
                        return;
                    }

                    // 显示二级评论
                    RecyclerView commentRv = holder.getView(R.id.comment_rv);

                    CommonRecyclerAdapter<CommentListResult.CommentBean>
                            commentAdapter = new CommonRecyclerAdapter<CommentListResult.CommentBean>
                            (mContext, parentItem.getComment(), R.layout.item_second_comment_rv) {
                        @Override
                        public void convert(ViewHolder holder, CommentListResult.CommentBean item, int position) {
                            StringBuffer sb = new StringBuffer();

                            sb.append("<font color='#0B5D99'>").append(item.getBhfz_nickname());
                            if (TextUtils.isEmpty(item.getHfz_nickname())) {
                                sb.append("：").append("</font>");
                            } else {
                                sb.append("</font>").append("回复").append("<font color='#0B5D99'>")
                                        .append(item.getHfz_nickname()).append("：")
                                        .append("</font>");
                            }

                            sb.append(item.getAc_content());
                            TextView commentTv = holder.getView(R.id.second_comment_tv);
                            commentTv.setText(GeneralUtil.fromHtml(sb.toString()));

                            /*holder.setOnIntemClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, CommentDetailActivity.class);
                                    intent.putExtra(CommentDetailActivity.EXTRA_ID, parentItem.getAc_id());
                                    startActivity(intent);
                                }
                            });*/
                        }
                    };
                    commentRv.setLayoutManager(new LinearLayoutManager(mContext));
                    commentRv.setAdapter(commentAdapter);
                }
            };
            mCommentRv.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }

        mCommentRv.stop();
        mCommentRv.showLoadComplete();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_comment;
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
