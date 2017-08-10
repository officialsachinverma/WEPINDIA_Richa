package com.wepindia.pos.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.models.User;
import com.wepindia.pos.R;

import java.util.ArrayList;

/**
 * Created by PriyabratP on 22-08-2016.
 */
public class UsersListAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<User> usersList;
    private DatabaseHandler handler;

    public UsersListAdapter(Activity activity, ArrayList<User> usersList, DatabaseHandler handler){
        this.activity = activity;
        this.usersList = usersList;
        this.handler = handler;
    }

    public int getCount() {
        return usersList.size();
    }


    public Object getItem(int i) {
        return usersList.get(i);
    }
    public User getItems(int i) {
        return usersList.get(i);
    }

    public long getItemId(int i) {
        return i;
    }

    public void notifyNewDataAdded(ArrayList<User> list) {
        this.usersList = list;
        notifyDataSetChanged();
    }

    public ArrayList<User> getItems() {
        return usersList;
    }

    static class ViewHolder {
        TextView textViewId;
        TextView textViewName;
        TextView textViewRole;
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(true)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_users,null);
            viewHolder = new ViewHolder();
            viewHolder.textViewId = (TextView) convertView.findViewById(R.id.tvUserId);
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.textViewRole = (TextView) convertView.findViewById(R.id.tvUserRole);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User user = usersList.get(i);
        viewHolder.textViewId.setText(user.getId()+"");
        viewHolder.textViewId.setPadding(3,0,0,0);
        viewHolder.textViewName.setText(user.getUserName()+"");
        viewHolder.textViewRole.setText(handler.getRoleName(user.getUserRole()+""));
        return convertView;
    }
}
