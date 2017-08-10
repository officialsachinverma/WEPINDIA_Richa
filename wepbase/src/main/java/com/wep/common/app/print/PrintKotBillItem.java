package com.wep.common.app.print;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by PriyabratP on 06-09-2016.
 */
public class PrintKotBillItem implements Serializable {

    private String billNo;
    private String tableNo;
    private int waiterNo;
    private String orderBy;
    private String customerName;
    private String date;
    private String time;
    private double subTotal;
    private double netTotal;
    private String addressLine1 = "";
    private String addressLine2 = "";
    private String addressLine3 = "";
    private String HeaderLine = "";
    private String footerLine = "";
    private ArrayList<BillKotItem> billKotItems;
    private ArrayList<BillTaxItem> billTaxItems;
    private ArrayList<BillServiceTaxItem> billServiceTaxItems;
    private ArrayList<BillServiceTaxItem> billcessTaxItems;
    private ArrayList<BillSubTaxItem> billSubTaxItems;
    private ArrayList<BillTaxSlab> billTaxSlabs;

    private ArrayList<BillTaxItem> billOtherChargesItems;
    private String strBillingModeName = "";


    private String strBillingMode = "";
    private String strPaymentStatus = "";
    private String strTotalSalesTaxAmount = "";
    private String strTotalServiceTaxAmount = "";
    private float fTotalsubTaxPercent = 0;
    private float fdiscount = 0;
    private float fdiscountPercentage = 0;
    private String isInterState = "";
    private String isDuplicate = "";


    public PrintKotBillItem() {
    }

    public PrintKotBillItem(String billNo, String tableNo, int waiterNo, String orderBy, String customerName, String date,
                            String time, double subTotal, double netTotal, String addressLine1, String addressLine2,
                            String addressLine3, String footerLine,String HeaderLine, ArrayList<BillKotItem> billKotItems, ArrayList<BillTaxItem> billTaxItems,
                            String BillingMode, String PaymentStatus, String TotalSalesTaxAmount, String TotalServiceTaxAmount,
                            Float TotalsubTaxPercent,  ArrayList<BillTaxItem> otherCharges, String BillingModeName,float fdiscount, float fdiscountPercentage) {
        this.billNo = billNo;
        this.tableNo = tableNo;
        this.waiterNo = waiterNo;
        this.orderBy = orderBy;
        this.customerName = customerName;
        this.date = date;
        this.time = time;
        this.subTotal = subTotal;
        this.netTotal = netTotal;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.footerLine = footerLine;
        this.HeaderLine = HeaderLine;
        this.billKotItems = billKotItems;
        this.billTaxItems = billTaxItems;

        this.strBillingMode = BillingMode;
        this.strPaymentStatus = PaymentStatus;

        this.strTotalSalesTaxAmount = TotalSalesTaxAmount;
        this.strTotalServiceTaxAmount = TotalServiceTaxAmount;
        this.fTotalsubTaxPercent = TotalsubTaxPercent;
        this.fdiscount = fdiscount;
        this.fdiscountPercentage = fdiscountPercentage;
        this.billOtherChargesItems = otherCharges;
        this.strBillingModeName = BillingModeName;
    }

    public ArrayList<BillTaxSlab> getBillTaxSlabs() {
        return billTaxSlabs;
    }

    public void setBillTaxSlabs(ArrayList<BillTaxSlab> billTaxSlabs) {
        this.billTaxSlabs = billTaxSlabs;
    }

    public String getIsDuplicate() {
        return isDuplicate;
    }

    public void setIsDuplicate(String isDuplicate) {
        this.isDuplicate = isDuplicate;
    }

    public String getIsInterState() {
        return isInterState;
    }

    public void setIsInterState(String isInterState) {
        this.isInterState = isInterState;
    }

    public ArrayList<BillServiceTaxItem> getBillcessTaxItems() {
        return billcessTaxItems;
    }

