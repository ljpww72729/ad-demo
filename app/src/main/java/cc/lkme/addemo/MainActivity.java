package cc.lkme.addemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 1000;
    EditText deeplinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission(MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        deeplinks = (EditText) findViewById(R.id.deeplinks);
        deeplinks.setText("https://lkme.cc/IfC/yGs2hfPK8");
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
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
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
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = getStringFromInputStream(is);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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

}
