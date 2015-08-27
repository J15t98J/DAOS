package uk.co.appsbystudio.damealiceowens.util;

import android.view.View;
import android.widget.Toast;

import uk.co.appsbystudio.damealiceowens.Pages.newsContentViews.NewsItem;

public class ManualDownloadClickListener implements View.OnClickListener {

    private final NewsItem activity;
    private final String URL;

    public ManualDownloadClickListener(NewsItem activity, String URL) {
        this.activity = activity;
        this.URL = URL;
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(activity, "Downloading...", Toast.LENGTH_SHORT).show();
        new ImageDownloader(activity).execute(URL);
    }
}
