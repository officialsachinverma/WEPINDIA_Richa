package com.wepindia.printers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.wepindia.printers.heydey.PrinterFragment;
import com.wepindia.printers.sohamsa.PrinterSohamsaActivity;

public class MainActivity extends WepPrinterBaseActivity {

    public static final int PRINTING_REQUEST_CODE_SOHAMSA = 200;
    public static final int PRINTING_REQUEST_CODE_HEYDAY = 201;
    public boolean isPrinterAvailable = false;

    @Override
    public void onConfigurationRequired() {
        Toast.makeText(MainActivity.this, "Not Configured", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrinterAvailable() {
        isPrinterAvailable = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
    }

    protected void testPrintSohamsa(String name,int num) {
        Intent intent = new Intent(getApplicationContext(), PrinterSohamsaActivity.class);
        intent.putExtra("printType", "TEST");
        intent.putExtra("code", PRINTING_REQUEST_CODE_SOHAMSA);
        intent.putExtra("name", name);
        intent.putExtra("printerNum", num);
        startActivityForResult(intent,PRINTING_REQUEST_CODE_SOHAMSA);
    }
    protected void testPrintHeyDay(String name,int num) {
        Intent intent = new Intent(getApplicationContext(), PrinterFragment.class);
        intent.putExtra("printType", "TEST");
        intent.putExtra("code", PRINTING_REQUEST_CODE_HEYDAY);
        intent.putExtra("name", name);
        intent.putExtra("printerNum", num);
        startActivityForResult(intent,PRINTING_REQUEST_CODE_HEYDAY);
        //Toast.makeText(this, "wefh3yir", Toast.LENGTH_SHORT).show();
    }

    public void testPrintSohamsa(View view) {
        //testPrintSohamsa("priyabrat",1);
        printTest();
    }

    public void testPrintHeyDay(View view) {
        if(isPrinterAvailable)
        {
            setResultt();
        }
        else
        {
            askForConfig();
        }
    }

    @Override
    public void onHomePressed() {

    }
}
