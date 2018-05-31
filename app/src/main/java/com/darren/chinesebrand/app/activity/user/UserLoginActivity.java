package com.darren.chinesebrand.app.activity.user;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.activity.UserProtocolActivity;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.UserLoginResult;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.app.util.DialogUitl;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.OnClick;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.log.LogUtils;
import com.darren.chinesebrand.framework.util.GeneralUtil;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;
import java.util.Map;

import io.reactivex.functions.Consumer;

/**
 * description:
 * author: Darren on 2017/11/20 09:36
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserLoginActivity extends BrandBaseActivity implements UMAuthListener {

    /****请输入手机号****/
    @ViewById(R.id.user_name_et)
    private EditText mUserNameEt;
    @ViewById(R.id.clear_user_name)
    private ImageView mClearUserName;
    /****请输入密码****/
    @ViewById(R.id.user_pwd_et)
    private EditText mUserPwdEt;
    /****登录****/
    @ViewById(R.id.user_login_bt)
    private Button mUserLoginBt;
    @ViewById(R.id.wx_login)
    private ImageView mWxLogin;
    @ViewById(R.id.qq_login)
    private ImageView mQqLogin;
    @ViewById(R.id.zc_login)
    private ImageView mZcLogin;
    @ViewById(R.id.drawer_layout)
    private RelativeLayout mDrawerLayout;
    @ViewById(R.id.delete_activity)
    private ImageView mDeleteActivity;

    private UMShareAPI mThirdLoginAPI;

    @Override
    protected void initData() {
        // 获取 UMShareAPI
        mThirdLoginAPI = UMShareAPI.get(this);
        mThirdLoginAPI.deleteOauth(this, SHARE_MEDIA.WEIXIN, this);
        mThirdLoginAPI.deleteOauth(this, SHARE_MEDIA.QQ, this);
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
        setContentView(R.layout.activity_user_login);
    }

    @OnClick(R.id.delete_activity)
    private void finishActivity() {
        AppManagerUtil.instance().finishActivity(this);
    }

    @OnClick(R.id.user_login_bt)
    private void userLoginBtClick() {
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

        String password = mUserPwdEt.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            showToast(mUserPwdEt.getHint().toString());
            return;
        }

        GoHttp.create(this)
                .url(Constant.UrlConstant.USER_LOGIN_URL)
                .addParam("account", userName)
                .addParam("password", password)
                .post()
                .execute(new HttpCallback<UserLoginResult>() {
                    @Override
                    public void onPreExecute() {
                        DialogUitl.showProgressDialog(UserLoginActivity.this, "请稍后...");
                    }

                    @Override
                    public void onError(Exception e) {
                        showToast(getString(R.string.net_error_tip));
                        DialogUitl.dismissProgressDialog();
                    }

                    @Override
                    protected void onSuccess(UserLoginResult result) {
                        DialogUitl.dismissProgressDialog();
                        if (result.isOk()) {
                            AppManagerUtil.instance().finishActivity(UserLoginActivity.this);
                            LoginSession.getLoginSession().updateUser(result.data);
                        }
                        showToast(result.msg);
                    }
                });
    }

    @OnClick(R.id.wx_login)
    private void wxLoginClick() {
        if (!isInstallWX()) {
            showToast("未检测到微信");
            return;
        }

        mThirdLoginAPI.getPlatformInfo(this, SHARE_MEDIA.WEIXIN, this);
    }

    /**
     * 是否安装微信客户端
     *
     * @return
     */
    private boolean isInstallWX() {
        PackageManager packageManager = getPackageManager();// 获取packagemanager
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pn = packageInfos.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    @OnClick(R.id.qq_login)
    private void qqLoginClick() {
        if (!isQQClientAvailable()) {
            showToast("未检测到QQ");
            return;
        }
        mThirdLoginAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, this);
    }

    /**
     * 判断 用户是否安装QQ客户端
     */
    private boolean isQQClientAvailable() {
        final PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        if (packageInfos != null) {
            for (int i = 0; i < packageInfos.size(); i++) {
                String pn = packageInfos.get(i).packageName;
                if (pn.equalsIgnoreCase("com.tencent.qqlite") || pn.equalsIgnoreCase("com.tencent.mobileqq")) {
                    return true;
                }
            }
        }
        return false;
    }

    @OnClick(R.id.zc_login)
    private void zcLoginClick() {
        startActivity(UserRegisterActivity.class);
    }

    @OnClick(R.id.clear_user_name)
    private void clearUserNameClick() {
        mUserNameEt.setText("");
    }


    @OnClick(R.id.register_protocol)
    private void registerProtocolClick() {
        Intent intent = new Intent(this, UserProtocolActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.forget_password)
    private void forgetPasswordClick() {
        startActivity(UserForgetPwdActivity.class);
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return;
        }

        // 第三方登录授权成功
        DialogUitl.showProgressDialog(this, "请稍后...");


        LogUtils.e(map.toString());
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {

    }
}
