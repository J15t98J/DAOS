package uk.co.appsbystudio.damealiceowens.util.db;

import android.provider.BaseColumns;

public class databaseFile {

    public static abstract class itemReadSchema implements BaseColumns {
        public static final String TABLE_NAME = "itemRead";
        public static final String COLUMN_NAME_GUID = "guid";
        public static final String COLUMN_NAME_READ = "read";
    }

}
