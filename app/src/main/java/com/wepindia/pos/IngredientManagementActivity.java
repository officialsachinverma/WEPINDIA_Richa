package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.ItemIngredients;
import com.wep.common.app.WepBaseActivity;
import com.wep.common.app.views.WepButton;
import com.wep.common.app.models.Ingredient;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.IngredientListAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class IngredientManagementActivity extends WepBaseActivity {

    AutoCompleteTextView autocompletetv_itemslist, autocompletetv_ingredientlist, autocompletetv_submitteditemsearch;
    EditText et_item_quantity, et_ingredient_quantity;
    TextView tv_ingredient_uom,tv_item_uom,tv_menucode;
    WepButton btnSave, btnSubmit,btn_add_new_item,btnPrint, btnClearItem,btnCloseItem;
    ListView lstvw_displayingredient;
    ArrayList<Ingredient> ingredientlist;
    IngredientListAdapter ingredientListAdapter;
    AlertDialog dialog;

    final int INGREDIENT_LIST = 0;
    final int SUBMITTED_LIST = 1;
    Context myContext;
    DatabaseHandler dbIngredientManagement;
    String strUserName = "", strUserId = "";
    MessageDialog MsgBox;
    TableLayout tbl_displaysubmitteditems;
    String Status_Saved = "0";
    String Status_Submitted = "1";
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_management);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbIngredientManagement = new DatabaseHandler(IngredientManagementActivity.this);
        myContext = this;
        MsgBox = new MessageDialog(myContext);

        try {
            strUserName = getIntent().getStringExtra("USER_NAME");
            strUserId = getIntent().getStringExtra("USER_ID");
            //tvTitleUserName.setText(strUserName.toUpperCase());
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            //tvTitleDate.setText("Date : " + s);
            com.wep.common.app.ActionBarUtils.setupToolbar(IngredientManagementActivity.this,toolbar,getSupportActionBar()," Ingredient Management ",strUserName," Date:"+s.toString());

            InitializeViews();
            dbIngredientManagement.CreateDatabase();
            dbIngredientManagement.OpenDatabase();
            reset();
            loadAutoCompleteOutwardItems();
            loadAutoCompleteSubmitItems();
            loadAutoCompleteIngredients();
            //DisplaySubmittedItems();
            miscActivities();


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
        }
    }


    void miscActivities()
    {
        autocompletetv_itemslist.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autocompletetv_itemslist.showDropDown();
                return false;
            }
        });
        autocompletetv_ingredientlist.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autocompletetv_ingredientlist.showDropDown();
                return false;
            }
        });
        autocompletetv_submitteditemsearch.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event){
                autocompletetv_submitteditemsearch.showDropDown();
                return false;
            }
        });
        try {
            autocompletetv_itemslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String itemSelected = autocompletetv_itemslist.getText().toString().toUpperCase();
                    Cursor crsr = dbIngredientManagement.getItemDetails(itemSelected);
                    if (crsr != null && crsr.moveToFirst()) { // item present in item database
                        int menucode = crsr.getInt(crsr.getColumnIndex("MenuCode"));
                        String item_uom = crsr.getString(crsr.getColumnIndex("UOM"));
                        if (item_uom != null && !item_uom.equals("")) {
                            tv_item_uom.setText(item_uom);
                        }
                        tv_menucode.setText(String.valueOf(menucode));
                        loadIngredientTable(menucode);
                    } else { // item not present in outward item table
                        MsgBox.Show(" Inappropriate Information", " Invalid Item ");
                    }

                }
            });

            autocompletetv_ingredientlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String ingredientSelected = autocompletetv_ingredientlist.getText().toString();
                    Cursor crsr = dbIngredientManagement.getItem_GoodsInward(ingredientSelected);
                    if(crsr!=null && crsr.moveToFirst())
                    {
                        String ingredient_uom = crsr.getString(crsr.getColumnIndex("UOM"));
                        if(ingredient_uom!= null && !ingredient_uom.equals(""))
                        {
                            tv_ingredient_uom.setText(ingredient_uom);
                        }
                    }
                }
            });

            autocompletetv_submitteditemsearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   /* String itemSelected = autocompletetv_submitteditemsearch.getText().toString().toUpperCase();
                    ClearTable(tbl_displaysubmitteditems);
                    TableRow row = new TableRow(myContext);

                    TextView sn = new TextView(myContext);
                    sn.setWidth(20);
                    sn.setBackgroundResource(R.drawable.border);
                    sn.setText(String.valueOf("0"));
                    sn.setTextSize(20);
                    sn.setPadding(10,0,0,0);


                    TextView ItemName = new TextView(myContext);
                    ItemName.setBackgroundResource(R.drawable.border);
                    ItemName.setWidth(130);
                    ItemName.setPadding(10,0,0,0);
                    ItemName.setTextSize(20);
                    ItemName.setText(itemSelected);

                    /*Button btndel = new Button(myContext);
                    btndel.setBackground(getResources().getDrawable(R.drawable.deletered1));
                    btndel.setPadding(5,0,0,0);*//*

                    Button btndel = new Button(myContext);
                    btndel.setBackground(getResources().getDrawable(R.drawable.delete_icon_border));
                    btndel.setLayoutParams(new TableRow.LayoutParams(40, 30));
                    btndel.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            final View row = v;
                            MsgBox = new MessageDialog(myContext);
                            MsgBox.setTitle("Confirm")
                                    .setMessage("Do you want to Delete this item ")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            TableRow tr = (TableRow) row.getParent();
                                            TextView IngredientName_temp = (TextView) tr.getChildAt(1);
                                            String itemname = IngredientName_temp.getText().toString();
                                            long lResult = dbIngredientManagement.DeleteSubmittedItem(itemname);
                                            if (lResult > 0) {
                                                Toast.makeText(myContext, itemname +" Deleted Successfully ", Toast.LENGTH_SHORT).show();
                                                Log.d("IngredientManagement: ", itemname +" Deleted Successfully from Submitted table");
                                                ViewGroup container = ((ViewGroup) row.getParent());
                                                container.removeView(row);
                                                container.invalidate();
                                            }
                                            ClearTable(tbl_displaysubmitteditems);
                                            loadAutoCompleteSubmitItems();
                                            DisplaySubmittedItems();
                                            dialog.dismiss();
                                        }
                                    })
                                    .setNegativeButton("No",null)
                                    .show();
                        }
                    });

                    TextView spc = new TextView(myContext);
                    spc.setWidth(5);

                   *//* int res = getResources().getIdentifier("delete", "drawable", myContext.getPackageName());
                    ImageButton ImgDelete = new ImageButton(myContext);
                    ImgDelete.setImageResource(res);*//*

                    Button ImgDelete = new Button(myContext);
                    ImgDelete.setBackground(getResources().getDrawable(R.drawable.delete_icon_border));
                    ImgDelete.setLayoutParams(new TableRow.LayoutParams(40, 30));
                    ImgDelete.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            final View row = v;
                            AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                                    .setTitle("Delete")
                                    .setMessage("Are you sure you want to Delete this Item")
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {

                                            TableRow tr = (TableRow) row.getParent();
                                            TextView IngredientName_temp = (TextView) tr.getChildAt(1);
                                            String itemname = IngredientName_temp.getText().toString();
                                            long lResult = dbIngredientManagement.DeleteSubmittedItem(itemname);
                                            if (lResult > 0) {
                                                Toast.makeText(myContext, itemname +" Deleted Successfully ", Toast.LENGTH_SHORT).show();
                                                Log.d("IngredientManagement: ", itemname +" Deleted Successfully from Submitted table");
                                                ViewGroup container = ((ViewGroup) row.getParent());
                                                container.removeView(row);
                                                container.invalidate();
                                            }
                                            ClearTable(tbl_displaysubmitteditems);
                                            loadAutoCompleteSubmitItems();
                                            DisplaySubmittedItems();
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
                    });

                    row.addView(sn);
                    row.addView(ItemName);
                    row.addView(spc);
                    row.addView(btndel);
                    tbl_displaysubmitteditems.addView(row);*/
                    //DisplaySubmittedItems();
                    String item = autocompletetv_submitteditemsearch.getText().toString().toUpperCase();
                    Intent intentInwardSupply = new Intent(myContext, ConvertIngredientActivity.class);
                    intentInwardSupply.putExtra("ItemName", item.toUpperCase());
                    intentInwardSupply.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
                    intentInwardSupply.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
                    intentInwardSupply.putExtra("CUST_ID", 0);
                    startActivity(intentInwardSupply);
                }
            });

            et_ingredient_quantity.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    String ingredient_name = autocompletetv_ingredientlist.getText().toString();
                    String ingredient_uom = tv_ingredient_uom.getText().toString();
                    if (!ingredient_name.equals("") && ingredient_uom.equals("")) {
                        Cursor crsr_item = dbIngredientManagement.getItem_GoodsInward(ingredient_name);
                        if (crsr_item != null && crsr_item.moveToFirst()) {
                            String item_uom_temp = crsr_item.getString(crsr_item.getColumnIndex("UOM"));
                            if (item_uom_temp != null && !item_uom_temp.equals("")) {
                                tv_ingredient_uom.setText(item_uom_temp);
                            }
                        }
                    }
                }
            });
            et_item_quantity.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    String item_name = autocompletetv_itemslist.getText().toString();
                    String item_uom = tv_item_uom.getText().toString();
                    if (!item_name.equals("") && item_uom.equals("")) {
                        Cursor crsr_item = dbIngredientManagement.getbyItemName(item_name);
                        if (crsr_item != null && crsr_item.moveToFirst()) {
                            String item_uom_temp = crsr_item.getString(crsr_item.getColumnIndex("UOM"));
                            if (item_uom_temp != null && !item_uom_temp.equals("")) {
                                tv_item_uom.setText(item_uom_temp);
                            }
                        }
                    }

                }
            });
        } catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
        }
    }

    void loadIngredientTable(int menucode)
    {
        int flag = 0;
        int count =0;
        try
        {
            Cursor crsr = dbIngredientManagement.getIngredientsForMenuCode(menucode);
            while (crsr != null && crsr.moveToNext()){
                //String itemname = crsr.getString(crsr.getColumnIndex("ItemName"));
                String item_qty_str = crsr.getString(crsr.getColumnIndex("Quantity"));
                float item_quantity = 0;
                int ingredientcode = 0;
                float ingredient_quantity = 0;
                String ingredientcode_str = crsr.getString(crsr.getColumnIndex("IngredientCode"));
                String ingredientname = crsr.getString(crsr.getColumnIndex("IngredientName"));
                String ingredient_qty_str = crsr.getString(crsr.getColumnIndex("IngredientQuantity"));

                String uom = crsr.getString(crsr.getColumnIndex("IngredientUOM"));
                String status = crsr.getString(crsr.getColumnIndex("Status"));

                if(item_qty_str !=null && !item_qty_str.equals(""))
                    item_quantity = Float.parseFloat(item_qty_str);
                if(ingredientcode_str !=null && !ingredientcode_str.equals(""))
                    ingredientcode = Integer.parseInt(ingredientcode_str);
                if(ingredient_qty_str !=null && !ingredient_qty_str.equals(""))
                    ingredient_quantity = Float.parseFloat(ingredient_qty_str);


                if (flag ==0)
                {
                    ClearTable(INGREDIENT_LIST);
                    tv_menucode.setText(String.valueOf(menucode));
                    et_item_quantity.setText(String.valueOf(item_quantity));
                    flag =1;
                }
                addingredienttotable( ingredientcode,  ingredientname,  ingredient_quantity,  uom);

            } // end of while
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
        }

    }

    void addingredienttotable( int ingredientcode, final String ingredientname, float ingredient_quantity, String uom)
    {
        Ingredient ingredient;
        if(ingredientlist == null || ingredientlist.size() ==0){
            ingredientlist = new ArrayList<>();
            ingredient = new Ingredient(1,ingredientcode,ingredientname, ingredient_quantity,uom);
        }
        else
            ingredient = new Ingredient(ingredientlist.size()+1,ingredientcode,ingredientname, ingredient_quantity,uom);

        ingredientlist.add(ingredient);
        if (ingredientListAdapter == null) {
            ingredientListAdapter = new IngredientListAdapter(this,ingredientlist );
            lstvw_displayingredient.setAdapter(ingredientListAdapter);
        } else {
            ingredientListAdapter.notifyNewDataAdded(ingredientlist);
        }
    }

    void loadAutoCompleteIngredients()
    {
        Cursor ingredient_crsr  = dbIngredientManagement.getAllItem_GoodsInward();
        ArrayList<String>  ingredientlist = new ArrayList<String>();
        while (ingredient_crsr !=null && ingredient_crsr.moveToNext()){
            String item = ingredient_crsr.getString(ingredient_crsr.getColumnIndex("ItemName"));
            if(item !=null)
                ingredientlist.add(item);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,ingredientlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocompletetv_ingredientlist.setAdapter(dataAdapter);
    }
    void loadAutoCompleteOutwardItems() {

        Cursor item_crsr  = dbIngredientManagement.getAllItems();
        ArrayList<String>  itemlist = new ArrayList<String>();
        while (item_crsr !=null && item_crsr.moveToNext()){
            String item = item_crsr.getString(item_crsr.getColumnIndex("ItemName"));
            if(item !=null)
                itemlist.add(item);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocompletetv_itemslist.setAdapter(dataAdapter);
    }

    void loadAutoCompleteSubmitItems() {
        Cursor item_crsr  = dbIngredientManagement.getItemsForSubmittedIngredients(Status_Submitted);
        ArrayList<String>  itemlist_submit = new ArrayList<String>();
        while (item_crsr !=null && item_crsr.moveToNext())
        {
            String item = item_crsr.getString(item_crsr.getColumnIndex("ItemName"));
            if(item !=null)
                itemlist_submit.add(item);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,itemlist_submit);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocompletetv_submitteditemsearch.setAdapter(dataAdapter);
    }

    void DisplaySubmittedItems()
    {
        Cursor item_crsr  = dbIngredientManagement.getItemsForSubmittedIngredients(Status_Submitted);
        ArrayList<String>  itemlist_submit = new ArrayList<String>();
        while (item_crsr !=null && item_crsr.moveToNext())
        {
            String item = item_crsr.getString(item_crsr.getColumnIndex("ItemName"));
            if(item !=null)
                itemlist_submit.add(item);
        }
        Collections.sort(itemlist_submit, String.CASE_INSENSITIVE_ORDER);
        try
        {
            //ClearTable(SUBMITTED_LIST);
            int count =0;
            for (final String item : itemlist_submit)
            {
                TableRow row = new TableRow(myContext);

                TextView sn = new TextView(myContext);
                sn.setWidth(95);
                sn.setBackgroundResource(R.drawable.border_itemdatabase);
                count++;
                sn.setPadding(10,0,0,0);
                sn.setTextSize(20);
                sn.setText(String.valueOf(count));


                TextView ItemName = new TextView(myContext);
                ItemName.setBackgroundResource(R.drawable.border_itemdatabase);
                ItemName.setWidth(320);
                ItemName.setPadding(10,0,0,0);
                ItemName.setTextSize(20);
                ItemName.setText(item);

                /*Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.deletered1));
                btndel.setPadding(5,0,0,0);*/

                Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.delete_icon_border));
                btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
                btndel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final View row = v;
                        MsgBox = new MessageDialog(myContext);
                        MsgBox.setTitle("Confirm")
                                .setMessage("Do you want to Delete this item ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        TableRow tr = (TableRow) row.getParent();
                                        TextView IngredientName_temp = (TextView) tr.getChildAt(1);
                                        String itemname = IngredientName_temp.getText().toString();
                                        long lResult = dbIngredientManagement.DeleteSubmittedItem(itemname);
                                        if (lResult > 0) {
                                            Toast.makeText(myContext, "Ingredient Deleted Successfully for "+itemname, Toast.LENGTH_SHORT).show();
                                            Log.d("IngredientManagement: ", "Ingredient Deleted Successfully for "+itemname);
                                            ViewGroup container = ((ViewGroup) row.getParent());
                                            container.removeView(row);
                                            container.invalidate();
                                        }
                                        ClearTable(SUBMITTED_LIST);
                                        DisplaySubmittedItems();
                                        loadAutoCompleteSubmitItems();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });

                TextView spc = new TextView(myContext);
                spc.setWidth(05);

                /*int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                ImageButton ImgDelete = new ImageButton(myContext);
                ImgDelete.setImageResource(res);
                ImgDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final View row = v;
                        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to Delete this Item")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        TableRow tr = (TableRow) row.getParent();
                                        TextView IngredientName_temp = (TextView) tr.getChildAt(1);
                                        String itemname = IngredientName_temp.getText().toString();
                                        long lResult = dbIngredientManagement.DeleteSubmittedItem(itemname);
                                        if (lResult > 0) {
                                            Toast.makeText(myContext, "Ingredient Deleted Successfully for"+itemname, Toast.LENGTH_SHORT).show();
                                            Log.d("IngredientManagement: ", "Ingredient Deleted Successfully for"+itemname);
                                            ViewGroup container = ((ViewGroup) row.getParent());
                                            container.removeView(row);
                                            container.invalidate();
                                        }
                                        ClearTable(tbl_displaysubmitteditems);
                                        DisplaySubmittedItems();
                                        loadAutoCompleteSubmitItems();
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
                    }  });*/

                row.addView(sn);
                row.addView(ItemName);
                row.addView(spc);
                row.addView(btndel);
                row.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentInwardSupply = new Intent(myContext, ConvertIngredientActivity.class);
                        intentInwardSupply.putExtra("ItemName", item.toUpperCase());
                        intentInwardSupply.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
                        intentInwardSupply.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
                        intentInwardSupply.putExtra("CUST_ID", 0);
                        startActivity(intentInwardSupply);                    }
                });
                tbl_displaysubmitteditems.addView(row);
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
        }
    }
    void DisplaySubmittedItems_old()
    {
        Cursor item_crsr  = dbIngredientManagement.getItemsForSubmittedIngredients(Status_Submitted);
        ArrayList<String>  itemlist_submit = new ArrayList<String>();
        while (item_crsr !=null && item_crsr.moveToNext())
        {
            String item = item_crsr.getString(item_crsr.getColumnIndex("ItemName"));
            if(item !=null)
                itemlist_submit.add(item);
        }
        Collections.sort(itemlist_submit, String.CASE_INSENSITIVE_ORDER);
        try
        {
            //ClearTable(tbl_displaysubmitteditems);
            int count =0;
            for (final String item : itemlist_submit)
            {
                TableRow row = new TableRow(myContext);

                TextView sn = new TextView(myContext);
                sn.setWidth(95);
                sn.setBackgroundResource(R.drawable.border_itemdatabase);
                count++;
                sn.setPadding(10,0,0,0);
                sn.setTextSize(20);
                sn.setText(String.valueOf(count));


                TextView ItemName = new TextView(myContext);
                ItemName.setBackgroundResource(R.drawable.border_itemdatabase);
                ItemName.setWidth(320);
                ItemName.setPadding(10,0,0,0);
                ItemName.setTextSize(20);
                ItemName.setText(item);

                /*Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.deletered1));
                btndel.setPadding(5,0,0,0);*/

                Button btndel = new Button(myContext);
                btndel.setBackground(getResources().getDrawable(R.drawable.delete_icon_border));
                btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
                btndel.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final View row = v;
                        MsgBox = new MessageDialog(myContext);
                        MsgBox.setTitle("Confirm")
                                .setMessage("Do you want to Delete this item ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        TableRow tr = (TableRow) row.getParent();
                                        TextView IngredientName_temp = (TextView) tr.getChildAt(1);
                                        String itemname = IngredientName_temp.getText().toString();
                                        long lResult = dbIngredientManagement.DeleteSubmittedItem(itemname);
                                        if (lResult > 0) {
                                            Toast.makeText(myContext, "Ingredient Deleted Successfully for "+itemname, Toast.LENGTH_SHORT).show();
                                            Log.d("IngredientManagement: ", "Ingredient Deleted Successfully for "+itemname);
                                            ViewGroup container = ((ViewGroup) row.getParent());
                                            container.removeView(row);
                                            container.invalidate();
                                        }
                                        ClearTable(SUBMITTED_LIST);
                                        DisplaySubmittedItems();
                                        loadAutoCompleteSubmitItems();
                                        dialog.dismiss();
                                    }
                                })
                                .setNegativeButton("No",null)
                                .show();
                    }
                });

                TextView spc = new TextView(myContext);
                spc.setWidth(05);

                /*int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                ImageButton ImgDelete = new ImageButton(myContext);
                ImgDelete.setImageResource(res);
                ImgDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final View row = v;
                        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to Delete this Item")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        TableRow tr = (TableRow) row.getParent();
                                        TextView IngredientName_temp = (TextView) tr.getChildAt(1);
                                        String itemname = IngredientName_temp.getText().toString();
                                        long lResult = dbIngredientManagement.DeleteSubmittedItem(itemname);
                                        if (lResult > 0) {
                                            Toast.makeText(myContext, "Ingredient Deleted Successfully for"+itemname, Toast.LENGTH_SHORT).show();
                                            Log.d("IngredientManagement: ", "Ingredient Deleted Successfully for"+itemname);
                                            ViewGroup container = ((ViewGroup) row.getParent());
                                            container.removeView(row);
                                            container.invalidate();
                                        }
                                        ClearTable(tbl_displaysubmitteditems);
                                        DisplaySubmittedItems();
                                        loadAutoCompleteSubmitItems();
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
                    }  });*/

                row.addView(sn);
                row.addView(ItemName);
                row.addView(spc);
                row.addView(btndel);
                row.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        Intent intentInwardSupply = new Intent(myContext, ConvertIngredientActivity.class);
                        intentInwardSupply.putExtra("ItemName", item.toUpperCase());
                        intentInwardSupply.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
                        intentInwardSupply.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
                        intentInwardSupply.putExtra("CUST_ID", 0);
                        startActivity(intentInwardSupply);                    }
                });
                //tbl_displaysubmitteditems.addView(row);
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
        }
    }

    void input_window()
    {
        /*AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);

        LayoutInflater UserAuthorization = (LayoutInflater) myContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View vwAuthorization = UserAuthorization.inflate(R.layout.activity_convert_ingredients, null);


        //tv_convert_item_authorize = (TextView)findViewById(R.id.tv_convert_item);
        final TextView tv_convert_menucode = (TextView) vwAuthorization.findViewById(R.id.tv_convert_menucode);
        final TextView tv_convert_item = (TextView) vwAuthorization.findViewById(R.id.tv_convert_item);
        final EditText et_convert_item_quantity = (EditText) vwAuthorization.findViewById(R.id.et_convert_item_quantity);
        final TextView tv_convert_item_uom = (TextView)vwAuthorization.findViewById(R.id.tv_convert_item_uom);

        final AutoCompleteTextView autocomplete_convert_ingredient = (AutoCompleteTextView) vwAuthorization.findViewById(R.id.autocomplete_convert_ingredient);
        final EditText et_convert_ingredient_quantity = (EditText)vwAuthorization.findViewById(R.id.et_convert_ingredient_quantity);
        TextView tv_convert_ingredient_uom = (TextView)vwAuthorization.findViewById(R.id.tv_convert_ingredient_uom);

        Button btn_convert_add = (Button)vwAuthorization.findViewById(R.id.btn_convert_add);
        *//*Button btn_convert_Submit = (Button)findViewById(R.id.btn_convert_Submit);
        Button btn_convert_convertIngredient = (Button)findViewById(R.id.btn_convert_convertIngredient);
        Button btn_convert_close = (Button)findViewById(R.id.btn_convert_close);
*//*
        final TableLayout tbl_convert_ingredients = (TableLayout) findViewById(R.id.tbl_convert_ingredients);


        *//*btn_convert_Submit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tv_convert_item_authorize.setText("Hi");
            }
        });*//*
        AlertDialog.Builder builder = new AlertDialog.Builder(myContext);
        builder
                .setTitle("Convert ItemIngredients Into Item")
                .setView(vwAuthorization)
                .setNegativeButton("Cancel", null)
                .setPositiveButton("Submit Changes", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub

                    }
                })
                .setNeutralButton("Convert ItemIngredients", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                //Do stuff, possibly set wantToCloseDialog to true then...
                if(wantToCloseDialog)
                    dialog.dismiss();
                else
                {
                    if(tbl_convert_ingredients.getChildCount()<0)
                    {
                        Toast.makeText(myContext, "Please Add Ingredient to Table", Toast.LENGTH_SHORT).show();
                    }
                    else if(!autocomplete_convert_ingredient.getText().toString().equals("")  && !et_convert_ingredient_quantity.getText().toString().equals(""))
                    {
                        Toast.makeText(myContext, "Ingredient and Quantity cannot have values while submitting ", Toast.LENGTH_SHORT).show();
                    }
                    else if(et_convert_item_quantity.getText().toString().equals(""))
                    {
                        Toast.makeText(myContext, "Please Enter Item Quantity ", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        int success =1;
                        int menucode = Integer.parseInt(tv_convert_menucode.getText().toString());
                        int del = dbIngredientManagement.DeleteSavedIngredients(menucode,Status_Submitted);
                        Log.d("Convert ItemIngredients", "Submitted Rows deleted :"+del);
                        int count = tbl_convert_ingredients.getChildCount();
                        for(int i = 0; i<count; i++)
                        {
                            ItemIngredients ingredient = new ItemIngredients();
                            TableRow RowIngredient = (TableRow)tbl_convert_ingredients.getChildAt(i);

                            // MenuCode
                            ingredient.setMenucode(menucode);
                            Log.d("SubmitIngredients", "MenuCode :" + menucode);
                            // Item Name
                            String itemname = tv_convert_item.getText().toString();
                            ingredient.setItemname(itemname);
                            Log.d("SubmitIngredients", "Item Name :" + itemname);
                            // ItemQuantity
                            String item_qty = et_convert_item_quantity.getText().toString();
                            ingredient.setItemquantity(Float.parseFloat(item_qty));
                            Log.d("SubmitIngredients", "Item Quantity :" + item_qty);

                            String item_uom = tv_convert_item_uom.getText().toString();
                            ingredient.setUom(item_uom);
                            Log.d("SubmitIngredients", "Item UOM :" + item_uom);

                            if (RowIngredient.getChildAt(1) != null) {
                                TextView IngredientCode = (TextView) RowIngredient.getChildAt(1);
                                ingredient.setIngredientcode(Integer.parseInt(IngredientCode.getText().toString()));
                                Log.d("SubmitIngredients", "Ingredient Code :" + IngredientCode.getText().toString());
                            }

                            if (RowIngredient.getChildAt(2) != null) {
                                TextView IngredientName = (TextView) RowIngredient.getChildAt(2);
                                ingredient.setIngredientname(IngredientName.getText().toString());
                                Log.d("SubmitIngredients", "Ingredient Name :" + IngredientName.getText().toString());
                            }

                            if (RowIngredient.getChildAt(3) != null) {
                                TextView IngredientQuatity = (TextView) RowIngredient.getChildAt(3);
                                ingredient.setIngredientquantity(Float.parseFloat(IngredientQuatity.getText().toString()));
                                Log.d("SubmitIngredients", "Ingredient Quantity :" + IngredientQuatity.getText().toString());
                            }

                            if (RowIngredient.getChildAt(4) != null) {
                                TextView UOM = (TextView) RowIngredient.getChildAt(4);
                                ingredient.setIngredientUOM(UOM.getText().toString());
                                Log.d("SubmitIngredients", "Ingredient UOM :" + UOM.getText().toString());
                            }

                            ingredient.setStatus(Status_Submitted);
                            Log.d("SubmitIngredients", "Status :" + Status_Submitted);

                            long l = dbIngredientManagement.saveIngredient(ingredient);
                            if(l>0)
                            {
                                String msg = ingredient.getIngredientname()+" add at "+l;
                                Log.d("SavingIngredients", msg);
                            }
                            else
                            {
                                success = -1;
                            }
                        }
                        if(success != -1)
                        {
                            Toast.makeText(myContext, "ItemIngredients submitted Successfully ", Toast.LENGTH_SHORT).show();
                        }
                    }


                }
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });*/

    }


    void reset()
    {
        autocompletetv_itemslist.setText("");
        et_item_quantity.setText("");
        tv_menucode.setText("0");

        autocompletetv_ingredientlist.setText("");
        et_ingredient_quantity.setText("");
        tv_ingredient_uom.setText("");
        tv_item_uom.setText("");
        autocompletetv_submitteditemsearch.setText("");
        ClearTable(INGREDIENT_LIST);
        ClearTable(SUBMITTED_LIST);
        DisplaySubmittedItems();
    }

    public  void ClearTable( int ListName)
    {
        if (ListName == INGREDIENT_LIST){
            if(ingredientlist!= null)
                ingredientlist.clear();
            ingredientlist= new ArrayList<>();
            ingredientListAdapter = null;
            lstvw_displayingredient.setAdapter(null);
        }else if (ListName == SUBMITTED_LIST)
        {
            if(tbl_displaysubmitteditems == null)
                return;

            for (int i = tbl_displaysubmitteditems.getChildCount(); i >0;  i--) {
                View Row = tbl_displaysubmitteditems.getChildAt(i-1);
                if (Row instanceof TableRow) {
                    ((TableRow) Row).removeAllViews();
                }
                ViewGroup container = ((ViewGroup) Row.getParent());
                container.removeView(Row);
                container.invalidate();
            }
        }
    }

    void InitializeViews()
    {
        autocompletetv_itemslist = (AutoCompleteTextView) findViewById(R.id.autocompletetv_itemslist);
        autocompletetv_ingredientlist = (AutoCompleteTextView) findViewById(R.id.autocompletetv_ingredientlist);
        autocompletetv_submitteditemsearch = (AutoCompleteTextView) findViewById(R.id.autocompletetv_submitteditemsearch);

        et_item_quantity = (EditText) findViewById(R.id.et_item_quantity);
        et_ingredient_quantity = (EditText) findViewById(R.id.et_ingredient_quantity);

        tv_ingredient_uom = (TextView) findViewById(R.id.tv_ingredient_uom);
        tv_item_uom = (TextView) findViewById(R.id.tv_item_uom);
        tv_menucode = (TextView) findViewById(R.id.tv_menucode);

        btnSave = (WepButton) findViewById(R.id.btnSave);
        btnSubmit = (WepButton) findViewById(R.id.btnSubmit);
        btn_add_new_item = (WepButton) findViewById(R.id.btn_add_new_item);
        btnPrint = (WepButton) findViewById(R.id.btnPrint);
        btnClearItem = (WepButton) findViewById(R.id.btnClearItem);
        btnCloseItem = (WepButton) findViewById(R.id.btnCloseItem);

        btn_add_new_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddIngredient(v);
            }
        });
        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PrintIngredients(v);
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Save(v);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Submit(v);
            }
        });
        btnClearItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Clear(v);
            }
        });
        btnCloseItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close(v);
            }
        });
        //btnAddIngredient = (Button) findViewById(R.id.btnAddIngredient);


        lstvw_displayingredient = (ListView) findViewById(R.id.lstvw_displayingredient);
        tbl_displaysubmitteditems = (TableLayout) findViewById(R.id.tbl_displaysubmitteditems);
    }

    public void PrintIngredients(View v)
    {
        Intent intentPrintIngredients = new Intent(myContext, PrintIngredients.class);
        intentPrintIngredients.putExtra("USER_ID", strUserId);//spUser.getString("USER_ID", "GHOST"));
        intentPrintIngredients.putExtra("USER_NAME", strUserName);//spUser.getString("USER_NAME", "GHOST"));
        intentPrintIngredients.putExtra("CUST_ID", 0);
        startActivity(intentPrintIngredients);
    }
    public void AddIngredient(View v) {
        try {
            // check for items
            String itemname = autocompletetv_itemslist.getText().toString().toUpperCase();
            if (itemname.equals("")) {
                MsgBox.Show(" Insufficient Information ", " Please Select Item ");
                return;
            }

            Cursor crsr_item = dbIngredientManagement.getItemDetails(itemname);
            if (!(crsr_item != null && crsr_item.moveToFirst())) {
                MsgBox.Show(" Invalid Item", " This item is not in the List ");
                return;
            }
            else
            {
                int count = crsr_item.getCount();
                String item_uom = crsr_item.getString(crsr_item.getColumnIndex("UOM"));
                if (item_uom!=null )
                    tv_item_uom.setText(item_uom);
            }

            String item_qty = et_item_quantity.getText().toString();
            if (item_qty.equals("")) {
                MsgBox.Show(" Insufficient Information ", " Please Enter Item Quantity ");
                return;
            }

            // check for ingredients
            String ingredientname = autocompletetv_ingredientlist.getText().toString().toUpperCase();
            if (ingredientname.equals("")) {
                MsgBox.Show(" Insufficient Information ", " Please Select Ingredient ");
                return;
            }
            Cursor crsr_ingredient = dbIngredientManagement.getItem_GoodsInward(ingredientname);
            if (!(crsr_ingredient != null && crsr_ingredient.moveToFirst())) {
                MsgBox.Show(" Invalid Ingredient", " This ingredient is not in the List ");
                return;
            }
            else
            {
                String ingredient_uom = crsr_ingredient.getString(crsr_ingredient.getColumnIndex("UOM"));
                if (ingredient_uom!=null )
                    tv_ingredient_uom.setText(ingredient_uom);
            }

            String ingredient_qty = et_ingredient_quantity.getText().toString();
            if (ingredient_qty.equals("")) {
                MsgBox.Show(" Insufficient Information ", " Please Enter Ingredient Quantity ");
                return;
            }
            // add to tbl

            //int count = tbl_displayingredient.getChildCount();
            String uom = tv_ingredient_uom.getText().toString();
            int ingredientcode = crsr_ingredient.getInt(crsr_ingredient.getColumnIndex("MenuCode"));
            addingredienttotable(ingredientcode, ingredientname, Float.parseFloat(ingredient_qty), uom);
            autocompletetv_ingredientlist.setText("");
            et_ingredient_quantity.setText("");
            tv_ingredient_uom.setText("");

        } catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
        }


    }
    public void Save(View v)
    {
        int success =0;
        int count = ingredientlist.size();
        int menucode = 0;
        if(count <1)
        {
            MsgBox.Show(" Insufficient Information ", " Please Add Ingredient ");
            return;
        }
        // check for items
        String itemname = autocompletetv_itemslist.getText().toString();
        if(itemname.equals("")){
            MsgBox.Show(" Insufficient Information ", " Please Select Item ");
            return;
        }

        Cursor crsr_item = dbIngredientManagement.getbyItemName(itemname) ;
        if(!(crsr_item !=null && crsr_item.moveToFirst()))        {
            MsgBox.Show(" Invalid Item", " Please Select Item from the List ");
            return;
        }
        else
        {
            menucode = crsr_item.getInt(crsr_item.getColumnIndex("MenuCode"));
        }

        String item_qty = et_item_quantity.getText().toString();
        if(item_qty.equals(""))
        {
            MsgBox.Show(" Insufficient Information ", " Please Enter Item Quantity ");
            return;
        }

        String ingredientname = autocompletetv_ingredientlist.getText().toString().toUpperCase();
        String ingredient_qty = et_ingredient_quantity.getText().toString();

        if(!(ingredientname.equals("") &&(ingredient_qty.equals("") )))
        {
            MsgBox.Show("Error ", " Ingredient and Quantity cannot have values while submitting ");
            return;
        }
        if (false)//tbl_displayingredient.getChildCount() < 1)
        {
            MsgBox.Show("Error ", " Please Add Ingredient to Table ");
            return;
        }


        try
        {
            int del = dbIngredientManagement.DeleteSavedIngredients(menucode,Status_Saved);
            Log.d("SavingIngredients", "Rows deleted :"+del);
            for(Ingredient ingredient_single : ingredientlist)
            {
                ItemIngredients itemIngredients = new ItemIngredients();

                // MenuCode
                itemIngredients.setMenucode(menucode);
                Log.d("SavingIngredients", "MenuCode :" + menucode);
                // Item Name
                itemIngredients.setItemname(itemname);
                Log.d("SavingIngredients", "Item Name :" + itemname);
                // ItemQuantity
                itemIngredients.setItemquantity(Float.parseFloat(String.format("%.2f", Float.parseFloat(item_qty))));
                Log.d("SavingIngredients", "Item Quantity :" + item_qty);

                String item_uom = tv_item_uom.getText().toString();
                itemIngredients.setUom(item_uom);
                Log.d("SavingIngredients", "Item UOM :" + item_uom);


                itemIngredients.setIngredientcode(ingredient_single.getIngredientcode());
                Log.d("SavingIngredients", "Ingredient Code :" + itemIngredients.getIngredientcode());

                itemIngredients.setIngredientname(ingredient_single.getIngredientName());
                Log.d("SavingIngredients", "Ingredient Name :" + itemIngredients.getIngredientname());

                itemIngredients.setIngredientquantity(Float.parseFloat(String.format("%.2f",ingredient_single.getIngredientQuantity())));
                Log.d("SavingIngredients", "Ingredient Quantity :" + itemIngredients.getIngredientquantity());

                itemIngredients.setIngredientUOM(ingredient_single.getUOM());
                Log.d("SavingIngredients", "Ingredient UOM :" + itemIngredients.getIngredientUOM());

                itemIngredients.setStatus(Status_Saved);
                Log.d("SavingIngredients", "Status :" + Status_Saved);

                long l = dbIngredientManagement.saveIngredient(itemIngredients);
                if(l>0)
                {
                    String msg = itemIngredients.getIngredientname()+" add at "+l;
                    Log.d("SavingIngredients", msg);
                }
                else
                {
                    success = -1;
                }
            }
            if(success != -1)
            {
                Toast.makeText(myContext, "ItemIngredients saved Successfully. Please Submit it.  ", Toast.LENGTH_SHORT).show();
            }
            reset();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
        }
    }
    public void Submit(View v)
    {
        int success =0;
        int menucode = 0;

        // check for items
        String itemname = autocompletetv_itemslist.getText().toString();
        if(itemname.equals("")){
            MsgBox.Show(" Insufficient Information ", " Please Select Item ");
            return;
        }

        Cursor crsr_item = dbIngredientManagement.getbyItemName(itemname) ;
        if(!(crsr_item !=null && crsr_item.moveToFirst()))        {
            MsgBox.Show(" Invalid Item", " Please Select Item from the List ");
            return;
        }
        else
        {
            menucode = crsr_item.getInt(crsr_item.getColumnIndex("MenuCode"));
        }

        String item_qty = et_item_quantity.getText().toString();
        if(item_qty.equals(""))
        {
            MsgBox.Show(" Insufficient Information ", " Please Enter Item Quantity ");
            return;
        }

        // check for ingredients
        String ingredientname = autocompletetv_ingredientlist.getText().toString().toUpperCase();
        String ingredient_qty = et_ingredient_quantity.getText().toString();

        if(!(ingredientname.equals("") &&(ingredient_qty.equals("") )))
        {
            MsgBox.Show("Error ", " Ingredient and Quantity cannot have values while submitting ");
            return;
        }
        int count = ingredientlist.size();
        if (count < 1)
        {
            MsgBox.Show("Error ", " Please Add Ingredient to Table ");
            return;
        }

        try
        {
            int del = dbIngredientManagement.DeleteSavedIngredients(menucode,Status_Saved);
            Log.d("SubmitIngredients", "Saved Rows deleted :"+del);
            del = dbIngredientManagement.DeleteSavedIngredients(menucode,Status_Submitted);
            Log.d("SubmitIngredients", "Submitted Rows deleted :"+del);
            for(Ingredient ingredient_single : ingredientlist)
            {
                ItemIngredients itemIngredient = new ItemIngredients();
                TableRow RowIngredient = null;// (TableRow)tbl_displayingredient.getChildAt(i);

                // MenuCode
                itemIngredient.setMenucode(menucode);
                Log.d("SubmitIngredients", "MenuCode :" + menucode);
                // Item Name
                itemIngredient.setItemname(itemname);
                Log.d("SubmitIngredients", "Item Name :" + itemname);
                // ItemQuantity
                itemIngredient.setItemquantity(Float.parseFloat(String.format("%2f",Float.parseFloat(item_qty))));
                Log.d("SubmitIngredients", "Item Quantity :" + itemIngredient.getItemquantity());

                String item_uom = tv_item_uom.getText().toString();
                itemIngredient.setUom(item_uom);
                Log.d("SubmitIngredients", "Item UOM :" + item_uom);

                itemIngredient.setIngredientcode(ingredient_single.getIngredientcode());
                Log.d("SubmitIngredients", "Ingredient Code :" + itemIngredient.getIngredientcode());

                itemIngredient.setIngredientname(ingredient_single.getIngredientName());
                Log.d("SubmitIngredients", "Ingredient Name :" + itemIngredient.getIngredientname());

                itemIngredient.setIngredientquantity(Float.parseFloat(String.format("%.2f",ingredient_single.getIngredientQuantity())));
                Log.d("SubmitIngredients", "Ingredient Quantity :" + itemIngredient.getIngredientquantity());

                itemIngredient.setIngredientUOM(ingredient_single.getUOM());
                Log.d("SubmitIngredients", "Ingredient UOM :" + itemIngredient.getIngredientUOM());

                itemIngredient.setStatus(Status_Submitted);
                Log.d("SubmitIngredients", "Status :" + Status_Submitted);

                long l = dbIngredientManagement.saveIngredient(itemIngredient);
                if(l>0)
                {
                    String msg = itemIngredient.getIngredientname()+" add at "+l;
                    Log.d("SubmitIngredients", msg);
                }
                else
                {
                    success = -1;
                }
            }
            if(success != -1)
            {
                Toast.makeText(myContext, "ItemIngredients Submitted Successfully for "+itemname, Toast.LENGTH_SHORT).show();
            }
            reset();
            loadAutoCompleteSubmitItems();
            //DisplaySubmittedItems();
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
        }
    }
    public void Clear(View v)
    {
        reset();
    }
    public void Close(View v)
    {
        dbIngredientManagement.CloseDatabase();
        this.finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setIcon(R.drawable.ic_launcher)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);
                            dbIngredientManagement.CloseDatabase();
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Nullable
    @Override
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
    }

    @Override
    public void onHomePressed() {
        //ActionBarUtils.navigateHome(this);
        finish();
    }
}
