package com.darren.chinesebrand.base.http.callback;

/**
 * description:
 * author: Darren on 2017/10/11 15:20
 * email: 240336124@qq.com
 * version: 1.0
 */
public interface EngineUploadCallback {
    EngineUploadCallback DEFAULT_CALL_BACK = new EngineUploadCallback() {
        public void onPreExecute() {

        }

        public void onError(Exception e) {

        }

        public void onComplete() {

        }

        public void onSuccess(String result) {

        }

        public void onResponse(long current, long total) {

        }
    };

    void onPreExecute();

    void onError(Exception e);

    void onComplete();

    void onSuccess(String result);

    void onResponse(long current, long total);
}
