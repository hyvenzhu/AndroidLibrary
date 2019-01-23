package library.demo;

import android.app.Application;

import library.common.App;


/**
 * @author zhuhf
 * @version [AndroidLibrary, 2018-03-06]
 */
public class AppDroid extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        App.init(this);
    }
}
