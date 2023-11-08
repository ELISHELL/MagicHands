package magichands.core.model.accessibility.point.multi;

public class f <T> {



    /* renamed from: 丨丨lll  reason: contains not printable characters */
    private volatile T f1287lll;

    /* renamed from: 丨丨lll  reason: contains not printable characters */
    public T m1785lll() {
        synchronized (this) {
            if (this.f1287lll != null) {
                return this.f1287lll;
            }
            try {
                wait();
                return this.f1287lll;
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /* renamed from: 丨丨lll  reason: contains not printable characters */
    public void m1786lll(T t) {
        synchronized (this) {
            this.f1287lll = t;
            notify();
        }
    }



}
