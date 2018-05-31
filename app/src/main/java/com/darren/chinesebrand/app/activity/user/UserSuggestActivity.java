package com.darren.chinesebrand.app.activity.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.app.util.DialogUitl;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.OnClick;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.http.BaseResult;
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.StatusBarUtil;

/**
 * description: 意见反馈
 * created by darren on 2017/11/26 14:03
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserSuggestActivity extends BrandBaseActivity {
    /****可输入300字......****/
    @ViewById(R.id.suggest_content_et)
    private EditText mSuggestContentEt;
    /****提 交****/
    @ViewById(R.id.submit_bt)
    private Button mSubmitBt;
    @ViewById(R.id.root_view)
    private LinearLayout mRootView;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initTitle() {
        StatusBarUtil.setStatusBarTrans(this, true);
        DefaultNavigationBar navigationBar = new DefaultNavigationBar.Builder(this, mRootView)
                .setLeftClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppManagerUtil.instance().finishActivity(UserSuggestActivity.this);
                    }
                })
                .setTitle("意见反馈")
                .setLeftIcon(R.drawable.comment_back)
                .hideRightIcon()
                .create();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_suggest);
    }

    @OnClick(R.id.submit_bt)
    private void submitBtClick() {
        String content = mSuggestContentEt.getText().toString().trim();

        if(TextUtils.isEmpty(content)){
            showToast(mSuggestContentEt.getHint());
            return;
        }

        GoHttp.create(this)
                .url(Constant.UrlConstant.USER_SUGGEST_URL)
                .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                .addParam("content",content)
                .post()
                .execute(new HttpCallback<BaseResult>() {
                    @Override
                    public void onPreExecute() {
                        DialogUitl.showProgressDialog(UserSuggestActivity.this,"请稍后...");
                    }

                    @Override
                    protected void onSuccess(BaseResult result) {
                        DialogUitl.dismissProgressDialog();

                        if(result.isOk()){
                            AppManagerUtil.instance().finishActivity(UserSuggestActivity.this);
                        }

                        showToast(result.msg);
                    }
                });
    }
}
