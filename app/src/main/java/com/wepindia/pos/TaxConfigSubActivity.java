/****************************************************************************
 * Project Name		:	VAJRA
 * <p>
 * File Name		:	TaxConfigActivity
 * <p>
 * Purpose			:	Represents Tax Configuration activity, takes care of all
 * UI back end operations in this activity, such as event
 * handling data read from or display in views.
 * <p>
 * DateOfCreation	:	31-October-2012
 * <p>
 * Author			:	Balasubramanya Bharadwaj B S
 ****************************************************************************/

package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.TaxConfigSub;
import com.wepindia.pos.GenericClasses.EditTextInputHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.Date;
import java.util.List;

public class TaxConfigSubActivity extends WepBaseActivity {
    // Context object
    Context myContext;

    // DatabaseHandler object
    DatabaseHandler dbTaxConfigSub = new DatabaseHandler(TaxConfigSubActivity.this);
    // MessageDialog object
    MessageDialog MsgBox;

    // View handlers
    EditText txtSubTaxDesc, txtSubTaxPercent, txtTaxDesc1, txtTaxPercent1, txtTaxId, txtTotalPercent1;
    Button btnAddSubTax, btnEditSubTax;
    TableLayout tblSubTaxConfig;
    LinearLayout lnrSubTaxConfig;

