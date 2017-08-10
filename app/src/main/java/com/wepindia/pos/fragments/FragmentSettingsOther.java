package com.wepindia.pos.fragments;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;

import com.wep.common.app.Database.BillSetting;
import com.wep.common.app.Database.DatabaseHandler;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;

public class FragmentSettingsOther extends Fragment {

    Context myContext;
    DatabaseHandler dbOtherSettings;
    MessageDialog MsgBox;// = new MessageDialog(DineInSettingsActivity.this);
    RadioButton rbDateTimeAuto, rbDateTimeManual, rbPriceChangeEnable, rbPriceChangeDisable;
    RadioButton rbBillwithStockEnable, rbBillwithStockDisable;
    RadioButton rbTaxForward, rbTaxReverse, rbTaxTypeItemwise, rbTaxTypeBillwise,rbDiscountItemwise,rbDiscountBillwise;
    RadioButton rbFastBillingItems, rbFastBillingDeptItems, rbFastBillingDeptCategItems;
    RadioButton rbBillNoResetEnable, rbBillNoResetDisable;
    RadioButton rbItemNoResetAuto, rbItemNoResetManual;
    RadioButton rbPrintPreviewEnable, rbPrintPreviewDisable;
    Button btnApply, btnClose;
    RadioButton rbCummulativeHeadingEnable, rbCummulativeHeadingDisable; // richa_2012
    RadioButton rbTableSplitingEnable, rbTableSplitingDisable;
    BillSetting objBillSettings = new BillSetting();
    Button btnApplyOtherSettings,btnCloseOtherSettings;


