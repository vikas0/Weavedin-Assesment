package com.vikas.itunepreview_weavedin.activities;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.vikas.itunepreview_weavedin.R;

public class SplashActivity extends BaseActivity {
View searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.splash_screen);
        searchView = findViewById(R.id.search_view);


        new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
             searchView.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    public void goToSearchScreen(View view) {
        activity.startActivity(new Intent(activity,SearchTracks.class));
        finish();
    }
}
