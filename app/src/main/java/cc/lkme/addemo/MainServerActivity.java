package cc.lkme.addemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainServerActivity extends AppCompatActivity {

    private Button adH5, normalH5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_server);
        adH5 = findViewById(R.id.ad_h5);
        adH5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainServerActivity.this, CustomWebviewActivity.class);
                intent.putExtra("deeplink_url","https://www.linkedme.cc/verify/oneKeyLogin.html/req-localhost.html");
                startActivity(intent);
            }
        });
        normalH5 = findViewById(R.id.normal_h5);
        normalH5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainServerActivity.this, CustomWebviewActivity.class);
                intent.putExtra("deeplink_url","https://www.baidu.com");
                startActivity(intent);
            }
        });

    }
}
