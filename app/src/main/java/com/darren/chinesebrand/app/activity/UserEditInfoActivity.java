package com.darren.chinesebrand.app.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.darren.chinesebrand.R;
import com.darren.chinesebrand.app.http.Constant;
import com.darren.chinesebrand.app.http.response.UserLoginResult;
import com.darren.chinesebrand.app.ui.loopview.LoopView;
import com.darren.chinesebrand.app.ui.loopview.OnItemSelectedListener;
import com.darren.chinesebrand.app.ui.loopview.WheelAdapter;
import com.darren.chinesebrand.app.user.LoginSession;
import com.darren.chinesebrand.app.user.UserData;
import com.darren.chinesebrand.app.util.DateUtil;
import com.darren.chinesebrand.base.AppManagerUtil;
import com.darren.chinesebrand.base.dao.PlaceUtil;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.callback.HttpCallback;
import com.darren.chinesebrand.base.inject.OnClick;
import com.darren.chinesebrand.base.inject.ViewById;
import com.darren.chinesebrand.framework.BrandBaseActivity;
import com.darren.chinesebrand.framework.dialog.CommonDialog;
import com.darren.chinesebrand.framework.http.BaseResult;
import com.darren.chinesebrand.framework.toolbar.DefaultNavigationBar;
import com.darren.chinesebrand.framework.util.GeneralUtil;
import com.darren.chinesebrand.framework.util.StatusBarUtil;
import com.hc.imageselectorlib.MultiImageSelector;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.functions.Consumer;

import static com.hc.imageselectorlib.MultiImageSelectorActivity.EXTRA_RESULT;

