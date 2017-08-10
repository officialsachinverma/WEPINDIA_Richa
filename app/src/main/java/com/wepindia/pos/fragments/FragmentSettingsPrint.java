package com.wepindia.pos.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.wep.common.app.utils.Preferences;
import com.wepindia.pos.GenericClasses.MessageDialog;
import com.wepindia.pos.R;
import com.wepindia.printers.heydey.PrinterFragment;
import com.wepindia.printers.sohamsa.PrinterSohamsaActivity;

import java.util.ArrayList;

public class FragmentSettingsPrint extends Fragment {

    Context myContext;
    ArrayList<String> printersList;
    MessageDialog messageBox;
    private Spinner spinnerKot,spinnerBill,spinnerReport,spinnerReceipt;
    private SharedPreferences sharedPreferences;
    boolean statusKotTest = false,statusBillTest = false,statusReportTest = false;
    private SharedPreferences.Editor editor;
    public static final int PRINTING_REQUEST_CODE_SOHAMSA = 200;
    public static final int PRINTING_REQUEST_CODE_HEYDAY = 201;
    private Button saveKotPrint,resetKotPrint,saveBillPrint,resetBillPrint,saveReportPrint,resetReportPrint,saveReceiptPrint,resetReceiptPrint,btnClosePrinter;


