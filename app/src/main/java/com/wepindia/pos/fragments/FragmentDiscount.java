package com.wepindia.pos.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.DiscountConfig;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

public class FragmentDiscount extends Fragment {

    Context myContext;
    DatabaseHandler dbDiscConfig;
    MessageDialog MsgBox;
    EditText txtDiscountDesc, txtDiscountPercent, txtDiscountAmount;
    WepButton btnAddDiscount, btnEditDiscount,btnClearDiscount,btnCloseDiscount;
    TableLayout tblDiscountConfig;
    String strDiscId, strDiscDesc, strDiscPercent, strDiscAmount;


    public FragmentDiscount() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbDiscConfig = new DatabaseHandler(getActivity());
            dbDiscConfig.OpenDatabase();
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_discount, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        EditTextInputHandler etInputValidate =  new EditTextInputHandler();
        txtDiscountDesc = (EditText)view.findViewById(R.id.etDiscDescription);
        txtDiscountPercent = (EditText)view.findViewById(R.id.etDiscPercent);
        txtDiscountAmount = (EditText)view.findViewById(R.id.etDiscAmount);
        etInputValidate.ValidateDecimalInput(txtDiscountPercent);
        tblDiscountConfig = (TableLayout)view.findViewById(R.id.tblDiscConfig);
        btnAddDiscount = (WepButton) view.findViewById(R.id.btnAddDiscount);
        btnAddDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddDiscConfig(v);
            }
        });
        btnEditDiscount = (WepButton) view.findViewById(R.id.btnEditDiscount);
        btnEditDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDiscConfig(v);
            }
        });
        btnClearDiscount = (WepButton) view.findViewById(R.id.btnClearDiscount);
        btnClearDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearDiscConfig();
            }
        });
        btnCloseDiscount = (WepButton) view.findViewById(R.id.btnCloseDiscount);
        btnCloseDiscount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseDiscConfig();
            }
        });
        ResetDiscountConfig();
        try{
            dbDiscConfig.CloseDatabase();
            dbDiscConfig.CreateDatabase();
            dbDiscConfig.OpenDatabase();
            DisplayDiscountConfig();
        }
        catch(Exception exp){
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void DisplayDiscountConfig(){
        Cursor crsrDiscConfig;
        crsrDiscConfig = dbDiscConfig.getAllDiscountConfig();

        TableRow rowDiscountConfig = null;
        TextView tvSno, tvDiscId, tvDiscDescription, tvDiscPercent, tvDiscAmount;
        ImageButton btnImgDelete;
        int i = 1;
        if(crsrDiscConfig.moveToFirst()){
            do{
                rowDiscountConfig = new TableRow(myContext);
                rowDiscountConfig.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowDiscountConfig.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setText(String.valueOf(i));
                rowDiscountConfig.addView(tvSno);

                tvDiscId = new TextView(myContext);
                tvDiscId.setTextSize(18);
                tvDiscId.setText(crsrDiscConfig.getString(0));
                rowDiscountConfig.addView(tvDiscId);

                tvDiscDescription = new TextView(myContext);
                tvDiscDescription.setTextSize(18);
                tvDiscDescription.setText(crsrDiscConfig.getString(1));
                rowDiscountConfig.addView(tvDiscDescription);

                tvDiscPercent = new TextView(myContext);
                tvDiscPercent.setTextSize(18);
                tvDiscPercent.setGravity(1);
                tvDiscPercent.setText(crsrDiscConfig.getString(2));
                rowDiscountConfig.addView(tvDiscPercent);

                tvDiscAmount = new TextView(myContext);
                tvDiscAmount.setTextSize(18);
                tvDiscAmount.setGravity(1);
                tvDiscAmount.setText(crsrDiscConfig.getString(3));
                rowDiscountConfig.addView(tvDiscAmount);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getActivity().getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowDiscountConfig.addView(btnImgDelete);

                rowDiscountConfig.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if(String.valueOf(v.getTag()) == "TAG"){
                            TableRow Row = (TableRow) v;
                            TextView DiscId = (TextView)Row.getChildAt(1);
                            TextView DiscDescription = (TextView)Row.getChildAt(2);
                            TextView DiscPercent = (TextView)Row.getChildAt(3);
                            TextView DiscAmount = (TextView)Row.getChildAt(4);
                            strDiscId = DiscId.getText().toString();
                            txtDiscountDesc.setText(DiscDescription.getText());
                            txtDiscountPercent.setText(DiscPercent.getText());
                            txtDiscountAmount.setText(DiscAmount.getText());
                            btnAddDiscount.setEnabled(false);
                            btnEditDiscount.setEnabled(true);
                        }
                    }
                });

                rowDiscountConfig.setTag("TAG");

                tblDiscountConfig.addView(rowDiscountConfig,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                i++;
            }while(crsrDiscConfig.moveToNext());
        }
        else{
            Log.d("DisplayDiscount","No Discount Config found");
        }

    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Are you sure you want to Delete this Discount")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView DiscId = (TextView) tr.getChildAt(1);
                            long lResult = dbDiscConfig.DeleteDiscount(DiscId.getText().toString());
                            //MsgBox.Show("", "Discount Deleted Successfully");
                            Toast.makeText(myContext, "Discount Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearDiscountConfigTable();
                            DisplayDiscountConfig();

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
        }
    };

    private boolean IsDiscountPercentExists(String strDiscName, double DiscountPercent){
        boolean isDiscountExists = false;
        double dDiscount = 0;
        TextView DiscountName, Discount;

        for(int i=1; i<tblDiscountConfig.getChildCount(); i++){

            TableRow Row = (TableRow)tblDiscountConfig.getChildAt(i);

            if(Row.getChildAt(0) != null){
                DiscountName = (TextView) Row.getChildAt(2);
                Discount = (TextView) Row.getChildAt(3);

                dDiscount = Double.parseDouble(Discount.getText().toString());

                Log.v("DiscountActivity", "Discount:" + dDiscount + " New Discount:" + DiscountPercent);

                if(DiscountPercent == dDiscount && strDiscName.equalsIgnoreCase(DiscountName.getText().toString())){
                    isDiscountExists = true;
                    break;
                }
            }
        }
        return isDiscountExists;
    }

    private void InsertDiscountConfig(int iDiscId,String strDiscDescription,float fDiscPercent, float fDiscAmount){
        long lRowId;

        DiscountConfig objDiscConfig = new DiscountConfig(strDiscDescription,iDiscId,fDiscPercent,fDiscAmount);

        lRowId = dbDiscConfig.addDiscountConfig(objDiscConfig);

        Log.d("InsertTaxConfig","Row Id: " + String.valueOf(lRowId));
    }

    private void ClearDiscountConfigTable(){

        for(int i=1;i<tblDiscountConfig.getChildCount();i++){
            View Row = tblDiscountConfig.getChildAt(i);
            if(Row instanceof TableRow){
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetDiscountConfig(){
        txtDiscountDesc.setText("");
        txtDiscountPercent.setText("");
        txtDiscountAmount.setText("");
        btnAddDiscount.setEnabled(true);
        btnEditDiscount.setEnabled(false);
    }

    public void AddDiscConfig(View v){
        String strDiscDescription = txtDiscountDesc.getText().toString();
        String strDiscPercent = txtDiscountPercent.getText().toString();
        String strDiscAmount = txtDiscountAmount.getText().toString();
        int iDiscId;
        if(strDiscDescription.equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please Enter Discount description before adding");
        } else if(strDiscPercent.equalsIgnoreCase("") && strDiscAmount.equals("")) {
            MsgBox.Show("Warning", "Please enter atleast percent or amount before adding");
        }else{
            if (strDiscAmount== null || strDiscAmount.equals(""))
            {
                strDiscAmount="0";
            }
            if (strDiscPercent== null || strDiscPercent.equals(""))
            {
                strDiscPercent="0";
            }
            if(Float.parseFloat(strDiscPercent) >0 && Float.parseFloat(strDiscAmount)>0) {
                MsgBox.Show("Warning", "Please enter any one, percent or amount before adding");
                return;
            }else if (Float.parseFloat(strDiscPercent) >99.99 || Float.parseFloat(strDiscPercent) < 0){
                MsgBox.Show("Warning", "Please enter discount percent between 0 and  99.99");
                return;
            }
            if(IsDiscountPercentExists(strDiscDescription, Double.parseDouble(strDiscPercent))){
                MsgBox.Show("Warning", "Discount is already present");
            } else {
                try
                {

                    iDiscId = dbDiscConfig.getDiscountId();
                    iDiscId++;
                    Log.d("InsertTaxConfig","Tax Id: " + String.valueOf(iDiscId));
                    InsertDiscountConfig(iDiscId,strDiscDescription,Float.parseFloat(strDiscPercent),Float.parseFloat(strDiscAmount));
                    ResetDiscountConfig();
                    ClearDiscountConfigTable();
                    DisplayDiscountConfig();
                }
                catch (Exception e)
                {
                    MsgBox = new MessageDialog(myContext);
                    MsgBox.Show("Error", e.getMessage());
                    Log.d("DiscountConfigActivity:" ,"Error : "+e.getMessage());
                }
            }
        }
    }

    public void EditDiscConfig(View v){
        strDiscDesc = txtDiscountDesc.getText().toString();
        strDiscPercent = txtDiscountPercent.getText().toString();
        strDiscAmount = txtDiscountAmount.getText().toString();

        if(strDiscDesc.equalsIgnoreCase("")){
            MsgBox.Show("Warning", "Please Enter Discount description before adding");
        } else if(strDiscPercent.equalsIgnoreCase("") && strDiscAmount.equals("")) {
            MsgBox.Show("Warning", "Please enter atleast percent or amount before adding");
        }
        else
        {
            if (strDiscAmount== null || strDiscAmount.equals(""))
            {
                strDiscAmount="0";
            }
            if (strDiscPercent== null || strDiscPercent.equals(""))
            {
                strDiscPercent="0";
            }
            if(Float.parseFloat(strDiscPercent) >0 && Float.parseFloat(strDiscAmount)>0) {
                MsgBox.Show("Warning", "Please enter any one, percent or amount before adding");
                return;
            }else if (Float.parseFloat(strDiscPercent) >99.99 || Float.parseFloat(strDiscPercent) < 0){
            MsgBox.Show("Warning", "Please enter discount percent between 0 and  99.99");
            return;
            }
            Log.d("Discount Selection","Id: " + strDiscId + " Description: " + strDiscDesc + " Percent: " + strDiscPercent);
            int iResult = dbDiscConfig.updateDiscountConfig(strDiscId, strDiscDesc, strDiscPercent, strDiscAmount);
            Log.d("updateDiscount","Updated Rows: " + String.valueOf(iResult));
            ResetDiscountConfig();
            if(iResult > 0){
                ClearDiscountConfigTable();
                DisplayDiscountConfig();
            }
            else{
                MsgBox.Show("Warning", "Update failed");
            }
        }
		/*if(IsDiscountPercentExists(Double.parseDouble(strDiscPercent))){
			MsgBox.Show("Warning", "Discount precent is already present");
		} else {

		}*/
    }
    public void ClearDiscConfig(){
        ResetDiscountConfig();
    }

    public void CloseDiscConfig(){

        dbDiscConfig.CloseDatabase();
        getActivity().finish();
    }
}
