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
        if (APPUtils.isPkgInstalled(UriSchemeActivity.this, "com.ss.android.article.news")) {
            binding.snssdk143.setText(binding.snssdk143.getText().toString() + "(已安装)");
        }
        if (APPUtils.isPkgInstalled(UriSchemeActivity.this, "com.netease.newsreader.activity")) {
            binding.newsapp.setText(binding.newsapp.getText().toString() + "(已安装)");
        }
        if (APPUtils.isPkgInstalled(UriSchemeActivity.this, "com.baidu.BaiduMap")) {
            binding.baidumap.setText(binding.baidumap.getText().toString() + "(已安装)");
        }
        if (APPUtils.isPkgInstalled(UriSchemeActivity.this, "com.sina.weibo")) {
            binding.sinaweiboWeibo.setText(binding.sinaweiboWeibo.getText().toString() + "(已安装)");
            binding.sinaweiboHuati.setText(binding.sinaweiboHuati.getText().toString() + "(已安装)");
        }
        if (APPUtils.isPkgInstalled(UriSchemeActivity.this, "com.qiyi.video")) {
            binding.iqiyi.setText(binding.iqiyi.getText().toString() + "(已安装)");
        }
        binding.jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri_scheme = binding.uriScheme.getText().toString();
                String packageName = binding.packageName.getText().toString();
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
        binding.demo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "lkmedemo://?click_id=CeG9o5VH8&lkme=1";
                String packageName = "com.microquation.linkedme";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
        binding.snssdk143.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "snssdk143://detail?groupid=6392048944789553409&gd_label=click_schema_mk001";
                String packageName = "com.ss.android.article.news";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
        binding.taobao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "taobao://shop.m.taobao.com/shop/shop_index.htm?shop_id=131259851&spm=a230r.7195193.1997079397.8.Pp3ZMM&point%7B%22from%22%3A%22h5%22%2C%22url%22%3A%22http%3A%2F%2Fshop131259851.m.taobao.com%2F%22%2C%22h5_uid%22%3A%22aJHIDrhLh0cCAYYbRtrrS3Ut%22%2C%22uaPoint%22%3A%22Mozilla%2F5.0%2520(Linux%3B%2520Android%25205.0.1%3B%2520SM-N9109W%2520Build%2FLRX22C%3B%2520wv)%2520AppleWebKit%2F537.36%2520(KHTML%2C%2520like%2520Gecko)%2520Version%2F4.0%2520Chrome%2F43.0.2357.121%2520Mobile%2520Safari%2F537.36%2520Rong%2F2.0%22%7D";
                String packageName = "com.taobao.taobao";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
        binding.newsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "newsapp://doc/CEQTTJ9G000380BQ?s=sps";
                String packageName = "com.netease.newsreader.activity";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
        //雾霾地图
        binding.baidumap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "baidumap://map/nearbysearch?query=%e9%9b%be%e9%9c%be%e5%9c%b0%e5%9b%be&src=thirdapp.nearbysearch.lkme";
//                String uri_scheme = "baidumap://map/nearbysearch?query=%e9%9b%be%e9%9c%be%e5%9c%b0%e5%9b%be&src=thirdapp.nearbysearch.wps.lkm";
                String packageName = "com.baidu.BaiduMap";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
        binding.baidubox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uri_scheme = "baiduboxapp://utils?action=sendIntent&minver=7.4&params=%7B%22intent%22%3A%22intent%3A%23Intent%3Baction%3Dcom.baidu.searchbox.action.HOME%3Bcomponent%3Dcom.baidu.searchbox%2F.MainActivity%3BS.targetCommand%3D%257B%2522mode%2522%253A%25220%2522%252C%2522intent%2522%253A%2522intent%253A%2523Intent%253BB.bdsb_append_param%253Dtrue%253BS.bdsb_light_start_url%253Dhttps%25253A%25252F%25252Fmbd.baidu.com%25252Fsearchbox%25253Faction%25253Dnovel%252526type%25253Dspecial%252526ua%25253D640_1136_iphone_8.1.0.11_0%252526ut%25253DiPhone7%252525252C2_10.1.1%252526from%25253D1099a%252526osname%25253Dbaiduboxapp%252526osbranch%25253Di0%252526cfrom%25253D1099a%252526service%25253Dbdbox%252526network%25253D1_0%252526sid%25253D226_457-198_387-189_356-176_318-156_266-1000106_268%252526puid%25253Dgav18gOW28eNA%252526data%25253D%2525257B%25252522sid%25252522%25253A266%25252C%25252522fromaction%25252522%25253A%25252522%25252525E4%25252525B8%2525252593%25252525E9%25252525A2%2525252598%25252525E5%2525252588%2525252597%25252525E8%25252525A1%25252525A8%25252522%2525257D%253Bend%2522%252C%2522class%2522%253A%2522com.baidu.searchbox.xsearch.UserSubscribeCenterActivity%2522%252C%2522min_v%2522%253A%252216787968%2522%257D%3Bend%22%7D&needlog=1&logargs=%7B%22source%22%3A%22other%22%2C%22from%22%3A%22openbox%22%2C%22page%22%3A%22other%22%2C%22type%22%3A%22%22%2C%22value%22%3A%22url%22%2C%22channel%22%3A%22%22%7D";
//                String uri_scheme = "baidumap://map/nearbysearch?query=%e9%9b%be%e9%9c%be%e5%9c%b0%e5%9b%be&src=thirdapp.nearbysearch.wps.lkm";
//                String packageName = "com.baidu.BaiduMap";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, null);
            }
        });
        binding.sinaweiboHuati.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "sinaweibo://pageinfo?containerid=10080890c2ed87d7deeb07ddfa270061b2c986&luicode=10000360&lfid=Linkedme";
                String packageName = "com.sina.weibo";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
        binding.sinaweiboWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "sinaweibo://detail?mblogid=mid&luicode=10000360&lfid=Linkedme";
                String packageName = "com.sina.weibo";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
        binding.iqiyi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri_scheme = "iqiyi://mobile/player?aid=203198201&ftype=27&subtype=124";
                String packageName = "com.qiyi.video";
                APPUtils.openAppWithUriScheme(UriSchemeActivity.this, uri_scheme, packageName);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int item_id = item.getItemId();
        switch (item_id) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
