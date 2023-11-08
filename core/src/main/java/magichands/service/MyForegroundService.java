package magichands.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class MyForegroundService extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "ForegroundServiceChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        // 创建通知渠道（仅适用于Android O及以上版本）
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            System.out.println("====================保活中");
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

        }else {
            // 在 Android 8 以下版本上，直接启动前台服务
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setContentTitle("Foreground Service")
                    .setContentText("Service is running")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            Notification notification = notificationBuilder.build();
            startForeground(1, notification);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 创建前台通知
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("保活服务")
                .setContentText("正常运行...")
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .build();

        // 将服务注册为前台服务
        startForeground(NOTIFICATION_ID, notification);

        // 执行你的任务逻辑...

        // 如果服务被终止，不要重新创建
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 停止前台服务
        stopForeground(true);
    }
}
