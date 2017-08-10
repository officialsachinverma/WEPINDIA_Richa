package com.wepindia.pos;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.TableBooking;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.ImageAdapter;
import com.wepindia.pos.GenericClasses.MessageDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class TableBookingActivity extends WepBaseActivity {

    // Context object
    Context myContext;

    // DatabaseHandler object
    DatabaseHandler dbTableBooking = new DatabaseHandler(TableBookingActivity.this);
    // MessageDialog object
    MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
    String strTime = "", strTableNo = "";
    // View handlers
    int iMaxTables =0;
    ArrayList<Integer> arrlstTableNumbers, arrlstTableNumbers_reserved;
    EditText tvCustomerName, tvTimeBooking, tvTableNo, tvMobileNo, tvSearchMobileNo;
    TableLayout tblTableBooking;
    com.wep.common.app.views.WepButton btnAddTB, btnSaveTB;
    LinearLayout linear_table;
    String strCustomerName, strTimeBooking, strMobileNo;
    int iTableNo, iTBookId;
    private Toolbar toolbar;
    private int mHour, mMinute;

    GridView grdTable;
    TextView txtTblNo;
    String previous_time_pendingKOT="0", next_time_pendingKOT="0";
    //String strUserName;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablebooking);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //setContentView(R.layout.activity_tablebooking);

        //getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);

        /*TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrTableBooking));
        tvTitleText.setText("Table Booking");
*/
        myContext = this;
        //strUserName = getIntent().getStringExtra("USER_NAME");
        //strBillMode = getIntent().getStringExtra("BILLING_MODE");
        //jBillingMode = Byte.parseByte(strBillMode);
        //strUserId = getIntent().getStringExtra("USER_ID");
        //iCustId = getIntent().getIntExtra("CUST_ID", 0);
        MsgBox = new MessageDialog(myContext);

        tvCustomerName = (EditText) findViewById(R.id.etTBCustomerName);
        tvTimeBooking = (EditText) findViewById(R.id.etTBTimeBooking);
        tvTableNo = (EditText) findViewById(R.id.etTBTableNo);
        tvMobileNo = (EditText) findViewById(R.id.etTBMobileNo);
        tvSearchMobileNo = (EditText) findViewById(R.id.etTBSearchMobileNo);
        tblTableBooking = (TableLayout) findViewById(R.id.tblTableBooking);

        btnAddTB = (com.wep.common.app.views.WepButton) findViewById(R.id.btnTBAdd);
        btnSaveTB = (com.wep.common.app.views.WepButton) findViewById(R.id.btnTBSave);

        linear_table = (LinearLayout)findViewById(R.id.linear_table);

        arrlstTableNumbers = new ArrayList<Integer>();
        arrlstTableNumbers_reserved = new ArrayList<Integer>();

        try {
            String strUserName = getIntent().getStringExtra("USER_NAME");
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar(),"Table Booking",strUserName," Date:"+s.toString());
            /*tvTitleUserName.setText(strUserName.toUpperCase());
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            tvTitleDate.setText("Date : " + s);*/
            dbTableBooking.CreateDatabase();
            dbTableBooking.OpenDatabase();
            ResetTableBooking();
            // DisplayItems();

            DisplayTableBooking();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressWarnings("deprecation")
    public void DisplayTableBooking() {

        Cursor crsrTBooking;
        crsrTBooking = dbTableBooking.getAllTableBooking();

        TableRow rowTBooking = null;
        TextView tvSno, tviewTBookId, tviewCustomerName, tviewTimeBooking, tviewTableNo, tviewMobileNo;

        int i = 1;
        if (crsrTBooking.moveToFirst()) {
            do {
                rowTBooking = new TableRow(myContext);
                rowTBooking.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                rowTBooking.setBackgroundResource(R.drawable.row_background);

                tvSno = new TextView(myContext);
                tvSno.setTextSize(18);
                tvSno.setGravity(1);
                tvSno.setText(String.valueOf(i));
                rowTBooking.addView(tvSno);

                tviewTBookId = new TextView(myContext);
                tviewTBookId.setTextSize(18);
                tviewTBookId.setText(crsrTBooking.getString(0));
                rowTBooking.addView(tviewTBookId);

                tviewCustomerName = new TextView(myContext);
                tviewCustomerName.setTextSize(18);
                tviewCustomerName.setText(crsrTBooking.getString(1));
                rowTBooking.addView(tviewCustomerName);

                tviewTimeBooking = new TextView(myContext);
                tviewTimeBooking.setTextSize(18);
                tviewTimeBooking.setGravity(1);
                tviewTimeBooking.setText(crsrTBooking.getString(2));
                rowTBooking.addView(tviewTimeBooking);

                tviewTableNo = new TextView(myContext);
                tviewTableNo.setTextSize(18);
                tviewTableNo.setGravity(1);
                tviewTableNo.setText(crsrTBooking.getString(3));
                rowTBooking.addView(tviewTableNo);

                tviewMobileNo = new TextView(myContext);
                tviewMobileNo.setTextSize(18);
                tviewMobileNo.setText(crsrTBooking.getString(4));
                rowTBooking.addView(tviewMobileNo);


                Button ImgDelete = new Button(myContext);
                ImgDelete.setBackground(getResources().getDrawable(R.drawable.delete_icon_border));
                ImgDelete.setLayoutParams(new TableRow.LayoutParams(40, 40));
                ImgDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final View v1 = v;
                        MsgBox = new MessageDialog(myContext);
                        MsgBox.setTitle("Confirm")
                                .setMessage("Do you want to Delete this Booking")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                       try
                                       {
                                           TableRow tblrow = (TableRow) v1.getParent();
                                           TextView CustName = (TextView) tblrow.getChildAt(2);
                                           TextView Time = (TextView) tblrow.getChildAt(3);
                                           TextView TableNo = (TextView) tblrow.getChildAt(4);
                                           TextView CustPhone = (TextView) tblrow.getChildAt(5);
                                           int deleted = dbTableBooking.DeleteTableBooking_WithDetails(CustName.getText().toString(),
                                                   Time.getText().toString(), TableNo.getText().toString(), CustPhone.getText().toString());
                                           View row = (View) v1.getParent();
                                           ViewGroup container = ((ViewGroup) row.getParent());
                                           container.removeView(row);
                                           container.invalidate();
                                           int child = tblTableBooking.getChildCount();
                                           int count =0;
                                           for(int i =1;i< child ;i++)
                                           {
                                               if(i==1)
                                                   count =0;

                                               TableRow row1 = (TableRow) tblTableBooking.getChildAt(i);
                                               TextView Sn = (TextView) row1.getChildAt(0);
                                               count++;
                                               if(Sn!=null)
                                                Sn.setText(String.valueOf(count));
                                           }
                                       }catch (Exception e)
                                       {
                                           e.printStackTrace();
                                           MsgBox.Show("Error", e.getMessage());
                                       }
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });

                rowTBooking.addView(ImgDelete);
                rowTBooking.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        if (String.valueOf(v.getTag()) == "TAG") {
                            TableRow Row = (TableRow) v;
                            TextView TBookId = (TextView) Row.getChildAt(1);
                            TextView CustName = (TextView) Row.getChildAt(2);
                            TextView TimeBooking = (TextView) Row.getChildAt(3);
                            TextView TableNo = (TextView) Row.getChildAt(4);
                            TextView MobileNo = (TextView) Row.getChildAt(5);

                            iTBookId = Integer.valueOf(TBookId.getText().toString());
                            tvCustomerName.setText(CustName.getText().toString());
                            tvTimeBooking.setText(TimeBooking.getText().toString());
                            tvTableNo.setText(TableNo.getText().toString());
                            tvMobileNo.setText(MobileNo.getText().toString());
                            btnAddTB.setEnabled(false);
                            btnSaveTB.setEnabled(true);
                            //btnAddTB.setTextColor(Color.GRAY);
                            //btnSaveTB.setTextColor(Color.BLACK);
                            linear_table.setVisibility(View.VISIBLE);
                            tvMobileNo.setVisibility(View.VISIBLE);
                        }
                    }
                });

                rowTBooking.setTag("TAG");

                tblTableBooking.addView(rowTBooking,
                        new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                i++;
            } while (crsrTBooking.moveToNext());
        } else {
            Log.d("Display Table Booking", "No Table Booking found");
        }
    }

    public void TimeSelection(View view) {
        try {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {
                            String mins_str = String.valueOf(minute);
                            if(minute>0 && minute<10)
                            {
                                mins_str = "0"+minute;
                            }
                            tvTimeBooking.setText(hourOfDay + ":" + mins_str);
                            strTimeBooking = hourOfDay + ":" + mins_str;
                            linear_table.setVisibility(View.VISIBLE);
                            tvMobileNo.setVisibility(View.VISIBLE);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void TableSelection(View view) {
        try {
            if(strTimeBooking== null || strTimeBooking.equals(""))
            {
                MsgBox = new MessageDialog(myContext);
                MsgBox.Show(" Information ", " Please Select Time First");
                return;
            }
            AlertDialog.Builder TableSelectionDialog = new AlertDialog.Builder(myContext);

            LayoutInflater TableSelection = (LayoutInflater) myContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View vwTableSelection = TableSelection.inflate(R.layout.table_selection, null);

            txtTblNo = (TextView) vwTableSelection.findViewById(R.id.tvSelectedTableNo);
            grdTable = (GridView) vwTableSelection.findViewById(R.id.table_selection_grid);
            grdTable.setOnItemClickListener(GridTableClick);

            //
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            calendar.setTime(sdf.parse(strTimeBooking));
            calendar.add(Calendar.MINUTE, -30);
            Date previous_time = calendar.getTime();

            calendar.setTime(sdf.parse(strTimeBooking));
            calendar.add(Calendar.MINUTE,30);
            Date next_time = calendar.getTime();
            long millis = previous_time.getTime();

            // Create an instance of SimpleDateFormat used for formatting
            // the string representation of date (month/day/year)
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");

            // Get the date today using Calendar object.
            Date today = Calendar.getInstance().getTime();
            // Using DateFormat format method we can create a string
            // representation of a date with the defined format.
            String strTimeBookingStart = df.format(previous_time);
            String strTimeBookingEnd = df.format(next_time);



            Cursor OccupiedTables = dbTableBooking.getBookedTableBetweenTime(strTimeBookingStart, strTimeBookingEnd);

            //Cursor OccupiedTables = dbTableBooking.getOccupiedTables();
            arrlstTableNumbers_reserved.clear();
            if (OccupiedTables!= null && OccupiedTables.moveToFirst()) {
                do {
                    // Add table number to array list
                    arrlstTableNumbers_reserved.add(OccupiedTables.getInt(3));
                } while (OccupiedTables.moveToNext());
            }

            calendar.setTime(sdf.parse(strTimeBooking));
            calendar.add(Calendar.MINUTE, -30);
            previous_time = calendar.getTime();

            calendar.setTime(sdf.parse(strTimeBooking));
            calendar.add(Calendar.MINUTE,30);
            next_time = calendar.getTime();
            strTimeBookingStart = df.format(previous_time);
            strTimeBookingEnd = df.format(next_time);

            OccupiedTables = null;
            OccupiedTables = dbTableBooking.getCurrentlyUsedTableBetweenTime(strTimeBookingStart, strTimeBookingEnd);

            //Cursor OccupiedTables = dbTableBooking.getOccupiedTables();
            arrlstTableNumbers.clear();
            if (OccupiedTables!= null && OccupiedTables.moveToFirst()) {
                do {
                    // Add table number to array list
                    arrlstTableNumbers.add(OccupiedTables.getInt(OccupiedTables.getColumnIndex("TableNumber")));
                } while (OccupiedTables.moveToNext());
            }

            Cursor crsrSettings = dbTableBooking.getBillSetting();
            if (crsrSettings!= null && crsrSettings.moveToFirst()) {
                iMaxTables = crsrSettings.getInt(crsrSettings.getColumnIndex("MaximumTables"));

                InitializeTableGrid(iMaxTables);
            }




            TableSelectionDialog.setIcon(R.drawable.ic_launcher)
                    .setTitle("Select Table for Booking")
                    .setView(vwTableSelection)
                    .setNegativeButton("Cancel", null)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                            tvTableNo.setText(txtTblNo.getText().toString());

                        }
                    });
            AlertDialog alert11 = TableSelectionDialog.create();
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(alert11.getWindow().getAttributes());
            lp.width = 150;
            lp.height = 500;
            /*lp.x=-170;
            lp.y=100;*/
            alert11.getWindow().setAttributes(lp);

            alert11.show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    AdapterView.OnItemClickListener GridTableClick = new AdapterView.OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View v, int position,
                                long id) {
            // TODO Auto-generated method stub
            if (v.getTag() != null) {
                strTableNo = String.valueOf(v.getTag());
                //Cursor crsrtblSelection = dbTableBooking.getOccupiedTablesByTableNo(Integer.valueOf(strTableNo));
                //Cursor crsrtblSelection = dbTableBooking.getCurrentlyUsedTableBetweenTime(previous_time_pendingKOT, next_time_pendingKOT);
                /*boolean flag = false;
                for (int i =0;flag == false && i< iMaxTables;i++)
                {
                    if(arrlstTableNumbers.contains(i+1))
                        flag = true;
                }
                if (flag) {*/
                if(arrlstTableNumbers.contains(position+1)){
                    MsgBox.Show("Warning", "Please Select Another Table, this Table is Occupied");
                    txtTblNo.setText("");
                } else {
                    txtTblNo.setText(strTableNo);
                    InitializeTableGrid1(iMaxTables, Integer.parseInt(strTableNo));
                }
            }
        }
    };

    private void InitializeTableGrid1(int Limit, int selection) {

        String[] TableText = new String[Limit];
        String[] TableImage = new String[Limit];
        int[] TableId = new int[Limit];

        selection -=1;

        for (int i = 0; i < Limit; i++) {
            TableText[i] = "Table" + String.valueOf(i + 1);
            //TableImage[i] = String.valueOf(R.drawable.img_table);
            if (arrlstTableNumbers.size() > 0 &&
                    arrlstTableNumbers.contains(i + 1)) {
                TableImage[i] = String.valueOf(R.drawable.img_table_occupied);
            } else if (arrlstTableNumbers_reserved.size() > 0 &&
                    arrlstTableNumbers_reserved.contains(i + 1)) {
                TableImage[i] = String.valueOf(R.drawable.img_table_advance_reserved);
            } else {
                TableImage[i] = String.valueOf(R.drawable.img_table);
            }
            TableId[i] = i + 1;
        }
        TableImage[selection] = String.valueOf(R.drawable.img_table_selected);

        grdTable.setAdapter(new ImageAdapter(myContext, TableText, TableId, TableImage, Byte.parseByte("2")));
    }

    private void InitializeTableGrid(int Limit) {

        String[] TableText = new String[Limit];
        String[] TableImage = new String[Limit];
        int[] TableId = new int[Limit];

        for (int i = 0; i < Limit; i++) {
            TableText[i] = "Table" + String.valueOf(i + 1);
            //TableImage[i] = String.valueOf(R.drawable.img_table);
            if (arrlstTableNumbers.size() > 0 &&
                    arrlstTableNumbers.contains(i + 1)) {
                TableImage[i] = String.valueOf(R.drawable.img_table_occupied);
            } else if (arrlstTableNumbers_reserved.size() > 0 &&
                    arrlstTableNumbers_reserved.contains(i + 1)) {
                TableImage[i] = String.valueOf(R.drawable.img_table_advance_reserved);
            } else {
                TableImage[i] = String.valueOf(R.drawable.img_table);
            }
            TableId[i] = i + 1;
        }

        grdTable.setAdapter(new ImageAdapter(myContext, TableText, TableId, TableImage, Byte.parseByte("2")));
    }

    private void InsertTableBooking(int iTBookId, String strCustomerName, String strTimeBooking, String strMobileNo,
                                    int iTableNo) {
        long lRowId;

        TableBooking objTableBooking = new TableBooking(strCustomerName, strTimeBooking, strMobileNo, iTableNo,
                iTBookId);

        lRowId = dbTableBooking.addTableBooking(objTableBooking);

        Log.d("TableBooking", "Row Id: " + String.valueOf(lRowId));
    }

    private void ClearTableBooking() {

        for (int i = 1; i < tblTableBooking.getChildCount(); i++) {
            View Row = tblTableBooking.getChildAt(i);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
        }
    }

    private void ResetTableBooking() {
        tvCustomerName.setText("");
        tvTimeBooking.setText("");
        tvTableNo.setText("");
        tvMobileNo.setText("");
        tvSearchMobileNo.setText("");
        btnAddTB.setEnabled(true);
        btnSaveTB.setEnabled(false);
        //btnAddTB.setTextColor(Color.BLACK);
        //btnSaveTB.setTextColor(Color.GRAY);
        linear_table.setVisibility(View.INVISIBLE);
        tvMobileNo.setVisibility(View.INVISIBLE);

    }

    public void AddTableBooking(View v) {
        try {
            if (tvCustomerName.getText().toString().equalsIgnoreCase("")) {
                MsgBox.Show("Warning", "Please fill CustomerName before Booking");
            } else if (tvTimeBooking.getText().toString().equalsIgnoreCase("")) {
                MsgBox.Show("Warning", "Please Select Time for Booking");
            } else if (tvTableNo.getText().toString().equalsIgnoreCase("")) {
                MsgBox.Show("Warning", "Please Select Table for Booking");
            } else if (tvMobileNo.getText().toString().equalsIgnoreCase("")) {
                MsgBox.Show("Warning", "Please Enter Mobile No for Booking");
            } else {
                try {


                    final String strCustomerName = tvCustomerName.getText().toString();
                    final String strTimeBooking = tvTimeBooking.getText().toString();
                    final String strMobileNo = tvMobileNo.getText().toString();
                    final int iTableNo = Integer.valueOf(tvTableNo.getText().toString());
                    int iTBookId;

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
                    calendar.setTime(sdf.parse(strTimeBooking));
                    calendar.add(Calendar.MINUTE, -15);
                    Date previous_time = calendar.getTime();

                    calendar.setTime(sdf.parse(strTimeBooking));
                    calendar.add(Calendar.MINUTE, 15);
                    Date next_time = calendar.getTime();
                     long millis = previous_time.getTime();

                    // Create an instance of SimpleDateFormat used for formatting
                    // the string representation of date (month/day/year)
                    SimpleDateFormat df = new SimpleDateFormat("HH:mm");

                    // Get the date today using Calendar object.
                                        Date today = Calendar.getInstance().getTime();
                    // Using DateFormat format method we can create a string
                    // representation of a date with the defined format.
                    String strTimeBookingStart = df.format(previous_time);
                    String strTimeBookingEnd = df.format(next_time);



                    Cursor crsr = dbTableBooking.checkBookingStatus(iTableNo, strTimeBookingStart, strTimeBookingEnd);
                    if (crsr != null && crsr.moveToFirst()) {
                        String timeBookedAt= "";
                        do {
                            timeBookedAt += crsr.getString(crsr.getColumnIndex("TimeForBooking"))+ ", ";
                        }while(crsr.moveToNext());
                        //String timeBookedAt = crsr.getString(crsr.getColumnIndex("TimeForBooking"));
                        String msg = " Table " + iTableNo + " is already booked for " + timeBookedAt + " Do you still want to book it ";
                        MsgBox.setMessage(msg)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        int iTBookId = dbTableBooking.getTableBookingId();
                                        iTBookId++;
                                        Log.d("Insert TableBooking", "Table Booking Id: " + String.valueOf(iTBookId));
                                        InsertTableBooking(iTBookId, strCustomerName, strTimeBooking, strMobileNo, iTableNo);
                                        ResetTableBooking();
                                        ClearTableBooking();
                                        DisplayTableBooking();
                                    }
                                })
                                .setNegativeButton("No", null)
                                .show();
                    } else {
                        //  table not booked
                        iTBookId = dbTableBooking.getTableBookingId();
                        iTBookId++;
                        Log.d("Insert TableBooking", "Table Booking Id: " + String.valueOf(iTBookId));
                        InsertTableBooking(iTBookId, strCustomerName, strTimeBooking, strMobileNo, iTableNo);
                        ResetTableBooking();
                        ClearTableBooking();
                        DisplayTableBooking();
                    }
                }
                catch (Exception ex) {
                    Toast.makeText(myContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("Table Booking :", ex.getMessage());
                }
            }
        } catch (Exception ex) {
            Toast.makeText(myContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d("Table Booking :", ex.getMessage());
        }
    }

    public void SaveTableBooking(View v) {
        if (tvCustomerName.getText().toString().equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter CustomerName before Booking");
        } else if (tvTimeBooking.getText().toString().equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Select Time for Booking");
        } else if (tvTableNo.getText().toString().equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Select Table for Booking");
        } else if (tvMobileNo.getText().toString().equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please Enter Mobile No for Booking");
        } else {
            strCustomerName = tvCustomerName.getText().toString();
            strTimeBooking = tvTimeBooking.getText().toString();
            strMobileNo = tvMobileNo.getText().toString();
            iTableNo = Integer.parseInt(tvTableNo.getText().toString());
            Log.d("TableBooking Selection", "Code: " + iTBookId + " Name: " + strCustomerName);

            int iResult = dbTableBooking.updateTableBooking(iTBookId, strCustomerName, strTimeBooking, iTableNo,
                    strMobileNo);
            Log.d("updateDept", "Updated Rows: " + String.valueOf(iResult));
            ResetTableBooking();
            if (iResult > 0) {
                ClearTableBooking();
                DisplayTableBooking();
            } else {
                MsgBox.Show("Warning", "Update failed");
            }
        }
    }

    public void ClearTableBooking(View v) {
        ResetTableBooking();
        ClearTableBooking();
        DisplayTableBooking();
    }

    public void CloseTableBooking(View v) {

        dbTableBooking.CloseDatabase();
        this.finish();
    }

    public void SearchTableBooking(View v) {
        ClearTableBooking();
        DisplayTBookingByMobileNo();
    }

    public void DisplayTBookingByMobileNo() {
        if (tvSearchMobileNo.getText().toString().equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Please fill MobileNo for Search");
        } else {
            Cursor crsrTBooking;
            crsrTBooking = dbTableBooking.getTableBookingByMobile(tvSearchMobileNo.getText().toString());

            TableRow rowTBooking = null;
            TextView tvSno, tviewTBookId, tviewCustomerName, tviewTimeBooking, tviewTableNo, tviewMobileNo;

            int i = 1;
            if (crsrTBooking.moveToFirst()) {
                do {
                    rowTBooking = new TableRow(myContext);
                    rowTBooking.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    rowTBooking.setBackgroundResource(R.drawable.row_background);

                    tvSno = new TextView(myContext);
                    tvSno.setTextSize(18);
                    tvSno.setGravity(1);
                    tvSno.setText(String.valueOf(i));
                    rowTBooking.addView(tvSno);

                    tviewTBookId = new TextView(myContext);
                    tviewTBookId.setTextSize(18);
                    tviewTBookId.setText(crsrTBooking.getString(0));
                    rowTBooking.addView(tviewTBookId);

                    tviewCustomerName = new TextView(myContext);
                    tviewCustomerName.setTextSize(18);
                    tviewCustomerName.setText(crsrTBooking.getString(1));
                    rowTBooking.addView(tviewCustomerName);

                    tviewTimeBooking = new TextView(myContext);
                    tviewTimeBooking.setTextSize(18);
                    tviewTimeBooking.setGravity(1);
                    tviewTimeBooking.setText(crsrTBooking.getString(2));
                    rowTBooking.addView(tviewTimeBooking);

                    tviewTableNo = new TextView(myContext);
                    tviewTableNo.setTextSize(18);
                    tviewTableNo.setGravity(1);
                    tviewTableNo.setText(crsrTBooking.getString(3));
                    rowTBooking.addView(tviewTableNo);

                    tviewMobileNo = new TextView(myContext);
                    tviewMobileNo.setTextSize(18);
                    tviewMobileNo.setText(crsrTBooking.getString(4));
                    rowTBooking.addView(tviewMobileNo);

                    rowTBooking.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            if (String.valueOf(v.getTag()) == "TAG") {
                                TableRow Row = (TableRow) v;
                                TextView TBookId = (TextView) Row.getChildAt(1);
                                TextView CustName = (TextView) Row.getChildAt(2);
                                TextView TimeBooking = (TextView) Row.getChildAt(3);
                                TextView TableNo = (TextView) Row.getChildAt(4);
                                TextView MobileNo = (TextView) Row.getChildAt(5);

                                iTBookId = Integer.valueOf(TBookId.getText().toString());
                                tvCustomerName.setText(CustName.getText().toString());
                                tvTimeBooking.setText(TimeBooking.getText().toString());
                                tvTableNo.setText(TableNo.getText().toString());
                                tvMobileNo.setText(MobileNo.getText().toString());
                                btnAddTB.setEnabled(false);
                                btnSaveTB.setEnabled(true);
                                //btnAddTB.setTextColor(Color.GRAY);
                                //btnSaveTB.setTextColor(Color.BLACK);
                            }
                        }
                    });

                    rowTBooking.setTag("TAG");

                    tblTableBooking.addView(rowTBooking,
                            new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
                    i++;
                } while (crsrTBooking.moveToNext());
            } else {
                MsgBox.Show("Warning", "Table Booking not found");
                Log.d("Display Table Booking", "No Table Booking found");
            }
        }
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
        //ActionBarUtils.navigateHome(this);
        finish();
    }
}
