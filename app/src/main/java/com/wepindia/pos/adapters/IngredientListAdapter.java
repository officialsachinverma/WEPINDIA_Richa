package com.wepindia.pos.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TableRow;
import android.widget.TextView;

import com.wep.common.app.models.Ingredient;
import com.wepindia.pos.R;

import java.util.ArrayList;

/**
 * Created by RichaA on 5/31/2017.
 */

public class IngredientListAdapter extends BaseAdapter {
    private Activity activity;

    private ArrayList<Ingredient> IngredientList;

    public IngredientListAdapter(Activity activity, ArrayList<Ingredient> itemsList) {

        this.activity = activity;

        this.IngredientList = itemsList;
    }
    @Override
    public int getCount() {
        return IngredientList.size();
    }

    @Override
    public Object getItem(int position) {
        return IngredientList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ViewHolder {
        TextView tv_Sn;
        TextView tv_ingredientName;
        TextView tv_qty;
        TextView tv_UOM;
        ImageButton tv_imgDel;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        IngredientListAdapter.ViewHolder viewHolder;
        int count =1;
        if(convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_ingredientlist, null);
            viewHolder = new IngredientListAdapter.ViewHolder();
            viewHolder.tv_Sn = (TextView) convertView.findViewById(R.id.tv_Sn);
            viewHolder.tv_ingredientName = (TextView) convertView.findViewById(R.id.tv_ingredientName);
            viewHolder.tv_qty = (TextView) convertView.findViewById(R.id.tv_qty);
            viewHolder.tv_UOM = (TextView) convertView.findViewById(R.id.tv_UOM);
            viewHolder.tv_imgDel = (ImageButton) convertView.findViewById(R.id.tv_imgDel);

            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (IngredientListAdapter.ViewHolder) convertView.getTag();
        }
        Ingredient ingredient = IngredientList.get(position);
        viewHolder.tv_Sn.setText(String.valueOf(position+1));
        viewHolder.tv_ingredientName.setText(ingredient.getIngredientName());
        viewHolder.tv_qty.setText(String.format("%.2f",ingredient.getIngredientQuantity()));
        viewHolder.tv_UOM.setText(ingredient.getUOM());
        viewHolder.tv_imgDel.setLayoutParams(new TableRow.LayoutParams(40, 35));
        viewHolder.tv_imgDel.setBackground(activity.getResources().getDrawable(R.drawable.delete_icon_border));
        viewHolder.tv_imgDel.setOnClickListener(mListener);
        viewHolder.tv_imgDel.setTag(position);

        return convertView;
    }

    public void notifyDataSetChanged(ArrayList<Ingredient> list) {
        this.IngredientList = list;
        notifyDataSetChanged();
    }

    public void notifyNewDataAdded(ArrayList<Ingredient> list) {
        this.IngredientList = list;
        notifyDataSetChanged();
    }

    private View.OnClickListener mListener = new View.OnClickListener() {
        public void onClick(final View v) {

            final int i  = Integer.parseInt(v.getTag().toString());
            final Ingredient ingredient = (Ingredient) getItem(i);
            final int ingredientCode = ingredient.getIngredientcode();
            final double ingredientQuantity = ingredient.getIngredientQuantity();
            final String ingredientName = ingredient.getIngredientName();


            AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                    .setTitle("Delete")
                    .setMessage("Are you sure you want to Delete ingredient : "+ingredientName)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            for(Ingredient ingredient : IngredientList)
                            {
                                if(ingredient.getIngredientcode()==ingredientCode && ingredient.getIngredientQuantity()== ingredientQuantity)
                                {
                                    IngredientList.remove(i);
                                    notifyDataSetChanged();
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
}
