package com.example.customviewsurfaceview;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static android.view.View.SYSTEM_UI_FLAG_FULLSCREEN;

public class MainActivity extends AppCompatActivity {

    private GameView mGameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Lock orientation into landscape.
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // Create a GameView and bind it to this activity.
        // You don't need a ViewGroup to fill the screen, because the system
        // has a FrameLayout to which this will be added.
        mGameView = new GameView(this);
        // Android 4.1 and higher simple way to request fullscreen.
        mGameView.setSystemUiVisibility(SYSTEM_UI_FLAG_FULLSCREEN);
        setContentView(mGameView);
    }

    /**
     * Pauses game when activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        mGameView.pause();
    }

    /**
     * Resumes game when activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        mGameView.resume();
    }
}
