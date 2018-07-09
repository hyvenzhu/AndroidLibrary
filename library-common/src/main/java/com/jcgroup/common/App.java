package com.jcgroup.common;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.jcgroup.common.framework.logic.net.RetrofitManager;
import com.jcgroup.common.framework.ui.activity.UIStateHelper;
import com.jcgroup.common.util.APKUtil;

/**
 * @author hiphonezhu@gmail.com
 * @version [DX-AndroidLibrary, 2018-3-6]
 */
public class App {
    static App sInstance;
    static Context appContext;
    static UIStateHelper uiStateHelper;
    InnerDB innerDB = new InnerDB() {
        @Override
        public void onDBCreate(SQLiteDatabase db) {
        
        }
        
        @Override
        public void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
        }
        
        @Override
        public int getDataBaseVersion() {
            return 1;
        }
    };
    
    public static void init(Application application) {
        sInstance = new App();
        appContext = application.getApplicationContext();
        uiStateHelper = new UIStateHelper();
        APKUtil.syncIsDebug(appContext);
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                uiStateHelper.addActivity(activity);
            }
            
            @Override
            public void onActivityStarted(Activity activity) {
            
            }
            
            @Override
            public void onActivityResumed(Activity activity) {
            
            }
            
            @Override
            public void onActivityPaused(Activity activity) {
            
            }
            
            @Override
            public void onActivityStopped(Activity activity) {
            }
            
            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            
            }
            
            @Override
            public void onActivityDestroyed(Activity activity) {
                uiStateHelper.removeActivity(activity);
            }
        });
        /**
         * 如果使用CA机构的证书，最低也需要适配 Android 4.x 对 TLS1.1、TLS1.2 的支持（默认 Android 20+ 才支持）。
         * 当然，如果服务器最低支持 TLS1.0，则可以不需要做任何适配。
         */
        RetrofitManager.getInstance().initHttps();
    }
    
    public static App getInstance() {
        if (sInstance == null) {
            throw new RuntimeException("must call init() first");
        }
        return sInstance;
    }
    
    public Context getApplicationContext() {
        return appContext;
    }
    
    public UIStateHelper getUiStateHelper() {
        return uiStateHelper;
    }
    
    public void setInnerDB(InnerDB innerDB) {
        this.innerDB = innerDB;
    }
    
    public InnerDB getInnerDB() {
        return innerDB;
    }
    
    public interface InnerDB {
        /**
         * 数据库创建
         *
         * @param db
         */
        void onDBCreate(SQLiteDatabase db);
        
        /**
         * 数据库升级
         *
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        void onDBUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
        
        /**
         * 数据库版本
         *
         * @return
         */
        int getDataBaseVersion();
    }
    
    
    /**
     * 退出程序
     *
     * @param context
     */
    public void exist(Context context) {
        uiStateHelper.finishAll();
    }
}
