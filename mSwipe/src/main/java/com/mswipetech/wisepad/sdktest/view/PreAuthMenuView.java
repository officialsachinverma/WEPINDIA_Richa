package com.mswipetech.wisepad.sdktest.view;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mswipetech.wisepad.R;

public class PreAuthMenuView extends BaseTitleActivity {
    public final static String log_tab = "PreAuthMenuView=>";


    CustomProgressDialog mProgressActivity = null;

    ApplicationData applicationData = null;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menuview);
        applicationData = (ApplicationData) getApplicationContext();
        initViews();

    }


    private void initViews() {
        ListView listView = (ListView) findViewById(R.id.menuview_LST_options);



        TextView txtHeading = (TextView) findViewById(R.id.topbar_LBL_heading);
        txtHeading.setText("Pre Auth  Menu");
        txtHeading.setTypeface(applicationData.font);
        String[] values =new String[]{"Pre auth sale","Pre auth completion"};
        MenuViewAdapter adapter = new MenuViewAdapter(this, values);
        int[] colors = {0, 0xFF0000FF, 0};
        listView.setDivider(new GradientDrawable(Orientation.LEFT_RIGHT, colors));
        listView.setDividerHeight(1);

        // Assign adapter to ListView
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = null;
                if(arg2 ==0)
                {
                    intent= new Intent(PreAuthMenuView.this, CreditSaleView.class);
                    intent.putExtra("isPreAuth", true);
                }else if(arg2 == 1){
                    intent= new Intent(PreAuthMenuView.this, PreAuthCompletion.class);
                    intent.putExtra("isPreAuth", true);
                }


                startActivity(intent);


            }

        });

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menuview, menu);
        return true;
    }


    public class MenuViewAdapter extends BaseAdapter {
        String[] listData = null;
        Context context;

        public MenuViewAdapter(Context context, String[] listData) {
            this.listData = listData;
            this.context = context;
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return listData.length;
        }

        @Override
        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(R.layout.menuviewlstitem, null);
            }


            TextView txtItem = (TextView) convertView.findViewById(R.id.menuview_lsttext);
            txtItem.setText(listData[position]);
            txtItem.setTypeface(applicationData.font);


            return convertView;
        }
    }
}
