package org.biohazard4050.infiniterecharge.data;

import android.provider.BaseColumns;

public class ScoutingContract {

    public static abstract class ScoutingData {

        public static final String TABLE_PRIMARY_DATA = "primary_data";
        public static final String TABLE_STAGING_DATA = "staging_data";

        // Common columns
        public static final String COLUMN_INFO_SCOUTER_NAME = "scouter_name";
        public static final String COLUMN_INFO_MATCH_NUMBER = "match_number";
        public static final String COLUMN_INFO_REMATCH_IND = "rematch_ind";
        public static final String COLUMN_INFO_TEAM_NUMBER = "team_number";
        public static final String COLUMN_INFO_ALLIANCE_COLOR = "alliance_color";
        public static final String COLUMN_INFO_STATION_POSITION = "station_position";
        public static final String COLUMN_INFO_ROBOT_POSITION = "robot_position";
        public static final String COLUMN_AUTO_BASELINE_IND = "baseline_ind";
        public static final String COLUMN_AUTO_PORT_TOP = "auto_port_top";
        public static final String COLUMN_AUTO_PORT_BOT = "auto_port_bot";
        public static final String COLUMN_AUTO_WHEEL_SPIN = "auto_wheel_spin_ind";   // Shouldn't have added but need to
        public static final String COLUMN_AUTO_WHEEL_COLOR = "auto_wheel_color_ind"; // keep since already in Tableau
        public static final String COLUMN_TELE_PORT_TOP = "tele_port_top";
        public static final String COLUMN_TELE_PORT_BOT = "tele_port_bot";
        public static final String COLUMN_TELE_WHEEL_SPIN = "tele_wheel_spin_ind";
        public static final String COLUMN_TELE_WHEEL_COLOR = "tele_wheel_color_ind";
        public static final String COLUMN_TELE_AVE_DELIVERY_TIME = "ave_delivery_time";
        public static final String COLUMN_EOM_STATE = "eom_state";
        public static final String COLUMN_EOM_PENALTY_YELLOW = "penalty_yellow";
        public static final String COLUMN_EOM_PENALTY_RED = "penalty_red";
        public static final String COLUMN_EOM_PENALTY_FOUL = "penalty_foul";
        public static final String COLUMN_EOM_PENALTY_DISABLED = "penalty_disabled";
        public static final String COLUMN_MATCH_NOTES = "match_notes";

        // primary_data only
        public static final String _ID = BaseColumns._ID;

        // staging_data only
        public static final String COLUMN_FINALIZED_IND = "finalized_ind";

        // Constants for column values
        public static final String ALLIANCE_RED = "RED";
        public static final String ALLIANCE_BLUE = "BLUE";

        public static final String POSITION_FAR = "FAR";
        public static final String POSITION_MIDDLE = "MIDDLE";
        public static final String POSITION_NEAR = "NEAR";

        public static final String CHECKBOX_CHECKED = "TRUE";
        public static final String CHECKBOX_UNCHECKED = "FALSE";

        public static final String STATE_NOTHING = "NOTHING";
        public static final String STATE_PARKED = "PARKED";
        public static final String STATE_FAILED = "FAILED";
        public static final String STATE_SUCCESS = "SUCCESS";
    }

}
