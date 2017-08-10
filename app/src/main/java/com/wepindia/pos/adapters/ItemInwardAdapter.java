package com.wepindia.pos.adapters;

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
import android.widget.Toast;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.ItemInward;
import com.wepindia.pos.InwardItemActivity;
import com.wepindia.pos.R;

import java.util.ArrayList;

/**
 * Created by RichaA on 3/16/2017.
 */

public class ItemInwardAdapter extends BaseAdapter {
    private Context activityContext;
    private DatabaseHandler dbHandler;
    private ArrayList<ItemInward> itemList;
    ItemInwardAdapter activityAdapter;
    String className;

    public ItemInwardAdapter( Context context, DatabaseHandler dbHandler, ArrayList<ItemInward> itemList, String className) {
        this.itemList = itemList;
        this.activityContext = context;
        this.dbHandler = dbHandler;
        this.className = className;
    }

    public ItemInwardAdapter getActivityAdapter() {
        return activityAdapter;
    }

    public void setActivityAdapter(ItemInwardAdapter activityAdapter) {
        this.activityAdapter = activityAdapter;
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void notifyDataSetChanged(ArrayList<ItemInward> list) {
        this.itemList = list;
        notifyDataSetChanged();
    }
    static class ViewHolder {
        TextView Sno;
        TextView ItemName;
        TextView AverageRate;
        TextView UOM;
        TextView Stock;
        ImageView btndel;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ItemInwardAdapter.ViewHolder viewHolder;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activityContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_inwarditem, null);
            viewHolder = new ItemInwardAdapter.ViewHolder();
            viewHolder.Sno = (TextView) convertView.findViewById(R.id.Sno);
            viewHolder.ItemName = (TextView) convertView.findViewById(R.id.ItemName);
            viewHolder.AverageRate = (TextView) convertView.findViewById(R.id.AverageRate);
            viewHolder.Stock = (TextView) convertView.findViewById(R.id.Stock);
            viewHolder.UOM = (TextView) convertView.findViewById(R.id.UOM);
            viewHolder.btndel = (ImageView) convertView.findViewById(R.id.btndel);
            viewHolder.btndel.setTag(position);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ItemInwardAdapter.ViewHolder) convertView.getTag();
        }
        ItemInward item = itemList.get(position);
        viewHolder.Sno.setText(String.valueOf(position+1));
        viewHolder.ItemName.setText(item.getStrItemname());
        viewHolder.AverageRate.setText(String.format("%.2f",item.getfAveragerate()));
        viewHolder.Stock.setText(String.format("%.2f",item.getfQuantity()));
        viewHolder.UOM.setText(item.getUOM());

        viewHolder.btndel.setLayoutParams(new TableRow.LayoutParams(40, 35));
        viewHolder.btndel.setBackground(activityContext.getResources().getDrawable(R.drawable.delete_icon_border));
        viewHolder.btndel.setOnClickListener(mListener);
        return convertView;
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

            AlertDialog.Builder builder = new AlertDialog.Builder(activityContext)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete this Item. " +
                            "\nThis item will no longer be available in SupplierItemLinkage, Purchase Order, GoodsInwardNote and IngredientManagement.\n"+
                            "\nPlease note that this item will not delete from inward stock database for current business date")
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            final int i  = Integer.parseInt(v.getTag().toString());
                            final ItemInward obj = (ItemInward) getItem(i);
                            String ItemName = obj.getStrItemname();
                            String MenuCode = String.valueOf(obj.getiMenuCode());
                            long lResult = dbHandler.DeleteItem_Inw(MenuCode);
                            if (lResult>0){
                                Toast.makeText(activityContext, "Item Deleted Successfully", Toast.LENGTH_SHORT).show();
                                Log.d("InwardItemActivity",ItemName+" delete successfully");
                                dbHandler.deleteSupplierItemLinkforItem(obj.getiMenuCode());
                                dbHandler.deleteItemInGoodsInward(ItemName);
                                if(className.equalsIgnoreCase("InwardItemActivity")) {
                                    ((InwardItemActivity) activityContext).loadSpinnerData();
                                }
                                itemList.remove(i);
                                activityAdapter.notifyDataSetChanged(itemList);
                            }
                            dialog.dismiss();
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();
    }};


}
