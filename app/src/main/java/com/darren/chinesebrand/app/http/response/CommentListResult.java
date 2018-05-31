package com.darren.chinesebrand.app.http.response;

import com.darren.chinesebrand.framework.http.BaseResult;

import java.util.List;

/**
 * description:
 * author: Darren on 2017/11/28 09:46
 * email: 240336124@qq.com
 * version: 1.0
 */
public class CommentListResult extends BaseResult{
    private DataBeanX data;

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public static class DataBeanX {
        /**
         * total : 3
         * per_page : 3
         * current_page : 1
         * last_page : 1
         * data : [{"ac_id":22,"ac_content":"就是这么厉害","au_logo":"http://ozi65v7vu.bkt.clouddn.com2017/11/cacb2201711271009413023.png","au_nickname":"李沈阳","likes":1,"comments":2,"comment":[{"ac_content":"456456","bhfz_nickname":"李沈阳","hfz_nickname":null},{"ac_content":"456456","bhfz_nickname":"李沈阳","hfz_nickname":"李沈阳"}]},{"ac_id":26,"ac_content":"就是这么厉害","au_logo":"http://ozi65v7vu.bkt.clouddn.com2017/11/cacb2201711271009413023.png","au_nickname":"李沈阳","likes":0,"comments":3,"comment":[{"ac_content":"就是这么厉害","bhfz_nickname":"李沈阳","hfz_nickname":"李沈阳"},{"ac_content":"就是这么厉害","bhfz_nickname":"李沈阳","hfz_nickname":null},{"ac_content":"就是这么厉害","bhfz_nickname":"李沈阳","hfz_nickname":"李沈阳"}]},{"ac_id":25,"ac_content":"就是这么厉害","au_logo":"http://ozi65v7vu.bkt.clouddn.com2017/11/cacb2201711271009413023.png","au_nickname":"李沈阳","likes":0,"comments":0,"comment":[]}]
         */

        private int total;
        private String per_page;
        private String current_page;
        private int last_page;
        private List<DataBean> data;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getPer_page() {
            return per_page;
        }

        public void setPer_page(String per_page) {
            this.per_page = per_page;
        }

        public String getCurrent_page() {
            return current_page;
        }

        public void setCurrent_page(String current_page) {
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

        public static class DataBean {
            /**
             * ac_id : 22
             * ac_content : 就是这么厉害
             * au_logo : http://ozi65v7vu.bkt.clouddn.com2017/11/cacb2201711271009413023.png
             * au_nickname : 李沈阳
             * likes : 1
             * comments : 2
             * comment : [{"ac_content":"456456","bhfz_nickname":"李沈阳","hfz_nickname":null},{"ac_content":"456456","bhfz_nickname":"李沈阳","hfz_nickname":"李沈阳"}]
             */

            private int ac_id;
            private String ac_content;
            private String au_logo;
            private String au_nickname;
            private int likes;
            public int is_like;
            private int comments;
            private List<CommentBean> comment;
            public String aac_ctime;

            public int getAc_id() {
                return ac_id;
            }

            public void setAc_id(int ac_id) {
                this.ac_id = ac_id;
            }

            public String getAc_content() {
                return ac_content;
            }

            public void setAc_content(String ac_content) {
                this.ac_content = ac_content;
            }

            public String getAu_logo() {
                return au_logo;
            }

            public void setAu_logo(String au_logo) {
                this.au_logo = au_logo;
            }

            public String getAu_nickname() {
                return au_nickname;
            }

            public void setAu_nickname(String au_nickname) {
                this.au_nickname = au_nickname;
            }

            public int getLikes() {
                return likes;
            }

            public void setLikes(int likes) {
                this.likes = likes;
            }

            public int getComments() {
                return comments;
            }

            public void setComments(int comments) {
                this.comments = comments;
            }

            public List<CommentBean> getComment() {
                return comment;
            }

            public void setComment(List<CommentBean> comment) {
                this.comment = comment;
            }


        }
    }

    public static class CommentBean {
        /**
         * ac_content : 456456
         * bhfz_nickname : 李沈阳
         * hfz_nickname : null
         */

        private String ac_content;
        private String bhfz_nickname;
        private String hfz_nickname;
        public int ac_id;

        public String getAc_content() {
            return ac_content;
        }

        public void setAc_content(String ac_content) {
            this.ac_content = ac_content;
        }

        public String getBhfz_nickname() {
            return bhfz_nickname;
        }

        public void setBhfz_nickname(String bhfz_nickname) {
            this.bhfz_nickname = bhfz_nickname;
        }

        public String getHfz_nickname() {
            return hfz_nickname;
        }

    }
}
