package com.darren.chinesebrand.app.http.response;

import com.darren.chinesebrand.framework.http.BaseResult;

import java.util.List;

/**
 * description:
 * author: Darren on 2017/11/21 09:32
 * email: 240336124@qq.com
 * version: 1.0
 */
public class BannerListResult extends BaseResult{

    public List<Banner> data;

    public static class Banner {
        public String url;
        public String img;
        public String title;
        public int ai_id;
    }
}
