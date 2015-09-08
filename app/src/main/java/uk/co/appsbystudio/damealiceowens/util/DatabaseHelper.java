package uk.co.appsbystudio.damealiceowens.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.util.json.JSONItem;

public class DatabaseHelper extends SQLiteOpenHelper{

	private static final String DATABASE_NAME = "cache.db";
	private static final Integer DATABASE_VERSION = 1;

	private final String[] fields = new String[]{ "title", "datetime", "author", "content", "isRead", "isFlagged", "isHidden" };

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
	    db.beginTransaction();
	    try {
		    db.execSQL("CREATE TABLE IF NOT EXISTS ITEMS(guid TEXT PRIMARY KEY, title TEXT, datetime TEXT, author TEXT, content TEXT, isRead TEXT, isFlagged TEXT, isHidden TEXT)");
		    db.setTransactionSuccessful();
	    } finally {
		    db.endTransaction();
	    }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

	@Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

	public void addOrUpdateItem(SQLiteDatabase db, JSONItem item) {
		if(getItem(db, item.getString("guid")) != null) {
			updateItem(db, item.getString("guid"), item);
		} else {
			db.beginTransaction();
			try {
				db.execSQL("INSERT INTO ITEMS VALUES(?, ?, ?, ?, ?, ?, ?, ?)", new String[]{item.getString("guid"), item.getString("title"), item.getString("datetime"), item.getString("author"), item.getString("content"), "false", "false", "false"});
				db.setTransactionSuccessful();
			} finally {
				db.endTransaction();
			}
		}
	}

	public void editItem(SQLiteDatabase db, String guid, String attribute, String value) {
		db.beginTransaction();
		try {
			db.execSQL("UPDATE ITEMS SET " + attribute + "=? WHERE guid=?", new String[]{ value, guid });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public void removeItem(SQLiteDatabase db, String guid) {
		db.beginTransaction();
		try {
			db.execSQL("DELETE FROM ITEMS WHERE guid=?", new String[]{ guid });
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	private void updateItem(SQLiteDatabase db, String guid, JSONItem item) {
		db.beginTransaction();
		try {
			for(String field : fields) {
				if(item.getString(field) != null) {
					db.execSQL("UPDATE ITEMS SET " + field + "=? WHERE guid=?", new String[]{item.getString(field), guid});
				}
			}
			db.setTransactionSuccessful();
		} finally {
			db.endTransaction();
		}
	}

	public JSONItem getItem(SQLiteDatabase db, String guid) {
		JSONItem item = new JSONItem();

		Cursor results = db.rawQuery("SELECT * FROM ITEMS WHERE guid=?", new String[]{ guid });
		results.moveToFirst();
		if(results.getCount() < 1) {
			return null;
		}
		for(String field : results.getColumnNames()) {
			item.setValue(field, results.getString(results.getColumnIndex(field)));
		}
		results.close();

		return item;
	}

	public ArrayList<String> getAllItems(SQLiteDatabase db) {
		ArrayList<String> array = new ArrayList<>();

		Cursor results = db.rawQuery("SELECT * FROM ITEMS", null);
		results.moveToFirst();
		while(!results.isAfterLast()) {
			array.add(results.getString(results.getColumnIndex("guid")));
			results.moveToNext();
		}
		results.close();

		return array;
	}

	public ArrayList<JSONItem> getVisibleItems(SQLiteDatabase db) {
		ArrayList<JSONItem> array = new ArrayList<>();

		Cursor results = db.rawQuery("SELECT * FROM ITEMS WHERE isHidden=?", new String[]{ "false" });
		results.moveToFirst();

		while(!results.isAfterLast()) {
			array.add(getItem(db, results.getString(results.getColumnIndex("guid"))));
			results.moveToNext();
		}
		results.close();

		return array;
	}

}
