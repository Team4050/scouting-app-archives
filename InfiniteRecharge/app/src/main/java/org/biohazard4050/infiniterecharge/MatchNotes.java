package org.biohazard4050.infiniterecharge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.biohazard4050.infiniterecharge.data.MatchRecord;
import org.biohazard4050.infiniterecharge.data.ScoutingContract.ScoutingData;
import org.biohazard4050.infiniterecharge.data.ScoutingDbHelper;

public class MatchNotes extends AppCompatActivity {

    private String matchNumber = "???";
    private String teamNumber = "???";
    private String allianceColor = "???";

    private String matchNotes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_notes);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                matchNumber = extras.getString("MATCH_NUMBER");
                teamNumber = extras.getString("TEAM_NUMBER");
                allianceColor = extras.getString("ALLIANCE_COLOR");
            }
        }

        setTitle("Match Notes [ Match: " + matchNumber + " | Team: " + teamNumber + " (" +
                allianceColor + ") ]");
    }

    public void onButtonClicked(View view) {
        EditText inputTxt = (EditText) findViewById(R.id.matchNotesEditText);
        matchNotes = inputTxt.getText().toString();

        writeToDB();

        MatchRecord matchRecord = new MatchRecord();
        readStagingData(matchRecord);
        writePrimaryData(matchRecord);

        this.finish();
    }

    private void writeToDB() {
        ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
        SQLiteDatabase scoutingDb = scoutingDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ScoutingData.COLUMN_MATCH_NOTES, matchNotes);
        values.put(ScoutingData.COLUMN_FINALIZED_IND, ScoutingData.CHECKBOX_CHECKED);

        int updateCount = scoutingDb.update(ScoutingData.TABLE_STAGING_DATA, values, null, null);

        if (updateCount == 0) {
            Toast missingDataToast = Toast.makeText(getApplicationContext(), "Error updating match", Toast.LENGTH_LONG);
            missingDataToast.setGravity(Gravity.CENTER, 0, 0);
            missingDataToast.show();
        }

        scoutingDb.close();
    }

    private void readStagingData(MatchRecord matchRecord) {
        ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
        SQLiteDatabase scoutingDb = scoutingDbHelper.getReadableDatabase();

        String[] projection = {
                ScoutingData.COLUMN_INFO_SCOUTER_NAME,
                ScoutingData.COLUMN_INFO_MATCH_NUMBER,
                ScoutingData.COLUMN_INFO_REMATCH_IND,
                ScoutingData.COLUMN_INFO_TEAM_NUMBER,
                ScoutingData.COLUMN_INFO_ALLIANCE_COLOR,
                ScoutingData.COLUMN_INFO_STATION_POSITION,
                ScoutingData.COLUMN_INFO_ROBOT_POSITION,
                ScoutingData.COLUMN_AUTO_BASELINE_IND,
                ScoutingData.COLUMN_AUTO_PORT_TOP,
                ScoutingData.COLUMN_AUTO_PORT_BOT,
                ScoutingData.COLUMN_TELE_PORT_TOP,
                ScoutingData.COLUMN_TELE_PORT_BOT,
                ScoutingData.COLUMN_TELE_WHEEL_SPIN,
                ScoutingData.COLUMN_TELE_WHEEL_COLOR,
                ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME,
                ScoutingData.COLUMN_EOM_STATE,
                ScoutingData.COLUMN_EOM_PENALTY_YELLOW,
                ScoutingData.COLUMN_EOM_PENALTY_RED,
                ScoutingData.COLUMN_EOM_PENALTY_FOUL,
                ScoutingData.COLUMN_EOM_PENALTY_DISABLED,
                ScoutingData.COLUMN_MATCH_NOTES
        };

        Cursor cursor = scoutingDb.query(ScoutingData.TABLE_STAGING_DATA, projection, null, null, null, null, null);

        try {
            int scouterIndex = cursor.getColumnIndex(ScoutingData.COLUMN_INFO_SCOUTER_NAME);
            int matchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_INFO_MATCH_NUMBER);
            int rematchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_INFO_REMATCH_IND);
            int teamIndex = cursor.getColumnIndex(ScoutingData.COLUMN_INFO_TEAM_NUMBER);
            int allianceIndex = cursor.getColumnIndex(ScoutingData.COLUMN_INFO_ALLIANCE_COLOR);
            int stationIndex = cursor.getColumnIndex(ScoutingData.COLUMN_INFO_STATION_POSITION);
            int robotIndex = cursor.getColumnIndex(ScoutingData.COLUMN_INFO_ROBOT_POSITION);
            int baselineIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_BASELINE_IND);
            int autoPortTopIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_PORT_TOP);
            int autoPortBotIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_PORT_BOT);
            int telePortTopIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_PORT_TOP);
            int telePortBotIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_PORT_BOT);
            int teleWheelSpinIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_WHEEL_SPIN);
            int teleWheelColorIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_WHEEL_COLOR);
            int aveDeliveryIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME);
            int endStateIndex = cursor.getColumnIndex(ScoutingData.COLUMN_EOM_STATE);
            int penaltyYellowIndex = cursor.getColumnIndex(ScoutingData.COLUMN_EOM_PENALTY_YELLOW);
            int penaltyRedIndex = cursor.getColumnIndex(ScoutingData.COLUMN_EOM_PENALTY_RED);
            int penaltyFoulIndex = cursor.getColumnIndex(ScoutingData.COLUMN_EOM_PENALTY_FOUL);
            int penaltyDisabledIndex = cursor.getColumnIndex(ScoutingData.COLUMN_EOM_PENALTY_DISABLED);
            int matchNotesIndex = cursor.getColumnIndex(ScoutingData.COLUMN_MATCH_NOTES);

            while (cursor.moveToNext()) {
                matchRecord.scouter = cursor.getString(scouterIndex);
                matchRecord.match = cursor.getString(matchIndex);
                matchRecord.rematch = cursor.getString(rematchIndex);
                matchRecord.team = cursor.getString(teamIndex);
                matchRecord.alliance = cursor.getString(allianceIndex);
                matchRecord.stationPos = cursor.getString(stationIndex);
                matchRecord.robotPos = cursor.getString(robotIndex);
                matchRecord.baseline = cursor.getString(baselineIndex);
                matchRecord.autoPortTop = cursor.getString(autoPortTopIndex);
                matchRecord.autoPortBot = cursor.getString(autoPortBotIndex);
                matchRecord.telePortTop = cursor.getString(telePortTopIndex);
                matchRecord.telePortBot = cursor.getString(telePortBotIndex);
                matchRecord.teleWheelSpin = cursor.getString(teleWheelSpinIndex);
                matchRecord.teleWheelColor = cursor.getString(teleWheelColorIndex);
                matchRecord.aveDelivery = cursor.getString(aveDeliveryIndex);
                matchRecord.endState = cursor.getString(endStateIndex);
                matchRecord.penaltyYellow = cursor.getString(penaltyYellowIndex);
                matchRecord.penaltyRed = cursor.getString(penaltyRedIndex);
                matchRecord.penaltyFoul = cursor.getString(penaltyFoulIndex);
                matchRecord.penaltyDisabled = cursor.getString(penaltyDisabledIndex);
                matchRecord.matchNotes = cursor.getString(matchNotesIndex);
            }
        } finally {
            cursor.close();
        }

        scoutingDb.close();
    }

    private void writePrimaryData(MatchRecord matchRecord) {
        ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
        SQLiteDatabase scoutingDb = scoutingDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ScoutingData.COLUMN_INFO_SCOUTER_NAME, matchRecord.scouter);
        values.put(ScoutingData.COLUMN_INFO_MATCH_NUMBER, matchRecord.match);
        values.put(ScoutingData.COLUMN_INFO_REMATCH_IND, matchRecord.rematch);
        values.put(ScoutingData.COLUMN_INFO_TEAM_NUMBER, matchRecord.team);
        values.put(ScoutingData.COLUMN_INFO_ALLIANCE_COLOR, matchRecord.alliance);
        values.put(ScoutingData.COLUMN_INFO_STATION_POSITION, matchRecord.stationPos);
        values.put(ScoutingData.COLUMN_INFO_ROBOT_POSITION, matchRecord.robotPos);
        values.put(ScoutingData.COLUMN_AUTO_BASELINE_IND, matchRecord.baseline);
        values.put(ScoutingData.COLUMN_AUTO_PORT_TOP, matchRecord.autoPortTop);
        values.put(ScoutingData.COLUMN_AUTO_PORT_BOT, matchRecord.autoPortBot);
        values.put(ScoutingData.COLUMN_TELE_PORT_TOP, matchRecord.telePortTop);
        values.put(ScoutingData.COLUMN_TELE_PORT_BOT, matchRecord.telePortBot);
        values.put(ScoutingData.COLUMN_TELE_WHEEL_SPIN, matchRecord.teleWheelSpin);
        values.put(ScoutingData.COLUMN_TELE_WHEEL_COLOR, matchRecord.teleWheelColor);
        values.put(ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME, matchRecord.aveDelivery);
        values.put(ScoutingData.COLUMN_EOM_STATE, matchRecord.endState);
        values.put(ScoutingData.COLUMN_EOM_PENALTY_YELLOW, matchRecord.penaltyYellow);
        values.put(ScoutingData.COLUMN_EOM_PENALTY_RED, matchRecord.penaltyRed);
        values.put(ScoutingData.COLUMN_EOM_PENALTY_FOUL, matchRecord.penaltyFoul);
        values.put(ScoutingData.COLUMN_EOM_PENALTY_DISABLED, matchRecord.penaltyDisabled);
        values.put(ScoutingData.COLUMN_MATCH_NOTES, matchRecord.matchNotes);

        try {
            long newRowId = scoutingDb.insertOrThrow(ScoutingData.TABLE_PRIMARY_DATA, null, values);

            if (newRowId == 0) {
                Toast missingDataToast = Toast.makeText(getApplicationContext(), "Error with saving to primary table", Toast.LENGTH_LONG);
                missingDataToast.setGravity(Gravity.CENTER, 0, 0);
                missingDataToast.show();
            }
        } catch (Exception e) {
            Toast missingDataToast = Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG);
            missingDataToast.setGravity(Gravity.CENTER, 0, 0);
            missingDataToast.show();
        }

        scoutingDb.close();
    }
}
