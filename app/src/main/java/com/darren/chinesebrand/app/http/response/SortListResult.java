package com.darren.chinesebrand.app.http.response;

import com.darren.chinesebrand.framework.http.BaseResult;

import java.util.List;

/**
 * description:
 * author: Darren on 2017/11/21 09:32
 * email: 240336124@qq.com
 * version: 1.0
 */
public class SortListResult extends BaseResult{

    private List<SortEntity> data;

    public List<SortEntity> getData() {
        return data;
    }

    public void setData(List<SortEntity> data) {
        this.data = data;
    }

    public static class SortEntity {
        /**
         * is_id : 2
         * is_name : 资讯
         * pnames : [{"is_id":9,"is_name":"国内"},{"is_id":12,"is_name":"体育资讯"}]
         */

        private int is_id;
        private String is_name;
        public List<SortEntity> pnames;

        public int getIs_id() {
            return is_id;
        }

        public void setIs_id(int is_id) {
            this.is_id = is_id;
        }

        public String getIs_name() {
            return is_name;
        }

        public void setIs_name(String is_name) {
            this.is_name = is_name;
        }
    }
}
