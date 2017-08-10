package com.wepindia.pos.fragments;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.razorpay.PaymentResultListener;
import com.wepindia.pos.R;

import com.wep.moduleactivate.LocalCheckPoint;

public class FragmentUpgradeSoftware extends Fragment implements View.OnClickListener, PaymentResultListener {

    private Button btnGST;
    private Button btnStorage;
    private LocalCheckPoint checkPoint;
    Context myContext ;
    public Activity myActivity ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        myActivity = getActivity();
        myContext = getContext();
        View view = inflater.inflate(R.layout.fragment_fragment_upgrade_software, container, false);
        btnGST = (Button) view.findViewById(R.id.btnGST);
        btnGST.setOnClickListener(this);
        btnStorage = (Button) view.findViewById(R.id.btnStorage);
        btnStorage.setOnClickListener(this);
        checkPoint = new LocalCheckPoint(getActivity());
        checkPoint.setActivate(btnGST.getTag().toString());
        return  view;
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
