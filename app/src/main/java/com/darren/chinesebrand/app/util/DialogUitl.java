package com.darren.chinesebrand.app.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Looper;


/**
 * 弹窗工具类
 */
public class DialogUitl {
    private static ProgressDialog mProgressDialog;

    /**
     * 显示弹窗
     *
     * @param msg
     */
    public static void showProgressDialog(final Context ct, final String msg) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                mProgressDialog = new ProgressDialog(ct);
                mProgressDialog.setMessage(msg);
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();
                Looper.loop();
            }
        }).start();
    }

    /**
     * 隐藏弹窗
     */
    public static void dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    /**
     * 销毁dialog
     */
    public static void destoryDialog() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
}