package com.darren.chinesebrand.base.dao;

/**
 * Created by hcDarren on 2017/9/24.
 */

public class PreferencesIOHandler implements IOHandler {

    @Override
    public void save(String key, String value) {
        PreferencesUtil.getInstance().saveParam(key, value);
    }

    @Override
    public void save(String key, double value) {
        PreferencesUtil.getInstance().saveParam(key, value);
    }

    @Override
    public void save(String key, int value) {
        PreferencesUtil.getInstance().saveParam(key, value);
    }

    @Override
    public void save(String key, long value) {
        PreferencesUtil.getInstance().saveParam(key, value);
    }

    @Override
    public void save(String key, boolean value) {
        PreferencesUtil.getInstance().saveParam(key, value);
    }

    @Override
    public void save(String key, Object value) {
        PreferencesUtil.getInstance().saveParam(key, value);
    }

    @Override
    public String getString(String key) {
        return (String) PreferencesUtil.getInstance().getParam(key, "");
    }

    @Override
    public double getDouble(String key, double defaultValue) {
        return (double) PreferencesUtil.getInstance().getParam(key, defaultValue);
    }

    @Override
    public int getInt(String key, int defaultValue) {
        return (int) PreferencesUtil.getInstance().getParam(key, defaultValue);
    }

    @Override
    public long getLong(String key, long defaultValue) {
        return (long) PreferencesUtil.getInstance().getParam(key, defaultValue);
    }

    @Override
    public boolean getBoolean(String key, boolean defaultValue) {
        return (boolean) PreferencesUtil.getInstance().getParam(key, defaultValue);
    }

    @Override
    public Object getObject(String key) {
        return PreferencesUtil.getInstance().getObject(key);
    }

    @Override
    public void delete(String key) {
        PreferencesUtil.getInstance().remove(key);
    }

    @Override
    public void clear() {
        PreferencesUtil.getInstance().clear();
    }
}
