package magichands.core.tool;

import android.inputmethodservice.InputMethodService;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.inputmethod.InputConnection;

import magichands.core.Env;

public class InputMethodTool  {



    public static void deleteSurroundingText(InputMethodService i) {
        try {
            InputConnection currentInputConnection = i.getCurrentInputConnection();
            if (currentInputConnection != null) {
                currentInputConnection.deleteSurroundingText(Integer.MAX_VALUE, Integer.MAX_VALUE);
            }
        } catch (Exception e) {
            System.out.println("键盘报错"+e);
            e.printStackTrace();
            // 这里可以进行异常处理，如记录日志或执行其他操作
        }
    }


    public static void sendEnterKey(InputMethodService i) {
        InputConnection currentInputConnection = i.getCurrentInputConnection();
        if (currentInputConnection != null) {
            long uptimeMillis = SystemClock.uptimeMillis();
            KeyEvent keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER, 0);
            currentInputConnection.sendKeyEvent(keyEvent);
            keyEvent = new KeyEvent(uptimeMillis, uptimeMillis, KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER, 0);
            currentInputConnection.sendKeyEvent(keyEvent);
        }
    }

    public static void commitText(String text,InputMethodService i) {
        InputConnection currentInputConnection = i.getCurrentInputConnection();
        if (currentInputConnection != null) {
            currentInputConnection.commitText(text, 0);
        }
    }



}


