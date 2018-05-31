package com.darren.chinesebrand.base.http.callback;


import com.darren.chinesebrand.base.http.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * description:
 * author: Darren on 2017/10/11 16:28
 * email: 240336124@qq.com
 * version: 1.0
 */
public abstract class HttpDownloadCallback implements EngineDownloadCallback {

    private File mSaveFile;

    public HttpDownloadCallback(File saveFile) {
        this.mSaveFile = saveFile;
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onResponse(InputStream is, long total) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mSaveFile);
            long sum = 0;
            byte[] buf = new byte[2048];
            int len = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
                progress(sum, total);
            }
            fos.flush();
            // 文件下载成功
            downloadComplete(mSaveFile);
        } catch (Exception e) {
            // Log.d(TAG, "文件下载失败");
            e.printStackTrace();
            onError(e);
        } finally {
            Utils.closeIo(is);
            Utils.closeIo(fos);
        }
    }

    /**
     * 文件下载完成，该方法运行在子线程中
     *
     * @param saveFile 本地下载好的文件
     */
    public abstract void downloadComplete(File saveFile);

    /**
     * 文件下载进度回调，该方法运行在子线程中
     */
    public abstract void progress(long current, long total);
}

