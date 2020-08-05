package com.flypika.pack.presentation.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Utils class for easy managing of Permissions
 */
public final class PermissionsUtils {

    /**
     * Determine whether you have been granted a particular permission.
     * @param context application context
     * @param permissions names of the permissions being checked
     * @return true if all permissions is granted
     */
    public static boolean isGranted(Context context, String... permissions){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return true;

        for (String permission : permissions){
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     * Determine whether user disable permission and set it as "never show again".
     * @param activity activity
     * @param permissions the name of the permission being checked
     * @return true if permissions "never show" flag is enable
     */
    public static boolean isNeverShowEnable(Activity activity, String permissions){
        return !ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions);
    }

    /**
     * Show native Android permission popup.
     * @param activity The target activity
     * @param request requestFocuseForce code. Must be > 0
     * @param strings The requested permissions. Must me non-null and not empty
     */
    public static void showRequestPopup(Activity activity, int request, String... strings){
        ActivityCompat.requestPermissions(
                activity,
                strings,
                request);
    }
}
