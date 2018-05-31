package com.darren.chinesebrand.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.activity.user.UserLoginActivity;
import com.darren.chinesebrand.app.aspectj.CheckLogin;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.BannerDetailResult;
import com.darren.chinesebrand.app.http.response.CommentListResult;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.app.util.DialogUitl;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.adapter.CommonRecyclerAdapter;
import com.darren.chinesebrand.base.adapter.ViewHolder;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.http.callback.ServerDataTransformException;
import com.darren.chinesebrand.base.inject.OnClick;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.dialog.CommonDialog;
import com.darren.chinesebrand.framework.http.BaseResult;
import com.darren.chinesebrand.framework.util.GeneralUtil;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description:
 * created by darren on 2017/11/25 15:47
 * email: 240336124@qq.com
 * version: 1.0
 */
public class BrandDetailActivity extends BrandBaseActivity {

    public static final String EXTRA_ID = "EXTRA_ID";
    @ViewById(R.id.back)
    private ImageView mBack;
    @ViewById(R.id.share_iv)
    private ImageView mShareIv;
    @ViewById(R.id.comment_rv)
    private RecyclerView mCommentRv;
    private String mBrandId;

    private CommonDialog mShareDialog;

    private BannerDetailResult.BannerDetail mDetailData;
    /****Title****/
    @ViewById(R.id.title_tv)
    private TextView mTitleTv;

    private CommonRecyclerAdapter<CommentListResult.DataBeanX.DataBean> mAdapter;
    private List<CommentListResult.DataBeanX.DataBean> mComments;
    @ViewById(R.id.web_view)
    private WebView mWebView;
    @ViewById(R.id.collect_cb)
    private CheckBox mCollectCb;
    /****查看更多评论 > ****/
    @ViewById(R.id.comment_more_tv)
    private TextView mCommentMoreTv;

    private CommonDialog mCommentDialog;

    private Handler mHandler = new Handler();

    @Override
    protected void initData() {
        mBrandId = getIntent().getStringExtra(EXTRA_ID);
        mComments = new ArrayList<>();
        requestDetail(mBrandId);
    }

    /**
     * 请求详情数据
     *
     * @param brandId
     */
    private void requestDetail(String brandId) {
        String detailUrl = Constant.UrlConstant.URL_BASE + "detail";

        Map<String, Object> params = new HashMap<>();
        if (LoginSession.getLoginSession().isLogin()) {
            params.put("token", LoginSession.getLoginSession().getUserInfo().token);
        }

        params.put("id", brandId);

        GoHttp.create(this).url(detailUrl)
                .get()
                .cache(true)
                .addParams(params)
                .execute(new HttpCallback<BannerDetailResult>() {

                    @Override
                    public void onError(Exception e) {
                        showToast(getString(R.string.net_error_tip));
                        AppManagerUtil.instance().finishActivity(BrandDetailActivity.this);
                    }

                    @Override
                    protected void serverTransformError(ServerDataTransformException e) {
                        showToast(getString(R.string.server_error_tip));
                        AppManagerUtil.instance().finishActivity(BrandDetailActivity.this);
                    }

                    @Override
                    protected void onSuccess(BannerDetailResult result) {
                        if (result.isOk()) {
                            showDetailData(result.data);
                        } else {
                            showToast(result.msg);
                            AppManagerUtil.instance().finishActivity(BrandDetailActivity.this);
                        }
                    }
                });
    }

    /**
     * 显示数据
     *
     * @param data
     */
    private void showDetailData(BannerDetailResult.BannerDetail data) {
        mDetailData = data;
        requestCommentList();

        mTitleTv.setText(data.ai_small_topic);
        mWebView.loadData(mDetailData.ai_content, "text/html;charset=utf-8", "utf-8");
        mCollectCb.setChecked(mDetailData.is_collection == 1);
    }

