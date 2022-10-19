package org.biohazard4050.deepspace.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.biohazard4050.deepspace.data.ScoutingContract.ScoutingData;

public class ScoutingDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "deepspace.db";

    private static final String SQL_CREATE_PRIMARY_DATA_TABLE =
            "CREATE TABLE " + ScoutingData.TABLE_PRIMARY_DATA + " (" +
                    ScoutingData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    ScoutingData.COLUMN_INFO_SCOUTER_NAME + " TEXT," +
                    ScoutingData.COLUMN_INFO_MATCH_NUMBER + " INTEGER," +
                    ScoutingData.COLUMN_INFO_REMATCH_IND + " TEXT," +
                    ScoutingData.COLUMN_INFO_TEAM_NUMBER + " TEXT," +
                    ScoutingData.COLUMN_INFO_ALLIANCE_COLOR + " TEXT," +
                    ScoutingData.COLUMN_INFO_STATION_POSITION + " TEXT," +
                    ScoutingData.COLUMN_INFO_ROBOT_POSITION + " TEXT," +
                    ScoutingData.COLUMN_INFO_START_HAB_LEVEL + " TEXT," +
                    ScoutingData.COLUMN_AUTO_BASELINE_IND + " TEXT," +
                    ScoutingData.COLUMN_AUTO_ROCKET_TOP_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_MID_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_BOT_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_TOP_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_MID_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_BOT_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_CARGO_SHIP_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_CARGO_SHIP_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_TOP_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_MID_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_BOT_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_TOP_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_MID_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_BOT_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_CARGO_SHIP_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_TELE_CARGO_SHIP_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME + " TEXT," +
                    ScoutingData.COLUMN_EOM_HAB_LEVEL + " TEXT," +
                    ScoutingData.COLUMN_EOM_ASSIST_LEVEL + " TEXT," +
                    ScoutingData.COLUMN_EOM_WAS_ASSISTED + " TEXT," +
                    ScoutingData.COLUMN_EOM_PENALTY_YELLOW + " TEXT," +
                    ScoutingData.COLUMN_EOM_PENALTY_RED + " TEXT," +
                    ScoutingData.COLUMN_EOM_PENALTY_FOUL + " TEXT," +
                    ScoutingData.COLUMN_EOM_PENALTY_DISABLED + " TEXT," +
                    ScoutingData.COLUMN_MATCH_NOTES + " TEXT)";

    private static final String SQL_DELETE_PRIMARY_DATA_TABLE =
            "DROP TABLE IF EXISTS " + ScoutingData.TABLE_PRIMARY_DATA;

    private static final String SQL_CREATE_STAGING_DATA_TABLE =
            "CREATE TABLE " + ScoutingData.TABLE_STAGING_DATA + " (" +
                    ScoutingData.COLUMN_INFO_SCOUTER_NAME + " TEXT," +
                    ScoutingData.COLUMN_INFO_MATCH_NUMBER + " INTEGER," +
                    ScoutingData.COLUMN_INFO_REMATCH_IND + " TEXT," +
                    ScoutingData.COLUMN_INFO_TEAM_NUMBER + " TEXT," +
                    ScoutingData.COLUMN_INFO_ALLIANCE_COLOR + " TEXT," +
                    ScoutingData.COLUMN_INFO_STATION_POSITION + " TEXT," +
                    ScoutingData.COLUMN_INFO_ROBOT_POSITION + " TEXT," +
                    ScoutingData.COLUMN_INFO_START_HAB_LEVEL + " TEXT," +
                    ScoutingData.COLUMN_AUTO_BASELINE_IND + " TEXT," +
                    ScoutingData.COLUMN_AUTO_ROCKET_TOP_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_MID_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_BOT_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_TOP_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_MID_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_ROCKET_BOT_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_CARGO_SHIP_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_AUTO_CARGO_SHIP_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_TOP_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_MID_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_BOT_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_TOP_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_MID_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_ROCKET_BOT_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_CARGO_SHIP_CARGO + " INTEGER," +
                    ScoutingData.COLUMN_TELE_CARGO_SHIP_HATCH + " INTEGER," +
                    ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME + " TEXT," +
                    ScoutingData.COLUMN_EOM_HAB_LEVEL + " TEXT," +
                    ScoutingData.COLUMN_EOM_ASSIST_LEVEL + " TEXT," +
                    ScoutingData.COLUMN_EOM_WAS_ASSISTED + " TEXT," +
                    ScoutingData.COLUMN_EOM_PENALTY_YELLOW + " TEXT," +
                    ScoutingData.COLUMN_EOM_PENALTY_RED + " TEXT," +
                    ScoutingData.COLUMN_EOM_PENALTY_FOUL + " TEXT," +
                    ScoutingData.COLUMN_EOM_PENALTY_DISABLED + " TEXT," +
                    ScoutingData.COLUMN_MATCH_NOTES + " TEXT," +
                    ScoutingData.COLUMN_FINALIZED_IND + " TEXT)";

    private static final String SQL_DELETE_STAGING_DATA_TABLE =
            "DROP TABLE IF EXISTS " + ScoutingData.TABLE_STAGING_DATA;

    public ScoutingDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_PRIMARY_DATA_TABLE);
        db.execSQL(SQL_CREATE_STAGING_DATA_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_PRIMARY_DATA_TABLE);
        db.execSQL(SQL_DELETE_STAGING_DATA_TABLE);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
