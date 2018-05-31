package com.darren.chinesebrand.app.http.response;

import com.darren.chinesebrand.framework.http.BaseResult;

/**
 * description:
 * author: Darren on 2017/11/21 09:32
 * email: 240336124@qq.com
 * version: 1.0
 */
public class BannerDetailResult extends BaseResult{

    public BannerDetail data;

    public static class BannerDetail {
        public String ai_is_comment;
        public String ai_content;
        public String ai_view;
        public String is_name;
        public String ai_small_topic;
        public String ai_topic;
        public String am_nickname;
        public String ai_ctime;
        public int is_like;
        public int is_collection;
    }
}
