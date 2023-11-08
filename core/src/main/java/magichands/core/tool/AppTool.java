package magichands.core.tool;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

public class AppTool {


    /**
     * 清除指定应用程序的数据
     *
     * @param packageName 应用程序的包名
     * @return 如果清除成功，则返回 true；否则返回 false
     */
    public static boolean clearAppData(Context context, String packageName) {
        try {
            // 获取PackageManager对象
            PackageManager packageManager = context.getPackageManager();
            // 获取ApplicationInfo对象
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            // 创建ComponentName对象
            ComponentName componentName = new ComponentName(applicationInfo.packageName, "com.android.settings.Settings$ApplicationDetailsActivity");
            // 创建Intent对象
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setComponent(componentName);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("pkg", applicationInfo.packageName);
            // 启动Intent
            context.startActivity(intent);
            // 返回结果
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
