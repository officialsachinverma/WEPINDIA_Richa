package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.Date;

public class TableStatusActivity extends WepBaseActivity implements View.OnClickListener {


        // Context object
    Context myContext;
    private static final int FILE_SELECT_CODE = 345;
    // DatabaseHandler object
    DatabaseHandler dbTableStatus = new DatabaseHandler(TableStatusActivity.this);
    // MessageDialog object
    MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
    TableLayout tblTableStatus;
    WepButton btnSearchTableStatus, btnCloseTableStatus, btnClearTableStatus;
    EditText txtTableSearchTable;
    private Toolbar toolbar;
    private TextView textViewAttachment;
    String strUserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablestatus);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //TextView textViewCenter = (TextView) toolbar.findViewById(com.wep.common.app.R.id.textViewCenter);

        /*getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);

        TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));*/
        //tvTitleText.setText("Table Status");

        myContext = this;
        MsgBox = new MessageDialog(myContext);
        strUserName = getIntent().getStringExtra("USER_NAME");
        //textViewAttachment.setOnClickListener(myContext);
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        com.wep.common.app.ActionBarUtils.setupToolbar(TableStatusActivity.this,toolbar,getSupportActionBar(),"Table Status",strUserName," Date:"+s.toString());

        tblTableStatus = (TableLayout) findViewById(R.id.tblTableStatus);
        txtTableSearchTable = (EditText) findViewById(R.id.etTableSearchTable);
        btnSearchTableStatus = (WepButton) findViewById(R.id.btnSearchTableStatus);
        btnCloseTableStatus = (WepButton) findViewById(R.id.btnCloseTableStatus);
        btnClearTableStatus = (WepButton) findViewById(R.id.btnClearTableStatus);
        btnClearTableStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear();
                for(int i=tblTableStatus.getChildCount()-1;i>0;i--)
                    tblTableStatus.removeViewAt(i);
                LoadTableStatus();
            }
        });
        try {
            dbTableStatus.CreateDatabase();
            dbTableStatus.OpenDatabase();

            LoadTableStatus();
        } catch (Exception exp) {
            Toast.makeText(myContext, "OnCreate: " + exp.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    void Clear()
    {
        txtTableSearchTable.setText("");
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        //intent.setType("*/*");      //all files
        intent.setType("text/xml");   //XML file only
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECT_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.textViewAttachment) {
            showFileChooser();
        }
    }

    @SuppressWarnings("deprecation")
    private void LoadTableStatus() {

        Cursor crsrOccupiedTable = dbTableStatus.getTableStatus();

        TableRow rowTableStatus = null;
        TextView tvSNo, tvTableNo, tvInTime, tvTimeCount, tvStatus;

        int i = 1;

        if (crsrOccupiedTable.moveToFirst()) {
            do {
                rowTableStatus = new TableRow(myContext);
                rowTableStatus.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                rowTableStatus.setBackgroundResource(R.drawable.row_background);

                tvSNo = new TextView(myContext);
                tvSNo.setTextSize(18);
                tvSNo.setGravity(1);
                tvSNo.setText(String.valueOf(i));
                tvSNo.setGravity(Gravity.CENTER);
                rowTableStatus.addView(tvSNo);

                tvTableNo = new TextView(myContext);
                tvTableNo.setTextSize(18);
                tvTableNo.setGravity(Gravity.CENTER);
                if(crsrOccupiedTable.getString(2).equalsIgnoreCase("1")) {
                    tvTableNo.setText(crsrOccupiedTable.getString(0));
                }
                else
                {
                    tvTableNo.setText(crsrOccupiedTable.getString(0) + " ( " + crsrOccupiedTable.getString(0) + " )");
                }
                rowTableStatus.addView(tvTableNo);

                tvInTime = new TextView(myContext);
                tvInTime.setTextSize(18);
                tvInTime.setGravity(Gravity.CENTER);
                tvInTime.setText(crsrOccupiedTable.getString(1));
                rowTableStatus.addView(tvInTime);

                tvTimeCount = new TextView(myContext);
                tvTimeCount.setTextSize(18);
                tvTimeCount.setText("");
                //rowTableStatus.addView(tvTimeCount);

                tvTimeCount = new TextView(myContext);
                tvTimeCount.setTextSize(18);
                tvTimeCount.setGravity(Gravity.CENTER);
                tvTimeCount.setText("Occupied");
                rowTableStatus.addView(tvTimeCount);

                rowTableStatus.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (String.valueOf(v.getTag()) == "TAG") {
                            TableRow Row = (TableRow) v;
                            TextView DeptCode = (TextView) Row.getChildAt(0);
                            TextView DeptName = (TextView) Row.getChildAt(1);
                            // strDeptCode = DeptCode.getText().toString();
                            // txtDeptName.setText(DeptName.getText());

                        }
                    }
                });

                rowTableStatus.setTag("TAG");

                tblTableStatus.addView(rowTableStatus,
                        new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                i++;
            } while (crsrOccupiedTable.moveToNext());
        }
    }

    private void ClearTableStatus() {

        for (int i = 1; i < tblTableStatus.getChildCount(); i++) {
            View Row = tblTableStatus.getChildAt(i);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    public void SearchTableStatus(View v) {

        ClearTableStatus();

        if (txtTableSearchTable.getText().toString().equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter Table No for Search");
        } else {
            Cursor crsrOccupiedTable = dbTableStatus.getTableStatusByTableNo(Integer.valueOf(txtTableSearchTable.getText().toString()));

            TableRow rowTableStatus = null;
            TextView tvSNo, tvTableNo, tvInTime, tvTimeCount, tvStatus;

            int i = 1;

            if (crsrOccupiedTable.moveToFirst()) {
                do {
                    rowTableStatus = new TableRow(myContext);
                    rowTableStatus.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    rowTableStatus.setBackgroundResource(R.drawable.row_background);

                    tvSNo = new TextView(myContext);
                    tvSNo.setTextSize(18);
                    tvSNo.setText(String.valueOf(i));
                    tvSNo.setGravity(Gravity.CENTER);
                    rowTableStatus.addView(tvSNo);

                    tvTableNo = new TextView(myContext);
                    tvTableNo.setTextSize(18);
                    tvTableNo.setText(crsrOccupiedTable.getString(0));
                    tvTableNo.setGravity(Gravity.CENTER);
                    rowTableStatus.addView(tvTableNo);

                    tvInTime = new TextView(myContext);
                    tvInTime.setTextSize(18);
                    tvInTime.setText(crsrOccupiedTable.getString(1));
                    tvInTime.setGravity(Gravity.CENTER);
                    rowTableStatus.addView(tvInTime);

//                    tvTimeCount = new TextView(myContext);
//                    tvTimeCount.setTextSize(18);
//                    tvTimeCount.setText("");
//                    rowTableStatus.addView(tvTimeCount);

                    tvStatus = new TextView(myContext);
                    tvStatus.setTextSize(18);
                    tvStatus.setText("Occupied");
                    tvStatus.setGravity(Gravity.CENTER);
                    rowTableStatus.addView(tvStatus);

//                    rowTableStatus.setOnClickListener(new View.OnClickListener() {
//
//                        public void onClick(View v) {
//                            // TODO Auto-generated method stub
//                            if (String.valueOf(v.getTag()) == "TAG") {
//                                TableRow Row = (TableRow) v;
//                                TextView DeptCode = (TextView) Row.getChildAt(0);
//                                TextView DeptName = (TextView) Row.getChildAt(1);
//                                // strDeptCode = DeptCode.getText().toString();
//                                // txtDeptName.setText(DeptName.getText());
//
//                            }
//                        }
//                    });

                    rowTableStatus.setTag("TAG");

                    tblTableStatus.addView(rowTableStatus,
                            new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    i++;
                } while (crsrOccupiedTable.moveToNext());
            }
        }
    }

    public void CloseTableStatus(View v) {
        // Close Database Connection
        dbTableStatus.CloseDatabase();

        // finish the activity
        this.finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
            final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
            final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
            final TextView tvAuthorizationUserId= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserId);
            final TextView tvAuthorizationUserPassword= (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserPassword);
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
                            /*Intent returnIntent =new Intent();
                            setResult(Activity.RESULT_OK,returnIntent);*/
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
