package com.wepindia.pos.GST;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.wepindia.pos.R;
import com.wepindia.pos.utils.ActionBarUtils;

import java.util.Date;

@SuppressWarnings("deprecation")
public class AmmendActivity extends TabActivity {

    Context myContext;
    String strUserName ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Remove default title bar
       // requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.activity_settings);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.application_title_bar);
        TextView tvTitleText = (TextView) findViewById(R.id.tvTitleBarCaption);
        TextView tvTitleUserName = (TextView) findViewById(R.id.tvTitleBarUserName);
        TextView tvTitleDate = (TextView) findViewById(R.id.tvTitleBarDate);
        ActionBarUtils.goBack(this, findViewById(R.id.imgTitleBackIcon));
        ActionBarUtils.goHome(this, findViewById(R.id.imgTitleHomeIcon));
        ActionBarUtils.takeScreenshot(this, findViewById(R.id.imgTitleScreenshotIcon), findViewById(R.id.lnrSettings));

        tvTitleText.setText("Ammend");
		/*
		 * getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
		 * R.layout.application_title_bar);
		 *
		 * TextView tvTitleText =
		 * (TextView)findViewById(R.id.tvTitleBarCaption);
		 * tvTitleText.setText("Settings");
		 */

        myContext = this;

        strUserName = getIntent().getStringExtra("USER_NAME");

        tvTitleUserName.setText(strUserName.toUpperCase());
        Date d = new Date();
        CharSequence s = DateFormat.format("dd-MM-yyyy", d.getTime());
        tvTitleDate.setText("Date : " + s);

        // Create a tab host
        TabHost tabSettings = getTabHost();

        // Header Footer tab
        TabSpec tabHeaderFooter = tabSettings.newTabSpec("GSTR1-B2CS");
        tabHeaderFooter.setIndicator("GSTR1-B2CS");
        tabHeaderFooter.setContent(new Intent(myContext, Ammend_b2cs_gstr1.class));

        // Dine In settings tab
        TabSpec tabDineInSettings = tabSettings.newTabSpec("DineInSettings");
        tabDineInSettings.setIndicator("GSTR2-B2B");
        tabDineInSettings.setContent(new Intent(myContext, Ammend_b2b_GSTR2.class));

        // Miscellaneous settings tab
        /*TabSpec tabMiscSettings = tabSettings.newTabSpec("MiscSettings");
        tabMiscSettings.setIndicator("Miscellaneous Settings");
        tabMiscSettings.setContent(new Intent(myContext, MiscellaneousSettingsActivity.class));*/

        /*// Machine settings tab
        TabSpec tabMachineSettings = tabSettings.newTabSpec("MachineSettings");
        tabMachineSettings.setIndicator("Machine Settings");
        tabMachineSettings.setContent(new Intent(myContext, MachineSettingsActivity.class));

        // Other settings tab
        TabSpec tabOtherSettings = tabSettings.newTabSpec("OtherSettings");
        tabOtherSettings.setIndicator("Other Settings");
        tabOtherSettings.setContent(new Intent(myContext, OtherSettingsActivity.class));

        // Backup settings tab
        TabSpec tabDbBackup = tabSettings.newTabSpec("Backup");
        tabDbBackup.setIndicator("Machine Settings");
        tabDbBackup.setContent(new Intent(myContext, DatabaseBackupActivity.class));

        // Mail settings tab
        TabSpec tabMail = tabSettings.newTabSpec("Mail");
        tabMail.setIndicator("Mail");
        tabMail.setContent(new Intent(myContext, MailConfigurationActivity.class));

        // Mail settings tab
        TabSpec tabPrint = tabSettings.newTabSpec("Print");
        tabPrint.setIndicator("Print");
        tabPrint.setContent(new Intent(myContext, MailConfigurationActivity.class));

        // GST settings tab
        TabSpec tabGST = tabSettings.newTabSpec("GST");
        tabGST.setIndicator("GST");
        tabGST.setContent(new Intent(myContext, GSTConfiguration_Activity.class));
*/
        // Add all the tabs to tab host
        tabSettings.addTab(tabHeaderFooter);
        tabSettings.addTab(tabDineInSettings);
        //tabSettings.addTab(tabMiscSettings);
//		tabSettings.addTab(tabMachineSettings);
  /*      tabSettings.addTab(tabOtherSettings);
        tabSettings.addTab(tabDbBackup);
        tabSettings.addTab(tabGST);
  */      //tabSettings.addTab(tabMail);
        /*tabSettings.addTab(tabPrint);*/
    }
}
