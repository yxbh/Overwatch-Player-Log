package me.benh.overwatchplayerlog.data.source;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import me.benh.overwatchplayerlog.data.OwPlayerRecord;

/**
 * Created by benhuang on 23/11/16.
 */

public class DataSource {

    private static final String SQL_SELECT_WITH_ID = OplSqlContract.Tables.V1.OwPlayerRecord.SQL_SELECT_WITH_ID;
    private static final String SQL_TABLE_NAME_OWPLAYERRECORD = OplSqlContract.Tables.V1.OwPlayerRecord.TABLE_NAME;

    private Context context;

    public DataSource(Context context) {
        this.context = context;
    }

    public boolean hasOwPlayerRecordId(String id) {
        Cursor cursor = getReadableDb().rawQuery(SQL_SELECT_WITH_ID, new String[] { id });
        cursor.moveToNext();
        return cursor.getInt(0) == 1;
    }

    public boolean hasOwPlayerRecord(OwPlayerRecord record) {
        return hasOwPlayerRecordId(record.getId());
    }

    public void saveNewOwPlayerRecord(OwPlayerRecord record) {
        ContentValues values = new ContentValues();
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_ID, record.getId());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_BATTLETAG, record.getBattleTag());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_PLATFORM, record.getPlatform());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_REGION, record.getRegion());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_RATING, record.getRating().getValue());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_NOTE, record.getNote());
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_CREATION_DATETIME, "DATETIME('now')");
        values.put(OplSqlContract.Tables.Latest.OwPlayerRecord.COLUMN_NAME_LASTUPDATE_DATETIME, "DATETIME('now')");

        getWritableDb().insert(OplSqlContract.Tables.Latest.OwPlayerRecord.TABLE_NAME, null, values);
    }

    private SQLiteDatabase getReadableDb() {
        return new OwPlayerRecordDbHelper(context).getReadableDatabase();
    }

    private SQLiteDatabase getWritableDb() {
        return new OwPlayerRecordDbHelper(context).getWritableDatabase();
    }
}
