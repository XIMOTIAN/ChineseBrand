package com.darren.chinesebrand.app.http.response;

import com.darren.chinesebrand.framework.http.BaseResult;

import java.io.Serializable;
import java.util.List;

/**
 * description:
 * created by darren on 2017/11/25 11:23
 * email: 240336124@qq.com
 * version: 1.0
 */
public class BrandListResult extends BaseResult {

    private BrandBeanData data;

    public BrandBeanData getData() {
        return data;
    }

    public void setData(BrandBeanData data) {
        this.data = data;
    }

    public static class BrandBeanData {
        /**
         * total : 1
         * per_page : 5
         * current_page : 1
         * last_page : 1
         * data : [{"ai_id":34,"ai_is_head_figure":1,"ai_is_position":2,"ai_topic":"骑士缺兵少将启用新秀 詹姆斯终于能歇一歇了？","ai_smimg":["http://ozi65v7vu.bkt.clouddn.com/2017/11/52904201711211040011334.jpg"],"ai_ctime":"2017-11-21 10:40:01","ai_status":1,"am_nickname":"李沈阳","collections":0,"comments":0,"is_name":"体育资讯","ai_start_time":1970}]
         */

        private int total;
        private int per_page;
        private int current_page;
        private int last_page;
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public int getPer_page() {
            return per_page;
        }

        public void setPer_page(int per_page) {
            this.per_page = per_page;
        }

        public int getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(int current_page) {
            this.current_page = current_page;
        }

        public int getLast_page() {
            return last_page;
        }

        public void setLast_page(int last_page) {
            this.last_page = last_page;
        }

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean implements Serializable {
            /**
             * ai_id : 34
             * ai_is_head_figure : 1
             * ai_is_position : 2
             * ai_topic : 骑士缺兵少将启用新秀 詹姆斯终于能歇一歇了？
             * ai_smimg : ["http://ozi65v7vu.bkt.clouddn.com/2017/11/52904201711211040011334.jpg"]
             * ai_ctime : 2017-11-21 10:40:01
             * ai_status : 1
             * am_nickname : 李沈阳
             * collections : 0
             * comments : 0
             * is_name : 体育资讯
             * ai_start_time : 1970
             */

            public String ai_id;
            private int ai_is_head_figure;
            private int ai_is_position;
            private String ai_topic;
            private String ai_ctime;
            private int ai_status;
            private String am_nickname;
            private int collections;
            private int comments;
            private String is_name;
            private List<String> ai_smimg;

            public int getAi_is_head_figure() {
                return ai_is_head_figure;
            }

            public void setAi_is_head_figure(int ai_is_head_figure) {
                this.ai_is_head_figure = ai_is_head_figure;
            }

            public int getAi_is_position() {
                return ai_is_position;
            }

            public void setAi_is_position(int ai_is_position) {
                this.ai_is_position = ai_is_position;
            }

            public String getAi_topic() {
                return ai_topic;
            }

            public void setAi_topic(String ai_topic) {
                this.ai_topic = ai_topic;
            }

            public String getAi_ctime() {
                return ai_ctime;
            }

            public void setAi_ctime(String ai_ctime) {
                this.ai_ctime = ai_ctime;
            }

            public int getAi_status() {
                return ai_status;
            }

            public void setAi_status(int ai_status) {
                this.ai_status = ai_status;
            }

            public String getAm_nickname() {
                return am_nickname;
            }

            public void setAm_nickname(String am_nickname) {
                this.am_nickname = am_nickname;
            }

            public int getCollections() {
                return collections;
            }

            public void setCollections(int collections) {
                this.collections = collections;
            }

            public int getComments() {
                return comments;
            }

            public void setComments(int comments) {
                this.comments = comments;
            }

            public String getIs_name() {
                return is_name;
            }

            public void setIs_name(String is_name) {
                this.is_name = is_name;
            }

            public List<String> getAi_smimg() {
                return ai_smimg;
            }

            public void setAi_smimg(List<String> ai_smimg) {
                this.ai_smimg = ai_smimg;
            }

            @Override
            public boolean equals(Object obj) {
                if (obj instanceof DataBean) {
                    DataBean compare = (DataBean) obj;
                    return compare.ai_id.equals(ai_id);
                }
                return super.equals(obj);
            }
        }
    }
}
