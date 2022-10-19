package org.biohazard4050.infiniterecharge;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.util.JsonWriter;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import org.biohazard4050.infiniterecharge.data.MatchRecord;
import org.biohazard4050.infiniterecharge.data.ScoutingContract.ScoutingData;
import org.biohazard4050.infiniterecharge.data.ScoutingDbHelper;

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
        String filename = "infiniterecharge_scouting.json";
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
                ScoutingData.COLUMN_AUTO_BASELINE_IND,
                ScoutingData.COLUMN_AUTO_PORT_TOP,
                ScoutingData.COLUMN_AUTO_PORT_BOT,
                ScoutingData.COLUMN_AUTO_WHEEL_SPIN,  // Shouldn't have added but need to
                ScoutingData.COLUMN_AUTO_WHEEL_COLOR, // keep since already in Tableau
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

        Cursor cursor = scoutingDb.query(ScoutingData.TABLE_PRIMARY_DATA, projection, null, null, null, null, ScoutingData._ID);

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
            int autoWheelSpinIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_WHEEL_SPIN);   // Shouldn't have added but need to
            int autoWheelColorIndex = cursor.getColumnIndex(ScoutingData.COLUMN_AUTO_WHEEL_COLOR); // keep since already in Tableau
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
                MatchRecord matchRecord = new MatchRecord();

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
                matchRecord.autoWheelSpin = "FALSE";  // Shouldn't have added but need to
                matchRecord.autoWheelColor = "FALSE"; // keep since already in Tableau
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
        writer.name("auto_baseline").value(matchRecord.baseline);
        writer.name("auto_port_top").value(matchRecord.autoPortTop);
        writer.name("auto_port_bot").value(matchRecord.autoPortBot);
        writer.name("auto_wheel_spin").value(matchRecord.autoWheelSpin);   // Shouldn't have added but need to
        writer.name("auto_wheel_color").value(matchRecord.autoWheelColor); // keep since already in Tableau
        writer.name("tele_port_top").value(matchRecord.telePortTop);
        writer.name("tele_port_bot").value(matchRecord.telePortBot);
        writer.name("tele_wheel_spin").value(matchRecord.teleWheelSpin);
        writer.name("tele_wheel_color").value(matchRecord.teleWheelColor);
        writer.name("ave_delivery_time").value(matchRecord.aveDelivery);
        writer.name("eom_state").value(matchRecord.endState);
        writer.name("penalty_yellow").value(matchRecord.penaltyYellow);
        writer.name("penalty_red").value(matchRecord.penaltyRed);
        writer.name("penalty_foul").value(matchRecord.penaltyFoul);
        writer.name("penalty_disabled").value(matchRecord.penaltyDisabled);
        writer.name("match_notes").value(matchRecord.matchNotes);
        writer.endObject();
    }
}
