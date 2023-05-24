package cc.lkme.addemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import cc.lkme.addemo.aid.CommonIdHelper;
import cc.lkme.addemo.aid.CommonIdObtainListener;

public class DeviceInfoActivity extends AppCompatActivity {

    private TextView oaid, androidId;
    private Button oaidCopy, androidIdCopy;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_info);
        oaid = findViewById(R.id.oaid);
        new Thread(new Runnable() {
            @Override
            public void run() {
                CommonIdHelper mCommonIdHelper = new CommonIdHelper(new CommonIdObtainListener() {
                    @Override
                    public void OnIdObtain(final String id) {
                        Log.d("oaid", "oaid:" + id);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                oaid.setText(id);
                            }
                        });
                    }
                });
                mCommonIdHelper.getOAID(DeviceInfoActivity.this.getApplicationContext());
            }
        }).start();

        oaidCopy = findViewById(R.id.oaid_copy);
        oaidCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cbm.setPrimaryClip(ClipData.newPlainText("oaid", oaid.getText().toString()));
                Toast.makeText(DeviceInfoActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
            }
        });

        androidId = findViewById(R.id.android_id);
        String androidIdValue = Settings.System.getString(DeviceInfoActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        androidId.setText(androidIdValue);

        androidIdCopy = findViewById(R.id.androidid_copy);
        androidIdCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                cbm.setPrimaryClip(ClipData.newPlainText("androidId", androidId.getText().toString()));
                Toast.makeText(DeviceInfoActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
