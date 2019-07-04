package cc.lkme.addemo;

import android.Manifest;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kepler.jd.Listener.OpenAppAction;
import com.kepler.jd.login.KeplerApiManager;
import com.kepler.jd.sdk.bean.KeplerAttachParameter;

public class JDActivity extends AppCompatActivity {

    public static final String TAG = "JD";

    private Button openJd, scan, copy;
    private TextView info;
    private EditText jd_http;
    private KeplerAttachParameter mKeplerAttachParameter = new KeplerAttachParameter();
    OpenAppAction mOpenAppAction = new OpenAppAction() {
        @Override
        public void onStatus(final int status, final String url) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG, "run: " + status);
                    if (status == OpenAppAction.OpenAppAction_start) {//开始状态未必一定执行，

//                            dialogShow();
                    } else {
//                            mKelperTask = null;
//                            dialogDiss();
                    }
                    if (status == OpenAppAction.OpenAppAction_result_NoJDAPP) {
                        //未安装京东
                    } else if (status == OpenAppAction.OpenAppAction_result_BlackUrl) {
                        //不在白名单
                    } else if (status == OpenAppAction.OpenAppAction_result_ErrorScheme) {
                        //协议错误
                    } else if (status == OpenAppAction.OpenAppAction_result_APP) {
                        //呼京东成功
                    } else if (status == OpenAppAction.OpenAppAction_result_NetError) {//网络异常
                    }
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jd);
        requestPermission();
        openJd = findViewById(R.id.open_jd);
        jd_http = findViewById(R.id.jd_http);
        scan = findViewById(R.id.scan);
        copy = findViewById(R.id.copy);
        info = findViewById(R.id.info);

        openJd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeplerApiManager.getWebViewService().openAppWebViewPage(JDActivity.this,
                        jd_http.getText().toString(),
                        mKeplerAttachParameter,
                        mOpenAppAction);
            }
        });
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JDActivity.this, ScanActivity.class);
                startActivityForResult(intent, 1000);
            }
        });
        copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cbm != null && cbm.hasPrimaryClip()) {
                    if (cbm.getPrimaryClip().getItemCount() > 0) {
                        jd_http.setText("");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            cbm.getPrimaryClipDescription().getTimestamp();
                        }
                        jd_http.setText(cbm.getPrimaryClip().getItemAt(0).getText());
                    } else {
                        Toast.makeText(JDActivity.this, "剪切板中无内容！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void requestPermission() {
        if (!(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 1001);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.scan, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.scan:
                //add the function to perform here
                Intent intent = new Intent(JDActivity.this, ScanActivity.class);
                startActivityForResult(intent, 1000);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 1000) {
                String result = data.getStringExtra("result");
                jd_http.setText(result);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
