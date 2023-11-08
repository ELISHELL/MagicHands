package magichands.core.model.accessibility.point.multi;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.os.Build;

import androidx.annotation.RequiresApi;

public class init {
    private static AccessibilityService f415lILLLIL;
    public static AccessibilityService service;
    private GestureDescription.StrokeDescription mStrokeDescription = null;
    public init(AccessibilityService service) {
        this.service = service;
        f415lILLLIL=service;
    }
    private boolean m1894LL1I1I() {
        return Build.VERSION.SDK_INT < 24;
    }
    public static AccessibilityService LLI1lI1() {
        return f415lILLLIL;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean LLI1lI1(int i, int i2, int i3) {
//        checkDestroyException();
        if (service == null) {
            return false;
        }
        if (m1894LL1I1I()) {
            return false;
        } else if (Build.VERSION.SDK_INT >= 24) {
            return c.m4081Lil1l().LLI1lI1().LLI1lI1(i, i2, i3);
        } else {
            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean m1930Lil1l(int i, int i2, int i3) {
        if (service== null) {
            return false;
        }
        if (m1894LL1I1I()) {
            return false;
        } else if (Build.VERSION.SDK_INT >= 24) {
            return c.m4081Lil1l().LLI1lI1().m5374Lil1l(i, i2, i3);
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean m3923lILLLIL(int i, int i2, int i3) {
        if (service== null) {
            return false;
        }
        if (m1894LL1I1I()) {
            return false;
        } else if (Build.VERSION.SDK_INT >= 24) {
            return c.m4081Lil1l().LLI1lI1().m7377lILLLIL(i, i2, i3);
        } else {
            return false;
        }
    }

}
