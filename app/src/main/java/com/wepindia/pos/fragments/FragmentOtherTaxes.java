package com.wepindia.pos.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.KOTModifier;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentOtherTaxes extends Fragment {

    Context myContext;
    DatabaseHandler dbKOTModifier;
    MessageDialog MsgBox;
    EditText txtModifierDesc, txtModifierAmount;
    CheckBox chkChargeable;
    TableLayout tblKOTModifier;
    Spinner spinnerModes;
    String strModifierId, strModifierDesc, strModifierAmount, strIsChargeable, strModifierModes;
    Button btnAddModifier, btnEditModifier,btnClearModifier,btnCloseModifier;


    public FragmentOtherTaxes() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbKOTModifier = new DatabaseHandler(getActivity());
            dbKOTModifier.OpenDatabase();
        }catch (Exception e){

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_other_taxes, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        EditTextInputHandler etInputValidate =  new EditTextInputHandler();
        txtModifierDesc = (EditText)view.findViewById(R.id.etModifierDescription);
        txtModifierAmount = (EditText)view.findViewById(R.id.etModifierAmount);
        etInputValidate.ValidateDecimalInput(txtModifierAmount);
        chkChargeable = (CheckBox)view.findViewById(R.id.chkIsChargeable);
        spinnerModes = (Spinner) view.findViewById(R.id.spnrModes);
        tblKOTModifier = (TableLayout)view.findViewById(R.id.tblKOTModifier);

        btnAddModifier = (Button)view.findViewById(R.id.btnAddModifier);
        btnAddModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddKOTModifier();
            }
        });
        btnEditModifier = (Button)view.findViewById(R.id.btnEditModifier);
        btnEditModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditKOTModifier();
            }
        });
        btnClearModifier = (Button)view.findViewById(R.id.btnClearModifier);
        btnClearModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearKOTModifier();
            }
        });
        btnCloseModifier = (Button)view.findViewById(R.id.btnCloseModifier);
        btnCloseModifier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseKOTModifier();
            }
        });
        ResetKOTModifier();
        try{
            dbKOTModifier.CloseDatabase();
            dbKOTModifier.CreateDatabase();
            dbKOTModifier.OpenDatabase();
            DisplayKOTModifier();
            loadSpinnerData();
        }
        catch(Exception exp){
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void loadSpinnerData()
    {
        List<String> listModes = new ArrayList<String>();
        listModes.add("Select");
        /*listModes.add("DineIn");
        listModes.add("CounterSales");
        listModes.add("PickUp");
        listModes.add("HomeDelivery");
*/
        Cursor crsrBillSetting = dbKOTModifier.getBillSetting();

        if(crsrBillSetting.moveToFirst()) {

            // displaying Captions as per settings
            String DineInCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeDineInCaption"));
            String CounterSalesCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeCounterSalesCaption"));
            String TakeAwayCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeTakeAwayCaption"));
            String HomeDeliveryCaption = crsrBillSetting.getString(crsrBillSetting.getColumnIndex("HomeHomeDeliveryCaption"));
            listModes.add(DineInCaption);
            listModes.add(CounterSalesCaption);
            listModes.add(TakeAwayCaption);
            listModes.add(HomeDeliveryCaption);
        }
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, listModes);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerModes.setAdapter(dataAdapter);
    }

    @SuppressWarnings("deprecation")
    private void DisplayKOTModifier(){
        Cursor crsrKOTModifier;
        crsrKOTModifier = dbKOTModifier.getAllKOTModifier();

        TableRow rowKOTModifier = null;
        TextView tvSno, tvModifierAmount, tvModifierId, tvModifierDescription, tvModifierChargeable, tvModes;
        ImageButton btnImgDelete;
        int i = 1;
        if(crsrKOTModifier.moveToFirst()){
            do{
                rowKOTModifier = new TableRow(myContext);
                rowKOTModifier.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowKOTModifier.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setText(String.valueOf(i));
                tvSno.setGravity(1);
                rowKOTModifier.addView(tvSno);

                tvModifierId = new TextView(myContext);
                tvModifierId.setTextSize(18);
                tvModifierId.setText(crsrKOTModifier.getString(0));
                rowKOTModifier.addView(tvModifierId);

                tvModifierDescription = new TextView(myContext);
                tvModifierDescription.setTextSize(18);
                tvModifierDescription.setText(crsrKOTModifier.getString(1));
                rowKOTModifier.addView(tvModifierDescription);

                tvModifierAmount = new TextView(myContext);
                tvModifierAmount.setTextSize(18);
                tvModifierAmount.setText(crsrKOTModifier.getString(2));
                tvModifierAmount.setGravity(1);
                rowKOTModifier.addView(tvModifierAmount);

                tvModifierChargeable = new TextView(myContext);
                if(crsrKOTModifier.getInt(3) == 1){
                    tvModifierChargeable.setText("Yes");
                }
                else{
                    tvModifierChargeable.setText("No");
                }
                tvModifierChargeable.setGravity(1);
                tvModifierChargeable.setTextSize(18);
                rowKOTModifier.addView(tvModifierChargeable);

                tvModes = new TextView(myContext);
                tvModes.setTextSize(18);
                tvModes.setText(crsrKOTModifier.getString(4));
                rowKOTModifier.addView(tvModes);

                TextView spc = new TextView(myContext);
                spc.setWidth(10);
                rowKOTModifier.addView(spc);
                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getActivity().getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowKOTModifier.addView(btnImgDelete);

                rowKOTModifier.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(String.valueOf(v.getTag()) == "TAG"){
                            TableRow Row = (TableRow) v;
                            TextView ModifierId = (TextView)Row.getChildAt(1);
                            TextView ModifierDescription = (TextView)Row.getChildAt(2);
                            TextView ModifierAmount = (TextView)Row.getChildAt(3);
                            TextView ModifierChargeable = (TextView)Row.getChildAt(4);
                            TextView spinner = (TextView)Row.getChildAt(5);
                            strModifierId = ModifierId.getText().toString();
                            txtModifierDesc.setText(ModifierDescription.getText());
                            txtModifierAmount.setText(ModifierAmount.getText());
                            if(ModifierChargeable.getText() == "Yes"){
                                chkChargeable.setChecked(true);
                            }
                            else{
                                chkChargeable.setChecked(false);
                            }
                            spinnerModes.setSelection(getIndex(spinnerModes, spinner.getText().toString()));
                            btnAddModifier.setClickable(false);
                            btnEditModifier.setClickable(true);
                            btnAddModifier.setTextColor(Color.GRAY);
                            btnEditModifier.setTextColor(Color.WHITE);
                        }
                    }
                });

                rowKOTModifier.setTag("TAG");

                tblKOTModifier.addView(rowKOTModifier,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                i++;
            }while(crsrKOTModifier.moveToNext());
        }
        else{
            Log.d("DisplayTax","No TaxConfig found");
        }

    }

    private int getIndex(Spinner s1, String prefNameCurGOV) {
        int index = 0;
        for (int i = 0; i < s1.getCount(); i++) {
            if (s1.getItemAtPosition(i).equals(prefNameCurGOV)) {
                index = i;
            }
        }
        return index;
    }


    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Are you sure you want to Delete this Other Charges")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView OtherChargesId = (TextView) tr.getChildAt(1);
                            long lResult = dbKOTModifier.DeleteOtherCharges(OtherChargesId.getText().toString());
                            //MsgBox.Show("", "OtherCharges Deleted Successfully");
                            Toast.makeText(myContext, "OtherCharges Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearKOTModifierTable();
                            DisplayKOTModifier();

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel,new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    private boolean IsModifierExists(String ModifierName, double Amount, String SpnrMode){
        boolean isModifierExists = false;
        String strModifier="", strModes = "";
        double OCAmt = 0;
        TextView Modifier, OCAmount, OCModes;

        for(int i=1; i<tblKOTModifier.getChildCount(); i++){

            TableRow Row = (TableRow)tblKOTModifier.getChildAt(i);

            if(Row.getChildAt(0) != null){
                Modifier = (TextView) Row.getChildAt(2);
                OCAmount = (TextView) Row.getChildAt(3);
                OCModes = (TextView) Row.getChildAt(5);

                strModifier = Modifier.getText().toString();
                strModes = OCModes.getText().toString();
                OCAmt = Double.parseDouble(OCAmount.getText().toString());

                Log.v("ModifierActivity", "Modifier:" + strModifier.toUpperCase() + " New Modifier:" + ModifierName.toUpperCase());

                if(strModifier.toUpperCase().equalsIgnoreCase(ModifierName.toUpperCase()) && OCAmt == Amount && strModes.equalsIgnoreCase(SpnrMode)){
                    isModifierExists = true;
                    break;
                }
            }
        }
        return isModifierExists;
    }

    private void InsertKOTModifier(String strModifierDescription,int iIsChargeable,int iModifierId,float fModifierAmount, String strModes){
        long lRowId;

        KOTModifier objKOTModifier = new KOTModifier(strModifierDescription,iIsChargeable,iModifierId,fModifierAmount, strModes);

        lRowId = dbKOTModifier.addKOTModifier(objKOTModifier);

        Log.d("InsertKOTmodifier","Row Id: " + String.valueOf(lRowId));
    }

    private void ClearKOTModifierTable(){

        for(int i=1;i<tblKOTModifier.getChildCount();i++){
            View Row = tblKOTModifier.getChildAt(i);
            if(Row instanceof TableRow){
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetKOTModifier(){
        txtModifierDesc.setText("");
        txtModifierAmount.setText("");
        chkChargeable.setChecked(false);
        loadSpinnerData();
        btnAddModifier.setClickable(true);
        btnEditModifier.setClickable(false);
        btnAddModifier.setTextColor(Color.WHITE);
        btnEditModifier.setTextColor(Color.GRAY);
    }

    public void AddKOTModifier(){
        int iModifierId, iIsChargeable;
        String strModifierDescription = txtModifierDesc.getText().toString();
        String strModifierAmount = txtModifierAmount.getText().toString();
        String spnrModes = spinnerModes.getSelectedItem().toString();
        if(chkChargeable.isChecked() == true){
            iIsChargeable = 1;
        }
        else{
            iIsChargeable = 0;
        }
        if(strModifierDescription.equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please Enter OtherCharges Name, amount and Select Mode before adding");
        } else if(strModifierAmount.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter OtherCharges Amount and Select Mode before adding");
        } else if(spnrModes.equalsIgnoreCase("Select")) {
            MsgBox.Show("Warning", "Please Select OtherCharges Mode before adding");
        }
        else{
            if(IsModifierExists(strModifierDescription, Double.parseDouble(strModifierAmount), spnrModes)){
                MsgBox.Show("Warning", "Other Charges is already present");
            } else {
                iModifierId = dbKOTModifier.getKOTModifierId();
                Log.d("AddModifier","Modifier Id: " + String.valueOf(iModifierId));
                iModifierId++;
                InsertKOTModifier(strModifierDescription,iIsChargeable,iModifierId,Float.parseFloat(strModifierAmount), spnrModes);
                ResetKOTModifier();
                ClearKOTModifierTable();
                DisplayKOTModifier();
            }
        }
    }

    public void EditKOTModifier(){
        strModifierDesc = txtModifierDesc.getText().toString();
        strModifierAmount = txtModifierAmount.getText().toString();
        strModifierModes = spinnerModes.getSelectedItem().toString();
        if(chkChargeable.isChecked() == true){
            strIsChargeable = "1";
        }
        else{
            strIsChargeable = "0";
        }
        Log.d("Modifier Selection","Id: " + strModifierId + " Description: " + strModifierDesc + " Chargeable: " + strIsChargeable +" Amount: " + strModifierAmount);
        if(strModifierDesc.equalsIgnoreCase("") || strModifierModes.equalsIgnoreCase("Select") ||
                strModifierAmount.equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please enter modifier name, amount and Select Mode before adding");
        }
        else {
            int iResult = dbKOTModifier.updateKOTModifier(strModifierId, strModifierDesc, strModifierAmount, strIsChargeable, strModifierModes);
            Log.d("updateCoupon", "Updated Rows: " + String.valueOf(iResult));
            ResetKOTModifier();
            if (iResult > 0) {
                ClearKOTModifierTable();
                DisplayKOTModifier();
            } else {
                MsgBox.Show("Warning", "Update failed");
            }
        }
		/*if(IsModifierExists(strModifierDesc)){
			MsgBox.Show("Warning", "Modifier is already present");
		} else {

		}*/
    }

    public void ClearKOTModifier(){
        ResetKOTModifier();
    }

    public void CloseKOTModifier(){
        dbKOTModifier.CloseDatabase();
        getActivity().finish();
    }
}
