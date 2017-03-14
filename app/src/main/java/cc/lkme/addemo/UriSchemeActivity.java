package cc.lkme.addemo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.net.URISyntaxException;

import cc.lkme.addemo.databinding.ActUriSchemeBinding;

public class UriSchemeActivity extends AppCompatActivity {

    ActUriSchemeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(UriSchemeActivity.this, R.layout.act_uri_scheme);
        setTitle("Uri Scheme");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        binding.jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri_scheme = binding.uriScheme.getText().toString();
                String packageName = binding.packageName.getText().toString();
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
        binding.demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "lkmedemo://?click_id=CeG9o5VH8&lkme=1";
                String packageName = "com.microquation.linkedme";
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
        binding.snssdk143.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "snssdk143://detail?groupid=6392048944789553409&gd_label=click_schema_mk001";
                String packageName = "com.ss.android.article.news";
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
        binding.taobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "taobao://shop.m.taobao.com/shop/shop_index.htm?shop_id=131259851&spm=a230r.7195193.1997079397.8.Pp3ZMM&point%7B%22from%22%3A%22h5%22%2C%22url%22%3A%22http%3A%2F%2Fshop131259851.m.taobao.com%2F%22%2C%22h5_uid%22%3A%22aJHIDrhLh0cCAYYbRtrrS3Ut%22%2C%22uaPoint%22%3A%22Mozilla%2F5.0%2520(Linux%3B%2520Android%25205.0.1%3B%2520SM-N9109W%2520Build%2FLRX22C%3B%2520wv)%2520AppleWebKit%2F537.36%2520(KHTML%2C%2520like%2520Gecko)%2520Version%2F4.0%2520Chrome%2F43.0.2357.121%2520Mobile%2520Safari%2F537.36%2520Rong%2F2.0%22%7D";
                String packageName = "com.taobao.taobao";
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
        binding.newsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "newsapp://doc/CEQTTJ9G000380BQ?s=sps";
                String packageName = "com.netease.newsreader.activity";
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
        //雾霾地图
        binding.baidumap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "baidumap://map/nearbysearch?query=%e9%9b%be%e9%9c%be%e5%9c%b0%e5%9b%be&src=thirdapp.nearbysearch.lkme";
//                String uri_scheme = "baidumap://map/nearbysearch?query=%e9%9b%be%e9%9c%be%e5%9c%b0%e5%9b%be&src=thirdapp.nearbysearch.wps.lkm";
                String packageName = "com.baidu.BaiduMap";
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
        binding.sinaweiboHuati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "sinaweibo://pageinfo?containerid=10080890c2ed87d7deeb07ddfa270061b2c986&luicode=10000360&lfid=Linkedme";
                String packageName = "com.sina.weibo";
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
        binding.sinaweiboWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "sinaweibo://detail?mblogid=mid&luicode=10000360&lfid=Linkedme";
                String packageName = "com.sina.weibo";
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
        binding.iqiyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "iqiyi://mobile/player?aid=203198201&ftype=27&subtype=124";
                String packageName = "com.qiyi.video";
                openAppWithUriScheme(uri_scheme, packageName);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id){
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void openAppWithUriScheme(String uri_scheme, String packageName) {
        try {
            Log.d("linkedme", "openAppWithUriScheme: uri scheme ==== " + uri_scheme);
            if (TextUtils.isEmpty(uri_scheme)) {
                Toast.makeText(UriSchemeActivity.this, "Uri Scheme不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (uri_scheme.contains(" ")){
                Toast.makeText(UriSchemeActivity.this, "Uri Scheme中含有空格！", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = Intent.parseUri(uri_scheme, Intent.URI_INTENT_SCHEME);
            if (!TextUtils.isEmpty(packageName)) {
                if (packageName.contains(" ")){
                    Toast.makeText(UriSchemeActivity.this, "packageName中含有空格！", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setPackage(packageName);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ResolveInfo resolveInfo = UriSchemeActivity.this.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfo != null) {
                Toast.makeText(UriSchemeActivity.this, "正在跳转...", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            } else {
                Toast.makeText(UriSchemeActivity.this, "无可处理该Uri Scheme的APP，无法唤起", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException ignore) {
            Toast.makeText(UriSchemeActivity.this, "Uri Scheme解析异常，无法唤起APP", Toast.LENGTH_SHORT).show();
        }
    }


}
