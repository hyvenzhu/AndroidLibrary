package com.android.baseline.framework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.baseline.AppDroid;
import com.android.baseline.util.KVDBHelper;

/**
 * 数据库轻量级操作封装
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2013-3-18]
 */
public class DBHelper {
    private DataBaseHelper dbHelper;
    private SQLiteDatabase writableDB;
    private SQLiteDatabase readableDB;
    /**
     * 数据库名称
     */
    private static final String DATABASE_NAME = "project.db";

    public DBHelper() {
        dbHelper = new DataBaseHelper(AppDroid.getInstance().getApplicationContext());
    }

    /**
     * 获取数据库操作对象
     *
     * @return SQLiteDatabase
     */
    public synchronized SQLiteDatabase getWritableSQLiteDatabase() {
        writableDB = dbHelper.getWritableDatabase();
        return writableDB;
    }

    /**
     * 获取数据库操作对象
     *
     * @return SQLiteDatabase
     */
    public SQLiteDatabase getReadableSQLiteDatabase() {
        readableDB = dbHelper.getReadableDatabase();
        return readableDB;
    }

    /**
     * 关闭数据库
     */
    public void close() {
        writableDB = null;
        readableDB = null;
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    public class DataBaseHelper extends SQLiteOpenHelper {
        private static final String TAG = "DataBaseHelper";

        public DataBaseHelper(Context context) {
            super(context, DATABASE_NAME,
                    null, AppDroid.getInstance().getDataBaseVersion());
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                db.execSQL(KVDBHelper.TABLE_CREATE_SQL);
                AppDroid.getInstance().onDBCreate(db);
                db.setTransactionSuccessful();
            } catch (Exception e) {
            } finally {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            AppDroid.getInstance().onDBUpgrade(db, oldVersion, newVersion);
        }
    }
}
