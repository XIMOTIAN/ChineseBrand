package com.darren.chinesebrand.base.http.callback;

/**
 * description: 一般请求回调
 * author: Darren on 2017/10/11 15:20
 * email: 240336124@qq.com
 * version: 1.0
 */
public interface EngineCallback {
    EngineCallback DEFAULT_CALL_BACK = new EngineCallback() {
        public void onPreExecute() {

        }

        public void onError(Exception e) {

        }

        public void onSuccess(String result) {

        }
    };

    void onPreExecute();

    void onError(Exception e);

    void onSuccess(String result);
}
