package com.example.micha.musicservice;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void handleMusic(View view) {
        switch (view.getId()) {

            case R.id.play:

                break;

            case R.id.stop:

                break;

            case R.id.fastforward:

                break;

            case R.id.rewind:

                break;
        }
    }
}
