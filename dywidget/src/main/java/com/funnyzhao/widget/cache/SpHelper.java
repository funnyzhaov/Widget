package com.funnyzhao.widget.cache;

import android.content.Context;
import android.content.SharedPreferences;
/**

 * -----------------------------------------------------------------

 * Copyright (C) 2019

 * -----------------------------------------------------------------

 * Author: funnyzhao

 * Create: 2019/8/13 0013 16:41

 * Desc:  cache辅助类

 * -----------------------------------------------------------------

 */

public class SpHelper {

    private String SPNAME;  //SP名称
    private static SpHelper mInstance; //单例
    private SharedPreferences mSharedPreference;
    private SharedPreferences.Editor mEditor;

    private SpHelper(Context context, String SpName) {
        SPNAME = SpName;
        mSharedPreference = context.getSharedPreferences(SPNAME, 0);
    }

    public static void initSpHelper(Context context, String spName) {
        if (mInstance == null) {
            synchronized (SpHelper.class) {
                mInstance = new SpHelper(context, spName);
            }
        }
    }

    public static SpHelper getSpHelper() {
        if (mInstance == null) {
            throw new ExceptionInInitializerError("在使用SP之前,需要在Application中执行initSpHelper操作");
        }
        return mInstance;
    }

    /**
     * 判断是否含有key
     *
     * @param key
     * @return
     */
    public boolean hasKey(String key) {
        if (mSharedPreference.contains(key)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 放入String值
     *
     * @param key
     * @param value
     * @return
     */
    public SpHelper putStringValue(String key, String value) {
        mEditor = mSharedPreference.edit();
        mEditor.putString(key, value);
        return mInstance;
    }

    /**
     * 放入boolean值
     *
     * @param key
     * @param value
     * @return
     */
    public SpHelper putBooleanValue(String key, Boolean value) {
        mEditor = mSharedPreference.edit();
        mEditor.putBoolean(key, value);
        return mInstance;
    }

    /**
     * 放入Int值
     *
     * @param key
     * @param value
     * @return
     */
    public SpHelper putIntValue(String key, int value) {
        mEditor = mSharedPreference.edit();
        mEditor.putInt(key, value);
        return mInstance;
    }

    /**
     * 获取String值
     *
     * @param key
     * @return
     */
    public String getStringValue(String key) {
        return mSharedPreference.getString(key, "");
    }

    /**
     * 获取boolean值
     *
     * @param key
     * @return
     */
    public boolean getBooleanValue(String key) {
        return mSharedPreference.getBoolean(key, false);
    }

    /**
     * 获取Int值
     *
     * @param key
     * @return
     */
    public int getIntValue(String key) {
        return mSharedPreference.getInt(key, 0);
    }

    /**
     * 清除某个值
     *
     * @param key
     * @return
     */
    public SpHelper clearKey(String key) {
        mEditor = mSharedPreference.edit();
        mEditor.remove(key);
        return mInstance;
    }

    /**
     * 执行提交操作
     */
    public void doCommit() {
        mEditor.commit();
    }

    /**
     * 执行提交操作
     */
    public void doApply() {
        mEditor.apply();
    }

    /**
     * 清空存储
     */
    public void doClearAll() {
        mSharedPreference.edit().clear().apply();
    }

    /**
     * 存储Long值
     *
     * @return
     */
    public SpHelper putLongValue(String key, Long value) {
        mEditor = mSharedPreference.edit();
        mEditor.putLong(key, value);
        return mInstance;
    }

    /**
     * 获取Long值
     *
     * @param key
     * @return
     */
    public Long getLongValue(String key) {
        return mSharedPreference.getLong(key, -1L);
    }


}

