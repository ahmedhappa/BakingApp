package com.example.ahmed.bakingapp.Model;

import android.provider.BaseColumns;

/**
 * Created by Ahmed on 31/01/2018.
 */

public class MyContractClass {

    private MyContractClass() {
    }

    public static final class IngeridentTable implements BaseColumns {
        public static final String TABLE_NAME = "ingredient";

        public static final String QUANTITY = "quantity";
        public static final String MEASURE = "measure";
        public static final String INGREDIENT = "ingredient_name";
    }

    public static final class StepTable implements BaseColumns {
        public static final String TABLE_NAME = "step";

        public static final String SHORT_DESCRIPTION = "short_description";
        public static final String DESCRIPTION = "description";
        public static final String VIDEO_URL = "video_url";
        public static final String THUMBERL_URL = "thumbnail_url";
    }
}
