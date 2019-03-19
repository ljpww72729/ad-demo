package cc.lkme.addemo;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import java.net.URISyntaxException;

/**
 * Created by LinkedME06 on 16/10/12.
 */

public class CustomWebviewActivity extends AppCompatActivity {

    private WebView start_webview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        start_webview = (WebView) findViewById(R.id.start_webview);
        WebSettings webSettings = start_webview.getSettings();
        //允许javascript
        webSettings.setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webSettings.setAllowUniversalAccessFromFileURLs(true);
        }
//        start_webview.addJavascriptInterface(new MyJavaScriptInterface(this), "HtmlViewer");
//        start_webview.loadUrl(getIntent().getStringExtra("deeplink_url") + "?"+ DeviceInfo.getInstance(CustomWebviewActivity.this).getParams("com.ctoutiao", "1"));
        start_webview.loadUrl(getIntent().getStringExtra("deeplink_url"));
        start_webview.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //重写该方法是为了处理uri scheme,对于uri scheme则直接唤起APP


                //去掉回车、换行、tab
                String stray_spacing = "[\n\r\t\\p{Zl}\\p{Zp}\u0085]+";
                url = url.trim();
                url = url.replaceAll(stray_spacing, "");
                System.out.println("url ===== " + url);
                String rfc2396regex = "^(([a-zA-Z][a-zA-Z0-9\\+\\-\\.]*)://)(([^/?#]*)?([^?#]*)(\\?([^#]*))?)?(#(.*))?";
                String http_scheme_slashes = "^(https?://)/+(.*)";
                //(?i)后面的匹配不区分大小写
                String all_schemes_pattern = "(?i)^(http|https|ftp|mms|rtsp|wais)://.*";
                if (url.matches(all_schemes_pattern)) {
                    view.loadUrl(url);
                    return false;
                }
                if (url.matches(rfc2396regex)) {
                    showAlert(url);
                    return true;
                }
                view.loadUrl(url);
                return false;

//                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                // TODO Auto-generated method stub
                super.onPageStarted(view, url, favicon);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                // TODO Auto-generated method stub
//                start_webview.loadUrl("javascript:window.HtmlViewer.showHTML('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');");
                super.onPageFinished(view, url);
            }

        });
    }


//    class MyJavaScriptInterface {
//
//        private Context ctx;
//
//        MyJavaScriptInterface(Context ctx) {
//            this.ctx = ctx;
//        }
//        @JavascriptInterface
//        public void showHTML(String html) {
//            new AlertDialog.Builder(ctx).setTitle("HTML").setMessage(html)
//                    .setPositiveButton(android.R.string.ok, null).setCancelable(false).create().show();
//        }
//
//    }

    public void showAlert(final String info) {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Uri Scheme 跳转提示").setMessage("以下为捕获到的Uri Scheme\n\n" + info + "\n\n 是否跳转？").setPositiveButton("跳转", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        openApp(info);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).setNeutralButton("复制", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager cbm = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
                        cbm.setPrimaryClip(ClipData.newPlainText("uri scheme", info));
                        Toast.makeText(CustomWebviewActivity.this, "已复制到剪切板", Toast.LENGTH_SHORT).show();
                    }
                }).create();
        alertDialog.show();
    }

    public void openApp(String url) {
        // 朗易思听： 我们有个三秒的超时逻辑走market，因此当走market的uri scheme的时候，你直接过滤掉，不要唤起应用市场
        try {
            Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

            if (intent != null) {
                //  view.stopLoading();

                PackageManager packageManager = getPackageManager();
                ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                if (info != null) {
                    //朗易思听： 如果有可接受该intent的APP则直接唤起APP
                    //intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    Toast.makeText(CustomWebviewActivity.this, "已安装APP并开始唤起", Toast.LENGTH_LONG).show();
                    startActivity(intent);
                } else {
                    //朗易思听： 走到该逻辑，说明没有安装app，你native调用market唤起应用市场下载app
                    Toast.makeText(CustomWebviewActivity.this, "未安装APP跳转到自定义页面", Toast.LENGTH_LONG).show();
                    //否则加载回调页面
                    String fallbackUrl = intent.getStringExtra("browser_fallback_url");

                    if (!TextUtils.isEmpty(fallbackUrl)) {
                        // 调用外置浏览器加载回调页面,建议外置浏览器加载回调页面
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(fallbackUrl));
                        startActivity(browserIntent);

                        //或者:在WebView中加载
//                                  view.loadUrl(fallbackUrl);
                    } else {
                        // view.loadUrl(url);
//                                    return super.shouldOverrideUrlLoading(view, url);
                    }
                }

            }
        } catch (URISyntaxException e) {
            //对uri语法异常的处理
            e.printStackTrace();
        }
    }


}
