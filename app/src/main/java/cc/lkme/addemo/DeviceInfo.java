package cc.lkme.addemo;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;

/**
 * Created by LinkedME06 on 16/10/17.
 */

public class DeviceInfo {

    private static DeviceInfo mInstance;
    private static Context mCtx;

    private DeviceInfo(Context context) {
        mCtx = context;
    }

    public static synchronized DeviceInfo getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DeviceInfo(context);
        }
        return mInstance;
    }

    /**
     * 创建展示广告调用链接
     */
    public String getParams(String packageName, String actionStr) {

        String params = "";
        //!!!参数值不完整

        //linedme demo应用
        String appStatus = isPackageInstalled(packageName) ? "1" : "0";
        String action = actionStr;
        //添加参数
        String adid = "";
        //添加参数
        String cid = "";
        String androidID = Settings.System.getString(mCtx.getContentResolver(), Settings.Secure.ANDROID_ID);
        String imei = getIMEI();
        String serialNumber = android.os.Build.SERIAL;
        String mac = getMac();
        String os = "0";
        String deviceName = BluetoothAdapter.getDefaultAdapter().getName();
        String model = Build.MODEL;
        params += "appStatus=" + appStatus + "&action=" + action
                + "&adid=" + adid + "&cid=" + cid
                + "&androidID=" + androidID + "&imei=" + imei
                + "&serialNumber=" + serialNumber + "&mac=" + mac
                + "&os=" + os + "&deviceName=" + deviceName
                + "&model=" + model;
        return params;
    }

    /**
     * 判断是否安装了某个应用
     *
     * @param packagename
     * @return
     */
    private boolean isPackageInstalled(String packagename) {
        try {
            mCtx.getPackageManager().getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * 获取mac地址
     *
     * @return
     */
    String getMac() {
        String macSerial = null;
        String str = "";
        String streth = "";
        try {
            //wifi
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);

            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
            if (macSerial != null) {
                return macSerial.replaceAll(":", "").toUpperCase();
            }
            //移动网络
            Process ppeth = Runtime.getRuntime().exec(
                    "cat /sys/class/net/eth0/address");
            InputStreamReader ireth = new InputStreamReader(ppeth.getInputStream());
            LineNumberReader inputeth = new LineNumberReader(ireth);

            for (; null != streth; ) {
                streth = inputeth.readLine();
                if (streth != null) {
                    macSerial = streth.trim();// 去空格
                    break;
                }
            }
            if (macSerial != null) {
                return macSerial.replaceAll(":", "").toUpperCase();
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * 获取IMEI号
     *
     * @return
     */
    public String getIMEI() {
        try {
            TelephonyManager tm = (TelephonyManager) mCtx.getSystemService(Context.TELEPHONY_SERVICE);
            return tm.getDeviceId();
        } catch (Exception ignore) {
        }
        return "";
    }




}
