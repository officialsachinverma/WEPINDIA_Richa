package com.wep.common.app.print;

import java.io.Serializable;

/**
 * Created by PriyabratP on 29-09-2016.
 */
public class Payment implements Serializable{

    public String bankName = "";
    public String merchantName = "";
    public String merchantAdd = "";
    public String dateTime = "";
    public String mId = "";
    public String tId = "";
    public String batchNo = "";
    public String voucherNo = "";
    public String refNo = "";
    public String saleType = "";
    public String cardNo = "";
    public String trxType = "";
    public String cardType = "";
    public String expDate = "";
    public String emvSigExpDate = "";
    public String cardHolderName = "";
    public String currency = "";
    public String cashAmount = "";
    public String baseAmount = "";
    public String tipAmount = "";
    public String totalAmount = "";
    public String authCode = "";
    public String rrNo = "";
    public String certif = "";
    public String appId = "";
    public String appName = "";
    public String tvr = "";
    public String tsi = "";
    public String appVersion = "";
    public String isPinVarifed = "";
    public String stan = "";
    public String cardIssuer = "";
    public String emiPerMonthAmount = "";
    public String total_Pay_Amount = "";
    public String noOfEmi = "";
    public String interestRate = "";
    public String billNo = "";
    public String firstDigitsOfCard = "";
    public String isConvenceFeeEnable = "false";
    public String invoiceNo = "";
    public String trxDate = "";
    public String trxImgDate = "";
    public String chequeDate = "";
    public String chequeNo = "";
    public String paymentType = "";
    public String paymentStatus = "";

    public Payment(){

    }

