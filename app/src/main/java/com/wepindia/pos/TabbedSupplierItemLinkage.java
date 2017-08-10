package com.wepindia.pos;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.WepBaseActivity;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.fragments.FragmentInwardStock;
import com.wepindia.pos.fragments.FragmentInwardSupply;

import com.wepindia.pos.fragments.FragmentSupplierDetails;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by RichaA on 3/15/2017.
 */
public class TabbedSupplierItemLinkage extends WepBaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Context myContext;
    String strUserName = "";
    DatabaseHandler dbTabbedSupplierItemLink;
    MessageDialog MsgBox;
    ViewPagerAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_inward_item_nongst);
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
        dbTabbedSupplierItemLink = new DatabaseHandler(TabbedSupplierItemLinkage.this);
        myContext = this;
        MsgBox = new MessageDialog(myContext);
        strUserName = getIntent().getStringExtra("USER_NAME");
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        com.wep.common.app.ActionBarUtils.setupToolbar(TabbedSupplierItemLinkage.this,toolbar,getSupportActionBar(),
                "Supplier Item Masters",strUserName," Date:"+s.toString());
    }

    private void setupViewPager(ViewPager viewPager) {
         adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle2=new Bundle();
        bundle2.putString("REPORT_TYPE", "2");
        FragmentInwardSupply reportFragment2 = new FragmentInwardSupply();
        reportFragment2.setArguments(bundle2);
        adapter.addFragment(reportFragment2, "Supplier Item Linkage");

        /*Bundle bundle1=new Bundle();
        bundle1.putString("REPORT_TYPE", "1");
        FragmentSupplierDetails reportFragment1 = new FragmentSupplierDetails();
        reportFragment1.setArguments(bundle1);
        adapter.addFragment(reportFragment1, "Supplier Details ");
*/
        Bundle bundle11=new Bundle();
        bundle11.putString("REPORT_TYPE", "1");
        FragmentInwardStock reportFragment11 = new FragmentInwardStock();
        reportFragment11.setArguments(bundle11);
        adapter.addFragment(reportFragment11, "Supplier Details ");

        viewPager.setAdapter(adapter);
        //adapter.notifyDataSetChanged();
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
                            case 0 : FragmentInwardSupply fragment0 = (FragmentInwardSupply) adapter.instantiateItem(viewPager, i);
                                fragment0.ClearItem1(null);
                                /*fragment0.loadAutoCompleteData();
                                fragment0.loadAutoCompleteData_item(-1);
                                fragment0.ClearingAndDisplaying();*/
                                break;

                            case 1:  FragmentSupplierDetails fragment = (FragmentSupplierDetails) adapter.instantiateItem(viewPager, i);
                                fragment.Clear();
                                fragment.Display();
                                    break;
                        }

                    }

                }
                @Override
                public void onPageScrollStateChanged(final int i) {
                }
    };

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
            LayoutInflater UserAuthorization = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View vwAuthorization = UserAuthorization.inflate(R.layout.user_authorization, null);
            final EditText txtUserId = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserId);
            final EditText txtPassword = (EditText) vwAuthorization.findViewById(R.id.etAuthorizationUserPassword);
            final TextView tvAuthorizationUserId = (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserId);
            final TextView tvAuthorizationUserPassword = (TextView) vwAuthorization.findViewById(R.id.tvAuthorizationUserPassword);
            tvAuthorizationUserId.setVisibility(View.GONE);
            tvAuthorizationUserPassword.setVisibility(View.GONE);
            txtUserId.setVisibility(View.GONE);
            txtPassword.setVisibility(View.GONE);
            AuthorizationDialog
                    .setTitle("Are you sure you want to exit ?")
                    .setView(vwAuthorization)
                    .setNegativeButton("No", null)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
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
