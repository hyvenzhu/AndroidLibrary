package com.android.baseline.framework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.baseline.AppDroid;
import com.android.baseline.framework.log.Logger;

/**
 * 数据库轻量级操作封装
 * 
 * @author zhuhf
 * @version [2013-3-18]
 */
public class DBHelper
{
    private static final String TAG = "DBHelper";
    private DataBaseHelper dbHelper = null;
    private SQLiteDatabase db = null;
    /** 数据库名称 */
    private static final String DATABASE_NAME = "project.db";
    /** 数据库版本 */
    private static final int DATABASE_VERSION = 1;

    public DBHelper()
    {
        dbHelper = new DataBaseHelper(AppDroid.getInstance().getApplicationContext());
    }

    /**
     * 打开数据库
     */
    public void open()
    {
        Logger.d(TAG,
                "open()");
        try
        {
            db = dbHelper.getWritableDatabase();
        }
        catch (Exception ex)
        {
            try
            {
                db = dbHelper.getReadableDatabase();
            }
            catch (Exception e)
            {
                Logger.e(TAG,
                        e);
            }
        }
    }

    private int tryCount;

    /**
     * 获取数据库操作对象
     * 
     * @return SQLiteDatabase
     */
    public SQLiteDatabase getSQLiteDatabase()
    {
        checkDBLocked();
        tryCount = 0;
        while (db == null)
        {
            if (tryCount > 60)
            {
                break;
            }
            open();
            if (db == null)
            {
                tryCount++;
                try
                {
                    Thread.sleep(50);
                }
                catch (InterruptedException e)
                {
                    Logger.e(TAG,
                            e);
                }
            }
        }
        return db;
    }

    /**
     * 关闭数据库
     */
    public void close()
    {
        if (dbHelper != null)
        {
            dbHelper.close();
        }
    }

    /**
     * 多线程"写"数据库必须进行同步控制
     */
    private void checkDBLocked()
    {
        while (db != null && db.isDbLockedByCurrentThread())
        {
            Logger.w(TAG,
                    "db is locked by other or current threads!");
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                Logger.e(TAG,
                        e);
            }
        }
    }

    public class DataBaseHelper extends SQLiteOpenHelper
    {
        private static final String TAG = "DataBaseHelper";

        public DataBaseHelper(Context context)
        {
            super(context,
                    DATABASE_NAME,
                    null,
                    DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db)
        {
            db.beginTransaction();
            try
            {
                db.setTransactionSuccessful();
            }
            catch (Exception e)
            {
                Logger.e(TAG,
                        e);
            }
            finally
            {
                db.endTransaction();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
        }

        public void close()
        {
            try
            {
                if (db != null && db.isOpen())
                {
                    db.close();
                }
            }
            catch (Exception e)
            {
                Logger.e(TAG,
                        e);
            }
        }
    }
}
