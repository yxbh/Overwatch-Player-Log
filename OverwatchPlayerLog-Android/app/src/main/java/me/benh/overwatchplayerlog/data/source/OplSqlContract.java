package me.benh.overwatchplayerlog.data.source;

/**
 * OverwatchPlayerLog SQL Contract class.
 */
public final class OplSqlContract {

    private OplSqlContract() {}

    private static final String SMALLINT_TYPE = " SMALLINT";
    private static final String TEXT_TYPE = " TEXT";
    private static final String BOOL_TYPE = " BOOLEAN";
    private static final String DATETIME_TYPE = " DATETIME";
    private static final String COMMA_SEP = ", ";

    public static class Tables {

        public static class Latest extends V1 {}

        public static class V1 {

            public static class MetaInfo {
                public static final String TABLE_NAME = "MetaInfo";
                public static final String COLUMN_NAME_KEY = "Key";
                public static final String COLUMN_NAME_VALUE = "Value";

                public static final String SQL_CREATE_TABLE =
                        "create table " + TABLE_NAME + "(" +
                                COLUMN_NAME_KEY + TEXT_TYPE + " unique" + COMMA_SEP +
                                COLUMN_NAME_VALUE + TEXT_TYPE + ")";

                public static final String SQL_DELETE_TABLE =
                        "DROP TABLE IF EXISTS " + TABLE_NAME;
            }

            public static class OwPlayerRecord {
                public static final String TABLE_NAME = "OwPlayerRecord";
                public static final String COLUMN_NAME_ID = "Id";
                public static final String COLUMN_NAME_BATTLETAG = "BattleTag";
                public static final String COLUMN_NAME_PLATFORM = "Platform";
                public static final String COLUMN_NAME_REGION = "Region";
                public static final String COLUMN_NAME_IS_FAVORITE = "IsFavorite";
                public static final String COLUMN_NAME_RATING = "Rating";
                public static final String COLUMN_NAME_NOTE = "Note";
                public static final String COLUMN_NAME_CREATION_DATETIME = "CreationDateTime";
                public static final String COLUMN_NAME_LASTUPDATE_DATETIME = "LastUpdateDateTime";

                public static final String SQL_CREATE_TABLE =
                        "CREATE TABLE " + TABLE_NAME + " (" +
                                COLUMN_NAME_ID          + TEXT_TYPE + " UNIQUE"   + COMMA_SEP +
                                COLUMN_NAME_BATTLETAG   + TEXT_TYPE + " UNIQUE"   + COMMA_SEP +
                                COLUMN_NAME_PLATFORM    + TEXT_TYPE + " NOT NULL" + COMMA_SEP +
                                COLUMN_NAME_REGION      + TEXT_TYPE + COMMA_SEP +
                                COLUMN_NAME_IS_FAVORITE + BOOL_TYPE + COMMA_SEP +
                                COLUMN_NAME_RATING      + SMALLINT_TYPE + COMMA_SEP +
                                COLUMN_NAME_NOTE        + TEXT_TYPE + COMMA_SEP +
                                COLUMN_NAME_CREATION_DATETIME   + DATETIME_TYPE + COMMA_SEP +
                                COLUMN_NAME_LASTUPDATE_DATETIME + DATETIME_TYPE + COMMA_SEP +
                                "PRIMARY KEY(" + COLUMN_NAME_ID + "));";

                public static final String SQL_DELETE_TABLE =
                        "DROP TABLE IF EXISTS " + TABLE_NAME;

                public static final String SQL_SELECT_WITH_ID =
                        "SELECT COUNT(*) FROM " + TABLE_NAME + "WHERE " + COLUMN_NAME_ID + " = ?";
            }

        } // V1

    } // Tables
} // OplSqlContract
