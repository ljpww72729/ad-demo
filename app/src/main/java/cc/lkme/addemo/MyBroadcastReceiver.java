package cc.lkme.addemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        IWXAPI api = WXAPIFactory.createWXAPI(context, "wx66b4d6aeb1235674", false);
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
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.tencent.mm", PackageManager.GET_ACTIVITIES);
            String versionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
}
