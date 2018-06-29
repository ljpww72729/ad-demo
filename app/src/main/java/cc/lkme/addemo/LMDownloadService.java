package cc.lkme.addemo;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import static cc.lkme.addemo.LMConstants.DOWNLOAD_FILE_NAME;
import static cc.lkme.addemo.LMConstants.DOWNLOAD_FILE_URL;


/**
 * Created by LinkedME06 on 15/01/2017.
 */

public class LMDownloadService extends Service {

    private DownloadManager dm;
    private BroadcastReceiver receiver;
    private long enqueue;
    public static final String DOWNLOAD_ADINFO = "download_adinfo";
    public static final String DOWNLOAD_PATH = "LMDownload/apk";
    public static final String DOWNLOAD_FULL_PATH = Environment.getExternalStorageDirectory() + File.separator + DOWNLOAD_PATH;
    private String fileName;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!PermissionUtils.selfPermissionGranted(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) &&
                Environment.getExternalStorageDirectory() != null &&
                Environment.getExternalStorageDirectory().exists()) {
            Log.i("LinkedME", "无读写存储卡权限，请先获取后再下载");
            stopSelf();
            return Service.START_NOT_STICKY;
        }
        fileName = intent.getStringExtra(DOWNLOAD_FILE_NAME);
        File path = new File(DOWNLOAD_FULL_PATH);
        if (!path.exists()) {
            path.mkdirs();
        }
        File file = new File(path, fileName);
        if (file.exists()) {
            Log.i("LinkedME", "download file is exist.");
            openFile();
            stopSelf();
            return Service.START_NOT_STICKY;
        }
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                System.out.println(intent);
                Iterator<String> iterator = intent.getExtras().keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next();
                    System.out.println(key);
                    System.out.println(intent.getExtras().get(key));
                }
                long downloadCompletedId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (enqueue != downloadCompletedId) {
                    return;
                }
                Log.i("LinkedME", "download file downloaded.");
                openFile();
                stopSelf();
            }
        };
        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload(intent.getStringExtra(DOWNLOAD_FILE_URL), fileName);
        return Service.START_STICKY;
    }

    /**
     * 打开apk文件引导用户安装
     */
    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M && getApplicationInfo().targetSdkVersion > Build.VERSION_CODES.M) {
            String authorities = "";
            List<ProviderInfo> providers = getPackageManager().queryContentProviders(getPackageName(), Process.myUid(), 0);
            if (providers != null) {
                for (ProviderInfo provider : providers) {
                    if (TextUtils.equals("android.support.v4.content.FileProvider", provider.name)) {
                        authorities = provider.authority;
                        break;
                    }
                }
            }
            Log.i("LinkedME", "设置FileProvider的Authorities为：" + authorities);
            if (TextUtils.isEmpty(authorities)) {
                Log.i("LinkedME", "未设置FileProvider的Authorities，请在Manifest.xml配置文件中配置provider，参考：https://developer.android.com/training/secure-file-sharing/setup-sharing.html。");
                stopSelf();
                return;
            }
            try {
                uri = FileProvider.getUriForFile(LMDownloadService.this, authorities,
                        new File(DOWNLOAD_FULL_PATH + File.separator + fileName));
            } catch (Exception e) {
                Log.i("LinkedME", "FileProvider的Authorities无正确匹配！");
                stopSelf();
                return;
            }
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(new File(DOWNLOAD_FULL_PATH + File.separator + fileName));
        }
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        if (receiver != null) {
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }


    /**
     * 开始下载文件
     *
     * @param downloadUrl 文件下载地址
     */
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    private void startDownload(String downloadUrl, String fileName) {
        dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        Log.i("LinkedME", "Download File is = " + downloadUrl + ",name=" + fileName);
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(downloadUrl));
        request.setMimeType("application/vnd.android.package-archive");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
//            if (LinkedME.getLinkActiveInstance().getSystemObserver().getWifiConnected()) {
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
//            } else {
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//            }
//        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }
        request.setDestinationInExternalPublicDir(DOWNLOAD_PATH, fileName);
        enqueue = dm.enqueue(request);
        Log.i("LinkedME", "the enqueue is " + enqueue);
    }


}