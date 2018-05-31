package com.darren.chinesebrand.base.http;

import android.content.Context;

import com.darren.chinesebrand.base.http.callback.EngineCallback;
import com.darren.chinesebrand.base.http.callback.EngineDownloadCallback;
import com.darren.chinesebrand.base.http.callback.EngineUploadCallback;

import java.util.Map;

/**
 * description:
 * author: Darren on 2017/10/11 15:19
 * email: 240336124@qq.com
 * version: 1.0
 */
public interface IHttpEngine {
    /**
     * get 请求
     * @param cache
     * @param context
     * @param url
     * @param params
     * @param callback
     */
    void get(boolean cache, Context context, String url, Map<String, Object> params, EngineCallback callback);

    /**
     * post 请求
     * @param cache
     * @param context
     * @param url
     * @param params
     * @param callback
     */
    void post(boolean cache, Context context, String url, Map<String, Object> params, EngineCallback callback);

    /**
     * 下载文件
     * @param downloadUrl
     * @param downloadCallback
     */
    void download(String downloadUrl, EngineDownloadCallback downloadCallback);

    /**
     * 上传文件
     * @param context
     * @param url
     * @param params
     * @param uploadCallback
     */
    void upload(Context context, String url, Map<String, Object> params, EngineUploadCallback uploadCallback);
}
