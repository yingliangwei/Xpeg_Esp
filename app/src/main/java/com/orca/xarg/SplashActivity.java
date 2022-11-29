package com.orca.xarg;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {
    private int i = 0;
    private ProgressBar progressBar;
    private Timer timer;

    static {
        System.loadLibrary("native");
    }

    private static native String Dev();

    @Override
    protected void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.layout_splash);
        TextView textView = findViewById(R.id.text);
        textView.setText(Dev());
        textView.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        this.progressBar = findViewById(R.id.progress);
        this.timer = new Timer();
        this.timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (i < 300) {
                    progressBar.setProgress(i);
                    i = i + 1;
                    return;
                }
                timer.cancel();
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, 0L, 5L);
    }
}
