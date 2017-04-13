package cc.lkme.addemo;

import android.graphics.drawable.Drawable;

/**
 * 应用信息实体类
 * Created by LinkedME06 on 12/04/2017.
 */

public class AppInfo {
    /**
     * 包名
     */
    private String packageName;
    /**
     * 应用名称
     */
    private String appName;
    /**
     * 应用图标，图标id
     */
    private int appIcon;
    /**
     * 应用图标，Drawable类型图标，通过uri scheme获取应用的图标会存到此处
     */
    private Drawable appIconDrawable;
    /**
     * 应用是否安装
     */
    private boolean isInstalled;
    /**
     * 唤起app的uri scheme
     */
    private String uriScheme;

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean installed) {
        isInstalled = installed;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public int getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(int appIcon) {
        this.appIcon = appIcon;
    }

    public Drawable getAppIconDrawable() {
        return appIconDrawable;
    }

    public void setAppIconDrawable(Drawable appIconDrawable) {
        this.appIconDrawable = appIconDrawable;
    }

    public String getUriScheme() {
        return uriScheme;
    }

    public void setUriScheme(String uriScheme) {
        this.uriScheme = uriScheme;
    }

}
