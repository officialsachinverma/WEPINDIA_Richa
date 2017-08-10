/****************************************************************************
 * Project Name		:	VAJRA
 *
 * File Name		:	HeaderFooterActivity
 *
 * Purpose			:	Represents bill header and footer configuration activity,
 * 						takes care of all UI back end operations in this activity,
 * 						such as event handling data read from or display in views.
 *
 * DateOfCreation	:	07-November-2012
 *
 * Author			:	Balasubramanya Bharadwaj B S
 *
 ****************************************************************************/
package com.wepindia.pos.GST;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class Ammend_b2cs_gstr1 extends Activity{

    Context myContext;
    DatabaseHandler dbAmmend_b2cs;
    MessageDialog MsgBox;// = new MessageDialog(HeaderFooterActivity.this);
    EditText et_month_ori, et_hsn_ori,et_aggregateValue_rev,et_hsn_rev,et_igstrate,et_sgstrate,et_cgstrate;
    Spinner spnr_supplytype_rev,spnr_supplytype_ori,spnr_proass;
    AutoCompleteTextView actv_pos_ori, actv_pos_rev;
    Button btnAdd, btnSave, btnClear,btnClose;
    TableLayout tbl_data ;
    TableRow rowItems;
    private String TAG = Ammend_b2cs_gstr1.class.getSimpleName();
    TextView tvTitleDate;// = (TextView) findViewById(R.id.tvTitleBarDate);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ammend_b2cs_gstr1);
        myContext = this;
         tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);

        dbAmmend_b2cs= new DatabaseHandler(Ammend_b2cs_gstr1.this);
        MsgBox = new MessageDialog(myContext);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN );

        try{
            dbAmmend_b2cs.CreateDatabase();
            dbAmmend_b2cs.OpenDatabase();
            InitializeViews();
            Reset();
        }
        catch(Exception exp){
            exp.printStackTrace();
            MsgBox.Show("Exception", exp.getMessage());
        }
    }

    void InitializeViews()
    {
        et_month_ori = (EditText)findViewById(R.id.et_month_ori);
        et_hsn_ori= (EditText)findViewById(R.id.et_hsn_ori);
        et_aggregateValue_rev = (EditText)findViewById(R.id.et_aggregateValue_rev);
        et_hsn_rev = (EditText)findViewById(R.id.et_hsn_rev);
        et_igstrate = (EditText)findViewById(R.id.et_igstrate);
        et_sgstrate = (EditText)findViewById(R.id.et_sgstrate);
        et_cgstrate = (EditText)findViewById(R.id.et_cgstrate);

        actv_pos_ori = (AutoCompleteTextView)findViewById(R.id.actv_pos_ori);
        actv_pos_rev = (AutoCompleteTextView) findViewById(R.id.actv_pos_rev);

        spnr_supplytype_ori = (Spinner)findViewById(R.id.spnr_supplytype_ori);
        spnr_supplytype_rev = (Spinner) findViewById(R.id.spnr_supplytype_rev);
        spnr_proass = (Spinner) findViewById(R.id.spnr_proass) ;

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSave = (Button) findViewById(R.id.btnSave);
        btnClear = (Button) findViewById(R.id.btnClear);
        btnClose = (Button) findViewById(R.id.btnClose);

        tbl_data = (TableLayout) findViewById(R.id.tbl_data);

    }

    void Reset()
    {
        et_month_ori.setText("04");
        et_hsn_ori.setText("h5");
        et_aggregateValue_rev.setText("900");
        et_hsn_rev.setText("h5");
        et_igstrate.setText("18");
        et_sgstrate.setText("9");
        et_cgstrate.setText("9");

        actv_pos_ori.setText("23");
        actv_pos_rev.setText("23");

        spnr_supplytype_ori.setSelection(0);
        spnr_supplytype_rev.setSelection(0);

        btnSave.setEnabled(false);

    }


    public void Add(View v)
    {
        String month = et_month_ori.getText().toString();
        String hsn = et_hsn_ori.getText().toString();
        String pos_ori = actv_pos_ori.getText().toString();
        String supply_ori = spnr_supplytype_ori.getSelectedItem().toString();

        String aggval = et_aggregateValue_rev.getText().toString();
        String hsn_rev = et_hsn_rev.getText().toString();
        String pos_rev = actv_pos_rev.getText().toString();
        String supply_rev = spnr_supplytype_rev.getSelectedItem().toString();

        String igstrate = et_igstrate.getText().toString();
        String cgstrate = et_cgstrate.getText().toString();
        String sgstrate = et_sgstrate.getText().toString();
        int count =1;

        String proass = spnr_proass.getSelectedItem().toString();

        if (month.equals("") || (pos_ori.equals("")) || (pos_rev.equals(""))||(aggval.equals(""))||(igstrate.equals(""))|| (sgstrate.equals(""))
                ||(cgstrate.equals("")))
        {
            MsgBox.setTitle(" Error ")
                    .setMessage(" Please fill all details ")
                    .show();
        }else if (proass.equals("")){
            MsgBox.setTitle(" Error ")
                    .setMessage(" Please Select Provissional Asses (Y/N) ")
                    .show();
        }else
        {
            rowItems = new TableRow(myContext);
            rowItems.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            //rowItems.setBackgroundResource(R.drawable.border_item);


            TextView Sno  = new TextView(myContext);
            Sno.setText(String.valueOf(count));
            Sno.setWidth(50);
            Sno.setBackgroundResource(R.drawable.border_item);
            count++;
            rowItems.addView(Sno);


            TextView tvmonth = new TextView(myContext);
            tvmonth.setText(month);
            tvmonth.setWidth(80);
            tvmonth.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvmonth);

            TextView tvsupply_ori = new TextView(myContext);
            tvsupply_ori.setText(supply_ori);
            tvsupply_ori.setWidth(85);
            tvsupply_ori.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvsupply_ori);

            TextView tvhsn_ori = new TextView(myContext);
            tvhsn_ori.setText(hsn);
            tvhsn_ori.setWidth(80);
            tvhsn_ori.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvhsn_ori);

            TextView tvpos_ori = new TextView(myContext);
            tvpos_ori.setText(pos_ori);
            tvpos_ori.setWidth(80);
            tvpos_ori.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvpos_ori);

            TextView tvsupply_rev = new TextView(myContext);
            tvsupply_rev.setText(supply_rev);
            tvsupply_rev.setBackgroundResource(R.drawable.border_item);
            tvsupply_rev.setWidth(80);
            rowItems.addView(tvsupply_rev);

            TextView tvhsn_rev = new TextView(myContext);
            tvhsn_rev.setBackgroundResource(R.drawable.border_item);
            tvhsn_rev.setText(hsn);
            tvhsn_rev.setWidth(90);
            rowItems.addView(tvhsn_rev);

            TextView tvpos_rev = new TextView(myContext);
            tvpos_rev.setText(pos_rev);
            tvpos_rev.setBackgroundResource(R.drawable.border_item);
            tvpos_rev.setWidth(80);
            rowItems.addView(tvpos_rev);

            TextView tvTaxVal = new TextView(myContext);
            float taxable = Float.parseFloat(aggval);
            tvTaxVal.setText(String.format("%.2f",taxable));
            tvTaxVal.setWidth(80);
            tvTaxVal.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvTaxVal);

            TextView tvIgstRate = new TextView(myContext);
            float irate = Float.parseFloat(igstrate);
            tvIgstRate.setBackgroundResource(R.drawable.border_item);
            tvIgstRate.setText(String.format("%.2f",irate));
            tvIgstRate.setWidth(80);
            rowItems.addView(tvIgstRate);

            TextView tvIgstAmt = new TextView(myContext);
            float iamount =0;
            if (!(pos_ori.equalsIgnoreCase(pos_rev)))
                iamount = taxable*(irate/100);

            tvIgstAmt.setText(String.format("%.2f",iamount));
            tvIgstAmt.setWidth(80);
            tvIgstAmt.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvIgstAmt);

            TextView tvCgstRate = new TextView(myContext);
            float crate = Float.parseFloat(cgstrate);
            tvCgstRate.setText(String.format("%.2f",crate));
            tvCgstRate.setWidth(80);
            tvCgstRate.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvCgstRate);

            TextView tvCgstAmt = new TextView(myContext);
            float camount =0;
            if (pos_ori.equalsIgnoreCase(pos_rev))
                camount = taxable*(crate/100);

            tvCgstAmt.setText(String.format("%.2f",camount));
            tvCgstAmt.setWidth(80);
            tvCgstAmt.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvCgstAmt);


            TextView tvSgstRate = new TextView(myContext);
            float srate = Float.parseFloat(sgstrate);
            tvSgstRate.setText(String.format("%.2f",srate));
            tvSgstRate.setWidth(80);
            tvSgstRate.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvSgstRate);

            TextView tvSgstAmt = new TextView(myContext);
            float samount =0;
            if (pos_ori.equalsIgnoreCase(pos_rev))
                samount = taxable*(srate/100);

            tvSgstAmt.setText(String.format("%.2f",samount));
            tvSgstAmt.setWidth(80);
            tvSgstAmt.setBackgroundResource(R.drawable.border_item);
            rowItems.addView(tvSgstAmt);

            TextView pro = new TextView(myContext);
            pro.setWidth(40);
            pro.setBackgroundResource(R.drawable.border_item);
            pro.setText(proass);
            rowItems.addView(pro);

            int res = getResources().getIdentifier("delete", "drawable", this.getPackageName());
            ImageButton btnItemDelete = new ImageButton(myContext);
            btnItemDelete.setImageResource(res);
            btnItemDelete.setBackgroundResource(R.drawable.border_item);
            btnItemDelete.setLayoutParams(new TableRow.LayoutParams(65, 30));
            btnItemDelete.setOnClickListener(new View.OnClickListener() {
                public void onClick(final View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(myContext)
                            .setTitle("Delete")
                            .setMessage("Are you sure you want to Delete this Item")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    View row = (View) v.getParent();
                                    ViewGroup container = ((ViewGroup) row.getParent());
                                    container.removeView(row);
                                    container.invalidate();

                                }
                            })
                            .setNegativeButton(R.string.cancel, null);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            rowItems.addView(btnItemDelete);

            tbl_data.addView(rowItems);

            btnSave.setEnabled(true);
        }

    }

    public void Clear(View v) {
        for (int i = 0; i < tbl_data.getChildCount(); i++) {
            View Row = tbl_data.getChildAt(i);
            if (Row instanceof TableRow) {
                ((TableRow) Row).removeAllViews();
            }
        }
        Reset();
    }

    public void Save(View V)
    {
        int count = tbl_data.getChildCount();
        int inserted =0;
        if (count<1)
        {
            MsgBox.setMessage("Please Add the Entry")
                    .setNeutralButton("OK",null)
                    .show();
            return;
        }

        for (int i=0; i<count; i++)
        {
            TableRow row = (TableRow) tbl_data.getChildAt(i);
            if (row.getChildAt(0)!= null)
            {
                TextView tvmonth = (TextView) row.getChildAt(1);
                TextView tvsupply_ori = (TextView) row.getChildAt(2);
                TextView tvhsn = (TextView) row.getChildAt(3);
                TextView tvpos_ori = (TextView) row.getChildAt(4);
                TextView tvsupply_rev = (TextView) row.getChildAt(5);
                TextView tvhsn_rev = (TextView) row.getChildAt(6);
                TextView tvpos_rev = (TextView) row.getChildAt(7);
                TextView tvaggval = (TextView) row.getChildAt(8);
                TextView tvigstrate = (TextView) row.getChildAt(9);
                TextView tvcgstrate = (TextView) row.getChildAt(11);
                TextView tvsgstrate = (TextView) row.getChildAt(13);
                TextView tvigstamt = (TextView) row.getChildAt(10);
                TextView tvcgstamt = (TextView) row.getChildAt(12);
                TextView tvsgstamt = (TextView) row.getChildAt(14);
                TextView tvproass = (TextView) row.getChildAt(15);

                String month = tvmonth.getText().toString();
                String supply_ori = tvsupply_ori.getText().toString();
                String hsn = tvhsn.getText().toString();
                String pos_ori = tvpos_ori.getText().toString();
                String supply_rev = tvsupply_rev.getText().toString();
                String hsn_rev = tvhsn_rev.getText().toString();
                String pos_rev = tvpos_rev.getText().toString();
                String aggval = tvaggval.getText().toString();
                String igstrate = tvigstrate.getText().toString();
                String cgstrate = tvcgstrate.getText().toString();
                String sgstrate = tvsgstrate.getText().toString();
                String igstamt = tvigstamt.getText().toString();
                String cgstamt = tvcgstamt.getText().toString();
                String sgstamt = tvsgstamt.getText().toString();
                String proass = tvproass.getText().toString();
                Calendar c =  Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String date = sdf.format(c.getTime());

                long l = dbAmmend_b2cs.addB2CsAmmend(month, supply_ori , hsn, pos_ori, supply_rev ,hsn_rev,pos_rev,aggval,
                        igstrate,cgstrate, sgstrate , igstamt, cgstamt ,sgstamt ,proass,"B2CS",date);
                if (l>0)
                    inserted++;
            }

        }
        if (inserted>0)
        {
            Toast.makeText(myContext, inserted+" Entries added.", Toast.LENGTH_SHORT).show();
        }

    }
    public void Close(View v){
        dbAmmend_b2cs.CloseDatabase();
        this.finish();
    }
}
