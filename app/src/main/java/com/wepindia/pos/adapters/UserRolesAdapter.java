package com.wepindia.pos.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wepindia.pos.R;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 18-08-2016.
 */
public class UserRolesAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> arrayListRoles;

    public UserRolesAdapter(Activity activity,ArrayList<String> arrayListRoles){
        this.activity = activity;
        this.arrayListRoles = arrayListRoles;
    }

    public int getCount() {
        return arrayListRoles.size();
    }


    public Object getItem(int i) {
        return arrayListRoles.get(i);
    }


    public long getItemId(int i) {
        return i;
    }

    public void notifyNewDataAdded(ArrayList<String> list) {
        this.arrayListRoles = list;
        notifyDataSetChanged();
    }

    public ArrayList<String> getItems() {
        return arrayListRoles;
    }

    static class ViewHolder {
        TextView textViewRoleName;
    }


    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(true)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_role,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewRoleName = (TextView) convertView.findViewById(R.id.textViewRoleName);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewRoleName.setText(arrayListRoles.get(i));
        return convertView;
    }
}
