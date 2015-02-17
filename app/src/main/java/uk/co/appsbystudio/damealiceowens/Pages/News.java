package uk.co.appsbystudio.damealiceowens.Pages;

import android.app.Activity;
import android.app.Fragment;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import uk.co.appsbystudio.damealiceowens.MainActivity;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsContentSlider;
import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsList;
import uk.co.appsbystudio.damealiceowens.R;
import uk.co.appsbystudio.damealiceowens.util.RSSFeedParser;
import uk.co.appsbystudio.damealiceowens.util.RSSItem;

public class News extends Fragment {

	private ArrayList<RSSItem> items;

	public final ClickListener listener = new ClickListener();

	private NewsList list;
	private NewsContentSlider detail;
    private Intent intentDetail;

    public News() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.fragment_news, container, false);

	    RSSFeedParser parser = new RSSFeedParser(this);
	    parser.execute("http://pastebin.com/raw.php?i=ZNcEAy7r");

	    list = new NewsList();
	    list.setListenerContext(this);
	    detail = new NewsContentSlider();
	    getChildFragmentManager().beginTransaction().replace(R.id.list_frame, list).addToBackStack(null).commit();
	    //getChildFragmentManager().beginTransaction().replace(R.id.detail_frame, detail).addToBackStack(null).commit();


	    return view;
    }

	public void rssParseCallback(ArrayList<RSSItem> array) {
		items = array;
		list.onRSSParse(items);
	}

	public class ClickListener implements ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			// TODO: animate this to slide in
            /*try {
                URL imageUrl = new URL(items.get(position).getString("image"));
                Bitmap imageBit = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());
                intentDetail.putExtra("image", imageUrl);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            String itemTitle = items.get(position).getString("title");
            String itemContent = items.get(position).getString("description");
            String itemImage = items.get(position).getString("url");

            intentDetail = new Intent(getActivity(), NewsContentSlider.class);
            intentDetail.putExtra("title", itemTitle);
            intentDetail.putExtra("content", itemContent);
            if (itemImage != null && !itemImage.isEmpty()) {
                intentDetail.putExtra("image", itemImage);
            } else {
                intentDetail.putExtra("image", "NO IMAGE");
            }

            startActivity(intentDetail);

			//list.getView().findViewById(R.id.newsList).setVisibility(View.GONE);
		}
	}
}
