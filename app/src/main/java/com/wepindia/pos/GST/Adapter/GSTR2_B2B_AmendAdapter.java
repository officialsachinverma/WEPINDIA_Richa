package com.wepindia.pos.GST.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.GSTR2_B2B_Amend;
import com.wepindia.pos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by RichaA on 4/10/2017.
 */

public class GSTR2_B2B_AmendAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<GSTR2_B2B_Amend> amendArrayList;
    private DatabaseHandler handler;
    private int GSTR;
    private String gstin_ori, invno_ori, invdate_ori, supplierType_gstr2;


    public GSTR2_B2B_AmendAdapter(Activity activity, ArrayList<GSTR2_B2B_Amend> amendArrayList, DatabaseHandler handler,
                                  int GSTR, String gstin_ori, String invno_ori, String invdate_ori,String supplietType_gstr2){
        this.activity = activity;
        this. amendArrayList = amendArrayList;
        this.handler = handler;
        this.GSTR = GSTR;
        this.gstin_ori = gstin_ori;
        this.invno_ori = invno_ori;
        this.invdate_ori = invdate_ori;
        this.supplierType_gstr2 = supplietType_gstr2;
    }


    public int getCount() {
        return  amendArrayList.size();
    }

    public Object getItem(int i) {
        return  amendArrayList.get(i);
    }
    public GSTR2_B2B_Amend getItems(int i) {
        return  amendArrayList.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public void notifyNewDataAdded(ArrayList<GSTR2_B2B_Amend> list) {
        this. amendArrayList = list;
        notifyDataSetChanged();
    }

    public ArrayList<GSTR2_B2B_Amend> getItems() {
        return  amendArrayList;
    }

    public void notifyDataSetChanged(ArrayList<GSTR2_B2B_Amend> allItem) {
        this. amendArrayList = allItem;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView SNo;
        TextView gstin_ori;
        TextView invno_ori;
        TextView invdate_ori;
        TextView gstin_rev;
        TextView invno_rev;
        TextView invdate_rev;
        TextView IGSTRate;
        TextView IGSTAmount;
        TextView CGSTRate;
        TextView CGSTAmount;
        TextView SGSTRate;
        TextView SGSTAmount;
        TextView cessAmount;
        TextView hsnCode;
        TextView tvg_s;
        TextView taxableValue;
        TextView value;
        TextView pos;
        ImageView btndel;
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {
        GSTR2_B2B_AmendAdapter.ViewHolder viewHolder = null;
        int count =1;
        if(true)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.ammend_gstr2_b2b_view,null);
            viewHolder = new GSTR2_B2B_AmendAdapter.ViewHolder();
            viewHolder.SNo = (TextView) convertView.findViewById(R.id.tvSNo);
            viewHolder.gstin_ori = (TextView) convertView.findViewById(R.id.tvgstin_ori);
            viewHolder.invno_ori = (TextView) convertView.findViewById(R.id.tvinvno_ori);
            viewHolder.invdate_ori = (TextView) convertView.findViewById(R.id.tvinvdate_ori);
            viewHolder.gstin_rev = (TextView) convertView.findViewById(R.id.tvgstin_rev);
            viewHolder.invno_rev = (TextView) convertView.findViewById(R.id.tvinvno_rev);
            viewHolder.invdate_rev = (TextView) convertView.findViewById(R.id.tvinvdate_rev);
            viewHolder.hsnCode = (TextView) convertView.findViewById(R.id.tvhsn);
            viewHolder.tvg_s = (TextView) convertView.findViewById(R.id.tvg_s);
            viewHolder.taxableValue = (TextView) convertView.findViewById(R.id.tvtaxableValue);
            viewHolder.value = (TextView) convertView.findViewById(R.id.tvValue);

            //viewHolder.IGSTRate = (TextView) convertView.findViewById(R.id.tvIGSTRate);
            viewHolder.IGSTAmount = (TextView) convertView.findViewById(R.id.tvIGSTAmount);
            //viewHolder.CGSTRate = (TextView) convertView.findViewById(R.id.tvCGSTRate);
            viewHolder.CGSTAmount = (TextView) convertView.findViewById(R.id.tvCGSTAmount);
            //viewHolder.SGSTRate = (TextView) convertView.findViewById(R.id.tvSGSTRate);
            viewHolder.SGSTAmount = (TextView) convertView.findViewById(R.id.tvSGSTAmount);
            viewHolder.cessAmount = (TextView) convertView.findViewById(R.id.tvcessAmount);

            viewHolder.btndel = (ImageView) convertView.findViewById(R.id.btnItemDelete);
            viewHolder.btndel.setTag(i);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (GSTR2_B2B_AmendAdapter.ViewHolder) convertView.getTag();
        }
        GSTR2_B2B_Amend itemOutward =  amendArrayList.get(i);
        viewHolder.SNo.setText(String.valueOf(itemOutward.getSno()));
        viewHolder.gstin_ori.setText(String.valueOf(itemOutward.getGstin_ori()));
        viewHolder.invno_ori.setText(itemOutward.getInvoiceNo_ori());
        viewHolder.invdate_ori.setText(itemOutward.getInvoiceDate_ori());
        viewHolder.gstin_rev.setText(String.valueOf(itemOutward.getGstin_rev()));
        viewHolder.invno_rev.setText(itemOutward.getInvoiceNo_rev());
        viewHolder.invdate_rev.setText(itemOutward.getInvoiceDate_rev());
        viewHolder.value.setText(String.format("%.2f",itemOutward.getValue()));
        viewHolder.tvg_s.setText(itemOutward.getType().toString());
        viewHolder.hsnCode.setText(itemOutward.getHSn());
        viewHolder.taxableValue.setText(String.format("%.2f",itemOutward.getTaxableValue()));
        //viewHolder.pos.setText(itemOutward.getPOS());
        //viewHolder.IGSTRate.setText(String.format("%.2f",itemOutward.getIrt()));
        viewHolder.IGSTAmount.setText(String.format("%.2f",itemOutward.getIgstamt()));
        //viewHolder.CGSTRate.setText(String.format("%.2f",itemOutward.getCrt()));
        viewHolder.CGSTAmount.setText(String.format("%.2f",itemOutward.getCgstamt()));
        //viewHolder.SGSTRate.setText(String.format("%.2f",itemOutward.getSrt()));
        viewHolder.SGSTAmount.setText(String.format("%.2f",itemOutward.getSgstamt()));
        viewHolder.cessAmount.setText(String.format("%.2f",itemOutward.getCsamt()));

        viewHolder.btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
        viewHolder.btndel.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon_border));
        viewHolder.btndel.setOnClickListener(mListener);

        return convertView;
    }



    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {


            final int i  = Integer.parseInt(v.getTag().toString());
            final GSTR2_B2B_Amend obj = getItems(i);
            //String ItemName = obj.getItemName();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete this entry")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                String inv_no_ori = (obj.getInvoiceNo_ori());
                                String inv_date_ori_str = (obj.getInvoiceDate_ori());
                                Date date = new SimpleDateFormat("dd-MM-yyyy").parse(inv_date_ori_str);
                                String inv_date_ori = String.valueOf(date.getTime());
                                String inv_no_rev = (obj.getInvoiceNo_rev());
                                String inv_date_rev_str = (obj.getInvoiceDate_rev());
                                date = new SimpleDateFormat("dd-MM-yyyy").parse(inv_date_rev_str);
                                String inv_date_rev = String.valueOf(date.getTime());
                                String hsn= (obj.getHSn());
                                long lResult = 0;
                                if(GSTR ==2)
                                    lResult =handler.DeleteAmmend_GSTR2_B2BA(inv_no_ori, inv_date_ori,inv_no_rev,
                                        inv_date_rev,hsn,obj.getTaxableValue(),
                                            obj.getIgstamt(),obj.getCgstamt(),obj.getSgstamt());
                                else
                                    lResult =handler.DeleteAmmend_GSTR1_B2BA(inv_no_ori, inv_date_ori,inv_no_rev,
                                            inv_date_rev,hsn,obj.getTaxableValue(),
                                            obj.getIgstamt(),obj.getCgstamt(),obj.getSgstamt());
                                if(lResult>0)
                                {
                                    amendArrayList.remove(i);
                                    /*ArrayList<GSTR2_B2B_Amend> newAmmendList = new ArrayList<GSTR2_B2B_Amend>();
                                    newAmmendList.addAll(amendArrayList);
                                    */

                                    /*notifyDataSetChanged(amendArrayList);
                                    adapter.notifyDataSetChanged();
                                    ll.setAdapter(adapter);*/
                                    amendArrayList.clear();
                                    if(GSTR == 1)
                                    {
                                        load_GSTR1();
                                    }else
                                    {
                                        load_GSTR2();
                                    }
                                    notifyDataSetChanged();
                                }
                            }catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(R.string.cancel, null);
            AlertDialog alert = builder.create();
            alert.show();

        }
    };

    public void load_GSTR2()
    {   String pos = "";
        try{

            if(amendArrayList == null)
                amendArrayList = new ArrayList<GSTR2_B2B_Amend>();
            Date dd  = new SimpleDateFormat("dd-MM-yyyy").parse(invdate_ori);
            Cursor cursor = handler.getAmmends_GSTR2_b2b(gstin_ori, invno_ori, String.valueOf(dd.getTime()), supplierType_gstr2);
            int count =1;
            while (cursor != null && cursor.moveToNext()) {
                GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
                ammend.setSno(count++);
                ammend.setGstin_ori(cursor.getString(cursor.getColumnIndex("GSTIN_Ori")));

                long invdate_ori = cursor.getLong(cursor.getColumnIndex("OriginalInvoiceDate"));
                Date date = new Date(invdate_ori);
                String dd1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
                ammend.setInvoiceDate_ori(dd1);

                ammend.setInvoiceNo_ori(cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")));
                ammend.setGstin_rev(cursor.getString(cursor.getColumnIndex("GSTIN")));
                ammend.setInvoiceNo_rev(cursor.getString(cursor.getColumnIndex("InvoiceNo")));
                long invdate_rev = cursor.getLong(cursor.getColumnIndex("InvoiceDate"));
                dd1 = new SimpleDateFormat("dd-MM-yyyy").format(invdate_rev);
                ammend.setInvoiceDate_rev(dd1);
                ammend.setValue(cursor.getDouble(cursor.getColumnIndex("Value")));
                ammend.setType(cursor.getString(cursor.getColumnIndex("SupplyType")));
                ammend.setHSn(cursor.getString(cursor.getColumnIndex("HSNCode")));
                ammend.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
                ammend.setIgstrate(cursor.getFloat(cursor.getColumnIndex("IGSTRate")));
                ammend.setCgstrate(cursor.getFloat(cursor.getColumnIndex("CGSTRate")));
                ammend.setSgstrate(cursor.getFloat(cursor.getColumnIndex("SGSTRate")));
                ammend.setIgstamt(cursor.getFloat(cursor.getColumnIndex("IGSTAmount")));
                ammend.setCgstamt(cursor.getFloat(cursor.getColumnIndex("CGSTAmount")));
                ammend.setSgstamt(cursor.getFloat(cursor.getColumnIndex("SGSTAmount")));
                ammend.setPOS(cursor.getString(cursor.getColumnIndex("POS")));
                pos = ammend.getPOS();
                amendArrayList.add(ammend);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void load_GSTR1()
    {   try{
        if(amendArrayList == null)
            amendArrayList = new ArrayList<GSTR2_B2B_Amend>();
        Date dd  = new SimpleDateFormat("dd-MM-yyyy").parse(invdate_ori);
        Cursor cursor = handler.getAmmends_GSTR1_b2b(gstin_ori, invno_ori, String.valueOf(dd.getTime()));
        int count =1;
        while (cursor != null && cursor.moveToNext()) {
            GSTR2_B2B_Amend ammend = new GSTR2_B2B_Amend();
            ammend.setSno(count++);
            ammend.setGstin_ori(cursor.getString(cursor.getColumnIndex("GSTIN")));

            long invdate_ori = cursor.getLong(cursor.getColumnIndex("OriginalInvoiceDate"));
            Date date = new Date(invdate_ori);
            String dd1 = new SimpleDateFormat("dd-MM-yyyy").format(date);
            ammend.setInvoiceDate_ori(dd1);

            ammend.setInvoiceNo_ori(cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")));
            ammend.setGstin_rev(cursor.getString(cursor.getColumnIndex("EcommerceGSTIN")));
            ammend.setInvoiceNo_rev(cursor.getString(cursor.getColumnIndex("InvoiceNo")));
            long invdate_rev = cursor.getLong(cursor.getColumnIndex("InvoiceDate"));
            dd1 = new SimpleDateFormat("dd-MM-yyyy").format(invdate_rev);
            ammend.setInvoiceDate_rev(dd1);
            ammend.setValue(cursor.getDouble(cursor.getColumnIndex("Value")));
            ammend.setType(cursor.getString(cursor.getColumnIndex("SupplyType")));
            ammend.setHSn(cursor.getString(cursor.getColumnIndex("HSNCode")));
            ammend.setTaxableValue(cursor.getDouble(cursor.getColumnIndex("TaxableValue")));
            ammend.setIgstrate(cursor.getFloat(cursor.getColumnIndex("IGSTRate")));
            ammend.setCgstrate(cursor.getFloat(cursor.getColumnIndex("CGSTRate")));
            ammend.setSgstrate(cursor.getFloat(cursor.getColumnIndex("SGSTRate")));
            ammend.setIgstamt(cursor.getFloat(cursor.getColumnIndex("IGSTAmount")));
            ammend.setCgstamt(cursor.getFloat(cursor.getColumnIndex("CGSTAmount")));
            ammend.setSgstamt(cursor.getFloat(cursor.getColumnIndex("SGSTAmount")));
            ammend.setCsamt(cursor.getFloat(cursor.getColumnIndex("cessAmount")));
            ammend.setCustStateCode(cursor.getString(cursor.getColumnIndex("CustStateCode")));
            amendArrayList.add(ammend);
        }
    }catch (Exception e)
    {
        e.printStackTrace();
    }

    }
}
