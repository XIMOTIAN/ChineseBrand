package com.darren.chinesebrand.base.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.darren.chinesebrand.R;
import com.darren.chinesebrand.framework.log.LogUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * ============================================================
 * <p>
 * project name : ��ĸ���������б�
 * <p>
 * copyright ZENG HUI (c) 2015
 * <p>
 * author : HUI
 * <p>
 * QQ : 240336124
 * <p>
 * version : 1.0
 * <p>
 * date created : On July, 2015
 * <p>
 * description :
 * <p>
 * revision history :
 * <p>
 * ============================================================
 */
public class DBManager {
    private final int BUFFER_SIZE = 1024;
    public static final String DB_NAME = "city_cn.s3db";
    public static String DB_PATH;
    private SQLiteDatabase database;
    private Context context;
    private File file = null;

    public DBManager(Context context) {
        this.context = context;
        DB_PATH = context.getCacheDir().getAbsolutePath();
    }

    public void openDatabase() {
        openDatabase(DB_PATH + "/" + DB_NAME);
        LogUtils.e("database open");
    }

    public SQLiteDatabase getDatabase() {
        return this.database;
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            LogUtils.d("open database fileName : " + dbfile);
            file = new File(dbfile);
            if (!file.exists()) {
                InputStream is = context.getResources().openRawResource(R.raw.ffu365);
                if (is != null) {
                    LogUtils.d("is null");
                }
                FileOutputStream fos = new FileOutputStream(dbfile);
                if (is != null) {
                    LogUtils.d("fosnull");
                }

                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                    fos.flush();
                }
                fos.close();
                is.close();
            }
            database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
            return database;
        } catch (FileNotFoundException e) {
            LogUtils.d("File not found");
            e.printStackTrace();
        } catch (IOException e) {
            LogUtils.d("IO exception");
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        if (this.database != null)
            this.database.close();
        LogUtils.e("database close");
    }
}