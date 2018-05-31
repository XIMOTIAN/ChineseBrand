package com.darren.chinesebrand.base.http;

import android.content.Context;
import android.text.TextUtils;

import com.darren.chinesebrand.base.http.callback.EngineCallback;
import com.darren.chinesebrand.base.http.callback.EngineDownloadCallback;
import com.darren.chinesebrand.base.http.callback.EngineUploadCallback;

import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * author: Darren on 2017/10/11 15:18
 * email: 240336124@qq.com
 * version: 1.0
 */
public class GoHttp {
    // 参数
    private Map<String, Object> mParams;
    // 默认的 HttpEngine
    private static GoHttpConfig mDefaultConfig = null;
    // 请求的 HttpEngine
    private IHttpEngine mHttpEngine = null;
    // 上下文
    private Context mContext;
    // 是否缓存
    private boolean mCache = false;
    // 接口地址路径
    private String mUrl;
    // 提交的类型
    private int mType = POST_TYPE;
    private static final int POST_TYPE = 0x0011;
    private static final int GET_TYPE = 0x0022;

    private GoHttp(Context context) {
        this.mContext = context;
        this.mParams = new HashMap();
    }

    public static GoHttp create(Context context) {
        return new GoHttp(context);
    }

    public GoHttp url(String url) {
        this.mUrl = url;
        return this;
    }

    public GoHttp post() {
        this.mType = POST_TYPE;
        return this;
    }

    public GoHttp get() {
        this.mType = GET_TYPE;
        return this;
    }

    public GoHttp cache(boolean isCache) {
        this.mCache = isCache;
        return this;
    }

    public GoHttp addParam(String key, Object value) {
        this.mParams.put(key, value);
        return this;
    }

    public GoHttp addParams(Map<String, Object> params) {
        this.mParams.putAll(params);
        return this;
    }

    /**
     * 上传文件
     *
     * @param callBack
     */
    public void upload(EngineUploadCallback callBack) {
        if (callBack == null) {
            callBack = EngineUploadCallback.DEFAULT_CALL_BACK;
        }

        onPrepare();

        if (this.mHttpEngine == null) {
            throw new NullPointerException("第三方的引擎为空，请在Application中初始化！");
        } else {
            callBack.onPreExecute();
            this.mHttpEngine.upload(this.mContext, this.mUrl, this.mParams, callBack);
        }
    }

    /**
     * 初始化加入一些参数和网络引擎
     */
    private void onPrepare() {
        if (TextUtils.isEmpty(mUrl)) {
            throw new NullPointerException("url 是空！");
        }

        if (mDefaultConfig != null) {
            if (mHttpEngine == null) {
                mHttpEngine = mDefaultConfig.getHttpEngine();
            }
            mParams.putAll(mDefaultConfig.getPublicParams());
        }

        if (this.mHttpEngine == null) {
            throw new NullPointerException("第三方的引擎为空，请在Application中初始化！");
        }
    }

    public void execute(EngineCallback callBack) {
        if (callBack == null) {
            callBack = EngineCallback.DEFAULT_CALL_BACK;
        }
        onPrepare();
        callBack.onPreExecute();
        if (this.mType == POST_TYPE) {
            mHttpEngine.post(mCache, mContext, this.mUrl, this.mParams, callBack);
        } else if (this.mType == GET_TYPE) {
            mHttpEngine.get(mCache, mContext, this.mUrl, this.mParams, callBack);
        }
    }

    public void execute() {
        this.execute(null);
    }

    public static void config(GoHttpConfig config) {
        mDefaultConfig = config;
    }

    public GoHttp engine(IHttpEngine httpEngine) {
        this.mHttpEngine = httpEngine;
        return this;
    }

    public void download(EngineDownloadCallback downLoadCallBack) {
        if (downLoadCallBack == null) {
            downLoadCallBack = EngineDownloadCallback.DEFAULT_CALL_BACK;
        }
        onPrepare();
        downLoadCallBack.onPreExecute();
        this.mHttpEngine.download(this.mUrl, downLoadCallBack);
    }

    public static GoHttpConfig getDefaultConfig() {
        return mDefaultConfig;
    }
}
