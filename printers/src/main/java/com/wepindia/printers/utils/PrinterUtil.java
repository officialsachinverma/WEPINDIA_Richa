package com.wepindia.printers.utils;

import android.content.Context;
import android.util.Base64;

import com.Bitmap.CreateBitmap;
import com.Utils.TextAlign;
import com.gprinter.command.EscCommand;
import com.wep.common.app.print.BillKotItem;
import com.wep.common.app.print.BillServiceTaxItem;
import com.wep.common.app.print.BillSubTaxItem;
import com.wep.common.app.print.BillTaxItem;
import com.wep.common.app.print.BillTaxSlab;
import com.wep.common.app.print.Payment;
import com.wep.common.app.print.PrintIngredientsModel;
import com.wep.common.app.print.PrintKotBillItem;
import com.wepindia.printers.R;

import org.apache.commons.lang.ArrayUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

/**
 * Created by PriyabratP on 04-01-2017.
 */

public class PrinterUtil {
    
    private Context context;
    
    public PrinterUtil(Context context){
        this.context = context;
    }

    public List<String[]> getPrintKOTUseSohamsa(PrintKotBillItem item) {
        List<String[]> tmpList = new ArrayList<String[]>();
        String tblno = "", modename = "";
        /*if(item.getBillingMode().equalsIgnoreCase("1")){
            tblno = "Table # "+item.getTableNo() + " | ";
            modename = "Dine In";
        } else if(item.getBillingMode().equalsIgnoreCase("2")){
            tblno = "";
            modename = "Counter Sales";
        } else if(item.getBillingMode().equalsIgnoreCase("3")){
            tblno = "";
            modename = "Pick Up";
        } else if(item.getBillingMode().equalsIgnoreCase("4")){
            tblno = "";
            modename = "Home Delivery";
        }*/

        if(item.getBillingMode().equalsIgnoreCase("1")){
            tblno = "Table # "+item.getTableNo() + " | ";
            modename = item.getStrBillingModeName();
        } else {
            tblno = "";
            modename = item.getStrBillingModeName();
        }
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", modename, TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", tblno + "KOT # "+item.getBillNo(), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "==========================================", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Attandant : "+item.getOrderBy(), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Date : "+item.getDate() +" | "+"Time : "+item.getTime(), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "==========================================", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Lists", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "Sl              NAME              QTY", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "===============================================", TextAlign.LEFT,context));
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        while (it.hasNext())
        {
            BillKotItem billKotItem = (BillKotItem) it.next();
            int id = billKotItem.getItemId();
            String name = getFormatedCharacterForPrint(billKotItem.getItemName(),10,0);
            String qty = billKotItem.getQty()+"";
            String pre = getPostAddedSpaceFormat("", String.valueOf(id),15,0)+name;
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getPreAddedSpaceFormat(pre,qty,38,0), TextAlign.LEFT,context));
        }
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "===========================================", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(5));

        return tmpList;
    }

    public List<String[]> getPrintBillUseSohamsa(PrintKotBillItem item) {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Resturant Invoice", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", item.getAddressLine1(), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", item.getAddressLine2(), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", item.getAddressLine3(), TextAlign.CENTER,context));
        //tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Phone: 080661 12000", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Bill No # "+item.getBillNo(), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Table # "+item.getTableNo(), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Date : "+item.getDate() +" | "+"Time : "+item.getTime(), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Cashier   : "+item.getOrderBy(), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Customer Name   : "+item.getCustomerName(), TextAlign.CENTER,context));
        if(item.getBillingMode().equalsIgnoreCase("1")) {
            tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Dine In", TextAlign.CENTER,context));
        } else if(item.getBillingMode().equalsIgnoreCase("2")) {
            tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Counter Sales", TextAlign.CENTER,context));
        } else if(item.getBillingMode().equalsIgnoreCase("3")) {
            tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Pick Up", TextAlign.CENTER,context));
        } else if(item.getBillingMode().equalsIgnoreCase("4")) {
            tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Home Delivery", TextAlign.CENTER,context));
        } else {
            tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "----------", TextAlign.CENTER,context));
        }
        tmpList.add(CreateBitmap.AddBlank(1));
        if(item.getBillingMode().equalsIgnoreCase("4")) {
            tmpList.add(CreateBitmap.AddText(20, "Bold", "MONOSPACE", "Payment Status   : " + item.getPaymentStatus(), TextAlign.CENTER, context));
            tmpList.add(CreateBitmap.AddBlank(1));
        }
        // -----------
        //tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Dine In", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "SNo  ITEM NAME    QTY   RATE    AMOUNT ", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "========================================", TextAlign.LEFT,context));
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        while (it.hasNext())
        {
            BillKotItem billKotItem = (BillKotItem) it.next();
            String preId = getPostAddedSpaceFormat("", String.valueOf(billKotItem.getItemId()),5,0);
            String preName = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getItemName()),10,0),13,0);
            String preQty = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getQty()),4,0),5,0);
            String preRate = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getRate()),4,0),8,0);
            String preAmount = getPostAddedSpaceFormat("", String.format("%.2f",billKotItem.getAmount()),7,0);
            String pre = preId+preName+preQty+preRate+preAmount;
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,context));
        }
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "============================================", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("SUB-TOTAL","", String.format("%.2f",item.getSubTotal()),37,0), TextAlign.LEFT,context));
        // Sales Tax
        double dTotalTaxAmt = 0, dSalesTaxAmt = 0;
        ArrayList<BillTaxItem> billTaxItems = item.getBillTaxItems();
        Iterator it1 = billTaxItems.iterator();
        while (it1.hasNext())
        {
            BillTaxItem billKotItem = (BillTaxItem) it1.next();
            String TxName = getPostAddedSpaceFormat("", String.valueOf(billKotItem.getTxName()),16,0);
            String TxPercent = getPostAddedSpaceFormat("", "@ " + String.format("%.2f",billKotItem.getPercent()) + " %",13,0);
            String TxValue = getPostAddedSpaceFormat("", String.format("%.2f",billKotItem.getPrice()),7,0);
            //String pre = getSpaceFormat(billKotItem.getTxName(), String.valueOf(billKotItem.getPercent()), String.valueOf(billKotItem.getPrice()),36,0);
            String pre = TxName + TxPercent + TxValue;
            dSalesTaxAmt += billKotItem.getPrice();
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,context));
        }
        //tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("Total VAT","", item.getTotalSalesTaxAmount(),36,0), TextAlign.LEFT,context));

        // Service Sub Tax Calculation
        double dSerSubTaxPer = 0, dSerSubTaxAmt = 0;
        ArrayList<BillSubTaxItem> billSubTaxItemsCal = item.getBillSubTaxItems();
        Iterator itCal = billSubTaxItemsCal.iterator();
        while (itCal.hasNext())
        {
            BillSubTaxItem billKotItem = (BillSubTaxItem) itCal.next();
            dSerSubTaxPer += billKotItem.getPercent();
            dSerSubTaxAmt += billKotItem.getPrice();
        }

        // Service Tax
        double dServiceTaxPer = 0, dServiceTaxAmt = 0;
        ArrayList<BillServiceTaxItem> billServiceTaxItems = item.getBillServiceTaxItems();
        Iterator it2 = billServiceTaxItems.iterator();
        while (it2.hasNext())
        {
            BillServiceTaxItem billKotItem = (BillServiceTaxItem) it2.next();
            dServiceTaxPer = billKotItem.getServicePercent();
            dServiceTaxAmt = billKotItem.getServicePrice();
            dServiceTaxPer = dServiceTaxPer - dSerSubTaxPer;
            dServiceTaxAmt = dServiceTaxAmt - dSerSubTaxAmt;
            String TxName = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getServiceTxName()),16,1);
            String TxPercent = getPostAddedSpaceFormat("","@ " + String.format("%.2f",dServiceTaxPer) + " %",13,1);
            String TxValue = getPostAddedSpaceFormat("",String.format("%.2f",dServiceTaxAmt),7,1);
            //String pre = getSpaceFormat(billKotItem.getServiceTxName(), String.valueOf(billKotItem.getServicePercent()), String.valueOf(billKotItem.getServicePrice()),36,0);
            //tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,context));
            String pre = TxName + TxPercent + TxValue;
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,context));
        }
        //servicetaxpercent = servicetaxpercent - Double.parseDouble(String.valueOf(item.getTotalsubTaxPercent()));
        //servicetaxamount = item.getSubTotal() * servicetaxpercent / 100;
        //tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("Service Tax", String.format("%.2f",servicetaxamount), String.format("%.2f",servicetaxpercent),36,0), TextAlign.LEFT,context));
        //Service Sub Tax
        ArrayList<BillSubTaxItem> billSubTaxItems = item.getBillSubTaxItems();
        Iterator it3 = billSubTaxItems.iterator();
        while (it3.hasNext())
        {
            BillSubTaxItem billKotItem = (BillSubTaxItem) it3.next();
            String TxName = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getTxName()),16,1);
            String TxPercent = getPostAddedSpaceFormat("","@ " + String.format("%.2f",billKotItem.getPercent()) + " %",13,1);
            String TxValue = getPostAddedSpaceFormat("",String.format("%.2f",billKotItem.getPrice()),7,1);
            //String pre = getSpaceFormat(billKotItem.getTxName(), String.format("%.2f",billKotItem.getPercent()), String.format("%.2f",billKotItem.getPrice()),36,0);
            String pre = TxName + TxPercent + TxValue;
            //String pre = getSpaceFormat(billKotItem.getTxName(), String.format("%.2f",billKotItem.getPercent()), String.format("%.2f",billKotItem.getPrice()),36,0);
            tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", pre, TextAlign.LEFT,context));
        }
        dTotalTaxAmt = dSalesTaxAmt + dServiceTaxAmt + dSerSubTaxAmt;
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("Total Tax Amount", "", String.format("%.2f",dTotalTaxAmt),37,0), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "============================================", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", getSpaceFormat("TOTAL  : ","", String.format("%.2f",item.getNetTotal()),34,0), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", item.getFooterLine(), TextAlign.CENTER,context));
        //tmpList.add(CreateBitmap.AddBlank(1));
        //tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Hope you will visit Again,", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(5));
        return tmpList;
    }

    public List<String[]> getPrintReportUseSohamsa(ArrayList<ArrayList<String>> itemReport, String reportName) {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", reportName, TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", "========================================", TextAlign.LEFT,context));
        //Iterator itRow = itemReport.iterator();
        //tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", "0123456789012345678901234567890123456789555555555", TextAlign.LEFT,context));
        for(int j=0;j<itemReport.size();j++)
        {
            ArrayList<String> arrayListColumn = itemReport.get(j);
            StringBuffer sb = new StringBuffer();
            for (int i=0;i<arrayListColumn.size();i++)
            {
                String str = arrayListColumn.get(i);
                String preTxt = getAbsoluteCharacter(String.valueOf(str),10,0);
                if(j==0)
                    sb.append(preTxt+" |");
                else
                    sb.append(" "+preTxt+" ");
                int rem = i%3;
                if(rem == 0 && i!=0)
                {
                    tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", sb.toString(), TextAlign.LEFT,context));
                    sb = new StringBuffer();
                }

                if(rem != 0 && (arrayListColumn.size()-1)==i)
                {
                    tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", getAbsoluteCharacter1(sb.toString(),0), TextAlign.LEFT,context));
                    sb = new StringBuffer();
                }
                /*else if(i == arrayListColumn.size())
                {
                    tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", sb.toString(), TextAlign.LEFT,context));
                    sb = new StringBuffer();
                }*/
            }
            if(j==0)
                tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", "========================================", TextAlign.LEFT,context));
        }
        tmpList.add(CreateBitmap.AddText(17, "Bold","MONOSPACE", "========================================", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddBlank(5));
        return tmpList;
    }

    public List<String[]> getTestPrintUseSohamsa() {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "01234567890123456789012345678912345", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "    ", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "0123456789012345678901234567890123456789", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "012345678901234567890123456789,", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGH", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "01234567890123456789012345678912345", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "0123456789012345678901234567890123456789", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "012345678901234567890123456789,", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "ABCDEFGHIJKLMNOPQRSTUVWXYZABCDEFGH", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(5));
        return tmpList;
    }

    public List<String[]> getPrintMSwipePaymentBillUseSohamsa(Payment payment, String reportName) {
        List<String[]> tmpList = new ArrayList<String[]>();
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", reportName, TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "======================================,", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", String.valueOf(payment.getMerchantName()), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", String.valueOf(payment.getMerchantAdd()), TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddBlank(1));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "======================================,", TextAlign.CENTER,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Date/Time     :"+ String.valueOf(payment.getDateTime()), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Invoice Id    :"+ String.valueOf(payment.getInvoiceNo()), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Card Name     : "+ String.valueOf(payment.getCardHolderName()), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Card Number   :"+ String.valueOf(payment.getCardNo()), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Expiry Date   : "+ String.valueOf(payment.getExpDate()), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Card Type     : "+ String.valueOf(payment.getCardType()), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Reference No  : "+ String.valueOf(payment.getRefNo()), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", "======================================", TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddText(20, "Bold","MONOSPACE", "Total         : "+ String.valueOf(payment.getTotal_Pay_Amount()), TextAlign.LEFT,context));
        tmpList.add(CreateBitmap.AddBlank(8));
        return tmpList;
    }
    private String getFormatedCharacterForPrint(String txt, int limit,int type) {
        if(txt.length()<limit){
            return txt+getSpaces(limit-txt.length(),type);
        }else {
            return txt.substring(0,limit);
        }
    }
    private String getFormatedCharacterForPrint_init(String txt, int limit,int type) {
        if(txt.length()<limit){
            return getSpaces(limit-txt.length(),type)+txt;
        }else {
            return txt.substring(0,limit);
        }
    }

    public String getSpaces(int num,int type)
    {
        StringBuffer sb = new StringBuffer();
        if(type==0)
        {
            for (int i=0;i<num;i++)
            {
                sb.append(context.getResources().getString(R.string.superSpace));
            }
        }
        else
        {
            for (int i=0;i<num;i++)
            {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

    public String getPreAddedSpaceFormat(String sourceTxt, String toAddTxt, int max,int type)
    {
        return sourceTxt+getSpaces(max-(sourceTxt.length()+toAddTxt.length()),type)+toAddTxt;
    }

    private String getSpaceFormat(String txtPre, String txtPercent, String txtPost, int num, int type) {
        return txtPre+getSpaces(num-(txtPre.length()+txtPercent.length()+txtPost.length()),type)+txtPost+txtPercent;
    }

    private String getSpaceFormater(String txtPre, String txtPost, int num, int type) {
        return txtPre+getSpaces(num-(txtPre.length()+txtPost.length()),type)+txtPost;
    }

    public String getAbsoluteCharacter(String str, int num,int type) {
        String strToDo = "";
        if(str.length() > num)
        {
            strToDo = str.substring(0,num);
        }
        else
        {
            strToDo = str;
        }
        String preTxt = getPostAddedSpaceFormat("", String.valueOf(strToDo),num,type);
        return preTxt;
    }

    private String getAbsoluteCharacter1(String str,int type) {
        return getSpaces(42-str.length(),type)+str;
    }

    public String getPostAddedSpaceFormat(String sourceTxt, String toAddTxt, int max,int type)
    {
        return sourceTxt+toAddTxt+getSpaces(max-(sourceTxt.length()+toAddTxt.length()),type);
    }

    // For Hey dey Printer
    public String getPrintKOT(PrintKotBillItem item) {
        EscCommand esc = new EscCommand();
        //esc.addPrintAndFeedLines((byte)1);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        String tblno = "", modename = "";
        /*if(item.getBillingMode().equalsIgnoreCase("1")){
            tblno = "Table # "+item.getTableNo() + " | ";
            modename = "Dine In";
        } else if(item.getBillingMode().equalsIgnoreCase("2")){
            tblno = "";
            modename = "Counter Sales";
        } else if(item.getBillingMode().equalsIgnoreCase("3")){
            tblno = "";
            modename = "Pick Up";
        } else if(item.getBillingMode().equalsIgnoreCase("4")){
            tblno = "";
            modename = "Home Delivery";
        }*/
        if(item.getBillingMode().equalsIgnoreCase("1")){
            tblno = "Table # "+item.getTableNo() + " | ";
            modename = item.getStrBillingModeName();
        } else {
            tblno = "";
            modename = item.getStrBillingModeName();
        }

        esc.addText(modename+"\n");
        esc.addText(tblno +"KOT # "+item.getBillNo()+"\n");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("=============================================\n");
        esc.addText("Attandant : "+item.getOrderBy()+"\n");
        esc.addText("Date : "+item.getDate() +" | "+"Time : "+item.getTime()+"\n");
        esc.addText("=============================================\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("Lists\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("=============================================\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("SNo              NAME               QTY"+"\n");
        esc.addText("=============================================\n");
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        while (it.hasNext())
        {
            BillKotItem billKotItem = (BillKotItem) it.next();
            int id = billKotItem.getItemId();
            String name = getFormatedCharacterForPrint(billKotItem.getItemName(),10,1);
            String qty = billKotItem.getQty()+"";
            String pre = getPostAddedSpaceFormat("",String.valueOf(id),15,1)+name;
            esc.addText(getPreAddedSpaceFormat(pre,qty,38,1)+"\n");
        }
        esc.addText("=============================================\n");
        esc.addPrintAndFeedLines((byte)3);

        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    public String getPrintBill(PrintKotBillItem item) {
        EscCommand esc = new EscCommand();
        //esc.addPrintAndFeedLines((byte)2);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addText("OUTWARDS INVOICE"+item.getIsDuplicate()+"\n");
        if(item.getHeaderLine()!=null && !item.getHeaderLine().equals("") )
            esc.addText(item.getHeaderLine()+"\n");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("GSTIN     : "+item.getAddressLine1()+"\n");
        esc.addText("Name      : "+item.getAddressLine2()+"\n");
        esc.addText("Address   : "+item.getAddressLine3()+"\n");
        esc.addPrintAndLineFeed();

        esc.addText("Bill no         : "+item.getBillNo()+"\n");
        if(item.getBillingMode().equals("1"))
            esc.addText("Table           : "+item.getTableNo()+"\n");
        esc.addText("Date            : "+item.getDate() +"      Time : "+item.getTime() +"\n");
       /* esc.addText("Date            : "+item.getDate() +"\n");
        esc.addText("Time            : "+item.getTime() +"\n");*/
        esc.addText("Cashier         : "+item.getOrderBy()+"\n");
        esc.addText("Customer Name   : "+item.getCustomerName()+"\n");
        if(item.getBillingMode().equalsIgnoreCase("4") || item.getBillingMode().equalsIgnoreCase("3")) {
            esc.addText("Payment Status  : " + item.getPaymentStatus()+"\n");
        }


        if(item.getBillingMode().equalsIgnoreCase("1") || item.getBillingMode().equalsIgnoreCase("2") ||
                item.getBillingMode().equalsIgnoreCase("3") || item.getBillingMode().equalsIgnoreCase("4")){
            esc.addText("Service         : "+ item.getStrBillingModeName() + "\n");
        } else {
            esc.addText("-----------" + "\n");
        }

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addText("================================================"+"\n");
        esc.addText("SI  ITEM NAME       QTY     RATE       AMOUNT "+"\n");
        esc.addText("================================================"+"\n");
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        int totalitemtypes =0, totalquantitycount =0;
        double subtotal =0;
        while (it.hasNext())
        {

            BillKotItem billKotItem = (BillKotItem) it.next();

            String preId = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getItemId()),3,1);
            String preName = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getItemName()),10,1),11,1);
            String HSN = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getHSNCode()),7,1),7,1);

            String preQty = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.valueOf(billKotItem.getQty())+billKotItem.getUOM(),8,1),9,1);
            String preRate = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",billKotItem.getRate()),9,1),10,1);
            String preAmount = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",billKotItem.getAmount())
                                                                                                +billKotItem.getTaxIndex(),14,1),14,1);
            String pre = preId+preName+/*HSN+*/preQty+preRate+preAmount;
            esc.addText(pre+"\n");
            totalitemtypes++;
            totalquantitycount += billKotItem.getQty();
            subtotal += billKotItem.getAmount();
        }
        esc.addText("------------------------------------------------"+"\n");
        esc.addText(getSpaceFormater("Total Item(s) : "+totalitemtypes+" /Qty : "+totalquantitycount,String.format("%.2f",subtotal),48,1)+"\n");
        float discount = item.getFdiscount();
        float discountPercentage = item.getdiscountPercentage();
        if(discountPercentage > 0)
        {
            String DiscName = getPostAddedSpaceFormat("","Discount Amount",23,1);
            String DiscPercent = getPostAddedSpaceFormat("","@ " + String.format("%.2f",discountPercentage) + " %",15,1);
            String DiscValue = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",discount),10,1),8 ,1);
            String pre = DiscName + DiscPercent + DiscValue;
            esc.addText(pre+"\n");
        }
        else if (discount > 0)
        {
            esc.addText(getSpaceFormater("Discount Amount",String.format("%.2f",discount),48,1)+"\n");
        }
        ArrayList<BillTaxItem> billOtherChargesItems = item.getBillOtherChargesItems();
        if(billOtherChargesItems.size()>0)
        {
            Iterator it1 = billOtherChargesItems.iterator();
            while (it1.hasNext())
            {
                BillTaxItem billKotItem = (BillTaxItem) it1.next();
                String TxName = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getTxName()),23,1);
                String TxPercent = getPostAddedSpaceFormat("","",15,1);
                String TxValue = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",billKotItem.getPrice()),10,1),8 ,1);
                String pre = TxName + TxPercent + TxValue;
                esc.addText(pre+"\n");
            }
        }
        // Tax Slab
        double dTotTaxAmt = 0;
        ArrayList<BillTaxSlab> billTaxSlab = item.getBillTaxSlabs();
        Iterator it11 = billTaxSlab.iterator();
        if(item.getIsInterState().equalsIgnoreCase("n")) // IntraState
        {
            if (it11.hasNext())
            {
                esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                esc.addText("================================================"+"\n");
                esc.addText("Tax(%)   TaxableVal   CGSTAmt  SGSTAmt    TaxAmt"+"\n");
                esc.addText("================================================"+"\n");
                esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                do
                {
                    BillTaxSlab billTaxSlabEntry = (BillTaxSlab) it11.next();
                    if(billTaxSlabEntry.getTaxRate()> 0)
                    {
                        String TxIndex = getPostAddedSpaceFormat("",String.valueOf(billTaxSlabEntry.getTaxIndex())+" "+
                                String.format("%.2f",billTaxSlabEntry.getTaxRate()),7,1);
                        String TaxableValue = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f", billTaxSlabEntry.getTaxableValue()),12,1),13,1);
                        String CGSTAmt = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f", billTaxSlabEntry.getCGSTAmount()),8,1),9,1);
                        String SGSTAmt = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f", billTaxSlabEntry.getSGSTAmount()),8,1),9,1);
                        String TotalTax = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f",billTaxSlabEntry.getTotalTaxAmount()),10,1),10,1);

                        String pre = TxIndex + TaxableValue + CGSTAmt+ SGSTAmt + TotalTax;
                        dTotTaxAmt += billTaxSlabEntry.getCGSTAmount()+billTaxSlabEntry.getSGSTAmount();
                        esc.addText(pre+"\n");

                    }
                }while (it11.hasNext());
            }
        }else // InterState
        {
            if (it11.hasNext())
            {
                esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                esc.addText("================================================"+"\n");
                esc.addText("Tax(%)   TaxableVal   IGSTAmt             TaxAmt"+"\n");
                esc.addText("================================================"+"\n");
                esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
                do
                {
                    BillTaxSlab billTaxSlabEntry = (BillTaxSlab) it11.next();
                    if(billTaxSlabEntry.getTaxRate()> 0)
                    {
                        String TxIndex = getPostAddedSpaceFormat("",String.valueOf(billTaxSlabEntry.getTaxIndex())+" "+
                                String.format("%.2f",billTaxSlabEntry.getTaxRate()),7,1);
                        String TaxableValue = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f", billTaxSlabEntry.getTaxableValue()),12,1),13,1);
                        String IGSTAmt = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f", billTaxSlabEntry.getIGSTAmount()),8,1),9,1);
                        String CGSTAmt = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init("",8,1),9,1);
                        String TotalTax = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f",billTaxSlabEntry.getTotalTaxAmount()),10,1),10,1);

                        String pre = TxIndex + TaxableValue + IGSTAmt+ CGSTAmt + TotalTax;
                        dTotTaxAmt += billTaxSlabEntry.getIGSTAmount();
                        esc.addText(pre+"\n");

                    }
                }while (it11.hasNext());
            }
        }

        esc.addText("\n");
        double  dtotalcessAmt =0;
        ArrayList<BillServiceTaxItem> billcessTaxItems = item.getBillcessTaxItems();
        Iterator it21 = billcessTaxItems.iterator();
        while (it21.hasNext()) {

            BillServiceTaxItem billKotItem = (BillServiceTaxItem) it21.next();
            if (billKotItem.getServicePercent() > 0){

                String TxName = getPostAddedSpaceFormat("", String.valueOf(billKotItem.getServiceTxName()), 23, 1);
                String TxPercent = getPostAddedSpaceFormat("", "@ " + String.format("%.2f", billKotItem.getServicePercent()) + " %", 15, 1);
                String TxValue = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f", billKotItem.getServicePrice()), 10, 1), 8, 1);
                dtotalcessAmt += billKotItem.getServicePrice();
                String pre = TxName + TxPercent + TxValue;
                esc.addText(pre + "\n");
            }
        }
        double dTotalTaxAmt = dTotTaxAmt +dtotalcessAmt;
        if(dTotalTaxAmt >0)
        {   esc.addText(getSpaceFormater("Total Tax Amount",String.format("%.2f",dTotalTaxAmt),48,1)+"\n");}
        esc.addText("================================================"+"\n");
        esc.addText(getSpaceFormater("TOTAL",String.format("%.2f",item.getNetTotal()),48,1)+"\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("================================================\n");
        if(!item.getFooterLine().equals(""))
            esc.addText(item.getFooterLine()+"\n");
        esc.addPrintAndFeedLines((byte)3);

        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    /*public String getPrintBill_old(PrintKotBillItem item) {
        EscCommand esc = new EscCommand();
        //esc.addPrintAndFeedLines((byte)2);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        //esc.addText("Resturant Bill"+"\n");
        esc.addText(item.getAddressLine1()+"\n");
        esc.addPrintAndLineFeed();

        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText(item.getAddressLine2()+"\n");
        String add = item.getAddressLine3();
        if(!add.trim().equals(""))
        {
            esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
            esc.addText(item.getAddressLine3()+"\n");
        }
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("===============================================\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("Bill no # "+item.getBillNo()+"\n");
        if(item.getBillingMode().equals("1"))
            esc.addText("Table # "+item.getTableNo()+"\n");
        esc.addText("Date : "+item.getDate() +" | "+"Time : "+item.getTime()+"\n");
        esc.addText("Cashier   : "+item.getOrderBy()+"\n");
        esc.addText("Customer Name   : "+item.getCustomerName()+"\n");
        if(item.getBillingMode().equalsIgnoreCase("4") || item.getBillingMode().equalsIgnoreCase("3")) {
            esc.addText("Payment Status   : " + item.getPaymentStatus()+"\n");
        }
        // -----------
       *//* if(item.getBillingMode().equalsIgnoreCase("1")) {
            esc.addText("Dine In" + "\n\n");
        } else if(item.getBillingMode().equalsIgnoreCase("2")) {
            esc.addText("Counter Sales" + "\n\n");
        } else if(item.getBillingMode().equalsIgnoreCase("3")) {
            esc.addText("Pick Up" + "\n\n");
        } else if(item.getBillingMode().equalsIgnoreCase("4")) {
            esc.addText("Home Delivery" + "\n\n");
        } else {
            esc.addText("-----------" + "\n");
        }*//*

        if(item.getBillingMode().equalsIgnoreCase("1") || item.getBillingMode().equalsIgnoreCase("2") ||
                item.getBillingMode().equalsIgnoreCase("3") || item.getBillingMode().equalsIgnoreCase("4")){
            esc.addText(item.getStrBillingModeName() + "\n\n");
        } else {
            esc.addText("-----------" + "\n");
        }

        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("SNo  ITEM NAME       QTY     RATE       AMOUNT "+"\n");
        esc.addText("================================================"+"\n");
        ArrayList<BillKotItem> billKotItems = item.getBillKotItems();
        Iterator it = billKotItems.iterator();
        while (it.hasNext())
        {
            BillKotItem billKotItem = (BillKotItem) it.next();

            *//*String amt = getFormatedCharacterForPrint(String.format("%.2f",billKotItem.getAmount()),6,1);
            if(amt.length() < 6)
                amt = ""+amt;*//*
            String preId = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getItemId()),4,1);
            String preName = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getItemName()),10,1),11,1);
            String HSN = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(billKotItem.getHSNCode()),7,1),7,1);

            String preQty = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.valueOf(billKotItem.getQty()),8,1),7,1);
            //String preRate = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.format("%.2f",billKotItem.getRate()),6,1),9,1);
            //String preAmount = getPostAddedSpaceFormat("",String.format("%.2f",billKotItem.getAmount()),7,1);
            String preRate = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",billKotItem.getRate()),10,1),11,1);
            String preAmount = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",billKotItem.getAmount()),13,1),15,1);
            String pre = preId+preName+*//*HSN+*//*preQty+preRate+preAmount;
            esc.addText(pre+"\n");
        }
        esc.addText("================================================"+"\n");
        esc.addText(getSpaceFormater("Sub-Total",String.format("%.2f",item.getSubTotal()),48,1)+"\n");
        float discount = item.getFdiscount();
        float discountPercentage = item.getdiscountPercentage();
        if(discountPercentage > 0)
        {
            String DiscName = getPostAddedSpaceFormat("","Discount Amount",23,1);
            String DiscPercent = getPostAddedSpaceFormat("","@ " + String.format("%.2f",discountPercentage) + " %",15,1);
            String DiscValue = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",discount),10,1),8 ,1);
            String pre = DiscName + DiscPercent + DiscValue;
            esc.addText(pre+"\n");
        }
        else if (discount > 0)
        {
            esc.addText(getSpaceFormater("Discount Amount",String.format("%.2f",discount),48,1)+"\n");
        }
        ArrayList<BillTaxItem> billOtherChargesItems = item.getBillOtherChargesItems();
        if(billOtherChargesItems.size()>0)
        {
            Iterator it1 = billOtherChargesItems.iterator();
            while (it1.hasNext())
            {
                BillTaxItem billKotItem = (BillTaxItem) it1.next();
                String TxName = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getTxName()),23,1);
                String TxPercent = getPostAddedSpaceFormat("","",15,1);
                //String TxValue = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getPrice()),8,1);
                String TxValue = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",billKotItem.getPrice()),10,1),8 ,1);

                //String pre = getSpaceFormat(billKotItem.getTxName(),String.valueOf(billKotItem.getPercent()),String.valueOf(billKotItem.getPrice()),36,3);
                String pre = TxName + TxPercent + TxValue;
//                dSalesTaxAmt += billKotItem.getPrice();
                esc.addText(pre+"\n");
            }
        }
        // Sales Tax
        double dTotalTaxAmt = 0, dSalesTaxAmt = 0;
        ArrayList<BillTaxItem> billTaxItems = item.getBillTaxItems();
        Iterator it1 = billTaxItems.iterator();
        while (it1.hasNext())
        {
            BillTaxItem billKotItem = (BillTaxItem) it1.next();
            if(billKotItem.getPercent()> 0)
            {
                String TxName = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getTxName()),23,1);
                String TxPercent = getPostAddedSpaceFormat("","@ " + String.format("%.2f", billKotItem.getPercent()) + " %",15,1);
                //String TxValue = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getPrice()),8,1);
                String TxValue = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",billKotItem.getPrice()),10,1),8 ,1);

                //String pre = getSpaceFormat(billKotItem.getTxName(),String.valueOf(billKotItem.getPercent()),String.valueOf(billKotItem.getPrice()),36,3);
                String pre = TxName + TxPercent + TxValue;
                dSalesTaxAmt += billKotItem.getPrice();
                esc.addText(pre+"\n");
            }
        }
        // Service Sub Tax Calculation
        double dSerSubTaxPer = 0, dSerSubTaxAmt = 0;
        *//*ArrayList<BillSubTaxItem> billSubTaxItemsCal = item.getBillSubTaxItems();
        Iterator itCal = billSubTaxItemsCal.iterator();
        while (itCal.hasNext())
        {
            BillSubTaxItem billKotItem = (BillSubTaxItem) itCal.next();
            dSerSubTaxPer += billKotItem.getPercent();
            dSerSubTaxAmt += billKotItem.getPrice();
        }*//*

        // Service Tax
        double dServiceTaxPer = 0, dServiceTaxAmt = 0, dtotalServiceAmt =0;
        boolean isServiceTaxApplied = false;
        ArrayList<BillServiceTaxItem> billServiceTaxItems = item.getBillServiceTaxItems();
        Iterator it2 = billServiceTaxItems.iterator();
        while (it2.hasNext()) {

            BillServiceTaxItem billKotItem = (BillServiceTaxItem) it2.next();
            if (billKotItem.getServicePercent() > 0){
                //isServiceTaxApplied = true;
                dServiceTaxPer = billKotItem.getServicePercent();
                dServiceTaxAmt = billKotItem.getServicePrice();
                dServiceTaxPer = dServiceTaxPer - dSerSubTaxPer;
                dServiceTaxAmt = dServiceTaxAmt - dSerSubTaxAmt;
                dtotalServiceAmt += dServiceTaxAmt;
                String TxName = getPostAddedSpaceFormat("", String.valueOf(billKotItem.getServiceTxName()), 23, 1);
                String TxPercent = getPostAddedSpaceFormat("", "@ " + String.format("%.2f", dServiceTaxPer) + " %", 15, 1);
                String TxValue = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f", dServiceTaxAmt), 10, 1), 8, 1);
                //String pre = getSpaceFormat(billKotItem.getServiceTxName(),String.valueOf(billKotItem.getServicePercent()),String.valueOf(billKotItem.getServicePrice()),36,1);
                String pre = TxName + TxPercent + TxValue;
                esc.addText(pre + "\n");
            }
        }
        // Service Sub Tax
        if(isServiceTaxApplied){
        ArrayList<BillSubTaxItem> billSubTaxItems = item.getBillSubTaxItems();
        Iterator it3 = billSubTaxItems.iterator();
            while (it3.hasNext())
            {
                BillSubTaxItem billKotItem = (BillSubTaxItem) it3.next();
                String TxName = getPostAddedSpaceFormat("",String.valueOf(billKotItem.getTxName()),23,1);
                String TxPercent = getPostAddedSpaceFormat("","@ " + String.format("%.2f",billKotItem.getPercent()) + " %",15,1);
                String TxValue = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.format("%.2f",billKotItem.getPrice()),7,1),8,1);
                //String pre = getSpaceFormat(billKotItem.getTxName(), String.format("%.2f",billKotItem.getPercent()), String.format("%.2f",billKotItem.getPrice()),36,0);
                String pre = TxName + TxPercent + TxValue;
                esc.addText(pre+"\n");
            }
        }
        else
        {
            dSerSubTaxAmt = 0;
        }
        double  dtotalcessAmt =0;
        ArrayList<BillServiceTaxItem> billcessTaxItems = item.getBillcessTaxItems();
        Iterator it21 = billcessTaxItems.iterator();
        while (it21.hasNext()) {

            BillServiceTaxItem billKotItem = (BillServiceTaxItem) it21.next();
            if (billKotItem.getServicePercent() > 0){

                String TxName = getPostAddedSpaceFormat("", String.valueOf(billKotItem.getServiceTxName()), 23, 1);
                String TxPercent = getPostAddedSpaceFormat("", "@ " + String.format("%.2f", billKotItem.getServicePercent()) + " %", 15, 1);
                String TxValue = getPostAddedSpaceFormat("", getFormatedCharacterForPrint_init(String.format("%.2f", billKotItem.getServicePrice()), 10, 1), 8, 1);
                dtotalcessAmt += billKotItem.getServicePrice();
                String pre = TxName + TxPercent + TxValue;
                esc.addText(pre + "\n");
            }
        }
        dTotalTaxAmt = dSalesTaxAmt + dtotalServiceAmt + dSerSubTaxAmt+dtotalcessAmt;
        if(dTotalTaxAmt >0)
        {   esc.addText(getSpaceFormater("Total Tax Amount",String.format("%.2f",dTotalTaxAmt),48,1)+"\n");}
        esc.addText("================================================"+"\n");
        esc.addText(getSpaceFormater("TOTAL",String.format("%.2f",item.getNetTotal()),48,1)+"\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText("================================================\n");
        if(!item.getFooterLine().equals(""))
            esc.addText(item.getFooterLine()+"\n");
        esc.addPrintAndFeedLines((byte)3);

        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }
*/
    public String getPrintIngredients(ArrayList<PrintIngredientsModel> item) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)2);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        //esc.addText("Resturant Bill"+"\n");
        esc.addText("ItemIngredients List\n");
        esc.addPrintAndLineFeed();
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("===============================================\n");

        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("ITEMID ITEM NAME               QTY    UOM "+"\n");
        esc.addText("==============================================="+"\n");

        Iterator it = item.iterator();
        int count =0;
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        while (it.hasNext())
        {
            PrintIngredientsModel Item = (PrintIngredientsModel) it.next();
            count++;

            String preId = getPostAddedSpaceFormat("",String.valueOf(Item.getItemId()),7,1);
            if(!String.valueOf(Item.getItemId()).equals(" ")&& count >1)
                esc.addText("-----------------------------------------------"+"\n");
            String preName = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(Item.getItemName()),22,1),17,1);
            String preQty = getPostAddedSpaceFormat("",getFormatedCharacterForPrint_init(String.valueOf(Item.getQty()),7,1),10,1);
            String preUOM = getPostAddedSpaceFormat("",getFormatedCharacterForPrint(String.valueOf(Item.getUom()),4,1),8,1);
            String pre = preId+preName+preQty+preUOM;
            esc.addText(pre+"\n");
        }
        esc.addText("==============================================="+"\n");
        esc.addPrintAndFeedLines((byte)3);
        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    public String getPrintReport(ArrayList<ArrayList<String>> itemReport, String reportName) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)3);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF, EscCommand.ENABLE.OFF);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addText(reportName+"\n");
        esc.addText("========================================"+"\n");
        Calendar time = Calendar.getInstance();
        esc.addText("["+String.format("%tR", time)+"]\n");
        esc.addPrintAndLineFeed();

        for(int j=0;j<itemReport.size();j++)
        {
            ArrayList<String> arrayListColumn = itemReport.get(j);
            StringBuffer sb = new StringBuffer();
            for (int i=0;i<arrayListColumn.size();i++)
            {
                String str = arrayListColumn.get(i);
                String preTxt = getAbsoluteCharacter(String.valueOf(str),10,1);
                if(j==0)
                    sb.append(preTxt+" |");
                else
                    sb.append(" "+preTxt+" ");
                int rem = i%3;
                if(rem == 0 && i!=0)
                {
                    esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
                    esc.addText(sb.toString()+"\n");
                    sb = new StringBuffer();
                }

                if(rem != 0 && (arrayListColumn.size()-1)==i)
                {
                    esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
                    esc.addText(getAbsoluteCharacter1(sb.toString(),1)+"\n");
                    sb = new StringBuffer();
                }
                /*else if(i == arrayListColumn.size())
                {
                    tmpList.add(CreateBitmap.AddText(18, "Bold","MONOSPACE", sb.toString(), TextAlign.LEFT,getApplicationContext()));
                    sb = new StringBuffer();
                }*/
            }
            if(j==0)
                esc.addText("========================================"+"\n");
        }
        esc.addText("========================================"+"\n");
        esc.addPrintAndFeedLines((byte)3);
        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }

    public String getPrintMSwipePaymentBill(Payment payment, String reportName) {
        EscCommand esc = new EscCommand();
        esc.addPrintAndFeedLines((byte)3);
        esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);
        esc.addSelectPrintModes(EscCommand.FONT.FONTA, EscCommand.ENABLE.OFF, EscCommand.ENABLE.ON, EscCommand.ENABLE.ON, EscCommand.ENABLE.OFF);
        esc.addText(reportName+"\n");
        esc.addText("======================================,"+"\n");
        esc.addText(String.valueOf(payment.getMerchantName())+"\n");
        esc.addText(String.valueOf(payment.getMerchantName())+"\n");
        esc.addText(String.valueOf(payment.getMerchantAdd())+"\n");
        esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);
        esc.addText("======================================,"+"\n");
        esc.addText("Date/Time     :"+String.valueOf(payment.getDateTime())+"\n");
        esc.addText("Invoice Id    :"+String.valueOf(payment.getInvoiceNo())+"\n");
        esc.addText("Card Name     : "+String.valueOf(payment.getCardHolderName())+"\n");
        esc.addText("Card Number   :"+String.valueOf(payment.getCardNo())+"\n");
        esc.addText("Expiry Date   : "+String.valueOf(payment.getExpDate())+"\n");
        esc.addText("Card Type     : "+String.valueOf(payment.getCardType())+"\n");
        esc.addText("Reference No  : "+String.valueOf(payment.getRefNo())+"\n");
        esc.addText("======================================"+"\n");
        esc.addText("Total         : "+String.valueOf(payment.getTotal_Pay_Amount())+"\n");
        Vector<Byte> datas = esc.getCommand();
        Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
        byte[] bytes = ArrayUtils.toPrimitive(Bytes);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        return str;
    }
}
