package com.darren.chinesebrand.app;

import android.text.TextUtils;

import com.darren.chinesebrand.app.http.response.BrandListResult;
import com.darren.chinesebrand.base.dao.IOHandlerFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * description:
 * author: Darren on 2017/12/1 09:29
 * email: 240336124@qq.com
 * version: 1.0
 */
public class HistoryDao {
    private static volatile HistoryDao mInstance;

    private static final String BRAND_HISTORY_KEY = "BRAND_HISTORY_KEY";

    private HistoryDao() {

    }

    public static HistoryDao getInstance() {
        if (mInstance == null) {
            if (mInstance == null) {
                synchronized (HistoryDao.class) {
                    mInstance = new HistoryDao();
                }
            }
        }
        return mInstance;
    }

    public void insertBrandHistory(BrandListResult.BrandBeanData.DataBean brand) {
        String brandHistoryJson = IOHandlerFactory.getDefaultIOHandler().getString(BRAND_HISTORY_KEY);

        List<BrandListResult.BrandBeanData.DataBean> brandHistoryList = null;
        if (TextUtils.isEmpty(brandHistoryJson)) {
            brandHistoryList = new ArrayList<>();
        } else {
            brandHistoryList = new Gson().fromJson(brandHistoryJson, new TypeToken<List<BrandListResult.BrandBeanData.DataBean>>() {
            }.getType());
        }

        if (brandHistoryList.contains(brand)) {
            return;
        }
        brandHistoryList.add(brand);
        IOHandlerFactory.getDefaultIOHandler().save(BRAND_HISTORY_KEY, new Gson().toJson(brandHistoryList));
    }

    public List<BrandListResult.BrandBeanData.DataBean> getBrandHistory() {
        String brandHistoryJson = IOHandlerFactory.getDefaultIOHandler().getString(BRAND_HISTORY_KEY);
        if (TextUtils.isEmpty(brandHistoryJson)) {
            return null;
        }

        return new Gson().fromJson(brandHistoryJson, new TypeToken<List<BrandListResult.BrandBeanData.DataBean>>() {
        }.getType());
    }
}
