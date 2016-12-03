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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import me.benh.overwatchplayerlog.data.OwPlayerRecord;
import me.benh.lib.helpers.DateTimeHelper;

/**
 * Created by benhuang on 23/11/16.
 */

public class DataSource {

    public static final String TAG = DataSource.class.getSimpleName();

    private static final String SQL_SELECT_WITH_ID = OplSqlContract.Tables.V1.OwPlayerRecord.SQL_SELECT_WITH_ID;

    private Context context;
    private SQLiteDatabase writeDatabase;
    private SQLiteDatabase readDatabase;

    public DataSource(@NonNull Context context) {
        this.context = context;
    }

    public boolean hasOwPlayerRecordId(@NonNull String id) {
        SQLiteDatabase db = getWritableDb();
        Cursor cursor = db.rawQuery(SQL_SELECT_WITH_ID, new String[] { id });
        cursor.moveToNext();

        boolean hasRecord = cursor.getInt(0) == 1;

        cursor.close();

        return hasRecord;
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
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_IS_FAVORITE, record.isFavorite());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_PLATFORM, record.getPlatform());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_REGION, record.getRegion());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_RATING, record.getRating().getValue());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_NOTE, record.getNote());
        Date date = DateTimeHelper.getCurrentUtilDate();
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_CREATION_DATETIME, DateTimeHelper.toString(date));
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_LASTUPDATE_DATETIME, DateTimeHelper.toString(date));

        SQLiteDatabase db = getWritableDb();
        db.beginTransaction();
        db.insert(OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME, null, values);
        db.setTransactionSuccessful();
        db.endTransaction();
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
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_IS_FAVORITE, record.isFavorite());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_PLATFORM, record.getPlatform());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_REGION, record.getRegion());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_RATING, record.getRating().getValue());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_NOTE, record.getNote());
        Date date = DateTimeHelper.getCurrentUtilDate();
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_LASTUPDATE_DATETIME, DateTimeHelper.toString(date));

        String where = OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { record.getId() };

        SQLiteDatabase db = getWritableDb();
        db.beginTransaction();
        db.update(OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME, values, where, selectionArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public OwPlayerRecord getOwPlayerRecordWithId(String id) {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = getOwPlayerRecordProjection();

        // Filter results WHERE "title" = 'My Title'
        String selection = OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { id };

        SQLiteDatabase db = getReadableDb();
        Cursor cursor = db.query(
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

        OwPlayerRecord record = getOwPlayerRecordFromCurrentCursorPos(cursor);
        cursor.close();

        return record;
    }


    public List<OwPlayerRecord> getAllOwPlayerRecords() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = getOwPlayerRecordProjection();

        SQLiteDatabase db = getReadableDb();
        Cursor cursor = db.query(
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
        cursor.close();

        sortOwPlayerRecordCollection(list);

        return list;
    }

    public List<OwPlayerRecord> getAllFavoriteOwPlayerRecords() {
        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = getOwPlayerRecordProjection();
        String selection = OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_IS_FAVORITE + " = TRUE";

        SQLiteDatabase db = getReadableDb();
        Cursor cursor = db.query(
                OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME,                     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
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
        cursor.close();

        sortOwPlayerRecordCollection(list);

        return list;
    }

    public void removeOwPlayerRecord(@NonNull OwPlayerRecord record) {
        String selection = OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { record.getId() };
        SQLiteDatabase db = getWritableDb();
        db.beginTransaction();
        db.delete(OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME, selection, selectionArgs);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void removeAllOwPlayerRecords() {
        SQLiteDatabase db = getWritableDb();
        db.beginTransaction();
        db.delete(OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME, null, null);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    private SQLiteDatabase getReadableDb() {
        return (null != readDatabase) ? readDatabase : (readDatabase = new OwPlayerRecordDbHelper(context).getReadableDatabase());
    }

    private SQLiteDatabase getWritableDb() {
        return (null != writeDatabase) ? writeDatabase : (writeDatabase = new OwPlayerRecordDbHelper(context).getReadableDatabase());
    }

    public void close() {
        if (null != readDatabase) {
            readDatabase.close();
        }
        if (null != writeDatabase) {
            writeDatabase.close();
        }
    }

    private String[] getOwPlayerRecordProjection() {
        return new String[] {
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_BATTLETAG,
            OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_IS_FAVORITE,
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
        record.setFavorite(cursor.getInt(2) != 0);
        record.setPlatform(cursor.getString(3));
        record.setRegion(cursor.getString(4));
        record.setRating(OwPlayerRecord.valueToRating(cursor.getInt(5)));
        record.setNote(cursor.getString(6));
        try {
            record.setRecordCreateDatetime(DateTimeHelper.toSqlDate(cursor.getString(7)));
            record.setRecordLastUpdateDatetime(DateTimeHelper.toSqlDate(cursor.getString(8)));
        } catch (ParseException e) {
            Log.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }
        return record;
    }

    private static void sortOwPlayerRecordCollection(List<OwPlayerRecord> collection) {
        Collections.sort(collection, new Comparator<OwPlayerRecord>() {
            @Override
            public int compare(OwPlayerRecord lhs, OwPlayerRecord rhs) {
                int res = String.CASE_INSENSITIVE_ORDER.compare(lhs.getBattleTag(), rhs.getBattleTag());
                return (res != 0) ? res : lhs.getBattleTag().compareTo(rhs.getBattleTag());
            }
        });
    }
}
