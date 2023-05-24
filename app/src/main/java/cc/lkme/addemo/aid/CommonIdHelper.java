package cc.lkme.addemo.aid;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import java.lang.reflect.Method;


public class CommonIdHelper {

    private CommonIdObtainListener commonIdObtainListener;
    private static final String manufacturer = Build.MANUFACTURER.toUpperCase();

    public CommonIdHelper(CommonIdObtainListener commonIdObtainListener) {
        this.commonIdObtainListener = commonIdObtainListener;
    }

    public void getOAID(Context context) {
        if ("ASUS".equals(manufacturer)
                || "HUAWEI".equals(manufacturer)
                || "OPPO".equals(manufacturer)
                || "ONEPLUS".equals(manufacturer)
                || "ZTE".equals(manufacturer)
                || "FERRMEOS".equals(manufacturer)
                || isFreeMeOS()
                || "SSUI".equals(manufacturer)
                || isSSUIOS()) {
            getCommonIdFromNewThead(context);
        } else if ("LENOVO".equals(manufacturer)
                || "MOTOLORA".equals(manufacturer)
                || "MEIZU".equals(manufacturer)
                || "NUBIA".equals(manufacturer)
                || "SAMSUNG".equals(manufacturer)
                || "VIVO".equals(manufacturer)
                || "XIAOMI".equals(manufacturer)
                || "BLACKSHARK".equals(manufacturer)) {
            getCommonIdFromNewThead(context);
//            new CommonDeviceIdHelper(context).getCommonId(commonIdObtainListener);
        }
    }

    /**
     * 启动子线程获取
     */
    private void getCommonIdFromNewThead(final Context context) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new CommonDeviceIdHelper(context).getCommonId(commonIdObtainListener);
            }
        }).start();
    }

    private String getProperty(String property) {
        String res = null;
        if (property == null) {
            return null;
        }
        try {
            Class clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getMethod("get", new Class[]{String.class, String.class});
            res = (String) method.invoke(clazz, new Object[]{property, "unknown"});
        } catch (Throwable ignore) {
        }
        return res;
    }


    private boolean isFreeMeOS() {
        String pro = getProperty("ro.build.freeme.label");
        if ((!TextUtils.isEmpty(pro)) && pro.equalsIgnoreCase("FREEMEOS")) {
            return true;
        }
        return false;
    }

    private boolean isSSUIOS() {
        String pro = getProperty("ro.ssui.product");
        if ((!TextUtils.isEmpty(pro)) && (!pro.equalsIgnoreCase("unknown"))) {
            return true;
        }
        return false;
    }


}
