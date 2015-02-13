package uk.co.appsbystudio.damealiceowens.Pages.newsContentViews;

import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import uk.co.appsbystudio.damealiceowens.R;

public class NewsContentSlider extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_content_slider);
        ((TextView) findViewById(R.id.item_content)).setText(getIntent().getStringExtra("content"));

    }

}
