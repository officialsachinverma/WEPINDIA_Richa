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
import android.widget.EditText;

import com.wep.common.app.Database.BillSetting;
import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

public class FragmentSettingsHeaderFooter extends Fragment {

    Context myContext;
    DatabaseHandler dbHeaderFooterSettings ;
    MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
    EditText addressLine1, addressLine2,addressLine3,addressLineFooter;
    BillSetting objBillSettings = new BillSetting();
    private String TAG = FragmentSettingsHeaderFooter.class.getSimpleName();
    private Button btnApplyHeaderFooter,btnCloseHeaderFooter;


    public FragmentSettingsHeaderFooter() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHeaderFooterSettings = new DatabaseHandler(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_header_footer_text, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        addressLine1 = (EditText)view.findViewById(R.id.addressLine1);
        /*addressLine2 = (EditText)view.findViewById(R.id.addressLine2);
        addressLine3 = (EditText)view.findViewById(R.id.addressLine3);*/
        addressLineFooter = (EditText)view.findViewById(R.id.addressLineFooter);
        btnApplyHeaderFooter = (Button) view.findViewById(R.id.btnApplyHeaderFooter);
        btnApplyHeaderFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Apply();
            }
        });
        btnCloseHeaderFooter = (Button) view.findViewById(R.id.btnCloseHeaderFooter);
        btnCloseHeaderFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close();
            }
        });
        try{
            dbHeaderFooterSettings.CloseDatabase();
            dbHeaderFooterSettings.CreateDatabase();
            dbHeaderFooterSettings.OpenDatabase();
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

    private void DisplaySettings(){
        String[] tokens = new String[3];
        tokens[0] = "";
        tokens[1] = "";
        tokens[2] = "";
        Cursor crsrHeaderFooterSetting = null;
        crsrHeaderFooterSetting = dbHeaderFooterSettings.getBillSetting();
        if(crsrHeaderFooterSetting.moveToFirst()){
            /*try{
                tokens = crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText")).split(Pattern.quote("|"));
            }catch (Exception e){
                tokens[0] = "";
                tokens[1] = "";
                tokens[2] = "";
            }
            if(!tokens[0].equalsIgnoreCase(""))
                addressLine1.setText(tokens[0]);
            if(!tokens[1].equalsIgnoreCase(""))
                addressLine2.setText(tokens[1]);
            if(!tokens[2].equalsIgnoreCase(""))
                addressLine3.setText(tokens[2]);*/
            addressLine1.setText(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("HeaderText")));
            addressLineFooter.setText(crsrHeaderFooterSetting.getString(crsrHeaderFooterSetting.getColumnIndex("FooterText")));
        }
        else{
            Log.d(TAG,"DisplayHeaderFooterSettings No data in BillSettings table");
        }
    }

    public void Apply(){
        if(false)/*addressLine1.getText().toString().trim().equalsIgnoreCase("") || addressLine2.getText().toString().trim().equalsIgnoreCase(""))*/
        {
            if(addressLine1.getText().toString().trim().equalsIgnoreCase(""))
            {
                MsgBox.Show("Information", "Address line1 is mandatory");
            }
            else if(addressLine2.getText().toString().trim().equalsIgnoreCase(""))
            {
                MsgBox.Show("Information", "Address line2 is mandatory");
            }
        }
        else
        {
            String str1 = addressLine1.getText().toString().trim();//+" |"+addressLine2.getText().toString().trim()+" |"+" "+addressLine3.getText().toString().trim();
            String str2 = addressLineFooter.getText().toString().trim();
            int iResult = 0;
            // Update new settings in database
            iResult = dbHeaderFooterSettings.updateHeaderFooterText(str1,str2);
            if(iResult > 0){
                MsgBox.Show("Information", "Saved Successfully");
            }
            else{
                MsgBox.Show("Exception", "Save Failed");
            }
        }
    }

    public void Close(){
        dbHeaderFooterSettings.CloseDatabase();
        getActivity().finish();
    }

}
