package me.benh.lib.helpers;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import me.benh.lib.common.Constants;

/**
 * Created by benhuang on 26/11/16.
 */

public class LogHelper {
    protected LogHelper() {}

    public static void d_resultCode(@NonNull String tag, int resultCode) {
        switch (resultCode) {
            case Activity.RESULT_OK: {
                Log.v(tag, "RESULT_OK");
                break;
            }

            case Activity.RESULT_CANCELED: {
                Log.v(tag, "RESULT_CANCELED");
                break;
            }

            case Constants.RESULT_ERROR: {
                Log.v(tag, "RESULT_ERROR");
                break;
            }
        }
    }
}
