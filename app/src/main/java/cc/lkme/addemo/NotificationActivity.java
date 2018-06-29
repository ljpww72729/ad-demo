package cc.lkme.addemo;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class NotificationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_notifaction);
        Button noty_one = findViewById(R.id.noty_one);
        noty_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an explicit intent for an Activity in your app
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                PendingIntent pendingIntent = PendingIntent.getActivity(NotificationActivity.this, 0, intent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationActivity.this, "1")
                        .setSmallIcon(R.drawable.no_app)
                        .setContentTitle("广告")
                        .setContentText("点击通知后打开应用主页")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);

// notificationId is a unique int for each notification that you must define
                notificationManager.notify(11, mBuilder.build());
            }
        });
        Button noty_two = findViewById(R.id.noty_two);
        noty_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent snoozeIntent = new Intent(NotificationActivity.this, MyBroadcastReceiver.class);
                snoozeIntent.putExtra("userName", "gh_b83a75769dc2");
                snoozeIntent.putExtra("path", "pages/logs/logs?from=linkedme&abc=bcd");
                PendingIntent snoozePendingIntent =
                        PendingIntent.getBroadcast(NotificationActivity.this, 0, snoozeIntent, 0);

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(NotificationActivity.this, "1")
                        .setSmallIcon(R.drawable.no_app)
                        .setContentTitle("广告")
                        .setContentText("点击打开微信小程序")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        // Set the intent that will fire when the user taps the notification
                        .setContentIntent(snoozePendingIntent)
                        .setAutoCancel(true);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(NotificationActivity.this);

// notificationId is a unique int for each notification that you must define
                notificationManager.notify(12, mBuilder.build());
            }
        });
        Button noty_three = findViewById(R.id.noty_three);
        noty_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IWXAPI api = WXAPIFactory.createWXAPI(NotificationActivity.this, "wx66b4d6aeb1235674", false);
                // 将该app注册到微信
                api.registerApp("wx66b4d6aeb1235674");
                WXLaunchMiniProgram.Req req = new WXLaunchMiniProgram.Req();
                req.userName = "gh_ff07d6a341b5"; // 填小程序原始id
//                req.path = "pages/index/index?path=" + URLEncoder.encode("/pages/activityPage/flow/index?q=tnews") + "&mini_appid=" + URLEncoder.encode("wxa344448166586158");
                req.path = "/pages/activityPage/flow/index?q=tnews";
                //拉起小程序页面的可带参路径，不填默认拉起小程序首页
                req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE;// 可选打开 开发版，体验版和正式版
                api.sendReq(req);
                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
                    String versionName = packageInfo.versionName;

                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
