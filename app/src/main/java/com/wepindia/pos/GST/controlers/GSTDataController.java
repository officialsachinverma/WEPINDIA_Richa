package com.wepindia.pos.GST.controlers;

import android.content.Context;
import android.database.Cursor;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.gst.B2Csmall;
import com.wep.common.app.gst.GSTR1_CDN_Data;
import com.wep.common.app.gst.GSTR1_B2B_Data;
import com.wep.common.app.gst.GSTR1B2CSAData;
import com.wep.common.app.gst.GSTR1_CDN_Details;
import com.wep.common.app.gst.GSTR1_B2B_A_Data;
import com.wep.common.app.gst.GSTR1_B2B_A_invoices;
import com.wep.common.app.gst.GSTR1_B2B_invoices;
import com.wep.common.app.gst.GSTR1_B2B_items;
import com.wep.common.app.gst.GSTR1_B2CL_A_Data;
import com.wep.common.app.gst.GSTR1_B2CL_A_invoices;
import com.wep.common.app.gst.GSTR1_B2CL_Data;
import com.wep.common.app.gst.GSTR1_B2CL_invoices;
import com.wep.common.app.gst.GSTR1_B2CL_item_details;
import com.wep.common.app.gst.GSTR1_B2CL_items;
import com.wep.common.app.gst.GSTR1_HSN_Data;
import com.wep.common.app.gst.GSTR1_HSN_Details;
import com.wep.common.app.gst.GSTR2_CDN_Data;
import com.wep.common.app.gst.GSTR1_B2B_item_details;
import com.wep.common.app.gst.GSTR2_B2B_A_Data_Unregistered;
import com.wep.common.app.gst.GSTR2_B2B_A_Data_registered;
import com.wep.common.app.gst.GSTR2_B2B_A_invoices_Unregistered;
import com.wep.common.app.gst.GSTR2_B2B_A_invoices_registered;
import com.wep.common.app.gst.GSTR2_B2B_Data_Unregistered;
import com.wep.common.app.gst.GSTR2_B2B_Data_registered;
import com.wep.common.app.gst.GSTR2_B2B_ITC_details;
import com.wep.common.app.gst.GSTR2_B2B_invoices_Unregistered;
import com.wep.common.app.gst.GSTR2_B2B_invoices_registered;
import com.wep.common.app.gst.GSTR2_B2B_item_details;
import com.wep.common.app.gst.GSTR2_B2B_items;
import com.wep.common.app.gst.GSTR2_CDN_Details;
import com.wep.common.app.gst.GSTR2_ITC_Details;
import com.wepindia.pos.GenericClasses.MessageDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by PriyabratP on 24-11-2016.
 */

public class GSTDataController {

    private final MessageDialog MsgBox;
    private DatabaseHandler dbReport;
    private Context myContext;

    public GSTDataController(Context context, DatabaseHandler dbReport) {
        this.myContext = context;
        this.dbReport = dbReport;
        MsgBox  = new MessageDialog(myContext);
    }


