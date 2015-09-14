package uk.co.appsbystudio.damealiceowens;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsItem;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;
import uk.co.appsbystudio.damealiceowens.util.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

	public DatabaseHelper dbHelper;
	public SQLiteDatabase db;

	private NewsItem detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

	    dbHelper = new DatabaseHelper(this);
	    db = dbHelper.getWritableDatabase();

		detail = new NewsItem();

		getSupportFragmentManager().beginTransaction().add(R.id.list_fragment_container, new NewsList()).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.detail_fragment_container, detail).commit();

		if(getSupportActionBar() != null) {
			getSupportActionBar().setTitle(R.string.title_activity_main);
		}
    }

	public void onPostSelected(String guid, String title, String content) {
		if(detail != null) {
			detail.displayItem(guid, title, content);
			if(getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi < 720) {
				findViewById(R.id.list_fragment_container).setVisibility(View.GONE);
				findViewById(R.id.detail_fragment_container).setVisibility(View.VISIBLE);
			}
		}
	}
}
