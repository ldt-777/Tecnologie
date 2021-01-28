package com.example.schoolcheck;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class InitialActivity extends AppCompatActivity {                                             //nel manifest è resa l'activity di partenza dell' app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //getSupportActionBar().hide();                                                              //nasconde l'actionbar;

        Intent toMain = new Intent(InitialActivity.this, MainActivity.class);
        new Handler().postDelayed(new Runnable() {                                                  //handler per posticipare l'apertura della MainActivity;
            @Override
            public void run() {
                startActivity(toMain);
                finish();                                                                           //non è possibile in questa maniera tornare alla schermata;
            }
        }, 2000);
    }
}