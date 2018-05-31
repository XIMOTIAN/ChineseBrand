package com.darren.chinesebrand.app.user;

import java.io.Serializable;

/**
 * description: 用户登录信息类
 * author: Darren on 2017/7/26 09:29
 * email: 240336124@qq.com
 * version: 1.0
 */
public class UserData implements Serializable {
    public String loginId;
    public String au_iphone;
    public String au_sex;
    public String au_logo;
    public String au_nickname;
    public String au_id;
    public String au_place;
    public int type;
    public String au_birthday;
    public String au_email;
    public String token;
}
