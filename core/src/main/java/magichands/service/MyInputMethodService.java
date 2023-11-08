package magichands.service;

import android.inputmethodservice.InputMethodService;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

import magichands.core.Env;


public class MyInputMethodService extends InputMethodService {



    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("注册键盘");
        Env.Companion.setCurrentInputConnection(this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 执行清理操作，如注销相关内容
    }


}
