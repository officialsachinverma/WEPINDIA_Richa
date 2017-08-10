package com.wepindia.pos.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.TaxConfig;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;
import com.wepindia.pos.TaxConfigSubActivity;

public class FragmentTax extends Fragment {

    Context myContext;
    DatabaseHandler dbTaxConfig;
    MessageDialog MsgBox;
    EditText txtTaxDesc, txtTaxPercent;
    TableLayout tblTaxConfig;
    String strTaxId, strTaxDesc, strTaxPercent;
    Float fTaxPercent, fTotalPercent, fSubTotalPercent;
    String n = "0";
    WepButton btnAddTax,btnEditTax,btnClearTax,btnCloseTax;
    String strUserName = "";

    public FragmentTax() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbTaxConfig = new DatabaseHandler(getActivity());
            dbTaxConfig.OpenDatabase();
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_tax, container, false);
        strUserName=getArguments().getString("USER_NAME");

        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        txtTaxDesc = (EditText) view.findViewById(R.id.etTaxDescription);
        txtTaxPercent = (EditText) view.findViewById(R.id.etTaxPercent);
        etInputValidate.ValidateDecimalInput(txtTaxPercent);
        tblTaxConfig = (TableLayout) view.findViewById(R.id.tblTaxConfig);

        btnAddTax = (WepButton) view.findViewById(R.id.btnAddTax);
        btnAddTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddTaxConfig();
            }
        });
        btnEditTax = (WepButton) view.findViewById(R.id.btnEditTax);
        btnEditTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTaxConfig();
            }
        });
        btnClearTax = (WepButton) view.findViewById(R.id.btnClearTax);
        btnClearTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearTaxConfig();
            }
        });
        btnCloseTax = (WepButton) view.findViewById(R.id.btnCloseTax);
        btnCloseTax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseTaxConfig();
            }
        });

        ResetTaxConfig();

        try {
            dbTaxConfig.CloseDatabase();
            dbTaxConfig.CreateDatabase();
            dbTaxConfig.OpenDatabase();
            //DisplayTaxConfig();

//            loadSpinnerData();
        } catch (Exception exp) {
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            ClearTaxConfigTable();
            DisplayTaxConfig();
        } catch (Exception exp) {
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @SuppressWarnings("deprecation")
    private void DisplayTaxConfig() {
        Cursor crsrTaxConfig;
        crsrTaxConfig = dbTaxConfig.getAllTaxConfig();

        TableRow rowTaxConfig = null;
        TextView tvSno, tvTaxId, tvTaxDescription, tvTaxPercent, tvTotalPercent;
        ImageButton btnImgDelete;
        Button btnAddSubTax;
        int i = 1;
        if (crsrTaxConfig.moveToFirst()) {
            do {
                rowTaxConfig = new TableRow(myContext);
                rowTaxConfig.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowTaxConfig.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setText(String.valueOf(i));
                rowTaxConfig.addView(tvSno);

                tvTaxId = new TextView(myContext);
                tvTaxId.setTextSize(18);
                tvTaxId.setText(crsrTaxConfig.getString(0));
                rowTaxConfig.addView(tvTaxId);

                tvTaxDescription = new TextView(myContext);
                tvTaxDescription.setTextSize(18);
                tvTaxDescription.setText(crsrTaxConfig.getString(1));
                rowTaxConfig.addView(tvTaxDescription);

                tvTaxPercent = new TextView(myContext);
                tvTaxPercent.setTextSize(18);
                tvTaxPercent.setGravity(1);
                tvTaxPercent.setText(crsrTaxConfig.getString(2));
                rowTaxConfig.addView(tvTaxPercent);

                tvTotalPercent = new TextView(myContext);
                tvTotalPercent.setTextSize(18);
                tvTotalPercent.setGravity(1);
                tvTotalPercent.setText(crsrTaxConfig.getString(3));
                rowTaxConfig.addView(tvTotalPercent);

                // Delete
                int res1 = getResources().getIdentifier("delete", "drawable", getActivity().getPackageName());
                btnAddSubTax = new Button(myContext);
//                btnAddSubTax.setImageResource(res1);
                btnAddSubTax.setText("Add SubTax");
                btnAddSubTax.setLayoutParams(new TableRow.LayoutParams(120, 40));
                btnAddSubTax.setOnClickListener(mListenerAddSubTax);
                rowTaxConfig.addView(btnAddSubTax);

                // Gap
                tvTotalPercent = new TextView(myContext);
                tvTotalPercent.setTextSize(18);
                tvTotalPercent.setGravity(1);
                tvTotalPercent.setText("   ");
                rowTaxConfig.addView(tvTotalPercent);

                /*// Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowTaxConfig.addView(btnImgDelete);*/

                rowTaxConfig.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (String.valueOf(v.getTag()) == "TAG") {
                            TableRow Row = (TableRow) v;
                            TextView TaxId = (TextView) Row.getChildAt(1);
                            TextView TaxDescription = (TextView) Row.getChildAt(2);
                            TextView TaxPercent = (TextView) Row.getChildAt(3);
                            strTaxId = TaxId.getText().toString();
                            txtTaxDesc.setText(TaxDescription.getText());
                            txtTaxPercent.setText(TaxPercent.getText());
                            btnAddTax.setClickable(false);
                            btnEditTax.setClickable(true);
                        }
                    }
                });

                rowTaxConfig.setTag("TAG");

                tblTaxConfig.addView(rowTaxConfig, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                i++;
            } while (crsrTaxConfig.moveToNext());
        } else {
            Log.d("DisplayTax", "No TaxConfig found");
        }

    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete this Tax. It will delete corresponding Sub Tax also")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView TaxId = (TextView) tr.getChildAt(1);
                            long lResult = dbTaxConfig.DeleteTax(TaxId.getText().toString());
                            lResult = dbTaxConfig.DeleteSubTaxByTaxId(TaxId.getText().toString());
                            //MsgBox.Show("", "Tax Deleted Successfully");
                            Toast.makeText(myContext, "Tax Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearTaxConfigTable();
                            DisplayTaxConfig();

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

    private View.OnClickListener mListenerAddSubTax = new View.OnClickListener() {

        public void onClick(View v) {

            TableRow tr = (TableRow) v.getParent();
            TextView TaxId = (TextView) tr.getChildAt(1);
            TextView TaxDesc = (TextView) tr.getChildAt(2);
            TextView TaxPercent = (TextView) tr.getChildAt(3);
            TextView TotalPercent = (TextView) tr.getChildAt(4);

            Intent intentTaxConfigSub = new Intent(myContext,TaxConfigSubActivity.class);
            intentTaxConfigSub.putExtra("TAX_ID", TaxId.getText().toString());
            intentTaxConfigSub.putExtra("TAX_DESC", TaxDesc.getText().toString());
            intentTaxConfigSub.putExtra("TAX_PERCENT", TaxPercent.getText().toString());
            intentTaxConfigSub.putExtra("TOTAL_PERCENT", TotalPercent.getText().toString());
            intentTaxConfigSub.putExtra("USER_NAME", strUserName);
            startActivity(intentTaxConfigSub);

            ClearTaxConfigTable();
            DisplayTaxConfig();
        }
    };

    private boolean IsTaxPercentExists(double TaxPercent) {
        boolean isTaxExists = false;
        double dTax = 0;
        TextView Discount;

        for (int i = 1; i < tblTaxConfig.getChildCount(); i++) {

            TableRow Row = (TableRow) tblTaxConfig.getChildAt(i);

            if (Row.getChildAt(0) != null) {
                Discount = (TextView) Row.getChildAt(3);

                dTax = Double.parseDouble(Discount.getText().toString());

                Log.v("TaxActivity", "Tax:" + dTax + " New Tax:" + TaxPercent);

                if (TaxPercent == dTax) {
                    isTaxExists = true;
                    break;
                }
            }
        }
        return isTaxExists;
    }

    private void InsertTaxConfig(int iTaxId, String strTaxDescription, float fTaxPercent, float fTotalPercent) {
        long lRowId;

        TaxConfig objTaxConfig = new TaxConfig(strTaxDescription, iTaxId, fTaxPercent, fTotalPercent);

        lRowId = dbTaxConfig.addTaxConfig(objTaxConfig);

        Log.d("InsertTaxConfig", "Row Id: " + String.valueOf(lRowId));
    }

    private void ClearTaxConfigTable() {

        for (int i = 1; i < tblTaxConfig.getChildCount(); i++) {
            View Row = tblTaxConfig.getChildAt(i);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetTaxConfig() {
        txtTaxDesc.setText("");
        txtTaxPercent.setText("");
        btnAddTax.setClickable(true);
        btnEditTax.setClickable(false);
    }

    public void AddTaxConfig() {
        String strTaxDescription = txtTaxDesc.getText().toString();
        String strTaxPercent = txtTaxPercent.getText().toString();
        int iTaxId;
        if (strTaxDescription.equalsIgnoreCase("") ||
                strTaxPercent.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please enter tax description and tax percent before adding");
        } else {
            if (IsTaxPercentExists(Double.parseDouble(strTaxPercent))) {
                MsgBox.Show("Warning", "Tax Percent is already present");
            } else {

                iTaxId = dbTaxConfig.getTaxId();
                Log.d("InsertTaxConfig", "Tax Id: " + String.valueOf(iTaxId));
                iTaxId++;
                InsertTaxConfig(iTaxId, strTaxDescription, Float.parseFloat(strTaxPercent), Float.parseFloat(strTaxPercent));
                ResetTaxConfig();
                ClearTaxConfigTable();
                DisplayTaxConfig();
            }
        }
    }

    public void EditTaxConfig() {
        strTaxDesc = txtTaxDesc.getText().toString();
        strTaxPercent = txtTaxPercent.getText().toString();
        Log.d("Tax Selection", "Id: " + strTaxId + " Description: " + strTaxDesc + " Percent: " + strTaxPercent);

        int iResult = dbTaxConfig.updateTaxConfig(strTaxId, strTaxDesc, strTaxPercent, strTaxPercent);
        Log.d("updateTax", "Updated Rows: " + String.valueOf(iResult));

        // Updating SubTax into Tax
        Cursor crsrSubTaxConfig = dbTaxConfig.getAllSubTaxConfig(strTaxId);
        fSubTotalPercent = Float.valueOf(n);
        if (crsrSubTaxConfig.moveToFirst()) {
            do {
                fSubTotalPercent = fSubTotalPercent + crsrSubTaxConfig.getFloat(2);
            } while (crsrSubTaxConfig.moveToNext());

            Cursor crsrTaxConfig = dbTaxConfig.getTaxConfig(Integer.valueOf(strTaxId));
            if (crsrTaxConfig.moveToFirst()) {

                fTaxPercent = crsrTaxConfig.getFloat(2);
                fTotalPercent = fTaxPercent + fSubTotalPercent;
            }
            int iResult1 = dbTaxConfig.updateTaxConfig(strTaxId, String.valueOf(fTotalPercent));
            Log.d("updateTax", "Updated Rows: " + String.valueOf(iResult1));
        }

        ResetTaxConfig();
        if (iResult > 0) {
            ClearTaxConfigTable();
            DisplayTaxConfig();
        } else {
            MsgBox.Show("Warning", "Update failed");
        }
    }

    public void ClearTaxConfig()
    {
        ResetTaxConfig();
    }

    public void CloseTaxConfig()
    {
        dbTaxConfig.CloseDatabase();
        getActivity().finish();
    }
}
