package uk.co.appsbystudio.damealiceowens.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.db.databaseFile;
import uk.co.appsbystudio.damealiceowens.util.db.databaseHelper;

public class NewsItemAdapter<RSSItem> extends ArrayAdapter {

	private final Context context;
	private final ArrayList<RSSItem> content;

	public NewsItemAdapter(Context context, ArrayList<RSSItem> content) {
		super(context, R.layout.news_list_item, content);

		this.context = context;
		this.content = content;
	}

	@Override
	public View getView(int position, View currentRow, ViewGroup parent) {
		if(currentRow == null) {
			currentRow = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.news_list_item, parent, false);
		}

		uk.co.appsbystudio.damealiceowens.util.RSSItem item = (uk.co.appsbystudio.damealiceowens.util.RSSItem) content.get(position);

		// TODO: complete local storage
        //*
        databaseHelper dbHelper = new databaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] projection = {databaseFile.itemReadSchema.COLUMN_NAME_READ};

        Cursor cursor = db.query(databaseFile.itemReadSchema.TABLE_NAME, projection, null, null, null, null, null);
        //*/

        if (cursor == null) {
            item.setValue("isRead", "true");
        } else {
            item.setValue("isRead", "false");
        }

		currentRow.findViewById(R.id.item_title).getLayoutParams().width = context.getResources().getDisplayMetrics().widthPixels - 200;
		currentRow.findViewById(R.id.item_info).getLayoutParams().width = context.getResources().getDisplayMetrics().widthPixels - 200;

		if(item.getBool("isRead")) {
			((ImageView) currentRow.findViewById(R.id.item_readIcon)).setImageResource(R.drawable.ic_action_read);
            ((TextView) currentRow.findViewById(R.id.item_title)).setTypeface(null, Typeface.NORMAL);
            ((TextView) currentRow.findViewById(R.id.item_info)).setTypeface(null, Typeface.NORMAL);
		} else {
			((ImageView) currentRow.findViewById(R.id.item_readIcon)).setImageResource(R.drawable.ic_action_unread);
            ((TextView) currentRow.findViewById(R.id.item_title)).setTypeface(null, Typeface.BOLD);
            ((TextView) currentRow.findViewById(R.id.item_info)).setTypeface(null, Typeface.BOLD);
		}

        ((TextView) currentRow.findViewById(R.id.item_title)).setText(item.getString("title"));
		((TextView) currentRow.findViewById(R.id.item_info)).setText(item.getString("pubDate") + " by " + item.getString("author"));

		return currentRow;
	}
}
