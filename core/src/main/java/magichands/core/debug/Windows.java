package magichands.core.debug;

import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.text.Layout;
import android.text.method.ScrollingMovementMethod;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.content.ContextCompat;

import com.hjq.toast.Toaster;

import java.lang.reflect.Method;

import magichands.core.Env;
import magichands.core.R;

public class Windows {

    private static AppCompatTextView tvContent;
    private static int mTouchStartX;
    private static int mTouchStartY;
    private static int mStartX;
    private static int mStartY;
    static WindowManager wm;
    static View view;
    static AppCompatTextView textView;
    public static WindowManager.LayoutParams layoutParams;
    private static int mTouchCurrentX;
    private static int mTouchCurrentY;
    static int num = 1;
    public static Boolean ws=false;


    @SuppressLint("SetTextI18n")
    public static void wsShow(Context Acc, int id, int id1, int id2) {
        if (!ws){
            num = 1;

            wm = (WindowManager) Acc.getSystemService(AccessibilityService.WINDOW_SERVICE);

            layoutParams = new WindowManager.LayoutParams();
//        layoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }

            int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | FLAG_NOT_FOCUSABLE;
            layoutParams.flags = flags;
            layoutParams.format = PixelFormat.TRANSLUCENT;


            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) ContextCompat.getSystemService(Env.Companion.getActivity(), WindowManager.class);
            assert windowManager != null;
            windowManager.getDefaultDisplay().getRealMetrics(displayMetrics); // 使用getRealMetrics方法获取真实尺寸
            int screenWidth = displayMetrics.widthPixels;
            int screenHeight = displayMetrics.heightPixels;

// 根据屏幕尺寸计算合适的宽度和高度
            int targetWidth = (int) (screenWidth * 0.55); // 设置为屏幕宽度的60%
            int targetHeight = (int) (screenHeight * 0.22); // 设置为屏幕高度的30%


            layoutParams.width = targetWidth;
            layoutParams.height = targetHeight;
            layoutParams.gravity = Gravity.CENTER;
            view = LayoutInflater.from(Acc).inflate(id, null);
            textView = view.findViewById(id1);
            textView.setMovementMethod(ScrollingMovementMethod.getInstance());
//        textView.setScrollbarFadingEnabled(false);
//        textView = (TextView)view;
            float v = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 4.0F, Acc.getResources().getDisplayMetrics());
            textView.setTextSize(v * 1.5f);
//        textView.setText("欢迎使用");
            textView.setTextColor(Color.WHITE);
            textView.setBackgroundColor(Color.argb(0x55, 0X00, 0x00, 0x00));
            view.setOnTouchListener((View.OnTouchListener) (new View.OnTouchListener() {
                @SuppressLint({"ClickableViewAccessibility"})
                public boolean onTouch(@org.jetbrains.annotations.Nullable View p0, @org.jetbrains.annotations.Nullable MotionEvent p1) {
                    System.out.println("123");
                    switch (p1.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mTouchStartX = (int) p1.getRawX();
                            mTouchStartY = (int) p1.getRawY();
                            mStartX = (int) p1.getX();
                            mStartY = (int) p1.getY();
                        case MotionEvent.ACTION_MOVE:
                            mTouchCurrentX = (int) p1.getRawX();
                            mTouchCurrentY = (int) p1.getRawY();
                            layoutParams.x += mTouchCurrentX - mTouchStartX;
                            layoutParams.y += mTouchCurrentY - mTouchStartY;
                            wm.updateViewLayout(view, layoutParams);
                            mTouchStartX = mTouchCurrentX;
                            mTouchStartY = mTouchCurrentY;

                    }
                    return true;
                }
            }));
            view.findViewById(id2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println("关闭");
                    wsHide();
                }
            });

//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showFilterDialog(); //点击日志窗口标题栏打开过滤器
//            }
//        });
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return true;
//            }
//        });
            wm.addView(view, layoutParams);
        }else{
            Toaster.show("已经展示,请勿重复示");
//            Toast.makeText(Env.Companion.getContext(), "已经展示,请勿重复示", Toast.LENGTH_LONG).show();
        }
        ws=true;
    }


    public static void wsHide() {
        ws=false;
        if (view != null && view.getParent() != null) {
            wm.removeView(view);
        } else {
            System.out.println("===========");
        }
    }


    public static void wsLogd(String log) {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCHABLE | FLAG_NOT_FOCUSABLE;
        layoutParams.flags = flags;
        textView.append(num + ":" + log + "\n");
        textView.post(new Runnable() {
            @Override
            public void run() {
                Layout layout = textView.getLayout();
                if (layout != null) {
                    int lastLineOffset = layout.getLineTop(textView.getLineCount()) - textView.getHeight();
                    textView.scrollTo(0, Math.max(lastLineOffset, 0));
                }
            }
        });
        textView.setTextColor(Color.GREEN); // 设置字体颜色为绿色
        textView.setTextSize(10);
        wm.updateViewLayout(view, layoutParams);
        num++;
    }

    public static void wsStop(String log) {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | FLAG_NOT_FOCUSABLE;
        layoutParams.flags = flags;
        textView.append(num + ":" + log + "\n");
        textView.setTextSize(3);
        int offset = textView.getLineCount() * textView.getLineHeight();
        if (offset > textView.getHeight()) {
            int lastLineOffset = textView.getLineCount() * textView.getLineHeight() - textView.getHeight();
            textView.scrollTo(0, lastLineOffset);
            System.out.println("定位：" + lastLineOffset + "," + textView.getHeight());
        } else {
            textView.scrollTo(0, 0);
        }
        textView.setTextSize(10);
        wm.updateViewLayout(view, layoutParams);
    }

    public static void wsClear() {
        int flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCHABLE | FLAG_NOT_FOCUSABLE;
        layoutParams.flags = flags;
        textView.setText("");
        wm.updateViewLayout(view, layoutParams);
        num = 1;
    }



    public static void  runHandler(int id,Handler handler){
        Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
        message.what = id;
        handler.sendMessage(message);
    }

    public static void  runHandler(int id,Handler handler,Object obj){

        Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
        message.what = id;
        message.obj = obj;
        handler.sendMessage(message);
    }


    public static void wsShow() {
        Handler mainHandler = new Handler(Looper.getMainLooper());
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                try {

                    Handler hostHandler = magichands.core.Handler.getHandler("getDebugWindows");
                    Bundle args = new Bundle();
                    args.putInt("floating_window", R.layout.layout_floating_window);
                    args.putInt("tv_content", R.id.tv_content);
                    args.putInt("iv_close", R.id.iv_close);
                    Message message = new Message();//发送一个消息，该消息用于在handleMessage中区分是谁发过来的消息；
                    message.what = 1;
                    message.setData(args);
                    assert hostHandler != null;
                    hostHandler.sendMessage(message);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }



}


