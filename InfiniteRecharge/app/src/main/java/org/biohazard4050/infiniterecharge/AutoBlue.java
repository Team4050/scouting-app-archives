package org.biohazard4050.infiniterecharge;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.biohazard4050.infiniterecharge.data.ScoutingContract.ScoutingData;
import org.biohazard4050.infiniterecharge.data.ScoutingDbHelper;

public class AutoBlue extends AppCompatActivity {

    private static final int COLOR_PRIMARY_DARK = 0xFF303F9F;

    private String baselineInd = ScoutingData.CHECKBOX_UNCHECKED;

    private int countPowerPortTop = 0;
    private int countPowerPortBot = 0;

    private String matchNumber;
    private String teamNumber;
    private String allianceColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_blue);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                matchNumber = extras.getString("MATCH_NUMBER");
                teamNumber = extras.getString("TEAM_NUMBER");
                allianceColor = extras.getString("ALLIANCE_COLOR");
            }
        }

        setTitle("Autonomous [ Match: " + matchNumber + " | Team: " + teamNumber + " (" +
                allianceColor + ") ]");
    }

    public void onElementClicked(View view) {
        switch (view.getId()) {
            case R.id.baselineImageView:
                if (baselineInd.equals(ScoutingData.CHECKBOX_UNCHECKED)) {
                    baselineInd = ScoutingData.CHECKBOX_CHECKED;
                    ImageView baselineImage = (ImageView) findViewById(R.id.baselineImageView);
                    baselineImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_green));
                } else {
                    baselineInd = ScoutingData.CHECKBOX_UNCHECKED;
                    ImageView baselineImage = (ImageView) findViewById(R.id.baselineImageView);
                    baselineImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.baseline_red));
                }
                break;
            case R.id.powerPortTopPlusImageView:
                countPowerPortTop = updateCountAndDisplay(countPowerPortTop, R.id.powerPortCountTopTextView, 1);
                break;
            case R.id.powerPortTopMinusImageView:
                countPowerPortTop = updateCountAndDisplay(countPowerPortTop, R.id.powerPortCountTopTextView, -1);
                break;
            case R.id.powerPortBotPlusImageView:
                countPowerPortBot = updateCountAndDisplay(countPowerPortBot, R.id.powerPortCountBotTextView, 1);
                break;
            case R.id.powerPortBotMinusImageView:
                countPowerPortBot = updateCountAndDisplay(countPowerPortBot, R.id.powerPortCountBotTextView, -1);
                break;
            default:
                break;
        }
    }

    // Update count, ensuring 0-99; update display; return new count value.
    private int updateCountAndDisplay(int countVar, int itemId, int incValue) {
        countVar += incValue;

        countVar = ((countVar < 0) ? 0 : countVar);
        countVar = ((countVar > 99) ? 99 : countVar);

        TextView quantityTextView = (TextView) findViewById(itemId);
        quantityTextView.setText("" + countVar);

        return countVar;
    }

    public void onButtonClicked(View view) {
        writeToDB();

        Intent theIntent = new Intent(this, TeleBlue.class);

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
        values.put(ScoutingData.COLUMN_AUTO_PORT_TOP, countPowerPortTop);
        values.put(ScoutingData.COLUMN_AUTO_PORT_BOT, countPowerPortBot);

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
