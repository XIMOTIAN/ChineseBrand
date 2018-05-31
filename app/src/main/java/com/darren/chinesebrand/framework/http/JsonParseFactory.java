package com.darren.chinesebrand.framework.http;

import com.darren.chinesebrand.base.http.Converter;
import com.google.gson.Gson;

/**
 * description:
 * author: Darren on 2017/10/18 10:23
 * email: 240336124@qq.com
 * version: 1.0
 */
public class JsonParseFactory extends Converter.Factory {
    @Override
    public <T> Converter<T, String> responseConverter(final Class<T> type) {
        return new Converter<T, String>() {
            @Override
            public T convert(String value) {
                return new Gson().fromJson(value, type);
            }
        };
    }

    private JsonParseFactory() {

    }

    public static JsonParseFactory create() {
        return new JsonParseFactory();
    }
}
