package magichands.core.model.accessibility.obj;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class call {


//    @Nullable
//    public static Boolean javaCallbackMethod(@NotNull AccessibilityNodeInfoCompat it) {
//        it.isCheckable()
//        return null;
//    }

    public static l m;
    public  void s(l i){
        m=i;
    }
    @Nullable
    public  interface l{
        Boolean CallbackMethod(@NotNull AccessibilityNodeInfoCompat it);
    }

    public static void accessibilityListener(l callback) {
        call bu = new call();
        bu.s(new l() {
            @Override
            public Boolean CallbackMethod(@NotNull AccessibilityNodeInfoCompat it) {

              return   callback.CallbackMethod(it);
            }
        });
    }
}
