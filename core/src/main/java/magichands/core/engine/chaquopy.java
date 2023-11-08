package magichands.core.engine;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;

import magichands.core.Env;
import python.config;

public class chaquopy {
    @SafeVarargs
    public static <T> T invoke(String a, String b, T... c) {
        Python python = Python.getInstance();
        PyObject pyObject = python.getModule(a).callAttr(b, c);
        return (T) pyObject;
    }

    public static void start(String path) {
        config.py_path = path;
        if (!Python.isStarted()) {
            Python.start(new AndroidPlatform(Env.Companion.getContext()));
        }
    }

}
