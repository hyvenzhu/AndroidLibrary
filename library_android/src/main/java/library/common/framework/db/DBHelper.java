package library.common.framework.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import library.common.App;

/**
 * 数据库轻量级操作封装
 *
 * @author hiphonezhu@gmail.com
 * @version [AndroidLibrary, 2018-3-6]
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
        dbHelper = new DataBaseHelper(App.getInstance().getApplicationContext());
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
                    null, App.getInstance().getInnerDB().getDataBaseVersion());
        }
        
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.beginTransaction();
            try {
                App.getInstance().getInnerDB().onDBCreate(db);
                db.setTransactionSuccessful();
            } catch (Exception e) {
            } finally {
                db.endTransaction();
            }
        }
        
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            App.getInstance().getInnerDB().onDBUpgrade(db, oldVersion, newVersion);
        }
    }
}
