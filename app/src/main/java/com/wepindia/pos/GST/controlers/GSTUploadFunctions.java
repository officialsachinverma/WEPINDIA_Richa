package com.wepindia.pos.GST.controlers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.wep.common.app.Database.DatabaseHandler;
import com.wep.common.app.gst.B2Csmall;
import com.wep.common.app.gst.GSTR1B2CSAData;
import com.wep.common.app.gst.GSTR1B2CSData;
import com.wep.common.app.gst.GSTR1_B2B_Data;
import com.wep.common.app.gst.GSTR1_B2B_invoices;
import com.wep.common.app.gst.GSTR1_B2B_item_details;
import com.wep.common.app.gst.GSTR1_B2B_items;
import com.wep.common.app.gst.GSTR1_B2CL_Data;
import com.wep.common.app.gst.GSTR1_B2CL_invoices;
import com.wep.common.app.gst.GSTR1_B2CL_item_details;
import com.wep.common.app.gst.GSTR1_B2CL_items;
import com.wep.common.app.gst.GSTR1_CDN_Data;
import com.wep.common.app.gst.GSTR1_CDN_Details;
import com.wep.common.app.gst.GSTR1_CDN_Items;
import com.wep.common.app.gst.GSTR1_CDN_Items_details;
import com.wep.common.app.gst.GSTR1_DOCS;
import com.wep.common.app.gst.GSTR1_DOCS_Data;
import com.wep.common.app.gst.GSTR1_HSN_Data;
import com.wep.common.app.gst.GSTR1_HSN_Details;
import com.wep.common.app.gst.GSTR2_B2B_Data_Unregistered;
import com.wep.common.app.gst.GSTR2_B2B_Data_registered;
import com.wep.common.app.gst.GSTR2_B2B_ITC_details;
import com.wep.common.app.gst.GSTR2_B2B_invoices_Unregistered;
import com.wep.common.app.gst.GSTR2_B2B_invoices_registered;
import com.wep.common.app.gst.GSTR2_B2B_item_details;
import com.wep.common.app.gst.GSTR2_B2B_items;
import com.wepindia.pos.GenericClasses.MessageDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

/**
 * Created by RichaA on 6/27/2017.
 */

public class GSTUploadFunctions {

    private final MessageDialog MsgBox;
    private DatabaseHandler dbReport;
    private Context myContext;
    private String BillNoPrefix;

