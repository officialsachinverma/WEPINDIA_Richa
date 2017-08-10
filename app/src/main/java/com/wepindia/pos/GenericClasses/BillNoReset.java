package com.wepindia.pos.GenericClasses;


import android.database.Cursor;

import android.util.Log;

import com.wep.common.app.Database.DatabaseHandler;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by RichaA on 3/9/2017.
 */

public class BillNoReset{

    public BillNoReset() {

    }

    public void setBillNo(DatabaseHandler dbHomeScreen )
    {
        Cursor crsrBillNoResetSetting = null;
        String date_milli_str ="", date_str="";
        crsrBillNoResetSetting = dbHomeScreen.getBillNoResetSetting();
        if (crsrBillNoResetSetting.moveToFirst()) {
            Cursor date_cursor = dbHomeScreen.getCurrentDate();
            try{
                if(date_cursor!= null && date_cursor.moveToFirst())
                {
                    date_str = date_cursor.getString(date_cursor.getColumnIndex("BusinessDate"));
                    Date dd = new SimpleDateFormat("dd-MM-yyyy").parse(date_str);
                    date_milli_str = String.valueOf(dd.getTime());
                }
                if (crsrBillNoResetSetting.getString(crsrBillNoResetSetting.getColumnIndex("Period")).equalsIgnoreCase("Enable")) {
                    int newBillNo = dbHomeScreen.getLastBillNoforDate(date_milli_str);
                    int iResult =  dbHomeScreen.UpdateBillNoResetwithDate("Enable",  date_str ,  newBillNo);
                    Log.d("BillNoReset", "status = "+iResult);
                }
                else // bill no reset disabled
                {
                    int newBillNo = dbHomeScreen.getLastBillNoforDate(date_milli_str);
                    if(newBillNo >0){
                        int iResult =  dbHomeScreen.UpdateBillNoResetwithDate("Disable",  date_str ,  newBillNo);
                        Log.d("BillNoReset", "status = "+iResult);
                    }else
                    {
                        newBillNo = dbHomeScreen.getLastBillNo();
                        int iResult =  dbHomeScreen.UpdateBillNoResetwithDate("Disable",  date_str ,  newBillNo);
                        Log.d("BillNoReset", "status = "+iResult);
                    }
                }

            }catch(Exception e)
            {
                e.printStackTrace();
            }

        } else {
            Log.d("OtherSettings", "No data in BillNoResetSettings table");
        }
    }
}