    public void setBillcessTaxItems(ArrayList<BillServiceTaxItem> billcessTaxItems) {
        this.billcessTaxItems = billcessTaxItems;
    }

    public float getdiscountPercentage() {
        return fdiscountPercentage;
    }

    public void setdiscountPercentage(float fdiscountPercentage) {
        this.fdiscountPercentage = fdiscountPercentage;
    }

    public float getFdiscount() {
        return fdiscount;
    }

    public void setFdiscount(float fdiscount) {
        this.fdiscount = fdiscount;
    }

    public String getStrBillingModeName() {
        return strBillingModeName;
    }

    public void setStrBillingModeName(String strBillingModeName) {
        this.strBillingModeName = strBillingModeName;
    }

    public ArrayList<BillTaxItem> getBillOtherChargesItems() {
        return billOtherChargesItems;
    }

    public void setBillOtherChargesItems(ArrayList<BillTaxItem> billOtherChargesItems) {
        this.billOtherChargesItems = billOtherChargesItems;
    }

    public Float getTotalsubTaxPercent() {
        return fTotalsubTaxPercent;
    }

    public void setTotalsubTaxPercent(Float TotalsubTaxPercent) {
        this.fTotalsubTaxPercent = TotalsubTaxPercent;
    }

    public String getTotalSalesTaxAmount() {
        return strTotalSalesTaxAmount;
    }

    public void setTotalSalesTaxAmount(String TotalSalesTaxAmount) {
        this.strTotalSalesTaxAmount = TotalSalesTaxAmount;
    }

    public String getTotalServiceTaxAmount() {
        return strTotalServiceTaxAmount;
    }

    public void setTotalServiceTaxAmount(String TotalServiceTaxAmount) {
        this.strTotalServiceTaxAmount = TotalServiceTaxAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getTableNo() {
        return tableNo;
    }

    public void setTableNo(String tableNo) {
        this.tableNo = tableNo;
    }

    public int getWaiterNo() {
        return waiterNo;
    }

    public void setWaiterNo(int waiterNo) {
        this.waiterNo = waiterNo;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getNetTotal() {
        return netTotal;
    }

    public void setNetTotal(double netTotal) {
        this.netTotal = netTotal;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getFooterLine() {
        return footerLine;
    }

    public void setFooterLine(String footerLine) {
        this.footerLine = footerLine;
    }

    public String getHeaderLine() { return HeaderLine; }

    public void setHeaderLine(String HeaderLine) {
        this.HeaderLine = HeaderLine;
    }

    public ArrayList<BillKotItem> getBillKotItems() {
        return billKotItems;
    }

    public void setBillKotItems(ArrayList<BillKotItem> billKotItems) {
        this.billKotItems = billKotItems;
    }

    public ArrayList<BillTaxItem> getBillTaxItems() {
        return billTaxItems;
    }

    public void setBillTaxItems(ArrayList<BillTaxItem> billTaxItems) {
        this.billTaxItems = billTaxItems;
    }

    public ArrayList<BillServiceTaxItem> getBillServiceTaxItems() {
        return billServiceTaxItems;
    }

    public void setBillServiceTaxItems(ArrayList<BillServiceTaxItem> billServiceTaxItems) {
        this.billServiceTaxItems = billServiceTaxItems;
    }

    public ArrayList<BillSubTaxItem> getBillSubTaxItems() {
        return billSubTaxItems;
    }

    public void setBillSubTaxItems(ArrayList<BillSubTaxItem> billSubTaxItems) {
        this.billSubTaxItems = billSubTaxItems;
    }

    // ---  added by raja
    public String getBillingMode() {
        return strBillingMode;
    }

    public void setBillingMode(String BillingMode) {
        this.strBillingMode = BillingMode;
    }

    public String getPaymentStatus() {
        return strPaymentStatus;
    }

    public void setPaymentStatus(String PaymentStatus) {
        this.strPaymentStatus = PaymentStatus;
    }

    // --------------------
}
