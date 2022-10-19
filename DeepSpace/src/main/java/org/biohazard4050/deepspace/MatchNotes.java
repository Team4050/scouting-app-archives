package org.biohazard4050.deepspace;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.biohazard4050.deepspace.data.MatchRecord;
import org.biohazard4050.deepspace.data.ScoutingContract.ScoutingData;
import org.biohazard4050.deepspace.data.ScoutingDbHelper;

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

                TextView titleText = (TextView) findViewById(R.id.titleTextView);

                if (allianceColor.equals("RED")) {
                    titleText.setTextColor(Color.RED);
                } else {
                    titleText.setTextColor(Color.BLUE);
                }
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
                ScoutingData.COLUMN_INFO_START_HAB_LEVEL,
                ScoutingData.COLUMN_AUTO_BASELINE_IND,
                ScoutingData.COLUMN_AUTO_ROCKET_TOP_CARGO,
                ScoutingData.COLUMN_AUTO_ROCKET_MID_CARGO,
                ScoutingData.COLUMN_AUTO_ROCKET_BOT_CARGO,
                ScoutingData.COLUMN_AUTO_ROCKET_TOP_HATCH,
                ScoutingData.COLUMN_AUTO_ROCKET_MID_HATCH,
                ScoutingData.COLUMN_AUTO_ROCKET_BOT_HATCH,
                ScoutingData.COLUMN_AUTO_CARGO_SHIP_CARGO,
                ScoutingData.COLUMN_AUTO_CARGO_SHIP_HATCH,
                ScoutingData.COLUMN_TELE_ROCKET_TOP_CARGO,
                ScoutingData.COLUMN_TELE_ROCKET_MID_CARGO,
                ScoutingData.COLUMN_TELE_ROCKET_BOT_CARGO,
                ScoutingData.COLUMN_TELE_ROCKET_TOP_HATCH,
                ScoutingData.COLUMN_TELE_ROCKET_MID_HATCH,
                ScoutingData.COLUMN_TELE_ROCKET_BOT_HATCH,
                ScoutingData.COLUMN_TELE_CARGO_SHIP_CARGO,
                ScoutingData.COLUMN_TELE_CARGO_SHIP_HATCH,
                ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME,
                ScoutingData.COLUMN_EOM_HAB_LEVEL,
                ScoutingData.COLUMN_EOM_ASSIST_LEVEL,
                ScoutingData.COLUMN_EOM_WAS_ASSISTED,
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
            int startHabIndex = cursor.getColumnIndex(ScoutingData.COLUMN_INFO_START_HAB_LEVEL);
            int baselineIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_BASELINE_IND);
            int autoRocTopCargoIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_ROCKET_TOP_CARGO);
            int autoRocMidCargoIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_ROCKET_MID_CARGO);
            int autoRocBotCargoIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_ROCKET_BOT_CARGO);
            int autoRocTopHatchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_ROCKET_TOP_HATCH);
            int autoRocMidHatchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_ROCKET_MID_HATCH);
            int autoRocBotHatchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_ROCKET_BOT_HATCH);
            int autoCargoCargoIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_CARGO_SHIP_CARGO);
            int autoCargoHatchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_CARGO_SHIP_HATCH);
            int teleRocTopCargoIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_ROCKET_TOP_CARGO);
            int teleRocMidCargoIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_ROCKET_MID_CARGO);
            int teleRocBotCargoIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_ROCKET_BOT_CARGO);
            int teleRocTopHatchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_ROCKET_TOP_HATCH);
            int teleRocMidHatchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_ROCKET_MID_HATCH);
            int teleRocBotHatchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_ROCKET_BOT_HATCH);
            int teleCargoCargoIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_CARGO_SHIP_CARGO);
            int teleCargoHatchIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_CARGO_SHIP_HATCH);
            int aveDeliveryIndex = cursor.getColumnIndex(ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME);
            int eomHabLevelIndex = cursor.getColumnIndex(ScoutingData.COLUMN_EOM_HAB_LEVEL);
            int eomAssistLevelIndex = cursor.getColumnIndex(ScoutingData.COLUMN_EOM_ASSIST_LEVEL);
            int eomWasAssistedIndex = cursor.getColumnIndex(ScoutingData.COLUMN_EOM_WAS_ASSISTED);
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
                matchRecord.startHabLevel = cursor.getString(startHabIndex);
                matchRecord.baseline = cursor.getString(baselineIndex);
                matchRecord.autoRocketTopCargo = cursor.getString(autoRocTopCargoIndex);
                matchRecord.autoRocketMidCargo = cursor.getString(autoRocMidCargoIndex);
                matchRecord.autoRocketBotCargo = cursor.getString(autoRocBotCargoIndex);
                matchRecord.autoRocketTopHatch = cursor.getString(autoRocTopHatchIndex);
                matchRecord.autoRocketMidHatch = cursor.getString(autoRocMidHatchIndex);
                matchRecord.autoRocketBotHatch = cursor.getString(autoRocBotHatchIndex);
                matchRecord.autoCargoShipCargo = cursor.getString(autoCargoCargoIndex);
                matchRecord.autoCargoShipHatch = cursor.getString(autoCargoHatchIndex);
                matchRecord.teleRocketTopCargo = cursor.getString(teleRocTopCargoIndex);
                matchRecord.teleRocketMidCargo = cursor.getString(teleRocMidCargoIndex);
                matchRecord.teleRocketBotCargo = cursor.getString(teleRocBotCargoIndex);
                matchRecord.teleRocketTopHatch = cursor.getString(teleRocTopHatchIndex);
                matchRecord.teleRocketMidHatch = cursor.getString(teleRocMidHatchIndex);
                matchRecord.teleRocketBotHatch = cursor.getString(teleRocBotHatchIndex);
                matchRecord.teleCargoShipCargo = cursor.getString(teleCargoCargoIndex);
                matchRecord.teleCargoShipHatch = cursor.getString(teleCargoHatchIndex);
                matchRecord.aveDelivery = cursor.getString(aveDeliveryIndex);
                matchRecord.endHabLevel = cursor.getString(eomHabLevelIndex);
                matchRecord.endAssistLevel = cursor.getString(eomAssistLevelIndex);
                matchRecord.endWasAssisted = cursor.getString(eomWasAssistedIndex);
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
        values.put(ScoutingData.COLUMN_INFO_START_HAB_LEVEL, matchRecord.startHabLevel);
        values.put(ScoutingData.COLUMN_AUTO_BASELINE_IND, matchRecord.baseline);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_TOP_CARGO, matchRecord.autoRocketTopCargo);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_MID_CARGO, matchRecord.autoRocketMidCargo);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_BOT_CARGO, matchRecord.autoRocketBotCargo);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_TOP_HATCH, matchRecord.autoRocketTopHatch);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_MID_HATCH, matchRecord.autoRocketMidHatch);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_BOT_HATCH, matchRecord.autoRocketBotHatch);
        values.put(ScoutingData.COLUMN_AUTO_CARGO_SHIP_CARGO, matchRecord.autoCargoShipCargo);
        values.put(ScoutingData.COLUMN_AUTO_CARGO_SHIP_HATCH, matchRecord.autoCargoShipHatch);
        values.put(ScoutingData.COLUMN_TELE_ROCKET_TOP_CARGO, matchRecord.teleRocketTopCargo);
        values.put(ScoutingData.COLUMN_TELE_ROCKET_MID_CARGO, matchRecord.teleRocketMidCargo);
        values.put(ScoutingData.COLUMN_TELE_ROCKET_BOT_CARGO, matchRecord.teleRocketBotCargo);
        values.put(ScoutingData.COLUMN_TELE_ROCKET_TOP_HATCH, matchRecord.teleRocketTopHatch);
        values.put(ScoutingData.COLUMN_TELE_ROCKET_MID_HATCH, matchRecord.teleRocketMidHatch);
        values.put(ScoutingData.COLUMN_TELE_ROCKET_BOT_HATCH, matchRecord.teleRocketBotHatch);
        values.put(ScoutingData.COLUMN_TELE_CARGO_SHIP_CARGO, matchRecord.teleCargoShipCargo);
        values.put(ScoutingData.COLUMN_TELE_CARGO_SHIP_HATCH, matchRecord.teleCargoShipHatch);
        values.put(ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME, matchRecord.aveDelivery);
        values.put(ScoutingData.COLUMN_EOM_HAB_LEVEL, matchRecord.endHabLevel);
        values.put(ScoutingData.COLUMN_EOM_ASSIST_LEVEL, matchRecord.endAssistLevel);
        values.put(ScoutingData.COLUMN_EOM_WAS_ASSISTED, matchRecord.endWasAssisted);
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
