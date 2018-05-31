package com.darren.chinesebrand.app.user;


import com.darren.chinesebrand.base.dao.IOHandler;
import com.darren.chinesebrand.base.dao.IOHandlerFactory;
import com.darren.chinesebrand.base.dao.PreferencesUtil;

/**
 * description:
 * author: Darren on 2017/7/26 11:04
 * email: 240336124@qq.com
 * version: 1.0
 */
public class LoginSession {
    private static LoginSession mInstance;
    // 用于获取是否登录
    private static final String IS_LOGIN_KEY = "IS_LOGIN_KEY";
    // 用户的数据
    private static final String USER_DATA_KEY = "USER_DATA_KEY";

    private IOHandler mIOHandler;

    public LoginSession() {
        mIOHandler = IOHandlerFactory.getDefaultIOHandler();
    }

    public static LoginSession getLoginSession() {
        if (mInstance == null) {
            mInstance = new LoginSession();
        }
        return mInstance;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public UserData getUserInfo() {
        Object userInfo = mIOHandler.getObject(USER_DATA_KEY);
        if (userInfo != null && userInfo instanceof UserData) {
            return (UserData) userInfo;
        }
        return null;
    }

    /**
     * 退出用户
     */
    public void exitUser() {
        PreferencesUtil.getInstance().remove(USER_DATA_KEY);
        mIOHandler.save(IS_LOGIN_KEY, false);
    }

    /**
     * 判断用户是否登录
     *
     * @return
     */
    public boolean isLogin() {
        return mIOHandler.getBoolean(IS_LOGIN_KEY, false);
    }

    /**
     * 更新用户信息
     */
    public void updateUser(UserData userData) {
        // TODO 用于登录状态只能保存一周
        mIOHandler.save(USER_DATA_KEY, userData);
        mIOHandler.save(IS_LOGIN_KEY, true);
    }

    /**
     * 获取用户登录的 ID
     */
    public String getUserLoginId() {
        UserData userInfo = getUserInfo();
        if (userInfo != null) {
            return userInfo.loginId;
        }
        return "";
    }
}
