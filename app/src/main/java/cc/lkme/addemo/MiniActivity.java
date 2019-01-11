package cc.lkme.addemo;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MiniActivity extends AppCompatActivity {

    private EditText app_appid, mini_appid, user_name, path, mini_info;
    private Button clear, paste, jump;
    private Spinner mini_type;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mini_act);
        mini_info = findViewById(R.id.mini_info);
        app_appid = findViewById(R.id.app_appid);
        mini_appid = findViewById(R.id.mini_appid);
        user_name = findViewById(R.id.user_name);
        path = findViewById(R.id.path);
        clear = findViewById(R.id.clear);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                app_appid.setText("");
                mini_appid.setText("");
                user_name.setText("");
                path.setText("");
                mini_info.setText("");
                mini_type.setSelection(0);
            }
        });
        mini_type = findViewById(R.id.mini_type);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mini_type, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mini_type.setAdapter(adapter);
        paste = findViewById(R.id.paste);
        paste.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                if (cbm != null && cbm.hasPrimaryClip()) {
                    if (cbm.getPrimaryClip().getItemCount() > 0) {
                        mini_info.setText("");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            cbm.getPrimaryClipDescription().getTimestamp();
                        }
                        mini_info.setText(cbm.getPrimaryClip().getItemAt(0).getText());
                        String miniInfoStr = mini_info.getText().toString();
                        if (!TextUtils.isEmpty(miniInfoStr)) {
                            String[] miniInfoArr = miniInfoStr.split(",");
                            if (miniInfoArr.length != 3) {
                                Toast.makeText(MiniActivity.this, "字符串格式化错误，请检查！", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            app_appid.setText(miniInfoArr[0]);
                            user_name.setText(miniInfoArr[1]);
                            path.setText(miniInfoArr[2]);
                        } else {
                            Toast.makeText(MiniActivity.this, "剪切板中无内容或包含空格！", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MiniActivity.this, "剪切板中无内容！", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        jump = findViewById(R.id.jump);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String appAppidStr = app_appid.getText().toString();
                String userNameStr = user_name.getText().toString();
                String pathStr = path.getText().toString();
                if (TextUtils.isEmpty(appAppidStr)) {
                    Toast.makeText(MiniActivity.this, "移动应用appid不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(userNameStr)) {
                    Toast.makeText(MiniActivity.this, "小程序原始id不能为空！", Toast.LENGTH_LONG).show();
                    return;
                }
//                if (TextUtils.isEmpty(pathStr)) {
//                    Toast.makeText(MiniActivity.this, "小程序原始path路径不能为空！", Toast.LENGTH_LONG).show();
//                    return;
//                }

                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
                    String versionName = packageInfo.versionName;
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(MiniActivity.this, "微信未安装，请先安装微信！", Toast.LENGTH_LONG).show();
                    return;
                }
                IWXAPI api = WXAPIFactory.createWXAPI(MiniActivity.this, appAppidStr, false);
                // 将该app注册到微信
                api.registerApp(appAppidStr);
                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = userNameStr; // 填小程序原始id
//                req.path = "pages/index/index?path=" + URLEncoder.encode("/pages/activityPage/flow/index?q=tnews") + "&mini_appid=" + URLEncoder.encode("wxa344448166586158");
                req.path = pathStr;
                //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                int selectedPostion = mini_type.getSelectedItemPosition();

                switch (selectedPostion) {
                    case 0:
                        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                        break;
                    case 1:
                        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_PREVIEW;// 可选打开 开发版，体验版和正式版
                        break;
                    case 2:
                        req.miniprogramType = WXLaunchMiniProgram.Req.MINIPROGRAM_TYPE_TEST;// 可选打开 开发版，体验版和正式版
                        break;

                }
                api.sendReq(req);

            }
        });
    }
}
