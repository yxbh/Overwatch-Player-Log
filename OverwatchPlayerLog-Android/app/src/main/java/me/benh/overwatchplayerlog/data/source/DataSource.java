package me.benh.overwatchplayerlog.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.util.Log;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.overwatchplayerlog.helpers.DateTimeHelper;

/**
 * Created by benhuang on 23/11/16.
 */

public class DataSource {

    public static final String TAG = DataSource.class.getSimpleName();

    private static final String SQL_SELECT_WITH_ID = OplSqlContract.Tables.V1.OwPlayerRecord.SQL_SELECT_WITH_ID;

    private Context context;

    public DataSource(@NonNull Context context) {
        this.context = context;
    }

    public boolean hasOwPlayerRecordId(@NonNull String id) {
        Cursor cursor = getReadableDb().rawQuery(SQL_SELECT_WITH_ID, new String[] { id });
        cursor.moveToNext();
        return cursor.getInt(0) == 1;
    }

    public boolean hasOwPlayerRecord(@NonNull OwPlayerRecord record) {
        return hasOwPlayerRecordId(record.getId());
    }

    public void saveNewOwPlayerRecord(@NonNull OwPlayerRecord record) {
        if (!record.isValid()) {
            throw new InvalidParameterException("Invalid record object " + record.toString());
        }

        if (hasOwPlayerRecordId(record.getId())) {
            throw new InvalidParameterException("The given record's id already exists and is not new: [" + record.getId() + "].");
        }

        ContentValues values = new ContentValues();
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID, record.getId());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_BATTLETAG, record.getBattleTag());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_PLATFORM, record.getPlatform());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_REGION, record.getRegion());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_RATING, record.getRating().getValue());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_NOTE, record.getNote());
        Date date = DateTimeHelper.getCurrentUtilDate();
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_CREATION_DATETIME, DateTimeHelper.toString(date));
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_LASTUPDATE_DATETIME, DateTimeHelper.toString(date));

        getWritableDb().insert(OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME, null, values);
    }

    public void updateOwPlayerRecord(@NonNull OwPlayerRecord record) {
        if (!record.isValid()) {
            throw new InvalidParameterException("Invalid record object " + record.toString());
        }

        boolean isNewRecord = !hasOwPlayerRecordId(record.getId());
        if (isNewRecord) {
            saveNewOwPlayerRecord(record);
            return;
        }

        ContentValues values = new ContentValues();
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_BATTLETAG, record.getBattleTag());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_PLATFORM, record.getPlatform());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_REGION, record.getRegion());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_RATING, record.getRating().getValue());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_NOTE, record.getNote());
        Date date = DateTimeHelper.getCurrentUtilDate();
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_LASTUPDATE_DATETIME, DateTimeHelper.toString(date));

        String where = OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { record.getId() };

        getWritableDb().update(OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME, values, where, selectionArgs);
    }

    public OwPlayerRecord getOwPlayerRecordWithId(String id) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = getOwPlayerRecordProjection();

        // Filter results WHERE "title" = 'My Title'
        String selection = OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { id };

        Cursor cursor = getReadableDb().query(
                OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        if (!cursor.moveToFirst()) {
            return null;
        }

        return getOwPlayerRecordFromCurrentCursorPos(cursor);
    }


    public List<OwPlayerRecord> getAllOwPlayerRecord() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = getOwPlayerRecordProjection();


        Cursor cursor = getReadableDb().query(
                OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                      // The sort order
        );

        List<OwPlayerRecord> list = new ArrayList<>();
        if (!cursor.moveToFirst()) {
            return list;
        }

        do {
            OwPlayerRecord record = getOwPlayerRecordFromCurrentCursorPos(cursor);
            if (null == record) {
                return null;
            }
            list.add(record);
        } while (cursor.moveToNext());

        return list;
    }

    private SQLiteDatabase getReadableDb() {
        return new OwPlayerRecordDbHelper(context).getReadableDatabase();
    }

    private SQLiteDatabase getWritableDb() {
        return new OwPlayerRecordDbHelper(context).getWritableDatabase();
    }

    private String[] getOwPlayerRecordProjection() {
        return new String[] {
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_BATTLETAG,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_PLATFORM,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_REGION,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_RATING,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_NOTE,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_CREATION_DATETIME,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_LASTUPDATE_DATETIME
        };
    }

    private OwPlayerRecord getOwPlayerRecordFromCurrentCursorPos(@NonNull Cursor cursor) {
        OwPlayerRecord record = new OwPlayerRecord();
        record.setId(cursor.getString(0));
        record.setBattleTag(cursor.getString(1));
        record.setPlatform(cursor.getString(2));
        record.setRegion(cursor.getString(3));
        record.setRating(OwPlayerRecord.valueToRating(cursor.getInt(4)));
        record.setNote(cursor.getString(5));
        try {
            record.setRecordCreateDatetime(DateTimeHelper.toSqlDate(cursor.getString(6)));
            record.setRecordLastUpdateDatetime(DateTimeHelper.toSqlDate(cursor.getString(7)));
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }
        return record;
    }
}
