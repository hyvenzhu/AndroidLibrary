package com.android.baseline.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.baseline.framework.db.BaseDAO;
import com.android.baseline.framework.log.Logger;

/**
 * SharedPreferences的数据库实现方式
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-2-20]
 */
public class SPDBHelper
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
        private final static SPDBHelper sInstance = new SPDBHelper();
    }

    public static SPDBHelper getInstance()
    {
        return SingleInstanceHolder.sInstance;
    }

    /** 数据库操作对象 */
    private BaseDAO baseDAO;

    private SPDBHelper()
    {
        baseDAO = BaseDAO.getInstance();
    }

    public boolean contains(String key)
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

    public static boolean contains(SQLiteDatabase db, String key)
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
            if (contains(key))
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
            if (contains(db,
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
            if (contains(key))
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
            if (contains(db,
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
            if (contains(key))
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
    
    public static void putInteger(SQLiteDatabase db, String key, int value)
    {
        try
        {
            if (contains(db, key))
            {
                ContentValues values = new ContentValues();
                values.put(COLUMN_VALUE,
                        value);
                BaseDAO.update(db, TABLE_NAME,
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
                BaseDAO.insert(db, TABLE_NAME, values);
            }
        }
        catch (Exception e)
        {
            Logger.e(TAG,
                    e);
        }
    }

    public long getLong(String key, long defaultValue)
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
            if (contains(key))
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
            if (contains(db,
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
    
    public double getDouble(String key, double defaultValue)
    {
        double result = defaultValue;
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
                result = cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE));
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

    public void putDouble(String key, double value)
    {
        try
        {
            if (contains(key))
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

    public static void putDouble(SQLiteDatabase db, String key, double value)
    {
        try
        {
            if (contains(db,
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
    
    public float getFloat(String key, float defaultValue)
    {
        float result = defaultValue;
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
                result = cursor.getFloat(cursor.getColumnIndex(COLUMN_VALUE));
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

    public void putFloat(String key, float value)
    {
        try
        {
            if (contains(key))
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

    public static void putFloat(SQLiteDatabase db, String key, float value)
    {
        try
        {
            if (contains(db,
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
