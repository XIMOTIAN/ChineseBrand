package com.darren.chinesebrand.app.http;

/**
 * description:
 * author: Darren on 2017/11/21 09:18
 * email: 240336124@qq.com
 * version: 1.0
 */
public class Constant {

    public static final class UrlConstant {
        // 主路径
        public static final String URL_BASE = "http://ppw.zmzxd.cn/index.php/api/v1/";

        // 首页分类
        public static final String HOME_SORT_URL = URL_BASE + "sort";
        // 发送验证码
        public static final String SEND_CODE_URL = URL_BASE + "haircode";
        // 用户注册
        public static final String USER_REGISTER_URL = URL_BASE + "register";
        // 用户登录
        public static final String USER_LOGIN_URL = URL_BASE + "login";
        // 用户忘记密码
        public static final String USER_FORGETPWD_URL = URL_BASE + "editpass";
        // 首页列表的Banner
        public static final String BANNER_LIST_URL = URL_BASE + "banner";
        // 获取用户登录信息
        public static final String USER_INFO_URL = URL_BASE + "loginuser";
        // 编辑用户图像
        public static final String EDIT_USER_HEADER = URL_BASE + "editlogo";
        // 用户建议
        public static final String USER_SUGGEST_URL = URL_BASE + "addfeedback";
        // 点赞评论
        public static final String COMMENT_LIKE_URL = URL_BASE + "comment_like";
        // 用户评论详情
        public static final String COMMENT_DETAIL_URL = URL_BASE + "commentdetail";
        // 对文章添加评论
        public static final String BRAND_COMMENT_URL = URL_BASE + "addcomment";
        // 消息支付
        public static final String MESSAGE_PUBLIC_URL = URL_BASE + "xiaoxi";
        // 我评论的
        public static final String MINE_COMMENT_URL = URL_BASE + "mycomment";
        // 评论我的
        public static final String COVER_COMMENT_URL = URL_BASE + "covercomment";
        // 我的收藏
        public static final String MINE_COLLECT_URL = URL_BASE + "mycollection";
        // 编辑用户信息
        public static final String EDIT_INFO_URL = URL_BASE +"editinfo";
    }

    public static final class VALUE {
        public static final int PAGE_SIZE = 8;
    }
}
