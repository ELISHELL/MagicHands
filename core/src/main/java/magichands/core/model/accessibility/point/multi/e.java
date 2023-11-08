package magichands.core.model.accessibility.point.multi;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class e {

    private GestureDescription.StrokeDescription mStrokeDescription = null;
    private AtomicBoolean f1357Lil1l = new AtomicBoolean(false);
    private List<GestureDescription> f1356lIlilLL = new LinkedList();
    private boolean f1355lILLLIL = false;

    private int il = 0;
    private int IIiL1 = 0;

    /* renamed from: 丨丨lll  reason: contains not printable characters */
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public boolean m1995lll(int i, int i2, int i3) {
//        return m1996lll(i, i2, (long) i3) && m1994Lil1l(i, i2, 1L);
//    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public  GestureDescription createGestureDescription(int startX, int startY, int endX, int endY, long duration, long startTime, boolean continueStroke, boolean isUp) {
        try {
            Path path = new Path();
            path.moveTo(startX, startY);
            if (startX != endX || startY != endY) {
                path.lineTo(endX, endY);
            }
            GestureDescription.StrokeDescription strokeDescription = !continueStroke ?
                    new GestureDescription.StrokeDescription(path, startTime, duration, isUp) :
                    mStrokeDescription.continueStroke(path, startTime, duration, isUp);
            GestureDescription.Builder builder = new GestureDescription.Builder();
            builder.addStroke(strokeDescription);
            GestureDescription gestureDescription = builder.build();
            mStrokeDescription = strokeDescription;
            return gestureDescription;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    private void m1992lll(long j) {
        if (j > 0) {
            try {
                Thread.sleep(j);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean m1996lll(int i, int i2, long j) {
        boolean m1993lll;
        synchronized (this.f1357Lil1l) {
            this.f1356lIlilLL.add(createGestureDescription(i, i2, i, i2, 1L, 1L, false, true));
            m1993lll = m1993lll();
            this.il = i;
            this.IIiL1 = i2;
            this.f1355lILLLIL = true;
            m1992lll(j);
        }
        return m1993lll;
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean LLI1lI1(int i, int i2, long j) {
        if (j <= 0) {
            j = 1;
        }
        long j2 = j;
        synchronized (this.f1357Lil1l) {
//            this.f1355lILLLIL=true;
            System.out.println("this.f1355lILLLIL:"+this.f1355lILLLIL);
            if (this.f1355lILLLIL) {
                if (this.il == i && this.IIiL1 == i2) {
                    return false;
                }
                this.f1356lIlilLL.add(createGestureDescription(this.il, this.IIiL1, i, i2, 1L, j2, true, true));
                boolean m1993lll = m1993lll();
                this.il = i;
                this.IIiL1 = i2;
                return m1993lll;
            }
            System.out.println("滑动失败");
            return false;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean m4001Lil1l(int i, int i2, long j) {
        synchronized (this.f1357Lil1l) {
            GestureDescription m3997lll = createGestureDescription(this.il, this.IIiL1, i, i2, 1L, 1L, true, false);
            if (m3997lll == null) {
                return false;
            }
            this.f1356lIlilLL.add(m3997lll);
            boolean m1993lll = m1993lll();
            this.f1355lILLLIL = false;
            m1992lll(j);
            return m1993lll;
        }
    }
    public boolean m1993lll() {
        if (init.service == null) {
            return false;
        }
        System.out.println("3==========");
        GestureDescription gestureDescription = this.f1356lIlilLL.get(0);
        if (gestureDescription != null) {
            System.out.println("4==========");
            new f();
            boolean dispatchGesture = init.LLI1lI1().dispatchGesture(gestureDescription, new AccessibilityService.GestureResultCallback() { // from class: 〇0〇oO00〇8.ooo8o088〇.〇Oo0oo80.Oo00o〇O0o.1
                @Override // android.accessibilityservice.AccessibilityService.GestureResultCallback
                public void onCancelled(GestureDescription gestureDescription2) {
//                    Log.w(StringFog.decrypt("FF9MW1l4UEdbXRY="), StringFog.decrypt("B1VKTERLVhNRUxYfGRAZHA=="));
                    try {
                        e.this.f1356lIlilLL.remove(0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    super.onCancelled(gestureDescription2);
                }

                @Override // android.accessibilityservice.AccessibilityService.GestureResultCallback
                public void onCompleted(GestureDescription gestureDescription2) {
                    synchronized (e.this.f1357Lil1l) {
                        try {
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (e.this.f1356lIlilLL.isEmpty()) {
                            return;
                        }
                        e.this.f1356lIlilLL.remove(0);
                        if (e.this.f1356lIlilLL.isEmpty()) {
                            return;
                        }
                        e.this.m1993lll();
//                        Log.w(StringFog.decrypt("FF9MW1l4UEdbXRY="), StringFog.decrypt("B1VKTERLVhNdXDsTEQwQHTRVXQ=="));
                        super.onCompleted(gestureDescription2);
                    }
                }
            }, null);
            if (dispatchGesture) {
                return dispatchGesture;
            }
//            Log.e(LLI1lI1, StringFog.decrypt("B1VKTERLVhNFUwtcEhMIWCRZSkhQTVBbV1Y="));
        }
        this.f1356lIlilLL.clear();
        return false;
    }


}
