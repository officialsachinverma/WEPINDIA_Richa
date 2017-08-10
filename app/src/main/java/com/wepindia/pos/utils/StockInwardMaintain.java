package com.wepindia.pos.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.ItemStock;
import com.wepindia.pos.GenericClasses.MessageDialog;

/**
 * Created by RichaA on 3/14/2017.
 */

public class StockInwardMaintain {
    private MessageDialog MsgBox;
    private DatabaseHandler db = null;
    private Context myContext = null;

    public StockInwardMaintain(Context myContext, DatabaseHandler db) {
        this.myContext = myContext;
        this.db = db;
        MsgBox = new MessageDialog(myContext);
    }

    public void addIngredientToStock_Inward(String businessdate, int MenuCode,String IngredientName, double openingStock, double rate )
    {
        try{
            Log.d("addIngredient_Inward():"," checked in");
            ItemStock item = new ItemStock();
            item.setMenuCode(MenuCode);
            item.setItemName(IngredientName);
            item.setOpeningStock(openingStock);
            item.setClosingStock(openingStock);
            item.setRate(rate);
            Log.d("addItemToStk_Inward():", item.getItemName() + " @ " + businessdate);
            long status = db.insertStockInward(item, businessdate);
            if (status < 1) {
                Log.d("StockInwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
    }

    public void updateOpeningStock_Inward(String businessdate, int MenuCode,String IngredientName,
                                          double openingStock, double rate )
    {
        try{
            Log.d("updateOpeningStock():"," Inward checked in");
            ItemStock item = new ItemStock();
            item.setMenuCode(MenuCode);
            item.setItemName(IngredientName);
            item.setOpeningStock(openingStock);
            item.setRate(rate);
            Log.d("updateOpeningStock():", "Inward : "+item.getItemName() + " @ " + businessdate);
            long status = db.updateOpeningStockInward(item, businessdate);
            if (status < 1) {
                Log.d("StockInwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
            }}catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
    }

    public void updateClosingStock_Inward(String businessdate, int MenuCode,String ItemName, double ClosingStock )
    {
        try{
            Log.d("updateClosingStock():"," Inward : checked in");
            ItemStock item = new ItemStock();
            item.setMenuCode(MenuCode);
            item.setItemName(ItemName);
            item.setClosingStock(ClosingStock);
            Log.d("updateClosingStock():", item.getItemName() + " @ " + businessdate);
            long status = db.updateClosingStockInward(item, businessdate);
            if (status < 1) {
                Log.d("StockInwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
            }}catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
    }

    public  void saveOpeningStock_Inward(String businessdate)
    {
        try{
            Log.d("SaveStock():","Inward  checked in");
            boolean stockAlreadyPresent = false;
            Cursor stockAlreadyPresent_cursor = db.getStockInwardForBusinessdate(businessdate);
            if(stockAlreadyPresent_cursor!=null && stockAlreadyPresent_cursor.moveToFirst()){// update only closing stock
                stockAlreadyPresent = true;
            }else
            { // new entry for businessdate
                //newBusinessDate = true; -- no need
            }
            final Cursor itemCursor = db.getAllItem_GoodsInward();
            if(itemCursor == null || !itemCursor.moveToFirst())
            {
                // no outward item in database
                Log.d("Stockinwardmaintain","SaveStock() : No outward item present");
            } else {
                if (!stockAlreadyPresent) {
                    do {
                        // new entry
                        ItemStock item = new ItemStock();
                        item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
                        item.setItemName(itemCursor.getString(itemCursor.getColumnIndex("ItemName")));
                        item.setOpeningStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
                        item.setClosingStock(itemCursor.getDouble(itemCursor.getColumnIndex("Quantity")));
                        double rate = 0;//itemCursor.getDouble(itemCursor.getColumnIndex("Value"));
                        item.setRate(rate);
                        Log.d("SaveStock():", item.getItemName() + " @ " + businessdate);
                        long status = db.insertStockInward(item, businessdate);
                        if (status < 1) {
                            Log.d("Stockoutwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
                        }
                    } while (itemCursor.moveToNext());
//                Toast.makeText(myContext, "Opening Stock Saved for " + businessdate, Toast.LENGTH_SHORT).show();
                }else{

                }
            }}catch (Exception e)
        {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
    }
    public  void updateClosingStock_Inward(String businessdate)
    {
        try{
            Log.d("UpdateStock():"," Inward checked in");
            boolean stockAlreadyPresent = false;

            Cursor stockAlreadyPresent_cursor = db.getStockInwardForBusinessdate(businessdate);
            if(stockAlreadyPresent_cursor!=null && stockAlreadyPresent_cursor.moveToFirst()){
                // update only closing stock
                stockAlreadyPresent = true;
            }
            final Cursor itemCursor = db.getAllItems();
            if(itemCursor == null || !itemCursor.moveToFirst())
            {
                // no outward item in database
                Log.d("StockInwardmaintain","SaveStock() : No Inward item present");
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
                        long status = db.updateClosingStockInward(item, businessDate_str);
                        if (status < 1) {
                            Log.d("StockInwardmaintain", " SaveStock() : cannot save stock for item :" + item.getItemName());
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


}




