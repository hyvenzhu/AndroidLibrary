package com.android.baseline.util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.android.baseline.framework.db.BaseDAO;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(contains(key));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(contains(db, key));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(getBoolean(key, defaultResult));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putBoolean(key, value));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putBoolean(db, key, value));
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
        onResult(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                e.onNext(getString(key, defaultValue));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putString(key, value));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putString(db, key, value));
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
        onResult(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> e) throws Exception {
                e.onNext(getInteger(key, defaultValue));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putInteger(key, value));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putInteger(db, key, value));
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
        onResult(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> e) throws Exception {
                e.onNext(getLong(key, defaultValue));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putLong(key, value));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putLong(db, key, value));
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
        onResult(new ObservableOnSubscribe<Double>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Double> e) throws Exception {
                e.onNext(getDouble(key, defaultValue));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putDouble(key, value));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putDouble(db, key, value));
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
        onResult(new ObservableOnSubscribe<Float>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Float> e) throws Exception {
                e.onNext(getFloat(key, defaultValue));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putFloat(key, value));
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
        onResult(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Boolean> e) throws Exception {
                e.onNext(putFloat(db, key, value));
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

    private static <T> void onResult(ObservableOnSubscribe<T> onSubscribe, final ResultListener<T> listener) {
        Observable.create(onSubscribe)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<T>() {
                    @Override
                    public void accept(@NonNull T t) throws Exception {
                        listener.onResult(t);
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
        return !TextUtils.isEmpty(keyPrefix) ? (keyPrefix + "_" + key) : key;
    }

    public interface ResultListener<T> {
        void onResult(T response);
    }
}
