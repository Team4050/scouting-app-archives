package org.biohazard4050.deepspace.data;

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
        public static final String COLUMN_INFO_START_HAB_LEVEL = "start_hab_level";
        public static final String COLUMN_AUTO_BASELINE_IND = "baseline_ind";
        public static final String COLUMN_AUTO_ROCKET_TOP_CARGO = "auto_rocket_top_cargo";
        public static final String COLUMN_AUTO_ROCKET_MID_CARGO = "auto_rocket_mid_cargo";
        public static final String COLUMN_AUTO_ROCKET_BOT_CARGO = "auto_rocket_bot_cargo";
        public static final String COLUMN_AUTO_ROCKET_TOP_HATCH = "auto_rocket_top_hatch";
        public static final String COLUMN_AUTO_ROCKET_MID_HATCH = "auto_rocket_mid_hatch";
        public static final String COLUMN_AUTO_ROCKET_BOT_HATCH = "auto_rocket_bot_hatch";
        public static final String COLUMN_AUTO_CARGO_SHIP_CARGO = "auto_cargo_ship_cargo";
        public static final String COLUMN_AUTO_CARGO_SHIP_HATCH = "auto_cargo_ship_hatch";
        public static final String COLUMN_TELE_ROCKET_TOP_CARGO = "tele_rocket_top_cargo";
        public static final String COLUMN_TELE_ROCKET_MID_CARGO = "tele_rocket_mid_cargo";t Line
        public static final String COLUMN_TELE_ROCKET_BOT_CARGO = "tele_rocket_bot_cargo";
        public static final String COLUMN_TELE_ROCKET_TOP_HATCH = "tele_rocket_top_hatch";
        public static final String COLUMN_TELE_ROCKET_MID_HATCH = "tele_rocket_mid_hatch";
        public static final String COLUMN_TELE_ROCKET_BOT_HATCH = "tele_rocket_bot_hatch";
        public static final String COLUMN_TELE_CARGO_SHIP_CARGO = "tele_cargo_ship_cargo";
        public static final String COLUMN_TELE_CARGO_SHIP_HATCH = "tele_cargo_ship_hatch";
        public static final String COLUMN_TELE_AVE_DELIVERY_TIME = "ave_delivery_time";
        public static final String COLUMN_EOM_HAB_LEVEL = "eom_hab_level";
        public static final String COLUMN_EOM_ASSIST_LEVEL = "eom_assist_level";
        public static final String COLUMN_EOM_WAS_ASSISTED = "eom_was_assisted";
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

        public static final String HABITAT_NOTHING = "NOTHING";
        public static final String HABITAT_LEVEL_1 = "LEVEL1";
        public static final String HABITAT_LEVEL_2 = "LEVEL2";
        public static final String HABITAT_LEVEL_3 = "LEVEL3";
    }

}
