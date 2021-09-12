package com.kridacreations.diary;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class WelcomeScreen extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    Boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        firstTime = sharedPreferences.getBoolean("firstTime", true);

        if(firstTime){
            Button nextMainActivity = (Button) findViewById(R.id.nextMainActivity);
            nextMainActivity.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    firstTime = false;
                    editor.putBoolean("firstTime", false);
                    editor.apply();

                    Intent i = new Intent(WelcomeScreen.this, InfoActivity.class);
                    startActivity(i);
                    finish();
                }
            });

        } else {
            Intent i = new Intent(WelcomeScreen.this, InfoActivity.class);
            startActivity(i);
            finish();
        }
    }
}