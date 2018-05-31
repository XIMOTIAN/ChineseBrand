package com.darren.chinesebrand.base.http.callback;


import com.darren.chinesebrand.base.http.Converter;
import com.darren.chinesebrand.base.http.GoHttp;
import com.darren.chinesebrand.base.http.Utils;

/**
 * description:
 * author: Darren on 2017/10/11 16:13
 * email: 240336124@qq.com
 * version: 1.0
 */
public abstract class HttpUploadCallback<T> implements EngineUploadCallback {
    @Override
    public void onPreExecute() {

    }

    @Override
    public void onError(Exception e) {
        onNetError(e);
    }

    /**
     * 网络错误
     *
     * @param e
     */
    private void onNetError(Exception e) {

    }

    @Override
    public void onSuccess(String resultStr) {
        Converter.Factory converterFactory = GoHttp.getDefaultConfig().getFactory();
        if (converterFactory == null) {
            serverTransformError(new ServerDataTransformException("数据解析转换错误，请在GoHttp中配置解析工厂"));
        }
        // 解析
        try {
            Class<T> type = (Class<T>) Utils.analysisClazzInfo(this);
            Converter<T, String> converter = converterFactory.responseConverter(type);
            T result = converter.convert(resultStr);
            onSuccess(result);
        } catch (Exception e) {
            serverTransformError(new ServerDataTransformException(e));
        }
    }

    protected void serverTransformError(ServerDataTransformException e) {

    }

    /**
     * 成功直接返回对象
     *
     * @param result
     */
    protected abstract void onSuccess(T result);
}
