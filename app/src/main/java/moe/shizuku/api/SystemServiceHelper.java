package moe.shizuku.api;

import android.content.Context;

public final class SystemServiceHelper {
    public static Object getSystemService(Context context, String name) {
        if (context == null) {
            return null;
        }
        return context.getSystemService(name);
    }

    private SystemServiceHelper() {}
}
