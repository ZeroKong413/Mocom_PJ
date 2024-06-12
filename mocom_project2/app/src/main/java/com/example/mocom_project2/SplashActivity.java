package com.example.mocom_project2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends Activity {
    // 스플래시 화면 시간 설정 (3초)
    private final int SPLASH_DISPLAY_TIME = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ab);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // 스플래시 화면 이후에 시작할 활동
                Intent mainIntent = new Intent(SplashActivity.this, LoginActivity.class);
                SplashActivity.this.startActivity(mainIntent);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_TIME);

    }

}