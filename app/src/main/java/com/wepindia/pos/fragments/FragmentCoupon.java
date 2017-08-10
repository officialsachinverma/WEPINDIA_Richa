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

import com.wep.common.app.Database.Coupon;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

public class FragmentCoupon extends Fragment {

    Context myContext;
    DatabaseHandler dbCoupon ;
    MessageDialog MsgBox;
    EditText txtCouponBarcode, txtCouponDesc, txtCouponAmount;
    TableLayout tblCoupon;
    String strCouponId, strCouponBarcode, strCouponDesc, strCouponAmount;
    WepButton btnAddCoupon, btnEditCoupon,btnClearCoupon,btnCloseCoupon;


    public FragmentCoupon() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbCoupon = new DatabaseHandler(getActivity());
            dbCoupon.OpenDatabase();
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_coupon, container, false);
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);
        EditTextInputHandler etInputValidate = new EditTextInputHandler();
        txtCouponBarcode = (EditText) view.findViewById(R.id.etCouponBarcode);
        txtCouponDesc = (EditText) view.findViewById(R.id.etCouponDescription);
        txtCouponAmount = (EditText) view.findViewById(R.id.etCouponAmount);
        etInputValidate.ValidateDecimalInput(txtCouponAmount);
        tblCoupon = (TableLayout) view.findViewById(R.id.tblCouponConfig);

        btnAddCoupon = (WepButton) view.findViewById(R.id.btnAddCoupon);
        btnAddCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCoupon();
            }
        });
        btnEditCoupon = (WepButton) view.findViewById(R.id.btnEditCoupon);
        btnEditCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditCoupon();
            }
        });
        btnClearCoupon = (WepButton) view.findViewById(R.id.btnClearCoupon);
        btnClearCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClearCoupon();
            }
        });
        btnCloseCoupon = (WepButton) view.findViewById(R.id.btnCloseCoupon);
        btnCloseCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CloseCoupon();
            }
        });

        ResetCoupon();

        try {
            dbCoupon.CloseDatabase();
            dbCoupon.CreateDatabase();
            dbCoupon.OpenDatabase();
            DisplayCoupon();
        } catch (Exception exp) {
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
        return view;
    }

    private void DisplayCoupon() {
        Cursor crsrCoupon;
        crsrCoupon = dbCoupon.getAllCoupon();
        int iPosition = 0;
        TableRow rowCoupon = null;
        TextView tvSno, tvCouponAmount, tvCouponBarcode, tvCouponId, tvCouponDescription;
        ImageButton btnImgDelete;
        int i = 1;
        if (crsrCoupon.moveToFirst()) {
            do {
                rowCoupon = new TableRow(myContext);
                rowCoupon.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                rowCoupon.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setText(String.valueOf(i));
                tvSno.setGravity(1);
                rowCoupon.addView(tvSno);

                tvCouponId = new TextView(myContext);
                tvCouponId.setTextSize(18);
                iPosition = crsrCoupon.getColumnIndex("CouponId");
                tvCouponId.setText(crsrCoupon.getString(iPosition));
                rowCoupon.addView(tvCouponId);

                tvCouponDescription = new TextView(myContext);
                tvCouponDescription.setTextSize(18);
                iPosition = crsrCoupon.getColumnIndex("CouponDescription");
                tvCouponDescription.setText(crsrCoupon.getString(iPosition));
                rowCoupon.addView(tvCouponDescription);

                tvCouponAmount = new TextView(myContext);
                tvCouponAmount.setTextSize(18);
                iPosition = crsrCoupon.getColumnIndex("CouponAmount");
                tvCouponAmount.setGravity(1);
                String amount = crsrCoupon.getString(iPosition);
                tvCouponAmount.setText(crsrCoupon.getString(iPosition));
                rowCoupon.addView(tvCouponAmount);

                tvCouponBarcode = new TextView(myContext);
                iPosition = crsrCoupon.getColumnIndex("CouponBarcode");
                tvCouponBarcode.setText(crsrCoupon.getString(iPosition));
                rowCoupon.addView(tvCouponBarcode);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", getActivity().getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListener);
                rowCoupon.addView(btnImgDelete);

                rowCoupon.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (String.valueOf(v.getTag()) == "TAG") {
                            TableRow Row = (TableRow) v;
                            TextView CouponId = (TextView) Row.getChildAt(1);
                            TextView CouponDescription = (TextView) Row.getChildAt(2);
                            TextView CouponAmount = (TextView) Row.getChildAt(3);
                            TextView CouponBarcode = (TextView) Row.getChildAt(4);
                            strCouponId = CouponId.getText().toString();
                            txtCouponDesc.setText(CouponDescription.getText());
                            txtCouponAmount.setText(CouponAmount.getText());
                            txtCouponBarcode.setText(CouponBarcode.getText());
                            btnAddCoupon.setEnabled(false);
                            btnEditCoupon.setEnabled(true);
                        }
                    }
                });

                rowCoupon.setTag("TAG");

                tblCoupon.addView(rowCoupon, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                i++;
            } while (crsrCoupon.moveToNext());
        } else {
            Log.d("DisplayTax", "No TaxConfig found");
        }

    }

    private View.OnClickListener mListener = new View.OnClickListener() {

        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setIcon(R.drawable.ic_launcher)
                    .setMessage("Are you sure you want to Delete this Coupon")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView CouponId = (TextView) tr.getChildAt(1);
                            long lResult = dbCoupon.DeleteCoupon(CouponId.getText().toString());
                            //MsgBox.Show("", "Coupon Deleted Successfully");
                            Toast.makeText(myContext, "Coupon Deleted Successfully", Toast.LENGTH_SHORT).show();

                            ClearCouponTable();
                            DisplayCoupon();

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

    private boolean IsCouponExists(String CouponDescription, double CouponAmount) {
        boolean isCouponExists = false;
        double dCouponAmt = 0;
        String strCouponDesc = "";
        TextView CouponDesc, CouponAmt;

        for (int i = 1; i < tblCoupon.getChildCount(); i++) {

            TableRow Row = (TableRow) tblCoupon.getChildAt(i);

            if (Row.getChildAt(0) != null) {
                CouponDesc = (TextView) Row.getChildAt(2);
                CouponAmt = (TextView) Row.getChildAt(3);

                strCouponDesc = CouponDesc.getText().toString();
                dCouponAmt = Double.parseDouble(CouponAmt.getText().toString());

                Log.v("CouponActivity", "Tax:" + dCouponAmt + " New Tax:" + CouponAmount);

                if (CouponAmount == dCouponAmt &&
                        CouponDescription.toUpperCase().equalsIgnoreCase(strCouponDesc.toUpperCase())) {
                    isCouponExists = true;
                    break;
                }
            }
        }
        return isCouponExists;
    }

    private void InsertCoupon(int iCouponId, String strCouponDescription, String strCouponBarcode, float fCouponAmount) {
        long lRowId;

        Coupon objCoupon = new Coupon(strCouponBarcode, strCouponDescription, iCouponId, fCouponAmount);

        lRowId = dbCoupon.addCoupon(objCoupon);

        Log.d("InsertCoupon", "Row Id: " + String.valueOf(lRowId));
    }

    private void ClearCouponTable() {

        for (int i = 1; i < tblCoupon.getChildCount(); i++) {
            View Row = tblCoupon.getChildAt(i);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetCoupon() {
        txtCouponDesc.setText("");
        txtCouponAmount.setText("");
        txtCouponBarcode.setText("");
        btnAddCoupon.setEnabled(true);
        btnEditCoupon.setEnabled(false);
    }

    public void AddCoupon() {

        String strCouponDescription = txtCouponDesc.getText().toString();
        String strCouponAmount = txtCouponAmount.getText().toString();
        String strCouponBarcode = txtCouponBarcode.getText().toString();
        int iCouponId;
        if (strCouponDescription.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter Coupon Description before adding");
        } else if(strCouponAmount.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter Coupon Amount before adding");
        } else {
            if (IsCouponExists(strCouponDescription, Double.parseDouble(strCouponAmount))) {
                MsgBox.Show("Warning", "Coupon is already present");
            } else {
                iCouponId = dbCoupon.getCouponId();
                iCouponId++;
                Log.d("AddCoupon", "Id:" + String.valueOf(iCouponId) + " Description:" + strCouponDescription + " Barcode:" + strCouponBarcode + " Amount:" + strCouponAmount);
                InsertCoupon(iCouponId, strCouponDescription, strCouponBarcode, Float.parseFloat(strCouponAmount));
                ResetCoupon();
                ClearCouponTable();
                DisplayCoupon();
            }
        }
    }

    public void EditCoupon() {
        strCouponDesc = txtCouponDesc.getText().toString();
        strCouponAmount = txtCouponAmount.getText().toString();
        strCouponBarcode = txtCouponBarcode.getText().toString();
        if (strCouponDesc.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter Coupon Description before adding");
        } else if(strCouponAmount.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter Coupon Amount before adding");
        }
        else
        {
            Log.d("EditCoupon", "Id:" + strCouponId + " Description:" + strCouponDesc + " Barcode:" + strCouponBarcode + " Amount:" + strCouponAmount);
            int iResult = dbCoupon.updateCoupon(strCouponId, strCouponDesc, strCouponBarcode, strCouponAmount);
            Log.d("updateCoupon", "Updated Rows: " + String.valueOf(iResult));
            ResetCoupon();
            if (iResult > 0) {
                ClearCouponTable();
                DisplayCoupon();
            } else {
                MsgBox.Show("Warning", "Update failed");
            }
        }
        /*if(IsCouponExists(strCouponDesc,Double.parseDouble(strCouponAmount))){
            MsgBox.Show("Warning", "Coupon is already present");
		} else {

		}*/
    }

    public void ClearCoupon() {
        ResetCoupon();
    }

    public void CloseCoupon() {

        dbCoupon.CloseDatabase();
        getActivity().finish();
    }
}
