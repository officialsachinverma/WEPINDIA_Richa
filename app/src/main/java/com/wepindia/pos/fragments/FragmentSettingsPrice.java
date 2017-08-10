package com.wepindia.pos.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.wep.common.app.Database.BillSetting;
import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

public class FragmentSettingsPrice extends Fragment {

    Context myContext;
    DatabaseHandler dbDineInSettings ;
    MessageDialog MsgBox;// = new MessageDialog(DineInSettingsActivity.this);
    EditText txtDI1From, txtDI2From, txtDI3From, txtDI1To, txtDI2To, txtDI3To, txtMaxWaiters, txtMaxTables;
    EditText txtDineIn1Caption, txtDineIn2Caption, txtDineIn3Caption;
    EditText etDineIn, etCounterSales,etPickUp,etDelivery;
    Button btnApply, btnClose;
    CheckBox chkDineIn1, chkCounterSales1, chkPickUp1, chkDelivery1;
    CheckBox chkDineIn2, chkCounterSales2, chkPickUp2, chkDelivery2;
    CheckBox chkDineIn3, chkCounterSales3, chkPickUp3, chkDelivery3;
    BillSetting objBillSettings = new BillSetting();
    private Button btnApplyDineInSettings,btnCloseDineInSettings;


    public FragmentSettingsPrice() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            dbDineInSettings = new DatabaseHandler(getActivity());
            dbDineInSettings.OpenDatabase();
        }
        catch(Exception exp){
            exp.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_settings_price, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        btnApplyDineInSettings = (Button) view.findViewById(R.id.btnApplyDineInSettings);
        btnApplyDineInSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Apply();
            }
        });
        btnCloseDineInSettings = (Button) view.findViewById(R.id.btnCloseDineInSettings);
        btnCloseDineInSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close();
            }
        });
        try {
            InitializeViews(view);
            dbDineInSettings.CloseDatabase();
            dbDineInSettings.CreateDatabase();
            dbDineInSettings.OpenDatabase();
            DisplaySettings();
        }
        catch(Exception exp){
            exp.printStackTrace();
            MsgBox.Show("Exception", exp.getMessage());
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplaySettings();
    }

    private void InitializeViews(View view){

        txtDI1From = (EditText)view.findViewById(R.id.etDineInPrice1From);
        txtDI2From = (EditText)view.findViewById(R.id.etDineInPrice2From);
        txtDI3From = (EditText)view.findViewById(R.id.etDineInPrice3From);
        txtDI1To = (EditText)view.findViewById(R.id.etDineInPrice1To);
        txtDI2To = (EditText)view.findViewById(R.id.etDineInPrice2To);
        txtDI3To = (EditText)view.findViewById(R.id.etDineInPrice3To);
        txtMaxWaiters = (EditText)view.findViewById(R.id.etDineInMaxWaiter);
        txtMaxTables = (EditText)view.findViewById(R.id.etDineInMaxTable);

        txtDineIn1Caption = (EditText)view.findViewById(R.id.etDineIn1Caption);
        txtDineIn2Caption = (EditText)view.findViewById(R.id.etDineIn2Caption);
        txtDineIn3Caption = (EditText)view.findViewById(R.id.etDineIn3Caption);

        etDineIn = (EditText)view.findViewById(R.id.etDineIn);
        etCounterSales = (EditText)view.findViewById(R.id.etCounterSales);
        etPickUp = (EditText)view.findViewById(R.id.etPickUp);
        etDelivery = (EditText)view.findViewById(R.id.etDelivery);

        chkDineIn1 = (CheckBox) view.findViewById(R.id.chkDineIn1);
        chkDineIn1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkDineIn1.isChecked()){
                    chkDineIn2.setChecked(false);
                    chkDineIn3.setChecked(false);
                }
            }
        });
        chkCounterSales1 = (CheckBox)view.findViewById(R.id.chkCounterSales1);
        chkCounterSales1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkCounterSales1.isChecked()){
                    chkCounterSales2.setChecked(false);
                    chkCounterSales3.setChecked(false);
                }
            }
        });
        chkPickUp1 = (CheckBox) view.findViewById(R.id.chkPickUp1);
        chkPickUp1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkPickUp1.isChecked()){
                    chkPickUp2.setChecked(false);
                    chkPickUp3.setChecked(false);
                }
            }
        });
        chkDelivery1 = (CheckBox) view.findViewById(R.id.chkDelivery1);
        chkDelivery1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkDelivery1.isChecked()){
                    chkDelivery2.setChecked(false);
                    chkDelivery3.setChecked(false);
                }
            }
        });

        chkDineIn2 = (CheckBox) view.findViewById(R.id.chkDineIn2);
        chkDineIn2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkDineIn2.isChecked()){
                    chkDineIn1.setChecked(false);
                    chkDineIn3.setChecked(false);
                }
            }
        });
        chkCounterSales2 = (CheckBox) view.findViewById(R.id.chkCounterSales2);
        chkCounterSales2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkCounterSales2.isChecked()){
                    chkCounterSales1.setChecked(false);
                    chkCounterSales3.setChecked(false);
                }
            }
        });
        chkPickUp2 = (CheckBox) view.findViewById(R.id.chkPickUp2);
        chkPickUp2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkPickUp2.isChecked()){
                    chkPickUp1.setChecked(false);
                    chkPickUp3.setChecked(false);
                }
            }
        });
        chkDelivery2 = (CheckBox) view.findViewById(R.id.chkDelivery2);
        chkDelivery2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkDelivery2.isChecked()){
                    chkDelivery1.setChecked(false);
                    chkDelivery3.setChecked(false);
                }
            }
        });

        chkDineIn3 = (CheckBox) view.findViewById(R.id.chkDineIn3);
        chkDineIn3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkDineIn3.isChecked()){
                    chkDineIn1.setChecked(false);
                    chkDineIn2.setChecked(false);
                }
            }
        });
        chkCounterSales3 = (CheckBox) view.findViewById(R.id.chkCounterSales3);
        chkCounterSales3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkCounterSales3.isChecked()){
                    chkCounterSales1.setChecked(false);
                    chkCounterSales2.setChecked(false);
                }
            }
        });
        chkPickUp3 = (CheckBox) view.findViewById(R.id.chkPickUp3);
        chkPickUp3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkPickUp3.isChecked()){
                    chkPickUp1.setChecked(false);
                    chkPickUp2.setChecked(false);
                }
            }
        });
        chkDelivery3 = (CheckBox) view.findViewById(R.id.chkDelivery3);
        chkDelivery3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(chkDelivery3.isChecked()){
                    chkDelivery1.setChecked(false);
                    chkDelivery2.setChecked(false);
                }
            }
        });

        btnApply = (Button)view.findViewById(R.id.btnApplyDineInSettings);
        btnClose = (Button)view.findViewById(R.id.btnCloseDineInSettings);
    }

    private void DisplaySettings(){
        Cursor crsrBillSetting = null;

        if(dbDineInSettings==null)
            dbDineInSettings = new DatabaseHandler(getActivity());

        // Read the settings from database and display it in views.
        crsrBillSetting = dbDineInSettings.getBillSetting();

        if(crsrBillSetting.moveToFirst()){

            // displaying Captions as per settings
            String DineInCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeDineInCaption"));
            String CounterSalesCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeCounterSalesCaption"));
            String TakeAwayCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeTakeAwayCaption"));
            String HomeDeliveryCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeHomeDeliveryCaption"));

            if (DineInCaption != null) {
                etDineIn.setText(DineInCaption);
            }
            if (CounterSalesCaption != null) {
                etCounterSales.setText(CounterSalesCaption);
            }
            if (TakeAwayCaption != null) {
                etPickUp.setText(TakeAwayCaption);
            }
            if (HomeDeliveryCaption != null) {
                etDelivery.setText(HomeDeliveryCaption);
            }


            // Dine In price range - From and To
            txtDI1From.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn1From")));
            txtDI1To.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn1To")));
            txtDI2From.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn2From")));
            txtDI2To.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn2To")));
            txtDI3From.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn3From")));
            txtDI3To.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn3To")));

            // Dine In Price Captions
            txtDineIn1Caption.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn1Caption")));
            txtDineIn2Caption.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn2Caption")));
            txtDineIn3Caption.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("DineIn3Caption")));

            // Maximum waiters and tables
            txtMaxTables.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("MaximumTables")));
            txtMaxWaiters.setText(crsrBillSetting.getString(crsrBillSetting.getColumnIndex("MaximumWaiters")));

            // Dine In
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("DineInRate")) == 1) {
                chkDineIn1.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("DineInRate")) == 2){
                chkDineIn2.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("DineInRate")) == 3){
                chkDineIn3.setChecked(true);
            }

            // Counter Sales
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("CounterSalesRate")) == 1) {
                chkCounterSales1.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("CounterSalesRate")) == 2){
                chkCounterSales2.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("CounterSalesRate")) == 3){
                chkCounterSales3.setChecked(true);
            }

            // Pick Up
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("PickUpRate")) == 1) {
                chkPickUp1.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("PickUpRate")) == 2){
                chkPickUp2.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("PickUpRate")) == 3){
                chkPickUp3.setChecked(true);
            }

            // Home Delivery
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("HomeDeliveryRate")) == 1) {
                chkDelivery1.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("HomeDeliveryRate")) == 2){
                chkDelivery2.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("HomeDeliveryRate")) == 3){
                chkDelivery3.setChecked(true);
            }
        }
        else{
            Log.d("DisplayDineInSettings","No data in BillSettings table");
        }
    }

    private int  ReadSettings(){

        // Local variables
        String strDI1From, strDI2From, strDI3From, strDI1To, strDI2To, strDI3To, strMaxWaiter, strMaxTables,
                strDineIn1Caption, strDineIn2Caption, strDineIn3Caption;
        String DineInCaption="", CounterSalesCaption="", TakeAwayCaption="", HomeDeliveryCaption= "";
        int iPrintKOT = 0, iKOTType = 0;
        int iDineIn = 0, iCounterSales = 0, iPickUp = 0, iHomeDelivery = 0;

        DineInCaption = etDineIn.getText().toString();
        CounterSalesCaption = etCounterSales.getText().toString();
        TakeAwayCaption = etPickUp.getText().toString();
        HomeDeliveryCaption = etDelivery.getText().toString();

        strDI1From = txtDI1From.getText().toString();
        strDI2From = txtDI2From.getText().toString();
        strDI3From = txtDI3From.getText().toString();
        strDI1To = txtDI1To.getText().toString();
        strDI2To = txtDI2To.getText().toString();
        strDI3To = txtDI3To.getText().toString();
        strMaxWaiter = txtMaxWaiters.getText().toString();
        strMaxTables = txtMaxTables.getText().toString();

        strDineIn1Caption = txtDineIn1Caption.getText().toString();
        strDineIn2Caption = txtDineIn2Caption.getText().toString();
        strDineIn3Caption = txtDineIn3Caption.getText().toString();

        // Dine In
        if(chkDineIn1.isChecked() == true)
        {
            iDineIn = 1;
        } else if(chkDineIn2.isChecked() == true)
        {
            iDineIn = 2;
        } else if(chkDineIn3.isChecked() == true)
        {
            iDineIn = 3;
        }

        // Counter Sales
        if(chkCounterSales1.isChecked() == true)
        {
            iCounterSales = 1;
        } else if(chkCounterSales2.isChecked() == true)
        {
            iCounterSales = 2;
        } else if(chkCounterSales3.isChecked() == true)
        {
            iCounterSales = 3;
        }

        // Pick Up
        if(chkPickUp1.isChecked() == true)
        {
            iPickUp = 1;
        } else if(chkPickUp2.isChecked() == true)
        {
            iPickUp = 2;
        } else if(chkPickUp3.isChecked() == true)
        {
            iPickUp = 3;
        }

        // Home Delivery
        if(chkDelivery1.isChecked() == true)
        {
            iHomeDelivery = 1;
        } else if(chkDelivery2.isChecked() == true)
        {
            iHomeDelivery = 2;
        } else if(chkDelivery3.isChecked() == true)
        {
            iHomeDelivery = 3;
        }

        if(strDI1From.equalsIgnoreCase("") || strDI2From.equalsIgnoreCase("") || strDI3From.equalsIgnoreCase("") || strDI1To.equalsIgnoreCase("") ||
                strDI2To.equalsIgnoreCase("") || strDI3To.equalsIgnoreCase("") || strMaxWaiter.equalsIgnoreCase("") || strMaxTables.equalsIgnoreCase("")){
            //Toast.makeText(myContext, "Please fill all the text boxes before saving the settings", Toast.LENGTH_LONG).show();
            MsgBox.Show("Note","Please fill all the text boxes before saving the settings");
            return 0;
        }else if (strMaxTables.equals("") || strMaxTables.equals("0"))
        {
            MsgBox.Show("Note","Max tables cannot be 0. Please configure atleast one table.");
            return 0;
        }
        else{
            // Initialize all the settings variable
            objBillSettings.setDineIn1From(Integer.parseInt(strDI1From));
            objBillSettings.setDineIn2From(Integer.parseInt(strDI2From));
            objBillSettings.setDineIn3From(Integer.parseInt(strDI3From));
            objBillSettings.setDineIn1To(Integer.parseInt(strDI1To));
            objBillSettings.setDineIn2To(Integer.parseInt(strDI2To));
            objBillSettings.setDineIn3To(Integer.parseInt(strDI3To));
            objBillSettings.setMaxWaiter(Integer.parseInt(strMaxWaiter));
            objBillSettings.setMaxTable(Integer.parseInt(strMaxTables));
            objBillSettings.setPrintKOT(1);
            objBillSettings.setKOTType(1);

            // DineIn price captions
            objBillSettings.setDineIn1Caption(strDineIn1Caption);
            objBillSettings.setDineIn2Caption(strDineIn2Caption);
            objBillSettings.setDineIn3Caption(strDineIn3Caption);

            // Rate Config
            objBillSettings.setDineInRate(iDineIn);
            objBillSettings.setCounterSalesRate(iCounterSales);
            objBillSettings.setPickUpRate(iPickUp);
            objBillSettings.setHomeDeliveryRate(iHomeDelivery);

            // captions
            objBillSettings.setDineInCaption(DineInCaption);
            objBillSettings.setCounterSalesCaption(CounterSalesCaption);
            objBillSettings.setTakeAwayCaption(TakeAwayCaption);
            objBillSettings.setHomeDeliveryCaption(HomeDeliveryCaption);
        }
        return 1;
    }

    private void SaveDineInSettings(){
        int iResult = 0;

        // Update new settings in database
        iResult = dbDineInSettings.updateDineInsettings(objBillSettings);

        if(iResult > 0){
            //Toast.makeText(myContext, "Settings saved", Toast.LENGTH_LONG).show();
            MsgBox.Show("Information", "Settings saved");
        }
        else{
            //Toast.makeText(myContext, "Apply Settings failed - Updated row:" + String.valueOf(iResult), Toast.LENGTH_LONG).show();
            MsgBox.Show("Warning", "Failed to save settings");
        }
    }

    public void Apply(){
        if(ReadSettings()>0)
            SaveDineInSettings();
    }

    public void Close(){
        dbDineInSettings.CloseDatabase();
        getActivity().finish();
    }
}
