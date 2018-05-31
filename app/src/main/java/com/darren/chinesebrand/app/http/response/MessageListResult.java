package com.darren.chinesebrand.app.http.response;

import com.darren.chinesebrand.framework.http.BaseResult;

import java.util.List;

/**
 * description: 消息列表
 * author: Darren on 2017/11/21 09:32
 * email: 240336124@qq.com
 * version: 1.0
 */
public class MessageListResult extends BaseResult{

    public MessageListData data;

    public static class MessageListData {
        public List<Message> data;
    }

    public static class Message{

        /**
         * id : 1
         * topic : dssasd
         * content : asdasdasdasadsdasad
         * ctime : 2017-11-30 18:29:49
         * utime : 2017-11-30 18:29:49
         * start_time : 2018
         * over_time : null
         * mid : 1
         * status : 1
         */

        private int id;
        private String topic;
        private String content;
        private String ctime;
        private String utime;
        private int start_time;
        private Object over_time;
        private int mid;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getCtime() {
            return ctime;
        }

        public void setCtime(String ctime) {
            this.ctime = ctime;
        }

        public String getUtime() {
            return utime;
        }

        public void setUtime(String utime) {
            this.utime = utime;
        }

        public int getStart_time() {
            return start_time;
        }

        public void setStart_time(int start_time) {
            this.start_time = start_time;
        }

        public Object getOver_time() {
            return over_time;
        }

        public void setOver_time(Object over_time) {
            this.over_time = over_time;
        }

        public int getMid() {
            return mid;
        }

        public void setMid(int mid) {
            this.mid = mid;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }
    }
}
