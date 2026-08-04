package moe.shizuku.api;

import android.os.IBinder;

public class ShizukuBinderWrapper {
    private final IBinder binder;

    public ShizukuBinderWrapper(IBinder binder) {
        this.binder = binder;
    }

    public IBinder getBinder() {
        return binder;
    }
}
