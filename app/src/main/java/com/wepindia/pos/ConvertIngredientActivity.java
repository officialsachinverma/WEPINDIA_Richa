package com.wepindia.pos;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
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
import com.wep.common.app.models.Ingredient;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.adapters.IngredientListAdapter;
import com.wepindia.pos.utils.StockInwardMaintain;
import com.wepindia.pos.utils.StockOutwardMaintain;

import java.util.ArrayList;
import java.util.Date;

public class ConvertIngredientActivity extends Activity {


    String Status_Saved = "0";
    String Status_Submitted = "1";


    TextView tv_convert_menucode,tv_convert_item ,tv_convert_item_uom;
    EditText et_convert_item_quantity ;

    AutoCompleteTextView autocompletetv_convert_ingredient ;
    EditText et_convert_ingredient_quantity ;
    TextView tv_convert_ingredient_uom;

    Button btn_convert_add , btn_convert_Submit,btn_convert_convertIngredient ,btn_convert_close;
//    TableLayout tbl_convert_ingredients; //TODO: Changed here for listview

    Context myContext;
    DatabaseHandler dbIngredientManagement;
    String strUserName = "", strUserId = "";
    MessageDialog MsgBox;
    String itemname_str = "";
    private Toolbar toolbar;

    ArrayList<Ingredient> ingredientlist; //TODO: Changed here for listview
    IngredientListAdapter ingredientListAdapter; //TODO: Changed here for listview
    ListView lv_convert_ingredients; //TODO: Changed here for listview

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_convert_ingredients);


        dbIngredientManagement = new DatabaseHandler(ConvertIngredientActivity.this);
        myContext = this;
        MsgBox = new MessageDialog(myContext);

        try {
            strUserName = getIntent().getStringExtra("USER_NAME");
            strUserId = getIntent().getStringExtra("USER_ID");
            //tvTitleUserName.setText(strUserName.toUpperCase());
            itemname_str = getIntent().getStringExtra("ItemName");
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
            //tvTitleDate.setText("Date : " + s);

            //com.wep.common.app.ActionBarUtils.setupToolbar(this,toolbar,getSupportActionBar()," Ingredient Management ",strUserName," Date:"+s.toString());
            InitialiseViewvariables();
            dbIngredientManagement.CreateDatabase();
            dbIngredientManagement.OpenDatabase();
            loadAutoCompleteIngredients();
            miscActivities();
            SetItemDetails(); //TODO: Changed here for listview



        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
            Log.d("Convertingredients ",e.getMessage() );
        }
    }

    void miscActivities()
    {
        autocompletetv_convert_ingredient.setOnTouchListener(new View.OnTouchListener(){
            //@Override
            public boolean onTouch(View v, MotionEvent event){
                autocompletetv_convert_ingredient.showDropDown();
                return false;
            }
        });
        try
        {
            autocompletetv_convert_ingredient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String ingredientSelected = autocompletetv_convert_ingredient.getText().toString();
                    Cursor crsr = dbIngredientManagement.getItem_GoodsInward(ingredientSelected);
                    if(crsr!=null && crsr.moveToFirst())
                    {
                        String ingredient_uom = crsr.getString(crsr.getColumnIndex("UOM"));
                        if(ingredient_uom!= null && !ingredient_uom.equals(""))
                        {
                            tv_convert_ingredient_uom.setText(ingredient_uom);
                        }
                    }
                }
            });

            et_convert_ingredient_quantity.addTextChangedListener(new TextWatcher() {
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                public void afterTextChanged(Editable s) {
                    String ingredient_name = autocompletetv_convert_ingredient.getText().toString();
                    String ingredient_uom = tv_convert_ingredient_uom.getText().toString();
                    if (!ingredient_name.equals("") && ingredient_uom.equals("")) {
                        Cursor crsr_item = dbIngredientManagement.getItem_GoodsInward(ingredient_name);
                        if (crsr_item != null && crsr_item.moveToFirst()) {
                            String item_uom_temp = crsr_item.getString(crsr_item.getColumnIndex("UOM"));
                            if (item_uom_temp != null && !item_uom_temp.equals("")) {
                                tv_convert_ingredient_uom.setText(item_uom_temp);
                            }
                        }
                    }
                }
            });
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
            Log.d("Convertingredients ",e.getMessage() );
        }

    }

    void loadIngredientTable(int menucode)
    {
        int count =0;
        try
        {
            Cursor crsr = dbIngredientManagement.getIngredientsForMenuCode(menucode,Status_Submitted);
            while (crsr != null && crsr.moveToNext()){
                float item_quantity = 0;
                int ingredientcode = 0;
                float ingredient_quantity = 0;
                String ingredientcode_str = crsr.getString(crsr.getColumnIndex("IngredientCode"));
                String ingredientname = crsr.getString(crsr.getColumnIndex("IngredientName"));
                String ingredient_qty_str = crsr.getString(crsr.getColumnIndex("IngredientQuantity"));

                String uom = crsr.getString(crsr.getColumnIndex("IngredientUOM"));
                String status = crsr.getString(crsr.getColumnIndex("Status"));

                if(ingredientcode_str !=null && !ingredientcode_str.equals(""))
                    ingredientcode = Integer.parseInt(ingredientcode_str);
                if(ingredient_qty_str !=null && !ingredient_qty_str.equals(""))
                    ingredient_quantity = Float.parseFloat(ingredient_qty_str);


                TableRow tr = new TableRow (myContext);

                TextView sn = new TextView(myContext);
                sn.setWidth(70);
                sn.setBackgroundResource(R.drawable.border_item);
                count++;
                sn.setPadding(4,0,0,0);
                sn.setTextSize(20);
                sn.setTextColor(getResources().getColor(R.color.black));
                sn.setText(String.valueOf(count));


                TextView IngredientCode  = new TextView(myContext);
                IngredientCode.setText(String.valueOf(ingredientcode));

                TextView IngredientName = new TextView(myContext);
                IngredientName.setBackgroundResource(R.drawable.border_item);
                IngredientName.setWidth(170);
                IngredientName.setTextSize(20);
                IngredientName.setPadding(4,0,0,0);
                IngredientName.setTextColor(getResources().getColor(R.color.black));
                IngredientName.setText(ingredientname);

                TextView IngredientQuantity = new TextView(myContext);
                IngredientQuantity.setBackgroundResource(R.drawable.border_item);
                IngredientQuantity.setWidth(90);
                IngredientQuantity.setTextSize(20);
                IngredientQuantity.setPadding(0,0,4,0);
                IngredientQuantity.setGravity(Gravity.RIGHT);
                IngredientQuantity.setTextColor(getResources().getColor(R.color.black));
                IngredientQuantity.setText(String.valueOf(ingredient_quantity));

                TextView UOM = new TextView(myContext);
                UOM.setBackgroundResource(R.drawable.border_item);
                UOM.setWidth(100);
                UOM.setGravity(Gravity.CENTER);
                UOM.setTextColor(getResources().getColor(R.color.black));
                UOM.setText(uom);
                UOM.setTextSize(20);

                /*int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
                ImageButton ImgDelete = new ImageButton(myContext);
                ImgDelete.setImageResource(res);*/

                Button ImgDelete = new Button(myContext);
                ImgDelete.setBackground(getResources().getDrawable(R.drawable.delete_icon));
                ImgDelete.setLayoutParams(new TableRow.LayoutParams(40, 35));
                ImgDelete.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        final View row = v;
                        AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to Delete this Ingredient")
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                        TableRow tr = (TableRow) row.getParent();
                                        TextView IngredientCode_temp = (TextView) tr.getChildAt(1);
                                        TextView IngredientName_temp = (TextView) tr.getChildAt(2);
                                        int menucode = Integer.parseInt(tv_convert_menucode.getText().toString());
                                        //int ingredientcode_temp = Integer.parseInt(IngredientCode_temp.getText().toString());
                                        String ingredientname = IngredientName_temp.getText().toString();
                                        long lResult= dbIngredientManagement.DeleteIngredients(menucode,ingredientname);
                                        if (lResult > 0) {
                                            Toast.makeText(myContext, "Ingredient Deleted Successfully", Toast.LENGTH_SHORT).show();
                                            Log.d("IngredientManagement: ", itemname_str+": Ingredient Deleted Successfully ("+ingredientname+")");
                                            ViewGroup container = ((ViewGroup) row.getParent());
                                            container.removeView(row);
                                            container.invalidate();
                                        }
                                        ClearTable(tbl_convert_ingredients);
                                        loadIngredientTable(menucode);
                                        Cursor crsr = dbIngredientManagement.getIngredientsForMenuCode(menucode);
                                        if (!(crsr != null && crsr.moveToNext())) {
                                            ClearTable(tbl_convert_ingredients);
                                        }
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
                    }  });

                TextView spc = new TextView(myContext);
                spc.setWidth(3);
                tr.addView(sn);
                tr.addView(IngredientCode);
                tr.addView(IngredientName);
                tr.addView(IngredientQuantity);
                tr.addView(UOM);
                tr.addView(spc);
                tr.addView(ImgDelete);
                tbl_convert_ingredients.addView(tr);

            } // end of while
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
            Log.d("Convertingredients ",e.getMessage() );
        }

    }

    public void AddIngredient(View v) {
        try {
            // check for items
            String itemname = tv_convert_item.getText().toString().toUpperCase();
            if (itemname.equals("")) {
                MsgBox.Show(" Insufficient Information ", " Please Select Item ");
                return;
            }


            String item_qty = et_convert_item_quantity.getText().toString();
            if (item_qty.equals("")) {
                MsgBox.Show(" Insufficient Information ", " Please Enter Item Quantity ");
                return;
            }

            // check for ingredients
            String ingredientname = autocompletetv_convert_ingredient.getText().toString().toUpperCase();
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
                    tv_convert_ingredient_uom.setText(ingredient_uom);
            }

            String ingredient_qty = et_convert_ingredient_quantity.getText().toString();
            if (ingredient_qty.equals("")) {
                MsgBox.Show(" Insufficient Information ", " Please Enter Ingredient Quantity ");
                return;
            }
            // add to tbl

            int count = tbl_convert_ingredients.getChildCount();
            String uom = tv_convert_ingredient_uom.getText().toString();
            int ingredientcode = crsr_ingredient.getInt(crsr_ingredient.getColumnIndex("MenuCode"));
            addingredienttotable(count, ingredientcode, ingredientname, Float.parseFloat(ingredient_qty), uom);
            autocompletetv_convert_ingredient.setText("");
            et_convert_ingredient_quantity.setText("");
            tv_convert_ingredient_uom.setText("");

        } catch (Exception e){
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
            Log.d("Convertingredients ",e.getMessage() );
        }
    }

    void addingredienttotable(int count, int ingredientcode, String ingredientname, float ingredient_quantity, String uom)
    {
        TableRow tr = new TableRow (myContext);

        TextView sn = new TextView(myContext);
        sn.setWidth(50);
        sn.setBackgroundResource(R.drawable.border);
        count++;
        sn.setText(String.valueOf(count));
        sn.setPadding(4,0,0,0);
        sn.setTextColor(getResources().getColor(R.color.black));
        sn.setTextSize(20);


        TextView IngredientCode  = new TextView(myContext);
        IngredientCode.setText(String.valueOf(ingredientcode));

        TextView IngredientName = new TextView(myContext);
        IngredientName.setBackgroundResource(R.drawable.border);
        IngredientName.setWidth(220);
        IngredientName.setTextColor(getResources().getColor(R.color.black));
        IngredientName.setTextSize(20);
        IngredientName.setPadding(4,0,0,0);
        IngredientName.setText(ingredientname);

        TextView IngredientQuantity = new TextView(myContext);
        IngredientQuantity.setBackgroundResource(R.drawable.border);
        IngredientQuantity.setWidth(100);
        IngredientQuantity.setTextColor(getResources().getColor(R.color.black));
        IngredientQuantity.setTextSize(20);
        IngredientQuantity.setGravity(Gravity.RIGHT);
        IngredientQuantity.setPadding(0,0,4,0);
        IngredientQuantity.setText(String.valueOf(ingredient_quantity));

        TextView UOM = new TextView(myContext);
        UOM.setBackgroundResource(R.drawable.border);
        UOM.setWidth(70);
        UOM.setGravity(Gravity.CENTER);
        UOM.setTextColor(getResources().getColor(R.color.black));
        UOM.setTextSize(20);
        UOM.setText(uom);

        /*int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
        ImageButton ImgDelete = new ImageButton(myContext);
        ImgDelete.setImageResource(res);*/

        Button ImgDelete = new Button(myContext);
        ImgDelete.setBackground(getResources().getDrawable(R.drawable.delete_icon_lightyellow));
        ImgDelete.setLayoutParams(new TableRow.LayoutParams(40, 30));
        ImgDelete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final View row = v;
                AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to Delete this Ingredient")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                TableRow tr = (TableRow) row.getParent();
                                TextView IngredientCode_temp = (TextView) tr.getChildAt(1);
                                TextView IngredientName_temp = (TextView) tr.getChildAt(2);
                                int menucode = Integer.parseInt(tv_convert_menucode.getText().toString());
                                //int ingredientcode_temp = Integer.parseInt(IngredientCode_temp.getText().toString());
                                String ingredientname = IngredientName_temp.getText().toString();
                                long lResult = dbIngredientManagement.DeleteIngredients(menucode,ingredientname);
                                if (lResult > 0) {
                                    Toast.makeText(myContext, "Ingredient Deleted Successfully", Toast.LENGTH_SHORT).show();
                                    Log.d("IngredientManagement: ", tv_convert_item.getText().toString()+": Ingredient Deleted Successfully ("+ingredientname+")");
                                    ViewGroup container = ((ViewGroup) row.getParent());
                                    container.removeView(row);
                                    container.invalidate();
                                }
                                ClearTable(tbl_convert_ingredients);
                                loadIngredientTable(menucode);
                                Cursor crsr = dbIngredientManagement.getIngredientsForMenuCode(menucode);
                                if (!(crsr != null && crsr.moveToNext())) {
                                   /* ClearTable(tbl_displaysubmitteditems);
                                    DisplaySubmittedItems();
                                    loadAutoCompleteSubmitItems();
                                    reset();*/
                                }
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
            }  });

        TextView spc = new TextView(myContext);
        spc.setWidth(3);
        tr.addView(sn);
        tr.addView(IngredientCode);
        tr.addView(IngredientName);
        tr.addView(IngredientQuantity);
        tr.addView(UOM);
        tr.addView(spc);
        tr.addView(ImgDelete);
        tbl_convert_ingredients.addView(tr);
    }


    public  void ClearTable( TableLayout tbl)
    {
        if(tbl == null)
            return;

        for (int i = tbl.getChildCount(); i >0;  i--) {
            View Row = tbl.getChildAt(i-1);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
            ViewGroup container = ((ViewGroup) Row.getParent());
            container.removeView(Row);
            container.invalidate();
        }
    }
    void SetItemDetails()
    {
        Cursor crsr = dbIngredientManagement.getIngredientsForItemName(itemname_str);
        if (crsr != null && crsr.moveToFirst()) { // item present in item database
            int menucode = crsr.getInt(crsr.getColumnIndex("MenuCode"));
            String item_uom = crsr.getString(crsr.getColumnIndex("UOM"));
            String item_quantity = crsr.getString(crsr.getColumnIndex("Quantity"));
            if (item_uom != null && !item_uom.equals("")) {
                tv_convert_item_uom.setText(item_uom);
            }

            tv_convert_menucode.setText(String.valueOf(menucode));
            tv_convert_item.setText(itemname_str);
            et_convert_item_quantity.setText(item_quantity);
            loadIngredientTable(menucode);

            /**** chnaged here for list ****/ // TODO: Chnaged here for listview
            ingredientlist.add(new Ingredient(ingredientlist.size()+1, menucode, itemname_str, Double.parseDouble(item_quantity), item_uom));
        }
        ingredientListAdapter.notifyDataSetChanged(ingredientlist);
    }

    void loadAutoCompleteIngredients()
    {
        Cursor ingredient_crsr  = dbIngredientManagement.getAllItem_GoodsInward();
        ArrayList<String> ingredientlist = new ArrayList<String>();
        while (ingredient_crsr !=null && ingredient_crsr.moveToNext()){
            String item = ingredient_crsr.getString(ingredient_crsr.getColumnIndex("ItemName"));
            if(item !=null)
                ingredientlist.add(item);
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,ingredientlist);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        autocompletetv_convert_ingredient.setAdapter(dataAdapter);
        //autocompletetv_convert_ingredient.setDropDownBackgroundResource(R.color.black);
    }
    
    void InitialiseViewvariables()
    {
        tv_convert_menucode = (TextView) findViewById(R.id.tv_convert_menucode);
        tv_convert_item = (TextView) findViewById(R.id.tv_convert_item);
        et_convert_item_quantity = (EditText) findViewById(R.id.et_convert_item_quantity);
        tv_convert_item_uom = (TextView)findViewById(R.id.tv_convert_item_uom);

        autocompletetv_convert_ingredient = (AutoCompleteTextView) findViewById(R.id.autocompletetv_convert_ingredient);
        et_convert_ingredient_quantity = (EditText)findViewById(R.id.et_convert_ingredient_quantity);
        tv_convert_ingredient_uom = (TextView)findViewById(R.id.tv_convert_ingredient_uom);

        btn_convert_add = (Button)findViewById(R.id.btn_convert_add);
        btn_convert_Submit = (Button)findViewById(R.id.btn_convert_Submit);
        btn_convert_convertIngredient = (Button)findViewById(R.id.btn_convert_convertIngredient);
        btn_convert_close = (Button)findViewById(R.id.btn_convert_close);
//        tbl_convert_ingredients = (TableLayout) findViewById(R.id.tbl_convert_ingredients);

        lv_convert_ingredients = (ListView) findViewById(R.id.lv_convert_ingredients);
        ingredientlist = new ArrayList<>();
        ingredientListAdapter = new IngredientListAdapter(this, ingredientlist);

        btn_convert_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddIngredient(v);
            }
        });
        btn_convert_Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Submit(v);
                }
            });
        btn_convert_convertIngredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConvertIngredients(v);
                }
            });
        btn_convert_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Close(v);
                }
            });
    }

    public void Submit(View v)
    {
        int success =0;
        int count = tbl_convert_ingredients.getChildCount();
        int menucode = 0;
        if(count <1)
        {
            MsgBox.Show(" Insufficient Information ", " Please Add Ingredient ");
            return;
        }
        // check for items

        if(itemname_str.equals("")){
            MsgBox.Show(" Insufficient Information ", " Please Select Item ");
            return;
        }

        Cursor crsr_item = dbIngredientManagement.getbyItemName(itemname_str) ;
        if(!(crsr_item !=null && crsr_item.moveToFirst()))        {
            MsgBox.Show(" Invalid Item", " Please Select Item from the List ");
            return;
        }
        else
        {
            menucode = crsr_item.getInt(crsr_item.getColumnIndex("MenuCode"));
        }

        String item_qty = et_convert_item_quantity.getText().toString();
        if(item_qty.equals(""))
        {
            MsgBox.Show(" Insufficient Information ", " Please Enter Item Quantity ");
            return;
        }

        // check for ingredients
        String ingredientname = autocompletetv_convert_ingredient.getText().toString().toUpperCase();
        String ingredient_qty = et_convert_ingredient_quantity.getText().toString();

        if(!(ingredientname.equals("") &&(ingredient_qty.equals("") )))
        {
            MsgBox.Show("Error ", " Ingredient and Quantity cannot have values while submitting ");
            return;
        }
        if (tbl_convert_ingredients.getChildCount() < 1)
        {
            MsgBox.Show("Error ", " Please Add Ingredient to Table ");
            return;
        }


        try
        {

            int del = dbIngredientManagement.DeleteSavedIngredients(menucode,Status_Submitted);
            Log.d("SubmitIngredients", "Submitted Rows deleted :"+del);
            for(int i = 0; i<count; i++)
            {
                ItemIngredients ingredient = new ItemIngredients();
                TableRow RowIngredient = (TableRow)tbl_convert_ingredients.getChildAt(i);

                // MenuCode
                ingredient.setMenucode(menucode);
                Log.d("SubmitIngredients", "MenuCode :" + menucode);
                // Item Name
                ingredient.setItemname(itemname_str);
                Log.d("SubmitIngredients", "Item Name :" + itemname_str);
                // ItemQuantity
                ingredient.setItemquantity(Float.parseFloat(item_qty));
                Log.d("SubmitIngredients", "Item Quantity :" + item_qty);

                String item_uom = tv_convert_item_uom.getText().toString();
                ingredient.setUom(item_uom);
                Log.d("Convert_Submit", "Item UOM :" + item_uom);

                if (RowIngredient.getChildAt(1) != null) {
                    TextView IngredientCode = (TextView) RowIngredient.getChildAt(1);
                    ingredient.setIngredientcode(Integer.parseInt(IngredientCode.getText().toString()));
                    Log.d("Convert_Submit", "Ingredient Code :" + IngredientCode.getText().toString());
                }

                if (RowIngredient.getChildAt(2) != null) {
                    TextView IngredientName = (TextView) RowIngredient.getChildAt(2);
                    ingredient.setIngredientname(IngredientName.getText().toString());
                    Log.d("Convert_Submit", "Ingredient Name :" + IngredientName.getText().toString());
                }

                if (RowIngredient.getChildAt(3) != null) {
                    TextView IngredientQuatity = (TextView) RowIngredient.getChildAt(3);
                    ingredient.setIngredientquantity(Float.parseFloat(IngredientQuatity.getText().toString()));
                    Log.d("Convert_Submit", "Ingredient Quantity :" + IngredientQuatity.getText().toString());
                }

                if (RowIngredient.getChildAt(4) != null) {
                    TextView UOM = (TextView) RowIngredient.getChildAt(4);
                    ingredient.setIngredientUOM(UOM.getText().toString());
                    Log.d("Convert_Submit", "Ingredient UOM :" + UOM.getText().toString());
                }

                ingredient.setStatus(Status_Submitted);
                Log.d("Convert_Submit", "Status :" + Status_Submitted);

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

            ClearTable(tbl_convert_ingredients);
            loadIngredientTable(menucode);
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
            Log.d("Convertingredients ",e.getMessage() );
        }
    }

    public void ConvertIngredients (View v) {

        String itemname = tv_convert_item.getText().toString();
        int menucode = Integer.parseInt(tv_convert_menucode.getText().toString());
        String item_quantity = et_convert_item_quantity.getText().toString();
        double rate =0;
        float item_qty_f =0;
        float item_qty_prev_f =0;
        int count = tbl_convert_ingredients.getChildCount();
        if(count <0)
        {
            MsgBox.Show("Error ", "Please Add Ingredient to Table");
            return;
        }
        if (item_quantity.equals(""))
        {
            MsgBox.Show("Error ", "Please fill Item Quantity");
            return;
        }
        else
        {
            item_qty_f = Float.parseFloat(item_quantity);
        }
       if(IngredientsInQuantity() > 0)
       {
           Cursor item_crsr  = dbIngredientManagement.getItem(menucode);

           if(item_crsr !=null && item_crsr.moveToFirst())
           {
               String item_prev_qty = item_crsr.getString(item_crsr.getColumnIndex("Quantity"));
               if(item_prev_qty != null && !item_prev_qty.equals(""))
               {
                   item_qty_prev_f = Float.parseFloat(item_prev_qty);
               }
               if(item_crsr.getDouble(item_crsr.getColumnIndex("DineInPrice1")) > 0)
                    rate = item_crsr.getDouble(item_crsr.getColumnIndex("DineInPrice1"));
               else if(item_crsr.getDouble(item_crsr.getColumnIndex("DineInPrice2")) > 0)
                   rate = item_crsr.getDouble(item_crsr.getColumnIndex("DineInPrice2"));
               else if(item_crsr.getDouble(item_crsr.getColumnIndex("DineInPrice3")) > 0)
                   rate = item_crsr.getDouble(item_crsr.getColumnIndex("DineInPrice3"));

           }
           long item_update = dbIngredientManagement.updateItemQuantity(menucode,item_qty_f+item_qty_prev_f);
           if(item_update > 0)
           {

               Log.d("ConvertingIngredients", itemname+" updated to "+(item_qty_f+item_qty_prev_f));
               // updating in table outwardStock

               Cursor date_cursor = dbIngredientManagement.getCurrentDate();
               String currentdate = "";
               if(date_cursor.moveToNext())
                   currentdate = date_cursor.getString(date_cursor.getColumnIndex("BusinessDate"));
               StockOutwardMaintain stock_outward = new StockOutwardMaintain(myContext, dbIngredientManagement);
               double OpeningQuantity =0;
               double ClosingQuantity =0;
               Cursor outward_item_stock = dbIngredientManagement.getOutwardStockItem(currentdate,menucode);
               if(outward_item_stock!=null && outward_item_stock.moveToNext()){
                   OpeningQuantity = outward_item_stock.getDouble(outward_item_stock.getColumnIndex("OpeningStock"));
                   OpeningQuantity += Double.parseDouble(item_quantity);

                   ClosingQuantity = outward_item_stock.getDouble(outward_item_stock.getColumnIndex("ClosingStock"));
                   ClosingQuantity += Double.parseDouble(item_quantity);
               }
               else
               {
                   OpeningQuantity = (Double.parseDouble(String.valueOf(item_qty_prev_f)) + Double.parseDouble(item_quantity));
                   ClosingQuantity = OpeningQuantity;
               }
               stock_outward.updateOpeningStock_Outward( currentdate, menucode,itemname,OpeningQuantity, rate );
               stock_outward.updateClosingStock_Outward( currentdate, menucode,itemname,ClosingQuantity);


               // updating ingredients
               for(int i =0; i< count; i++)
               {
                   TableRow tr = (TableRow) tbl_convert_ingredients.getChildAt(i);

                   TextView IngredientName = (TextView) tr.getChildAt(2);
                   TextView IngredientQuantity = (TextView) tr.getChildAt(3);

                   String ingredientname = IngredientName.getText().toString();
                   float ingredientquantity = Float.parseFloat(IngredientQuantity.getText().toString());
                   float ingredient_qty_prev = 0;
                   Cursor ingredient_crsr = dbIngredientManagement.getItem_GoodsInward(ingredientname);
                   if (ingredient_crsr!= null && ingredient_crsr.moveToFirst()) {
                       String ingredient_qty_prev_str = ingredient_crsr.getString(ingredient_crsr.getColumnIndex("Quantity"));
                       if (ingredient_qty_prev_str != null && !ingredient_qty_prev_str.equals("")) {
                           ingredient_qty_prev = Float.parseFloat(ingredient_qty_prev_str);
                       }

                   }
                   ingredientquantity = ingredient_qty_prev-ingredientquantity;
                   int ingredient_updated = dbIngredientManagement.updateIngredientQuantityInGoodsInward(ingredientname, ingredientquantity);
                   if(ingredient_updated > 0)
                   {
                       // update inward stock
                       Cursor inward_crsr = dbIngredientManagement.getItem_GoodsInward(ingredientname);
                       if(ingredient_crsr!=null && inward_crsr.moveToFirst())
                       {
                           int menuCode = ingredient_crsr.getInt(inward_crsr.getColumnIndex("MenuCode"));
                           StockInwardMaintain stock_inward = new StockInwardMaintain(myContext, dbIngredientManagement);
                           stock_inward.updateClosingStock_Inward(currentdate,menuCode,ingredientname,ingredientquantity);
                       }
                   }
                   Log.d("ConvertingIngredients", ingredientname+" updated to "+ingredientquantity);

               }// end for
               Toast.makeText(myContext, itemname+ " and its ItemIngredients Updated Successfully ", Toast.LENGTH_SHORT).show();

           }
       }
    }

    int  IngredientsInQuantity()
    {
        int properQuantity = 1;
        int count = tbl_convert_ingredients.getChildCount();
        for (int i =0;(properQuantity == 1)&&(i <count);i++)
        {
            TableRow tr = (TableRow) tbl_convert_ingredients.getChildAt(i);
            TextView IngredientName = (TextView) tr.getChildAt(2);
            TextView IngredientQuantity = (TextView) tr.getChildAt(3);
            TextView IngredientUOM = (TextView) tr.getChildAt(4);
            String unit = IngredientUOM.getText().toString();
            float ingredient_qty_prev = 0;

            String ingredientname = IngredientName.getText().toString();
            float ingredient_qty = Float.parseFloat(IngredientQuantity.getText().toString());
            Cursor ingredient_crsr = dbIngredientManagement.getItem_GoodsInward(ingredientname);
            if (ingredient_crsr!= null && ingredient_crsr.moveToFirst()) {
                String ingredient_qty_prev_str = ingredient_crsr.getString(ingredient_crsr.getColumnIndex("Quantity"));
                if (ingredient_qty_prev_str != null && !ingredient_qty_prev_str.equals("")) {
                    ingredient_qty_prev = Float.parseFloat(ingredient_qty_prev_str);

                }
            }
            if(ingredient_qty > ingredient_qty_prev) // insuffient ingredient in Stock
            {
                properQuantity= -1;
                Toast.makeText(myContext, ingredientname+" is less in quantity.\nRequired quantity is "+ingredient_qty
                        +" "+unit+" and available quantity is "+ingredient_qty_prev+" "+unit, Toast.LENGTH_LONG).show();
            }
        }
        return  properQuantity;
    }
    public void Close(View v)
    {
        //dbIngredientManagement.CloseDatabase();
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

/*@Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }*/
    
}
