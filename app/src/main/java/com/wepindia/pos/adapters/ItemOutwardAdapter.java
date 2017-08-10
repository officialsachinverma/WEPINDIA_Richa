package com.wepindia.pos.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.ItemOutward;
import com.wep.common.app.utils.AppUtils;
import com.wepindia.pos.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by RichaA on 2/13/2017.
 */

public class ItemOutwardAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<ItemOutward> itemOutwardsArrayList;
    private DatabaseHandler handler;

    public ItemOutwardAdapter(Activity activity, ArrayList<ItemOutward> itemOutwardsArrayList, DatabaseHandler handler){
        this.activity = activity;
        this.itemOutwardsArrayList = itemOutwardsArrayList;
        this.handler = handler;
    }
    public int getCount() {
        return itemOutwardsArrayList.size();
    }

    public Object getItem(int i) {
        return itemOutwardsArrayList.get(i);
    }
    public ItemOutward getItems(int i) {
        return itemOutwardsArrayList.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public void notifyNewDataAdded(ArrayList<ItemOutward> list) {
        this.itemOutwardsArrayList = list;
        notifyDataSetChanged();
    }

    public ArrayList<ItemOutward> getItems() {
        return itemOutwardsArrayList;
    }

    public void notifyDataSetChanged(ArrayList<ItemOutward> allItem) {
        this.itemOutwardsArrayList = allItem;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView menuCode;
        TextView itemName;
        TextView rate1;
        TextView rate2;
        TextView rate3;
        TextView stock;
        ImageView itemImg;
        ImageView btndel;
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ItemOutwardAdapter.ViewHolder viewHolder = null;
        if(true)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_outwarditem,null);
            viewHolder = new ItemOutwardAdapter.ViewHolder();
            viewHolder.menuCode = (TextView) convertView.findViewById(R.id.tvMenuCode);
            viewHolder.itemName = (TextView) convertView.findViewById(R.id.tvItemName);
            viewHolder.rate1 = (TextView) convertView.findViewById(R.id.tvDineIn1);
            viewHolder.rate2 = (TextView) convertView.findViewById(R.id.tvDineIn2);
            viewHolder.rate3 = (TextView) convertView.findViewById(R.id.tvDineIn3);
            viewHolder.stock = (TextView) convertView.findViewById(R.id.tvStock);
            viewHolder.itemImg = (ImageView) convertView.findViewById(R.id.imgIcon);
            viewHolder.btndel = (ImageView) convertView.findViewById(R.id.btnItemDelete);
            viewHolder.btndel.setTag(i);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ItemOutwardAdapter.ViewHolder) convertView.getTag();
        }
        ItemOutward itemOutward = itemOutwardsArrayList.get(i);
        viewHolder.menuCode.setText(String.valueOf(itemOutward.getMenuCode()));
        viewHolder.itemName.setText(itemOutward.getItemName());
        viewHolder.rate1.setText(String.format("%.2f",itemOutward.getDineIn1()));
        viewHolder.rate2.setText(String.format("%.2f",itemOutward.getDineIn2()));
        viewHolder.rate3.setText(String.format("%.2f",itemOutward.getDineIn3()));
        viewHolder.stock.setText(String.valueOf(itemOutward.getStock()));

        TableRow.LayoutParams rowparams = new TableRow.LayoutParams(60, 40);
        rowparams.gravity = Gravity.CENTER;
        viewHolder.itemImg.setLayoutParams(rowparams);
        viewHolder.itemImg.setScaleType(ImageView.ScaleType.FIT_CENTER);

        String icon = AppUtils.getImagePath(itemOutward.getImageUri(),itemOutward.getItemName());
        Uri uri = Uri.fromFile(new File(icon));
        try{
            Picasso.with(activity)
                    .load(uri)
                    .placeholder(R.drawable.img_noimage) //this is optional the image to display while the url image is downloading
                    .error(R.drawable.img_noimage)         //this is also optional if some error has occurred in downloading the image this image would be displayed
                    .into(viewHolder.itemImg);
        }catch (Exception e){
            viewHolder.itemImg.setImageResource(R.drawable.img_noimage);
        }

        viewHolder.btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
        viewHolder.btndel.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon_border));
        viewHolder.btndel.setOnClickListener(mListener);

        return convertView;
    }


    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

            final int i  = Integer.parseInt(v.getTag().toString());
            final ItemOutward obj = getItems(i);
            String ItemName = obj.getItemName();
            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete Item "+ ItemName)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            int menucode = obj.getMenuCode();
                            long lResult = handler.DeleteItemByMenuCode(menucode);
                            if(lResult>0)
                            {
                                itemOutwardsArrayList.remove(i);
                                notifyDataSetChanged();
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

}
