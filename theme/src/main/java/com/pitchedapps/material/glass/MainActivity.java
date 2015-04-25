package com.pitchedapps.material.glass;

/**
 * Created by a on 21/03/2015.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(MainActivity.this, com.pitchedapps.material.glass.activities.Main.class);
        startActivity(intent);
    }
}
