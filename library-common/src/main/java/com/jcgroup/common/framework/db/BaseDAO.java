package com.jcgroup.common.framework.db;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * SQLiteOpenHelper 单例模式本身是不需要再对 CRUD 操作加锁的，
 * 但如果某个线程调用了 db 的 close 方法，会导致另一个线程 db 操作报"attempt to re-open an already-closed object: SQLiteDatabase"异常，
 * 简单处理，CRUD 全部加锁处理。
 *
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public class BaseDAO {
    private DBHelper dBHelper;
    
    private BaseDAO() {
        dBHelper = new DBHelper();
    }
    
    private static class SingleInstanceHolder {
        private final static BaseDAO sInstance = new BaseDAO();
    }
    
    public static BaseDAO getInstance() {
        return SingleInstanceHolder.sInstance;
    }
    
    public synchronized void closeDB() {
        dBHelper.close();
    }
    
    /**
     * 事务执行任务
     *
     * @param transactionListener
     * @return 事务执行成功与否
     */
    public synchronized boolean executeWithTransaction(TransactionListener transactionListener) {
        try {
            dBHelper.getWritableSQLiteDatabase().beginTransaction();
            if (transactionListener != null) {
                transactionListener.doTransaction();
            }
            dBHelper.getWritableSQLiteDatabase().setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            dBHelper.getWritableSQLiteDatabase().endTransaction();
        }
    }
    
    /**
     * 批量任务接口
     *
     * @author hiphonezhu@gmail.com
     * @version [Android-BaseLine, 2015-4-2]
     */
    public interface TransactionListener {
        void doTransaction();
    }
    
    public synchronized Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having, String orderBy) {
        return dBHelper.getReadableSQLiteDatabase().query(table,
                columns,
                selection,
                selectionArgs,
                groupBy,
                having,
                orderBy);
    }
    
    public synchronized Cursor rawQuery(String sql, String[] selectionArgs) {
        return dBHelper.getReadableSQLiteDatabase().rawQuery(sql,
                selectionArgs);
    }
    
    /**
     * 插入数据
     *
     * @param tableName
     * @param values
     */
    public synchronized long insert(String tableName, ContentValues values) {
        return dBHelper.getWritableSQLiteDatabase().insert(tableName,
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
    public synchronized int delete(String tableName, String whereClause, String[] whereArgs) {
        return dBHelper.getWritableSQLiteDatabase().delete(tableName,
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
                                   String[] whereArgs) {
        return dBHelper.getWritableSQLiteDatabase().update(tableName,
                values,
                whereClause,
                whereArgs);
    }
    
    /**
     * execSQL语句
     *
     * @param sql
     */
    public synchronized void execSQL(String sql) {
        dBHelper.getWritableSQLiteDatabase().execSQL(sql);
    }
    
    /**
     * execSQL语句
     *
     * @param sql
     * @param bindArgs
     */
    public synchronized void execSQL(String sql, Object[] bindArgs) {
        dBHelper.getWritableSQLiteDatabase().execSQL(sql,
                bindArgs);
    }
}