    public FragmentSettingsOther() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            dbOtherSettings = new DatabaseHandler(getActivity());
            dbOtherSettings.OpenDatabase();
        }catch (Exception e){

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_settings_other, container, false);
        myContext = getActivity();

        MsgBox = new MessageDialog(myContext);

        try {
            InitializeViews(view);
            dbOtherSettings.CloseDatabase();
            dbOtherSettings.CreateDatabase();
            dbOtherSettings.OpenDatabase();
            DisplaySettings();
        } catch (Exception exp) {
            exp.printStackTrace();
            MsgBox.Show("Exception", exp.getMessage());
        }
        btnApplyOtherSettings = (Button) view.findViewById(R.id.btnApplyOtherSettings);
        btnApplyOtherSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Apply();
            }
        });

        btnCloseOtherSettings = (Button) view.findViewById(R.id.btnCloseOtherSettings);
        btnCloseOtherSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close();
            }
        });
        return view;
    }

    private void InitializeViews(View view) {

        rbDateTimeAuto = (RadioButton) view.findViewById(R.id.rbDateTimeAuto);
        rbDateTimeManual = (RadioButton) view.findViewById(R.id.rbDateTimeManual);

        rbPriceChangeEnable = (RadioButton) view.findViewById(R.id.rbPriceChangeEnable);
        rbPriceChangeDisable = (RadioButton) view.findViewById(R.id.rbPriceChangeDisable);

        rbBillwithStockEnable = (RadioButton) view.findViewById(R.id.rbBillwithStockEnable);
        rbBillwithStockDisable = (RadioButton) view.findViewById(R.id.rbBillwithStockDisable);

        rbTaxForward = (RadioButton) view.findViewById(R.id.rbTaxForward);
        rbTaxReverse = (RadioButton) view.findViewById(R.id.rbTaxReverse);

        rbTaxTypeItemwise = (RadioButton) view.findViewById(R.id.rbTaxTypeItemwise);
        rbTaxTypeBillwise = (RadioButton) view.findViewById(R.id.rbTaxTypeBillwise);

        rbDiscountItemwise = (RadioButton) view.findViewById(R.id.rbDiscountItemwise);
        rbDiscountBillwise = (RadioButton) view.findViewById(R.id.rbDiscountBillwise);

        btnApply = (Button) view.findViewById(R.id.btnApplyOtherSettings);
        btnClose = (Button) view.findViewById(R.id.btnCloseOtherSettings);

        rbFastBillingItems = (RadioButton) view.findViewById(R.id.rbFastBillingItems);
        rbFastBillingDeptItems = (RadioButton) view.findViewById(R.id.rbFastBillingDeptItems);
        rbFastBillingDeptCategItems = (RadioButton) view.findViewById(R.id.rbFastBillingDeptCategItems);

        rbBillNoResetEnable = (RadioButton) view.findViewById(R.id.rbBillNoResetEnable);
        rbBillNoResetDisable = (RadioButton) view.findViewById(R.id.rbBillNoResetDisable);

        rbItemNoResetAuto = (RadioButton) view.findViewById(R.id.rbItemNoResetAuto);
        rbItemNoResetManual = (RadioButton) view.findViewById(R.id.rbItemNoResetManual);

        rbPrintPreviewEnable = (RadioButton) view.findViewById(R.id.rbPrintPreviewEnable);
        rbPrintPreviewDisable = (RadioButton) view.findViewById(R.id.rbPrintPreviewDisable);

        rbTableSplitingEnable = (RadioButton) view.findViewById(R.id.rbTableSplitingEnable);
        rbTableSplitingDisable = (RadioButton) view.findViewById(R.id.rbTableSplitingDisable);

        //richa_2012
        rbCummulativeHeadingEnable = (RadioButton) view.findViewById(R.id.rbCummulativeHeadingEnable);
        rbCummulativeHeadingDisable = (RadioButton) view.findViewById(R.id.rbCummulativeHeadingDisable);
    }

    @Override
    public void onResume() {
        super.onResume();
        DisplaySettings();
    }

    private void DisplaySettings() {
        Cursor crsrBillSetting = null;
        Cursor crsrBillNoResetSetting = null;
        // Read the settings from database and display it in views.
        crsrBillSetting = dbOtherSettings.getBillSetting();
        crsrBillNoResetSetting = dbOtherSettings.getBillNoResetSetting();

        if(crsrBillNoResetSetting.moveToFirst())
        {
            if (crsrBillNoResetSetting.getString(crsrBillNoResetSetting.getColumnIndex("Period")).equalsIgnoreCase("Enable")) {
                rbBillNoResetEnable.setChecked(true);
            } else {
                rbBillNoResetDisable.setChecked(true);
            }
        } else {
            Log.d("OtherSettings", "No data in BillNoResetSettings table");
        }

        if (crsrBillSetting.moveToFirst()) {

            // Date And Time
            int auto = crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("DateAndTime"));
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("DateAndTime")) == 1) {
                rbDateTimeAuto.setChecked(true);
            } else {
                rbDateTimeManual.setChecked(true);
            }

            // Price Change
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("PriceChange")) == 1) {
                rbPriceChangeEnable.setChecked(true);
            } else {
                rbPriceChangeDisable.setChecked(true);
            }

            // Bill with Stock
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("BillwithStock")) == 1) {
                rbBillwithStockEnable.setChecked(true);
            } else {
                rbBillwithStockDisable.setChecked(true);
            }

            // Tax
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("Tax")) == 1) {
                rbTaxForward.setChecked(true);
            } else {
                rbTaxReverse.setChecked(true);
            }

            // Tax Type
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("TaxType")) == 1) {
                rbTaxTypeItemwise.setChecked(true);
            } else {
                rbTaxTypeBillwise.setChecked(true);
            }
            // Discount Type
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("DiscountType")) == 1) {
                rbDiscountItemwise.setChecked(true);
            } else {
                rbDiscountBillwise.setChecked(true);
            }

            // Fast Billing Mode
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("FastBillingMode")) == 1) {
                rbFastBillingItems.setChecked(true);
            } else if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("FastBillingMode")) == 2) {
                rbFastBillingDeptItems.setChecked(true);
            } else {
                rbFastBillingDeptCategItems.setChecked(true);
            }

            // Item No Reset
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("ItemNoReset")) == 0) {
                rbItemNoResetAuto.setChecked(true);
            } else {
                rbItemNoResetManual.setChecked(true);
            }

            // Print Preview
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("PrintPreview")) == 1) {
                rbPrintPreviewEnable.setChecked(true);
            } else {
                rbPrintPreviewDisable.setChecked(true);
            }

            // Print Preview
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("TableSpliting")) == 1) {
                rbTableSplitingEnable.setChecked(true);
            } else {
                rbTableSplitingDisable.setChecked(true);
            }

            //richa_2012
            // CummulativeHeading Enable
            if (crsrBillSetting.getInt(crsrBillSetting.getColumnIndex("CummulativeHeadingEnable")) == 1) {
                rbCummulativeHeadingEnable.setChecked(true);
            } else {
                rbCummulativeHeadingDisable.setChecked(true);
            }

        } else {
            Log.d("OtherSettings", "No data in BillSettings table");
        }
    }

    private void ReadOtherSettings() {

        // Local variables
        int iLoginWith = 0, iPeripherals = 0;
        int iDateAndTime = 0, iPriceChange = 0, iBillwithStock = 0, iBillwithoutStock = 0, iTax = 0, iTaxType = 0,iDiscountType =0;
        int iKOT = 0, iToken = 0, iKitchen = 0;
        int iOtherChargesItemwise = 0, iOtherChargesBillwise = 0, iRestoreDefault = 0;
        int fastBillingMode = 0, iItemNoReset = 0, iPrintPreview = 0, iTableSpliting = 0;
        int CummulativeHeadingEnable = 0; //richa_2012

        // Date And Time
        if (rbDateTimeAuto.isChecked() == true) {
            iDateAndTime = 1;
        } else {
            iDateAndTime = 0;
        }

        // Price Change
        if (rbPriceChangeEnable.isChecked() == true) {
            iPriceChange = 1;
        } else {
            iPriceChange = 0;
        }

        // BillwithStock
        if (rbBillwithStockEnable.isChecked() == true) {
            iBillwithStock = 1;
        } else {
            iBillwithStock = 0;
        }

        // Tax
        if (rbTaxForward.isChecked() == true) {
            iTax = 1;
        } else {
            iTax = 0;
        }

        // TaxType
        if (rbTaxTypeItemwise.isChecked() == true) {
            iTaxType = 1;
        } else {
            iTaxType = 0;
        }


        // DiscountType
        if (rbDiscountItemwise.isChecked() == true) {
            iDiscountType = 1;
        } else {
            iDiscountType = 0;
        }

        // Fast Billing Mode
        if (rbFastBillingItems.isChecked() == true) {
            fastBillingMode = 1;
        } else if (rbFastBillingDeptItems.isChecked() == true) {
            fastBillingMode = 2;
        } else {
            fastBillingMode = 3;
        }

        // Item No Reset
        if (rbItemNoResetAuto.isChecked() == true) {
            iItemNoReset = 0;
        } else {
            iItemNoReset = 1;
        }

        // Print Preview
        if (rbPriceChangeEnable.isChecked() == true) {
            iPrintPreview = 1;
        } else {
            iPrintPreview = 0;
        }

        // Table Spliting
        if (rbTableSplitingEnable.isChecked() == true) {
            iTableSpliting = 1;
        } else {
            iTableSpliting = 0;
        }

        // richa_2012
        // CuumulativeHeading Enable
        if (rbCummulativeHeadingEnable.isChecked() == true) {
            CummulativeHeadingEnable = 1;
        } else {
            CummulativeHeadingEnable = 0;
        }

        // Initialize all the settings variable
        objBillSettings.setLoginWith(0);
        objBillSettings.setDateAndTime(iDateAndTime);
        objBillSettings.setPriceChange(iPriceChange);
        objBillSettings.setBillwithStock(iBillwithStock);
        objBillSettings.setBillwithoutStock(0);
        objBillSettings.setTax(iTax);
        objBillSettings.setTaxType(iTaxType);
        objBillSettings.setDiscountType(iDiscountType);
        objBillSettings.setKOT(0);
        objBillSettings.setToken(0);
        objBillSettings.setKitchen(0);
        objBillSettings.setOtherChargesItemwise(0);
        objBillSettings.setOtherChargesBillwise(0);
        objBillSettings.setPeripherals(0);
        objBillSettings.setRestoreDefault(0);
        objBillSettings.setFastBillingMode(fastBillingMode);
        objBillSettings.setItemNoReset(iItemNoReset);
        objBillSettings.setPrintPreview(iPrintPreview);
        objBillSettings.setTableSpliting(iTableSpliting);
        objBillSettings.setCummulativeHeadingEnable(CummulativeHeadingEnable); // richa_2012
    }

    private void SaveOtherSettings() {
        int iResult = 0;

        // Update new settings in database
        iResult = dbOtherSettings.updateOtherSettings(objBillSettings);

        if (iResult > 0) {
            // Toast.makeText(myContext, "Settings saved",
            // Toast.LENGTH_LONG).show();
            MsgBox.Show("Information", "Settings saved");
        } else {
            // Toast.makeText(myContext, "Apply Settings failed - Updated row:"
            // + String.valueOf(iResult), Toast.LENGTH_LONG).show();
            MsgBox.Show("Warning", "Failed to save settings");
        }
    }

    public void Apply() {
        // Read data to update
        ReadOtherSettings();

        // Save data in database
        SaveOtherSettings();

        // Bill no Reset Update
        String strPeriod = "";
        int iResult = 0;
        if(rbBillNoResetEnable.isChecked() == true) {
            strPeriod = "Enable";
            iResult = dbOtherSettings.UpdateBillNoReset(strPeriod);
        } else if(rbBillNoResetDisable.isChecked() == true) {
            strPeriod = "Disable";
            iResult = dbOtherSettings.UpdateBillNoResetPeriod(strPeriod);
        }
        // Display saved data
        // DisplaySettings();
    }

    public void Close() {
        dbOtherSettings.CloseDatabase();
        getActivity().finish();
    }
}
