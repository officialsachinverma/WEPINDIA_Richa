package com.wepindia.pos.GST.Adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
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

import java.util.ArrayList;

/**
 * Created by RichaA on 4/12/2017.
 */

public class GSTR1_B2CS_AmendAdapter extends BaseAdapter {
    private DatabaseHandler handler;
    private Activity activity;
    private ArrayList<GSTR2_B2B_Amend> ammendArraylist;

    public GSTR1_B2CS_AmendAdapter(Activity activity, ArrayList<GSTR2_B2B_Amend> ammendlist,DatabaseHandler handler) {
        this.handler = handler;
        this.activity = activity;
        this.ammendArraylist = ammendlist;
    }

    @Override
    public int getCount() {
        return ammendArraylist.size();
    }

    @Override
    public Object getItem(int position) {
        return ammendArraylist.get(position);
    }

    public GSTR2_B2B_Amend getItems(int i) {
        return ammendArraylist.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void notifyNewDataAdded(ArrayList<GSTR2_B2B_Amend> list) {
        this.ammendArraylist = list;
        notifyDataSetChanged();
    }

    public ArrayList<GSTR2_B2B_Amend> getItems() {
        return ammendArraylist;
    }

    public void notifyDataSetChanged(ArrayList<GSTR2_B2B_Amend> allItem) {
        this.ammendArraylist = allItem;
        notifyDataSetChanged();
    }

    class ViewHolder {
        TextView SNo;
        TextView monthTax;
        TextView hsn_ori;
        TextView type_ori;
        TextView pos_ori;
        TextView hsn_rev;
        TextView type_rev;
        TextView pos_rev;
        TextView taxableValue;
        TextView IGSTAmount;
        TextView CGSTAmount;
        TextView SGSTAmount;
        TextView cessAmount;
        ImageView btndel;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GSTR1_B2CS_AmendAdapter.ViewHolder viewHolder = null;
        int count = 0;
        if (true) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.amend_gstr1_b2cs_view, null);
            viewHolder = new GSTR1_B2CS_AmendAdapter.ViewHolder();
            viewHolder.SNo = (TextView) convertView.findViewById(R.id.tvSNo);
            viewHolder.monthTax = (TextView) convertView.findViewById(R.id.tvTaxMonth);
            viewHolder.hsn_ori = (TextView) convertView.findViewById(R.id.tvhsn_ori);
            viewHolder.type_ori = (TextView) convertView.findViewById(R.id.tvtype_ori);
            viewHolder.pos_ori = (TextView) convertView.findViewById(R.id.tvpos_ori);
            viewHolder.hsn_rev = (TextView) convertView.findViewById(R.id.tvhsn_rev);
            viewHolder.type_rev = (TextView) convertView.findViewById(R.id.tvtype_rev);
            viewHolder.pos_rev = (TextView) convertView.findViewById(R.id.tvpos_rev);

            viewHolder.taxableValue = (TextView) convertView.findViewById(R.id.tvtaxableValue);

            viewHolder.IGSTAmount = (TextView) convertView.findViewById(R.id.tvIGSTAmount);
            viewHolder.CGSTAmount = (TextView) convertView.findViewById(R.id.tvCGSTAmount);
            viewHolder.SGSTAmount = (TextView) convertView.findViewById(R.id.tvSGSTAmount);
            viewHolder.cessAmount = (TextView) convertView.findViewById(R.id.tvcessAmount);

            viewHolder.btndel = (ImageView) convertView.findViewById(R.id.btnItemDelete);
            viewHolder.btndel.setTag(count++);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (GSTR1_B2CS_AmendAdapter.ViewHolder) convertView.getTag();
        }
        GSTR2_B2B_Amend itemOutward = ammendArraylist.get(position);
        viewHolder.SNo.setText(String.valueOf(itemOutward.getSno()));
        viewHolder.monthTax.setText(itemOutward.getTaxMonth());
        viewHolder.type_ori.setText(itemOutward.getType_ori());
        viewHolder.type_rev.setText(itemOutward.getType_rev());
        viewHolder.hsn_ori.setText(itemOutward.getHsn_ori());
        viewHolder.hsn_rev.setText(itemOutward.getHsn_rev());
        viewHolder.taxableValue.setText(String.format("%.2f", itemOutward.getTaxableValue()));
        viewHolder.pos_ori.setText(itemOutward.getPos_ori());
        viewHolder.pos_rev.setText(itemOutward.getCustStateCode());

        viewHolder.IGSTAmount.setText(String.format("%.2f", itemOutward.getIgstamt()));
        viewHolder.CGSTAmount.setText(String.format("%.2f", itemOutward.getCgstamt()));
        viewHolder.SGSTAmount.setText(String.format("%.2f", itemOutward.getSgstamt()));
        viewHolder.cessAmount.setText(String.format("%.2f", itemOutward.getCsamt()));

        viewHolder.btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
        viewHolder.btndel.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon_border));
        viewHolder.btndel.setOnClickListener(mListener);

        return convertView;
    }


    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {


            final int i = Integer.parseInt(v.getTag().toString());
            final GSTR2_B2B_Amend obj = getItems(i);
            //String ItemName = obj.getItemName();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete this entry")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                String taxMonth = (obj.getTaxMonth());
                                String hsn_ori = (obj.getHsn_ori());
                                String pos_ori = obj.getPos_ori();
                                String pos_rev = obj.getCustStateCode();
                                long lResult = handler.DeleteAmmend_GSTR1_B2CSA(taxMonth,hsn_ori, obj.getTaxableValue()
                                ,pos_ori,pos_rev, obj.getIgstamt(),obj.getCgstamt(), obj.getSgstamt(),obj.getCsamt());

                                if (lResult > 0) {
                                   // Log.d("GSTR1_B2CSA","Deleted sucessfully");
                                    ammendArraylist.remove(i);
                                    notifyDataSetChanged();
                                }/*else
                                {
                                    //Log.d("GSTR1_B2CSA","Cannot delete entry");
                                }*/
                            } catch (Exception e) {
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
