package com.darren.chinesebrand.framework;

import android.widget.Toast;

import com.darren.chinesebrand.base.BaseActivity;

/**
 * description:
 * author: Darren on 2017/11/14 09:22
 * email: 240336124@qq.com
 * version: 1.0
 */
public abstract class BrandBaseActivity extends BaseActivity {
    public void showToast(CharSequence text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
