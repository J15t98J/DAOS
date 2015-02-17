package uk.co.appsbystudio.damealiceowens.util;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.R;

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
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		uk.co.appsbystudio.damealiceowens.util.RSSItem item = (uk.co.appsbystudio.damealiceowens.util.RSSItem) content.get(position);

		// TODO: replace temp value w/ logic that gets isRead from local storage
		item.setValue("isRead", "false");

		if(currentRow == null) {
			currentRow = inflater.inflate(R.layout.news_list_item, parent, false);
		}

		if(item.getBool("isRead")) {
			((ImageView) currentRow.findViewById(R.id.item_readIcon)).setImageResource(R.drawable.ic_action_read);
            ((TextView) currentRow.findViewById(R.id.item_title)).setTypeface(null, Typeface.NORMAL);
            ((TextView) currentRow.findViewById(R.id.item_info)).setTypeface(null, Typeface.NORMAL);
		} else {
			((ImageView) currentRow.findViewById(R.id.item_readIcon)).setImageResource(R.drawable.ic_action_unread);
            ((TextView) currentRow.findViewById(R.id.item_title)).setTypeface(null, Typeface.BOLD);
            ((TextView) currentRow.findViewById(R.id.item_info)).setTypeface(null, Typeface.BOLD);
		}

        if (item.getString("title").length() > 20) {
            ((TextView) currentRow.findViewById(R.id.item_title)).setText(item.getString("title").substring(0, 25) + "...");
        } else {
            ((TextView) currentRow.findViewById(R.id.item_title)).setText(item.getString("title"));
        }

		((TextView) currentRow.findViewById(R.id.item_info)).setText(item.getString("pubDate") + " by " + item.getString("author"));

		return currentRow;
	}
}
