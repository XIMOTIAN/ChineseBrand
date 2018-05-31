package com.darren.chinesebrand.app.activity;

import android.content.DialogInterface;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.aspectj.CheckLogin;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.CommentDetailResult;
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
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.GeneralUtil;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * description: 评论列表页
 * created by darren on 2017/11/26 14:03
 * email: 240336124@qq.com
 * version: 1.0
 */
public class CommentDetailActivity extends BrandBaseActivity {
    @ViewById(R.id.root_view)
    private LinearLayout mRootView;
    private int mPage = 1;
    private int mPageSize = 8;
    public static final String EXTRA_ID = "EXTRA_ID";
    private int mCommentId;
    private CommonRecyclerAdapter<CommentListResult.CommentBean> mAdapter;
    @ViewById(R.id.user_header_iv)
    private CircleImageView mUserHeaderIv;
    /****Darren****/
    @ViewById(R.id.user_name_tv)
    private TextView mUserNameTv;
    /****4小时前****/
    @ViewById(R.id.time_tv)
    private TextView mTimeTv;
    @ViewById(R.id.like_comment_cb)
    private CheckBox mLikeCommentCb;
    /****33****/
    @ViewById(R.id.like_number_tv)
    private TextView mLikeNumberTv;
    /****8****/
    @ViewById(R.id.comment_number_tv)
    private TextView mCommentNumberTv;
    @ViewById(R.id.comment_content_tv)
    private TextView mCommentContentTv;
    @ViewById(R.id.comment_rv)
    private RecyclerView mCommentRv;

    private List<CommentListResult.CommentBean> mComments;
    private CommonDialog mCommentDialog;
    // 是否是二级评论
    private boolean mIsSecondComment = false;
    // 回复的是谁
    private String mCommentName;
    // 对评论进行评论的评论id
    private int mSubmitCommentId;

    private Handler mHandler = new Handler();

    @Override
    protected void initData() {
        mCommentId = getIntent().getIntExtra(EXTRA_ID, mCommentId);
        mComments = new ArrayList<>();
        requestCommentList();
    }

    private void requestCommentList() {

        Map<String, Object> params = new HashMap<>();
        if (LoginSession.getLoginSession().isLogin()) {
            params.put("token", LoginSession.getLoginSession().getUserInfo().token);
        }
        params.put("rows", mPageSize);
        params.put("page", mPage);
        params.put("id", mCommentId);

        GoHttp.create(this)
                .url(Constant.UrlConstant.COMMENT_DETAIL_URL)
                .addParams(params)
                .get()
                .execute(new HttpCallback<CommentDetailResult>() {
                    @Override
                    protected void onSuccess(CommentDetailResult result) {
                        if (result.isOk()) {
                            showCommentList(result.data);
                        }
                    }
                });
    }

    private void showCommentList(final CommentListResult.DataBeanX.DataBean data) {

        mComments.clear();
        mComments.addAll(data.getComment());

        mUserNameTv.setText(data.getAu_nickname());
        mCommentNumberTv.setText(data.getComments() + "");
        Glide.with(this).load(data.getAu_logo())
                .error(R.drawable.default_user_header)
                .into(mUserHeaderIv);
        mLikeNumberTv.setText(data.getLikes() + "");
        mCommentContentTv.setText(data.getAc_content());
        mTimeTv.setText(data.aac_ctime);
        mLikeCommentCb.setChecked(data.is_like == 1);

        mLikeCommentCb.setOnClickListener(new View.OnClickListener() {
            @Override
            @CheckLogin
            public void onClick(View v) {
                mLikeCommentCb.setChecked(true);

                // 有没有被点赞，如果有不允许取消点赞
                if (data.is_like != 1) {
                    // 调用接口点赞
                    data.is_like = 1;
                    data.setLikes(data.getLikes() + 1);
                    mLikeNumberTv.setText(data.getLikes() + "");

                    GoHttp.create(CommentDetailActivity.this)
                            .url(Constant.UrlConstant.COMMENT_LIKE_URL)
                            .post()
                            .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                            .addParam("pid", data.getAc_id())
                            .execute(new HttpCallback<BaseResult>() {
                                @Override
                                protected void onSuccess(BaseResult result) {

                                }
                            });
                }
            }
        });

        if (data.getComment() == null || data.getComment().size() == 0) {
            mCommentRv.setVisibility(View.GONE);
            return;
        } else {
            mCommentRv.setVisibility(View.VISIBLE);
        }

        if (mAdapter == null) {
            mAdapter = new CommonRecyclerAdapter<CommentListResult.CommentBean>
                    (this, mComments, R.layout.item_second_comment_rv) {
                @Override
                public void convert(ViewHolder holder, final CommentListResult.CommentBean item, int position) {
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
                            mIsSecondComment = true;
                            mCommentName = item.getBhfz_nickname();
                            mSubmitCommentId = item.ac_id;
                            writeComment();
                        }
                    });
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
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, mRootView)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppManagerUtil.instance().finishActivity(CommentDetailActivity.this);
                    }
                })
                .setTitle("评论详情")
                .setLeftIcon(R.drawable.comment_back)
                .hideRightIcon()
                .create();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_comment_detail);
    }

    @OnClick(R.id.write_comment)
    private void writeCommentClick() {
        mIsSecondComment = false;
        mSubmitCommentId = mCommentId;
        writeComment();
    }

    @CheckLogin
    private void writeComment() {
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
                            GeneralUtil.hideSoftInputFromWindow(getWindow().getDecorView());
                        }
                    })
                    .create();
        }
        mCommentDialog.show();
        final EditText commentEt = mCommentDialog.getView(R.id.comment_content_et);
        if (mIsSecondComment) {
            commentEt.setHint("回复：" + mCommentName);
        } else {
            commentEt.setHint("优质评论将会被优先展示");
        }

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
        GoHttp.create(this).post()
                .url(Constant.UrlConstant.URL_BASE + "add_comment")
                .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                .addParam("content", commentContent)
                .addParam("pid", mSubmitCommentId)
                .execute(new HttpCallback<BaseResult>() {

                    @Override
                    public void onPreExecute() {
                        DialogUitl.showProgressDialog(CommentDetailActivity.this, "请稍后...");
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
}
