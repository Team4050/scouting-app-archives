package org.biohazard4050.deepspace;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

public class Sandstorm extends AppCompatActivity {

    private static final int COLOR_PRIMARY_DARK = 0xFF303F9F;

    private String baselineInd = ScoutingData.CHECKBOX_UNCHECKED;

    private int countRocketTopCargo = 0;
    private int countRocketMidCargo = 0;
    private int countRocketBotCargo = 0;
    private int countRocketTopHatch = 0;
    private int countRocketMidHatch = 0;
    private int countRocketBotHatch = 0;
    private int countCargoShipCargo = 0;
    private int countCargoShipHatch = 0;

    private String matchNumber;
    private String teamNumber;
    private String allianceColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sandstorm);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                matchNumber = extras.getString("MATCH_NUMBER");
                teamNumber = extras.getString("TEAM_NUMBER");
                allianceColor = extras.getString("ALLIANCE_COLOR");
            }
        }

        setTitle("Sandstorm [ Match: " + matchNumber + " | Team: " + teamNumber + " (" +
                allianceColor + ") ]");
    }

    public void onCheckboxClicked(View view) {
        if (((CheckBox) view).isChecked()) {
            baselineInd = ScoutingData.CHECKBOX_CHECKED;
        } else {
            baselineInd = ScoutingData.CHECKBOX_UNCHECKED;
        }
    }

    public void onElementClicked(View view) {
        if (baselineInd == ScoutingData.CHECKBOX_UNCHECKED) {
            CheckBox baselineCheckbox = (CheckBox) findViewById(R.id.baselineInd);
            baselineCheckbox.setChecked(true);
            baselineInd = ScoutingData.CHECKBOX_CHECKED;
        }

        switch (view.getId()) {
            case R.id.rocketTopCargoImageView:
                countRocketTopCargo = updateCountAndDisplay(countRocketTopCargo, R.id.rocketTopCargoTextView);
                break;
            case R.id.rocketMidCargoImageView:
                countRocketMidCargo = updateCountAndDisplay(countRocketMidCargo, R.id.rocketMiddleCargoTextView);
                break;
            case R.id.rocketBotCargoImageView:
                countRocketBotCargo = updateCountAndDisplay(countRocketBotCargo, R.id.rocketBottomCargoTextView);
                break;
            case R.id.rocketTopHatchImageView:
                countRocketTopHatch = updateCountAndDisplay(countRocketTopHatch, R.id.rocketTopHatchTextView);
                break;
            case R.id.rocketMidHatchImageView:
                countRocketMidHatch = updateCountAndDisplay(countRocketMidHatch, R.id.rocketMiddleHatchTextView);
                break;
            case R.id.rocketBotHatchImageView:
                countRocketBotHatch = updateCountAndDisplay(countRocketBotHatch, R.id.rocketBottomHatchTextView);
                break;
            case R.id.cargoShipCargoImageView:
                countCargoShipCargo = updateCountAndDisplay(countCargoShipCargo, R.id.cargoShipCargoTextView);
                break;
            case R.id.cargoShipHatchImageView:
                countCargoShipHatch = updateCountAndDisplay(countCargoShipHatch, R.id.cargoShipHatchTextView);
                break;
            default:
                break;
        }
    }

    // Update count, ensuring 0-99; update display; return new count value.
    private int updateCountAndDisplay(int countVar, int itemId) {
        countVar++;

        countVar = ((countVar < 0) ? 0 : countVar);
        countVar = ((countVar > 9) ? 9 : countVar);

        TextView quantityTextView = (TextView) findViewById(itemId);
        quantityTextView.setText("" + countVar);

        return countVar;
    }

    public void onButtonClicked(View view) {
        writeToDB();

        Intent theIntent = new Intent(Sandstorm.this, Teleop.class);

        theIntent.putExtra("MATCH_NUMBER", matchNumber);
        theIntent.putExtra("TEAM_NUMBER", teamNumber);
        theIntent.putExtra("ALLIANCE_COLOR", allianceColor);

        startActivity(theIntent);
    }

    private void writeToDB() {
        ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
        SQLiteDatabase scoutingDb = scoutingDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ScoutingData.COLUMN_AUTO_BASELINE_IND, baselineInd);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_TOP_CARGO, countRocketTopCargo);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_MID_CARGO, countRocketMidCargo);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_BOT_CARGO, countRocketBotCargo);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_TOP_HATCH, countRocketTopHatch);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_MID_HATCH, countRocketMidHatch);
        values.put(ScoutingData.COLUMN_AUTO_ROCKET_BOT_HATCH, countRocketBotHatch);
        values.put(ScoutingData.COLUMN_AUTO_CARGO_SHIP_CARGO, countCargoShipCargo);
        values.put(ScoutingData.COLUMN_AUTO_CARGO_SHIP_HATCH, countCargoShipHatch);

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
