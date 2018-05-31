package com.darren.chinesebrand.base.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * <p>
 * project name : TiantianFangFu
 * <p>
 * copyright ZENG HUI (c) 2015
 * <p>
 * author : HUI
 * <p>
 * version : 1.0
 * <p>
 * date created : On June, 2015
 * <p>
 * description :
 * <p>
 * revision history :
 * <p>
 * ============================================================
 */
public class PlaceUtil {
    private static DBManager dbm;
    private static SQLiteDatabase db;

    /**
     * 获取所有省份
     *
     * @param context
     * @return
     */
    public static List<Province> getProvince(Context context) {
        dbm = new DBManager(context.getApplicationContext());
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<Province> list = new ArrayList<>();
        try {
            String sql = "select * from ffu_area where parentid = 0";
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                String id = cursor.getString(cursor.getColumnIndex("id"));
                list.add(new Province(name, id));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null)
            db.close();
        dbm.closeDatabase();
        return list;
    }

    /**
     * 通过省份 id 获取所有城市
     *
     * @param context
     * @param id
     * @return
     */
    public static List<String> getCityByProvinceId(Context context, String id) {
        dbm = new DBManager(context.getApplicationContext());
        dbm.openDatabase();
        db = dbm.getDatabase();
        List<String> list = new ArrayList<>();
        try {
            String sql = "select * from ffu_area where parentid = " + id;
            Cursor cursor = db.rawQuery(sql, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("name"));
                list.add(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (db != null)
            db.close();
        dbm.closeDatabase();
        return list;
    }

    public final static class Province {
        public String name;
        public String id;

        public Province(String name, String id) {
            this.name = name;
            this.id = id;
        }
    }
}
