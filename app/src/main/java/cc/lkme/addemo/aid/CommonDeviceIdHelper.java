package cc.lkme.addemo.aid;

import android.content.ComponentName;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;

import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


class CommonDeviceIdHelper {
    private Context mContext;
    private static final String manufacturer = Build.MANUFACTURER.toUpperCase();
    private final LinkedBlockingQueue<IBinder> linkedBlockingQueue = new LinkedBlockingQueue<>(1);

    CommonDeviceIdHelper(Context ctx) {
        mContext = ctx;
    }

    void getCommonId(CommonIdObtainListener commonIdObtainListener) {
        String packageName = null;
        Intent intent = new Intent();
        try {
            int flag = 0;
            switch (manufacturer) {
                case "HUAWEI":
                    packageName = "com.huawei.hwid";
                    intent.setAction("com.uodis.opendevice.OPENIDS_SERVICE");
                    intent.setPackage("com.huawei.hwid");
                    break;
                case "ASUS":
                    packageName = "com.asus.msa.SupplementaryDID";
                    intent.setAction("com.asus.msa.action.ACCESS_DID");
                    ComponentName componentName = new ComponentName("com.asus.msa.SupplementaryDID", "com.asus.msa.SupplementaryDID.SupplementaryDIDService");
                    intent.setComponent(componentName);
                    break;
                case "MEIZU":
                    packageName = "com.meizu.flyme.openidsdk";
                    break;
                case "SAMSUNG":
                    packageName = "com.samsung.android.deviceidservice";
                    intent.setClassName("com.samsung.android.deviceidservice", "com.samsung.android.deviceidservice.DeviceIdService");
                    break;
                case "XIAOMI":
                case "BLACKSHARK":
                    break;
                case "LENOVO":
                case "MOTOLORA":
                    intent.setClassName("com.zui.deviceidservice", "com.zui.deviceidservice.DeviceidService");
                    break;
                case "VIVO":
                    break;
                case "OPPO":
                case "ONEPLUS":
                    intent.setComponent(new ComponentName("com.heytap.openid", "com.heytap.openid.IdentifyService"));
                    intent.setAction("action.com.heytap.openid.OPEN_ID_SERVICE");
                    break;
                case "ZTE":
                case "FERRMEOS":
                case "SSUI":
                    packageName = "com.mdid.msa";
                    Intent intentMask = new Intent();
                    intentMask.setClassName(packageName, "com.mdid.msa.service.MsaKlService");
                    intentMask.setAction("com.bun.msa.action.start.service");
                    intentMask.putExtra("com.bun.msa.param.pkgname", mContext.getPackageName());
                    try {
                        intent.putExtra("com.bun.msa.param.runinset", true);
                        mContext.startService(intent);
                    } catch (Exception ignore) {
                    }
                    intent.setClassName(packageName, "com.mdid.msa.service.MsaIdService");
                    intent.setAction("com.bun.msa.action.bindto.service");
                    intent.putExtra("com.bun.msa.param.pkgname", mContext.getPackageName());
                    break;
                case "NUBIA":
                    break;
            }
            if (packageName != null) {
                mContext.getPackageManager().getPackageInfo(packageName, flag);
            }

            switch (manufacturer.toUpperCase()) {
                case "MEIZU":
                    Uri uri = Uri.parse("content://com.meizu.flyme.openidsdk/");
                    Cursor cursor = null;
                    ContentResolver contentResolver = mContext.getContentResolver();
                    try {
                        cursor = contentResolver.query(uri, null, null, new String[]{"oaid"}, null);
                        String oaid = null;
                        if (cursor != null && !cursor.isClosed()) {
                            cursor.moveToFirst();
                            int valueIdx = cursor.getColumnIndex("value");
                            if (valueIdx > 0) {
                                oaid = cursor.getString(valueIdx);
                            }

                            if (commonIdObtainListener != null) {
                                commonIdObtainListener.OnIdObtain(oaid);
                            }

                        }
                    } catch (Throwable ignore) {
                    } finally {
                        if (cursor != null) {
                            cursor.close();
                        }
                    }
                    break;
                case "XIAOMI":
                case "BLACKSHARK":
                case "VIVO":
                case "NUBIA":
                    getOAID(commonIdObtainListener);
                    break;
                default:
                    try {
                        boolean isBin = mContext.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
                        if (isBin) {
                            if (manufacturer.toUpperCase().equals("OPPO")
                                    || manufacturer.toUpperCase().equals("ONEPLUS")) {
                                try {
                                    SystemClock.sleep(3000);
                                } catch (Exception ignore) {
                                }
                            }
                            IBinder iBinder = linkedBlockingQueue.poll(3, TimeUnit.SECONDS);
                            if (iBinder == null) {
                                return;
                            }
                            // SAMSUNG 在这里有区别，需要实际验证
                            String sha1 = getSignSHA1();
                            // oppo 是 OUID
                            packageName = mContext.getPackageName();
                            CommonIdInter commonIdInter = CommonBinder.genInterface(iBinder, packageName, sha1, "OUID");
                            String ids = null;
                            if (commonIdInter != null) {
                                ids = commonIdInter.getId();
                            }
                            if (commonIdObtainListener != null) {
                                commonIdObtainListener.OnIdObtain(ids);
                            }

                        }
                    } catch (Throwable ignore) {
                    } finally {
                        mContext.unbindService(serviceConnection);
                    }
                    break;
            }
        } catch (Throwable ignore) {
        }
    }

