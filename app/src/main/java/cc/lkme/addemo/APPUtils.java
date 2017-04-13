package cc.lkme.addemo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.net.URISyntaxException;

/**
 * Created by LinkedME06 on 16/03/2017.
 */

public class APPUtils {
    /**
     * 判断本机是否已经安装了APP
     */
    public static boolean isPkgInstalled(Context context, String pkgName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo(pkgName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        if (packageInfo == null) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 通过uri scheme获取对应的应用信息
     *
     * @param context    Context
     * @param uri_scheme Uri Scheme
     * @return AppInfo应用信息对象。null则无可处理该uri scheme的应用，反之存在
     */
    public static AppInfo getAppInfoByScheme(Context context, String uri_scheme) {
        AppInfo appInfo = null;
        Intent intent = null;
        try {
            intent = Intent.parseUri(uri_scheme, Intent.URI_INTENT_SCHEME);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
        ResolveInfo resolveInfo = context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        if (resolveInfo != null) {
            appInfo = new AppInfo();
            appInfo.setAppIconDrawable(resolveInfo.loadIcon(context.getPackageManager()));
            appInfo.setPackageName(resolveInfo.activityInfo.packageName);
            appInfo.setAppName((String) resolveInfo.loadLabel(context.getPackageManager()));
            //此处无法获取到icon的id，只能通过loadIcon获取icon的drawable
            appInfo.setAppIcon(resolveInfo.getIconResource());
            appInfo.setUriScheme(uri_scheme);
            appInfo.setInstalled(true);
        }
        return appInfo;
    }

    public static void openAppWithUriScheme(Context context, String uri_scheme, String packageName) {
        try {
            Log.d("linkedme", "openAppWithUriScheme: uri scheme ==== " + uri_scheme);
            if (TextUtils.isEmpty(uri_scheme)) {
                Toast.makeText(context, "Uri Scheme不能为空！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (uri_scheme.contains(" ")) {
                Toast.makeText(context, "Uri Scheme中含有空格！", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = Intent.parseUri(uri_scheme, Intent.URI_INTENT_SCHEME);
            if (!TextUtils.isEmpty(packageName)) {
                if (packageName.contains(" ")) {
                    Toast.makeText(context, "packageName中含有空格！", Toast.LENGTH_SHORT).show();
                    return;
                }
                intent.setPackage(packageName);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ResolveInfo resolveInfo =
                    context.getPackageManager().resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
            if (resolveInfo != null) {
                Toast.makeText(context, "正在跳转...", Toast.LENGTH_SHORT).show();
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "无可处理该Uri Scheme的APP，无法唤起", Toast.LENGTH_SHORT).show();
            }
        } catch (URISyntaxException ignore) {
            Toast.makeText(context, "Uri Scheme解析异常，无法唤起APP", Toast.LENGTH_SHORT).show();
        }
    }

}
