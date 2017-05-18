package common;

import android.database.sqlite.SQLiteDatabase;

import com.android.baseline.AppDroid;

/**
 * @author hiphonezhu@gmail.com
 * @version [Android-BaseLine, 2016/03/09 16:57]
 */
public class MyDroid extends AppDroid {
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDBCreate(SQLiteDatabase db) {
        super.onDBCreate(db);
    }

    @Override
    public void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDBUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public int getDataBaseVersion() {
        return super.getDataBaseVersion();
    }
}
