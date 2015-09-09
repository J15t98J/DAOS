package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.DatabaseHelper;
import uk.co.appsbystudio.damealiceowens.util.ImageDownloader;
import uk.co.appsbystudio.damealiceowens.util.ManualDownloadClickListener;
import uk.co.appsbystudio.damealiceowens.util.json.JSONItem;

public class NewsItem extends Fragment {

	private DatabaseHelper dbHelper;
	private SQLiteDatabase db;

	private String guid;
	private JSONItem feedItem;

	private final HashMap<String, ImageView> imageViews = new HashMap<>();

	//guid = getIntent().getStringExtra("guid");
	//parseInput(getIntent().getStringExtra("title"), getIntent().getStringExtra("content"));

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.activity_news_item, container, false);
	}

	@Override
	public void onPause() {
		super.onPause();

		if(db.isOpen()) {
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
			dbHelper.close();
		}
	}

	@Override
	public void onResume() {
		super.onResume();

		dbHelper = new DatabaseHelper(getActivity());
		db = dbHelper.getWritableDatabase();
		db.beginTransaction();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
		menuInflater.inflate(R.menu.menu_news_item, menu);
	}

	@Override
	public void onPrepareOptionsMenu(Menu menu) {
		if(db.isOpen()) {
			feedItem = dbHelper.getItem(db, guid);
			menu.findItem(R.id.action_toggleReadStatus).setIcon(feedItem.getBool("isRead") ? R.drawable.ic_action_mark_unread : R.drawable.ic_action_mark_read)
					.setTitle(feedItem.getBool("isRead") ? R.string.action_mark_unread : R.string.action_mark_read);
			menu.findItem(R.id.action_toggleFlaggedStatus).setIcon(feedItem.getBool("isFlagged") ? R.drawable.ic_action_important : R.drawable.ic_action_not_important)
					.setTitle(feedItem.getBool("isFlagged") ? R.string.action_unflag : R.string.action_flag);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if(db.isOpen()) {
			feedItem = dbHelper.getItem(db, guid);
			switch (item.getItemId()) {
				case R.id.action_toggleReadStatus:
					boolean wasRead = feedItem.getBool("isRead");
					dbHelper.editItem(db, guid, "isRead", !wasRead ? "true" : "false");
					getActivity().invalidateOptionsMenu();
					Toast.makeText(getActivity(), wasRead ? "Marked as unread" : "Marked as read", Toast.LENGTH_SHORT).show();
					return true;
				case R.id.action_toggleFlaggedStatus:
					boolean wasFlagged = feedItem.getBool("isFlagged");
					dbHelper.editItem(db, guid, "isFlagged", !wasFlagged ? "true" : "false");
					getActivity().invalidateOptionsMenu();
					Toast.makeText(getActivity(), wasFlagged ? "Unflagged" : "Flagged", Toast.LENGTH_SHORT).show();
					return true;
				case R.id.action_delete:
					dbHelper.editItem(db, guid, "isHidden", "true");
					Toast.makeText(getActivity(), "Post hidden", Toast.LENGTH_SHORT).show();
					//this.finish(); TODO: return to empty view
					return true;
				default:
					return false;
			}
		}
		return false;
	}

	private void displayItem(String guid, String title, String content) {
		this.guid = guid;
		parseInput(title, content);
	}

	private void parseInput(String title, String content) {
		String shouldDownload = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("pref_key_download_images", "Never");
		NetworkInfo network = ((ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
		boolean wifiConnected = network != null && network.getType() == ConnectivityManager.TYPE_WIFI;
		((TextView)getActivity().findViewById(R.id.item_title)).setText(title);

		String contentCopy = content;
		final Matcher pattern = Pattern.compile("<img src=\"(.*?)\" />").matcher(contentCopy);
		while(pattern.find()) {
			String[] split = contentCopy.split(Pattern.quote(pattern.group()));
			if(!split[0].equals("")) {
				addNewTextView(split[0]);
			}
			contentCopy = split[1];

			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			params.gravity = Gravity.CENTER_HORIZONTAL;
			params.bottomMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));

			if(shouldDownload.equals("Always") || (shouldDownload.equals("Only via Wi-Fi") && wifiConnected)) {
				ImageView newImage = addNewImageView(BitmapFactory.decodeResource(getResources(), R.drawable.ic_icon_loading_image));
				newImage.setLayoutParams(params);
				imageViews.put(pattern.group(1), newImage);
				new ImageDownloader(getActivity(), this).execute(pattern.group(1));
			} else {
				ImageView newImage = addNewImageView(BitmapFactory.decodeResource(getResources(), R.drawable.manual_download));
				newImage.setLayoutParams(params);
				newImage.setClickable(true);
				newImage.setOnClickListener(new ManualDownloadClickListener(this, newImage, pattern.group(1)));
				newImage.setAdjustViewBounds(true);
				newImage.setMaxWidth(Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics())));
				imageViews.put(pattern.group(1), newImage);
			}
		}
		addNewTextView(contentCopy);
	}

	private void addNewTextView(String text) {
		TextView item = new TextView(getActivity());

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		params.bottomMargin = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics()));
		item.setLayoutParams(params);
		item.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15F);
		item.setTextColor(Color.parseColor("#000000"));
		item.setText(trimWhitespace(Html.fromHtml(text)));
		item.setMovementMethod(LinkMovementMethod.getInstance());
		item.setLinksClickable(true);
		//item.setTextIsSelectable(true);
		//item.setBackgroundColor(Color.argb(255, ((Double)(Math.random() * 255)).intValue(), ((Double)(Math.random() * 255)).intValue(), ((Double)(Math.random() * 255)).intValue()));

		((LinearLayout)getActivity().findViewById(R.id.item_frame)).addView(item);
	}

	private ImageView addNewImageView(Bitmap image) {
		ImageView item = new ImageView(getActivity());

		item.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		item.setImageBitmap(image);
		//item.setBackgroundColor(Color.parseColor("#00FF00"));

		((LinearLayout)getActivity().findViewById(R.id.item_frame)).addView(item);
		return item;
	}

	public void onImagesDownloaded(HashMap<String, Bitmap> images) {
		for(String image : images.keySet()) {
			if(images.get(image) != null) {
				ImageView view = imageViews.get(image);
				BitmapDrawable bitmap = new BitmapDrawable(getResources(), images.get(image));
				view.setImageDrawable(bitmap);
				view.setAdjustViewBounds(true);
				view.setMaxHeight(bitmap.getIntrinsicHeight());
				view.setMaxWidth(((LinearLayout)view.getParent()).getWidth());
			}
		}
		// TODO: scroll view if past pic so that reading is not interrupted
	}

	private CharSequence trimWhitespace(CharSequence item) {
		while(item.charAt(item.length()-1) == '\n') {
			item = item.subSequence(0, item.length()-1);
		}
		return item;
	}
}
