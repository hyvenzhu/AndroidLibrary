package com.android.baseline.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.baseline.framework.db.BaseDAO;
import com.android.baseline.framework.log.Logger;

/**
 * 配置信息数据库保存 key-value
 * 
 * @author haifeng
 * @version [139cx, 2014-2-20]
 */
public class SharedPreferencesDBUtil
{
    private static final String TAG = "SharedPreferencesDBUtil";
    public static final String TABLE_NAME = "shared_prefs";
    private static final String COLUMN_KEY = "key";
    private static final String COLUMN_VALUE = "value";
    /** 建表语句 */
    public static final String TABLE_CREATE_SQL = new StringBuilder()
            .append("CREATE TABLE ")
                .append(TABLE_NAME)
                .append("(")
                .append(COLUMN_KEY)
                .append(" TEXT,")
                .append(COLUMN_VALUE)
                .append(" TEXT)")
                .toString();

    private static class SingleInstanceHolder
    {
        private final static SharedPreferencesDBUtil sInstance = new SharedPreferencesDBUtil();
    }

    public static SharedPreferencesDBUtil getInstance()
    {
        return SingleInstanceHolder.sInstance;
    }

    /** 数据库操作对象 */
    private BaseDAO baseDAO;

    private SharedPreferencesDBUtil()
    {
        baseDAO = BaseDAO.getInstance();
    }

    public boolean checkKeyExist(String key)
    {
        boolean isExist = false;
        Cursor cursor = null;
        try
        {
            cursor = baseDAO.query(TABLE_NAME,
                    null,
                    COLUMN_KEY + "=?",
                    new String[]
                    { key },
                    null,
                    null,
                    null);
            if (cursor.getCount() > 0)
            {
                isExist = true;
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return isExist;
    }

    public static boolean checkKeyExist(SQLiteDatabase db, String key)
    {
        boolean isExist = false;
        Cursor cursor = null;
        try
        {
            cursor = BaseDAO.query(db,
                    TABLE_NAME,
                    null,
                    COLUMN_KEY + "=?",
                    new String[]
                    { key },
                    null,
                    null,
                    null);
            if (cursor.getCount() > 0)
            {
                isExist = true;
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return isExist;
    }

    /**
     * 查询某个value是否存在
     * 
     * @param key
     * @param defaultResult
     * @return
     */
    public boolean getBoolean(String key, boolean defaultResult)
    {
        boolean result = defaultResult;
        Cursor cursor = null;
        try
        {
            cursor = baseDAO.query(TABLE_NAME,
                    null,
                    COLUMN_KEY + "=?",
                    new String[]
                    { key },
                    null,
                    null,
                    null);
            if (cursor.moveToNext())
            {
                result = cursor.getInt(cursor.getColumnIndex(COLUMN_VALUE)) == 1;
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return result;
    }

    /**
     * 存入布尔值
     * 
     * @param key
     * @param value
     */
    public void putBoolean(String key, boolean value)
    {
        try
        {
            if (checkKeyExist(key))
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_VALUE,
                        value);
                baseDAO.update(TABLE_NAME,
                        values,
                        COLUMN_KEY + "=?",
                        new String[]
                        { key });
            }
            else
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_KEY,
                        key);
                values.put(COLUMN_VALUE,
                        value);
                baseDAO.insert(TABLE_NAME,
                        values);
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
    }

    public static void putBoolean(SQLiteDatabase db, String key, boolean value)
    {
        try
        {
            if (checkKeyExist(db,
                    key))
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_VALUE,
                        value);
                BaseDAO.update(db,
                        TABLE_NAME,
                        values,
                        COLUMN_KEY + "=?",
                        new String[]
                        { key });
            }
            else
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_KEY,
                        key);
                values.put(COLUMN_VALUE,
                        value);
                BaseDAO.insert(db,
                        TABLE_NAME,
                        values);
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
    }

    public String getString(String key, String defaultValue)
    {
        String result = defaultValue;
        Cursor cursor = null;
        try
        {
            cursor = baseDAO.query(TABLE_NAME,
                    new String[]
                    { COLUMN_VALUE },
                    COLUMN_KEY + "=?",
                    new String[]
                    { key },
                    null,
                    null,
                    null);
            if (cursor.moveToNext())
            {
                result = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return result;
    }

    public void putString(String key, String value)
    {
        try
        {
            if (checkKeyExist(key))
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_VALUE,
                        value);
                baseDAO.update(TABLE_NAME,
                        values,
                        COLUMN_KEY + "=?",
                        new String[]
                        { key });
            }
            else
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_KEY,
                        key);
                values.put(COLUMN_VALUE,
                        value);
                baseDAO.insert(TABLE_NAME,
                        values);
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
    }

    public static void putString(SQLiteDatabase db, String key, String value)
    {
        try
        {
            if (checkKeyExist(db,
                    key))
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_VALUE,
                        value);
                BaseDAO.update(db,
                        TABLE_NAME,
                        values,
                        COLUMN_KEY + "=?",
                        new String[]
                        { key });
            }
            else
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_KEY,
                        key);
                values.put(COLUMN_VALUE,
                        value);
                BaseDAO.insert(db,
                        TABLE_NAME,
                        values);
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
    }

    public int getInteger(String key, int defaultValue)
    {
        int result = defaultValue;
        Cursor cursor = null;
        try
        {
            cursor = baseDAO.query(TABLE_NAME,
                    new String[]
                    { COLUMN_VALUE },
                    COLUMN_KEY + "=?",
                    new String[]
                    { key },
                    null,
                    null,
                    null);
            if (cursor.moveToNext())
            {
                result = cursor.getInt(cursor.getColumnIndex(COLUMN_VALUE));
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return result;
    }

    public void putInteger(String key, int value)
    {
        try
        {
            if (checkKeyExist(key))
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_VALUE,
                        value);
                baseDAO.update(TABLE_NAME,
                        values,
                        COLUMN_KEY + "=?",
                        new String[]
                        { key });
            }
            else
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_KEY,
                        key);
                values.put(COLUMN_VALUE,
                        value);
                baseDAO.insert(TABLE_NAME,
                        values);
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
    }

    public Long getLong(String key, long defaultValue)
    {
        long result = defaultValue;
        Cursor cursor = null;
        try
        {
            cursor = baseDAO.query(TABLE_NAME,
                    new String[]
                    { COLUMN_VALUE },
                    COLUMN_KEY + "=?",
                    new String[]
                    { key },
                    null,
                    null,
                    null);
            if (cursor.moveToNext())
            {
                result = cursor.getLong(cursor.getColumnIndex(COLUMN_VALUE));
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
        finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
        return result;
    }

    public void putLong(String key, long value)
    {
        try
        {
            if (checkKeyExist(key))
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_VALUE,
                        value);
                baseDAO.update(TABLE_NAME,
                        values,
                        COLUMN_KEY + "=?",
                        new String[]
                        { key });
            }
            else
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_KEY,
                        key);
                values.put(COLUMN_VALUE,
                        value);
                baseDAO.insert(TABLE_NAME,
                        values);
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
    }

    public static void putLong(SQLiteDatabase db, String key, long value)
    {
        try
        {
            if (checkKeyExist(db,
                    key))
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_VALUE,
                        value);
                BaseDAO.update(db,
                        TABLE_NAME,
                        values,
                        COLUMN_KEY + "=?",
                        new String[]
                        { key });
            }
            else
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_KEY,
                        key);
                values.put(COLUMN_VALUE,
                        value);
                BaseDAO.insert(db,
                        TABLE_NAME,
                        values);
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
    }
}
