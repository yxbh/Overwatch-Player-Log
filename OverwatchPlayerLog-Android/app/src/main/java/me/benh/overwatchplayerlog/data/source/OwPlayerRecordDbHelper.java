package me.benh.overwatchplayerlog.data.source;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import junit.framework.Assert;

/**
 * Created by benhuang on 25/11/16.
 */

public final class OwPlayerRecordDbHelper extends SQLiteOpenHelper {
    public static final String TAG = OwPlayerRecordDbHelper.class.getSimpleName();

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "OverwatchPlayerLog.db";

    public OwPlayerRecordDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(OplSqlContract.Tables.V1.MetaInfo.SQL_CREATE_TABLE);
        db.execSQL(OplSqlContract.Tables.V1.OwPlayerRecord.SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "onUpgrade [" + String.valueOf(oldVersion) + " --> " + String.valueOf(newVersion) + "]");
        Assert.assertTrue("Old version must be less or equal to new version", oldVersion <= newVersion);
        while (oldVersion != newVersion) {
            upgradeDb(db, oldVersion, oldVersion+1);
            ++oldVersion;
        }
    }

    private void upgradeDb(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.v(TAG, "upgradeDb [" + String.valueOf(oldVersion) + " --> " + String.valueOf(newVersion) + "]");

        switch (oldVersion) {
            case 1: {
                if (2 == newVersion) {
                    UpgradePaths.v1ToV2(db);
                }
                return;
            }
        }

        throw new RuntimeException(
                "Unsupported upgrade pathway from V" + String.valueOf(oldVersion) +
                        " to V" + String.valueOf(newVersion));
    }

    public static class UpgradePaths {
        public static void v1ToV2(SQLiteDatabase db) {
            // TODO
            assert false;
        }
    }
}
