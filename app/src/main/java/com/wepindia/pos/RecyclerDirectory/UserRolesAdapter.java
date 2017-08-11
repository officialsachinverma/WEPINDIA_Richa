package com.wepindia.pos.RecyclerDirectory;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wepindia.pos.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PriyabratP on 18-08-2016.
 */
public class UserRolesAdapter extends RecyclerView.Adapter<UserRolesAdapter.ViewHolder> {

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

    public UserRolesAdapter(ArrayList<String> List) {
        arrayListRoles=List;
    }

    public void notifyNewDataAdded(ArrayList<String> list) {
        this.arrayListRoles = list;
        notifyDataSetChanged();
    }

    public ArrayList<String> getItems() {
        return arrayListRoles;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewRoleName, textViewRoleSn;
        public ViewHolder(View view) {
            super(view);
            textViewRoleName=(TextView) view.findViewById(R.id.textViewRoleName);
            textViewRoleSn=(TextView) view.findViewById(R.id.textViewRoleSn);
        }
    }
    public void add(String role, int position) {
        arrayListRoles.add(position, role);
        notifyItemInserted(position);
    }
    public void remove(String item) {
        int position = arrayListRoles.indexOf(item);
        arrayListRoles.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item_role, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String list = arrayListRoles.get(position);
        Log.d("Divya", list);
        holder.textViewRoleName.setText(list);
        holder.textViewRoleSn.setText(""+(position+1)+".");
    }


    @Override
    public int getItemCount() {
        return arrayListRoles.size();
    }

    public View getView(int i, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if(true)
        {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.row_item_role,null);
            viewHolder = new ViewHolder(convertView);
            viewHolder.textViewRoleName = (TextView) convertView.findViewById(R.id.textViewRoleName);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textViewRoleName.setText(arrayListRoles.get(i));
        viewHolder.textViewRoleSn.setText(""+i);
        return convertView;
    }
}
