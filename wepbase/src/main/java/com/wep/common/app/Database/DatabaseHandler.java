/***************************************************************************
 * Project Name		:	VAJRA
 * <p/>
 * File Name		:	DatabaseHandler
 * <p/>
 * DateOfCreation	:	13-October-2012
 * <p/>
 * Author			:	Balasubramanya Bharadwaj B S
 **************************************************************************/

package com.wep.common.app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.Toast;

import com.wep.common.app.gst.GSTR1_CDN_Details;
import com.wep.common.app.gst.Model_reconcile;
import com.wep.common.app.gst.get.GetGSTR1CounterPartySummary;
import com.wep.common.app.gst.get.GetGSTR1SecSummary;
import com.wep.common.app.gst.get.GetGSTR1Summary;
import com.wep.common.app.gst.get.GetGSTR2B2BFinal;
import com.wep.common.app.gst.get.GetGSTR2B2BInvoice;
import com.wep.common.app.gst.get.GetGSTR2B2BItem;
import com.wep.common.app.models.GSTR2_B2B_Amend;
import com.wep.common.app.models.ItemInward;
import com.wep.common.app.models.ItemOutward;
import com.wep.common.app.models.ItemStock;
import com.wep.common.app.models.Items;
import com.wep.common.app.print.Payment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final String TAG = DatabaseHandler.class.getSimpleName();
    // Variable section
    static String colName = "_id";
    static String colAge = "Age";

    // DatabaseVersion
    private static final int DB_VERSION = 1;
    String strDate = "";
    Date strDate_date;
    Calendar Time; // Time variable
    // Database path
    // "/sdcard/AppDatabase/"; //"/data/data/com.wepindia.pos/databases/";
    private static String DB_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_FnB/";

    // Database backup path
    private static String DB_BACKUP_PATH = Environment.getExternalStorageDirectory().getPath() + "/WeP_FnB_Backup/";

    // Database name
    private static final String DB_NAME = "WeP_FnB_Database.db";

    // Database table names

    private static final String TBL_BILLSETTING = "BillSetting";
    private static final String TBL_BILLNORESETCONFIG = "BillNoConfiguration";
    private static final String TBL_CATEGORY = "Category";
    private static final String TBL_COMPLIMENTARYBILLDETAIL = "ComplimentaryBillDetail";
    private static final String TBL_COUPON = "Coupon";
    private static final String TBL_CUSTOMER = "Customer";
    private static final String TBL_DELETEDKOT = "DeletedKOT";
    private static final String TBL_DEPARTMENT = "Department";
    private static final String TBL_DESCRIPTION = "Description";
    private static final String TBL_DISCOUNTCONFIG = "DiscountConfig";
    private static final String TBL_VOUCHERCONFIG = "VoucherConfig";
    private static final String TBL_EMPLOYEE = "Employee";
    private static final String TBL_KOTMODIFIER = "KOTModifier";
    private static final String TBL_KITCHEN = "Kitchen";
    private static final String TBL_MACHINESETTING = "MachineSetting";
    private static final String TBL_PAYMENTRECEIPT = "PaymentReceipt";
    private static final String TBL_PENDINGKOT = "PendingKOT";
    private static final String TBL_RIDERSETTLEMENT = "RiderSettlement";
    private static final String TBL_TAXCONFIG = "TaxConfig";
    private static final String TBL_SUBTAXCONFIG = "TaxConfigSub";
    private static final String TBL_USER = "User";
    private static final String TBL_TABLEBOOKING = "TableBooking";
    private static final String TBL_MAILSETTING = "MailConfiguration";
    private static final String TBL_REPORTSMASTER = "ReportsMaster";
    private static final String TBL_PURCHASEORDER = "PurchaseOrder";
    private static final String TBL_GOODSINWARD = "GoodsInward";
    private static final String TBL_INGREDIENTS = "Ingredients";
    private static final String TBL_SupplierItemLinkage= "SupplierItemLinkage";


    // Column Names for the tables
    private static final String KEY_ServiceTaxPercent = "ServiceTaxPercent";
    private static final String KEY_TaxType = "TaxType";
    private static final String KEY_DiscountType = "DiscountType";
    private static final String KEY_FastBillingMode = "FastBillingMode";
    private static final String KEY_CategCode = "CategCode";
    private static final String KEY_DeptCode = "DeptCode";
    private static final String KEY_CustId = "CustId";
    private static final String KEY_EmployeeId = "EmployeeId";
    private static final String KEY_SubUdfNumber = "SubUdfNumber";
    private static final String KEY_TableNumber = "TableNumber";
    private static final String KEY_DiscId = "DiscId";
    private static final String KEY_KitchenCode = "KitchenCode";
    private static final String KEY_ModifierAmount = "ModifierAmount";
    private static final String KEY_Reason = "Reason";
    private static final String KEY_Amount = "Amount";
    private static final String KEY_ServiceTaxAmount = "ServiceTaxAmount";
    private static final String KEY_DiscountAmount = "DiscountAmount";
    private static final String KEY_DiscountPercent = "DiscountPercent";
    private static final String KEY_ItemName = "ItemName";
    private static final String KEY_SalesTax = "SalesTax";
    private static final String KEY_OtherTax = "OtherTax";
    private static final String KEY_ItemNumber = "ItemNumber";
    private static final String KEY_Quantity = "Quantity";
    private static final String KEY_Rate = "Rate";
    private static final String KEY_Time = "Time";
    private static final String KEY_TokenNumber = "TokenNumber";
    private static final String KEY_Table_Split_No = "TableSplitNo";
    private static final String KEY_DeliveryCharge = "DeliveryCharge";
    private static final String KEY_BillAmount = "BillAmount";
    // private static final String KEY_InvoiceNo = "InvoiceNo";
    private static final String KEY_TotalItems = "TotalItems";
    private static final String KEY_UserId = "UserId";
    private static final String KEY_IngredientCode = "IngredientCode";
    private static final String KEY_IngredientName = "IngredientName";
    private static final String KEY_IngredientQuantity = "IngredientQuantity";
    private static final String KEY_IngredientUOM = "IngredientUOM";
    //private static final String KEY_InvoiceDate = "InvoiceDate";

    // BillDetail
    private static final String KEY_TotalServiceTaxAmount = "TotalServiceTaxAmount";
    private static final String KEY_BillStatus = "BillStatus";
    private static final String KEY_CardPayment = "CardPayment";
    private static final String KEY_CashPayment = "CashPayment";
    private static final String KEY_CouponPayment = "CouponPayment";
    private static final String KEY_WalletPayment = "WalletPayment";
    private static final String KEY_ReprintCount = "ReprintCount";
    private static final String KEY_TotalDiscountAmount = "TotalDiscountAmount";
    private static final String KEY_TotalTaxAmount = "TotalTaxAmount";

    private static final String KEY_PettyCashPayment = "PettyCashPayment";
    private static final String KEY_PaidTotalPayment = "PaidTotalPayment";
    private static final String KEY_ChangePayment = "ChangePayment";

    // BillItem
    private static final String KEY_TaxAmount = "TaxAmount";
    private static final String KEY_TaxPercent = "TaxPercent";

    // BillSetting
    private static final String KEY_DineIn3Caption = "DineIn3Caption";
    private static final String KEY_DineIn2Caption = "DineIn2Caption";
    private static final String KEY_DineIn1Caption = "DineIn1Caption";
    private static final String KEY_BillwithoutStock = "BillwithoutStock";
    private static final String KEY_WeighScale = "WeighScale";
    private static final String KEY_ServiceTaxType = "ServiceTaxType";
    private static final String KEY_BusinessDate = "BusinessDate";
    private static final String KEY_DineIn1From = "DineIn1From";
    private static final String KEY_DineIn1To = "DineIn1To";
    private static final String KEY_DineIn2From = "DineIn2From";
    private static final String KEY_DineIn2To = "DineIn2To";
    private static final String KEY_DineIn3From = "DineIn3From";
    private static final String KEY_DineIn3To = "DineIn3To";
    private static final String KEY_FooterText = "FooterText";
    private static final String KEY_HeaderText = "HeaderText";
    private static final String KEY_KOTType = "KOTType";
    private static final String KEY_MaximumTables = "MaximumTables";
    private static final String KEY_MaximumWaiters = "MaximumWaiters";
    private static final String KEY_POSNumber = "POSNumber";
    private static final String KEY_PrintKOT = "PrintKOT";
    private static final String KEY_SubUdfText = "SubUdfText";
    private static final String KEY_TIN = "TIN";
    private static final String KEY_ActiveForBilling = "ActiveForBilling";
    private static final String KEY_LoginWith = "LoginWith";
    private static final String KEY_DateAndTime = "DateAndTime";
    private static final String KEY_PriceChange = "PriceChange";
    private static final String KEY_BillwithStock = "BillwithStock";
    private static final String KEY_Tax = "Tax";
    private static final String KEY_KOT = "KOT";
    private static final String KEY_Token = "Token";
    private static final String KEY_Kitchen = "Kitchen";
    private static final String KEY_OtherChargesItemwise = "OtherChargesItemwise";
    private static final String KEY_OtherChargesBillwise = "OtherChargesBillwise";
    private static final String KEY_Peripherals = "Peripherals";
    private static final String KEY_RestoreDefault = "RestoreDefault";
    private static final String KEY_DineInRate = "DineInRate";
    private static final String KEY_CounterSalesRate = "CounterSalesRate";
    private static final String KEY_PickUpRate = "PickUpRate";
    private static final String KEY_HomeDeliveryRate = "HomeDeliveryRate";
    private static final String KEY_HomeDineInCaption = " HomeDineInCaption";
    private static final String KEY_HomeCounterSalesCaption = " HomeCounterSalesCaption";
    private static final String KEY_HomeTakeAwayCaption = " HomeTakeAwayCaption";
    private static final String KEY_HomeHomeDeliveryCaption = " HomeHomeDeliveryCaption";
    private static final String KEY_ItemNoReset = " ItemNoReset";
    private static final String KEY_PrintPreview = " PrintPreview";
    private static final String KEY_CummulativeHeadingEnable = " CummulativeHeadingEnable"; // richa_2012
    private static final String KEY_TableSpliting = " TableSpliting";

    // Bill No Reset
    private static final String KEY_BillNoReset_InvoiceNo = " InvoiceNo";
    private static final String KEY_BillNoReset_Period = " Period";
    private static final String KEY_BillNoReset_PeriodDate = " PeriodDate";

    // Category
    private static final String KEY_CategName = "CategName";

    // Complimentary Bill Detail
    private static final String KEY_Complimentary_Reason = "ComplimentaryReason";
    private static final String KEY_Paid_Amount = "PaidAmount";

    // Department
    public static final String KEY_DeptName = "DeptName";

    // Coupon
    private static final String KEY_CouponAmount = "CouponAmount";
    private static final String KEY_CouponBarcode = "CouponBarcode";
    private static final String KEY_CouponDescription = "CouponDescription";
    private static final String KEY_CouponId = "CouponId";

    // Customer
    private static final String KEY_CustAddress = "CustAddress";
    private static final String KEY_CustContactNumber = "CustContactNumber";
    //private static final String KEY_CustName = "CustName";
    private static final String KEY_LastTransaction = "LastTransaction";
    private static final String KEY_TotalTransaction = "TotalTransaction";
    private static final String KEY_CreditAmount = "CreditAmount";

    // Description
    private static final String KEY_DescriptionId = "DescriptionId";
    private static final String KEY_DescriptionText = "DescriptionText";

    // DiscountConfig
    private static final String KEY_DiscDescription = "DiscDescription";
    private static final String KEY_DiscPercentage = "DiscPercentage";
    private static final String KEY_DiscAmount = "DiscAmount";

    // Employee
    private static final String KEY_EmployeeContactNumber = "EmployeeContactNumber";
    private static final String KEY_EmployeeName = "EmployeeName";
    private static final String KEY_EmployeeRole = "EmployeeRole";

    // Item
    private static final String KEY_ImageUri = "ImageUri";
    private static final String KEY_AdditionalTaxId = "AdditionalTaxId";
    private static final String KEY_BillWithStock = "BillWithStock";
    private static final String KEY_DeliveryPrice = "DeliveryPrice";
    private static final String KEY_DineInPrice1 = "DineInPrice1";
    private static final String KEY_DineInPrice2 = "DineInPrice2";
    private static final String KEY_DineInPrice3 = "DineInPrice3";
    private static final String KEY_DiscountEnable = "DiscountEnable";
    private static final String KEY_ItemBarcode = " ItemBarcode";
    private static final String KEY_LongName = " ItemName";
    private static final String KEY_MenuCode = " MenuCode";
    private static final String KEY_OptionalTaxId1 = "OptionalTaxId1";
    private static final String KEY_OptionalTaxId2 = "OptionalTaxId2";
    private static final String KEY_PickUpPrice = "PickUpPrice";
    private static final String KEY_SalesTaxId = "SalesTaxId";
    private static final String KEY_TakeAwayPrice = "TakeAwayPrice";
    private static final String KEY_SalesTaxPercent = "SalesTaxPercent";
    private static final String KEY_SerTaxPercent = "ServiceTaxPercent";
    private static final String KEY_ItemId = "ItemId";

    // KOTModifier
    private static final String KEY_IsChargeable = "IsChargeable";
    private static final String KEY_ModifierDescription = "ModifierDescription";
    private static final String KEY_ModifierId = "ModifierId";
    private static final String KEY_ModifierModes = "ModifierModes";

    // MachineSetting
    private static final String KEY_BaudRate = "BaudRate";
    private static final String KEY_DataBits = "DataBits";
    private static final String KEY_Parity = "Parity";
    private static final String KEY_PortName = "PortName";
    private static final String KEY_StopBits = "StopBits";

    // PaymentReceipt
    private static final String KEY_Id = "_Id";
    private static final String KEY_BillType = "BillType";
    private static final String KEY_DescriptionId1 = "DescriptionId1";
    private static final String KEY_DescriptionId2 = "DescriptionId2";
    private static final String KEY_DescriptionId3 = "DescriptionId3";

    // PendingKOT
    private static final String KEY_IsCheckedOut = "IsCheckedOut";
    private static final String KEY_OrderMode = "OrderMode";

    // KITCHEN
    private static final String KEY_KitchenName = "KitchenName";

    // RiderSettlement
    private static final String KEY_PettyCash = "PettyCash";
    private static final String KEY_SettledAmount = "SettledAmount";

    // TableBooking
    private static final String KEY_TBookId = "TBookId";
    private static final String KEY_CustomerName = "CustName";
    private static final String KEY_TimeForBooking = "TimeForBooking";
    private static final String KEY_TableNo = "TableNo";
    private static final String KEY_MobileNo = "MobileNo";

    // TaxConfig
    private static final String KEY_TaxDescription = "TaxDescription";
    private static final String KEY_TaxId = "TaxId";
    private static final String KEY_TaxPercentage = "TaxPercentage";
    private static final String KEY_TotalPercentage = "TotalPercentage";

    // TaxConfigSub
    private static final String KEY_SubTaxId = "SubTaxId";
    private static final String KEY_SubTaxDescription = "SubTaxDescription";
    private static final String KEY_SubTaxPercentage = "SubTaxPercent";

    // User
    private static final String KEY_AccessLevel = "AccessLevel";
    private static final String KEY_Password = "Password";
    private static final String KEY_UserName = "UserName";


    // VoucherConfig
    private static final String KEY_VoucherId = "VoucherId";
    private static final String KEY_VoucherDescription = "VoucherDescription";
    private static final String KEY_VoucherPercentage = "VoucherPercentage";

    // MailConfiguration
    private static final String KEY_FromMailId = "FromMailId";
    private static final String KEY_FromMailPassword = "FromMailPassword";
    private static final String KEY_SmtpServer = "SmtpServer";
    private static final String KEY_PortNo = "PortNo";
    private static final String KEY_FromDate = "FromDate";
    private static final String KEY_ToDate = "ToDate";
    private static final String KEY_SendMail = "SendMail";
    private static final String KEY_AutoMail = "AutoMail";

    // ReportsMaster
    private static final String KEY_ReportsId = "ReportsId";
    private static final String KEY_ReportsName = "ReportsName";
    private static final String KEY_ReportsType = "ReportsType";
    private static final String KEY_Status = "Status";

    // USERS_new
    private static final String TBL_USERS = "Users";
    private static final String TBL_USERSROLE = "UsersRole";
    private static final String TBL_USERROLEACCESS = "UserRoleAccess";
//    private static final String KEY_ID = "_id";

    private static final String KEY_ROLE_ID = "RoleId";
    private static final String KEY_ROLE_NAME = "RoleName";
    private static final String TABLE_NAME_ROLE_ACCESS_MAP = "_tbl_role_map";
    private static final String KEY_ACCESS_NAME = "RoleAccessName";
    private static final String KEY_ACCESS_CODE = "RoleAccessId";
    private static final String TABLE_NAME_USER = "tbl_user";
    private static final String KEY_USER_ID = "UserId";
    private static final String KEY_USER_NAME = "Name";
    private static final String KEY_USER_MOBILE = "Mobile";
    private static final String KEY_USER_DESIGNATION = "Designation";
    private static final String KEY_USER_LOGIN = "LoginId";
    private static final String KEY_USER_ADHAR = "AadhaarNo";
    private static final String KEY_USER_EMAIL = "Email";
    private static final String KEY_USER_ADDRESS = "Address";
    private static final String KEY_USER_FILE_LOCATION = "FileLocation";
    private static final String KEY_USER_PASS = "Password";


    private static final String TBL_TRANSACTIONS = "tbl_transactions";
    private static final String KEY_TRANS_ID = "transId";
    private static final String KEY_MERCHANT_NAME = "merchantName";
    private static final String KEY_MERCHANT_ADDRESS = "merchantAdd";
    private static final String KEY_DATE_TIME = "dateTime";
    private static final String KEY_M_ID = "mId";
    private static final String KEY_T_ID = "tId";
    private static final String KEY_BATCH_NO = "batchNo";
    private static final String KEY_VOUCHER_NO = "voucherNo";
    private static final String KEY_REFERENCE_NO = "refNo";
    private static final String KEY_BillNoPrefix = "BillNoPrefix";
    private static final String KEY_SALES_TYPE = "saleType";
    private static final String KEY_CARD_NO = "cardNo";

    private static final String KEY_TRX_TYPE = "trxType";
    private static final String KEY_CARD_TYPE = "cardType";
    private static final String KEY_EXP_DATE = "expDate";
    private static final String KEY_EMVSIGN_EXPDATE = "emvSigExpDate";
    private static final String KEY_CARDHOLDER_NAME = "cardHolderName";
    private static final String KEY_CURRENCY = "currency";
    private static final String KEY_CASH_AMOUNT = "cashAmount";
    private static final String KEY_BASE_AMOUNT = "baseAmount";
    private static final String KEY_TIP_AMOUNT = "tipAmount";
    private static final String KEY_TOTAL_AMOUNT = "totalAmount";

    private static final String KEY_AUTH_CODE = "authCode";
    private static final String KEY_RRNO = "rrNo";
    private static final String KEY_CERTIFI = "certif";
    private static final String KEY_APP_ID = "appId";
    private static final String KEY_APP_NAME = "appName";
    private static final String KEY_TVR = "tvr";
    private static final String KEY_TSI = "tsi";
    private static final String KEY_APP_VERSION = "appVersion";
    private static final String KEY_IS_PINVERIFIED = "isPinVarifed";
    private static final String KEY_STAN = "stan";

    private static final String KEY_CARD_ISSUER = "cardIssuer";
    private static final String KEY_CARD_EMI_AMOUNT = "emiPerMonthAmount";
    private static final String KEY_TOTAL_PAYAMOUNT = "total_Pay_Amount";
    private static final String KEY_NO_EMI = "noOfEmi";
    private static final String KEY_INTEREST_RATE = "interestRate";
    private static final String KEY_BILL_NO = "billNo";
    private static final String KEY_FIRST_DIGIT_OF_CARD = "firstDigitsOfCard";
    private static final String KEY_IS_INCONV = "isConvenceFeeEnable";
    private static final String KEY_INVOICE = "invoiceNo";
    private static final String KEY_TRX_DATE = "trxDate";

    private static final String KEY_TRX_IMG_DATE = "trxImgDate";
    private static final String KEY_CHEQUE_DATE = "chequeDate";
    private static final String KEY_CHEQUE_NO = "chequeNo";


    String QUERY_CREATE_TBL_TRANSACTIONS = "CREATE TABLE " + TBL_TRANSACTIONS + "("
            + KEY_TRANS_ID + " INTEGER PRIMARY KEY,"
            + KEY_MERCHANT_NAME + " TEXT,"
            + KEY_MERCHANT_ADDRESS + " TEXT,"
            + KEY_DATE_TIME + " TEXT,"
            + KEY_M_ID + " TEXT,"
            + KEY_T_ID + " TEXT,"
            + KEY_BATCH_NO + " TEXT,"
            + KEY_VOUCHER_NO + " TEXT,"
            + KEY_REFERENCE_NO + " TEXT,"
            + KEY_SALES_TYPE + " TEXT,"
            + KEY_CARD_NO + " TEXT,"
            + KEY_TRX_TYPE + " TEXT,"
            + KEY_CARD_TYPE + " TEXT,"
            + KEY_EXP_DATE + " TEXT,"
            + KEY_EMVSIGN_EXPDATE + " TEXT,"
            + KEY_CARDHOLDER_NAME + " TEXT,"
            + KEY_CURRENCY + " TEXT,"
            + KEY_CASH_AMOUNT + " TEXT,"
            + KEY_BASE_AMOUNT + " TEXT,"
            + KEY_TIP_AMOUNT + " TEXT,"
            + KEY_TOTAL_AMOUNT + " TEXT,"
            + KEY_AUTH_CODE + " TEXT,"
            + KEY_RRNO + " TEXT,"
            + KEY_CERTIFI + " TEXT,"
            + KEY_APP_ID + " TEXT,"
            + KEY_APP_NAME + " TEXT,"
            + KEY_TVR + " TEXT,"
            + KEY_TSI + " TEXT,"
            + KEY_APP_VERSION + " TEXT,"
            + KEY_IS_PINVERIFIED + " TEXT,"
            + KEY_STAN + " TEXT,"
            + KEY_CARD_ISSUER + " TEXT,"
            + KEY_CARD_EMI_AMOUNT + " TEXT,"
            + KEY_TOTAL_PAYAMOUNT + " TEXT,"
            + KEY_NO_EMI + " TEXT,"
            + KEY_INTEREST_RATE + " TEXT,"
            + KEY_BILL_NO + " TEXT,"
            + KEY_FIRST_DIGIT_OF_CARD + " TEXT,"
            + KEY_IS_INCONV + " TEXT,"
            + KEY_INVOICE + " TEXT,"
            + KEY_TRX_DATE + " TEXT,"
            + KEY_TRX_IMG_DATE + " TEXT,"
            + KEY_CHEQUE_DATE + " TEXT,"
            + KEY_CHEQUE_NO + " TEXT"
            + ")";

    public long saveTransaction(Payment payment) {
        long status = 0;
        dbFNB = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(KEY_USER_ID,user.getUserId());
        contentValues.put(KEY_MERCHANT_NAME, payment.getMerchantName());
        contentValues.put(KEY_MERCHANT_ADDRESS, payment.getMerchantAdd());
        contentValues.put(KEY_DATE_TIME, payment.getDateTime());
        contentValues.put(KEY_M_ID, payment.getmId());
        contentValues.put(KEY_T_ID, payment.gettId());
        contentValues.put(KEY_BATCH_NO, payment.getBatchNo());
        contentValues.put(KEY_VOUCHER_NO, payment.getVoucherNo());
        contentValues.put(KEY_REFERENCE_NO, payment.getRefNo());
        contentValues.put(KEY_SALES_TYPE, payment.getSaleType());
        contentValues.put(KEY_CARD_NO, payment.getCardNo());
        contentValues.put(KEY_TRX_TYPE, payment.getTrxType());
        contentValues.put(KEY_CARD_TYPE, payment.getCardType());
        contentValues.put(KEY_EXP_DATE, payment.getExpDate());
        contentValues.put(KEY_EMVSIGN_EXPDATE, payment.getEmvSigExpDate());
        contentValues.put(KEY_CARDHOLDER_NAME, payment.getCardHolderName());
        contentValues.put(KEY_CURRENCY, payment.getCurrency());
        contentValues.put(KEY_CASH_AMOUNT, payment.getCashAmount());
        contentValues.put(KEY_BASE_AMOUNT, payment.getBaseAmount());
        contentValues.put(KEY_TIP_AMOUNT, payment.getTipAmount());
        contentValues.put(KEY_TOTAL_AMOUNT, payment.getTotalAmount());
        contentValues.put(KEY_AUTH_CODE, payment.getAuthCode());
        contentValues.put(KEY_RRNO, payment.getRrNo());
        contentValues.put(KEY_CERTIFI, payment.getCertif());
        contentValues.put(KEY_APP_ID, payment.getAppId());
        contentValues.put(KEY_APP_NAME, payment.getAppName());
        contentValues.put(KEY_TVR, payment.getTvr());
        contentValues.put(KEY_TSI, payment.getTsi());
        contentValues.put(KEY_APP_VERSION, payment.getAppVersion());
        contentValues.put(KEY_IS_PINVERIFIED, payment.getIsPinVarifed());
        contentValues.put(KEY_STAN, payment.getStan());
        contentValues.put(KEY_CARD_ISSUER, payment.getCardIssuer());
        contentValues.put(KEY_CARD_EMI_AMOUNT, payment.getEmiPerMonthAmount());
        contentValues.put(KEY_TOTAL_PAYAMOUNT, payment.getTotal_Pay_Amount());
        contentValues.put(KEY_NO_EMI, payment.getNoOfEmi());
        contentValues.put(KEY_INTEREST_RATE, payment.getInterestRate());
        contentValues.put(KEY_BILL_NO, payment.getBillNo());
        contentValues.put(KEY_FIRST_DIGIT_OF_CARD, payment.getFirstDigitsOfCard());
        contentValues.put(KEY_IS_INCONV, payment.getIsConvenceFeeEnable());
        contentValues.put(KEY_INVOICE, payment.getInvoiceNo());
        contentValues.put(KEY_TRX_DATE, payment.getTrxDate());

        contentValues.put(KEY_TRX_IMG_DATE, payment.getTrxImgDate());
        contentValues.put(KEY_CHEQUE_DATE, payment.getChequeDate());
        contentValues.put(KEY_CHEQUE_NO, payment.getChequeNo());

        try {
            status = dbFNB.insert(TBL_TRANSACTIONS, null, contentValues);

        } catch (Exception e) {
            status = 0;
            e.printStackTrace();
            //Log.d(TAG,e.toString());
        }
        return status;
    }


    String QUERY_CREATE_TABLE_USER_ROLE = "CREATE TABLE " + TBL_USERSROLE + "("
            + KEY_ROLE_ID + " INTEGER PRIMARY KEY,"
            + KEY_ROLE_NAME + " TEXT" + ")";
    String QUERY_CREATE_TABLE_ROLE_ACCESS = "CREATE TABLE " + TBL_USERROLEACCESS + "("
            + KEY_ACCESS_CODE + " INTEGER,"
            + KEY_ACCESS_NAME + " TEXT,"
            + KEY_ROLE_ID + " TEXT )";

    String QUERY_CREATE_TABLE_USERS = "CREATE TABLE " + TBL_USERS + "("
            + KEY_USER_ID + " INTEGER PRIMARY KEY,"
            + KEY_USER_NAME + " TEXT,"
            + KEY_USER_MOBILE + " TEXT,"
            + KEY_USER_DESIGNATION + " TEXT,"
            + KEY_ROLE_ID + " INTEGER,"
            + KEY_USER_LOGIN + " TEXT,"
            + KEY_USER_PASS + " TEXT,"
            + KEY_USER_ADHAR + " TEXT,"
            + KEY_USER_EMAIL + " TEXT,"
            + KEY_USER_ADDRESS + " TEXT,"
            + KEY_USER_FILE_LOCATION + " TEXT" + ")";


    // USERS_new end

// gst

    static final String TBL_INWARD_SUPPLY_LEDGER = "InwardSupplyLedger";
    static final String TBL_INWARD_SUPPLY_ITEMS_DETAILS = "InwardSupplyItemsDetails";
    static final String TBL_GSTR2_AMEND = "InwardSupplyLedgerAmend";
    static final String TBL_Supplier = "SupplierDetails";
    static final String TBL_OUTWARD_SUPPLY_LEDGER = "OutwardSupplyLedger";
    static final String TBL_GSTR1_AMEND = "OutwardSupplyLedgerAmend";
    static final String TBL_OUTWARD_SUPPLY_ITEMS_DETAILS = "OutwardSuppyItemsDetails";
    static final String TBL_CreditDebit_Outward = "CreditDebitOutward";
    static final String TBL_CreditDebit_Inward = "CreditDebitInward";

    static final String TBL_USER_GSTIN = "USERS_GSTIN";
    static final String TBL_USER_COMPOUNDING = "USERS_COMPOUNDING";
    static final String TBL_USERS_UNREGISTERED = "USERS_Unregistered";
    static final String TBL_READ_FROM_2A = "ReadFrom2A";
    //static final String TBL_READ_FROM_1A = "ReadFrom1A";
    static final String TBL_ITEM_Outward = "Item_Outward";
    static final String TBL_ITEM_Inward = "Item_Inward";
    static final String TBL_OWNER_DETAILS = "OwnerDetails";

    private static final String TBL_BILLDETAIL = "OutwardSuppyItemsDetails";
    private static final String TBL_BILLITEM = "OutwardSupplyLedger";
    private static final String TBL_StockOutward = "StockOutward";
    private static final String TBL_StockInward = "StockInward";
    public static final String KEY_OpeningStock = "OpeningStock";
    public static final String KEY_ClosingStock = "ClosingStock";
    public static final String KEY_ClosingStockValue = "ClosingStockValue";

    public static final String KEY_GSTIN = "GSTIN";
    public static final String KEY_GSTIN_Ori = "GSTIN_Ori";
    public static final String KEY_CustName = "CustName";
    public static final String KEY_InvoiceNo = "InvoiceNo";
    public static final String KEY_CustStateCode = "CustStateCode";
    public static final String KEY_InvoiceDate = "InvoiceDate";
    public static final String KEY_Value = "Value";
    public static final String KEY_HSNCode = "HSNCode";
    public static final String KEY_SUPPLIERNAME = "SupplierName";
    public static final String KEY_SupplierPhone = "SupplierPhone";
    public static final String KEY_SupplierCount = "SupplierCount";
    public static final String KEY_Count = "Count";
    public static final String KEY_AverageRate = "AverageRate";
    public static final String KEY_SupplierAddress = "SupplierAddress";
    public static final String KEY_SupplierCode = "SupplierCode";
    public static final String KEY_TaxableValue = "TaxableValue";
    public static final String KEY_IGSTAmount = "IGSTAmount";
    public static final String KEY_SGSTAmount = "SGSTAmount";
    public static final String KEY_CGSTAmount = "CGSTAmount";
    public static final String KEY_POS = "POS";
    public static final String KEY_DeviceId = "DeviceId";
    public static final String KEY_DeviceName = "DeviceName";
    public static final String KEY_BillingMode = "BillingMode"; // richa_2012
    public static final String KEY_MONTH = "Month";
    public static final String KEY_SupplyType_REV = "RevisedSupplyType";
    public static final String KEY_NoteType = "NoteType";
    public static final String KEY_NoteNo = "NoteNo";
    public static final String KEY_NoteDate = " NoteDate";
    public static final String KEY_DifferentialValue = "DifferentialValue";
    public static final String KEY_MONTH_ITC_IGSTAMT = "MonthITC_IGSTAmount";
    public static final String KEY_MONTH_ITC_CGSTAMT = "MonthITC_CGSTAmount";
    public static final String KEY_MONTH_ITC_SGSTAMT = "MonthITC_SGSTAmount";
    public static final String KEY_HSNCode_REV = "ReviseHSNCode";
    public static final String KEY_POS_REV = " RevisedPOS";
    public static final String KEY_SupplyType = "SupplyType";
    public static final String KEY_TaxationType = "TaxationType";
    public static final String KEY_Description = "Description";
    public static final String KEY_Price = "Price";
    public static final String KEY_UOM = "UOM";
    public static final String KEY_DiscountRate = "DiscountRate";
    public static final String KEY_IGSTRate = "IGSTRate";
    public static final String KEY_CGSTRate = "CGSTRate";
    public static final String KEY_SGSTRate = "SGSTRate";
    public static final String KEY_cessRate = "cessRate";
    public static final String KEY_cessAmount = "cessAmount";
    public static final String KEY_ITC_Eligible = "ITC_Eligible";
    public static final String KEY_Total_ITC_IGST = "Total_ITC_IGST";
    public static final String KEY_Total_ITC_CGST = "Total_ITC_CGST";
    public static final String KEY_Total_ITC_SGST = "Total_ITC_SGST";
    public static final String KEY_SupplierType = "SupplierType";
    public static final String KEY_SupplierPOS = "SupplierPOS";
    public static final String KEY_AttractsReverseCharge = "AttractsReverseCharge";
    public static final String KEY_AdditionalChargeName = "AdditionalChargeName";
    public static final String KEY_AdditionalChargeAmount = "AdditionalChargeAmount";
    public static final String KEY_isGoodinward = "isGoodinward";
    public static final String KEY_GrandTotal = "GrandTotal";
    public static final String KEY_SubTotal = "SubTotal";
    public static final String KEY_OriginalInvoiceNo = "OriginalInvoiceNo";
    public static final String KEY_OriginalInvoiceDate = "OriginalInvoiceDate";
    public static final String KEY_BusinessType = "BusinessType";
    public static final String KEY_ReverseCharge = "ReverseCharge";
    public static final String KEY_ProvisionalAssess = "ProvisionalAssess";
    public static final String KEY_LineNumber = "LineNumber";
    public static final String KEY_Ecom_GSTIN = "EcommerceGSTIN";
    public static final String KEY_FIRM_NAME = "FirmName";
    public static final String KEY_Address = "Address";
    public static final String KEY_TINCIN = "TINCIN";
    public static final String KEY_PhoneNo = "Phone";
    public static final String KEY_Owner_Name = "OwnerName";
    public static final String KEY_IsMainOffice = "IsMainOffice";
    public static final String KEY_PurchaseOrderNo = "PurchaseOrderNo";

    public static final String KEY_GSTIN_OUT = "GSTIN_Out";
    public static final String KEY_GSTEnable = "GSTEnable";
    public static final String KEY_POS_OUT = "POS_Out";
    public static final String KEY_HSNCode_OUT = "HSNCode_Out";
    public static final String KEY_ReverseCharge_OUT = "ReverseCharge_Out";


    String QUERY_CREATE_TABLE_OWNER_DETAILS = "CREATE TABLE " + TBL_OWNER_DETAILS + " ( " +
            KEY_GSTIN + " TEXT, " + KEY_Owner_Name + "  TEXT, " + KEY_FIRM_NAME + " TEXT, " + KEY_PhoneNo + " TEXT, " +
            KEY_POS +" TEXT,"+ KEY_DeviceId+" Text, "+KEY_DeviceName+" Text, "+KEY_USER_EMAIL+" TEXT, "+
            KEY_REFERENCE_NO+" TEXT, "+ KEY_BillNoPrefix +" TEXT, "+
            KEY_Address + " TEXT, " + KEY_TINCIN + " TEXT, " + KEY_IsMainOffice + "  TEXT ) ";

    String QUERY_CREATE_TABLE_Stock_Outward = "CREATE TABLE " + TBL_StockOutward + " ( " +
            KEY_BusinessDate +" TEXT, "+KEY_MenuCode + " INTEGER, " + KEY_ItemName + "  TEXT, " + KEY_OpeningStock + " REAL, " +
            KEY_ClosingStock + " REAL, " + KEY_Rate + " REAL ) ";

    String QUERY_CREATE_TABLE_Stock_Inward = "CREATE TABLE " + TBL_StockInward + " ( " +
            KEY_BusinessDate +" TEXT, "+KEY_MenuCode + " INTEGER, " + KEY_ItemName + "  TEXT, " + KEY_OpeningStock + " REAL, " +
            KEY_ClosingStock + " REAL, " + KEY_Rate + " REAL ) ";

    String QUERY_CREATE_TABLE_ITEM_Inward = "CREATE TABLE " + TBL_ITEM_Inward + "( " + KEY_MenuCode +
            " INTEGER PRIMARY KEY, " + KEY_TaxationType + " TEXT, " +
            KEY_SupplyType + " TEXT, " + KEY_SupplierCode + " INTEGER, " + KEY_SUPPLIERNAME + " TEXT, " +
            KEY_HSNCode + " TEXT, " + KEY_ItemName + " TEXT, " + KEY_IGSTRate + " REAL, " + KEY_IGSTAmount + " REAL, " +
            KEY_CGSTRate + " REAL," + KEY_CGSTAmount + " REAL," + KEY_SGSTRate + " REAL, " + KEY_SGSTAmount + " REAL,"+
            KEY_cessRate + " REAL, " + KEY_cessAmount + " REAL," +
            KEY_ImageUri + " TEXT, " + KEY_Quantity + " REAL, " + KEY_UOM + " TEXT, " + KEY_ItemBarcode + " TEXT, "+
            KEY_Rate + " REAL, "  +
            KEY_DiscId + " NUMERIC, " + KEY_DiscountEnable + " NUMERIC, "  +
            KEY_Count+" INTEGER, "+ KEY_AverageRate+" REAL, "+KEY_TaxType + " NUMERIC )";

    String QUERY_CREATE_TABLE_ITEM_Outward = "CREATE TABLE " + TBL_ITEM_Outward + "( "
            + KEY_TaxType + " NUMERIC, "
            + KEY_SupplyType + " TEXT, "
            + KEY_TaxationType + " TEXT, "
            + KEY_HSNCode + " TEXT, "
            + KEY_ItemName + " TEXT, "
            + KEY_Quantity + " REAL, "
            + KEY_UOM + " TEXT,"
            + KEY_Rate + " REAL, "
            + KEY_IGSTRate + " REAL, "
            + KEY_CGSTRate + " REAL,"
            + KEY_SGSTRate + " REAL, "
            + KEY_cessRate + " REAL, "
            + KEY_ImageUri + " TEXT, "
            + KEY_AdditionalTaxId + " NUMERIC, "
            + KEY_BillWithStock + " NUMERIC,"
            + KEY_CategCode + " NUMERIC, "
            + KEY_DeliveryPrice + " REAL, "
            + KEY_DeptCode + " NUMERIC, "
            + KEY_DineInPrice1 + " REAL, "
            + KEY_DineInPrice2 + " REAL, "
            + KEY_DineInPrice3 + " REAL, "
            + KEY_DiscId + " NUMERIC, "
            + KEY_DiscountEnable + " NUMERIC, "
            + KEY_DiscountPercent + " REAL, "
            + KEY_ItemBarcode + " TEXT, "
            + KEY_KitchenCode + " NUMERIC, "
            + KEY_MenuCode + " INTEGER, "
            + KEY_OptionalTaxId1 + " NUMERIC, "
            + KEY_OptionalTaxId2 + " NUMERIC, "
            + KEY_PickUpPrice + " REAL, "
            + KEY_PriceChange + " NUMERIC, "
            + KEY_SalesTaxId + " NUMERIC, "
            + KEY_TakeAwayPrice + " REAL, "
            + KEY_SalesTaxPercent + " REAL, "
            + KEY_SerTaxPercent + " REAL, "
            + KEY_ItemId + " INTEGER PRIMARY KEY" +
            ")";

    String QUERY_CREATE_TABLE_READ_2A = " CREATE TABLE  " + TBL_READ_FROM_2A + "(  " +
            KEY_GSTIN + " TEXT, " +
            KEY_SUPPLIERNAME + " TEXT, " +
            KEY_InvoiceNo + " TEXT, " +
            KEY_InvoiceDate + " TEXT, " +
            KEY_Value + " REAL, " +
            KEY_SupplyType + " TEXT, " +
            KEY_HSNCode + " TEXT, " +
            KEY_ReverseCharge + " TEXT, " +
            KEY_ProvisionalAssess + " TEXT, " +
            KEY_LineNumber + " INTEGER, " +
            KEY_TaxableValue + " REAL, " +
            KEY_IGSTRate + " REAL," +
            KEY_IGSTAmount + " REAL, " +
            KEY_CGSTRate + " REAL," +
            KEY_CGSTAmount + " REAL," +
            KEY_SGSTRate + " REAL," +
            KEY_SGSTAmount + " REAL, " +
            KEY_POS + " TEXT " +
            ")";


    static final String TBL_READ_FROM_1A = "ReadFrom1A";
    static final String TBL_READ_FROM_1 = "ReadFrom1";

    private String KEY_ReturnPeriod = "ReturnPeriod";
    private String KEY_ttl_inv = "ttl_inv";
    private String KEY_ttl_tax = "ttl_tax";
    private String KEY_ttl_igst = "ttl_igst";
    private String KEY_ttl_sgst = "ttl_sgst";
    private String KEY_ttl_cgst = "ttl_cgst";
    private String KEY_ss_ttl_inv = "ss_ttl_inv";
    private String KEY_ss_ttl_tax = "ss_ttl_tax";
    private String KEY_ss_ttl_igst = "ss_ttl_igst";
    private String KEY_ss_ttl_sgst = "ss_ttl_sgst";
    private String KEY_ss_ttl_cgst = "ss_ttl_cgst";
    private String KEY_cs_ttl_inv = "cs_ttl_inv";
    private String KEY_cs_ttl_tax = "cs_ttl_tax";
    private String KEY_cs_ttl_igst = "cs_ttl_igst";
    private String KEY_cs_ttl_sgst = "cs_ttl_sgst";
    private String KEY_cs_ttl_cgst = "cs_ttl_cgst";
    private String KEY_sec_nm = "sec_nm";
    private String KEY_ctin = "ctin";

    String QUERY_CREATE_TABLE_READ_1A = "CREATE TABLE " + TBL_READ_FROM_1A + " (" +
            KEY_GSTIN + " TEXT," +
            KEY_ReturnPeriod + " TEXT," +
            KEY_ttl_inv + " REAL," +
            KEY_ttl_tax + " REAL," +
            KEY_ttl_igst + " REAL," +
            KEY_ttl_sgst + " REAL," +
            KEY_ttl_cgst + " REAL," +
            KEY_ss_ttl_inv + " REAL," +
            KEY_ss_ttl_tax + " REAL," +
            KEY_ss_ttl_igst + " REAL," +
            KEY_ss_ttl_sgst + " REAL," +
            KEY_ss_ttl_cgst + " REAL," +
            KEY_cs_ttl_inv + " REAL," +
            KEY_cs_ttl_tax + " REAL," +
            KEY_cs_ttl_igst + " REAL," +
            KEY_cs_ttl_sgst + " REAL," +
            KEY_cs_ttl_cgst + " REAL," +
            KEY_sec_nm + " TEXT," +
            KEY_ctin + " TEXT" +
            ")";
    String QUERY_CREATE_TABLE_READ_1 = "CREATE TABLE " + TBL_READ_FROM_1 + " (" +
            KEY_GSTIN + " TEXT," +
            KEY_ReturnPeriod + " TEXT," +
            KEY_ttl_inv + " REAL," +
            KEY_ttl_tax + " REAL," +
            KEY_ttl_igst + " REAL," +
            KEY_ttl_sgst + " REAL," +
            KEY_ttl_cgst + " REAL," +
            KEY_ss_ttl_inv + " REAL," +
            KEY_ss_ttl_tax + " REAL," +
            KEY_ss_ttl_igst + " REAL," +
            KEY_ss_ttl_sgst + " REAL," +
            KEY_ss_ttl_cgst + " REAL," +
            KEY_cs_ttl_inv + " REAL," +
            KEY_cs_ttl_tax + " REAL," +
            KEY_cs_ttl_igst + " REAL," +
            KEY_cs_ttl_sgst + " REAL," +
            KEY_cs_ttl_cgst + " REAL," +
            KEY_sec_nm + " TEXT," +
            KEY_ctin + " TEXT" +
            ")";


    // richa_2012
    String QUERY_CREATE_TABLE_Outward_Supply_Ledger = "CREATE TABLE " + TBL_OUTWARD_SUPPLY_LEDGER +
            "( " + KEY_GSTIN + " TEXT, " + KEY_CustName + " TEXT, " + KEY_CustStateCode + " TEXT, " + KEY_InvoiceNo + " TEXT, " +
            KEY_InvoiceDate + " TEXT, " + KEY_SupplyType + " TEXT, " +
            KEY_BusinessType + " TEXT, " + KEY_TaxationType + " TEXT, " + KEY_HSNCode + " TEXT, "
            + KEY_ItemNumber + " NUMERIC, " + KEY_ItemName + " TEXT, " + KEY_Quantity + " REAL, " + KEY_UOM + " TEXT, " +
            KEY_Value + " REAL, " + KEY_TaxableValue + " REAL, " + KEY_IGSTRate + " REAL," +
            KEY_IGSTAmount + " REAL," + KEY_CGSTRate + " REAL," + KEY_CGSTAmount + " REAL, " + KEY_SGSTRate + " REAL," +
            KEY_SGSTAmount + " REAL," + KEY_cessRate + " REAL," + KEY_cessAmount + " REAL," +
            KEY_SubTotal + " REAl, " + KEY_BillingMode + " TEXT, " + KEY_ServiceTaxAmount + " REAL, " +
            KEY_ServiceTaxPercent + " REAL," + KEY_ModifierAmount + " REAL, " + KEY_TaxType + " NUMERIC, " +
            KEY_KitchenCode + " NUMERIC, " + KEY_CategCode + " NUMERIC, " + KEY_DeptCode + " NUMERIC, " +
            KEY_DiscountPercent + " REAL, " + KEY_DiscountAmount + " REAL, " + KEY_TaxAmount + " REAL, " + KEY_BillStatus+" INTEGER, "+
            KEY_TaxPercent + " REAL)";

    String QUERY_CREATE_TABLE_Outward_Supply_Items_Details = " CREATE TABLE " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + "( "
            + KEY_GSTIN + " TEXT, "
            + KEY_CustName + " TEXT, "
            + KEY_CustStateCode + " Text, "
            + KEY_InvoiceNo + " INTEGER, "
            + KEY_InvoiceDate + " TEXT, "
            + KEY_Time + " TEXT, "
            + KEY_POS + " TEXT, "
            + KEY_TotalItems + " NUMERIC,"
            + KEY_TaxableValue + " TEXT, "
            + KEY_SubTotal + " REAl, "
            + KEY_BillStatus + " NUMERIC,"
           // + KEY_IGSTRate + " REAL,"
            + KEY_IGSTAmount + " REAL,"
           // + KEY_CGSTRate + " REAL,"
            + KEY_CGSTAmount + " REAL, "
           // + KEY_SGSTRate + " REAL,"
            + KEY_SGSTAmount + " REAL,"
            + KEY_cessAmount + " REAL,"
            + KEY_GrandTotal + " REAL, "
            + KEY_ReverseCharge + " TEXT, "
            + KEY_BusinessType + " TEXT,"
            + KEY_ProvisionalAssess + " TEXT, "
            + KEY_Ecom_GSTIN + " TEXT, "
            + KEY_CardPayment + " REAL, "
            + KEY_CashPayment + " REAL, "
            + KEY_BillAmount + " REAL, "
            + KEY_BillingMode + " TEXT, "
            + KEY_CouponPayment + " REAL, "
            + KEY_WalletPayment + " REAL, "
            + KEY_CustId + " NUMERIC, "
            + KEY_DeliveryCharge + " REAL, "
            + KEY_TotalServiceTaxAmount + " REAL, "
            + KEY_EmployeeId + " NUMERIC,"
            + KEY_ReprintCount + " NUMERIC, "
            + KEY_TotalDiscountAmount + " REAL,"
            + KEY_DiscPercentage + " REAL,"
            + KEY_TotalTaxAmount + " REAL, "
            + KEY_UserId + " NUMERIC, "
            + KEY_PettyCashPayment + " REAL, "
            + KEY_PaidTotalPayment + " REAL, "
            + KEY_ChangePayment + " REAL, "
            + KEY_TableNo +" TEXT, "
            + KEY_Table_Split_No+" TEXT "
            +")";

    String QUERY_CREATE_TABLE_OUTWARD_SUPPLY_AMMEND = " CREATE TABLE " + TBL_GSTR1_AMEND + " (" +
            KEY_GSTIN + "  TEXT, " + KEY_CustName + " TEXT, " + KEY_CustStateCode+" TEXT, "+
            KEY_MONTH + " TEXT, " + KEY_SupplyType + " TEXT, " +
            KEY_HSNCode + " TEXT, " + KEY_POS + "  TEXT, " + KEY_SupplyType_REV + " TEXT, " +
            KEY_HSNCode_REV + " TEXT, " + KEY_POS_REV + "  TEXT, " + KEY_TaxableValue + " REAL, " +
            KEY_CGSTRate + " REAL," + KEY_CGSTAmount + " REAL," + KEY_SGSTRate + " REAL, " + KEY_SGSTAmount + " REAL, " +
            KEY_IGSTRate + " REAL, " +KEY_IGSTAmount + " REAL , " + KEY_cessRate + " REAL," + KEY_cessAmount + " REAL," +
            KEY_ProvisionalAssess + "  TEXT, " + KEY_BusinessType + " TEXT,  " +
            KEY_OriginalInvoiceNo + " TEXT , " +
            KEY_OriginalInvoiceDate + " TEXT , " + KEY_InvoiceNo + " TEXT , " + KEY_InvoiceDate + " TEXT , " +
            KEY_TaxationType + "  TEXT, " + KEY_ItemName + " TEXT, " +
            KEY_Quantity + " NUMERIC, " + KEY_UOM + " TEXT, " + KEY_Value + " REAL, " +
            KEY_DiscountPercent + "  REAL, " +
            KEY_GrandTotal + " REAL," + KEY_ReverseCharge + "  TEXT, " +
            KEY_Ecom_GSTIN + "  TEXT )";

    String QUERY_CREATE_TABLE_CREDITDEBIT_OUTWARD = " CREATE TABLE " + TBL_CreditDebit_Outward + " (" +
            KEY_GSTIN + "  TEXT, " + KEY_CustName + " TEXT, " + KEY_NoteType + " TEXT, " + KEY_NoteNo + " TEXT, " +
            KEY_NoteDate + " TEXT, " + KEY_InvoiceNo + " TEXT , " + KEY_InvoiceDate + " TEXT , " +
            KEY_AttractsReverseCharge + " TEXT, " + KEY_Reason+" TEXT, "+ KEY_DifferentialValue + " TEXT, " + KEY_CGSTRate + " REAL," +
            KEY_CGSTAmount + " REAL," + KEY_SGSTRate + " REAL, " + KEY_SGSTAmount + " REAL, " +KEY_cessRate + " REAL, " +
            KEY_cessAmount + " REAL, " + KEY_IGSTRate + " REAL, " +KEY_IGSTAmount + " REAL )";

    String QUERY_CREATE_TABLE_CREDITDEBIT_Inward = " CREATE TABLE " + TBL_CreditDebit_Inward + " (" +
            KEY_GSTIN + "  TEXT, " + KEY_CustName + " TEXT, " + KEY_NoteType + " TEXT, " + KEY_NoteNo + " TEXT, " +
            KEY_NoteDate + " TEXT, " + KEY_InvoiceNo + " TEXT , " + KEY_InvoiceDate + " TEXT , " +
            KEY_AttractsReverseCharge + " TEXT, " + KEY_Reason+" TEXT, "+
            KEY_DifferentialValue + " TEXT, " + KEY_CGSTRate + " REAL," + KEY_CGSTAmount + " REAL," + KEY_SGSTRate + " REAL, " +
            KEY_SGSTAmount + " REAL, " + KEY_IGSTRate + " REAL, " + KEY_IGSTAmount + " REAL " + KEY_ITC_Eligible + " TEXT, " +
            KEY_Total_ITC_IGST + " TEXT, " + KEY_Total_ITC_CGST + " TEXT, " + KEY_Total_ITC_SGST + " TEXT, " +
            KEY_MONTH_ITC_IGSTAMT + " TEXT, " + KEY_MONTH_ITC_CGSTAMT + " TEXT, " + KEY_MONTH_ITC_SGSTAMT + " TEXT ) ";

    String QUERY_CREATE_TABLE_Supplier = " CREATE TABLE " + TBL_Supplier + " ( " +
            KEY_SupplierCode + " INTEGER PRIMARY KEY, " + KEY_GSTIN + " TEXT," + KEY_SUPPLIERNAME + " TEXT, " +
            KEY_SupplierType + " TEXT, " + KEY_SupplierPhone + "  TEXT, " + KEY_SupplierAddress + " TEXT)";


    String QUERY_CREATE_TABLE_GSTR2_AMMEND = " CREATE TABLE " + TBL_GSTR2_AMEND + " ( " +
            KEY_GSTIN_Ori + " TEXT," + KEY_CustName + " TEXT, " + KEY_SupplierType + " TEXT, " + KEY_SupplyType + "  TEXT, " +
            KEY_TaxationType + " TEXT, " + KEY_OriginalInvoiceNo + " TEXT, " + KEY_OriginalInvoiceDate + " TEXT, " +
            KEY_GSTIN+" TEXT, "+
            KEY_InvoiceNo + " TEXT, " + KEY_InvoiceDate + " TEXT, " + KEY_HSNCode + " TEXT, " + KEY_ItemName + " TEXT, " +
            KEY_Quantity + " NUMERIC, " + KEY_UOM + " TEXT, " + KEY_Value + " REAL, " +
            KEY_DiscountPercent + " NUMERIC, " + KEY_TaxableValue + " REAL, " + KEY_IGSTRate + " REAL, " + KEY_IGSTAmount + " REAL, " +
            KEY_CGSTRate + " REAL , " + KEY_CGSTAmount + " REAL, " + KEY_SGSTRate + " REAL, " + KEY_SGSTAmount + " REAL, " +KEY_cessAmount + " REAL, " +
            KEY_POS + " TEXT, " + KEY_ITC_Eligible + " TEXT, " + KEY_Total_ITC_IGST + " REAL, " + KEY_Total_ITC_CGST + " REAL, " +
            KEY_Total_ITC_SGST + " TEXT, " +
            KEY_MONTH_ITC_IGSTAMT + " TEXT, " + KEY_MONTH_ITC_CGSTAMT + " TEXT, " + KEY_MONTH_ITC_SGSTAMT + " TEXT )";

    String QUERY_CREATE_TABLE_INWARD_SUPPLY_LEDGER = " CREATE TABLE " + TBL_INWARD_SUPPLY_LEDGER + " ( "
            + KEY_SupplierCode + " INTEGER, " + KEY_GSTIN + " TEXT, " + KEY_SupplierPhone + " TEXT, " + KEY_SupplierType + " TEXT, " + KEY_SUPPLIERNAME + " TEXT, " +
            KEY_InvoiceNo + " TEXT , " + KEY_InvoiceDate + " TEXT, " +
            KEY_SupplyType + " TEXT , " + KEY_TaxationType + " TEXT , " + KEY_HSNCode + " TEXT , " + KEY_MenuCode + " INTEGER, " +
            KEY_ItemName + " TEXT, " + KEY_Quantity + " NUMERIC, " + KEY_UOM + "  TEXT, " +
            KEY_Value + " REAL, " + KEY_DiscountPercent + " REAL, " + KEY_TaxableValue + " REAL, " + KEY_SalesTax + " REAL," + KEY_ServiceTaxAmount
            + " REAl, " +
            KEY_IGSTRate + " REAL, " + KEY_IGSTAmount + " REAL, " + KEY_CGSTRate + " REAL, " + KEY_CGSTAmount + " REAL, " +
            KEY_SGSTRate + " REAL, " + KEY_SGSTAmount + " REAL," + KEY_SubTotal + " TEXT, " + KEY_ITC_Eligible + " TEXT, " +
            KEY_Total_ITC_IGST + " REAL, " + KEY_Total_ITC_CGST + " REAL, " + KEY_Total_ITC_SGST + " REAL )";


    String QUERY_CREATE_TABLE_INWARD_SUPPLY_ITEM_DETAILS = "CREATE TABLE " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + " ( " +
            KEY_SupplierCode + " INTEGER, " + KEY_GSTIN + " TEXT, " + KEY_SUPPLIERNAME + " TEXT, " + KEY_SupplierType + " TEXT, " + KEY_SupplierPhone + " TEXT, " + KEY_BusinessType + " TEXT, "
            + KEY_AttractsReverseCharge + " TEXT, " +
            KEY_InvoiceNo + " TEXT, " + KEY_InvoiceDate + " TEXT, " + KEY_TaxationType + " TEXT, " +
            KEY_Value + " REAL, " + KEY_DiscountPercent + " REAL, " + KEY_TaxableValue + " REAL, " + KEY_CGSTRate + " REAL, " +
            KEY_CGSTAmount + " REAL, " + KEY_SGSTRate + " REAL, " + KEY_SGSTAmount + " REAL, " + KEY_IGSTRate + " REAL, " +
            KEY_IGSTAmount + " REAL, " + KEY_AdditionalChargeName + " TEXT, " + KEY_AdditionalChargeAmount + " REAL, " +
            KEY_SubTotal + " REAL, " + KEY_GrandTotal + " REAL, " + KEY_TotalItems + " TEXT, " + KEY_POS + " TEXT, " + KEY_ITC_Eligible + " TEXT, " + KEY_Total_ITC_IGST + " REAL, " +
            KEY_Total_ITC_CGST + " REAL, " + KEY_Total_ITC_SGST + " REAL ) ";


    // Creating queries for creating tables in database


    String QUERY_CREATE_TABLE_PURCHASE_ORDER = " CREATE TABLE " + TBL_PURCHASEORDER + " ( " +
            KEY_PurchaseOrderNo + " INTEGER, " + KEY_InvoiceNo + " TEXT, " + KEY_InvoiceDate + " TEXT, " +
            KEY_SupplierCode + " INTEGER, " + KEY_SUPPLIERNAME + " TEXT, " + KEY_SupplierPhone + " TEXT, " +
            KEY_SupplierAddress + " TEXT, " + KEY_GSTIN+" TEXT, "+KEY_SupplierType+" TEXT, "+
            KEY_SupplierPOS+" TEXT, "+
            KEY_MenuCode + " INTEGER, " + KEY_SupplyType + " TEXT , " + KEY_HSNCode+" TEXT, "+
            KEY_ItemName + " TEXT, " + KEY_Value + " REAL, " + KEY_Quantity + " REAL, " + KEY_UOM + "  TEXT, " + KEY_TaxableValue + " REAL, " +
            KEY_IGSTRate + " REAL," + KEY_IGSTAmount + " REAl, "+KEY_CGSTRate + " REAL," + KEY_CGSTAmount + " REAl, "+
            KEY_SGSTRate + " REAL," + KEY_SGSTAmount + " REAl, "+ KEY_cessRate + " REAL," + KEY_cessAmount + " REAl, "+
            KEY_SalesTax + " REAL," + KEY_ServiceTaxAmount + " REAl, " + KEY_Amount + " REAL," + KEY_AdditionalChargeName + " TEXT, " +
            KEY_AdditionalChargeAmount + " REAL , " + KEY_isGoodinward + " INTEGER )";

    String QUERY_CREATE_TABLE_GOODS_INWARD = " CREATE TABLE " + TBL_GOODSINWARD + " ( " +
            KEY_MenuCode + " INTEGER PRIMARY KEY,  " + KEY_SupplyType + " TEXT , " + KEY_ItemName + " TEXT, " + KEY_Value + " REAL, " +
            KEY_Quantity + " REAL, " + KEY_UOM + "  TEXT, "+KEY_SupplierCount+" INTEGER )";

    String QUERY_CREATE_TABLE_INGREDIENTS = " CREATE TABLE " + TBL_INGREDIENTS + " ( " +
            KEY_MenuCode + " INTEGER, " + KEY_ItemName + " TEXT, " + KEY_Quantity + " REAL, " + KEY_UOM + "  TEXT, " +
            KEY_IngredientCode + " INTEGER, " + KEY_IngredientName + " TEXT, " + KEY_IngredientQuantity + " REAL, " +
            KEY_IngredientUOM + " STRING, " + KEY_Status + " TEXT )";

    String QUERY_CREATE_TABLE_SUPPLIERITEMLINKAGE = " CREATE TABLE " + TBL_SupplierItemLinkage + " ( " +
            KEY_MenuCode + " INTEGER, " + KEY_SupplierCode + " INTEGER ) ";


    String QUERY_CREATE_TABLE_BILLSETTING = "CREATE TABLE " + TBL_BILLSETTING + " ( "
            + KEY_DineIn3Caption + " TEXT, "
            + KEY_DineIn2Caption + " TEXT, "
            + KEY_DineIn1Caption + " TEXT, "
            + KEY_WeighScale + " NUMERIC, "
            + KEY_ServiceTaxPercent + " REAL, "
            + KEY_ServiceTaxType + " NUMERIC, "
            + KEY_BusinessDate + " TEXT, "
            + KEY_DineIn1From + " NUMERIC, "
            + KEY_DineIn1To + " NUMERIC,"
            + KEY_DineIn2From + " NUMERIC,"
            + KEY_DineIn2To + " NUMERIC, "
            + KEY_DineIn3From + " NUMERIC, "
            + KEY_DineIn3To + " NUMERIC, "
            + KEY_FooterText + " TEXT, "
            + KEY_HeaderText + " TEXT, "
            + KEY_KOTType + " NUMERIC, "
            + KEY_MaximumTables + " NUMERIC, "
            + KEY_MaximumWaiters + " NUMERIC, "
            + KEY_POSNumber + " NUMERIC, "
            + KEY_PrintKOT + " NUMERIC, "
            + KEY_SubUdfText + " TEXT, "
            + KEY_TIN + " TEXT, "
            + KEY_ActiveForBilling + " NUMERIC, "
            + KEY_LoginWith + " NUMERIC, "
            + KEY_DateAndTime + " NUMERIC, "
            + KEY_PriceChange + " NUMERIC, "
            + KEY_BillwithStock + " NUMERIC, "
            + KEY_BillwithoutStock + " NUMERIC, "
            + KEY_Tax + " NUMERIC, "
            + KEY_TaxType + " NUMERIC, "
            + KEY_DiscountType + " NUMERIC, "
            + KEY_FastBillingMode + " NUMERIC, "
            + KEY_KOT + " NUMERIC, "
            + KEY_Token + " NUMERIC, "
            + KEY_Kitchen + " NUMERIC, "
            + KEY_OtherChargesItemwise + " NUMERIC, "
            + KEY_OtherChargesBillwise + " NUMERIC, "
            + KEY_Peripherals + " NUMERIC, "
            + KEY_RestoreDefault + " NUMERIC, "
            + KEY_DineInRate + " NUMERIC, "
            + KEY_CounterSalesRate + " NUMERIC, "
            + KEY_PickUpRate + " NUMERIC, "
            + KEY_HomeDeliveryRate + " NUMERIC ,"
            + KEY_HomeDineInCaption + " TEXT, "
            + KEY_HomeCounterSalesCaption + " TEXT, "
            + KEY_HomeTakeAwayCaption + " TEXT, "
            + KEY_HomeHomeDeliveryCaption + " TEXT, "
            + KEY_CummulativeHeadingEnable + " NUMERIC, " // richa_2012
            + KEY_GSTIN + " TEXT," + KEY_GSTIN_OUT + " TEXT, " +
            KEY_POS + " TEXT, " + KEY_POS_OUT + " TEXT, " +
            KEY_HSNCode + " TEXT, " + KEY_HSNCode_OUT + " TEXT, " +
            KEY_ReverseCharge + " TEXT, " + KEY_ReverseCharge_OUT + " TEXT, " + KEY_GSTEnable + " TEXT, " +
            KEY_ItemNoReset + " NUMERIC , " + KEY_PrintPreview + " NUMERIC , " + KEY_TableSpliting + " NUMERIC )";

    String QUERY_CREATE_TABLE_BILLNO_RESET = "CREATE TABLE " + TBL_BILLNORESETCONFIG + "( " + KEY_BillNoReset_InvoiceNo +
            " NUMERIC, " + KEY_BillNoReset_Period + " TEXT," + KEY_BillNoReset_PeriodDate + " TEXT, KOTNo NUMERIC)";

    String QUERY_CREATE_TABLE_CATEGORY = "CREATE TABLE " + TBL_CATEGORY + "( " + KEY_CategCode +
            " INTEGER, " + KEY_CategName + " TEXT," + KEY_DeptCode + " INTEGER)";

    String QUERY_CREATE_TABLE_COMPLIMENTARY_BILL_DETAIL = "CREATE TABLE " + TBL_COMPLIMENTARYBILLDETAIL + "( " + KEY_InvoiceNo +
            " NUMERIC, " + KEY_Complimentary_Reason + " TEXT," + KEY_Paid_Amount + " REAL)";

    String QUERY_CREATE_TABLE_COUPON = "CREATE TABLE " + TBL_COUPON + " (" + KEY_CouponAmount + " REAL, " +
            KEY_CouponBarcode + " TEXT, " + KEY_CouponDescription + " TEXT," + KEY_CouponId + " INTEGER PRIMARY KEY)";

    String QUERY_CREATE_TABLE_CUSTOMER = "CREATE TABLE " + TBL_CUSTOMER + " (" + KEY_CustAddress + " TEXT, " +
            KEY_CustContactNumber + " NUMERIC, " + KEY_CustId + " INTEGER PRIMARY KEY, " + KEY_CustName + " TEXT, " +
            KEY_LastTransaction + " REAL, " + KEY_TotalTransaction + " REAL, " + KEY_CreditAmount +
            " REAL, " + KEY_GSTIN + " TEXT)";

    String QUERY_CREATE_TABLE_DELETEDKOT = "CREATE TABLE " + TBL_DELETEDKOT + "(" + KEY_Reason + " TEXT, " +
            KEY_EmployeeId + " NUMERIC," + KEY_SubUdfNumber + " NUMERIC," + KEY_TableNumber + " NUMERIC," +
            KEY_Time + " TEXT, " + KEY_TokenNumber + " NUMERIC, " + KEY_Table_Split_No + " NUMERIC)";

    String QUERY_CREATE_TABLE_DEPARTMENT = "CREATE TABLE " + TBL_DEPARTMENT + "( " + KEY_DeptCode +
            " INTEGER PRIMARY KEY," + KEY_DeptName + " TEXT)";

    String QUERY_CREATE_TABLE_DECRIPTION = "CREATE TABLE " + TBL_DESCRIPTION + " ( " + KEY_DescriptionId +
            " INTEGER PRIMARY KEY, " + KEY_DescriptionText + " TEXT)";

    String QUERY_CREATE_TABLE_DISCOUNTCONFIG = "CREATE TABLE " + TBL_DISCOUNTCONFIG + "( " +
            KEY_DiscDescription + " TEXT, " + KEY_DiscId + " INTEGER PRIMARY KEY, " + KEY_DiscPercentage + " REAL, " + KEY_DiscAmount + " REAL)";

    String QUERY_CREATE_TABLE_EMPLOYEE = "CREATE TABLE " + TBL_EMPLOYEE + " ( " +
            KEY_EmployeeContactNumber + " NUMERIC, " + KEY_EmployeeId + " INTEGER PRIMARY KEY, " +
            KEY_EmployeeName + " TEXT, " + KEY_EmployeeRole + " NUMERIC )";


    String QUERY_CREATE_TABLE_KOTMODIFIER = "CREATE TABLE " + TBL_KOTMODIFIER + " (" + KEY_IsChargeable +
            " NUMERIC, " + KEY_ModifierAmount + " REAL, " + KEY_ModifierDescription + " TEXT, " + KEY_ModifierId +
            " INTEGER PRIMARY KEY, " + KEY_ModifierModes + " TEXT)";

    String QUERY_CREATE_TABLE_KITCHEN = "CREATE TABLE " + TBL_KITCHEN + "(" + KEY_KitchenCode +
            " INTEGER PRIMARY KEY," + KEY_KitchenName + " TEXT)";

    String QUERY_CREATE_TABLE_MACHINESETTING = "CREATE TABLE " + TBL_MACHINESETTING + " ( " + KEY_BaudRate +
            " NUMERIC, " + KEY_DataBits + " NUMERIC, " + KEY_Parity + " NUMERIC, " + KEY_PortName + " TEXT, " +
            KEY_StopBits + " NUMERIC)";

    String QUERY_CREATE_TABLE_PAYMENTRECEIPT = "CREATE TABLE " + TBL_PAYMENTRECEIPT + "(" + KEY_Reason +
            " TEXT, " + KEY_Id + " INTEGER PRIMARY KEY, " + KEY_Amount + " REAL, " + KEY_BillType + " NUMERIC, " +
            KEY_DescriptionText + " TEXT, " +
            KEY_InvoiceDate + " TEXT, " + KEY_DescriptionId1 + " NUMERIC, " + KEY_DescriptionId2 + " NUMERIC, " +
            KEY_DescriptionId3 + " NUMERIC)";

    String QUERY_CREATE_TABLE_PENDINGKOT = "CREATE TABLE " + TBL_PENDINGKOT + "( "
            + KEY_IsCheckedOut + " NUMERIC, "
            + KEY_OrderMode + " NUMERIC, "
            + KEY_ServiceTaxAmount + " REAL, "
            + KEY_ServiceTaxPercent + " REAL, "
            + KEY_IGSTRate + " REAL, "
            + KEY_IGSTAmount + " REAL, "
            + KEY_cessRate + " REAL, "
            + KEY_cessAmount + " REAL, "
            + KEY_ModifierAmount + " REAL, "
            + KEY_TaxType + " NUMERIC, "
            + KEY_Amount + " REAL, " +
            KEY_CategCode + " NUMERIC, "
            + KEY_CustId + " NUMERIC,"
            + KEY_DeptCode + " NUMERIC, "
            + KEY_DiscountAmount + " REAL,"
            + KEY_DiscountPercent + " REAL, "
            + KEY_EmployeeId + " NUMERIC," +
            KEY_ItemName + " TEXT,"
            + KEY_HSNCode + " TEXT, "
            + KEY_ItemNumber + " NUMERIC,"
            + KEY_KitchenCode + " NUMERIC," +
            KEY_Quantity + " REAL, "
            + KEY_Rate + " REAL, "
            + KEY_SubUdfNumber + " NUMERIC,"
            + KEY_TableNumber + " NUMERIC,"
            + KEY_TaxAmount + " REAL, "
            + KEY_TaxPercent + " REAL, "
            + KEY_Time + " TEXT, "
            + KEY_TokenNumber + " NUMERIC, "
            + KEY_Table_Split_No + " NUMERIC, "
            + KEY_SupplyType + " TEXT, "
            + KEY_POS + " TEXT, "
            + KEY_UOM + " TEXT, " +
            " PrintKOTStatus NUMERIC)";

    String QUERY_CREATE_TABLE_RIDERSETTLEMENT = "CREATE TABLE " + TBL_RIDERSETTLEMENT + " (" + KEY_DeliveryCharge +
            " REAL, " + KEY_BillAmount + " REAL, " + KEY_InvoiceNo + " NUMERIC, " + KEY_EmployeeId + " NUMERIC," +
            KEY_PettyCash + " REAL, " + KEY_SettledAmount + " REAL, " + KEY_TotalItems + " NUMERIC, " + KEY_CustId + " NUMERIC)";


    String QUERY_CREATE_TABLE_TABLEBOOKING = "CREATE TABLE " + TBL_TABLEBOOKING + " ( " + KEY_TBookId +
            " INTEGER PRIMARY KEY, " + KEY_CustomerName + " TEXT, " + KEY_TimeForBooking + " TEXT," + KEY_TableNo + " NUMERIC, " +
            KEY_MobileNo + " NUMERIC)";


    String QUERY_CREATE_TABLE_TAXCONFIG = "CREATE TABLE " + TBL_TAXCONFIG + " ( " + KEY_TaxDescription +
            " TEXT, " + KEY_TaxId + " INTEGER PRIMARY KEY, " + KEY_TaxPercentage + " REAL, " + KEY_TotalPercentage + " REAL )";

    String QUERY_CREATE_TABLE_TAXCONFIG_SUB = "CREATE TABLE " + TBL_SUBTAXCONFIG + " ( " + KEY_SubTaxId + " INTEGER PRIMARY KEY, "
            + KEY_SubTaxDescription + " TEXT, " + KEY_SubTaxPercentage + " REAL, " + KEY_TaxId + " INTEGER)";

    String QUERY_CREATE_TABLE_USER = "CREATE TABLE " + TBL_USER + " ( " + KEY_AccessLevel + " NUMERIC, " +
            KEY_Password + " TEXT," + KEY_UserId + " TEXT, " + KEY_UserName + " TEXT)";


    String QUERY_CREATE_TABLE_VOUCHERCONFIG = "CREATE TABLE " + TBL_VOUCHERCONFIG + " ( " + KEY_VoucherId +
            " INTEGER PRIMARY KEY , " + KEY_VoucherDescription + " TEXT, " + KEY_VoucherPercentage + " REAL)";

    String QUERY_CREATE_TABLE_MAILCONFIGURATION = "CREATE TABLE " + TBL_MAILSETTING + "( " +
            KEY_FromMailId + " TEXT, " + KEY_FromMailPassword + " TEXT, " + KEY_SmtpServer + " TEXT, " + KEY_PortNo + " TEXT, " + KEY_FromDate +
            " TEXT, " + KEY_ToDate + " TEXT, " + KEY_SendMail + " NUMERIC, " + KEY_AutoMail + " NUMERIC )";

    String QUERY_CREATE_TABLE_REPORTSMASTER = "CREATE TABLE " + TBL_REPORTSMASTER + " ( " + KEY_ReportsId + " INTEGER PRIMARY KEY ," +
            KEY_ReportsName + " TEXT, " + KEY_ReportsType + " INTEGER, " + KEY_Status + " INTEGER )";


    // SQLite Database and Context
    private SQLiteDatabase dbFNB;
    Context myContext;
    ContentValues cvDbValues = new ContentValues();

    public DatabaseHandler(Context context) {
        super(context, DB_PATH + DB_NAME, null, DB_VERSION);
        this.myContext = context;
        // Change Database and Database backup path if memory card is not
        // present
        if (Environment.getExternalStorageDirectory().exists() == false) {
            DB_PATH = Environment.getRootDirectory().getPath() + "/WeP_FnB/";
            DB_BACKUP_PATH = Environment.getRootDirectory().getPath() + "/WeP_FnB_Backup/";
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(QUERY_CREATE_TABLE_USER);
            //db.execSQL(QUERY_CREATE_TABLE_BILLDETAILS);
            //db.execSQL(QUERY_CREATE_TABLE_BILLITEM);
            db.execSQL(QUERY_CREATE_TABLE_BILLSETTING);
            db.execSQL(QUERY_CREATE_TABLE_BILLNO_RESET);
            db.execSQL(QUERY_CREATE_TABLE_CATEGORY);
            db.execSQL(QUERY_CREATE_TABLE_COMPLIMENTARY_BILL_DETAIL);
            db.execSQL(QUERY_CREATE_TABLE_COUPON);
            db.execSQL(QUERY_CREATE_TABLE_DELETEDKOT);
            db.execSQL(QUERY_CREATE_TABLE_DEPARTMENT);
            db.execSQL(QUERY_CREATE_TABLE_DECRIPTION);
            db.execSQL(QUERY_CREATE_TABLE_DISCOUNTCONFIG);
            db.execSQL(QUERY_CREATE_TABLE_EMPLOYEE);
            //db.execSQL(QUERY_CREATE_TABLE_ITEM);
            db.execSQL(QUERY_CREATE_TABLE_KOTMODIFIER);
            db.execSQL(QUERY_CREATE_TABLE_MACHINESETTING);
            db.execSQL(QUERY_CREATE_TABLE_PAYMENTRECEIPT);
            db.execSQL(QUERY_CREATE_TABLE_PENDINGKOT);
            db.execSQL(QUERY_CREATE_TABLE_RIDERSETTLEMENT);
            db.execSQL(QUERY_CREATE_TABLE_TABLEBOOKING);
            db.execSQL(QUERY_CREATE_TABLE_TAXCONFIG);
            db.execSQL(QUERY_CREATE_TABLE_TAXCONFIG_SUB);
            db.execSQL(QUERY_CREATE_TABLE_VOUCHERCONFIG);
            db.execSQL(QUERY_CREATE_TABLE_KITCHEN);
            db.execSQL(QUERY_CREATE_TABLE_MAILCONFIGURATION);
            db.execSQL(QUERY_CREATE_TABLE_REPORTSMASTER);
            db.execSQL(QUERY_CREATE_TABLE_CUSTOMER);
            db.execSQL(QUERY_CREATE_TABLE_USERS);
            db.execSQL(QUERY_CREATE_TABLE_ROLE_ACCESS);
            db.execSQL(QUERY_CREATE_TABLE_USER_ROLE);
            db.execSQL(QUERY_CREATE_TABLE_INWARD_SUPPLY_LEDGER);
            db.execSQL(QUERY_CREATE_TABLE_INWARD_SUPPLY_ITEM_DETAILS);
            db.execSQL(QUERY_CREATE_TABLE_GSTR2_AMMEND);
            db.execSQL(QUERY_CREATE_TABLE_Outward_Supply_Ledger);
            db.execSQL(QUERY_CREATE_TABLE_Outward_Supply_Items_Details);
            db.execSQL(QUERY_CREATE_TABLE_OUTWARD_SUPPLY_AMMEND);
            db.execSQL(QUERY_CREATE_TABLE_Supplier);
            db.execSQL(QUERY_CREATE_TABLE_CREDITDEBIT_OUTWARD);
            db.execSQL(QUERY_CREATE_TABLE_CREDITDEBIT_Inward);
            db.execSQL(QUERY_CREATE_TABLE_OWNER_DETAILS);
            db.execSQL(QUERY_CREATE_TABLE_READ_2A);
            db.execSQL(QUERY_CREATE_TABLE_READ_1A);
            db.execSQL(QUERY_CREATE_TABLE_ITEM_Inward);
            db.execSQL(QUERY_CREATE_TABLE_ITEM_Outward);
            db.execSQL(QUERY_CREATE_TABLE_PURCHASE_ORDER);
            db.execSQL(QUERY_CREATE_TABLE_GOODS_INWARD);
            db.execSQL(QUERY_CREATE_TABLE_INGREDIENTS);
            db.execSQL(QUERY_CREATE_TABLE_SUPPLIERITEMLINKAGE);
            db.execSQL(QUERY_CREATE_TBL_TRANSACTIONS);
            db.execSQL(QUERY_CREATE_TABLE_Stock_Outward);
            db.execSQL(QUERY_CREATE_TABLE_Stock_Inward);
            setDefaultTableValues(db);
        } catch (Exception ex) {
            Toast.makeText(myContext, "OnCreate : " + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE IF EXISTS " + TBL_BILLDETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_Supplier);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BILLSETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_BILLNORESETCONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CATEGORY);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COMPLIMENTARYBILLDETAIL);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_COUPON);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CUSTOMER);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_DELETEDKOT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_DEPARTMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_DESCRIPTION);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_DISCOUNTCONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_VOUCHERCONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_EMPLOYEE);
        //db.execSQL("DROP TABLE IF EXISTS " + TBL_ITEM_Outward);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_KITCHEN);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_KOTMODIFIER);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MACHINESETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_MAILSETTING);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PAYMENTRECEIPT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_PENDINGKOT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_RIDERSETTLEMENT);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_TAXCONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_SUBTAXCONFIG);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_TABLEBOOKING);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_REPORTSMASTER);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_USERSROLE);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_USERROLEACCESS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_INWARD_SUPPLY_ITEMS_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_INWARD_SUPPLY_LEDGER);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_GSTR2_AMEND);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_OUTWARD_SUPPLY_LEDGER);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_GSTR1_AMEND);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_READ_FROM_2A);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_READ_FROM_1A);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ITEM_Inward);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_ITEM_Outward);
        //db.execSQL("DROP TABLE IF EXISTS " + TBL_OWNER_DETAILS);
        db.execSQL("DROP TABLE IF EXISTS " + TBL_CreditDebit_Inward);
        db.execSQL("DROP TABLE IF EXIXTS " + TBL_CreditDebit_Outward);
        db.execSQL("DROP TABLE IF EXIXTS " + TBL_PURCHASEORDER);
        //db.execSQL("DROP TABLE IF EXIXTS " + TBL_GOODSINWARD);
        db.execSQL("DROP TABLE IF EXIXTS " + TBL_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXIXTS " + TBL_TRANSACTIONS);
        //db.execSQL("DROP TABLE IF EXIXTS " + TBL_StockOutward);
        //db.execSQL("DROP TABLE IF EXIXTS " + TBL_StockInward);
        onCreate(db);
    }

    public void setDefaultTableValues(SQLiteDatabase db) {//user HARDCODING

        ContentValues cnDbValues;
        cvDbValues = new ContentValues();
        cvDbValues.put("AccessLevel", "1");
        cvDbValues.put("Password", "admin");
        cvDbValues.put("UserId", "admin");
        cvDbValues.put("UserName", "administrator");
        long l = db.insert(TBL_USER, null, cvDbValues);
        if (l == -1) {
        }


        /*cvDbValues = new ContentValues();
        //cvDbValues.put(KEY_GSTIN, "G12345678901234");
        cvDbValues.put(KEY_GSTIN, "04AABFN9870CMZT");
        cvDbValues.put(KEY_POS, "29");
        cvDbValues.put(KEY_Owner_Name, "Anuj Sharma");
        cvDbValues.put(KEY_DeviceId, "MACID_00");
        cvDbValues.put(KEY_DeviceName, "TAB2200+");
        cvDbValues.put(KEY_USER_EMAIL, "abc@xyz.com");
        cvDbValues.put(KEY_FIRM_NAME, "Sharma & Sons");
        cvDbValues.put(KEY_PhoneNo, "1234567890");
        cvDbValues.put(KEY_Address, "Bangalore");
        cvDbValues.put(KEY_TINCIN, "1234567890");
        cvDbValues.put(KEY_IsMainOffice, "YES");
        try {
            l = db.insert(TBL_OWNER_DETAILS, null, cvDbValues);

        } catch (Exception e) {
            l = 0;
            //Log.d(TAG, e.toString());
            e.printStackTrace();
        }*/


        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_USER_NAME, "admin");
        cvDbValues.put(KEY_USER_MOBILE, "1234567890");
        cvDbValues.put(KEY_USER_DESIGNATION, "Owner");
        cvDbValues.put(KEY_ROLE_ID, 1);
        cvDbValues.put(KEY_USER_LOGIN, "admin");
        cvDbValues.put(KEY_USER_PASS, "admin");
        cvDbValues.put(KEY_USER_ADHAR, "Adhaar1");
        cvDbValues.put(KEY_USER_EMAIL, "wep@india.com");
        cvDbValues.put(KEY_USER_ADDRESS, "lavelle road");
        cvDbValues.put(KEY_USER_FILE_LOCATION, "xx");
        long status = 0;
        try {
            status = db.insert(TBL_USERS, null, cvDbValues);

        } catch (Exception e) {
            status = 0;
            Log.d(TAG, e.toString());
            e.printStackTrace();
        }

        try {
            long status1 = 0;

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "Manager");
            cvDbValues.put(KEY_ACCESS_CODE, "0");
            cvDbValues.put(KEY_ACCESS_NAME, "Operator");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "Manager");
            cvDbValues.put(KEY_ACCESS_CODE, "1");
            cvDbValues.put(KEY_ACCESS_NAME, "Masters");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "Manager");
            cvDbValues.put(KEY_ACCESS_CODE, "2");
            cvDbValues.put(KEY_ACCESS_NAME, "Payment & Receipt");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "Manager");
            cvDbValues.put(KEY_ACCESS_CODE, "3");
            cvDbValues.put(KEY_ACCESS_NAME, "Reports");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_ROLE_ID, "Manager");
            cvDbValues.put(KEY_ACCESS_CODE, "4");
            cvDbValues.put(KEY_ACCESS_NAME, "PurchaseOrder");
            status1 = db.insert(TBL_USERROLEACCESS, null, cvDbValues);

        } catch (Exception e) {
            status = 0;
            Log.d(TAG, e.toString());
        }

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ROLE_NAME, "Manager");
        db.insert(TBL_USERSROLE, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ROLE_NAME, "HeadCook");
        db.insert(TBL_USERSROLE, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ROLE_NAME, "Waiter");
        db.insert(TBL_USERSROLE, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ROLE_NAME, "Rider");
        db.insert(TBL_USERSROLE, null, cvDbValues);


        cvDbValues = new ContentValues();
        cvDbValues.put("DineIn3Caption", "Rate 3");
        cvDbValues.put("DineIn2Caption", "Rate 2");
        cvDbValues.put("DineIn1Caption", "Rate 1");
        cvDbValues.put("WeighScale", 0);
        cvDbValues.put("ServiceTaxPercent", 5.0);
        cvDbValues.put("ServiceTaxType", 2);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
        cvDbValues.put("BusinessDate", timeStamp);
        cvDbValues.put("DineIn1From", 1);
        cvDbValues.put("DineIn1To", 10);
        cvDbValues.put("DineIn2From", 2);
        cvDbValues.put("DineIn2To", 20);
        cvDbValues.put("DineIn3From", 3);
        cvDbValues.put("DineIn3To", 30);
        cvDbValues.put("FooterText", " Thankyou ");
        cvDbValues.put("HeaderText", "Restaurant");
        //cvDbValues.put("HeaderText", "Restaurant|St Marks Road|Bangalore|");
        cvDbValues.put("KOTType", 1);
        cvDbValues.put("MaximumTables", 12);
        cvDbValues.put("MaximumWaiters", 4);
        cvDbValues.put("POSNumber", 29);
        cvDbValues.put("PrintKOT", 1);
        cvDbValues.put("SubUdftext", "Seat No.");
        cvDbValues.put("TIN", "123456789");
        cvDbValues.put("ActiveForBilling", 0);
        cvDbValues.put("LoginWith", 2);
        cvDbValues.put("DateAndTime", 1);
        cvDbValues.put("PriceChange", 0);
        cvDbValues.put("BillwithStock", 0);
        cvDbValues.put("BillwithoutStock", 0);
        cvDbValues.put("Tax", 1);
        cvDbValues.put("TaxType", 1);
        cvDbValues.put(KEY_DiscountType, 0); // 1 itemwise, 0 billwise
        cvDbValues.put("KOT", 1);
        cvDbValues.put("Token", 0);
        cvDbValues.put("Kitchen", 1);
        cvDbValues.put("OtherChargesItemwise", 0);
        cvDbValues.put("OtherChargesBillwise", 0);
        cvDbValues.put("Peripherals", 1);
        cvDbValues.put("RestoreDefault", 0);
        cvDbValues.put("DineInRate", 1);
        cvDbValues.put("CounterSalesRate", 1);
        cvDbValues.put("PickUpRate", 1);
        cvDbValues.put("HomeDeliveryRate", 1);
        cvDbValues.put(KEY_HomeDineInCaption, "Dine In");
        cvDbValues.put(KEY_HomeCounterSalesCaption, "Counter Sales");
        cvDbValues.put(KEY_HomeTakeAwayCaption, "Take Away");
        cvDbValues.put(KEY_HomeHomeDeliveryCaption, "Home Delivery");
        cvDbValues.put(KEY_CummulativeHeadingEnable, 1); // richa_2012
        cvDbValues.put(KEY_GSTIN, 0);
        cvDbValues.put(KEY_POS, 0);
        cvDbValues.put(KEY_HSNCode, 1);
        cvDbValues.put(KEY_ReverseCharge, 0);
        cvDbValues.put(KEY_GSTIN_OUT, 0);
        cvDbValues.put(KEY_POS_OUT, 1);
        cvDbValues.put(KEY_HSNCode_OUT, 1);
        cvDbValues.put(KEY_ReverseCharge_OUT, 0);
        cvDbValues.put(KEY_GSTEnable, 0);
        cvDbValues.put(KEY_FastBillingMode, 1);
        cvDbValues.put(KEY_ItemNoReset, 0);
        cvDbValues.put(KEY_PrintPreview, 0);
        cvDbValues.put(KEY_TableSpliting, 0);

        status = 0;
        try {
            status = db.insert(TBL_BILLSETTING, null, cvDbValues);
        } catch (Exception e) {
            status = 0;

            Log.d(TAG, e.toString());
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        // Bill No Reset Configuration
        cvDbValues = new ContentValues();
        cvDbValues.put("InvoiceNo", 0);
        cvDbValues.put("Period", "Disable");
        cvDbValues.put("PeriodDate", "");
        cvDbValues.put("KOTNo", 0);
        db.insert(TBL_BILLNORESETCONFIG, null, cvDbValues);

        // ReportsMaster
        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Bill wise Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Transaction Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Tax Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Service Tax Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Void Bill Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Duplicate Bill Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "KOT Pending Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "KOT Deleted Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Item wise Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Day wise Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Month wise Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Department wise Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Category wise Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Kitchen wise Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Waiter wise Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Waiter Detailed Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Rider wise Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Rider Detailed Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "User wise Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "User Detailed Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Customer wise Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Customer Detailed Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Payments Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Receipts Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Fast Selling Itemwise Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);


        //gst reports

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-B2B");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-B2BA");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-B2CS");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-B2CSA");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-B2CL");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-B2CLA");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR2-Registered");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR2_Registered_Amend");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR2-UnRegistered");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR2_UnRegistered_Amend");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR2A");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-CDN");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR2-2A Validation");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-1A Validation");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);


        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Supplier Wise Report");
        cvDbValues.put("ReportsType", 3);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Cummulative Payment-Reciept-Sales Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        // richa_2012
        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Cummulative Billing Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Outward Stock Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Inward Stock Report");
        cvDbValues.put("ReportsType", 2);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);



        // Tax Config
        cvDbValues = new ContentValues();
        cvDbValues.put("TaxDescription", "Sales");
        cvDbValues.put("TaxId", 1);
        cvDbValues.put("TaxPercentage", 5);
        cvDbValues.put("TotalPercentage", 5);
        db.insert(TBL_TAXCONFIG, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("TaxDescription", "Service");
        cvDbValues.put("TaxId", 2);
        cvDbValues.put("TaxPercentage", 10);
        cvDbValues.put("TotalPercentage", 10);
        db.insert(TBL_TAXCONFIG, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-HSN Summary");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR1-Documents Issued");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "GSTR4-Composite Report");
        cvDbValues.put("ReportsType", 4);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);

        /*cvDbValues = new ContentValues();
        cvDbValues.put("ReportsName", "Bill wise Report");
        cvDbValues.put("ReportsType", 1);
        cvDbValues.put("Status", 0);
        db.insert(TBL_REPORTSMASTER, null, cvDbValues);*/
    }


    // richa - gst functions

    public long addDebit(GSTR1_CDN_Details note, String name, String reason, String reverseCharge) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GSTIN, name);
        contentValues.put(KEY_CustName, name);
        contentValues.put(KEY_Reason, reason);
        contentValues.put(KEY_AttractsReverseCharge, reverseCharge);
        contentValues.put(KEY_InvoiceNo, note.getInum());
        contentValues.put(KEY_InvoiceDate, note.getIdt());
        contentValues.put(KEY_NoteType, note.getNtty());
        contentValues.put(KEY_NoteNo, note.getNt_num());
        contentValues.put(KEY_NoteDate, note.getNt_dt());
        contentValues.put(KEY_DifferentialValue, note.getVal());
        contentValues.put(KEY_IGSTRate, note.getIrt());
        contentValues.put(KEY_IGSTAmount, note.getIamt());
        contentValues.put(KEY_SGSTRate, note.getSrt());
        contentValues.put(KEY_SGSTAmount, note.getSamt());
        contentValues.put(KEY_CGSTRate, note.getCrt());
        contentValues.put(KEY_CGSTAmount, note.getSamt());

        long result = dbFNB.insert(TBL_CreditDebit_Inward, null, contentValues);
        return result;
    }
    public long editDebit(GSTR1_CDN_Details note, String reason) {

        String whereClause = KEY_InvoiceNo+" LIKE '"+note.getInum()+"' AND "+KEY_InvoiceDate+" LIKE '"+note.getIdt()+"' AND "
                +KEY_NoteNo+" LIKE '"+note.getNt_num()+"' AND "+KEY_NoteDate+" LIKE '"+note.getNt_dt()+"'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_Reason, reason);
        contentValues.put(KEY_DifferentialValue, note.getVal());
        contentValues.put(KEY_IGSTRate, note.getIrt());
        contentValues.put(KEY_IGSTAmount, note.getIamt());
        contentValues.put(KEY_SGSTRate, note.getSrt());
        contentValues.put(KEY_SGSTAmount, note.getSamt());
        contentValues.put(KEY_CGSTRate, note.getCrt());
        contentValues.put(KEY_CGSTAmount, note.getSamt());

        long result = dbFNB.update(TBL_CreditDebit_Inward, contentValues,whereClause,null);
        return result;
    }
    public long addCredit(GSTR1_CDN_Details note, String name, String reason, String reverseCharge) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GSTIN, name);
        contentValues.put(KEY_CustName, name);
        contentValues.put(KEY_Reason, reason);
        contentValues.put(KEY_AttractsReverseCharge, reverseCharge);
        contentValues.put(KEY_InvoiceNo, note.getInum());
        contentValues.put(KEY_InvoiceDate, note.getIdt());
        contentValues.put(KEY_NoteType, note.getNtty());
        contentValues.put(KEY_NoteNo, note.getNt_num());
        contentValues.put(KEY_NoteDate, note.getNt_dt());
        contentValues.put(KEY_DifferentialValue, note.getVal());
        contentValues.put(KEY_IGSTRate, note.getIrt());
        contentValues.put(KEY_IGSTAmount, note.getIamt());

        contentValues.put(KEY_SGSTRate, note.getSrt());
        contentValues.put(KEY_SGSTAmount, note.getSamt());
        contentValues.put(KEY_CGSTRate, note.getCrt());
        contentValues.put(KEY_CGSTAmount, note.getCamt());
        contentValues.put(KEY_cessAmount, note.getCsamt());
        contentValues.put(KEY_cessRate, note.getCsrt());

        long result = dbFNB.insert(TBL_CreditDebit_Outward, null, contentValues);
        return result;
    }

    public long editCredit(GSTR1_CDN_Details note, String reason) {

        String whereClause = KEY_InvoiceNo+" LIKE '"+note.getInum()+"' AND "+KEY_InvoiceDate+" LIKE '"+note.getIdt()+"' AND "
                +KEY_NoteNo+" LIKE '"+note.getNt_num()+"' AND "+KEY_NoteDate+" LIKE '"+note.getNt_dt()+"'";
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_Reason, reason);
        contentValues.put(KEY_DifferentialValue, note.getVal());
        contentValues.put(KEY_IGSTRate, note.getIrt());
        contentValues.put(KEY_IGSTAmount, note.getIamt());
        contentValues.put(KEY_SGSTRate, note.getSrt());
        contentValues.put(KEY_SGSTAmount, note.getSamt());
        contentValues.put(KEY_CGSTRate, note.getCrt());
        contentValues.put(KEY_CGSTAmount, note.getSamt());
        contentValues.put(KEY_cessRate, note.getCsrt());
        contentValues.put(KEY_cessAmount, note.getCsamt());

        long result = dbFNB.update(TBL_CreditDebit_Outward, contentValues,whereClause,null);
        return result;
    }

    public Cursor getCreditDetails(String invoiceNo, String invoiceDate,String type,String custgstin) {
        String selectQuery = "SELECT * FROM " + TBL_CreditDebit_Outward + " WHERE " + KEY_InvoiceDate + " LIKE '" + invoiceDate +
                "' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+KEY_NoteType+" LIKE '"+type+"' AND "+
                KEY_CustName+" LIKE '"+custgstin+"'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getDebitDetails(String invoiceNo, String invoiceDate,String type,String supplier_gstin) {
        String selectQuery = "SELECT * FROM " + TBL_CreditDebit_Inward + " WHERE " + KEY_InvoiceDate + " LIKE '" + invoiceDate +
                "' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+KEY_NoteType+" LIKE '"+type+"' AND "+KEY_GSTIN+" LIKE '"+supplier_gstin+"'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public int DeleteOutwardNote(String invoiceNo, String invoiceDate,String creditNo, String creditDate, String noteType) {

        String deleteQuery = KEY_InvoiceDate + " LIKE '" + invoiceDate +"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+
                "' AND "+KEY_NoteNo+" LIKE '"+creditNo+"' AND "+KEY_NoteDate+" LIKE '"+creditDate+"' AND "+KEY_NoteType+" LIKE '"+noteType+"'";
        return dbFNB.delete(TBL_CreditDebit_Outward, deleteQuery, null);
    }
    public int DeleteInwardNote(String invoiceNo, String invoiceDate, String creditNo, String creditDate, String noteType) {

        String deleteQuery = KEY_InvoiceDate + " LIKE '" + invoiceDate +"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+
                "' AND "+KEY_NoteNo+" LIKE '"+creditNo+"' AND "+KEY_NoteDate+" LIKE '"+creditDate+"' AND "+KEY_NoteType+" LIKE '"+noteType+"'";
        return dbFNB.delete(TBL_CreditDebit_Inward, deleteQuery, null);
    }

    public int getCreditNoteNo(String invoiceNo, String invoiceDate) {
        int max = 1;
        String whereClause = "SELECT NoteNo FROM "+TBL_CreditDebit_Outward+" WHERE "+KEY_InvoiceNo+" LIKE '"+
                invoiceNo+"' AND "+KEY_InvoiceDate+" LIKE '"+invoiceDate+"'";
        Cursor cursor = dbFNB.rawQuery(whereClause, null);
        if(cursor.moveToNext())
            max = cursor.getInt(cursor.getColumnIndex("NoteNo")) ;
        else
        {
            cursor = dbFNB.rawQuery("SELECT max(NoteNo) as NoteNo FROM "+TBL_CreditDebit_Outward, null);
            while(cursor.moveToNext())
                max = cursor.getInt(cursor.getColumnIndex("NoteNo")) +1 ;
        }
        return max;
    }
    public int getMaxCreditNoteNo() {
        int max = 0;
        Cursor cursor = dbFNB.rawQuery("SELECT NoteNo FROM "+TBL_CreditDebit_Outward, null);
        while(cursor.moveToNext())
            max = cursor.getInt(cursor.getColumnIndex("NoteNo")) ;
        return max+1;
    }
    public int getMaxDebitNoteNo() {
        int max = 0;
        Cursor cursor = dbFNB.rawQuery("SELECT NoteNo FROM "+TBL_CreditDebit_Inward, null);
        while(cursor.moveToNext())
            max = cursor.getInt(cursor.getColumnIndex("NoteNo")) ;
        return max+1;
    }

    public Cursor getdebitdetails(String invoiceNo, String invoiceDate,String supplierGSTIN)
    {

        String selectQuery = "SELECT * FROM " + TBL_CreditDebit_Inward+ " WHERE " + KEY_InvoiceDate + " LIKE '" + invoiceDate +
                "' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+KEY_GSTIN+" LIKE '"+supplierGSTIN+"'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getInward_taxed(String StartDate, String EndDate) {
        String selectQuery = "SELECT * FROM " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getInward_taxed_reconcile(String StartDate, String EndDate) {
        String selectQuery = "SELECT * FROM " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "' AND (" + KEY_SupplierType + " LIKE 'Registered' OR ( " + KEY_SupplierType + " LIKE 'UnRegistered' AND " + KEY_AttractsReverseCharge
                + " LIKE 'yes'))";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getitems_inward_taxed(String No, String Date, String gstin, String supplierName, String supplierType) {
        String selectQuery = "SELECT * FROM " + TBL_INWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceNo + " LIKE '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND " + KEY_GSTIN + " LIKE '" + gstin + "' AND " + KEY_SUPPLIERNAME + " LIKE '" +
                supplierName + "' AND " + KEY_SupplierType + " LIKE '" + supplierType + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getitems_inward_untaxed(String No, String Date, String supplierName, String supplierType) {
        String selectQuery = "SELECT * FROM " + TBL_INWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceNo + " LIKE '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND " + KEY_SUPPLIERNAME + " LIKE '" +
                supplierName + "' AND " + KEY_SupplierType + " LIKE '" + supplierType + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }


    public Cursor getInward_taxed_amend() {
        String b2b = "B2B";
        String selectQuery = "SELECT * FROM " + TBL_GSTR2_AMEND;
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getOutwardB2b(String StartDate, String EndDate) {
        String b2b = "B2B";

        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "' AND " + KEY_BusinessType + " LIKE 'B2B' AND BillStatus =1"+
                " order by GSTIN asc";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getitems_b2b(String No, String Date, String cust_GSTIN) {
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceNo + " Like '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND "/*+KEY_GSTIN+" LIKE '"+cust_GSTIN+"' AND "+*/+
                KEY_BusinessType+" LIKE 'B2B'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getitems_outward_details(String Startdate, String Enddate) {
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + Startdate + "' AND '" +Enddate+"' ";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getitems_outward_details_withDept(String Startdate, String Enddate) {
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " , "+TBL_DEPARTMENT+"  WHERE " +KEY_BillStatus+" = 1 AND "+
                KEY_InvoiceDate + " BETWEEN '" + Startdate + "' AND '" +Enddate+"' AND "+TBL_OUTWARD_SUPPLY_LEDGER+"."+KEY_DeptCode+" = "+TBL_DEPARTMENT+"."+KEY_DeptCode;
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getitems_outward_details_withCateg(String Startdate, String Enddate) {
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " , "+TBL_CATEGORY+"  WHERE " +KEY_BillStatus+" = 1 AND "+
                KEY_InvoiceDate + " BETWEEN '" + Startdate + "' AND '" +Enddate+"' AND "+TBL_OUTWARD_SUPPLY_LEDGER+"."+KEY_CategCode+" = "+TBL_CATEGORY+"."+KEY_CategCode;
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }



    public Cursor getitems_b2ba(String No_ori, String Date_ori,String No, String Date, String cust_GSTIN, String custStateCode) {
        String selectQuery = "SELECT * FROM " + TBL_GSTR1_AMEND + " WHERE " + KEY_InvoiceNo + " Like '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND "+KEY_GSTIN+" LIKE '"+cust_GSTIN+"' AND "+ KEY_OriginalInvoiceNo+" LIKE '"+No_ori+"' AND "+KEY_OriginalInvoiceDate
                +" LIKE '"+Date_ori+"' AND "+KEY_BusinessType+" LIKE 'B2BA' AND "+
                KEY_CustStateCode+" LIKE '"+custStateCode+"'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getOutwardB2ba() {
        String b2b = "B2B";
        String selectQuery = "SELECT * FROM " + TBL_GSTR1_AMEND + " WHERE BusinessType LIKE '" + b2b + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        // Cursor result = dbFNB.query(TBL_OUTWARD_SUPPLIES_LEDGER_AMEND, new String[] {"*"}, "BusinessType = B2B",  null, null, null, null);
        return result;
    }

    public Cursor getOutwardB2Cl(String startDate, String endDate) {
        String b2c = "B2C";
        /*String selectQuery = "SELECT * FROM "+ TBL_OUTWARD_SUPPLY_LEDGER + " WHERE BusinessType LIKE '"+b2c+"'"; // AND GrandTotal > 250000 AND IGSTRate > 0";*/
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + startDate +
                "' AND '" + endDate + "' AND " + KEY_BusinessType + " LIKE 'B2C' AND " + KEY_SubTotal + " > 250000 "
                +" AND BillStatus =1 Order By CustStateCode asc";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getitems_b2cl(String No, String Date, String custname_str, String statecode_str) {
        //No = String.valueOf(160005);
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceNo + " LIKE '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND " + KEY_CustName + "  LIKE '" + custname_str + "' AND " + KEY_CustStateCode + " LIKE '" +
                statecode_str + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getitems_b2cl_withoutCustName(String No, String Date,  String statecode_str) {
        //No = String.valueOf(160005);
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceNo + " LIKE '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND " +  KEY_CustStateCode + " LIKE '" +
                statecode_str + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getitems_b2cl(String No, String Date, String statecode_str) {
        //No = String.valueOf(160005);
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE " + KEY_InvoiceNo + " LIKE '" + No + "' AND " +
                KEY_InvoiceDate + " LIKE '" + Date + "' AND " + KEY_CustStateCode + " LIKE '" + statecode_str + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getOutwardB2Cs(String startDate, String endDate) {
        String b2c = "B2C";
        /*String selectQuery = "SELECT * FROM "+ TBL_OUTWARD_SUPPLY_LEDGER + " WHERE BusinessType LIKE '"+b2c+"'"; // AND GrandTotal > 250000 AND IGSTRate > 0";*/
        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + startDate +
                "' AND '" + endDate + "' AND " + KEY_BusinessType + " LIKE 'B2C' AND BillStatus = 1 ";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getOutwardB2cla() {
        String b2c = "B2C";
        String selectQuery = "SELECT * FROM " + TBL_GSTR1_AMEND + " WHERE BusinessType LIKE '" + b2c + "' AND GrandTotal > 250000";
        //String selectQuery = "SELECT * FROM "+ TBL_OUTWARD_SUPPLIES_LEDGER_AMEND + " WHERE GrandTotal > 250000";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getSupplierlist(String StartDate, String EndDate) {
        String selectQuery = " Select " + KEY_SUPPLIERNAME + " , " + KEY_GSTIN + " FROM " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + " WHERE " +
                KEY_InvoiceDate + " BETWEEN '" + StartDate + "' AND '" + EndDate + "' AND " + KEY_SupplierType + " LIKE 'Registered' OR ( " + KEY_SupplierType + " LIKE 'UnRegistered' AND " +
                KEY_AttractsReverseCharge + " LIKE 'no')";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getInwardtaxed_2A(String StartDate, String EndDate) {
        String selectQuery = " Select * FROM " + TBL_READ_FROM_2A + " WHERE " +
                KEY_InvoiceDate + " BETWEEN '" + StartDate + "' AND '" + EndDate + "' ";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public long saveData_2A(Model_reconcile s) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_GSTIN, s.getGstin());
        contentValues.put(KEY_InvoiceNo, s.getInvoiceNo());
        contentValues.put(KEY_InvoiceDate, s.getInvoiceDate());
        contentValues.put(KEY_POS, s.getPos());
        contentValues.put(KEY_AttractsReverseCharge, s.getAttractsReverseCharge());
        contentValues.put(KEY_ProvisionalAssess, s.getProAss());

        contentValues.put(KEY_SupplyType, s.getSupplyType());
        contentValues.put(KEY_HSNCode, s.getHSN());
        contentValues.put(KEY_TaxableValue, s.getTaxable_value());
        contentValues.put(KEY_IGSTRate, s.getIgst_rate());
        contentValues.put(KEY_IGSTAmount, s.getIgst_amt());

        contentValues.put(KEY_SGSTRate, s.getSgst_rate());
        contentValues.put(KEY_SGSTAmount, s.getSgst_amt());
        contentValues.put(KEY_CGSTRate, s.getCgst_rate());
        contentValues.put(KEY_CGSTAmount, s.getCgst_amt());

        long result = dbFNB.insert(TBL_READ_FROM_2A, null, contentValues);
        return result;
    }


    public long add_GSTR1_B2CSAmmend(GSTR2_B2B_Amend ammend) {
        ContentValues contentValues = new ContentValues();
        long result =0;
        try {
            contentValues.put(KEY_MONTH, ammend.getTaxMonth());
            contentValues.put(KEY_HSNCode, ammend.getHsn_ori());
            contentValues.put(KEY_SupplyType, ammend.getType_ori());
            contentValues.put(KEY_POS, ammend.getPos_ori());
            contentValues.put(KEY_HSNCode_REV, ammend.getHsn_rev());
            contentValues.put(KEY_SupplyType_REV, ammend.getType_rev());
            contentValues.put(KEY_CustStateCode, ammend.getCustStateCode());

            contentValues.put(KEY_TaxableValue, ammend.getTaxableValue());
            contentValues.put(KEY_IGSTRate, ammend.getIgstrate());
            contentValues.put(KEY_IGSTAmount, ammend.getIgstamt());
            contentValues.put(KEY_CGSTRate, ammend.getCgstrate());
            contentValues.put(KEY_CGSTAmount, ammend.getCgstamt());
            contentValues.put(KEY_SGSTRate, ammend.getSgstrate());
            contentValues.put(KEY_SGSTAmount, ammend.getSgstamt());
            contentValues.put(KEY_cessAmount, ammend.getCsamt());

            contentValues.put(KEY_BusinessType, "B2CSA");
            contentValues.put(KEY_InvoiceDate, ammend.getInvoiceDate_ori());

            //contentValues.put(KEY_BusinessType,bussinessType);


            result = dbFNB.insert(TBL_GSTR1_AMEND, null, contentValues);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;

    }
    public Cursor getAmmends_GSTR1_b2cs(String hsn_ori, String taxMonth)
    {
        String whereClause = "Select * FROM "+TBL_GSTR1_AMEND+" WHERE "+ KEY_MONTH+" LIKE '"+taxMonth+"' AND "
                +KEY_HSNCode+" LIKE '"+hsn_ori+"' AND "+KEY_BusinessType+" LIKE 'B2CSA'";
        return dbFNB.rawQuery(whereClause, null);
    }
    public long DeleteAmmend_GSTR1_B2CSA(String taxMonth, String hsn_ori, double taxableVal,String pos_ori, String custStateCode,
                                         double IGSTAmt,double CGSTAmt,double SGSTAmt,double cessAmt)
    {
        String deleteClause = KEY_MONTH+" LIKE '"+taxMonth+"' AND "+
                KEY_HSNCode+" LIKE '"+hsn_ori+"' AND "+KEY_TaxableValue+" = "+taxableVal+" AND "+
                KEY_IGSTAmount+" = "+IGSTAmt+" AND "+KEY_CGSTAmount+" = "+CGSTAmt+" AND "+
                KEY_SGSTAmount+" = "+SGSTAmt+" AND "+KEY_cessAmount+" = "+cessAmt+" AND "+
                KEY_POS+" LIKE '"+pos_ori+"' AND "+KEY_CustStateCode+" LIKE '"+custStateCode+"' AND "+
                KEY_BusinessType+" LIKE 'B2CSA'";
        return dbFNB.delete(TBL_GSTR1_AMEND, deleteClause, null);
    }


    public long add_GSTR2_B2BAmmend(GSTR2_B2B_Amend ammend) {
        ContentValues contentValues = new ContentValues();
        long result =0;
        try {
            contentValues.put(KEY_GSTIN_Ori, ammend.getGstin_ori());
            contentValues.put(KEY_OriginalInvoiceNo, ammend.getInvoiceNo_ori());
            Date  d = new SimpleDateFormat("dd-MM-yyyy").parse(ammend.getInvoiceDate_ori());
            contentValues.put(KEY_OriginalInvoiceDate, d.getTime());
            contentValues.put(KEY_GSTIN, ammend.getGstin_rev());
            contentValues.put(KEY_InvoiceNo, ammend.getInvoiceNo_rev());
             d = new SimpleDateFormat("dd-MM-yyyy").parse(ammend.getInvoiceDate_rev());
            contentValues.put(KEY_InvoiceDate, d.getTime());
            contentValues.put(KEY_HSNCode, ammend.getHSn());
            contentValues.put(KEY_SupplyType, ammend.getType());
            contentValues.put(KEY_Value, ammend.getValue());
            contentValues.put(KEY_TaxableValue, ammend.getTaxableValue());
            contentValues.put(KEY_IGSTRate, ammend.getIgstrate());
            contentValues.put(KEY_IGSTAmount, ammend.getIgstamt());
            contentValues.put(KEY_CGSTRate, ammend.getCgstrate());
            contentValues.put(KEY_CGSTAmount, ammend.getCgstamt());
            contentValues.put(KEY_SGSTRate, ammend.getSgstrate());
            contentValues.put(KEY_SGSTAmount, ammend.getSgstamt());
            contentValues.put(KEY_cessAmount, ammend.getCsamt());
            contentValues.put(KEY_POS, ammend.getPOS());
            contentValues.put(KEY_SupplierType, ammend.getSupplierType());
            //contentValues.put(KEY_BusinessType,bussinessType);


             result = dbFNB.insert(TBL_GSTR2_AMEND, null, contentValues);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;

    }

    public long add_GSTR1_B2CLAmmend(GSTR2_B2B_Amend ammend) {
        ContentValues contentValues = new ContentValues();
        long result =0;
        try {
            contentValues.put(KEY_CustName, ammend.getRecipientName());
            contentValues.put(KEY_CustStateCode, ammend.getCustStateCode());
            contentValues.put(KEY_OriginalInvoiceNo, ammend.getInvoiceNo_ori());
            Date  d = new SimpleDateFormat("dd-MM-yyyy").parse(ammend.getInvoiceDate_ori());
            contentValues.put(KEY_OriginalInvoiceDate, d.getTime());

            contentValues.put(KEY_InvoiceNo, ammend.getInvoiceNo_rev());
            d = new SimpleDateFormat("dd-MM-yyyy").parse(ammend.getInvoiceDate_rev());
            contentValues.put(KEY_InvoiceDate, d.getTime());
            contentValues.put(KEY_HSNCode, ammend.getHSn());
            contentValues.put(KEY_SupplyType, ammend.getType());
            //contentValues.put(KEY_POS, ammend.getPOS());
            contentValues.put(KEY_Value, ammend.getValue());
            contentValues.put(KEY_TaxableValue, ammend.getTaxableValue());
            contentValues.put(KEY_IGSTRate, ammend.getIgstrate());
            contentValues.put(KEY_IGSTAmount, ammend.getIgstamt());
            contentValues.put(KEY_cessAmount, ammend.getCsamt());
            //contentValues.put(KEY_POS_REV, ammend.getPOS());
            contentValues.put(KEY_BusinessType, "B2CLA");

            //contentValues.put(KEY_BusinessType,bussinessType);


            result = dbFNB.insert(TBL_GSTR1_AMEND, null, contentValues);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;

    }
    public Cursor getAmmends_GSTR1_b2cl(String recipientName, String recipientStateCode, String inv_no_ori,
                                        String inv_date_ori, String CustStateCode)
    {
        String whereClause = "Select * FROM "+TBL_GSTR1_AMEND+" WHERE "+ KEY_OriginalInvoiceNo+" LIKE '"+inv_no_ori+"' AND "
                +KEY_OriginalInvoiceDate+" LIKE '"+inv_date_ori+"' AND "/*+KEY_POS+" LIKE '"+pos+*/+
                /*"' AND "+*/KEY_CustName+" LIKE '"+recipientName+"' AND "+
                KEY_CustStateCode+" LIKE '"+CustStateCode+"' AND "+KEY_BusinessType+" LIKE 'B2CLA'";
        return dbFNB.rawQuery(whereClause, null);
    }
    public long DeleteAmmend_GSTR1_b2cl(String inv_no_ori, String inv_date_ori,String inv_no_rev,
                                        String inv_date_rev,String hsn, double taxableVal,double IGSTAmount,double cessAmount)
    {
        String deleteClause = KEY_OriginalInvoiceNo+" LIKE '"+inv_no_ori+"' AND "+KEY_OriginalInvoiceDate+" LIKE '"+inv_date_ori+"' AND "+
                KEY_InvoiceNo+" LIKE '"+inv_no_rev+"' AND "+KEY_InvoiceDate+" LIKE '"+inv_date_rev+"' AND "+
                KEY_IGSTAmount+" = "+IGSTAmount+" AND "+KEY_cessAmount+" ="+cessAmount+" AND "+
                KEY_HSNCode+" LIKE '"+hsn+"' AND "+KEY_TaxableValue+" = "+taxableVal+" AND "+KEY_BusinessType+" LIKE 'B2CLA'";
        return dbFNB.delete(TBL_GSTR1_AMEND, deleteClause, null);
    }


    public long add_GSTR1_B2BAmmend(GSTR2_B2B_Amend ammend) {
        ContentValues contentValues = new ContentValues();
        long result =0;
        try {
            contentValues.put(KEY_GSTIN, ammend.getGstin_ori());
            contentValues.put(KEY_OriginalInvoiceNo, ammend.getInvoiceNo_ori());
            Date  d = new SimpleDateFormat("dd-MM-yyyy").parse(ammend.getInvoiceDate_ori());
            contentValues.put(KEY_OriginalInvoiceDate, d.getTime());
            contentValues.put(KEY_Ecom_GSTIN, ammend.getGstin_rev());
            contentValues.put(KEY_InvoiceNo, ammend.getInvoiceNo_rev());
            d = new SimpleDateFormat("dd-MM-yyyy").parse(ammend.getInvoiceDate_rev());
            contentValues.put(KEY_InvoiceDate, d.getTime());
            contentValues.put(KEY_HSNCode, ammend.getHSn());
            contentValues.put(KEY_SupplyType, ammend.getType());
            contentValues.put(KEY_Value, ammend.getValue());
            contentValues.put(KEY_TaxableValue, ammend.getTaxableValue());
            contentValues.put(KEY_IGSTRate, ammend.getIgstrate());
            contentValues.put(KEY_IGSTAmount, ammend.getIgstamt());
            contentValues.put(KEY_CGSTRate, ammend.getCgstrate());
            contentValues.put(KEY_CGSTAmount, ammend.getCgstamt());
            contentValues.put(KEY_SGSTRate, ammend.getSgstrate());
            contentValues.put(KEY_SGSTAmount, ammend.getSgstamt());
            contentValues.put(KEY_cessAmount, ammend.getCsamt());
            contentValues.put(KEY_CustStateCode, ammend.getCustStateCode());
            contentValues.put(KEY_BusinessType, "B2BA");

            //contentValues.put(KEY_BusinessType,bussinessType);


            result = dbFNB.insert(TBL_GSTR1_AMEND, null, contentValues);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return result;

    }
    public Cursor getAmmends_GSTR1_b2b(String gstin, String inv_no_ori, String inv_date_ori)
    {
        String whereClause = "Select * FROM "+TBL_GSTR1_AMEND+" WHERE "+ KEY_OriginalInvoiceNo+" LIKE '"+inv_no_ori+"' AND "+KEY_OriginalInvoiceDate+" LIKE '"+inv_date_ori+
                "' AND "+KEY_GSTIN+" LIKE '"+gstin+"' AND "+KEY_BusinessType+" LIKE 'B2BA'";
        return dbFNB.rawQuery(whereClause, null);
    }
    public long DeleteAmmend_GSTR1_B2BA(String inv_no_ori, String inv_date_ori,String inv_no_rev,
                                        String inv_date_rev,String hsn, double taxableVal,
                                        double IgstAmt, double CgstAmt, double SgstAmt)
    {
        String deleteClause = KEY_OriginalInvoiceNo+" LIKE '"+inv_no_ori+"' AND "+KEY_OriginalInvoiceDate+" LIKE '"+inv_date_ori+"' AND "+
                KEY_InvoiceNo+" LIKE '"+inv_no_rev+"' AND "+KEY_InvoiceDate+" LIKE '"+inv_date_rev+"' AND "+
                KEY_HSNCode+" LIKE '"+hsn+"' AND "+KEY_TaxableValue+" = "+taxableVal+" AND "+
                KEY_IGSTAmount+" = "+IgstAmt+" AND "+KEY_CGSTAmount+" = "+CgstAmt+" AND "+KEY_SGSTAmount+" = "+SgstAmt+" AND "+
                KEY_BusinessType+" LIKE 'B2BA'";
        return dbFNB.delete(TBL_GSTR1_AMEND, deleteClause, null);
    }

    public  String getSupplierGSTIN(String supplierCode)
    {
        String whereClause = "Select  GSTIN from "+TBL_Supplier+" WHERE "+KEY_SupplierCode+" LIKE '"+supplierCode+"'";
        Cursor cursor = dbFNB.rawQuery(whereClause,null);
        String gstin = "";
        if(cursor!=null && cursor.moveToFirst())
            gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
        if(gstin==null)
            gstin="";
        return gstin;

    }
    public long DeleteAmmend_GSTR2_B2BA(String inv_no_ori, String inv_date_ori,String inv_no_rev,
                                        String inv_date_rev,String hsn, double taxableVal,
                                        double IgstAmt, double CgstAmt, double SgstAmt)
    {
        String deleteClause = KEY_OriginalInvoiceNo+" LIKE '"+inv_no_ori+"' AND "+KEY_OriginalInvoiceDate+" LIKE '"+inv_date_ori+"' AND "+
                KEY_InvoiceNo+" LIKE '"+inv_no_rev+"' AND "+KEY_InvoiceDate+" LIKE '"+inv_date_rev+"' AND "+
                KEY_IGSTAmount+" = "+IgstAmt+" AND "+KEY_CGSTAmount+" = "+CgstAmt+" AND "+KEY_SGSTAmount+" = "+SgstAmt+" AND "+
                KEY_HSNCode+" LIKE '"+hsn+"' AND "+KEY_TaxableValue+" = "+taxableVal;
        return dbFNB.delete(TBL_GSTR2_AMEND, deleteClause, null);
    }

    public Cursor getAmmends_GSTR2_b2b(String gstin, String inv_no_ori, String inv_date_ori,String supplierType)
    {
        String whereClause = "Select * FROM "+TBL_GSTR2_AMEND+" WHERE "+ KEY_OriginalInvoiceNo+" LIKE '"+inv_no_ori+"' AND "+
                KEY_OriginalInvoiceDate+" LIKE '"+inv_date_ori+"' AND "+KEY_GSTIN_Ori+" LIKE '"+gstin+"'  AND "+
                KEY_SupplierType+" LIKE '"+supplierType+"'";
        return dbFNB.rawQuery(whereClause, null);
    }


    public long addB2CsAmmend(String month, String supply_ori, String hsn, String pos_ori, String supply_rev, String hsn_rev,
                              String pos_rev, String aggval, String igstrate, String cgstrate, String sgstrate, String igstamt,
                              String cgstamt, String sgstamt, String proass, String bussinessType, String date) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_MONTH, month);
        contentValues.put(KEY_SupplyType, supply_ori);
        contentValues.put(KEY_HSNCode, hsn);
        contentValues.put(KEY_POS, pos_ori);
        contentValues.put(KEY_SupplyType_REV, supply_rev);
        contentValues.put(KEY_HSNCode_REV, hsn_rev);

        contentValues.put(KEY_POS_REV, pos_rev);
        contentValues.put(KEY_TaxableValue, aggval);
        contentValues.put(KEY_IGSTRate, igstrate);
        contentValues.put(KEY_IGSTAmount, igstamt);
        contentValues.put(KEY_CGSTRate, cgstrate);
        contentValues.put(KEY_CGSTAmount, cgstamt);
        contentValues.put(KEY_SGSTRate, sgstrate);
        contentValues.put(KEY_SGSTAmount, sgstamt);
        contentValues.put(KEY_ProvisionalAssess, proass);
        contentValues.put(KEY_BusinessType, bussinessType);
        contentValues.put(KEY_InvoiceDate, date);

        long result = dbFNB.insert(TBL_GSTR1_AMEND, null, contentValues);
        return result;
    }

    public String getGSTIN() {
        String Selectquery = "Select GSTIN FROM " + TBL_OWNER_DETAILS + " WHERE IsMainOffice='YES'";
        Cursor result = dbFNB.rawQuery(Selectquery, null);
        String gstin = null;
        if (result != null) {
            if (result.moveToFirst())
                gstin = result.getString(result.getColumnIndex("GSTIN"));
        }
        return gstin;

    }
    public String getOwnerReferenceNo() {
        String Selectquery = "Select "+KEY_REFERENCE_NO+" FROM " + TBL_OWNER_DETAILS + " WHERE IsMainOffice='YES'";
        Cursor result = dbFNB.rawQuery(Selectquery, null);
        String refno = null;
        if (result != null) {
            if (result.moveToFirst())
                refno = result.getString(result.getColumnIndex(KEY_REFERENCE_NO));
        }
        return refno;

    }
    public Cursor getOwnerDetail() {
        String Selectquery = "Select * FROM " + TBL_OWNER_DETAILS ;
        Cursor result = dbFNB.rawQuery(Selectquery, null);

        return result;

    }
    public Cursor getOwnerDetail_counter() {
        SQLiteDatabase db = getReadableDatabase();
        String Selectquery = "Select * FROM " + TBL_OWNER_DETAILS ;
        Cursor result = null;
        try {
            result = db.rawQuery(Selectquery, null);
        }catch (Exception e)
        {
            e.printStackTrace();
            result = null;
        }finally {
            return result;
        }



    }

    public int deleteOwnerDetails() {
        SQLiteDatabase db = getWritableDatabase();
        int result=0;
        try{
             result = db.delete(TBL_OWNER_DETAILS, null, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            result =0;

        }finally {
            return result;
        }
    }
    public String gettaxeename() {
        String Selectquery = "Select " + KEY_Owner_Name + " FROM " + TBL_OWNER_DETAILS + " WHERE " + KEY_IsMainOffice + " LIKE 'yes'";
        Cursor result = dbFNB.rawQuery(Selectquery, null);
        String gstin = null;
        if (result != null)
            gstin = result.getString(result.getColumnIndex("Name")).toString();
        return gstin;

    }

    public Cursor getItemListForSupplier(String supplierName) {
        // check in inward section
        Cursor result = null;
        String queryString = "Select " + KEY_ItemName + " FROM " + TBL_ITEM_Inward + " WHERE " + KEY_SUPPLIERNAME + " LIKE '" + supplierName + "'";
        result = dbFNB.rawQuery(queryString, null);
        return result;

    }

    public Cursor getItemsdetail_Inw(String Itemname, String SupplierName) {
        // check in inward section
        String selectQuery = " Select * FROM " + TBL_ITEM_Inward + " WHERE  " + KEY_SUPPLIERNAME + " LIKE '" + SupplierName + "'" +
                " AND " + KEY_ItemName + " LIKE '" + Itemname + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getGSTINListOnCategory(String category) {
        Cursor result = null;
        String queryString = "Select GSTIN FROM " + TBL_USER_GSTIN + " WHERE Category LIKE '" + category + "'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    public Cursor getBillsforGSTIN_2(String gstin, String suppliername, String startdate, String enddate) {
        Cursor result = null;
        // richa to do ->date
        String queryString = "Select * FROM " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_GSTIN + " LIKE '" + gstin + "' AND " +
                KEY_SUPPLIERNAME + " LIKE '" + suppliername + "' AND " + KEY_InvoiceDate + " BETWEEN '" + startdate + "' AND '" + enddate + "'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    public Cursor getbill_inward(String invoiceno, String invoicedate) {
        Cursor result = null;
        // richa to do ->date
        String queryString = "Select * FROM " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceNo + " LIKE '" + invoiceno + "'"
                + " AND " + KEY_InvoiceDate + " LIKE '" + invoicedate + "'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }


    public Cursor getBillsforGSTIN_2A(String Invno, String InvDate, String gstin) {
        Cursor result = null;

        String queryString = "Select * FROM " + TBL_READ_FROM_2A + " WHERE " + KEY_GSTIN + " LIKE '" + gstin + "' AND " + KEY_InvoiceNo +
                " LIKE '" + Invno + "' AND " + KEY_InvoiceDate + " LIKE '" + InvDate + "'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    public Cursor getBillsforGSTIN_1(String Invno, String InvDate, String gstin) {
        Cursor result = null;

        String queryString = "Select * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_GSTIN + " LIKE '" +
                gstin + "' AND " + KEY_InvoiceNo + " LIKE '" + Invno + "' AND " + KEY_InvoiceDate + " LIKE '" + InvDate + "'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    public Cursor getBillsforGSTIN_1A(String Startdate, String Enddate) {
        Cursor result = null;

        String queryString = "Select * FROM " + TBL_READ_FROM_1A + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + Startdate + "' AND '" +
                Enddate + "'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }


    /*************************************************************************************************************************************
     * Checks if Database is exists
     *
     * @return True if Database exists False otherwise
     *************************************************************************************************************************************/
    private boolean IsDatabaseExists() {
        File dbDirectory, dbFile;
        dbDirectory = new File(DB_PATH);
        if (!dbDirectory.exists()) {
            dbDirectory.mkdir();
        }
        dbFile = new File(DB_PATH + DB_NAME);
        return dbFile.exists();
    }

    public void CreateDatabase() {
        try {
            dbFNB = this.getReadableDatabase();

        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, exp.toString());
        }
    }

    public void DeleteDatabase() {
        try {
            File fDelete = new File(DB_PATH + DB_NAME);
            boolean bResult = false;
            if (fDelete.exists()) {
                bResult = fDelete.delete();
                Toast.makeText(myContext, "DB deletion = " + String.valueOf(bResult), Toast.LENGTH_SHORT).show();
            }

            File BackupDirectory = new File(DB_BACKUP_PATH);
            if (BackupDirectory.exists() && BackupDirectory.isDirectory()) {
                File[] BackupFiles = BackupDirectory.listFiles();
                for (int i = 0; i < BackupFiles.length; i++) {
                    bResult = BackupFiles[i].delete();
                    Toast.makeText(myContext, "Backup File" + (i + 1) + " deletion = " + String.valueOf(bResult),
                            Toast.LENGTH_SHORT).show();
                }
                bResult = BackupDirectory.delete();
                Toast.makeText(myContext, "Backup Directory deletion = " + String.valueOf(bResult), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, exp.toString());
        }
    }

    private void CopyDatabase() throws IOException {
        InputStream isAssetDbFile = myContext.getAssets().open(DB_NAME);
        OutputStream osNewDbFile = new FileOutputStream(DB_PATH + DB_NAME);
        byte[] bFileBuffer = new byte[1024];
        int iBytesRead = 0;

        while ((iBytesRead = isAssetDbFile.read(bFileBuffer)) > 0) {
            osNewDbFile.write(bFileBuffer, 0, iBytesRead);
        }

        osNewDbFile.flush();
        osNewDbFile.close();
        isAssetDbFile.close();
    }

    public void OpenDatabase() {
        try {
            dbFNB = this.getWritableDatabase();

        } catch (Exception exp) {
            Toast.makeText(myContext, exp.getMessage(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, exp.toString());
        }
    }

    public int BackUpDatabase() {
        try {
            int iCount = 1;
            long lBackupLastModified = 0;
            String strDbBackupFileName = "CopyOf_WeP_FnB_Database";
            String[] DirectoryFiles;
            File dbBackupDirectory = new File(DB_BACKUP_PATH);
            if (!dbBackupDirectory.exists()) {
                dbBackupDirectory.mkdir();
            }
            DirectoryFiles = dbBackupDirectory.list();
            for (int i = 0; i < DirectoryFiles.length; i++) {

                if (DirectoryFiles[i].contains(strDbBackupFileName)) {
                    iCount++;
                }
            }
            if (iCount > 1) {
                String strTemp = strDbBackupFileName + String.valueOf(iCount - 1) + ".db";
                File DbFile = new File(DB_BACKUP_PATH + strTemp);
                lBackupLastModified = DbFile.lastModified();

            }
            /*if (lDbLastModified == lBackupLastModified) {
                return 0;
            }*/
            strDbBackupFileName += String.valueOf(iCount);

            InputStream istrmDbFile = new FileInputStream(dbFNB.getPath());// returns the path where database is stored internally
            OutputStream ostrmBackUpDbFile = new FileOutputStream(DB_BACKUP_PATH + strDbBackupFileName);
            byte[] bFileBuffer = new byte[1024];
            int iBytesRead = 0;
            while ((iBytesRead = istrmDbFile.read(bFileBuffer)) > 0) {
                ostrmBackUpDbFile.write(bFileBuffer, 0, iBytesRead);
            }

            ostrmBackUpDbFile.flush();
            ostrmBackUpDbFile.close();
            istrmDbFile.close();
            return 1;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return -1;
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public void CloseDatabase() {
        // Close database connection
        while (true) {
            if (dbFNB != null && dbFNB.isOpen()) {
                dbFNB.close();
            } else {
                break;
            }
        }
    }

    // Database Insert, Read, Update and Delete Functions

    /*************************************************************************************************************************************
     * Clears all the bill and other transaction from the database. Clears the
     * details from following tables 1. BillDetail 2. BillItem 3. PendingKOT 4.
     * RiderSettlement 5. PaymentReceipt 6. Customer (Transaction details)
     *************************************************************************************************************************************/
    public void DeleteAllTransaction() {
        int iResult = 0;

        // BillDetail
        iResult = dbFNB.delete(TBL_BILLDETAIL, null, null);
        Log.d("DeleteAllTransaction", TBL_BILLDETAIL + " - Deleted rows:" + iResult);

        // BillItem
        iResult = dbFNB.delete(TBL_BILLITEM, null, null);
        Log.d("DeleteAllTransaction", TBL_BILLITEM + " - Deleted rows:" + iResult);

        // PendingKOT
        iResult = dbFNB.delete(TBL_PENDINGKOT, null, null);
        Log.d("DeleteAllTransaction", TBL_PENDINGKOT + " - Deleted rows:" + iResult);

        // RriderSettlement
        iResult = dbFNB.delete(TBL_RIDERSETTLEMENT, null, null);
        Log.d("DeleteAllTransaction", TBL_RIDERSETTLEMENT + " - Deleted rows:" + iResult);

        // PaymentReceipt
        iResult = dbFNB.delete(TBL_PAYMENTRECEIPT, null, null);
        Log.d("DeleteAllTransaction", TBL_PAYMENTRECEIPT + " - Deleted rows:" + iResult);

        // Customer
        cvDbValues = new ContentValues();
        cvDbValues.put("LastTransaction", 0);
        cvDbValues.put("TotalTransaction", 0);
        iResult = dbFNB.update(TBL_CUSTOMER, cvDbValues, null, null);
        Log.d("DeleteAllTransaction", TBL_CUSTOMER + " - Updated rows:" + iResult);

        Toast.makeText(myContext, "All transaction are cleared", Toast.LENGTH_SHORT).show();
    }

    /************************************************************************************************************************************/
    /*****************************************************
     * Table - BillSetting
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Bill setting-----
    public long addBillSetting(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("BusinessDate", objBillSetting.getBusinessDate());
        cvDbValues.put("DineIn1From", objBillSetting.getDineIn1From());
        cvDbValues.put("DineIn1To", objBillSetting.getDineIn1To());
        cvDbValues.put("DineIn2From", objBillSetting.getDineIn2From());
        cvDbValues.put("DineIn2To", objBillSetting.getDineIn2To());
        cvDbValues.put("DineIn3From", objBillSetting.getDineIn3From());
        cvDbValues.put("DineIn3To", objBillSetting.getDineIn3To());
        cvDbValues.put("FooterText", objBillSetting.getFooterText());
        cvDbValues.put("HeaderText", objBillSetting.getHeaderText());
        cvDbValues.put("KOTType", objBillSetting.getKOTType());
        cvDbValues.put("MaximumTables", objBillSetting.getMaxTable());
        cvDbValues.put("MaximumWaiters", objBillSetting.getMaxWaiter());
        cvDbValues.put("POSNumber", objBillSetting.getPOSNumber());
        cvDbValues.put("PrintKOT", objBillSetting.getPrintKOT());
        cvDbValues.put("SubUdfText", objBillSetting.getSubUdfText());
        cvDbValues.put("TIN", objBillSetting.getTIN());

        return dbFNB.insert(TBL_BILLSETTING, null, cvDbValues);
    }

    // -----Retrieve Bill setting-----
    public Cursor getBillSetting() {
        return dbFNB.query(TBL_BILLSETTING, new String[]{"*"}, null, null, null, null, null);
    }

    // -----Retrieve Business Date-----
    public Cursor getCurrentDate() {
        return dbFNB.query(TBL_BILLSETTING, new String[]{"BusinessDate"}, null, null, null, null, null);

    }

    // -----Update Bill setting-----
    public int updateBillSetting(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("BusinessDate", objBillSetting.getBusinessDate());
        cvDbValues.put("DineIn1From", objBillSetting.getDineIn1From());
        cvDbValues.put("DineIn1To", objBillSetting.getDineIn1To());
        cvDbValues.put("DineIn2From", objBillSetting.getDineIn2From());
        cvDbValues.put("DineIn2To", objBillSetting.getDineIn2To());
        cvDbValues.put("DineIn3From", objBillSetting.getDineIn3From());
        cvDbValues.put("DineIn3To", objBillSetting.getDineIn3To());
        cvDbValues.put("FooterText", objBillSetting.getFooterText());
        cvDbValues.put("HeaderText", objBillSetting.getHeaderText());
        cvDbValues.put("KOTType", objBillSetting.getKOTType());
        cvDbValues.put("MaximumTables", objBillSetting.getMaxTable());
        cvDbValues.put("MaximumWaiters", objBillSetting.getMaxWaiter());
        cvDbValues.put("POSNumber", objBillSetting.getPOSNumber());
        cvDbValues.put("PrintKOT", objBillSetting.getPrintKOT());
        cvDbValues.put("SubUdfText", objBillSetting.getSubUdfText());
        cvDbValues.put("TIN", objBillSetting.getTIN());

        return dbFNB.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    /*// Temp functions - Header-Footer Text update
    public int updateHeaderFooterText(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("FooterText", objBillSetting.getFooterText());
        cvDbValues.put("HeaderText", objBillSetting.getHeaderText());

        return dbFNB.update(TBL_BILLSETTING, cvDbValues, null, null);
    }*/
    // Temp functions - Header-Footer Text update
    public int updateHeaderFooterText(String str1, String str2) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("FooterText", str2);
        contentValues.put("HeaderText", str1);
        return dbFNB.update(TBL_BILLSETTING, contentValues, null, null);
    }

    // Temp functions - Other bill settings update
    public int updateMiscellaneousBillsettings(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("BusinessDate", objBillSetting.getBusinessDate());
        cvDbValues.put("POSNumber", objBillSetting.getPOSNumber());
        cvDbValues.put("SubUdfText", objBillSetting.getSubUdfText());
        cvDbValues.put("TIN", objBillSetting.getTIN());
        cvDbValues.put("ServiceTaxType", objBillSetting.getServiceTaxType());
        cvDbValues.put("ServiceTaxPercent", objBillSetting.getServiceTaxPercent());
        cvDbValues.put("WeighScale", objBillSetting.getWeighScale());

        return dbFNB.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    // update Business date
    public int updateBusinessDate(String NextDate) {
        cvDbValues = new ContentValues();

        cvDbValues.put("BusinessDate", NextDate);

        return dbFNB.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    // Temp functions - Dine In settings update
    public int updateDineInsettings(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DineIn1From", objBillSetting.getDineIn1From());
        cvDbValues.put("DineIn1To", objBillSetting.getDineIn1To());
        cvDbValues.put("DineIn2From", objBillSetting.getDineIn2From());
        cvDbValues.put("DineIn2To", objBillSetting.getDineIn2To());
        cvDbValues.put("DineIn3From", objBillSetting.getDineIn3From());
        cvDbValues.put("DineIn3To", objBillSetting.getDineIn3To());
        cvDbValues.put("PrintKOT", objBillSetting.getPrintKOT());
        cvDbValues.put("KOTType", objBillSetting.getKOTType());
        cvDbValues.put("MaximumTables", objBillSetting.getMaxTable());
        cvDbValues.put("MaximumWaiters", objBillSetting.getMaxWaiter());
        cvDbValues.put("DineIn1Caption", objBillSetting.getDineIn1Caption());
        cvDbValues.put("DineIn2Caption", objBillSetting.getDineIn2Caption());
        cvDbValues.put("DineIn3Caption", objBillSetting.getDineIn3Caption());

        cvDbValues.put("DineInRate", objBillSetting.getDineInRate());
        cvDbValues.put("CounterSalesRate", objBillSetting.getCounterSalesRate());
        cvDbValues.put("PickUpRate", objBillSetting.getPickUpRate());
        cvDbValues.put("HomeDeliveryRate", objBillSetting.getHomeDeliveryRate());

        cvDbValues.put(KEY_HomeDineInCaption, objBillSetting.getDineInCaption());
        cvDbValues.put(KEY_HomeCounterSalesCaption, objBillSetting.getCounterSalesCaption());
        cvDbValues.put(KEY_HomeTakeAwayCaption, objBillSetting.getTakeAwayCaption());
        cvDbValues.put(KEY_HomeHomeDeliveryCaption, objBillSetting.getHomeDeliveryCaption());

        return dbFNB.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    // Temp functions - Other settings update
    public int updateOtherSettings(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("LoginWith", objBillSetting.getLoginWith());
        cvDbValues.put("DateAndTime", objBillSetting.getDateAndTime());
        cvDbValues.put("PriceChange", objBillSetting.getPriceChange());
        cvDbValues.put("BillwithStock", objBillSetting.getBillwithStock());
        cvDbValues.put("BillwithoutStock", objBillSetting.getBillwithoutStock());
        cvDbValues.put("Tax", objBillSetting.getTax());
        cvDbValues.put("TaxType", objBillSetting.getTaxType());
        cvDbValues.put(KEY_DiscountType, objBillSetting.getDiscountType());
        cvDbValues.put("KOT", objBillSetting.getKOT());
        cvDbValues.put("Token", objBillSetting.getToken());
        cvDbValues.put("Kitchen", objBillSetting.getKitchen());
        cvDbValues.put("OtherChargesItemwise", objBillSetting.getOtherChargesItemwise());
        cvDbValues.put("OtherChargesBillwise", objBillSetting.getOtherChargesBillwise());
        cvDbValues.put("Peripherals", objBillSetting.getPeripherals());
        cvDbValues.put("RestoreDefault", objBillSetting.getRestoreDefault());
        cvDbValues.put(KEY_FastBillingMode, objBillSetting.getFastBillingMode());
        cvDbValues.put("ItemNoReset", objBillSetting.getItemNoReset());
        cvDbValues.put("PrintPreview", objBillSetting.getPrintPreview());
        cvDbValues.put("TableSpliting", objBillSetting.getTableSpliting());
        cvDbValues.put(KEY_CummulativeHeadingEnable, objBillSetting.getCummulativeHeadingEnable()); // richa_2012

        return dbFNB.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    public int updateGSTSettings(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_GSTIN, objBillSetting.getGSTIN());
        cvDbValues.put(KEY_POS, objBillSetting.getPOS());
        cvDbValues.put(KEY_HSNCode, objBillSetting.getHSNCode());
        cvDbValues.put(KEY_ReverseCharge, objBillSetting.getReverseCharge());
        cvDbValues.put(KEY_GSTIN_OUT, objBillSetting.getGSTIN_out());
        cvDbValues.put(KEY_POS_OUT, objBillSetting.getPOS_out());
        cvDbValues.put(KEY_HSNCode_OUT, objBillSetting.getHSNCode_out());
        cvDbValues.put(KEY_ReverseCharge_OUT, objBillSetting.getReverseCharge_out());
        cvDbValues.put(KEY_GSTEnable, objBillSetting.getGSTEnable());
        return dbFNB.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    // Temp functions - Other settings update
    public int updateDateAndTime(BillSetting objBillSetting) {
        cvDbValues = new ContentValues();
        cvDbValues.put("DateAndTime", objBillSetting.getDateAndTime());
        return dbFNB.update(TBL_BILLSETTING, cvDbValues, null, null);
    }

    /************************************************************************************************************************************/
    /*****************************************************
     * Table - MachineSetting
     *******************************************************/
    /************************************************************************************************************************************/
    // -----Insert Machine Setting-----
    public long addMachineSetting(MachineSetting objMachineSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("BaudRate", objMachineSetting.getBaudRate());
        cvDbValues.put("DataBits", objMachineSetting.getDataBits());
        cvDbValues.put("Parity", objMachineSetting.getParity());
        cvDbValues.put("PortName", objMachineSetting.getPortName());
        cvDbValues.put("StopBits", objMachineSetting.getStopBits());

        return dbFNB.insert(TBL_MACHINESETTING, null, cvDbValues);
    }

    // -----Retrieve Machine setting-----
    public Cursor getMachineSetting() {
        return dbFNB.query(TBL_MACHINESETTING,
                new String[]{"BaudRate", "DataBits", "Parity", "PortName", "StopBits"}, null, null, null, null,
                null);
    }

    // -----Update Machine Setting-----
    public int updateMachineSetting(MachineSetting objMachineSetting) {
        cvDbValues = new ContentValues();

        cvDbValues.put("BaudRate", objMachineSetting.getBaudRate());
        cvDbValues.put("DataBits", objMachineSetting.getDataBits());
        cvDbValues.put("Parity", objMachineSetting.getParity());
        cvDbValues.put("PortName", objMachineSetting.getPortName());
        cvDbValues.put("StopBits", objMachineSetting.getStopBits());

        return dbFNB.update(TBL_MACHINESETTING, cvDbValues, null, null);
    }

    /************************************************************************************************************************************/
    /*********************************************************
     * Table - User
     *************************************************************/
    /************************************************************************************************************************************/
    // -----Insert Users-----
    /*public long addUser(User objUser) {
        cvDbValues = new ContentValues();

        cvDbValues.put("AccessLevel", objUser.getAccessLevel());
        cvDbValues.put("Password", objUser.getPassword());
        cvDbValues.put("UserId", objUser.getUserId());
        cvDbValues.put("UserName", objUser.getUserName());

        return dbFNB.insert(TBL_USER, null, cvDbValues);
    }*/

    // -----Retrieve All Users-----
    public Cursor getAllUser() {
        return dbFNB.query(TBL_USER, new String[]{"*"}, null, null, null, null, null);
    }

    // -----Retrieve single User-----
    /*public Cursor getUser(String UserId) {
        return dbFNB.query(TBL_USERS, new String[]{"*"}, "UserId='" + UserId + "'", null, null, null, null);
        *//*
         * return dbFNB.query(TBL_USER, new String[] {"*"}, "UserId LIKE'" +
		 * UserId + "%'", null, null, null, null);
		 *//*
    }*/

    // -----Retrieve single User-----
    /*public Cursor getUser(String UserId, String Password) {
        Cursor cursor = null;
        try {
            cursor = dbFNB.query(TBL_USER, new String[]{"*"}, "UserId='" + UserId + "' AND Password='" + Password + "'",
                    null, null, null, null);

        }
        catch (Exception e)
        {
            Log.d("getUser() ",e+"");
        }return cursor;
    }*/

    // -----Update User table-----
   /* public int updateUser(User objUser) {
        cvDbValues = new ContentValues();

        cvDbValues.put("AccessLevel", objUser.getAccessLevel());
        cvDbValues.put("Password", objUser.getPassword());
        cvDbValues.put("UserName", objUser.getUserName());

        return dbFNB.update(TBL_USER, cvDbValues, "UserId='" + objUser.getUserId() + "'", null);
    }*/

    /************************************************************************************************************************************/
    /******************************************************
     * Table - Department
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Department-----
    public long addDepartment(Department objDept) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DeptCode", objDept.getDeptCode());
        cvDbValues.put("DeptName", objDept.getDeptName());

        return dbFNB.insert(TBL_DEPARTMENT, null, cvDbValues);
    }

    // -----Retrieve all Departments-----
    public Cursor getAllDepartments() {
        return dbFNB.query(TBL_DEPARTMENT, new String[]{"DeptCode", "DeptName"}, null, null, null, null, null);
    }

    public Cursor getDepartments() {
        return dbFNB.rawQuery("Select DeptCode as _id, DeptName from Department", null);
    }

    public int getDepartmentIdByName(String name) {
        int id = -1;
        try {
            Cursor cursor = dbFNB.query(TBL_DEPARTMENT, null, KEY_DeptName + "=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(KEY_DeptCode)));
            } else {
                id = 0;
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }

        // return contact
        return id;
    }

    // -----Retrieve single Department-----
    public Cursor getDepartment(int DeptCode) {
        try {
            return dbFNB.query(TBL_DEPARTMENT, new String[]{"DeptCode", "DeptName"}, "DeptCode=" + DeptCode, null, null, null, null);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
            return null;
        }
    }

    // -----Retrieve single Department-----
    public Cursor getDepartment(String DeptName) {
        return dbFNB.query(TBL_DEPARTMENT, new String[]{"DeptCode", "DeptName"}, "DeptName=" + DeptName, null, null,
                null, null);
    }

    // -----Retrieve highest DeptCode from table-----
    public int getDeptCode() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(DeptCode) FROM " + TBL_DEPARTMENT, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Department table----
    public int updateDepartment(String strDeptCode, String strDeptName) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DeptName", strDeptName);

        return dbFNB.update(TBL_DEPARTMENT, cvDbValues, "DeptCode=" + strDeptCode, null);
    }

    // -----Delete Department from Dept Table-----
    public int DeleteDept(String DeptCode) {

        return dbFNB.delete(TBL_DEPARTMENT, "DeptCode=" + DeptCode, null);
    }

    /************************************************************************************************************************************/
    /*******************************************************
     * Table - Category
     ***********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Category-----
    public long addCategory(Category objCateg) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CategCode", objCateg.getCategCode());
        cvDbValues.put("CategName", objCateg.getCategName());
        cvDbValues.put("DeptCode", objCateg.getDeptCode());

        return dbFNB.insert(TBL_CATEGORY, null, cvDbValues);
    }

    // -----Retrieve all Category-----
    public Cursor getAllCategory() {
        return dbFNB.query(TBL_CATEGORY, new String[]{"CategCode", "CategName", "DeptCode"}, null, null, null, null,
                null);
    }

    // -----Retrieve all Category-----
    public Cursor getAllCategorywithDeptName() {
        return dbFNB.rawQuery("Select * from " + TBL_CATEGORY + ", " + TBL_DEPARTMENT + " where " + TBL_DEPARTMENT
                + ".DeptCode = " + TBL_CATEGORY + ".DeptCode", null);
    }

    public Cursor getCategories() {
        return dbFNB.rawQuery("Select CategCode as _id, CategName, DeptCode from Category", null);
    }

    public Cursor getCategoryByDept(int deptcode) {
        return dbFNB.rawQuery("Select CategCode, CategName, DeptCode from Category where DeptCode=" + deptcode, null);
    }

    public int getCategoryIdByName(String name) {
        int id = -1;
        try {
            Cursor cursor = dbFNB.query(TBL_CATEGORY, null, "CategName=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CategCode")));
            } else {
                id = 0;
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        // return contact
        return id;
    }

    public ArrayList<String> getCategoryNameByDeptCode(String name) {
        String categname = "";
        ArrayList<String> list = new ArrayList<String>();
        try {
            Cursor cursor = dbFNB.query(TBL_CATEGORY, null, "DeptCode=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            list.add("Select department first");
            while (cursor != null && cursor.moveToNext()) {
                list.add(cursor.getString(cursor.getColumnIndex("CategName")));
            }
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        // return contact
        return list;
    }

    public List<String> getAllCategforDept() {
        List<String> list = new ArrayList<String>();

        Cursor cursor = dbFNB.rawQuery("SELECT  CategCode as _id, CategName, DeptCode FROM Category", null);// selectQuery,selectedArguments

        list.add("Select");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));// adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection

        // returning lables
        return list;
    }

    // -----Retrieve single Category-----
    public Cursor getCategory(int CategCode) {
        return dbFNB.query(TBL_CATEGORY, new String[]{"CategCode", "CategName"}, "CategCode=" + CategCode, null,
                null, null, null);
    }

    // -----Retrieve highest CategCode from table-----
    public int getCategCode() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(CategCode) FROM " + TBL_CATEGORY, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Category table----
    public int updateCategory(String strCategCode, String strCategName, int iDeptCode) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CategName", strCategName);
        cvDbValues.put("DeptCode", iDeptCode);

        return dbFNB.update(TBL_CATEGORY, cvDbValues, "CategCode=" + strCategCode, null);
    }

    public List<String> getAllDeptforCateg() {
        List<String> list = new ArrayList<String>();

        Cursor cursor = dbFNB.rawQuery("SELECT  DeptCode as _id, Deptname FROM Department", null);// selectQuery,selectedArguments

        list.add("Select");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));// adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection

        // returning lables
        return list;
    }

    // -----Delete Category
    public int DeleteCateg(String CategCode) {

        return dbFNB.delete(TBL_CATEGORY, "CategCode=" + CategCode, null);
    }

    // -----Delete Category by Dept Code
    public int DeleteCategByDeptCode(String DeptCode) {

        return dbFNB.delete(TBL_CATEGORY, "DeptCode=" + DeptCode, null);
    }

    /************************************************************************************************************************************/
    /********************************************************
     * Table - Kitchen
     ***********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Kitchen-----
    public long addKitchen(Kitchen objKitchen) {
        cvDbValues = new ContentValues();

        cvDbValues.put("KitchenCode", objKitchen.getKitchenCode());
        cvDbValues.put("KitchenName", objKitchen.getKitchenName());

        return dbFNB.insert(TBL_KITCHEN, null, cvDbValues);
    }

    // -----Retrieve all Kitchen-----
    public Cursor getAllKitchen() {
        return dbFNB.query(TBL_KITCHEN, new String[]{"KitchenCode", "KitchenName"}, null, null, null, null, null);
    }

    // -----Retrieve single Kitchen-----
    public Cursor getKitchen(int KitchenCode) {
        return dbFNB.query(TBL_KITCHEN, new String[]{"KitchenCode", "KitchenName"}, "KitchenCode=" + KitchenCode,
                null, null, null, null);
    }

    // -----Retrieve Kitchen name-----
    public String getKitchenName(int KitchenCode) {
        Cursor result;
        result = dbFNB.query(TBL_KITCHEN, new String[]{"KitchenName"}, "KitchenCode=" + KitchenCode, null, null,
                null, null);

        if (result.moveToFirst()) {
            return result.getString(0);
        } else {
            return "No Name";
        }
    }

    // -----Retrieve highest KitchenCode from table-----
    public int getKitchenCode() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(KitchenCode) FROM " + TBL_KITCHEN, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Kitchen table----
    public int updateKitchen(String strKitchenCode, String strKitchenName) {
        cvDbValues = new ContentValues();

        cvDbValues.put("KitchenName", strKitchenName);

        return dbFNB.update(TBL_KITCHEN, cvDbValues, "KitchenCode=" + strKitchenCode, null);
    }

    // -----Delete Kitchen
    public int DeleteKitchen(String KitchenCode) {

        return dbFNB.delete(TBL_KITCHEN, "KitchenCode=" + KitchenCode, null);
    }

    /************************************************************************************************************************************/
    /*****************************************************
     * Table - Description
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Description-----
    public long addDescription(Description objDescription) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DescriptionId", objDescription.getDescriptionId());
        cvDbValues.put("DescriptionText", objDescription.getDescriptionText());

        return dbFNB.insert(TBL_DESCRIPTION, null, cvDbValues);
    }

    // -----Retrieve all Description-----
    public Cursor getAllDescription() {
        return dbFNB.query(TBL_DESCRIPTION, new String[]{"DescriptionId", "DescriptionText"}, null, null, null, null,
                null);
    }

    // -----Retrieve single Description-----
    public Cursor getDescription(int DescriptionId) {
        return dbFNB.query(TBL_DESCRIPTION, new String[]{"DescriptionId", "DescriptionText"},
                "DescriptionId=" + DescriptionId, null, null, null, null);
    }

    // -----Retrieve highest DescriptionId from table-----
    public int getDescriptionId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(DescriptionId) FROM " + TBL_DESCRIPTION, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Description table----
    public int updateDescription(String strDescriptionId, String strDescriptionText) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DescriptionText", strDescriptionText);

        return dbFNB.update(TBL_DESCRIPTION, cvDbValues, "DescriptionId=" + strDescriptionId, null);
    }

    // -----Delete Payment Descriptiom
    public int DeleteDescription(String DescriptionId) {
        return dbFNB.delete(TBL_DESCRIPTION, "DescriptionId=" + DescriptionId, null);
    }

    /************************************************************************************************************************************/
    /******************************************************
     * Table - taxConfig
     ***********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Tax configuration-----
    public long addTaxConfig(TaxConfig objTax) {
        cvDbValues = new ContentValues();

        cvDbValues.put("TaxDescription", objTax.getTaxDescription());
        cvDbValues.put("TaxId", objTax.getTaxId());
        cvDbValues.put("TaxPercentage", objTax.getTaxPercentage());
        cvDbValues.put("TotalPercentage", objTax.getTotalPercentage());

        return dbFNB.insert(TBL_TAXCONFIG, null, cvDbValues);
    }

    // -----Retrieve all TaxConfig-----
    public Cursor getAllTaxConfig() {
        return dbFNB.query(TBL_TAXCONFIG, new String[]{"TaxId", "TaxDescription", "TaxPercentage", "TotalPercentage"}, null, null, null,
                null, null);
    }

    // -----Retrieve single TaxConfig-----
    public Cursor getTaxConfig(int TaxId) {
        return dbFNB.query(TBL_TAXCONFIG, new String[]{"TaxId", "TaxDescription", "TaxPercentage", "TotalPercentage"}, "TaxId=" + TaxId,
                null, null, null, null);
    }

    // -----Retrieve single TaxConfig-----
    public Cursor getTaxConfig(double TaxPercent) {
        return dbFNB.query(TBL_TAXCONFIG, new String[]{"TaxId", "TaxDescription", "TaxPercentage", "TotalPercentage"},
                "TaxPercentage=" + TaxPercent, null, null, null, null);
    }

    // -----Retrieve highest TaxId from table-----
    public int getTaxId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(TaxId) FROM " + TBL_TAXCONFIG, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update TaxConfig table----
    public int updateTaxConfig(String strTaxId, String strTaxDescription, String strTaxPercent) {
        cvDbValues = new ContentValues();

        cvDbValues.put("TaxDescription", strTaxDescription);
        cvDbValues.put("TaxPercentage", strTaxPercent);
//        cvDbValues.put("TotalPercentage", strTotalPercent);

        return dbFNB.update(TBL_TAXCONFIG, cvDbValues, "TaxId=" + strTaxId, null);
    }

    // -----Update TaxConfig table----
    public int updateTaxConfig(String strTaxId, String strTaxDescription, String strTaxPercent, String strTotalPercent) {
        cvDbValues = new ContentValues();

        cvDbValues.put("TaxDescription", strTaxDescription);
        cvDbValues.put("TaxPercentage", strTaxPercent);
        cvDbValues.put("TotalPercentage", strTotalPercent);

        return dbFNB.update(TBL_TAXCONFIG, cvDbValues, "TaxId=" + strTaxId, null);
    }

    // -----Update TaxConfig table----
    public int updateTaxConfig(String strTaxId, String strTotalPercent) {
        cvDbValues = new ContentValues();

        cvDbValues.put("TotalPercentage", strTotalPercent);

        return dbFNB.update(TBL_TAXCONFIG, cvDbValues, "TaxId=" + strTaxId, null);
    }

    // -----Delete Tax
    public int DeleteTax(String TaxId) {
        return dbFNB.delete(TBL_TAXCONFIG, "TaxId=" + TaxId, null);
    }


    // -----Insert SubTax configuration-----
    public long addSubTaxConfig(TaxConfigSub objTaxSub) {
        cvDbValues = new ContentValues();

        cvDbValues.put("SubTaxDescription", objTaxSub.getSubTaxDescription());
        cvDbValues.put("SubTaxId", objTaxSub.getSubTaxId());
        cvDbValues.put("SubTaxPercent", objTaxSub.getSubTaxPercentage());
        cvDbValues.put("TaxId", objTaxSub.getTaxId());

        return dbFNB.insert(TBL_SUBTAXCONFIG, null, cvDbValues);
    }

    // -----Retrieve all SubTaxConfig-----
    public Cursor getAllSubTaxConfig() {
        return dbFNB.query(TBL_SUBTAXCONFIG, new String[]{"SubTaxId", "SubTaxDescription", "SubTaxPercent"}, null, null, null,
                null, null);
    }

    // -----Retrieve all SubTaxConfig-----
    public Cursor getAllSubTaxConfig(String TaxId) {
        return dbFNB.query(TBL_SUBTAXCONFIG, new String[]{"SubTaxId", "SubTaxDescription", "SubTaxPercent"}, "TaxId=" + TaxId, null, null,
                null, null);
    }

    // -----Retrieve all Category-----
    public Cursor getAllSubTaxwithTaxName(int TaxId) {
        return dbFNB.rawQuery("Select * from " + TBL_SUBTAXCONFIG + ", " + TBL_TAXCONFIG + " where " + TBL_TAXCONFIG
                + ".TaxId = " + TBL_SUBTAXCONFIG + ".TaxId AND " + TBL_SUBTAXCONFIG + ".TaxId=" + TaxId, null);
    }

    // -----Retrieve highest SubTaxId from table-----
    public int getSubTaxId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(SubTaxId) FROM " + TBL_SUBTAXCONFIG, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update SubTaxConfig table----
    public int updateSubTaxConfig(String strSubTaxId, String strSubTaxDescription, String strSubTaxPercent, String TaxId) {
        cvDbValues = new ContentValues();

        cvDbValues.put("SubTaxDescription", strSubTaxDescription);
        cvDbValues.put("SubTaxPercent", strSubTaxPercent);
        cvDbValues.put("TaxId", TaxId);

        return dbFNB.update(TBL_SUBTAXCONFIG, cvDbValues, "SubTaxId=" + strSubTaxId, null);
    }

    public List<String> getAllSpnrTaxConfig() {
        List<String> list = new ArrayList<String>();

        Cursor cursor = dbFNB.rawQuery("SELECT  * FROM " + TBL_TAXCONFIG, null);// selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("TaxDescription")));// adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection

        // returning lables
        return list;
    }

    // -----Delete Tax
    public int DeleteSubTax(String SubTaxId) {
        return dbFNB.delete(TBL_SUBTAXCONFIG, "SubTaxId=" + SubTaxId, null);
    }

    // -----Delete Tax by Tax Id
    public int DeleteSubTaxByTaxId(String TaxId) {
        return dbFNB.delete(TBL_SUBTAXCONFIG, "TaxId=" + TaxId, null);
    }


    /************************************************************************************************************************************/
    /****************************************************
     * Table - DiscountConfig
     ********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Discount configuration-----
    public long addDiscountConfig(DiscountConfig objDiscount) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DiscDescription", objDiscount.getDiscDescription());
        cvDbValues.put("DiscId", objDiscount.getDiscId());
        cvDbValues.put("DiscPercentage", objDiscount.getDiscPercentage());
        cvDbValues.put("DiscAmount", objDiscount.getDiscAmount());

        return dbFNB.insert(TBL_DISCOUNTCONFIG, null, cvDbValues);
    }

    // -----Retrieve all DiscountConfig-----
    public Cursor getAllDiscountConfig() {
        return dbFNB.query(TBL_DISCOUNTCONFIG, new String[]{"DiscId", "DiscDescription", "DiscPercentage", "DiscAmount"}, null,
                null, null, null, null);
    }

    // -----Retrieve single DiscountConfig-----
    public Cursor getDiscountConfig(int DiscId) {
        return dbFNB.query(TBL_DISCOUNTCONFIG, new String[]{"DiscId", "DiscDescription", "DiscPercentage", "DiscAmount"},
                "DiscId=" + DiscId, null, null, null, null);
    }

    // -----Retrieve single DiscountConfig-----
    public Cursor getDiscountValue(String DiscName) {
        return dbFNB.query(TBL_DISCOUNTCONFIG, new String[]{"DiscId", "DiscDescription", "DiscPercentage", "DiscAmount"},
                "DiscDescription='" + DiscName + "'", null, null, null, null);
    }

    // -----Retrieve highest DiscId from table-----
    public int getDiscountId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(DiscId) FROM " + TBL_DISCOUNTCONFIG, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update DiscountConfig table----
    public int updateDiscountConfig(String strDiscId, String strDiscDescription, String strDiscPercent, String strDiscAmt) {
        cvDbValues = new ContentValues();

        cvDbValues.put("DiscDescription", strDiscDescription);
        cvDbValues.put("DiscPercentage", strDiscPercent);
        cvDbValues.put("DiscAmount", strDiscAmt);
        return dbFNB.update(TBL_DISCOUNTCONFIG, cvDbValues, "DiscId=" + strDiscId, null);
    }

    // -----Delete Discount
    public int DeleteDiscount(String DiscId) {
        return dbFNB.delete(TBL_DISCOUNTCONFIG, "DiscId=" + DiscId, null);
    }

    /************************************************************************************************************************************/
    /********************************************************
     * Table - Coupon
     ************************************************************/
    /************************************************************************************************************************************/
    // -----Insert Coupon configuration-----
    public long addCoupon(Coupon objCoupon) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CouponId", objCoupon.getCouponId());
        cvDbValues.put("CouponDescription", objCoupon.getCouponDescription());
        cvDbValues.put("CouponBarcode", objCoupon.getCouponBarcode());
        cvDbValues.put("CouponAmount", objCoupon.getCouponAmount());

        return dbFNB.insert(TBL_COUPON, null, cvDbValues);
    }

    // -----Retrieve all Coupon-----
    public Cursor getAllCoupon() {
        return dbFNB.query(TBL_COUPON,
                new String[]{"CouponId", "CouponDescription", "CouponBarcode", "CouponAmount"}, null, null, null,
                null, null);
    }

    // -----Retrieve single Coupon based on CouponId-----
    public Cursor getCoupon(int CouponId) {
        return dbFNB.query(TBL_COUPON,
                new String[]{"CouponId", "CouponDescription", "CouponBarcode", "CouponAmount"},
                "CouponId=" + CouponId, null, null, null, null);
    }

    // -----Retrieve single CouponConfig-----
    public Cursor getCouponValue(String CouponName) {
        return dbFNB.query(TBL_COUPON, new String[]{"CouponId", "CouponDescription", "CouponBarcode", "CouponAmount"},
                "CouponDescription='" + CouponName + "'", null, null, null, null);
    }

    // -----Retrieve single Coupon based on Barcode-----
    public Cursor getCoupon(String CouponBarcode) {
        return dbFNB.query(TBL_COUPON,
                new String[]{"CouponId", "CouponDescription", "CouponBarcode", "CouponAmount"},
                "CouponBarcode=" + CouponBarcode, null, null, null, null);
    }

    // -----Retrieve highest CouponId from table-----
    public int getCouponId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(CouponId) FROM " + TBL_COUPON, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Coupon table----
    public int updateCoupon(String strCouponId, String strCouponDescription, String strCouponBarcode,
                            String strCouponAmount) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CouponDescription", strCouponDescription);
        cvDbValues.put("CouponBarcode", strCouponBarcode);
        cvDbValues.put("CouponAmount", strCouponAmount);

        return dbFNB.update(TBL_COUPON, cvDbValues, "CouponId=" + strCouponId, null);
    }

    // -----Delete Coupon
    public int DeleteCoupon(String CouponId) {
        return dbFNB.delete(TBL_COUPON, "CouponId=" + CouponId, null);
    }


    /************************************************************************************************************************************/
    /****************************************************
     * Table - VoucherConfig
     ********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Voucher configuration-----
    public long addVoucherConfig(VoucherConfig objVoucher) {
        cvDbValues = new ContentValues();

        cvDbValues.put("VoucherDescription", objVoucher.getVoucherDescription());
        cvDbValues.put("VoucherId", objVoucher.getVoucherId());
        cvDbValues.put("VoucherPercentage", objVoucher.getVoucherPercentage());

        return dbFNB.insert(TBL_VOUCHERCONFIG, null, cvDbValues);
    }

    // -----Retrieve all VoucherConfig-----
    public Cursor getAllVoucherConfig() {
        return dbFNB.query(TBL_VOUCHERCONFIG, new String[]{"VoucherId", "VoucherDescription", "VoucherPercentage"}, null,
                null, null, null, null);
    }

    // -----Retrieve single VoucherConfig-----
    public Cursor getVoucherConfig(int VoucherId) {
        return dbFNB.query(TBL_VOUCHERCONFIG, new String[]{"VoucherId", "VoucherDescription", "VoucherPercentage"},
                "VoucherId=" + VoucherId, null, null, null, null);
    }

    // -----Retrieve single VoucherConfig-----
    public Cursor getVoucherValue(String VoucherName) {
        return dbFNB.query(TBL_VOUCHERCONFIG, new String[]{"VoucherId", "VoucherDescription", "VoucherPercentage"},
                "VoucherDescription='" + VoucherName + "'", null, null, null, null);
    }

    // -----Retrieve highest VoucherId from table-----
    public int getVoucherId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(VoucherId) FROM " + TBL_VOUCHERCONFIG, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update VoucherConfig table----
    public int updateVoucherConfig(String strVoucherId, String strVoucherDescription, String strVoucherPercent) {
        cvDbValues = new ContentValues();

        cvDbValues.put("VoucherDescription", strVoucherDescription);
        cvDbValues.put("VoucherPercentage", strVoucherPercent);

        return dbFNB.update(TBL_VOUCHERCONFIG, cvDbValues, "VoucherId=" + strVoucherId, null);
    }

    // -----Delete Voucher
    public int DeleteVoucher(String VoucherId) {
        return dbFNB.delete(TBL_VOUCHERCONFIG, "VoucherId=" + VoucherId, null);
    }


    /************************************************************************************************************************************/
    /******************************************************
     * Table - KOTModifier
     *********************************************************/
    /************************************************************************************************************************************/
    // -----Insert KOT modifier-----
    public long addKOTModifier(KOTModifier objKOTModifier) {
        cvDbValues = new ContentValues();

        cvDbValues.put("ModifierId", objKOTModifier.getModifierId());
        cvDbValues.put("ModifierDescription", objKOTModifier.getModifierDescription());
        cvDbValues.put("ModifierAmount", objKOTModifier.getModifierAmount());
        cvDbValues.put("IsChargeable", objKOTModifier.getIsChargeable());
        cvDbValues.put("ModifierModes", objKOTModifier.getModifierModes());

        return dbFNB.insert(TBL_KOTMODIFIER, null, cvDbValues);
    }

    // -----Retrieve all KOTModifier-----
    public Cursor getAllKOTModifier() {
        return dbFNB.query(TBL_KOTMODIFIER,
                new String[]{"ModifierId", "ModifierDescription", "ModifierAmount", "IsChargeable", "ModifierModes"}, null, null,
                null, null, null);
    }

    // -----Retrieve KOTModifier by Modes-----
    public Cursor getKOTModifierByModes(String strModes) {
        return dbFNB.query(TBL_KOTMODIFIER,
                new String[]{"ModifierId", "ModifierDescription", "ModifierAmount", "IsChargeable", "ModifierModes"}, "IsChargeable = '1' AND ModifierModes='" + strModes + "'", null,
                null, null, null);
    }

    // -----Retrieve single KOTModifier-----
    public Cursor getKOTModifier(int ModifierId) {
        return dbFNB.query(TBL_KOTMODIFIER, new String[]{"*"}, "ModifierId=" + ModifierId, null, null, null, null);
    }

    // -----Retrieve highest KOTModifierId from table-----
    public int getKOTModifierId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(ModifierId) FROM " + TBL_KOTMODIFIER, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update KOTModifier table----
    public int updateKOTModifier(String strModifierId, String strModifierDescription, String strModifierAmount,
                                 String strIsChargeable, String strModes) {
        cvDbValues = new ContentValues();

        cvDbValues.put("ModifierDescription", strModifierDescription);
        cvDbValues.put("ModifierAmount", strModifierAmount);
        cvDbValues.put("IsChargeable", strIsChargeable);
        cvDbValues.put("ModifierModes", strModes);

        return dbFNB.update(TBL_KOTMODIFIER, cvDbValues, "ModifierId=" + strModifierId, null);
    }

    // -----Delete Other Charges
    public int DeleteOtherCharges(String ModifierId) {
        return dbFNB.delete(TBL_KOTMODIFIER, "ModifierId=" + ModifierId, null);
    }

    /************************************************************************************************************************************/
    /****************************************************
     * Table - PaymentReceipt
     ********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Payment / Receipt-----
    public long addPaymentReceipt(PaymentReceipt objPaymentReceipt) {
        cvDbValues = new ContentValues();

        cvDbValues.put("Reason", objPaymentReceipt.getReason());
        cvDbValues.put("Amount", objPaymentReceipt.getAmount());
        cvDbValues.put("BillType", objPaymentReceipt.getBillType());
        cvDbValues.put("InvoiceDate", objPaymentReceipt.getDate());
        cvDbValues.put("DescriptionId1", objPaymentReceipt.getDescriptionId1());
        cvDbValues.put("DescriptionId2", objPaymentReceipt.getDescriptionId2());
        cvDbValues.put("DescriptionId3", objPaymentReceipt.getDescriptionId3());
        cvDbValues.put(KEY_DescriptionText, objPaymentReceipt.getDescriptionText());

        return dbFNB.insert(TBL_PAYMENTRECEIPT, null, cvDbValues);
    }

    // ----- Retrieve all Payment Receipt Bills-----
    public Cursor getAllPaymentReceipt() {
        return dbFNB.query(TBL_PAYMENTRECEIPT, new String[]{"_Id", "Reason", "Amount", "BillType", "InvoiceDate",
                "DescriptionId1", "DescriptionId2", "DescriptionId3"}, null, null, null, null, null);
    }

    /************************************************************************************************************************************/
    /*******************************************************
     * Table - Customer
     ***********************************************************/
    /************************************************************************************************************************************/
    // -----Retrieve Customer ID-----
    public int getCustomerId() {
        Cursor result = dbFNB.rawQuery("SELECT MAX(CustId) FROM" + TBL_PENDINGKOT, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 1;
        }
    }

    public String getTableNumberByEmpId(int empId) {
        LinkedHashSet<String> list = new LinkedHashSet<String>();
        SQLiteDatabase db = getReadableDatabase();
        StringBuffer tableNo = new StringBuffer();
        try {
            Cursor cursor = db.query(TBL_PENDINGKOT, null, KEY_EmployeeId + "=?", new String[]{String.valueOf(empId)}, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int in = cursor.getInt(cursor.getColumnIndex(KEY_TableNumber));
                    list.add(in + "");
                    //tableNo.append(in+",");
                }
            }
        } catch (Exception e) {
            tableNo = new StringBuffer();
            list = new LinkedHashSet<String>();
            Log.d(TAG, e.toString());
            //Log.d(TAG,e.toString());
        } finally {
            db.close();
        }
        return list.toString();
    }

    // -----Insert Customer-----
    public long addCustomer(Customer objCustomer) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CustName", objCustomer.getCustName());
        cvDbValues.put("LastTransaction", objCustomer.getLastTransaction());
        cvDbValues.put("TotalTransaction", objCustomer.getTotalTransaction());
        cvDbValues.put("CustContactNumber", objCustomer.getCustContactNumber());
        cvDbValues.put("CustAddress", objCustomer.getCustAddress());
        cvDbValues.put("CreditAmount", objCustomer.getCreditAmount());
        cvDbValues.put(KEY_GSTIN, objCustomer.getStrCustGSTIN());

        return dbFNB.insert(TBL_CUSTOMER, null, cvDbValues);
    }

    // -----Retrieve All customers-----
    public Cursor getAllCustomer() {
        return dbFNB.query(TBL_CUSTOMER, new String[]{"*"}, null, null, null, null, null);
    }

    // -----Retrieve single Customer-----
    public Cursor getCustomer(int iCustId) {
        return dbFNB.query(TBL_CUSTOMER, new String[]{"*"}, "CustId=" + iCustId, null, null, null, null);
    }

    // -----Retrieve single Customer-----
    public Cursor getCustomer(String strCustPhone) {
        return dbFNB.query(TBL_CUSTOMER, new String[]{"*"}, "CustContactNumber='" + strCustPhone + "'", null, null,
                null, null);
    }

    public Cursor getCustomerList(String Name) {
        return dbFNB.rawQuery("SELECT * FROM " + TBL_CUSTOMER + " WHERE CustName LIKE '" + Name + "%'", null);
    }

    public float getCustomerTotalTransaction(int iCustId) {
        SQLiteDatabase db = getWritableDatabase();
        float result = 0;
        try {
            Cursor cursor = db.query(TBL_CUSTOMER, new String[]{"TotalTransaction"}, "CustId=" + iCustId, null, null,
                    null, null);

            if (cursor.moveToFirst()) {
                result = cursor.getFloat(0);
            } else {
                result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    public List<String> getAllCustomerName() {
        List<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_CUSTOMER;
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);// selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("CustName")));
            } while (cursor.moveToNext());
        }
        // returning lables
        return list;
    }

    public float getCustomerCreditAmount(int iCustId) {
        SQLiteDatabase db = getWritableDatabase();
        float result = 0;
        try {
            Cursor cursor = db.query(TBL_CUSTOMER, new String[]{"CreditAmount"}, "CustId=" + iCustId, null, null,
                    null, null);

            if (cursor.moveToFirst()) {
                result = cursor.getFloat(0);
            } else {
                result = 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    // -----Update Customer table-----
    public int updateCustomer(String strCustAddress, String strCustContactNumber, String strCustName, int iCustId,
                              float fLastTransaction, float fTotalTransaction, float fCreditAmount, String gstin) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CustAddress", strCustAddress);
        cvDbValues.put("CustContactNumber", strCustContactNumber);
        cvDbValues.put("CustName", strCustName);
        cvDbValues.put("LastTransaction", fLastTransaction);
        cvDbValues.put("TotalTransaction", fTotalTransaction);
        cvDbValues.put("CreditAmount", fCreditAmount);
        cvDbValues.put(KEY_GSTIN, gstin);

        return dbFNB.update(TBL_CUSTOMER, cvDbValues, "CustId=" + iCustId, null);
    }

    // -----Update Customer table-----
    public int updateCustomerTransaction(int iCustId, float fLastTransaction, float fTotalTransaction, float fCreditAmount) {
        SQLiteDatabase db = getWritableDatabase();
        int result = 0;
        try {
            cvDbValues = new ContentValues();

            cvDbValues.put("LastTransaction", fLastTransaction);
            cvDbValues.put("TotalTransaction", fTotalTransaction);
            cvDbValues.put("CreditAmount", fCreditAmount);

            result = db.update(TBL_CUSTOMER, cvDbValues, "CustId=" + iCustId, null);
        } catch (Exception e) {
            e.printStackTrace();
            result = 0;
        }
        return result;
    }

    // ----- Delete Customer --------
    public int DeleteCustomer(int CustId) {

        return dbFNB.delete(TBL_CUSTOMER, "CustId=" + CustId, null);
    }

    /************************************************************************************************************************************/
    /*******************************************************
     * Table - Employee
     ***********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Employee-----
    public long addEmployee(Employee objEmployee) {
        cvDbValues = new ContentValues();

        cvDbValues.put("EmployeeContactNumber", objEmployee.getEmployeeContactNumber());
        cvDbValues.put("EmployeeName", objEmployee.getEmployeeName());
        cvDbValues.put("EmployeeRole", objEmployee.getEmployeeRole());

        return dbFNB.insert(TBL_EMPLOYEE, null, cvDbValues);
    }

    // -----Retrieve All Waiters-----
    public Cursor getAllEmployee() {
        return dbFNB.query(TBL_EMPLOYEE, new String[]{"*"}, null, null, null, null, null);
    }

    // -----Retrieve All Delivery riders-----
    public Cursor getAllDeliveryRiders() {
//        return dbFNB.query(TBL_EMPLOYEE, new String[]{"*"}, "EmployeeRole=2 OR EmployeeRole=3", null, null, null,
//                null);
        return dbFNB.query(TBL_USERS, new String[]{"*"}, "RoleId=4", null, null, null, null);
    }

    // -----Retrieve All Employees-----
    public Cursor getAllWaiters() {
//        return dbFNB.query(TBL_EMPLOYEE, new String[]{"*"}, "EmployeeRole=1 OR EmployeeRole=3", null, null, null,
//                null);
        return dbFNB.query(TBL_USERS, new String[]{"*"}, "RoleId=3", null, null, null,
                null);
    }

    // -----Retrieve single Employee-----
    public Cursor getEmployee(int iEmployeeId) {
        return dbFNB.query(TBL_EMPLOYEE,
                new String[]{"EmployeeContactNumber", "EmployeeId", "EmployeeName", "EmployeeRole"},
                "EmployeeId=" + iEmployeeId, null, null, null, null);
    }

    // -----Update Employee table-----
    public int updateEmployee(String strEmployeeContactNumber, String strEmployeeName, int iEmployeeId,
                              int iEmployeeRole) {
        cvDbValues = new ContentValues();

        cvDbValues.put("EmployeeContactNumber", strEmployeeContactNumber);
        cvDbValues.put("EmployeeName", strEmployeeName);
        cvDbValues.put("EmployeeRole", iEmployeeRole);

        return dbFNB.update(TBL_EMPLOYEE, cvDbValues, "EmployeeId=" + iEmployeeId, null);
    }

    /************************************************************************************************************************************/
    /*********************************************************
     * Table - Item
     *************************************************************/
    /************************************************************************************************************************************/
    // -----Insert Items-----
    public long addItem(ItemOutward objItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put("MenuCode", objItem.getMenuCode());
        cvDbValues.put(KEY_ItemName, objItem.getItemName());
        cvDbValues.put("ItemBarcode", objItem.getBarCode());
        cvDbValues.put("DeptCode", objItem.getDeptCode());
        cvDbValues.put("CategCode", objItem.getCategCode());
        cvDbValues.put("KitchenCode", objItem.getKitchenCode());
        cvDbValues.put("DineInPrice1", objItem.getDineIn1());
        cvDbValues.put("DineInPrice2", objItem.getDineIn2());
        cvDbValues.put("DineInPrice3", objItem.getDineIn3());
        cvDbValues.put("PickUpPrice", 0);
        cvDbValues.put("TakeAwayPrice",0);
        cvDbValues.put("DeliveryPrice", 0);
        cvDbValues.put("Quantity", objItem.getStock());
        cvDbValues.put("ImageUri", objItem.getImageUri());
        cvDbValues.put("TaxType", 0);
        cvDbValues.put(KEY_DiscountPercent, objItem.getItemDiscount());
        cvDbValues.put(KEY_HSNCode, objItem.getHSN());
        cvDbValues.put(KEY_IGSTRate, objItem.getIGSTRate());
        cvDbValues.put(KEY_CGSTRate, objItem.getCGSTRate());
        cvDbValues.put(KEY_SGSTRate, objItem.getSGSTRate());
        cvDbValues.put(KEY_cessRate, objItem.getCessRate());
        cvDbValues.put(KEY_SupplyType, objItem.getSupplyType());
        cvDbValues.put(KEY_UOM, objItem.getUOM());
        cvDbValues.put(KEY_TaxationType, objItem.getTaxationType());
        cvDbValues.put(KEY_Rate, objItem.getDineIn1());
        return dbFNB.insert(TBL_ITEM_Outward, null, cvDbValues);
    }

    public long addItem(Item objItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put("MenuCode", objItem.getMenuCode());
        cvDbValues.put(KEY_ItemName, objItem.getItemname());
//        cvDbValues.put("ShortName", objItem.getShortName());
        cvDbValues.put("ItemBarcode", objItem.getItemBarcode());
        cvDbValues.put("DeptCode", objItem.getDeptCode());
        cvDbValues.put("CategCode", objItem.getCategCode());
        cvDbValues.put("KitchenCode", objItem.getKitchenCode());
        cvDbValues.put("DineInPrice1", objItem.getDineInPrice1());
        cvDbValues.put("DineInPrice2", objItem.getDineInPrice2());
        cvDbValues.put("DineInPrice3", objItem.getDineInPrice3());
        cvDbValues.put("PickUpPrice", objItem.getPickUpPrice());
        cvDbValues.put("TakeAwayPrice", objItem.getTakeAwayPrice());
        cvDbValues.put("DeliveryPrice", objItem.getDeliveryPrice());
        cvDbValues.put("SalesTaxId", objItem.getSalesTaxId());
        cvDbValues.put("AdditionalTaxId", objItem.getAdditionalTaxId());
        cvDbValues.put("OptionalTaxId1", objItem.getOptionalTaxId1());
        cvDbValues.put("OptionalTaxId2", objItem.getOptionalTaxId2());
        cvDbValues.put("DiscId", objItem.getDiscId());
        cvDbValues.put("Quantity", objItem.getQuantity());
        cvDbValues.put("PriceChange", objItem.getPriceChange());
        cvDbValues.put("DiscountEnable", objItem.getDiscountEnable());
        cvDbValues.put("BillWithStock", objItem.getBillWithStock());
        cvDbValues.put("ImageUri", objItem.getImageUri());
        cvDbValues.put("TaxType", objItem.getTaxType());

        cvDbValues.put(KEY_HSNCode, objItem.getHSNCode());
        cvDbValues.put(KEY_IGSTRate, objItem.getIGSTRate());
        cvDbValues.put(KEY_CGSTRate, objItem.getCGSTRate());
        cvDbValues.put(KEY_SGSTRate, objItem.getSGSTRate());
        //cvDbValues.put(KEY_IGSTAmount, objItem.getIGSTAmount());
        //cvDbValues.put(KEY_CGSTAmount, objItem.getCGSTAmount());
        //cvDbValues.put(KEY_SGSTAmount, objItem.getSGSTAmount());
        cvDbValues.put(KEY_SupplyType, objItem.getSupplyType());
        cvDbValues.put(KEY_UOM, objItem.getMOU());
        cvDbValues.put(KEY_TaxationType, objItem.getTaxationType());
        cvDbValues.put(KEY_Rate, objItem.getRate());

        cvDbValues.put(KEY_SalesTaxPercent, objItem.getSalesTaxPercent());
        cvDbValues.put(KEY_SerTaxPercent, objItem.getServiceTaxPercent());

        return dbFNB.insert(TBL_ITEM_Outward, null, cvDbValues);
    }

    public long addItem_Inw(Item objItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_ItemName, objItem.getItemname());
//        cvDbValues.put("ShortName", objItem.getShortName());
        cvDbValues.put("ItemBarcode", objItem.getItemBarcode());
        /*cvDbValues.put("DeptCode", objItem.getDeptCode());
        cvDbValues.put("CategCode", objItem.getCategCode());
        cvDbValues.put("KitchenCode", objItem.getKitchenCode());
        cvDbValues.put("DineInPrice1", objItem.getDineInPrice1());
        cvDbValues.put("DineInPrice2", objItem.getDineInPrice2());
        cvDbValues.put("DineInPrice3", objItem.getDineInPrice3());
        cvDbValues.put("PickUpPrice", objItem.getPickUpPrice());
        cvDbValues.put("TakeAwayPrice", objItem.getTakeAwayPrice());
        cvDbValues.put("DeliveryPrice", objItem.getDeliveryPrice());
        */
        cvDbValues.put("SalesTaxId", objItem.getSalesTaxId());
        cvDbValues.put("AdditionalTaxId", objItem.getAdditionalTaxId());
        /*cvDbValues.put("OptionalTaxId1", objItem.getOptionalTaxId1());
        cvDbValues.put("OptionalTaxId2", objItem.getOptionalTaxId2());
        */
        cvDbValues.put("DiscId", objItem.getDiscId());
        cvDbValues.put("Quantity", objItem.getQuantity());
        cvDbValues.put(KEY_Rate, objItem.getRate());
                /*cvDbValues.put("PriceChange", objItem.getPriceChange());
        cvDbValues.put("DiscountEnable", objItem.getDiscountEnable());
        cvDbValues.put("BillWithStock", objItem.getBillWithStock());
        */
        cvDbValues.put("ImageUri", objItem.getImageUri());
        cvDbValues.put("TaxType", objItem.getTaxType());


        cvDbValues.put(KEY_HSNCode, objItem.getHSNCode());
        cvDbValues.put(KEY_IGSTRate, objItem.getIGSTRate());
        cvDbValues.put(KEY_IGSTAmount, objItem.getIGSTAmount());
        cvDbValues.put(KEY_CGSTRate, objItem.getCGSTRate());
        cvDbValues.put(KEY_CGSTAmount, objItem.getCGSTAmount());
        cvDbValues.put(KEY_SGSTRate, objItem.getSGSTRate());
        cvDbValues.put(KEY_SGSTAmount, objItem.getSGSTAmount());
        cvDbValues.put(KEY_UOM, objItem.getMOU());
        cvDbValues.put(KEY_TaxationType, objItem.getTaxationType());
        cvDbValues.put(KEY_SupplierCode, objItem.getsuppliercode());
        cvDbValues.put(KEY_SUPPLIERNAME, objItem.getsupplierName());

        return dbFNB.insert(TBL_ITEM_Inward, null, cvDbValues);
    }

    public int clearOutwardItemdatabase() {
        return dbFNB.delete(TBL_ITEM_Outward, null, null);
    }

    public int deleteItem_inward(String suppliertype, String supplier_gstin, String suppliername_str, String invno, String invodate) {
      /*int result = dbFNB.delete (TBL_INWARD_SUPPLY_LEDGER, KEY_SUPPLIERNAME+" LIKE '"+suppliername_str +"' AND "+
                        KEY_GSTIN +" LIKE '"+supplier_gstin+
                        "' AND "+KEY_SupplierType +" LIKE ' "+suppliertype+"' AND "+ KEY_InvoiceNo+" LIKE '"+invno+"' AND "+
                        KEY_InvoiceDate+" LIKE '"+invodate+"'",null);*/
        int result = dbFNB.delete(TBL_INWARD_SUPPLY_LEDGER, KEY_SUPPLIERNAME + " LIKE '" + suppliername_str + "' AND " + KEY_GSTIN + " LIKE '" + supplier_gstin +
                "' AND " + KEY_SupplierType + " LIKE '" + suppliertype + "' AND " + KEY_InvoiceNo + " LIKE '" + invno + "' AND " + KEY_InvoiceDate + " LIKE '" + invodate
                + "'", null);
        return result;
    }

    public int deleteItemDetail_inward(String suppliertype, String supplier_gstin, String suppliername_str, String invno, String invodate) {
        int result = dbFNB.delete(TBL_INWARD_SUPPLY_ITEMS_DETAILS, KEY_SUPPLIERNAME + " LIKE '" + suppliername_str + "' AND " + KEY_GSTIN + " LIKE '" + supplier_gstin +
                "' AND " + KEY_SupplierType + " LIKE '" + suppliertype + "' AND " + KEY_InvoiceNo + " LIKE '" + invno + "' AND " + KEY_InvoiceDate + " LIKE '" + invodate
                + "'", null);
        return result;
    }

    // public Cursor GetAllData()
    // {
    // return dbFNB.rawQuery("Select * from Item", null);
    // }
    // -----Retrieve highest DeptCode from table-----
    public int getItemMenuCode() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(MenuCode) FROM " + TBL_ITEM_Outward, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Retrieve All Items without Dept and Categ-----
    public Cursor getAllItemsWithoutDeptCateg() {

        return dbFNB.rawQuery("Select * from Item_Outward where DeptCode not in (Select DeptCode from Department) and CategCode not in (Select CategCode from Category)", null);
    }

    // -----Retrieve All Items-----
    public Cursor getAllItems() {

        return dbFNB.query(TBL_ITEM_Outward, new String[]{"*"}, null, null, null, null, null);
    }






   /* public Cursor getAllItemswithDeptCategName() {
        return dbFNB.rawQuery("Select * from " + TBL_ITEM_Outward + ", " + TBL_DEPARTMENT + ", " + TBL_CATEGORY, null);
        *//*return dbFNB.rawQuery("Select * from " + TBL_ITEM_Outward + ", " + TBL_DEPARTMENT + ", " + TBL_CATEGORY + " where " + TBL_DEPARTMENT
                + ".DeptCode = " + TBL_ITEM_Outward + ".DeptCode or " + TBL_CATEGORY + ".CategCode = " + TBL_ITEM_Outward + ".CategCode", null);*//*
    }*/

    // -----Retrieve Items based on DeptCode-----
    public Cursor getItems(int DeptCode) {
        return dbFNB.query(TBL_ITEM_Outward, new String[]{"MenuCode", "ItemName", "Quantity", "ImageUri"},
                "DeptCode=" + DeptCode, null, null, null, null);
    }

    public Cursor getItemsForAllDepartments() {
        return dbFNB.query(TBL_ITEM_Outward, new String[]{"MenuCode", "ItemName", "Quantity", "ImageUri"},
                "DeptCode > 0", null, null, null, null);
    }

    // -----Retrieve Items based on CategCode-----
    public Cursor getCatItems(int DeptCode) {
        return dbFNB.query(TBL_CATEGORY, new String[]{"CategCode", "CategName"}, "DeptCode=" + DeptCode, null, null,
                null, null);
    }

    // -----Retrieve Items based on CategCode-----
    public Cursor getCategoryItems(int DeptCode) {
        return dbFNB.rawQuery("Select CategCode as _id, CategName, DeptCode from Category where DeptCode=" + DeptCode, null);
    }
    public Cursor getAllCategories() {
        return dbFNB.rawQuery("Select CategCode as _id, CategName, DeptCode from Category ", null);
    }

    public Cursor getCategorybyDept() {
        return dbFNB.rawQuery("Select CategCode as _id, CategName, DeptCode from Category where DeptCode not in (Select DeptCode from Department)", null);
    }

    // -----Retrieve Items based on CategCode-----
    public Cursor getCatbyItems(int CategCode) {
        return dbFNB.query(TBL_ITEM_Outward, new String[]{"MenuCode", "ItemName", "ImageUri"}, "CategCode=" + CategCode, null, null, null, null);
    }

    // -----Retrieve Items based on CategCode and DeptCode-----
    public Cursor getDeptCatbyItems(int CategCode, int DeptCode) {
        return dbFNB.query(TBL_ITEM_Outward, new String[]{"MenuCode", "ItemName", "ImageUri"},
                "CategCode=" + CategCode + " AND DeptCode=" + DeptCode, null, null, null, null);
    }

    // -----Retrieve Single Item based on ItemBarcode-----
    public Cursor getItem(String Barcode) {
        return dbFNB.query(TBL_ITEM_Outward, new String[]{"*"}, "ItemBarcode=" + Barcode, null, null, null, null);
    }

    // -----Retrieve Single Item based on Item MenuCode-----
    public Cursor getItem(int MenuCode) {
        return dbFNB.query(TBL_ITEM_Outward, new String[]{"*"}, "MenuCode=" + MenuCode, null, null, null, null);
    }

//    public Cursor getItem_new(int MenuCode) {
//        Cursor cursor =null;
//
//        try {
//            SQLiteDatabase db = getReadableDatabase();
//            cursor = db.query(TBL_ITEM_Outward, new String[]{"*"}, "MenuCode=" + MenuCode, null, null, null, null);
//        }catch(Exception e)
//        {
//            e.printStackTrace();
//        }
//        finally {
//            return cursor;
//        }
//    }

    // -----Retrieve Single Item based on Item Name-----
    public Cursor getbyItemName(String ItemName) {
//        return dbFNB.query(TBL_ITEM_Outward, new String[]{"*"}, "ItemName = '" + ItemName + "'", null, null, null, null);
        return dbFNB.rawQuery("SELECT MenuCode FROM " + TBL_ITEM_Outward + " WHERE ItemName = '" + ItemName + "'", null);
    }

    public int getMenuCodebyItemName_inw(String ItemName) {
//        return dbFNB.query(TBL_ITEM_Outward, new String[]{"*"}, "ItemName = '" + ItemName + "'", null, null, null, null);
        int menu = -1;
        String queryString = "SELECT * FROM " + TBL_ITEM_Inward + " WHERE " + KEY_ItemName + " LIKE '" + ItemName + "'";
        Cursor crsr = dbFNB.rawQuery(queryString, null);
        if (crsr != null && crsr.moveToFirst()) {
            menu = crsr.getInt(crsr.getColumnIndex(KEY_MenuCode));
        }
        return menu;
    }

    // -----Retrieve Item List based on Item MenuCode-----
    public Cursor getItemList(int MenuCode) {
        return dbFNB.rawQuery("SELECT * FROM " + TBL_ITEM_Outward + "  WHERE MenuCode LIKE " + MenuCode + "%", null);
    }

    // -----Retrieve Item List based on Item MenuCode-----
    public Cursor getItemList(String Name) {
        return dbFNB.rawQuery("SELECT * FROM " + TBL_ITEM_Outward + "  WHERE ItemName LIKE '" + Name + "%'", null);
    }

    public Cursor getItemDetails(String Name) {
        return dbFNB.rawQuery("SELECT * FROM " + TBL_ITEM_Outward + "  WHERE ItemName LIKE '" + Name + "'", null);
    }

    // -----Retrieve Item stock quantity on Item MenuCode-----
    public double getStockValue(int MenuCode) {
        Cursor result;

        result = dbFNB.query(TBL_ITEM_Outward, new String[]{"Quantity"}, "MenuCode=" + MenuCode, null, null, null, null);

        if (result.moveToFirst()) {

            return result.getDouble(0);

        } else {

            return 0.0;
        }
    }

    // -----Update Item table-----
    public int updateItem(int MenuCode, String ItemName, String ShortName, String ItemBarCode, int DeptCode,
                          int CategCode, int KitchenCode, double DineInPrice1, double DineInPrice2, double DineInprice3,
                          double TakeAwayPrice, double PickUpPrice, double DeliveryPrice, int SalesTaxId, int AdditionalTaxId,
                          int OptionalTaxId1, int OptionalTaxId2, int DiscId, double Stock, int PriceChange, int DiscountEnable,
                          int BillWithStock, String ImageUri, int TaxType, double frate, String hsnCode,
                          String g_s, String MOU_str, String taxationtype_str, double IGSTRate, double CGSTRate, double SGSTRate,double cessRate,
                          float fSalesTax, float fServiceTax, int ItemId , double itemDiscount) {

        cvDbValues = new ContentValues();

        cvDbValues.put("MenuCode", MenuCode);
        cvDbValues.put("ItemName", ItemName);
        //cvDbValues.put("ShortName", ShortName);
        cvDbValues.put("ItemBarcode", ItemBarCode);
        cvDbValues.put("DeptCode", DeptCode);
        cvDbValues.put("CategCode", CategCode);
        cvDbValues.put("KitchenCode", KitchenCode);
        cvDbValues.put("DineInPrice1", DineInPrice1);
        cvDbValues.put("DineInPrice2", DineInPrice2);
        cvDbValues.put("DineInPrice3", DineInprice3);
        cvDbValues.put("TakeAwayPrice", TakeAwayPrice);
        cvDbValues.put("PickUpPrice", PickUpPrice);
        cvDbValues.put("DeliveryPrice", DeliveryPrice);
        cvDbValues.put("SalesTaxId", SalesTaxId);
        cvDbValues.put("AdditionalTaxId", AdditionalTaxId);
        cvDbValues.put("OptionalTaxId1", OptionalTaxId1);
        cvDbValues.put("OptionalTaxId2", OptionalTaxId2);
        cvDbValues.put("DiscId", DiscId);
        cvDbValues.put("Quantity", Stock);
        cvDbValues.put("PriceChange", PriceChange);
        cvDbValues.put("DiscountEnable", DiscountEnable);
        cvDbValues.put("BillWithStock", BillWithStock);
        cvDbValues.put("ImageUri", ImageUri);
        cvDbValues.put("TaxType", TaxType);

        cvDbValues.put(KEY_DiscountPercent, itemDiscount);
        cvDbValues.put(KEY_Rate, frate);
        cvDbValues.put(KEY_HSNCode, hsnCode);
        cvDbValues.put(KEY_IGSTRate, IGSTRate);
        cvDbValues.put(KEY_CGSTRate, CGSTRate);
        cvDbValues.put(KEY_SGSTRate, SGSTRate);
        cvDbValues.put(KEY_cessRate, cessRate);
        cvDbValues.put(KEY_SupplyType, g_s);
        cvDbValues.put(KEY_UOM, MOU_str);
        cvDbValues.put(KEY_TaxationType, taxationtype_str);

        cvDbValues.put(KEY_SalesTaxPercent, fSalesTax);
        cvDbValues.put(KEY_SerTaxPercent, fServiceTax);

        return dbFNB.update(TBL_ITEM_Outward, cvDbValues, "ItemId=" + ItemId, null);
    }


    public int updateItem_Inw(Item objItem) {

        cvDbValues = new ContentValues();

        cvDbValues.put("ItemName", objItem.getItemname());
        cvDbValues.put(KEY_MenuCode, objItem.getMenuCode());
        cvDbValues.put(KEY_SUPPLIERNAME, objItem.getsupplierName());
        cvDbValues.put(KEY_SupplierCode, objItem.getsuppliercode());
        cvDbValues.put(KEY_ItemBarcode, objItem.getItemBarcode());
        cvDbValues.put(KEY_HSNCode, objItem.getHSNCode());
        cvDbValues.put(KEY_Rate, objItem.getRate());
        cvDbValues.put(KEY_Quantity, objItem.getQuantity());
        cvDbValues.put(KEY_UOM, objItem.getMOU());
        cvDbValues.put(KEY_IGSTRate, objItem.getIGSTRate());
        cvDbValues.put(KEY_CGSTRate, objItem.getCGSTRate());
        cvDbValues.put(KEY_SGSTRate, objItem.getSGSTRate());
        cvDbValues.put("ImageUri", objItem.getImageUri());
        cvDbValues.put(KEY_SupplyType, objItem.getSupplyType());

        return dbFNB.update(TBL_ITEM_Inward, cvDbValues, "MenuCode=" + objItem.getMenuCode(), null);
    }

    public int updateItem_Inw(int supplierCode, int menuCode, String itemName , float quantity, float rate ) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_Rate, rate);
        cvDbValues.put(KEY_Quantity, quantity);
        return dbFNB.update(TBL_ITEM_Inward, cvDbValues, "MenuCode=" + menuCode, null);
        //return dbFNB.update(TBL_ITEM_Inward, cvDbValues, "SupplierCode=" + supplierCode+" AND "+KEY_ItemName+" LIKE '"+itemName+"'", null);
    }

    // -----Update Item stock-----
    public int updateItemStock(int MenuCode, float Stock) {
        SQLiteDatabase db = getWritableDatabase();
        int result = 0;
        try {
            cvDbValues = new ContentValues();

            cvDbValues.put("Quantity", Stock);

            result = db.update(TBL_ITEM_Outward, cvDbValues, "MenuCode=" + MenuCode, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // -----Update Item stock-----
    public int updateItemStock(int MenuCode, float Stock, float Rate1, float Rate2, float Rate3) {
        cvDbValues = new ContentValues();

        cvDbValues.put("Quantity", Stock);
        cvDbValues.put("DineInPrice1", Rate1);
        cvDbValues.put("DineInPrice2", Rate2);
        cvDbValues.put("DineInPrice3", Rate3);
        return dbFNB.update(TBL_ITEM_Outward, cvDbValues, "MenuCode=" + MenuCode, null);
    }

    // -----Delete Items from Item Table-----
    public int DeleteItemByMenuCode(int MenuCode) {

        return dbFNB.delete(TBL_ITEM_Outward, "MenuCode=" + MenuCode, null);
    }

    public int DeleteItem_Inw(String MenuCode) {

        return dbFNB.delete(TBL_ITEM_Inward, "MenuCode=" + MenuCode, null);
    }
    public long DeleteSupplierItems_suppliercode(int suppliercode) {

        return dbFNB.delete(TBL_SupplierItemLinkage, "SupplierCode=" + suppliercode, null);
    }
    public long deleteSupplierItemLinkforItem(int menucode) {

        return dbFNB.delete(TBL_SupplierItemLinkage, "MenuCode=" + menucode, null);
    }

    // -----Delete Items from Item Table by Dept Code-----
    public int DeleteItemByDeptCode(String DeptCode) {

        return dbFNB.delete(TBL_ITEM_Outward, "DeptCode=" + DeptCode, null);
    }

    // -----Delete Items from Item Table by Categ Code-----
    public int DeleteItemByCategCode(String CategCode) {

        return dbFNB.delete(TBL_ITEM_Outward, "CategCode=" + CategCode, null);
    }

    /************************************************************************************************************************************/
    /*******************************************************
     * Table - PendingKOT
     *********************************************************/
    /************************************************************************************************************************************/
    // -----Retrieve Token Number-----
    public int getTokenNumber() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(TokenNumber) FROM " + TBL_PENDINGKOT, null);

        if (result.moveToFirst()) {
            return result.getInt(0) + 1;
        } else {
            return 1;
        }
    }

    // -----Insert KOT items-----
    public long addKOT(PendingKOT objPendingKOT) {
        cvDbValues = new ContentValues();

        cvDbValues.put("TokenNumber", objPendingKOT.getTokenNumber());
        cvDbValues.put("TableNumber", objPendingKOT.getTableNumber());
        cvDbValues.put("SubUdfNumber", objPendingKOT.getSubUdfNumber());
        cvDbValues.put("EmployeeId", objPendingKOT.getEmployeeId());
        cvDbValues.put("CustId", objPendingKOT.getCusId());
        cvDbValues.put("Time", objPendingKOT.getTime());
        cvDbValues.put("ItemNumber", objPendingKOT.getItemNumber());
        cvDbValues.put("ItemName", objPendingKOT.getItemName());
        cvDbValues.put("Quantity", objPendingKOT.getQuantity());
        cvDbValues.put("Rate", objPendingKOT.getRate());
        cvDbValues.put("Amount", objPendingKOT.getAmount());
        cvDbValues.put("TaxPercent", objPendingKOT.getTaxPercent());
        cvDbValues.put("TaxAmount", objPendingKOT.getTaxAmount());
        cvDbValues.put("DiscountPercent", objPendingKOT.getDiscountPercent());
        cvDbValues.put("DiscountAmount", objPendingKOT.getDiscountAmount());
        cvDbValues.put("ModifierAmount", objPendingKOT.getModifierAmount());
        cvDbValues.put("ServiceTaxPercent", objPendingKOT.getServiceTaxPercent());
        cvDbValues.put("ServiceTaxAmount", objPendingKOT.getServiceTaxAmount());
        cvDbValues.put("TaxType", objPendingKOT.getTaxType());
        cvDbValues.put("DeptCode", objPendingKOT.getDeptCode());
        cvDbValues.put("CategCode", objPendingKOT.getCategCode());
        cvDbValues.put("KitchenCode", objPendingKOT.getKitchenCode());
        cvDbValues.put("OrderMode", objPendingKOT.getOrderMode());
        cvDbValues.put("IsCheckedOut", objPendingKOT.getIsCheckedOut());
        cvDbValues.put("TableSplitNo", objPendingKOT.getTableSplitNo());
        cvDbValues.put("PrintKOTStatus", objPendingKOT.getPrintKOTStatus());
        cvDbValues.put(KEY_HSNCode, objPendingKOT.getHSNCode());
        cvDbValues.put(KEY_UOM, objPendingKOT.getUOM());
        cvDbValues.put(KEY_POS, objPendingKOT.getPOS());
        cvDbValues.put(KEY_SupplyType, objPendingKOT.getSupplyType());
        cvDbValues.put(KEY_IGSTRate, objPendingKOT.getIGSTRate());
        cvDbValues.put(KEY_IGSTAmount, objPendingKOT.getIGSTAmount());
        cvDbValues.put(KEY_cessRate, objPendingKOT.getCessRate());
        cvDbValues.put(KEY_cessAmount, objPendingKOT.getCessAmount());

        return dbFNB.insert(TBL_PENDINGKOT, null, cvDbValues);
    }

    public long addKOT_new(PendingKOT objPendingKOT) {

        long result =0;
        SQLiteDatabase db = this.getWritableDatabase();
        try {
        cvDbValues = new ContentValues();

        cvDbValues.put("TokenNumber", objPendingKOT.getTokenNumber());
        cvDbValues.put("TableNumber", objPendingKOT.getTableNumber());
        cvDbValues.put("SubUdfNumber", objPendingKOT.getSubUdfNumber());
        cvDbValues.put("EmployeeId", objPendingKOT.getEmployeeId());
        cvDbValues.put("CustId", objPendingKOT.getCusId());
        cvDbValues.put("Time", objPendingKOT.getTime());
        cvDbValues.put("ItemNumber", objPendingKOT.getItemNumber());
        cvDbValues.put("ItemName", objPendingKOT.getItemName());
        cvDbValues.put("Quantity", objPendingKOT.getQuantity());
        cvDbValues.put("Rate", objPendingKOT.getRate());
        cvDbValues.put("Amount", objPendingKOT.getAmount());
        cvDbValues.put("TaxPercent", objPendingKOT.getTaxPercent());
        cvDbValues.put("TaxAmount", objPendingKOT.getTaxAmount());
        cvDbValues.put("DiscountPercent", objPendingKOT.getDiscountPercent());
        cvDbValues.put("DiscountAmount", objPendingKOT.getDiscountAmount());
        cvDbValues.put("ModifierAmount", objPendingKOT.getModifierAmount());
        cvDbValues.put("ServiceTaxPercent", objPendingKOT.getServiceTaxPercent());
        cvDbValues.put("ServiceTaxAmount", objPendingKOT.getServiceTaxAmount());
        cvDbValues.put("TaxType", objPendingKOT.getTaxType());
        cvDbValues.put("DeptCode", objPendingKOT.getDeptCode());
        cvDbValues.put("CategCode", objPendingKOT.getCategCode());
        cvDbValues.put("KitchenCode", objPendingKOT.getKitchenCode());
        cvDbValues.put("OrderMode", objPendingKOT.getOrderMode());
        cvDbValues.put("IsCheckedOut", objPendingKOT.getIsCheckedOut());
        cvDbValues.put("TableSplitNo", objPendingKOT.getTableSplitNo());
        cvDbValues.put("PrintKOTStatus", objPendingKOT.getPrintKOTStatus());
        cvDbValues.put(KEY_HSNCode, objPendingKOT.getHSNCode());
        cvDbValues.put(KEY_UOM, objPendingKOT.getUOM());
        cvDbValues.put(KEY_POS, objPendingKOT.getPOS());
        cvDbValues.put(KEY_SupplyType, objPendingKOT.getSupplyType());
        cvDbValues.put(KEY_IGSTRate, objPendingKOT.getIGSTRate());
        cvDbValues.put(KEY_IGSTAmount, objPendingKOT.getIGSTAmount());
        cvDbValues.put(KEY_cessRate, objPendingKOT.getCessRate());
        cvDbValues.put(KEY_cessAmount, objPendingKOT.getCessAmount());

        result = db.insert(TBL_PENDINGKOT, null, cvDbValues);
    }catch(Exception e )
    {
        result = 0;
        e.printStackTrace();
    }finally

    {
        return result;
    }
}

    public long updateKOT(int ItemNo, float Qty, float Amount, float TaxAmt, float SerTaxAmt, int OrderMode, int PrintKOTStatus
                    ,float IAmt, float cessAmt) {
        cvDbValues = new ContentValues();
        cvDbValues.put("Quantity", Qty);
        cvDbValues.put("Amount", Amount);
        cvDbValues.put("TaxAmount", TaxAmt);
        cvDbValues.put("ServiceTaxAmount", SerTaxAmt);
        cvDbValues.put(KEY_IGSTAmount, IAmt);
        cvDbValues.put(KEY_cessAmount, cessAmt);
        return dbFNB.update(TBL_PENDINGKOT, cvDbValues, "ItemNumber=" + ItemNo + " AND OrderMode=" + OrderMode, null);// AND PrintKOTStatus = " + PrintKOTStatus, null);
    }

    public long updateKOT_new(int ItemNo, float Qty, float Amount, float TaxAmt, float SerTaxAmt, int OrderMode, int PrintKOTStatus
                    ,float IAmt, float cessAmt) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result =0;
        try
        {
            cvDbValues = new ContentValues();
            cvDbValues.put("Quantity", Qty);
            cvDbValues.put("Amount", Amount);
            cvDbValues.put("TaxAmount", TaxAmt);
            cvDbValues.put("ServiceTaxAmount", SerTaxAmt);
            cvDbValues.put(KEY_IGSTAmount, IAmt);
            cvDbValues.put(KEY_cessAmount, cessAmt);
            result =  db.update(TBL_PENDINGKOT, cvDbValues, "ItemNumber=" + ItemNo + " AND OrderMode=" + OrderMode, null);// AND PrintKOTStatus = " + PrintKOTStatus, null);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        finally {
            return result;
        }

    }

    public long updateKOTNo(int KOTNo) {
        cvDbValues = new ContentValues();
        cvDbValues.put("KOTNo", KOTNo);
        return dbFNB.update(TBL_BILLNORESETCONFIG, cvDbValues, null, null);
    }

    // -----Retrieve new KOT Number-----
    public int getKOTNo() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT KOTNo FROM BillNoConfiguration", null);

        if (result.moveToFirst()) {
            return result.getInt(0) + 1;
        } else {
            return 1;
        }
    }

    public Cursor getItemsForUpdatingKOT(int TableNo, int SubUdfNo, int TableSplitNo, int ItemNo, int OrderMode) {
        int PrintStatus = 0;
        return dbFNB.query(TBL_PENDINGKOT, new String[]{"*"},
                "TableNumber=" + TableNo + " AND SubUdfNumber=" + SubUdfNo + " AND TableSplitNo=" + TableSplitNo +
                        " AND ItemNumber=" + ItemNo + " AND OrderMode=" + OrderMode + " AND PrintKOTStatus = " + PrintStatus
                , null, null, null, null);
    }
    public Cursor getItemsForUpdatingKOT_new(int TableNo, int SubUdfNo, int TableSplitNo, int ItemNo, int OrderMode) {
        int PrintStatus = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.query(TBL_PENDINGKOT, new String[]{"*"},
                    "TableNumber=" + TableNo + " AND SubUdfNumber=" + SubUdfNo + " AND TableSplitNo=" + TableSplitNo +
                            " AND ItemNumber=" + ItemNo + " AND OrderMode=" + OrderMode + " AND PrintKOTStatus = " + PrintStatus
                    , null, null, null, null);
        }catch (Exception e)
        {
            cursor = null;
            e.printStackTrace();
        }
        finally {
            return cursor;
        }
    }

    // -----Retrieve KOT items for billing from Pending KOT table-----
    public Cursor getKOTItems(int TableNumber, int SubUdfNumber, int TableSplitNo) {
        return dbFNB.query(TBL_PENDINGKOT, new String[]{"*"},
                "TableNumber=" + TableNumber + " AND SubUdfNumber=" + SubUdfNumber + " AND TableSplitNo=" + TableSplitNo, null, null, null, null);
    }

    // -----Retrieve KOT items for Reprint bill from Bill Detail-----
    public Cursor getItemsForReprintBill(int InvoiceNo) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);
        return dbFNB.rawQuery("Select * from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "'", null);
    }

    public Cursor getItemsFromBillItem_new(int InvoiceNo, String InvoiceDate) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor crsr = null;
        try
        {
            crsr = db.rawQuery("Select * from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "' AND "
                    +KEY_InvoiceDate+" LIKE '"+InvoiceDate+"'", null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            crsr = null;
        }
        finally {
            return crsr;
        }
    }public Cursor getItemsFromBillItem(int InvoiceNo, String InvoiceDate) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);
        /*SQLiteDatabase db = this.getReadableDatabase();*/
        Cursor crsr = null;
        try
        {
            crsr = dbFNB.rawQuery("Select * from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "' AND "
                    +KEY_InvoiceDate+" LIKE '"+InvoiceDate+"'", null);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            crsr = null;
        }
        finally {
            return crsr;
        }
    }
    public int getBillingModeBillNumber(int InvoiceNo) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor crsr = null;
        int discount  = 0;
        try
        {
            crsr = db.rawQuery("Select * from " + TBL_BILLDETAIL + " where InvoiceNo = '" + InvoiceNo + "'", null);
            if(crsr!= null && crsr.moveToFirst())
                discount = crsr.getInt(crsr.getColumnIndex(KEY_BillingMode));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            discount = 0;
        }
        finally {
            return discount;
        }
    }
    public float getDiscountAmountForBillNumber(int InvoiceNo) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor crsr = null;
        float discount  = 0;
        try
        {
            crsr = db.rawQuery("Select * from " + TBL_BILLDETAIL + " where InvoiceNo = '" + InvoiceNo + "'", null);
            if(crsr!= null && crsr.moveToFirst())
                discount = crsr.getFloat(crsr.getColumnIndex("TotalDiscountAmount"));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            discount = 0;
        }
        finally {
            return discount;
        }
    }
    public float getDiscountPercentForBillNumber(int InvoiceNo) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor crsr = null;
        float discount  = 0;
        try
        {
            crsr = db.rawQuery("Select * from " + TBL_BILLDETAIL + " where InvoiceNo = '" + InvoiceNo + "'", null);
            if(crsr!= null && crsr.moveToFirst())
                discount = crsr.getFloat(crsr.getColumnIndex(KEY_DiscPercentage));

        }
        catch(Exception e)
        {
            e.printStackTrace();
            discount = 0;
        }
        finally {
            return discount;
        }
    }

    // -----Retrieve KOT items for sa;es tax print
    public Cursor getItemsForSalesTaxPrint(int InvoiceNo) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);
        return dbFNB.rawQuery("Select SUM(TaxAmount) as TaxAmount, TaxPercent from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "' GROUP BY TaxPercent", null);
    }

    public Cursor getItemsForOtherChargesPrint(String jBillingMode) {

        return dbFNB.rawQuery("Select * from " + TBL_KOTMODIFIER + " where ModifierModes LIKE '" +
                jBillingMode+ "' AND "+KEY_IsChargeable+" LIKE '1'", null);
    }

    // -----Retrieve KOT items for service tax print
    public Cursor getItemsForServiceTaxPrint(int InvoiceNo) {
        // return dbFNB.rawQuery("Select * from BillItem, BillDetail where BillDetail.BillNumber = '" + BillNumber + "' AND BillItem.BillNumber = BillDetail.BillNumber", null);
        return dbFNB.rawQuery("Select SUM(ServiceTaxAmount) as TaxAmount, ServiceTaxPercent from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "' GROUP BY TaxPercent", null);
    }

    // -----Retrieve Customer pending order items for billing from Pending KOT
    // table-----
    public Cursor getKOTItems(int CustId, String OrderMode) {
        return dbFNB.query(TBL_PENDINGKOT, new String[]{"*"}, "CustId=" + CustId + " AND OrderMode=" + OrderMode,
                null, null, null, null);
    }

    // -----Retrieve Checked out status for Pending KOT-----
    public int getCheckedOutStatus(int TableNumber, int SubUdfNumber) {
        Cursor result = dbFNB.query(TBL_PENDINGKOT, new String[]{"IsCheckedOut"},
                "TableNumber=" + TableNumber + " AND SubUdfNumber=" + SubUdfNumber, null, null, null, null);
        if (result.moveToFirst()) {
            return result.getInt(result.getColumnIndex("IsCheckedOut"));
        }
        return -1;
    }

    // -----Delete finalized KOT items from Pending KOT table-----
    public int deleteKOTItems(int TableNumber, int SubUdfNumber, int TableSplitNo) {

        return dbFNB.delete(TBL_PENDINGKOT, "TableNumber=" + TableNumber + " AND SubUdfNumber=" + SubUdfNumber + " AND TableSplitNo=" + TableSplitNo, null);
    }

    // -----Delete finalized KOT items from Pending KOT table by Token Number-----
    public int deleteKOTItemsByItemToken(String ItemNumber, int TokenNumber, int SubUdfNumber) {

        return dbFNB.delete(TBL_PENDINGKOT, "ItemNumber=" + ItemNumber + " AND TokenNumber=" + TokenNumber + " AND SubUdfNumber=" + SubUdfNumber, null);
    }
 public int deleteKOTItemsByItemToken_new(String ItemNumber, int TokenNumber, int SubUdfNumber) {

     SQLiteDatabase db = this.getWritableDatabase();
     int result =0;
     try {
         result = db.delete(TBL_PENDINGKOT, "ItemNumber=" + ItemNumber + " AND TokenNumber=" + TokenNumber + " AND SubUdfNumber=" + SubUdfNumber, null);
     }catch (Exception e)
     {
         e.printStackTrace();
         result =0;
     }finally {
         return result;
     }

    }

    // -----Delete Customer items from Pending KOT table-----
    public int deleteKOTItems(int CustId, String OrderMode) {

        return dbFNB.delete(TBL_PENDINGKOT, "CustId=" + CustId + " AND OrderMode=" + OrderMode, null);
    }

    // -----Delete all KOT items from Pending KOT table-----
    public int deleteAllKOTItems() {

        return dbFNB.delete(TBL_PENDINGKOT, null, null);
    }

    // -----Retrieve occupied table numbers and SubUdf numbers for
    // ShiftTable/MergeTable-----
    public Cursor getOccupiedTables() {
        return dbFNB.rawQuery("SELECT DISTINCT TableNumber, SubUdfNumber, TableSplitNo FROM " + TBL_PENDINGKOT + " where TableNumber != '0'", null);
    }

    // Occupied Table Split No-----
    public Cursor getOccupiedTableSplitNo(int TableNo) {
        return dbFNB.rawQuery("SELECT DISTINCT TableSplitNo FROM " + TBL_PENDINGKOT + " where TableNumber=" + TableNo, null);
    }

    // Table Status -----
    public Cursor getTableStatus() {
        return dbFNB.rawQuery("SELECT DISTINCT TableNumber, Time, TableSplitNo FROM " + TBL_PENDINGKOT + " where TableNumber != '0'", null);
    }

    // Table Selection
    public Cursor getOccupiedTablesByTableNo(int TableNo) {
        return dbFNB.rawQuery("SELECT * FROM " + TBL_PENDINGKOT + " where TableNumber = '" + TableNo + "'", null);
    }

    // Table Status -----
    public Cursor getTableStatusByTableNo(int TableNo) {
        return dbFNB.rawQuery("SELECT DISTINCT TableNumber, Time FROM " + TBL_PENDINGKOT + " where TableNumber != '0' AND TableNumber=" + TableNo, null);
    }

    // Table Status -----
    public Cursor getKOTStatus() {
        return dbFNB.rawQuery("SELECT DISTINCT TokenNumber, TableNumber, EmployeeId, Time, TableSplitNo FROM " + TBL_PENDINGKOT + " where TableNumber != '0'", null);
    }

    // Table Status -----
    public Cursor getKOTStatus(int OrderMode) {
        return dbFNB.rawQuery("SELECT DISTINCT TokenNumber, Time FROM " + TBL_PENDINGKOT + " where TableNumber = '0' AND OrderMode=" + OrderMode, null);
    }

    // KOT Status by Table No-----
    public Cursor getKOTStatusByTableNo(int TableNo) {
        return dbFNB.rawQuery("SELECT DISTINCT TokenNumber, TableNumber, EmployeeId, Time, TableSplitNo FROM " + TBL_PENDINGKOT + " where TableNumber != '0' AND TableNumber=" + TableNo, null);
    }

    // -----Retrieve Pending orders CustomerId from PendingKOT-----
    public Cursor getCustomerPendingOrders(String OrderMode) {
        return dbFNB.rawQuery("SELECT * FROM PendingKOT, Customer WHERE Customer.CustId = PendingKOT.CustId "
                + "AND PendingKOT.OrderMode=" + OrderMode, null);
    }

    // -----Update table number after shifting KOT------
    public int updateKOTTable(int SourceTable, int SourceTblSplitNo, int DestinationTable, int DestinationTblSplitNo) {
        cvDbValues = new ContentValues();

        cvDbValues.put("TableNumber", DestinationTable);
        cvDbValues.put(KEY_Table_Split_No, DestinationTblSplitNo);

        return dbFNB.update(TBL_PENDINGKOT, cvDbValues,
                "TableNumber=" + SourceTable + " AND "+KEY_Table_Split_No+" =" + SourceTblSplitNo, null);
    }

    // -----Update table number after shifting KOT------
    public int updateKOTTable(int SourceTable, int SourceSubUdf, int SourceTblSplitNo, int DestinationTable, int DestinationSubUdf, int DestinationTblSplitNo) {
        cvDbValues = new ContentValues();

        cvDbValues.put("TableNumber", DestinationTable);
        cvDbValues.put("SubUdfNumber", DestinationSubUdf);
        cvDbValues.put("TableSplitNo", DestinationTblSplitNo);

        return dbFNB.update(TBL_PENDINGKOT, cvDbValues,
                "TableNumber=" + SourceTable + " AND SubUdfNumber=" + SourceSubUdf + " AND TableSplitNo=" + SourceTblSplitNo, null);
    }

    // -----Update table number after shifting KOT------
    public int updateCheckedOutStatus(int iTableNumber, int iSubUdfNumber) {
        cvDbValues = new ContentValues();

        cvDbValues.put("IsCheckedOut", 1);

        return dbFNB.update(TBL_PENDINGKOT, cvDbValues,
                "TableNumber=" + iTableNumber + " AND SubUdfNumber=" + iSubUdfNumber, null);
    }

    /************************************************************************************************************************************/
    /******************************************************
     * Table - DeletedKOT
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Deleted KOT details-----
    public long addDeletedKOT(DeletedKOT objDeletedKOT) {
        cvDbValues = new ContentValues();

        cvDbValues.put("Reason", objDeletedKOT.getReason());
        cvDbValues.put("EmployeeId", objDeletedKOT.getEmployeeId());
        cvDbValues.put("SubUdfNumber", objDeletedKOT.getSubudfNumber());
        cvDbValues.put("TableNumber", objDeletedKOT.getTableNumber());
        cvDbValues.put("Time", objDeletedKOT.getTime());
        cvDbValues.put("TokenNumber", objDeletedKOT.getTokenNumber());

        return dbFNB.insert(TBL_DELETEDKOT, null, cvDbValues);
    }
public long addDeletedKOT_new(DeletedKOT objDeletedKOT) {
    SQLiteDatabase db = this.getWritableDatabase();
    long result =0;
    try {
        cvDbValues = new ContentValues();

        cvDbValues.put("Reason", objDeletedKOT.getReason());
        cvDbValues.put("EmployeeId", objDeletedKOT.getEmployeeId());
        cvDbValues.put("SubUdfNumber", objDeletedKOT.getSubudfNumber());
        cvDbValues.put("TableNumber", objDeletedKOT.getTableNumber());
        cvDbValues.put("Time", objDeletedKOT.getTime());
        cvDbValues.put("TokenNumber", objDeletedKOT.getTokenNumber());

        result = db.insert(TBL_DELETEDKOT, null, cvDbValues);
    }catch (Exception e)
    {
        result =0;
        e.printStackTrace();
    }
    finally {
        return result;
    }
    }

    // -----Delete all KOT items from Pending KOT table-----
    public int deleteAllVoidedKOT() {

        return dbFNB.delete(TBL_DELETEDKOT, null, null);
    }

    /************************************************************************************************************************************/
    /******************************************************
     * Table - BillDetail
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Bill-----
    public long addBill(BillDetail objBillDetail, String gstin) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_BillingMode, objBillDetail.getBillingMode()); // richa_2012
        cvDbValues.put(KEY_InvoiceNo, objBillDetail.getBillNumber());
        cvDbValues.put("Time", objBillDetail.getTime());
        cvDbValues.put(KEY_GSTIN, gstin);
        cvDbValues.put(KEY_TableNo, objBillDetail.getTableNo());
        cvDbValues.put(KEY_Table_Split_No, objBillDetail.getTableSplitNo());
        cvDbValues.put(KEY_InvoiceDate, objBillDetail.getDate());
        cvDbValues.put(KEY_GrandTotal, objBillDetail.getBillAmount());
        cvDbValues.put("TotalItems", objBillDetail.getTotalItems());
        cvDbValues.put("BillAmount", objBillDetail.getBillAmount());
        cvDbValues.put("TotalDiscountAmount", objBillDetail.getTotalDiscountAmount());
        cvDbValues.put(KEY_DiscPercentage, objBillDetail.getTotalDiscountPercentage());
        cvDbValues.put("TotalServiceTaxAmount", objBillDetail.getTotalServiceTaxAmount());
        cvDbValues.put("TotalTaxAmount", objBillDetail.getTotalTaxAmount());
        cvDbValues.put("CashPayment", objBillDetail.getCashPayment());
        cvDbValues.put("CardPayment", objBillDetail.getCardPayment());
        cvDbValues.put("CouponPayment", objBillDetail.getCouponPayment());
        cvDbValues.put("BillStatus", objBillDetail.getBillStatus());
        cvDbValues.put("ReprintCount", objBillDetail.getReprintCount());
        cvDbValues.put("DeliveryCharge", objBillDetail.getDeliveryCharge());
        cvDbValues.put("EmployeeId", objBillDetail.getEmployeeId());
        cvDbValues.put("UserId", objBillDetail.getUserId());
        cvDbValues.put("CustId", objBillDetail.getCustId());
        cvDbValues.put("PettyCashPayment", objBillDetail.getPettyCashPayment());
        cvDbValues.put(KEY_WalletPayment, objBillDetail.getWalletAmount());
        cvDbValues.put("PaidTotalPayment", objBillDetail.getPaidTotalPayment());
        cvDbValues.put("ChangePayment", objBillDetail.getChangePayment());
        cvDbValues.put(KEY_CustName, objBillDetail.getCustname());
        cvDbValues.put(KEY_CustStateCode, objBillDetail.getCustStateCode());
        cvDbValues.put(KEY_POS, objBillDetail.getPOS());
        cvDbValues.put(KEY_BusinessType, objBillDetail.getBusinessType());
        cvDbValues.put(KEY_TaxableValue, objBillDetail.getAmount());
        cvDbValues.put(KEY_IGSTAmount, objBillDetail.getIGSTAmount());
        cvDbValues.put(KEY_CGSTAmount, objBillDetail.getCGSTAmount());
        cvDbValues.put(KEY_SGSTAmount, objBillDetail.getSGSTAmount());
        cvDbValues.put(KEY_cessAmount, objBillDetail.getCessAmount());
        cvDbValues.put(KEY_SubTotal, objBillDetail.getSubTotal());

        return dbFNB.insert(TBL_BILLDETAIL, null, cvDbValues);
    }

    public long updateBill(BillDetail objBillDetail) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_BillAmount,objBillDetail.getBillAmount());
        return dbFNB.update(TBL_BILLDETAIL,cv,KEY_InvoiceNo+"="+objBillDetail.getBillNumber()+" AND "
                +KEY_InvoiceDate+"="+objBillDetail.getDate()+" AND "+KEY_CustId+" ="+objBillDetail.getCustId(),null);

    }

    // insert bill to inward supply ledger
    public long addBill_inward(String suppliername_str, String supplieraddress_str, String supplierphn, String suppliertype_str,
                               String supplier_gstin, String pos_str1, String invno, String invodate, String additionalCharge,
                               String additionalchargename, String grantotal, String count, String subtotal, String igstamt,
                               String cgstamt, String sgstamt, String taxableValue, String reversecharge) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SupplierPhone, supplierphn);
        cvDbValues.put(KEY_SUPPLIERNAME, suppliername_str);
        cvDbValues.put(KEY_TotalItems, count);
        cvDbValues.put(KEY_SupplierType, suppliertype_str);
        cvDbValues.put(KEY_GSTIN, supplier_gstin);
        cvDbValues.put(KEY_InvoiceNo, invno);
        cvDbValues.put(KEY_InvoiceDate, invodate);
        cvDbValues.put(KEY_AdditionalChargeName, additionalchargename);
        cvDbValues.put(KEY_AdditionalChargeAmount, additionalCharge);
        cvDbValues.put(KEY_TaxableValue, taxableValue);
        cvDbValues.put(KEY_GrandTotal, grantotal);
        cvDbValues.put(KEY_SubTotal, subtotal);
        cvDbValues.put(KEY_POS, pos_str1);
        cvDbValues.put(KEY_IGSTAmount, igstamt);
        cvDbValues.put(KEY_CGSTAmount, cgstamt);
        cvDbValues.put(KEY_SGSTAmount, sgstamt);
        cvDbValues.put(KEY_AttractsReverseCharge, reversecharge);
        //cvDbValues.put(KEY_BusinessType, bussinesstype);

        return dbFNB.insert(TBL_INWARD_SUPPLY_ITEMS_DETAILS, null, cvDbValues);
    }

    /*// -----Retrieve new bill Number-----
    public int getNewBillNumbers() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX( " + KEY_InvoiceNo + " ) FROM " + TBL_BILLDETAIL, null);

        if (result.moveToFirst()) {
            return result.getInt(0) + 1;
        } else {
            return 1;
        }
    }*/
    // -----Retrieve new bill Number-----
    public int getNewBillNumbers() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT " + KEY_InvoiceNo + " FROM BillNoConfiguration", null);

        if (result.moveToFirst()) {
            return result.getInt(0) + 1;
        } else {
            return 1;
        }
    }

    public int getLastBillNoforDate(String date_str) {
        Cursor cursor = dbFNB.rawQuery("SELECT MAX(InvoiceNo) as InvoiceNo FROM "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+" WHERE "+
                KEY_InvoiceDate+" LIKE '"+date_str+"'", null);
        int invno =0;
        if(cursor!=null && cursor.moveToFirst()){
            invno = cursor.getInt(cursor.getColumnIndex(KEY_InvoiceNo));
        }
        return invno;
    }

    public int getLastBillNo() {
        Cursor cursor = dbFNB.rawQuery("SELECT MAX(InvoiceNo) as InvoiceNo FROM "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS, null);
        int invno =0;
        if(cursor!=null && cursor.moveToFirst()){
            invno = cursor.getInt(cursor.getColumnIndex(KEY_InvoiceNo));
        }
        return invno;
    }


    // -----Retrieve Bill setting-----
    public Cursor getBillNoResetSetting() {
        return dbFNB.query("BillNoConfiguration", new String[]{"*"}, null, null, null, null, null);
    }

    public int UpdateBillNoResetInvoiceNo(int invno) {
        cvDbValues = new ContentValues();
        cvDbValues.put("InvoiceNo", invno);

        return dbFNB.update("BillNoConfiguration", cvDbValues, null, null);
    }

    public int UpdateBillNoReset(String period) {
        Date d = new Date();
        CharSequence s = android.text.format.DateFormat.format("dd-MM-yyyy", d.getTime());
        Calendar cal = Calendar.getInstance(); //adding one day to current date cal.add(Calendar.DAY_OF_MONTH, 1); Date tommrrow = cal.getTime();
        cal.add(Calendar.DAY_OF_MONTH, 1);
        Date tommrrow = cal.getTime();
        CharSequence s1 = android.text.format.DateFormat.format("dd-MM-yyyy", tommrrow.getTime());

        cvDbValues = new ContentValues();
        cvDbValues.put("InvoiceNo", 0);
        cvDbValues.put("Period", period);
        cvDbValues.put("PeriodDate", s1.toString());

        return dbFNB.update("BillNoConfiguration", cvDbValues, null, null);
    }

    public int UpdateBillNoResetwithDate(String period, String date , int invoiceNo) {
//        Date d = new Date();
//        CharSequence s = android.text.format.DateFormat.format("dd-MM-yyyy", d.getTime());
//        Calendar cal = Calendar.getInstance(); //adding one day to current date cal.add(Calendar.DAY_OF_MONTH, 1); Date tommrrow = cal.getTime();
//        cal.add(Calendar.DAY_OF_MONTH, 1);
//        Date tommrrow = cal.getTime();
//        CharSequence s1 = android.text.format.DateFormat.format("dd-MM-yyyy", tommrrow.getTime());

        cvDbValues = new ContentValues();
        cvDbValues.put("InvoiceNo", invoiceNo);
        cvDbValues.put("Period", period);
        cvDbValues.put("PeriodDate", date);

        return dbFNB.update("BillNoConfiguration", cvDbValues, null, null);
    }

    public int UpdateBillNoResetPeriod(String period) {
        cvDbValues = new ContentValues();
        cvDbValues.put("Period", period);

        return dbFNB.update("BillNoConfiguration", cvDbValues, null, null);
    }

    // -----Retrieve all Bills Detail-----
    public Cursor getAllBillDetail() {
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"}, null, null, null, null, null);
    }

    // -----Retrieve single bill details-----
    public Cursor getBillDetail(int InvoiceNumber) {
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"}, KEY_InvoiceNo + "=" + InvoiceNumber, null, null, null, null);
    }
    public Cursor getBillDetailByInvoiceDate(long InvoiceDate) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null)
            return db.query(TBL_BILLDETAIL, new String[]{"*"}, KEY_InvoiceDate + "=" + InvoiceDate, null, null, null, null);
        else
            return null;
    }
    public Cursor getBillDetail_counter(int InvoiceNumber) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null)
        return db.query(TBL_BILLDETAIL, new String[]{"*"}, KEY_InvoiceNo + "=" + InvoiceNumber, null, null, null, null);
        else
            return null;
    }

    public Cursor getBillDetail(int InvoiceNumber, String InvoiceDate) {
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"}, KEY_InvoiceNo + "=" + InvoiceNumber+
                " AND "+KEY_InvoiceDate+" LIKE '"+InvoiceDate+"'", null, null, null, null);
    }
    public Cursor getBillDetail_counter(int InvoiceNumber, String InvoiceDate) {
        SQLiteDatabase db = getWritableDatabase();
        if(db!=null)
            return db.query(TBL_BILLDETAIL, new String[]{"*"}, KEY_InvoiceNo + "=" + InvoiceNumber+
                " AND "+KEY_InvoiceDate+" LIKE '"+InvoiceDate+"'", null, null, null, null);
        else
            return null;
    }

    // -----Retrieve single bill details by Customer Id-----
    public Cursor getBillDetailByCustomer(int CustId, int BillStatus, float BillAmount) {
        try {
            Date date1 = new Date();
            CharSequence sdate = android.text.format.DateFormat.format("dd-MM-yyyy", date1.getTime());

            strDate = sdate.toString();
            strDate_date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(strDate);

        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Time = Calendar.getInstance();
        String strTime = String.format("%tR", Time);
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"}, "CustId= '" + CustId + "' AND BillStatus = '" + BillStatus + "' AND BillAmount = '" + BillAmount + "' AND InvoiceDate = '" + strDate_date.getTime() + "' AND Time = '" + strTime + "'", null, null, null, null);
    }


    // -----Retrieve single bill details by Customer Id-----
    public Cursor getBillDetailByCustomerWithTime(int CustId, int BillStatus, float BillAmount ) {
        try {
            Date date1 = new Date();
            CharSequence sdate = android.text.format.DateFormat.format("dd-MM-yyyy", date1.getTime());

            strDate = sdate.toString();
            strDate_date = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).parse(strDate);

        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        /*Time = Calendar.getInstance();
        String strTime = String.format("%tR", Time);*/

        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"}, "CustId= '" + CustId + "' AND BillStatus = '" + BillStatus +
                "' AND BillAmount = '" + BillAmount + "' AND InvoiceDate = '" + strDate_date.getTime() + "' ", null, null, null, null);
    }

    //public Cursor getBillDetailByCustomerWithTime11(int CustId, int BillStatus, float BillAmount ,String time,String strDate ) {


        /*SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        try {
             date = format.parse(strDate);
            System.out.println(date.getTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Cursor cursor = dbFNB.query(TBL_BILLDETAIL, new String[]{"*"}, "CustId= '" + CustId + "' AND BillStatus = '" + BillStatus +
                "' AND BillAmount = '" + BillAmount + "' AND Time = '" + time + "' AND InvoiceDate LIKE '"+date.getTime()+"'", null, null, null, null);
        while(cursor !=null && cursor.moveToNext() && paid < 1 )
        {
            double billamount = cursor.getDouble(cursor.getColumnIndex(KEY_BillAmount));
            float ff = Float.parseFloat(String.format("%.2f",billamount));
            double roundedAmount_database = Math.ceil(ff);
            double roundedAmount_received = Math.ceil(BillAmount);
            //Log.d("roundedAmount_database",String.valueOf(roundedAmount_database));
            //Log.d("roundedAmount_received",String.valueOf(roundedAmount_received));
            if(roundedAmount_database ==roundedAmount_received )
                paid =cursor.getInt(cursor.getColumnIndex(KEY_InvoiceNo));

        }*/
   // }

//    public Cursor getBillDetailByCustomerWithTime1(int CustId, int BillStatus, float BillAmount ,String time) {
//
//        double amt1 = BillAmount-0.5;
//        double amt2 = BillAmount+0.5;
//        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"}, "CustId= '" + CustId + "' AND BillStatus = '" + BillStatus +
//                "' AND BillAmount = '" + BillAmount + "' AND "+KEY_Time +" LIKE '" + time + "' ", null, null, null, null);
//    }

    public int getBillDetailByCustomerWithTime1(int CustId, int BillStatus, double BillAmount ,String time) {

        int paid =0;
        SQLiteDatabase db = this.getReadableDatabase();
        try
        {
            Cursor cursor =  db.query(TBL_BILLDETAIL, new String[]{"*"}, "CustId= '" + CustId + "' AND BillStatus = '" + BillStatus +
                    "'  AND "+KEY_Time +" LIKE '" + time + "' ", null, null, null, null);
            while(cursor !=null && cursor.moveToNext() && paid < 1 )
            {
                double billamount = cursor.getDouble(cursor.getColumnIndex(KEY_BillAmount));
                float ff = Float.parseFloat(String.format("%.2f",billamount));
               double roundedAmount_database = Math.ceil(ff);
               double roundedAmount_received = Math.ceil(BillAmount);
                //Log.d("roundedAmount_database",String.valueOf(roundedAmount_database));
                //Log.d("roundedAmount_received",String.valueOf(roundedAmount_received));
                if(roundedAmount_database ==roundedAmount_received )
                    paid =cursor.getInt(cursor.getColumnIndex(KEY_InvoiceNo));

            }
        }catch (Exception e)
        {
            paid =0;
            e.printStackTrace();
        }
        finally {
            return paid;
        }

    }

    public Cursor getBillDetailByCustomerIdTime(int CustId, int BillStatus, String time,int billingMode  ) {

        Cursor cursor =null;
        SQLiteDatabase db = this.getReadableDatabase();
        try {
             cursor = db.query(TBL_BILLDETAIL, new String[]{"*"}, "CustId= '" + CustId + "' AND BillStatus = '" + BillStatus +
                    "'  AND " + KEY_Time + " LIKE '" + time + "' AND "+KEY_BillingMode+" = "+billingMode, null, null, null, null);

        } catch (Exception e) {
            cursor = null;
            e.printStackTrace();
        } finally {
            return cursor;
        }
    }
    // -----Void Bill-----
    public int makeBillVoids(int InvoiceNo , String InvoiceDate) {
        SQLiteDatabase db ;
        int result = 0;
        try
        {
            db = this.getReadableDatabase();
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_BillStatus, 0);
            result =db.update(TBL_BILLDETAIL, cvDbValues, KEY_InvoiceNo + "=" + InvoiceNo+" AND "+
                    KEY_InvoiceDate+" LIKE '"+InvoiceDate+"'", null);
        }catch (Exception e)
        {
           e.printStackTrace();
            result =0;
        }
        finally {
             return result;
        }

    }
public int makeBillVoid(int InvoiceNo ) {
        SQLiteDatabase db ;
        int result = 0;
        try
        {
            db = this.getReadableDatabase();
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_BillStatus, 0);
            result =db.update(TBL_BILLDETAIL, cvDbValues, KEY_InvoiceNo + "=" + InvoiceNo, null);
        }catch (Exception e)
        {
           e.printStackTrace();
            result =0;
        }
        finally {
             return result;
        }

    }

    // -----update Reprint Count for duplicate bill print-----
    public int updateBillRepintCount(int InvoiceNo) {
        Cursor result = getBillDetails(InvoiceNo);

        if (result.moveToFirst()) {
            cvDbValues = new ContentValues();
            int iReprintCount = 0;
            iReprintCount = result.getInt(result.getColumnIndex("ReprintCount"));
            cvDbValues.put("ReprintCount", iReprintCount + 1);
            return dbFNB.update(TBL_BILLDETAIL, cvDbValues, KEY_InvoiceNo + "=" + InvoiceNo, null);

        } else {
            Toast.makeText(myContext, "No bill found with bill number " + InvoiceNo, Toast.LENGTH_SHORT).show();
            return -1;
        }

    }

    // -----Update Bill Status Delivery Charge, Rider Code of Pending Delivery
    // bill-----
    public int updatePendingDeliveryBill(int InvoiceNo, int EmployeeId, float DeliveryCharge, float CashPayment, float PaidTotalPayment) {
        cvDbValues = new ContentValues();

        cvDbValues.put("EmployeeId", EmployeeId);
        cvDbValues.put("DeliveryCharge", DeliveryCharge);
        cvDbValues.put("BillStatus", 1);
        cvDbValues.put("CashPayment", CashPayment);
        cvDbValues.put("PaidTotalPayment", PaidTotalPayment);

        return dbFNB.update(TBL_BILLDETAIL, cvDbValues, KEY_InvoiceNo + "=" + InvoiceNo, null);
    }
    public int updatePendingDeliveryBill_Ledger(int InvoiceNo, float PaidTotalPayment) {
        cvDbValues = new ContentValues();
        cvDbValues.put("BillStatus", 1);

        String whereClaus = KEY_InvoiceNo + "=" + InvoiceNo+" AND "+KEY_BillStatus+" = 2 ";
        return dbFNB.update(TBL_OUTWARD_SUPPLY_LEDGER, cvDbValues, whereClaus, null);
    }

    /************************************************************************************************************************************/
    /*******************************************************
     * Table - BillItem
     ***********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Bill Items-----
    public long addBillItem(BillItem objBillItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_InvoiceNo, objBillItem.getBillNumber());
        cvDbValues.put(KEY_BillingMode, objBillItem.getBillingMode()); // richa_2012
        cvDbValues.put("ItemNumber", objBillItem.getItemNumber());
        cvDbValues.put("ItemName", objBillItem.getItemName());
        cvDbValues.put("Quantity", objBillItem.getQuantity());
        cvDbValues.put("Value", objBillItem.getValue());
        cvDbValues.put("ModifierAmount", objBillItem.getModifierAmount());
        cvDbValues.put(KEY_TaxableValue, objBillItem.getAmount());
        cvDbValues.put("DiscountAmount", objBillItem.getDiscountAmount());
        cvDbValues.put("DiscountPercent", objBillItem.getDiscountPercent());
        cvDbValues.put("ServiceTaxAmount", objBillItem.getServiceTaxAmount());
        cvDbValues.put("ServiceTaxPercent", objBillItem.getServiceTaxPercent());
        cvDbValues.put("TaxAmount", objBillItem.getTaxAmount());
        cvDbValues.put("TaxPercent", objBillItem.getTaxPercent());
        cvDbValues.put("DeptCode", objBillItem.getDeptCode());
        cvDbValues.put("CategCode", objBillItem.getCategCode());
        cvDbValues.put("KitchenCode", objBillItem.getKitchenCode());
        cvDbValues.put("TaxType", objBillItem.getTaxType());
        //cvDbValues.put("CustId", objBillItem.getCustId());
        //cvDbValues.put(KEY_BusinessType, objBillItem.getBusinessType());
        cvDbValues.put(KEY_InvoiceDate, objBillItem.getInvoiceDate());
        cvDbValues.put(KEY_HSNCode, objBillItem.getHSNCode());
        cvDbValues.put(KEY_IGSTRate, objBillItem.getIGSTRate());
        cvDbValues.put(KEY_IGSTAmount, objBillItem.getIGSTAmount());
        cvDbValues.put(KEY_CGSTRate, objBillItem.getCGSTRate());
        cvDbValues.put(KEY_CGSTAmount, objBillItem.getCGSTAmount());
        cvDbValues.put(KEY_SGSTRate, objBillItem.getSGSTRate());
        cvDbValues.put(KEY_SGSTAmount, objBillItem.getSGSTAmount());
        cvDbValues.put(KEY_cessRate, objBillItem.getCessRate());
        cvDbValues.put(KEY_cessAmount, objBillItem.getCessAmount());
        cvDbValues.put(KEY_SupplyType, objBillItem.getSupplyType());
        cvDbValues.put(KEY_SubTotal, objBillItem.getSubTotal());
        cvDbValues.put(KEY_CustName, objBillItem.getCustName());
        cvDbValues.put(KEY_CustStateCode, objBillItem.getCustStateCode());
        cvDbValues.put(KEY_UOM, objBillItem.getUom());
        cvDbValues.put(KEY_BusinessType, objBillItem.getBusinessType());
        cvDbValues.put(KEY_BillStatus, objBillItem.getBillStatus());

        return dbFNB.insert(TBL_BILLITEM, null, cvDbValues);
    }


    // -----Insert Bill Items-----
    public long addBillItem_inward(BillItem objBillItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SUPPLIERNAME, objBillItem.getSupplierName());
        cvDbValues.put(KEY_SupplierCode, objBillItem.getSuppliercode());
        cvDbValues.put(KEY_SupplierPhone, objBillItem.getSupplierPhone());

        cvDbValues.put(KEY_InvoiceNo, objBillItem.getBillNumber());
        cvDbValues.put(KEY_InvoiceDate, objBillItem.getInvoiceDate());

        cvDbValues.put(KEY_MenuCode, objBillItem.getItemNumber());
        cvDbValues.put(KEY_SupplyType, objBillItem.getSupplyType());
        cvDbValues.put(KEY_ItemName, objBillItem.getItemName());
        cvDbValues.put(KEY_Quantity, objBillItem.getQuantity());
        cvDbValues.put(KEY_UOM, objBillItem.getUom());
        //cvDbValues.put(KEY_Rate, objBillItem.getRate());
        cvDbValues.put(KEY_Value, objBillItem.getValue());
        cvDbValues.put(KEY_TaxableValue, objBillItem.getTaxableValue());
        cvDbValues.put(KEY_SalesTax, objBillItem.getTaxAmount());
        cvDbValues.put(KEY_ServiceTaxAmount, objBillItem.getServiceTaxAmount());
        cvDbValues.put(KEY_Amount, objBillItem.getAmount());


        cvDbValues.put(KEY_SupplierType, objBillItem.getSupplierType());
        cvDbValues.put(KEY_GSTIN, objBillItem.getGSTIN());


        //  cvDbValues.put(KEY_BusinessType, objBillItem.getBusinessType());
        cvDbValues.put(KEY_HSNCode, objBillItem.getHSNCode());
        cvDbValues.put(KEY_IGSTRate, objBillItem.getIGSTRate());
        cvDbValues.put(KEY_IGSTAmount, objBillItem.getIGSTAmount());
        cvDbValues.put(KEY_CGSTRate, objBillItem.getCGSTRate());
        cvDbValues.put(KEY_CGSTAmount, objBillItem.getCGSTAmount());
        cvDbValues.put(KEY_SGSTRate, objBillItem.getSGSTRate());
        cvDbValues.put(KEY_SGSTAmount, objBillItem.getSGSTAmount());
        cvDbValues.put(KEY_SubTotal, objBillItem.getSubTotal());
        cvDbValues.put(KEY_TaxationType, objBillItem.getTaxationType());

        return dbFNB.insert(TBL_INWARD_SUPPLY_LEDGER, null, cvDbValues);
    }

    // -----Retrieve single bill items-----
    public Cursor getBillItems(int InvoiceNo) {
        return dbFNB.query(TBL_BILLITEM, new String[]{"*"}, KEY_InvoiceNo + "=" + InvoiceNo, null, null, null, null);
    }

    /************************************************************************************************************************************/
    /***********************************************
     * Table - ComplimentaryBillDetail
     ****************************************************/
    /************************************************************************************************************************************/
    // -----Insert Complimentary Bill-----
    public long addComplimentaryBillDetail(ComplimentaryBillDetail objComplimentaryBillDetail) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_InvoiceNo, objComplimentaryBillDetail.getBillNumber());
        cvDbValues.put("ComplimentaryReason", objComplimentaryBillDetail.getComplimentaryReason());
        cvDbValues.put("PaidAmount", objComplimentaryBillDetail.getPaidAmount());

        return dbFNB.insert(TBL_COMPLIMENTARYBILLDETAIL, null, cvDbValues);
    }

    /************************************************************************************************************************************/
    /****************************************************
     * Table - RiderSettlement
     *******************************************************/
    /************************************************************************************************************************************/
    // ************************* Table - RiderSettlement
    // *************************

    // -----Insert Rider delivery order-----
    public long addRiderSettlement(RiderSettlement objRiderSettlement) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_InvoiceNo, objRiderSettlement.getBillNumber());
        cvDbValues.put("TotalItems", objRiderSettlement.getTotalItems());
        cvDbValues.put("BillAmount", objRiderSettlement.getBillAmount());
        cvDbValues.put("EmployeeId", objRiderSettlement.getEmployeeId());
        cvDbValues.put("PettyCash", objRiderSettlement.getPettyCash());
        cvDbValues.put("SettledAmount", objRiderSettlement.getSettledAmount());
        cvDbValues.put("DeliveryCharge", objRiderSettlement.getDeliveryCharge());
        cvDbValues.put("CustId", objRiderSettlement.getCustId());

        return dbFNB.insert(TBL_RIDERSETTLEMENT, null, cvDbValues);
    }

    // -----Retrieve pending rider delivery orders-----
    public Cursor getRiderPendingDelivery() {
        return dbFNB.query(TBL_RIDERSETTLEMENT, new String[]{"*"}, "SettledAmount=0", null, null, null, null);
    }

    // -----Retrieve pending rider delivery orders-----
    public Cursor getRiderSettlementByCustId(int CustId) {
        return dbFNB.query(TBL_RIDERSETTLEMENT, new String[]{"*"}, "SettledAmount='0' AND CustId = '" + CustId + "'", null, null, null, null);
    }

    // -----Retrieve delivery charge for bill-----
    public double getDeliveryCharge(String strBillNumber) {
        Cursor result = dbFNB.query(TBL_RIDERSETTLEMENT, new String[]{"DeliveryCharge"},
                KEY_InvoiceNo + "=" + strBillNumber, null, null, null, null);
        if (result.moveToFirst()) {

            return result.getDouble(result.getColumnIndex("DeliveryCharge"));
        }
        return -1;
    }

    // -----Update pending rider delivery-----
    public int updateRiderPendingDelivery(int InvoiceNo, float SettledAmount) {
        cvDbValues = new ContentValues();

        cvDbValues.put("SettledAmount", SettledAmount);

        return dbFNB.update(TBL_RIDERSETTLEMENT, cvDbValues, KEY_InvoiceNo + "=" + InvoiceNo, null);
    }

    /************************************************************************************************************************************/
    /************************************************************ Reports ***************************************************************/
    /************************************************************************************************************************************/

    // -----Bill wise and Transaction Report-----
    public Cursor getBillDetailReport(String StartDate, String EndDate) {
        /*return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"},
                "BillStatus=1 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null,
                KEY_InvoiceNo);*/

        //String QUERY_REPORT = "Select * from "+TBL_BILLDETAIL+" where "+"BillStatus=1 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'";
        //return dbFNB.rawQuery(QUERY_REPORT,null);
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"},
                " BillStatus=1 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null,KEY_InvoiceDate);
    }


    public Cursor getoutwardStock(String Date) {
        Cursor cursor = null;
        cursor =dbFNB.query(TBL_StockOutward, new String []{"*"},KEY_BusinessDate+" LIKE '"+Date+"'",null, null,null,null,null);
        return cursor;
    }

    public Cursor getinwardStock(String Date) {
        Cursor cursor = null;
        cursor =dbFNB.query(TBL_StockInward, new String []{"*"},KEY_BusinessDate+" LIKE '"+Date+"'",null, null,null,null,null);
        return cursor;
    }
    // richa_2012
    // -----Bill wise and Transaction Report-----
    public Cursor getBillingReport(String StartDate, String EndDate, int billingMode) {
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"},
                "BillStatus=1 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "' AND " + KEY_BillingMode + " LIKE '"
                        + billingMode + "'", null, null, null, KEY_InvoiceDate);
    }
    public Cursor getStockOutwardForBusinessdate(String businessDate) {
        return dbFNB.query(TBL_StockOutward, new String[]{"*"}, KEY_BusinessDate+" LIKE '"+businessDate+"'",
                null, null, null, null);
    }

    public long insertStockOutward(ItemStock item, String businessDate_str) {
        long l = 0;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_BusinessDate, businessDate_str);
        cvdbValues.put(KEY_MenuCode,item.getMenuCode());
        cvdbValues.put(KEY_ItemName, item.getItemName());
        cvdbValues.put(KEY_OpeningStock, item.getOpeningStock());
        cvdbValues.put(KEY_ClosingStock, item.getClosingStock());
        cvdbValues.put(KEY_Rate, item.getRate());
        l = dbFNB.insert(TBL_StockOutward, null, cvdbValues);
        return l;
    }

    public long updateClosingStockOutward(ItemStock item, String businessDate_str) {
        long l = 0;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_ClosingStock, item.getClosingStock());
        l = dbFNB.update(TBL_StockOutward, cvdbValues, KEY_MenuCode+" ="+item.getMenuCode()+" AND "+
                KEY_BusinessDate+" LIKE '"+businessDate_str+"'", null);
        return l;
    }

    public long updateOpeningStockOutward(ItemStock item, String businessDate_str) {
        long l = 0;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_OpeningStock, item.getOpeningStock());
        l = dbFNB.update(TBL_StockOutward, cvdbValues, KEY_MenuCode+" ="+item.getMenuCode()+" AND "+
                KEY_BusinessDate+" LIKE '"+businessDate_str+"'", null);
        return l;
    }

    public Cursor getOutwardStockItem(String currentdate,int MenuCode) {
        Cursor cursor =null;
        cursor =  dbFNB.query(TBL_StockOutward, new String[]{"*"}, KEY_BusinessDate+" LIKE '"+currentdate+"' AND "+
                KEY_MenuCode+" = "+MenuCode, null, null, null, null);
        return cursor;
    }
    public Cursor getOutwardStockItem_counter(String currentdate,int MenuCode) {
        Cursor cursor =null;
        SQLiteDatabase db = getWritableDatabase();
        try{
            if(db!=null)
            {
                cursor =  db.query(TBL_StockOutward, new String[]{"*"}, KEY_BusinessDate+" LIKE '"+currentdate+"' AND "+
                        KEY_MenuCode+" = "+MenuCode, null, null, null, null);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            return cursor;
        }

    }
    public int clearOutwardStock(String currentdate) {
       int del =0;
        del =  dbFNB.delete(TBL_StockOutward, KEY_BusinessDate+" LIKE '"+currentdate+"' ",null);
        return del;
    }


    public Cursor getInwardStockItem(String currentdate,int MenuCode) {
        Cursor cursor =null;
        cursor =  dbFNB.query(TBL_StockInward, new String[]{"*"}, KEY_BusinessDate+" LIKE '"+currentdate+"' AND "+
                KEY_MenuCode+" = "+MenuCode, null, null, null, null);
        return cursor;
    }

    public Cursor getStockInwardForBusinessdate(String businessDate) {
        return dbFNB.query(TBL_StockInward, new String[]{"*"}, KEY_BusinessDate+" LIKE '"+businessDate+"'",
                null, null, null, null);
    }
    public long insertStockInward(ItemStock item, String businessDate_str) {
        long l = 0;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_BusinessDate, businessDate_str);
        cvdbValues.put(KEY_MenuCode,item.getMenuCode());
        cvdbValues.put(KEY_ItemName, item.getItemName());
        cvdbValues.put(KEY_OpeningStock, item.getOpeningStock());
        cvdbValues.put(KEY_ClosingStock, item.getClosingStock());
        cvdbValues.put(KEY_Rate, item.getRate());
        l = dbFNB.insert(TBL_StockInward, null, cvdbValues);
        return l;
    }

    public Cursor getInwardStock(String itemname) {
        Cursor cursor = null;
        cursor =dbFNB.query(TBL_StockInward, new String []{"*"},KEY_ItemName+" LIKE '"+itemname+"'",null, null,null,null,null);
        return cursor;
    }

    public long updateOpeningStockInward(ItemStock item, String businessDate_str) {
        long l = 0;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_OpeningStock, item.getOpeningStock());
        l = dbFNB.update(TBL_StockInward, cvdbValues, KEY_MenuCode+" ="+item.getMenuCode()+" AND "+
                KEY_BusinessDate+" LIKE '"+businessDate_str+"'", null);
        return l;
    }

    public long updateClosingStockInward(ItemStock item, String businessDate_str) {
        long l = 0;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_ClosingStock, item.getClosingStock());
        l = dbFNB.update(TBL_StockInward, cvdbValues, KEY_MenuCode+" ="+item.getMenuCode()+" AND "+
                KEY_BusinessDate+" LIKE '"+businessDate_str+"'", null);
        return l;
    }

    // -----Tax Report-----
    /*public Cursor getBillsforTaxReports(String StartDate, String EndDate)
    {
        String selectQuery = "Select "+KEY_InvoiceNo+" , "+KEY_InvoiceDate+" FROM "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+
                " WHERE "+KEY_BillStatus+" = 1 AND "+KEY_InvoiceDate+" BETWEEN '"+StartDate+"' AND '"+EndDate+"'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }*/
    public Cursor getTaxDetailforBill(String Invoiceno, String Invoicedate, String taxPercentName, String taxAmountName)
    {
        String selectQuery = "Select "+taxPercentName+" , "+taxAmountName+" ,"+KEY_TaxableValue+","+KEY_DiscountAmount+" FROM "+TBL_OUTWARD_SUPPLY_LEDGER+
                " WHERE "+KEY_InvoiceNo+" LIKE '"+Invoiceno+"' AND "+KEY_InvoiceDate+" LIKE '"+Invoicedate+"'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }
    public Cursor getTaxReport(String StartDate, String EndDate) {
        /*return dbFNB.rawQuery("SELECT * FROM BillDetail, BillItem, TaxConfig WHERE BillDetail.BillStatus=1 AND "
                + "BillItem.BillNumber=BillDetail.BillNumber AND BillItem.TaxPercent=TaxConfig.TaxPercentage AND "
                + "BillDetail.InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);*/

        // richa
       /* return dbFNB.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + " , " + TBL_BILLITEM + " , TaxConfig WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                + TBL_BILLITEM + ".InvoiceNo= " + TBL_BILLDETAIL + ".InvoiceNo AND " *//*+ TBL_BILLITEM + ".TaxPercent=TaxConfig.TaxPercentage AND "*//*
                + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);*/

       /* return dbFNB.rawQuery("SELECT * FROM OutwardSuppyItemsDetails ,OutwardSupplyLedger,TaxConfig WHERE OutwardSuppyItemsDetails.BillStatus=1 AND\n" +
                "OutwardSuppyItemsDetails.InvoiceNo=OutwardSupplyLedger.InvoiceNo AND\n" +
                "OutwardSuppyItemsDetails.InvoiceDate BETWEEN '"+StartDate+"' AND '"+EndDate+"'", null);*/
        return dbFNB.rawQuery("SELECT  CGSTRate,CGSTAmount,Value FROM OutwardSuppyItemsDetails,OutwardSupplyLedger  WHERE OutwardSuppyItemsDetails.BillStatus =1 " +
                " AND OutwardSuppyItemsDetails.InvoiceNo = OutwardSupplyLedger.InvoiceNo " +
                " AND OutwardSuppyItemsDetails.InvoiceDate BETWEEN '"+StartDate+"' AND '"+EndDate+"'", null);
    }

    public Cursor getTaxReport_Service(String StartDate, String EndDate) {

        return dbFNB.rawQuery("SELECT  ServiceTaxPercent,ServiceTaxAmount,Value FROM OutwardSuppyItemsDetails,OutwardSupplyLedger WHERE OutwardSuppyItemsDetails.BillStatus =1 " +
                " AND OutwardSuppyItemsDetails.InvoiceNo = OutwardSupplyLedger.InvoiceNo " +
                " AND OutwardSuppyItemsDetails.InvoiceDate BETWEEN '"+StartDate+"' AND '"+EndDate+"'", null);
    }




    // -----Service Tax Report-----
    public Cursor getServiceTaxReport(String StartDate, String EndDate) {

        /*return dbFNB.rawQuery("SELECT * FROM BillDetail, BillItem WHERE BillDetail.BillStatus=1 AND "
                + "BillItem.BillNumber=BillDetail.BillNumber AND BillDetail.InvoiceDate BETWEEN '" + StartDate + "' AND '"
                + EndDate + "'", null);*/

        // richa

        return dbFNB.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + " , " + TBL_BILLITEM + " WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                + TBL_BILLITEM + ".InvoiceNo= " + TBL_BILLDETAIL + ".InvoiceNo AND " + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '"
                + EndDate + "'", null);
    }

    // -----Void bills Report-----
    public Cursor getVoidBillsReport(String StartDate, String EndDate) {
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"},
                "BillStatus=0 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null,
                "InvoiceNo");
    }

    // -----Duplicate bill Report-----
    public Cursor getDuplicateBillsReport(String StartDate, String EndDate) {
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"},
                "ReprintCount>0 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null,
                "InvoiceNo");
    }

    // -----KOT Pending Report-----
    public Cursor getKOTPendingReport() {
        return dbFNB.query(TBL_PENDINGKOT, new String[]{"*"}, "OrderMode=1", null, null, null, "TokenNumber");
    }

    // -----KOT Deleted Report-----
    public Cursor getKOTDeletedReport() {
        return dbFNB.query(TBL_DELETEDKOT, new String[]{"*"}, null, null, null, null, "TokenNumber");
    }

    // -----Item wise Report-----
    public Cursor getItemwiseReport(String StartDate, String EndDate) {
        /*return dbFNB.rawQuery("SELECT * FROM BillItem, BillDetail WHERE BillDetail.BillStatus=1 AND "
                + "BillItem.BillNumber=BillDetail.BillNumber AND BillDetail.InvoiceDate BETWEEN '" + StartDate + "' AND '"
                + EndDate + "'", null);*/
        // richa
        return dbFNB.rawQuery("SELECT * FROM " + TBL_BILLITEM + " , " + TBL_BILLDETAIL + " WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                + TBL_BILLITEM + ".InvoiceNo= " + TBL_BILLDETAIL + ".InvoiceNo AND " + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '"
                + EndDate + "' ORDER BY ItemNumber ASC", null);
    }

    // -----Fast Selling Item wise Report-----
    /*public Cursor getFastSellingItemwiseReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery("SELECT sum(Quantity) as Qty, * FROM " + TBL_BILLITEM + " , Category, Department, " + TBL_BILLDETAIL +
                " WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND " + TBL_BILLITEM + ".InvoiceNo= " + TBL_BILLDETAIL + ".InvoiceNo AND Category.CategCode = " +
                TBL_BILLITEM + ".CategCode AND " + "Department.DeptCode = " + TBL_BILLITEM + ".DeptCode AND " +
                TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "' Group By ItemName Order by Qty desc LIMIT 10", null);
    }*/
    public Cursor getFastSellingItemwiseReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery("SELECT  * FROM " + TBL_BILLITEM +
                " WHERE BillStatus=1 AND " +
                TBL_OUTWARD_SUPPLY_LEDGER + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "' ", null);
    }
    // -----Day wise and Month wise Report-----
    public Cursor getDaywiseReport(String StartDate, String EndDate) {
        return dbFNB.query(TBL_BILLDETAIL, new String[]{"*"},
                "BillStatus=1 AND InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null, null, null, "InvoiceDate");
    }

    // -----Department wise Report-----
    public Cursor getDepartmentwiseReport(String StartDate, String EndDate) {
        /*return dbFNB.rawQuery("SELECT * FROM BillItem, BillDetail, Department WHERE BillDetail.BillStatus=1 AND "
                + "BillItem.DeptCode=Department.DeptCode AND BillItem.BillNumber=BillDetail.BillNumber AND BillDetail.InvoiceDate BETWEEN '"
                + StartDate + "' AND '" + EndDate + "'", null);*/
        // richa
        return dbFNB.rawQuery("SELECT * FROM " + TBL_BILLITEM + " , " + TBL_BILLDETAIL + " , Department WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                + TBL_BILLITEM + ".DeptCode=Department.DeptCode AND " + TBL_BILLITEM + ".InvoiceNo= " + TBL_BILLDETAIL + ".InvoiceNo AND " +
                TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
    }

    // -----Category wise Report-----
    public Cursor getCategorywiseReport(String StartDate, String EndDate) {
       /* return dbFNB.rawQuery("SELECT * FROM BillItem, BillDetail, Category WHERE BillDetail.BillStatus=1 AND "
                + "BillItem.CategCode=Category.CategCode AND BillItem.BillNumber=BillDetail.BillNumber AND BillDetail.InvoiceDate BETWEEN '"
                + StartDate + "' AND '" + EndDate + "'", null);*/
        // richa
        return dbFNB.rawQuery("SELECT * FROM " + TBL_BILLITEM + " , " + TBL_BILLDETAIL + " , Category WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                + TBL_BILLITEM + ".CategCode=Category.CategCode AND " + TBL_BILLITEM + ".InvoiceNo= " + TBL_BILLDETAIL + ".InvoiceNo AND " +
                TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
    }

    // -----Kitchen wise Report-----
    public Cursor getKitchenwiseReport(String StartDate, String EndDate) {
        /*return dbFNB.rawQuery("SELECT * FROM BillItem, BillDetail, Kitchen WHERE BillDetail.BillStatus=1 AND "
                + "BillItem.KitchenCode=Kitchen.KitchenCode AND BillItem.BillNumber=BillDetail.BillNumber AND BillDetail.InvoiceDate BETWEEN '"
                + StartDate + "' AND '" + EndDate + "'", null);*/
        // richa
        return dbFNB.rawQuery("SELECT * FROM " + TBL_BILLITEM + ", " + TBL_BILLDETAIL + ", Kitchen WHERE  " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                + TBL_BILLITEM + ".KitchenCode=Kitchen.KitchenCode AND " + TBL_BILLITEM + ".InvoiceNo= " + TBL_BILLDETAIL + ".InvoiceNo AND " +
                TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
    }

    // -----Waiter wise Report-----
    public Cursor getWaiterwiseReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery(
                "SELECT * FROM " + TBL_BILLDETAIL + ", "+TBL_USERS+" WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                        + TBL_BILLDETAIL + "."+KEY_EmployeeId+"="+TBL_USERS+"."+KEY_USER_ID+" AND " + TBL_BILLDETAIL + ".EmployeeId>0 AND "
                        + KEY_ROLE_ID+" LIKE '3' AND " + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'",
                null);
    }

    public Cursor getSupplierwiseReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery(
                "SELECT SupplierCode, SupplierName, Amount, AdditionalChargeAmount FROM " + TBL_PURCHASEORDER +" WHERE "+
                        KEY_isGoodinward+"=1 AND "+KEY_InvoiceDate+" BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
    }

    // -----Waiter detailed Report-----
    public Cursor getWaiterDetailedReport(int WaiterId, String StartDate, String EndDate) {
        Cursor cursor = null;
        try
        {
            if(!dbFNB.isOpen())
                dbFNB = this.getReadableDatabase();
            cursor = dbFNB.rawQuery(
                    "SELECT * FROM " + TBL_BILLDETAIL + " , "+TBL_USERS+" WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                            + TBL_BILLDETAIL + ".EmployeeId= "+TBL_USERS+".UserId AND  " + TBL_BILLDETAIL + ".EmployeeId=" + WaiterId + " AND "
                            + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'",
                    null);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return cursor;
    }

    // -----Rider wise Report-----
    public Cursor getRiderwiseReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery("SELECT * FROM  " + TBL_BILLDETAIL + " , "+TBL_USERS+" WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                        + TBL_BILLDETAIL + ".EmployeeId= "+TBL_USERS+".UserId AND " + TBL_BILLDETAIL + ".EmployeeId>0 AND "
                        + KEY_ROLE_ID+" LIKE '4' AND "  + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'",
                null);
    }

    // -----Rider detailed Report-----
    public Cursor getRiderDetailedReport(int RiderId, String StartDate, String EndDate) {
        Cursor cursor = null;
        try {
            if(!dbFNB.isOpen())
                dbFNB = this.getReadableDatabase();
            cursor = dbFNB.rawQuery(
                    "SELECT * FROM " + TBL_BILLDETAIL + " , "+TBL_USERS+" WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                            + TBL_BILLDETAIL + ".EmployeeId = "+TBL_USERS+".UserId AND " + TBL_BILLDETAIL + ".EmployeeId=" + RiderId + " AND "
                            + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'",
                    null);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return cursor;
    }

    // -----User wise Report-----
    public Cursor getUserwiseReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + ", "+TBL_USERS+" WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                        + TBL_BILLDETAIL + ".UserId=Users.UserId AND " + TBL_BILLDETAIL + ".InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'",
                null);
    }

    // -----User detailed Report-----
    public Cursor getUserDetailedReport(String UserId, String StartDate, String EndDate) {
        Cursor cursor = null;
        try
        {
            if(!dbFNB.isOpen())
                dbFNB = this.getReadableDatabase();
            cursor = dbFNB.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + ", Users WHERE " + TBL_BILLDETAIL + ".BillStatus=1 AND "
                    + TBL_BILLDETAIL + ".UserId='" + UserId + "' AND " + TBL_BILLDETAIL + ".UserId=Users.UserId AND "
                    + TBL_BILLDETAIL + "." + KEY_InvoiceDate + "  BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return cursor;
    }

    // -----Customer wise Report-----
    public Cursor getCustomerwiseReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery("SELECT * FROM " + TBL_BILLDETAIL + ", Customer WHERE OutwardSuppyItemsDetails.BillStatus=1 AND "
                + "OutwardSuppyItemsDetails.CustId>0 AND OutwardSuppyItemsDetails.CustId=Customer.CustId AND " + "OutwardSuppyItemsDetails.InvoiceDate BETWEEN '"
                + StartDate + "' AND '" + EndDate + "'", null);
    }

    // -----Customer Detailed Report-----
    public Cursor getCustomerDetailedReport(int CustId, String StartDate, String EndDate) {
        Cursor cursor = null;
        try{
            if (!dbFNB.isOpen())
                dbFNB = this.getReadableDatabase();
            cursor =  dbFNB.rawQuery("SELECT * FROM OutwardSuppyItemsDetails, Customer WHERE OutwardSuppyItemsDetails.BillStatus=1 AND "
                    + "OutwardSuppyItemsDetails.CustId=" + CustId + " AND Customer.CustId=" + CustId + " AND "
                    + "OutwardSuppyItemsDetails.InvoiceDate BETWEEN '" + StartDate + "' AND '" + EndDate + "'", null);
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        return cursor;
    }

    // -----Payment Report-----
    public Cursor getPaymentReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery("SELECT * FROM PaymentReceipt, Description WHERE PaymentReceipt.BillType=1 AND "
                + "PaymentReceipt.DescriptionId1=Description.DescriptionId AND " + "PaymentReceipt.InvoiceDate BETWEEN '"
                + StartDate + "' AND '" + EndDate + "'", null);

    }

    // -----Receipt Report-----
    public Cursor getReceiptReport(String StartDate, String EndDate) {
        return dbFNB.rawQuery("SELECT * FROM PaymentReceipt, Description WHERE PaymentReceipt.BillType=2 AND "
                + "PaymentReceipt.DescriptionId1=Description.DescriptionId AND " + "PaymentReceipt.InvoiceDate BETWEEN '"
                + StartDate + "' AND '" + EndDate + "'", null);


    }

    // -----Retrieve all Reports Name -----
    public Cursor getAllReportsName() {
        return dbFNB.query(TBL_REPORTSMASTER, new String[]{"ReportsId", "ReportsName", "Status"}, null, null, null, null,
                "ReportsName ASC");
    }

    public List<String> getAllItemsName() {
        List<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  ItemName FROM " + TBL_ITEM_Outward;
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);// selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(KEY_ItemName)));// adding
                // 2nd
                // column
                // data
            } while (cursor.moveToNext());
        }
        // returning lables
        return list;
    }

    public long saveSupplierDetails(String supplierType_str, String suppliergstin_str, String suppliername_str,
                                    String supplierphn_str, String supplieraddress_str) {
        long l = 0;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_SupplierType, supplierType_str);
        cvdbValues.put(KEY_GSTIN, suppliergstin_str);
        cvdbValues.put(KEY_SUPPLIERNAME, suppliername_str);
        cvdbValues.put(KEY_SupplierPhone, supplierphn_str);
        cvdbValues.put(KEY_SupplierAddress, supplieraddress_str);
        l = dbFNB.insert(TBL_Supplier, null, cvdbValues);
        return l;
    }
    public long updateSupplierDetails(String supplierType_str, String suppliergstin_str, String suppliername_str,
                                    String supplierphn_str, String supplieraddress_str, int suppliercode) {
        long l = 0;
        String whereclause = KEY_SupplierCode+"="+suppliercode;
        ContentValues cvdbValues = new ContentValues();
        cvdbValues.put(KEY_SupplierType, supplierType_str);
        cvdbValues.put(KEY_GSTIN, suppliergstin_str);
        cvdbValues.put(KEY_SUPPLIERNAME, suppliername_str);
        cvdbValues.put(KEY_SupplierPhone, supplierphn_str);
        cvdbValues.put(KEY_SupplierAddress, supplieraddress_str);
        l = dbFNB.update(TBL_Supplier,cvdbValues,KEY_SupplierCode+"="+suppliercode,null );
        return l;
    }

    public int getSuppliercode(String suppliername) {
        int code = -1;
        String selectQuery = "Select " + KEY_SupplierCode + " FROM " + TBL_Supplier + " WHERE " + KEY_SUPPLIERNAME + " LIKE '" + suppliername + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            code = cursor.getInt(cursor.getColumnIndex(KEY_SupplierCode));
        }
        return code;

    }

    public Cursor getSupplierDetails(String suppliername) {
        String selectquery = "Select * FROM " + TBL_Supplier + " WHERE " + KEY_SUPPLIERNAME + " LIKE '" + suppliername + "'";
        Cursor cursor = dbFNB.rawQuery(selectquery, null);
        return cursor;
    }

    // richa 2712_inward


    // -----Update item quantity in item outward as per ingredient management-----
    public int updateItemQuantity(int menucode, float item_quantity) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_Quantity, item_quantity);

        return dbFNB.update(TBL_ITEM_Outward, cvDbValues, KEY_MenuCode + "=" + menucode, null);
    }

    // -----Update ingredient quantity in GoodsInward as per ingredient management-----
    public int updateIngredientQuantityInGoodsInward(String ingredientname, float ingredientquantity) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_Quantity, ingredientquantity);

        return dbFNB.update(TBL_GOODSINWARD, cvDbValues, KEY_ItemName + " LIKE '" + ingredientname + "'", null);
    }
    public long updateSupplierCountInGoodsInward(String ingredientname, int count) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_SupplierCount, count);
        return dbFNB.update(TBL_GOODSINWARD, cvDbValues, KEY_ItemName + " LIKE '" + ingredientname + "'", null);
    }

    public int updateIngredientQuantityInGoodsInward(String ingredientname, double ingredientquantity, double rate) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_Quantity, ingredientquantity);
        cvDbValues.put(KEY_Value, rate);

        return dbFNB.update(TBL_GOODSINWARD, cvDbValues, KEY_ItemName + " LIKE '" + ingredientname + "'", null);
    }

    public long addLinkage(String supplierCode_str, String  menuCode_str)
    {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_MenuCode, menuCode_str);
        cvDbValues.put(KEY_SupplierCode, supplierCode_str);
        return  dbFNB.insert(TBL_SupplierItemLinkage, null, cvDbValues);
    }
    public long deleteLinkage(String supplierCode_str, String  menuCode_str)
    {
        String deleteClause = KEY_SupplierCode+" = "+supplierCode_str+" AND "+KEY_MenuCode+" = "+menuCode_str;
        return  dbFNB.delete(TBL_SupplierItemLinkage, deleteClause,null);

    }
    public Cursor isLinked(int supplierCode, int  menuCode)
    {
        String selectquery = "Select * FROM " + TBL_SupplierItemLinkage + " WHERE " + KEY_SupplierCode + " = "
                + supplierCode + " AND "+KEY_MenuCode+" = "+menuCode;
        Cursor cursor = dbFNB.rawQuery(selectquery, null);
        return cursor;
    }

    // -----Retrieve Single Item based on Item MenuCode-----
    public Cursor getItem_inward(int MenuCode) {
        return dbFNB.query(TBL_ITEM_Inward, new String[]{"*"}, "MenuCode=" + MenuCode, null, null, null, null);
    }

    public Cursor getItemDetail_inward(String itemName) {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_ITEM_Inward + " WHERE " + KEY_ItemName + " LIKE '" + itemName + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);// selectQuery,selectedArgument
        return cursor;
    }

    public Cursor getItemdetailsforSupplier(int suppliercode, String itemname) {
        String selectquery = "Select * FROM " + TBL_ITEM_Inward + " WHERE " + KEY_ItemName + " LIKE '" + itemname + "' AND "
                + KEY_SupplierCode + " = " + suppliercode;
        Cursor cursor = dbFNB.rawQuery(selectquery, null);
        return cursor;
    }

    public Cursor getSupplierDetails_forcode(int supplierCode) {
        String selectquery = "Select * FROM " + TBL_Supplier + " WHERE " + KEY_SupplierCode + " LIKE '" + supplierCode + "'";
        Cursor cursor = dbFNB.rawQuery(selectquery, null);
        return cursor;
    }

    public Cursor getSupplierDetails_nonGST(String suppliername) {
        String selectquery = "Select * FROM " + TBL_Supplier + " WHERE " + KEY_SUPPLIERNAME + " LIKE '" + suppliername + "'";
        Cursor cursor = dbFNB.rawQuery(selectquery, null);
        return cursor;
    }

    public int updateItem_Inw_nonGST(Item objItem) {

        cvDbValues = new ContentValues();

        cvDbValues.put("ItemName", objItem.getItemname());
        cvDbValues.put(KEY_HSNCode, objItem.getHSNCode());
        cvDbValues.put(KEY_MenuCode, objItem.getMenuCode());
        cvDbValues.put(KEY_SUPPLIERNAME, objItem.getsupplierName());
        cvDbValues.put(KEY_SupplierCode, objItem.getsuppliercode());
        cvDbValues.put(KEY_ItemBarcode, objItem.getItemBarcode());
        cvDbValues.put(KEY_Rate, objItem.getRate());
        //cvDbValues.put(KEY_Quantity, objItem.getQuantity());
        cvDbValues.put(KEY_UOM, objItem.getMOU());
        cvDbValues.put(KEY_SalesTaxPercent, objItem.getSalesTaxPercent());
        cvDbValues.put(KEY_ServiceTaxPercent, objItem.getServiceTaxPercent());
        cvDbValues.put("ImageUri", objItem.getImageUri());
        cvDbValues.put(KEY_SupplyType, objItem.getSupplyType());
        cvDbValues.put(KEY_AverageRate, objItem.getAverageRate());
        cvDbValues.put(KEY_Count, objItem.getCount());

        return dbFNB.update(TBL_ITEM_Inward, cvDbValues, "MenuCode=" + objItem.getMenuCode(), null);
    }

    public long resetStock_inward(int supplierCode, int menuCode,String itemName) {

        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_Quantity, 0);
        String whereClause = KEY_MenuCode+" = "+menuCode+" AND "+KEY_ItemName+" LIKE '"+itemName+"' AND "+KEY_SupplierCode+" = "+supplierCode;
        return dbFNB.update(TBL_ITEM_Inward, cvDbValues, whereClause, null);
    }

    public long addItem_Inw_nonGST(Item objItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SupplierCode, objItem.getsuppliercode());
        cvDbValues.put(KEY_SUPPLIERNAME, objItem.getsupplierName());
        cvDbValues.put(KEY_ItemName, objItem.getItemname());
        cvDbValues.put(KEY_HSNCode, objItem.getHSNCode());
        cvDbValues.put("ItemBarcode", objItem.getItemBarcode());
        cvDbValues.put(KEY_CGSTRate, objItem.getCGSTRate());
        cvDbValues.put(KEY_SGSTRate, objItem.getSGSTRate());
        cvDbValues.put(KEY_IGSTRate, objItem.getIGSTRate());
        cvDbValues.put("Quantity", objItem.getQuantity());
        cvDbValues.put(KEY_Rate, objItem.getRate());
        cvDbValues.put(KEY_SupplyType, objItem.getSupplyType());
        cvDbValues.put("ImageUri", objItem.getImageUri());
        cvDbValues.put(KEY_UOM, objItem.getMOU());
        cvDbValues.put(KEY_Count,1);
        cvDbValues.put(KEY_AverageRate,objItem.getAverageRate());
        return dbFNB.insert(TBL_ITEM_Inward, null, cvDbValues);
    }
    public long addItem_InwardDatabase(ItemInward objItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SupplyType, objItem.getSupplyType());
        cvDbValues.put(KEY_TaxationType, objItem.getTaxationType());
        cvDbValues.put(KEY_ItemName, objItem.getStrItemname());
        cvDbValues.put(KEY_HSNCode, objItem.getHSNCode());
        cvDbValues.put(KEY_ItemBarcode, objItem.getStrItemBarcode());
        cvDbValues.put(KEY_ImageUri, objItem.getStrImageUri());
        cvDbValues.put(KEY_Quantity, objItem.getfQuantity());
        cvDbValues.put(KEY_AverageRate, objItem.getfAveragerate());
        cvDbValues.put(KEY_UOM, objItem.getUOM());
        cvDbValues.put(KEY_CGSTRate, objItem.getCGSTRate());
        cvDbValues.put(KEY_SGSTRate, objItem.getSGSTRate());
        cvDbValues.put(KEY_IGSTRate, objItem.getIGSTRate());
        cvDbValues.put(KEY_cessRate, objItem.getCessRate());
        cvDbValues.put(KEY_Count,1);
        return dbFNB.insert(TBL_ITEM_Inward, null, cvDbValues);
    }
    public int updateItem_InwardDatabase(ItemInward objItem) {

        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SupplyType, objItem.getSupplyType());
        cvDbValues.put(KEY_TaxationType, objItem.getTaxationType());
        cvDbValues.put(KEY_ItemName, objItem.getStrItemname());
        cvDbValues.put(KEY_HSNCode, objItem.getHSNCode());
        cvDbValues.put(KEY_ItemBarcode, objItem.getStrItemBarcode());
        cvDbValues.put(KEY_ImageUri, objItem.getStrImageUri());
        cvDbValues.put(KEY_Quantity, objItem.getfQuantity());
        cvDbValues.put(KEY_AverageRate, objItem.getfAveragerate());
        cvDbValues.put(KEY_UOM, objItem.getUOM());
        cvDbValues.put(KEY_CGSTRate, objItem.getCGSTRate());
        cvDbValues.put(KEY_SGSTRate, objItem.getSGSTRate());
        cvDbValues.put(KEY_IGSTRate, objItem.getIGSTRate());
        cvDbValues.put(KEY_cessRate, objItem.getCessRate());
        cvDbValues.put(KEY_Count,1);

        return dbFNB.update(TBL_ITEM_Inward, cvDbValues, "MenuCode=" + objItem.getiMenuCode(), null);
    }



    public int updateItem_inward(int MenuCode, int taxationtype, String g_s, int suppliercode, String suppliername,
                                 String hsnCode, String ItemName, float IGSTRate, float igstamount, float CGSTRate, float cgstamount,
                                 float SGSTRate, float sgstamount, String ImageUri, int AdditionalTaxId, float quantity, String MOU_str,
                                 int CategCode, float frate, int DeptCode, int DiscId, int DiscountEnable, String ItemBarCode, int KitchenCode,
                                 int PriceChange, int salestaxid, float fSalesTax, float fServiceTax, int TaxType) {

        cvDbValues = new ContentValues();

        //cvDbValues.put("MenuCode", MenuCode);
        cvDbValues.put("ItemName", ItemName);
        cvDbValues.put("ItemBarcode", ItemBarCode);
        cvDbValues.put("DeptCode", DeptCode);
        cvDbValues.put("CategCode", CategCode);
        cvDbValues.put("KitchenCode", KitchenCode);
        cvDbValues.put("AdditionalTaxId", AdditionalTaxId);
        cvDbValues.put("DiscId", DiscId);
        cvDbValues.put("Quantity", quantity);
        cvDbValues.put("PriceChange", PriceChange);
        cvDbValues.put("DiscountEnable", DiscountEnable);
        cvDbValues.put("ImageUri", ImageUri);
        cvDbValues.put("TaxType", TaxType);

        cvDbValues.put(KEY_Rate, frate);
        cvDbValues.put(KEY_HSNCode, hsnCode);
        cvDbValues.put(KEY_IGSTRate, IGSTRate);
        cvDbValues.put(KEY_CGSTRate, CGSTRate);
        cvDbValues.put(KEY_SGSTRate, SGSTRate);
        cvDbValues.put(KEY_SupplyType, g_s);
        cvDbValues.put(KEY_UOM, MOU_str);
        cvDbValues.put(KEY_SalesTaxPercent, fSalesTax);
        cvDbValues.put(KEY_SerTaxPercent, fServiceTax);
        cvDbValues.put(KEY_TaxationType, taxationtype);

        cvDbValues.put(KEY_SupplierCode, suppliercode);
        cvDbValues.put(KEY_SUPPLIERNAME, suppliername);
        cvDbValues.put(KEY_IGSTAmount, igstamount);
        cvDbValues.put(KEY_CGSTAmount, cgstamount);
        cvDbValues.put(KEY_SGSTAmount, sgstamount);
        cvDbValues.put(KEY_SalesTaxId, salestaxid);


        return dbFNB.update(TBL_ITEM_Outward, cvDbValues, KEY_MenuCode + "=" + MenuCode, null);
    }


    // richa 2712_inward
    public Cursor getAllSupplierName_nonGST() {

        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_Supplier+" ORDER BY SupplierName ASC"/* + " WHERE " + KEY_SupplierType + " LIKE 'UnRegistered'"*/;
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);// selectQuery,selectedArgument
        return cursor;
    }

    public  long deleteSupplier(int supplierCode)
    {
        return dbFNB.delete(TBL_Supplier, KEY_SupplierCode + "=" + supplierCode, null);
    }
    public Cursor getAllInwardItemNames() {

        Cursor cursor = null;
        String selectQuery = "SELECT DISTINCT " + KEY_ItemName + " FROM " + TBL_ITEM_Inward;
        cursor = dbFNB.rawQuery(selectQuery, null);// selectQuery,selectedArgument
        return cursor;
    }
    public Cursor getAllInwardItems() {

        Cursor cursor = null;
        String selectQuery = "SELECT * FROM " + TBL_ITEM_Inward;
        cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

/*
    clear the Inward Item Data base
     */

    public int clearInwardItemdatabase() {
        return dbFNB.delete(TBL_ITEM_Inward, null, null);
    }

       /*
    clear the Inward Item Data base  based on current date
     */

    public int clearInwardStock(String currentdate) {
        int del = 0;
        del = dbFNB.delete(TBL_StockInward, KEY_BusinessDate + " LIKE '" + currentdate + "' ", null);
        return del;
    }

/*
    clear the supplier linked Data base
     */

    public int clearSupplierLinkage() {
        return dbFNB.delete(TBL_SupplierItemLinkage, null, null);
    }
    /*
    public ArrayList<String> getAllSupplierName_nonGST() {
        ArrayList<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_Supplier+" WHERE "+KEY_SupplierType+" LIKE 'UnRegistered'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);// selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(KEY_SUPPLIERNAME)));// adding
            } while (cursor.moveToNext());
        }
        // returning lables
        return list;
    }
    */


    public List<String> getitemlist_inward_nonGST(String suppliername, int suppliercode) {
        List<String> list = new ArrayList<String>();
        String selectQuery = "Select DISTINCT " + KEY_ItemName + "  FROM " + TBL_ITEM_Inward + " WHERE " + KEY_SUPPLIERNAME +
                " LIKE '" + suppliername + "' AND " + KEY_SupplierCode + " LIKE '" + suppliercode + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        while (cursor != null && cursor.moveToNext()) {
            String item = cursor.getString(cursor.getColumnIndex(KEY_ItemName));
            list.add(item);
        }
        return list;
    }

    /*public List<String> getitemlist_inward_nonGST_Goods(String suppliername, int suppliercode) {
        List<String> list = new ArrayList<String>();
        String selectQuery = "Select DISTINCT " + KEY_ItemName + "  FROM " + TBL_ITEM_Inward + " WHERE " + KEY_SUPPLIERNAME +
                " LIKE '" + suppliername + "' AND " + KEY_SupplierCode + " LIKE '" + suppliercode + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        list.add("Not in list");
        //list.add("Add new item");
        while (cursor != null && cursor.moveToNext()) {
            String item = cursor.getString(cursor.getColumnIndex(KEY_ItemName));
            list.add(item);
        }
        return list;
    }*/

    public Cursor getItemsForSubmittedIngredients(String status) {
        Cursor cursor = null;
        String selectQuery = " Select DISTINCT " + KEY_ItemName + " from " + TBL_INGREDIENTS +
                " WHERE " + KEY_Status + " LIKE '" + status + "'";
        cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

    public int DeleteIngredients(int MenuCode, String ingredientname) {
        return dbFNB.delete(TBL_INGREDIENTS, KEY_MenuCode + "=" + MenuCode + " AND " +
                KEY_IngredientName + " LIKE '" + ingredientname + "'", null);
    }

    public int DeleteSubmittedItem(String itemname) {
        return dbFNB.delete(TBL_INGREDIENTS, KEY_ItemName + " LIKE '" + itemname + "'", null);
    }

    public Cursor getIngredientsForMenuCode(int menucode) {
        Cursor cursor = null;
        String selectQuery = "Select *  FROM " + TBL_INGREDIENTS + " WHERE " + KEY_MenuCode +
                " = " + menucode;
        cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }
    public Cursor getIngredientsForMenuCode(int menucode, String status) {
        Cursor cursor = null;
        String selectQuery = "Select *  FROM " + TBL_INGREDIENTS + " WHERE " + KEY_MenuCode +
                " = " + menucode +" AND "+KEY_Status+" LIKE '"+status+"'";
        cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getIngredientsForItemName(String itemname) {
        Cursor cursor = null;
        String selectQuery = "Select *  FROM " + TBL_INGREDIENTS + " WHERE " + KEY_ItemName +
                " LIKE '" + itemname + "'";
        cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

    public int DeleteSavedIngredients(int menucode, String status) {
        //String WhereClause = KEY_MenuCode + " = " + menucode + " AND " + KEY_Status + " LIKE '" + status + "'";
        String WhereClause = KEY_MenuCode + " = " + menucode ;
        return dbFNB.delete(TBL_INGREDIENTS, WhereClause, null);
    }

    public long saveIngredient(ItemIngredients ingredient) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_MenuCode, ingredient.getMenucode());
        cvDbValues.put(KEY_ItemName, ingredient.getItemname());
        cvDbValues.put(KEY_Quantity, ingredient.getItemquantity());
        cvDbValues.put(KEY_IngredientCode, ingredient.getIngredientcode());
        cvDbValues.put(KEY_IngredientName, ingredient.getIngredientname());
        cvDbValues.put(KEY_IngredientQuantity, ingredient.getIngredientquantity());
        cvDbValues.put(KEY_UOM, ingredient.getUom());
        cvDbValues.put(KEY_IngredientUOM, ingredient.getIngredientUOM());
        cvDbValues.put(KEY_Status, ingredient.getStatus());
        return dbFNB.insert(TBL_INGREDIENTS, null, cvDbValues);
    }


    public Cursor getItem_GoodsInward(String itemname) {
        Cursor cursor = null;
        String selectQuery = " Select * from " + TBL_GOODSINWARD + " WHERE " + KEY_ItemName + " LIKE '" + itemname + "'";
        cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getAllItem_GoodsInward() {
        Cursor cursor = null;
        String selectQuery = " Select * from " + TBL_GOODSINWARD;
        cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

    public long addIngredient(String itemname_str, double quantity_f, String uom_str, double rate, int supplierCount) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ItemName, itemname_str);
        cvDbValues.put(KEY_Quantity, quantity_f);
        cvDbValues.put(KEY_UOM, uom_str);
        cvDbValues.put(KEY_Value, rate);
        cvDbValues.put(KEY_SupplierCount, supplierCount);
        return dbFNB.insert(TBL_GOODSINWARD, null, cvDbValues);
    }

    public long updateIngredient(String itemname_str, double quantity_f, double rate, int SupplierCount) {
        cvDbValues = new ContentValues();
        cvDbValues.put(KEY_ItemName, itemname_str);
        cvDbValues.put(KEY_Quantity, quantity_f);
        cvDbValues.put(KEY_Value, rate);
        cvDbValues.put(KEY_SupplierCount, SupplierCount);

        String whereClause = KEY_ItemName + " LIKE '" + itemname_str + "'";
        return dbFNB.update(TBL_GOODSINWARD, cvDbValues, whereClause, null);
    }

    public int deleteItemInGoodsInward(String  itemname) {

        return dbFNB.delete(TBL_GOODSINWARD, KEY_ItemName + " LIKE '"+itemname+"'", null);
    }


    public int deletePurchaseOrder(int suppliercode, int purchaseorder) {

        return dbFNB.delete(TBL_PURCHASEORDER, KEY_SupplierCode + "=" + suppliercode + " AND " + KEY_PurchaseOrderNo + " = " + purchaseorder, null);
    }
    public long deletePurchaseOrderEntry(String puchaseOrderNo, String supplierCode,String itemname,
                                        double rate, double quantity) {

        String deleteClause = KEY_SupplierCode + " Like '" + supplierCode + "' AND " +KEY_PurchaseOrderNo +
                " LIKE '" + puchaseOrderNo+"' AND "+KEY_ItemName+" LIKE '"+itemname+"' AND "+KEY_Rate+"="+rate+
                " AND "+KEY_Quantity+"="+quantity;
        return dbFNB.delete(TBL_PURCHASEORDER, deleteClause, null);
    }

    public List<String> getPurchaseOrderlist_inward_nonGST(int suppliercode) {
        List<String> list = new ArrayList<String>();
        String selectQuery = "Select DISTINCT  " + KEY_PurchaseOrderNo + "  FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " LIKE '" + suppliercode + "' AND "+KEY_isGoodinward+" = 0";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        list.add("NA");
        //list.add("Make New Purchase Order");
        while (cursor != null && cursor.moveToNext()) {
            String item = cursor.getString(cursor.getColumnIndex(KEY_PurchaseOrderNo));
            list.add(item);
        }
        return list;
    }

    public List<String> getPurchaseOrderlist(int suppliercode) {
        List<String> list = new ArrayList<String>();
        String selectQuery = "Select DISTINCT  " + KEY_PurchaseOrderNo + "  FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " LIKE '" + suppliercode + "' AND "+KEY_isGoodinward+" = 0";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        while (cursor != null && cursor.moveToNext()) {
            String item = cursor.getString(cursor.getColumnIndex(KEY_PurchaseOrderNo));
            list.add(item);
        }
        return list;
    }

    public Cursor getMaxPurchaseOrderNo() {
        Cursor cursor = dbFNB.rawQuery("SELECT MAX(PurchaseOrderNo) FROM PurchaseOrder", null);
        return cursor;
    }


    // richa -> same as function getbill_inward
    public Cursor checkDuplicatePurchaseOrder(int suppliercode, int Menucode, int purchaseorder) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " = " + suppliercode + " AND " + KEY_MenuCode
                + " = " + Menucode + " AND " + KEY_PurchaseOrderNo + " = " + purchaseorder;
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    public Cursor checkduplicatePO(int suppliercode, int purchaseorder) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " = " + suppliercode + " AND " +
                KEY_PurchaseOrderNo + " = " + purchaseorder;
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    public Cursor getPurchaseOrderDetails(int suppliercode, int purchaseOrder) {
        Cursor result = null;
        // richa to do ->date
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " = " + suppliercode + " AND " +
                KEY_PurchaseOrderNo + " = " + purchaseOrder;
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }
    public Cursor getPurchaseOrder_for_gstin(String startDate, String endDate,String gstin ) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_GSTIN + " Like '" + gstin + "' AND " +
                KEY_InvoiceDate+" BETWEEN '"+startDate+"' AND '"+endDate+"' AND "+KEY_isGoodinward+" LIKE '1'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }
    public Cursor getPurchaseOrder_for_gstin(String invoiceNo,String invoiceDate,String gstin,String purchaseorder ) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_GSTIN + " Like '" + gstin + "' AND " +
                /*KEY_PurchaseOrderNo+" LIKE '"+purchaseorder+"' AND "+*/
                KEY_InvoiceDate+" LIKE '"+invoiceDate+"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+KEY_isGoodinward+" LIKE '1'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }
    public Cursor getPurchaseOrder_for_unregisteredSupplier(String invoiceNo,String invoiceDate,String purchaseorder, String supplierCode ) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " + KEY_SupplierCode + " Like '" + supplierCode + "' AND " +
                KEY_SupplierType+" LIKE 'UnRegistered' AND "+
                /*KEY_PurchaseOrderNo+" LIKE '"+purchaseorder+"' AND "+*/
                KEY_InvoiceDate+" LIKE '"+invoiceDate+"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+KEY_isGoodinward+" LIKE '1'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }
    public Cursor getPurchaseOrder_for_unregistered(String startDate,String endDate ) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_PURCHASEORDER + " WHERE " +
                /*KEY_PurchaseOrderNo+" LIKE '"+purchaseorder+"' AND "+*/
                KEY_SupplierType+" LIKE 'UnRegistered' AND "+
                KEY_InvoiceDate+" BETWEEN '"+startDate+"' AND '"+endDate+"' AND "+KEY_isGoodinward+" LIKE '1'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    // -----Insert Purchase Order-----
    public long InsertPurchaseOrder(PurchaseOrder po) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SUPPLIERNAME, po.getSupplierName());
        cvDbValues.put(KEY_SupplierCode, po.getSupplierCode());
        cvDbValues.put(KEY_SupplierPhone, po.getSupplierPhone());
        cvDbValues.put(KEY_SupplierAddress, po.getSupplierAddress());
        cvDbValues.put(KEY_SupplierType, po.getSupplierType());
        cvDbValues.put(KEY_SupplierPOS, po.getSupplierPOS());
        cvDbValues.put(KEY_GSTIN, po.getSupplierGSTIN());
        cvDbValues.put(KEY_PurchaseOrderNo, po.getPurchaseOrderNo());
        cvDbValues.put(KEY_InvoiceNo, po.getInvoiceNo());
        cvDbValues.put(KEY_InvoiceDate, po.getInvoiceDate());

        cvDbValues.put(KEY_MenuCode, po.getMenuCode());
        cvDbValues.put(KEY_SupplyType, po.getSupplyType());
        cvDbValues.put(KEY_HSNCode, po.getHSNCode());
        cvDbValues.put(KEY_ItemName, po.getItemName());
        cvDbValues.put(KEY_Quantity, po.getQuantity());
        cvDbValues.put(KEY_UOM, po.getUOM());
        cvDbValues.put(KEY_Value, po.getValue());
        cvDbValues.put(KEY_TaxableValue, po.getTaxableValue());
        cvDbValues.put(KEY_IGSTRate,po.getIgstRate());
        cvDbValues.put(KEY_IGSTAmount,po.getIgstAmount());
        cvDbValues.put(KEY_CGSTRate,po.getCgstRate());
        cvDbValues.put(KEY_CGSTAmount,po.getCgstAmount());
        cvDbValues.put(KEY_SGSTRate,po.getSgstRate());
        cvDbValues.put(KEY_SGSTAmount,po.getSgstAmount());
        cvDbValues.put(KEY_cessRate,po.getCsRate());
        cvDbValues.put(KEY_cessAmount,po.getCsAmount());

        cvDbValues.put(KEY_Amount, po.getAmount());
        cvDbValues.put(KEY_AdditionalChargeName, po.getAdditionalCharge());
        cvDbValues.put(KEY_AdditionalChargeAmount, po.getAdditionalChargeAmount());
        cvDbValues.put(KEY_isGoodinward, po.getIsgoodInward());


        return dbFNB.insert(TBL_PURCHASEORDER, null, cvDbValues);
    }
    public long UpdatePurchaseOrder(PurchaseOrder po) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SUPPLIERNAME, po.getSupplierName());
        cvDbValues.put(KEY_SupplierCode, po.getSupplierCode());
        cvDbValues.put(KEY_SupplierPhone, po.getSupplierPhone());
        cvDbValues.put(KEY_SupplierAddress, po.getSupplierAddress());
        cvDbValues.put(KEY_SupplierType, po.getSupplierType());
        cvDbValues.put(KEY_GSTIN, po.getSupplierGSTIN());

        cvDbValues.put(KEY_PurchaseOrderNo, po.getPurchaseOrderNo());
        cvDbValues.put(KEY_MenuCode, po.getMenuCode());
        cvDbValues.put(KEY_SupplyType, po.getSupplyType());
        cvDbValues.put(KEY_ItemName, po.getItemName());
        cvDbValues.put(KEY_Quantity, po.getQuantity());
        cvDbValues.put(KEY_UOM, po.getUOM());
        cvDbValues.put(KEY_Value, po.getValue());
        cvDbValues.put(KEY_TaxableValue, po.getTaxableValue());
        cvDbValues.put(KEY_IGSTRate,po.getIgstRate());
        cvDbValues.put(KEY_IGSTAmount,po.getIgstAmount());
        cvDbValues.put(KEY_CGSTRate,po.getCgstRate());
        cvDbValues.put(KEY_CGSTAmount,po.getCgstAmount());
        cvDbValues.put(KEY_SGSTRate,po.getSgstRate());
        cvDbValues.put(KEY_SGSTAmount,po.getSgstAmount());
        cvDbValues.put(KEY_Amount, po.getAmount());
        cvDbValues.put(KEY_AdditionalChargeName, po.getAdditionalCharge());
        cvDbValues.put(KEY_AdditionalChargeAmount, po.getAdditionalChargeAmount());
        cvDbValues.put(KEY_isGoodinward, po.getIsgoodInward());

        String whereClause = KEY_PurchaseOrderNo + " = " + po.getPurchaseOrderNo() + " AND " +
                KEY_SupplierCode + " = " + po.getSupplierCode() + " AND " + KEY_MenuCode + " = " + po.getMenuCode();

        return dbFNB.update(TBL_PURCHASEORDER, cvDbValues, whereClause, null);
    }

    public long InsertPurchaseOrder(BillItem objBillItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SUPPLIERNAME, objBillItem.getSupplierName());
        cvDbValues.put(KEY_SupplierCode, objBillItem.getSuppliercode());
        cvDbValues.put(KEY_SupplierPhone, objBillItem.getSupplierPhone());
        cvDbValues.put(KEY_SupplierType, objBillItem.getSupplierType());
        cvDbValues.put(KEY_GSTIN, objBillItem.getSupplierGSTIN());
        cvDbValues.put(KEY_InvoiceNo, objBillItem.getBillNumber());
        cvDbValues.put(KEY_InvoiceDate, objBillItem.getInvoiceDate());
        cvDbValues.put(KEY_PurchaseOrderNo, objBillItem.getPurchaseOrderNo());

        cvDbValues.put(KEY_MenuCode, objBillItem.getItemNumber());
        cvDbValues.put(KEY_SupplyType, objBillItem.getSupplyType());
        cvDbValues.put(KEY_ItemName, objBillItem.getItemName());
        cvDbValues.put(KEY_Quantity, objBillItem.getQuantity());
        cvDbValues.put(KEY_UOM, objBillItem.getUom());
        cvDbValues.put(KEY_Value, objBillItem.getValue());
        cvDbValues.put(KEY_TaxableValue, objBillItem.getTaxableValue());
        cvDbValues.put(KEY_SalesTax, objBillItem.getTaxAmount());
        cvDbValues.put(KEY_ServiceTaxAmount, objBillItem.getServiceTaxAmount());
        cvDbValues.put(KEY_Amount, objBillItem.getAmount());
        cvDbValues.put(KEY_AdditionalChargeName, objBillItem.getAdditionalChargeName());
        cvDbValues.put(KEY_AdditionalChargeAmount, objBillItem.getAdditionalChargeAmount());
        cvDbValues.put(KEY_isGoodinward, objBillItem.getIsGoodInwarded());


        return dbFNB.insert(TBL_PURCHASEORDER, null, cvDbValues);
    }

    // -----Update Purchase Order-----
    public long UpdatePurchaseOrder(BillItem objBillItem) {
        cvDbValues = new ContentValues();

        cvDbValues.put(KEY_SUPPLIERNAME, objBillItem.getSupplierName());
        cvDbValues.put(KEY_SupplierCode, objBillItem.getSuppliercode());
        cvDbValues.put(KEY_SupplierPhone, objBillItem.getSupplierPhone());

        cvDbValues.put(KEY_PurchaseOrderNo, objBillItem.getPurchaseOrderNo());
       /* cvDbValues.put(KEY_InvoiceNo, objBillItem.getBillNumber());
        cvDbValues.put(KEY_InvoiceDate, objBillItem.getInvoiceDate());*/

        cvDbValues.put(KEY_MenuCode, objBillItem.getItemNumber());
        cvDbValues.put(KEY_SupplyType, objBillItem.getSupplyType());
        cvDbValues.put(KEY_ItemName, objBillItem.getItemName());
        cvDbValues.put(KEY_Quantity, objBillItem.getQuantity());
        cvDbValues.put(KEY_UOM, objBillItem.getUom());
        cvDbValues.put(KEY_Value, objBillItem.getValue());
        cvDbValues.put(KEY_TaxableValue, objBillItem.getTaxableValue());
        cvDbValues.put(KEY_SalesTax, objBillItem.getTaxAmount());
        cvDbValues.put(KEY_ServiceTaxAmount, objBillItem.getServiceTaxAmount());
        cvDbValues.put(KEY_Amount, objBillItem.getAmount());
        cvDbValues.put(KEY_AdditionalChargeName, objBillItem.getAdditionalChargeName());
        cvDbValues.put(KEY_AdditionalChargeAmount, objBillItem.getAdditionalChargeAmount());
        cvDbValues.put(KEY_isGoodinward, objBillItem.getIsGoodInwarded());

        String whereClause = KEY_PurchaseOrderNo + " = " + objBillItem.getPurchaseOrderNo() + " AND " +
                KEY_SupplierCode + " = " + objBillItem.getSuppliercode() + " AND " + KEY_MenuCode + " = " + objBillItem.getItemNumber();

        return dbFNB.update(TBL_PURCHASEORDER, cvDbValues, whereClause, null);
    }

    public Cursor getGSTR2_b2bA_invoices_for_gstin_registered(String startDate, String endDate,String gstin ) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_GSTR2_AMEND + " WHERE " + KEY_GSTIN + " Like '" + gstin + "' AND " +
                KEY_InvoiceDate+" BETWEEN '"+startDate+"' AND '"+endDate+"' AND  "+KEY_SupplierType+" LIKE 'Registered'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }
    public Cursor getGSTR2_b2bA_ammends_for_gstin_registered(String invoiceNo,String invoiceDate,String gstin,String invoiceNo_ori,
                                                  String invoicedate_ori)
    {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_GSTR2_AMEND + " WHERE " + KEY_GSTIN + " Like '" + gstin + "' AND " +
                KEY_InvoiceDate+" LIKE '"+invoiceDate+"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+KEY_SupplierType+
                " LIKE 'Registered' AND "+KEY_OriginalInvoiceNo+" LIKE '"+invoiceNo_ori+"' AND  "+KEY_OriginalInvoiceDate+" LIKE '"
                +invoicedate_ori+"'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }
    // richa 2712_inward
    public ArrayList<String> getAllSupplierName() {
        ArrayList<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TBL_Supplier;
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);// selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex(KEY_SUPPLIERNAME)));// adding
            } while (cursor.moveToNext());
        }
        // returning lables
        return list;
    }

    public List<String> getitemlist_inward(String suppliername) {
        List<String> list = new ArrayList<String>();
        String selectQuery = "Select " + KEY_ItemName + "  FROM " + TBL_ITEM_Inward + " WHERE " + KEY_SUPPLIERNAME + " LIKE '" + suppliername + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        while (cursor != null && cursor.moveToNext()) {
            String item = cursor.getString(cursor.getColumnIndex(KEY_ItemName));
            list.add(item);
        }
        return list;
    }


    public Cursor getLinkedMenuCodeForSupplier(int Suppliercode) {
        String selectQuery = "Select *  FROM " + TBL_SupplierItemLinkage + " WHERE " + KEY_SupplierCode + " LIKE '" + Suppliercode + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);

        return cursor;
    }
    public Cursor getLinkedSupplierCodeForItem(int menucode) {
        String selectQuery = "Select *  FROM " + TBL_SupplierItemLinkage + " WHERE " + KEY_MenuCode + " LIKE '" + menucode + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);

        return cursor;
    }

    public Cursor getallsupplier() {
        String selectQuery = "Select *  FROM " + TBL_Supplier;
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);

        return cursor;
    }

    public List<String> getAllMenuCode() {
        List<String> list = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT  MenuCode FROM " + TBL_ITEM_Outward;
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);// selectQuery,selectedArguments
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(cursor.getColumnIndex("MenuCode")));// adding
                // 2nd
                // column
                // data
            } while (cursor.moveToNext());
        }
        // returning lables
        return list;
    }


    // Table Booking
    public long addTableBooking(TableBooking objTBooking) {
        cvDbValues = new ContentValues();

        cvDbValues.put("TBookId", objTBooking.getTBookId());
        cvDbValues.put("CustName", objTBooking.getCustomerName());
        cvDbValues.put("TimeForBooking", objTBooking.getTimeBooking());
        cvDbValues.put("TableNo", objTBooking.getTableNo());
        cvDbValues.put("MobileNo", objTBooking.getMobileNo());

        return dbFNB.insert(TBL_TABLEBOOKING, null, cvDbValues);
    }

    // -----Retrieve all Table Booking-----
    public Cursor getAllTableBooking() {
        return dbFNB.query(TBL_TABLEBOOKING, new String[]{"*"}, null, null, null, null, null);
    }

    // -----Retrieve single Table Booking-----
    public Cursor getTableBooking(int TBookId) {
        return dbFNB.query(TBL_TABLEBOOKING, new String[]{"TBookId", "CustomerName"}, "TBookId=" + TBookId, null, null,
                null, null);
    }

    // -----Retrieve single Table Booking-----
    public Cursor getTableBookingByTableNo(String TableNo) {
        return dbFNB.query(TBL_TABLEBOOKING, new String[]{"*"}, "TableNo=" + TableNo, null, null,
                null, null);
    }

    // -----Retrieve single Table Booking-----
    public Cursor getTableBookingByMobile(String MobileNo) {
        return dbFNB.query(TBL_TABLEBOOKING, new String[]{"*"}, "MobileNo=" + MobileNo, null, null,
                null, null);
    }
    // -----Retrieve single Table Booking-----
    public Cursor checkBookingStatus(int iTableNo,String bookingtimeStart, String bookingTimeEnd) {
        return dbFNB.query(TBL_TABLEBOOKING, new String[]{"*"}, KEY_TableNo+" = " +iTableNo+" AND "+
                        KEY_TimeForBooking+" Between  '"+bookingtimeStart+"' AND '"+bookingTimeEnd+"'", null, null,
                null, null);
    }
    // -----Retrieve single Table Booking-----
    public Cursor getBookedTableBetweenTime(String bookingtimeStart, String bookingTimeEnd) {
        return dbFNB.query(TBL_TABLEBOOKING, new String[]{"*"},
                KEY_TimeForBooking+" Between  '"+bookingtimeStart+"' AND '"+bookingTimeEnd+"'", null, null,
                null, null);
    }
    public Cursor getCurrentlyUsedTableBetweenTime(String bookingtimeStart, String bookingTimeEnd) {
        return dbFNB.query(TBL_PENDINGKOT, new String[]{KEY_TableNumber},
                KEY_Time+" Between  '"+bookingtimeStart+"' AND '"+bookingTimeEnd+"'", null, null,
                null, null);
    }

    // -----Delete all KOT items from Pending KOT table-----
    public int deleteAllReservedTables() {

        return dbFNB.delete(TBL_TABLEBOOKING, null, null);
    }

    // -----Retrieve highest TBookId from table-----
    public int getTableBookingId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(TBookId) FROM " + TBL_TABLEBOOKING, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }


    // -----Update Table Booking table----
    public int updateTableBooking(int iTBookId, String strCustomerName, String strTimeBooking, int iTableNo, String strMobileNo) {
        cvDbValues = new ContentValues();

        cvDbValues.put("CustName", strCustomerName);
        cvDbValues.put("TimeForBooking", strTimeBooking);
        cvDbValues.put("TableNo", iTableNo);
        cvDbValues.put("MobileNo", strMobileNo);

        return dbFNB.update(TBL_TABLEBOOKING, cvDbValues, "TBookId=" + iTBookId, null);
    }

    // Delete Table Booking by Table No
    public int DeleteTableBooking(String TableNo) {

        return dbFNB.delete(TBL_TABLEBOOKING, "TableNo=" + TableNo, null);
    }
    public int DeleteTableBooking_WithDetails(String CustName,String Time,String TableNo, String CustPhone) {

        String whereString = KEY_CustName+" LIKE '"+CustName+"' AND "+KEY_TimeForBooking+" LIKE '"+Time+"' AND "+KEY_TableNo+" = "+
                Integer.parseInt(TableNo)+" AND "+KEY_MobileNo+" = "+Integer.parseInt(CustPhone);
        return dbFNB.delete(TBL_TABLEBOOKING, whereString, null);
    }


    // Mail Configuration Settings
    public long addMailSettings(MailSettings objMailSettings) {
        cvDbValues = new ContentValues();

        cvDbValues.put("FromMailId", objMailSettings.getFromMailId());
        cvDbValues.put("FromMailPassword", objMailSettings.getFromMailPassword());
        cvDbValues.put("SmtpServer", objMailSettings.getSmtpServer());
        cvDbValues.put("{PortNo", objMailSettings.getportNo());
        cvDbValues.put("{FromDate", objMailSettings.getFromDate());
        cvDbValues.put("{ToDate", objMailSettings.getToDate());
        cvDbValues.put("{SendMail", objMailSettings.getSendMail());
        cvDbValues.put("{AutoMail", objMailSettings.getAutoMail());

        return dbFNB.insert(TBL_MAILSETTING, null, cvDbValues);
    }

    // -----Retrieve Mail Settings-----
    public Cursor getMailSettings() {
        return dbFNB.query(TBL_MAILSETTING, new String[]{"FromMailId", "FromMailPassword", "SmtpServer", "PortNo", "FromDate", "ToDate", "SendMail", "AutoMail"}, null, null, null, null, null);
    }

    // -----Retrieve highest MailConfigId from table-----
    public int getMailSettingsId() {
        Cursor result;
        result = dbFNB.rawQuery("SELECT MAX(MailConfigId) FROM " + TBL_MAILSETTING, null);

        if (result.moveToFirst()) {
            return result.getInt(0);
        } else {
            return 0;
        }
    }

    // -----Update Mail Configuration ----
    public int updateMailSettings(MailSettings objMailSettings) {
        cvDbValues = new ContentValues();

        cvDbValues.put("FromMailId", objMailSettings.getFromMailId());
        cvDbValues.put("FromMailPassword", objMailSettings.getFromMailPassword());
        cvDbValues.put("SmtpServer", objMailSettings.getSmtpServer());
        cvDbValues.put("PortNo", objMailSettings.getportNo());
        cvDbValues.put("FromDate", objMailSettings.getFromDate());
        cvDbValues.put("ToDate", objMailSettings.getToDate());
        cvDbValues.put("SendMail", objMailSettings.getSendMail());
        cvDbValues.put("AutoMail", objMailSettings.getAutoMail());

        return dbFNB.update(TBL_MAILSETTING, cvDbValues, null, null);
    }

    //  ------------- Update Reports for Mail Send ------------------
    public int updateReportsMaster(int ReportsId, int Status) {
        cvDbValues = new ContentValues();

        cvDbValues.put("Status", Status);
        return dbFNB.update(TBL_REPORTSMASTER, cvDbValues, "ReportsId=" + ReportsId, null);
    }

    // New User, Roles, Role Access modules -------------------------------------

    public long addRole(String roleName) {
        long status = 0;

        ContentValues contentValues = new ContentValues();
        contentValues.put("RoleName", roleName);
        try {
            status = dbFNB.insert(TBL_USERSROLE, null, contentValues);
            //Log.d(TAG,"code "+status);
            if (status > 0) {

            }
            //Log.d(TAG,"Inserted Successfully with code "+status);
        } catch (Exception e) {
            status = 0;
            Log.d(TAG,e.toString());
        }
        return status;
    }

    public String getRole(String roleName) {
        String str = "";

        try {
            Cursor cursor = dbFNB.query(TBL_USERSROLE, null, "RoleId=?",
                    new String[]{String.valueOf(roleName)}, null, null, null, null);
            if (cursor != null)
                while (cursor.moveToNext()) {
                    str = cursor.getString(cursor.getColumnIndex("RoleId"));
                    break;
                }
        } catch (Exception e) {
            str = "";
            Log.d(TAG,e.toString());
        }
        return str;
    }

    public String getRoleName(String roleName) {
        String str = "";
        try {
            Cursor cursor = dbFNB.query(TBL_USERSROLE, null, "RoleId=?",
                    new String[]{String.valueOf(roleName)}, null, null, null, null);
            if (cursor != null)
                while (cursor.moveToNext()) {
                    str = cursor.getString(cursor.getColumnIndex("RoleName"));
                    break;
                }
        } catch (Exception e) {
            str = "";
            Log.d(TAG,e.toString());
        }
        return str;
    }

    public ArrayList<String> getAllRoles() {
        ArrayList<String> list = new ArrayList<String>();
        list = new ArrayList<String>();
//        list.add(0, "Manager");
//        list.add(1, "Head Cook");
//        list.add(2, "Waiter");

        String SELECT_QUERY = "SELECT * FROM " + TBL_USERSROLE;
        Cursor cursor = dbFNB.rawQuery(SELECT_QUERY, null);
        if (cursor != null) {
            //Log.d(TAG,"fetched "+cursor.getCount()+" Items");
            while (cursor.moveToNext()) {
                String role = cursor.getString(cursor.getColumnIndex("RoleName"));
                list.add(role);
            }
        }
        return list;
    }

    public void addAccessesForRole(String roleName, ArrayList<String> listsAccess, SparseBooleanArray checkedItems) {

        for (int i = 0; i < checkedItems.size(); i++) {
            long status = 0;
            if(checkedItems.get(checkedItems.keyAt(i)) == false)
            {
                continue;
            }
            ContentValues contentValues = new ContentValues();
            contentValues.put("RoleId", roleName);
            contentValues.put("RoleAccessId", checkedItems.keyAt(i));
            contentValues.put("RoleAccessName", listsAccess.get(checkedItems.keyAt(i)));
            try {
                status = dbFNB.insert(TBL_USERROLEACCESS, null, contentValues);
                //Log.d(TAG,"code "+status);
                if (status > 0) {

                }
                //Log.d(TAG,"Inserted Successfully with code "+status);
            } catch (Exception e) {
                status = 0;
                Log.d(TAG,e.toString());
            }
        }
    }

    public int  deleteAccessesForRole(String roleName) {
        int status =0;
        try {
            status = dbFNB.delete(TBL_USERROLEACCESS, "RoleId" + " = ?", new String[]{String.valueOf(roleName)});
            //status = dbFNB.insert(TBL_USERROLEACCESS, null, contentValues);
            //Log.d(TAG,"code "+status);
            if (status > 0) {

            }
            //Log.d(TAG,"Inserted Successfully with code "+status);
        } catch (Exception e) {
            status = 0;
            Log.d(TAG,e.toString());
        }
        return status;
    }
    public void deleteRole(String roleName) {
        dbFNB.delete(TBL_USERSROLE, "RoleName" + " = ?", new String[]{String.valueOf(roleName)});
    }

    public ArrayList<Integer> getPermissionsForRole(String roleName) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        Cursor cursor = dbFNB.query(TBL_USERROLEACCESS, null, "RoleId=?",
                new String[]{String.valueOf(roleName)}, null, null, null, null);
        if (cursor != null) {
            //Log.d(TAG,"fetched "+cursor.getCount()+" Items");
            while (cursor.moveToNext()) {
                int role = cursor.getInt(cursor.getColumnIndex("RoleAccessId"));
                list.add(role);
            }
        }
        return list;
    }

    public ArrayList<String> getPermissionsNamesForRole(String roleName) {
        ArrayList<String> list = new ArrayList<String>();
        Cursor cursor = dbFNB.query(TBL_USERROLEACCESS, null, KEY_ROLE_ID + "=?", new String[]{String.valueOf(roleName)}, null, null, null, null);
        if (cursor != null) {
            Log.d(TAG, "fetched " + cursor.getCount() + " Items");
            while (cursor.moveToNext()) {
                String role = cursor.getString(cursor.getColumnIndex(KEY_ACCESS_NAME));
                list.add(role);
            }
        }
        return list;
    }

    public Cursor getUser(String UserId, String Password) {
        Cursor cursor = null;
        try {
            cursor = dbFNB.query(TBL_USERS, new String[]{"*"}, "LoginId ='" + UserId + "' AND Password ='" + Password + "'",
                    null, null, null, null);

        } catch (Exception e) {
            Log.d(TAG, e.getMessage() + "");
        }
        return cursor;
    }

    public long addNewUser(com.wep.common.app.models.User user) {
        long status = 0;

        ContentValues contentValues = new ContentValues();
        //contentValues.put(KEY_USER_ID,user.getUserId());
        contentValues.put("Name", user.getUserName());
        contentValues.put("Mobile", user.getUserMobile());
        contentValues.put("Designation", user.getUserDesignation());
        contentValues.put("RoleId", user.getUserRole());
        contentValues.put("LoginId", user.getUserLogin());
        contentValues.put("Password", user.getUserPassword());
        contentValues.put("AadhaarNo", user.getUserAdhar());
        contentValues.put("Email", user.getUserEmail());
        contentValues.put("Address", user.getUserAddress());
        contentValues.put("FileLocation", user.getUserFileLoc());
        try {
            status = dbFNB.insert(TBL_USERS, null, contentValues);

        } catch (Exception e) {
            status = 0;
            Log.d(TAG,e.toString());
        }
        return status;
    }

    public long updateUser(com.wep.common.app.models.User user) {
        long status = 0;
        ContentValues contentValues = new ContentValues();
        //contentValues.put(KEY_USER_ID,user.getId());
        contentValues.put("Name", user.getUserName());
        contentValues.put("Mobile", user.getUserMobile());
        contentValues.put("Designation", user.getUserDesignation());
        contentValues.put("RoleId", user.getUserRole());
        contentValues.put("LoginId", user.getUserLogin());
        contentValues.put("Password", user.getUserPassword());
        contentValues.put("AadhaarNo", user.getUserAdhar());
        contentValues.put("Email", user.getUserEmail());
        contentValues.put("Address", user.getUserAddress());
        contentValues.put("FileLocation", user.getUserFileLoc());
        try {
            status = dbFNB.update(TBL_USERS, contentValues, KEY_USER_ID + " = ?", new String[]{String.valueOf(user.getId())});

        } catch (Exception e) {
            status = 0;
            Log.d(TAG, e.toString());
        }
        return status;
    }

    public ArrayList<com.wep.common.app.models.User> getAllUsers() {
        ArrayList<com.wep.common.app.models.User> list = new ArrayList<com.wep.common.app.models.User>();
        list = new ArrayList<com.wep.common.app.models.User>();

        String SELECT_QUERY = "SELECT * FROM " + TBL_USERS + " ORDER BY UserId ASC";
        Cursor cursor = dbFNB.rawQuery(SELECT_QUERY, null);
        if (cursor != null) {
            //Log.d(TAG,"fetched "+cursor.getCount()+" Items");
            while (cursor.moveToNext()) {
                com.wep.common.app.models.User user = new com.wep.common.app.models.User();
                user.setId(cursor.getInt(cursor.getColumnIndex("UserId")));
                user.setUserPassword(cursor.getString(cursor.getColumnIndex(KEY_USER_PASS)));
                user.setUserName(cursor.getString(cursor.getColumnIndex("Name")));
                user.setUserMobile(cursor.getString(cursor.getColumnIndex("Mobile")));
                user.setUserDesignation(cursor.getString(cursor.getColumnIndex("Designation")));
                user.setUserRole(cursor.getString(cursor.getColumnIndex("RoleId")));
                user.setUserLogin(cursor.getString(cursor.getColumnIndex("LoginId")));
                user.setUserAdhar(cursor.getString(cursor.getColumnIndex("AadhaarNo")));
                user.setUserEmail(cursor.getString(cursor.getColumnIndex("Email")));
                user.setUserAddress(cursor.getString(cursor.getColumnIndex("Address")));
                list.add(user);
            }
        }
        return list;
    }

    public void deleteUser(String login) {
        dbFNB.delete(TBL_USERS, "LoginId" + " = ?", new String[]{String.valueOf(login)});
    }

    public Cursor getUsers(String UserId) {
        return dbFNB.query(TBL_USERS, new String[]{"*"}, "UserId='" + UserId + "'", null, null, null, null);

    }
    public Cursor getUsers_counter(String UserId) {
        Cursor cursor = null;
        try
        {
            SQLiteDatabase db = getWritableDatabase();
            cursor = db.query(TBL_USERS, new String[]{"*"}, "UserId='" + UserId + "'", null, null, null, null);
        }catch(Exception e)
        {
            e.printStackTrace();
            Toast.makeText(myContext, e.getMessage(), Toast.LENGTH_SHORT).show();
            cursor = null;
        }
        finally {
            return cursor;
        }

    }

    public com.wep.common.app.models.User getUserAuth(String loginName) {
        SQLiteDatabase db = this.getReadableDatabase();
        com.wep.common.app.models.User contact = null;
        try {
            Cursor cursor = db.query(TBL_USERS, null, "LoginId=?", new String[]{String.valueOf(loginName)}, null, null, null, null);
            if (cursor != null)
                cursor.moveToFirst();
            contact = new com.wep.common.app.models.User(/*Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2)*/);
            contact.setUserLogin(cursor.getString(cursor.getColumnIndex("LoginId")));
            contact.setUserPassword(cursor.getString(cursor.getColumnIndex("Password")));
        } catch (Exception e) {
            contact = null;
            e.printStackTrace();
        }finally {
            db.close();
        }
        // return contact
        return contact;
    }

    public String getAdharNumberByUserId(String userId) {
        String adhatTxt = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String QUERY = "SELECT * FROM " + TBL_USERS + " WHERE LoginId=?";
        try {
            Cursor cursor = db.rawQuery(QUERY, new String[]{String.valueOf(userId)});
            if (cursor != null) {
                cursor.moveToFirst();
                adhatTxt = cursor.getString(cursor.getColumnIndex("AadhaarNo"));
            }
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
            adhatTxt = null;
        }finally {
            db.close();
        }
        return adhatTxt;
    }
    // -----------------------------------------------------------------

    // Getting Report Name from ReportsMaster
    public Cursor getReportsNameCursor(String ReportsType) {

        return dbFNB.rawQuery("Select ReportsId as _id, ReportsName FROM ReportsMaster where ReportsType=" +
                ReportsType + " AND ReportsName not in ('Service Tax Report','Kitchen wise Report'," +
                " 'GSTR1-1A Validation','GSTR2-2A Validation','GSTR2A','GSTR2-B2C') order by ReportsName asc", null);
    }

    // Getting Values for PAYBILL
    public Cursor getPayBillCoupon() {
        return dbFNB.rawQuery("Select CouponId, CouponDescription, CouponAmount from " + TBL_COUPON, null);
    }

    public Cursor getPayBillVoucher() {
        return dbFNB.rawQuery("Select VoucherId, VoucherDescription, VoucherPercentage from " + TBL_VOUCHERCONFIG, null);
    }

    public Cursor getPayBillDiscount() {
        return dbFNB.rawQuery("Select DiscId, DiscDescription, DiscPercentage, DiscAmount from " + TBL_DISCOUNTCONFIG, null);
    }

    public Cursor getPayBillCustomer() {
        return dbFNB.rawQuery("Select CustId, CustName, CreditAmount from " + TBL_CUSTOMER, null);
    }

    public Cursor getPayBillCustomer(String CustId) {
        return dbFNB.rawQuery("Select CustId, CustName, CreditAmount from " + TBL_CUSTOMER + " Where CustId = '" + CustId + "'", null);
    }

    public Cursor getPayBillCustomerByMobileNo(String MobileNi) {
        return dbFNB.rawQuery("Select CustId, CustName, CreditAmount from " + TBL_CUSTOMER + " Where CustContactNumber = '" + MobileNi + "'", null);
    }

    public void RestoreDefault() {
        // BillDetail
        long result = dbFNB.delete(TBL_BILLSETTING, null, null);

        ContentValues cnDbValues;
        cvDbValues = new ContentValues();
        cvDbValues.put("DineIn3Caption", "Rate 3");
        cvDbValues.put("DineIn2Caption", "Rate 2");
        cvDbValues.put("DineIn1Caption", "Rate 1");
        cvDbValues.put("WeighScale", 0);
        cvDbValues.put("ServiceTaxPercent", 5.0);
        cvDbValues.put("ServiceTaxType", 2);
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String timeStamp = dateFormat.format(date);
        cvDbValues.put("BusinessDate", timeStamp);
        cvDbValues.put("DineIn1From", 1);
        cvDbValues.put("DineIn1To", 10);
        cvDbValues.put("DineIn2From", 2);
        cvDbValues.put("DineIn2To", 20);
        cvDbValues.put("DineIn3From", 3);
        cvDbValues.put("DineIn3To", 30);
        cvDbValues.put("FooterText", " Thankyou ");
        cvDbValues.put("HeaderText", "Restaurant");
        cvDbValues.put("KOTType", 1);
        cvDbValues.put("MaximumTables", 12);
        cvDbValues.put("MaximumWaiters", 4);
        cvDbValues.put("POSNumber", 29);
        cvDbValues.put("PrintKOT", 1);
        cvDbValues.put("SubUdftext", "Seat No.");
        cvDbValues.put("TIN", "123456789");
        cvDbValues.put("ActiveForBilling", 0);
        cvDbValues.put("LoginWith", 2);
        cvDbValues.put("DateAndTime", 1);
        cvDbValues.put("PriceChange", 0);
        cvDbValues.put("BillwithStock", 0);
        cvDbValues.put("BillwithoutStock", 0);
        cvDbValues.put("Tax", 1);
        cvDbValues.put("TaxType", 1);
        cvDbValues.put(KEY_DiscountType, 0); // 1 itemwise, 0 billwise
        cvDbValues.put("KOT", 1);
        cvDbValues.put("Token", 0);
        cvDbValues.put("Kitchen", 1);
        cvDbValues.put("OtherChargesItemwise", 0);
        cvDbValues.put("OtherChargesBillwise", 0);
        cvDbValues.put("Peripherals", 1);
        cvDbValues.put("RestoreDefault", 0);
        cvDbValues.put("DineInRate", 1);
        cvDbValues.put("CounterSalesRate", 1);
        cvDbValues.put("PickUpRate", 1);
        cvDbValues.put("HomeDeliveryRate", 1);
        // richa_2012
        cvDbValues.put(KEY_HomeDineInCaption, "Dine In");
        cvDbValues.put(KEY_HomeCounterSalesCaption, "Counter Sales");
        cvDbValues.put(KEY_HomeTakeAwayCaption, "Take Away");
        cvDbValues.put(KEY_HomeHomeDeliveryCaption, "Home Delivery");
        cvDbValues.put(KEY_CummulativeHeadingEnable, 1);
        cvDbValues.put(KEY_GSTIN, 0);
        cvDbValues.put(KEY_POS, 0);
        cvDbValues.put(KEY_HSNCode, 1);
        cvDbValues.put(KEY_ReverseCharge, 0);
        cvDbValues.put(KEY_GSTIN_OUT, 0);
        cvDbValues.put(KEY_POS_OUT, 0);
        cvDbValues.put(KEY_HSNCode, 0);
        cvDbValues.put(KEY_ReverseCharge_OUT, 0);
        cvDbValues.put(KEY_GSTEnable, 0);
        cvDbValues.put(KEY_FastBillingMode, 1);
        cvDbValues.put(KEY_ItemNoReset, 0);
        cvDbValues.put(KEY_PrintPreview, 0);
        cvDbValues.put(KEY_TableSpliting, 0);
        long result1 = dbFNB.insert(TBL_BILLSETTING, null, cvDbValues);
    }

    public int getUsersIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        try{
            Cursor cursor = db.query(TBL_USERS, null, "Name=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("UserId")));
            } else {
                id = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        // return contact
        return id;
    }

    public int getCustomersIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        int id = -1;
        try{
            Cursor cursor = db.query(TBL_CUSTOMER, null, "CustName=?", new String[]{String.valueOf(name)}, null, null, null, null);
            //if (cursor != null)
            if (cursor.moveToFirst()) {
                id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("CustId")));
            } else {
                id = 0;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            db.close();
        }
        // return contact
        return id;
    }

    public List<String> getAllUsersforReport() {
        List<String> list = new ArrayList<String>();

        Cursor cursor = dbFNB.rawQuery("SELECT  UserId as _id, Name FROM Users where RoleId not in ('3','4')", null);// selectQuery,selectedArguments

        list.add("Select");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));// adding 2nd column data
            } while (cursor.moveToNext());
        }
        // closing connection

        // returning lables
        return list;
    }

    public List<String> getAllUsersforReport(int RoleId) {
        List<String> list = new ArrayList<String>();

        Cursor cursor = dbFNB.rawQuery("SELECT  UserId as _id, Name FROM Users where RoleId=" + RoleId, null);// selectQuery,selectedArguments

        list.add("Select");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));// adding 2nd column data
            } while (cursor.moveToNext());
        }
        return list;
    }

    public List<String> getAllCustomersforReport() {
        List<String> list = new ArrayList<String>();

        Cursor cursor = dbFNB.rawQuery("SELECT  CustId as _id, CustName FROM Customer", null);// selectQuery,selectedArguments

        list.add("Select");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));// adding 2nd column data
            } while (cursor.moveToNext());
        }
        return list;
    }

    public void addGSTR2B2BItems(ArrayList<GetGSTR2B2BFinal> finalsList) {

        if(finalsList == null || finalsList.size() <0)
            return ;
        Iterator<GetGSTR2B2BFinal> iterator = finalsList.iterator();
        while (iterator.hasNext()) {
            GetGSTR2B2BFinal element = iterator.next();
            String ctin = element.getCtin();
            ArrayList<GetGSTR2B2BInvoice> invoicesList = element.getInvoicesList();
            Iterator iterator2 = invoicesList.iterator();
            while (iterator2.hasNext()) {
                GetGSTR2B2BInvoice element2 = (GetGSTR2B2BInvoice) iterator2.next();
                ArrayList<GetGSTR2B2BItem> items1 = element2.getItems();
                Iterator<GetGSTR2B2BItem> it = items1.iterator();
                while (it.hasNext()) {
                    GetGSTR2B2BItem item = it.next();
                    String iNum = element2.getInum();
                    String iDate = element2.getIdt();
                    int itemLine = item.getNum();
                    if (isGSTR2B2BdataExists(ctin, iNum, iDate, itemLine)) {
                        // update
                        long status = 0;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(KEY_GSTIN, ctin);
                        contentValues.put(KEY_InvoiceNo, iNum);
                        contentValues.put(KEY_InvoiceDate, iDate);
                        contentValues.put(KEY_Value, element2.getVal());
                        contentValues.put(KEY_POS, element2.getPos());
                        contentValues.put(KEY_ReverseCharge, element2.getRchrg());
                        contentValues.put(KEY_ProvisionalAssess, element2.getPro_ass());
                        contentValues.put(KEY_LineNumber, itemLine);
                        contentValues.put(KEY_SupplyType, item.getTy());
                        contentValues.put(KEY_HSNCode, item.getHsn_sc());
                        contentValues.put(KEY_TaxableValue, item.getTxval());
                        contentValues.put(KEY_IGSTRate, item.getIrt());
                        contentValues.put(KEY_IGSTAmount, item.getIamt());
                        contentValues.put(KEY_CGSTRate, item.getCrt());
                        contentValues.put(KEY_CGSTAmount, item.getCamt());
                        contentValues.put(KEY_SGSTRate, item.getSrt());
                        contentValues.put(KEY_SGSTAmount, item.getSamt());
                        try {
                            //status = dbFNB.insert(TBL_READ_FROM_2A, null, contentValues);
                            status = dbFNB.update(TBL_READ_FROM_2A, contentValues, "GSTIN=?  and InvoiceNo=? and InvoiceDate=? and LineNumber=?", new String[]{ctin, iNum, iDate, itemLine + ""});
                            Log.d(TAG, "code " + status);
                            if (status > 0) {

                            }
                            Log.d(TAG, "Updated with code " + status);
                        } catch (Exception e) {
                            status = 0;
                            Log.d(TAG, e.toString());
                        }
                    } else {
                        // Insert
                        long status = 0;
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(KEY_GSTIN, ctin);
                        contentValues.put(KEY_InvoiceNo, iNum);
                        contentValues.put(KEY_InvoiceDate, iDate);
                        contentValues.put(KEY_Value, element2.getVal());
                        contentValues.put(KEY_POS, element2.getPos());
                        contentValues.put(KEY_ReverseCharge, element2.getRchrg());
                        contentValues.put(KEY_ProvisionalAssess, element2.getPro_ass());
                        contentValues.put(KEY_LineNumber, itemLine);
                        contentValues.put(KEY_SupplyType, item.getTy());
                        contentValues.put(KEY_HSNCode, item.getHsn_sc());
                        contentValues.put(KEY_TaxableValue, item.getTxval());
                        contentValues.put(KEY_IGSTRate, item.getIrt());
                        contentValues.put(KEY_IGSTAmount, item.getIamt());
                        contentValues.put(KEY_CGSTRate, item.getCrt());
                        contentValues.put(KEY_CGSTAmount, item.getCamt());
                        contentValues.put(KEY_SGSTRate, item.getSrt());
                        contentValues.put(KEY_SGSTAmount, item.getSamt());
                        try {
                            status = dbFNB.insert(TBL_READ_FROM_2A, null, contentValues);
                            Log.d(TAG, "code " + status);
                            if (status > 0) {

                            }
                            Log.d(TAG, "Inserted Successfully with code " + status);
                        } catch (Exception e) {
                            status = 0;
                            Log.d(TAG, e.toString());
                        }
                    }
                }
            }
        }
    }

    private boolean isGSTR2B2BdataExists(String ctin, String iNum, String iDate, int itemLine) {
        String selectQuery = "select * from " + TBL_READ_FROM_2A + " where GSTIN='" + ctin + "'  and InvoiceNo='" + iNum + "' and InvoiceDate='" + iDate + "' and LineNumber=" + itemLine + "";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        if (result.getCount() > 0)
            return true;
        else
            return false;
    }

    public Cursor getGSTR2B2BDetails(String StartDate, String EndDate, String gstn) {
        String selectQuery = "SELECT * FROM " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + " WHERE  " + KEY_GSTIN + " = '" + gstn + "' and " + KEY_InvoiceDate + " BETWEEN '" + StartDate + "' AND '" + EndDate + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getGSTR2GSTNs() {
        String selectQuery = "select DISTINCT " + KEY_GSTIN + " from " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + "  where GSTIN !=''";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getGSTR2CDNGSTNs() {
        String selectQuery = "select DISTINCT " + KEY_GSTIN + " from CreditDebitOutward  where GSTIN !=''";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public Cursor getGSTR2B2BItems(String InvoiceNo) {
        String selectQuery = "SELECT * FROM " + TBL_INWARD_SUPPLY_LEDGER + " WHERE  " + KEY_InvoiceNo + " = '" + InvoiceNo + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public void saveGSTR1ASummary(ArrayList<GetGSTR1Summary> dataList) {
        for (int i = 0; i < dataList.size(); i++) {
            GetGSTR1Summary getGSTR1Summary = dataList.get(i);
            String gstin = getGSTR1Summary.getGstin();
            String ret_pd = getGSTR1Summary.getRet_pd();
            double ttl_inv = getGSTR1Summary.getTtl_inv();
            double ttl_tax = getGSTR1Summary.getTtl_tax();
            double ttl_igst = getGSTR1Summary.getTtl_igst();
            double ttl_sgst = getGSTR1Summary.getTtl_sgst();
            double ttl_cgst = getGSTR1Summary.getTtl_cgst();
            ArrayList<GetGSTR1SecSummary> section_summary = getGSTR1Summary.getSec_sum();
            for (int j = 0; j < section_summary.size(); j++) {
                GetGSTR1SecSummary getGSTR1SecSummary = section_summary.get(j);
                String Section_name = getGSTR1SecSummary.getSec_nm();
                double ss_ttl_inv = getGSTR1SecSummary.getTtl_inv();
                double ss_ttl_tax = getGSTR1SecSummary.getTtl_tax();
                double ss_ttl_igst = getGSTR1SecSummary.getTtl_igst();
                double ss_ttl_sgst = getGSTR1SecSummary.getTtl_sgst();
                double ss_ttl_cgst = getGSTR1SecSummary.getTtl_cgst();
                ArrayList<GetGSTR1CounterPartySummary> counter_party_summary = getGSTR1SecSummary.getCpty_sum();
                for (int k = 0; k < counter_party_summary.size(); k++) {
                    GetGSTR1CounterPartySummary getGSTR1CounterPartySummary = counter_party_summary.get(k);
                    String ctin = getGSTR1CounterPartySummary.getCtin();
                    double cs_ttl_inv = getGSTR1CounterPartySummary.getTtl_inv();
                    double cs_ttl_tax = getGSTR1CounterPartySummary.getTtl_tax();
                    double cs_ttl_igst = getGSTR1CounterPartySummary.getTtl_igst();
                    double cs_ttl_sgst = getGSTR1CounterPartySummary.getTtl_sgst();
                    double cs_ttl_cgst = getGSTR1CounterPartySummary.getTtl_cgst();
                    // Insert
                    long status = 0;
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(KEY_GSTIN, ctin);
                    contentValues.put(KEY_ReturnPeriod, ret_pd);
                    contentValues.put(KEY_ttl_inv, ttl_inv);
                    contentValues.put(KEY_ttl_tax, ttl_tax);
                    contentValues.put(KEY_ttl_igst, ttl_igst);
                    contentValues.put(KEY_ttl_sgst, ttl_sgst);
                    contentValues.put(KEY_ttl_cgst, ttl_cgst);
                    contentValues.put(KEY_ss_ttl_inv, ss_ttl_inv);
                    contentValues.put(KEY_ss_ttl_tax, ss_ttl_tax);
                    contentValues.put(KEY_ss_ttl_igst, ss_ttl_igst);
                    contentValues.put(KEY_ss_ttl_sgst, ss_ttl_sgst);
                    contentValues.put(KEY_ss_ttl_cgst, ss_ttl_cgst);
                    contentValues.put(KEY_cs_ttl_inv, cs_ttl_inv);
                    contentValues.put(KEY_cs_ttl_tax, cs_ttl_tax);
                    contentValues.put(KEY_cs_ttl_igst, cs_ttl_igst);
                    contentValues.put(KEY_cs_ttl_sgst, cs_ttl_sgst);
                    contentValues.put(KEY_cs_ttl_cgst, cs_ttl_cgst);
                    contentValues.put(KEY_sec_nm, Section_name);
                    contentValues.put(KEY_ctin, ctin);

                    try {
                        status = dbFNB.insert(TBL_READ_FROM_1A, null, contentValues);
                        Log.d(TAG, "code " + status);
                        if (status > 0) {

                        }
                        Log.d(TAG, "Inserted Successfully with code " + status);
                    } catch (Exception e) {
                        status = 0;
                        Log.d(TAG, e.toString());
                    }
                }
            }
        }
    }


    public long addOwnerDetails(String name, String gstin, String phone, String email, String address, String pos, String office, String RefernceNo, String billPrefix)
    {
        long status = 0;
        ContentValues cvDbValues = new ContentValues();
        cvDbValues.put(KEY_Owner_Name,name);
        cvDbValues.put(KEY_GSTIN,gstin);
        cvDbValues.put(KEY_PhoneNo,phone);
        cvDbValues.put(KEY_USER_EMAIL,email);
        cvDbValues.put(KEY_USER_ADDRESS,address);
        cvDbValues.put(KEY_POS,pos);
        cvDbValues.put(KEY_IsMainOffice,office);
        cvDbValues.put(KEY_REFERENCE_NO,RefernceNo);
        cvDbValues.put(KEY_BillNoPrefix,billPrefix);

        cvDbValues.put(KEY_DeviceId, "MACID_00");
        cvDbValues.put(KEY_DeviceName, "TAB2200+");
        cvDbValues.put(KEY_FIRM_NAME, "Sharma & Sons");
        cvDbValues.put(KEY_TINCIN, "1234567890");
        cvDbValues.put(KEY_IsMainOffice, "YES");


        try {
            status = dbFNB.insert(TBL_OWNER_DETAILS, null, cvDbValues);
            //Log.d(TAG,"code "+status);
            if (status > 0) {

            }
            //Log.d(TAG,"Inserted Successfully with code "+status);
        } catch (Exception e) {
            status = 0;
            Log.d(TAG,e.toString());
        }
        return status;

    }

    public int updateOwnerDetails(String BillNoPrefix)
    {
        int result =0;
        try{
            cvDbValues = new ContentValues();
            cvDbValues.put(KEY_BillNoPrefix, BillNoPrefix);
            result= dbFNB.update(TBL_OWNER_DETAILS, cvDbValues, null, null);
        }catch (Exception e){
            e.printStackTrace();
            result = 0;
        }finally {
            //db.close();
            return result;
        }
    }
    public String getGstin_owner() {
        String gstin = "";
        String selectQuery = "Select * FROM " + TBL_OWNER_DETAILS;
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        if (result != null && result.moveToFirst()) {
            gstin = result.getString(result.getColumnIndex(KEY_GSTIN));
        }
        return gstin;
    }
    public String getBillNoPrefix() {
        String billNoPrefix = "";
        SQLiteDatabase db = getWritableDatabase();
        String selectQuery = "Select BillNoPrefix FROM " + TBL_OWNER_DETAILS;
        try{
            Cursor result = db.rawQuery(selectQuery, null);
            if (result != null && result.moveToFirst()) {
                billNoPrefix = result.getString(result.getColumnIndex(KEY_BillNoPrefix));
                if(billNoPrefix == null)
                    billNoPrefix = "";
            }
        }catch(Exception e)
        {
            e.printStackTrace();
            billNoPrefix = "";
        }
        return billNoPrefix.trim();
    }
    public String getOwnerPOS() {
        String pos = "";
        String selectQuery = "Select POS FROM " + TBL_OWNER_DETAILS;
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        if (result != null && result.moveToFirst()) {
            pos = result.getString(result.getColumnIndex("POS"));
        }
        return pos;
    }
    public String getOwnerPOS_counter() {
        String pos = "";
        String selectQuery = "Select POS FROM " + TBL_OWNER_DETAILS;
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            Cursor result = db.rawQuery(selectQuery, null);
            if (result != null && result.moveToFirst()) {
                pos = result.getString(result.getColumnIndex("POS"));
            }
        }catch (Exception e){
            e.printStackTrace();
            pos = "0";
        }finally {
            return pos;
        }

    }


    public Cursor getInvoice_inward(String date) {
        String selectQuery = " Select " + KEY_TaxableValue + " FROM " + TBL_INWARD_SUPPLY_ITEMS_DETAILS + " WHERE "
                + KEY_InvoiceDate + " LIKE '" + date + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

    public Cursor getInvoice_outward(String date) {
        String selectQuery = " Select " + KEY_TaxableValue + " FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE "
                + KEY_InvoiceDate + " LIKE '" + date + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }

    public ArrayList<String> getGSTR1B2B_A_gstinList(String startDate, String endDate) {
        String selectQuery = "SELECT DISTINCT GSTIN FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2BA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "'  ";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            list.add(gstin);
        }

        return list;
    }
    public Cursor getGSTR1B2b_A_for_gstin(String StartDate, String EndDate, String gstin) {
        String b2b = "B2B";

        String selectQuery = "SELECT InvoiceNo, InvoiceDate, GSTIN,OriginalInvoiceNo,InvoiceNo," +
                " InvoiceDate, OriginalInvoiceDate, TaxableValue , CustStateCode, ReverseCharge,ProvisionalAssess,EcommerceGSTIN FROM " + TBL_GSTR1_AMEND + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "' AND " + KEY_BusinessType + " LIKE 'B2BA' AND "+KEY_GSTIN+" LIKE '"+gstin+"'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getGSTR1B2b_A_for_gstin(String StartDate, String EndDate, String gstin, String invoiceNo) {
        String b2b = "B2B";
        // csrate & csAmount is not extracted as not implemented for now - richa
        String selectQuery = "SELECT DISTINCT InvoiceNo, InvoiceDate, GSTIN,OriginalInvoiceNo,InvoiceNo," +
                " InvoiceDate, OriginalInvoiceDate, TaxableValue , POS, ReverseCharge,ProvisionalAssess,EcommerceGSTIN FROM " + TBL_GSTR1_AMEND + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "' AND " + KEY_BusinessType + " LIKE 'B2BA' AND "+KEY_GSTIN+" LIKE '"+gstin+"' AND "
                +KEY_InvoiceNo+" LIKE '"+invoiceNo+"'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public ArrayList<String> getGSTR1B2B_A_invoiceListList(String startDate, String endDate, String gstin) {
        String selectQuery = "SELECT DISTINCT InvoiceNo FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2BA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate +
                "' AND "+ KEY_GSTIN+" LIKE '"+gstin+"'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String invoiceNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
            list.add(invoiceNo);
        }

        return list;
    }
    public ArrayList<String> getGSTR1B2B_gstinList(String startDate, String endDate) {
        String selectQuery = "SELECT DISTINCT GSTIN FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE  " +
                KEY_BusinessType + " = 'B2B' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "' AND "+
                KEY_BillStatus+" = 1";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            list.add(gstin);
        }

        return list;
    }

    public Cursor getGSTR1B2b_for_gstin(String StartDate, String EndDate, String gstin) {
        String b2b = "B2B";

        String selectQuery = "SELECT * FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE " + KEY_InvoiceDate + " BETWEEN '" + StartDate +
                "' AND '" + EndDate + "' AND " + KEY_BusinessType + " LIKE 'B2B' AND "+KEY_GSTIN+" LIKE '"+gstin+"'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getGSTR1B2CSAItems(String startDate, String endDate) {
        String selectQuery = "SELECT * FROM " + TBL_GSTR1_AMEND + " WHERE  " + KEY_BusinessType + " = 'B2CSA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getGSTR1B2CSAItems1(String startDate) {
        String selectQuery = "SELECT * FROM " + TBL_GSTR1_AMEND + " WHERE  " + KEY_BusinessType + " = 'B2CSA' AND " + KEY_MONTH +
                " LIKE '" + startDate + "' ";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getGSTR1B2BAItems(String startDate, String endDate) {
        String selectQuery = "SELECT * FROM " + TBL_GSTR1_AMEND + " WHERE  " + KEY_BusinessType + " = 'B2BA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }

    public ArrayList<String> getGSTR1B2CL_stateCodeList_ammend_old(String startDate, String endDate) {
        String selectQuery = "SELECT  CustStateCode, POS FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2CLA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "' ";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String pos = cursor.getString(cursor.getColumnIndex("POS"));
            String state_cd = cursor.getString(cursor.getColumnIndex("CustStateCode"));
            if(pos!=null && state_cd!=null && !pos.equalsIgnoreCase(state_cd) )
            {
                if(!list.contains(state_cd))
                    list.add(state_cd);
            }
        }

        return list;
    }public ArrayList<String> getGSTR1B2CL_stateCodeList_ammend(String startDate, String endDate) {
        String selectQuery = "SELECT  CustStateCode FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2CLA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "' ";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
//            String pos = cursor.getString(cursor.getColumnIndex("POS"));
            String state_cd = cursor.getString(cursor.getColumnIndex("CustStateCode"));
            if(!list.contains(state_cd))
                list.add(state_cd);
        }

        return list;
    }
    public ArrayList<String> getGSTR1B2CL_stateCodeList(String startDate, String endDate) {
        String selectQuery = "SELECT  CustStateCode, POS FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE  " +
                KEY_BusinessType + " = 'B2C' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" + endDate + "' AND "+
                KEY_BillStatus+" = 1 AND "+KEY_TaxableValue+" > 250000";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String pos = cursor.getString(cursor.getColumnIndex("POS"));
            String state_cd = cursor.getString(cursor.getColumnIndex("CustStateCode"));
            if(!pos.equalsIgnoreCase(state_cd))
            {
                if(!list.contains(state_cd))
                    list.add(state_cd);
            }
        }

        return list;
    }
    public Cursor getGSTR1B2CL_stateCodeCursor_ammend(String startDate, String endDate, String stateCd) {
        String selectQuery = "SELECT  CustStateCode, POS, Invoicedate, InvoiceNo,OriginalInvoicedate, " +
                "OriginalInvoiceNo,CustName,TaxableValue," +
                "ProvisionalAssess, EcommerceGSTIN FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2CLA' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "' AND "+KEY_CustStateCode+" LIKE '"+stateCd+"'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }
    public Cursor getGSTR1B2CL_stateCodeCursor(String startDate, String endDate, String stateCd) {
        String selectQuery = "SELECT  CustStateCode, POS, Invoicedate, InvoiceNo,CustName,TaxableValue," +
                "ProvisionalAssess, EcommerceGSTIN FROM " + TBL_OUTWARD_SUPPLY_ITEMS_DETAILS + " WHERE  " +
                KEY_BusinessType + " = 'B2C' AND " + KEY_InvoiceDate + " BETWEEN '" + startDate + "' AND '" +
                endDate + "' AND "+KEY_CustStateCode+" LIKE '"+stateCd+"' AND "+
                KEY_BillStatus+" = 1 AND "+KEY_TaxableValue+" > 250000";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }
    public Cursor getGSTR1B2CL_invoices(String InvoiceNo, String InvoiceDate, String custState, String custName) {
        String selectQuery = "SELECT  * FROM " + TBL_OUTWARD_SUPPLY_LEDGER + " WHERE  " +
                KEY_BusinessType + " = 'B2C' AND " + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "' AND " +
                KEY_InvoiceNo + " LIKE '"+InvoiceNo+"' AND "+KEY_CustStateCode+" LIKE '"+custState+"' AND "+KEY_CustName+
                " LIKE '"+custName+"'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }
    public Cursor getGSTR1B2CL_invoices_ammend(String InvoiceNo, String InvoiceDate, String custState, String custName,
                                               String pos) {
        String selectQuery = "SELECT  * FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2CLA' AND " + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "' AND " +
                KEY_InvoiceNo + " LIKE '"+InvoiceNo+"' AND "+KEY_CustStateCode+" LIKE '"+custState+"' AND "+KEY_CustName+
                " LIKE '"+custName+/*"' AND "+KEY_POS+" LIKE '"+pos+*/"'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }
/*
public Cursor getGSTR1B2CL_invoices_ammend(String InvoiceNo, String InvoiceDate, String custState, String custName,
                                               String pos) {
        String selectQuery = "SELECT  * FROM " + TBL_GSTR1_AMEND + " WHERE  " +
                KEY_BusinessType + " = 'B2CLA' AND " + KEY_InvoiceDate + " LIKE '" + InvoiceDate + "' AND " +
                KEY_InvoiceNo + " LIKE '"+InvoiceNo+"' AND "+KEY_CustStateCode+" LIKE '"+custState+"' AND "+KEY_CustName+
                " LIKE '"+custName+"' AND "+KEY_POS+" LIKE '"+pos+"'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        return cursor;
    }
*/

    public Cursor getGSTR1_CDN_forgstin(String startDate, String endDate, String gstin) {
        String selectQuery = "SELECT * FROM CreditDebitOutward WHERE  " + KEY_GSTIN + " = '" + gstin +
                "' and " + KEY_NoteDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getGSTR1_CDN(String startDate, String endDate) {
        String selectQuery = "SELECT * FROM CreditDebitOutward WHERE  " +
                KEY_NoteDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor isNotePresentforInvoice(String invoiceNo, String invoiceDate, String notetype) {
        String selectQuery = "SELECT * FROM CreditDebitOutward WHERE  " + KEY_InvoiceNo + " LIKE '" + invoiceNo +
                "' and " + KEY_InvoiceDate + " Like '" + invoiceDate + "' AND " +KEY_NoteType+" LIKE '" +notetype + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }public Cursor getNoteDetails(String noteNo, String noteDate, String gstin) {
        String selectQuery = "SELECT * FROM CreditDebitOutward WHERE  " + KEY_NoteNo + " LIKE '" + noteNo +
                "' and " + KEY_NoteDate + " Like '" + noteDate + "' AND " +KEY_GSTIN+" LIKE '" +gstin + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public Cursor getInvoices_outward(String startDate, String endDate)
    {
        String selectQuery = "Select * from "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+" WHERE BillStatus =1 AND "+KEY_InvoiceDate+
                " BETWEEN '"+startDate+"' AND '"+endDate+"'";
        return dbFNB.rawQuery(selectQuery, null);
    }
    public Cursor getAllInvoices_outward(String startDate, String endDate)
    {
        String selectQuery = "Select * from "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+" WHERE "+KEY_InvoiceDate+
                " BETWEEN '"+startDate+"' AND '"+endDate+"'";
        return dbFNB.rawQuery(selectQuery, null);
    }


    public ArrayList<String> gethsn_list_for_invoices(Cursor cursor) {
        ArrayList<String > list = new ArrayList<>();
        while (cursor!=null && cursor.moveToNext())
        {
            String invNo = cursor.getString(cursor.getColumnIndex("InvoiceNo"));
            String invDt = cursor.getString(cursor.getColumnIndex("InvoiceDate"));

            String selectQuery = "SELECT  HSNCode FROM "+TBL_OUTWARD_SUPPLY_LEDGER+" WHERE  "
                    + KEY_InvoiceDate + " LIKE '" + invDt + "' AND " + KEY_InvoiceNo +" LIKE '"+invNo+"'";
            Cursor result = dbFNB.rawQuery(selectQuery, null);
            while (result!=null && result.moveToNext())
            {
                String hsn_new = result.getString(result.getColumnIndex("HSNCode"));
                if(!list.contains(hsn_new))
                {
                    list.add(hsn_new);
                }
            }
        }
        return list;
    }

    public Cursor gethsn(String startDate , String endDate, String hsn , String BusinessType)
    {
        String selectQuery = "Select OutwardSuppyItemsDetails.POS, OutwardSupplyLedger.* from "+TBL_OUTWARD_SUPPLY_LEDGER+", " +TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+" Where  OutwardSupplyLedger."+KEY_InvoiceDate+
                " BETWEEN '"+startDate+"' AND '"+endDate+"' AND OutwardSupplyLedger."+KEY_HSNCode+" LIKE '"+hsn+"' AND OutwardSupplyLedger."+
                KEY_BusinessType+" LIKE '" +BusinessType+"' "+
                "AND "+TBL_OUTWARD_SUPPLY_ITEMS_DETAILS+".InvoiceNo = OutwardSupplyLedger.InvoiceNo AND " +
                "OutwardSuppyItemsDetails.InvoiceDate =OutwardSupplyLedger.InvoiceDate  AND OutwardSuppyItemsDetails.BillStatus = 1 ";
        return dbFNB.rawQuery(selectQuery, null);
    }


    public ArrayList<String> getGSTR1_CDN_gstinlist(String startDate, String endDate) {
        String selectQuery = "SELECT DISTINCT GSTIN FROM CreditDebitOutward WHERE  " + KEY_NoteDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if(gstin==null)
                continue;
            else
                list.add(gstin);
        }
        return list;
    }
    public ArrayList<String> getGSTR2_CDN_gstinlist(String startDate, String endDate) {
        String selectQuery = "SELECT DISTINCT GSTIN FROM CreditDebitInward WHERE  " + KEY_NoteDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if(gstin==null)
                continue;
            else
                list.add(gstin);
        }
        return list;
    }
    public Cursor getGSTR2_CDN_forgstin(String startDate, String endDate, String gstin) {
        String selectQuery = "SELECT * FROM CreditDebitInward WHERE  " + KEY_GSTIN + " = '" + gstin +
                "' and " + KEY_NoteDate + " BETWEEN '" + startDate + "' AND '" + endDate + "'";
        Cursor result = dbFNB.rawQuery(selectQuery, null);
        return result;
    }
    public ArrayList<String> getGSTR2_b2b_gstinList(String startDate, String endDate) {
        String selectQuery = "SELECT DISTINCT GSTIN FROM "+TBL_PURCHASEORDER+" WHERE  " + KEY_InvoiceDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "' AND "+KEY_SupplierType+" LIKE 'Registered' AND "+
                KEY_isGoodinward+" LIKE '1'";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if(gstin==null)
                continue;
            else
                list.add(gstin);
        }
        return list;
    }
    public ArrayList<String> getGSTR2_b2b_A_gstinList(String startDate, String endDate) {
        String selectQuery = "SELECT DISTINCT GSTIN FROM "+TBL_GSTR2_AMEND+" WHERE  " + KEY_InvoiceDate +
                " BETWEEN '" + startDate + "' AND '" + endDate + "' AND "+KEY_SupplierType+" LIKE 'Registered' ";
        Cursor cursor = dbFNB.rawQuery(selectQuery, null);
        ArrayList<String> list = new ArrayList<>();
        while(cursor!=null && cursor.moveToNext())
        {
            String gstin = cursor.getString(cursor.getColumnIndex("GSTIN"));
            if(gstin==null)
                continue;
            else
                list.add(gstin);
        }
        return list;
    }
    public Cursor getGSTR2_b2b_A_unregisteredSupplierList(String startDate,String endDate ) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_GSTR2_AMEND + " WHERE " +
                /*KEY_PurchaseOrderNo+" LIKE '"+purchaseorder+"' AND "+*/
                KEY_SupplierType+" LIKE 'UnRegistered' AND "+
                KEY_InvoiceDate+" BETWEEN '"+startDate+"' AND '"+endDate+"'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    public Cursor getGSTR2_A_ammend_for_supplierName(String invoiceNo,String invoiceDate,String invoiceNo_ori,String invoiceDate_ori,
                                                     String supplierName, String pos_supplier ) {
        Cursor result = null;
        String queryString = "Select * FROM " + TBL_GSTR2_AMEND + " WHERE " + KEY_OriginalInvoiceNo + " Like '" + invoiceNo_ori + "' AND " +
                KEY_OriginalInvoiceDate+" LIKE '"+invoiceDate_ori+"' AND "+ KEY_SupplierType+" LIKE 'UnRegistered' AND "+
                KEY_InvoiceDate+" LIKE '"+invoiceDate+"' AND "+KEY_InvoiceNo+" LIKE '"+invoiceNo+"' AND "+
                KEY_GSTIN+" LIKE '"+supplierName+"' AND "+KEY_POS+" LIKE '"+pos_supplier+"'";
        result = dbFNB.rawQuery(queryString, null);
        return result;
    }

    // All New Methods
    public Cursor getItemDepartments() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select DeptCode as _id, DeptName from Department", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            if(db.isOpen())
                db.close();
        }
        return cursor;
    }

    public ArrayList<Items> getItemItems() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Items> list = null;
        try{
            Cursor cursor = db.query(TBL_ITEM_Outward,null,null,null,null,null,null);
            if(cursor!=null)
            {
                list = new ArrayList<Items>();
                while (cursor.moveToNext())
                {
                    Items items = new Items(
                            cursor.getString(cursor.getColumnIndex("ItemName")),
                            cursor.getString(cursor.getColumnIndex("ImageUri")),
                            cursor.getInt(cursor.getColumnIndex("MenuCode"))
                    );
                    list.add(items);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }finally {
            //db.close();
        }
        return list;
    }


    public ArrayList<Items> getItemItems(int CategCode) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Items> list = null;
        try{
            Cursor cursor = db.query(TBL_ITEM_Outward, new String[]{"MenuCode", "ItemName", "ImageUri"}, "CategCode=" + CategCode, null, null, null, null);
            if(cursor!=null)
            {
                list = new ArrayList<Items>();
                while (cursor.moveToNext())
                {
                    Items items = new Items(
                            cursor.getString(cursor.getColumnIndex("ItemName")),
                            cursor.getString(cursor.getColumnIndex("ImageUri")),
                            cursor.getInt(cursor.getColumnIndex("MenuCode"))
                    );
                    list.add(items);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }finally {
            //db.close();
        }
        return list;
    }

    public ArrayList<Items> getItemItems_dept(int deptCode) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Items> list = null;
        try{
            Cursor cursor = db.query(TBL_ITEM_Outward, new String[]{"MenuCode", "ItemName", "ImageUri"},
                    KEY_DeptCode+"=" + deptCode, null, null, null, null);
            if(cursor!=null)
            {
                list = new ArrayList<Items>();
                while (cursor.moveToNext())
                {
                    Items items = new Items(
                            cursor.getString(cursor.getColumnIndex("ItemName")),
                            cursor.getString(cursor.getColumnIndex("ImageUri")),
                            cursor.getInt(cursor.getColumnIndex("MenuCode"))
                    );
                    list.add(items);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }finally {
            //db.close();
        }
        return list;
    }


    public ArrayList<Department> getItemDepartment() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Department> list = null;
        try{
            Cursor cursor = db.query(TBL_DEPARTMENT, new String[]{"DeptCode", "DeptName"}, null, null, null, null, null);
            if(cursor!=null)
            {
                list = new ArrayList<Department>();
                while (cursor.moveToNext())
                {
                    Department items = new Department(cursor.getString(cursor.getColumnIndex("DeptName")), cursor.getInt(cursor.getColumnIndex("DeptCode")));
                    list.add(items);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }finally {
            //db.close();
        }
        return list;
    }

    public ArrayList<Category> getAllItemCategory() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Category> list = null;
        try{
            Cursor cursor = db.rawQuery("Select * from Category", null);
            if(cursor!=null)
            {
                list = new ArrayList<Category>();
                while (cursor.moveToNext())
                {
                    Category items = new Category(cursor.getString(cursor.getColumnIndex("CategName")), cursor.getInt(cursor.getColumnIndex("CategCode")), cursor.getInt(cursor.getColumnIndex("DeptCode")));
                    list.add(items);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }finally {
            //db.close();
        }
        return list;
    }

    public ArrayList<Category> getAllItemCategory(int DeptCode) {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<Category> list = null;
        try{
            Cursor cursor = db.rawQuery("Select CategCode as _id, CategName, DeptCode from Category where DeptCode=" + DeptCode, null);
            if(cursor!=null)
            {
                list = new ArrayList<Category>();
                while (cursor.moveToNext())
                {
                    Category items = new Category(cursor.getString(cursor.getColumnIndex("CategName")), cursor.getInt(cursor.getColumnIndex("_id")), cursor.getInt(cursor.getColumnIndex("DeptCode")));
                    list.add(items);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }finally {
            //db.close();
        }
        return list;
    }

    // -----Retrieve Single Item based on Item MenuCode-----
    public Cursor getItemss(int MenuCode) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_ITEM_Outward, new String[]{"*"}, "MenuCode=" + MenuCode, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    // -----Retrieve Bill setting-----
    public Cursor getBillSettings() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_BILLSETTING, new String[]{"*"}, null, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    public Cursor getKOTModifierByModes_new(String strModes) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_KOTMODIFIER,
                    new String[]{"ModifierId", "ModifierDescription", "ModifierAmount", "IsChargeable", "ModifierModes"}, "IsChargeable = '1' AND ModifierModes='" + strModes + "'", null,
                    null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;

    }
    public Cursor getKOTItems_new(int CustId, String OrderMode) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_PENDINGKOT, new String[]{"*"}, "CustId=" + CustId + " AND OrderMode=" + OrderMode,
                    null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;

    }


    // -----Retrieve single TaxConfig-----
    public Cursor getTaxConfigs(int TaxId)
    {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_TAXCONFIG, new String[]{"TaxId", "TaxDescription", "TaxPercentage", "TotalPercentage"}, "TaxId=" + TaxId, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    // -----Retrieve single Customer-----
    public Cursor getFnbCustomer(String strCustPhone) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_CUSTOMER, new String[]{"*"}, "CustContactNumber='" + strCustPhone + "'", null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    // -----Retrieve single Customer-----
    public Cursor getCustomerById(int iCustId) {

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_CUSTOMER, new String[]{"*"}, "CustId=" + iCustId, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    // -----Insert Customer-----
    public long addCustomers(Customer objCustomer) {
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues cvDbValues = new ContentValues();
            cvDbValues.put("CustName", objCustomer.getCustName());
            cvDbValues.put("LastTransaction", objCustomer.getLastTransaction());
            cvDbValues.put("TotalTransaction", objCustomer.getTotalTransaction());
            cvDbValues.put("CustContactNumber", objCustomer.getCustContactNumber());
            cvDbValues.put("CustAddress", objCustomer.getCustAddress());
            cvDbValues.put("CreditAmount", objCustomer.getCreditAmount());
            cvDbValues.put(KEY_GSTIN, objCustomer.getStrCustGSTIN());
            return db.insert(TBL_CUSTOMER, null, cvDbValues);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }finally {
            //db.close();
        }
    }

    // -----Retrieve all SubTaxConfig-----
    public Cursor getAllSubTaxConfigs(String TaxId) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_SUBTAXCONFIG, new String[]{"SubTaxId", "SubTaxDescription", "SubTaxPercent"}, "TaxId=" + TaxId, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    public Cursor getItemsForOtherChargesPrints(String jBillingMode) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select * from " + TBL_KOTMODIFIER + " where ModifierModes LIKE '" + jBillingMode+ "' AND "+
                    KEY_IsChargeable+" LIKE '1'", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    // -----Retrieve KOT items for sa;es tax print
    public Cursor getItemsForSalesTaxPrints(int InvoiceNo) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select SUM(TaxAmount) as TaxAmount, TaxPercent from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "' GROUP BY TaxPercent", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    public Cursor getItemsForIGSTTaxPrints(int InvoiceNo, String InvoiceDate) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select SUM(IGSTAmount) as IGSTAmount, IGSTRate from " + TBL_BILLITEM +
                    " where InvoiceNo = '" + InvoiceNo + "' AND +"+KEY_InvoiceDate+" LIKE '"+InvoiceDate+"' GROUP BY IGSTRate", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    public Cursor getItemsForCGSTTaxPrints(int InvoiceNo, String InvoiceDate) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select SUM(CGSTAmount) as CGSTAmount, CGSTRate from " + TBL_BILLITEM +
                    " where InvoiceNo = '" + InvoiceNo + "' AND InvoiceDate LIKE '"+InvoiceDate+"' GROUP BY CGSTRate", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }
    /*public Cursor getItemsForCGSTTaxPrint(int InvoiceNo) {
        //SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = dbFNB.rawQuery("Select SUM(CGSTAmount) as CGSTAmount, CGSTRate from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "' GROUP BY CGSTRate", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }*/

    // -----Retrieve KOT items for service tax print
    public Cursor getItemsForServiceTaxPrints(int InvoiceNo) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select SUM(ServiceTaxAmount) as TaxAmount, ServiceTaxPercent from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "' GROUP BY TaxPercent", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    public Cursor getItemsForSGSTTaxPrints(int InvoiceNo, String InvoiceDate) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select SUM(SGSTAmount) as SGSTAmount, SGSTRate from " + TBL_BILLITEM +
                    " where InvoiceNo = '" + InvoiceNo + "' AND "+KEY_InvoiceDate+" LIKE '"+InvoiceDate+"' GROUP BY SGSTRate", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }
    public Cursor getItemsForcessTaxPrints(int InvoiceNo, String InvoiceDate) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select SUM(cessAmount) as cessAmount, cessRate from " + TBL_BILLITEM +
                    " where InvoiceNo = '" + InvoiceNo + "' AND "+KEY_InvoiceDate+" LIKE '"+InvoiceDate+"' GROUP BY cessRate", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    public Cursor getItemsForTaxSlabPrints(int InvoiceNo, String InvoiceDate) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select IGSTRate, CGSTRate, SGSTRate, IGSTAmount, CGSTAmount, SGSTAmount, TaxableValue from " + TBL_BILLITEM +
                    " where InvoiceNo = '" + InvoiceNo + "' AND "+KEY_InvoiceDate+" LIKE '"+InvoiceDate+"'", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
            db.close();
        }finally {
            return cursor;

        }


    }

    /*public Cursor getItemsForSGSTTaxPrint(int InvoiceNo) {

        Cursor cursor = null;
        try{
            cursor = dbFNB.rawQuery("Select SUM(SGSTAmount) as SGSTAmount, SGSTRate from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "' GROUP BY SGSTRate", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }*/


    // -----Retrieve new bill Number-----
    public int getNewBillNumber() {
        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor result;
            result = db.rawQuery("SELECT " + KEY_InvoiceNo + " FROM BillNoConfiguration", null);
            if (result.moveToFirst()) {
                return result.getInt(0) + 1;
            } else {
                return 1;
            }
        }catch (Exception e){
            e.printStackTrace();
            return 1;
        }finally {
            //db.close();
        }
    }

    /************************************************************************************************************************************/
    // -----Insert Bill Items-----
    public long addBillItems(BillItem objBillItem) {
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues cvDbValues = new ContentValues();
            cvDbValues.put(KEY_InvoiceNo, objBillItem.getBillNumber());
            cvDbValues.put(KEY_BillingMode, objBillItem.getBillingMode()); // richa_2012
            cvDbValues.put("ItemNumber", objBillItem.getItemNumber());
            cvDbValues.put("ItemName", objBillItem.getItemName());
            cvDbValues.put("Quantity", objBillItem.getQuantity());
            cvDbValues.put("Value", objBillItem.getValue());
            cvDbValues.put("ModifierAmount", objBillItem.getModifierAmount());
            cvDbValues.put(KEY_TaxableValue, objBillItem.getAmount());
            cvDbValues.put("DiscountAmount", objBillItem.getDiscountAmount());
            cvDbValues.put(KEY_DiscountPercent, objBillItem.getDiscountPercent());
            cvDbValues.put("ServiceTaxAmount", objBillItem.getServiceTaxAmount());
            cvDbValues.put("ServiceTaxPercent", objBillItem.getServiceTaxPercent());
            cvDbValues.put("TaxAmount", objBillItem.getTaxAmount());
            cvDbValues.put("TaxPercent", objBillItem.getTaxPercent());
            cvDbValues.put("DeptCode", objBillItem.getDeptCode());
            cvDbValues.put("CategCode", objBillItem.getCategCode());
            cvDbValues.put("KitchenCode", objBillItem.getKitchenCode());
            cvDbValues.put("TaxType", objBillItem.getTaxType());
            cvDbValues.put(KEY_InvoiceDate, objBillItem.getInvoiceDate());
            cvDbValues.put(KEY_HSNCode, objBillItem.getHSNCode());
            cvDbValues.put(KEY_IGSTRate, objBillItem.getIGSTRate());
            cvDbValues.put(KEY_IGSTAmount, objBillItem.getIGSTAmount());
            cvDbValues.put(KEY_CGSTRate, objBillItem.getCGSTRate());
            cvDbValues.put(KEY_CGSTAmount, objBillItem.getCGSTAmount());
            cvDbValues.put(KEY_SGSTRate, objBillItem.getSGSTRate());
            cvDbValues.put(KEY_SGSTAmount, objBillItem.getSGSTAmount());
            cvDbValues.put(KEY_cessRate, objBillItem.getCessRate());
            cvDbValues.put(KEY_cessAmount, objBillItem.getCessAmount());
            cvDbValues.put(KEY_SupplyType, objBillItem.getSupplyType());
            cvDbValues.put(KEY_SubTotal, objBillItem.getSubTotal());
            cvDbValues.put(KEY_CustName, objBillItem.getCustName());
            cvDbValues.put(KEY_GSTIN, objBillItem.getGSTIN());
            cvDbValues.put(KEY_CustStateCode, objBillItem.getCustStateCode());
            cvDbValues.put(KEY_UOM, objBillItem.getUom());
            cvDbValues.put(KEY_BusinessType, objBillItem.getBusinessType());
            cvDbValues.put(KEY_BillStatus, objBillItem.getBillStatus());

            return db.insert(TBL_BILLITEM, null, cvDbValues);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }finally {
            //db.close();
        }
    }

    /************************************************************************************************************************************/
    /******************************************************
     * Table - BillDetail
     **********************************************************/
    /************************************************************************************************************************************/
    // -----Insert Bill-----
    public long addBilll(BillDetail objBillDetail, String gstin) {
        long rData = -1;
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues cvDbValues = new ContentValues();
            cvDbValues.put(KEY_BillingMode, objBillDetail.getBillingMode()); // richa_2012
            cvDbValues.put(KEY_InvoiceNo, objBillDetail.getBillNumber());
            cvDbValues.put("Time", objBillDetail.getTime());
            cvDbValues.put(KEY_GSTIN, gstin);
            cvDbValues.put(KEY_InvoiceDate, objBillDetail.getDate());
            cvDbValues.put(KEY_GrandTotal, objBillDetail.getBillAmount());
            cvDbValues.put("TotalItems", objBillDetail.getTotalItems());
            cvDbValues.put("BillAmount", objBillDetail.getBillAmount());
            cvDbValues.put("TotalDiscountAmount", objBillDetail.getTotalDiscountAmount());
            cvDbValues.put(KEY_DiscPercentage, objBillDetail.getTotalDiscountPercentage());
            cvDbValues.put("TotalServiceTaxAmount", objBillDetail.getTotalServiceTaxAmount());
            cvDbValues.put("TotalTaxAmount", objBillDetail.getTotalTaxAmount());
            cvDbValues.put("CashPayment", objBillDetail.getCashPayment());
            cvDbValues.put("CardPayment", objBillDetail.getCardPayment());
            cvDbValues.put("CouponPayment", objBillDetail.getCouponPayment());
            cvDbValues.put("BillStatus", objBillDetail.getBillStatus());
            cvDbValues.put("ReprintCount", objBillDetail.getReprintCount());
            cvDbValues.put("DeliveryCharge", objBillDetail.getDeliveryCharge());
            cvDbValues.put("EmployeeId", objBillDetail.getEmployeeId());
            cvDbValues.put("UserId", objBillDetail.getUserId());
            cvDbValues.put("CustId", objBillDetail.getCustId());
            cvDbValues.put("PettyCashPayment", objBillDetail.getPettyCashPayment());
            cvDbValues.put(KEY_WalletPayment, objBillDetail.getWalletAmount());
            cvDbValues.put("PaidTotalPayment", objBillDetail.getPaidTotalPayment());
            cvDbValues.put("ChangePayment", objBillDetail.getChangePayment());
            cvDbValues.put(KEY_CustName, objBillDetail.getCustname());
            cvDbValues.put(KEY_CustStateCode, objBillDetail.getCustStateCode());
            cvDbValues.put(KEY_POS, objBillDetail.getPOS());
            cvDbValues.put(KEY_BusinessType, objBillDetail.getBusinessType());
            cvDbValues.put(KEY_TaxableValue, objBillDetail.getAmount());
            cvDbValues.put(KEY_IGSTAmount, objBillDetail.getIGSTAmount());
            cvDbValues.put(KEY_CGSTAmount, objBillDetail.getCGSTAmount());
            cvDbValues.put(KEY_SGSTAmount, objBillDetail.getSGSTAmount());
            cvDbValues.put(KEY_cessAmount, objBillDetail.getCessAmount());
            cvDbValues.put(KEY_SubTotal, objBillDetail.getSubTotal());

            rData = db.insert(TBL_BILLDETAIL, null, cvDbValues);
        }catch (Exception e){
            Log.d(TAG,e.toString());
            rData = -1;
        }finally {
            //db.close();
        }
        return rData;
    }

    /*public long updateBill(BillDetail objBillDetail) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_BillAmount,objBillDetail.getBillAmount());
        return dbFNB.update(TBL_BILLDETAIL,cv,KEY_InvoiceNo+"="+objBillDetail.getBillNumber()+" AND " +KEY_InvoiceDate+"="+objBillDetail.getDate()+" AND "+KEY_CustId+" ="+objBillDetail.getCustId(),null);

    }*/

    // -----Delete Customer items from Pending KOT table-----
    public int deleteKOTItem(int CustId, String OrderMode) {
        SQLiteDatabase db = getWritableDatabase();
        try{
            int result = db.delete(TBL_PENDINGKOT, "CustId=" + CustId + " AND OrderMode=" + OrderMode, null);
            return result;
        }catch (Exception e){
            e.printStackTrace();
            return 1;
        }finally {
            //db.close();
        }
    }

    /************************************************************************************************************************************/
    /***********************************************
     * Table - ComplimentaryBillDetail
     ****************************************************/
    /************************************************************************************************************************************/
    // -----Insert Complimentary Bill-----
    public long addComplimentaryBillDetails(ComplimentaryBillDetail objComplimentaryBillDetail) {
        SQLiteDatabase db = getWritableDatabase();
        try{
            ContentValues cvDbValues = new ContentValues();
            cvDbValues.put(KEY_InvoiceNo, objComplimentaryBillDetail.getBillNumber());
            cvDbValues.put("ComplimentaryReason", objComplimentaryBillDetail.getComplimentaryReason());
            cvDbValues.put("PaidAmount", objComplimentaryBillDetail.getPaidAmount());
            return db.insert(TBL_COMPLIMENTARYBILLDETAIL, null, cvDbValues);
        }catch (Exception e){
            e.printStackTrace();
            return 1;
        }finally {
            //db.close();
        }
    }
    public int UpdateBillNoResetInvoiceNos(int invno) {
        SQLiteDatabase db = getWritableDatabase();
        int result =0;
        try{
            cvDbValues = new ContentValues();
            cvDbValues.put("InvoiceNo", invno);
            result= db.update("BillNoConfiguration", cvDbValues, null, null);
        }catch (Exception e){
            e.printStackTrace();
            result = 0;
        }finally {
            //db.close();
            return result;
        }
    }

    public Cursor getItemLists(String Name) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT * FROM " + TBL_ITEM_Outward + "  WHERE ItemName LIKE '" + Name + "%'", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }
    public Cursor getItemDetail(String Name) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("SELECT * FROM " + TBL_ITEM_Outward + "  WHERE ItemName LIKE '" + Name + "'", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    public List<String> getAllItemsNames() {
        SQLiteDatabase db = getWritableDatabase();
        List<String> list = new ArrayList<String>();
        try{
            // Select All Query
            String selectQuery = "SELECT  ItemName FROM " + TBL_ITEM_Outward;
            Cursor cursor = db.rawQuery(selectQuery, null);// selectQuery,selectedArguments
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndex(KEY_ItemName)));// adding
                    // 2nd
                    // column
                    // data
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }finally {
            //db.close();
        }
        // returning lables
        return list;
    }

    public List<String> getAllMenuCodes() {
        SQLiteDatabase db = getWritableDatabase();
        List<String> list = new ArrayList<String>();

        try{
            // Select All Query
            String selectQuery = "SELECT  MenuCode FROM " + TBL_ITEM_Outward;
            Cursor cursor = db.rawQuery(selectQuery, null);// selectQuery,selectedArguments
            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    list.add(cursor.getString(cursor.getColumnIndex("MenuCode")));// adding
                    // 2nd
                    // column
                    // data
                } while (cursor.moveToNext());
            }
        }catch (Exception e){
            e.printStackTrace();
            list = null;
        }finally {
            //db.close();
        }
        // returning lables
        return list;
    }

    // -----Retrieve single bill details-----
    public Cursor getBillDetails(int InvoiceNumber) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_BILLDETAIL, new String[]{"*"}, KEY_InvoiceNo + "=" + InvoiceNumber, null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }
    public Cursor getUserr(String UserId, String Password) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.query(TBL_USERS, new String[]{"*"}, "LoginId ='" + UserId + "' AND Password ='" + Password + "'",
                    null, null, null, null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    // -----Void Bill-----
    public int makeBillVoids(int InvoiceNo) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            ContentValues cvDbValues = new ContentValues();
            cvDbValues.put(KEY_BillStatus, 0);
            return db.update(TBL_BILLDETAIL, cvDbValues, KEY_InvoiceNo + "=" + InvoiceNo, null);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }finally {
            //db.close();
        }
    }

    // -----Retrieve KOT items for Reprint bill from Bill Detail-----
    public Cursor getItemsForReprintBills(int InvoiceNo) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = null;
        try{
            cursor = db.rawQuery("Select * from " + TBL_BILLITEM + " where InvoiceNo = '" + InvoiceNo + "'", null);
        }catch (Exception e){
            e.printStackTrace();
            cursor = null;
        }finally {
            //db.close();
        }
        return cursor;
    }

    // -----update Reprint Count for duplicate bill print-----
    public int updateBillRepintCounts(int InvoiceNo) {
        SQLiteDatabase db = getWritableDatabase();
        try{
            Cursor result = getBillDetails(InvoiceNo);
            if (result.moveToFirst()) {
                cvDbValues = new ContentValues();
                int iReprintCount = 0;
                iReprintCount = result.getInt(result.getColumnIndex("ReprintCount"));
                cvDbValues.put("ReprintCount", iReprintCount + 1);
                return db.update(TBL_BILLDETAIL, cvDbValues, KEY_InvoiceNo + "=" + InvoiceNo, null);

            } else {
                Toast.makeText(myContext, "No bill found with bill number " + InvoiceNo, Toast.LENGTH_SHORT).show();
                return -1;
            }
        }catch (Exception e){
            e.printStackTrace();
            return -1;

        }finally {
            //db.close();
        }
    }

    // -----Delete Items from Item Table-----
    public int deleteAllOutwardItem() {
        SQLiteDatabase db = getWritableDatabase();
        int del =0;
        try{
            del = db.delete(TBL_ITEM_Outward, null, null);
        }catch (Exception e){
            e.printStackTrace();
            del = 0;
        }finally {
            //db.close();
        }
        return del;
    }

    public ArrayList<ItemOutward> getAllItem() {
        SQLiteDatabase db = getWritableDatabase();
        ArrayList<ItemOutward> dataList = new ArrayList<ItemOutward>();
        try{
            Cursor cursorItem = db.query(TBL_ITEM_Outward, new String[]{"*"}, null, null, null, null, null);
            while(cursorItem!=null && cursorItem.moveToNext())
            {
                ItemOutward item = new ItemOutward();
                item.setMenuCode(cursorItem.getInt(cursorItem.getColumnIndex("MenuCode")));
                item.setItemName(cursorItem.getString(cursorItem.getColumnIndex("ItemName")));
                item.setDineIn1(cursorItem.getFloat(cursorItem.getColumnIndex("DineInPrice1")));
                item.setDineIn2(cursorItem.getFloat(cursorItem.getColumnIndex("DineInPrice2")));
                item.setDineIn3(cursorItem.getFloat(cursorItem.getColumnIndex("DineInPrice3")));
                item.setStock(cursorItem.getFloat(cursorItem.getColumnIndex("Quantity")));
                item.setDeptCode(cursorItem.getInt(cursorItem.getColumnIndex("DeptCode")));
                item.setCategCode(cursorItem.getInt(cursorItem.getColumnIndex("CategCode")));
                item.setKitchenCode(cursorItem.getInt(cursorItem.getColumnIndex("KitchenCode")));
                item.setBarCode(cursorItem.getString(cursorItem.getColumnIndex("ItemBarcode")));
                item.setImageUri(cursorItem.getString(cursorItem.getColumnIndex("ImageUri")));
                item.setUOM(cursorItem.getString(cursorItem.getColumnIndex("UOM")));
                item.setCGSTRate(cursorItem.getFloat(cursorItem.getColumnIndex("SalesTaxPercent")));
                item.setSGSTRate(cursorItem.getFloat(cursorItem.getColumnIndex("ServiceTaxPercent")));
                item.setItemId(cursorItem.getInt(cursorItem.getColumnIndex("ItemId")));
                dataList.add(item);
            }
        }catch (Exception e){
            dataList = null;
            e.printStackTrace();
        }finally {
            //db.close();
        }
        return dataList;
    }


    public int deleteAllInwardItem() {
        SQLiteDatabase db = getWritableDatabase();
        int del =0;
        try{
            del = db.delete(TBL_ITEM_Inward, null, null);
        }catch (Exception e){
            e.printStackTrace();
            del = 0;
        }finally {
            //db.close();
        }
        return del;
    }

}
