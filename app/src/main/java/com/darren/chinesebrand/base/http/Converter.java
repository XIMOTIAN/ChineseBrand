package com.darren.chinesebrand.base.http;

/**
 * description:
 * author: Darren on 2017/10/18 09:51
 * email: 240336124@qq.com
 * version: 1.0
 */
public interface Converter<T, F> {
    T convert(F value);

    /**
     * 具体的产品
     */
    abstract class Factory {
        // 类型转换
        public abstract <T> Converter<T, String> responseConverter(Class<T> type);
    }
}
