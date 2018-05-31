package com.darren.chinesebrand.base.http.callback;

import java.io.InputStream;

/**
 * description:
 * author: Darren on 2017/10/11 15:20
 * email: 240336124@qq.com
 * version: 1.0
 */
public interface EngineDownloadCallback {
    EngineDownloadCallback DEFAULT_CALL_BACK = new EngineDownloadCallback() {
        public void onPreExecute() {
        }

        public void onError(Exception e) {
        }

        public void onResponse(InputStream is, long length) {
        }
    };

    void onPreExecute();

    void onError(Exception e);

    void onResponse(InputStream inputStream, long contentLength);
}
