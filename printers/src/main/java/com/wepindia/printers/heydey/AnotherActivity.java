package com.wepindia.printers.heydey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.wepindia.printers.R;

public class AnotherActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another);
    }

    public void openFragment(View view) {
        startActivity(new Intent(getApplicationContext(),PrinterFragment.class));
    }
}
