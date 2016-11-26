package me.benh.overwatchplayerlog.helpers;

import android.app.Activity;
import android.content.Intent;

import me.benh.overwatchplayerlog.Constant;

/**
 * Created by benhuang on 26/11/16.
 */

public final class ActivityHelper {
    private ActivityHelper() {}

    public static void finishWithError(Activity activity) {
        activity.setResult(Constant.RESULT_ERROR);
        activity.finish();
    }

    public static void finishWithCanceled(Activity activity) {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    public static void finishWithSuccess(Activity activity) {
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    public static void finishWithSuccess(Activity activity, Intent intent) {
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }
}
