package magichands.core.debug;

import static magichands.core.OpenApi.file.saveBitmapToSDCard;
import static magichands.core.OpenApi.getAcc;
import static magichands.core.OpenApi.getApc;
import static magichands.core.OpenApi.getCon;
import static magichands.core.OpenApi.recordscreen.getBitmap;
import static magichands.core.OpenApi.recordscreen.isScreenShot;
import static magichands.core.OpenApi.recordscreen.setUpVirtualDisplay;
import static magichands.core.OpenApi.recordscreen.startScreenCapture;
import static magichands.core.OpenApi.release.intBitmap;
import static magichands.core.OpenApi.windows.clear;
import static magichands.core.debug.Interaction.checkResponse;
import static magichands.core.debug.Interaction.downloadFile;
import static magichands.core.debug.Interaction.inn;
import static magichands.core.debug.Interaction.uploadFile;
import static magichands.core.model.accessibility.get.Uix.saveCurrentUiHierarchyToFile;
import static magichands.core.tool.FileTool.deleteFolder;
import static magichands.core.tool.ZipTool.decompress;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;

import net.lingala.zip4j.exception.ZipException;

import java.lang.reflect.Method;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import magichands.core.OpenApi;
import magichands.core.tool.FileTool;

public class communication {

    public static void Link() {
        FileTool.deleteFile("/storage/emulated/0/log.txt");
        FileTool.deleteFile(String.valueOf(getCon().getExternalFilesDir("js") + "/" + "main.js.bak"));

        new Thread("判断指令") {
            // 步骤2：复写run（），内容 = 定义线程行为
            @Override
            public void run() {
                ExecutorService executor = Executors.newFixedThreadPool(5); // 创建线程池
                while (true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    inn();
                    System.out.println("================");
                    switch (checkResponse()) {
                        case 0:
                            System.out.println("指令:0");
                            break;
                        case 3:

                            System.out.println("指令:3");
                            downloadFile(getCon().getExternalFilesDir("") + "/newdebug.zip");
                            deleteFolder(String.valueOf(getCon().getExternalFilesDir("debug")));
                            try {
                                decompress(getCon().getExternalFilesDir("") + "/newdebug.zip", getCon().getExternalFilesDir("debug") + "", null);
                            } catch (ZipException e) {
                                throw new RuntimeException(e);
                            }
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {

                                    OpenApi.start(false, false);


                                }
                            });

                            break;
                        case 4:

                            System.out.println("指令:4");
                            downloadFile(getCon().getExternalFilesDir("") + "/newdebug.zip");
                            deleteFolder(String.valueOf(getCon().getExternalFilesDir("debug")));
                            try {
                                decompress(getCon().getExternalFilesDir("") + "/newdebug.zip", getCon().getExternalFilesDir("debug") + "", null);
                            } catch (ZipException e) {
                                throw new RuntimeException(e);
                            }
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {

                                    OpenApi.start(false, false);

                                }
                            });

                            break;
                        case 5:

                            System.out.println("指令:5");
                            final Intent intent = getCon().getPackageManager().getLaunchIntentForPackage(getCon().getPackageName());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            getCon().startActivity(intent);
                            //杀掉以前进程
                            Process.killProcess(Process.myPid());

                            break;
                        case 6:
                            System.out.println("指令:6");
                            downloadFile(getCon().getExternalFilesDir("") + "/newdebug.zip");
                            deleteFolder(String.valueOf(getCon().getExternalFilesDir("debug")));
                            try {
                                decompress(getCon().getExternalFilesDir("") + "/newdebug.zip", getCon().getExternalFilesDir("debug") + "", null);
                            } catch (ZipException e) {
                                throw new RuntimeException(e);
                            }
                            Handler mainHandler = new Handler(Looper.getMainLooper());
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Handler hostHandler=  magichands.core.Handler.getHandler("getUi");
                                        Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                                        message.what = 1;
                                        hostHandler.sendMessage(message);
                                    } catch (Exception e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            });


                            break;
                        case 7:
                            System.out.println("指令:7");

                            break;
                        case 8:
                            System.out.println("指令:8");

                            break;
                        case 9:
                            startScreenCapture(getApc());
                            for (int i = 0; i < 8; i++) {
                                try {
                                    // 延迟5秒
                                    Thread.sleep(500);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                if (isScreenShot()) {
                                    setUpVirtualDisplay(-1, 70, 396, 143);
                                    for (int o = 0; o < 8; o++) {
                                        try {
                                            // 延迟5秒
                                            Thread.sleep(500);

                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (getBitmap() != null) {
                                            break;
                                        }
                                    }

                                    saveBitmapToSDCard(getBitmap(), 100, getCon().getExternalFilesDir("debug") + "/uix/1.png");
                                    break;
                                }

                            }
                            saveCurrentUiHierarchyToFile(getAcc());
                            intBitmap();
                            uploadFile();

//                            try {
//                                Thread.sleep(2500);
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                            png();


                            break;
//                        case 30:
//                            System.out.println("指令:30");
//
//                            deleteFolder(String.valueOf(Global.getExternalFilesDir("new.apk")));
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                            downloadFilepy(Global.getExternalFilesDir("") + "/new.apk");
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                            delog();
//                            toast("系统提示:","路径:","python编译文件:："+Global.getExternalFilesDir("new.apk"));
//                            stolog();
////                                FileUtils.copyAssetsFolderToSdCard(this, "chaquopy", Global.getExternalFilesDir("").toString() + "/chaquopy")
//
//                            break;
//                        case 31:
//                            System.out.println("指令:31");
//
//                            deleteFolder(String.valueOf(Global.getExternalFilesDir("java.apk")));
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                            downloadFilejava(Global.getExternalFilesDir("") + "/java.apk");
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                throw new RuntimeException(e);
//                            }
//                            loadApk(String.valueOf(Global.getExternalFilesDir("java.apk")));
//
//                            break;

                    }
                }

            }
        }.start();


    }
}
