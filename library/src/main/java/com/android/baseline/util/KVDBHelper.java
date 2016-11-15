package com.android.baseline.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.android.baseline.framework.db.BaseDAO;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * key-value数据库
 *
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2014-2-20]
 */
public class KVDBHelper {
    public static final String TABLE_NAME = "key_value";
    private static final String COLUMN_KEY = "key";
    private static final String COLUMN_VALUE = "value";
    /**
     * 建表语句
     */
    public static final String TABLE_CREATE_SQL = new StringBuilder()
            .append("CREATE TABLE ")
            .append(TABLE_NAME)
            .append("(")
            .append(COLUMN_KEY)
            .append(" TEXT,")
            .append(COLUMN_VALUE)
            .append(" TEXT)")
            .toString();

    /**
     * 数据库操作对象
     */
    private BaseDAO baseDAO;

    private static String keyPrefix; // key前缀, 常用来区分用户

    public KVDBHelper() {
        this(null);
    }

    public KVDBHelper(String keyPrefix) {
        if (keyPrefix == null) {
            keyPrefix = "";
        }
        this.keyPrefix = keyPrefix;
        baseDAO = BaseDAO.getInstance();
    }

    public void contains(final String key, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(contains(key));
                subscriber.onCompleted();
            }
        }, listener);
    }

    private boolean contains(final String key) {
        boolean isExist = false;
        Cursor cursor = null;
        try {
            cursor = baseDAO.query(TABLE_NAME,
                    null,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)},
                    null,
                    null,
                    null);
            if (cursor.getCount() > 0) {
                isExist = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExist;
    }

    public static void contains(final SQLiteDatabase db, final String key, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(contains(db, key));
                subscriber.onCompleted();
            }
        }, listener);
    }

    private static boolean contains(final SQLiteDatabase db, final String key) {
        boolean isExist = false;
        Cursor cursor = null;
        try {
            cursor = BaseDAO.query(db,
                    TABLE_NAME,
                    null,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)},
                    null,
                    null,
                    null);
            if (cursor.getCount() > 0) {
                isExist = true;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return isExist;
    }

    public void getBoolean(final String key, final boolean defaultResult, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(getBoolean(key, defaultResult));
                subscriber.onCompleted();
            }
        }, listener);
    }

    /**
     * 查询某个value是否存在
     *
     * @param key
     * @param defaultResult
     * @return
     */
    public boolean getBoolean(final String key, final boolean defaultResult) {
        boolean result = defaultResult;
        Cursor cursor = null;
        try {
            cursor = baseDAO.query(TABLE_NAME,
                    null,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)},
                    null,
                    null,
                    null);
            if (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(COLUMN_VALUE)) == 1;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void putBoolean(final String key, final boolean value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putBoolean(key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    /**
     * 存入布尔值
     *
     * @param key
     * @param value
     */
    public boolean putBoolean(String key, boolean value) {
        if (contains(key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.update(TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.insert(TABLE_NAME,
                    values) != -1;
        }
    }

    public static void putBoolean(final SQLiteDatabase db, final String key, final boolean value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putBoolean(db, key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public static boolean putBoolean(SQLiteDatabase db, String key, boolean value) {
        if (contains(db,
                key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.update(db,
                    TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.insert(db,
                    TABLE_NAME,
                    values) != -1;
        }
    }

    public void getString(final String key, final String defaultValue, final ResultListener<String> listener) {
        onResult(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                subscriber.onNext(getString(key, defaultValue));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public String getString(String key, String defaultValue) {
        String result = defaultValue;
        Cursor cursor = null;
        try {
            cursor = baseDAO.query(TABLE_NAME,
                    new String[]
                            {COLUMN_VALUE},
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)},
                    null,
                    null,
                    null);
            if (cursor.moveToNext()) {
                result = cursor.getString(cursor.getColumnIndex(COLUMN_VALUE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void putString(final String key, final String value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putString(key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public boolean putString(String key, String value) {
        if (contains(key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.update(TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.insert(TABLE_NAME,
                    values) != -1;
        }
    }

    public static void putString(final SQLiteDatabase db, final String key, final String value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putString(db, key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public static boolean putString(SQLiteDatabase db, String key, String value) {
        if (contains(db,
                key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.update(db,
                    TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.insert(db,
                    TABLE_NAME,
                    values) != -1;
        }
    }

    public void getInteger(final String key, final int defaultValue, final ResultListener<Integer> listener) {
        onResult(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                subscriber.onNext(getInteger(key, defaultValue));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public int getInteger(String key, int defaultValue) {
        int result = defaultValue;
        Cursor cursor = null;
        try {
            cursor = baseDAO.query(TABLE_NAME,
                    new String[]
                            {COLUMN_VALUE},
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)},
                    null,
                    null,
                    null);
            if (cursor.moveToNext()) {
                result = cursor.getInt(cursor.getColumnIndex(COLUMN_VALUE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void putInteger(final String key, final int value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putInteger(key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public boolean putInteger(String key, int value) {
        if (contains(key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.update(TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.insert(TABLE_NAME,
                    values) != -1;
        }
    }

    public static void putInteger(final SQLiteDatabase db, final String key, final int value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putInteger(db, key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public static boolean putInteger(SQLiteDatabase db, String key, int value) {
        if (contains(db, key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.update(db, TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.insert(db, TABLE_NAME, values) != -1;
        }
    }

    public void getLong(final String key, final long defaultValue, final ResultListener<Long> listener) {
        onResult(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                subscriber.onNext(getLong(key, defaultValue));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public long getLong(String key, long defaultValue) {
        long result = defaultValue;
        Cursor cursor = null;
        try {
            cursor = baseDAO.query(TABLE_NAME,
                    new String[]
                            {COLUMN_VALUE},
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)},
                    null,
                    null,
                    null);
            if (cursor.moveToNext()) {
                result = cursor.getLong(cursor.getColumnIndex(COLUMN_VALUE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void putLong(final String key, final long value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putLong(key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public boolean putLong(String key, long value) {
        if (contains(key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.update(TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.insert(TABLE_NAME,
                    values) != -1;
        }
    }

    public static void putLong(final SQLiteDatabase db, final String key, final long value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putLong(db, key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public static boolean putLong(SQLiteDatabase db, String key, long value) {
        if (contains(db,
                key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.update(db,
                    TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.insert(db,
                    TABLE_NAME,
                    values) != -1;
        }
    }

    public void getDouble(final String key, final double defaultValue, final ResultListener<Double> listener) {
        onResult(new Observable.OnSubscribe<Double>() {
            @Override
            public void call(Subscriber<? super Double> subscriber) {
                subscriber.onNext(getDouble(key, defaultValue));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public double getDouble(String key, double defaultValue) {
        double result = defaultValue;
        Cursor cursor = null;
        try {
            cursor = baseDAO.query(TABLE_NAME,
                    new String[]
                            {COLUMN_VALUE},
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)},
                    null,
                    null,
                    null);
            if (cursor.moveToNext()) {
                result = cursor.getDouble(cursor.getColumnIndex(COLUMN_VALUE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void putDouble(final String key, final double value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putDouble(key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public boolean putDouble(String key, double value) {
        if (contains(key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.update(TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.insert(TABLE_NAME,
                    values) != 0;
        }
    }

    public static void putDouble(final SQLiteDatabase db, final String key, final double value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putDouble(db, key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public static boolean putDouble(SQLiteDatabase db, String key, double value) {
        if (contains(db,
                key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.update(db,
                    TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.insert(db,
                    TABLE_NAME,
                    values) != -1;
        }
    }

    public void getFloat(final String key, final float defaultValue, final ResultListener<Float> listener) {
        onResult(new Observable.OnSubscribe<Float>() {
            @Override
            public void call(Subscriber<? super Float> subscriber) {
                subscriber.onNext(getFloat(key, defaultValue));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public float getFloat(String key, float defaultValue) {
        float result = defaultValue;
        Cursor cursor = null;
        try {
            cursor = baseDAO.query(TABLE_NAME,
                    new String[]
                            {COLUMN_VALUE},
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)},
                    null,
                    null,
                    null);
            if (cursor.moveToNext()) {
                result = cursor.getFloat(cursor.getColumnIndex(COLUMN_VALUE));
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return result;
    }

    public void putFloat(final String key, final float value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putFloat(key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public boolean putFloat(String key, float value) {
        if (contains(key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.update(TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return baseDAO.insert(TABLE_NAME,
                    values) != -1;
        }
    }

    public static void putFloat(final SQLiteDatabase db, final String key, final float value, final ResultListener<Boolean> listener) {
        onResult(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                subscriber.onNext(putFloat(db, key, value));
                subscriber.onCompleted();
            }
        }, listener);
    }

    public static boolean putFloat(SQLiteDatabase db, String key, float value) {
        if (contains(db,
                key)) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.update(db,
                    TABLE_NAME,
                    values,
                    COLUMN_KEY + "=?",
                    new String[]
                            {wrapKey(key)}) > 0;
        } else {
            ContentValues values = new ContentValues();
            values.put(COLUMN_KEY,
                    wrapKey(key));
            values.put(COLUMN_VALUE,
                    value);
            return BaseDAO.insert(db,
                    TABLE_NAME,
                    values) != -1;
        }
    }

    private static <T> void onResult(Observable.OnSubscribe<T> onSubscribe, final ResultListener<T> listener) {
        Observable.create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<T>() {
                    @Override
                    public void call(T o) {
                        listener.onResult(o);
                    }
                });
    }

    /**
     * 给key加上前缀
     *
     * @param key
     * @return
     */
    private static String wrapKey(String key) {
        return keyPrefix + "_" + key;
    }

    public interface ResultListener<T> {
        void onResult(T response);
    }
}
