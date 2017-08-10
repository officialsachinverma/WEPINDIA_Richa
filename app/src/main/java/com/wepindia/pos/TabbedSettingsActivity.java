package com.wepindia.pos;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.fragments.FragmentSettingsGST;
import com.wepindia.pos.fragments.FragmentSettingsHeaderFooter;
import com.wepindia.pos.fragments.FragmentSettingsMachine;
import com.wepindia.pos.fragments.FragmentSettingsOther;
import com.wepindia.pos.fragments.FragmentSettingsPrice;
import com.wepindia.pos.fragments.FragmentSettingsPrint;
import com.wepindia.pos.fragments.Fragment_DisplayOwnerDetail;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabbedSettingsActivity extends WepBaseActivity{

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Context myContext;
    String strUserName = "";
    DatabaseHandler dbReportTab;
    MessageDialog MsgBox;
    ViewPagerAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        init();
        setupViewPager(viewPager);
        viewPager.addOnPageChangeListener (myOnPageChangeListener);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {
        dbReportTab = new DatabaseHandler(TabbedSettingsActivity.this);
        myContext = this;
        MsgBox = new MessageDialog(myContext);
        strUserName = getIntent().getStringExtra("USER_NAME");
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        com.wep.common.app.ActionBarUtils.setupToolbar(TabbedSettingsActivity.this,toolbar,getSupportActionBar(),"Settings",strUserName," Date:"+s.toString());
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle1=new Bundle();
        bundle1.putString("REPORT_TYPE", "1");
        FragmentSettingsHeaderFooter reportFragment1 = new FragmentSettingsHeaderFooter();
        reportFragment1.setArguments(bundle1);
        adapter.addFragment(reportFragment1, "Header Footer");

        Bundle bundle2=new Bundle();
        bundle2.putString("REPORT_TYPE", "2");
        FragmentSettingsPrice reportFragment2 = new FragmentSettingsPrice();
        reportFragment2.setArguments(bundle2);
        adapter.addFragment(reportFragment2, "Price");

        /*Bundle bundle3=new Bundle();
        bundle3.putString("REPORT_TYPE", "3");
        FragmentSettingsMiscellaneous reportFragment3 = new FragmentSettingsMiscellaneous();
        reportFragment3.setArguments(bundle3);*/
        //adapter.addFragment(reportFragment3, "Miscellaneous");
        //
        Bundle bundle3=new Bundle();
        bundle3.putString("REPORT_TYPE", "3");
        Fragment_DisplayOwnerDetail reportFragment3 = new Fragment_DisplayOwnerDetail();
        reportFragment3.setArguments(bundle3);
        adapter.addFragment(reportFragment3, "Owner Detail");



        Bundle bundle4=new Bundle();
        bundle4.putString("REPORT_TYPE", "4");
        FragmentSettingsOther reportFragment4 = new FragmentSettingsOther();
        reportFragment1.setArguments(bundle4);
        adapter.addFragment(reportFragment4, "Other");

        Bundle bundle5=new Bundle();
        bundle5.putString("REPORT_TYPE", "5");
        FragmentSettingsGST reportFragment5 = new FragmentSettingsGST();
        reportFragment5.setArguments(bundle5);
        adapter.addFragment(reportFragment5, "GST");

        Bundle bundle6=new Bundle();
        bundle6.putString("REPORT_TYPE", "6");
        FragmentSettingsMachine reportFragment6 = new FragmentSettingsMachine();
        reportFragment6.setArguments(bundle6);
        adapter.addFragment(reportFragment6, "Machine");


        Bundle bundle7=new Bundle();
        bundle7.putString("REPORT_TYPE", "7");
        FragmentSettingsPrint reportFragment7 = new FragmentSettingsPrint();
        reportFragment7.setArguments(bundle7);
        adapter.addFragment(reportFragment7, "Print");

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragmentList = new ArrayList<android.support.v4.app.Fragment>();
        private final List<String> mFragmentTitleList = new ArrayList<String>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    ViewPager.OnPageChangeListener myOnPageChangeListener =
            new ViewPager.OnPageChangeListener(){

                @Override
                public void onPageScrolled(final int i, final float v, final int i2) {
                }
                @Override
                public void onPageSelected(final int i) {
                    if(adapter!=null){
                        switch(i)
                        {

                           /* case 2:  FragmentSettingsMiscellaneous fragment = (FragmentSettingsMiscellaneous) adapter.instantiateItem(viewPager, i);
                                fragment.onResume();
                                break;*/
                        }

                    }

                }
                @Override
                public void onPageScrollStateChanged(final int i) {
                }
            };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            AlertDialog.Builder AuthorizationDialog = new AlertDialog.Builder(myContext);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setIcon(R.drawable.ic_launcher)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            /*Intent returnIntent = new Intent();
                            setResult(Activity.RESULT_OK, returnIntent);*/
                            dbReportTab.CloseDatabase();
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.wep.common.app.R.menu.menu_main, menu);
        for (int j = 0; j < menu.size(); j++) {
            MenuItem item = menu.getItem(j);
            item.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS);
        }
        return true;
    }

    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }else if (id == com.wep.common.app.R.id.action_home) {

        }else if (id == com.wep.common.app.R.id.action_screen_shot) {

        }
        return super.onOptionsItemSelected(item);
    }
}