    // Variables
    String intentStrTaxId, intentStrTaxDesc, intentStrTaxPercent;
    String strTaxId, strSubTaxId, strSubTaxDesc, strSubTaxPercent;
    Float fTaxPercent, fTotalPercent, fSubTotalPercent;
    String n = "0";
    String strUserName = "";
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tax_config_sub);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
        TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrItemMaster));
        tvTitleText.setText("Item Master");*/


        strUserName = getIntent().getStringExtra("USER_NAME");
        //tvTitleUserName.setText(strUserName.toUpperCase());
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        //tvTitleDate.setText("Date : " + s);
        com.wep.common.app.ActionBarUtils.setupToolbar(TaxConfigSubActivity.this,toolbar,getSupportActionBar(),"Sub Tax Config",strUserName," Date:"+s.toString());

        myContext = this;
        MsgBox = new MessageDialog(myContext);
        //tvTitleText.setText("Sub Tax Config");

        lnrSubTaxConfig = (LinearLayout) findViewById(R.id.lnrSubTaxConfig);
        EditTextInputHandler etInputValidate = new EditTextInputHandler();


        txtTaxDesc1 = (EditText) findViewById(R.id.etTaxDescription1);
        txtTaxPercent1 = (EditText) findViewById(R.id.etTaxPercent1);
        txtTaxId = (EditText) findViewById(R.id.etTaxId);
        txtTotalPercent1 = (EditText) findViewById(R.id.etTotalPercent1);
        txtSubTaxDesc = (EditText) findViewById(R.id.etSubTaxDescription);
        txtSubTaxPercent = (EditText) findViewById(R.id.etSubTaxPercent);
        tblSubTaxConfig = (TableLayout) findViewById(R.id.tblSubTaxConfig);
        btnAddSubTax = (Button) findViewById(R.id.btnAddSubTax);
        btnEditSubTax = (Button) findViewById(R.id.btnEditSubTax);

        ResetSubTaxConfig();

        try {
            dbTaxConfigSub.CloseDatabase();
            dbTaxConfigSub.CreateDatabase();
            dbTaxConfigSub.OpenDatabase();

            txtTaxDesc1.setText(getIntent().getStringExtra("TAX_DESC"));
            txtTaxPercent1.setText(getIntent().getStringExtra("TAX_PERCENT"));
            txtTaxId.setText(getIntent().getStringExtra("TAX_ID"));
            txtTotalPercent1.setText(getIntent().getStringExtra("TOTAL_PERCENT"));
            DisplaySubTaxConfig();
//            loadAutoCompleteData();
        } catch (Exception exp) {
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void DisplaySubTaxConfig() {
        Cursor crsrSubTaxConfig;
//        crsrSubTaxConfig = dbTaxConfig.getAllSubTaxConfig();
        crsrSubTaxConfig = dbTaxConfigSub.getAllSubTaxwithTaxName(Integer.valueOf(txtTaxId.getText().toString()));

        TableRow rowSubTaxConfig = null;
        TextView tvSno, tvSubTaxId, tvSubTaxDescription, tvSubTaxPercent, tvTaxName, tvTaxId, tvTaxPercent, tvTotalPercent;
        ImageButton btnImgDelete;
        int i = 1;
        if (crsrSubTaxConfig.moveToFirst()) {
            do {
                rowSubTaxConfig = new TableRow(myContext);
                rowSubTaxConfig.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                rowSubTaxConfig.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setText(String.valueOf(i));
                tvSno.setGravity(1);
                rowSubTaxConfig.addView(tvSno);

                tvSubTaxId = new TextView(myContext);
                tvSubTaxId.setTextSize(18);
                tvSubTaxId.setText(crsrSubTaxConfig.getString(crsrSubTaxConfig.getColumnIndex("SubTaxId")));
                rowSubTaxConfig.addView(tvSubTaxId);

                tvSubTaxDescription = new TextView(myContext);
                tvSubTaxDescription.setTextSize(18);
                tvSubTaxDescription.setText(crsrSubTaxConfig.getString(crsrSubTaxConfig.getColumnIndex("SubTaxDescription")));
                rowSubTaxConfig.addView(tvSubTaxDescription);

                tvSubTaxPercent = new TextView(myContext);
                tvSubTaxPercent.setTextSize(18);
                tvSubTaxPercent.setGravity(1);
                tvSubTaxPercent.setText(crsrSubTaxConfig.getString(crsrSubTaxConfig.getColumnIndex("SubTaxPercent")));
                rowSubTaxConfig.addView(tvSubTaxPercent);

                tvTaxName = new TextView(myContext);
                tvTaxName.setTextSize(18);
                tvTaxName.setText(crsrSubTaxConfig.getString(crsrSubTaxConfig.getColumnIndex("TaxDescription")));
                rowSubTaxConfig.addView(tvTaxName);

                // Delete
                int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                btnImgDelete = new ImageButton(myContext);
                btnImgDelete.setImageResource(res);
                btnImgDelete.setLayoutParams(new TableRow.LayoutParams(60, 40));
                btnImgDelete.setOnClickListener(mListenerSubTax);
                rowSubTaxConfig.addView(btnImgDelete);

                tvTaxId = new TextView(myContext);
                tvTaxId.setTextSize(18);
                tvTaxId.setText(crsrSubTaxConfig.getString(crsrSubTaxConfig.getColumnIndex("TaxId")));
                rowSubTaxConfig.addView(tvTaxId);

                tvTaxPercent = new TextView(myContext);
                tvTaxPercent.setTextSize(18);
                tvTaxPercent.setText(crsrSubTaxConfig.getString(crsrSubTaxConfig.getColumnIndex("TaxPercentage")));
                rowSubTaxConfig.addView(tvTaxPercent);

                tvTotalPercent = new TextView(myContext);
                tvTotalPercent.setTextSize(18);
                tvTotalPercent.setText(crsrSubTaxConfig.getString(crsrSubTaxConfig.getColumnIndex("TotalPercentage")));
                rowSubTaxConfig.addView(tvTotalPercent);

                rowSubTaxConfig.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (String.valueOf(v.getTag()) == "TAG") {
                            TableRow Row = (TableRow) v;
                            TextView SubTaxId = (TextView) Row.getChildAt(1);
                            TextView SubTaxDescription = (TextView) Row.getChildAt(2);
                            TextView SubTaxPercent = (TextView) Row.getChildAt(3);
                            TextView TaxDesc = (TextView) Row.getChildAt(4);
                            TextView TaxId = (TextView) Row.getChildAt(6);
                            TextView TaxPercent = (TextView) Row.getChildAt(7);
                            TextView TotalPercent = (TextView) Row.getChildAt(8);

                            strSubTaxId = SubTaxId.getText().toString();
                            txtSubTaxDesc.setText(SubTaxDescription.getText());
                            txtSubTaxPercent.setText(SubTaxPercent.getText());
                            txtTaxDesc1.setText(TaxDesc.getText().toString());
                            txtTaxId.setText(TaxId.getText().toString());
                            txtTaxPercent1.setText(TaxPercent.getText().toString());
                            //spinner.setSelection(Integer.parseInt(TaxPercent.getText().toString()));
                            btnAddSubTax.setClickable(false);
                            btnEditSubTax.setClickable(true);
                            btnAddSubTax.setTextColor(Color.GRAY);
                            btnEditSubTax.setTextColor(Color.WHITE);
                        }
                    }
                });

                rowSubTaxConfig.setTag("TAG");

                tblSubTaxConfig.addView(rowSubTaxConfig, new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                i++;
            } while (crsrSubTaxConfig.moveToNext());
        } else {
            Log.d("DisplaySubTax", "No SubTaxConfig found");
        }

    }

    private View.OnClickListener mListenerSubTax = new View.OnClickListener() {

        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete this SubTax")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            TableRow tr = (TableRow) v.getParent();
                            TextView SubTaxId = (TextView) tr.getChildAt(1);
                            TextView TaxId = (TextView) tr.getChildAt(6);
                            long lResult = dbTaxConfigSub.DeleteSubTax(SubTaxId.getText().toString());
                            //MsgBox.Show("", "SubTax Deleted Successfully");
                            Toast.makeText(myContext, "SubTax Deleted Successfully", Toast.LENGTH_SHORT).show();

                            // Updating SubTax into Tax
                            Cursor crsrSubTaxConfig = dbTaxConfigSub.getAllSubTaxConfig(TaxId.getText().toString());
                            fSubTotalPercent = Float.valueOf(n);
                            if (crsrSubTaxConfig.moveToFirst()) {
                                do {
                                    fSubTotalPercent = fSubTotalPercent + crsrSubTaxConfig.getFloat(2);
                                } while (crsrSubTaxConfig.moveToNext());

                                Cursor crsrTaxConfig = dbTaxConfigSub.getTaxConfig(Integer.valueOf(Integer.valueOf(TaxId.getText().toString())));
                                if (crsrTaxConfig.moveToFirst()) {

                                    fTaxPercent = crsrTaxConfig.getFloat(2);
                                    fTotalPercent = fTaxPercent + fSubTotalPercent;
                                }
                                int iResult1 = dbTaxConfigSub.updateTaxConfig(TaxId.getText().toString(), String.valueOf(fTotalPercent));
                                Log.d("updateTax", "Updated Rows: " + String.valueOf(iResult1));
                            } else {
                                Cursor crsrTaxConfig = dbTaxConfigSub.getTaxConfig(Integer.valueOf(Integer.valueOf(TaxId.getText().toString())));
                                if (crsrTaxConfig.moveToFirst()) {

                                    fTaxPercent = crsrTaxConfig.getFloat(2);
                                    fTotalPercent = fTaxPercent + fSubTotalPercent;
                                }
                                int iResult1 = dbTaxConfigSub.updateTaxConfig(TaxId.getText().toString(), String.valueOf(fTotalPercent));
                            }

                            ClearSubTaxConfigTable();
                            DisplaySubTaxConfig();

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

    private void InsertSubTaxConfig(int iSubTaxId, String strSubTaxDescription, float fSubTaxPercent, int iTaxId) {
        long lRowId;

        TaxConfigSub objSubTaxConfig = new TaxConfigSub(strSubTaxDescription, iSubTaxId, fSubTaxPercent, iTaxId);

        lRowId = dbTaxConfigSub.addSubTaxConfig(objSubTaxConfig);

        Log.d("InsertTaxConfig", "Row Id: " + String.valueOf(lRowId));
    }

    private void ClearSubTaxConfigTable() {

        for (int i = 1; i < tblSubTaxConfig.getChildCount(); i++) {
            View Row = tblSubTaxConfig.getChildAt(i);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetSubTaxConfig() {
        /*txtTaxDesc1.setText("");
        txtTaxPercent1.setText("");
        txtTaxId.setText("");
        txtTotalPercent1.setText("");*/

        /*txtTaxDesc1.setEnabled(false);
        txtTaxPercent1.setEnabled(false);
        txtTaxId.setEnabled(false);
        txtTotalPercent1.setEnabled(false);*/

        txtSubTaxDesc.setText("");
        txtSubTaxPercent.setText("");
        btnAddSubTax.setClickable(true);
        btnEditSubTax.setClickable(false);
        btnAddSubTax.setTextColor(Color.WHITE);
        btnEditSubTax.setTextColor(Color.GRAY);
    }

    public void AddSubTaxConfig(View v) {
        int iTaxId = Integer.valueOf(txtTaxId.getText().toString());
        String strSubTaxDescription = txtSubTaxDesc.getText().toString();
        String strSubTaxPercent = txtSubTaxPercent.getText().toString();
        int iSubTaxId;
        if (strSubTaxDescription.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter Sub Tax Description before adding");
        } else if (strSubTaxPercent.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter Sub Tax Percent before adding");
        } else {
            double subTaxPercent_d = Double.parseDouble(strSubTaxPercent);
            if(subTaxPercent_d <0 || subTaxPercent_d > 99.99)
            {
                MsgBox.Show("Warning", "Please enter sub tax percent between 0 and 99.99");
                return;
            }
            iSubTaxId = dbTaxConfigSub.getSubTaxId();
            Log.d("InsertTaxConfig", "Tax Id: " + String.valueOf(iSubTaxId));
            iSubTaxId++;
            InsertSubTaxConfig(iSubTaxId, strSubTaxDescription, Float.parseFloat(strSubTaxPercent), iTaxId);

            // Updating SubTax into Tax
            Cursor crsrSubTaxConfig = dbTaxConfigSub.getAllSubTaxConfig(String.valueOf(iTaxId));
            fSubTotalPercent = Float.valueOf(n);
            if (crsrSubTaxConfig.moveToFirst()) {
                do {
                    fSubTotalPercent = fSubTotalPercent + crsrSubTaxConfig.getFloat(2);
                } while (crsrSubTaxConfig.moveToNext());

                Cursor crsrTaxConfig = dbTaxConfigSub.getTaxConfig(Integer.valueOf(Integer.valueOf(txtTaxId.getText().toString())));
                if (crsrTaxConfig.moveToFirst()) {

                    fTaxPercent = crsrTaxConfig.getFloat(2);
                    fTotalPercent = fTaxPercent + fSubTotalPercent;
                }
                int iResult1 = dbTaxConfigSub.updateTaxConfig(txtTaxId.getText().toString(), String.valueOf(fTotalPercent));
                Log.d("updateTax", "Updated Rows: " + String.valueOf(iResult1));
            } else {
                Cursor crsrTaxConfig = dbTaxConfigSub.getTaxConfig(Integer.valueOf(Integer.valueOf(txtTaxId.getText().toString())));
                if (crsrTaxConfig.moveToFirst()) {

                    fTaxPercent = crsrTaxConfig.getFloat(2);
                    fTotalPercent = fTaxPercent + fSubTotalPercent;
                }
                int iResult1 = dbTaxConfigSub.updateTaxConfig(txtTaxId.getText().toString(), String.valueOf(fTotalPercent));
            }
            ResetSubTaxConfig();
            ClearSubTaxConfigTable();
            DisplaySubTaxConfig();
        }
    }

    public void EditSubTaxConfig(View v) {
        strTaxId = txtTaxId.getText().toString();
        strSubTaxDesc = txtSubTaxDesc.getText().toString();
        strSubTaxPercent = txtSubTaxPercent.getText().toString();
        double subTaxPercent_d = Double.parseDouble(strSubTaxPercent);
        if(subTaxPercent_d <0 || subTaxPercent_d > 99.99)
        {
            MsgBox.Show("Warning", "Please enter sub tax percent between 0 and 99.99");
            return;
        }
        Log.d("Tax Selection", "Sub Id: " + strSubTaxId + " Sub Description: " + strSubTaxDesc + " Percent: " + strSubTaxPercent);

        int iResult = dbTaxConfigSub.updateSubTaxConfig(strSubTaxId, strSubTaxDesc, strSubTaxPercent, strTaxId);
        Log.d("updateTax", "Updated Rows: " + String.valueOf(iResult));

        // Updating SubTax into Tax
        Cursor crsrSubTaxConfig = dbTaxConfigSub.getAllSubTaxConfig(strTaxId);
        fSubTotalPercent = Float.valueOf(n);
        if (crsrSubTaxConfig.moveToFirst()) {
            do {
                fSubTotalPercent = fSubTotalPercent + crsrSubTaxConfig.getFloat(2);
            } while (crsrSubTaxConfig.moveToNext());

            Cursor crsrTaxConfig = dbTaxConfigSub.getTaxConfig(Integer.valueOf(strTaxId));
            if (crsrTaxConfig.moveToFirst()) {

                fTaxPercent = crsrTaxConfig.getFloat(2);
                fTotalPercent = fTaxPercent + fSubTotalPercent;
            }
            int iResult1 = dbTaxConfigSub.updateTaxConfig(strTaxId, String.valueOf(fTotalPercent));
            Log.d("updateTax", "Updated Rows: " + String.valueOf(iResult1));
        }

        if (iResult > 0) {
            ResetSubTaxConfig();
            ClearSubTaxConfigTable();
            DisplaySubTaxConfig();

        } else {
            MsgBox.Show("Warning", "Update failed");
        }
        /*if(IsTaxPercentExists(Double.parseDouble(strTaxPercent))){
            MsgBox.Show("Warning", "Tax Percent already exists");
		} else {

		}*/
    }

    public void ClearSubTaxConfig(View v) {
        ResetSubTaxConfig();
    }

    public void CloseSubTaxConfig(View v) {

        dbTaxConfigSub.CloseDatabase();
        this.finish();
    }

    //    private void loadAutoCompleteData() {
//
//        List<String> labels = dbTaxConfig.getAllSpnrTaxConfig();
//
//        // Creating adapter for spinner
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, labels);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner.setAdapter(dataAdapter);
//
//
//    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
            final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
            final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
            final TextView tvAuthorizationUserId = (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserId);
            final TextView tvAuthorizationUserPassword = (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserPassword);
            tvAuthorizationUserId.setVisibility(View.GONE);
            tvAuthorizationUserPassword.setVisibility(View.GONE);
            txtUserId.setVisibility(View.GONE);
            txtPassword.setVisibility(View.GONE);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setView(vwAuthorization)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);*/
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }
    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }

}
