package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GST.fragments.Fragment_GSTR1_B2B_Amend;
import com.wepindia.pos.GST.fragments.Fragment_GSTR1_B2CS_Amend;
import com.wepindia.pos.GST.fragments.Fragment_GSTR1_B2CL_Amend;
import com.wepindia.pos.GST.fragments.Fragment_GSTR2_B2B_Amend;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabbedAmmendActivity extends WepBaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Context myContext;
    String strUserName = "";
    DatabaseHandler dbTabbedInward;
    MessageDialog MsgBox;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_credit_debit_note);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        init();
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void init() {
        dbTabbedInward = new DatabaseHandler(TabbedAmmendActivity.this);
        myContext = this;
        MsgBox = new MessageDialog(myContext);
        strUserName = getIntent().getStringExtra("USER_NAME");
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        com.wep.common.app.ActionBarUtils.setupToolbar(TabbedAmmendActivity.this,toolbar,getSupportActionBar(),
                "Amendments",strUserName," Date:"+s.toString());
    }

    private void setupViewPager(ViewPager viewPager) {
        TabbedAmmendActivity.ViewPagerAdapter adapter = new TabbedAmmendActivity.ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle1=new Bundle();
        bundle1.putString("REPORT_TYPE", "1");
        Fragment_GSTR2_B2B_Amend reportFragment1 = new Fragment_GSTR2_B2B_Amend();
        reportFragment1.setArguments(bundle1);
        adapter.addFragment(reportFragment1, "GSTR2_B2B ");

        Bundle bundle2=new Bundle();
        bundle2.putString("REPORT_TYPE", "2");
        Fragment_GSTR1_B2B_Amend reportFragment2 = new Fragment_GSTR1_B2B_Amend();
        reportFragment2.setArguments(bundle2);
        adapter.addFragment(reportFragment2, "GSTR1_B2B ");

        Bundle bundle3=new Bundle();
        bundle3.putString("REPORT_TYPE", "3");
        Fragment_GSTR1_B2CL_Amend reportFragment3 = new Fragment_GSTR1_B2CL_Amend();
        reportFragment3.setArguments(bundle3);
        adapter.addFragment(reportFragment3, "GSTR1_B2CL ");

        Bundle bundle4=new Bundle();
        bundle4.putString("REPORT_TYPE", "4");
        Fragment_GSTR1_B2CS_Amend reportFragment4 = new Fragment_GSTR1_B2CS_Amend();
        reportFragment4.setArguments(bundle4);
        adapter.addFragment(reportFragment4, "GSTR1_B2CS ");

        viewPager.setAdapter(adapter);
    }


    class ViewPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<Fragment>();
        private final List<String> mFragmentTitleList = new ArrayList<String>();

        public ViewPagerAdapter(android.support.v4.app.FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

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
                           dbTabbedInward.CloseDatabase();
                            finish();
                        }
                    })
                    .show();
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onHomePressed() {
        ActionBarUtils.navigateHome(this);    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
