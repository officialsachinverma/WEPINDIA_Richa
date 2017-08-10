package com.wepindia.pos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.ItemStock;
import com.wepindia.pos.GenericClasses.MessageDialog;


/**
 * Created by RichaA on 3/13/2017.
 */

public class StockOutwardMaintain {
    
    private MessageDialog MsgBox;
    private DatabaseHandler db = null;
    private Context myContext = null;

    public StockOutwardMaintain(Context myContext, DatabaseHandler db) {
        this.myContext = myContext;
        this.db = db;
        MsgBox = new MessageDialog(myContext);
    }
    
    public void addItemToStock_Outward(String businessdate, int MenuCode,String ItemName, double openingStock, double rate )
    {
        try{
        Log.d("addItemToStk_Outward():"," checked in");
        ItemStock item = new ItemStock();
        item.setMenuCode(MenuCode);
        item.setItemName(ItemName);
        item.setOpeningStock(openingStock);
        item.setClosingStock(openingStock);
        item.setRate(rate);
        Log.d("addItemToStk_Outward():", item.getItemName() + " @ " + businessdate);
        long status = db.insertStockOutward(item, businessdate);
        if (status < 1) {
            Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
        }}catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
        
    }
    public void updateOpeningStock_Outward(String businessdate, int MenuCode,String ItemName, double openingStock, double rate )
    {
        try{
        Log.d("updateOpeningStock():"," checked in");
        ItemStock item = new ItemStock();
        item.setMenuCode(MenuCode);
        item.setItemName(ItemName);
        item.setOpeningStock(openingStock);
        item.setRate(rate);
        Log.d("updateOpeningStock():", item.getItemName() + " @ " + businessdate);
        long status = db.updateOpeningStockOutward(item, businessdate);
        if (status < 1) {
            Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
        }}catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
        
    }
    public void updateClosingStock_Outward(String businessdate, int MenuCode,String ItemName, double ClosingStock )
    {
        try{
        Log.d("updateClosingStock():"," checked in");
        ItemStock item = new ItemStock();
        item.setMenuCode(MenuCode);
        item.setItemName(ItemName);
        item.setClosingStock(ClosingStock);
        Log.d("updateClosingStock():", item.getItemName() + " @ " + businessdate+" closing quantity:"+ClosingStock);
        long status = db.updateClosingStockOutward(item, businessdate);
        if (status < 1) {
            Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
        }}catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }

    }

    public  void saveOpeningStock_Outward(String businessdate)
    {
        try{
        Log.d("SaveStock():"," checked in");
        boolean stockAlreadyPresent = false;
        Cursor stockAlreadyPresent_cursor = db.getStockOutwardForBusinessdate(businessdate);
        if(stockAlreadyPresent_cursor!=null && stockAlreadyPresent_cursor.moveToFirst()){// update only closing stock
            stockAlreadyPresent = true;
        }else
        { // new entry for businessdate
            //newBusinessDate = true; -- no need
        }
        final Cursor itemCursor = db.getAllItems();
        if(itemCursor == null || !itemCursor.moveToFirst())
        {
            // no outward item in database
            Log.d("Stockoutwardmaintain","SaveStock() : No outward item present");
        } else {
            if (!stockAlreadyPresent) {
                do {
                    // new entry
                    ItemStock item = new ItemStock();
                    item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
                    item.setItemName(itemCursor.getString(itemCursor.getColumnIndex("ItemName")));
                    item.setOpeningStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
                    item.setClosingStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
                    double rate = 0;
                    if (itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice1")) > 0) {
                        rate = itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice1"));
                    } else if (itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice2")) > 0) {
                        rate = itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice2"));
                    } else if (itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice3")) > 0) {
                        rate = itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice3"));
                    }
                    item.setRate(rate);
                    Log.d("SaveStock():", item.getItemName() + " @ " + businessdate);
                    long status = db.insertStockOutward(item, businessdate);
                    if (status < 1) {
                        Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
                    }
                } while (itemCursor.moveToNext());
//                Toast.makeText(myContext, "Opening Stock Saved for " + businessdate, Toast.LENGTH_SHORT).show();
            }else{   // stock entry present
                // update opening stock
//                do {
//                    // new entry
//                    ItemStock item = new ItemStock();
//                    item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
//                    item.setItemName(itemCursor.getString(itemCursor.getColumnIndex("ItemName")));
//                    item.setClosingStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
//                    long status = db.updateOpeningStockOutward(item, businessdate);
//                    if (status < 1) {
//                        Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
//                    }
//                } while (itemCursor.moveToNext());
                
            }
        }}catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
    }

    public  void updateClosingStock_Outward(String businessdate)
    {
        try{
        Log.d("UpdateStock():"," checked in");
        //Cursor businessDatecursor = db.getBillSetting();
        //String businessdate = "";
        boolean stockAlreadyPresent = false;
//        if(businessDatecursor!=null && businessDatecursor.moveToFirst()){
//            businessdate = businessDatecursor.getString(businessDatecursor.getColumnIndex("BusinessDate"));

            Cursor stockAlreadyPresent_cursor = db.getStockOutwardForBusinessdate(businessdate);
            if(stockAlreadyPresent_cursor!=null && stockAlreadyPresent_cursor.moveToFirst()){
                // update only closing stock
                stockAlreadyPresent = true;
            }
            final Cursor itemCursor = db.getAllItems();
            if(itemCursor == null || !itemCursor.moveToFirst())
            {
                // no outward item in database
                Log.d("Stockoutwardmaintain","SaveStock() : No outward item present");
            }
            else
            {
                final String businessDate_str = businessdate;

                if (stockAlreadyPresent) {
                    do {
                        // new entry
                        ItemStock item = new ItemStock();
                        item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
                        item.setItemName(itemCursor.getString(itemCursor.getColumnIndex("ItemName")));
                        item.setClosingStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
                        long status = db.updateClosingStockOutward(item, businessDate_str);
                        if (status < 1) {
                            Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
                        }
                    } while (itemCursor.moveToNext());
                    Toast.makeText(myContext, "Closing Stock Updated for "+businessdate, Toast.LENGTH_SHORT).show();
                }
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
    }
    private  void SaveStock1()
    {
        Cursor businessDatecursor = db.getBillSetting();
        String businessdate = "";
        boolean newBusinessDate = true;
        if(businessDatecursor!=null && businessDatecursor.moveToFirst()){
            businessdate = businessDatecursor.getString(businessDatecursor.getColumnIndex("BusinessDate"));

            Cursor stockAlreadyPresent_cursor = db.getStockOutwardForBusinessdate(businessdate);
            if(stockAlreadyPresent_cursor!=null && stockAlreadyPresent_cursor.moveToFirst()){
                // update only closing stock
                newBusinessDate = false;
            }else
            { // new entry for businessdate
                //newBusinessDate = true; -- no need
            }
            final Cursor itemCursor = db.getAllItems();
            if(itemCursor == null || !itemCursor.moveToFirst())
            {
                // no outward item in database
                Log.d("Stockoutwardmaintain","SaveStock() : No outward item present");
            }
            else
            {
                final boolean businessDate_bool = newBusinessDate;
                final String businessDate_str = businessdate;
                new AsyncTask<Void,Void,Void>() {
                    ProgressDialog pd;

                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        pd = new ProgressDialog(myContext);
                        pd.setMessage("Saving Stock...");
                        pd.setCancelable(false);
                        pd.show();
                    }

                    @Override
                    protected Void doInBackground(Void... params) {
                        try {
                            //do {
                            if (businessDate_bool) {
                                // new entry
                                ItemStock item = new ItemStock();
                                item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
                                item.setItemName(itemCursor.getString(itemCursor.getColumnIndex("ItemName")));
                                item.setOpeningStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
                                item.setClosingStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
                                double rate = 0;
                                if (itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice1")) > 0) {
                                    rate = itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice1"));
                                } else if (itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice2")) > 0) {
                                    rate = itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice2"));
                                } else if (itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice3")) > 0) {
                                    rate = itemCursor.getDouble(itemCursor.getColumnIndex("DineInPrice3"));
                                }
                                item.setRate(rate);
                                long status = db.insertStockOutward(item, businessDate_str);
                                if (status < 1) {
                                    Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
                                }
                            } else {
                                // already stock is present , only closing stock update is required
                                ItemStock item = new ItemStock();
                                item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
                                item.setItemName(itemCursor.getString(itemCursor.getColumnIndex("ItemName")));
                                item.setClosingStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
                                long status = db.updateClosingStockOutward(item, businessDate_str);
                                if (status < 1) {
                                    Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
                                }
                            }

                            // } while (itemCursor.moveToNext());
                        } catch (Exception exp) {
                            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        try {
                            //ResetItem();
                            //ClearItemTable();
                            //DisplayItemList();
                            Toast.makeText(myContext, "Stock saved successfull", Toast.LENGTH_LONG).show();
                            pd.dismiss();
                        } catch (Exception e) {
                            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                };
            }

        }else
        {
            MsgBox.Show("Error","Please save businessdate first");
        }
    }
}
