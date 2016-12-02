package me.benh.lib.helpers;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;

import me.benh.lib.common.Constants;

/**
 * Created by benhuang on 26/11/16.
 */

public class ActivityHelper {
    protected ActivityHelper() {}

    public static void finishWithError(@NonNull Activity activity) {
        activity.setResult(Constants.RESULT_ERROR);
        activity.finish();
    }

    public static void finishWithCanceled(@NonNull Activity activity) {
        activity.setResult(Activity.RESULT_CANCELED);
        activity.finish();
    }

    public static void finishWithSuccess(@NonNull Activity activity) {
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    public static void finishWithSuccess(@NonNull Activity activity, Intent intent) {
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

    public static void startUrlActivity(@NonNull Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }
}
