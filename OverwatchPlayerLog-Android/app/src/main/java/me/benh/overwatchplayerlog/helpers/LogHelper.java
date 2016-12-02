package me.benh.overwatchplayerlog.helpers;

import android.app.Activity;
import android.util.Log;

import me.benh.overwatchplayerlog.common.Constants;

/**
 * Created by benhuang on 26/11/16.
 */

public final class LogHelper {
    private LogHelper() {}

    public static void d_resultCode(String tag, int resultCode) {
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
