package com.wepindia.pos.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.Database.Supplier_Model;
import com.wepindia.pos.R;
import com.wepindia.pos.SupplierDetailsActivity;

import java.util.ArrayList;

/**
 * Created by RichaA on 3/15/2017.
 */

public class SupplierAdapter extends BaseAdapter {
    private Context activityContext;
    private ArrayList<Supplier_Model> SupplierList;
    android.support.v4.app.Fragment fragment ;
    String activityName;
    DatabaseHandler db;

    public SupplierAdapter(Context activityContext, ArrayList<Supplier_Model> supplierList, DatabaseHandler db , String activityName) {
        this.activityContext = activityContext;
        SupplierList = supplierList;
        this.db = db;
        this.fragment = fragment;
        this.activityName = activityName;
    }

    @Override
    public int getCount() {
        return SupplierList.size();
    }

    @Override
    public Supplier_Model getItem(int position) {
        return SupplierList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyDataSetChanged(ArrayList<Supplier_Model> list) {
        this.SupplierList = list;
        notifyDataSetChanged();
    }
    public void notifyNewDataAdded(ArrayList<Supplier_Model> list) {
        this.SupplierList = list;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
       SupplierAdapter.ViewHolder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_supplier, null);
            viewHolder = new SupplierAdapter.ViewHolder();
            viewHolder.tvSNo = (TextView) convertView.findViewById(R.id.tvSNo);
            viewHolder.tvSupplierName = (TextView) convertView.findViewById(R.id.tvSupplierName);
            viewHolder.tvSupplierPhone = (TextView) convertView.findViewById(R.id.tvSupplierPhone);
            viewHolder.tbSupplierGSTIN = (TextView) convertView.findViewById(R.id.tbSupplierGSTIN);
            viewHolder.tvSupplierAddress = (TextView) convertView.findViewById(R.id.tvSupplierAddress);
            viewHolder.imgBtnDelete = (ImageView) convertView.findViewById(R.id.imgBtnDelete);
            viewHolder.imgBtnDelete.setLayoutParams(new TableRow.LayoutParams(45, 35));
            viewHolder.imgBtnDelete.setBackground(activityContext.getResources().getDrawable(R.drawable.delete_icon_border));
            viewHolder.imgBtnDelete.setOnClickListener(mListener);
            viewHolder.imgBtnDelete.setTag(position);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (SupplierAdapter.ViewHolder) convertView.getTag();
        }
        Supplier_Model supplier = SupplierList.get(position);
        viewHolder.tvSNo.setText(String.valueOf(position+1));
        viewHolder.tvSupplierName.setText(supplier.getSupplierName());
        viewHolder.tvSupplierPhone.setText(supplier.getSupplierPhone());
        viewHolder.tbSupplierGSTIN.setText(supplier.getSupplierGSTIN());
        viewHolder.tvSupplierAddress.setText(supplier.getSupplierAddress());

        return convertView;
    }

    private  View.OnClickListener mListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final int i  = Integer.parseInt(v.getTag().toString());
            final Supplier_Model supplier_model = (Supplier_Model) getItem(i);
            final int SupplierCode = supplier_model.getSupplierCode();
            final String SupplierName = supplier_model.getSupplierName();
            AlertDialog.Builder builder = new AlertDialog.Builder(activityContext)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete supplier : "+SupplierName+"\n Please note all the items (if any) for this supplier will also be deleted.")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            for(Supplier_Model supplier : SupplierList)
                            {
                                if(supplier.getSupplierCode()==SupplierCode )
                                {
                                    long ll = db.deleteSupplier(SupplierCode);
                                    if(ll >0)
                                    {
                                        SupplierList.remove(i);
                                        notifyDataSetChanged(SupplierList);
                                        //FragmentSupplierDetails.loadAutoCompleteSupplierName(); sp = new FragmentSupplierDetails();
                                        if(activityName.equalsIgnoreCase("com.wepindia.pos.SupplierDetailsActivity"))
                                        {
                                            ((SupplierDetailsActivity) activityContext).loadAutoCompleteSupplierName();
                                            ((SupplierDetailsActivity) activityContext).Clear();
                                            ((SupplierDetailsActivity) activityContext).Display();
                                        }


                                        Toast.makeText(activityContext, SupplierName+" deleted sucessfully", Toast.LENGTH_SHORT).show();
                                        long del = db.DeleteSupplierItems_suppliercode(SupplierCode);
                                        if(del > 0)
                                            Toast.makeText(activityContext, del+" items also deleted for supplier "+SupplierName, Toast.LENGTH_SHORT).show();

                                    }else
                                    {
                                        Toast.makeText(activityContext, SupplierName+" cannot be deleted", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                }
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



    static class ViewHolder {
        TextView tvSNo;
        TextView tvSupplierName;
        TextView tvSupplierPhone;
        TextView tbSupplierGSTIN;
        TextView tvSupplierAddress;
        ImageView imgBtnDelete;
    }
}
