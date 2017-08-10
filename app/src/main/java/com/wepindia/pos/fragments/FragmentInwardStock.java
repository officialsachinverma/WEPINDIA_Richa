package com.wepindia.pos.fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.Supplier_Model;
import com.wep.common.app.models.ItemStock;
import com.wep.common.app.views.WepButton;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;
import com.wepindia.pos.adapters.ItemInwardAdapter;
import com.wepindia.pos.adapters.SupplierAdapter;

import java.util.ArrayList;


public class FragmentInwardStock extends Fragment {


    Context myContext;
    DatabaseHandler dbStockInward ;
    MessageDialog MsgBox;

    TextView ItemLongName,item_uom;
    TextView tvExistingStock,tvItemNewStock, txtRate1, tvItemRate1, tvItemExistingStock;
    EditText txtNewStock;
    WepButton btnUpdate,btnClearStock,btnCloseStock,btn_InwardItem,btn_Supplier;
    TableRow rowItemExistingStock,rowItemRate1;

    private ItemInwardAdapter itemsAdapter;
    private SupplierAdapter supplierAdapter;
    private ListView listViewSupplier, listViewItem;

    boolean SUPPLIER_MODE = false;
    int supplierCode = -1;
    String businessDate="";

    public FragmentInwardStock() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbStockInward = new DatabaseHandler(getActivity());
            //dbStockInward.OpenDatabase();
        }catch (Exception e){   e.printStackTrace();  }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_inward_stock, container, false);
        
        myContext = getActivity();
        MsgBox = new MessageDialog(myContext);      

        try{
            InitializeViews(view);
            dbStockInward.CloseDatabase();
            dbStockInward.CreateDatabase();
            dbStockInward.OpenDatabase();
            Cursor cursor = dbStockInward.getCurrentDate();
            if(cursor!=null && cursor.moveToFirst())
            {
                businessDate= cursor.getString(cursor.getColumnIndex("BusinessDate"));
            }
            tvItemNewStock.setEnabled(false);
            txtNewStock.setEnabled(false);
            loadItems(0);
            ResetStock();
        }
        catch(Exception exp){
            Toast.makeText(myContext, "FragmentInwardStock: " + exp.getMessage(), Toast.LENGTH_LONG).show();
            exp.printStackTrace();
        }
        return view;
    }

    private void InitializeViews(View view) {
        ItemLongName = (TextView) view.findViewById(R.id.txtItemLongNameValue);
        item_uom = (TextView) view.findViewById(R.id.item_uom);
        tvExistingStock = (TextView) view.findViewById(R.id.tvItemExistingStockValue);
        tvItemNewStock = (TextView) view.findViewById(R.id.tvItemNewStock);
        tvItemExistingStock = (TextView) view.findViewById(R.id.tvItemExistingStock);
        txtNewStock = (EditText) view.findViewById(R.id.etItemNewStock);
        txtRate1 = (TextView) view.findViewById(R.id.etItemRate1);
        tvItemRate1 = (TextView) view.findViewById(R.id.tvItemRate1);

        rowItemExistingStock = (TableRow) view.findViewById(R.id.rowItemExistingStock);
        rowItemRate1 = (TableRow) view.findViewById(R.id.rowItemRate1);
        listViewItem = (ListView) view.findViewById(R.id.listViewFilter3);
        listViewItem.setOnItemClickListener(itemsClick);
        listViewSupplier = (ListView) view.findViewById(R.id.listViewFilter1);
        listViewSupplier.setOnItemClickListener(supplierClick);

        btnUpdate = (WepButton) view.findViewById(R.id.btnUpdateStock);
        btnClearStock = (WepButton) view.findViewById(R.id.btnClearStock);
        btnCloseStock = (WepButton) view.findViewById(R.id.btnCloseStock);
        btn_Supplier = (WepButton) view.findViewById(R.id.btn_Supplier);
        btn_InwardItem = (WepButton) view.findViewById(R.id.btn_InwardItem);
        btn_Supplier.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                btnSupplierClick();
            }
        });
        btn_InwardItem.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                btnItemClick();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                UpdateStock(arg0);
            }
        });

        btnClearStock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                ResetStock();
            }
        });
        btnCloseStock.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                CloseStock(arg0);
            }
        });
        
        
    }
   public void btnItemClick()
   {
       listViewSupplier.setVisibility(View.INVISIBLE);
       tvItemNewStock.setEnabled(false);
       txtNewStock.setEnabled(false);
       tvItemRate1.setText("Weighted Avg Rate");
       tvItemExistingStock.setText("Existing Stock");
       rowItemExistingStock.setVisibility(View.VISIBLE);
       rowItemRate1.setVisibility(View.INVISIBLE);
       loadItems(0);
       ResetStock();
       SUPPLIER_MODE = false;
       supplierCode = -1;
   }
   private void btnSupplierClick()
    {
        listViewItem.setVisibility(View.INVISIBLE);
        tvItemNewStock.setEnabled(true);
        txtNewStock.setEnabled(true);
        tvItemRate1.setText("Prev. Rate");
        tvItemExistingStock.setText("Stock purchased till now for supplier");
        rowItemExistingStock.setVisibility(View.GONE);
        rowItemRate1.setVisibility(View.VISIBLE);
        loadSupplier();
        ResetStock();
        SUPPLIER_MODE = true;
        supplierCode = -1;
    }
    private AdapterView.OnItemClickListener itemsClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ItemStock items = (ItemStock) itemsAdapter.getItem(position);
            if (view.getTag() != null) {

                if (items!=null) {
                    //strMenuCode = Item.getString(Item.getColumnIndex("MenuCode"));
                    ItemLongName.setText(items.getItemName());
                    tvExistingStock.setText(String.format("%.2f",items.getOpeningStock()));
                    item_uom.setText(items.getUOM());
                    txtRate1.setText(String.format("%.2f",items.getRate()));
                    txtNewStock.setText("0");
                    btnUpdate.setEnabled(true);
                }
            }
        }
    };
    private AdapterView.OnItemClickListener supplierClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Supplier_Model supplier = (Supplier_Model) supplierAdapter.getItem(position);
            supplierCode  = supplier.getSupplierCode();
            loadItems( supplierCode );
            ResetStock();
        }
    };


    public void UpdateStock(View v) {

        String strNewStock = txtNewStock.getText().toString();
        String strRate1 = txtRate1.getText().toString();
        String itemname = ItemLongName.getText().toString();

        if (strNewStock.equalsIgnoreCase("")) {
            MsgBox.Show("Warning", "Enter stock before updating");
            return;
        }  if(strRate1.equalsIgnoreCase(""))  {
            MsgBox.Show("Warning", "Enter rate before updating");
            return;
        }
        /*UpdateItemStock_goodsInward(itemname, Float.parseFloat(strNewStock), Float.parseFloat(strRate1));
        UpdateStockInward(itemname, Float.parseFloat(strNewStock), Double.parseDouble(strRate1));*/

        String uom = item_uom.getText().toString();
        double rate_new = UpdateGoodsInward(itemname, Float.parseFloat(strNewStock), uom, Double.parseDouble(strRate1));
        UpdateStockInward(itemname,Float.parseFloat(strNewStock),   rate_new);

        if(SUPPLIER_MODE)
        {            // update for Supplierwise item in TBL_ITEM_INWARD
            UpdateItemStock_TBL_Item_Inward(itemname, Float.parseFloat(strNewStock), Float.parseFloat(strRate1));
            btnSupplierClick();
        }else
        {
            btnItemClick();
        }
        ResetStock();
    }


    private void loadItems(final int supplierCode) {
        new AsyncTask<Void, Void, ArrayList<ItemStock>>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<ItemStock> doInBackground(Void... params) {
                ArrayList<ItemStock> itemList = new ArrayList<ItemStock>();
                Cursor cursor = null;
                if(supplierCode == 0){
                    cursor = dbStockInward.getAllItem_GoodsInward();
                    while (cursor!=null && cursor.moveToNext())
                    {
                        ItemStock item = new ItemStock();
                        //item.setMenuCode(cursor.getInt(cursor.getColumnIndex("MenuCode")));
                        item.setItemName(cursor.getString(cursor.getColumnIndex("ItemName")));
                        item.setOpeningStock(cursor.getDouble(cursor.getColumnIndex("Quantity")));
                        item.setRate(cursor.getDouble(cursor.getColumnIndex("Value")));
                        item.setUOM(cursor.getString(cursor.getColumnIndex("UOM")));
                        itemList.add(item);
                    }
                }else{
                    cursor = dbStockInward.getLinkedMenuCodeForSupplier(supplierCode);
                    while (cursor!=null && cursor.moveToNext())
                    {
                        ItemStock item = new ItemStock();
                        item.setItemName(cursor.getString(cursor.getColumnIndex("ItemName")));
                        item.setOpeningStock(cursor.getDouble(cursor.getColumnIndex("Quantity")));
                        item.setRate(cursor.getDouble(cursor.getColumnIndex("Rate")));
                        item.setUOM(cursor.getString(cursor.getColumnIndex("UOM")));
                        itemList.add(item);
                    }
                }
                return itemList;
            }

            @Override
            protected void onPostExecute(ArrayList<ItemStock> itemList) {
                super.onPostExecute(itemList);
                //editTextOrderNo.setText(String.valueOf(dbStockInward.getNewBillNumber()));
                if(itemList!=null)
                    setItemsAdapter(itemList);
                listViewItem.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
    private void loadItems_for_supplier(final int supplierCode) {
        new AsyncTask<Void, Void, ArrayList<ItemStock>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<ItemStock> doInBackground(Void... params) {
                ArrayList<ItemStock> itemList = new ArrayList<ItemStock>();
                Cursor cursor = null;
                try {
                    cursor = dbStockInward.getLinkedMenuCodeForSupplier(supplierCode);
                    while (cursor!=null && cursor.moveToNext())
                    {
                        ItemStock item = new ItemStock();
                        item.setItemName(cursor.getString(cursor.getColumnIndex("ItemName")));
                        item.setOpeningStock(cursor.getDouble(cursor.getColumnIndex("Quantity")));
                        item.setRate(cursor.getDouble(cursor.getColumnIndex("Rate")));
                        itemList.add(item);
                    }
                } catch (Exception e) {
                    itemList = null;
                }
                return itemList;
            }

            @Override
            protected void onPostExecute(ArrayList<ItemStock> itemList) {
                super.onPostExecute(itemList);
                if(itemList!=null)
                    setItemsAdapter(itemList);
                listViewItem.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    public void CloseStock(View v) {
        // Close Database connection and finish the activity
        dbStockInward.CloseDatabase();
        getActivity().finish();
    }
    private void ResetStock() {
        ItemLongName.setText("");
        txtNewStock.setText("0");
        tvExistingStock.setText("0");
        txtRate1.setText("0");
        btnUpdate.setEnabled(false);
        item_uom.setText("");
    }
//    private void loadSupplier() {
//        ArrayList<Supplier_Model> supplierList = new ArrayList<Supplier_Model>();
//        Cursor cursor = dbStockInward.getAllSupplierName_nonGST();
//        if (cursor != null && cursor.moveToFirst()) {
//            do {
//                Supplier_Model  supplier  = new Supplier_Model();
//                supplier.setSupplierCode(cursor.getInt(cursor.getColumnIndex("SupplierCode")));
//                supplier.setSupplierName(cursor.getString(cursor.getColumnIndex("SupplierName")));
//                supplier.setSupplierAddress(cursor.getString(cursor.getColumnIndex("SupplierAddress")));
//                supplier.setSupplierPhone(cursor.getInt(cursor.getColumnIndex("SupplierPhone")));
//                supplierList.add(supplier);// adding
//            } while (cursor.moveToNext());
//        }
//        if(supplierList!=null){
//            setSupplierAdapter(supplierList);
//
//        }
//
//
//
//    }

    private void loadSupplier() {
        new AsyncTask<Void, Void, ArrayList<Supplier_Model>>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected ArrayList<Supplier_Model> doInBackground(Void... params) {
                ArrayList<Supplier_Model> supplierList = new ArrayList<Supplier_Model>();
                Cursor cursor = dbStockInward.getAllSupplierName_nonGST();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Supplier_Model  supplier  = new Supplier_Model();
                        supplier.setSupplierCode(cursor.getInt(cursor.getColumnIndex("SupplierCode")));
                        supplier.setSupplierName(cursor.getString(cursor.getColumnIndex("SupplierName")));
                        supplier.setSupplierAddress(cursor.getString(cursor.getColumnIndex("SupplierAddress")));
                        supplier.setSupplierPhone(cursor.getString(cursor.getColumnIndex("SupplierPhone")));
                        supplierList.add(supplier);// adding
                    } while (cursor.moveToNext());
                }
                return supplierList;
            }

            @Override
            protected void onPostExecute(ArrayList<Supplier_Model> supplierList) {
                super.onPostExecute(supplierList);
                if(supplierList!=null){
                    setSupplierAdapter(supplierList);
                }
                listViewSupplier.setVisibility(View.VISIBLE);
            }
        }.execute();
    }
    public void setSupplierAdapter(ArrayList<Supplier_Model> supplierList)
    {
        if(supplierAdapter==null){

            supplierAdapter = new SupplierAdapter(getActivity(),supplierList,dbStockInward, "todo");
            listViewSupplier.setAdapter(supplierAdapter);
        }
        else
            supplierAdapter.notifyDataSetChanged(supplierList);
    }

    public void setItemsAdapter(ArrayList<ItemStock> itemList)
    {
        if(itemsAdapter==null){
            //itemsAdapter = new ItemInwardAdapter(myContext,dbStockInward,itemList);
            listViewItem.setAdapter(itemsAdapter);
        }
        else ;
            //itemsAdapter.notifyDataSetChanged(itemList);
    }


    private double UpdateGoodsInward(String itemname, float quantity, String mou, double rate)
    {
        double rate_new = 0;
        try {
            Cursor item_present_crsr = dbStockInward.getItem_GoodsInward(itemname);
            if (item_present_crsr != null && item_present_crsr.moveToFirst()) {
                // already present , needs to update
                String qty_str = item_present_crsr.getString(item_present_crsr.getColumnIndex("Quantity"));
                float qty_prev = Float.parseFloat(qty_str);
                //quantity += qty_prev;
                int newSupplierCount = item_present_crsr.getInt(item_present_crsr.getColumnIndex("SupplierCount"));
                double rate_prev = item_present_crsr.getDouble(item_present_crsr.getColumnIndex("Value"));
                double temp = rate_prev * qty_prev;
                double newtemp = quantity *rate;
                rate_new = (temp+newtemp)/(quantity+qty_prev);
                /*rate_new = rate_prev*newSupplierCount;
                rate_new += rate;
                rate_new /= newSupplierCount;*/

                Long l = dbStockInward.updateIngredient(itemname, quantity+qty_prev,rate_new, newSupplierCount);
                if (l > 0) {
                    Log.d(" GoodsInwardNote ", itemname + " updated  successfully at " + l);
                }

            }else
            {
                // new entry
                rate_new = rate;
                Long  l = dbStockInward.addIngredient(itemname, quantity, mou, rate_new, 1);
                if (l > 0) {
                    Log.d(" GoodsInwardNote ", itemname + " added  successfully at " + l);
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            return rate_new;
        }
    }
    private void UpdateStockInward(String itemname, float quantity,  double rate)
    {
        try{
            Cursor item_present_crsr = dbStockInward.getInwardStock(itemname);
            double Openingqty_prev = 0;
            double Closingqty_prev = 0;
            ItemStock item = new ItemStock();

            if (item_present_crsr != null && item_present_crsr.moveToFirst()) {
                // already present , needs to update
                String qty_str = item_present_crsr.getString(item_present_crsr.getColumnIndex("OpeningStock"));
                if(!qty_str.equalsIgnoreCase(""))
                    Openingqty_prev =  Double.parseDouble(qty_str);
                String ClosingQty_str = item_present_crsr.getString(item_present_crsr.getColumnIndex("ClosingStock"));
                if(!qty_str.equalsIgnoreCase(""))
                    Closingqty_prev =  Double.parseDouble(qty_str);
                item.setMenuCode(item_present_crsr.getInt(item_present_crsr.getColumnIndex("MenuCode")));
                item.setOpeningStock(Openingqty_prev+Double.parseDouble(String.valueOf(quantity)));
                item.setClosingStock(Closingqty_prev+Double.parseDouble(String.valueOf(quantity)));
                item.setRate(rate);
                Long l = dbStockInward.updateOpeningStockInward(item, businessDate);
                if (l>0) {
                    Log.d("Stockinwardmaintain", " SaveStock() : opening save stock for item :" + item.getItemName());
                }
                l = dbStockInward.updateClosingStockInward(item, businessDate);
                if (l>0) {
                    Log.d("Stockinwardmaintain", " SaveStock() : closing save stock for item :" + item.getItemName());
                }

            }else
            {
                // new entry
                Cursor itemCursor = dbStockInward.getItem_GoodsInward(itemname);
                if(itemCursor != null && itemCursor.moveToFirst())
                {
                    item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
                    item.setItemName(itemname);
                    item.setOpeningStock(quantity);
                    item.setClosingStock(quantity);
                    item.setRate(rate);
                    Log.d("SaveStock():", item.getItemName() + " @ " + businessDate);
                    long l = dbStockInward.insertStockInward(item, businessDate);
                    if (l>0) {
                        Log.d("Stockinwardmaintain", " SaveStock() : save stock for item :" + item.getItemName());
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void UpdateItemStock_goodsInward(String ItemName, float NewStock, float Rate1) {
        long lRowId = 0;
        Cursor cursor = dbStockInward.getItem_GoodsInward(ItemName);
        if(cursor!=null && cursor.moveToFirst())
        {
            float previousQty = cursor.getFloat(cursor.getColumnIndex("Quantity"));
            lRowId = dbStockInward.updateIngredientQuantityInGoodsInward(ItemName, previousQty+NewStock, Rate1);
            Log.d("FragmentInwardStock", "TBL_GoodsInward : Row Id:" + String.valueOf(lRowId));
        }
    }
    private void UpdateItemStock_TBL_Item_Inward(String ItemName, float NewStock, float Rate1) {
        long lRowId = 0;
        Cursor cursor = dbStockInward.getItemdetailsforSupplier(supplierCode, ItemName);
        if(cursor!=null && cursor.moveToFirst())
        {
            float previousQty = cursor.getFloat(cursor.getColumnIndex("Quantity"));
            int menuCode = cursor.getInt(cursor.getColumnIndex("MenuCode"));
            lRowId = dbStockInward.updateItem_Inw(supplierCode,menuCode,ItemName, previousQty+NewStock, Rate1);
            Log.d("FragmentInwardStock", "TBL_ItemInward : Row Id:" + String.valueOf(lRowId));
        }
    }
    private void UpdateStockInward_old(String itemname, float quantity, double rate)
    {
        try{
            Cursor item_present_crsr = dbStockInward.getInwardStock(itemname);
            double Openingqty_prev = 0;
            double Closingqty_prev = 0;
            ItemStock item = new ItemStock();

            if (item_present_crsr != null && item_present_crsr.moveToFirst()) {
                // already present , needs to update
                String qty_str = item_present_crsr.getString(item_present_crsr.getColumnIndex("OpeningStock"));
                if(!qty_str.equalsIgnoreCase(""))
                    Openingqty_prev =  Double.parseDouble(qty_str);
                String ClosingQty_str = item_present_crsr.getString(item_present_crsr.getColumnIndex("ClosingStock"));
                if(!qty_str.equalsIgnoreCase(""))
                    Closingqty_prev =  Double.parseDouble(qty_str);
                item.setMenuCode(item_present_crsr.getInt(item_present_crsr.getColumnIndex("MenuCode")));
                item.setOpeningStock(Openingqty_prev+Double.parseDouble(String.valueOf(quantity)));
                item.setClosingStock(Closingqty_prev+Double.parseDouble(String.valueOf(quantity)));
                Long l = dbStockInward.updateOpeningStockInward(item, businessDate);
                if (l>0) {
                    Log.d("Stockinwardmaintain", " SaveStock() : save stock for item :" + item.getItemName());
                }
                l = dbStockInward.updateClosingStockInward(item, businessDate);
                if (l>0) {
                    Log.d("Stockinwardmaintain", " SaveStock() : save stock for item :" + item.getItemName());
                }

            }else
            {
                // new entry
                Cursor itemCursor = dbStockInward.getItem_GoodsInward(itemname);
                if(itemCursor == null || !itemCursor.moveToFirst())
                {
                    item.setMenuCode(itemCursor.getInt(itemCursor.getColumnIndex("MenuCode")));
                    item.setItemName(itemname);
                    item.setOpeningStock(quantity);
                    item.setClosingStock(quantity);
                    item.setRate(rate);
                    Log.d("SaveStock():", item.getItemName() + " @ " + businessDate);
                    long l = dbStockInward.insertStockInward(item, businessDate);
                    if (l>0) {
                        Log.d("Stockinwardmaintain", " SaveStock() : save stock for item :" + item.getItemName());
                    }
                }
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}


