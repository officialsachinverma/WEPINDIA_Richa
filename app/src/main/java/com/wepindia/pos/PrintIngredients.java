package com.wepindia.pos;

import android.content.Context;

import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wepindia.pos.utils.ActionBarUtils;
import com.wepindia.printers.WepPrinterBaseActivity;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.print.PrintIngredientsModel;
import com.wepindia.pos.GenericClasses.MessageDialog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by RichaA on 3/7/2017.
 */
public class PrintIngredients extends WepPrinterBaseActivity {

    DatabaseHandler dbIngredientManagement;
    TableLayout tbl_displaysubmitteditems;
    Button btn_Print;
    MessageDialog MsgBox;
    String strUserName = "", strUserId = "";
    String Status_Submitted = "1";
    public boolean isPrinterAvailable = false;

    Context myContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_print_ingredients);

        dbIngredientManagement = new DatabaseHandler(PrintIngredients.this);
        myContext = this;
        MsgBox = new MessageDialog(myContext);

        try {
            strUserName = getIntent().getStringExtra("USER_NAME");
            strUserId = getIntent().getStringExtra("USER_ID");
            Date d = new Date();
            CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());

            btn_Print = (Button)findViewById(R.id.btn_Print);
            btn_Print.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Print(v);
                }
            });
            tbl_displaysubmitteditems = (TableLayout) findViewById(R.id.tbl_displaysubmitteditems);
            dbIngredientManagement.CreateDatabase();
            dbIngredientManagement.OpenDatabase();
            load();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            MsgBox.Show(" Error ", e.getMessage());
            Log.d("Convertingredients ", e.getMessage());
        }
    }


    void load()
    {
        Cursor item_crsr  = dbIngredientManagement.getItemsForSubmittedIngredients(Status_Submitted);
        ArrayList<String> itemlist_submit = new ArrayList<String>();
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

                CheckBox sn = new CheckBox(myContext);
                sn.setWidth(80);
               // sn.setBackgroundResource(R.drawable.border_itemdatabase);
                count++;
                sn.setPadding(10,0,0,0);
                sn.setTextSize(20);
                sn.setText(String.valueOf(count)+" - ");



                TextView ItemName = new TextView(myContext);
                //ItemName.setBackgroundResource(R.drawable.border_itemdatabase);
                //ItemName.setWidth(320);
                ItemName.setPadding(10,0,0,0);
                ItemName.setTextSize(21);
                ItemName.setText(item);
                ItemName.setVisibility(View.VISIBLE);



                TextView spc = new TextView(myContext);
                spc.setWidth(05);


                row.addView(sn);
                row.addView(ItemName);
                //row.addView(spc);
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
    public void Close(View v)
    {
        //dbIngredientManagement.CloseDatabase();
        this.finish();
    }

    public void onPrinterAvailable() {
        isPrinterAvailable = true;

    }


    public void Print(View v) {
        if (isPrinterAvailable) {
            if (tbl_displaysubmitteditems.getChildCount() < 1) {
                MsgBox.Show("Warning", "Insert item before Print Bill");
                return;
            } else {
                int checked = 0;
                ArrayList<PrintIngredientsModel> billItems = new ArrayList<PrintIngredientsModel>();
                for (int i = 0; i < tbl_displaysubmitteditems.getChildCount(); i++) {

                    TableRow row = (TableRow) tbl_displaysubmitteditems.getChildAt(i);
                    CheckBox ch = (CheckBox) row.getChildAt(0);
                    TextView itemname = (TextView) row.getChildAt(1);
                    String itemname_str = itemname.getText().toString();

                    if (ch.isChecked()) {
                        checked =1;
                        PrintIngredientsModel item = new PrintIngredientsModel();
                        Cursor crsr = dbIngredientManagement.getIngredientsForItemName(itemname_str);
                        if (crsr != null && crsr.moveToFirst()) { // item present in item database
                            int menucode = crsr.getInt(crsr.getColumnIndex("MenuCode"));
                            String item_uom = crsr.getString(crsr.getColumnIndex("UOM"));
                            double item_quantity_item = crsr.getDouble(crsr.getColumnIndex("Quantity"));
                            item.setItemId(String.valueOf(menucode));
                            item.setItemName(itemname_str);
                            item.setQty(Double.parseDouble(String.format("%2f",item_quantity_item)));
                            if (item_uom != null && !item_uom.equals("")) {
                                item.setUom(item_uom);
                            }
                            billItems.add(item);
                            // adding ingredients
                            crsr = dbIngredientManagement.getIngredientsForMenuCode(menucode);
                            while (crsr != null && crsr.moveToNext()) {
                                float item_quantity = 0;
                                double ingredient_quantity = 0;
                                //String ingredientcode_str = crsr.getString(crsr.getColumnIndex("IngredientCode"));
                                String ingredientname = crsr.getString(crsr.getColumnIndex("IngredientName"));
                                String ingredient_qty_str = crsr.getString(crsr.getColumnIndex("IngredientQuantity"));
                                String uom = crsr.getString(crsr.getColumnIndex("IngredientUOM"));
                                if (ingredient_qty_str != null && !ingredient_qty_str.equals(""))
                                    ingredient_quantity = Double.parseDouble(ingredient_qty_str);
                                PrintIngredientsModel ingredient = new PrintIngredientsModel();
                                ingredient.setItemName(ingredientname);
                                ingredient.setQty(Double.parseDouble(String.format("%2f",ingredient_quantity)));
                                ingredient.setUom(uom);

                                billItems.add(ingredient);

                            }
                        }
                    } // end of if(checked)
                } // end of for
                if(checked==0)
                {
                    MsgBox.Show("Message"," Please select atleast one item to print");
                    return;
                }


                //startActivity(intent);
                if (isPrinterAvailable) {
                    printHeydeyIngredients(billItems, "ItemIngredients");
                }

            }


        } else {
            askForConfig();
            Toast.makeText(PrintIngredients.this, "Printer not configured", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }
    @Override
    public void onConfigurationRequired() {

    }
}
