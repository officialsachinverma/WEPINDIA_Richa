package com.wep.moduleactivate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.razorpay.PaymentResultListener;

public class MainActivity_upgrade extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private Button btnGST;
    private Button btnStorage;
    private LocalCheckPoint checkPoint;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnGST = (Button) findViewById(R.id.btnGST);
        btnGST.setOnClickListener(this);
        btnStorage = (Button) findViewById(R.id.btnStorage);
        btnStorage.setOnClickListener(this);
        checkPoint = new LocalCheckPoint(MainActivity_upgrade.this);
        checkPoint.setActivate(btnGST.getTag().toString());
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btnGST)
        {
            checkPoint.check(btnGST.getTag().toString());
        }
        else if(id == R.id.btnStorage)
        {
            checkPoint.check(btnStorage.getTag().toString());
        }
    }

    @Override
    public void onPaymentSuccess(String msg) {
        checkPoint.setPaymentSuccess(msg);
    }

    @Override
    public void onPaymentError(int status,String msg) {
        checkPoint.setPaymentFailed(status,msg);
    }
}
