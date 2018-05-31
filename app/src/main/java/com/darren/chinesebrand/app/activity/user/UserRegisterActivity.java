package com.darren.chinesebrand.app.activity.user;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.ui.VerificationCodeButton;
import com.darren.chinesebrand.app.util.DialogUitl;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.http.callback.ServerDataTransformException;
import com.darren.chinesebrand.base.inject.OnClick;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.http.BaseResult;
import com.darren.chinesebrand.framework.util.GeneralUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;

import io.reactivex.functions.Consumer;

/**
 * description:
 * author: Darren on 2017/11/20 09:36
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserRegisterActivity extends BrandBaseActivity {

    /****请输入手机号****/
    @ViewById(R.id.user_name_et)
    private EditText mUserNameEt;
    @ViewById(R.id.clear_user_name)
    private ImageView mClearUserName;
    /****请输入验证码****/
    @ViewById(R.id.user_pwd_et)
    private EditText mUserPwdEt;
    /****请输入验证码****/
    @ViewById(R.id.user_code_et)
    private EditText mUserCodeEt;
    /****发送验证码****/
    @ViewById(R.id.send_code)
    private VerificationCodeButton mSendCode;

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        RxTextView.textChanges(mUserNameEt).subscribe(new Consumer<CharSequence>() {
            @Override
            public void accept(CharSequence text) throws Exception {
                mClearUserName.setVisibility(TextUtils.isEmpty(text) ? View.INVISIBLE : View.VISIBLE);
            }
        });
    }

    @Override
    protected void initTitle() {

    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_user_register);
    }

    @OnClick(R.id.delete_activity)
    private void finishActivity() {
        AppManagerUtil.instance().finishActivity(this);
    }


    @OnClick(R.id.clear_user_name)
    private void clearUserNameClick() {
        mUserNameEt.setText("");
    }


    @OnClick(R.id.user_register_bt)
    private void userRegisterBtClick() {
        String userName = mUserNameEt.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            showToast(mUserNameEt.getHint().toString());
            return;
        }

        userName = GeneralUtil.removeAllSpace(userName);
        if (!GeneralUtil.judgePhoneQual(userName)) {
            showToast("请输入正确的手机号码");
            return;
        }

        String userCode = mUserCodeEt.getText().toString().trim();
        if (TextUtils.isEmpty(userCode)) {
            showToast(mUserCodeEt.getHint().toString());
            return;
        }

        String password = mUserPwdEt.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            showToast(mUserPwdEt.getHint().toString());
            return;
        }

        GoHttp.create(this)
                .url(Constant.UrlConstant.USER_REGISTER_URL)
                .addParam("code", userCode)
                .addParam("phone", userName)
                .addParam("password", password)
                .post()
                .execute(new HttpCallback<BaseResult>() {
                    @Override
                    public void onPreExecute() {
                        DialogUitl.showProgressDialog(UserRegisterActivity.this, "请稍后...");
                    }

                    @Override
                    public void onError(Exception e) {
                        showToast(getString(R.string.net_error_tip));
                        DialogUitl.dismissProgressDialog();
                    }

                    @Override
                    protected void onSuccess(BaseResult result) {
                        DialogUitl.dismissProgressDialog();
                        if (result.isOk()) {
                            AppManagerUtil.instance().finishActivity(UserRegisterActivity.this);
                            showToast("注册成功，请登录");
                        } else {
                            showToast(result.msg);
                        }
                    }
                });
    }

    @OnClick(R.id.goto_login)
    private void gotoLoginClick() {
        AppManagerUtil.instance().finishActivity(UserLoginActivity.class);
        AppManagerUtil.instance().finishActivity(this);
        startActivity(UserLoginActivity.class);
    }

    @OnClick(R.id.send_code)
    private void sendCodeClick() {
        String userName = mUserNameEt.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            showToast(mUserNameEt.getHint().toString());
            return;
        }

        userName = GeneralUtil.removeAllSpace(userName);
        if (!GeneralUtil.judgePhoneQual(userName)) {
            showToast("请输入正确的手机号码");
            return;
        }

        GoHttp.create(this).url(Constant.UrlConstant.SEND_CODE_URL)
                .addParam("iphone", userName)
                .post()
                .execute(new HttpCallback<BaseResult>() {
                    @Override
                    public void onPreExecute() {
                        // 设置按钮为请稍后
                        mSendCode.waitSend();
                    }

                    @Override
                    public void onError(Exception e) {
                        showToast(getString(R.string.net_error_tip));
                        mSendCode.restoreDefault();
                    }

                    @Override
                    protected void serverTransformError(ServerDataTransformException e) {
                        showToast(getString(R.string.net_error_tip));
                        mSendCode.restoreDefault();
                    }

                    @Override
                    protected void onSuccess(BaseResult result) {
                        if (result.isOk()) {
                            showToast("验证码已发送，请留意您的短信");
                            mSendCode.startCountDown("%02ds", 60);
                        } else {
                            showToast(result.msg);
                            mSendCode.restoreDefault();
                        }
                    }
                });
    }
}
