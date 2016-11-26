package me.cookly.ginrai;

import android.content.Intent;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

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

        Button back = (Button) findViewById(R.id.buttonFeedActivityBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
