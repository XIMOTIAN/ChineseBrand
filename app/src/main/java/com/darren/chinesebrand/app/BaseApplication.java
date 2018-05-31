package com.darren.chinesebrand.app;

import android.app.Application;

import com.darren.chinesebrand.base.dao.PreferencesUtil;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.GoHttpConfig;
import com.darren.chinesebrand.framework.http.JsonParseFactory;
import com.darren.chinesebrand.framework.http.OkHttpEngine;
import com.darren.sharecomponent.ShareApplication;
import com.squareup.leakcanary.LeakCanary;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * author: Darren on 2017/11/14 10:02
 * email: 240336124@qq.com
 * version: 1.0
 */
public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);

        PreferencesUtil.getInstance().init(this);

        Map<String,Object> publicParams = new HashMap<>();
        publicParams.put("platform","android");
        GoHttpConfig httpConfig = new GoHttpConfig.Builder()
                .addConverterFactory(JsonParseFactory.create())
                .publicParams(publicParams)
                .engine(new OkHttpEngine())
                .build();
        GoHttp.config(httpConfig);

        // 第三方登录和分享
        ShareApplication.attach(this);


    }
}
