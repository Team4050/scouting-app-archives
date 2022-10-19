package org.biohazard4050.deepspace;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.biohazard4050.deepspace.data.ScoutingContract.ScoutingData;
import org.biohazard4050.deepspace.data.ScoutingDbHelper;

public class EndOfMatch extends AppCompatActivity {

    private String endState = ScoutingData.HABITAT_NOTHING;
    private String habAssist = ScoutingData.HABITAT_NOTHING;
    private String wasAssisted = ScoutingData.CHECKBOX_UNCHECKED;
    private String penaltyYellow = ScoutingData.CHECKBOX_UNCHECKED;
    private String penaltyRed = ScoutingData.CHECKBOX_UNCHECKED;
    private String penaltyFoul = ScoutingData.CHECKBOX_UNCHECKED;
    private String penaltyDisabled = ScoutingData.CHECKBOX_UNCHECKED;

    private String matchNumber = "???";
    private String teamNumber = "???";
    private String allianceColor = "???";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_match);

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

        setTitle("End of Match [ Match: " + matchNumber + " | Team: " + teamNumber + " (" +
                allianceColor + ") ]");

        RadioButton didNothingRadio = (RadioButton) findViewById(R.id.nothingRadioButton);

        didNothingRadio.setChecked(true);

        RadioButton habAssistNothingRadio = (RadioButton) findViewById(R.id.habAssistNothingRadioButton);

        habAssistNothingRadio.setChecked(true);
    }

    public void onButtonClicked(View view) {
        writeToDB();

        Intent theIntent = new Intent(EndOfMatch.this, MatchNotes.class);

        theIntent.putExtra("MATCH_NUMBER", matchNumber);
        theIntent.putExtra("TEAM_NUMBER", teamNumber);
        theIntent.putExtra("ALLIANCE_COLOR", allianceColor);

        startActivity(theIntent);
    }

    public void onCheckboxClicked(View view) {
        boolean isChecked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.wasAssistedCheckbox:
                if (isChecked) {
                    wasAssisted = ScoutingData.CHECKBOX_CHECKED;
                } else {
                    wasAssisted = ScoutingData.CHECKBOX_UNCHECKED;
                }
                break;
            case R.id.penaltyYellowCheckbox:
                if (isChecked) {
                    penaltyYellow = ScoutingData.CHECKBOX_CHECKED;
                } else {
                    penaltyYellow = ScoutingData.CHECKBOX_UNCHECKED;
                }
                break;
            case R.id.penaltyRedCheckbox:
                if (isChecked) {
                    penaltyRed = ScoutingData.CHECKBOX_CHECKED;
                } else {
                    penaltyRed = ScoutingData.CHECKBOX_UNCHECKED;
                }
                break;
            case R.id.penaltyFoulCheckbox:
                if (isChecked) {
                    penaltyFoul = ScoutingData.CHECKBOX_CHECKED;
                } else {
                    penaltyFoul = ScoutingData.CHECKBOX_UNCHECKED;
                }
                break;
            case R.id.penaltyDisabledCheckbox:
                if (isChecked) {
                    penaltyDisabled = ScoutingData.CHECKBOX_CHECKED;
                } else {
                    penaltyDisabled = ScoutingData.CHECKBOX_UNCHECKED;
                }
                break;
        }
    }

    public void onRadioButtonClicked(View view) {
        boolean isChecked = ((RadioButton) view).isChecked();

        switch (view.getId()) {
            case R.id.nothingRadioButton:
                if (isChecked) {
                    endState = ScoutingData.HABITAT_NOTHING;
                }
                break;
            case R.id.habitat1RadioButton:
                if (isChecked) {
                    endState = ScoutingData.HABITAT_LEVEL_1;
                }
                break;
            case R.id.habitat2RadioButton:
                if (isChecked) {
                    endState = ScoutingData.HABITAT_LEVEL_2;
                }
                break;
            case R.id.habitat3RadioButton:
                if (isChecked) {
                    endState = ScoutingData.HABITAT_LEVEL_3;
                }
                break;
            case R.id.habAssistNothingRadioButton:
                if (isChecked) {
                    habAssist = ScoutingData.HABITAT_NOTHING;
                }
                break;
            case R.id.habAssistLevel2RadioButton:
                if (isChecked) {
                    habAssist = ScoutingData.HABITAT_LEVEL_2;
                }
                break;
            case R.id.habAssistLevel3RadioButton:
                if (isChecked) {
                    habAssist = ScoutingData.HABITAT_LEVEL_3;
                }
                break;
        }
    }

    private void writeToDB() {
        ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
        SQLiteDatabase scoutingDb = scoutingDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ScoutingData.COLUMN_EOM_HAB_LEVEL, endState);
        values.put(ScoutingData.COLUMN_EOM_ASSIST_LEVEL, habAssist);
        values.put(ScoutingData.COLUMN_EOM_WAS_ASSISTED, wasAssisted);
        values.put(ScoutingData.COLUMN_EOM_PENALTY_YELLOW, penaltyYellow);
        values.put(ScoutingData.COLUMN_EOM_PENALTY_RED, penaltyRed);
        values.put(ScoutingData.COLUMN_EOM_PENALTY_FOUL, penaltyFoul);
        values.put(ScoutingData.COLUMN_EOM_PENALTY_DISABLED, penaltyDisabled);

        int updateCount = scoutingDb.update(ScoutingData.TABLE_STAGING_DATA, values, null, null);

        if (updateCount == 0) {
            Toast missingDataToast = Toast.makeText(getApplicationContext(), "Error updating match", Toast.LENGTH_LONG);
            missingDataToast.setGravity(Gravity.CENTER, 0, 0);
            missingDataToast.show();
        }

        scoutingDb.close();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
            SQLiteDatabase scoutingDb = scoutingDbHelper.getReadableDatabase();

            String[] projection = {
                    ScoutingData.COLUMN_FINALIZED_IND
            };

            String finalized = "";

            Cursor cursor = scoutingDb.query(ScoutingData.TABLE_STAGING_DATA, projection, null, null, null, null, null);

            try {
                int finalizedIndex = cursor.getColumnIndex(ScoutingData.COLUMN_FINALIZED_IND);

                while (cursor.moveToNext()) {
                    finalized = cursor.getString(finalizedIndex);
                }
            } finally {
                cursor.close();
            }

            scoutingDb.close();

            if (finalized.equals(ScoutingData.CHECKBOX_CHECKED)) {
                this.finish();
            }
        }
    }
}
