package com.wepindia.printers;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wep.common.app.print.Payment;
import com.wep.common.app.utils.Preferences;


public class PaymentSuccessFailActivity extends WepPrinterBaseActivity {

    private boolean isPrinterAvailable = false;
    private Payment payment;

    @Override
    public void onConfigurationRequired() {

    }

    @Override
    public void onPrinterAvailable() {
        isPrinterAvailable = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success_fail);
        Intent data = getIntent();
        if (data != null) {
            payment = (Payment) data.getSerializableExtra("payment");
            //Toast.makeText(getApplicationContext(), "Not null"+payment.getBankName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "null", Toast.LENGTH_SHORT).show();
        }

        Button btnPrint = (Button) findViewById(R.id.button2);
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPrinterAvailable) {
                    Intent intent = null;
                    String prf = Preferences.getSharedPreferencesForPrint(PaymentSuccessFailActivity.this).getString("receipt", "--Select--");
                    /*if (prf.equalsIgnoreCase("Sohamsa"))
                    {
                        printSohamsaPayment(payment, "Card Payment Receipt", "PaymentPrint");
                    }
                    else */
                    if (prf.equalsIgnoreCase("Heyday"))
                    {
                        printHeydeyPayment(payment, "Card Payment Receipt", "PaymentPrint");
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Printer not configured", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    askForConfig();
                }
            }
        });
    }

    @Override
    public void onHomePressed() {

    }
}
