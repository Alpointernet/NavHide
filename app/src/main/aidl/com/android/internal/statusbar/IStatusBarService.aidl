package com.android.internal.statusbar;

import android.os.IBinder;

interface IStatusBarService {
    void disable(int what, IBinder token, String pkg);
    void disable2(int what, IBinder token, String pkg);
}
