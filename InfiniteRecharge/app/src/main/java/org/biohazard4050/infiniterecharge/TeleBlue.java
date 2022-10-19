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

import java.util.Locale;

public class TeleBlue extends AppCompatActivity {

    private static final int COLOR_PRIMARY_DARK = 0xFF303F9F;

    private String wheelSpinInd = ScoutingData.CHECKBOX_UNCHECKED;
    private String wheelColorInd = ScoutingData.CHECKBOX_UNCHECKED;

    private int countPowerPortTop = 0;
    private int countPowerPortBot = 0;

    private String aveDeliveryTime = "-1.0";

    private float[] secondsToDeliver;
    private int deliveryArrayIndex = 0;
    private long prevDeliveryTime;
    private long currDeliveryTime;

    private String matchNumber;
    private String teamNumber;
    private String allianceColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tele_blue);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();

            if (extras != null) {
                matchNumber = extras.getString("MATCH_NUMBER");
                teamNumber = extras.getString("TEAM_NUMBER");
                allianceColor = extras.getString("ALLIANCE_COLOR");
            }
        }

        setTitle("Teleop [ Match: " + matchNumber + " | Team: " + teamNumber + " (" +
                allianceColor + ") ]");

        secondsToDeliver = new float[99];
        prevDeliveryTime = System.currentTimeMillis();
        currDeliveryTime = System.currentTimeMillis();
    }

    private void recordDeliveryData() {
        prevDeliveryTime = currDeliveryTime;
        currDeliveryTime = System.currentTimeMillis();
        secondsToDeliver[deliveryArrayIndex] = (((float) (currDeliveryTime - prevDeliveryTime)) / 1000.0f);
        deliveryArrayIndex++;
    }

    public void onElementClicked(View view) {
        switch (view.getId()) {
            case R.id.powerPortTopPlusImageView:
                countPowerPortTop = updateCountAndDisplay(countPowerPortTop, R.id.powerPortCountTopTextView, 1);
                recordDeliveryData();
                break;
            case R.id.powerPortTopMinusImageView:
                countPowerPortTop = updateCountAndDisplay(countPowerPortTop, R.id.powerPortCountTopTextView, -1);
                break;
            case R.id.powerPortBotPlusImageView:
                countPowerPortBot = updateCountAndDisplay(countPowerPortBot, R.id.powerPortCountBotTextView, 1);
                recordDeliveryData();
                break;
            case R.id.powerPortBotMinusImageView:
                countPowerPortBot = updateCountAndDisplay(countPowerPortBot, R.id.powerPortCountBotTextView, -1);
                break;
            case R.id.wheelSpinImageView:
                if (wheelSpinInd.equals(ScoutingData.CHECKBOX_UNCHECKED)) {
                    wheelSpinInd = ScoutingData.CHECKBOX_CHECKED;
                    ImageView spinImage = (ImageView) findViewById(R.id.wheelSpinImageView);
                    spinImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wheel_spin_green));
                } else {
                    wheelSpinInd = ScoutingData.CHECKBOX_UNCHECKED;
                    ImageView spinImage = (ImageView) findViewById(R.id.wheelSpinImageView);
                    spinImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wheel_spin_red));
                }
                break;
            case R.id.wheelColorImageView:
                if (wheelColorInd.equals(ScoutingData.CHECKBOX_UNCHECKED)) {
                    wheelColorInd = ScoutingData.CHECKBOX_CHECKED;
                    ImageView spinImage = (ImageView) findViewById(R.id.wheelColorImageView);
                    spinImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wheel_color_green));
                } else {
                    wheelColorInd = ScoutingData.CHECKBOX_UNCHECKED;
                    ImageView spinImage = (ImageView) findViewById(R.id.wheelColorImageView);
                    spinImage.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.wheel_color_red));
                }
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
        float totalSeconds = 0;

        if (deliveryArrayIndex == 0) {
            aveDeliveryTime = "0.0";
        } else {
            for (int idx = 0; idx < deliveryArrayIndex; idx++)
            {
                totalSeconds += secondsToDeliver[idx];
            }

            float averageSeconds = (totalSeconds / (float) deliveryArrayIndex);

            aveDeliveryTime = String.format(Locale.US,"%.1f", averageSeconds);
        }

        writeToDB();

        Intent theIntent = new Intent(this, EndOfMatch.class);

        theIntent.putExtra("MATCH_NUMBER", matchNumber);
        theIntent.putExtra("TEAM_NUMBER", teamNumber);
        theIntent.putExtra("ALLIANCE_COLOR", allianceColor);

        startActivity(theIntent);
    }

    private void writeToDB() {
        ScoutingDbHelper scoutingDbHelper = new ScoutingDbHelper(this);
        SQLiteDatabase scoutingDb = scoutingDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ScoutingData.COLUMN_TELE_PORT_TOP, countPowerPortTop);
        values.put(ScoutingData.COLUMN_TELE_PORT_BOT, countPowerPortBot);
        values.put(ScoutingData.COLUMN_TELE_WHEEL_SPIN, wheelSpinInd);
        values.put(ScoutingData.COLUMN_TELE_WHEEL_COLOR, wheelColorInd);
        values.put(ScoutingData.COLUMN_TELE_AVE_DELIVERY_TIME, aveDeliveryTime);

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
