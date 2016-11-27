package me.cookly.ginrai;

import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

public class NavigationBarActivity extends AppCompatActivity {

    private FirebaseRemoteConfig mFirebaseRemoteConfig;
    private ShareFragment shareFragment = null;
    private LiveFeedFragment liveFeedFragment = null;
    BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_bar);


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                if (tabId == R.id.tab_share) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    System.out.println("share");
                    startShareFragment();
                }
                if (tabId == R.id.tab_live_feed) {
                    // The tab with id R.id.tab_favorites was selected,
                    // change your content accordingly.
                    System.out.println("live feed");
                    startLiveFeedFragment(false);
                }
            }
        });

        System.out.println("setup push noti");
        setupPushNotification();
    }

    private void setupPushNotification(){
        if (isFirstTime()) {
            System.out.println("First TIme");
            mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .setDeveloperModeEnabled(BuildConfig.DEBUG)
                    .build();
            mFirebaseRemoteConfig.setConfigSettings(configSettings);
            //mFirebaseRemoteConfig.activateFetched();
            //subscribeToTopic();
            mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
            mFirebaseRemoteConfig.fetch().addOnCompleteListener(this, new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()){
                        System.out.println("fetched remote config");
                        mFirebaseRemoteConfig.activateFetched();
                    }else{
                        System.out.println("DID NOT fetched remote config");
                    }
                    subscribeToTopic();
                }
            });
                    /*
                    .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(MainActivity.this, "Fetch Succeeded",
                                        Toast.LENGTH_SHORT).show();

                                // Once the config is successfully fetched it must be activated before newly fetched
                                // values are returned.
                                mFirebaseRemoteConfig.activateFetched();
                            } else {
                                Toast.makeText(MainActivity.this, "Fetch Failed",
                                        Toast.LENGTH_SHORT).show();
                            }
                            displayWelcomeMessage();
                        }
                    });*/
        }
    }
    private void subscribeToTopic() {
        String value = mFirebaseRemoteConfig.getString("heaven_send");
        if (value.equals("no_notification")) {
            System.out.println("no notification");

        } else {
            System.out.println("send notification");
            FirebaseMessaging.getInstance().subscribeToTopic("heaven_send_notification");
        }

    }

    private boolean isFirstTime()
    {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean ranBefore = preferences.getBoolean("RanBefore", false);
        if (!ranBefore) {

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("RanBefore", true);
            editor.commit();
            // Send the SMS

        }
        //return true;
        return !ranBefore;

    }
    public void startShareFragment(){
        if(shareFragment == null) {
            shareFragment = new ShareFragment();
            System.out.println("New Fragment");
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.homepageContentContainer, shareFragment).commit();
    }

    public void startLiveFeedFragment(boolean setPosition){
        if(setPosition){
            bottomBar.selectTabAtPosition(1);
        }

        if(liveFeedFragment == null) {
            liveFeedFragment = new LiveFeedFragment();
            System.out.println("New Fragment");
        }
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.homepageContentContainer, liveFeedFragment).commit();
    }

}
