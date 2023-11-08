package magichands.core.tool;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import java.io.IOException;

public class SystemPropertiesTool {

    static class Location {

        private Context context;
        private  LocationManager locationManager;
        private  LocationListener locationListener;
        private android.location.Location lastLocation;

        public Location(Context context) {
            this.context = context;
            this.locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            this.locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(@NonNull android.location.Location location) {
                    lastLocation = location;
                }

                @Override
                public void onProviderDisabled(@NonNull String provider) {}

                @Override
                public void onProviderEnabled(@NonNull String provider) {}

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {}
            };
        }

        Location() {
        }

        public boolean checkPermission() {
            return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }

        public void requestPermission(int requestCode) {
            ActivityCompat.requestPermissions((Activity) context, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, requestCode);
        }

        public boolean isGpsEnabled() {
            return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        }

        public void start() {
            if (checkPermission() && isGpsEnabled()) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        public void stop() {
            locationManager.removeUpdates(locationListener);
        }

        public android.location.Location getLastLocation() {
            return lastLocation;
        }
    }


    /**
     * 获取本机wifi IP 地址
     * @return 本机wifi IP 地址
     */
    public static String getLocalIPAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            @SuppressLint("MissingPermission") WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            String ipString = String.format("%d.%d.%d.%d", (ipAddress & 0xff), (ipAddress >> 8 & 0xff), (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
            return ipString;
        }
        return "";
    }

    /**
     * 获取本机外网 IP 地址
     * @return 本机外网 IP 地址
     */
    public static String getPublicIPAddress() {
        String ipAddress = "";
        String url = "https://api.ipify.org";
        try {
            ipAddress = HttpTool.sendGetRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ipAddress;
    }


    /**
     * 获取当前位置信息
     * @param context 上下文
     * @return 返回包含设备经纬度信息的 Location 对象
     */
    public static String getCurrentLocation(Context context) {
        Looper.prepare();
       Location locationUtil = new Location(context);
        if (!locationUtil.checkPermission()) {
            locationUtil.requestPermission(100);
            return null;

        }else{
            if (!locationUtil.isGpsEnabled()) {
                // 打开 GPS 设置界面
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(intent);
                return null;
            }
        }

        locationUtil.start();
        android.location.Location location = locationUtil.getLastLocation();
        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            // 使用经纬度信息做自己的业务逻辑
            System.out.println("经度：" + latitude);
            System.out.println("纬度：" + longitude);
            return "经度：" + latitude+",纬度：" + longitude;
        }
        locationUtil.stop();
        Looper.loop();
        return null;
    }

    /**

     获取当前 Android 版本号，以字符串形式返回版本信息
     @return 返回包括 Android 版本和 API 级别的字符串
     */
    public static String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android " + release + " (API level " + sdkVersion + ")";
    }
    /**

     获取当前屏幕高度
     @param context 上下文对象
     @return 当前屏幕高度，单位为像素
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }
    public static int getScreenHeightWithStatusBarAndNavigationBar(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);

        int realHeight = metrics.heightPixels;

        // 获取整个屏幕的高度，包括通知栏和虚拟导航栏
        int statusBarHeight = getStatusBarHeight(context);
        int navigationBarHeight = getNavigationBarHeight(context);

        int fullHeight = realHeight + statusBarHeight + navigationBarHeight;

        return fullHeight;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight(Context context) {
        if (hasNavigationBar(context)) {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                return context.getResources().getDimensionPixelSize(resourceId);
            }
        }
        return 0;
    }

    public static boolean hasNavigationBar(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            Point realSize = new Point();
            Point screenSize = new Point();
            display.getRealSize(realSize);
            display.getSize(screenSize);
            return realSize.y != screenSize.y;
        }
        return false;
    }

    /**
     * 获取屏幕宽度
     * @param context 上下文对象
     * @return 屏幕宽度（像素）
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }
    /**
     * 获取屏幕dpi
     *
     * @param context 上下文对象
     * @return 屏幕dpi值
     */
    public static int getScreenDpi(Context context) {
        DisplayMetrics metrics = new DisplayMetrics(); // 创建一个DisplayMetrics对象，用于存储屏幕参数
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE); // 获取WindowManager服务对象
        wm.getDefaultDisplay().getMetrics(metrics); // 获取屏幕参数，将结果存储在metrics对象中
        return metrics.densityDpi; // 返回屏幕dpi值
    }

    /**
     * 获取当前设备时间的时间戳
     *
     * @return 当前设备时间的时间戳，单位为毫秒
     */
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
}
