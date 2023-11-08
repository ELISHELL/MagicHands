package magichands.core;

import static com.tencent.yolov8ncnn.Yolov8Ncnn.speed;
import static magichands.core.OpenApi.device.getCurrentTimestamp;
import static magichands.core.OpenApi.file.deleteFile;
import static magichands.core.OpenApi.http.sendGetRequest;
import static magichands.core.OpenApi.time.timestampToStandardTime;
import static magichands.core.OpenApi.windows.clear;
import static magichands.core.debug.Interaction.sendLogToServer;
import static magichands.core.debug.Windows.runHandler;
import static magichands.core.debug.Windows.ws;
import static magichands.core.debug.Windows.wsShow;
import static magichands.core.debug.tool.method;
import static magichands.core.engine.chaquopy.start;
import static magichands.core.model.accessibility.AppAccessibilityService.acc;
import static magichands.core.model.adb.run.runShell;
import static magichands.core.tool.HttpTool.generateJwtToken;
import static magichands.core.tool.StringTool.isStartWithStorage;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.inputmethodservice.InputMethodService;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.benjaminwan.ocrlibrary.OcrEngine;
//import com.example.opencvlibrary.OpencvOper;
import com.github.gzuliyujiang.oaid.DeviceID;
import com.github.gzuliyujiang.oaid.DeviceIdentifier;
import com.github.toyobayashi.nodeexample.NodeJs;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tencent.yolov8ncnn.Yolov8Ncnn;

import net.lingala.zip4j.exception.ZipException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ai.onnxruntime.OrtSession;
import cn.vove7.auto.core.api.Nav_apiKt;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import magichands.core.engine.chaquopy;
import magichands.core.model.accessibility.AndroidPlatformReflectionUtils;
import magichands.core.model.accessibility.AppAccessibilityService;
import magichands.core.model.accessibility.inf.find;
import magichands.core.model.accessibility.inf.refresh;
import magichands.core.model.accessibility.obj.call;
import magichands.core.model.accessibility.obj.privateApi;
import magichands.core.model.adb.run;
import magichands.core.tool.ClipBoardTool;
import magichands.core.tool.ColorTool;
import magichands.core.tool.FileTool;
import magichands.core.tool.HttpTool;
import magichands.core.tool.ImageTool;
import magichands.core.tool.InputMethodTool;
import magichands.core.tool.LoadDexTool;
import magichands.core.tool.OnnxTool;
import magichands.core.tool.SystemPropertiesTool;
import magichands.core.tool.TimeTool;
import magichands.core.tool.YoloTool;
import magichands.core.tool.ZipTool;
import magichands.core.tool.socket.webSocketClient;
import magichands.core.tool.socket.webSocketServer;
import magichands.core.ui.vue.InterActive;
import okhttp3.FormBody;

public class OpenApi {
    //    @SuppressLint("StaticFieldLeak")
//    static Context context = Env.Companion.getContext();
//    @SuppressLint("StaticFieldLeak")
//    static Activity activity = Env.Companion.getActivity();
//
//    @SuppressLint("StaticFieldLeak")
//    static AppCompatActivity appCompatActivity = Env.Companion.getAppCompatActivity();
//
//    @SuppressLint("StaticFieldLeak")
//    static AppAccessibilityService appAccessibilityService = Env.Companion.getAppAccessibilityService();
//
//    @SuppressLint("StaticFieldLeak")
//    static InputMethodService currentInputConnection = Env.Companion.getCurrentInputConnection();

    static privateApi privateApi = new privateApi();

    static Boolean START_STATE;

    private static NodeJs nodejs;

    public static Context getCon() {

        return Env.Companion.getContext();
    }

    public static Activity getAct() {

        return Env.Companion.getActivity();
    }

    public static AppAccessibilityService getAcc() {

        return Env.Companion.getAppAccessibilityService();
    }

    public static AppCompatActivity getAca() {

        return Env.Companion.getAppCompatActivity();
    }

    public static AppCompatActivity getApc() {

        return Env.Companion.getAppCompatActivity();
    }

    public static InputMethodService getIms() {

        return Env.Companion.getCurrentInputConnection();

    }

    public static void sleep(Object tim) {
        privateApi.delay(tim);
    }

    public static void start(Boolean s, Boolean m) {

        new Thread("js") {
            @Override
            public void run() {
                START_STATE = true;
                deleteFile("/storage/emulated/0/log.txt");
                clear();
                magichands.core.engine.rhino.start(s, m);
                OpenApi.windows.stop();
                START_STATE = false;
            }
        }.start();
    }


