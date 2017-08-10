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

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.gst.GSTR1_CDN_Details;
import com.wepindia.pos.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by RichaA on 4/10/2017.
 */

public class CDNoteAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<GSTR1_CDN_Details> creditdebitArrayList;
    private DatabaseHandler handler;
    private String GSTR_type;

    public CDNoteAdapter(Activity activity, ArrayList<GSTR1_CDN_Details> itemOutwardsArrayList, DatabaseHandler handler, String GSTR_type){
        this.activity = activity;
        this. creditdebitArrayList = itemOutwardsArrayList;
        this.handler = handler;
        this.GSTR_type = GSTR_type;
    }
    public int getCount() {
        return  creditdebitArrayList.size();
    }

    public Object getItem(int i) {
        return  creditdebitArrayList.get(i);
    }
    public GSTR1_CDN_Details getItems(int i) {
        return  creditdebitArrayList.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public void notifyNewDataAdded(ArrayList<GSTR1_CDN_Details> list) {
        this. creditdebitArrayList = list;
        notifyDataSetChanged();
    }

    public ArrayList<GSTR1_CDN_Details> getItems() {
        return  creditdebitArrayList;
    }

    public void notifyDataSetChanged(ArrayList<GSTR1_CDN_Details> allItem) {
        this. creditdebitArrayList = allItem;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView SNo;
        TextView CreditNo;
        TextView CreditDate;
        TextView DifferentialValue;
        TextView IGSTRate;
        TextView IGSTAmount;
        TextView CGSTRate;
        TextView CGSTAmount;
        TextView SGSTRate;
        TextView SGSTAmount;
        TextView cessAmount;
        TextView tvNotetype;
        ImageView btndel;
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {
        CDNoteAdapter.ViewHolder viewHolder = null;
        int count =1;
        if(true)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.credit_view,null);
            viewHolder = new CDNoteAdapter.ViewHolder();
            viewHolder.SNo = (TextView) convertView.findViewById(R.id.tvSNo);
            viewHolder.tvNotetype = (TextView) convertView.findViewById(R.id.tvNotetype);
            viewHolder.CreditNo = (TextView) convertView.findViewById(R.id.tvCreditNo);
            viewHolder.CreditDate = (TextView) convertView.findViewById(R.id.tvCreditDate);
            viewHolder.DifferentialValue = (TextView) convertView.findViewById(R.id.tvDifferentialValue);
            viewHolder.IGSTRate = (TextView) convertView.findViewById(R.id.tvIGSTRate);
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
            viewHolder = (CDNoteAdapter.ViewHolder) convertView.getTag();
        }
        GSTR1_CDN_Details itemOutward =  creditdebitArrayList.get(i);
        viewHolder.SNo.setText(String.valueOf(itemOutward.getSno()));
        viewHolder.tvNotetype.setText(String.valueOf(itemOutward.getNtty()));
        viewHolder.CreditNo.setText(String.valueOf(itemOutward.getNt_num()));
        viewHolder.CreditDate.setText(itemOutward.getNt_dt());
        viewHolder.DifferentialValue.setText(String.format("%.2f",itemOutward.getVal()));
        viewHolder.IGSTRate.setText(String.format("%.2f",(itemOutward.getIrt() +itemOutward.getCrt()+itemOutward.getSrt())));
        viewHolder.IGSTAmount.setText(String.format("%.2f",itemOutward.getIamt()));
        //viewHolder.CGSTRate.setText(String.format("%.2f",itemOutward.getCrt()));
        viewHolder.CGSTAmount.setText(String.format("%.2f",itemOutward.getCamt()));
       // viewHolder.SGSTRate.setText(String.format("%.2f",itemOutward.getSrt()));
        viewHolder.SGSTAmount.setText(String.format("%.2f",itemOutward.getSamt()));
        viewHolder.cessAmount.setText(String.format("%.2f",itemOutward.getCsamt()));

        viewHolder.btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
        viewHolder.btndel.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon_border));
        viewHolder.btndel.setOnClickListener(mListener);

        return convertView;
    }


    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {


            final int i  = Integer.parseInt(v.getTag().toString());
            final GSTR1_CDN_Details obj = getItems(i);
            //String ItemName = obj.getItemName();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete this note")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try{
                                String noteNo = String.valueOf(obj.getNt_num());
                                String noteDate = (obj.getNt_dt());
                                Date date_note = (new SimpleDateFormat("dd-MM-yyyy")).parse(noteDate);
                                String invoiceNo = (obj.getInum());
                                String invoiceDate = (obj.getIdt());
                                String notyType = obj.getNtty();
                                int lResult =0;
                                if(GSTR_type.equalsIgnoreCase("Outward"))
                                    lResult = handler.DeleteOutwardNote(invoiceNo, invoiceDate,noteNo,String.valueOf(date_note.getTime()),notyType);
                                else
                                    lResult = handler.DeleteInwardNote(invoiceNo, invoiceDate,noteNo,String.valueOf(date_note.getTime()),notyType);
                            if(lResult>0)
                            {
                                creditdebitArrayList.remove(i);
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
}