    public GSTUploadFunctions(Context context, DatabaseHandler dbReport , String BillNoPrefix) {
        this.myContext = context;
        this.dbReport = dbReport;
        this.BillNoPrefix = BillNoPrefix;
        MsgBox  = new MessageDialog(myContext);
    }


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
                                    gstrate,
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("TaxableValue")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("IGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("CGSTAmount")))),
                                    Double.parseDouble(String.format("%.2f",cursor_b2bitems_for_inv.getDouble(cursor_b2bitems_for_inv.getColumnIndex("SGSTAmount")))),
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
                                String custStateCode = cursor.getString(cursor.getColumnIndex("CustStateCode"));
                                String pos = cursor.getString(cursor.getColumnIndex("POS"));
                                if(pos!=null && custStateCode!=null & pos.equals(custStateCode))
                                    custStateCode="";

                                GSTR1_B2B_invoices inv = new GSTR1_B2B_invoices(
                                        BillNoPrefix+invno,
                                        newDate,
                                        Double.parseDouble(String.format("%.2f",cursor.getDouble(cursor.getColumnIndex("BillAmount")))),
                                        custStateCode,
                                        rchrg,
                                        cursor.getString(cursor.getColumnIndex("EcommerceGSTIN")),
                                        inv_typ,
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



                    ArrayList<GSTR1_B2CL_items> item_list = new ArrayList<>();
                    Cursor cursor_b2clitems_for_inv = dbReport.getGSTR1B2CL_invoices(invoiceNo,invoiceDate,custStateCd_temp,custName);
                    if (cursor_b2clitems_for_inv != null &&  cursor_b2clitems_for_inv.moveToFirst() ) {
                        int i =0;
                        do
                        {//item details
                            GSTR1_B2CL_item_details item_details = new GSTR1_B2CL_item_details(
                                    cursor_b2clitems_for_inv.getDouble(cursor_b2clitems_for_inv.getColumnIndex("IGSTRate")),
                                    cursor_b2clitems_for_inv.getDouble(cursor_b2clitems_for_inv.getColumnIndex("TaxableValue")),
                                    cursor_b2clitems_for_inv.getDouble(cursor_b2clitems_for_inv.getColumnIndex("IGSTAmount")),
                                    cursor_b2clitems_for_inv.getDouble(cursor_b2clitems_for_inv.getColumnIndex("cessAmount"))
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
                                    BillNoPrefix+invoiceNo,
                                    newDate,
                                    taxableValue,
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

    public ArrayList<GSTR1_CDN_Data> getGSTR1CDNData(String startDate, String endDate) {
        ArrayList<GSTR1_CDN_Data> cdn_list = new ArrayList<GSTR1_CDN_Data>();
        try {
            ArrayList<String> counterPartyGSTIN_list = dbReport.getGSTR1_CDN_gstinlist(startDate, endDate);
            for (String gstin : counterPartyGSTIN_list) {
                Cursor cursor = dbReport.getGSTR1_CDN_forgstin(startDate,endDate,gstin);
                if (cursor == null || !cursor.moveToFirst())
                {
                   continue;
                }
                ArrayList<GSTR1_CDN_Details> notelist = new ArrayList<>();
                ArrayList<String> notelistencountered = new ArrayList<>();
                do
                {
                    String notedate_str = cursor.getString(cursor.getColumnIndex("NoteDate"));
                    String noteNo_str = cursor.getString(cursor.getColumnIndex("NoteNo"));
                    if(!notelistencountered.contains(noteNo_str+notedate_str+gstin))
                    {

                        Cursor cc = dbReport.getNoteDetails(noteNo_str,notedate_str,gstin);
                        if(cc!=null && cc.moveToFirst())
                        {
                            String notetype = cc.getString(cursor.getColumnIndex("NoteType"));
                            String reason = cc.getString(cursor.getColumnIndex("Reason"));
                            String invoiceNo = cc.getString(cursor.getColumnIndex("InvoiceNo"));
                            String Invdate_str = cc.getString(cursor.getColumnIndex("InvoiceDate"));
                            Date dd_inv =new Date(Long.parseLong(Invdate_str));
                            String invoiceDate =  new SimpleDateFormat("dd-MM-yyyy").format(dd_inv);
                            String p_gst = "Y";
                            double val = 0;
                            Cursor ss = dbReport.getBillDetail(Integer.parseInt(invoiceNo), invoiceDate);
                            if(ss!=null && ss.moveToFirst())
                            {
                                val = ss.getDouble(ss.getColumnIndex("BillAmount"));
                            }
                            int num =1;
                            ArrayList<GSTR1_CDN_Items> itms = new ArrayList<>();
                            do{
                                double rt = cc.getDouble(cc.getColumnIndex("IGSTRate")) +
                                        cc.getDouble(cc.getColumnIndex("CGSTRate")) +
                                        cc.getDouble(cc.getColumnIndex("SGSTRate"));

                                GSTR1_CDN_Items_details itm_det = new GSTR1_CDN_Items_details(rt,
                                        cc.getDouble(cursor.getColumnIndex("DifferentialValue")),
                                        cc.getDouble(cursor.getColumnIndex("IGSTAmount")),
                                        cc.getDouble(cursor.getColumnIndex("CGSTAmount")),
                                        cc.getDouble(cursor.getColumnIndex("SGSTAmount")),
                                        cc.getDouble(cursor.getColumnIndex("cessAmount"))
                                );

                                GSTR1_CDN_Items items = new GSTR1_CDN_Items(num++, itm_det);
                                itms.add(items);

                            }while(cc.moveToNext());
                            if(itms.size()>0)
                            {
                                int noteno=0;
                                if(noteNo_str.contains("."))
                                {
                                    double dd = Double.parseDouble(noteNo_str);
                                    noteno = (int)dd;
                                }
                                else
                                    noteno = Integer.parseInt(noteNo_str);
                                GSTR1_CDN_Details notes = new GSTR1_CDN_Details(notetype,noteno,notedate_str,p_gst,reason,
                                        invoiceNo, invoiceDate,val,itms);
                                notelist.add(notes);
                            }
                        }
                    }
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

   /* private ArrayList<GSTR1B2CSAData> makeGSTR1B2CSA(String start_milli, String end_milli){
        ArrayList<GSTR1B2CSAData> list = new ArrayList<GSTR1B2CSAData>();
        list = dataController.getGSTR1B2CSAList(start_milli,end_milli);
        return list;
    }*/
    public ArrayList<GSTR1B2CSData> makeGSTR1B2CS(String start_milli, String end_milli)
    {
        ArrayList<GSTR1B2CSData> list = new ArrayList<GSTR1B2CSData>();
        ArrayList<B2Csmall> b2CsmallsList = getGSTR1B2CSDataList(start_milli, end_milli);

        for (B2Csmall b2Csmall : b2CsmallsList) {
            String custStateCode = "";
            if(b2Csmall.getSply_ty().equalsIgnoreCase("INTER"))
                custStateCode = b2Csmall.getStateCode();
            GSTR1B2CSData b2CSData = new GSTR1B2CSData(
                    b2Csmall.getSply_ty(),
                    b2Csmall.getGSTRate(),
                    b2Csmall.getEtype(),
                    b2Csmall.getEtin(),
                    custStateCode,
                    b2Csmall.getTaxableValue(),
                    b2Csmall.getIGSTAmt(),
                    b2Csmall.getCGSTAmt(),
                    b2Csmall.getSGSTAmt(),
                    b2Csmall.getCessAmt()
            );
            list.add(b2CSData);
        }
        return list;
    }
    public ArrayList<com.wep.common.app.gst.B2Csmall> getGSTR1B2CSDataList(String StartDate, String EndDate) {
        ArrayList<com.wep.common.app.gst.B2Csmall> datalist_s = new ArrayList<com.wep.common.app.gst.B2Csmall>();
        try {
            Cursor cursor = dbReport.getOutwardB2Cs(StartDate, EndDate);
            if (cursor == null || !cursor.moveToFirst()) {
                MsgBox.setMessage("No data for entered period B2CS")
                        .setPositiveButton("OK", null)
                        .show();
            }
            else {
                do {// item_detail table
                    String stateCode = cursor.getString(cursor.getColumnIndex("CustStateCode"));
                    String ownerPOS = cursor.getString(cursor.getColumnIndex("POS"));
                    if(stateCode== null)
                        stateCode = "";
                    float TaxableValue_f = Float.parseFloat(cursor.getString(cursor.getColumnIndex("TaxableValue")));
                    if ((stateCode.equals("")) || (stateCode.equals(ownerPOS))|| (!(stateCode.equals("") && TaxableValue_f <= 250000)) || (!(stateCode.equals("29") && TaxableValue_f <= 250000))) {
                        // for intrastate + interstate only  <=2.5L
                        String InvNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        String InvDate = cursor.getString(cursor.getColumnIndex("InvoiceDate"));
                        String custname_str = cursor.getString(cursor.getColumnIndex("CustName"));
                        String statecode_str = cursor.getString(cursor.getColumnIndex("CustStateCode"));
                        String eType = "";
                        String etin = cursor.getString(cursor.getColumnIndex("EcommerceGSTIN"));
                        if(etin == null) {
                            etin ="";
                            eType = "OE";
                        }else  {
                            eType = "E";
                        }

                        Cursor rowcursor = dbReport.getitems_b2cl(InvNo, InvDate, custname_str, statecode_str);
                        if (rowcursor == null) {

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
                                    String ProAss = "";

                                    subtotal_f = Double.parseDouble(rowcursor.getString(rowcursor.getColumnIndex("SubTotal")));
                                    double gstrate = IGSTRate_f + CGSTRate_f+SGSTRate_f;

                                    if((SGSTAmount_f + CGSTAmount_f+IGSTAmount_f) == 0) // nil rated
                                        continue;

                                    B2Csmall obj = new B2Csmall();
                                    String suply_type = "";
                                    if(IGSTAmount_f >0)
                                        suply_type = "INTER";
                                    else
                                        suply_type = "INTRA" ;

                                    obj.setSply_ty(suply_type);
                                    obj.setSupplyType(SupplyType_str);
                                    obj.setHSNCode(HSN);
                                    obj.setStateCode(stateCode);
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
                                            //if (data_s.getGSTRate()==gstrate && data_s.getStateCode().equalsIgnoreCase(stateCode)) {
                                            if (data_s.getGSTRate()==gstrate &&
                                                    data_s.getSply_ty().equalsIgnoreCase(suply_type) &&
                                                    data_s.getEtype().equalsIgnoreCase(eType)) {
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
                                    }
                                }

                            }// end try
                            catch (Exception e) {
                                //MsgBox = new AlertDialog.Builder(myContext);
                                MsgBox.setTitle("Error while fetching items details")
                                        .setMessage(e.getMessage())
                                        .setPositiveButton("OK", null)
                                        .show();
                                e.printStackTrace();
                            }
                        } // end else bill level
                    }
                } while (cursor.moveToNext());
                Collections.sort(datalist_s, B2Csmall.HSNComparator);
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
    public ArrayList<GSTR1_HSN_Data> getGSTR1HSNData(String startDate, String endDate) {
        ArrayList<GSTR1_HSN_Data> final_hsn_list = new ArrayList<>();

        Cursor cursor_invoices_outward = dbReport.getInvoices_outward(startDate,endDate);
        DatabaseHandler db = new DatabaseHandler(myContext);
        int num =1;
        ArrayList<GSTR1_HSN_Details> datalist_for_hsn = new ArrayList<>();
        try {
            while (cursor_invoices_outward!=null && cursor_invoices_outward.moveToNext())
            {
                int invNo = cursor_invoices_outward.getInt(cursor_invoices_outward.getColumnIndex("InvoiceNo"));
                String invDt = cursor_invoices_outward.getString(cursor_invoices_outward.getColumnIndex("InvoiceDate"));
                Cursor cursor_itemLedger = db.getItemsFromBillItem_new(invNo,invDt);
                while (cursor_itemLedger!=null && cursor_itemLedger.moveToNext())
                {
                    GSTR1_HSN_Details hsn = new GSTR1_HSN_Details (num++,
                            cursor_itemLedger.getString(cursor_itemLedger.getColumnIndex("HSNCode")),
                            cursor_itemLedger.getString(cursor_itemLedger.getColumnIndex("ItemName")),
                            cursor_itemLedger.getString(cursor_itemLedger.getColumnIndex("UOM")),
                            Double.parseDouble(String.format("%.2f", cursor_itemLedger.getDouble(cursor_itemLedger.getColumnIndex("Quantity")))),
                            cursor_itemLedger.getDouble(cursor_itemLedger.getColumnIndex("Value")),
                            cursor_itemLedger.getDouble(cursor_itemLedger.getColumnIndex("TaxableValue")),
                            Double.parseDouble(String.format("%.2f", cursor_itemLedger.getDouble(cursor_itemLedger.getColumnIndex("IGSTAmount")))),
                            Double.parseDouble(String.format("%.2f", cursor_itemLedger.getDouble(cursor_itemLedger.getColumnIndex("CGSTAmount")))),
                            Double.parseDouble(String.format("%.2f", cursor_itemLedger.getDouble(cursor_itemLedger.getColumnIndex("CGSTAmount")))),
                            Double.parseDouble(String.format("%.2f", cursor_itemLedger.getDouble(cursor_itemLedger.getColumnIndex("cessAmount"))))
                    );
                    if(datalist_for_hsn.size()==0) {
                        datalist_for_hsn.add(hsn);
                        continue;
                    }
                    int found =0;
                    for(GSTR1_HSN_Details data : datalist_for_hsn)
                    {
                        if(data.getHsn_sc().equalsIgnoreCase(hsn.getHsn_sc()))
                        {
                            data.setQty(data.getQty()+hsn.getQty());
                            data.setVal(data.getVal()+ hsn.getVal());
                            data.setTxval(data.getTxval()+ hsn.getTxval());
                            data.setIamt(data.getIamt()+ hsn.getIamt());
                            data.setCamt(data.getCamt()+ hsn.getCamt());
                            data.setSamt(data.getSamt()+ hsn.getSamt());
                            data.setCsamt(data.getCsamt()+ hsn.getCsamt());
                            found =1;
                            break;
                        }
                    }
                    if(found==0)
                        datalist_for_hsn.add(hsn);
                }
            }

            if (datalist_for_hsn.size() > 0){
                GSTR1_HSN_Data Completedata_for_hsn = new GSTR1_HSN_Data(datalist_for_hsn);
                final_hsn_list.add(Completedata_for_hsn);
            }

        }catch (Exception e)
        {
            e.printStackTrace();
            return (final_hsn_list = new ArrayList<>());
        }
        return final_hsn_list;
    }

    public ArrayList<GSTR1_DOCS_Data> getGSTR1DOCData(String startDate, String endDate) {
        ArrayList<GSTR1_DOCS_Data> DocDataList = new ArrayList<>();

        // Invoices for outward supply
        Cursor cursor = dbReport.getAllInvoices_outward(startDate,endDate);
        if (cursor!=null && cursor.moveToFirst())
        {
            String from = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
            String to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
            int totnum =0;
            int cancel =0;
            do {
                to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                totnum++;
                if(cursor.getInt(cursor.getColumnIndex("BillStatus"))==0)
                    cancel++;

            }while(cursor.moveToNext());
            GSTR1_DOCS document = new GSTR1_DOCS(1,BillNoPrefix+from,BillNoPrefix+to,totnum,cancel,totnum-cancel);
            ArrayList<GSTR1_DOCS> list = new ArrayList<>();
            list.add(document);
            GSTR1_DOCS_Data doc = new GSTR1_DOCS_Data(1,list);
            DocDataList.add(doc);

        }
        // Invoices for inward supply from unregistered person
        cursor = dbReport.getInward_taxed(startDate,endDate);
        if (cursor!=null && cursor.moveToFirst())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if (gstin ==null  || gstin.equals(""))
            {
                String from = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                String to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                int totnum =0;
                int cancel =0;
                do {
                    gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
                    if (gstin ==null  || gstin.equals("")) {
                        to = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
                        totnum++;
                        /*if (cursor.getInt(cursor.getColumnIndex("BillStatus")) == 0)
                            cancel++;*/
                    }

                }while(cursor.moveToNext());
                GSTR1_DOCS document = new GSTR1_DOCS(1,from,to,totnum,cancel,totnum-cancel);
                ArrayList<GSTR1_DOCS> list = new ArrayList<>();
                list.add(document);
                GSTR1_DOCS_Data doc = new GSTR1_DOCS_Data(2,list);
                DocDataList.add(doc);
            }
        }
        // Credit Note
        cursor = dbReport.getGSTR1_CDN(startDate,endDate);
        if (cursor!=null && cursor.moveToFirst())
        {
            String from = cursor.getString(cursor.getColumnIndex("NoteNo"));
            String to = cursor.getString(cursor.getColumnIndex("NoteNo"));
            int totnum =0;
            int cancel =0;
            do {
                to = cursor.getString(cursor.getColumnIndex("NoteNo"));
                totnum++;
                /*if(cursor.getInt(cursor.getColumnIndex("BillStatus"))==0)
                    cancel++;*/

            }while(cursor.moveToNext());
            GSTR1_DOCS document = new GSTR1_DOCS(1,from,to,totnum,cancel,totnum-cancel);
            ArrayList<GSTR1_DOCS> list = new ArrayList<>();
            list.add(document);
            GSTR1_DOCS_Data doc = new GSTR1_DOCS_Data(5,list);
            DocDataList.add(doc);

        }
        return DocDataList;
    }
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
                        double gstrate = cursor_item.getDouble(cursor_item.getColumnIndex("IGSTRate")) +
                                cursor_item.getDouble(cursor_item.getColumnIndex("CGSTRate"))+
                                cursor_item.getDouble(cursor_item.getColumnIndex("SGSTRate"));
                        totval += cursor_item.getDouble(cursor_item.getColumnIndex("Amount"));
                        GSTR2_B2B_item_details item_details = new GSTR2_B2B_item_details(
                                cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount")),
                                cursor_item.getDouble(cursor_item.getColumnIndex("cessAmount")),
                                gstrate
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
                    double gstrate =cursor_item.getDouble(cursor_item.getColumnIndex("IGSTRate")) +
                            cursor_item.getDouble(cursor_item.getColumnIndex("CGSTRate")) +
                            cursor_item.getDouble(cursor_item.getColumnIndex("SGSTRate"));
                    totval += cursor_item.getDouble(cursor_item.getColumnIndex("Amount"));
                    GSTR2_B2B_item_details item_details = new GSTR2_B2B_item_details(
                            cursor_item.getDouble(cursor_item.getColumnIndex("TaxableValue")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("IGSTAmount")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("CGSTAmount")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("SGSTAmount")),
                            cursor_item.getDouble(cursor_item.getColumnIndex("cessAmount")),
                            gstrate
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

}