    public FragmentSettingsPrint() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fragment_settings_print, container, false);
        myContext = getActivity();
        messageBox = new MessageDialog(myContext);
        sharedPreferences = Preferences.getSharedPreferencesForPrint(getActivity()); // getSharedPreferences("PrinterConfigurationActivity", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        printersList = new ArrayList<String>();
        printersList.add("--Select--");
        //printersList.add("Sohamsa");
        printersList.add("Heyday");
        spinnerKot = (Spinner) view.findViewById(R.id.spnr1);
        spinnerBill = (Spinner) view.findViewById(R.id.spnr2);
        spinnerReport = (Spinner) view.findViewById(R.id.spnr3);
        spinnerReceipt = (Spinner) view.findViewById(R.id.spnr4);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(myContext,android.R.layout.simple_spinner_item, printersList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //initSinglePrinter();
        spinnerKot.setAdapter(dataAdapter);
        spinnerKot.setSelection(getPosition(sharedPreferences.getString("kot","--Select--")));
        spinnerBill.setAdapter(dataAdapter);
        spinnerBill.setSelection(getPosition(sharedPreferences.getString("bill","--Select--")));
        spinnerReport.setAdapter(dataAdapter);
        spinnerReport.setSelection(getPosition(sharedPreferences.getString("report","--Select--")));
        spinnerReceipt.setAdapter(dataAdapter);
        spinnerReceipt.setSelection(getPosition(sharedPreferences.getString("receipt","--Select--")));
        saveKotPrint = (Button) view.findViewById(R.id.saveKotPrint);
        saveKotPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveKotPrint();
            }
        });
        resetKotPrint = (Button) view.findViewById(R.id.resetKotPrint);
        resetKotPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetKotPrint();
            }
        });

        saveBillPrint = (Button) view.findViewById(R.id.saveBillPrint);
        saveBillPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBillPrint();
            }
        });
        resetBillPrint = (Button) view.findViewById(R.id.resetBillPrint);
        resetBillPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetBillPrint();
            }
        });

        saveReportPrint = (Button) view.findViewById(R.id.saveReportPrint);
        saveReportPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReportPrint();
            }
        });
        resetReportPrint = (Button) view.findViewById(R.id.resetReportPrint);
        resetReportPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetReportPrint();
            }
        });

        saveReceiptPrint = (Button) view.findViewById(R.id.saveReceiptPrint);
        saveReceiptPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReceiptPrint();
            }
        });
        resetReceiptPrint = (Button) view.findViewById(R.id.resetReceiptPrint);
        resetReceiptPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetReceiptPrint();
            }
        });

        btnClosePrinter = (Button) view.findViewById(R.id.btnClosePrinter);
        btnClosePrinter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Close();
            }
        });
        return view;
    }

   /* private void initSinglePrinter() {
        SharedPreferences sharedPreferences = Preferences.getSharedPreferencesForPrint(getActivity()); // getSharedPreferences("PrinterConfigurationActivity", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("kot","Heyday");
        editor.putString("bill","Heyday");
        editor.putString("report","Heyday");
        editor.putString("receipt","Heyday");
        editor.commit();
    }*/

    public void saveKotPrint() {
        String str = printersList.get(spinnerKot.getSelectedItemPosition());
        if(str.equalsIgnoreCase("--Select--"))
        {
            messageBox.Show("Select a Printer","In order to print please select a printer");
        }
        else if(str.equalsIgnoreCase("Sohamsa"))
        {
            //testPrintSohamsa("kot",0);
            Toast.makeText(getActivity(), "KOT Printer Saved Successfully", Toast.LENGTH_SHORT).show();
            editor.putString("kot","Sohamsa");
            editor.commit();
        }
        else if(str.equalsIgnoreCase("Heyday"))
        {
            //testPrintHeyDay("kot",0);
            Toast.makeText(getActivity(), "KOT Printer Saved Successfully", Toast.LENGTH_SHORT).show();
            editor.putString("kot","Heyday");
            editor.commit();
        }
    }

    public void saveBillPrint() {
        String str = printersList.get(spinnerBill.getSelectedItemPosition());
        if(str.equalsIgnoreCase("--Select--"))
        {
            messageBox.Show("Select a Printer","In order to print please select a printer");
        }
        else if(str.equalsIgnoreCase("Sohamsa"))
        {
            //testPrintSohamsa("bill",1);
            Toast.makeText(getActivity(), "Billing Printer Configured Successfully", Toast.LENGTH_SHORT).show();
            editor.putString("bill","Sohamsa");
            editor.commit();
        }
        else if(str.equalsIgnoreCase("Heyday"))
        {
            //testPrintHeyDay("bill",1);
            Toast.makeText(getActivity(), "Billing Printer Configured Successfully", Toast.LENGTH_SHORT).show();
            editor.putString("bill","Heyday");
            //editor.putInt("printer",data.getIntExtra("printer",0));
            editor.commit();
        }
    }

    public void saveReportPrint() {
        String str = printersList.get(spinnerReport.getSelectedItemPosition());
        if(str.equalsIgnoreCase("--Select--"))
        {
            messageBox.Show("Select a Printer","In order to print please select a printer");
        }
        else if(str.equalsIgnoreCase("Sohamsa"))
        {
            //testPrintSohamsa("report",2);
            Toast.makeText(getActivity(), "Report Printer Configured Successfully", Toast.LENGTH_SHORT).show();
            editor.putString("report","Sohamsa");
            editor.commit();
        }
        else if(str.equalsIgnoreCase("Heyday"))
        {
            //testPrintHeyDay("report",2);
            Toast.makeText(getActivity(), "Report Printer Configured Successfully", Toast.LENGTH_SHORT).show();
            editor.putString("report","Heyday");
            //editor.putInt("printer",data.getIntExtra("printer",0));
            editor.commit();
        }
    }

    public void saveReceiptPrint() {
        String str = printersList.get(spinnerReceipt.getSelectedItemPosition());
        if(str.equalsIgnoreCase("--Select--"))
        {
            messageBox.Show("Select a Printer","In order to print please select a printer");
        }
        else if(str.equalsIgnoreCase("Sohamsa"))
        {
            //testPrintSohamsa("report",3);
            Toast.makeText(getActivity(), "Receipt Printer Configured Successfully", Toast.LENGTH_SHORT).show();
            editor.putString("receipt","Sohamsa");
            editor.commit();
        }
        else if(str.equalsIgnoreCase("Heyday"))
        {
            //testPrintHeyDay("report",3);
            Toast.makeText(getActivity(), "Receipt Printer Configured Successfully", Toast.LENGTH_SHORT).show();
            editor.putString("receipt","Heyday");
            //editor.putInt("printer",data.getIntExtra("printer",0));
            editor.commit();
        }
    }

    public void resetKotPrint() {
        editor.putString("kot","--Select--");
        editor.commit();
        spinnerKot.setSelection(0);
    }

    public void resetBillPrint() {
        editor.putString("bill","--Select--");
        editor.commit();
        spinnerBill.setSelection(0);
    }

    public void resetReportPrint() {
        editor.putString("report","--Select--");
        editor.commit();
        spinnerReport.setSelection(0);
    }

    public void resetReceiptPrint() {
        editor.putString("receipt","--Select--");
        editor.commit();
        spinnerReceipt.setSelection(0);
    }

    public int getPosition(String str){
        if(str.equalsIgnoreCase("--Select--"))
        {
            return 0;
        }
        else if(str.equalsIgnoreCase("Heyday"))
        {
            return 1;
        }
        else
            return -1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null && requestCode == PRINTING_REQUEST_CODE_SOHAMSA)
        {
            if(data.getStringExtra("name").equalsIgnoreCase("kot"))
            {
                Toast.makeText(getActivity(), "KOT Printer Configured Successfully", Toast.LENGTH_SHORT).show();
                editor.putString("kot","Sohamsa");
                editor.commit();

            }
            else if(data.getStringExtra("name").equalsIgnoreCase("bill"))
            {
                Toast.makeText(getActivity(), "Billing Printer Configured Successfully", Toast.LENGTH_SHORT).show();
                editor.putString("bill","Sohamsa");
                editor.commit();

            }
            else if(data.getStringExtra("name").equalsIgnoreCase("report"))
            {
                Toast.makeText(getActivity(), "Report Printer Configured Successfully", Toast.LENGTH_SHORT).show();
                editor.putString("report","Sohamsa");
                editor.commit();
            }
            else if(data.getStringExtra("name").equalsIgnoreCase("receipt"))
            {
                Toast.makeText(getActivity(), "Receipt Printer Configured Successfully", Toast.LENGTH_SHORT).show();
                editor.putString("receipt","Sohamsa");
                editor.commit();
            }
        }
        else if(data!=null && requestCode == PRINTING_REQUEST_CODE_HEYDAY)
        {
            if(data.getStringExtra("name").equalsIgnoreCase("kot"))
            {
                Toast.makeText(getActivity(), "KOT Printer Configured Successfully", Toast.LENGTH_SHORT).show();
                editor.putString("kot","Heyday");
                editor.putInt("printer",data.getIntExtra("printer",0));
                editor.commit();
            }
            else if(data.getStringExtra("name").equalsIgnoreCase("bill"))
            {
                Toast.makeText(getActivity(), "Billing Printer Configured Successfully", Toast.LENGTH_SHORT).show();
                editor.putString("bill","Heyday");
                editor.putInt("printer",data.getIntExtra("printer",0));
                editor.commit();
            }
            else if(data.getStringExtra("name").equalsIgnoreCase("report"))
            {
                Toast.makeText(getActivity(), "Report Printer Configured Successfully", Toast.LENGTH_SHORT).show();
                editor.putString("report","Heyday");
                editor.putInt("printer",data.getIntExtra("printer",0));
                editor.commit();
            }
            else if(data.getStringExtra("name").equalsIgnoreCase("receipt"))
            {
                Toast.makeText(getActivity(), "Receipt Printer Configured Successfully", Toast.LENGTH_SHORT).show();
                editor.putString("receipt","Heyday");
                editor.putInt("printer",data.getIntExtra("printer",0));
                editor.commit();
            }
        }
    }

    protected void testPrintSohamsa(String name,int num) {
        Intent intent = new Intent(getActivity(), PrinterSohamsaActivity.class);
        intent.putExtra("printType", "TEST");
        intent.putExtra("code", PRINTING_REQUEST_CODE_SOHAMSA);
        intent.putExtra("name", name);
        intent.putExtra("printerNum", num);
        startActivityForResult(intent,PRINTING_REQUEST_CODE_SOHAMSA);
    }
    protected void testPrintHeyDay(String name,int num) {
        Intent intent = new Intent(getActivity(), PrinterFragment.class);
        intent.putExtra("printType", "TEST");
        intent.putExtra("code", PRINTING_REQUEST_CODE_HEYDAY);
        intent.putExtra("name", name);
        intent.putExtra("printerNum", num);
        startActivityForResult(intent,PRINTING_REQUEST_CODE_HEYDAY);
        //Toast.makeText(this, "wefh3yir", Toast.LENGTH_SHORT).show();
    }

    public void Close()
    {
        getActivity().finish();
    }
}
