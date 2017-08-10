package com.wepindia.pos;


import android.app.AlertDialog;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.fragments.FragmentGSTLink;
import com.wepindia.pos.fragments.FragmentUpgradeSoftware;
import com.wepindia.pos.fragments.ReportFragment;
import com.wepindia.pos.utils.ActionBarUtils;
import com.wepindia.printers.WepPrinterBaseActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TabbedReportActivity extends WepPrinterBaseActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    Context myContext;
    String strUserName = "";
    DatabaseHandler dbReportTab;
    MessageDialog MsgBox;
    String GSTEnable = "", reportTab1 = "";
    public boolean isPrinterAvailable = false;
    private AppCompatDelegate delegate;


    public void onSohamsaPrinterResponses(String resp) {

    }

    public void connectionStatus(String statusTxt) {

    }

    public void onConfigurationRequired() {

    }

    public void onPrinterAvailable() {
        isPrinterAvailable = true;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_report);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        init();
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Nullable
    public ActionBarDrawerToggle.Delegate getDrawerToggleDelegate() {
        return null;
    }

    public boolean hasWindowFeature(int featureId) {
        return false;
    }

    @Nullable
    public ActionMode startSupportActionMode(@NonNull ActionMode.Callback callback) {
        return null;
    }

    public void installViewFactory() {

    }

    public View createView(@Nullable View parent, String name, @NonNull Context context, @NonNull AttributeSet attrs) {
        return null;
    }

    public void setHandleNativeActionModesEnabled(boolean enabled) {

    }

    public boolean isHandleNativeActionModesEnabled() {
        return false;
    }

    public boolean applyDayNight() {
        return false;
    }

    public void setLocalNightMode(int mode) {

    }

    private void init() {
        /*TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
        TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrReports));
        tvTitleText.setText("Reports");*/

        dbReportTab = new DatabaseHandler(TabbedReportActivity.this);
        myContext = this;
        MsgBox = new MessageDialog(myContext);
        strUserName = getIntent().getStringExtra("USER_NAME");

        //tvTitleUserName.setText(strUserName.toUpperCase());
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        //tvTitleDate.setText("Date : " + s);
        com.wep.common.app.ActionBarUtils.setupToolbar(TabbedReportActivity.this,toolbar,getSupportActionBar(),"Reports",strUserName," Date:"+s.toString());

        try {
            dbReportTab.CreateDatabase();
            dbReportTab.OpenDatabase();

            Cursor billsettingcursor = dbReportTab.getBillSetting();
            if ((billsettingcursor != null) && billsettingcursor.moveToFirst()) {
                GSTEnable = billsettingcursor.getString(billsettingcursor.getColumnIndex("GSTEnable"));
                if (GSTEnable == null) {
                    GSTEnable = "0";
                }
            }
            if (GSTEnable.equalsIgnoreCase("1")) {
                reportTab1 = "GST Report";
            } else {
                reportTab1 = "Sales Report";
            }
            dbReportTab.CloseDatabase();
        } catch (Exception e) {
            e.printStackTrace();
            MsgBox.Show("Error", e.getMessage());
        }
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        Bundle bundle1=new Bundle();
        bundle1.putString("REPORT_TYPE", "1");
        ReportFragment reportFragment1 = new ReportFragment();
        reportFragment1.setArguments(bundle1);
        adapter.addFragment(reportFragment1, reportTab1);

        Bundle bundle2=new Bundle();
        bundle2.putString("REPORT_TYPE", "2");
        ReportFragment reportFragment2 = new ReportFragment();
        reportFragment2.setArguments(bundle2);
        adapter.addFragment(reportFragment2, "Inventory Report");

        Bundle bundle3=new Bundle();
        bundle3.putString("REPORT_TYPE", "3");
        ReportFragment reportFragment3 = new ReportFragment();
        reportFragment3.setArguments(bundle3);
        adapter.addFragment(reportFragment3, "Employee/Customer Report");

        Bundle bundle4=new Bundle();
        bundle4.putString("REPORT_TYPE", "4");
        ReportFragment reportFragment4 = new ReportFragment();
        reportFragment4.setArguments(bundle4);
        adapter.addFragment(reportFragment4, "GST Reports");
        viewPager.setAdapter(adapter);

        Bundle bundle5=new Bundle();
        FragmentGSTLink reportFragment5 = new FragmentGSTLink();
        adapter.addFragment(reportFragment5, "GST Link");
        viewPager.setAdapter(adapter);

        FragmentUpgradeSoftware reportFragment6 = new FragmentUpgradeSoftware();
        adapter.addFragment(reportFragment6, "Upgrade Software");
        viewPager.setAdapter(adapter);
    }

    @Nullable
    public ActionMode onWindowStartingSupportActionMode(ActionMode.Callback callback) {
        return null;
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
