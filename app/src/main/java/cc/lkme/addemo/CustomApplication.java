package cc.lkme.addemo;

import android.app.Application;

import com.facebook.stetho.Stetho;

/**
 * Created by LinkedME06 on 16/10/26.
 */

public class CustomApplication extends Application {

    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
    }

}
