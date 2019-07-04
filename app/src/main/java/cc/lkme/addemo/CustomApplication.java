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
        KeplerApiManager.asyncInitSdk(this, "b94ca1d53cebb4dec732481eddae05cb", "9656d3317cc346b9a02850ae489bdba5",
                new AsyncInitListener() {
                    @Override
                    public void onSuccess() {
// TODO Auto-generated method stub
                        Log.i("JD", "Kepler asyncInitSdk onSuccess ");
                    }

                    @Override
                    public void onFailure() {
                        // TODO Auto-generated method stub
                        Log.i("JD",
                                "Kepler asyncInitSdk 授权失败，请检查 lib 工程资源引用；包名,签名证书是否和注册一致");
                    }
                });
    }

}
