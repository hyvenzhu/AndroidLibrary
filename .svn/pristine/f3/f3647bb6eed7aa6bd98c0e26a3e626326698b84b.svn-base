package com.android.baseline.framework.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Sqlite“写”操作必须进行数据同步锁操作, 多线程“读”是支持的 其他DAO的“写”操作需要通过该类来操作
 * 
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-3-10]
 */
public class BaseDAO
{
    SQLiteDatabase db;
    private DBHelper dbHelper;

    private BaseDAO()
    {
        dbHelper = new DBHelper();
    }

    private static class SingleInstanceHolder
    {
        private final static BaseDAO sInstance = new BaseDAO();
    }

    public static BaseDAO getInstance()
    {
        return SingleInstanceHolder.sInstance;
    }

    public synchronized void closeDB()
    {
        dbHelper.close();
    }

    public synchronized Cursor query(String table, String[] columns, String selection,
            String[] selectionArgs, String groupBy, String having, String orderBy)
    {
        db = dbHelper.getSQLiteDatabase();
        return db.query(table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }

    public synchronized Cursor rawQuery(String sql, String[] selectionArgs)
    {
        db = dbHelper.getSQLiteDatabase();
        return db.rawQuery(sql,
                selectionArgs);
    }

    public synchronized static Cursor query(SQLiteDatabase db, String table, String[] columns,
            String selection, String[] selectionArgs, String groupBy, String having, String orderBy)
    {
        return db.query(table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }

    /**
     * 插入数据
     * 
     * @param tableName
     * @param values
     */
    public synchronized long insert(String tableName, ContentValues values)
    {
        db = dbHelper.getSQLiteDatabase();
        return db.insert(tableName,
                null,
                values);
    }

    /**
     * 插入数据, 适用于数据库创建与升级时使用
     * 
     * @param db
     * @param tableName
     * @param values
     */
    public static synchronized long insert(SQLiteDatabase db, String tableName, ContentValues values)
    {
        return db.insert(tableName,
                null,
                values);
    }

    /**
     * 删除数据
     * 
     * @param tableName
     * @param whereClause
     * @param whereArgs
     */
    public synchronized int delete(String tableName, String whereClause, String[] whereArgs)
    {
        db = dbHelper.getSQLiteDatabase();
        return db.delete(tableName,
                whereClause,
                whereArgs);
    }

    /**
     * 删除数据
     * 
     * @param tableName
     * @param whereClause
     * @param whereArgs
     */
    public static synchronized int delete(SQLiteDatabase db, String tableName, String whereClause,
            String[] whereArgs)
    {
        return db.delete(tableName,
                whereClause,
                whereArgs);
    }

    /**
     * 更新数据
     * 
     * @param tableName
     * @param values
     * @param whereClause
     * @param whereArgs
     */
    public synchronized int update(String tableName, ContentValues values, String whereClause,
            String[] whereArgs)
    {
        db = dbHelper.getSQLiteDatabase();
        return db.update(tableName,
                values,
                whereClause,
                whereArgs);
    }

    /**
     * 更新数据
     * 
     * @param tableName
     * @param values
     * @param whereClause
     * @param whereArgs
     */
    public static synchronized int update(SQLiteDatabase db, String tableName,
            ContentValues values, String whereClause, String[] whereArgs)
    {
        return db.update(tableName,
                values,
                whereClause,
                whereArgs);
    }

    /**
     * execSQL语句
     * 
     * @param sql
     */
    public synchronized void execSQL(String sql)
    {
        db = dbHelper.getSQLiteDatabase();
        db.execSQL(sql);
    }

    /**
     * execSQL语句
     * 
     * @param sql
     */
    public static synchronized void execSQL(SQLiteDatabase db, String sql)
    {
        db.execSQL(sql);
    }

    /**
     * execSQL语句
     * 
     * @param sql
     * @param bindArgs
     */
    public synchronized void execSQL(String sql, Object[] bindArgs)
    {
        db = dbHelper.getSQLiteDatabase();
        db.execSQL(sql,
                bindArgs);
    }

    /**
     * execSQL语句
     * 
     * @param sql
     * @param bindArgs
     */
    public static synchronized void execSQL(SQLiteDatabase db, String sql, Object[] bindArgs)
    {
        db.execSQL(sql,
                bindArgs);
    }
}
