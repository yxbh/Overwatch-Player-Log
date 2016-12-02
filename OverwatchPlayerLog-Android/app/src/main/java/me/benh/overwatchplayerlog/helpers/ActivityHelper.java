package me.benh.overwatchplayerlog.helpers;

import android.app.Activity;
import android.content.Intent;

import me.benh.lib.common.Constants;
import me.benh.overwatchplayerlog.common.Arguements;
import me.benh.overwatchplayerlog.common.Requests;
import me.benh.overwatchplayerlog.controllers.OwPlayerRecordEditActivity;
import me.benh.overwatchplayerlog.controllers.listanddetails.OwPlayerItemDetailActivity;
import me.benh.overwatchplayerlog.controllers.listanddetails.OwPlayerItemDetailFragment;
import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.data.OwPlayerRecordWrapper;

/**
 * Created by benhuang on 26/11/16.
 */

public final class ActivityHelper extends me.benh.lib.helpers.ActivityHelper {

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

    public static void startDetailActivity(Activity activity, OwPlayerRecordWrapper recordwWrapper) {
        startDetailActivity(activity, recordwWrapper.getRecord());
    }

    public static void startDetailActivity(Activity activity, OwPlayerRecord record) {
        Intent intent = new Intent(activity, OwPlayerItemDetailActivity.class);
        intent.putExtra(OwPlayerItemDetailFragment.ARG_OWPLAYERRECORD, new OwPlayerRecordWrapper(record));

        activity.startActivityForResult(intent, Requests.VIEW_RECORD_DETAIL);
    }

    public static void startEditActivity(Activity activity, OwPlayerRecord record) {
        Intent intent = new Intent(activity, OwPlayerRecordEditActivity.class);
        intent.putExtra(Arguements.OWPLAYERRECORD, new OwPlayerRecordWrapper(record));
        activity.startActivityForResult(intent, Requests.EDIT_RECORD);
    }
}
