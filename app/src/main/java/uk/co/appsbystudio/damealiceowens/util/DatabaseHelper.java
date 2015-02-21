package uk.co.appsbystudio.damealiceowens.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{

	public static final String DATABASE_NAME = "cache.db";
    public static final Integer DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS ITEMS(guid TEXT PRIMARY KEY, title TEXT, pubDate TEXT, author TEXT, description TEXT, isRead INTEGER, isFlagged INTEGER, isHidden INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

	@Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	public void addItem(SQLiteDatabase db, RSSItem item) {
		db.execSQL("INSERT INTO ITEMS VALUES('" + item.getString("guid") + "','" + item.getString("title") + "','" + item.getString("pubDate") + "','" + item.getString("author") + "','" + item.getString("description") + "','" + item.getBool("isRead") + "','" + item.getBool("isFlagged") + "','" + item.getBool("isHidden") + "')");
	}

	public void editItem(SQLiteDatabase db, Integer guid, String attribute, String value) {
		db.execSQL("UPDATE ITEMS SET " + attribute + "=" + value + " WHERE guid=" + guid);
	}

	public RSSItem getItem(SQLiteDatabase db, String guid) {
		RSSItem item = new RSSItem();

		System.out.println(guid);
		Cursor results = db.rawQuery("SELECT * FROM ITEMS WHERE guid='" + guid + "'", null);
		results.moveToFirst();
		if(results.getCount() < 1) {
			return null;
		}
		for(String field : results.getColumnNames()) {
			System.out.println(field);

			item.setValue(field, results.getString(results.getColumnIndex(field)));
		}
		results.close();
		return item;
	}

	public ArrayList<RSSItem> getVisibleitems(SQLiteDatabase db) {
		ArrayList<RSSItem> array = new ArrayList<RSSItem>();

		Cursor results = db.rawQuery("SELECT * FROM ITEMS WHERE isHidden='false'", null);
		results.moveToFirst();

		while(results.moveToNext()) {
			array.add(getItem(db, results.getString(results.getColumnIndex("guid"))));
		}

		results.close();
		return array;
	}
}
