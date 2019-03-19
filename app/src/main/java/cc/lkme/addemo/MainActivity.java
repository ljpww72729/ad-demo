package cc.lkme.addemo;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import static cc.lkme.addemo.LMConstants.DOWNLOAD_FILE_NAME;
import static cc.lkme.addemo.LMConstants.DOWNLOAD_FILE_URL;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1000;
    EditText deeplinks, package_name;
    EditText app_pkg_name;

    /**
     * 方法描述：判断某一应用是否正在运行 Created by cafeting on 2017/2/4.
     *
     * @param context     上下文
     * @param packageName 应用的包名
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isAppRunning(Context context, String packageName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        if (list.size() <= 0) {
            return false;
        }
        for (ActivityManager.RunningTaskInfo info : list) {
            if (info.baseActivity.getPackageName().equals(packageName)) {
                return true;
            }
        }
        return false;
    }

    //获取已安装应用的 uid，-1 表示未安装此应用或程序异常
    public static int getPackageUid(Context context, String packageName) {
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            if (applicationInfo != null) {
//                Log.d(applicationInfo.uid);
                return applicationInfo.uid;
            }
        } catch (Exception e) {
            return -1;
        }
        return -1;
    }

    /**
     * 判断某一 uid 的程序是否有正在运行的进程，即是否存活 Created by cafeting on 2017/2/4.
     *
     * @param context 上下文
     * @param uid     已安装应用的 uid
     * @return true 表示正在运行，false 表示没有运行
     */
    public static boolean isProcessRunning(Context context, int uid) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServiceInfos = am.getRunningServices(200);
        if (runningServiceInfos.size() > 0) {
            for (ActivityManager.RunningServiceInfo appProcess : runningServiceInfos) {
                if (uid == appProcess.uid) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission(MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1002);
        }
        deeplinks = (EditText) findViewById(R.id.deeplinks);
//        deeplinks.setText("https://lkme.cc/IfC/yGs2hfPK8");
        deeplinks.setText("http://60.205.217.173:9099/browser/test.html");
//        deeplinks.setText("https://www.linkedme.cc/h5/partner");
        package_name = (EditText) findViewById(R.id.package_name);
        package_name.setText("com.ctoutiao");

        TextView appIsOpen = (TextView) findViewById(R.id.appIsOpen);
        appIsOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String pName = "com.microquation.linkedme";
                int uid = getPackageUid(MainActivity.this, pName);
                if (uid > 0) {
                    boolean rstA = isAppRunning(MainActivity.this, pName);
                    boolean rstB = isProcessRunning(MainActivity.this, uid);
                    if (rstA || rstB) {
                        //指定包名的程序正在运行中
                        Toast.makeText(MainActivity.this, "正在运行", Toast.LENGTH_SHORT).show();
                    } else {
                        //指定包名的程序未在运行中
                        Toast.makeText(MainActivity.this, "未运行", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    //应用未安装
                    Toast.makeText(MainActivity.this, "未安装", Toast.LENGTH_SHORT).show();
                }

            }
        });


        TextView openAndH5 = (TextView) findViewById(R.id.openAndH5);
        openAndH5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // 必须先执行打开h5页面再打开App，否则当系统拦截尝试打开其他App时，会被h5页面的activity覆盖，无法看到打开提示对话框


                Intent intentWeb = new Intent(MainActivity.this, CustomWebviewActivity.class);
                intentWeb.putExtra("deeplink_url", deeplinks.getText().toString());
                startActivity(intentWeb);

