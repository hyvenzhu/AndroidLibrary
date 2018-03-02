package com.android.baseline;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.android.baseline.framework.ui.activity.UIStateHelper;

/**
 * App application
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-5-3]
 */
public class AppDroid extends Application {
    private static AppDroid sInstance;
    public UIStateHelper uiStateHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        uiStateHelper = new UIStateHelper();
    }

    public static AppDroid getInstance() {
        return sInstance;
    }

    /**
     * 数据库创建
     *
     * @param db
     */
    public void onDBCreate(SQLiteDatabase db) {
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    public void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    /**
     * 数据库版本
     *
     * @return
     */
    public int getDataBaseVersion() {
        return 1;
    }

    /**
     * 退出程序
     * @param context
     */
    public void exist(Context context) {
        uiStateHelper.finishAll();
    }
}
