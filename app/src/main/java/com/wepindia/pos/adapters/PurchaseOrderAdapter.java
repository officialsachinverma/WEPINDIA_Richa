package com.wepindia.pos.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.PurchaseOrder;

import com.wepindia.pos.R;

import java.util.ArrayList;

/**
 * Created by RichaA on 5/2/2017.
 */

public class PurchaseOrderAdapter extends BaseAdapter{

    private Activity activity;
    private DatabaseHandler dbHandler;
    private ArrayList<PurchaseOrder> purchaseOrderList;

    public PurchaseOrderAdapter(Activity activity, DatabaseHandler dbHandler,ArrayList<PurchaseOrder> itemsList) {

        this.activity = activity;
        this.dbHandler = dbHandler;
        this.purchaseOrderList = itemsList;
    }
    @Override
    public int getCount() {
        return purchaseOrderList.size();
    }

    @Override
    public Object getItem(int position) {
        return purchaseOrderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView tv_Sn;
        TextView tv_g_s;
        TextView tv_hsnCode;
        TextView tv_itemName;
        TextView tv_rate;
        TextView tv_qty;
        TextView tv_UOM;
        TextView tv_taxVal;
        TextView tv_igstAmt;
        TextView tv_cgstAmt;
        TextView tv_sgstAmt;
        TextView tv_cessAmt;
        TextView tv_amt;
        ImageButton tv_imgDel;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        PurchaseOrderAdapter.ViewHolder viewHolder;
        int count =1;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_purchaseorder, null);
            viewHolder = new PurchaseOrderAdapter.ViewHolder();
            viewHolder.tv_Sn = (TextView) convertView.findViewById(R.id.tv_Sn);
            //viewHolder.tv_g_s = (TextView) convertView.findViewById(R.id.tv_g_s);
            viewHolder.tv_hsnCode = (TextView) convertView.findViewById(R.id.tv_hsnCode);
            viewHolder.tv_itemName = (TextView) convertView.findViewById(R.id.tv_itemName);
            viewHolder.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
            viewHolder.tv_qty = (TextView) convertView.findViewById(R.id.tv_qty);
            viewHolder.tv_UOM = (TextView) convertView.findViewById(R.id.tv_UOM);
            viewHolder.tv_taxVal = (TextView) convertView.findViewById(R.id.tv_taxVal);
            viewHolder.tv_igstAmt = (TextView) convertView.findViewById(R.id.tv_igstAmt);
            viewHolder.tv_cgstAmt = (TextView) convertView.findViewById(R.id.tv_cgstAmt);
            viewHolder.tv_sgstAmt = (TextView) convertView.findViewById(R.id.tv_sgstAmt);
            viewHolder.tv_cessAmt = (TextView) convertView.findViewById(R.id.tv_cessAmt);
            viewHolder.tv_amt = (TextView) convertView.findViewById(R.id.tv_amt);
            viewHolder.tv_imgDel = (ImageButton) convertView.findViewById(R.id.tv_imgDel);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (PurchaseOrderAdapter.ViewHolder) convertView.getTag();
        }
        PurchaseOrder po = purchaseOrderList.get(position);
        viewHolder.tv_Sn.setText(String.valueOf(position+1));
        //viewHolder.tv_g_s.setText(po.getSupplyType());
        viewHolder.tv_hsnCode.setText(po.getHSNCode());
        viewHolder.tv_itemName.setText(po.getItemName());
        viewHolder.tv_rate.setText(String.format("%.2f",po.getValue()));
        viewHolder.tv_qty.setText(String.format("%.2f",po.getQuantity()));
        viewHolder.tv_UOM.setText(po.getUOM());
        viewHolder.tv_taxVal.setText(String.format("%.2f",po.getTaxableValue()));
        viewHolder.tv_igstAmt.setText(String.format("%.2f",po.getIgstAmount()));
        viewHolder.tv_cgstAmt.setText(String.format("%.2f",po.getCgstAmount()));
        viewHolder.tv_sgstAmt.setText(String.format("%.2f",po.getSgstAmount()));
        viewHolder.tv_cessAmt.setText(String.format("%.2f",po.getCsAmount()));
        viewHolder.tv_amt.setText(String.format("%.2f",po.getAmount()));

        viewHolder.tv_imgDel.setLayoutParams(new TableRow.LayoutParams(40, 35));
        viewHolder.tv_imgDel.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon_border));
        viewHolder.tv_imgDel.setOnClickListener(mListener);
        viewHolder.tv_imgDel.setTag(position);

        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<PurchaseOrder> list) {
        this.purchaseOrderList = list;
        notifyDataSetChanged();
    }

    public void notifyNewDataAdded(ArrayList<PurchaseOrder> list) {
        this.purchaseOrderList = list;
        notifyDataSetChanged();
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

            final int i  = Integer.parseInt(v.getTag().toString());
            final PurchaseOrder po = (PurchaseOrder)getItem(i);
            final String puchaseOrderNo = po.getPurchaseOrderNo();
            final String supplierCode = po.getSupplierCode();
            final String itemname = po.getItemName();
            final double rate = po.getValue();
            final double quantity = po.getQuantity();

            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete "+itemname+" of quantity "+quantity)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            long lResult = 1;/*dbHandler.deletePurchaseOrderEntry(puchaseOrderNo, supplierCode,itemname,
                                    rate,quantity);*/
                            if(lResult>0)
                            {
                                purchaseOrderList.remove(i);
                                notifyDataSetChanged();
                                calculateSubTotal();
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

        }
    };


    public void calculateSubTotal()
    {
        float grandtotal_f =0;
        float subtotal_f =0;
        float additionalcharge_f = 0;
        TextView Amount;
        String amount_str = "0";
        for (PurchaseOrder po : purchaseOrderList)
        {

            subtotal_f += po.getAmount();

        }
        EditText et_inward_sub_total = (EditText)(activity).findViewById(R.id.et_inward_sub_total);
        EditText et_inward_additionalchargeamount = (EditText)(activity).findViewById(R.id.et_inward_additionalchargeamount);
        EditText et_inward_grand_total = (EditText)(activity).findViewById(R.id.et_inward_grand_total);
        CheckBox chk_inward_additional_charge = (CheckBox) (activity).findViewById(R.id.chk_inward_additional_charge);
        et_inward_sub_total.setText(String.format("%.2f",subtotal_f));

        // addtional charge
        if (chk_inward_additional_charge.isChecked())
            if( et_inward_additionalchargeamount.getText().toString().equals(""))
            {
                additionalcharge_f =0;
            }
            else
            {
                additionalcharge_f = Float.parseFloat(et_inward_additionalchargeamount.getText().toString());
            }

        grandtotal_f = subtotal_f+ additionalcharge_f;
        et_inward_grand_total.setText(String.format("%.2f",grandtotal_f));

    }

}
