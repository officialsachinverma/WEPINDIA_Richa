package com.wep.common.app.Database;

/**
 * Created by ALV on 21-09-2016.
 */
public class MailSettings {
    // Private variables
    String strFromMailId;
    String strFromMailPassword;
    String strSmtpServer;
    String strPortNo;
    String strFromDate;
    String strToDate;
    int iSendMail;
    int iAutoMail;

    // Default constructor
    public MailSettings() {
        this.strFromMailId = "";
        this.strFromMailPassword = "";
        this.strSmtpServer = "";
        this.strPortNo = "";
        this.strFromDate = "";
        this.strToDate = "";
        this.iSendMail = 0;
        this.iAutoMail = 0;
    }

    // Parameterized construcor
    public MailSettings(String FromMailId, String FromMailPassword, String SmtpServer, String PortNo, String FromDate, String ToDate, int SendMail, int AutoMail) {
        this.strFromMailId = FromMailId;
        this.strFromMailPassword = FromMailPassword;
        this.strSmtpServer = SmtpServer;
        this.strPortNo = PortNo;
        this.strFromDate = FromDate;
        this.strToDate = ToDate;
        this.iSendMail = SendMail;
        this.iAutoMail = AutoMail;
    }

    // getting From Mail Id
    public String getFromMailId() {
        return this.strFromMailId;
    }

    // getting From Mail Password
    public String getFromMailPassword() {
        return this.strFromMailPassword;
    }

    // getting Smtp Server
    public String getSmtpServer() {
        return this.strSmtpServer;
    }

    // getting Port No
    public String getportNo() {
        return this.strPortNo;
    }

    // getting From Date
    public String getFromDate() {
        return this.strFromDate;
    }

    // getting To Date
    public String getToDate() {
        return this.strToDate;
    }

    // getting Send Mail
    public int getSendMail() {
        return this.iSendMail;
    }

    // getting Auto Mail
    public int getAutoMail() {
        return this.iAutoMail;
    }

    // setting From Mail Id
    public void setFromMailId(String FromMailId) {
        this.strFromMailId = FromMailId;
    }

    // setting From Mail Password
    public void setFromMailPassword(String FromMailPass) {
        this.strFromMailPassword = FromMailPass;
    }

    // setting Smtp Server
    public void setSmtpServer(String SmtpServer) {
        this.strSmtpServer = SmtpServer;
    }

    // setting Port No
    public void setPortNo(String PortNo) {
        this.strPortNo = PortNo;
    }

    // setting From Date
    public void setFromDate(String FromDate) {
        this.strFromDate = FromDate;
    }

    // setting Smtp Server
    public void setToDate(String ToDate) {
        this.strToDate = ToDate;
    }

    // setting TableNo
    public void setSendMail(int SendMail) {
        this.iSendMail = SendMail;
    }

    // setting TBookId
    public void setAutoMail(int AutoMail) {
        this.iAutoMail = AutoMail;
    }
}
