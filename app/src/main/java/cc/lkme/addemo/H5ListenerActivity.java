package cc.lkme.addemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.net.URISyntaxException;

public class H5ListenerActivity extends AppCompatActivity {

    EditText uri_scheme, h5;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.h5listener);
        initView();
    }

    private void initView() {
        uri_scheme = findViewById(R.id.uri_scheme);
        h5 = findViewById(R.id.h5);
        // 打开App，如果唤起了则不打开h5页面，否则打开h5页面
        Button button1 = findViewById(R.id.button1);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uriSchemeEmpty()) {
                    return;
                }

                // 必须先执行打开h5页面再打开App，否则当系统拦截尝试打开其他App时，会被h5页面的activity覆盖，无法看到打开提示对话框


                Intent intentWeb = new Intent(H5ListenerActivity.this, CustomWebviewActivity.class);
                intentWeb.putExtra("deeplink_url", h5.getText().toString());
                startActivity(intentWeb);

                Intent intent = null;
                try {
                    intent = Intent.parseUri(uri_scheme.getText().toString(), Intent.URI_INTENT_SCHEME);
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                if (intent != null) {
                    PackageManager packageManager = getPackageManager();
                    ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                    if (info != null) {
                        //朗易思听： 如果有可接受该intent的APP则直接唤起APP
                        //intent.addCategory(Intent.CATEGORY_BROWSABLE);
                        Toast.makeText(H5ListenerActivity.this, "已安装APP并尝试唤起", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean uriSchemeEmpty() {
        if (TextUtils.isEmpty(uri_scheme.getText().toString().trim())) {
            Toast.makeText(this, "uri scheme 为空", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

}
