package magichands.core;



import static magichands.core.OpenApi.getAct;
import static magichands.core.OpenApi.getCon;
import static magichands.core.tool.FileTool.readJsonFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import android.widget.Toast;


import com.getcapacitor.vue;
import com.hjq.toast.Toaster;

import java.lang.reflect.Method;
import java.util.Objects;

import magichands.core.debug.Windows;
import magichands.core.ui.xml.Parse;


public class Handler {


    public static android.os.Handler ui= new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println("展示vue or xml");
                    try {
                       String ui= readJsonFile(getCon().getExternalFilesDir("debug")+"/config.json","ui");
                       System.out.println("==========="+ui);
                        if (Objects.equals(ui, "1")) {
//                            openApp(1,"pa.magichands");
//                            sleep(5000);



                            Intent intent = new Intent(getCon(), Class.forName("magichands.service.XmlService"));
                            getCon().startActivity(intent);

                        }else{
                            Intent intent = new Intent(getCon(), vue.class);
                            getCon().startActivity(intent);
                        }

                    } catch (Exception e) {
                        System.out.println("报错：" + e);

                    }
                    break;
                case  2:
                    System.out.println("展示xml悬浮窗");
                    Parse.showViewOnScreen((View) msg.obj,getAct());
                    break;
            }
        }
    };

    public static android.os.Handler toast=new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    try {
                        String toastMessage = (String) msg.obj;
                        Toast.makeText(getCon(), toastMessage, Toast.LENGTH_LONG).show();
//                        Toaster.show(toastMessage);
                    } catch (Exception e) {
                        System.out.println("报错：" + e);

                    }
                    break;
            }
        }
    };

    public static android.os.Handler debugWindows = new android.os.Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    System.out.println("展示日志悬浮窗");
                    try {
                        Bundle args = msg.getData();
                        int floating_window = args.getInt("floating_window");
                        int tv_content = args.getInt("tv_content");
                        int iv_close = args.getInt("iv_close");
                        Windows.wsShow(Env.Companion.getContext(), floating_window,tv_content,iv_close);
                    } catch (Exception e) {
                        System.out.println("报错：" + e);

                    }
                    break;
                case 2:
                    System.out.println("通知日志悬浮窗打印日志");
                    try {
                        String message1 = (String) msg.obj;
                        Windows.wsLogd(message1);
                    } catch (Exception e) {
                        System.out.println("报错：" + e);

                    }
                    break;

                case 3:
                    System.out.println("清除日志悬浮窗");
                    try {

                        Windows.wsClear();
                    } catch (Exception e) {
                        System.out.println("报错：" + e);

                    }
                    break;

                case 4:
                    System.out.println("结束日志悬浮窗不可触摸状态");
                    try {
                        String message2 = (String) msg.obj;
                        Windows.wsStop(message2);
                    } catch (Exception e) {
                        System.out.println("报错：" + e);

                    }
                    break;

                case 5:
                    System.out.println("杀死日志悬浮窗");
                    try {
                        Windows.wsHide();
                    } catch (Exception e) {
                        System.out.println("报错：" + e);
                    }
                    break;

            }
        }
    };

    public static android.os.Handler getHandler(String name){
        Class<?>   hostHandlerClass = null;
        try {

            hostHandlerClass = Class.forName(Objects.requireNonNull(Env.Companion.getMainActivity()));
            Method hostHandlerField = hostHandlerClass.getDeclaredMethod(name); // 替换为宿主中的 Handler 对象的变量名
            hostHandlerField.setAccessible(true);
            return  (android.os.Handler)  hostHandlerField.invoke(hostHandlerClass.newInstance());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }

}
