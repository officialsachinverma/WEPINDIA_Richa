package com.wepindia.pos.GST;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.wep.common.app.gst.Model_reconcile;
import com.wepindia.pos.R;

import java.util.ArrayList;

/**
 * Created by welcome on 05-11-2016.
 */

public class reconcileAdapter extends ArrayAdapter<Model_reconcile> {

    Context myContext;
    ArrayList<Model_reconcile> row_data;
    public reconcileAdapter(Context context, ArrayList<Model_reconcile> row_data)
    {
        super(context, R.layout.activity_gstr2a_items_list, row_data);
        myContext = context;
        this.row_data = row_data;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater row_inflater = LayoutInflater.from(myContext);
        View row_view = row_inflater.inflate(R.layout.activity_gstr2a_items_list,parent,false);
        TextView tv_Sno, tv_gstin, tv_invoiceno, tv_invoiceDate, tv_value,tv_hsn,tv_taxableValue, tv_igstAmt, tv_sgstAmt, tv_cgstAmt, tv_pos;
        tv_Sno = (TextView) row_view.findViewById(R.id.gstr2a_item_sno);
        tv_gstin = (TextView) row_view.findViewById(R.id.gstr2a_item_gstin);
        tv_invoiceno = (TextView) row_view.findViewById(R.id.gstr2a_item_invoice_no);
        tv_invoiceDate = (TextView) row_view.findViewById(R.id.gstr2a_item_invoice_date);
        tv_value = (TextView) row_view.findViewById(R.id.gstr2a_item_value);
        tv_hsn = (TextView) row_view.findViewById(R.id.gstr2a_item_hsn);
        tv_taxableValue = (TextView) row_view.findViewById(R.id.gstr2a_item_taxable_value);
        tv_igstAmt = (TextView) row_view.findViewById(R.id.gstr2a_item_igst_amt);
        tv_cgstAmt = (TextView) row_view.findViewById(R.id.gstr2a_item_cgst_amt);
        tv_sgstAmt = (TextView) row_view.findViewById(R.id.gstr2a_item_sgst_amt);
        tv_pos = (TextView) row_view.findViewById(R.id.gstr2a_item_pos);

        for(int i =0;i<row_data.size();i++)
        {
            tv_Sno.setText(row_data.get(i).getSno());
            tv_gstin.setText(row_data.get(i).getGstin());
            tv_invoiceno.setText(row_data.get(i).getInvoiceNo());
            tv_invoiceDate.setText(row_data.get(i).getInvoiceDate());
            tv_value.setText(row_data.get(i).getValue());
            tv_hsn.setText(row_data.get(i).getHSN());
            tv_taxableValue.setText(row_data.get(i).getTaxable_value());
            tv_igstAmt.setText(row_data.get(i).getIgst_amt());
            tv_cgstAmt.setText(row_data.get(i).getCgst_amt());
            tv_sgstAmt.setText(row_data.get(i).getSgst_amt());
            tv_pos.setText(row_data.get(i).getPos());
        }
        return row_view;

    }
}