    public static void toast(String hh, String tag, String msg) {
        String currentTime = timestampToStandardTime(getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss");
        method("MagicHands运行日志：toast：(" + currentTime + " Tag：" + tag + ")" + ":" + msg);
        sendLogToServer("MagicHands运行日志：toast：(" + currentTime + " Tag：" + tag + ")" + ":" + msg);
        Log.d("MagicHands运行日志", "toast：(" + currentTime + " Tag：" + tag + ")" + ":" + msg);
        android.os.Handler mainHandler = new android.os.Handler(Looper.getMainLooper());
        System.out.println("悬浮窗状态:" + ws);
        if (ws) {
            if (Settings.canDrawOverlays(getCon())) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            runHandler(2, magichands.core.Handler.getHandler("getDebugWindows"), "MagicHands运行日志：toast：(" + currentTime + " Tag：" + tag + ")" + ":" + msg);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    android.os.Handler hostHandler = magichands.core.Handler.getHandler("getToast");
                    Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                    message.what = 1;
                    message.obj = msg;
                    assert hostHandler != null;
                    hostHandler.sendMessage(message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public static void logd(String hh, String tag, String msg) {
        String currentTime = timestampToStandardTime(getCurrentTimestamp(), "yyyy-MM-dd HH:mm:ss");
        method("MagicHands运行日志：logd：(" + currentTime + " Tag：" + tag + ")" + ":" + msg);
        sendLogToServer("MagicHands运行日志：logd：(" + currentTime + " Tag：" + tag + ")" + ":" + msg);
        Log.d("MagicHands运行日志", "logd：(" + currentTime + " Tag：" + tag + ")" + ":" + msg);
        if (ws) {

            if (Settings.canDrawOverlays(getCon())) {
                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            runHandler(2, magichands.core.Handler.getHandler("getDebugWindows"), "MagicHands运行日志：logd：(" + currentTime + " Tag：" + tag + ")" + ":" + msg);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }

    }

    public static void exit() {
        try {
            throw new Exception("停止");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

    public static String getExternalFilesDir() {
        return getCon().getExternalFilesDir("").getParent();
    }

    public static Object pairArray(int x, int y, int x1, int y1) {

        return privateApi.pairArray(x, y, x1, y1);
    }

    public static Object pairArray(int x, int y, int x1, int y1, int x2, int y2) {

        return privateApi.pairArray(x, y, x1, y1, x2, y2);
    }

    public static Object pairArray(int x, int y, int x1, int y1, int x2, int y2, int x3, int y3) {

        return privateApi.pairArray(x, y, x1, y1, x2, y2, x3, y3);
    }


    public static Object listpairArray(Object a, Object b) {

        return privateApi.listpairArray(a, b);
    }

    public static Object listpairArray(Object a, Object b, Object c) {

        return privateApi.listpairArray(a, b, c);
    }

    public static Object listpairArray(Object a, Object b, Object c, Object d) {

        return privateApi.listpairArray(a, b, c, d);
    }

    public static float[][][][] Float4DArray(String jsonString) {
        return OnnxTool.Float4DArray(jsonString);

    }

    public static void nodejsStart() {

        try {
            nodejs = NodeJs.create();
        } catch (Exception e) {
            System.out.println("报错:" + e);
        }

    }

    public static String nodejsEval(String src) {

        String str = null;
        try {
            str = nodejs.evalString(src);
            Log.d("运行结果", str);
        } catch (Exception e) {
            System.out.println(e);
        }

        return str;

    }


    public static class ai {

        public static class model {
            public static OrtSession onnxcreate(String path) {
                return OnnxTool.onnxcreate(path);

            }

            public static List<Long> illation(float[][][][] in, String type) {
                return OnnxTool.illation(in, type);

            }

        }

        public static class detect {
            public static Yolov8Ncnn.Obj[] V8Detect(Bitmap bitmap) {
                return YoloTool.Detect(bitmap);

            }


            public static Double V8speed() {

                return speed;

            }

            public static Boolean V8Load(String name, String[] list, int current_model, int current_cpugpu) {

                if (!Env.Companion.getRun()) {
                    if (!isStartWithStorage(name)) {
                        name = getExternalFilesDir() + "/files/debug/assets/" + name;
                    }
                }
                return YoloTool.YoloV8Load(name, list, current_model, current_cpugpu);
            }

            public static Bitmap V8Draw(Yolov8Ncnn.Obj[] objects, Bitmap b) {

                return YoloTool.draw(objects, b);

            }

        }

        public static class ocr {

            public static OcrEngine OcrEngine() {
                OcrEngine ocr = new OcrEngine(getCon());
                return ocr;
            }

        }

    }

    public static class app {
        public static Object openApp(int m, Object pkg) {
            switch (m) {
                case 1:
                    Intent launchIntent = getCon().getPackageManager().getLaunchIntentForPackage(pkg.toString());
                    if (launchIntent != null) {
                        // 目标应用已安装，打开它
                        getCon().startActivity(launchIntent);
                        return true;
                    } else {
                        // 目标应用未安装
                        // 处理此情况的代码
                        return false;
                    }
                case 2:
                    return privateApi.openApp(pkg);

                default:
                    return false;

            }

        }

        public static void openScheme(String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            String intentUri = intent.toUri(Intent.URI_INTENT_SCHEME);
            getCon().startActivity(intent);

        }
    }

    public static class button {
        public static Object home() {
            return Nav_apiKt.home();
        }

        public static Object back() {
            return Nav_apiKt.back();
        }

        public static Object quickSettings() {
            return Nav_apiKt.quickSettings();
        }


        public static Object powerDialog() {
            return Nav_apiKt.powerDialog();
        }

        public static Boolean pullNotificationBar() {
            return Nav_apiKt.pullNotificationBar();
        }


        public static Boolean recents() {
            return Nav_apiKt.recents();
        }


        public static Boolean lockScreen() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return Nav_apiKt.lockScreen();
            }
            return false;
        }


        public static Boolean screenShot() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                return Nav_apiKt.screenShot();
            }
            return false;
        }

        public static Boolean splitScreen() {
            return Nav_apiKt.splitScreen();
        }

    }


    public static class color {

        public static int getRgb(Bitmap b, int x, int y) {
            return ColorTool.getRGBColorAtPoint(b, x, y);
        }

//        public static int getR(Bitmap b, int x, int y) {
//            return ColorTool.getR(b, x, y);
//        }
//
//        public static int getG(Bitmap b, int x, int y) {
//            return ColorTool.getG(b, x, y);
//        }
//
//        public static int getB(Bitmap b, int x, int y) {
//            return ColorTool.getB(b, x, y);
//        }
//
//        public static Point[] findMultiColor(Bitmap bitmap, String color, String colorMulti, double threshold, int sx, int sy, int ex, int ey, int count, int direction) {
//            OpencvOper Oper = new com.example.opencvlibrary.OpencvOper();
//            try {
//                return ColorTool.findMultiColor(Oper.BitmapToMat(bitmap), color, colorMulti, threshold, sx, sy, ex, ey, count, direction);
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            return null;
//        }
    }


    public static class device {

//        public static String getAndroidID() {
//            String androidID = Settings.Secure.getString(getCon().getContentResolver(), Settings.Secure.ANDROID_ID);
//            return androidID;
//        }


        public static void unlockDevice() {
            Context context = getCon();

            // 获取 PowerManager 和 KeyguardManager 实例
            PowerManager powerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);

            if (powerManager == null || keyguardManager == null) {
                // 检查获取服务是否成功
                return;
            }

            // 检查屏幕状态
            if (!powerManager.isInteractive()) { // 如果屏幕处于休眠状态
                // 创建唤醒锁并请求唤醒设备
                @SuppressLint("InvalidWakeLockTag")
                PowerManager.WakeLock wakeLock = powerManager.newWakeLock(
                        PowerManager.PARTIAL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP,
                        "ScreenOn");
                wakeLock.acquire(1000L); // 保持唤醒 1 秒
            }

            // 禁用设备的锁屏
            KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("unLock");
            keyguardLock.disableKeyguard();
        }

        public static void openAccessibilitySettings() {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            getCon().startActivity(intent);
        }

        public static Boolean checkAccessibilityPermissions() {
            AccessibilityManager accessibilityManager = (AccessibilityManager) getCon().getSystemService(Context.ACCESSIBILITY_SERVICE);
            String packageName = getCon().getPackageName();
            boolean isServiceEnabled = false;

            List<AccessibilityServiceInfo> enabledServices = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK);

            for (AccessibilityServiceInfo service : enabledServices) {
                if (service.getResolveInfo().serviceInfo.packageName.equals(packageName)) {
                    isServiceEnabled = true;
                    break;
                }
            }

            if (isServiceEnabled) {
                // 应用程序已经被授予了辅助功能权限
                return true;
            } else {
                // 应用程序没有被授予辅助功能权限，跳转到无障碍设置界面
                return false;
            }
        }


        public static String getImei() {
            return DeviceIdentifier.getIMEI(getAca());
        }


        public static String getAndroidID() {
            return DeviceIdentifier.getAndroidID(getAca());
        }

        public static String getWidevineID() {
            return DeviceIdentifier.getWidevineID();
        }

        public static String getPseudoID() {
            return DeviceIdentifier.getPseudoID();
        }

        public static String getGUID() {
            return DeviceIdentifier.getGUID(getAca());
        }

        public static Boolean supportedOAID() {
            return DeviceID.supportedOAID(getAca());
        }

        public static String getOAID() {
            return DeviceID.getUniqueID(getAca());
        }

        public static int getStatusBarHeight(Context context) {
            return SystemPropertiesTool.getStatusBarHeight(context);
        }

        public static int getNavigationBarHeight(Context context) {
            return SystemPropertiesTool.getNavigationBarHeight(context);
        }


        public static int getScreenHeight(Context context) {
            return SystemPropertiesTool.getScreenHeight(context);
        }

        public static int getScreenWidth(Context context) {
            return SystemPropertiesTool.getScreenWidth(context);
        }


        public static int getScreenDpi(Context context) {
            return SystemPropertiesTool.getScreenDpi(context);
        }


        public static long getCurrentTimestamp() {
            return SystemPropertiesTool.getCurrentTimestamp();
        }


        public static String getLocalIPAddress(Context context) {
            return SystemPropertiesTool.getLocalIPAddress(context);
        }


        public static String getPublicIPAddress() {
            return SystemPropertiesTool.getPublicIPAddress();
        }


        public static String getCurrentLocation(Context context) {
            return SystemPropertiesTool.getCurrentLocation(context);

        }


        public static String getAndroidVersion() {
            return SystemPropertiesTool.getAndroidVersion();
        }

        public static String getClipboardContent() {
            return ClipBoardTool.getClipboardContent(getCon());
        }

        public static Boolean setClipboardContent(String text) {
            return ClipBoardTool.setClipboardContent(getCon(), text);
        }

    }

    public static class file {

        /**
         * 判断指定路径的文件是否存在
         *
         * @param filePath 文件路径
         * @return 存在返回 true，否则返回 false
         */
        public static boolean isFileExists(String filePath) {

            return FileTool.isFileExists(filePath);
        }

        /**
         * 判断指定路径的文件夹是否存在
         *
         * @param dirPath 文件夹路径
         * @return 存在返回 true，否则返回 false
         */
        public static boolean isDirectoryExists(String dirPath) {

            return FileTool.isDirectoryExists(dirPath);
        }

        /**
         * 读取指定文件中所有行的内容
         *
         * @param filePath 文件路径
         * @return 包含所有行内容的字符串数组，如果文件不存在或读取失败，则返回 null
         */
        public static String readLinesFromFile(String filePath) {

            return Arrays.toString((FileTool.readLinesFromFile(filePath)));
        }

        /**
         * 读取指定文件中指定行的内容
         *
         * @param filePath   文件路径
         * @param lineNumber 行号（从 1 开始）
         * @return 指定行的内容字符串，如果文件不存在或行号不正确，则返回 null
         */
        public static String readLineFromFile(String filePath, int lineNumber) {

            return FileTool.readLineFromFile(filePath, lineNumber);
        }

        /**
         * 删除指定文件夹
         *
         * @param folderPath 文件夹路径
         * @return 如果成功删除，则返回 true，否则返回 false
         */
        public static boolean deleteFolder(String folderPath) {

            return FileTool.deleteFolder(folderPath);
        }

        /**
         * 创建文件夹，如果文件夹已存在，则不执行任何操作
         *
         * @param folderPath 文件夹路径
         * @return 如果成功创建或文件夹已经存在，则返回 true；否则返回 false
         */
        public static boolean createFolder(String folderPath) {

            return FileTool.createFolder(folderPath);
        }

        public static boolean saveInputStreamToFile(String filePath, InputStream inputStream) {

            return FileTool.saveInputStreamToFile(inputStream, filePath);
        }

        /**
         * 获取指定文件夹下所有文件名的列表
         *
         * @param folderPath 文件夹路径
         * @return 文件名列表；如果文件夹不存在或者不是一个文件夹，返回 null
         */
        public static List<String> listFolderFiles(String folderPath) {

            return FileTool.listFolderFiles(folderPath);
        }

        /**
         * 将内容追加写入文本文件，并自动换行
         *
         * @param filePath 文件路径
         * @param content  要写入的内容
         * @return 如果写入成功，则返回 true；否则返回 false
         */
        public static boolean appendTextToFile(String filePath, String content) {
            return FileTool.appendTextToFile(filePath, content);
        }

        /**
         * 将内容写入文本文件（覆盖原有内容）
         *
         * @param filePath 文件路径
         * @param content  要写入的内容
         * @return 如果写入成功，则返回 true；否则返回 false
         */
        public static boolean writeTextToFile(String filePath, String content) {
            return FileTool.writeTextToFile(filePath, content);
        }

        /**
         * 删除指定的文件
         *
         * @param filePath 要删除的文件路径
         * @return 如果删除成功，则返回 true；否则返回 false
         */
        public static boolean deleteFile(String filePath) {
            return FileTool.deleteFile(filePath);
        }

        /**
         * 创建一个新文件
         *
         * @param filePath 文件路径
         * @return 如果创建成功，则返回 true；否则返回 false
         */
        public static boolean createNewFile(String filePath) {
            return FileTool.createNewFile(filePath);
        }

        /**
         * 在给定文件中查找指定的文本，并返回第一次出现的行号。如果没有找到文本，则返回-1。
         *
         * @param filePath   要查找的文件路径
         * @param searchText 要查找的文本
         * @return 第一次出现指定文本的行号，如果没有找到返回-1
         * @throws RuntimeException 如果发生任何I/O错误
         */
        public static int findLineNumber(String filePath, String searchText) {

            return FileTool.findLineNumber(filePath, searchText);
        }

        /**
         * 替换文件中的指定字符串为另一个字符串
         *
         * @param filePath  文件路径
         * @param oldString 要替换的字符串
         * @param newString 替换成的字符串
         * @return 成功返回 true，失败返回 false
         * @throws IOException 如果文件操作失败抛出 IOException
         */
        public static Boolean replaceString(String filePath, String oldString, String newString) {


            return FileTool.replaceString(filePath, oldString, newString);
        }

        /**
         * 删除指定文件中指定行的内容
         *
         * @param filePath   要删除内容的文件路径
         * @param lineNumber 要删除的行号
         * @return 成功返回 true，失败返回 false
         * @throws IOException 如果无法读取或写入文件，会抛出 IOException 异常
         */
        public static Boolean deleteLine(String filePath, int lineNumber) {
            return FileTool.deleteLine(filePath, lineNumber);
        }

        public static InputStream getAssetFile(Context context, String filePath) {
            if (!Env.Companion.getRun()) {
                filePath = getExternalFilesDir() + "/files/debug/assets/" + filePath;
            }
            if (!isStartWithStorage(filePath)) {
                return FileTool.getAssetFile(context, filePath);
            }
            return readFileInputStream(filePath);
        }

        public static InputStream readFileInputStream(String filePath) {
            try {
                FileInputStream inputStream = new FileInputStream(filePath);
                return inputStream;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        public static boolean saveBitmapToSDCard(Bitmap bitmap, int quality, String filePath) {
            boolean result = false;
            // 首先检查SD卡是否可用
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                FileOutputStream fos = null;
                try {
                    // 创建目录
                    File file = new File(filePath);
                    File parentFile = file.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }

                    // 将Bitmap保存到文件
                    fos = new FileOutputStream(file);
                    result = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fos != null) {
                            fos.flush();
                            fos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return result;
        }

    }

    public static class http {
        /**
         * 发送不带文件的GET请求，并返回请求结果
         *
         * @param url 请求的URL
         * @return 请求结果
         */
        public static String sendGetRequest(String url) {

            try {
                return HttpTool.sendGetRequest(url);
            } catch (IOException e) {
                return null;
            }

        }

        /**
         * 发送带文件的GET请求，并返回请求结果
         *
         * @param url 请求的URL
         * @param f   文件路径
         * @return 请求结果
         */
        public static String sendGetRequestWithFile(String url, String fileKey, String f) {
            try {
                File file = new File(f);
                return HttpTool.sendGetRequestWithFile(url, fileKey, file);
            } catch (IOException e) {
                return null;

            }
        }

        /**
         * 发送不带文件的POST请求，并返回请求结果
         *
         * @param url 请求的URL
         * @param jn  请求的表单数据
         * @return 请求结果
         */
        public static String sendPostRequest(String url, String jn) {
            try {
                // 创建 JsonParser 对象
                JsonParser jsonParser = new JsonParser();
                // 将字符串解析为 JsonElement
                JsonElement jsonElement = jsonParser.parse(jn);
                // 将 JsonElement 转换为 JsonObject
                JsonObject json = jsonElement.getAsJsonObject();
                FormBody.Builder builder = new FormBody.Builder();
                for (String key : json.keySet()) {
                    String value = json.get(key).getAsString();
                    System.out.println("大哥：" + value);
                    builder.add(key, value);
                }
                FormBody formData = builder.build();
                return HttpTool.sendPostRequest(url, formData);
            } catch (IOException e) {
                return null;
            }
        }

        /**
         * 发送带文件的POST请求，并返回请求结果
         *
         * @param url     请求的URL
         * @param jn      请求的表单数据
         * @param f       文件
         * @param fileKey 文件参数名
         * @return 请求结果
         */
        public static String sendPostRequestWithFile(String url, String jn, String fileKey, String f) {

            try {
                File file = new File(f);
                if (jn != null && !jn.equals("")) {
                    JsonParser jsonParser = new JsonParser();
                    // 将字符串解析为 JsonElement
                    JsonElement jsonElement = jsonParser.parse(jn);
                    // 将 JsonElement 转换为 JsonObject
                    JsonObject json = jsonElement.getAsJsonObject();
                    FormBody.Builder builder = new FormBody.Builder();
                    for (String key : json.keySet()) {
                        String value = json.get(key).getAsString();
                        builder.add(key, value);
                    }
                    FormBody formData = builder.build();
                    return HttpTool.sendPostRequestWithFile(url, formData, fileKey, file);
                    // 条件为真时的逻辑处理
                }


                return HttpTool.sendPostRequestWithFile(url, null, fileKey, file);
            } catch (IOException e) {
                return null;
            }


        }


    }


    public static class image {


        public static Bitmap base64ToBitmap(String base64String) {

            return ImageTool.base64Bitmap(base64String);

        }

        public static String bitmapToBase64(Bitmap b) {
            return ImageTool.byteBase64(ImageTool.bitmapByte(b));
        }


//        public static Point[] findImgBySurfEx(Bitmap bitmap, double imgResize, Bitmap template, double tempResize, double threshold, int sx, int sy, int ex, int ey, boolean upright) {
//            OpencvOper Oper = new com.example.opencvlibrary.OpencvOper();
//            try {
//                return ImageTool.findImgBySurfEx(Oper.BitmapToMat(bitmap), imgResize, Oper.BitmapToMat(template), tempResize, threshold, sx, sy, ex, ey, upright);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        public static Point[] findImgBySift(Bitmap bitmap, double imgResize, Bitmap template, double tempResize, double threshold, int sx, int sy, int ex, int ey) {
//            OpencvOper Oper = new com.example.opencvlibrary.OpencvOper();
//            try {
//
//                return ImageTool.findImgBySift(Oper.BitmapToMat(bitmap), imgResize, Oper.BitmapToMat(template), tempResize, threshold, sx, sy, ex, ey);
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            return null;
//        }
//
//        public static OpencvOper.Match[] FindImgByPyramid(Bitmap bitmap, Bitmap temp, int sx, int sy, int ex, int ey, int matchMethod, double weakThreshold, double strictThreshold, int count, int scaleTimes, double scalePro) {
//            OpencvOper Oper = new com.example.opencvlibrary.OpencvOper();
//            try {
//                return ImageTool.FindImgByPyramid(Oper.BitmapToMat(bitmap), Oper.BitmapToMat(temp), sx, sy, ex, ey, matchMethod, weakThreshold, strictThreshold, count, scaleTimes, scalePro);
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            return null;
//        }
//
//        public static OpencvOper.Match[] findImgByContour(Bitmap bitmap, Bitmap temp, double resize, double threshold, int sx, int sy, int ex, int ey, int count, int weakThreshold, int strongThreshold) {
//            OpencvOper Oper = new com.example.opencvlibrary.OpencvOper();
//            try {
//                return ImageTool.findImgByContour(Oper.BitmapToMat(bitmap), Oper.BitmapToMat(temp), resize, threshold, sx, sy, ex, ey, count, weakThreshold, strongThreshold);
//            } catch (Exception e) {
//                throw new RuntimeException(e);
//            }
//        }
//
//        public static OpencvOper.Match[] findTranslucentImg(Bitmap bitmap, Bitmap temp, double resize, double threshold, int sx, int sy, int ex, int ey, int count, int colorOffset) {
//            OpencvOper Oper = new com.example.opencvlibrary.OpencvOper();
//            try {
//
//                return ImageTool.findTranslucentImg(Oper.BitmapToMat(bitmap), Oper.BitmapToMat(temp), resize, threshold, sx, sy, ex, ey, count, colorOffset);
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//            return null;
//        }

        public static Bitmap getBitmapFromInputStream(InputStream inputStream) {
            return ImageTool.getBitmapFromInputStream(inputStream);
        }
    }

    public static class input {
        public static void InputMethodInput(String text) {

            InputMethodTool.commitText(text, getIms());
        }

        public static void InputMethodEnterKey() {
            InputMethodTool.sendEnterKey(getIms());
        }

        public static void InputMethodDeleteText() {
            InputMethodTool.deleteSurroundingText(getIms());
        }


    }


    public static class node {
        public static class obj {
            public static void nodeFilter(call.l callback) {
                magichands.core.model.accessibility.obj.call.accessibilityListener(callback);
            }

            public static Object filter() {

                return privateApi.find();
            }

            public static Object textNodeRegular(Object a) {

                return privateApi.textNodeRegular(a);
            }

            public static Object nodeExist(Object a) {
                return privateApi.exist(a);
            }

            public static Object containsSwich(Object NodeInfo, Object node) {
                return privateApi.containsswich(NodeInfo, node);
            }

            public static Object allSwich(Object NodeInfo, Object node) {

                return privateApi.allswich(NodeInfo, node);

            }

            public static Object getText(Object node) {
                Pattern pattern = Pattern.compile("text:\\s([^,]*),");
                String n = String.valueOf(privateApi.getText(node));
                System.out.println("node信息：" + n);
                Matcher matcher = pattern.matcher(n);
                if (matcher.find()) {
                    String text = matcher.group(1);
                    return text;
                }
                return null;
            }

            public static Object getBounds(Object node) {

                Pattern pattern = Pattern.compile("bounds: (.*?), childCount");
                Matcher matcher = pattern.matcher(String.valueOf(privateApi.getText(node)));
                if (matcher.find()) {
                    String bounds = matcher.group(1);
                    return bounds;
                }
                return null;
            }

            public static Object tryClick(Object a) {

                return privateApi.tryClick(a);
            }

            public static Object click(Object a) {

                return privateApi.click(a);
            }

            public static Object longClick(Object a) {

                return privateApi.longClick(a);
            }

            public static Object tryLongClick(Object a) {

                return privateApi.tryLongClick(a);
            }

            public static Object doubleClick(Object a) {

                return privateApi.doubleClick(a);
            }

            public static Object globalClick(Object a) {

                return privateApi.globalClick(a);
            }

            public static Object globalLongClick(Object a) {

                return privateApi.globalLongClick(a);
            }

        }

        public static class inf {
            public static AccessibilityNodeInfo findNodeByDesc(String desc) {
                return find.findNodeByDesc(desc);
            }

            public static AccessibilityNodeInfo findNodeById(String id) {
                return find.findNodeById(id);
            }

            public static AccessibilityNodeInfo findNodeByText(String text) {
                return find.findNodeByText(text);
            }

            public static AccessibilityNodeInfo findPreviousSibling(AccessibilityNodeInfo node) {
                return find.findPreviousSibling(node);
            }

            public static AccessibilityNodeInfo findNextSibling(AccessibilityNodeInfo node) {
                return find.findNextSibling(node);
            }

            public static AccessibilityNodeInfo getParentNode(AccessibilityNodeInfo node) {
                return find.getParentNode(node);
            }

            public static List<AccessibilityNodeInfo> getChildNodes(AccessibilityNodeInfo node) {
                return find.getChildNodes(node);
            }

            public static Object inputText(Object a, Object b) {

                return magichands.core.model.accessibility.inf.input.inputText(acc, (AccessibilityNodeInfo) a, b.toString());

            }
        }

        public static void refreshCurrentPageNodes() {
            refresh.refreshCurrentPageNodes();
        }

        public static Boolean clearAccessibilityCache() {
            return AndroidPlatformReflectionUtils.clearAccessibilityCache();
        }

    }

    public static class plugins {

        public static void loadApk(String file) {
            LoadDexTool.load(getCon(), file);
        }

        public static boolean isClass(String className) {
            try {
                Class.forName(className);
                return true;
            } catch (ClassNotFoundException e) {
                return false;
            }
        }

        public static void startPy(String path) {
            chaquopy.start(path);

        }

        @SafeVarargs
        public static <T> String invokePy(String a, String b, T... c) {

            return String.valueOf(chaquopy.invoke(a, b, c));
        }


    }

    public static class point {
        static magichands.core.model.accessibility.point.multi.multiApi multiApi;

        public point() {
            multiApi = new magichands.core.model.accessibility.point.multi.multiApi(new magichands.core.model.accessibility.point.multi.init(getAcc()));
        }


        public static Object swipeToPoint(Object sx, Object sy, Object ex, Object ey, Object dur) {
            return privateApi.swipeToPoint(sx, sy, ex, ey, dur);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static boolean touchDown(int x, int y) {
            return multiApi.touchDown(x, y, 1);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static boolean touchMove(int x, int y) {
            return multiApi.touchMove(x, y, 1);
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        public static boolean touchUp(int x, int y) {
            return multiApi.touchUp(x, y, 1);
        }

        public static Object clickPoint(Object x, Object y) {

            return privateApi.clickPoint(x, y);

        }

        public static Object longClickPoint(Object x, Object y) {
            return privateApi.longClickPoint(x, y);
        }

        public static Object gesture(Object a, Object b) {

            return privateApi.gesture(a, b);
        }

        public static Object gestures(Object a, Object b) {

            return privateApi.gestures(a, b);
        }

    }

    public static class release {

        public static void closeInputStream(InputStream b) {
            try {
                b.close();
                b = null;
            } catch (IOException e) {
                System.out.println(e);
            }

        }

        public static void bitRecycle(Bitmap b) {
            if (b != null && !b.isRecycled()) {
                b.recycle();
                b = null;

            }
            intBitmap();
        }

        public static void intBitmap() {
            magichands.core.recordscreen.Env.Companion.setBitmap(null);
        }

    }

    public static class recordscreen {

        public static void startScreenCapture(AppCompatActivity ay) {
            magichands.core.recordscreen.Image.Companion.init(ay);
            magichands.core.recordscreen.Image.Companion.startScreenCapture(ay);
        }

        public static Boolean stopScreenCapture(AppCompatActivity ay) {
            return (Boolean) magichands.core.recordscreen.Image.Companion.stopScreenCapture();
        }

        public static Boolean isScreenShot() {
            return magichands.core.recordscreen.Image.Companion.isScreenCapture();

        }

        public static void setUpVirtualDisplay(int x, int y, int ex, int ey) {

            Function0<Unit> callback = () -> {
                // 在这里处理回调逻辑
                System.out.println("Callback invoked");
                return Unit.INSTANCE;
            };

// 调用 setUpVirtualDisplay 函数并传递回调函数
            magichands.core.recordscreen.Image.Companion.setUpVirtualDisplay(callback, x, y, ex, ey);

        }

        public static Bitmap getBitmap() {
            return magichands.core.recordscreen.Env.Companion.getBitmap();
        }


    }


    public static class shell {

        public static String executeCommand(String cmd) {
            return run.executeCommand(cmd);
        }

        public static String superShell(String cmd) {
            final String[] rn = {""};
            rn[0] = null;
            runShell(cmd, new run.ShellResultCallback() {
                @Override
                public void onResultReceived(String result) {
                    // 在这里处理返回的 result 数据
                    rn[0] = result;
                    System.out.println("执行 ：" + rn[0]);
                }
            });
            for (int i = 0; i < 300; i++) {
                try {
                    // 延迟5秒
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (rn[0] != null) break;

            }
            return rn[0];
        }

    }

    public static class socket {

        public static webSocketClient webSocketClient(String ws, webSocketClient.WebSocketListener wc) {

            try {
                webSocketClient webSocketClient = new webSocketClient(
                        new URI(ws), wc
                );
                webSocketClient.connect();
//            webSocketClient.sendMessageToServer("color.getR(srcMat,12,32)");
                return webSocketClient;
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }

//        new WebSocketClientClass.WebSocketListener() {
//            @Override
//            public void onConnected() {
//                // 连接建立时的操作
//            }
//
//            @Override
//            public void onMessageReceived(String message) {
//                // 收到消息时的操作
//            }
//
//            @Override
//            public void onDisconnected(int code, String reason, boolean remote) {
//                // 连接关闭时的操作
//            }
//
//            @Override
//            public void onError(Exception ex) {
//                // 错误处理
//            }
//        }

        }

        public static webSocketServer webSocketServer(int port, webSocketServer.WebSocketListener wc) {


            webSocketServer webSocketClient = new webSocketServer(
                    port, wc
            );
            webSocketClient.start();
            return webSocketClient;


//        new WebSocketClientClass.WebSocketListener() {
//            @Override
//            public void onConnected() { b
//                // 连接建立时的操作
//            }
//
//            @Override
//            public void onMessageReceived(String message) {
//                // 收到消息时的操作
//            }
//
//            @Override
//            public void onDisconnected(int code, String reason, boolean remote) {
//                // 连接关闭时的操作
//            }
//
//            @Override
//            public void onError(Exception ex) {
//                // 错误处理
//            }
//        }

        }


    }


    public static class time {

        public static String timestampToStandardTime(long timestamp, String pn) {

            return TimeTool.timestampToStandardTime(timestamp, pn);
        }


    }

    public static class ui {

        public static class vue {

            public static void onClick(InterActive.button_vue.l callback) {
                InterActive.onClick(callback);
            }

            public static Map<String, String> getVueValue() {
                return magichands.core.ui.vue.Env.getCallmap();
            }


        }

        public static class xml {

            public static View parseLayout(Context context, String layoutName) {
                if (!Env.Companion.getRun()) {
                    if (!isStartWithStorage(layoutName)) {
                        layoutName = getExternalFilesDir() + "/files/debug/assets/" + layoutName;
                    }
                }
                if (!isStartWithStorage(layoutName)) {
                    return magichands.core.ui.xml.Parse.parseLayout(context, layoutName);
                }
                return magichands.core.ui.xml.Parse.parseLayout1(context, layoutName);

            }

            public static void showViewOnScreen(View view, Activity ac) {


                Handler mainHandler = new Handler(Looper.getMainLooper());
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            android.os.Handler hostHandler = magichands.core.Handler.getHandler("getUi");
                            Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                            message.what = 2;
                            message.obj = view;
                            hostHandler.sendMessage(message);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }

                    }
                });

            }

            public static void setOnItemSelectedListener(Spinner spinner) {
                magichands.core.ui.xml.Spinner.setOnItemSelectedListener(spinner);
            }

            public static Spinner getSpinner(String id) {
                return magichands.core.ui.xml.Spinner.getSpinner(id);
            }

            public static String getSpinnerName() {
                // 返回 spinnerName 变量中存储的 Spinner 控件中选中项的名称
                return magichands.core.ui.xml.Spinner.getSpinnerName();
            }

            public static String getText(EditText editText) {

                return magichands.core.ui.xml.EditText.getText(editText);
            }

            public static void setText(EditText editText,String text) {

                 magichands.core.ui.xml.EditText.setText(editText,text);
            }

            public static EditText getEditText(String id) {
                return magichands.core.ui.xml.EditText.getEditText(id);
            }

            public static void setOnClickListener(Button b, AppCompatActivity c) {
                magichands.core.ui.xml.Button.setOnClickListener(b, c);
            }

            public static Button getButton(String id) {

                return magichands.core.ui.xml.Button.getButton(id);
            }

            public static void onClick(magichands.core.ui.xml.Button.button.l callback) {
                magichands.core.ui.xml.Button.onClick(callback);
            }


        }


    }

    public static class usb {
        public static String Usbip;

        public static void setUsbIp(String ip) {
            Usbip = "http://" + ip ;
        }

        public static String findUsb(String uid, String serial, String vid, String pid) {

            return sendGetRequest(Usbip + "/device/find?uid=" + uid + "&serial=" + serial + "&vid=" + vid + "&pid=" + pid);
        }

        public static String usbResolution(String uid, int width, int height) {
            return sendGetRequest(Usbip + "/device/resolution?uid=" + uid + "&width=" + width + "&height=" + height);
        }

        public static String usbInit(String uid) {
            String jwtToken = generateJwtToken("fjc", "magichands", "root");
            return sendGetRequest(Usbip + "/device/init?uid=" + uid);

        }

        public static String usbClickPoint(String uid, String x, String y) {
            return sendGetRequest(Usbip + "/api/clickPoint?uid=" + uid + "&x=" + x + "&y=" + y);
        }

        public static String usbTouchDown(String uid, String x, String y) {
            return sendGetRequest(Usbip + "/api/touchDown?uid=" + uid + "&x=" + x + "&y=" + y);
        }

        public static String usbTouchMove(String uid, String x, String y) {
            return sendGetRequest(Usbip + "/api/touchMove?uid=" + uid + "&x=" + x + "&y=" + y);
        }

        public static String usbtouchUp(String uid, String x, String y) {
            return sendGetRequest(Usbip + "/api/touchUp?uid=" + uid);
        }

        public static String usbLongClickPoint(String uid, int x, int y, int dur) {
            return sendGetRequest(Usbip + "/api/longClickPoint?uid=" + uid + "&x=" + x + "&y=" + y + "&dur=" + dur);
        }

        public static String usbSwipeToPoint(String uid, int x, int y, int sx, int sy) {
            return sendGetRequest(Usbip + "/api/swipeToPoint?uid=" + uid + "&x=" + x + "&y=" + y + "&sx=" + sx + "&xy=" + sy);
        }

        public static String usbKeyBoard(String uid, String key) {
            return sendGetRequest(Usbip + "/api/keyBoard?uid=" + uid + "&key=" + key);
        }


    }

    public static class video {

        public static void captureH264() {
//            initVideoEncoder("video/avc", getScreenWidth(Global), getScreenHeight(Global));
        }


    }

    public static class windows {

        public static void requestFloatWindowPermission(AppCompatActivity a) {
            Permissions.Companion.requestFloatWindowPermission(a);
        }

        public static boolean canWindows() {

            return Settings.canDrawOverlays(getCon());

        }

        public static void show() {
            wsShow();
        }

        public static void stop() {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        runHandler(4, magichands.core.Handler.getHandler("getDebugWindows"), "结束");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        }

        public static void hide() {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        runHandler(5, magichands.core.Handler.getHandler("getDebugWindows"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        }

        public static void clear() {
            Handler mainHandler = new Handler(Looper.getMainLooper());
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        runHandler(3, magichands.core.Handler.getHandler("getDebugWindows"));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });


        }


    }

    public static class zip {


        public static Boolean compress(String srcFilePath, String destFilePath, String password) {
            try {
                ZipTool.compress(srcFilePath, destFilePath, password);
                System.out.println("解压缩成功！");
                return true;
            } catch (ZipException e) {
                System.err.println("解压缩失败：" + e.getMessage());
                e.printStackTrace();
                return false;
            }

        }


        public static Boolean decompress(String zipFilePath, String destFilePath, String password) {
            try {
                ZipTool.decompress(zipFilePath, destFilePath, password);
                System.out.println("解压缩成功！");
                return true;
            } catch (ZipException e) {
                System.err.println("解压缩失败：" + e.getMessage());
                e.printStackTrace();
                return false;
            }
        }


    }
}
