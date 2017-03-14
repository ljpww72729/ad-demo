package cc.lkme.addemo;

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
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
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

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final String fileName = intent.getStringExtra(DOWNLOAD_FILE_NAME);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long downloadCompletedId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, 0);
                if (enqueue != downloadCompletedId) {
                    return;
                }
                intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Uri uri;
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
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
                    if (TextUtils.isEmpty(authorities)) {
                        stopSelf();
                        return;
                    }
                    Log.d("linkedme", "onReceive: " + authorities);

                    try {
//                        uri = FileProvider.getUriForFile(LMDownloadService.this, authorities,
//                                new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + fileName));
                        uri = Uri.fromFile(new File(getCacheDir() + File.separator + fileName));

                    } catch (Exception e) {
                        stopSelf();
                        return;
                    }
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DOWNLOADS + File.separator + fileName));
                }
                intent.setDataAndType(uri, "application/vnd.android.package-archive");
                startActivity(intent);
                stopSelf();
            }
        };

        registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
        startDownload(intent.getStringExtra(DOWNLOAD_FILE_URL), fileName);
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(receiver);
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
        DownloadManager.Request request = new DownloadManager.Request(
                Uri.parse(downloadUrl));
        request.setMimeType("application/vnd.android.package-archive");
//        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
        request.setDestinationUri(Uri.fromFile(new File(getCacheDir(), fileName)));
        enqueue = dm.enqueue(request);
    }


}