/**
 * description: 用户编辑资料界面
 * author: Darren on 2017/11/27 09:32
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserEditInfoActivity extends BrandBaseActivity {
    @ViewById(R.id.root_view)
    private ViewGroup mRootView;
    @ViewById(R.id.user_header)
    private CircleImageView mUserHeader;

    private RxPermissions mRxPermissions;

    private static final int SELECT_IMAGE_REQUEST = 0x0011;
    /****未知****/
    @ViewById(R.id.user_sex)
    private TextView mUserSex;
    /****940223****/
    @ViewById(R.id.user_birthday)
    private TextView mUserBirthday;
    /****湖南长沙****/
    @ViewById(R.id.user_location)
    private TextView mUserLocation;
    /****240336124@qq.com****/
    @ViewById(R.id.user_email)
    private TextView mUserEmail;
    /****Darren****/
    @ViewById(R.id.nick_name)
    private TextView mNickName;

    private CommonDialog mEditNickNameDialog, mEditSexDialog, mEditEmailDialog, mEditPhoneDialog, mBirthdayDialog, mLocationDialog;
    private Handler mHandler = new Handler();
    /****14726932514****/
    @ViewById(R.id.user_phone)
    private TextView mUserPhone;

    @Override
    protected void initData() {
        mRxPermissions = new RxPermissions(this);

        requestUserInfo();
    }

    /**
     * 请求用户信息
     */
    private void requestUserInfo() {
        GoHttp.create(this).url(Constant.UrlConstant.USER_INFO_URL)
                .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                .post()
                .cache(true)
                .execute(new HttpCallback<UserLoginResult>() {
                    @Override
                    protected void onSuccess(UserLoginResult result) {
                        if (result.isOk()) {
                            showUserInfo(result.data);
                        }
                    }
                });
    }

    private void showUserInfo(UserData data) {
        Glide.with(this)
                .load(data.au_logo)
                .error(R.drawable.default_user_header)
                .into(mUserHeader);

        mNickName.setText(data.au_nickname);
        mUserBirthday.setText(data.au_birthday);
        mUserEmail.setText(data.au_email + "");
        mUserLocation.setText(data.au_place);
        mUserSex.setText(data.au_sex);
        mUserPhone.setText(data.au_iphone);
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
                        AppManagerUtil.instance().finishActivity(UserEditInfoActivity.this);
                    }
                })
                .setTitle("编辑资料")
                .setLeftIcon(R.drawable.comment_back)
                .hideRightIcon()
                .create();
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_edit_user_info);
    }

    @OnClick(R.id.user_header)
    private void userHeaderClick() {
        mRxPermissions.request(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
                        if (aBoolean) {
                            MultiImageSelector.create().single().start(UserEditInfoActivity.this, SELECT_IMAGE_REQUEST);
                        } else {
                            showToast("当前用户未授权");
                        }
                    }
                });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK) {
            ArrayList<String> resultList = data.getStringArrayListExtra(EXTRA_RESULT);
            if (resultList != null && !resultList.isEmpty()) {
                String filePath = resultList.get(0);
                Glide.with(this).load(filePath).diskCacheStrategy(DiskCacheStrategy.NONE)
                        .signature(new StringSignature(UUID.randomUUID().toString())).into(mUserHeader);

                uploadHeaderFile(filePath);
            }
        }
    }


    /**
     * 更改用户图像
     *
     * @param filePath
     */
    private void uploadHeaderFile(String filePath) {
        File file = new File(filePath);

        GoHttp.create(this)
                .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                .addParam("file", file)
                .post()
                .url(Constant.UrlConstant.EDIT_USER_HEADER)
                .execute(new HttpCallback<BaseResult>() {

                    @Override
                    public void onError(Exception e) {
                        showToast(getString(R.string.net_error_tip));
                    }

                    @Override
                    protected void onSuccess(BaseResult result) {
                        showToast(result.msg);
                    }
                });
    }

    @OnClick(R.id.nick_name_ll)
    private void nickNameLlClick() {
        // 显示修改昵称的Dialog
        if (mEditNickNameDialog == null) {
            mEditNickNameDialog = new CommonDialog.Builder(this)
                    .setView(R.layout.dialog_write_username)
                    .setOnClickListener(R.id.sure_bt, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText nickNameEt = mEditNickNameDialog.getView(R.id.nickname_et);
                            String nickNameStr = nickNameEt.getText().toString().trim();
                            if (TextUtils.isEmpty(nickNameStr)) {
                                showToast(nickNameEt.getHint());
                                return;
                            }

                            mNickName.setText(nickNameStr);
                            mEditNickNameDialog.dismiss();
                            GoHttp.create(UserEditInfoActivity.this)
                                    .url(Constant.UrlConstant.EDIT_INFO_URL)
                                    .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                                    .addParam("username", nickNameStr)
                                    .execute();
                        }
                    })
                    .fromBottom()
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            GeneralUtil.hideSoftInputFromWindow(mRootView);
                        }
                    })
                    .fullWidth()
                    .create();

            final EditText nickNameEt = mEditNickNameDialog.getView(R.id.nickname_et);
            nickNameEt.setText(mNickName.getText().toString().trim());
            GeneralUtil.cursorToEnd(nickNameEt);

            final ImageView clearContent = mEditNickNameDialog.getView(R.id.clear_content);
            RxTextView.textChanges(nickNameEt).subscribe(new Consumer<CharSequence>() {
                @Override
                public void accept(CharSequence text) throws Exception {
                    clearContent.setVisibility(TextUtils.isEmpty(text) ? View.INVISIBLE : View.VISIBLE);
                }
            });

            clearContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nickNameEt.setText("");
                }
            });
        }

        mEditNickNameDialog.show();
        final EditText nickNameEt = mEditNickNameDialog.getView(R.id.nickname_et);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 弹出键盘
                //设置可获得焦点
                nickNameEt.setFocusable(true);
                nickNameEt.setFocusableInTouchMode(true);
                //请求获得焦点
                nickNameEt.requestFocus();
                GeneralUtil.showSoftInputFromWindow(nickNameEt);
            }
        }, 200);
    }

    @OnClick(R.id.user_sex_ll)
    private void userSexLlClick() {

        if (mEditSexDialog == null) {
            mEditSexDialog = new CommonDialog.Builder(this)
                    .setView(R.layout.dialog_write_usersex)
                    .fromBottom()
                    .fullWidth()
                    .create();

            final RadioButton boyCb = mEditSexDialog.getView(R.id.sex_boy);
            final RadioButton girlCb = mEditSexDialog.getView(R.id.sex_girl);
            if (boyCb.getText().toString().equals(mUserSex.getText().toString())) {
                boyCb.setChecked(true);
            } else if (girlCb.getText().toString().equals(mUserSex.getText().toString())) {
                girlCb.setChecked(true);
            }

            mEditSexDialog.setOnClickListener(R.id.sure_bt, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String sex = boyCb.isChecked() ? "男" : girlCb.isChecked() ? "女" : "保密";
                    mUserSex.setText(sex);

                    mEditSexDialog.dismiss();

                    GoHttp.create(UserEditInfoActivity.this)
                            .url(Constant.UrlConstant.EDIT_INFO_URL)
                            .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                            .addParam("sex", "男".equals(sex) ? 1 : "女".equals(sex) ? 2 : 3)
                            .execute();
                }
            });
        }

        mEditSexDialog.show();
    }

    @OnClick(R.id.user_email_ll)
    private void userEmailLlClick() {
        if (mEditEmailDialog == null) {
            mEditEmailDialog = new CommonDialog.Builder(this)
                    .setView(R.layout.dialog_write_username)
                    .setOnClickListener(R.id.sure_bt, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText nickNameEt = mEditEmailDialog.getView(R.id.nickname_et);
                            String nickNameStr = nickNameEt.getText().toString().trim();
                            if (TextUtils.isEmpty(nickNameStr)) {
                                showToast(nickNameEt.getHint());
                                return;
                            }
                            if (!GeneralUtil.judgeEmailQual(nickNameStr)) {
                                showToast("请输入正确的邮箱格式");
                                return;
                            }

                            mUserEmail.setText(nickNameStr);
                            mEditEmailDialog.dismiss();
                            GoHttp.create(UserEditInfoActivity.this)
                                    .url(Constant.UrlConstant.EDIT_INFO_URL)
                                    .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                                    .addParam("email", nickNameStr)
                                    .execute();
                        }
                    })
                    .fromBottom()
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            GeneralUtil.hideSoftInputFromWindow(mRootView);
                        }
                    })
                    .fullWidth()
                    .create();

            final EditText nickNameEt = mEditEmailDialog.getView(R.id.nickname_et);

            nickNameEt.setHint("请输入邮箱");
            nickNameEt.setText(mUserEmail.getText().toString().trim());
            GeneralUtil.cursorToEnd(nickNameEt);

            final ImageView clearContent = mEditEmailDialog.getView(R.id.clear_content);
            RxTextView.textChanges(nickNameEt).subscribe(new Consumer<CharSequence>() {
                @Override
                public void accept(CharSequence text) throws Exception {
                    clearContent.setVisibility(TextUtils.isEmpty(text) ? View.INVISIBLE : View.VISIBLE);
                }
            });

            clearContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nickNameEt.setText("");
                }
            });
        }

        mEditEmailDialog.show();
        final EditText nickNameEt = mEditEmailDialog.getView(R.id.nickname_et);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 弹出键盘
                //设置可获得焦点
                nickNameEt.setFocusable(true);
                nickNameEt.setFocusableInTouchMode(true);
                //请求获得焦点
                nickNameEt.requestFocus();
                GeneralUtil.showSoftInputFromWindow(nickNameEt);
            }
        }, 200);
    }

    @OnClick(R.id.user_phone_ll)
    private void userPhoneLlClick() {
        if (!TextUtils.isEmpty(mUserPhone.getText().toString().trim())) {
            return;
        }
        if (mEditPhoneDialog == null) {
            mEditPhoneDialog = new CommonDialog.Builder(this)
                    .setView(R.layout.dialog_write_username)
                    .setOnClickListener(R.id.sure_bt, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EditText nickNameEt = mEditPhoneDialog.getView(R.id.nickname_et);
                            String nickNameStr = nickNameEt.getText().toString().trim();
                            if (TextUtils.isEmpty(nickNameStr)) {
                                showToast(nickNameEt.getHint());
                                return;
                            }
                            if (!GeneralUtil.judgePhoneQual(nickNameStr)) {
                                showToast("请输入正确的手机号码");
                                return;
                            }

                            mUserPhone.setText(nickNameStr);
                            mEditPhoneDialog.dismiss();
                            GoHttp.create(UserEditInfoActivity.this)
                                    .url(Constant.UrlConstant.EDIT_INFO_URL)
                                    .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                                    .addParam("phone", nickNameStr)
                                    .execute();
                        }
                    })
                    .fromBottom()
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            GeneralUtil.hideSoftInputFromWindow(mRootView);
                        }
                    })
                    .fullWidth()
                    .create();

            final EditText nickNameEt = mEditPhoneDialog.getView(R.id.nickname_et);
            nickNameEt.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
            nickNameEt.setLines(1);
            nickNameEt.setHint("请输入手机号");
            nickNameEt.setText(mUserPhone.getText().toString().trim());
            GeneralUtil.cursorToEnd(nickNameEt);

            final ImageView clearContent = mEditPhoneDialog.getView(R.id.clear_content);
            RxTextView.textChanges(nickNameEt).subscribe(new Consumer<CharSequence>() {
                @Override
                public void accept(CharSequence text) throws Exception {
                    clearContent.setVisibility(TextUtils.isEmpty(text) ? View.INVISIBLE : View.VISIBLE);
                }
            });

            clearContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    nickNameEt.setText("");
                }
            });
        }

        mEditPhoneDialog.show();
        final EditText nickNameEt = mEditPhoneDialog.getView(R.id.nickname_et);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // 弹出键盘
                //设置可获得焦点
                nickNameEt.setFocusable(true);
                nickNameEt.setFocusableInTouchMode(true);
                //请求获得焦点
                nickNameEt.requestFocus();
                GeneralUtil.showSoftInputFromWindow(nickNameEt);
            }
        }, 200);
    }

    @OnClick(R.id.user_birthday_ll)
    private void userBirthdayLlClick() {
        if (mBirthdayDialog == null) {
            mBirthdayDialog = new CommonDialog.Builder(this)
                    .setView(R.layout.dialog_write_userbirthday)
                    .fromBottom().fullWidth()
                    .create();
            // 年
            final LoopView loopYear = mBirthdayDialog.getView(R.id.loop_year);
            loopYear.setNotLoop();
            final List<String> years = new ArrayList<>();
            for (int i = 1970; i <= DateUtil.currentYear(); i++) {
                years.add(i + "");
            }
            loopYear.setAdapter(new WheelAdapter() {
                @Override
                public int getCount() {
                    return years.size();
                }

                @Override
                public String getItem(int position) {
                    return years.get(position);
                }
            });
            loopYear.setCurrentPosition(1990 - 1970);


            // 月
            final LoopView loopMonth = mBirthdayDialog.getView(R.id.loop_month);
            loopMonth.setNotLoop();
            final List<String> month = new ArrayList<>();
            for (int i = 1; i <= 12; i++) {
                month.add(GeneralUtil.addZeroForNum(i + "", 2));
            }
            loopMonth.setAdapter(new WheelAdapter() {
                @Override
                public int getCount() {
                    return month.size();
                }

                @Override
                public String getItem(int position) {
                    return month.get(position);
                }
            });

            // 日
            final LoopView loopDay = mBirthdayDialog.getView(R.id.loop_day);
            loopDay.setNotLoop();
            final List<String> days = new ArrayList<>();
            for (int i = 1; i <= DateUtil.daysByYearMonth(1990, 1); i++) {
                days.add(GeneralUtil.addZeroForNum(i + "", 2));
            }
            loopDay.setAdapter(new WheelAdapter() {
                @Override
                public int getCount() {
                    return days.size();
                }

                @Override
                public String getItem(int position) {
                    return days.get(position);
                }
            });
            // 不同年月日改变天数
            loopMonth.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    days.clear();

                    for (int i = 1; i <= DateUtil.daysByYearMonth(loopYear.getSelectedItem() + 1970, index + 1); i++) {
                        days.add(GeneralUtil.addZeroForNum(i + "", 2));
                    }

                    loopDay.setAdapter(new WheelAdapter() {
                        @Override
                        public int getCount() {
                            return days.size();
                        }

                        @Override
                        public String getItem(int position) {
                            return days.get(position);
                        }
                    });
                    loopDay.setCurrentPosition(0);
                }
            });

            mBirthdayDialog.setOnClickListener(R.id.sure_bt, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 提交生日
                    String birthDay = years.get(loopYear.getSelectedItem())
                            + "-" + month.get(loopMonth.getSelectedItem())
                            + "-" + days.get(loopDay.getSelectedItem());
                    mUserBirthday.setText(birthDay);
                    mBirthdayDialog.dismiss();
                    GoHttp.create(UserEditInfoActivity.this)
                            .url(Constant.UrlConstant.EDIT_INFO_URL)
                            .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                            .addParam("birthday", birthDay)
                            .execute();
                }
            });
        }

        mBirthdayDialog.show();
    }

    @OnClick(R.id.user_location_ll)
    private void userLocationLlClick() {
        if (mLocationDialog == null) {
            mLocationDialog = new CommonDialog.Builder(this)
                    .setView(R.layout.dialog_write_userbirthday)
                    .fromBottom().fullWidth()
                    .create();
            // 年
            final LoopView loopYear = mLocationDialog.getView(R.id.loop_year);
            loopYear.setVisibility(View.GONE);

            // 月
            final LoopView loopMonth = mLocationDialog.getView(R.id.loop_month);
            loopMonth.setNotLoop();
            final List<PlaceUtil.Province> provinces = PlaceUtil.getProvince(this);

            loopMonth.setAdapter(new WheelAdapter() {
                @Override
                public int getCount() {
                    return provinces.size();
                }

                @Override
                public String getItem(int position) {
                    return provinces.get(position).name;
                }
            });

            // 日
            final LoopView loopDay = mLocationDialog.getView(R.id.loop_day);
            loopDay.setNotLoop();
            final List<String> citys = PlaceUtil.getCityByProvinceId(this, provinces.get(0).id);

            loopDay.setAdapter(new WheelAdapter() {
                @Override
                public int getCount() {
                    return citys.size();
                }

                @Override
                public String getItem(int position) {
                    return citys.get(position);
                }
            });
            // 不同年月日改变天数
            loopMonth.setListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(int index) {
                    citys.clear();
                    citys.addAll(PlaceUtil.getCityByProvinceId(UserEditInfoActivity.this, provinces.get(index).id));
                    loopDay.setAdapter(new WheelAdapter() {
                        @Override
                        public int getCount() {
                            return citys.size();
                        }

                        @Override
                        public String getItem(int position) {
                            return citys.get(position);
                        }
                    });
                    loopDay.setCurrentPosition(0);
                }
            });

            mLocationDialog.setOnClickListener(R.id.sure_bt, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 提交生日
                    String location = provinces.get(loopMonth.getSelectedItem()).name
                            + "-" + citys.get(loopDay.getSelectedItem());
                    mUserLocation.setText(location);
                    mLocationDialog.dismiss();
                    GoHttp.create(UserEditInfoActivity.this)
                            .url(Constant.UrlConstant.EDIT_INFO_URL)
                            .addParam("token", LoginSession.getLoginSession().getUserInfo().token)
                            .addParam("place", location)
                            .execute();
                }
            });
        }
        mLocationDialog.show();
    }
}
