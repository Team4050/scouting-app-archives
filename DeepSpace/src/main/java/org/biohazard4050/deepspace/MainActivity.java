package org.biohazard4050.deepspace;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.JsonWriter;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.biohazard4050.deepspace.data.MatchRecord;
import org.biohazard4050.deepspace.data.ScoutingContract.ScoutingData;
import org.biohazard4050.deepspace.data.ScoutingDbHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
        SQLiteDatabase scoutingDb = scoutingDbHelper.getWritableDatabase();
        scoutingDb.delete(ScoutingData.TABLE_STAGING_DATA, null, null);
        scoutingDb.close();

    }

    public void onButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.beginScoutingButton:
                Intent theIntent = new Intent(this, MatchInfo.class);
                startActivity(theIntent);
                break;
            case R.id.dumpDataFileButton:
                writeDataFile();
                break;
        }
    }

    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
            SQLiteDatabase scoutingDb = scoutingDbHelper.getWritableDatabase();
            scoutingDb.delete(ScoutingData.TABLE_STAGING_DATA, null, null);
            scoutingDb.close();
        }
    }

    private void writeDataFile() {
        String dirPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String filename = "deepspace_scouting.json";
        String resultMessage = "";

        try {
            ArrayList<MatchRecord> matchDataRecords = new ArrayList<MatchRecord>();

            readMatchDataFromDB(matchDataRecords);

            File file = new File(dirPath, filename);
            FileOutputStream outputStream = new FileOutputStream(file, false);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            writer.setIndent("  ");

            writer.beginObject();
            writer.name("matches");
            writer.beginArray();

            for (MatchRecord matchRecord : matchDataRecords) {
                writeMatchRecord(matchRecord, writer);
            }

            writer.endArray();
            writer.endObject();
            writer.close();

            outputStream.flush();
            outputStream.close();

            resultMessage = "File Written Successfully";
        } catch (Exception e) {
            resultMessage = e.getMessage();
        }

        Toast missingDataToast = Toast.makeText(getApplicationContext(), resultMessage, Toast.LENGTH_LONG);
        missingDataToast.setGravity(Gravity.CENTER, 0, 0);
        missingDataToast.show();
    }

    private void readMatchDataFromDB(ArrayList<MatchRecord> matchDataRecords) {
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

        Cursor cursor = scoutingDb.query(ScoutingData.TABLE_PRIMARY_DATA, projection, null, null, null, null, ScoutingData._ID);

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
                MatchRecord matchRecord = new MatchRecord();

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

                matchDataRecords.add(matchRecord);
            }
        } finally {
            cursor.close();
        }

        scoutingDb.close();
    }

    private void writeMatchRecord(MatchRecord matchRecord, JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name("scouter_name").value(matchRecord.scouter);
        writer.name("match_number").value(matchRecord.match);
        writer.name("is_rematch").value(matchRecord.rematch);
        writer.name("team_number").value(matchRecord.team);
        writer.name("alliance_color").value(matchRecord.alliance);
        writer.name("station_position").value(matchRecord.stationPos);
        writer.name("robot_position").value(matchRecord.robotPos);
        writer.name("start_hab_level").value(matchRecord.startHabLevel);
        writer.name("auto_baseline").value(matchRecord.baseline);
        writer.name("auto_rocket_top_cargo").value(matchRecord.autoRocketTopCargo);
        writer.name("auto_rocket_mid_cargo").value(matchRecord.autoRocketMidCargo);
        writer.name("auto_rocket_bot_cargo").value(matchRecord.autoRocketBotCargo);
        writer.name("auto_rocket_top_hatch").value(matchRecord.autoRocketTopHatch);
        writer.name("auto_rocket_mid_hatch").value(matchRecord.autoRocketMidHatch);
        writer.name("auto_rocket_bot_hatch").value(matchRecord.autoRocketBotHatch);
        writer.name("auto_cargo_ship_cargo").value(matchRecord.autoCargoShipCargo);
        writer.name("auto_cargo_ship_hatch").value(matchRecord.autoCargoShipHatch);
        writer.name("tele_rocket_top_cargo").value(matchRecord.teleRocketTopCargo);
        writer.name("tele_rocket_mid_cargo").value(matchRecord.teleRocketMidCargo);
        writer.name("tele_rocket_bot_cargo").value(matchRecord.teleRocketBotCargo);
        writer.name("tele_rocket_top_hatch").value(matchRecord.teleRocketTopHatch);
        writer.name("tele_rocket_mid_hatch").value(matchRecord.teleRocketMidHatch);
        writer.name("tele_rocket_bot_hatch").value(matchRecord.teleRocketBotHatch);
        writer.name("tele_cargo_ship_cargo").value(matchRecord.teleCargoShipCargo);
        writer.name("tele_cargo_ship_hatch").value(matchRecord.teleCargoShipHatch);
        writer.name("ave_delivery_time").value(matchRecord.aveDelivery);
        writer.name("eom_hab_level").value(matchRecord.endHabLevel);
        writer.name("eom_assist_level").value(matchRecord.endAssistLevel);
        writer.name("eom_was_assisted").value(matchRecord.endWasAssisted);
        writer.name("penalty_yellow").value(matchRecord.penaltyYellow);
        writer.name("penalty_red").value(matchRecord.penaltyRed);
        writer.name("penalty_foul").value(matchRecord.penaltyFoul);
        writer.name("penalty_disabled").value(matchRecord.penaltyDisabled);
        writer.name("match_notes").value(matchRecord.matchNotes);
        writer.endObject();
    }

}
