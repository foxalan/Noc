package com.example.notepad;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.menu.ActionMenuItemView;

import com.example.notepad.activity.GuideActivity;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences ;
    private SharedPreferences.Editor editor ;
    private boolean isGuide = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPreferences = getSharedPreferences("isGuide",MODE_PRIVATE);
        editor = sharedPreferences.edit();
        //editor.putBoolean("isguide",true);
       // editor.commit();
        isGuide =  sharedPreferences.getBoolean("isguide",true);
        if (isGuide){

            startActivity(new Intent(MainActivity.this, GuideActivity.class));

        }else {
            setContentView(R.layout.activity_main);
        }


    }
}
