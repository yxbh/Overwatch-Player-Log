package me.benh.lib.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import me.benh.lib.common.Constants;

/**
 * Created by benhuang on 26/11/16.
 */

public class ActivityHelper {
    protected ActivityHelper() {}

    public static void finishWithError(Activity activity) {
        activity.setResult(Constants.RESULT_ERROR);
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

    public static void startUrlActivity(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }
}
