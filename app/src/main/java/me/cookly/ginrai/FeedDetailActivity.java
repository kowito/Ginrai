package me.cookly.ginrai;

import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;

public class FeedDetailActivity extends AppCompatActivity {

    public static final String extra = "lksd32iuo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);

        HashMap<String, Object> hash = (HashMap<String, Object>) getIntent().getSerializableExtra(extra);
        TextView title = (TextView) findViewById(R.id.textViewFeedTitle);
        title.setText(hash.get("dish").toString());
    }
}
