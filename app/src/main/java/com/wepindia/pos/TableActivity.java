/****************************************************************************
 * Project Name		:	VAJRA
 * <p/>
 * File Name		:	TableActivity
 * <p/>
 * Purpose			:	Represents Table screen activity, takes care of all
 * UI back end operations for Shift and Merge Table
 * in this activity, and also event handling.
 * <p/>
 * DateOfCreation	:	15-December-2012
 * <p/>
 * Author			:	Balasubramanya Bharadwaj B S
 ****************************************************************************/

package com.wepindia.pos;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mswipetech.wisepad.sdktest.view.ApplicationData;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.ImageAdapter;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.WaiterAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TableActivity extends WepBaseActivity {

    Context myContext;
    DatabaseHandler dbTableWaiter;
    MessageDialog MsgBox;
    GridView grdTable, grdWaiter;
    TextView tvSelectedTable, tvSelectedWaiter;
    TextView tv_tablesplit1,tv_tablesplit2,tv_tablesplit3,tv_tablesplit4,tvTableSplitHeading;
    String DISABLE = "0";
    String ENABLE = "1";
    // Variables
    public static final String TABLE_NO = "table_number";
    public static final String WAITER_NO = "waiter_number";
    public static final String SUBUDF_NO = "subudf_number";
    public static final String TABLE_SPLIT_NO = "table_split_number";
    ArrayList<Integer> arrlstTableNumbers,arrlstTableNumbers_reserved;
    Cursor crsrSettings = null;
    List<Map<String, String>> lstWaiters;
    String strTableNumber = "_", strWaiterNumber = "", strTableSplitNo = "";
    int iMaxTables = 0, iMaxWaiters = 0, TableSplitEnable=0;
    CheckBox ckSplit1, ckSplit2, ckSplit3, ckSplit4;
    String strBillMode = "", strUserId = "", strUserName = "";
    int iCustId = 0;
    String[] tableoccupied =new String[4];
    int  SelectTableSplit = -1;
    LinearLayout lnrTableSplit;
    FrameLayout frame_split;
    private Toolbar toolbar;
    TextView tvTableSelectVLine, tvVerticalLine;
    private WepButton btn_DineInTableBooking,btn_TableCancel,btn_TableDineIn,btn_TableOK,btn_DineInTableStatus;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        /*TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
        TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrTableActivity));
        tvTitleText.setText("Table - Waiter");*/

        myContext = this;
        strUserId = ApplicationData.getUserId(this);//ApplicationData.USER_ID;
        strUserName = ApplicationData.getUserName(this);//ApplicationData.USER_NAME;
        //strUserName = getIntent().getStringExtra("USER_NAME");
        strBillMode = getIntent().getStringExtra("BILLING_MODE");
        //jBillingMode = Byte.parseByte(strBillMode);
        //strUserId = getIntent().getStringExtra("USER_ID");
        iCustId = getIntent().getIntExtra("CUST_ID", 0);

        //tvTitleUserName.setText(strUserName.toUpperCase());
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        //tvTitleDate.setText("Date : " + s);

        try {
            com.wep.common.app.ActionBarUtils.setupToolbar(TableActivity.this,toolbar,getSupportActionBar(),"Table/Waiter Selection",strUserName," Date:"+s.toString());
            lnrTableSplit = (LinearLayout) findViewById(R.id.lnrTableSplit);
            frame_split = (FrameLayout) findViewById(R.id.frame_split);
            grdTable = (GridView) findViewById(R.id.grid_Image_Table);
            grdTable.setOnItemClickListener(GridTableClick);
            grdWaiter = (GridView) findViewById(R.id.grid_Image_Waiter);
            grdWaiter.setOnItemClickListener(GridWaiterClick);

            tvSelectedTable = (TextView) findViewById(R.id.tvTableSelected);
            tvSelectedWaiter = (TextView) findViewById(R.id.tvWaiterSelected);

            tvTableSplitHeading = (TextView) findViewById(R.id.tvTableSplitHeading);
            tv_tablesplit1 = (TextView) findViewById(R.id.tv_tablesplit1);
            tv_tablesplit2 = (TextView) findViewById(R.id.tv_tablesplit2);
            tv_tablesplit3 = (TextView) findViewById(R.id.tv_tablesplit3);
            tv_tablesplit4 = (TextView) findViewById(R.id.tv_tablesplit4);

            ckSplit1 = (CheckBox) findViewById(R.id.chkSplit1);
            ckSplit2 = (CheckBox) findViewById(R.id.chkSplit2);
            ckSplit3 = (CheckBox) findViewById(R.id.chkSplit3);
            ckSplit4 = (CheckBox) findViewById(R.id.chkSplit4);

            tvTableSelectVLine = (TextView) findViewById(R.id.tvTableSelectVLine);
            tvVerticalLine = (TextView)findViewById(R.id.tvVerticalLine);

            btn_DineInTableBooking = (WepButton)findViewById(R.id.btn_DineInTableBooking);
            btn_TableCancel = (WepButton)findViewById(R.id.btn_TableCancel);
            btn_TableDineIn = (WepButton)findViewById(R.id.btn_TableDineIn);
            btn_TableOK = (WepButton)findViewById(R.id.btn_TableOK);
            btn_DineInTableStatus = (WepButton)findViewById(R.id.btn_DineInTableStatus);

            btn_DineInTableBooking.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DineInTableBooking(v);
                }
            });
            btn_TableCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Cancel(v);
                }
            });
            btn_TableDineIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DineIn(v);
                }
            });
            btn_TableOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OK(v);
                }
            });
            btn_DineInTableStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DineInTableStatus(v);
                }
            });



            MsgBox = new MessageDialog(myContext);

            getDb().CreateDatabase();
            getDb().OpenDatabase();

            lstWaiters = new ArrayList<Map<String, String>>();
            arrlstTableNumbers = new ArrayList<Integer>();
            arrlstTableNumbers_reserved = new ArrayList<Integer>();

            crsrSettings = getDb().getBillSetting();

            Date date = new Date();
            Calendar calendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
            //calendar.setTime(sdf.parse(String.valueOf(date)));
            calendar.setTime((date));
            calendar.add(Calendar.MINUTE, -15);
            Date previous_time = calendar.getTime();

            calendar.setTime(date);
            calendar.add(Calendar.MINUTE,15);
            Date next_time = calendar.getTime();
            //long millis = previous_time.getTime();

            // Create an instance of SimpleDateFormat used for formatting
            // the string representation of date (month/day/year)
            SimpleDateFormat df = new SimpleDateFormat("HH:mm");

            // Get the date today using Calendar object.
            //Date today = Calendar.getInstance().getTime();
            // Using DateFormat format method we can create a string
            // representation of a date with the defined format.
            String strTimeBookingStart = df.format(previous_time);
            String strTimeBookingEnd = df.format(next_time);



            Cursor OccupiedTables = getDb().getBookedTableBetweenTime(strTimeBookingStart, strTimeBookingEnd);

            //Cursor OccupiedTables = dbTableBooking.getOccupiedTables();
            arrlstTableNumbers_reserved.clear();
            if (OccupiedTables!= null && OccupiedTables.moveToFirst()) {
                do {
                    // Add table number to array list
                    arrlstTableNumbers_reserved.add(OccupiedTables.getInt(3));
                } while (OccupiedTables.moveToNext());
            }
            OccupiedTables = null;
            OccupiedTables = getDb().getOccupiedTables();
            if (OccupiedTables.moveToFirst()) {
                do {
                    // Add table number to array list
                    arrlstTableNumbers.add(OccupiedTables.getInt(0));
                } while (OccupiedTables.moveToNext());
            }

            Cursor crsrWaiters = getDb().getAllWaiters();
            iMaxWaiters = crsrWaiters.getCount();
            Log.v("TableActivity", "Waiters:" + iMaxWaiters);
            if (crsrWaiters.moveToFirst()) {

                do {
                    Map<String, String> mapWaiters = new HashMap<String, String>(2);
                    mapWaiters.put("Id", crsrWaiters.getString(crsrWaiters.getColumnIndex("UserId")));
                    mapWaiters.put("Name", crsrWaiters.getString(crsrWaiters
                            .getColumnIndex("Name")));

                    lstWaiters.add(mapWaiters);

                } while (crsrWaiters.moveToNext());
            }

            if (crsrSettings.moveToFirst()) {
                iMaxTables = crsrSettings.getInt(crsrSettings.getColumnIndex("MaximumTables"));

                InitializeTableGrid(iMaxTables);
                InitializeWaiterGrid(iMaxWaiters);
                TableSplitEnable = crsrSettings.getInt(crsrSettings.getColumnIndex("TableSpliting"));
                if( TableSplitEnable == 1)
                {
                    lnrTableSplit.setVisibility(View.VISIBLE);
                } else {
                    lnrTableSplit.setVisibility(View.GONE);
                    frame_split.setVisibility(View.GONE);
                    //tvTableSelectVLine.setVisibility(View.GONE);
                    //tvVerticalLine.setVisibility(View.GONE);
                }
                int count = setTableSplit(DISABLE,strTableNumber);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public DatabaseHandler getDb(){
        if(dbTableWaiter==null){
            dbTableWaiter = new DatabaseHandler(this);
            try{
                dbTableWaiter.OpenDatabase();
            }catch (Exception e){

            }
        }
        return dbTableWaiter;
    }

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

    int setTableSplit(String EnableorDisable, String strTableNumber)
    {
        int count =0;
        if (EnableorDisable.equals(DISABLE))
        {
            tvTableSplitHeading.setTextColor(Color.GRAY);
            tv_tablesplit1.setTextColor(Color.GRAY);
            ckSplit1.setEnabled(false);
            ckSplit1.setTextColor(Color.GRAY);
            tv_tablesplit2.setTextColor(Color.GRAY);
            ckSplit2.setEnabled(false);
            ckSplit2.setTextColor(Color.GRAY);
            tv_tablesplit3.setTextColor(Color.GRAY);
            ckSplit3.setEnabled(false);
            ckSplit3.setTextColor(Color.GRAY);
            tv_tablesplit4.setTextColor(Color.GRAY);
            ckSplit4.setEnabled(false);
            ckSplit4.setTextColor(Color.GRAY);

        }
        else {
            tvTableSplitHeading.setTextColor(Color.BLACK);
            tv_tablesplit1.setTextColor(Color.BLACK);
            ckSplit1.setEnabled(true);
            ckSplit1.setTextColor(Color.BLACK);
            tv_tablesplit2.setTextColor(Color.BLACK);
            ckSplit2.setEnabled(true);
            ckSplit2.setTextColor(Color.BLACK);
            tv_tablesplit3.setTextColor(Color.BLACK);
            ckSplit3.setEnabled(true);
            ckSplit3.setTextColor(Color.BLACK);
            tv_tablesplit4.setTextColor(Color.BLACK);
            ckSplit4.setEnabled(true);
            ckSplit4.setTextColor(Color.BLACK);


            ckSplit1.setChecked(false);
            ckSplit2.setChecked(false);
            ckSplit3.setChecked(false);
            ckSplit4.setChecked(false);

            tv_tablesplit1.setText("Table  " + strTableNumber + " A : ");
            tv_tablesplit2.setText("Table  " + strTableNumber + " B : ");
            tv_tablesplit3.setText("Table  " + strTableNumber + " C : ");
            tv_tablesplit4.setText("Table  " + strTableNumber + " D : ");

            for (int i = 0; i < 4; i++)
                tableoccupied[i] = "0";

            if (!strTableNumber.equals("_")) {
                Cursor OccupiedTableSplitNo = getDb().getOccupiedTableSplitNo(Integer.valueOf(strTableNumber));
                count = 0;
                SelectTableSplit = -1;
                if (OccupiedTableSplitNo.moveToFirst()) {
                    do {
                        count++;
                        if (OccupiedTableSplitNo.getString(0).equals("1")) {
                            ckSplit1.setChecked(true);
                            ckSplit1.setEnabled(false);
                            tableoccupied[0] = "1";
                        } else if (OccupiedTableSplitNo.getString(0).equals("2")) {
                            ckSplit2.setChecked(true);
                            ckSplit2.setEnabled(false);
                            tableoccupied[1] = "1";

                        } else if (OccupiedTableSplitNo.getString(0).equals("3")) {
                            ckSplit3.setChecked(true);
                            ckSplit3.setEnabled(false);
                            tableoccupied[2] = "1";
                        } else if (OccupiedTableSplitNo.getString(0).equals("4")) {
                            ckSplit4.setChecked(true);
                            ckSplit4.setEnabled(false);
                            tableoccupied[3] = "1";
                        }
                    } while (OccupiedTableSplitNo.moveToNext());
                }

                if (count == 4)
                    SelectTableSplit = -1;
                int i = 0;
                while (count != 4 && i < 4) {
                    if (tableoccupied[i].equals("0")) {
                        if (i == 0) {
                            ckSplit1.setChecked(true);
                            SelectTableSplit = 1;
                        } else if (i == 1) {
                            ckSplit2.setChecked(true);
                            SelectTableSplit = 2;
                        } else if (i == 2) {
                            ckSplit3.setChecked(true);
                            SelectTableSplit = 3;
                        } else if (i == 3) {
                            ckSplit4.setChecked(true);
                            SelectTableSplit = 4;
                        }
                        break;
                    }
                    i++;
                }

            }
        }
        return count;
    }

    OnItemClickListener GridTableClick = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View v, int position,
                                long id) {
            // TODO Auto-generated method stub
            if (v.getTag() != null) {
                strTableNumber = String.valueOf(v.getTag());


                Cursor crsrTableBooking = getDb().getTableBookingByTableNo(strTableNumber);
                if (crsrTableBooking.moveToFirst()) {
                    String CustName = crsrTableBooking.getString(crsrTableBooking.getColumnIndex("CustName"));
                    String Time = crsrTableBooking.getString(crsrTableBooking.getColumnIndex("TimeForBooking"));
                    MsgBox.Show("", "This table is booked by the Customer - " + CustName + " at - " + Time);
                }

                if(TableSplitEnable == 0)
                {
                    if (arrlstTableNumbers.contains(Integer.parseInt(strTableNumber)))
                    {
                        MsgBox.Show("", "This table is already in use. Please select another table");
                        return;
                    }
                }
                tvSelectedTable.setText("Selected Table #: " + strTableNumber);
                InitializeTableGrid1(iMaxTables, Integer.parseInt(strTableNumber));
                int count = setTableSplit(ENABLE, strTableNumber);

                if (count == 4) {
                    Toast.makeText(myContext, " This table cannot be split further for booking", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
            if (TableSplitEnable == 0 && !(strTableNumber.equalsIgnoreCase("_") || strWaiterNumber.equalsIgnoreCase(""))) {
                OK(v);
            }
        }

    };

    OnItemClickListener GridWaiterClick = new OnItemClickListener() {

        public void onItemClick(AdapterView<?> adapter, View v, int position,
                                long id) {
            // TODO Auto-generated method stub
            if (v.getTag() != null) {
                strWaiterNumber = String.valueOf(v.getTag());
                //tvSelectedWaiter.setText("Selected Waiter #: " + strWaiterNumber);
                InitializeWaiterGrid1(strWaiterNumber);
                if (!strTableNumber.equalsIgnoreCase("-") && SelectTableSplit != -1) {
                    // finish the activity
                    OK(v);
                }
            }
        }

    };

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
        String startTime="0", endTime = "0";
        Date date = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE,-30);
        startTime = String.valueOf(cal.getTimeInMillis());
        cal.setTime(date);
        cal.add(Calendar.MINUTE,+30);
        endTime = String.valueOf(cal.getTimeInMillis());
        Cursor crsrReservedTable = getDb().getBookedTableBetweenTime(startTime, endTime);
        while(crsrReservedTable!=null && crsrReservedTable.moveToNext())
        {
            int tableId = crsrReservedTable.getInt(crsrReservedTable.getColumnIndex("TableId"));
            TableImage[tableId] = String.valueOf(R.drawable.img_table_advance_reserved);
        }

        grdTable.setAdapter(new ImageAdapter(myContext, TableText, TableId, TableImage, Byte.parseByte("2")));
    }

    public  void CheckboxEvent (View v)
    {
        CheckBox checkBox = (CheckBox)v;
        if(!checkBox.isChecked()){
            // checkbox is unchecked
            SelectTableSplit = -1;
            return;
        }
        else
        {
            // checkbox is checked
            int id = v.getId();
            if(ckSplit1.isEnabled() && ckSplit1.isChecked() )
            {
                if(id == R.id.chkSplit1)
                    SelectTableSplit = 1;
                else
                    ckSplit1.setChecked(false);
            }
            if(ckSplit2.isEnabled() && ckSplit2.isChecked() )
            {
                if(id == R.id.chkSplit2)
                    SelectTableSplit = 2;
                else
                    ckSplit2.setChecked(false);
            }
            if(ckSplit3.isEnabled() && ckSplit3.isChecked() )
            {
                if(id == R.id.chkSplit3)
                    SelectTableSplit = 3;
                else
                    ckSplit3.setChecked(false);
            }
            if(ckSplit4.isEnabled() && ckSplit4.isChecked() )
            {
                if(id == R.id.chkSplit4)
                    SelectTableSplit = 4;
                else
                    ckSplit4.setChecked(false);
            }
            if(!strTableNumber.equals("-") && SelectTableSplit!= -1 && !strWaiterNumber.equals(""))
            OK(v);
        }
    }

    private void InitializeWaiterGrid(int Limit) {

        String[] WaiterText = new String[Limit];
        String[] WaiterImage = new String[Limit];
        int[] WaiterId = new int[Limit];

        if (Limit > 0) {
            Map<String, String> mapWaiters = new HashMap<String, String>(2);
            for (int i = 0; i < Limit; i++) {
                mapWaiters = lstWaiters.get(i);
                WaiterText[i] = mapWaiters.get("Name");
                WaiterImage[i] = String.valueOf(R.drawable.img_waiter_idle);
                WaiterId[i] = Integer.parseInt(mapWaiters.get("Id"));
            }

        }
        grdWaiter.setAdapter(new WaiterAdapter(myContext, WaiterText, WaiterId, WaiterImage, Byte.parseByte("2")));
    }

    private void InitializeWaiterGrid1(String selectedWaiter ) {

        String[] WaiterText = new String[iMaxWaiters];
        String[] WaiterImage = new String[iMaxWaiters];
        int[] WaiterId = new int[iMaxWaiters];

        if (iMaxWaiters > 0) {
            Map<String, String> mapWaiters = new HashMap<String, String>(2);
            for (int i = 0; i < iMaxWaiters; i++) {
                mapWaiters = lstWaiters.get(i);
                WaiterText[i] = mapWaiters.get("Name");
                String id = mapWaiters.get("Id");

                if(id.equalsIgnoreCase(selectedWaiter)) {
                    WaiterImage[i] = String.valueOf(R.drawable.img_waiter_selected);
                    tvSelectedWaiter.setText("Selected Waiter #: " + WaiterText[i]);
                }
                else
                    WaiterImage[i] = String.valueOf(R.drawable.img_waiter_idle);
                WaiterId[i] = Integer.parseInt(mapWaiters.get("Id"));
            }

        }
        grdWaiter.setAdapter(new WaiterAdapter(myContext, WaiterText, WaiterId, WaiterImage, Byte.parseByte("2")));
    }
    public void OK(View v) {
        if (SelectTableSplit == -1 || strTableNumber.equalsIgnoreCase("_") || strWaiterNumber.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Select Table Number, Waiter Number");
        } else {


            // Close Database Connection
            getDb().CloseDatabase();

            /*if (ckSplit1.isChecked()) {
               // strTableSplitNo = ckSplit2.getText().toString();
                strTableSplitNo = "1";
            }
            else if (ckSplit2.isChecked()) {
                // strTableSplitNo = ckSplit2.getText().toString();
                strTableSplitNo = "2";
            }
            else  if (ckSplit3.isChecked()) {
                strTableSplitNo = "3";
            }
            else if (ckSplit4.isChecked()) {
                strTableSplitNo = "4";
            }*/
            // set Result
            Intent intentResult = new Intent(myContext, BillingDineInActivity.class);

            intentResult.putExtra(TABLE_NO, strTableNumber);
            intentResult.putExtra(WAITER_NO, strWaiterNumber);
            intentResult.putExtra(TABLE_SPLIT_NO, String.valueOf(SelectTableSplit));
            intentResult.putExtra(SUBUDF_NO, "1");

            intentResult.putExtra("BILLING_MODE", strBillMode);
            intentResult.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
            intentResult.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
            intentResult.putExtra("CUST_ID", 0);
            startActivity(intentResult);
            //setResult(RESULT_OK, intentResult);

            // finish the activity
            this.finish();
        }
    }

    public void Cancel(View v) {
        // Close Database Connection
        getDb().CloseDatabase();
        /*Intent intentResult = new Intent();
        intentResult.putExtra("isCancelled",true);
        setResult(RESULT_CANCELED, intentResult);*/
        // finish the activity
        this.finish();
    }

    public void DineIn(View v) {
        // set Result
        Intent intentResult = new Intent(myContext, BillingDineInActivity.class);

        intentResult.putExtra(TABLE_NO, "0");
        intentResult.putExtra(WAITER_NO, "0");
        intentResult.putExtra(TABLE_SPLIT_NO, "1");
        intentResult.putExtra(SUBUDF_NO, "0");

        intentResult.putExtra("BILLING_MODE", strBillMode);
        intentResult.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
        intentResult.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
        intentResult.putExtra("CUST_ID", 0);
        startActivity(intentResult);
        //setResult(RESULT_OK, intentResult);

        this.finish();
    }

    public void DineInTableBooking(View v) {

        Intent intentTableBooking = new Intent(myContext, TableBookingActivity.class);
        intentTableBooking.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
        intentTableBooking.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
        intentTableBooking.putExtra("CUST_ID", 0);
        startActivity(intentTableBooking);

       /* Intent intentTableBooking = new Intent(myContext,TableBookingActivity.class);
        startActivity(intentTableBooking);*/
    }

    public void DineInTableStatus(View v) {
        Intent intentTableStatus = new Intent(myContext,TableStatusActivity.class);
        intentTableStatus.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
        intentTableStatus.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
        intentTableStatus.putExtra("CUST_ID", 0);
        startActivity(intentTableStatus);
    }

    @Override
    public void onBackPressed() {
        getDb().CloseDatabase();
        /*Intent intentResult = new Intent();
        intentResult.putExtra("isCancelled",true);
        setResult(RESULT_CANCELED, intentResult);*/
        // finish the activity
        this.finish();
    }

    @Override
    public void onHomePressed() {
        //ActionBarUtils.navigateHome(this);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            dbTableWaiter.CloseDatabase();
        }catch (Exception e){

        }
    }
}