    public String getOAID(CommonIdObtainListener commonIdObtainListener) {
        String ids = null;
        Uri uri = null;
        try {
            switch (manufacturer.toUpperCase()) {
                case "XIAOMI":
                case "BLACKSHARK":
                    Class idProvider = Class.forName("com.android.id.impl.IdProviderImpl");
                    Method oaid = idProvider.getMethod("getOAID", new Class[]{Context.class});
                    Object idImpl = idProvider.newInstance();
                    ids = (String) oaid.invoke(idImpl, mContext);
                    break;
                case "VIVO":
                    uri = Uri.parse("content://com.vivo.vms.IdProvider/IdentifierId/OAID");
                    Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
                    if (cursor != null) {
                        if (cursor.moveToNext()) {
                            ids = cursor.getString(cursor.getColumnIndex("value"));
                        }
                        cursor.close();
                    }
                    break;
                case "NUBIA":
                    Bundle bundle = null;
                    uri = Uri.parse("content://cn.nubia.identity/identity");
                    try {
                        if (Build.VERSION.SDK_INT > 17) {
                            ContentProviderClient contentProviderClient = mContext.getContentResolver().acquireContentProviderClient(uri);
                            bundle = contentProviderClient.call("getOAID", null, null);
                            if (contentProviderClient != null) {
                                if (Build.VERSION.SDK_INT >= 24) {
                                    contentProviderClient.close();
                                } else {
                                    contentProviderClient.release();
                                }
                            }
                        } else {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                                bundle = mContext.getContentResolver().call(uri, "getOAID", null, null);
                            }
                        }
                        if (bundle != null) {
                            int code = bundle.getInt("code", -1);
                            if (code == 0) {
                                ids = bundle.getString("id");
                            }
                        }
                    } catch (Exception ignore) {
                    }
                    break;
            }
        } catch (Throwable ignore) {
        }
        if (commonIdObtainListener != null) {
            commonIdObtainListener.OnIdObtain(ids);
        }
        return ids;
    }

    private String getSignSHA1() {
        String sha1 = null;
        try {
            Signature[] signatures;
            PackageInfo packageInfo;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),
                        PackageManager.GET_SIGNING_CERTIFICATES);
                signatures = packageInfo.signingInfo.getApkContentsSigners();
            } else {
                packageInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(),
                        PackageManager.GET_SIGNATURES);
                signatures = packageInfo.signatures;
            }
            if (signatures != null && signatures.length > 0) {
                byte[] byteArray = signatures[0].toByteArray();
                try {
                    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
                    if (messageDigest != null) {
                        byte[] digest = messageDigest.digest(byteArray);
                        StringBuilder sb = new StringBuilder();
                        for (byte b : digest) {
                            sb.append(Integer.toHexString((b & 255) | 256).substring(1, 3));
                        }
                        sha1 = sb.toString();
                    }
                } catch (Throwable ignore) {
                }
            }
        } catch (Throwable ignore) {
        }

        return sha1;
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            try {
                linkedBlockingQueue.put(service);
            } catch (Throwable ignore) {
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            // TODO: 7/6/20 lipeng 这里是否需要处理？
        }
    };
}
