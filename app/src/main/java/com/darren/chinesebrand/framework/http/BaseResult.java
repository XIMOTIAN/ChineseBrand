package com.darren.chinesebrand.framework.http;

import java.io.Serializable;

/**
 * description:
 * author: Darren on 2017/11/21 09:23
 * email: 240336124@qq.com
 * version: 1.0
 */
public class BaseResult implements Serializable {
    public String bol;

    public String msg;

    public boolean isOk() {
        return "true".equals(bol);
    }
}
