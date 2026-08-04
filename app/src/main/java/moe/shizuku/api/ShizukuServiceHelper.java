package moe.shizuku.api;

import android.content.Context;
import android.content.Intent;

public final class ShizukuServiceHelper {
    public static boolean isShizukuInstalled(Context context) {
        return false;
    }

    public static boolean isShizukuRunning(Context context) {
        return false;
    }

    public static boolean hasPermission(Context context) {
        return false;
    }

    public static void requestPermission(Context context) {
        if (context != null) {
            context.startActivity(new Intent(ShizukuApiConstants.ACTION_REQUEST_PERMISSION));
        }
    }

    private ShizukuServiceHelper() {}
}
