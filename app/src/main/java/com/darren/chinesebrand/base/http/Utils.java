package com.darren.chinesebrand.base.http;

import java.io.Closeable;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.Map;

/**
 * description:
 * author: Darren on 2017/10/11 15:36
 * email: 240336124@qq.com
 * version: 1.0
 */
public class Utils {
    /**
     * 拼接参数
     *
     * @param url
     * @param params
     * @return
     */
    public static String jointParams(String url, Map<String, Object> params) {
        if (params != null && params.size() > 0) {
            StringBuffer stringBuffer = new StringBuffer(url);
            if (!url.contains("?")) {
                // 不包含 ? 直接添加一个
                stringBuffer.append("?");
            }

            if (!url.endsWith("?")) {
                // 如果不是以 ? 结尾
                stringBuffer.append("&");
            }

            Iterator iterator = params.entrySet().iterator();

            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
            }

            stringBuffer.deleteCharAt(stringBuffer.length() - 1);

            return stringBuffer.toString();
        } else {
            return url;
        }
    }

    /**
     * 解析类上面的泛型
     *
     * @param object
     * @return
     */
    public static Class<?> analysisClazzInfo(Object object) {
        Type genType = object.getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        return (Class) params[0];
    }

    public static void closeIo(Closeable closeable) {
        try {
            if (closeable != null)
                closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
