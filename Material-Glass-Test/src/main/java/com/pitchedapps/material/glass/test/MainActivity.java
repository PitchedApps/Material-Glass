package com.pitchedapps.material.glass.test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Constructor;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b1 = (Button) findViewById(R.id.button);
        b1.setOnClickListener(cmte);
    }

    View.OnClickListener cmte = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intentcm = getPackageManager().getLaunchIntentForPackage("org.cyanogenmod.theme.chooser");
                if (intentcm == null) {
                    Toast.makeText(getApplicationContext(), getString(R.string.cm_not_installed), Toast.LENGTH_SHORT).show();
                } else {
                    final String className = "com.pitchedapps.material.glass.test.CmThemeEngineLauncher";
                    Class<?> cl = null;
                    try {
                        cl = Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        Log.e("LAUNCHER CLASS MISSING", "Launcher class for: '" + className + "' missing!");
                    }
                    if (cl != null) {
                        Constructor<?> constructor = null;
                        try {
                            constructor = cl.getConstructor(Context.class);
                        } catch (NoSuchMethodException e) {
                            Log.e("LAUNCHER CLASS CONS",
                                    "Launcher class for: '" + className + "' is missing a constructor!");
                        }
    //                        try {
    //                            if (constructor != null)
    //                                constructor.newInstance(getActivity());
    //                        } catch (Exception e) {
    //                            e.printStackTrace();
    //                        }
                    }

                }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
