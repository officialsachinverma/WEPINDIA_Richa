package com.wepindia.pos.GST;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.R;

import java.util.Calendar;

/**
 * Created by welcome on 23-10-2016.
 */

public class GSTR2_Activity extends Activity implements DatePickerDialog.OnDateSetListener {

    Context myContext;
    // DatabaseHandler_gst object
    DatabaseHandler dbGSTR2;
    //Message dialog object
    public AlertDialog.Builder MsgBox;



    // Variables
    public static String strUserName = null;
    String month_str = "", year_str = "",date_str= "";
    TextView edit_month, edit_year;
    int year = 2016, month =3, day =1;
    String month_array[] = {"January", "February", "March", "April","May","June","July","August","September","October","November","December"};
    TextView textView_taxeeName, textView_taxeeGSTIN;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove default title bar
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gstr2);

        dbGSTR2 = new DatabaseHandler(GSTR2_Activity.this);
        myContext = this;


        // Richa to do
        edit_month = (TextView) findViewById(R.id.text_MonthValue_gstr2);
        edit_year = (TextView) findViewById(R.id.text_YearValue_gstr2);

        textView_taxeeName = (TextView) findViewById(R.id.edittext_TaxPayerName);
        textView_taxeeGSTIN = (TextView) findViewById(R.id.edittext_gstin);


        try {

            dbGSTR2.CreateDatabase();
            //dbGSTR1.OpenDatabase();

            // richa to do hardcoded date
            month_str = "October";
            year_str = "2016";
            edit_month.setText(month_str);
            edit_year.setText(year_str);

            String gstin = dbGSTR2.getGSTIN();
            if(gstin != null)
                textView_taxeeGSTIN.setText(gstin);
            String taxeename = dbGSTR2.gettaxeename();
            if(taxeename != null)
                textView_taxeeGSTIN.setText(taxeename);

            dbGSTR2.CloseDatabase();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        this.year = year;
        this.month = month;
        day = dayOfMonth;
        try {
            edit_month = (TextView) findViewById(R.id.text_MonthValue);
            edit_year = (TextView) findViewById(R.id.text_YearValue);
            month_str = month_array[month];
            year_str = String.valueOf(year);
            edit_month.setText(month_str);
            edit_year.setText(year_str);
        }
        catch(Exception e)
        {
            Toast.makeText(myContext, ""+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
    }
    // calendar button click event
    public void dateDialog(View view) {
        try {
            DatePickerFragment dialog = new DatePickerFragment();
            dialog.show(getFragmentManager(), "Date Picker");
        }
        catch (Exception e)
        {
            Toast.makeText(myContext, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public class DatePickerFragment extends DialogFragment
    {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState)
        {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), (DatePickerDialog.OnDateSetListener)getActivity(),year, month,day);
        }
    }


    public void GSTR2_tables(View view)
    {
        Intent intentHomeScreen;
        try {
            intentHomeScreen = new Intent(myContext, GSTR2_tables_Activity.class);
            intentHomeScreen.putExtra("Date", date_str);
            intentHomeScreen.putExtra("Month", month_str);
            intentHomeScreen.putExtra("Year", year_str);
            startActivity(intentHomeScreen);
        }
        catch(Exception e)
        {
            Toast.makeText(myContext, "" +e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void GSTR1_b2c(View view)
    {
        /*Intent intentHomeScreen;
        try {
            intentHomeScreen = new Intent(myContext, GSTR1_b2c_Activity.class);
            intentHomeScreen.putExtra("Date", date_str);
            intentHomeScreen.putExtra("Month", month_str);
            intentHomeScreen.putExtra("Year", year_str);
            startActivity(intentHomeScreen);
        }
        catch(Exception e)
        {
            Toast.makeText(myContext, "" +e.getMessage(), Toast.LENGTH_SHORT).show();
        }*/
    }
}
