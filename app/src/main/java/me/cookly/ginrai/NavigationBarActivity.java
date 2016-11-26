package me.cookly.ginrai;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class NavigationBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);


        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_share) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    System.out.println("share");
                }
                if (tabId == R.id.tab_live_feed) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    System.out.println("live feed");
                }
            }
        });
    }
}