    private void requestCommentList() {
        String commentUrl = Constant.UrlConstant.URL_BASE + "detail_comment/" + mBrandId;

        Map<String, Object> params = new HashMap<>();
        if (LoginSession.getLoginSession().isLogin()) {
            params.put("token", LoginSession.getLoginSession().getUserInfo().token);
        }
        params.put("rows", 3);
        params.put("page", 1);
        params.put("id", mBrandId);

        GoHttp.create(this)
                .url(commentUrl)
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
        mComments.clear();
        mComments.addAll(comments);
        if (mAdapter == null) {
            mAdapter = new CommonRecyclerAdapter<CommentListResult.DataBeanX.DataBean>
                    (this, mComments, R.layout.item_brand_comment_rv) {
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

                    holder.setOnIntemClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, CommentDetailActivity.class);
                            intent.putExtra(CommentDetailActivity.EXTRA_ID, parentItem.getAc_id());
                            startActivity(intent);
                        }
                    });

                    if (parentItem.getComment() == null || parentItem.getComment().size() == 0) {
                        holder.setViewVisibility(R.id.comment_rv, View.GONE);
                        return;
                    } else {
                        holder.setViewVisibility(R.id.comment_rv, View.VISIBLE);
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

                            holder.setOnIntemClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(mContext, CommentDetailActivity.class);
                                    intent.putExtra(CommentDetailActivity.EXTRA_ID, parentItem.getAc_id());
                                    startActivity(intent);
                                }
                            });
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
    }

    @Override
    protected void initView() {
        mCommentRv.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void initTitle() {
        StatusBarUtil.setStatusBarTrans(this, true);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_brand_detail);
    }

    @OnClick(R.id.back)
    private void backClick() {
        AppManagerUtil.instance().finishActivity(this);
    }

    @OnClick(R.id.share_iv)
    private void shareIvClick() {
        if (mShareDialog == null) {
            mShareDialog = new CommonDialog.Builder(this)
                    .setView(R.layout.dialog_detail_share)
                    .setOnClickListener(R.id.delete_dialog, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mShareDialog.dismiss();
                        }
                    })
                    .setOnClickListener(R.id.share_qq, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            qqShare();
                            mShareDialog.dismiss();
                        }
                    })
                    .setOnClickListener(R.id.share_wx, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wxShare();
                            mShareDialog.dismiss();
                        }
                    })
                    .setOnClickListener(R.id.share_wb, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            wbShare();
                            mShareDialog.dismiss();
                        }
                    })
                    .fromBottom()
                    .fullWidth()
                    .create();
        }

        mShareDialog.show();
    }

    /**
     * wx分享
     */
    @OnClick(R.id.share_wx)
    private void wxShare() {
        showToast("wx分享");
    }

    /**
     * QQ分享
     */
    @OnClick(R.id.share_qq)
    private void qqShare() {
        showToast("QQ分享");
    }

    /**
     * WB分享
     */
    @OnClick(R.id.share_wb)
    private void wbShare() {
        showToast("WB分享");
    }

    @OnClick(R.id.collect_cb)
    private void collectCbClick() {
        if (!LoginSession.getLoginSession().isLogin()) {
            startActivity(UserLoginActivity.class);
            mCollectCb.setChecked(false);
            return;
        }
        // 添加收藏和取消收藏
        String collectUrl = Constant.UrlConstant.URL_BASE + "collection";
        GoHttp.create(this)
                .post()
                .addParam("id", mBrandId)
                .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                .url(collectUrl)
                .execute();
    }

    @OnClick(R.id.comment_more_tv)
    private void commentMoreTvClick() {
        Intent intent = new Intent(this, BrandCommentListActivity.class);
        intent.putExtra(BrandCommentListActivity.EXTRA_ID, mBrandId);
        startActivity(intent);
    }

    @OnClick(R.id.write_comment)
    @CheckLogin
    private void writeCommentClick() {
        // 弹出评论框
        if (mCommentDialog == null) {
            mCommentDialog = new CommonDialog.Builder(this)
                    .setView(R.layout.dialog_write_comment)
                    .fullWidth()
                    .fromBottom()
                    .setOnClickListener(R.id.submit_comment, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final EditText commentEt = mCommentDialog.getView(R.id.comment_content_et);
                            submitComment(commentEt);
                        }
                    })
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            GeneralUtil.hideSoftInputFromWindow(mCommentRv);
                        }
                    })
                    .create();
        }
        mCommentDialog.show();
        final EditText commentEt = mCommentDialog.getView(R.id.comment_content_et);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 弹出键盘
                //设置可获得焦点
                commentEt.setFocusable(true);
                commentEt.setFocusableInTouchMode(true);
                //请求获得焦点
                commentEt.requestFocus();
                GeneralUtil.showSoftInputFromWindow(commentEt);
            }
        }, 200);
    }

    private void submitComment(final EditText commentEt) {
        String commentContent = commentEt.getText().toString();
        if (TextUtils.isEmpty(commentContent)) {
            showToast("请输入评论内容");
            return;
        }

        // 调用接口发起评论
        GoHttp.create(this)
                .url(Constant.UrlConstant.BRAND_COMMENT_URL)
                .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                .addParam("pid", mBrandId)
                .addParam("content", commentContent)
                .execute(new HttpCallback<BaseResult>() {

                    @Override
                    public void onPreExecute() {
                        DialogUitl.showProgressDialog(BrandDetailActivity.this, "请稍后...");
                    }

                    @Override
                    public void onError(Exception e) {
                        DialogUitl.dismissProgressDialog();
                        showToast(getString(R.string.net_error_tip));
                    }

                    @Override
                    protected void serverTransformError(ServerDataTransformException e) {
                        DialogUitl.dismissProgressDialog();
                        showToast(getString(R.string.server_error_tip));
                    }

                    @Override
                    protected void onSuccess(BaseResult result) {
                        DialogUitl.dismissProgressDialog();
                        if (result.isOk()) {
                            commentEt.setText("");
                            mCommentDialog.dismiss();
                            requestCommentList();
                        }
                        showToast(result.msg);
                    }
                });
    }

    @OnClick(R.id.comment_list_iv)
    private void commentListIvClick() {
        Intent intent = new Intent(this, BrandCommentListActivity.class);
        intent.putExtra(BrandCommentListActivity.EXTRA_ID, mBrandId);
        startActivity(intent);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        requestCommentList();
    }
}
