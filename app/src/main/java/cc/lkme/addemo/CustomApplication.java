package cc.lkme.addemo;

import android.app.Application;
import android.util.Log;

import com.kepler.jd.Listener.AsyncInitListener;
import com.kepler.jd.login.KeplerApiManager;

/**
 * Created by LinkedME06 on 16/10/26.
 */

public class CustomApplication extends Application {
    private static CustomApplication instance;

    public static CustomApplication getInstance() {
        return instance;
    }

    public void onCreate() {
        super.onCreate();
        instance = this;
//        Stetho.initializeWithDefaults(this);
    }

}
