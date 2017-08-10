package com.mswipetech.wisepad.sdktest.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mswipetech.wisepad.R;
import com.wepindia.printers.WepPrinterBaseActivity;

public class TransactionActivity extends WepPrinterBaseActivity {

    //private DatabaseHandler db = null;

    public boolean isPrinterAvailable = false;

    @Override
    public void onConfigurationRequired() {

    }

    @Override
    public void onPrinterAvailable() {
        isPrinterAvailable = true;
        Toast.makeText(getApplicationContext(), "hguyg", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        //db = new DatabaseHandler(TransactionActivity.this);
    }

    public void print(){
        if(isPrinterAvailable)
        {
            printTest();
        }
        else
        {
            askForConfig();
        }
    }

    public void clickSavePrint(View view){
        print();
    }

    public void clickSaveOk(View view){

    }

    @Override
    public void onHomePressed() {

    }
}