//                Intent intent = null;
//                try {
//                    intent = Intent.parseUri("lkmedemo://linkedme?click_id=CeG9o5VH8&lkme=1", Intent.URI_INTENT_SCHEME);
//                } catch (URISyntaxException e) {
//                    e.printStackTrace();
//                }
//
//                if (intent != null) {
//                    PackageManager packageManager = getPackageManager();
//                    ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
//                    if (info != null) {
//                        //朗易思听： 如果有可接受该intent的APP则直接唤起APP
//                        //intent.addCategory(Intent.CATEGORY_BROWSABLE);
//                        Toast.makeText(MainActivity.this, "已安装APP并开始唤起", Toast.LENGTH_LONG).show();
//                        startActivity(intent);
//                    }
//                }


            }
        });


        TextView webview = (TextView) findViewById(R.id.webview);
        webview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CustomWebviewActivity.class);
                intent.putExtra("deeplink_url", deeplinks.getText().toString());
                startActivity(intent);
            }
        });
        TextView default_browser = (TextView) findViewById(R.id.default_browser);
        default_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.android.browser");
                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //如果有可接受该intent的APP则直接唤起APP
                    startActivity(intent);
                } else {
                    //未安装该浏览器
                    Toast.makeText(MainActivity.this, "未安装该浏览器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView chrome = (TextView) findViewById(R.id.chrome);
        chrome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.android.chrome");
                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //如果有可接受该intent的APP则直接唤起APP
                    startActivity(intent);
                } else {
                    //未安装该浏览器
                    Toast.makeText(MainActivity.this, "未安装该浏览器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView uc_browser = (TextView) findViewById(R.id.uc_browser);
        uc_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.UCMobile");
                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //如果有可接受该intent的APP则直接唤起APP
                    startActivity(intent);
                } else {
                    //未安装该浏览器
                    Toast.makeText(MainActivity.this, "未安装该浏览器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView qq_browser = (TextView) findViewById(R.id.qq_browser);
        qq_browser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.tencent.mtt");
                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //如果有可接受该intent的APP则直接唤起APP
                    startActivity(intent);
                } else {
                    //未安装该浏览器
                    Toast.makeText(MainActivity.this, "未安装该浏览器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView liebao = (TextView) findViewById(R.id.liebao);
        liebao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(deeplinks.getText().toString()));
                intent.setPackage("com.ijinshan.browser_fast");
                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //如果有可接受该intent的APP则直接唤起APP
                    startActivity(intent);
                } else {
                    //未安装该浏览器
                    Toast.makeText(MainActivity.this, "未安装该浏览器", Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView showAD = (TextView) findViewById(R.id.showAD);
        showAD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            connectService("https://lkme.cc/i/ad/monitor?" + DeviceInfo.getInstance(MainActivity.this).getParams("com.ctoutiao", "0"));
//                            connectService("http://lkme.cc/ad/openapi/get_ad?imei=863267033980153&linkedme_key=4c6d903b4b44eab7c990e0ce6f9c1e03&ad_position=11132_0&os=Android&os_version=6.0.1&device_model=OPPO+R9s+Plus&app_version=5.7.6.4-debug&carrier=%E4%B8%AD%E5%9B%BD%E8%81%94%E9%80%9A&net=WIFI&timestamp=1487744414084");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
        TextView open_package = (TextView) findViewById(R.id.open_package);
        open_package.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String package_name_str = package_name.getText().toString().trim();
                if (!TextUtils.isEmpty(package_name_str)) {
                    if (isPkgInstalled(package_name_str)) {
                        Toast.makeText(MainActivity.this, "已安装APP并开始唤起", Toast.LENGTH_LONG).show();
                        Intent resolveIntent = MainActivity.this.getPackageManager().getLaunchIntentForPackage(package_name_str);// 这里的packname就是从上面得到的目标apk的包名
                        // 启动目标应用
                        if (resolveIntent != null) {
                            MainActivity.this.startActivity(resolveIntent);
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "未安装APP", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "包名不能为空", Toast.LENGTH_LONG).show();
                }
            }
        });
        TextView service_down_apk = (TextView) findViewById(R.id.service_down_apk);
        service_down_apk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "http://60.205.217.173:9099/browser/demo.apk";
                Intent intent = new Intent(MainActivity.this, LMDownloadService.class);
                intent.putExtra(DOWNLOAD_FILE_URL, url);
                intent.putExtra(DOWNLOAD_FILE_NAME, "demo.apk");
                startService(intent);
            }
        });
        TextView uri_scheme = (TextView) findViewById(R.id.uri_scheme);
        uri_scheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UriSchemeActivity.class);
                startActivity(intent);
            }
        });

        TextView uri_scheme_list = (TextView) findViewById(R.id.uri_scheme_list);
        uri_scheme_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, UriSchemeListActivity.class);
                startActivity(intent);
            }
        });

        TextView show_img = (TextView) findViewById(R.id.show_img);
        show_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ShowImg.class);
                startActivity(intent);
            }
        });
        TextView p_chklst = (TextView) findViewById(R.id.p_chklst);
        p_chklst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PChklistActivity.class);
                startActivity(intent);
            }
        });
        app_pkg_name = (EditText)

                findViewById(R.id.app_pkg_name);

        TextView open_instant_app = (TextView) findViewById(R.id.open_instant_app);
        open_instant_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(app_pkg_name.getText().toString())) {
                    Toast.makeText(MainActivity.this, "请输入包名", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Intent intent = getPackageManager().getLaunchIntentForPackage(app_pkg_name.getText().toString().trim());
                        startActivity(intent);
                    } catch (Exception ignore) {
                        ignore.printStackTrace();
                        Toast.makeText(MainActivity.this, "无可打开的App", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        TextView open_mini_app = findViewById(R.id.open_mini_app);
        open_mini_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MiniActivity.class);
                startActivity(intent);
            }
        });
        TextView open_notification = findViewById(R.id.open_notification);
        open_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);
            }
        });

        TextView open_h5listener = findViewById(R.id.open_h5listener);
        open_h5listener.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, H5ListenerActivity.class);
                startActivity(intent);
            }
        });

    }


    private String connectService(String requestUrl) throws IOException {
        InputStream is = null;

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setInstanceFollowRedirects(false);
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setRequestProperty("charset", "utf-8");
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = getStringFromInputStream(is);
            Log.d("LinkedME", "connectService response : " + contentAsString);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    private static String getStringFromInputStream(InputStream is)
            throws IOException {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            os.write(buffer, 0, len);
        }
        is.close();
        String state = os.toString();
        os.close();
        return state;
    }

    public void getPermission(final int permission) {
        String sysPermission = Manifest.permission.READ_PHONE_STATE;
        switch (permission) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE:
                sysPermission = Manifest.permission.READ_PHONE_STATE;
                break;
            default:
                break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    sysPermission)
                    != PackageManager.PERMISSION_GRANTED) {
                //没有权限

                // Should we show an explanation?
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        sysPermission)) {


                    // Show an expanation to the user *asynchronously* -- don't block
                    // this thread waiting for the user's response! After the user
                    // sees the explanation, try again to request the permission.
                    final String finalSysPermission = sysPermission;
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{finalSysPermission},
                            permission);
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{sysPermission},
                            permission);
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "已授予电话的权限", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(this, "未授予电话的权限", Toast.LENGTH_LONG).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 判断本机是否已经安装了APP
     */
    private boolean isPkgInstalled(String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = MainActivity.this.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }
}