    public Payment(String bankName, String merchantName, String merchantAdd, String dateTime, String mId, String tId, String batchNo, String voucherNo, String refNo, String saleType, String cardNo, String trxType, String cardType, String expDate, String emvSigExpDate, String cardHolderName, String currency, String cashAmount, String baseAmount, String tipAmount, String totalAmount, String authCode, String rrNo, String certif, String appId, String appName, String tvr, String tsi, String appVersion, String isPinVarifed, String stan, String cardIssuer, String emiPerMonthAmount, String total_Pay_Amount, String noOfEmi, String interestRate, String billNo, String firstDigitsOfCard, String isConvenceFeeEnable, String invoiceNo, String trxDate, String trxImgDate, String chequeDate, String chequeNo) {
        this.bankName = bankName;
        this.merchantName = merchantName;
        this.merchantAdd = merchantAdd;
        this.dateTime = dateTime;
        this.mId = mId;
        this.tId = tId;
        this.batchNo = batchNo;
        this.voucherNo = voucherNo;
        this.refNo = refNo;
        this.saleType = saleType;
        this.cardNo = cardNo;
        this.trxType = trxType;
        this.cardType = cardType;
        this.expDate = expDate;
        this.emvSigExpDate = emvSigExpDate;
        this.cardHolderName = cardHolderName;
        this.currency = currency;
        this.cashAmount = cashAmount;
        this.baseAmount = baseAmount;
        this.tipAmount = tipAmount;
        this.totalAmount = totalAmount;
        this.authCode = authCode;
        this.rrNo = rrNo;
        this.certif = certif;
        this.appId = appId;
        this.appName = appName;
        this.tvr = tvr;
        this.tsi = tsi;
        this.appVersion = appVersion;
        this.isPinVarifed = isPinVarifed;
        this.stan = stan;
        this.cardIssuer = cardIssuer;
        this.emiPerMonthAmount = emiPerMonthAmount;
        this.total_Pay_Amount = total_Pay_Amount;
        this.noOfEmi = noOfEmi;
        this.interestRate = interestRate;
        this.billNo = billNo;
        this.firstDigitsOfCard = firstDigitsOfCard;
        this.isConvenceFeeEnable = isConvenceFeeEnable;
        this.invoiceNo = invoiceNo;
        this.trxDate = trxDate;
        this.trxImgDate = trxImgDate;
        this.chequeDate = chequeDate;
        this.chequeNo = chequeNo;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantAdd() {
        return merchantAdd;
    }

    public void setMerchantAdd(String merchantAdd) {
        this.merchantAdd = merchantAdd;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String gettId() {
        return tId;
    }

    public void settId(String tId) {
        this.tId = tId;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getVoucherNo() {
        return voucherNo;
    }

    public void setVoucherNo(String voucherNo) {
        this.voucherNo = voucherNo;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public String getSaleType() {
        return saleType;
    }

    public void setSaleType(String saleType) {
        this.saleType = saleType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getTrxType() {
        return trxType;
    }

    public void setTrxType(String trxType) {
        this.trxType = trxType;
    }

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getEmvSigExpDate() {
        return emvSigExpDate;
    }

    public void setEmvSigExpDate(String emvSigExpDate) {
        this.emvSigExpDate = emvSigExpDate;
    }

    public String getCardHolderName() {
        return cardHolderName;
    }

    public void setCardHolderName(String cardHolderName) {
        this.cardHolderName = cardHolderName;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(String cashAmount) {
        this.cashAmount = cashAmount;
    }

    public String getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(String baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(String tipAmount) {
        this.tipAmount = tipAmount;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getRrNo() {
        return rrNo;
    }

    public void setRrNo(String rrNo) {
        this.rrNo = rrNo;
    }

    public String getCertif() {
        return certif;
    }

    public void setCertif(String certif) {
        this.certif = certif;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getTvr() {
        return tvr;
    }

    public void setTvr(String tvr) {
        this.tvr = tvr;
    }

    public String getTsi() {
        return tsi;
    }

    public void setTsi(String tsi) {
        this.tsi = tsi;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getIsPinVarifed() {
        return isPinVarifed;
    }

    public void setIsPinVarifed(String isPinVarifed) {
        this.isPinVarifed = isPinVarifed;
    }

    public String getStan() {
        return stan;
    }

    public void setStan(String stan) {
        this.stan = stan;
    }

    public String getCardIssuer() {
        return cardIssuer;
    }

    public void setCardIssuer(String cardIssuer) {
        this.cardIssuer = cardIssuer;
    }

    public String getEmiPerMonthAmount() {
        return emiPerMonthAmount;
    }

    public void setEmiPerMonthAmount(String emiPerMonthAmount) {
        this.emiPerMonthAmount = emiPerMonthAmount;
    }

    public String getTotal_Pay_Amount() {
        return total_Pay_Amount;
    }

    public void setTotal_Pay_Amount(String total_Pay_Amount) {
        this.total_Pay_Amount = total_Pay_Amount;
    }

    public String getNoOfEmi() {
        return noOfEmi;
    }

    public void setNoOfEmi(String noOfEmi) {
        this.noOfEmi = noOfEmi;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getFirstDigitsOfCard() {
        return firstDigitsOfCard;
    }

    public void setFirstDigitsOfCard(String firstDigitsOfCard) {
        this.firstDigitsOfCard = firstDigitsOfCard;
    }

    public String getIsConvenceFeeEnable() {
        return isConvenceFeeEnable;
    }

    public void setIsConvenceFeeEnable(String isConvenceFeeEnable) {
        this.isConvenceFeeEnable = isConvenceFeeEnable;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getTrxDate() {
        return trxDate;
    }

    public void setTrxDate(String trxDate) {
        this.trxDate = trxDate;
    }

    public String getTrxImgDate() {
        return trxImgDate;
    }

    public void setTrxImgDate(String trxImgDate) {
        this.trxImgDate = trxImgDate;
    }

    public String getChequeDate() {
        return chequeDate;
    }

    public void setChequeDate(String chequeDate) {
        this.chequeDate = chequeDate;
    }

    public String getChequeNo() {
        return chequeNo;
    }

    public void setChequeNo(String chequeNo) {
        this.chequeNo = chequeNo;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