    public ArrayList<com.wep.common.app.gst.B2Csmall> getGSTR1B2CSDataList(String StartDate, String EndDate) {
        String GSTEnable = "1", POSEnable = "1", HSNEnable = "1", ReverseChargeEnabe = "0";
        ArrayList<com.wep.common.app.gst.B2Csmall> datalist_s = new ArrayList<com.wep.common.app.gst.B2Csmall>();
        try {
            /*Cursor billsettingcursor = dbReport.getBillSetting();
            if (billsettingcursor != null && billsettingcursor.moveToFirst()) {
                GSTEnable = billsettingcursor.getString(billsettingcursor.getColumnIndex("GSTEnable"));
                if (GSTEnable == null) {
                    GSTEnable = "0";
                } else if (GSTEnable.equals("1")) {
                    HSNEnable = billsettingcursor.getString(billsettingcursor.getColumnIndex("HSNCode_Out"));
                    if (HSNEnable == null) {
                        HSNEnable = "0";
                    }
                    POSEnable = billsettingcursor.getString(billsettingcursor.getColumnIndex("POS_Out"));
                    if (POSEnable == null) {
                        POSEnable = "0";
                    }
                    ReverseChargeEnabe = billsettingcursor.getString(billsettingcursor.getColumnIndex("ReverseCharge_Out"));
                    if (ReverseChargeEnabe == null) {
                        ReverseChargeEnabe = "0";
                    }
                }
            }*/
            //String StartDate = txtReportDateStart.getText().toString();
            //String EndDate = txtReportDateEnd.getText().toString();
            Cursor cursor = dbReport.getOutwardB2Cs(StartDate, EndDate);
            if (cursor == null) {
                //MsgBox = new AlertDialog.Builder(myContext);
                MsgBox.setMessage("No data for entered period B2CS")
                        .setPositiveButton("OK", null)
                        .show();
            } else {
                if (cursor.moveToFirst()) {

                    int count = 1;

                    do {// item_detail table
                        String stateCode = cursor.getString(cursor.getColumnIndex("CustStateCode"));
                        if(stateCode== null)
                            stateCode = "";
                        float TaxableValue_f = Float.parseFloat(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                        if ((stateCode.equals("")) || (stateCode.equals("29"))|| (!(stateCode.equals("") && TaxableValue_f <= 250000)) || (!(stateCode.equals("29") && TaxableValue_f <= 250000))) {
                            // for intrastate + interstate only  <=2.5L
                            String InvNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                            String InvDate = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                            String custname_str = cursor.getString(cursor.getColumnIndex("CustName"));
                            String statecode_str = cursor.getString(cursor.getColumnIndex("CustStateCode"));

                            Cursor rowcursor = dbReport.getitems_b2cl(InvNo, InvDate, custname_str, statecode_str);
                            if (rowcursor == null) {
                                //MsgBox = new AlertDialog.Builder(myContext);
                                MsgBox.setMessage("No items for Invoice No : " + InvNo + " & Invoice Date : " + InvDate)
                                        .setPositiveButton("OK", null)
                                        .show();
                            } else { // bill level

                                try {

                                    while (rowcursor.moveToNext()) {

                                        String SupplyType_str;
                                        double subtotal_f, taxablevalue_f, CGSTRate_f, CGSTAmount_f, SGSTRate_f;
                                        double SGSTAmount_f, IGSTRate_f, IGSTAmount_f;
                                        int found = 0;

                                        // supply type (g/s)
                                        SupplyType_str = rowcursor.getString(rowcursor.getColumnIndex("SupplyType"));

                                        String HSN = rowcursor.getString(rowcursor.getColumnIndex("HSNCode"));
                                        String desc = rowcursor.getString(rowcursor.getColumnIndex("ItemName"));
                                        HSN = HSN + "-" + desc;
                                        //HSNCode_str = HSN;


                                        String CustStateCode_str  = rowcursor.getString(rowcursor.getColumnIndex("CustStateCode"));
                                        //String POS_str1 = rowcursor.getString(rowcursor.getColumnIndex("POS"));


                                        taxablevalue_f = Float.parseFloat(rowcursor.getString(rowcursor.getColumnIndex("TaxableValue")));

                                        String igstrate_str = rowcursor.getString(rowcursor.getColumnIndex("IGSTRate"));
                                        if (igstrate_str == null) {
                                            IGSTRate_f = 0;
                                        } else {
                                            IGSTRate_f = Float.parseFloat(igstrate_str);
                                        }

                                        String igstamt_str = rowcursor.getString(rowcursor.getColumnIndex("IGSTAmount"));
                                        if (igstamt_str == null) {
                                            IGSTAmount_f = 0;
                                        } else {
                                            IGSTAmount_f = Float.parseFloat(igstamt_str);
                                        }

                                        String cgstrate_str = rowcursor.getString(rowcursor.getColumnIndex("CGSTRate"));
                                        if (cgstrate_str == null) {
                                            CGSTRate_f = 0;
                                        } else {
                                            CGSTRate_f = Double.parseDouble(cgstrate_str);
                                        }

                                        String cgstamt_str = rowcursor.getString(rowcursor.getColumnIndex("CGSTAmount"));
                                        if (cgstamt_str == null) {
                                            CGSTAmount_f = 0;
                                        } else {
                                            CGSTAmount_f = Double.parseDouble(cgstamt_str);
                                        }

                                        String sgstrate_str = rowcursor.getString(rowcursor.getColumnIndex("SGSTRate"));
                                        if (sgstrate_str == null) {
                                            SGSTRate_f = 0;
                                        } else {
                                            SGSTRate_f = Float.parseFloat(sgstrate_str);
                                        }

                                        String sgstamt_str = rowcursor.getString(rowcursor.getColumnIndex("SGSTAmount"));
                                        if (sgstamt_str == null) {
                                            SGSTAmount_f = 0;
                                        } else {
                                            SGSTAmount_f = Double.parseDouble(sgstamt_str);
                                        }

                                        double cessamount = 0;
                                        String cessamt_str = rowcursor.getString(rowcursor.getColumnIndex("cessAmount"));
                                        if (cessamt_str == null) {
                                            cessamount = 0;
                                        } else {
                                            cessamount = Double.parseDouble(cessamt_str);
                                        }


                                        String cessRate ="0";
                                        String Orderno="0";
                                        String OrderDate="0";
                                        String etin="";
                                        String eType = "";
                                        String ProAss = "";

                                        subtotal_f = Double.parseDouble(rowcursor.getString(rowcursor.getColumnIndex("SubTotal")));


                                        double gstrate = IGSTRate_f + CGSTRate_f+SGSTRate_f;
                                        B2Csmall obj = new B2Csmall();
                                        obj.setSupplyType(SupplyType_str);
                                        obj.setHSNCode(HSN);
                                        obj.setStateCode(stateCode);
                                        //obj.setPlaceOfSupply(POS_str);
                                        obj.setTaxableValue(taxablevalue_f);
                                        obj.setIGSTRate(IGSTRate_f);
                                        obj.setIGSTAmt(IGSTAmount_f);
                                        obj.setCGSTRate(CGSTRate_f);
                                        obj.setCGSTAmt(CGSTAmount_f);
                                        obj.setSGSTRate(SGSTRate_f);
                                        obj.setSGSTAmt(SGSTAmount_f);
                                        obj.setGSTRate(gstrate);
                                        obj.setProAss(ProAss);
                                        obj.setSubTotal(subtotal_f);
                                        obj.setCessRate(cessRate);
                                        obj.setCessAmt(cessamount);
                                        obj.setOrderno(Orderno);
                                        obj.setOrderDate(OrderDate);
                                        obj.setEtin(etin);
                                        obj.setEtype(eType);

                                        if (datalist_s.size() == 0) // empty list
                                        {
                                            datalist_s.add(obj);
                                        } else {
                                            found = 0;
                                            for (B2Csmall data_s : datalist_s) {
                                                if (data_s.getGSTRate()==gstrate && data_s.getStateCode().equalsIgnoreCase(stateCode)) {
                                                    // taxval
                                                    double taxableval = data_s.getTaxableValue();
                                                    taxableval += taxablevalue_f;
                                                    data_s.setTaxableValue(taxableval);

                                                    // IGST Amt
                                                    double igstamt_temp = data_s.getIGSTAmt();
                                                    igstamt_temp += IGSTAmount_f;
                                                    data_s.setIGSTAmt(igstamt_temp);

                                                    // CGST Amt
                                                    double cgstamt_temp = data_s.getCGSTAmt();
                                                    cgstamt_temp += CGSTAmount_f;
                                                    data_s.setCGSTAmt(cgstamt_temp);

                                                    // SGST Amt
                                                    double sgstamt_temp = data_s.getSGSTAmt();
                                                    sgstamt_temp += SGSTAmount_f;
                                                    data_s.setSGSTAmt(sgstamt_temp);

                                                    // cess Amt
                                                    double cessamt_temp = (data_s.getCessAmt());
                                                    cessamt_temp += cessamount;
                                                    data_s.setCessAmt(cessamt_temp);

                                                    //SubTotal
                                                    double subtot = data_s.getSubTotal();
                                                    subtot += subtotal_f;
                                                    data_s.setSubTotal(subtot);

                                                    found = 1;
                                                    break;

                                                }
                                            }  // end of for loop
                                            if (found == 0) // not in list
                                            {
                                                datalist_s.add(obj);
                                            }
                                        } // end of else


                                        // } while (cursor.moveToNext());

                                    }

                                }// end try
                                catch (Exception e) {
                                    //MsgBox = new AlertDialog.Builder(myContext);
                                    MsgBox.setTitle("Error while fetching items details")
                                            .setMessage(e.getMessage())
                                            .setPositiveButton("OK", null)
                                            .show();
                                }
                            } // end else bill level
                        }
                    } while (cursor.moveToNext());
                    Collections.sort(datalist_s, B2Csmall.HSNComparator);
                }
            }
        }// end try
        catch (Exception e) {
            datalist_s = null;
            //MsgBox = new AlertDialog.Builder(myContext);
            MsgBox.setMessage(e.getMessage())
                    .setPositiveButton("OK", null)
                    .show();
        }
        return datalist_s;
    }

    /*private ArrayList<GSTR1_B2B_Data> makeGSTR1B2B(String start_milli, String end_milli){
    ArrayList<GSTR1_B2B_Data> list = dataController.getGSTR1B2BList(start_milli,end_milli);
    return list;
}
*/
    /*private ArrayList<GSTR1B2CSAData> makeGSTR1B2CSA(String start_milli, String end_milli){
        ArrayList<GSTR1B2CSAData> list = new ArrayList<GSTR1B2CSAData>();
        list = dataController.getGSTR1B2CSAList(start_milli,end_milli);
        return list;
    }
    private ArrayList<GSTR1B2CSData> makeGSTR1B2CS(String start_milli, String end_milli)
    {
        ArrayList<GSTR1B2CSData> list = new ArrayList<GSTR1B2CSData>();
        ArrayList<B2Csmall> b2CsmallsList = dataController.getGSTR1B2CSDataList(start_milli, end_milli);
        double gt = 0;
       // Collections.sort(b2CsmallsList, B2Csmall.HSNComparator);

        for (B2Csmall b2Csmall : b2CsmallsList) {
            gt = gt + b2Csmall.getSubTotal();
            GSTR1B2CSData b2CSData = new GSTR1B2CSData(
                    "*flag*"*//*"A"*//*,
                    "*chksum*"*//*"HHJJHJJHJJJJJJ"*//*,
                    b2Csmall.getStateCode(),
                    b2Csmall.getSupplyType(),
                    b2Csmall.getHSNCode().substring(0, b2Csmall.getHSNCode().indexOf("-")),
                    b2Csmall.getTaxableValue(),
                    b2Csmall.getIGSTRate(),
                    b2Csmall.getIGSTAmt(),
                    b2Csmall.getCGSTRate(),
                    b2Csmall.getCGSTAmt(),
                    b2Csmall.getSGSTRate(),
                    b2Csmall.getSGSTAmt(),
                    Double.parseDouble(b2Csmall.getCessRate()),
                    b2Csmall.getCessAmt(),
                    (b2Csmall.getProAss().equalsIgnoreCase("")) ? "*pro_ass*" : b2Csmall.getProAss(),
                    b2Csmall.getEtin(),
                    b2Csmall.getEtype(),
                    b2Csmall.getOrderno(),
                    b2Csmall.getOrderDate()
            );
            list.add(b2CSData);
        }
        return list;
    }*/
    /*public ArrayList<GSTR2B2BData> getB2BItems(String StartDate, String EndDate)
    {
        ArrayList<GSTR2B2BData> b2bDataList = new ArrayList<GSTR2B2BData>();
        try {
            Cursor cursor = dbReport.getGSTR2GSTNs();
            if (cursor == null)
            {
                b2bDataList = null;
            }
            else
            {
                if (cursor.moveToFirst()) {
                    do {
                        String num = cursor.getString(cursor.getColumnIndex("GSTIN"));
                        ArrayList<GSTR2B2BInvoices> gstr2B2BInvoicesItemss = getGSTR2B2BGSTR2B2BInvoices(StartDate,EndDate,num);
                        GSTR2B2BData btoData = new GSTR2B2BData(num,gstr2B2BInvoicesItemss);
                        b2bDataList.add(btoData);
                    } while (cursor.moveToNext()) ;
                }
            }
        }
        catch (Exception e) {
            b2bDataList = null;
        }
        return b2bDataList;
    }
    public ArrayList<GSTR2B2BInvoices> getGSTR2B2BGSTR2B2BInvoices(String StartDate, String EndDate, String gstn)
    {
        ArrayList<GSTR2B2BInvoices> gstr2B2BInvoicesList = new ArrayList<GSTR2B2BInvoices>();
        try {

            Cursor cursor = dbReport.getGSTR2B2BDetails(StartDate, EndDate,gstn);
            if (cursor == null)
            {
                gstr2B2BInvoicesList = null;
            }
            else
            {
                if (cursor.moveToFirst()) {
                    do {
                        String num = cursor.getString(cursor.getColumnIndex("GSTIN"));
                        String dt = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                        double val = cursor.getDouble(cursor.getColumnIndex("SubTotal"));
                        String pos = cursor.getString(cursor.getColumnIndex("POS"));
                        String InvoiceNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        String rchrg = cursor.getString(cursor.getColumnIndex("AttractsReverseCharge"));
                        ArrayList<GSTR2B2BInvoiceItems> itemsList = getGSTR2G2BInvoiceItemsList(InvoiceNo);
                        GSTR2B2BInvoices b2BInvoices = new GSTR2B2BInvoices(
                                "*flag*",
                                "*chksum*",
                                num,
                                dt,
                                val,
                                pos,
                                rchrg.toUpperCase(),
                                itemsList
                                );
                        gstr2B2BInvoicesList.add(b2BInvoices);

                    } while (cursor.moveToNext()) ;
                }
            }
        }
        catch (Exception e) {
            gstr2B2BInvoicesList = null;
        }
        return gstr2B2BInvoicesList;
    }

    private ArrayList<GSTR2B2BInvoiceItems> getGSTR2G2BInvoiceItemsList(String num) {
        ArrayList<GSTR2B2BInvoiceItems> gstr2B2BInvoiceItemsList = new ArrayList<GSTR2B2BInvoiceItems>();
        try {
            Cursor cursor = dbReport.getGSTR2B2BItems(num);
            if (cursor == null)
            {
                gstr2B2BInvoiceItemsList = null;
            }
            else
            {
                int i = 0;
                if (cursor.moveToFirst()) {
                    do {
                        i++;
                        String ty = cursor.getString(cursor.getColumnIndex("SupplyType"));
                        String hsn_sc = cursor.getString(cursor.getColumnIndex("HSNCode"));
                        double txval = cursor.getDouble(cursor.getColumnIndex("TaxableValue"));
                        double irt = cursor.getDouble(cursor.getColumnIndex("IGSTRate"));
                        double iamt = cursor.getDouble(cursor.getColumnIndex("IGSTAmount"));
                        double crt = cursor.getDouble(cursor.getColumnIndex("CGSTRate"));
                        double camt = cursor.getDouble(cursor.getColumnIndex("CGSTAmount"));
                        double srt = cursor.getDouble(cursor.getColumnIndex("SGSTRate"));
                        double samt = cursor.getDouble(cursor.getColumnIndex("SGSTAmount"));
                        String elg = cursor.getString(cursor.getColumnIndex("ITC_Eligible"));
                        GSTR2B2BItemDetails gstr2B2BItemDetails = new GSTR2B2BItemDetails(
                                ty,
                                hsn_sc,
                                txval,
                                irt,
                                iamt,
                                crt,
                                camt,
                                srt,
                                samt,
                                elg
                        );

                        GSTR2_ITC_Details gstr2B2BITCDetails = new GSTR2_ITC_Details(
                                cursor.getDouble(cursor.getColumnIndex("IGSTAmount")),
                                cursor.getDouble(cursor.getColumnIndex("CGSTAmount")),
                                cursor.getDouble(cursor.getColumnIndex("SGSTAmount")),
                                0,
                                0,
                                0
                        );
                        GSTR2B2BInvoiceItems b2BInvoices = new GSTR2B2BInvoiceItems(i,"A",gstr2B2BITCDetails,gstr2B2BItemDetails);
                        gstr2B2BInvoiceItemsList.add(b2BInvoices);
                    } while (cursor.moveToNext()) ;
                }
            }
        }
        catch (Exception e) {
            gstr2B2BInvoiceItemsList = null;
        }
        return gstr2B2BInvoiceItemsList;
    }
*/
    public ArrayList<GSTR1_B2CL_Data> getGSTR1B2CLList(String startDate, String endDate) {
        ArrayList<GSTR1_B2CL_Data> b2CL_DataList = new ArrayList<GSTR1_B2CL_Data>();
        try
        {
            ArrayList<String> stateCd_List = dbReport.getGSTR1B2CL_stateCodeList(startDate,endDate);
            for (String state_cd : stateCd_List )
            {
                Cursor cursor_billDetail = dbReport.getGSTR1B2CL_stateCodeCursor(startDate,endDate,state_cd);
                if(cursor_billDetail ==null || !cursor_billDetail.moveToFirst())
                {
                    //MsgBox.Show("","No records for B2CL");
                    return b2CL_DataList;
                }
                ArrayList<GSTR1_B2CL_invoices> invoiceList = new ArrayList<>();
                String custStateCd_temp="";
                do
                {
                    String invoiceNo = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("InvoiceNo"));
                    String invoiceDate = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("InvoiceDate"));
                    String custName = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("CustName"));
                    String provisionalAssess = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("ProvisionalAssess"));
                    String etin = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("EcommerceGSTIN"));
                    double taxableValue = cursor_billDetail.getDouble(cursor_billDetail.getColumnIndex("TaxableValue"));
                    String pos_temp = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("POS"));
                    custStateCd_temp = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("CustStateCode"));

                    if(provisionalAssess== null)
                        provisionalAssess= "N";
                    if(pos_temp.equals(custStateCd_temp))
                        continue;

                    double cessRate =0;
                    double cessAmt  =0;
                    String Orderno="0";
                    String OrderDate="0";

                    //String eType = "";

                    ArrayList<GSTR1_B2CL_items> item_list = new ArrayList<>();
                    Cursor cursor_b2clitems_for_inv = dbReport.getGSTR1B2CL_invoices(invoiceNo,invoiceDate,custStateCd_temp,custName);
                    if (cursor_b2clitems_for_inv != null &&  cursor_b2clitems_for_inv.moveToFirst() ) {
                        int i =0;
                        do
                        {//item details
                            GSTR1_B2CL_item_details item_details = new GSTR1_B2CL_item_details(
                                    cursor_b2clitems_for_inv.getString(cursor_b2clitems_for_inv.getColumnIndex("SupplyType")),
                                    cursor_b2clitems_for_inv.getString(cursor_b2clitems_for_inv.getColumnIndex("HSNCode")),
                                    cursor_b2clitems_for_inv.getDouble(cursor_b2clitems_for_inv.getColumnIndex("TaxableValue")),
                                    cursor_b2clitems_for_inv.getDouble(cursor_b2clitems_for_inv.getColumnIndex("IGSTRate")),
                                    cursor_b2clitems_for_inv.getDouble(cursor_b2clitems_for_inv.getColumnIndex("IGSTAmount")),
                                    cessRate, cessAmt
                            );
                            GSTR1_B2CL_items item = new GSTR1_B2CL_items(++i, item_details);
                            item_list.add(item);
                        } while (cursor_b2clitems_for_inv.moveToNext());
                    }

                    if(item_list!=null && item_list.size()>0) {
                        try {
                            Date newD = new Date(Long.parseLong(invoiceDate));
                            String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);
                            GSTR1_B2CL_invoices inv = new GSTR1_B2CL_invoices(
                                    custName,
                                    invoiceNo,
                                    newDate,
                                    taxableValue,
                                    pos_temp,
                                    provisionalAssess,
                                    Orderno,
                                    OrderDate,
                                    etin,
                                    item_list
                            );
                            invoiceList.add(inv);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }while (cursor_billDetail.moveToNext());
                if(invoiceList!=null && invoiceList.size()>0)
                {
                    GSTR1_B2CL_Data b2CL_data = new GSTR1_B2CL_Data(custStateCd_temp,invoiceList);
                    b2CL_DataList.add(b2CL_data);
                }
            }// end of for
        } catch (Exception e) {
            e.printStackTrace();
            b2CL_DataList = null;
        }
        return b2CL_DataList;
    }

    public ArrayList<GSTR1_B2CL_A_Data> getGSTR1B2CL_A_List(String startDate, String endDate) {
        ArrayList<GSTR1_B2CL_A_Data> b2CL_DataList_ammend = new ArrayList<>();
        try
        {
            ArrayList<String> stateCd_List_ammend= dbReport.getGSTR1B2CL_stateCodeList_ammend(startDate,endDate);
            for (String state_cd : stateCd_List_ammend )
            {
                Cursor cursor_billDetail = dbReport.getGSTR1B2CL_stateCodeCursor_ammend(startDate,endDate,state_cd);
                if(cursor_billDetail ==null || !cursor_billDetail.moveToFirst())
                {
                    //MsgBox.Show("","No records for B2CLA");
                    return b2CL_DataList_ammend;
                }
                ArrayList<GSTR1_B2CL_A_invoices> invoiceList = new ArrayList<>();
                String custStateCd_temp="";
                ArrayList<String> alreadyAddedAmmendBill = new ArrayList<>();
                do
                {
                    String invoiceNo = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("InvoiceNo"));
                    String invoiceDate = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("InvoiceDate"));
                    String invoiceNo_ori = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("OriginalInvoiceNo"));
                    String invoiceDate_ori = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("OriginalInvoiceDate"));
                    String custName = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("CustName"));
                    String provisionalAssess = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("ProvisionalAssess"));
                    String etin = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("EcommerceGSTIN"));
                    double taxableValue = cursor_billDetail.getDouble(cursor_billDetail.getColumnIndex("TaxableValue"));
                    String pos_temp = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("POS"));
                    custStateCd_temp = cursor_billDetail.getString(cursor_billDetail.getColumnIndex("CustStateCode"));

                    if(provisionalAssess==null)
                        provisionalAssess= "N";
                    String str = invoiceNo+invoiceDate+ invoiceNo_ori + invoiceDate_ori+pos_temp+custStateCd_temp;
                    if(!alreadyAddedAmmendBill.contains(str))
                        alreadyAddedAmmendBill.add(str);
                    else
                        continue;

                    if(pos_temp.equals(custStateCd_temp))
                        continue;

                    double cessRate =0;
                    double cessAmt  =0;
                    String Orderno="0";
                    String OrderDate="0";

                    //String eType = "";

                    ArrayList<GSTR1_B2CL_items> item_list = new ArrayList<>();
                    Cursor cursor_b2clitems_Ammned_for_inv = dbReport.getGSTR1B2CL_invoices_ammend(invoiceNo,invoiceDate,
                            custStateCd_temp,custName,pos_temp);
                    if (cursor_b2clitems_Ammned_for_inv != null &&  cursor_b2clitems_Ammned_for_inv.moveToFirst() ) {
                        int i =0;
                        do
                        {//item details
                            GSTR1_B2CL_item_details item_details = new GSTR1_B2CL_item_details(
                                    cursor_b2clitems_Ammned_for_inv.getString(cursor_b2clitems_Ammned_for_inv.getColumnIndex("SupplyType")),
                                    cursor_b2clitems_Ammned_for_inv.getString(cursor_b2clitems_Ammned_for_inv.getColumnIndex("HSNCode")),
                                    cursor_b2clitems_Ammned_for_inv.getDouble(cursor_b2clitems_Ammned_for_inv.getColumnIndex("TaxableValue")),
                                    cursor_b2clitems_Ammned_for_inv.getDouble(cursor_b2clitems_Ammned_for_inv.getColumnIndex("IGSTRate")),
                                    cursor_b2clitems_Ammned_for_inv.getDouble(cursor_b2clitems_Ammned_for_inv.getColumnIndex("IGSTAmount")),
                                    cessRate, cessAmt
                            );
                            GSTR1_B2CL_items item = new GSTR1_B2CL_items(++i, item_details);
                            item_list.add(item);
                        } while (cursor_b2clitems_Ammned_for_inv.moveToNext());
                    }

                    if(item_list!=null && item_list.size()>0) {
                        try {
                            Date newD = new Date(Long.parseLong(invoiceDate));
                            String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);
                            Date newD_ori = new Date(Long.parseLong(invoiceDate_ori));
                            String newDate_ori = new SimpleDateFormat("dd-MM-yyyy").format(newD_ori);
                            GSTR1_B2CL_A_invoices inv = new GSTR1_B2CL_A_invoices(
                                    invoiceNo_ori,
                                    newDate_ori,
                                    custName,
                                    invoiceNo,
                                    newDate,
                                    taxableValue,
                                    pos_temp,
                                    provisionalAssess,
                                    Orderno,
                                    OrderDate,
                                    etin,
                                    item_list
                            );
                            invoiceList.add(inv);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }while (cursor_billDetail.moveToNext());
                if(invoiceList!=null && invoiceList.size()>0)
                {
                    GSTR1_B2CL_A_Data b2CL_A_data = new GSTR1_B2CL_A_Data(custStateCd_temp,invoiceList);
                    b2CL_DataList_ammend.add(b2CL_A_data);
                }
            }// end of for
        } catch (Exception e) {
            e.printStackTrace();
            b2CL_DataList_ammend = null;
        }
        return b2CL_DataList_ammend;
    }


    public ArrayList<GSTR1_B2B_A_Data> getGSTR1B2BAList(String startDate, String endDate) {
        ArrayList<GSTR1_B2B_A_Data> b2bADataList = new ArrayList<GSTR1_B2B_A_Data>();
        try {
            ArrayList<String > gstinList = dbReport.getGSTR1B2B_A_gstinList(startDate,endDate);
            if(gstinList.size() ==0)
            {
                //MsgBox.Show("","No records for B2BA");
                return b2bADataList;
            }
            for (String gstin_str  : gstinList )
            {
                Cursor cursor = dbReport.getGSTR1B2b_A_for_gstin(startDate,endDate,gstin_str);
                ArrayList<String> ammendRecords = new ArrayList<>();
                double cessRate =0;
                double cessAmt  =0;
                String Orderno="0";
                String OrderDate="0";
                String etin="";
                //String eType = "";
                ArrayList<GSTR1_B2B_A_invoices> invoiceList = new ArrayList<>();
                while (cursor!=null && cursor.moveToNext())
                {
                    String pos = cursor.getString(cursor.getColumnIndex("POS"));
                    String str = cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo"));
                    str += cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate"));
                    str += cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                    str += cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                    str +=pos;

                    if(ammendRecords!=null && !(ammendRecords.contains(str)))
                       ammendRecords.add(str);
                    else
                        continue;


                    GSTR1_B2B_A_Data b2B_A_Data = new GSTR1_B2B_A_Data();
                    b2B_A_Data.setCtin(cursor.getString(cursor.getColumnIndex("GSTIN")));

                    //do {
                    ArrayList<GSTR1_B2B_items> item_list = new ArrayList<>();

                    //item details
                    String invno = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                    String invdt = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                    String invno_ori = cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo"));
                    String invdt_ori = cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate"));
                    String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));

                    Cursor cursor_b2bitems_for_inv = dbReport.getitems_b2ba(invno_ori, invdt_ori,invno, invdt, gstin,pos);
                    int i = 0;

                    while (cursor_b2bitems_for_inv!=null && cursor_b2bitems_for_inv.moveToNext())
                    {
                        double gstrate = cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTRate")) +
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTRate")) +
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTRate"));
                        GSTR1_B2B_item_details item_details = new GSTR1_B2B_item_details(
                                cursor_b2bitems_for_inv.getString(cursor_b2bitems_for_inv.getColumnIndex("SupplyType")),
                                cursor_b2bitems_for_inv.getString(cursor_b2bitems_for_inv.getColumnIndex("HSNCode")),
                                gstrate,
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("TaxableValue")),
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTRate")),
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTAmount")),
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTRate")),
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTAmount")),
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTRate")),
                                cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTAmount")),
                                cessRate,
                                cessAmt
                        );
                        GSTR1_B2B_items item= new GSTR1_B2B_items(++i,item_details);
                        item_list.add(item);
                    }

                    if(item_list!=null && item_list.size()>0) {
                        try {
                            String date_str = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                            Date newD = new Date(Long.parseLong(date_str));
                            String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);

                            String date_str_ori = cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate"));
                            Date newD_ori = new Date(Long.parseLong(date_str_ori));
                            String newDate_ori = new SimpleDateFormat("dd-MM-yyyy").format(newD_ori);
                            String rcheg = cursor.getString(cursor.getColumnIndex("ReverseCharge"));
                            String prs =  cursor.getString(cursor.getColumnIndex("ProvisionalAssess"));
                            if(rcheg== null)
                                rcheg= "N";
                            if(prs== null)
                                prs= "N";
                            GSTR1_B2B_A_invoices inv = new GSTR1_B2B_A_invoices(
                                    cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")),
                                    newDate_ori,
                                    cursor.getString(cursor.getColumnIndex("InvoiceNo")),
                                    newDate,
                                    cursor.getDouble(cursor.getColumnIndex("TaxableValue")),
                                    cursor.getString(cursor.getColumnIndex("POS")),
                                    rcheg,
                                    prs,
                                    Orderno,
                                    OrderDate,
                                    cursor.getString(cursor.getColumnIndex("EcommerceGSTIN")),
                                    item_list
                            );
                            invoiceList.add(inv);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
                if(invoiceList!=null && invoiceList.size()>0)
                {
                    GSTR1_B2B_A_Data b2B_A_Data_temp = new GSTR1_B2B_A_Data(gstin_str,invoiceList);
                    b2bADataList.add(b2B_A_Data_temp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            b2bADataList = null;
        }
        return b2bADataList;
    }
    /*public ArrayList<GSTR1_B2B_A_Data> getGSTR1B2BAList_old(String startDate, String endDate) {
        ArrayList<GSTR1_B2B_A_Data> b2bADataList = new ArrayList<GSTR1_B2B_A_Data>();
        try {
            ArrayList<String > gstinList = dbReport.getGSTR1B2B_A_gstinList(startDate,endDate);
            if(gstinList.size() ==0)
            {
                MsgBox.Show("","No records for B2BA");
                return b2bADataList;
            }
            for (String gstin_str  : gstinList )
            {
                Cursor cursor1 = dbReport.getGSTR1B2b_A_for_gstin(startDate,endDate,gstin_str);
                ArrayList<String> ammendRecords = new ArrayList<>();
                while (cursor1!=null && cursor1.moveToNext())
                {
                    String str = cursor1.getString(cursor1.getColumnIndex("OriginalInvoiceNo"));
                    str += cursor1.getString(cursor1.getColumnIndex("OriginalInvoiceDate"));
                    str += cursor1.getString(cursor1.getColumnIndex("InvoiceNo"));
                    str += cursor1.getString(cursor1.getColumnIndex("InvoiceDate"));
                    if(ammendRecords!=null && !(ammendRecords.contains(str)))
                        ammendRecords.add(str);
                }

                //int c = cursor.getCount();

                String cessRate ="0";
                String cessAmt  ="0";
                String Orderno="0";
                String OrderDate="0";
                String etin="";
                //String eType = "";

                ArrayList<GSTR1_B2B_A_invoices> invoiceList = new ArrayList<>();
                ArrayList<String> invoiceList_from_database = dbReport.getGSTR1B2B_A_invoiceListList(startDate,endDate,gstin_str);
                for (String invoiceNo : invoiceList_from_database)
                {
                    Cursor cursor = dbReport.getGSTR1B2b_A_for_gstin(startDate,endDate,gstin_str,invoiceNo);
                    if (cursor != null &&  cursor.moveToFirst() )
                    {

                        GSTR1_B2B_A_Data b2B_A_Data = new GSTR1_B2B_A_Data();
                        b2B_A_Data.setCtin(cursor.getString(cursor.getColumnIndex("GSTIN")));

                        //do {
                            ArrayList<GSTR1_B2B_items> item_list = new ArrayList<>();

                            //item details
                            String invno = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                            String invdt = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                            Cursor cursor_b2bitems_for_inv = null ;// dbReport.getitems_b2ba(invno, invdt, gstin);
                            int i = 0;
                            while (cursor_b2bitems_for_inv!=null && cursor_b2bitems_for_inv.moveToNext())
                            {
                                GSTR1_B2B_item_details item_details = new GSTR1_B2B_item_details(
                                        cursor_b2bitems_for_inv.getString(cursor_b2bitems_for_inv.getColumnIndex("SupplyType")),
                                        cursor_b2bitems_for_inv.getString(cursor_b2bitems_for_inv.getColumnIndex("HSNCode")),
                                        cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("TaxableValue")),
                                        cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTRate")),
                                        cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTAmount")),
                                        cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTRate")),
                                        cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTAmount")),
                                        cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTRate")),
                                        cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTAmount")),
                                        0,0
                                );
                                GSTR1_B2B_items item= new GSTR1_B2B_items(++i,item_details);
                                item_list.add(item);
                            }

                            if(item_list!=null && item_list.size()>0) {
                                try {
                                    String date_str = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                                    Date newD = new Date(Long.parseLong(date_str));
                                    String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);

                                    String date_str_ori = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                                    Date newD_ori = new Date(Long.parseLong(date_str_ori));
                                    String newDate_ori = new SimpleDateFormat("dd-MM-yyyy").format(newD_ori);

                                    GSTR1_B2B_A_invoices inv = new GSTR1_B2B_A_invoices(
                                            cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")),
                                            newDate_ori,
                                            cursor.getString(cursor.getColumnIndex("InvoiceNo")),
                                            newDate,
                                            cursor.getDouble(cursor.getColumnIndex("TaxableValue")),
                                            cursor.getString(cursor.getColumnIndex("POS")),
                                            cursor.getString(cursor.getColumnIndex("ReverseCharge")),
                                            cursor.getString(cursor.getColumnIndex("ProvisionalAssess")),
                                            "",
                                            "",
                                            cursor.getString(cursor.getColumnIndex("EcommerceGSTIN")),
                                            item_list
                                    );
                                    invoiceList.add(inv);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        //}while (cursor.moveToNext());
                    }
                    if(invoiceList!=null && invoiceList.size()>0)
                    {
                        GSTR1_B2B_A_Data b2B_A_Data = new GSTR1_B2B_A_Data(gstin_str,invoiceList);
                        b2bADataList.add(b2B_A_Data);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            b2bADataList = null;
        }
        return b2bADataList;
    }*/
    public ArrayList<GSTR1_B2B_Data> getGSTR1B2BList(String startDate, String endDate) {
        ArrayList<GSTR1_B2B_Data> b2BDataList = new ArrayList<GSTR1_B2B_Data>();
        try {
            ArrayList<String > gstinList = dbReport.getGSTR1B2B_gstinList(startDate,endDate);
            if(gstinList.size() ==0)
            {
                //MsgBox.Show("","No records for B2B");
                return b2BDataList;
            }
            for (String gstin_str  : gstinList )
            {
                Cursor cursor = dbReport.getGSTR1B2b_for_gstin(startDate,endDate,gstin_str);
                //int c = cursor.getCount();

                String cessRate ="0";
                String cessAmt  ="0";
                String Orderno="0";
                String OrderDate="0";
                String etin="";
                //String eType = "";
                ArrayList<GSTR1_B2B_invoices> invoiceList = new ArrayList<>();
                if (cursor != null &&  cursor.moveToFirst() )
                {
                    do {
                        ArrayList<GSTR1_B2B_items> item_list = new ArrayList<>();

                        //item details
                        String invno = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        String invdt = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                        String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                        Cursor cursor_b2bitems_for_inv = dbReport.getitems_b2b(invno, invdt, gstin);
                        int i = 0;
                        while (cursor_b2bitems_for_inv!=null && cursor_b2bitems_for_inv.moveToNext())
                        {
                            double gstrate = Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTRate")))) +
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTRate")))) +
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTRate"))));

                            GSTR1_B2B_item_details item_details = new GSTR1_B2B_item_details(
                                    cursor_b2bitems_for_inv.getString(cursor_b2bitems_for_inv.getColumnIndex("SupplyType")),
                                    cursor_b2bitems_for_inv.getString(cursor_b2bitems_for_inv.getColumnIndex("HSNCode")),
                                    gstrate,
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("TaxableValue")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTRate")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTRate")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTRate")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("cessRate")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("cessAmount"))))

                            );
                            GSTR1_B2B_items item= new GSTR1_B2B_items(++i,item_details);
                            item_list.add(item);
                        }

                        if(item_list!=null && item_list.size()>0) {
                            try {
                                String rchrg = cursor.getString(cursor.getColumnIndex("ReverseCharge"));
                                String prs  =cursor.getString(cursor.getColumnIndex("ProvisionalAssess"));
                                if(rchrg==null)
                                    rchrg = "N";
                                 if(prs==null)
                                     prs = "N";
                                String inv_typ = "R";
                                String date_str = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                                Date newD = new Date(Long.parseLong(date_str));
                                String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);
                                GSTR1_B2B_invoices inv = new GSTR1_B2B_invoices(
                                        cursor.getString(cursor.getColumnIndex("InvoiceNo")),
                                        newDate,
                                        Double.parseDouble(String.format("%.2f",cursor.getDouble(cursor.getColumnIndex("BillAmount")))),
                                        cursor.getString(cursor.getColumnIndex("CustStateCode")),
                                        rchrg,
                                        prs,
                                        "",//order_num
                                        "",//order_date
                                        inv_typ,
                                        cursor.getString(cursor.getColumnIndex("EcommerceGSTIN")),
                                        item_list
                                );
                                invoiceList.add(inv);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }

                    }while (cursor.moveToNext());
                    if(invoiceList!=null && invoiceList.size()>0)
                    {
                        GSTR1_B2B_Data b2BData = new GSTR1_B2B_Data(gstin_str,invoiceList);
                        b2BDataList.add(b2BData);
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            b2BDataList = null;
        }
        return b2BDataList;
    }

    public ArrayList<GSTR1B2CSAData> getGSTR1B2CSAList(String startDate, String endDate) {
        ArrayList<GSTR1B2CSAData> list = new ArrayList<GSTR1B2CSAData>();
        try {
            Cursor cursor = dbReport.getGSTR1B2CSAItems(startDate,endDate);
            if (cursor == null)
            {
                list = null;
            }
            else
            {
                String cessRate ="0";
                String cessAmt  ="0";
                String Orderno="0";
                String OrderDate="0";
                String etin="";
                String eType = "";
                String ProAss = "";
                int i = 0;
                int c = cursor.getCount();
                if (cursor.moveToFirst()) {
                    do {
                        i++;

                        GSTR1B2CSAData gstr1B2CSAData = new GSTR1B2CSAData(
                                "*flag*",
                                "*chksum*",
                                cursor.getString(cursor.getColumnIndex("POS")),
                                cursor.getString(cursor.getColumnIndex("Month")),
                                cursor.getString(cursor.getColumnIndex("SupplyType")),
                                cursor.getString(cursor.getColumnIndex("HSNCode")),
                                cursor.getString(cursor.getColumnIndex("RevisedPOS")),
                                cursor.getString(cursor.getColumnIndex("RevisedSupplyType")),
                                cursor.getString(cursor.getColumnIndex("ReviseHSNCode")),
                                cursor.getDouble(cursor.getColumnIndex("TaxableValue")),
                                cursor.getDouble(cursor.getColumnIndex("IGSTRate")),
                                cursor.getDouble(cursor.getColumnIndex("IGSTAmount")),
                                cursor.getDouble(cursor.getColumnIndex("CGSTRate")),
                                cursor.getDouble(cursor.getColumnIndex("CGSTAmount")),
                                cursor.getDouble(cursor.getColumnIndex("SGSTRate")),
                                cursor.getDouble(cursor.getColumnIndex("SGSTAmount")),
                                cursor.getString(cursor.getColumnIndex("ProvisionalAssess")),
                                Double.parseDouble(cessRate),
                                Double.parseDouble(cessAmt),
                                etin,
                                eType,
                                Orderno,
                                OrderDate
                                );
                        list.add(gstr1B2CSAData);
                    } while (cursor.moveToNext()) ;
                }
            }
        }
        catch (Exception e) {
           e.printStackTrace();
            list = null;
        }
        return list;
    }

    public ArrayList<GSTR1_CDN_Data> getGSTR1CDNData(String startDate, String endDate) {
        ArrayList<GSTR1_CDN_Data> cdn_list = new ArrayList<GSTR1_CDN_Data>();
        try {
            ArrayList<String> counterPartyGSTIN_list = dbReport.getGSTR1_CDN_gstinlist(startDate, endDate);
            for (String gstin : counterPartyGSTIN_list) {
                Cursor cursor = dbReport.getGSTR1_CDN_forgstin(startDate,endDate,gstin);
                if (cursor == null || !cursor.moveToFirst())
                {
                    MsgBox.Show("", "No data for Credit / Debit note");
                    return cdn_list;
                }
                ArrayList<GSTR1_CDN_Details> notelist = new ArrayList<>();
                do
                {
                    String reason = cursor.getString(cursor.getColumnIndex("Reason"));
                    if(reason== null)
                        reason="";
                    String etin = "";
                    double cessrate =0, cessamt =0;
                    String notedate_str = cursor.getString(cursor.getColumnIndex("NoteDate"));
                    Date dd_note = new Date(Long.parseLong(notedate_str));
                    String dd_note_str = new SimpleDateFormat("dd-MM-yyyy").format(dd_note);
                    String Invdate_str = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                    Date dd_inv =new Date(Long.parseLong(Invdate_str));
                    String dd_inv_str =  new SimpleDateFormat("dd-MM-yyyy").format(dd_inv);
                    GSTR1_CDN_Details nt_det = new GSTR1_CDN_Details(
                            cursor.getString(cursor.getColumnIndex("NoteType")),
                            cursor.getInt(cursor.getColumnIndex("NoteNo")),
                            dd_note_str,
                            reason,
                            cursor.getString(cursor.getColumnIndex("InvoiceNo")),
                            dd_inv_str,
                            cursor.getString(cursor.getColumnIndex("AttractsReverseCharge")),
                            cursor.getDouble(cursor.getColumnIndex("DifferentialValue")),
                            cursor.getDouble(cursor.getColumnIndex("IGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("IGSTAmount")),
                            cursor.getDouble(cursor.getColumnIndex("CGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("CGSTAmount")),
                            cursor.getDouble(cursor.getColumnIndex("SGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("SGSTAmount")),
                            cessamt,
                            cursor.getDouble(cursor.getColumnIndex("cessAmount")),
                            etin
                             );
                    notelist.add(nt_det);
                }while(cursor.moveToNext());
                if(notelist!=null && notelist.size()>0)
                {
                    GSTR1_CDN_Data cdn_entry =  new GSTR1_CDN_Data(gstin,notelist);
                    cdn_list.add(cdn_entry);
                }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return cdn_list;
        }

        return cdn_list;
    }

    /*private ArrayList<GSTR1_CDN_Details> getGSTR1CDNsList(String startDate, String endDate, String num) {
        ArrayList<GSTR1_CDN_Details> list = new ArrayList<GSTR1_CDN_Details>();
        try {

            Cursor cursor = null;//dbReport.getGSTR1GSTR1CDNCDN(startDate, endDate,num);
            if (cursor == null)
            {
                list = null;
            }
            else
            {
                if (cursor.moveToFirst()) {
                    do {
                        //String num = cursor.getString(cursor.getColumnIndex("GSTIN"));
                        GSTR1_CDN_Details cdncdn =  new GSTR1_CDN_Details(
                                cursor.getString(cursor.getColumnIndex("NoteType")),
                                cursor.getDouble(cursor.getColumnIndex("NoteNo")),
                                cursor.getString(cursor.getColumnIndex("NoteDate")),
                                "*rsn*",
                                cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo")),
                                cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate")),
                                cursor.getDouble(cursor.getColumnIndex("DifferentialValue")),
                                cursor.getDouble(cursor.getColumnIndex("IGSTRate")),
                                cursor.getDouble(cursor.getColumnIndex("IGSTAmount")),
                                cursor.getDouble(cursor.getColumnIndex("CGSTRate")),
                                cursor.getDouble(cursor.getColumnIndex("CGSTAmount")),
                                cursor.getDouble(cursor.getColumnIndex("SGSTRate")),
                                cursor.getDouble(cursor.getColumnIndex("SGSTAmount"))
                        );
                        list.add(cdncdn);
                    } while (cursor.moveToNext()) ;
                }
            }
        }
        catch (Exception e) {
            list = null;
        }
        return list;
    }*/

    public ArrayList<GSTR2_B2B_Data_registered> getGSTR2_B2B_DataList_registered(String startDate, String endDate) {
        ArrayList<GSTR2_B2B_Data_registered> dataList_registered = new ArrayList<>();
        try
        {
            ArrayList<String> gstinList = dbReport.getGSTR2_b2b_gstinList(startDate, endDate);
            for (String gstin : gstinList)
            {
                Cursor cursor = dbReport.getPurchaseOrder_for_gstin(startDate,endDate,gstin);
                ArrayList<String> po_list = new ArrayList<>();
                ArrayList<GSTR2_B2B_invoices_registered> invoiceList = new ArrayList<>();
                while(cursor!=null && cursor.moveToNext())
                {
                    String purchaseorder = cursor.getString(cursor.getColumnIndex("PurchaseOrderNo"));
                    String invoiceNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                    String invoiceDate = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                    String pos_supplier = cursor.getString(cursor.getColumnIndex("SupplierPOS"));
                    String supplierCode = cursor.getString(cursor.getColumnIndex("SupplierCode"));
                    if(pos_supplier==null)
                        pos_supplier="";
                    String str = gstin+supplierCode+invoiceNo+invoiceDate+pos_supplier;
                    if(po_list.contains(str))
                        continue;

                    po_list.add(str);
                    Cursor cursor_item = dbReport.getPurchaseOrder_for_gstin(invoiceNo,invoiceDate,gstin,purchaseorder);
                    int i=1;
                    double totval = 0;
                    ArrayList<GSTR2_B2B_items> itms_list = new ArrayList<>();
                    while (cursor_item!=null && cursor_item.moveToNext())
                    {
                        totval += cursor_item.getDouble(cursor_item.getColumnIndex("Amount"));
                        GSTR2_B2B_item_details item_details = new GSTR2_B2B_item_details(
                                /*cursor_item.getString(cursor_item.getColumnIndex("SupplyType")),
                                cursor_item.getString(cursor_item.getColumnIndex("HSNCode")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("IGSTRate")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("CGSTRate")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("SGSTRate")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount")),
                                0,
                                0,
                                "ip"*/
                        );
                        GSTR2_B2B_ITC_details itc = new GSTR2_B2B_ITC_details();
                        GSTR2_B2B_items itm = new GSTR2_B2B_items(i++, item_details,itc);
                        itms_list.add(itm);
                    }
                    if(itms_list!=null && itms_list.size()>0)
                    {
                        Date newD = new Date(Long.parseLong(invoiceDate));
                        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);
                        GSTR2_B2B_invoices_registered inv = new GSTR2_B2B_invoices_registered(
                                invoiceNo,
                                newDate,
                                totval,
                                pos_supplier,
                                "N",
                                "R",
                                itms_list
                        );
                        invoiceList.add(inv);
                    }
                }
                GSTR2_B2B_Data_registered b2B_data_registered = new GSTR2_B2B_Data_registered(gstin, invoiceList);
                dataList_registered.add(b2B_data_registered);
            }// end for
        }catch (Exception e)
        {
            e.printStackTrace();
            dataList_registered= null;
        }
        finally {
            return dataList_registered;
        }
    }

    public ArrayList<GSTR2_B2B_A_Data_registered> getGSTR2_B2B_A_DataList_registered(String startDate, String endDate) {
        ArrayList<GSTR2_B2B_A_Data_registered> dataList_registered = new ArrayList<>();
        try
        {
            ArrayList<String> gstinList = dbReport.getGSTR2_b2b_A_gstinList(startDate, endDate);
            for (String gstin : gstinList)
            {
                Cursor cursor = dbReport.getGSTR2_b2bA_invoices_for_gstin_registered(startDate,endDate,gstin);
                ArrayList<String> record_list = new ArrayList<>();
                ArrayList<GSTR2_B2B_A_invoices_registered> invoiceList = new ArrayList<>();
                while(cursor!=null && cursor.moveToNext())
                {
                    String invoiceNo_ori = cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo"));
                    String invoiceDate_ori = cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate"));
                    String invoiceNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                    String invoiceDate = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                    String pos_supplier = cursor.getString(cursor.getColumnIndex("POS"));

                    if(pos_supplier==null)
                        pos_supplier="";
                    String str = gstin+invoiceNo_ori+invoiceDate_ori+invoiceNo+invoiceDate+pos_supplier;
                    if(record_list.contains(str))
                        continue;

                    record_list.add(str);
                    Cursor cursor_item = dbReport.getGSTR2_b2bA_ammends_for_gstin_registered(invoiceNo,invoiceDate,gstin,
                                                    invoiceNo_ori,invoiceDate_ori);
                    int i=1;
                    double totval = 0;
                    ArrayList<GSTR2_B2B_items> itms_list = new ArrayList<>();
                    while (cursor_item!=null && cursor_item.moveToNext())
                    {
                        totval += cursor_item.getDouble(cursor_item.getColumnIndex("Amount"));
                        GSTR2_B2B_item_details item_details = new GSTR2_B2B_item_details(
                               /* cursor_item.getString(cursor_item.getColumnIndex("SupplyType")),
                                cursor_item.getString(cursor_item.getColumnIndex("HSNCode")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("IGSTRate")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("CGSTRate")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("SGSTRate")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount")),
                                0,
                                0,
                                "ip"*/
                        );
                        GSTR2_B2B_ITC_details itc = new GSTR2_B2B_ITC_details();
                        GSTR2_B2B_items itm = new GSTR2_B2B_items(i++, item_details,itc);
                        itms_list.add(itm);
                    }
                    if(itms_list!=null && itms_list.size()>0)
                    {
                        Date newD = new Date(Long.parseLong(invoiceDate));
                        String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);
                        Date newD_ori = new Date(Long.parseLong(invoiceDate_ori));
                        String newDate_ori = new SimpleDateFormat("dd-MM-yyyy").format(newD_ori);
                        GSTR2_B2B_A_invoices_registered inv = new GSTR2_B2B_A_invoices_registered(
                                invoiceNo,
                                newDate,
                                invoiceNo_ori,
                                newDate_ori,
                                totval,
                                pos_supplier,
                                "N",
                                itms_list
                        );
                        invoiceList.add(inv);
                    }
                }
                GSTR2_B2B_A_Data_registered b2B_A_data_registered = new GSTR2_B2B_A_Data_registered(gstin, invoiceList);
                dataList_registered.add(b2B_A_data_registered);
            }// end for
        }catch (Exception e)
        {
            e.printStackTrace();
            dataList_registered= null;
        }
        finally {
            return dataList_registered;
        }
    }
    public ArrayList<GSTR2_B2B_Data_Unregistered> getGSTR2_B2B_DataList_Unregistered(String startDate, String endDate) {
        ArrayList<GSTR2_B2B_Data_Unregistered> dataList_registered = new ArrayList<>();
        try
        {
            Cursor cursor = dbReport.getPurchaseOrder_for_unregistered(startDate,endDate);
            ArrayList<GSTR2_B2B_invoices_Unregistered> invoiceList = new ArrayList<>();
            ArrayList<String>po_list = new ArrayList<>();
            while(cursor!=null && cursor.moveToNext())
            {
                String purchaseorder = cursor.getString(cursor.getColumnIndex("PurchaseOrderNo"));
                String invoiceNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                String invoiceDate = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                String pos_supplier = cursor.getString(cursor.getColumnIndex("SupplierPOS"));
                String supplierCode = cursor.getString(cursor.getColumnIndex("SupplierCode"));
                String cname = cursor.getString(cursor.getColumnIndex("SupplierName"));
                if(pos_supplier==null)
                    pos_supplier="";
                String str = supplierCode+invoiceNo+invoiceDate+pos_supplier;
                if(po_list.contains(str))
                    continue;

                po_list.add(str);
                Cursor cursor_item = dbReport.getPurchaseOrder_for_unregisteredSupplier(invoiceNo,invoiceDate,purchaseorder,supplierCode);
                int i=1;
                double totval = 0;
                ArrayList<GSTR2_B2B_items> itms_list = new ArrayList<>();
                while (cursor_item!=null && cursor_item.moveToNext())
                {
                    totval += cursor_item.getDouble(cursor_item.getColumnIndex("Amount"));
                    GSTR2_B2B_item_details item_details = new GSTR2_B2B_item_details(
                            /*cursor_item.getString(cursor_item.getColumnIndex("SupplyType")),
                            cursor_item.getString(cursor_item.getColumnIndex("HSNCode")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("IGSTRate")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("CGSTRate")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("SGSTRate")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount")),
                            0,
                            0,
                            "ip"*/
                    );
                    GSTR2_B2B_ITC_details itc = new GSTR2_B2B_ITC_details();
                    GSTR2_B2B_items itm = new GSTR2_B2B_items(i++, item_details,itc);
                    itms_list.add(itm);
                }
                if(itms_list!=null && itms_list.size()>0)
                {
                    Date newD = new Date(Long.parseLong(invoiceDate));
                    String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);
                    GSTR2_B2B_invoices_Unregistered inv = new GSTR2_B2B_invoices_Unregistered(
                            cname,
                            invoiceNo,
                            newDate,
                            totval,
                            pos_supplier,
                            "N",
                            itms_list
                    );
                    invoiceList.add(inv);
                }
            }
            GSTR2_B2B_Data_Unregistered b2B_data_Unregistered = new GSTR2_B2B_Data_Unregistered(invoiceList);
            dataList_registered.add(b2B_data_Unregistered);

        }catch (Exception e)
        {
            e.printStackTrace();
            dataList_registered= null;
        }
        finally {
            return dataList_registered;
        }
    }
    public ArrayList<GSTR2_B2B_A_Data_Unregistered> getGSTR2_B2B_A_DataList_Unregistered(String startDate, String endDate) {
        ArrayList<GSTR2_B2B_A_Data_Unregistered> dataList_Unregistered = new ArrayList<>();
        try
        {
            Cursor cursor = dbReport.getGSTR2_b2b_A_unregisteredSupplierList(startDate,endDate);
            ArrayList<GSTR2_B2B_A_invoices_Unregistered> invoiceList = new ArrayList<>();
            ArrayList<String>custname_list = new ArrayList<>();
            while(cursor!=null && cursor.moveToNext())
            {
                String invoiceNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                String invoiceDate = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                String invoiceNo_ori = cursor.getString(cursor.getColumnIndex("OriginalInvoiceNo"));
                String invoiceDate_ori = cursor.getString(cursor.getColumnIndex("OriginalInvoiceDate"));
                String pos_supplier = cursor.getString(cursor.getColumnIndex("POS"));
                String supplierName = cursor.getString(cursor.getColumnIndex("GSTIN"));
                if(pos_supplier==null)
                    pos_supplier="";
                String str = invoiceNo_ori+invoiceDate_ori+invoiceNo+invoiceDate+pos_supplier+supplierName;
                if(custname_list.contains(str))
                    continue;

                custname_list.add(str);
                Cursor cursor_item = dbReport.getGSTR2_A_ammend_for_supplierName(invoiceNo,invoiceDate,invoiceNo_ori,
                        invoiceDate_ori,supplierName,pos_supplier);
                int i=1;
                double totval = 0;
                ArrayList<GSTR2_B2B_items> itms_list = new ArrayList<>();
                while (cursor_item!=null && cursor_item.moveToNext())
                {
                    double taxval = cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue"));
                    double igstAmt = cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount"));
                    double cgstAmt = cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount"));
                    double sgstAmt = cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount"));
                    totval +=taxval+igstAmt+cgstAmt+sgstAmt;
                    GSTR2_B2B_item_details item_details = new GSTR2_B2B_item_details(
                            /*cursor_item.getString(cursor_item.getColumnIndex("SupplyType")),
                            cursor_item.getString(cursor_item.getColumnIndex("HSNCode")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("IGSTRate")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("CGSTRate")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("SGSTRate")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount")),
                            0,
                            0,
                            "ip"*/
                    );
                    GSTR2_B2B_ITC_details itc = new GSTR2_B2B_ITC_details();
                    GSTR2_B2B_items itm = new GSTR2_B2B_items(i++, item_details,itc);
                    itms_list.add(itm);
                }
                if(itms_list!=null && itms_list.size()>0)
                {
                    Date newD = new Date(Long.parseLong(invoiceDate));
                    String newDate = new SimpleDateFormat("dd-MM-yyyy").format(newD);
                    Date newD_ori = new Date(Long.parseLong(invoiceDate_ori));
                    String newDate_ori = new SimpleDateFormat("dd-MM-yyyy").format(newD_ori);
                    GSTR2_B2B_A_invoices_Unregistered inv = new GSTR2_B2B_A_invoices_Unregistered(
                            supplierName,
                            invoiceNo,
                            newDate,
                            invoiceNo_ori,
                            newDate_ori,
                            totval,
                            pos_supplier,
                            "N",
                            itms_list
                    );
                    invoiceList.add(inv);
                }
            }
            GSTR2_B2B_A_Data_Unregistered b2B_A_data_Unregistered = new GSTR2_B2B_A_Data_Unregistered(invoiceList);
            dataList_Unregistered.add(b2B_A_data_Unregistered);

        }catch (Exception e)
        {
            e.printStackTrace();
            dataList_Unregistered= null;
        }
        finally {
            return dataList_Unregistered;
        }
    }

    public ArrayList<GSTR2_CDN_Data> getGSTR2_CDNData(String startDate, String endDate) {
        ArrayList<GSTR2_CDN_Data> cdn_list = new ArrayList<>();
        try {
            ArrayList<String> counterPartyGSTIN_list = dbReport.getGSTR2_CDN_gstinlist(startDate, endDate);
            for (String gstin : counterPartyGSTIN_list) {
                Cursor cursor = dbReport.getGSTR2_CDN_forgstin(startDate,endDate,gstin);
                if (cursor == null || !cursor.moveToFirst())
                {
                    MsgBox.Show("", "No data for Credit / Debit note");
                    return cdn_list;
                }
                ArrayList<GSTR2_CDN_Details> notelist = new ArrayList<>();
                do
                {
                    String reason = cursor.getString(cursor.getColumnIndex("Reason"));
                    if(reason== null)
                        reason="";
                    String etin = "";
                    double cessrate =0, cessamt =0;
                    String notedate_str = cursor.getString(cursor.getColumnIndex("NoteDate"));
                    Date dd_note = new Date(Long.parseLong(notedate_str));
                    String dd_note_str = new SimpleDateFormat("dd-MM-yyyy").format(dd_note);
                    String Invdate_str = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                    Date dd_inv =new Date(Long.parseLong(Invdate_str));
                    String dd_inv_str =  new SimpleDateFormat("dd-MM-yyyy").format(dd_inv);
                    GSTR2_ITC_Details itc = new GSTR2_ITC_Details();
                    GSTR2_CDN_Details nt_det = new GSTR2_CDN_Details(
                            cursor.getString(cursor.getColumnIndex("NoteType")),
                            cursor.getDouble(cursor.getColumnIndex("NoteNo")),
                            dd_note_str,
                            reason,
                            cursor.getString(cursor.getColumnIndex("InvoiceNo")),
                            dd_inv_str,
                            cursor.getString(cursor.getColumnIndex("AttractsReverseCharge")),
                            cursor.getDouble(cursor.getColumnIndex("DifferentialValue")),
                            cursor.getDouble(cursor.getColumnIndex("IGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("IGSTAmount")),
                            cursor.getDouble(cursor.getColumnIndex("CGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("CGSTAmount")),
                            cursor.getDouble(cursor.getColumnIndex("SGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("SGSTAmount")),
                            cessamt,
                            cessamt,
                            "ip",
                            itc
                    );
                    notelist.add(nt_det);
                }while(cursor.moveToNext());
                if(notelist!=null && notelist.size()>0)
                {
                    GSTR2_CDN_Data cdn_entry =  new GSTR2_CDN_Data(gstin,notelist);
                    cdn_list.add(cdn_entry);
                }

            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return cdn_list;
        }

        return cdn_list;
    }

    public ArrayList<GSTR1_HSN_Data> getGSTR1HSNData(String startDate, String endDate) {
        ArrayList<GSTR1_HSN_Data> final_hsn_list = new ArrayList<>();
        int i =1;
        Cursor cursor_invoices_outward = dbReport.getInvoices_outward(startDate,endDate);
        ArrayList<String> hsn_list_for_dateRange = dbReport.gethsn_list_for_invoices(cursor_invoices_outward);
        try {
            for(String HSN : hsn_list_for_dateRange)
            {
                ArrayList<GSTR1_HSN_Details> datalist_for_hsn = new ArrayList<>();

                // B2C - inter + intra
                {

                    Cursor B2CInvoices_for_hsn = dbReport.gethsn(startDate, endDate, HSN, "B2C");
                    while (B2CInvoices_for_hsn.moveToNext()) {
                        GSTR1_HSN_Details newData;
                        if (B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("POS")).
                                equals(B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("CustStateCode"))))  // intra state
                        {
                            newData = new GSTR1_HSN_Details(
                                    i++,
                                    B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("SupplyType")),
                                    B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("HSNCode")),
                                    B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("TaxableValue")),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("IGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("IGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("CGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("CGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("SGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("SGSTAmount")))),
                                    0, //csrate
                                    0, // csamt
                                    B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("ItemName")),
                                    B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("UOM")),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("Quantity")))),
                                    "INTRAB2C"
                            );
                        } else { // inter state supplies
                            newData = new GSTR1_HSN_Details(
                                    i++,
                                    B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("SupplyType")),
                                    B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("HSNCode")),
                                    B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("TaxableValue")),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("IGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("IGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("CGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("CGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("SGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("SGSTAmount")))),
                                    0, //csrate
                                    0, // csamt
                                    B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("ItemName")),
                                    B2CInvoices_for_hsn.getString(B2CInvoices_for_hsn.getColumnIndex("UOM")),
                                    Double.parseDouble(String.format("%.2f", B2CInvoices_for_hsn.getDouble(B2CInvoices_for_hsn.getColumnIndex("Quantity")))),
                                    "INTRB2C"
                            );
                        }
                        int inserted = 0;
                        for (GSTR1_HSN_Details hsn_in_list : datalist_for_hsn) {
                            String newData_hsn = newData.getHsn_sc();
                            String newData_businesstype = newData.getSply_ty();
                            double newData_irt = newData.getIrt();
                            double newData_crt = newData.getCrt();
                            double newData_srt = newData.getSrt();
                            if (hsn_in_list.getHsn_sc().equals(newData_hsn) && hsn_in_list.getSply_ty().equals(newData_businesstype)
                                    && (hsn_in_list.getIrt() == newData_irt)
                                    && (hsn_in_list.getCrt() == newData_crt)
                                    && (hsn_in_list.getSrt() == newData_srt))
                            {
                                double iamt = hsn_in_list.getIamt() + newData.getIamt();
                                double camt = hsn_in_list.getCamt() + newData.getCamt();
                                double samt = hsn_in_list.getSamt() + newData.getSamt();
                                double taxval = hsn_in_list.getTxval() + newData.getTxval();
                                double qty = hsn_in_list.getQty() + newData.getQty();

                                hsn_in_list.setIamt(Double.parseDouble(String.format("%.2f", iamt)));
                                hsn_in_list.setCamt(Double.parseDouble(String.format("%.2f", camt)));
                                hsn_in_list.setSamt(Double.parseDouble(String.format("%.2f", samt)));
                                hsn_in_list.setTxval(Double.parseDouble(String.format("%.2f", taxval)));
                                hsn_in_list.setQty(Double.parseDouble(String.format("%.2f", qty)));
                                inserted = 1;
                                break;
                            }
                        }
                        if (inserted == 0)
                            datalist_for_hsn.add(newData);

                    }
                }
                // B2b - inter + intra
                {
                    Cursor B2BInvoices_for_hsn = dbReport.gethsn( startDate ,  endDate, HSN ,"B2B");
                    while (B2BInvoices_for_hsn.moveToNext())
                    {
                        GSTR1_HSN_Details  newData;

                        if(B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("POS")).
                                equals(B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("CustStateCode"))))  // intra state
                        {
                            newData = new GSTR1_HSN_Details(
                                    i++,
                                    B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("SupplyType")),
                                    B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("HSNCode")),
                                    B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("TaxableValue")),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("IGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("IGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("CGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("CGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("SGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("SGSTAmount")))),
                                    0, //csrate
                                    0, // csamt
                                    B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("ItemName")),
                                    B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("UOM")),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("Quantity")))),
                                    "INTRAB2B"
                            );
                        }else
                        { // inter state supplies
                            newData = new GSTR1_HSN_Details(
                                    i++,
                                    B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("SupplyType")),
                                    B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("HSNCode")),
                                    B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("TaxableValue")),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("IGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("IGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("CGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("CGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("SGSTRate")))),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("SGSTAmount")))),
                                    0, //csrate
                                    0, // csamt
                                    B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("ItemName")),
                                    B2BInvoices_for_hsn.getString(B2BInvoices_for_hsn.getColumnIndex("UOM")),
                                    Double.parseDouble(String.format("%.2f", B2BInvoices_for_hsn.getDouble(B2BInvoices_for_hsn.getColumnIndex("Quantity")))),
                                    "INTRB2B"
                            );
                        }
                        int inserted = 0;
                        for( GSTR1_HSN_Details hsn_in_list : datalist_for_hsn)
                        {
                            String newData_hsn = newData.getHsn_sc();
                            String newData_businesstype = newData.getSply_ty();
                            double newData_irt = newData.getIrt();
                            double newData_crt = newData.getCrt();
                            double newData_srt = newData.getSrt();
                            if (hsn_in_list.getHsn_sc().equals(newData_hsn) && hsn_in_list.getSply_ty().equals(newData_businesstype) &&
                                    (hsn_in_list.getIrt() == newData_irt)  &&
                                    (hsn_in_list.getCrt() == newData_crt)  &&
                                    (hsn_in_list.getSrt() == newData_srt) )
                            {
                                double iamt = hsn_in_list.getIamt() +newData.getIamt();
                                double camt = hsn_in_list.getCamt() +newData.getCamt();
                                double samt = hsn_in_list.getSamt() +newData.getSamt();
                                double taxval = hsn_in_list.getTxval() +newData.getTxval();
                                double qty = hsn_in_list.getQty() +newData.getQty();

                                hsn_in_list.setIamt(Double.parseDouble(String.format("%.2f",iamt)));
                                hsn_in_list.setCamt(Double.parseDouble(String.format("%.2f",camt)));
                                hsn_in_list.setSamt(Double.parseDouble(String.format("%.2f",samt)));
                                hsn_in_list.setTxval(Double.parseDouble(String.format("%.2f",taxval)));
                                hsn_in_list.setQty(Double.parseDouble(String.format("%.2f",qty)));
                                inserted = 1;
                                break;
                            }
                        }
                        if(inserted == 0)
                            datalist_for_hsn.add(newData);

                    }
                }
                // till now all inter+intra detail for b2b and b2c for single hsn is made
                if (datalist_for_hsn.size() > 0){
                    GSTR1_HSN_Data Completedata_for_hsn = new GSTR1_HSN_Data(datalist_for_hsn);
                    final_hsn_list.add(Completedata_for_hsn);
                }

            } // end for

        }catch (Exception e)
        {
            e.printStackTrace();
            return final_hsn_list;
        }

        return final_hsn_list;
    }/*public ArrayList<GSTR1_HSN_Data> getGSTR1HSNData(String startDate, String endDate) {
        ArrayList<GSTR1_HSN_Data> hsn_list = new ArrayList<>();
        int i =1;
        //ArrayList<GSTR1_HSN_Details> hsn_list = new ArrayList<>();
        try {
            Cursor cursor_hsn_det  = dbReport.getGSTR1_hsn_INTRB2C(startDate, endDate);
            int c = cursor_hsn_det.getCount();
            ArrayList<GSTR1_HSN_Details>dataList = new ArrayList<>();
            while(cursor_hsn_det!=null && cursor_hsn_det.moveToNext())
            {
                String invoiceno = cursor_hsn_det.getString(cursor_hsn_det.getColumnIndex("InvoiceNo"));
                String invoicedate = cursor_hsn_det.getString(cursor_hsn_det.getColumnIndex("InvoiceDate"));
                String custStateCode = cursor_hsn_det.getString(cursor_hsn_det.getColumnIndex("CustStateCode"));
                String pos = cursor_hsn_det.getString(cursor_hsn_det.getColumnIndex("POS"));
                if(pos!=null && custStateCode!=null && pos.equals(custStateCode)) // INTRA- b2c
                {
                    Cursor cursor_hsn = dbReport.getGSTR1_hsn_list(invoiceno,invoicedate,custStateCode);
                    while (cursor_hsn!=null && cursor_hsn.moveToNext())
                    {
                        GSTR1_HSN_Details newData_hsn = new GSTR1_HSN_Details( i++,
                                cursor_hsn.getString(cursor_hsn.getColumnIndex("SupplyType")),
                                cursor_hsn.getString(cursor_hsn.getColumnIndex("HSNCode")),
                                cursor_hsn.getDouble(cursor_hsn.getColumnIndex("TaxableValue")),
                                Double.parseDouble(String.format("%.2f",cursor_hsn.getDouble(cursor_hsn.getColumnIndex("IGSTRate")))),
                                Double.parseDouble(String.format("%.2f",cursor_hsn.getDouble(cursor_hsn.getColumnIndex("IGSTAmount")))),
                                Double.parseDouble(String.format("%.2f",cursor_hsn.getDouble(cursor_hsn.getColumnIndex("CGSTRate")))),
                                Double.parseDouble(String.format("%.2f",cursor_hsn.getDouble(cursor_hsn.getColumnIndex("CGSTAmount")))),
                                Double.parseDouble(String.format("%.2f",cursor_hsn.getDouble(cursor_hsn.getColumnIndex("SGSTRate")))),
                                Double.parseDouble(String.format("%.2f",cursor_hsn.getDouble(cursor_hsn.getColumnIndex("SGSTAmount")))),
                                0, //csrate
                                0, // csamt
                                cursor_hsn.getString(cursor_hsn.getColumnIndex("ItemName")),
                                cursor_hsn.getString(cursor_hsn.getColumnIndex("UOM")),
                                Double.parseDouble(String.format("%.2f",cursor_hsn.getDouble(cursor_hsn.getColumnIndex("Quantity")))),
                                "INTRAB2C"
                                );
                        if (!dataList.contains(newData_hsn))
                        {
                            dataList.add(newData_hsn);
                        }
                        else
                        {
                            for(GSTR1_HSN_Details data : dataList)
                            {
                                if (data.getHsn_sc().equalsIgnoreCase(newData_hsn.getHsn_sc()))
                                {

                                }
                            }
                        }
                    }


                }

                //while(cursor_hsn!=null && cursor_hsn.moveToNext())
            }
            *//*for (String gstin : counterPartyGSTIN_list) {
                Cursor cursor = dbReport.getGSTR2_CDN_forgstin(startDate,endDate,gstin);
                if (cursor == null || !cursor.moveToFirst())
                {
                    MsgBox.Show("", "No data for Credit / Debit note");
                    return cdn_list;
                }
                ArrayList<GSTR2_CDN_Details> notelist = new ArrayList<>();
                do
                {
                    String reason = cursor.getString(cursor.getColumnIndex("Reason"));
                    if(reason== null)
                        reason="";
                    String etin = "";
                    double cessrate =0, cessamt =0;
                    String notedate_str = cursor.getString(cursor.getColumnIndex("NoteDate"));
                    Date dd_note = new Date(Long.parseLong(notedate_str));
                    String dd_note_str = new SimpleDateFormat("dd-MM-yyyy").format(dd_note);
                    String Invdate_str = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                    Date dd_inv =new Date(Long.parseLong(Invdate_str));
                    String dd_inv_str =  new SimpleDateFormat("dd-MM-yyyy").format(dd_inv);
                    GSTR2_ITC_Details itc = new GSTR2_ITC_Details();
                    GSTR2_CDN_Details nt_det = new GSTR2_CDN_Details(
                            cursor.getString(cursor.getColumnIndex("NoteType")),
                            cursor.getDouble(cursor.getColumnIndex("NoteNo")),
                            dd_note_str,
                            reason,
                            cursor.getString(cursor.getColumnIndex("InvoiceNo")),
                            dd_inv_str,
                            cursor.getString(cursor.getColumnIndex("AttractsReverseCharge")),
                            cursor.getDouble(cursor.getColumnIndex("DifferentialValue")),
                            cursor.getDouble(cursor.getColumnIndex("IGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("IGSTAmount")),
                            cursor.getDouble(cursor.getColumnIndex("CGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("CGSTAmount")),
                            cursor.getDouble(cursor.getColumnIndex("SGSTRate")),
                            cursor.getDouble(cursor.getColumnIndex("SGSTAmount")),
                            cessamt,
                            cessamt,
                            "ip",
                            itc
                    );
                    notelist.add(nt_det);
                }while(cursor.moveToNext());
                if(notelist!=null && notelist.size()>0)
                {
                    GSTR2_CDN_Data cdn_entry =  new GSTR2_CDN_Data(gstin,notelist);
                    cdn_list.add(cdn_entry);
                }

            }*//*
        }catch (Exception e)
        {
            e.printStackTrace();
            return hsn_list;
        }

        return hsn_list;
    }*/
}
